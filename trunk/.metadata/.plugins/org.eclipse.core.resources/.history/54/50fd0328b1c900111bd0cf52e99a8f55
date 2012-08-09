import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

/**
 * Model representation of game of life.
 * 
 * @author Andrey
 */
public class GameOfLife {
	private Cell[][] gameField;
	private int rows, cols;
	private int generations;
	private ArrayList<Point> alive;
	private ArrayList<Point> died;
	private boolean wrap;

	public enum LifeState {
		NEW, RUNNING, PAUSED, STABLE
	};

	LifeState currentState;

	public GameOfLife(int cols, int rows, boolean[][] initState, boolean wrap) {
		alive = new ArrayList<Point>();
		died = new ArrayList<Point>();
		this.rows = rows;
		this.cols = cols;
		this.wrap = wrap;
		gameField = new Cell[cols][rows];
		init(initState);
		currentState = LifeState.NEW;
		generations = 0;
	}

	public GameOfLife(int cols, int rows, boolean wrap) {
		this(cols, rows, new boolean[cols][rows], wrap);
	}

	public void pause() {
		currentState = LifeState.PAUSED;
	}

	public void run() {
		currentState = LifeState.RUNNING;
	}

	public int getGenerations() {
		return generations;
	}

	public void randomise(double ratioAlive) {
		if (currentState == LifeState.NEW || currentState == LifeState.PAUSED) {
			if (ratioAlive < 0.2)
				for (int x = 0; x < cols; x += 3) {
					for (int y = 0; y < rows; y += 3) {
						double zeroChance = Math.random() < 0.5 ? 1 : 0;
						double effectiveRate = Math.random() * 4 * ratioAlive * zeroChance;
						for (int checkY = boundH(y - 1); checkY != boundH(y + 2); checkY = boundH(checkY + 1)) {
							for (int checkX = boundW(x - 1); checkX != boundW(x + 2); checkX = boundW(checkX + 1)) {
								Cell pertinent = gameField[checkY][checkX];
								if (Math.random() < effectiveRate) {
									alive.add(pertinent.getPlace());
									pertinent.revive();
								}
							}
						}
					}
				}
			else
				for (int x = 0; x < cols; x++) {
					for (int y = 0; y < rows; y++) {
						Cell pertinent = gameField[x][y];
						if (Math.random() < ratioAlive) {
							alive.add(pertinent.getPlace());
							pertinent.revive();
						}
					}
				}
		}
	}

	public void update() {
		int changes = 0;
		Cell[][] newState = new Cell[cols][rows];
		ArrayList<Cell> messWith = new ArrayList<Cell>();
		for (Point check : died) {
			for (int checkY = boundH(check.y - 1); checkY != boundH(check.y + 2); checkY = boundH(checkY + 1)) {
				for (int checkX = boundW(check.x - 1); checkX != boundW(check.x + 2); checkX = boundW(checkX + 1)) {
					if (newState[checkX][checkY] == null) {
						newState[checkX][checkY] = updateCell(checkX, checkY);
						if (gameField[checkX][checkY].isAlive() != newState[checkX][checkY].isAlive())
							messWith.add(newState[checkX][checkY]);
					}
				}
			}
		}
		died = new ArrayList<Point>();
		for (Cell changed : messWith) {
			if (!changed.isAlive()) {
				died.add(changed.getPlace());
			}
		}
		for (Point check : alive) {
			if (gameField[check.x][check.y].getChanged()) {
				for (int checkY = boundH(check.y - 1); checkY != boundH(check.y + 2); checkY = boundH(checkY + 1)) {
					for (int checkX = boundW(check.x - 1); checkX != boundW(check.x + 2); checkX = boundW(checkX + 1)) {
						if (newState[checkX][checkY] == null) {
							newState[checkX][checkY] = updateCell(checkX, checkY);
							if (newState[checkX][checkY].getState() != gameField[checkX][checkY].getState()) {
								messWith.add(newState[checkX][checkY]);
								if (!newState[checkX][checkY].isAlive())
									died.add(new Point(checkX, checkY));
							}
						}
					}
				}
			} else
				gameField[check.x][check.y].age();
		}
		for (Cell changeCell : messWith) {
			if (changeCell.isAlive()) {
				alive.add(changeCell.getPlace());
			} else {
				alive.remove(changeCell.getPlace());
			}
		}

		for (int x = 0; x < gameField.length; x++) {
			for (int y = 0; y < gameField[x].length; y++) {
				if (newState[x][y] != null) {
					if (gameField[x][y].getState() != newState[x][y].getState() && !newState[x][y].isOscillating())
						changes++;
					gameField[x][y] = newState[x][y];

				}
			}
		}
		if (changes == 0) {
			currentState = LifeState.STABLE;

		} else {
			generations++;
		}

	}

	private Cell updateCell(int x, int y) {
		Cell pertinent = gameField[x][y].clone();
		if (pertinent.isAlive()) {
			pertinent.age();
		}

		int aliveCount = 0;
		if (wrap) {
			for (int checkY = boundH(y - 1); checkY != boundH(y + 2); checkY = boundH(checkY + 1)) {
				for (int checkX = boundW(x - 1); checkX != boundW(x + 2); checkX = boundW(checkX + 1)) {
					if (!(checkX == x && checkY == y) && gameField[checkX][checkY].isAlive()) {
						aliveCount++;
					}
				}
			}
		} else {
			for (int checkY = y - 1; checkY < y + 2; checkY++) {
				for (int checkX = x - 1; checkX < x + 2; checkX++) {
					if (!(checkX == x && checkY == y) && checkX == boundW(checkX) && checkY == boundH(checkY)
							&& gameField[checkX][checkY].isAlive()) {
						aliveCount++;
					}
				}
			}
		}
		if (aliveCount <= 1 || aliveCount >= 4) {
			pertinent.kill();// killing dead cells does nothing - no need to check
		}

		if (aliveCount == 3) {
			pertinent.revive();// Reviving an alive cell does nothing - no need to check
		}

		return pertinent;
	}

	public void reset() {
		for (int x = 0; x < cols; x++)
			for (int y = 0; y < rows; y++)
				gameField[x][y].kill();
		alive = new ArrayList<Point>();
		generations = 0;
		currentState = LifeState.NEW;
	}

	public void switchCellState(int x, int y) {
		if (currentState == LifeState.NEW || currentState == LifeState.PAUSED) {
			Cell pertinent = gameField[x][y];
			if (pertinent.getState() == Cell.ALIVE) {
				alive.remove(pertinent.getPlace());
				pertinent.kill();
			} else {
				alive.add(pertinent.getPlace());
				pertinent.revive();
			}
		}
	}

	public boolean getCellState(int x, int y) {
		return gameField[x][y].getState();
	}

	public int getCols() {
		return cols;
	}

	public int getRows() {
		return rows;
	}

	public void init(boolean[][] toState) {
		if (toState.length != gameField.length || toState[0].length != gameField[0].length)
			return;
		for (int x = 0; x < toState.length; x++) {
			for (int y = 0; y < toState[x].length; y++) {
				Point place = new Point(x, y);
				gameField[x][y] = new Cell(place);
				if (toState[x][y]) {
					alive.add(place);
					gameField[x][y].revive();
				}
			}
		}
	}

	public Color[][] getPrettyColors() {
		Color[][] prettyColors = new Color[cols][rows];

		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				prettyColors[x][y] = gameField[x][y].getColor();
			}
		}
		return prettyColors;
	}

	public Color getDeadColor() {
		return Color.DARK_GRAY;
	}

	public Cell[] getLiveCells() {
		Cell[] live = new Cell[alive.size()];
		for (int i = 0; i < alive.size(); i++)
			live[i] = gameField[alive.get(i).x][alive.get(i).y];
		return live;
	}

	public boolean isStable() {
		return currentState == LifeState.STABLE;
	}

	public void toggleWrap() {
		wrap = !wrap;
	}

	private int boundW(int getBounded) {
		return (cols + getBounded % cols) % cols;
	}

	private int boundH(int getBounded) {
		return (rows + getBounded % rows) % rows;
	}
}
