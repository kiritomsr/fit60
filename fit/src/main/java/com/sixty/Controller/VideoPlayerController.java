package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.MySession;
import com.sixty.Pojo.Video;
import com.sixty.Service.VideoMapper;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VideoPlayerController implements Initializable {
    private Video video;
    private MediaPlayer videoplayer;
    private Duration duration;
    private double volumeValue;
    private boolean mouse = false;
    @FXML private Button backButton;
    @FXML private Label videoTitle;
    @FXML private Label videoTime;
    @FXML private MediaView videoView;
    @FXML private Slider slider;
    @FXML private Slider sldVol;
    @FXML private ImageView btnPlay;
    @FXML private ImageView btnVol;
    @FXML private ImageView btnMax;
    @FXML private ImageView btnLike;

    private MySession mySession = MySession.mySession;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.video = mySession.getVideo();
        videoTitle.setText(video.getTitle());
        setBtnLike();
        Media media = new Media(new File("src/main/resources/video/" + video.getUrl()).toURI().toString());
        videoplayer = new MediaPlayer(media);
        videoView.setMediaPlayer(videoplayer);

        videoView.addEventFilter(MouseEvent.MOUSE_CLICKED,new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                videoPlay();
            }
        });

//        videoplayer.setCycleCount(1);
        videoplayer.setOnReady(new Runnable() {
            @Override
            public void run() {

                    duration = videoplayer.getMedia().getDuration();
                    volumeValue = videoplayer.getVolume();
                    
                    updateValues();

            }
        });

        //mediaPlayer当前进度发生改变时候，进度条 、时间标签、音量条数据
        videoplayer.currentTimeProperty().addListener(new ChangeListener<Duration>(){
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                if(!mouse) {
                    updateValues();
                }
            }
        });
        videoplayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                videoplayer.seek(Duration.ZERO);
                videoplayer.pause();
                btnPlay.setImage(new Image("file:src/main/resources/picture/icon/play.png"));
            }
        });

        setProcessSlider();
        setVolumeSD();

    }

    @FXML private void backToList() throws IOException {
        if(videoplayer.getStatus() == MediaPlayer.Status.PLAYING){ videoplayer.stop(); }
        videoplayer.dispose();   //释放meidaPlayer的Media资源
        videoplayer = null;
        System.gc();    //通知JVM垃圾回收器
        App.jump("showVideo");
    }

    @FXML private void videoPlay() {
        MediaPlayer.Status status = videoplayer.getStatus();

        if(status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.READY || status == MediaPlayer.Status.STOPPED){
            videoplayer.play();
            btnPlay.setImage(new Image("file:src/main/resources/picture/icon/stop.png"));
        }else if(videoplayer.getCurrentTime().equals(videoplayer.getStopTime())){

            videoplayer.seek(Duration.ZERO);
        } else {
            videoplayer.pause();
            btnPlay.setImage(new Image("file:src/main/resources/picture/icon/play.png"));
        }
    }

    private void setProcessSlider(){
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(slider.isValueChanging()){     //加入Slider正在改变的判定，否则由于update线程的存在，mediaPlayer会不停地回绕
                videoplayer.seek(duration.multiply(slider.getValue()/100.0));
            }
        });
        slider.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            videoplayer.seek(duration.multiply(slider.getValue()/100.0));

            mouse = false;
        });
        slider.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            mouse = true;
        });
    }

    private void setVolumeSD(){
        sldVol.valueProperty().addListener((observable, oldValue, newValue) -> {
            videoplayer.setVolume(newValue.doubleValue()/100);
            volumeValue = newValue.doubleValue();

        });
    }

    @FXML private void videoMute() {
        if(videoplayer.getVolume() == 0.0){
            videoplayer.setVolume(volumeValue);
            btnVol.setImage(new Image("file:src/main/resources/picture/icon/sound.png"));
        }else {
            videoplayer.setVolume(0);
            btnVol.setImage(new Image("file:src/main/resources/picture/icon/soundClose.png"));
        }
    }

    @FXML private void videoLike() throws IOException {
        VideoMapper.changeLike(mySession.getVideo(), mySession.getUser().getUsername());
        setBtnLike();
    }

    private void setBtnLike(){
        if(VideoMapper.isLiked(mySession.getVideo(), mySession.getUser().getUsername())){
            btnLike.setImage(new Image("file:src/main/resources/picture/icon/collected.png"));
        }else {
            btnLike.setImage(new Image("file:src/main/resources/picture/icon/collect.png"));
        }
    }

    @FXML private void videoMax() {
        Stage stage = (Stage)(btnMax.getScene().getWindow());
        stage.setFullScreen(true);
    }

    //更新视频数据（进度条 、时间标签、音量条数据）
    protected void updateValues(){
        if(slider != null && videoTime!=null && sldVol != null && btnVol != null){
            Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    Duration currentTime = videoplayer.getCurrentTime();
                    videoTime.setText(formatTime(currentTime,duration));    //设置时间标签
                    slider.setDisable(duration.isUnknown());   //无法读取时间是隐藏进度条
                    if(!slider.isDisabled() && duration.greaterThan(Duration.ZERO) && !slider.isValueChanging()){
                        slider.setValue(currentTime.toMillis()/duration.toMillis() * 100);   //设置进度条
                    }
                    if(!sldVol.isValueChanging()){
                        sldVol.setValue((int)Math.round(videoplayer.getVolume() *100));   //设置音量条
                        if(videoplayer.getVolume() == 0){        //设置音量按钮
                            btnVol.setImage(new Image("file:src/main/resources/picture/icon/soundClose.png"));
                        }else{
                            btnVol.setImage(new Image("file:src/main/resources/picture/icon/sound.png"));
                        }
                    }
                }
            });
        }
    }

    //将Duration数据格式化，用于播放时间标签
    protected String formatTime(Duration elapsed,Duration duration){
        //将两个Duartion参数转化为 hh：mm：ss的形式后输出
        int intElapsed = (int)Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        int elapsedMinutes = (intElapsed - elapsedHours *60 *60)/ 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;
        if(duration.greaterThan(Duration.ZERO)){
            int intDuration = (int)Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            int durationMinutes = (intDuration - durationHours *60 * 60) / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;

            if(durationHours > 0){
                return String.format("%02d:%02d:%02d / %02d:%02d:%02d",elapsedHours,elapsedMinutes,elapsedSeconds,durationHours,durationMinutes,durationSeconds);
            }else{
                return String.format("%02d:%02d / %02d:%02d",elapsedMinutes,elapsedSeconds,durationMinutes,durationSeconds);
            }
        }else{
            if(elapsedHours > 0){
                return String.format("%02d:%02d:%02d / %02d:%02d:%02d",elapsedHours,elapsedMinutes,elapsedSeconds);
            }else{
                return String.format("%02d:%02d / %02d:%02d",elapsedMinutes,elapsedSeconds);
            }
        }
    }
}
