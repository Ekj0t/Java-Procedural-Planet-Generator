import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Procedural Planet Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PlanetPanel panel = new PlanetPanel();

        frame.add(panel);
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}