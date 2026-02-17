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

    private final Map<Character, boolean[][]> maps; // value[y][x]
    private final Color color;
    private final int dotWidth;
    private final int dotSpace;
    private final int dotArc;
    private final int charSpace;

    public Font(Color color, int dotWidth, int charSpace) {
        this.color = color;
        this.maps = new HashMap<>();
        this.dotWidth = dotWidth;
        this.dotSpace = Math.max(1, dotWidth / 32);
        this.dotArc = Math.max(1, dotWidth / 6);
        this.charSpace = charSpace;
    }

    public void add(char character, String matrix) {
        maps.put(character, check(matrix));
    }

    private static final int HEIGHT = 5;

    private static boolean[][] check(String matrix) {
        String[] lines = matrix.split("\n");
        boolean[][] result = new boolean[lines.length][];
        if (lines.length != HEIGHT) {
            throw new IllegalArgumentException("height: " + lines.length);
        }
        for (int y = 0; y < lines.length; y++) {
            var line = lines[y];
            result[y] = new boolean[3];
            for (int twoX = 0; twoX < line.length(); twoX += 2) {
                var c = line.charAt(twoX);
                if (c != 'x' && c != ' ' && c != '.') {
                    throw new IllegalArgumentException("invalid char: " + c);
                }
                result[y][twoX / 2] = (c =='x');
                if (twoX + 1 < line.length()) {
                    if (line.charAt(twoX + 1) != ' ') {
                        throw new IllegalArgumentException("missing space at " + (twoX + 1));
                    }
                }
            }
        }
        return result;
    }

    public Group render(String str) {
        Group result = new Group();
        for (int i = 0; i < str.length(); i++) {
            render(result, i * 3 * dotWidth + charSpace * i, str.charAt(i));
        }
        return result;
    }

    private void render(Group dest, int ofs, char character) {
        boolean[][] matrix = maps.get(character);
        if (matrix == null) {
            throw new IllegalArgumentException("unknown character: " + character);
        }
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                if (matrix[y][x]) {
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
