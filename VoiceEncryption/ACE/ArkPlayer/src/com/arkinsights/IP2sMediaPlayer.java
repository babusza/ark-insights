/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arkinsights;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.media.MediaPlayer;
import javax.swing.*;

/**
 * Example of playing all mp3 audio files in a given directory using a JavaFX
 * MediaView launched from Swing
 */
public class IP2sMediaPlayer extends JFrame {

    static Image icon;
    static SceneGenerator sg;
    static JFXPanel fxPanel;
    static String path;
    public static Decryption f;

    IP2sMediaPlayer(Decryption f) {
        this.f = f;
        icon = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("icon.png"));
    }
    // static Scene scene;
    public static JFrame frame;

    public void play(String fileName, String path, double rate, String trackID) throws Exception {
        this.path = path;
        initAndShowGUI(fileName, path, rate, trackID);
        f.btPlay.setEnabled(false);
    }

    public boolean close() {

        sg.player.dispose();
        frame.setVisible(false);
       
//        this.dispose();
        return true;
    }

    private static void initAndShowGUI(final String fileName, final String path, double defaultRate, String trackID) {
        // This method is invoked on Swing thread   
        // final JFrame frame = new JFrame("Ark-Insights Recording Player");

        frame = new JFrame("Ark-Insights Recording Player");
        // JDialog  frame = new JDialog(frame,true);
        //  this = new JFrame("Ark-Insights Recording Player"); 
        frame.setIconImage(icon);
        fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setBounds(200, 100, 500, 210);
        frame.setResizable(false);
        sg = new SceneGenerator();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                sg.stopMedia(path);
                f.btPlay.setEnabled(true);
            }
        });

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Scene scene = sg.createScene(fileName, path, defaultRate, trackID);
                fxPanel.setScene(scene);
            }
        });
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
