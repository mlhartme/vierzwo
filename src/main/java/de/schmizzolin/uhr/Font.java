package de.schmizzolin.uhr;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.HashMap;
import java.util.Map;

/** Variable width font with fixed height. Each character is defined as a matrix of dots. */
public class Font {
    private static final int HEIGHT = 8;

    private static final String[] DIGITS = { """
            .
            x x x
            x   x
            x   x
            x   x
            x x x
            .
            .
            """, """
            .
              x
            x x
              x
              x
            x x x
            .
            .
            """, """
            .
            x x x
                x
              x
            x
            x x x
            .
            .
            """, """
            .
            x x x
                x
              x x
                x
            x x x
            .
            .
            """, """
            .
            x   x
            x   x
            x x x
                x
                x
            .
            .
            """, """
            .
            x x x
            x
            x x x
                x
            x x x
            .
            .
            """, """
            .
            x x x
            x
            x x x
            x   x
            x x x
            .
            .
            """, """
            .
            x x x
                x
              x
              x
              x
            .
            .
            """, """
            .
            x x x
            x   x
            x x x
            x   x
            x x x
            .
            .
            """, """
            .
            x x x
            x   x
            x x x
                x
            x x x
            .
            .
            """
    };

    private static final String HOUR = """
            .
            x
            x
            x x
            x   x
            x   x
            .
            .
            """;
    private static final String EM = """
            .
            .
            .
            x x   x
            x   x   x
            x   x   x
            .
            .
            """;
    private static final String DEGREE = """
            x x
            x x
            .
            .
            .
            .
            .
            .
            """;
    private static final String CELSIUS = """
            .
            . x x
            x
            x
            x
              x x
            .
            .
            """;
    private static final String TO = """
            .
            .
            .
            x x x
            .
            .
            .
            .
            """;
    private static final String SPACE = """
            . . .
            . . .
            . . .
            . . .
            . . .
            . . .
            . . .
            . . .
            """;
    private static final String COLON = """
            .
            .
            x
            .
            .
            x
            .
            .
            """;

    //-- https://listed.to/@DieSieben/7851/api-des-deutschen-wetterdienstes at the end

    // icon 1 - Sonne
    private static final String SUNNY = """
                x   x
                x   x
            x x x x x x x
                x x x
                x x x
            x x x x x x x
                x   x
                x   x
            """;
    // icon 2 Sonne, leicht bewölkt
    private static final String SUNNY_WITH_CLOUD = """
                x   x
                x   x
            x x x x x x x
                x x x
                x x x
            x x x x + +   + +
                x + + + + + + +
                x + + + + + + +
            """;
    // icon 3 Sonne, bewölkt
    private static final String SUNNY_WITH_MORE_CLOUD = """
         .           +   +
         .           +   +
         .       + + + + + + +
           x x x   x x x +
         x x x x x x x x x + +
         x x x x x x x x x
         x x x x x x x x x
         .
         """;
    // icon 4 Wolken
    private static final String CLOUDY = """
         .
         .
         .
           x x x   x x
         x x x x x + + x + +
         x x x x + + + + + + +
         x x x x + + + + + + +
         .
         """;

    // icon 5 - Nebel
    private static final String FOG = """
         .
         x   x   x   x
           x   x   x

         x   x   x   x
           x   x   x
         .
         .
         """;

    // TODO icon 6 - Nebel, rutschgefahr

    // icon 7 --leichter Regen
    private static final String LIGHT_RAIN = """
         .
           x x x   x x x
         x x x x x x x x x x
         x x x x x x x x x x x
         x x x x x x x x x x x
                 +
               +
             +
         """;

    // icon 8 - Regen
    private static final String RAIN = """
         .
           x x x   x x x
         x x x x x x x x x x
         x x x x x x x x x x x
         x x x x x x x x x x x
               +   +
             +   +
         +   +
         """;

    // icon 9 - starker Regen
    private static final String HEAVY_RAIN = """
         .
           x x x   x x x
         x x x x x x x x x x
         x x x x x x x x x x x
         x x x x x x x x x x x
             +   +   +
           +   +   +
         +   +   +
         """;

    // unknown icon
    private static final String UNKNOWN = """
         .
           x
           x
           x
         .
           x
         .
         .
         """;


    private static final String SMALL_SUN = """
            .
            .
            .
            x x
            x x
            .
            .
            .
            """;

    private static final String SMALL_RAIN = """
            .
            .
            .
            x x
            x x
            .
            .
            .
            """;

    public static Font create() {
        Font font = new Font();
        font.add(' ', SPACE);
        font.add(':', COLON, 1, Color.WHITE);

        font.add('h', HOUR, 3, Color.WHITE);
        font.add('m', EM, 5, Color.WHITE);
        font.add('d', DEGREE, 2, Color.WHITE);
        font.add('c', CELSIUS, 3, Color.WHITE);
        font.add('-', TO, 3, Color.WHITE);

        font.add('!', UNKNOWN, 7, Color.LIGHTGRAY);

        // iday pictogramm
        // https://content.meteoblue.com/de/forschung-bildung/spezifikationen/standards/symbole-und-piktogramme
        /*  1 */ font.add('A', SUNNY, 7, Color.YELLOW);
        /*  2 */ font.add('B', SUNNY_WITH_CLOUD, 10, Color.YELLOW, Color.LIGHTGRAY);
        /*  3 */ font.add('C', SUNNY_WITH_MORE_CLOUD, 11, Color.GRAY, Color.YELLOW);
        /*  4 */ font.add('D', CLOUDY, 11, Color.DARKGRAY, Color.GRAY);
        /*  5 */ font.add('E', FOG, 7, Color.LIGHTGRAY);
        /*  7 */ font.add('G', LIGHT_RAIN, 11, Color.GRAY, Color.BLUE);
        /*  8 */ font.add('H', RAIN, 11, Color.GRAY, Color.BLUE);
        /*  9 */ font.add('I', HEAVY_RAIN, 11, Color.GRAY, Color.BLUE);

        font.add('s', SMALL_SUN, 2, Color.YELLOW);
        font.add('p', SMALL_RAIN, 2, Color.BLUE);

        for (int i = 0; i < DIGITS.length; i++) {
            font.add((char) ('0' + i), DIGITS[i]);
        }
        return font;
    }

    private final Map<Character, Matrix> dots; // value[y][x]

    public Font() {
        this.dots = new HashMap<>();
    }

    protected Matrix get(char c) {
        return dots.get(dots.containsKey(c) ? c : '!');
    }

    public void add(char character, String matrix) {
        add(character, matrix, 3, Color.WHITE);
    }

    public void add(char character, String matrix, int width, Color color) {
        add(character, matrix, width, color, color);
    }
    public void add(char character, String matrix, int width, Color color, Color color2) {
        dots.put(character, Matrix.create(width, HEIGHT, matrix, color, color2));
    }

    public void add(char character, Matrix matrix) {
        dots.put(character, matrix);
    }

    /** @return number of dots */
    public int width(String... text) {
        var result = 0;
        for (var str : text) {
            result = Math.max(result, width(str));
        }
        return result;
    }

    /** @return number of dots */
    public int width(String str) {
        var result = str.length() - 1;
        for (var i = 0; i < str.length(); i++) {
            result += get(str.charAt(i)).width();
        }
        return result;
    }

    public int height(String... text) {
        return text.length * HEIGHT;
    }

    public Group render(int dotWidth, String... text) {
        var maxDots = width(text);
        var result = new Group();
        var yOfs = 0;
        for (var str : text) {
            var xOfs = (maxDots - width(str)) / 2 * dotWidth;
            for (int i = 0; i < str.length(); i++) {
                var character = str.charAt(i);
                var matrix = get(character);
                if (matrix == null) {
                    throw new IllegalArgumentException("unknown character: " + character);
                }
                renderCharacter(result, xOfs, yOfs, dotWidth, matrix);
                xOfs += (matrix.width() + 1) * dotWidth;
            }
            yOfs += HEIGHT * dotWidth;
        }
        return result;
    }

    private void renderCharacter(Group dest, int xOfs, int yOfs, int dotWidth, Matrix matrix) {
        var dotSpace = Math.max(1, dotWidth / 32);
        var dotArc = Math.max(1, dotWidth / 6);

        for (int y = 0; y < matrix.height(); y++) {
            for (int x = 0; x < matrix.width(); x++) {
                var color = matrix.get(x, y);
                if (color != null) {
                    Rectangle rect = new Rectangle(xOfs + x * dotWidth, yOfs + y * dotWidth, dotWidth, dotWidth);
                    rect.setFill(color);
                    rect.setStrokeWidth(dotSpace);
                    rect.setStrokeType(StrokeType.INSIDE);
                    rect.setStroke(Color.BLACK);
                    rect.setArcWidth(dotArc);
                    rect.setArcHeight(dotArc);
                    dest.getChildren().add(rect);
                }
            }
        }
    }
}
