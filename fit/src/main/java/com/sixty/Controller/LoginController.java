package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.*;
import com.sixty.Service.UserInfoMapper;
import com.sixty.Service.TrainerInfoMapper;
import com.sixty.Service.UserMapper;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField loginUsername;
    @FXML private PasswordField loginPassword;
    @FXML private Button loginButton;
    @FXML private Label loginInfo;
    @FXML private RadioButton memberID;
    @FXML private RadioButton trainerID;
    @FXML private RadioButton administratorID;
    @FXML private MediaView backgroundVideo;
    @FXML private AnchorPane anchorPane;


    private MySession mySession = MySession.mySession;
    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        backgroundVideo.setMediaPlayer(new MediaPlayer(new Media(new File("src/main/resources/video/background.mp4").toURI().toString())));
        backgroundVideo.getMediaPlayer().setMute(true);
        backgroundVideo.getMediaPlayer().setAutoPlay(true);
        backgroundVideo.getMediaPlayer().setCycleCount(MediaPlayer.INDEFINITE);

        backgroundVideo.getMediaPlayer().play();
        EventHandler<KeyEvent> loginEnter = keyEvent ->{
            if(keyEvent.getCode() == KeyCode.ENTER){
                try {
                    loginButtonClicked();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        anchorPane.addEventFilter(KeyEvent.KEY_PRESSED, loginEnter);
        loginUsername.requestFocus();
        loginUsername.getOnMouseClicked();
    }

    @FXML
    public void loginButtonClicked() throws Exception {
        int identity;
        String username = loginUsername.getCharacters().toString();
        String password = loginPassword.getCharacters().toString();

        if (username.equals("")) {
            loginInfo.setVisible(true);
            loginInfo.setText("Please enter username:");
            return;
        }else if(password.equals("")){
            loginInfo.setVisible(true);
            loginInfo.setText("Please enter password:");
            return;
        }

        if (memberID.isSelected()) identity = 1;
        else if (trainerID.isSelected()) identity = 2;
        else if (administratorID.isSelected()) identity = 3;
        else { loginInfo.setVisible(true); loginInfo.setText("Please choose identity"); return; }

        switch (identity){
            case 1:
                loginMember(username, password);break;
            case 2:
                loginTrainer(username, password);break;
            case 3:
                loginAdmin(username, password);
        }
    }

    public void loginMember(String username, String password) throws IOException {
        User user = UserMapper.findUserByName(username);
        UserInfo userInfo = UserInfoMapper.findMemberCenterInfoByName(loginUsername.getCharacters().toString());
        if(user == null){
            loginInfo.setVisible(true);
            loginInfo.setText("Username is wrong, please enter again.");
            return;
        }
        else if(!user.getPassword().equals(password)){
            loginInfo.setVisible(true);
            loginInfo.setText("Password is wrong, please enter again.");
            return;
        }
        else {
            mySession.setUser(user);
            mySession.setUserInfo(UserInfoMapper.findMemberCenterInfoByName(username));
            App.jump("showVideo");
        }
    }

    public void loginTrainer(String username, String password) throws IOException {
        Trainer trainer = UserMapper.findTrainerByName(loginUsername.getCharacters().toString());
        TrainerInfo trainerInfo = TrainerInfoMapper.findTrainerIntroByName(loginUsername.getCharacters().toString());
        if(trainer == null){
            loginInfo.setVisible(true);
            loginInfo.setText("Username is wrong, please enter again.");
            return;
        }
        else if(!trainer.getPassword().equals(password)){
            loginInfo.setVisible(true);
            loginInfo.setText("Password is wrong, please enter again.");
            return;
        }
        else {
            mySession.setTrainer(trainer);
            mySession.setTrainerInfo(TrainerInfoMapper.findTrainerIntroByName(username));
            App.jump("trainerInfo");
        }

    }

    public void loginAdmin(String username, String password) throws Exception {
        if(!username.equals("123")){
            loginInfo.setVisible(true);
            loginInfo.setText("Username is wrong, please enter again.");
            return;
        }
        else if(!password.equals("123")){
            loginInfo.setVisible(true);
            loginInfo.setText("Password is wrong, please enter again.");
            return;
        }
        else {
            App.jump( "adminMemberInfo");
        }
    }

    @FXML
    private void RegisterClicked() throws Exception{
        //System.out.println("注册");
        App.jump("register");

    }

}
