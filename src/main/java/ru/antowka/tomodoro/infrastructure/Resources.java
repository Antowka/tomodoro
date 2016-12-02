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

    /**
     * Init dong sound
     */
    private void initDongSound() {
        final URL resource = getClass().getResource("/sounds/dong.wav");
        final Media media = new Media(resource.toString());
        soundDong = new MediaPlayer(media);
    }

    /**
     * Play dong sound
     */
    public void playSoundDong() {
        Toolkit.getDefaultToolkit().beep();
        soundDong.play();
    }
}
