package de.schmizzolin.vierzwo;

import javafx.scene.paint.Color;

import java.util.List;

public class Matrix {
    public static Matrix create(int width, int height, List<String> matrix, Color color, Color color2, Color color3) {
        Color[][] result = new Color[matrix.size()][];
        if (matrix.size() != height) {
            throw new IllegalArgumentException("height: " + matrix.size());
        }
        for (int y = 0; y < matrix.size(); y++) {
            var line = matrix.get(y);
            result[y] = new Color[width];
            for (int twoX = 0; twoX < line.length(); twoX += 2) {
                var c = line.charAt(twoX);
                if ("x+* .".indexOf(c) < 0) {
                    throw new IllegalArgumentException("invalid char: " + c);
                }
                result[y][twoX / 2] = switch (c) {
                    case 'x' -> color;
                    case '+' -> color2;
                    case '*' -> color3;
                    default -> null;
                };
                if (twoX + 1 < line.length()) {
                    if (line.charAt(twoX + 1) != ' ') {
                        throw new IllegalArgumentException("line " + y + ": missing space at " + (twoX + 1));
                    }
                }
            }
        }
        return new Matrix(width, height, result);
    }

    private final int width;
    private final int height;
    private final Color[][] dots;

    public Matrix(int width, int height, Color[][] dots) {
        this.width = width;
        this.height = height;
        this.dots = dots;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public Color get(int x, int y) {
        return dots[y][x];
    }
}
