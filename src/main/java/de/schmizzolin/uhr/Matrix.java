package de.schmizzolin.uhr;

import javafx.scene.paint.Color;

public class Matrix {
    public static Matrix create(int width, int height, String matrix, Color color) {
        String[] lines = matrix.split("\n");
        Color[][] result = new Color[lines.length][];
        if (lines.length != height) {
            throw new IllegalArgumentException("height: " + lines.length);
        }
        for (int y = 0; y < lines.length; y++) {
            var line = lines[y];
            result[y] = new Color[width];
            for (int twoX = 0; twoX < line.length(); twoX += 2) {
                var c = line.charAt(twoX);
                if (c != 'x' && c != ' ' && c != '.') {
                    throw new IllegalArgumentException("invalid char: " + c);
                }
                result[y][twoX / 2] = (c =='x') ? color : null;
                if (twoX + 1 < line.length()) {
                    if (line.charAt(twoX + 1) != ' ') {
                        throw new IllegalArgumentException("missing space at " + (twoX + 1));
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
