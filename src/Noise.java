import java.util.Random;

public class Noise {

    private final int[] permutation = new int[512];

    public Noise(long seed) {

        int[] p = new int[256];
        Random random = new Random(seed);

        for (int i = 0; i < 256; i++) {
            p[i] = i;
        }

        for (int i = 255; i > 0; i--) {

            int j = random.nextInt(i + 1);

            int temp = p[i];
            p[i] = p[j];
            p[j] = temp;
        }

        for (int i = 0; i < 512; i++) {
            permutation[i] = p[i & 255];
        }
    }

    public double noise(double x, double y) {

        int xi = (int)Math.floor(x) & 255;
        int yi = (int)Math.floor(y) & 255;

        double xf = x - Math.floor(x);
        double yf = y - Math.floor(y);

        double u = fade(xf);
        double v = fade(yf);

        int aa = permutation[permutation[xi] + yi];
        int ab = permutation[permutation[xi] + yi + 1];
        int ba = permutation[permutation[xi + 1] + yi];
        int bb = permutation[permutation[xi + 1] + yi + 1];

        double x1 = lerp(
                grad(aa, xf, yf),
                grad(ba, xf - 1, yf),
                u
        );

        double x2 = lerp(
                grad(ab, xf, yf - 1),
                grad(bb, xf - 1, yf - 1),
                u
        );

        return (lerp(x1, x2, v) + 1) / 2;
    }

    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }

    private double grad(int hash, double x, double y) {

        int h = hash & 3;

        double u = h < 2 ? x : y;
        double v = h < 2 ? y : x;

        return ((h & 1) == 0 ? u : -u) +
                ((h & 2) == 0 ? v : -v);
    }
}