import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PlanetPanel extends JPanel {

    private BufferedImage image;
    private Noise noise;

    public PlanetPanel() {

        int width = 800;
        int height = 800;

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //SEED
        noise = new Noise(54321); // seed

        generateTerrain();
    }

    private void generateTerrain() {

        int width = image.getWidth();
        int height = image.getHeight();

        double scale = 0.01;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                double nx = x * scale;
                double ny = y * scale;

                double value = 0;

                double amplitude = 1;
                double frequency = 1;

                double maxValue = 0;

                for (int i = 0; i < 5; i++) {

                    value += noise.noise(nx * frequency, ny * frequency) * amplitude;

                    maxValue += amplitude;

                    amplitude *= 0.5;
                    frequency *= 2;
                }

                value /= maxValue;
                value = Math.pow(value, 1.2);

                int color;

                if (value < 0.35) {
                    color = new Color(0, 0, 150).getRGB(); // deep ocean
                }
                else if (value < 0.45) {
                    color = new Color(50, 120, 200).getRGB(); // shallow water
                }
                else if (value < 0.5) {
                    color = new Color(240, 230, 140).getRGB(); // beach
                }
                else if (value < 0.7) {
                    color = new Color(34, 139, 34).getRGB(); // land
                }
                else if (value < 0.85) {
                    color = new Color(100, 100, 100).getRGB(); // mountains
                }
                else {
                    color = Color.WHITE.getRGB(); // snow
                }

                image.setRGB(x, y, color);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }
}