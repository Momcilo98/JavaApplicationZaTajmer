
package javaapplicationzatajmer;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class JavaApplicationZaTajmer {
    static Timer timer;
    static int totalSeconds = 0;
    static int elapsedSeconds = 0;
    static boolean isRunning = false;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Timer");
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JButton resetButton = new JButton("Reset");
        JLabel timeLabel = new JLabel("Vreme: 00:00 min");
        JTextField timeInput = new JTextField(10);
        JLabel inputLabel = new JLabel("Unesite vreme u formatu mm:ss ili ss:");

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = timeInput.getText();
                if (parseInputTime(input)) {
                    elapsedSeconds = 0;
                    startTimer(timeLabel);
                } else {
                    JOptionPane.showMessageDialog(frame, "Unesite ispravno vreme u formatu: mm:ss ili ss.");
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopTimer();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetTimer(timeLabel);
            }
        });

        frame.setLayout(new java.awt.FlowLayout());
        frame.add(inputLabel);
        frame.add(timeInput);
        frame.add(timeLabel);
        frame.add(startButton);
        frame.add(stopButton);
        frame.add(resetButton);
        frame.setSize(400, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static boolean parseInputTime(String input) {
        try {
            if (input.contains(":")) {
                String[] parts = input.split(":");
                int minutes = Integer.parseInt(parts[0]);
                int seconds = Integer.parseInt(parts[1]);
                totalSeconds = minutes * 60 + seconds;
            } else {
                totalSeconds = Integer.parseInt(input);
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static void startTimer(JLabel timeLabel) {
        if (!isRunning) {
            isRunning = true;
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    elapsedSeconds++;
                    int minutes = elapsedSeconds / 60;
                    int seconds = elapsedSeconds % 60;
                    timeLabel.setText(String.format("Vreme: %02d:%02d min", minutes, seconds));

                    if (elapsedSeconds >= totalSeconds) {
                        playSound("C:\\models\\logo-corporate-piano-152476.mp3");
                        stopTimer();
                        resetTimer(timeLabel);
                    }
                }
            }, 1000, 1000);
        }
    }

    private static void stopTimer() {
        if (isRunning) {
            isRunning = false;
            if (timer != null) {
                timer.cancel();
            }
        }
    }

    private static void resetTimer(JLabel timeLabel) {
        stopTimer();
        elapsedSeconds = 0;
        timeLabel.setText("Vreme: 00:00 min");
    }

    private static void playSound(String soundFile) {
        try {
            FileInputStream fis = new FileInputStream(soundFile);
            Player playMP3 = new Player(fis);
            playMP3.play();
        } catch (JavaLayerException | IOException e) {
            e.printStackTrace();
        }
    }
}
