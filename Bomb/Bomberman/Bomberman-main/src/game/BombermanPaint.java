package game;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class BombermanPaint extends JComponent implements FloorListener
{
    private final static int squareSize = 40;
    private final static int characterPlacement = 15;
    private final static int squareMiddle = squareSize /2;
    private final static int bombPlacement = -6;
    private final Map floor;

    public BombermanPaint(Map floor) {
			this.floor = floor;
	}

    public static int getSquareSize() {
	return squareSize;
    }
    public static int getSquareMiddle() {
	return squareMiddle;
    }

    public Dimension getPreferredSize() {
	super.getPreferredSize();
	return new Dimension(this.floor.getWidth() * squareSize, this.floor.getHeight() * squareSize);
    }

    public void floorChanged() {
	repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
			super.paintComponent(g);
			final Graphics2D g2d = (Graphics2D) g;
		// Paint map:
			for (int rowIndex = 0; rowIndex < floor.getHeight(); rowIndex++) {
				for (int colIndex = 0; colIndex < floor.getWidth(); colIndex++) {
					if (floor.getMapTile(rowIndex, colIndex) == MapTile.BREAKABLEBLOCK) {
						paintBreakableBlock(rowIndex, colIndex, g2d);
					} else if (floor.getMapTile(rowIndex, colIndex) == MapTile.UNBREAKABLEBLOCK) {
						paintUnbreakableBlock(rowIndex, colIndex, g2d);
					} else {
						paintFloor(rowIndex, colIndex, g2d);
					}
				}
			}
			// Paint player:
			paintPlayer(floor.getPlayer(), g2d);

			//Paint enemies
			for (Enemy e : floor.getEnemyList()) {
				paintEnemy(e, g2d);
			}
			//Paint bombs
			for (Bomb b : floor.getBombList()) {
				int bombX = floor.squareToPixel(b.getColIndex());
				int bombY = floor.squareToPixel(b.getRowIndex());

				Toolkit t = Toolkit.getDefaultToolkit();
				Image i = t.getImage("Bomberman/Bomberman-main/src/resourses/bomb.png");
				g2d.drawOval(bombX - bombPlacement, bombY - bombPlacement, Bomb.getBOMBSIZE(), Bomb.getBOMBSIZE());
				g2d.fillOval(bombX - bombPlacement, bombY - bombPlacement, Bomb.getBOMBSIZE(), Bomb.getBOMBSIZE());
				g2d.setClip(new Ellipse2D.Double(bombX - bombPlacement, bombY - bombPlacement,
						Bomb.getBOMBSIZE(), Bomb.getBOMBSIZE()));
				g2d.drawImage(i, bombX - bombPlacement,
						bombY - bombPlacement, this);
			}
    }
    private void paintBreakableBlock(int rowIndex, int colIndex, Graphics g2d){
			Toolkit t = Toolkit.getDefaultToolkit();
			Image i = t.getImage("Bomberman/Bomberman-main/src/resourses/brakable.png");
			g2d.drawImage(i, colIndex * squareSize, rowIndex * squareSize, this);
    }

    private void paintUnbreakableBlock(int rowIndex, int colIndex, Graphics g2d){
			Toolkit t = Toolkit.getDefaultToolkit();
			Image i = t.getImage("Bomberman/Bomberman-main/src/resourses/unbrakable.png");
			g2d.drawImage(i, colIndex * squareSize, rowIndex * squareSize, this);
    }

    private void paintFloor(int rowIndex, int colIndex, Graphics g2d){
			Toolkit t = Toolkit.getDefaultToolkit();
			Image i = t.getImage("Bomberman/Bomberman-main/src/resourses/floor.png");
			g2d.drawImage(i, colIndex * squareSize, rowIndex * squareSize, this);
	}

    private void paintEnemy(Enemy e, Graphics g2d){
			Toolkit t = Toolkit.getDefaultToolkit();
			Image i = t.getImage("Bomberman/Bomberman-main/src/resourses/enemy.png");
			g2d.drawOval(e.getX() - characterPlacement, e.getY() - characterPlacement, e.getSize(), e.getSize());
			g2d.fillOval(e.getX() - characterPlacement, e.getY() - characterPlacement, e.getSize(), e.getSize());
			g2d.setClip(new Ellipse2D.Double(e.getX() - characterPlacement, e.getY() - characterPlacement,
					e.getSize(), e.getSize()));
			g2d.drawImage(i, e.getX() - characterPlacement,
					e.getY() - characterPlacement, this);
    }

    private void paintPlayer(Player player, Graphics g2d){
			Toolkit t = Toolkit.getDefaultToolkit();
			Image i = t.getImage("Bomberman/Bomberman-main/src/resourses/player.png");
			g2d.drawOval(player.getX() - characterPlacement, player.getY() - characterPlacement, player.getSize(), player.getSize());
			g2d.fillOval(player.getX() - characterPlacement, player.getY() - characterPlacement, player.getSize(), player.getSize());

			g2d.setClip(new Ellipse2D.Double(player.getX() - characterPlacement, player.getY() - characterPlacement,
					player.getSize(), player.getSize()));
			g2d.drawImage(i, player.getX() - characterPlacement,
					player.getY() - characterPlacement, this);

    }
}
