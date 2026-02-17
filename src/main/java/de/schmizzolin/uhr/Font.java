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
              x .
            x . .
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

    private static final String COLON = """
            . . .
            . x .
            . . .
            . x .
            . . .
            """;

    public static Font create(Color color, int dotSize) {
        Font font = new Font(color, dotSize, dotSize / 2);
        font.add(':', COLON);
        for (int i = 0; i < DIGITS.length; i++) {
            font.add((char) ('0' + i), DIGITS[i]);
        }
        return font;
    }

    private final Map<Character, Matrix> dots; // value[y][x]
    private final Color color;
    private final int dotWidth;
    private final int dotSpace;
    private final int dotArc;
    private final int charSpace;

    public Font(Color color, int dotWidth, int charSpace) {
        this.color = color;
        this.dots = new HashMap<>();
        this.dotWidth = dotWidth;
        this.dotSpace = Math.max(1, dotWidth / 32);
        this.dotArc = Math.max(1, dotWidth / 6);
        this.charSpace = charSpace;
    }

    public void add(char character, String matrix) {
        dots.put(character, Matrix.create(matrix));
    }

    public void add(char character, Matrix matrix) {
        dots.put(character, matrix);
    }

    public Group render(String str) {
        Group result = new Group();
        for (int i = 0; i < str.length(); i++) {
            render(result, i * 3 * dotWidth + charSpace * i, str.charAt(i));
        }
        return result;
    }

    private void render(Group dest, int ofs, char character) {
        Matrix matrix = dots.get(character);
        if (matrix == null) {
            throw new IllegalArgumentException("unknown character: " + character);
        }
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
