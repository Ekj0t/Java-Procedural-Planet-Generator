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

        generatePlanet();
    }

    private void generatePlanet() {

        int width = image.getWidth();
        int height = image.getHeight();

        int centerX = width / 2;
        int centerY = height / 2;

        int radius = width / 2;

        double scale = 3.0;

        double lightX = -0.5;
        double lightY = -0.5;
        double lightZ = 1;

        double len = Math.sqrt(lightX*lightX + lightY*lightY + lightZ*lightZ);
        lightX /= len;
        lightY /= len;
        lightZ /= len;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                double dx = (x - centerX) / (double) radius;
                double dy = (y - centerY) / (double) radius;

                double dist = dx * dx + dy * dy;

                if (dist > 1) {
                    image.setRGB(x, y, Color.BLACK.getRGB());
                    continue;
                }

                double dz = Math.sqrt(1 - dist);

                double nx3 = dx;
                double ny3 = dy;
                double nz3 = dz;

                double brightness =
                        nx3 * lightX +
                                ny3 * lightY +
                                nz3 * lightZ;

                brightness = Math.max(0, brightness);

                double longitude = Math.atan2(dy, dx);
                double latitude = Math.acos(dz);

                double value = noise.noise(dx * scale, dy * scale)
                        + noise.noise(dy * scale, dz * scale)
                        + noise.noise(dx * scale, dz * scale);

                value /= 3.0;
                int color;

                if (value < 0.35)
                    color = new Color(0, 0, 150).getRGB();
                else if (value < 0.45)
                    color = new Color(50, 120, 200).getRGB();
                else if (value < 0.5)
                    color = new Color(240, 230, 140).getRGB();
                else if (value < 0.7)
                    color = new Color(34, 139, 34).getRGB();
                else if (value < 0.85)
                    color = new Color(100, 100, 100).getRGB();
                else
                    color = Color.WHITE.getRGB();

                Color c = new Color(color);

                int r = (int)(c.getRed() * brightness);
                int g = (int)(c.getGreen() * brightness);
                int b = (int)(c.getBlue() * brightness);

                Color shaded = new Color(
                        Math.min(255, r),
                        Math.min(255, g),
                        Math.min(255, b)
                );

                image.setRGB(x, y, shaded.getRGB());
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }
}