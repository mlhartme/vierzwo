package de.schmizzolin.vierzwo;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

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

    public static Font create() {
        Font font = new Font();
        font.addResource("basic");

        //-- https://listed.to/@DieSieben/7851/api-des-deutschen-wetterdienstes at the end
        // iday pictogramm
        // https://content.meteoblue.com/de/forschung-bildung/spezifikationen/standards/symbole-und-piktogramme
        font.addResource("weather");
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
            result.add(Color.valueOf(name));
        }
        while (result.size() < 3) {
            result.add(result.get(result.size() - 1));
        }
        return result;
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
