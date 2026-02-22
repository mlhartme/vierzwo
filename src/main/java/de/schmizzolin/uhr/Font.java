package de.schmizzolin.uhr;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.HashMap;
import java.util.Map;

/* Characters made from 3x5 dots */
public class Font {
    private static final String[] DIGITS = { """
            x x x
            x   x
            x   x
            x   x
            x x x
            """, """
              x
            x x
              x
              x
            x x x
            """, """
            x x x
                x
              x
            x
            x x x
            """, """
            x x x
                x
              x x
                x
            x x x
            """, """
            x   x
            x   x
            x x x
                x
                x
            """, """
            x x x
            x
            x x x
                x
            x x x
            """, """
            x x x
            x
            x x x
            x   x
            x x x
            """, """
            x x x
                x
              x
              x
              x
            """, """
            x x x
            x   x
            x x x
            x   x
            x x x
            """, """
            x x x
            x   x
            x x x
                x
            x x x
            """
    };

    private static final String SPACE = """
            . . .
            . . .
            . . .
            . . .
            . . .
            """;
    private static final String COLON = """
            .
            x
            .
            .
            x
            """;
    private static final String UP = """
              x
            x x x
              x
              x
              .
            """;

    private static final String DOWN = """
              .
              x
              x
            x x x
              x
            """;

    public static Font create(Color color) {
        Font font = new Font(color);
        font.add(' ', SPACE);
        font.add(':', COLON, 1);
        font.add('^', UP);
        font.add('v', DOWN);
        for (int i = 0; i < DIGITS.length; i++) {
            font.add((char) ('0' + i), DIGITS[i]);
        }
        return font;
    }

    private final Map<Character, Matrix> dots; // value[y][x]
    private final Color color;

    public Font(Color color) {
        this.color = color;
        this.dots = new HashMap<>();
    }

    public void add(char character, String matrix) {
        add(character, matrix, 3);
    }

    public void add(char character, String matrix, int width) {
        dots.put(character, Matrix.create(width, 5, matrix));
    }

    public void add(char character, Matrix matrix) {
        dots.put(character, matrix);
    }

    /** @return number of dots */
    public int width(String str) {
        int result = 0;
        for (int i = 0; i < str.length(); i++) {
            result += dots.get(str.charAt(i)).width();
        }
        return result + (str.length() - 1);
    }

    public int height() {
        return 5;
    }

    public Group render(String str, int dotWidth) {
        var result = new Group();
        var ofs = 0;
        for (int i = 0; i < str.length(); i++) {
            var character = str.charAt(i);
            var matrix = dots.get(character);
            if (matrix == null) {
                throw new IllegalArgumentException("unknown character: " + character);
            }
            render(result, ofs, dotWidth, matrix);
            ofs += (matrix.width() + 1) * dotWidth;
        }
        return result;
    }

    private void render(Group dest, int ofs, int dotWidth, Matrix matrix) {
        var dotSpace = Math.max(1, dotWidth / 32);
        var dotArc = Math.max(1, dotWidth / 6);

        for (int y = 0; y < matrix.height(); y++) {
            for (int x = 0; x < matrix.width(); x++) {
                if (matrix.get(x, y)) {
                    Rectangle rect = new Rectangle(ofs + x * dotWidth, y * dotWidth, dotWidth, dotWidth);
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
