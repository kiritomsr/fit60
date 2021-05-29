package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.Video;
import com.sixty.Service.VideoMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminAddVideoController implements Initializable {
    @FXML
    private TextField videoTitle;
    @FXML private ComboBox<String> videoType;
    @FXML private TextField videoUrl;
    @FXML private Button videoChoose;
    @FXML private MediaView videoMedia;
    @FXML private Label addInfo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addCombox();
    }

    public void addCombox(){
        ObservableList<String> options = FXCollections.observableArrayList(Video.types);
        videoType.getItems().addAll(Video.types);
    }

    @FXML
    public void chooseFile() throws Exception{
        final Stage stage = new Stage();
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./src/main/resources/video"));
        videoChoose.setOnAction((final ActionEvent e) -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                openFile(file);
            }
        });
    }

    private void openFile(File file) {
        final Desktop desktop = Desktop.getDesktop();
        EventQueue.invokeLater(() -> {
            try {
                desktop.open(file);
                String string = file.toURI().toString();
                Media media = new Media(string);
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                videoMedia.setMediaPlayer(mediaPlayer);
                videoMedia.setVisible(true);
                videoUrl.setText(string.split("/")[string.split("/").length-1]);

            } catch (IOException ex) {
                Logger.getLogger(RegisterController.
                        class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        });
    }

    @FXML //click upload button
    public void clickUpload() throws Exception {
        String title = videoTitle.getCharacters().toString();
        String type = videoType.getValue();
        String url = videoUrl.getCharacters().toString();
        //logic incomplete
        if(title.equals("")) {
            addInfo.setText("Require Title.");
            addInfo.setVisible(true);
            return;
        }
        if(!VideoMapper.checkTitle(title)){
            addInfo.setText("Duplicate Title.");
            addInfo.setVisible(true);
            return;
        }
        if(type==null){
            addInfo.setText("Require Type.");
            addInfo.setVisible(true);
            return;
        }
        if(url.equals("")) {
            addInfo.setText("Require Url.");
            addInfo.setVisible(true);
            return;
        }
        Video video = new Video();
        video.setTitle(title);
        video.setType(type);
        video.setUrl(url);
        VideoMapper.addVideo(video);

        videoTitle.clear();
        videoType.getItems().removeAll();
        videoType.setValue(null);
        videoUrl.clear();
        videoMedia.setVisible(false);
        addInfo.setVisible(false);
        //showVideo();
    }

    public void jumpToMemberInfo() throws IOException {
        App.jump("adminMemberInfo");
    }
    public void jumpToTrainerInfo() throws IOException{
        App.jump("adminTrainerInfo");
    }
    public void jumpToVideoManagement() throws IOException{
        App.jump("adminVideo");
    }
    public void jumpToIncome() throws IOException{
        App.jump("adminIncome");
    }
    public void adminLogout() throws IOException {
        App.jump("login");
    }
}
