package MusicPlayer;

import java.io.File;
import java.util.Arrays;
import java.util.Hashtable;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import Debug.Debug;

public class MusicPlayer {

    // TODO: load all clips beforehand?

    static Hashtable<String, Clip> loaded = new Hashtable<>(); // filename, loaded audio

    public static void playFile(String filepath) {
        Clip clip = loaded.get(filepath);

        // if clip is running just reload the clip and it will overlap the audios
        if (clip == null || clip.isRunning()) {
            try {
                File audioFile = new File(filepath);

                if (!audioFile.exists())
                    throw new RuntimeException("cant find file :(");

                AudioInputStream audioInput = AudioSystem.getAudioInputStream(audioFile);
                clip = AudioSystem.getClip();
                clip.open(audioInput);

                // set audio lower
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-10f); // decibels

                loaded.put(filepath, clip);
                Debug.debug("loaded: " + filepath);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        clip.setFramePosition(0);
        clip.start();

    }

    public static void closeAll() {
        // System.out.println(loaded.keySet());

        for (String file: loaded.keySet()) {
            loaded.get(file).close();
            loaded.remove(file);
            Debug.debug("closed audio: " + file);
        }
    }

}
