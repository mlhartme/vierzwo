package de.schmizzolin.uhr;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.HashMap;
import java.util.Map;

/* Charcters made from 3x5 dots */
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

    public static Font create(Color color, int dotWidth, int charSpace) {
        Font font = new Font(color, dotWidth, charSpace);
        font.add(':', COLON);
        for (int i = 0; i < DIGITS.length; i++) {
            font.add((char) ('0' + i), DIGITS[i]);
        }
        return font;
    }

    private final Map<Character, String> maps;
    private final Color color;
    private final int dotWidth;
    private final int dotSpace;
    private final int charSpace;

    public Font(Color color, int dotWidth, int charSpace) {
        this.color = color;
        this.maps = new HashMap<>();
        this.dotWidth = dotWidth;
        this.dotSpace = Math.max(1, dotWidth / 32);
        this.charSpace = charSpace;
    }

    public void add(char character, String matrix) {
        maps.put(character, check(matrix));
    }

    private static final int HEIGHT = 5;

    private static String check(String matrix) {
        String[] lines = matrix.split("\n");
        if (lines.length != HEIGHT) {
            throw new IllegalArgumentException("height: " + lines.length);
        }
        for (String line : lines) {
            for (int i = 0; i < line.length(); i += 2) {
                var c = line.charAt(i);
                if (c != 'x' && c != ' ' && c != '.') {
                    throw new IllegalArgumentException("invalid char: " + c);
                }
            }
        }
        return matrix;
    }

    public HBox render(String str) {
        HBox characters = new HBox(charSpace);
        for (int i = 0; i < str.length(); i++) {
            var c = render(str.charAt(i));
            characters.getChildren().add(c);
        }
        return characters;
    }

    private Group render(char character) {
        var result = new Group();
        String matrix = maps.get(character);
        if (matrix == null) {
            throw new IllegalArgumentException("unknown character: " + character);
        }
        String[] lines = matrix.split("\n");
        System.out.println("char: " + character + ": ");
        for (int y = 0; y < lines.length; y++) {
            var line = lines[y];
            for (int i = 0; i < line.length(); i += 2) {
                var x = i / 2;
                var c = line.charAt(i);
                if (c == 'x') {
                    System.out.print("(" + x + "," + y + ") ");
                    Rectangle rect = new Rectangle(x * dotWidth, y * dotWidth, dotWidth, dotWidth);
                    rect.setFill(color);
                    rect.setStrokeWidth(dotSpace);
                    rect.setStrokeType(StrokeType.INSIDE);
                    rect.setStroke(Color.BLACK);
                    rect.setArcWidth(5);
                    rect.setArcHeight(5);
                    result.getChildren().add(rect);
                }
            }
        }
        System.out.println();
        return result;
    }
}
