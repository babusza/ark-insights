/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arkinsights;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBoxBuilder;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 *
 * @author ark-insingths
 */
public class IP2sMediaPlayer extends javax.swing.JFrame implements MediaPlayerEventListener {

    private static final long serialVersionUID = 1L;

    /**
     * Creates new form ArkPlayer
     */
    private javax.swing.JButton btBackWard;
    private javax.swing.JButton btDefault;
    private javax.swing.JButton btForward;
    private javax.swing.JButton btPlay;
    private javax.swing.JButton btStop;
    private static javax.swing.JLabel lbCurrentTime;
    private javax.swing.JLabel lbPlaing;
    private javax.swing.JLabel lbSpeeds;
    private static javax.swing.JLabel lbTotalTime;
    private javax.swing.JLabel lbNote;
    public static Slider slider;
    static long totalTime = 0;
    public static EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private final JFrame fMedia;

    public IP2sMediaPlayer(Decryption d) {
        initComponents();
        setLocationRelativeTo(null);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                mediaPlayerComponent.getMediaPlayer().release();
                d.btPlay.setEnabled(true);
            }
        });

        loadLib();
        fMedia = new JFrame();
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(this);
        fMedia.setContentPane(mediaPlayerComponent);
        fMedia.setVisible(true);
        fMedia.setVisible(false);

    }

    private void loadLib() {
    //    String path = "C:\\Users\\Administrator\\Documents\\ArkPlayer\\lib\\VLC";
        String path = getClass().getResource("Decryption.class").toString();
        path = path.replaceAll("%20", " ");
        path = path.substring(9, path.indexOf("Ark-Insights Recording Player(off-site)")) + "Ark-Insights Recording Player(off-site)/app/lib/VLC/";      
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), path);
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
    }

    void play(String fileName, String path, float rate, String trackID) {
        this.setVisible(true);
        lbPlaing.setText("Now Playing: " + trackID);
        System.out.println("path : " + path);
        mediaPlayerComponent.getMediaPlayer().setRate(rate);
        mediaPlayerComponent.getMediaPlayer().playMedia(path);
        showRate(1.0f);
        slider.setValue(slider.getMin());

    }

    void close() {
        this.setVisible(false);
        mediaPlayerComponent.getMediaPlayer().release();
    }

    public static String formatTime(long millis) {
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return hms;
    }

    private void initComponents() {
        Font fLabel = new java.awt.Font("Tahoma", 0, 14);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
        this.getContentPane().setBackground(new Color(204, 255, 255));
        // setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ark-Insights Recording Player");
        setAlwaysOnTop(true);
        setMinimumSize(new java.awt.Dimension(500, 210));
        setResizable(false);

        lbPlaing = new javax.swing.JLabel();
        btBackWard = new javax.swing.JButton();
        btDefault = new javax.swing.JButton();
        btForward = new javax.swing.JButton();
        lbCurrentTime = new javax.swing.JLabel();
        btPlay = new javax.swing.JButton();
        lbTotalTime = new javax.swing.JLabel();
        btStop = new javax.swing.JButton();
        lbNote = new javax.swing.JLabel();
        lbSpeeds = new javax.swing.JLabel();

        lbPlaing.setFont(fLabel); // NOI18N
        lbPlaing.setText("Now Playing : ");
        lbPlaing.setBounds(20, 10, 400, 20);
        this.add(lbPlaing);

        lbCurrentTime.setFont(fLabel); // NOI18N
        lbCurrentTime.setText("Current Time");
        lbCurrentTime.setBounds(20, 15, 200, 80);
        this.add(lbCurrentTime);

        lbTotalTime.setFont(fLabel); // NOI18N
        lbTotalTime.setText("Total Time");
        lbTotalTime.setBounds(200, 15, 200, 80);
        this.add(lbTotalTime);

        final JFXPanel fxPanel = new JFXPanel();
        fxPanel.setBounds(20, 70, 460, 20);
        this.add(fxPanel);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
        });

//        slider = new JSlider();
//        slider.setBounds(20, 70, 460, 15);
//        this.add(slider);
//
//        slider.addMouseListener(new Handlerclass());
//        slider.addMouseMotionListener(new mouseMotion());
        btBackWard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/arkinsights/backward.png"))); // NOI18N      
        btBackWard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBackWardActionPerformed(evt);
            }
        });
        btBackWard.setBounds(20, 100, 40, 40);
        this.add(btBackWard);

        btDefault.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/arkinsights/default.png"))); // NOI18N       
        btDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDefaultActionPerformed(evt);
            }
        });
        btDefault.setBounds(65, 100, 40, 40);
        this.add(btDefault);

        btForward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/arkinsights/forward.png"))); // NOI18N
        btForward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btForwardActionPerformed(evt);
            }
        });
        btForward.setBounds(110, 100, 40, 40);
        this.add(btForward);

        lbSpeeds.setFont(fLabel);
        lbSpeeds.setText("Speeds : ");
        lbSpeeds.setBounds(160, 100, 100, 40);
        this.add(lbSpeeds);

        btPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/arkinsights/pause.png"))); // NOI18N      
        btPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btPlayActionPerformed(evt);
            }
        });
        btPlay.setBounds(435, 100, 40, 40);
        this.add(btPlay);

        btStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/arkinsights/stop.png"))); // NOI18N
        btStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btStopActionPerformed(evt);
            }
        });
        btStop.setBounds(390, 100, 40, 40);
        this.add(btStop);

        lbNote.setFont(fLabel); // NOI18N
        lbNote.setText("Speeds min=0.5 max=2.5");
        lbNote.setBounds(20, 140, 200, 50);
        this.add(lbNote);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pack();
    }

    private static void initFX(JFXPanel fxPanel) {
        // This method is invoked on the JavaFX thread
        Scene scene = createScene();
        fxPanel.setScene(scene);
    }

    private static Scene createScene() {
        final StackPane layout = new StackPane();
        slider = new Slider();
        layout.setId("slider");
        layout.setStyle("-fx-font-size: 15; "
                + "-fx-padding: 15; "
                + "-fx-alignment: center; "
                + "-fx-font-color: #333333; "
                + "-fx-background-color:  #CCFFFF;");
        layout.getChildren().addAll(
                VBoxBuilder.create().spacing(10).alignment(Pos.TOP_LEFT).children(
                        slider
                ).build()
        );
        slider.setOnMouseDragged(new EventHandler() {
            @Override
            public void handle(Event t) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mediaPlayerComponent.getMediaPlayer().setTime((long) slider.getValue());
                        lbCurrentTime.setText("Current Time : " + formatTime(mediaPlayerComponent.getMediaPlayer().getTime()));
                    }
                });
            }
        });
        slider.setOnMousePressed(new EventHandler() {
            @Override
            public void handle(Event t) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mediaPlayerComponent.getMediaPlayer().setTime((long) slider.getValue());
                        lbCurrentTime.setText("Current Time : " + formatTime(mediaPlayerComponent.getMediaPlayer().getTime()));
                    }
                });
            }
        });
        return new Scene(layout, 50, 350);
    }

    private void btForwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btForwardActionPerformed
        // TODO add your handling code here:
        float rate = mediaPlayerComponent.getMediaPlayer().getRate();
        rate += 0.1f;
        if (rate >= 2.5f) {
            rate = 2.5f;
        }
        mediaPlayerComponent.getMediaPlayer().setRate(rate);
        showRate(rate);
    }

    private void btBackWardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBackWardActionPerformed
        // TODO add your handling code here:
        float rate = mediaPlayerComponent.getMediaPlayer().getRate();
        rate -= 0.1f;
        if (rate <= 0.5f) {
            rate = 0.5f;
        }
        mediaPlayerComponent.getMediaPlayer().setRate(rate);
        showRate(rate);
    }

    private void btDefaultActionPerformed(java.awt.event.ActionEvent evt) {
        mediaPlayerComponent.getMediaPlayer().setRate(1.0f);
        showRate(1.00f);
    }

    private void btPlayActionPerformed(java.awt.event.ActionEvent evt) {
        if (mediaPlayerComponent.getMediaPlayer().isPlaying()) {
            btPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/arkinsights/play.png")));
            mediaPlayerComponent.getMediaPlayer().pause();
        } else {
            btPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/arkinsights/pause.png")));
            long curTime = (long) slider.getValue();
            if (curTime == 0) {
                mediaPlayerComponent.getMediaPlayer().play();
            } else {
//                mediaPlayerComponent.getMediaPlayer().mute(true);
//                mediaPlayerComponent.getMediaPlayer().play();
//                try {
//                    Thread.sleep(3000);
//                } catch (Exception e) {
//                }
//                mediaPlayerComponent.getMediaPlayer().mute(false);
                System.out.println("time : " + mediaPlayerComponent.getMediaPlayer().getTime() + "   length" + mediaPlayerComponent.getMediaPlayer().getLength());
                mediaPlayerComponent.getMediaPlayer().setTime(curTime);
                mediaPlayerComponent.getMediaPlayer().play();
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                     //   slider.setValue(curTime);
//                    }
//                });

            }
        }
    }

    public static void setTotalTime(long newTime) {
        long newTotal = TimeUnit.MILLISECONDS.toSeconds(newTime);
        if (totalTime < newTotal) {
            lbTotalTime.setText("Total Time : " + formatTime(newTime));
            totalTime = newTotal;
        }
    }

    private void btStopActionPerformed(java.awt.event.ActionEvent evt) {
        mediaPlayerComponent.getMediaPlayer().stop();
        btPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/arkinsights/play.png")));
        lbCurrentTime.setText("Current Time : " + formatTime(0));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                slider.setValue(slider.getMin());
            }
        });
    }

    private void showRate(float rate) {
        lbSpeeds.setText("Speeds : " + String.format("%.01f", rate));
    }

    @Override
    public void mediaChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, libvlc_media_t l, String string) {
        // System.out.println("mediaChanged");
    }

    @Override
    public void opening(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        //   System.out.println("opening");
    }

    @Override
    public void buffering(uk.co.caprica.vlcj.player.MediaPlayer mp, float f) {
//        System.out.println("buffering");
    }

    @Override
    public void playing(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        //   System.out.println("playing");       
    }

    @Override
    public void paused(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        //   System.out.println("paused");
    }

    @Override
    public void stopped(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        //   System.out.println("stopped");
        mediaPlayerComponent.getMediaPlayer().play();
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        mediaPlayerComponent.getMediaPlayer().pause();
        while (mediaPlayerComponent.getMediaPlayer().isPlaying()) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
        }

    }

    @Override
    public void forward(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        //    System.out.println("forward");
    }

    @Override
    public void backward(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        //   System.out.println("backward");
    }

    @Override
    public void finished(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        System.out.println("finished");
        btPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/arkinsights/play.png")));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                slider.setValue(slider.getMax());
                slider.setValue(slider.getMin());
            }
        });
        mediaPlayerComponent.getMediaPlayer().stop();
        lbCurrentTime.setText("Current Time : " + formatTime(0));
    }

    @Override
    public void timeChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, long l) {
        System.out.println("timeChange : " + mediaPlayerComponent.getMediaPlayer().getTime());
        lbCurrentTime.setText("Current Time : " + formatTime(mediaPlayerComponent.getMediaPlayer().getTime()));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                slider.setValue(mediaPlayerComponent.getMediaPlayer().getTime());
            }
        });
    }

    @Override
    public void positionChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, float f) {
        //     System.out.println("positionChanged");
    }

    @Override
    public void seekableChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, int i) {
        //    System.out.println("seekableChanged");
    }

    @Override
    public void pausableChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, int i) {
        //     System.out.println("pausableChanged");
    }

    @Override
    public void titleChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, int i) {
        //    System.out.println("titleChanged");
    }

    @Override
    public void snapshotTaken(uk.co.caprica.vlcj.player.MediaPlayer mp, String string) {
        //     System.out.println("snapshotTaken");
    }

    @Override
    public void lengthChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, long l) {
        System.out.println("lengthChanged");
        setTotalTime(l);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //  long max = TimeUnit.MILLISECONDS.toSeconds(mediaPlayerComponent.getMediaPlayer().getLength());
                //  System.out.println("old max : "+slider.getMax()+"  new max : "+max);
//                if (max > slider.getMax()) {
//               //     System.out.println("setMax");
//                    slider.setMax(max);

                if (slider.getMax() < mediaPlayerComponent.getMediaPlayer().getLength()) {
                    slider.setMax(mediaPlayerComponent.getMediaPlayer().getLength());
                }
            }
        });
//        if (!mediaPlayerComponent.getMediaPlayer().isPlaying()) {
//            lbTotalTime.setText("Total Time : " + formatTime(0));
//        }
    }

    @Override
    public void videoOutput(uk.co.caprica.vlcj.player.MediaPlayer mp, int i) {
        //       System.out.println("videoOutput");
    }

    @Override
    public void error(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        //      System.out.println("error");
    }

    @Override
    public void mediaMetaChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, int i) {
        //    System.out.println("mediaMetaChanged");
    }

    @Override
    public void mediaSubItemAdded(uk.co.caprica.vlcj.player.MediaPlayer mp, libvlc_media_t l) {
        //     System.out.println("mediaSubItemAdded");
    }

    @Override
    public void mediaDurationChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, long l) {
//        slider.setValue((int) mediaPlayerComponent.getMediaPlayer().getTime());
    }

    @Override
    public void mediaParsedChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, int i) {
        //    System.out.println("mediaParsedChanged");
    }

    @Override
    public void mediaFreed(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        //    System.out.println("mediaFreed");
    }

    @Override
    public void mediaStateChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, int i) {
        //     System.out.println("mediaStateChanged");
    }

    @Override
    public void newMedia(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        //    System.out.println("newMedia");
    }

    @Override
    public void subItemPlayed(uk.co.caprica.vlcj.player.MediaPlayer mp, int i) {
        //    System.out.println("subItemPlayed");
    }

    @Override
    public void subItemFinished(uk.co.caprica.vlcj.player.MediaPlayer mp, int i) {
        //   System.out.println("subItemFinished");
    }

    @Override
    public void endOfSubItems(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        //   System.out.println("endOfSubItems");
    }

//    public class Handlerclass implements MouseListener {
//
//        @Override
//        public void mousePressed(MouseEvent e) {
//            System.out.println("mousePressed");
//            //    mediaPlayerComponent.getMediaPlayer().setTime();
//        }
//
//        @Override
//        public void mouseReleased(MouseEvent e) {
//            //  System.out.println("mouseReleased");
//            //   mediaPlayerComponent.getMediaPlayer().setTime(slider.getValue());
//        }
//
//        @Override
//        public void mouseEntered(MouseEvent e) {
//            //   System.out.println("mouseEntered");
//            //   mediaPlayerComponent.getMediaPlayer().setTime(slider.getValue());
//        }
//
//        @Override
//        public void mouseExited(MouseEvent e) {
//            //   System.out.println("mouseExited");
//            //  mediaPlayerComponent.getMediaPlayer().setTime(slider.getValue());
//        }
//
//        @Override
//        public void mouseClicked(MouseEvent e) {
//            // System.out.println("mouseClicked");
////            int time = slider.getValue();
////            mediaPlayerComponent.getMediaPlayer().setTime(time);
//        }
//
//    }
//    public class mouseMotion implements MouseMotionListener {
//
//        @Override
//        public void mouseDragged(MouseEvent e) {
////            mediaPlayerComponent.getMediaPlayer().setTime(slider.getValue());
//        }
//
//        @Override
//        public void mouseMoved(MouseEvent me) {
//
//        }
//
//    }
}
