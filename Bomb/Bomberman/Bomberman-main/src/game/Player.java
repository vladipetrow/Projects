package game;

import javax.swing.*;
import java.awt.event.*;

public class Player extends MovementSet
{
    private final static int playerStartX = 60;
    private final static int playerStartY = 60;
    private final static int playerPixelsByStep = 4;
    private int explosionRadius;
    private int bombCount;
    private Map floor;

    public Action up = new AbstractAction() {
	public void actionPerformed(ActionEvent e) {  // Extention of ActionListener
			movePlayer(Move.UP);
	}
    };

    public Action right = new AbstractAction() {
	public void actionPerformed(ActionEvent e) {
			movePlayer(Move.RIGHT);
	}
    };

    public Action down = new AbstractAction() {
	public void actionPerformed(ActionEvent e) {
			movePlayer(Move.DOWN);
	}
    };

    public Action left = new AbstractAction() {
	public void actionPerformed(ActionEvent e) {
			movePlayer(Move.LEFT);
	}
    };

    public Action dropBomb = new AbstractAction()
    {
	public void actionPerformed(ActionEvent e) {
	    if(!floor.squareHasBomb(getBombRowIndex(), getBombColIndex()) && floor.getBombListSize() < getBombCount()){
		floor.addToBombList(new Bomb(getBombRowIndex(), getBombColIndex(), getExplosionRadius()));
	    }
	    floor.notifyListeners();
	}
    };

    public Player(BombermanPaint bombermanComponent, Map floor) {
	super(playerStartX, playerStartY, playerPixelsByStep);
	explosionRadius = 1;
	bombCount = 1;
	this.floor = floor;
	setPlayerButtons(bombermanComponent);
    }

	  public void setPlayerButtons(BombermanPaint bombermanComponent){
	bombermanComponent.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0,false), "moveRight");
	bombermanComponent.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0,true), "moveRight-released");

	bombermanComponent.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "moveLeft");
	bombermanComponent.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "moveLeft-released");
	bombermanComponent.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "moveUp");
	bombermanComponent.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "moveUp-released");

	bombermanComponent.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "moveDown");
	bombermanComponent.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "moveDown-released");
	bombermanComponent.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "dropBomb");

    bombermanComponent.getActionMap().put("moveRight", right);
	bombermanComponent.getActionMap().put("moveLeft", left);
	bombermanComponent.getActionMap().put("moveUp", up);
	bombermanComponent.getActionMap().put("moveDown", down);
	bombermanComponent.getActionMap().put("dropBomb", dropBomb);

    }
    public int getBombCount() {
	return bombCount;
    }

    public int getExplosionRadius() {
	return explosionRadius;
    }

    private void movePlayer(Move move) {
	moveCharacters(move);
	if(floor.collisionWithBlock(this)){
	    moveBack(move);
	}
	if(floor.collisionWithBombs(this)){
	    moveBack(move);
	}
	floor.checkIfPlayerLeftBomb();
	floor.notifyListeners();
    }

}
