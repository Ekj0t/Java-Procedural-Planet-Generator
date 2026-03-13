import java.awt.*;

public class PlanetGenerator {

    private PlanetType type;

    private int earthColor(double h) {

        if (h < 0.35) return new Color(0,0,150).getRGB();
        if (h < 0.45) return new Color(50,120,200).getRGB();
        if (h < 0.5) return new Color(240,230,140).getRGB();
        if (h < 0.7) return new Color(34,139,34).getRGB();
        if (h < 0.85) return new Color(100,100,100).getRGB();
        return Color.WHITE.getRGB();
    }

    private int desertColor(double h) {

        if (h < 0.35) return new Color(210,180,140).getRGB();
        if (h < 0.6) return new Color(237,201,175).getRGB();
        if (h < 0.8) return new Color(205,133,63).getRGB();

        return new Color(160,82,45).getRGB();
    }

    private int iceColor(double h) {

        if (h < 0.4) return new Color(180,220,255).getRGB();
        if (h < 0.7) return new Color(220,240,255).getRGB();
        if (h < 0.9) return new Color(200,200,220).getRGB();

        return Color.WHITE.getRGB();
    }

    private int lavaColor(double h) {

        if (h < 0.4) return new Color(60,0,0).getRGB();
        if (h < 0.6) return new Color(120,20,0).getRGB();
        if (h < 0.8) return new Color(200,60,0).getRGB();

        return new Color(255,120,0).getRGB();
    }

    public PlanetGenerator(PlanetType type) {
        this.type = type;
    }

    public int getColor(double height) {

        switch (type) {

            case EARTH:
                return earthColor(height);

            case DESERT:
                return desertColor(height);

            case ICE:
                return iceColor(height);

            case LAVA:
                return lavaColor(height);
        }

        return Color.WHITE.getRGB();
    }
}