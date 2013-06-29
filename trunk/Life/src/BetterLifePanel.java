import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BetterLifePanel extends LifePanel {
	private int rows, cols;
	private int rowSize, colSize;
	private GameOfLife myLife;
	private boolean active;
	private int mX, mY;

	public BetterLifePanel(GameOfLife life, Dimension myDim) {
		myLife = life;
		active = true;
		rows = life.getRows();
		cols = life.getCols();
		rowSize = (int) ((double) myDim.getHeight() / rows);
		colSize = (int) ((double) myDim.getWidth() / cols);
		this.setPreferredSize(new Dimension(cols * colSize, rows * rowSize));

		SelectListener listener = new SelectListener();
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(myLife.getDeadColor());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);
		for (int x = 0; x < cols; x++) {
			g.drawLine(x * colSize, 0, x * colSize, rows * rowSize);
			for (int y = 0; y < rows; y++) {
				g.setColor(Color.BLACK);
				if (x == 0)
					g.drawLine(0, y * rowSize, cols * colSize, y * rowSize);
			}
		}

		for (Cell living : myLife.getLiveCells()) {
			Point p = living.getPlace();
			g.setColor(living.getColor());
			g.fillRect(p.x * colSize + 1, p.y * rowSize + 1, colSize - 2, rowSize - 2);
		}

		if (active && mX >= 0 && mY >= 0) {
			g.setColor(Color.red);
			g.fillRect(mX * colSize + 1, mY * rowSize + 1, colSize - 2, rowSize - 2);
		}

	}

	public void disable() {
		active = false;
	}

	public void enable() {
		active = true;
	}

	public class SelectListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent event) {
			if (active) {
				int x = event.getX() / colSize;
				int y = event.getY() / rowSize;
				myLife.switchCellState(x, y);
				repaint();
			}
		}

		@Override
		public void mouseMoved(MouseEvent event) {
			mX = event.getX() / colSize;
			mY = event.getY() / rowSize;
			repaint();
		}
	}
}
