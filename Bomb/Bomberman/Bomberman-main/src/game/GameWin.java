package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWin extends JFrame implements ActionListener {
    private static final int menuSize = 500;
    private final JPanel buttonPanel = new JPanel();


    public Action newGameAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            dispose();
            StartEngine.startGame();
        }
    };

    private JButton newGame;

    public GameWin() {
        super("Bomberman");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        setContentPane(new JLabel(new ImageIcon("Bomberman/Bomberman-main/src/resourses/gamewin.png")));
        setLayout(new FlowLayout());
        setSize(menuSize,menuSize);
        setLocationRelativeTo(null);

        String newGameLabel = "<html>New Game</html>";
        newGame = new JButton(newGameLabel);

        newGame.addActionListener(newGameAction);

        buttonPanel.add(newGame);

        setLayout(new GridBagLayout());
        add(buttonPanel,new GridBagConstraints());

        setSize(menuSize, menuSize);
        setVisible(true);
    }

    public void actionPerformed( ActionEvent evt) {

    }
}
