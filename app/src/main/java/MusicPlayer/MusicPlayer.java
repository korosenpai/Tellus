package MusicPlayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.stream.Stream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import Utils.Debug;

public class MusicPlayer {

    // TODO: load all clips beforehand?
    // then clone clips before playing

    static final String rootPath = "src/main/assets/audio/";
    static Hashtable<String, Clip> loaded = new Hashtable<>(); // filename, loaded audio

    // load all audio files in audio/
    // NOTE: should only be called once at startup
    public static void loadAll() {
        try {
            Stream<Path> paths = Files.walk(Paths.get(rootPath));
            paths.filter(Files::isRegularFile).forEach(MusicPlayer::load);
            paths.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    // NOTE: should only be called by loadAll
    public static boolean load(Path filepath) {
        String sfilePath = filepath.toString();

        // do not load clip twice
        if (loaded.get(sfilePath) != null) return true;

        try {
            File audioFile = new File(sfilePath);

            if (!audioFile.exists())
                throw new RuntimeException("cant find file :(");

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);

            // set audio lower
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-10f); // decibels

            loaded.put(sfilePath, clip);
            Debug.system("loaded: " + filepath);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    public static void playFile(String filePath) {
        filePath = rootPath + filePath;
        Clip clip = loaded.get(filePath);

        // if clip is running just reload the clip and it will overlap the audios
        // TODO: clone loaded clips
        if (clip == null || clip.isRunning()) {
        try {
                File audioFile = new File(filePath);

                if (!audioFile.exists())
                    throw new RuntimeException("cant find '" + filePath + "':(");

                AudioInputStream audioInput = AudioSystem.getAudioInputStream(audioFile);
                clip = AudioSystem.getClip();
                clip.open(audioInput);

                // set audio lower
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-10f); // decibels

                loaded.put(filePath, clip);
                Debug.system("loaded: " + filePath);


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
            Debug.system("closed audio: " + file);
        }
    }

}

// public class MyClip implements Cloneable {
//     private Clip dummy;
// 
//     public MyClip() {
//     }
// 
//     public Clip get() {
//         return dummy;
//     }
// 
//     public void set(Clip c) {
//         this.dummy = c;
//     }
// 
//     @Override
//     protected Object clone() throws CloneNotSupportedException {
//         MyClip clone = (MyClip) super.clone();
//         if (dummy != null) {
//             try {
//                 // Create a new AudioInputStream from the original file
//                 URL url = getClass().getResource("/Resources/sound/Laser.wav");
//                 AudioInputStream inputStream = AudioSystem.getAudioInputStream(url);
//                 // Open the new Clip with the cloned AudioInputStream
//                 clone.dummy = AudioSystem.getClip();
//                 clone.dummy.open(inputStream);
//             } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
//                 e.printStackTrace();
//             }
//         }
//         return clone;
//     }
// }
