package khouim;

import javax.swing.JFrame;

public class App {

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Adventure");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        
        gamePanel.setUpGame();
        gamePanel.requestFocusInWindow(); // <-- ensures KeyListener works
        gamePanel.startGameThread();
    }
}
