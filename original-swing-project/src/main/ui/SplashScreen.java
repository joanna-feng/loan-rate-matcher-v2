package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/*
 * Represents the splash screen displayed before the main application starts.
 */
public class SplashScreen extends JWindow {
    private static final int DISPLAY_TIME = 3000; // 3 seconds
    private static final String IMAGE_PATH = "data/splash.png"; // Path to splash image

    /*
     * EFFECTS: Displays the splash screen and waits before closing.
     */
    public SplashScreen() {
        try {
            Image image = getImage();

            // Set image to a JLabel
            JLabel splashLabel = new JLabel(new ImageIcon(image));
            getContentPane().add(splashLabel, BorderLayout.CENTER);

            // Adjust window size based on image
            setSize(image.getWidth(null), image.getHeight(null));
            setLocationRelativeTo(null); // Center on screen

            // Make visible
            setVisible(true);
            repaint();

            // Use a Swing Timer to close splash after DISPLAY_TIME
            Timer timer = new Timer(DISPLAY_TIME, e -> {
                setVisible(false);
                dispose();
                launchMainApp(); // Start main application after splash
            });
            timer.setRepeats(false);
            timer.start();

        } catch (IOException e) {
            System.out.println("Error loading splash image: " + e.getMessage());
        }
    }

    /**
     * EFFECTS: Loads and returns the splash image from the file system using the
     * predefined IMAGE_PATH.
     * Throws an IOException if the image file does not exist or cannot be read
     * properly.
     */

    private Image getImage() throws IOException {
        // Load the splash image
        File imageFile = new File(IMAGE_PATH);
        if (!imageFile.exists()) {
            throw new IOException("Splash image file not found: " + IMAGE_PATH);
        }
        Image image = ImageIO.read(imageFile);
        if (image == null) {
            throw new IOException("Failed to load splash image.");
        }
        return image;
    }

    /*
     * EFFECTS: Launches the main application after splash.
     */
    private void launchMainApp() {
        SwingUtilities.invokeLater(() -> new LoanRateMatcherGUI(true));
    }

    /*
     * EFFECTS: Main method to test splash screen independently.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SplashScreen::new);
    }
}