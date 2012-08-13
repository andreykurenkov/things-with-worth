package org.ThingsWithWorth.GenreGetter.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.ThingsWithWorth.GenreGetter.io.fileio.MusicFile;
import org.ThingsWithWorth.GenreGetter.io.fileio.MusicFile.MusicFileFactory;
import org.ThingsWithWorth.GenreGetter.wikiio.MusicWikiInterface;
import org.ThingsWithWorth.Nicities.StringHelper;
import org.ThingsWithWorth.Nicities.FileIO.DirScanner;
import org.ThingsWithWorth.Nicities.GUI.Components.FileAdderPanel;
import org.ThingsWithWorth.Nicities.GUI.Helpers.Dialogs;
import org.ThingsWithWorth.Nicities.GUI.Helpers.GBHelp;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class GenreGetterFrame extends JFrame {
	private JCheckBox save, recursive;
	private JPanel folderPanel, genrePanel, lookupPanel, progressPanel, checkboxPanel, radioPanel;
	private JButton lookup, stopLookup;
	private JRadioButton folderName, fileTag, manualSelect, autoSelect;
	private JProgressBar bar;
	private FileAdderPanel adder;
	private JTree artistsTree;
	private FinishableTreeNode artists;
	private DefaultTreeModel artistsModel;
	private HashMap<String, DefaultMutableTreeNode> artistNodes;
	private HashMap<String, ArrayList<String>> foundGenres;
	private Runnable genreClassify;
	private boolean runGenreClassify;
	private boolean done;

	public GenreGetterFrame() {
		this("GenreGetter", true);
	}

	public GenreGetterFrame(String title, boolean progressBar) {
		super(title);
		done = false;
		artistNodes = new HashMap<String, DefaultMutableTreeNode>();
		foundGenres = new HashMap<String, ArrayList<String>>();
		setupFolderView();
		setupGenreView();
		setFolderView();
		SwingWorker<Void, Void> guiSpeeder = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				while (!done) {
					if (runGenreClassify) {
						genreClassify.run();
						runGenreClassify = false;
					}
				}
				return null;
			}

		};
		guiSpeeder.execute();
	}

	private void setupFolderView() {
		folderPanel = new JPanel(new BorderLayout());
		this.setResizable(true);
		save = new JCheckBox("Save folders");
		recursive = new JCheckBox("Add contained folders");
		checkboxPanel = new JPanel();
		checkboxPanel.add(save);
		checkboxPanel.add(recursive);

		adder = new FileAdderPanel("Add folders containing music albums here", null, null, false);
		folderPanel.add(adder, BorderLayout.CENTER);
		folderPanel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.SOUTH);

		lookup = new JButton("Start Lookup");
		adder.addAddListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lookup.setEnabled(true);
			}

		});
		lookup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean foundFolders = doLookUp(folderName.isSelected());
				if (foundFolders) {
					setGenreView();
				} else {
					lookup.setEnabled(true);
					JOptionPane.showMessageDialog(GenreGetterFrame.this, "No folders containing valid files found.",
							"Folders Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		lookupPanel = new JPanel();
		folderName = new JRadioButton("Use folder name for albums");
		fileTag = new JRadioButton("Use song tags name for albums");
		lookupPanel.add(folderName);
		lookupPanel.add(fileTag);
		lookupPanel.add(lookup);
		folderName.setSelected(true);
		ButtonGroup albumNameGroup = new ButtonGroup();
		albumNameGroup.add(folderName);
		albumNameGroup.add(fileTag);
	}

	private void setupGenreView() {
		manualSelect = new JRadioButton("Manually select album genre");
		autoSelect = new JRadioButton("Auto select album genre");
		ButtonGroup albumSelectGroup = new ButtonGroup();
		albumSelectGroup.add(manualSelect);
		albumSelectGroup.add(autoSelect);
		manualSelect.setSelected(true);
		radioPanel = new JPanel();
		radioPanel.add(manualSelect);
		radioPanel.add(autoSelect);

		artists = new FinishableTreeNode("Musical Artists");
		artistsModel = new DefaultTreeModel(artists);
		artistsTree = new JTree(artistsModel);
		artistsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		artistsTree.setShowsRootHandles(true);
		artistsTree.setCellRenderer(new MusicTreeRenderer());
		artistsTree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent event) {
				TreePath path = event.getNewLeadSelectionPath();
				final FinishableTreeNode lastNode;
				if (path != null)
					lastNode = (FinishableTreeNode) path.getLastPathComponent();
				else
					lastNode = null;
				if (lastNode != null) {
					if (!lastNode.isLeaf()) {
						((JTree) event.getSource()).setSelectionPath(event.getOldLeadSelectionPath());
					} else if (!lastNode.done) {
						genreClassify = new Runnable() {
							@Override
							public void run() {
								String[] genres = foundGenres.get(lastNode.toString()).toArray(new String[1]);
								String choice = null;
								if (genres.length > 0 && genres[0] != null) {
									System.out.println("yep");
									choice = Dialogs.getRadioSelection(null, "Genre Select",
											"Select from the found genres, or choose your own", genres, true);
								} else {
									choice = JOptionPane.showInputDialog(GenreGetterFrame.this,
											"No genres were found for this album - you can opt to define your own below.");
								}
								if (choice != null) {
									lastNode.done = true;
									((FinishableTreeNode) lastNode.getParent()).countDone++;
								}

							}
						};
						runGenreClassify = true;

					}
				}

			}
		});
		genrePanel = new JPanel(new BorderLayout());
		genrePanel.add(new JScrollPane(artistsTree), BorderLayout.CENTER);

		progressPanel = new JPanel(new GridBagLayout());
		bar = new JProgressBar();
		bar.setStringPainted(true);
		stopLookup = new JButton("Stop Lookup");
		stopLookup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setFolderView();
				// TODO
			}
		});
		progressPanel.add(bar, GBHelp.constrain(0, 0, GBHelp.BOTH, 0.8, 1, GBHelp.REL, GBHelp.REM, GBHelp.WEST));
		progressPanel.add(stopLookup, GBHelp.constrain(1, 0, GBHelp.BOTH, 0.2, 1, GBHelp.REM, GBHelp.REM, GBHelp.EAST));

	}

	private void setFolderView() {
		this.remove(radioPanel);
		this.remove(genrePanel);
		this.remove(progressPanel);
		this.add(checkboxPanel, BorderLayout.NORTH);
		this.add(folderPanel, BorderLayout.CENTER);
		this.add(lookupPanel, BorderLayout.SOUTH);
		this.pack();
		this.repaint();
	}

	private void setGenreView() {
		this.remove(checkboxPanel);
		this.remove(folderPanel);
		this.remove(lookupPanel);
		this.add(radioPanel, BorderLayout.NORTH);
		this.add(genrePanel, BorderLayout.CENTER);
		this.add(progressPanel, BorderLayout.SOUTH);
		this.pack();
		this.repaint();
	}

	public static void main(String[] args) {
		GenreGetterFrame frame = new GenreGetterFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	private class MusicTreeRenderer implements TreeCellRenderer {
		private TreeCellRenderer defaultCellRenderer = new DefaultTreeCellRenderer();
		private final ImageIcon TOP_ICON = new ImageIcon("Res/music.png");
		private final ImageIcon ALBUM_ICON = new ImageIcon("Res/album.png");
		private final ImageIcon ARTIST_ICON = new ImageIcon("Res/artist.png");

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {
			JLabel label = (JLabel) defaultCellRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf,
					row, hasFocus);
			FinishableTreeNode node = (FinishableTreeNode) value;
			if (node.getLevel() == 0)
				label.setIcon(TOP_ICON);
			else if (node.getLevel() == 1) {
				label.setIcon(ARTIST_ICON);
				if (node.countDone == node.getChildCount())
					label.setForeground(Color.GREEN);
				else
					label.setForeground(Color.RED);
			} else if (node.getLevel() == 2) {
				label.setIcon(ALBUM_ICON);
				if (node.done) {
					label.setForeground(Color.GREEN);
				} else {
					label.setForeground(Color.RED);
				}
			}
			return label;

		}
	}

	class FinishableTreeNode extends DefaultMutableTreeNode {
		public boolean done = false;
		public int countDone = 0;

		public FinishableTreeNode(String string) {
			super(string);
		}
	}

	public boolean doLookUp(final boolean byFolder) {
		final ArrayList<File> foundAlbumFolders = new ArrayList<File>();
		FileFilter albumFolderFilter = new MusicFile.AlbumFolderFilter();
		for (File folder : adder.getFileList().getFiles())
			foundAlbumFolders.addAll(Arrays.asList(DirScanner.recursiveFileFind(folder, albumFolderFilter, true)));
		if (foundAlbumFolders.size() == 0)
			return false;
		SwingWorker<HashMap<String, ArrayList<String>>, Void> worker = new SwingWorker<HashMap<String, ArrayList<String>>, Void>() {
			@Override
			public HashMap<String, ArrayList<String>> doInBackground() {
				int at = 0;
				bar.setModel(new DefaultBoundedRangeModel(0, 1, 0, foundAlbumFolders.size()));
				for (File albumFile : foundAlbumFolders) {

					String artist = "";
					String album = "";
					if (byFolder) {
						album = albumFile.getName();
						artist = albumFile.getParentFile().getName();
					} else {
						MusicFile musicFile = null;
						for (File possible : albumFile.listFiles()) {
							musicFile = (MusicFile) MusicFileFactory.instance.tryToMakeSmart(possible);
							if (musicFile != null)
								break;
						}
						if (musicFile == null)
							continue;
						else {
							Tag musicTag = musicFile.getTagData();
							if (musicTag == null)
								continue;
							album = musicTag.getFirst(FieldKey.ALBUM);
							artist = musicTag.getFirst(FieldKey.ARTIST);
						}
					}
					String artistForMap = StringHelper.getInTitleCase(artist);

					ArrayList<String> genres = new ArrayList<String>();
					if (!foundGenres.containsKey(album)) {
						foundGenres.put(album, genres);
						if (!artistNodes.containsKey(artistForMap)) {
							DefaultMutableTreeNode artistNode = new FinishableTreeNode(artistForMap);
							artistsModel.insertNodeInto(artistNode, artists, artists.getChildCount());
							artistNodes.put(artistForMap, artistNode);
						}
						DefaultMutableTreeNode artistNode = artistNodes.get(artistForMap);
						artistsModel.insertNodeInto(new FinishableTreeNode(album), artistNode, artistNode.getChildCount());
					} else {
						genres = foundGenres.get(album);
					}
					if (album.length() < 25) {
						bar.setString((int) (bar.getPercentComplete() * 100) + "% ; Searching for " + album);
					} else
						bar.setString((int) (bar.getPercentComplete() * 100) + "% ; Searching for " + album.substring(0, 20)
								+ "...");
					String[] albumGenres = MusicWikiInterface.findAlbumGenres(album, artist);
					if (albumGenres != null && albumGenres.length > 0) {
						genres.addAll(Arrays.asList(albumGenres));
					} else {
						String[] artistGenres = MusicWikiInterface.findArtistGenres(artist);
						if (artistGenres != null && artistGenres.length > 0) {
							genres.addAll(Arrays.asList(artistGenres));
						}
					}
					at++;
					bar.getModel().setValue(at);
				}
				return foundGenres;

			}

			@Override
			public void done() {

				JOptionPane.showMessageDialog(GenreGetterFrame.this, "All genres have been retrieved",
						"Genre retrieval done", JOptionPane.PLAIN_MESSAGE);
			}
		};
		worker.execute();
		return true;
	}

}
