import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainFrame extends JFrame {
    private JTextField[] gradeFields;
    private JButton submitButton;
    private JLabel resultLabel;
    private String[] subjects;

    public MainFrame() {
        initialize();
    }

    private void initialize() {
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    // Load the background image
                    Image backgroundImage = ImageIO.read(new File("background.jpg"));
                    // Draw the background image
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        mainPanel.setLayout(new GridLayout(10, 2, 10, 10));

        // Define subjects
        subjects = new String[]{
            "COE110", "CPA102", "CPP105", "CPP106",
            "ETH101", "HIS101", "ITE101", "PED104"
        };
        gradeFields = new JTextField[subjects.length];

        // Font and size for subject labels
        Font labelFont = new Font("Arial", Font.BOLD, 24);

        // Add labels and text fields for subjects and grades
        for (int i = 0; i < subjects.length; i++) {
            JLabel label = new JLabel(subjects[i]);
            label.setFont(labelFont);
            mainPanel.add(label);

            JTextField textField = new JTextField(10);
            textField.setPreferredSize(new Dimension(150, 30));
            textField.setFont(labelFont);
            gradeFields[i] = textField;
            mainPanel.add(textField);
        }

        // Create and add submit button
        submitButton = new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(120, 30));
        submitButton.setFont(labelFont);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateGrades()) {
                    calculateGWA();
                }
            }
        });
        mainPanel.add(submitButton);

        // Create label for displaying result
        resultLabel = new JLabel("GWA: ");
        resultLabel.setFont(labelFont);
        mainPanel.add(resultLabel);

        // Set panel border and properties
        int topPadding = 50;
        mainPanel.setBorder(new EmptyBorder(topPadding, 10, 10, 10));
        setTitle("Grading System");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(mainPanel);
        pack();
        setVisible(true);
    }

    private boolean validateGrades() {
        for (JTextField field : gradeFields) {
            String gradeText = field.getText().trim();
            if (!gradeText.isEmpty()) {
                try {
                    double grade = Double.parseDouble(gradeText);
                    if (grade < 1.0 || grade > 5.0) {
                        JOptionPane.showMessageDialog(this, "Invalid grade input. Grades must be between 1.0 and 5.0.", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid grade input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        return true;
    }

    private void calculateGWA() {
        double totalCredits = 0.0;
        double totalGradePoints = 0.0;
        double creditsPerSubject = 3.0;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < subjects.length; i++) {
            String gradeText = gradeFields[i].getText().trim();
            if (!gradeText.isEmpty()) {
                double grade = Double.parseDouble(gradeText);
                totalGradePoints += grade * creditsPerSubject;
                totalCredits += creditsPerSubject;
                sb.append(subjects[i]).append(": ").append(gradeText).append("\n");
            }
        }

        if (totalCredits > 0) {
            double gwa = totalGradePoints / totalCredits;
            resultLabel.setText(String.format("GWA: %.2f", gwa));
            sb.append(gwa);
            saveDataToFile(sb.toString());
        } else {
            JOptionPane.showMessageDialog(this, "No valid grades entered. Please enter grades.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveDataToFile(String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("grade_records.txt"))) {
            writer.write(data);
            JOptionPane.showMessageDialog(this, "Records saved to grade_records.txt", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving records to file.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
    }
}
