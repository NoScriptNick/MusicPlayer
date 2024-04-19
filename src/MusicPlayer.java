import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    //Class Variables
    private Clip clip;
    private Timer timer;
    private JLabel currentTime;
    private JLabel totalTime;
    //private variable time, used long data type because that's what it saves as
    private long time = 0;

    //constructor - how we build the music player
    public MusicPlayer(){
        //Song Options
        String [] songList = {"Dumbbells.wav", "Hey Papi.wav", "Code Kings", "Fire in My Belly", "For Loop", "GORG", "GUI Mastermind", "Hashin_ in the Code",
        "GUI Mastermind", "Mr. Scott", "Programming", "The Boolean Blues", "The Codebreaker_s Fury"};

        //Frame
        JFrame frame = new JFrame("Music Player");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        //Buttons
        JPanel buttonPanel = new JPanel();
        JButton playButton = new JButton("Play");
        JButton pauseButton = new JButton("Pause");
        buttonPanel.add(playButton);
        buttonPanel.add(pauseButton);

        //Labels
        JPanel labelPanel = new JPanel();
        currentTime = new JLabel("Current Time: 0s");
        totalTime = new JLabel("Total Time: 0s");
        labelPanel.add(currentTime);
        labelPanel.add(totalTime);

        //Dropdown
        JPanel dropDown = new JPanel();
        JComboBox<String> songSelector = new JComboBox<>(songList);
        dropDown.add(songSelector);

        //Button Function
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                //tells program what song to play
                playMusic(songSelector.getSelectedItem().toString());
                startTimer();
                updateLabels();
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){;
                pauseMusic(songSelector.getSelectedItem().toString());
                stopTimer();
            }
        });

        //Main Panel Order
        panel.add(dropDown);
        panel.add(buttonPanel);
        panel.add(labelPanel);

        //Make Frame Visible
        frame.add(panel);
        frame.setVisible(true);
    }

    public void playMusic(String selectedSong){
        try {
            File file = new File(System.getProperty("user.dir") + "\\src\\audio\\" + selectedSong);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            //sets the time of the song so it can play where it was previously paused
            clip.setMicrosecondPosition(time);
            clip.start();
        }
        catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void pauseMusic(String selectedSong){
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void startTimer(){
        timer = new Timer(1000, new ActionListener(){
            public void actionPerformed(ActionEvent e){
                updateLabels();
            }
        });
        timer.start();
    }

    public void updateLabels(){
        if (clip != null && clip.isOpen()) {
            if (clip.isRunning()) {
                currentTime.setText("Current Time: " + clip.getMicrosecondPosition()/1000000 + "s");
                totalTime.setText("Total Time: " + clip.getMicrosecondLength()/1000000 + "s");
            }
        }
    }

    public void stopTimer(){
        if (timer != null) {
            //below saves the time of the music when stopped
            time = clip.getMicrosecondPosition();
            timer.stop();
        }
    }

    public static void main(String[] args) {
        new MusicPlayer();
    }
}
