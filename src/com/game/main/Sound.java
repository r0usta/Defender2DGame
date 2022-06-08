package com.game.main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {
    Clip clip; // To open an audio file
    URL[] soundURL; // Used to store paths to audio files

    public Sound() {
        soundURL = new URL[6];
        soundURL[0] = getClass().getResource("/assets/sound/Game.wav");
        soundURL[1] = getClass().getResource("/assets/sound/Game Over.wav");
        soundURL[2] = getClass().getResource("/assets/sound/Hit monster.wav");
        soundURL[3] = getClass().getResource("/assets/sound/Pick up object.wav");
        soundURL[4] = getClass().getResource("/assets/sound/Receive damage.wav");
        soundURL[5] = getClass().getResource("/assets/sound/Swing weapon.wav");

    }

    /**
     * Format for opening an audio file
     * @param index soundURL array
     */
    public void setFile(int index) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[index]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }
}
