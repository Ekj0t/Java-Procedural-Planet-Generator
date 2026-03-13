import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class PlanetPanel extends JPanel {

    private double rotation = 0;
    private PlanetGenerator generator;
    private BufferedImage image;
    private Noise noise;
    private PlanetType currentType;
    private Random random = new Random();
    private long globalSeed;

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
                    globalSeed = seed;
                    noise = new Noise(seed);

                    System.out.println("New planet seed: " + seed);
                }

                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_P) {

                    PlanetType[] types = PlanetType.values();

                    int index = (currentType.ordinal() + 1) % types.length;

                    currentType = types[index];
                    generator = new PlanetGenerator(currentType);

                    System.out.println("Planet type: " + currentType);
                }
            }
        });

        new Timer(30, e -> {
            rotation += 0.01;
            generatePlanet();
            repaint();
        }).start();

        // initial seed
        noise = new Noise(54321);

        // initialize planet type
        PlanetType[] types = PlanetType.values();
        currentType = types[random.nextInt(types.length)];

        generator = new PlanetGenerator(currentType);

        System.out.println("Planet type: " + currentType);
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

        double cosR = Math.cos(rotation);
        double sinR = Math.sin(rotation);

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

                brightness = brightness * 0.85 + 0.15;

                brightness *= 0.7 + 0.3 * nz3;

                brightness = Math.min(1, brightness);


                double rx = dx * cosR - dz * sinR;
                double rz = dx * sinR + dz * cosR;

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

                int r = ((color >> 16) & 255);
                int g = ((color >> 8) & 255);
                int b = (color & 255);

                r = (int)(r * brightness);
                g = (int)(g * brightness);
                b = (int)(b * brightness);

                r = Math.min(255, r);
                g = Math.min(255, g);
                b = Math.min(255, b);

                int rgb = (r << 16) | (g << 8) | b;

                image.setRGB(x, y, rgb);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Minecrafter", Font.PLAIN, 16));
        g.drawString("Planet: " + currentType, 10, 20);
        g.drawString("Seed: " + globalSeed, 10, 40);
    }
}