package ru.antowka.tomodoro.infrastructure;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.awt.*;
import java.net.URL;

/**
 * Sound, images, i-net and other resources
 */
public class Resources {

    private MediaPlayer soundDong;
    private MediaPlayer soundSirena;

    /**
     * Play dong sound
     */
    public void playSoundDong() {
        soundDong = loadSound("/sounds/dong.wav");
        Toolkit.getDefaultToolkit().beep();
        soundDong.setCycleCount(MediaPlayer.INDEFINITE);
        soundDong.play();
    }

    /**
     * Play sirena sound
     */
    public void  playSirena() {

        soundSirena = loadSound("/sounds/sirena.wav");
        Toolkit.getDefaultToolkit().beep();
        soundSirena.setCycleCount(MediaPlayer.INDEFINITE);
        soundSirena.play();
    }

    /**
     * Load sound by path
     *
     * @param path
     * @return
     */
    private MediaPlayer loadSound(String path) {
        final URL resource = getClass().getResource(path);
        final Media media = new Media(resource.toString());
        return new MediaPlayer(media);
    }
}
