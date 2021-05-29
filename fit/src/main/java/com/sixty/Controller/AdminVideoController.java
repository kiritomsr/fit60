package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.MySession;
import com.sixty.Pojo.Video;
import com.sixty.Service.VideoMapper;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminVideoController implements Initializable {
    @FXML
    private GridPane videoGrid;
    @FXML private Pagination videoPagination;
    private ArrayList<MediaView> videoViews = new ArrayList<>();
    @FXML private MediaView videoView1;
    @FXML private MediaView videoView2;
    @FXML private MediaView videoView3;
    @FXML private MediaView videoView4;
    private ArrayList<TextField> videoTitles = new ArrayList<>();
    @FXML private TextField videoTitle1;
    @FXML private TextField videoTitle2;
    @FXML private TextField videoTitle3;
    @FXML private TextField videoTitle4;
    private ArrayList<Label> videoTypes = new ArrayList<>();
    @FXML private Label videoType1;
    @FXML private Label videoType2;
    @FXML private Label videoType3;
    @FXML private Label videoType4;
    private ArrayList<Label> videoUrls = new ArrayList<>();
    @FXML private Label videoUrl1;
    @FXML private Label videoUrl2;
    @FXML private Label videoUrl3;
    @FXML private Label videoUrl4;
    private ArrayList<Button> videoDeletes = new ArrayList<>();
    @FXML private Button videoDelete1;
    @FXML private Button videoDelete2;
    @FXML private Button videoDelete3;
    @FXML private Button videoDelete4;


    EventHandler<MouseEvent> videoPaneClick ;
    EventHandler<KeyEvent> videoPaneEnter ;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initVideoGrid();
        showVideo();
    }
    public void initVideoGrid() throws IOException {
        videoViews.add(videoView1);
        videoViews.add(videoView2);
        videoViews.add(videoView3);
        videoViews.add(videoView4);
        videoTitles.add(videoTitle1);
        videoTitles.add(videoTitle2);
        videoTitles.add(videoTitle3);
        videoTitles.add(videoTitle4);
        videoTypes.add(videoType1);
        videoTypes.add(videoType2);
        videoTypes.add(videoType3);
        videoTypes.add(videoType4);
        videoUrls.add(videoUrl1);
        videoUrls.add(videoUrl2);
        videoUrls.add(videoUrl3);
        videoUrls.add(videoUrl4);
        videoDeletes.add(videoDelete1);
        videoDeletes.add(videoDelete2);
        videoDeletes.add(videoDelete3);
        videoDeletes.add(videoDelete4);

    }

    public void showVideo() throws IOException {
        ArrayList<Video> videos = VideoMapper.getVideoList();
        final int pageNum;
        int len = videos.size();
        if (len%4==0) pageNum = len/4;
        else pageNum = len/4 +1;
        videoPagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);

        videoPagination.setPageCount(pageNum);//设置页数
        videoPagination.setPageFactory(param -> {
            int page = param + 1;

            VBox box = new VBox(5);
            int rowNum = 4;
            //最后页隐藏多余
            if(page == pageNum && (videos.size() % 4)!=0) {
                rowNum = videos.size() % 4;
                for (int i=0;i<4;i++) {
                    videoDeletes.get(i).setVisible(false);
                    videoViews.get(i).setVisible(false);
                    videoTypes.get(i).setVisible(false);
                    videoTitles.get(i).setVisible(false);
                    videoUrls.get(i).setVisible(false);
                }
            }

            for (int i=0;i<rowNum;i++) {
                String newUrl = videos.get(4 * page + i - 4).getUrl();
                File file = new File("src/main/resources/video/" + newUrl);
                String url1 = file.toURI().toString();
                Media media = new Media(url1);
                MediaPlayer mplayer = new MediaPlayer(media);
                videoViews.get(i).setMediaPlayer(mplayer);
                videoTypes.get(i).setText(videos.get(4 * page + i - 4).getType());
                videoTitles.get(i).setText(videos.get(4 * page + i - 4).getTitle());
                videoUrls.get(i).setText(videos.get(4 * page + i - 4).getUrl());
                videoDeletes.get(i).setVisible(true);
                videoViews.get(i).setVisible(true);
                videoTypes.get(i).setVisible(true);
                videoTitles.get(i).setVisible(true);
                videoUrls.get(i).setVisible(true);
                videoTitles.get(i).setStyle("  -fx-text-inner-color:black; ");
            }


            return box;

        });
        videoPaneClick = e -> {
            for( Node node: videoGrid.getChildren()) {
                if( node instanceof TextField) {
                    if( node.getBoundsInParent().contains(e.getSceneX(),  e.getSceneY()-60)) {
//                        System.out.println( "Node: " + node + " at " + GridPane.getRowIndex(node) + "/" + GridPane.getColumnIndex(node));
                        changeEditable(GridPane.getRowIndex(node));
                    }
                }
                if(node instanceof Button){
                    if( node.getBoundsInParent().contains(e.getSceneX(),  e.getSceneY()-60)) {
//                        System.out.println( "Node: " + node + " at " + GridPane.getRowIndex(node) + "/" + GridPane.getColumnIndex(node));
                        try {
                            deleteVideo(GridPane.getRowIndex(node));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }
            videoGrid.removeEventFilter(MouseEvent.MOUSE_CLICKED, videoPaneClick);
        };
        videoGrid.addEventFilter(MouseEvent.MOUSE_CLICKED, videoPaneClick);

    }
    public void changeEditable(int i){
        TextField thisNode = videoTitles.get(i-1);
        String oldTitle = thisNode.getCharacters().toString();
        thisNode.setDisable(false);

        videoPaneEnter = keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER){
                thisNode.setDisable(true);
                thisNode.removeEventFilter(KeyEvent.KEY_PRESSED, videoPaneEnter);
                videoGrid.addEventFilter(MouseEvent.MOUSE_CLICKED, videoPaneClick);
                String newTitle = thisNode.getCharacters().toString();
                try {
                    Video oldVideo = VideoMapper.findVideoByTitle(oldTitle);
                    VideoMapper.deleteVideo(oldVideo);
                    assert oldVideo != null;
                    oldVideo.setTitle(newTitle);
                    VideoMapper.addVideo(oldVideo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thisNode.addEventFilter(KeyEvent.KEY_PRESSED, videoPaneEnter);
    }

    public void deleteVideo(int i) throws IOException {
        TextField thisNode = videoTitles.get(i-1);
        String oldTitle = thisNode.getCharacters().toString();
        Video oldVideo = VideoMapper.findVideoByTitle(oldTitle);
        VideoMapper.deleteVideo(oldVideo);

        showVideo();
    }
    public void jumpToMemberInfo() throws IOException{
        App.jump("adminMemberInfo");
    }
    public void jumpToTrainerInfo() throws IOException{
        App.jump("adminTrainerInfo");
    }
    public void jumpToAddVideo() throws IOException{
        App.jump("adminAddVideo");
    }
    public void jumpToIncome() throws IOException{
        App.jump("adminIncome");
    }
    public void adminLogout() throws IOException {
        App.jump("login");
    }
}
