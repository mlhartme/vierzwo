package de.schmizzolin.vierzwo;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

// Beispiel für eine eigene Pane mit automatischer Aktualisierung
public class Text extends JPanel {
    private Font font;
    private String[] text;

    public Text() {
        this.font = Font.create("font");
        this.text = new String[0];
        setBackground(Color.BLACK);
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    protected void setText(String... text) {
        this.text = text;
        update();
    }

    protected void update() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (text.length == 0) {
            return;
        }

        var insets = getInsets();
        var width = getWidth() - insets.left - insets.right;
        var height = getHeight() - insets.top - insets.bottom;
        var xDots = font.width(text);
        var yDots = font.height(text);
        var dotSize = Math.max(1, Math.min(width / xDots, height / yDots));
        var x = insets.left + (width - xDots * dotSize) / 2;
        var y = insets.top + (height - yDots * dotSize) / 2;
        var copy = (Graphics2D) graphics.create();
        try {
            font.render(copy, dotSize, x, y, text);
        } finally {
            copy.dispose();
        }
    }
}
