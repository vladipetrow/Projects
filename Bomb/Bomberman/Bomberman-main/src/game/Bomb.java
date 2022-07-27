package game;

public class Bomb
{
    private final static int bombSize = 30;
    private int timeToExplosion = 100;
    private final int rowIndex;
    private final int colIndex;
    private final int explosionRadius;
    private boolean playerLeft;

    public Bomb(final int rowIndex, final int colIndex, int explosionRadius) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.explosionRadius = explosionRadius;
        playerLeft = false;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    public static int getBOMBSIZE() {
        return bombSize;
    }

    public int getTimeToExplosion() {
        return timeToExplosion;
    }

    public void setTimeToExplosion(final int timeToExplosion) {
        this.timeToExplosion = timeToExplosion;
    }

    public int getExplosionRadius() {
        return explosionRadius;
    }

    public boolean isPlayerLeft() {
        return playerLeft;
    }

    public void setPlayerLeft(final boolean playerLeft) {
        this.playerLeft = playerLeft;
    }
}