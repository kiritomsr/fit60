package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.MySession;
import com.sixty.Pojo.Video;
import com.sixty.Service.VideoMapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class VideoListController implements Initializable {


    @FXML private TextField inputVideoKeyword;
    @FXML private ChoiceBox<String> chooseVideoTypes;
    @FXML private Pane videoPane1;
    @FXML private Pane videoPane2;
    @FXML private Pane videoPane3;
    @FXML private Pane videoPane4;
    @FXML private Pane videoPane5;
    @FXML private Pane videoPane6;
    ArrayList<Pane> videoPanes = new ArrayList<>();
    @FXML private Pagination videoPagination;
    @FXML private Label videoType1;
    @FXML private Label videoType2;
    @FXML private Label videoType3;
    @FXML private Label videoType4;
    @FXML private Label videoType5;
    @FXML private Label videoType6;
    ArrayList<Label> videoTypes = new ArrayList<>();
    @FXML private Label videoName1;
    @FXML private Label videoName2;
    @FXML private Label videoName3;
    @FXML private Label videoName4;
    @FXML private Label videoName5;
    @FXML private Label videoName6;
    ArrayList<Label> videoNames = new ArrayList<>();
    @FXML private MediaView videoView1;
    @FXML private MediaView videoView2;
    @FXML private MediaView videoView3;
    @FXML private MediaView videoView4;
    @FXML private MediaView videoView5;
    @FXML private MediaView videoView6;
    ArrayList<MediaView> videoViews = new ArrayList<>();

    private MySession mySession = MySession.mySession;
    private String type = "";
    private String keyword = "";

    public void initVideos(){
        videoPanes.add(videoPane1);
        videoPanes.add(videoPane2);
        videoPanes.add(videoPane3);
        videoPanes.add(videoPane4);
        videoPanes.add(videoPane5);
        videoPanes.add(videoPane6);

        videoTypes.add(videoType1);
        videoTypes.add(videoType2);
        videoTypes.add(videoType3);
        videoTypes.add(videoType4);
        videoTypes.add(videoType5);
        videoTypes.add(videoType6);

        videoNames.add(videoName1);
        videoNames.add(videoName2);
        videoNames.add(videoName3);
        videoNames.add(videoName4);
        videoNames.add(videoName5);
        videoNames.add(videoName6);

        videoViews.add(videoView1);
        videoViews.add(videoView2);
        videoViews.add(videoView3);
        videoViews.add(videoView4);
        videoViews.add(videoView5);
        videoViews.add(videoView6);
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        videoPagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        initVideos();

        chooseVideoTypes.setItems(FXCollections.observableArrayList("All","Yoga","HIIT","Run","Strength","My Favorite"));
        chooseVideoTypes.setValue("All");
        chooseVideoTypes.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends  Number> ov,
                 Number old_val, Number new_val) ->{
                    int i = new_val.intValue();
                    System.out.println(i);
                    if (i==0){
                        type = "";
                    } else if(i==1){
                        type = "Yoga";
                    } else if (i==2){
                        type = "HIIT";
                    } else if (i==3){
                        type = "Run";
                    } else if (i==4){
                        type = "Strength";
                    } else if (i==5){
                        type = "My Favorite";
                    }
                    try {
                        selectVideo();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        selectVideo();

    }

    //show video list page
    public void showVideo(ArrayList<Video> videos) {
        int listNum = videos.size();

        int pageNum = (listNum%6==0)?listNum/6:listNum/6 +1;

        if (listNum == 0){
            for (int i=0;i<videoPanes.size();i++){
                videoPanes.get(i).setVisible(false);
            }
            return;
        }

        videoPagination.setPageCount(pageNum);//设置页数

        videoPagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                VBox box = new VBox(5);
                if ((param+1)!=pageNum||videos.size()%6==0){
                    for (int i=0;i<6;i++){
                        videoViews.get(i).setMediaPlayer(new MediaPlayer(new Media(new File("src/main/resources/video/" + videos.get(6*param+i).getUrl()).toURI().toString())));
                        videoTypes.get(i).setText("Type: "+videos.get(6*param+i).getType());
                        videoNames.get(i).setText("Title: "+videos.get(6*param+i).getTitle());
                        videoPanes.get(i).setVisible(true);
                    }
                }//if
                else {
                    for (int i=0;i<(videos.size()%6);i++){
                        videoViews.get(i).setMediaPlayer(new MediaPlayer(new Media(new File("src/main/resources/video/" + videos.get(6*param+i).getUrl()).toURI().toString())));
                        videoTypes.get(i).setText("Type: "+videos.get(6*param+i).getType());
                        videoNames.get(i).setText("Title: "+videos.get(6*param+i).getTitle());
                        videoPanes.get(i).setVisible(true);
                    }
                    for (int i=(videos.size()%6);i<6;i++){
                        videoPanes.get(i).setVisible(false);
                    }
                }
                return box;
            }
        });

        for(Pane videoPane : videoPanes){
            int i = videoPanes.indexOf(videoPane);
            EventHandler<MouseEvent> paneHover = e -> {
                if(videoViews.get(i).getMediaPlayer()==null) return;

                videoViews.get(i).getMediaPlayer().setMute(true);
                videoViews.get(i).getMediaPlayer().play();
                for( Node node: videoPanes.get(i).getChildren()) {
                    if (node instanceof Rectangle) {
                        node.setOpacity(0.2);
                    }
                    if (node instanceof Label) {
                        node.setOpacity(0);
                    }
                }
            };

            EventHandler<MouseEvent> paneHout = e -> {
                if(videoViews.get(i).getMediaPlayer()==null) return;
                videoViews.get(i).getMediaPlayer().stop();
                for( Node node: videoPanes.get(i).getChildren()) {
                    if (node instanceof Rectangle) {
                        node.setOpacity(0.6);
                    }
                    if (node instanceof Label) {
                        node.setOpacity(1);
                    }
                }
            };

            EventHandler<MouseEvent> paneClick = e -> {
                String title = videoNames.get(i).getText().split(": ")[1];
                try {
                    mySession.setVideo(VideoMapper.findVideoByTitle(title));
                    App.jump("videoPlayer");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            };

            videoPane.addEventFilter(MouseEvent.MOUSE_ENTERED, paneHover);
            videoPane.addEventFilter(MouseEvent.MOUSE_EXITED, paneHout);
            videoPane.addEventFilter(MouseEvent.MOUSE_CLICKED, paneClick);
        }


    }

    @FXML
    public void videoKeywordsTyped() throws IOException {
        keyword = inputVideoKeyword.getText();
        selectVideo();
    }

    public void selectVideo() throws IOException{
        ArrayList<Video> videos = VideoMapper.getVideoList();
        if(!keyword.equals("")){
            videos = VideoMapper.selectVideoByKeyword(videos, keyword);
        }

        if(type.equals("My Favorite")){
            videos = VideoMapper.selectVideoLiked(videos, mySession.getUser().getUsername());
        }else if(!type.equals("")) {
            videos = VideoMapper.selectVideoByType(videos, type);
        }

        showVideo(videos);
    }

    @FXML
    public void jumpToTrainer() throws IOException {
        App.jump("showTrainer");
    }

    @FXML
    public void jumpToCenter() throws IOException {
        App.jump("showCenter");
    }
}
