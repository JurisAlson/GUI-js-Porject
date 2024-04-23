public class App {
    public static void main(String[] args) {
        // Create and show the main frame
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainFrame frame = new MainFrame();
            }
        });
    }
}
