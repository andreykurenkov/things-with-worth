import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class LameLifePanel extends LifePanel {
	private JButton[][] visibleCells;
	private GameOfLife myLife;
	private int rows, cols;

	public LameLifePanel(GameOfLife life, Dimension myDim) {
		this.setPreferredSize(myDim);
		myLife = life;
		rows = myLife.getRows();
		cols = myLife.getCols();
		this.setLayout(new GridLayout(rows, cols));
		int minBorder = Math.min((int) myDim.getWidth(), (int) myDim.getHeight()) / Math.max(rows, cols);
		visibleCells = new JButton[cols][rows];

		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				visibleCells[x][y] = new JButton();
				visibleCells[x][y].addActionListener(new LifeListener(x, y));
				visibleCells[x][y].setPreferredSize(new Dimension(minBorder, minBorder));
				add(visibleCells[x][y]);
			}
		}
	}

	public void paint(Graphics g) {
		Color[][] newColors = myLife.getPrettyColors();
		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				visibleCells[x][y].setBackground(newColors[x][y]);
			}
		}
		super.paint(g);
	}

	public void disable() {
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				visibleCells[x][y].setEnabled(false);
				visibleCells[x][y].setFocusable(false);
			}
		}
	}

	public void enable() {
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				visibleCells[x][y].setEnabled(true);
				visibleCells[x][y].setFocusable(true);
			}
		}
	}

	public class LifeListener implements ActionListener {
		private int col;
		private int row;

		public LifeListener(int x, int y) {
			col = x;
			row = y;
		}

		public void actionPerformed(ActionEvent e) {
			myLife.switchCellState(col, row);
			repaint();
		}
	}
}