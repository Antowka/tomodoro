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
        play(soundDong, "/sounds/dong.wav");
    }

    /**
     * Play sirena sound
     */
    public void  playSirena() {
        play(soundSirena, "/sounds/sirena.wav");
    }

    /**
     * Play any player
     *
     * @param player
     * @param path
     */
    private void play(MediaPlayer player, String path) {

        //play only if not playing
        if(player == null || !player.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            player = loadSound(path);
            Toolkit.getDefaultToolkit().beep();
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.play();
        }
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
