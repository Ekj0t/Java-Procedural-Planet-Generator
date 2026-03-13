import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class PlanetPanel extends JPanel {

    private double rotation = 0;
    private PlanetGenerator generator;
    private BufferedImage image;
    private Noise noise;
    private Random random = new Random();

    public PlanetPanel() {

        int width = 800;
        int height = 800;

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {

                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_R) {

                    long seed = random.nextLong();

                    noise = new Noise(seed);

                    PlanetType[] types = PlanetType.values();
                    PlanetType type = types[random.nextInt(types.length)];

                    generator = new PlanetGenerator(type);

                    System.out.println("New planet seed: " + seed);
                    System.out.println("Planet type: " + type);
                }
            }
        });

        new Timer(40, e -> {
            rotation += 0.01;
            generatePlanet();
            repaint();
        }).start();

        //SEED
        noise = new Noise(54321); // seed

        PlanetType[] types = PlanetType.values();
        PlanetType type = types[random.nextInt(types.length)];

        generator = new PlanetGenerator(type);

        System.out.println("Planet type: " + type);
    }

    private void generatePlanet() {

        int width = image.getWidth();
        int height = image.getHeight();

        int centerX = width / 2;
        int centerY = height / 2;

        int radius = Math.min(width, height) / 2 - 40;

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

// ambient
                brightness = brightness * 0.85 + 0.15;

// limb shading
                brightness *= 0.7 + 0.3 * nz3;

                brightness = Math.min(1, brightness);


                double rx = dx * Math.cos(rotation) - dz * Math.sin(rotation);
                double rz = dx * Math.sin(rotation) + dz * Math.cos(rotation);

                double value = 0;
                double amplitude = 1;
                double frequency = 1;
                double maxValue = 0;

                for (int i = 0; i < 4; i++) {

                    double n =
                            noise.noise(rx * scale * frequency, dy * scale * frequency) +
                                    noise.noise(dy * scale * frequency, rz * scale * frequency) +
                                    noise.noise(rx * scale * frequency, rz * scale * frequency);

                    n /= 3.0;

                    value += n * amplitude;

                    maxValue += amplitude;

                    amplitude *= 0.5;
                    frequency *= 2;
                }

                value /= maxValue;

                value = Math.pow(value, 1.2);

                double eps = 0.01;

                double valueX = 0;
                double valueY = 0;

                amplitude = 1;
                frequency = 1;
                maxValue = 0;

                for (int i = 0; i < 4; i++) {

                    double nx =
                            noise.noise((rx + eps) * scale * frequency, dy * scale * frequency) +
                                    noise.noise(dy * scale * frequency, rz * scale * frequency) +
                                    noise.noise((rx + eps) * scale * frequency, rz * scale * frequency);

                    nx /= 3.0;

                    double ny =
                            noise.noise(rx * scale * frequency, (dy + eps) * scale * frequency) +
                                    noise.noise((dy + eps) * scale * frequency, rz * scale * frequency) +
                                    noise.noise(rx * scale * frequency, rz * scale * frequency);

                    ny /= 3.0;

                    valueX += nx * amplitude;
                    valueY += ny * amplitude;

                    maxValue += amplitude;

                    amplitude *= 0.5;
                    frequency *= 2;
                }

                valueX /= maxValue;
                valueY /= maxValue;

                double slopeX = valueX - value;
                double slopeY = valueY - value;

                brightness -= slopeX * 0.8;
                brightness -= slopeY * 0.8;

                brightness = Math.max(0, Math.min(1, brightness));

                int color = generator.getColor(value);

                Color c = new Color(color);

                int r = (int)(c.getRed() * brightness);
                int g = (int)(c.getGreen() * brightness);
                int b = (int)(c.getBlue() * brightness);

                Color shaded = new Color(
                        Math.min(255, r),
                        Math.min(255, g),
                        Math.min(255, b)
                );

                double edge = Math.sqrt(dist);

                double atmosphere = edge - 0.89;

                if (atmosphere > 0) {

                    atmosphere = Math.pow(atmosphere * 6, 3);

                    int ar = (int)(30 * atmosphere);
                    int ag = (int)(80 * atmosphere);
                    int ab = (int)(200 * atmosphere);

                    r = Math.min(255, shaded.getRed() + ar);
                    g = Math.min(255, shaded.getGreen() + ag);
                    b = Math.min(255, shaded.getBlue() + ab);

                    shaded = new Color(r, g, b);
                }

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