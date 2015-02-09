/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arkinsights;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import java.io.*;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeListener;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class SceneGenerator implements MediaPlayerEventListener {

    final Label currentlyPlaying = new Label();
    final Label currentTime = new Label();
    final Label totalTime = new Label();
    final Label lbRate = new Label();
    Slider slider = new Slider();
    final Label lbNote = new Label();

    Image imagePlay;
    Image imagePause;
    Image imageStop;
    Image imageForward;
    Image imageBackward;
    Image imageDefault;
    float pRate = 0.1f;
    String trackID;

    private EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private JFrame fMedia;
    Button stop;
    Button play;
    Button backward;
    Button ori;
    Button forward;

    public Scene createScene(String fileName, String path, float defaultRate, String trackID) {

        loadLib();
        fMedia = new JFrame();
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        fMedia.setContentPane(mediaPlayerComponent);
        fMedia.setVisible(true);
        fMedia.setVisible(false);

        play(fileName, path+fileName, 1.0f, trackID);
        currentlyPlaying.setText("Now Playing: " + trackID);
        this.trackID = trackID;
        imagePlay = new Image(getClass().getResourceAsStream("play.png"));
        imagePause = new Image(getClass().getResourceAsStream("pause.png"));
        imageStop = new Image(getClass().getResourceAsStream("stop.png"));
        imageForward = new Image(getClass().getResourceAsStream("forward.png"));
        imageBackward = new Image(getClass().getResourceAsStream("backward.png"));
        imageDefault = new Image(getClass().getResourceAsStream("default.png"));
        final StackPane layout = new StackPane();

        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            JOptionPane.showMessageDialog(new JFrame(), "Cannot find video source directory: " + dir);
            Platform.exit();
            return null;
        }

        stop = new Button("", new ImageView(imageStop));
        stop.setId("Stop");
        play = new Button("", new ImageView(imagePause));
        play.setId("Pause");
        backward = new Button("", new ImageView(imageBackward));
        backward.setId("backward");
        ori = new Button("", new ImageView(imageDefault));
        ori.setId("ori");
        forward = new Button("", new ImageView(imageForward));
        forward.setId("forward");
        lbNote.setText("Speeds min 0.5   max 2.5");

//        player.setOnEndOfMedia(new Runnable() {
//            @Override
//            public void run() {
//                if (player.getStartTime() != Duration.ZERO) {
//                    player.setStartTime(Duration.ZERO);
//                    double t = player.getTotalDuration().toSeconds() + player.getStartTime().toSeconds();
//                    slider.setMax(t);
//                }
//                slider.setValue(slider.getMin());
//                player.seek(Duration.ZERO);
//                play.setId("Play");
//                play.setGraphic(new ImageView(imagePlay));
//                player.pause();
//                //      player.setStartTime(new Duration(0.0));
//            }
//        });
//        slider.setOnMouseDragged(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//
//                Duration dTime = Duration.seconds(slider.getValue());
//                //  player = createPlayer(("file:///" + (dir + "\\" + fileName).replace("\\", "/").replaceAll(" ", "%20")));
//                player.seek(dTime);
//                double d = slider.getValue();
//                String time = getTimeFormat(d);
//              
        
//
//            }
//        });
//        slider.setOnMousePressed(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                Duration dTime = Duration.seconds(slider.getValue());
//                double d = slider.getValue();
//                String time = getTimeFormat(d);
//                currentTime.setText("Current Time : " + time);
//                //        player.setStartTime(dTime);
//
//                if (player.getStartTime() != Duration.ZERO) {
//                    player.setStartTime(Duration.ZERO);
//                    double t = player.getTotalDuration().toSeconds() + player.getStartTime().toSeconds();
//                    slider.setMax(t);
//                }
//                player.seek(dTime);
//
//            }
//        });
//
        stop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mediaPlayerComponent.getMediaPlayer().stop();
                play.setId("Play");
                play.setGraphic(new ImageView(imagePlay));
                slider.setValue(0.0);
            }
        });
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if ("Pause".equals(play.getId())) {
                    play.setId("Play");
                    play.setGraphic(new ImageView(imagePlay));
                    mediaPlayerComponent.getMediaPlayer().pause();
                } else {
                    play.setId("Pause");
                    play.setGraphic(new ImageView(imagePause));
                    // Duration dTime = Duration.seconds(slider.getValue());
                    // player.setStartTime(dTime);                    
                    //  player.setStartTime(dTime);
                    mediaPlayerComponent.getMediaPlayer().play();
                }
            }
        });

        forward.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                float cRate = mediaPlayerComponent.getMediaPlayer().getRate();
                float nRate = cRate + pRate;
                if (nRate < 0.0f) {
                    nRate = 1.0f;
                } else if (nRate > 2.5f) {
                    nRate = 2.5f;
                }
                mediaPlayerComponent.getMediaPlayer().setRate(nRate);
                showRate(nRate);
            }
        });

        backward.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                float cRate = mediaPlayerComponent.getMediaPlayer().getRate();
                float nRate = cRate - pRate;
                if (nRate <= 0.5f) {
                    nRate = 0.5f;
                }
                mediaPlayerComponent.getMediaPlayer().setRate(nRate);
                showRate(nRate);

            }
        });

        ori.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mediaPlayerComponent.getMediaPlayer().setRate(1.0f);
                showRate(1.0f);
            }
        });
        slider.setBackground(Background.EMPTY);
        lbRate.setText("Speeds : " + defaultRate);
        
        setCurrentlyPlaying(mediaPlayerComponent.getMediaPlayer());
        mediaPlayerComponent.getMediaPlayer().setRate(defaultRate);
        Button invisiblePause = new Button("Pause");
        invisiblePause.setVisible(false);
        play.prefHeightProperty().bind(invisiblePause.heightProperty());
        play.prefWidthProperty().bind(invisiblePause.widthProperty());
        layout.setId("mediaplayer");
        layout.setStyle("-fx-font-size: 15; "
                + "-fx-padding: 15; "
                + "-fx-alignment: center; "
                + "-fx-font-color: #333333; "
                + "-fx-background-color:  #CCFFFF;");
        BorderPane borderPane = new BorderPane();
        HBox hbox = HBoxBuilder.create().spacing(10).alignment(Pos.BOTTOM_LEFT).children(backward, ori, forward, lbRate).build();
        borderPane.setLeft(hbox);
        HBox hbox2 = HBoxBuilder.create().spacing(10).alignment(Pos.BOTTOM_LEFT).children(stop, play).build();
        borderPane.setRight(hbox2);  
        layout.getChildren().addAll(
                invisiblePause,
                VBoxBuilder.create().spacing(10).alignment(Pos.TOP_LEFT).children(
                        currentlyPlaying,
                        slider,
                        HBoxBuilder.create().spacing(10).alignment(Pos.CENTER).children(currentTime, totalTime).build(),
                        borderPane, lbNote
                ).build()
        );
        return new Scene(layout, 400, 350);
    }
    ChangeListener progressChangeListener;

    public void setCurrentlyPlaying(final MediaPlayer player) {
        mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(this);

        long maxTime = player.getLength();
        long currentTime = player.getTime();
        slider.setMax(maxTime);
        slider.setValue(currentTime);
        totalTime.setText("Total Time : " + formatTime(maxTime));

    }

    private String formatTime(long millis) {
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return hms;
    }

    private void loadLib() {
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Users\\ark-insingths\\Documents\\NetBeansProjects\\IP2SMediaPlayer\\lib\\VLC\\");
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

    }

    private void play(String fileName, String path, float rate, String trackID) {
        mediaPlayerComponent.getMediaPlayer().setRate(rate);
        mediaPlayerComponent.getMediaPlayer().playMedia(path);
        showRate(1.0f);
    }

    private void showRate(float rate) {
        lbRate.setText("Speeds : " + String.format("%.01f", rate));
    }
    
    public void stopMedia(String path) {
        try {
            mediaPlayerComponent.getMediaPlayer().release();
            File dir = new File(path);
            File[] f = dir.listFiles();
            for (File f1 : f) {
                try {
                    if (!f1.delete()) {
                        f1.deleteOnExit();
                    }
                } catch (Exception e) {
                    f1.deleteOnExit();
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void mediaChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, libvlc_media_t l, String string) {
        System.out.println("mediaChanged");
    }

    @Override
    public void opening(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        System.out.println("opening");
    }

    @Override
    public void buffering(uk.co.caprica.vlcj.player.MediaPlayer mp, float f) {
        System.out.println("buffering");
    }

    @Override
    public void playing(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        System.out.println("playing");
    }

    @Override
    public void paused(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        System.out.println("paused");
    }

    @Override
    public void stopped(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        System.out.println("stopped");
    }

    @Override
    public void forward(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        System.out.println("forward");
    }

    @Override
    public void backward(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        System.out.println("backward");
    }

    @Override
    public void finished(uk.co.caprica.vlcj.player.MediaPlayer mp) {
//        System.out.println("finished");
//        slider.setValue(slider.getMin());
//        play.setId("Play");
//        play.setGraphic(new ImageView(imagePlay));
    }

    @Override
    public void timeChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, long l) {
        System.out.println("timeChanged");
//        slider.setValue(mediaPlayerComponent.getMediaPlayer().getTime());
    }

    @Override
    public void positionChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, float f) {
        System.out.println("positionChanged");       
    }

    @Override
    public void seekableChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, int i) {
        System.out.println("seekableChanged");
    }

    @Override
    public void pausableChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, int i) {
        System.out.println("pausableChanged");
    }

    @Override
    public void titleChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, int i) {
        System.out.println("titleChanged");
    }

    @Override
    public void snapshotTaken(uk.co.caprica.vlcj.player.MediaPlayer mp, String string) {
        System.out.println("snapshotTaken");
    }

    @Override
    public void lengthChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, long l) {
        System.out.println("lengthChanged");
    }

    @Override
    public void videoOutput(uk.co.caprica.vlcj.player.MediaPlayer mp, int i) {
        System.out.println("videoOutput");
    }

    @Override
    public void error(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        System.out.println("error");
    }

    @Override
    public void mediaMetaChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, int i) {
        System.out.println("mediaMetaChanged");
    }

    @Override
    public void mediaSubItemAdded(uk.co.caprica.vlcj.player.MediaPlayer mp, libvlc_media_t l) {
        System.out.println("mediaSubItemAdded");
    }

    @Override
    public void mediaDurationChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, long l) {
        System.out.println("mediaDurationChanged");
    }

    @Override
    public void mediaParsedChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, int i) {
        System.out.println("mediaParsedChanged");
    }

    @Override
    public void mediaFreed(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        System.out.println("mediaFreed");
    }

    @Override
    public void mediaStateChanged(uk.co.caprica.vlcj.player.MediaPlayer mp, int i) {
        System.out.println("mediaStateChanged");
    }

    @Override
    public void newMedia(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        System.out.println("newMedia");
    }

    @Override
    public void subItemPlayed(uk.co.caprica.vlcj.player.MediaPlayer mp, int i) {
        System.out.println("subItemPlayed");
    }

    @Override
    public void subItemFinished(uk.co.caprica.vlcj.player.MediaPlayer mp, int i) {
        System.out.println("subItemFinished");
    }

    @Override
    public void endOfSubItems(uk.co.caprica.vlcj.player.MediaPlayer mp) {
        System.out.println("endOfSubItems");
    }

}
