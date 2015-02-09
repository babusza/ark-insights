/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ip2s.mediaplayer;

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
    FrmMediaPlayer filter;

    IP2sMediaPlayer(FrmMediaPlayer filter) {
        this.filter = filter;
        setAlwaysOnTop(true);
        icon = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("icon.png"));
        setLocationRelativeTo(this);
        System.out.println("create IP2sMediaPlayer");
    }
    // static Scene scene;
    public static JFrame frame;

    public void play(String fileName, String path, double rate, String trackID) throws Exception {

        this.path = path;
        initAndShowGUI(fileName, path, rate, trackID, filter);

    }

    public void setOntop(boolean b) {
        if (frame != null) {
            frame.setAlwaysOnTop(b);
        }
    }

    public void close() {
        try {
            this.filter.curTrack = "";
            sg.player.dispose();
            frame.setVisible(false);
        } catch (Exception e) {
            System.out.println("Error close player " + e.getMessage());
        }
//        this.dispose();
    }

    private static void initAndShowGUI(final String fileName, final String path, double defaultRate, String trackID, FrmMediaPlayer filter) {
        // This method is invoked on Swing thread   
        // final JFrame frame = new JFrame("Ark-Insights Recording Player");

        frame = new JFrame("Ark-Insights Recording Player");
        frame.setAlwaysOnTop(true);
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
                filter.curTrack = "";
            }
        });
        frame.setLocationRelativeTo(frame);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("bf create sene");
                Scene scene = sg.createScene(fileName, path, defaultRate, trackID);
                System.out.println("create seen");
                fxPanel.setScene(scene);
            }
        });
        frame.setVisible(true);
    }

}
