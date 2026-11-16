package de.schmizzolin.vierzwo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Variable width font with fixed height. Each character is defined as a matrix of dots. */
public class Font {
    private static final int HEIGHT = 8;

    public static Font create(String name) {
        Font font = new Font();
        font.addResource(name);
        return font;
    }

    //--

    private final Map<Character, Matrix> dots; // value[y][x]

    public Font() {
        this.dots = new HashMap<>();
    }

    protected Matrix get(char c) {
        return dots.get(dots.containsKey(c) ? c : '!');
    }

    public void addResource(String name) {
        List<String> lines;

        try {
            lines = new ArrayList<>(readResource(name));
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }

        while (!lines.isEmpty()) {
            var header = lines.removeFirst();
            if (!header.startsWith("---")) {
                throw new IllegalArgumentException("invalid header: " + header);
            }
            eatEmpty(lines);
            var body = eatBody(lines);
            add(removeAfter(header.substring(3).trim(), "/").trim(), body);

            eatEmpty(lines);
        }
    }

    private static void eatEmpty(List<String> lines) {
        while (!lines.isEmpty()) {
            if (!lines.getFirst().isBlank()) {
                return;
            }
            lines.removeFirst();
        }
    }

    private static List<String> eatBody(List<String> lines) {
        List<String> result = new ArrayList<>();
        while (!lines.isEmpty()) {
            var first = lines.getFirst();
            if (first.isEmpty() || first.startsWith("---")) {
                break;
            }
            result.add(lines.removeFirst());
        }
        return result;
    }

    private void addOne(String header, List<String> body) {
        add(removeAfter(header, "/").trim(), body);
    }

    private static String removeAfter(String str, String c) {
        var idx = str.indexOf(c);
        return idx < 0 ? str : str.substring(0, idx);
    }

    private void add(String header, List<String> body) {
        if (header.isBlank()) {
            add(' ', body, matrixWidth(body), Color.WHITE, Color.WHITE, Color.WHITE);
        } else {
            if (header.charAt(1) != ' ') {
                throw new IllegalArgumentException("invalid header: " + header);
            }
            var c = header.charAt(0);
            var colors = threeColors(header.substring(2).trim());
            add(c, body, matrixWidth(body), colors.get(0), colors.get(1), colors.get(2));
        }
    }

    private static int matrixWidth(List<String> matrix) {
        var width = 0;
        for (String line : matrix) {
            width = Math.max(width, (line.length() + 1) / 2);
        }
        return width;
    }

    private static List<Color> threeColors(String str) {
        List<Color> result = new ArrayList<>();
        for (var name : str.split(" ")) {
            result.add(color(name));
        }
        while (result.size() < 3) {
            result.add(result.get(result.size() - 1));
        }
        return result;
    }

    private static Color color(String name) {
        return switch (name) {
            case "BLACK" -> Color.BLACK;
            case "WHITE" -> Color.WHITE;
            case "RED" -> Color.RED;
            case "YELLOW" -> Color.YELLOW;
            case "LIGHTGRAY" -> new Color(0xD3D3D3);
            case "DARKGRAY" -> new Color(0xA9A9A9);
            case "GRAY" -> Color.GRAY;
            case "BLUE" -> Color.BLUE;
            default -> throw new IllegalArgumentException("unknown color: " + name);
        };
    }

    private List<String> readResource(String name) throws IOException {
        try (var in = getClass().getResourceAsStream("/" + name)) {
            if (in == null) {
                throw new IOException("resource not found: " + name);
            }
            try (var reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                return reader.readAllLines();
            }
        }
    }

    public void add(char character, List<String> matrix, int width, Color color, Color color2, Color color3) {
        try {
            dots.put(character, Matrix.create(width, HEIGHT, matrix, color, color2, color3));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("char " + character + ": " + e.getMessage(), e);
        }
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

    public void render(Graphics2D dest, int dotWidth, int x, int y, String... text) {
        var maxDots = width(text);
        for (var str : text) {
            var xOfs = x + (maxDots - width(str)) / 2 * dotWidth;
            for (int i = 0; i < str.length(); i++) {
                var character = str.charAt(i);
                var matrix = get(character);
                if (matrix == null) {
                    throw new IllegalArgumentException("unknown character: " + character);
                }
                renderCharacter(dest, xOfs, y, dotWidth, matrix);
                xOfs += (matrix.width() + 1) * dotWidth;
            }
            y += HEIGHT * dotWidth;
        }
    }

    private void renderCharacter(Graphics2D dest, int xOfs, int yOfs, int dotWidth, Matrix matrix) {
        var dotSpace = Math.max(1, dotWidth / 32);
        var dotArc = Math.max(1, dotWidth / 6);

        for (int y = 0; y < matrix.height(); y++) {
            for (int x = 0; x < matrix.width(); x++) {
                var color = matrix.get(x, y);
                if (color != null) {
                    var rect = new RoundRectangle2D.Double(
                            xOfs + x * dotWidth, yOfs + y * dotWidth, dotWidth, dotWidth, dotArc, dotArc);
                    dest.setColor(color);
                    dest.fill(rect);
                    dest.setColor(Color.BLACK);
                    dest.setStroke(new BasicStroke(dotSpace));
                    dest.draw(rect);
                }
            }
        }
    }
}
