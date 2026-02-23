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
    private static final String UP = """
              .
              x
            x x x
              x
              x
              .
              .
              .
            """;

    private static final String DOWN = """
              .
              .
              x
              x
            x x x
              x
              .
              .
            """;

    public static Font create() {
        Font font = new Font();
        font.add(' ', SPACE);
        font.add(':', COLON, 1, Color.WHITE);
        font.add('^', UP, 3, Color.LIGHTPINK);
        font.add('v', DOWN, 3, Color.LIGHTBLUE);
        for (int i = 0; i < DIGITS.length; i++) {
            font.add((char) ('0' + i), DIGITS[i]);
        }
        return font;
    }

    private final Map<Character, Matrix> dots; // value[y][x]

    public Font() {
        this.dots = new HashMap<>();
    }

    public void add(char character, String matrix) {
        add(character, matrix, 3, Color.WHITE);
    }

    public void add(char character, String matrix, int width, Color color) {
        dots.put(character, Matrix.create(width, HEIGHT, matrix, color));
    }

    public void add(char character, Matrix matrix) {
        dots.put(character, matrix);
    }

    /** @return number of dots */
    public int width(String... text) {
        var result = 0;
        for (var str : text) {
            var line = 0;
            for (var i = 0; i < str.length(); i++) {
                line += dots.get(str.charAt(i)).width();
            }
            line += str.length() - 1;
            result = Math.max(result, line);
        }
        return result;
    }

    public int height(String... text) {
        return text.length * HEIGHT;
    }

    public Group render(int dotWidth, String... text) {
        var result = new Group();
        var yOfs = 0;
        for (var str : text) {
            var xOfs = 0;
            for (int i = 0; i < str.length(); i++) {
                var character = str.charAt(i);
                var matrix = dots.get(character);
                if (matrix == null) {
                    throw new IllegalArgumentException("unknown character: " + character);
                }
                render(result, xOfs, yOfs, dotWidth, matrix);
                xOfs += (matrix.width() + 1) * dotWidth;
            }
            yOfs += HEIGHT * dotWidth;
        }
        return result;
    }

    private void render(Group dest, int xOfs, int yOfs, int dotWidth, Matrix matrix) {
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
