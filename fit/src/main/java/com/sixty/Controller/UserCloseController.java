package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.*;
import com.sixty.Service.TrainerInfoMapper;
import com.sixty.Service.UserInfoMapper;
import com.sixty.Service.UserMapper;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaView;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserCloseController implements Initializable {

    @FXML private TextField loginUsername;
    @FXML private PasswordField loginPassword;
    @FXML private Label loginInfo;

    private MySession mySession = MySession.mySession;
    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loginUsername.setText(mySession.getUser().getUsername());
    }

    @FXML
    public void closeClicked() throws Exception {

        String password = loginPassword.getCharacters().toString();

        if(password.equals("")){
            loginInfo.setVisible(true);
            loginInfo.setText("Please enter password:");
            return;
        }

        User user = UserMapper.findUserByName(mySession.getUser().getUsername());
        UserInfo userInfo = UserInfoMapper.findMemberCenterInfoByName(loginUsername.getCharacters().toString());

        if(!user.getPassword().equals(password)){
            loginInfo.setVisible(true);
            loginInfo.setText("Password is wrong, please enter again.");
            return;
        }
        else {
            UserMapper.deleteUser(user);
            UserInfoMapper.deleteMemberCenterInfo(userInfo);
            System.exit(0);
        }

    }



    @FXML
    private void cancelClicked() throws Exception{

        loginUsername.getScene().getWindow().getOnCloseRequest();
        loginUsername.getScene().getWindow().hide();

        App.show();

    }

}
