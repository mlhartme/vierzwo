package de.schmizzolin.vierzwo;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main {
    private static final Dimension WINDOW_SIZE = new Dimension(360, 120);

    private final JFrame frame;
    private final Rectangle screenBounds;
    private Rectangle windowedBounds;
    private boolean fullScreen;

    private Main() {
        frame = new JFrame("Vierzwo");
        screenBounds = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration().getBounds();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.setUndecorated(true);
        var content = new Vierzwo();
        frame.setContentPane(content);
        frame.setSize(WINDOW_SIZE);
        frame.setLocationRelativeTo(null);

        contextMenu(content);
        movable(content);

        frame.setVisible(true);
        switchFullScreen();
    }

    private void contextMenu(JPanel content) {
        var menu = new JPopupMenu();
        var title = new JMenuItem("Vierzwo");
        title.setEnabled(false);
        menu.add(title);
        menu.addSeparator();

        var fullscreen = new JMenuItem("Fullscreen");
        fullscreen.addActionListener(e -> switchFullScreen());
        menu.add(fullscreen);

        var quit = new JMenuItem("Quit");
        quit.addActionListener(e -> frame.dispose());
        menu.add(quit);

        content.setComponentPopupMenu(menu);
        content.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    switchFullScreen();
                }
            }
        });
    }

    private void movable(JPanel content) {
        var dragOffset = new int[2];
        content.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragOffset[0] = e.getXOnScreen() - frame.getX();
                dragOffset[1] = e.getYOnScreen() - frame.getY();
            }
        });
        content.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!fullScreen) {
                    frame.setLocation(e.getXOnScreen() - dragOffset[0], e.getYOnScreen() - dragOffset[1]);
                }
            }
        });
    }

    private void switchFullScreen() {
        fullScreen = !fullScreen;
        if (fullScreen) {
            windowedBounds = frame.getBounds();
            frame.setBounds(screenBounds);
        } else {
            frame.setBounds(windowedBounds);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
