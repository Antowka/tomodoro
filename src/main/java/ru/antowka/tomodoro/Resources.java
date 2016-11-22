package ru.antowka.tomodoro;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.awt.*;
import java.net.URL;

/**
 * Sound, images, i-net and other resources
 */
public class Resources {

    private static Resources instance;
    private MediaPlayer soundDong;

    /**
     * For singleton
     *
     * @return
     */
    public static Resources getInstance() {

        if(instance == null) {
            instance = new Resources();
            instance.initDongSound();
        }

        return instance;
    }

    /**
     * Init dong sound
     */
    private void initDongSound() {
        final URL resource = getClass().getResource("/sounds/dong.mp3");
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
