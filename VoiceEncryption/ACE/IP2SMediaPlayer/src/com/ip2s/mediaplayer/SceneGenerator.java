/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ip2s.mediaplayer;

import java.io.*;
import java.text.DecimalFormat;
import javafx.application.Platform;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.util.Duration;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class SceneGenerator {

    final Label currentlyPlaying = new Label();
    final Label currentTime = new Label();
    final Label totalTime = new Label();
    final Label lbRate = new Label();
    Slider slider = new Slider();
    final Label lbNote = new Label();
    private ChangeListener<Duration> progressChangeListener;

    MediaPlayer player;
    MediaView mediaView;
    Image imagePlay;
    Image imagePause;
    Image imageStop;
    Image imageForward;
    Image imageBackward;
    Image imageDefault;
    double pRate = 0.1;
    String trackID;

    public Scene createScene(String fileName, String path, double defaultRate, String trackID) {
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
        player = createPlayer(("file:///" + (dir + "\\" + fileName).replace("\\", "/").replaceAll(" ", "%20")));
        mediaView = new MediaView(player);
        final Button stop = new Button("", new ImageView(imageStop));
        stop.setId("Stop");
        final Button play = new Button("", new ImageView(imagePause));
        play.setId("Pause");
        final Button backward = new Button("", new ImageView(imageBackward));
        backward.setId("backward");
        final Button ori = new Button("", new ImageView(imageDefault));
        ori.setId("ori");
        final Button forward = new Button("", new ImageView(imageForward));
        forward.setId("forward");
        lbNote.setText("Speeds min 0.5   max 2.5");
        player.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                if (player.getStartTime() != Duration.ZERO) {
                    player.setStartTime(Duration.ZERO);
                    double t = player.getTotalDuration().toSeconds() + player.getStartTime().toSeconds();
                    slider.setMax(t);
                }
                slider.setValue(slider.getMin());
                player.seek(Duration.ZERO);
                play.setId("Play");
                play.setGraphic(new ImageView(imagePlay));
                player.pause();
                //      player.setStartTime(new Duration(0.0));
            }
        });
        slider.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                Duration dTime = Duration.seconds(slider.getValue());
                //  player = createPlayer(("file:///" + (dir + "\\" + fileName).replace("\\", "/").replaceAll(" ", "%20")));
                player.seek(dTime);
                double d = slider.getValue();
                String time = getTimeFormat(d);
                currentTime.setText("Current Time : " + time);

            }
        });
        slider.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Duration dTime = Duration.seconds(slider.getValue());
                double d = slider.getValue();
                String time = getTimeFormat(d);
                currentTime.setText("Current Time : " + time);
                //        player.setStartTime(dTime);

                if (player.getStartTime() != Duration.ZERO) {
                    player.setStartTime(Duration.ZERO);
                    double t = player.getTotalDuration().toSeconds() + player.getStartTime().toSeconds();
                    slider.setMax(t);
                }
                player.seek(dTime);

            }
        });

        stop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //    final MediaPlayer curPlayer = mediaView.getMediaPlayer();
                // curPlayer.setRate(1.0);
                player.stop();
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
                    player.pause();
                } else {
                    play.setId("Pause");
                    play.setGraphic(new ImageView(imagePause));
                    Duration dTime = Duration.seconds(slider.getValue());
                    // player.setStartTime(dTime);                    
                    player.setStartTime(dTime);
                    player.play();
                }
            }
        });

        forward.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                double cRate = player.getRate();
                cRate = Double.parseDouble(new DecimalFormat("##.####").format(cRate));
                double nRate = cRate + pRate;
                if (nRate < 0.0) {
                    nRate = 1.0;
                } else if (nRate > 2.5) {
                    nRate = 2.5;
                }
                player.setRate(nRate);
                String r = nRate + "";
                if (r.length() > 3) {
                    r = r.substring(0, 3);
                }
                lbRate.setText("Speeds : " + r);
            }
        });

        backward.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                double cRate = player.getRate();
                cRate = Double.parseDouble(new DecimalFormat("##.####").format(cRate));
                double nRate = cRate - pRate;
                if (nRate <= 0.5) {
                    nRate = 0.5;
                }
                player.setRate(nRate);
                String r = nRate + "";
                if (r.length() > 3) {
                    r = r.substring(0, 3);
                }
                lbRate.setText("Speeds : " + r);

            }
        });

        ori.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                player.setRate(1.0);
                //  player.play();
                lbRate.setText("Speeds : " + "1.0");
            }
        });

        //slider.setBackground(Background.EMPTY);
        lbRate.setText("Speeds : " + defaultRate);
        mediaView.setMediaPlayer(player);
        mediaView.getMediaPlayer().play();

        setCurrentlyPlaying(mediaView.getMediaPlayer());
        player.setRate(defaultRate);
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
        //   HBox hbox = HBoxBuilder.create().spacing(10).alignment(Pos.BOTTOM_LEFT).children(backward, ori, forward).build();

        layout.getChildren().addAll(
                invisiblePause,
                VBoxBuilder.create().spacing(10).alignment(Pos.TOP_LEFT).children(
                        currentlyPlaying,
                        slider,
                        HBoxBuilder.create().spacing(10).alignment(Pos.CENTER).children(currentTime, totalTime).build(),
                        mediaView, borderPane, lbNote
                ).build()
        );

        return new Scene(layout, 400, 350);
    }

    public void setCurrentlyPlaying(final MediaPlayer newPlayer) {
        progressChangeListener = new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration oldValue, Duration newValue) {
                double dTime = newPlayer.getCurrentTime().toSeconds();
                String time = getTimeFormat(dTime);
                currentTime.setText("Current Time : " + time);
                dTime = newPlayer.getTotalDuration().toSeconds();
                time = getTimeFormat(dTime);
                //  System.out.println("start time : " + player.getStartTime());
                if (player.getStartTime() != Duration.ZERO) {
                    double t = newPlayer.getTotalDuration().toSeconds() + player.getStartTime().toSeconds();
                    String st = getTimeFormat(t);
                    slider.setMax(t);
                    slider.setValue(newPlayer.getCurrentTime().toSeconds());
                    totalTime.setText("Total Time : " + st);
                } else {
                    slider.setMax(newPlayer.getTotalDuration().toSeconds());
                    slider.setValue(newPlayer.getCurrentTime().toSeconds());
                    totalTime.setText("Total Time : " + time);
                }
                String rate = (new Double(player.getRate())).toString();
                if (rate.length() > 3) {
                    rate = rate.substring(0, 3);
                }
                lbRate.setText("Speeds : " + rate);

            }
        };
        newPlayer.currentTimeProperty().addListener(progressChangeListener);
        String source = newPlayer.getMedia().getSource();
        source = source.substring(source.lastIndexOf("/") + 1).replaceAll("%20", " ");
        currentlyPlaying.setText("Now Playing: " + trackID);
    }

    /**
     * @return a MediaPlayer for the given source which will report any errors
     * it encounters
     */
    String getTimeFormat(double dTime) {
        String time = String.format("%02d:%02d:%02d", (int) dTime / 3600, ((int) dTime % 3600) / 60, (int) dTime % 60);
        return time;
    }

    private MediaPlayer createPlayer(String aMediaSrc) {
        final MediaPlayer player = new MediaPlayer(new Media(aMediaSrc));
        player.setOnError(new Runnable() {
            @Override
            public void run() {
                System.out.println("Media error occurred: " + player.getError());
            }
        });
        return player;
    }

    public void stopMedia(String path) {
        try {
            // final MediaPlayer curPlayer = mediaView.getMediaPlayer();
            //   curPlayer.dispose();
            player.dispose();
            //  this.wait(2000);
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

}
