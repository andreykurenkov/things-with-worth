package org.thingswithworth.nicities.gui.helpers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Collection;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;

import org.thingswithworth.nicities.gui.components.DirectoryBrowserPanel;
import org.thingswithworth.nicities.gui.components.JFileList;

public class Dialogs {
	public static int getIntSafely(Component in, String title, String checkMessage, int min, int max) {
		boolean validInput = false;
		int input = -1;
		while (!validInput) {
			try {
				input = Integer.parseInt(JOptionPane.showInputDialog(in, checkMessage, title, JOptionPane.QUESTION_MESSAGE));
				validInput = input <= max && input >= min;
				if (!validInput)
					JOptionPane.showMessageDialog(null, "Not valid. Min value: " + min + " and max " + max + ".");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, ":(. Thats not a valid int input. Try again.");
				validInput = false;
			}
		}
		return input;
	}

	public static String getRadioSelection(Window in, String title, String inquisition, String[] options, boolean allowCustom) {
		final JDialog dialog = new JDialog(in, title, Dialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.add(new JLabel(inquisition), BorderLayout.NORTH);
		JPanel optionsPanel = new JPanel(new GridLayout(allowCustom ? options.length + 1 : options.length, 1));
		final ButtonGroup radioGroup = new ButtonGroup();
		final JRadioButton custom = new JRadioButton("Custom:");
		final JTextField customText = new JTextField();
		final JRadioButton[] buttons = new JRadioButton[options.length + (allowCustom ? 1 : 0)];
		for (int i = 0; i < options.length; i++) {
			JRadioButton select = new JRadioButton(options[i]);
			buttons[i] = select;
			if (i == 0)
				select.setSelected(true);
			select.setActionCommand("" + i);
			radioGroup.add(select);
			optionsPanel.add(select);
		}
		if (allowCustom) {
			JPanel customPanel = new JPanel(new BorderLayout());
			custom.setActionCommand("custom");
			buttons[options.length] = custom;
			radioGroup.add(custom);
			customPanel.add(custom, BorderLayout.WEST);
			customPanel.add(customText, BorderLayout.CENTER);
			optionsPanel.add(customPanel);
		}
		dialog.add(optionsPanel, BorderLayout.CENTER);
		final JButton okay = new JButton("Accept");
		final JButton cancel = new JButton("Cancel");
		JPanel resolvePanel = new JPanel(new GridLayout(1, 2));
		resolvePanel.add(okay);
		resolvePanel.add(cancel);
		customText.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent event) {
				radioGroup.setSelected(custom.getModel(), true);
			}
		});
		okay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!radioGroup.getSelection().getActionCommand().equals("custom"))
					dialog.setVisible(false);
				else if (customText.getText().length() > 0)
					dialog.setVisible(false);
			}

		});
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractButton none = new JRadioButton();
				none.setActionCommand("null - none selected in ButtonGroup since canceled.");
				radioGroup.add(none);
				radioGroup.setSelected(none.getModel(), true);
				dialog.setVisible(false);
			}

		});
		KeyEventDispatcher buttonHandler = new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {
				boolean keyHandled = false;
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						okay.doClick();
						keyHandled = true;
					} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						cancel.doClick();
						keyHandled = true;
					} else if (e.getKeyCode() == KeyEvent.VK_UP) {
						if (custom.isSelected())
							radioGroup.setSelected(buttons[buttons.length - 2].getModel(), true);
						else {
							int parse = (Integer.parseInt(radioGroup.getSelection().getActionCommand()) - 1);
							if (parse == -1)
								parse = buttons.length - 1;
							radioGroup.setSelected(buttons[parse].getModel(), true);
						}
						keyHandled = true;
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						if (!custom.isSelected())
							radioGroup.setSelected(
									buttons[(Integer.parseInt(radioGroup.getSelection().getActionCommand()) + 1)
											% buttons.length].getModel(), true);
						else
							radioGroup.setSelected(buttons[0].getModel(), true);
						keyHandled = true;
					} else if (Character.isLetterOrDigit(e.getKeyChar()) || Character.isSpaceChar(e.getKeyChar())) {
						custom.setSelected(true);
						if (!customText.hasFocus())
							customText.setText(customText.getText() + e.getKeyChar());
						keyHandled = true;
					} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
						custom.setSelected(true);
						if (customText.getText().length() > 0)
							customText.setText(customText.getText().substring(0, customText.getText().length() - 1));
						keyHandled = true;
					}
				}
				return keyHandled;
			}
		};
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(buttonHandler);
		dialog.add(resolvePanel, BorderLayout.SOUTH);
		dialog.pack();
		centerDialog(dialog, in);
		dialog.setVisible(true);
		while (dialog.isVisible()) {
		}
		String toReturn = null;
		if (radioGroup.getSelection().getActionCommand().equals("null - none selected in ButtonGroup since canceled."))
			toReturn = null;
		else if (allowCustom && radioGroup.getSelection().getActionCommand().equals("custom"))
			toReturn = customText.getText();
		else
			toReturn = options[Integer.parseInt(radioGroup.getSelection().getActionCommand())];
		dialog.dispose();
		KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(buttonHandler);
		return toReturn;
	}

	public static File getFile(Window in, String title, String inquisition, File startDir, int selection) {
		final JDialog dialog = new JDialog(in, title, Dialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.add(new JLabel(inquisition), BorderLayout.NORTH);
		final JFileChooser chooser = new JFileChooser();
		dialog.add(chooser, BorderLayout.CENTER);
		if (startDir != null)
			chooser.setCurrentDirectory(startDir);
		chooser.setFileSelectionMode(selection);
		chooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent action) {
				if (action.getActionCommand().equals("CancelSelection")) {
					chooser.setSelectedFile(null);
					dialog.setVisible(false);
				}
				if (action.getActionCommand().equals("ApproveSelection")) {
					if (chooser.getSelectedFile() != null) {
						dialog.setVisible(false);
					} else
						JOptionPane.showMessageDialog(dialog, "Select something!", "No selection :(",
								JOptionPane.ERROR_MESSAGE);

				}
			}
		});
		dialog.pack();
		centerDialog(dialog, in);
		dialog.setVisible(true);
		while (dialog.isVisible()) {
		}
		File toReturn = chooser.getSelectedFile();
		dialog.dispose();
		return toReturn;
	}

	public static File[] getFolders(Window in, String title, String inquisition, File startDir, File[] initialFiles) {
		if (startDir == null)
			startDir = FileSystemView.getFileSystemView().getHomeDirectory();
		final JDialog dialog = new JDialog(in, title, Dialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		JLabel instr = new JLabel(inquisition);
		dialog.add(instr, BorderLayout.NORTH);
		JPanel majorComponents = new JPanel(new GridLayout(1, 2));
		majorComponents.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		final DirectoryBrowserPanel browser = new DirectoryBrowserPanel(startDir, "Directory Browser");
		browser.getList().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		majorComponents.add(browser);
		browser.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		final JFileList selectedList;
		if (initialFiles == null) {
			selectedList = new JFileList();
		} else {
			selectedList = new JFileList(initialFiles);
		}
		JPanel selectedPanel = new JPanel(new BorderLayout());
		selectedPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		JLabel titleLabel = new JLabel("Added Files");
		titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, 14));
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		selectedPanel.add(titleLabel, BorderLayout.NORTH);
		selectedPanel.add(new JScrollPane(selectedList), BorderLayout.CENTER);
		selectedList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		majorComponents.add(selectedPanel);
		dialog.add(majorComponents, BorderLayout.CENTER);
		JButton done = new JButton("Done");
		JButton add = new JButton("Add Selected");
		JButton remove = new JButton("Remove Selected");
		JPanel buttonsPanel = new JPanel(new GridLayout(1, 3));
		buttonsPanel.add(add);
		buttonsPanel.add(done);
		buttonsPanel.add(remove);
		dialog.add(buttonsPanel, BorderLayout.SOUTH);
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Object o : browser.getList().getSelectedValues()) {
					selectedList.addFile((File) o);
				}
			}

		});
		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (Object o : selectedList.getSelectedValues())
					selectedList.removeFile((File) o);
			}

		});
		done.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dialog.setVisible(false);
			}
		});
		dialog.pack();
		centerDialog(dialog, in);
		dialog.setVisible(true);
		while (dialog.isVisible()) {
		}
		dialog.dispose();
		return selectedList.getFiles();
	}

	public static Object[] displayListDialog(Window in, String title, String inquisition, Collection<? extends Object> list,
			final int numSelectable) {
		return displayListDialog(in, title, inquisition, list.toArray(), numSelectable);
	}

	public static Object[] displayListDialog(Window in, String title, String inquisition, Object[] data,
			final int numSelectable) {
		final JDialog dialog = new JDialog(in, title, Dialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		JLabel instr = new JLabel(inquisition);
		dialog.add(instr, BorderLayout.NORTH);
		final JList list = new JList(data);
		dialog.add(list, BorderLayout.CENTER);
		if (numSelectable == 0)
			list.setBackground(instr.getBackground());
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (numSelectable > 0) {
					int[] oldSelection = list.getSelectedIndices();
					int[] newSelection = new int[Math.min(numSelectable, oldSelection.length)];
					for (int i = 0; i < newSelection.length; i++) {
						newSelection[i] = oldSelection[oldSelection.length - 1 - i];
					}
				} else
					list.clearSelection();
			}

		});
		JButton done = new JButton("Accept");
		dialog.add(done, BorderLayout.SOUTH);
		done.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dialog.setVisible(false);
			}
		});
		dialog.pack();
		centerDialog(dialog, in);
		dialog.setVisible(true);
		while (dialog.isVisible()) {
		}
		dialog.dispose();
		return list.getSelectedValues();
	}

	public static void centerDialog(JDialog dialog, Window in) {
		if (in != null && dialog != null) {
			int x = in.getX() + (in.getWidth() - dialog.getSize().width) / 2;
			int y = in.getY() + (in.getHeight() - dialog.getSize().height) / 2;
			dialog.setLocation(x, y);
		}
	}

	public static void askIfExitApplication(Component in) {
		int quit = JOptionPane.showConfirmDialog(in, "Do you want to quit the application?", "Quit?",
				JOptionPane.YES_NO_OPTION);
		if (quit == JOptionPane.YES_OPTION)
			System.exit(0);
	}

	public static void makeSafeToCloseWithCustomAction(final JDialog make, WindowAdapter action) {
		make.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		make.addWindowListener(action);
	}

	public static void makeSafeToClose(final JDialog make) {
		makeSafeToCloseWithCustomAction(make, new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				Dialogs.askIfExitApplication(make);
			}
		});
	}

	public static void makeSafeToCloseWithCustomAction(final JFrame make, WindowAdapter action) {
		make.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		make.addWindowListener(action);
	}

	public static void makeSafeToClose(final JFrame make) {
		makeSafeToCloseWithCustomAction(make, new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				Dialogs.askIfExitApplication(make);
			}
		});
	}
}
