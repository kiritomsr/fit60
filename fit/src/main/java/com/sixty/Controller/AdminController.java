package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.*;
import com.sixty.Service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.SneakyThrows;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminController implements Initializable {

    @FXML private TableView<User> memberTable;
    @FXML private TableColumn<User, String> usernameM;
    @FXML private TableColumn<User, String> phoneM;
    @FXML private TableColumn<User, String> emailM;
    @FXML private TableColumn<User, String> genderM;

    @FXML private TableView<Trainer> trainerTable;
    @FXML private TableColumn<Trainer, String> usernameT;
    @FXML private TableColumn<Trainer, String> phoneT;
    @FXML private TableColumn<Trainer, String> emailT;
    @FXML private TableColumn<Trainer, String> genderT;
    @FXML private TableColumn<Trainer, String> priceT;

    @FXML private TextField videoTitle;
    @FXML private ComboBox<String> videoType;
    @FXML private TextField videoUrl;
    @FXML private Button videoChoose;
    @FXML private MediaView videoMedia;
    @FXML private Label addInfo;

    @FXML private GridPane videoGrid;
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

    EventHandler<MouseEvent> videoPaneClick;
    EventHandler<KeyEvent> videoPaneEnter;
    EventHandler<MouseEvent> trainerTableClick;
    EventHandler<KeyEvent> trainerTableEnter;

    public void showMember() throws Exception {
        ObservableList<User> userObservableList = FXCollections.observableArrayList(UserMapper.getUserList());
        memberTable.setItems(userObservableList);
        usernameM.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        phoneM.setCellValueFactory(new PropertyValueFactory<User, String>("phone"));
        emailM.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        genderM.setCellValueFactory(new PropertyValueFactory<User, String>("sex"));
    }

    public void showTrainer() throws Exception {
        ObservableList<Trainer> trainerObservableList = FXCollections.observableArrayList(UserMapper.getTrainerList());
        usernameT.setCellValueFactory(new PropertyValueFactory<Trainer, String>("username"));
        phoneT.setCellValueFactory(new PropertyValueFactory<Trainer, String>("phone"));
        emailT.setCellValueFactory(new PropertyValueFactory<Trainer, String>("email"));
        genderT.setCellValueFactory(new PropertyValueFactory<Trainer, String>("sex"));
        trainerTable.setItems(trainerObservableList);

        priceT.setCellFactory(col-> {
            return new TableCell<Trainer, String>() {
                @SneakyThrows
                @Override
                protected void updateItem(String s, boolean b) {
                    super.updateItem(s, b);
                    int row = this.getIndex();

                    //limit digit input without characters.
                    //textField.setTextFormatter(new DecimalTextFormatter(minDecimals, maxDecimals));

                    if(row>=trainerObservableList.size() || row == -1) return;

                    TextField textField = new TextField();
                    textField.setDisable(true);

                    TrainerInfo trainerInfo = TrainerInfoMapper.findTrainerIntroByName(trainerObservableList.get(row).getUsername());
                    textField.setText(String.valueOf(trainerInfo.getPrice()));

                    trainerTableClick = new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            System.out.println(row);
                            textField.setDisable(false);
                            textField.removeEventHandler(MouseEvent.MOUSE_CLICKED, trainerTableClick);
                            textField.addEventHandler(KeyEvent.KEY_PRESSED, trainerTableEnter);
                        }
                    };

                    trainerTableEnter = new EventHandler<KeyEvent>() {
                        @SneakyThrows
                        @Override
                        public void handle(KeyEvent keyEvent) {
                            if(!keyEvent.getCode().equals(KeyCode.ENTER)) return;
                            textField.setDisable(true);
                            float price = Float.parseFloat(textField.getCharacters().toString());
                            TrainerInfoMapper.changeTrainerPrice(trainerInfo, price);
                            textField.removeEventFilter(KeyEvent.KEY_PRESSED, trainerTableEnter);
                            textField.addEventHandler(MouseEvent.MOUSE_CLICKED, trainerTableClick);
                        }

                    };

                    textField.addEventFilter(MouseEvent.MOUSE_CLICKED, trainerTableClick);


                    if(b){
                        setText(null);
                        setGraphic(null);
                    } else {
                        this.setGraphic(textField);
                    }
                }
            };
        });
    }

    public void addCombox(){
        ObservableList<String> options = FXCollections.observableArrayList(Video.types);
        videoType.getItems().addAll(Video.types);
    }

    public void initVideoGrid() throws IOException{
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

        videoPagination.setPageCount(pageNum);//设置页数
        videoPagination.setPageFactory(param -> {
            int page = param + 1;
//            System.out.println(page);
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
        showVideo();
    }

    @FXML
    public void adminLogout() throws IOException {
        App.jump("login");
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showMember();
        showTrainer();
        addCombox();
        initVideoGrid();
        showVideo();
    }

    static class DecimalTextFormatter extends TextFormatter<Number> {
        private static DecimalFormat format = new DecimalFormat("#.0;-#.0");

        public DecimalTextFormatter(int minDecimals, int maxDecimals) {
            super(
                    new StringConverter<Number>() {
                        @Override
                        public String toString(Number object) {
                            if (object == null) {
                                return "";
                            }
                            String format = "0.";
                            for (int i = 0; i < maxDecimals; i++) {
                                if (i < minDecimals) {
                                    format = format + "0";
                                } else {
                                    format = format + "#";
                                }
                            }
                            format = format + ";-" + format;
                            DecimalFormat df = new DecimalFormat(format);
                            String formatted = df.format(object);
                            return formatted;
                        }

                        @Override
                        public Number fromString(String string) {
                            try {
                                return format.parse(string);
                            } catch (ParseException e) {
                                return null;
                            }
                        }
                    },
                    0,
                    new UnaryOperator<Change>() {
                        @Override
                        public TextFormatter.Change apply(TextFormatter.Change change) {
                            if (change.getControlNewText().isEmpty()) {
                                return change;
                            }

                            ParsePosition parsePosition = new ParsePosition(0);
                            Object object = format.parse(change.getControlNewText(), parsePosition);

                            if (change.getControlNewText().equals("-")) {
                                return change;
                            }

                            if (change.getCaretPosition() == 1) {
                                if (change.getControlNewText().equals(".")) {
                                    return change;
                                }
                            }

                            if (object == null || parsePosition.getIndex() < change.getControlNewText().length()) {
                                return null;
                            } else {
                                int decPos = change.getControlNewText().indexOf(".");
                                if (decPos > 0) {
                                    int numberOfDecimals = change.getControlNewText().substring(decPos + 1).length();
                                    if (numberOfDecimals > maxDecimals) {
                                        return null;
                                    }
                                }
                                return change;
                            }
                        }
                    }
            );
        }
    }
}



