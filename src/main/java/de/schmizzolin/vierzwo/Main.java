package de.schmizzolin.vierzwo;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main {
    private static final Dimension WINDOW_SIZE = new Dimension(360, 120);

    private final JFrame frame;
    private final GraphicsDevice screen;
    private boolean fullScreen;

    private Main() {
        frame = new JFrame("Vierzwo");
        screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.setUndecorated(true);
        frame.setContentPane(new Vierzwo());
        frame.setSize(WINDOW_SIZE);
        frame.setLocationRelativeTo(null);

        contextMenu();
        movable();

        frame.setVisible(true);
        switchFullScreen();
    }

    private void contextMenu() {
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

        frame.getContentPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    switchFullScreen();
                }
            }

            private void showPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private void movable() {
        var dragOffset = new int[2];
        frame.getContentPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragOffset[0] = e.getXOnScreen() - frame.getX();
                dragOffset[1] = e.getYOnScreen() - frame.getY();
            }
        });
        frame.getContentPane().addMouseMotionListener(new MouseAdapter() {
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
            frame.setBounds(screen.getDefaultConfiguration().getBounds());
            screen.setFullScreenWindow(frame);
        } else {
            screen.setFullScreenWindow(null);
            frame.setSize(WINDOW_SIZE);
            frame.setLocationRelativeTo(null);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
