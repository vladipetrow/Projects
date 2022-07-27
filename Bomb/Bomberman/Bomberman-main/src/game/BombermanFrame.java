package game;

import javax.swing.*;
import java.awt.*;

public class BombermanFrame extends JFrame
{
    private Map floor;
    private BombermanPaint bombermanComponent;

    public BombermanFrame(final String title, Map floor) throws HeadlessException {
	super(title);
	this.floor = floor;
	this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

	bombermanComponent = new BombermanPaint(floor);
	floor.createPlayer(bombermanComponent, floor);

	this.setLayout(new BorderLayout());
	this.add(bombermanComponent, BorderLayout.CENTER);
	this.pack();
	this.setVisible(true);
    }
    public BombermanPaint getBombermanComponent() {
	return bombermanComponent;
    }

}

