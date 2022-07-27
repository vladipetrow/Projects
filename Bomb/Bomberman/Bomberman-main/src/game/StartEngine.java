package game;

import javax.swing.*;
import java.awt.event.ActionEvent;

public final class StartEngine
{
    private static final int delay = 30;
    private static int width = 10;
    private static int height = 10;
    private static int numOfEnemies = 4;
    private static Timer clockTimer = null;

    private StartEngine() {}

    public static void main(String[] args) {
		new Menu();
    }

    public static void startGame() {
	Map floor = new Map(width, height, numOfEnemies);

	BombermanFrame frame = new BombermanFrame("Bomberman", floor);

	frame.setLocationRelativeTo(null);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	floor.addFloorListener(frame.getBombermanComponent());

	Action doOneStep = new AbstractAction()
	{
	    public void actionPerformed(ActionEvent e) {
		tick(frame, floor);
	    }
	};
	clockTimer = new Timer(delay, doOneStep);
	clockTimer.setCoalesce(true);
	clockTimer.start();
    }

    private static void gameOver(BombermanFrame frame, Map floor) {
	clockTimer.stop();
	frame.dispose();
	new GameOver();
    }

	private static void gameWin(BombermanFrame frame, Map floor) {
		clockTimer.stop();
		frame.dispose();
		new GameWin();
	}


	private static void tick(BombermanFrame frame, Map floor) {
		if(floor.getIsGameWin()){
			gameWin(frame, floor);
		}
	if (floor.getIsGameOver()) {
	    gameOver(frame, floor);
	}
	    floor.moveEnemies();
	    floor.bombCreate();
	    floor.explosionHandler();
		floor.playerInExplosion();
		floor.enemyInExplosion();
	    floor.notifyListeners();
    }
}
