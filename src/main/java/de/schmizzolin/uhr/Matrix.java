package de.schmizzolin.uhr;

public class Matrix {
    public static Matrix create(String matrix) {
        return create(3, 5, matrix);
    }

    public static Matrix create(int width, int height, String matrix) {
        String[] lines = matrix.split("\n");
        boolean[][] result = new boolean[lines.length][];
        if (lines.length != height) {
            throw new IllegalArgumentException("height: " + lines.length);
        }
        for (int y = 0; y < lines.length; y++) {
            var line = lines[y];
            result[y] = new boolean[width];
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
        return new Matrix(width, height, result);
    }

    private final int width;
    private final int height;
    private final boolean[][] dots;

    public Matrix(int width, int height, boolean[][] dots) {
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

    public boolean get(int x, int y) {
        return dots[y][x];
    }
}
