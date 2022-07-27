package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame implements ActionListener {
    private static final int menuSize = 500;
    private final JPanel buttonPanel = new JPanel();

    public Action newGameAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            dispose();
            StartEngine.startGame();
        }
    };
    public Action quitAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    };
    private JButton newGame;
    private JButton quit;

    public Menu() throws HeadlessException {
        super("Bomberman");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        setContentPane(new JLabel(new ImageIcon("Bomberman/Bomberman-main/src/resourses/background.png")));
        setLayout(new FlowLayout());
        setSize(menuSize,menuSize);
        setLocationRelativeTo(null);

        String newGameLabel = "<html>New Game</html>";
        String quitLabel = "<html>Quit</html>";
        newGame = new JButton(newGameLabel);
        quit = new JButton(quitLabel);

        newGame.addActionListener(newGameAction);
        quit.addActionListener(quitAction);

        buttonPanel.add(newGame);
        buttonPanel.add(quit);

        setLayout(new GridBagLayout());
        add(buttonPanel,new GridBagConstraints());

        setSize(menuSize, menuSize);
        setVisible(true);
    }

    public void actionPerformed( ActionEvent evt) {

    }
}