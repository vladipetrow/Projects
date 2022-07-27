package game;

public class MovementSet
{
    private final static int sizeOfCharacters = 30;
    private int x;
    private int y;
    private int pixelsPerStep;

    protected MovementSet(int x, int y, int pixelsPerStep) {
	this.x = x;
	this.y = y;
	this.pixelsPerStep = pixelsPerStep;
    }

	public enum Move
    {
	DOWN(0, 1),
	UP(0, -1),
	RIGHT(1, 0),
	LEFT(-1, 0);
		private final int deltaX;
		private final int deltaY;
		Move(final int deltaX, final int deltaY) {
			this.deltaX = deltaX;
			this.deltaY = deltaY;
		}

    }

    public void moveCharacters(Move move) {
	y += move.deltaY * pixelsPerStep;
	x += move.deltaX * pixelsPerStep;
    }

    public void moveBack(Move currentDirection) {
			if (currentDirection == Move.DOWN) {
				moveCharacters(Move.UP);
			} else if (currentDirection == Move.UP) {
				moveCharacters(Move.DOWN);
			} else if (currentDirection == Move.LEFT) {
				moveCharacters(Move.RIGHT);
			} else if (currentDirection == Move.RIGHT) {
				moveCharacters(Move.LEFT);
			}
    }

    public int getSize() {
	return sizeOfCharacters;
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public int getBombColIndex() {
	return Map.pixelToSquare(x);
    }

    public int getBombRowIndex() {
	return Map.pixelToSquare(y);
    }
}
