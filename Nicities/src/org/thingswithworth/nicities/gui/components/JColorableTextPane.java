package org.thingswithworth.nicities.gui.components;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.thingswithworth.nicities.datastructure.IntervalTree.Interval;
import org.thingswithworth.nicities.datastructure.IntervalTree.IntervalTree;

public class JColorableTextPane extends JTextPane {
	private HashMap<String, Color> wordColorings;
	private HashMap<String, Color> blockColorings;
	private HashSet<RegionColoring> regionColorings;
	private AttributeSet inputAttributes;
	private StyleContext styleContext;
	private IntervalTree<MemorizedColorChange> existingWordColorings;

	public JColorableTextPane() {
		existingWordColorings = new IntervalTree<MemorizedColorChange>();
		wordColorings = new HashMap<String, Color>();
		blockColorings = new HashMap<String, Color>();
		regionColorings = new HashSet<RegionColoring>();
		getDocument().addDocumentListener(new ColoredListener());
		styleContext = StyleContext.getDefaultStyleContext();
		inputAttributes = this.getInputAttributes();
		styleContext.addAttribute(inputAttributes, StyleConstants.Foreground, Color.BLACK);
		setCharacterAttributes(inputAttributes, true);
	}

	public void addColoring(String word, Color color) {
		wordColorings.put(word, color);
	}

	public void addRegionColoring(String start, String end, Color color) {
		regionColorings.add(new RegionColoring(start, end, color));
	}

	public void append(Color color, String string) {
		int len = getDocument().getLength(); // same value as getText().length();
		try {
			getStyledDocument().insertString(len, string, inputAttributes);
			colorText(len, len + string.length(), color);
			existingWordColorings.addInterval(new Interval<MemorizedColorChange>(len, len + string.length(),
					new MemorizedColorChange(color, string)));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

	}

	public void colorText(final int start, final int length, Color color) {
		final AttributeSet colorSet = styleContext.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
		getStyledDocument().setCharacterAttributes(start, length, colorSet, false);
	}

	public void doFullColoring() {
		// TODO
	}

	private class MemorizedColorChange {
		public String colored;
		public Color color;
		public RegionColoring region;

		public MemorizedColorChange(RegionColoring region) {
			this.color = region.color;
			this.colored = region.startString + ".*" + region.endString;
			this.region = region;
		}

		public MemorizedColorChange(Color color, String colored) {
			this.color = color;
			this.colored = colored;
			this.region = null;
		}

		public boolean isRegion() {
			return region != null;
		}
	}

	private class RegionColoring {
		public String startString;
		public String endString;
		public Color color;

		public RegionColoring(String start, String end, Color color) {
			startString = start;
			endString = end;
			this.color = color;
		}

		public boolean equals(Object ob) {
			if (ob instanceof RegionColoring) {
				RegionColoring rc = (RegionColoring) ob;
				return rc.endString.equals(endString) && rc.startString.equals(startString);
			}
			return false;
		}
	}

	private class ColoredListener implements DocumentListener {
		@Override
		public void changedUpdate(DocumentEvent event) {
		}

		@Override
		public void insertUpdate(final DocumentEvent event) {
			handleWordColoring(event);
			handleRegionColoring(event);
			checkForInsertDisruption(event);
		}

		@Override
		public void removeUpdate(DocumentEvent event) {
			List<Interval<MemorizedColorChange>> colorings = existingWordColorings.getIntervals();
			for (Interval<MemorizedColorChange> change : colorings) {

			}
			List<Interval<MemorizedColorChange>> affected = existingWordColorings.getIntervals(event.getOffset(),
					event.getOffset() + event.getLength());

		}

		public void checkForInsertDisruption(DocumentEvent event) {
			List<Interval<MemorizedColorChange>> intervals = existingWordColorings.getIntervals();
			Interval<MemorizedColorChange> longestRegion = null;
			long longest = 0;
			for (Interval<MemorizedColorChange> interval : intervals) {
				MemorizedColorChange change = interval.getData();
				if (interval.getStart() > event.getOffset()) {
					interval.setStart(interval.getStart() + event.getLength());
					interval.setEnd(interval.getEnd() + event.getLength());
				} else if (interval.getEnd() >= event.getOffset()) {
					interval.setEnd(interval.getEnd() + event.getLength());
					if (interval.getStart() <= event.getOffset()) {
						if (change.isRegion()) {
							if (interval.getStart() + change.region.startString.length() <= event.getOffset()) {
								longestRegion = interval.getLength() > longest ? interval : longestRegion;
								longest = longestRegion.getLength();
							} else {
								// todo: invalidate region
							}
						} else {
							// todo:invalidate word
						}
					}
				}
				if (longestRegion != null) {
					final int offset = event.getOffset();
					final int length = event.getLength();
					final Color color = longestRegion.getData().color;
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							colorText(offset, length, color);
						}
					});
				}
			}
		}

		@SuppressWarnings("unused")
		private void handleWordColoring(DocumentEvent event) {
			final Document doc = event.getDocument();
			try {
				String str = doc.getText(event.getOffset(), event.getLength());
				if (containsWhitespace(str)) {
					for (final Entry<String, Color> coloring : wordColorings.entrySet()) {
						if (event.getOffset() - coloring.getKey().length() >= 0) {
							final String fullString = doc.getText(event.getOffset() - coloring.getKey().length(), coloring
									.getKey().length() + event.getLength());
							String[] words = fullString.split(" |\t|\n");
							for (final String word : words) {
								if (word.equals(coloring.getKey())) {
									int index = fullString.indexOf(word);
									while (index != -1) {
										final AttributeSet colorSet = styleContext.addAttribute(SimpleAttributeSet.EMPTY,
												StyleConstants.Foreground, coloring.getValue());
										final int offset = event.getOffset() - fullString.length() + 1 + index;// yep
										SwingUtilities.invokeLater(new Runnable() {
											@Override
											public void run() {
												colorText(offset, word.length(), coloring.getValue());
											}
										});

										existingWordColorings.addInterval(new Interval<MemorizedColorChange>(offset, offset
												+ word.length(), new MemorizedColorChange(coloring.getValue(), coloring
												.getKey())));

										index = fullString.indexOf(word, index + 1);
									}
								}
							}
						}
					}
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}

		private void handleRegionColoring(DocumentEvent event) {
			final Document doc = event.getDocument();
			try {
				for (final RegionColoring coloring : regionColorings) {
					// if short then check for start (otherwise assume paste and so look for closings)
					if (event.getLength() <= coloring.startString.length()) {
						if (event.getOffset() - coloring.startString.length() >= 0) {
							final String fullString = doc.getText(event.getOffset() - coloring.startString.length(),
									coloring.startString.length() + event.getLength());
							int startIndex = fullString.indexOf(coloring.startString);
							if (startIndex != -1) {
								int endIndex = doc.getText(event.getOffset(), doc.getLength() - event.getOffset()).indexOf(
										coloring.endString);
								if (endIndex != -1) {
									final int offset = event.getOffset() + event.getLength() - coloring.startString.length();
									final int length = endIndex + 1;
									SwingUtilities.invokeLater(new Runnable() {
										@Override
										public void run() {

											colorText(offset, length, coloring.color);
										}
									});
									existingWordColorings.addInterval(new Interval<MemorizedColorChange>(offset, offset
											+ length, new MemorizedColorChange(coloring)));
								}
							}
						}
					}
					// Check for closings
					if (event.getOffset() - coloring.endString.length() + 1 >= 0) {
						int endOffset = event.getOffset() - coloring.endString.length() + 1;
						final String fullString = doc
								.getText(endOffset, event.getLength() + coloring.endString.length() - 1);

						int endIndex = fullString.indexOf(coloring.endString);
						while (endIndex != -1) {
							int startIndex = endOffset + endIndex + 1;
							int regionLength = 0;
							while (startIndex >= 0) {
								if ((startIndex + coloring.startString.length()) < doc.getLength()
										&& doc.getText(startIndex, coloring.startString.length()).equals(
												coloring.startString)) {
									break;
								} else {
									startIndex--;
									regionLength++;
								}
							}
							if (startIndex != -1) {
								if (!doc.getText(startIndex, regionLength - 1).contains(coloring.endString)) {
									final int offset = startIndex;
									final int length = 1 + regionLength;
									SwingUtilities.invokeLater(new Runnable() {
										@Override
										public void run() {
											colorText(offset, length, coloring.color);
										}
									});
									endIndex = fullString
											.indexOf(coloring.endString, endIndex + coloring.endString.length());
									existingWordColorings.addInterval(new Interval<MemorizedColorChange>(startIndex,
											startIndex + 1 + regionLength, new MemorizedColorChange(coloring)));

								} else
									endIndex = -1;
							} else
								endIndex = -1;
						}

					}
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}

		private boolean containsWhitespace(String checkIn) {
			return checkIn.contains("\n") || checkIn.contains(" ") || checkIn.contains("\t");
		}

	}
}
