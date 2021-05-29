package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.User;
import com.sixty.Pojo.MySession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;


public class UserInfoController {
    @FXML
    private Label userName;
    @FXML
    private Label userPhone;
    @FXML
    private Label userEmail;
    @FXML
    private Label userSex;
    @FXML
    private ImageView userPhoto;
    @FXML
    private Button myFavorite;
    @FXML
    private Button back3;

    private MySession mySession = MySession.mySession;

    public void initialize() {
        User user = mySession.getUser();
        userName.setText(user.getUsername());
        userPhone.setText(user.getPhone());
        userEmail.setText(user.getEmail());
        userSex.setText(user.getSex());
        userPhoto.setImage(new Image("file:src/main/resources/picture/UserPhoto/"+ user.getPhotoUrl()));
    }

    public void changeInformation()throws IOException{
        App.jump("userInfoChange");
    }

    public void backToIndex()throws IOException{
        App.jump("showCenter");
    }

    public void closeAccount() throws IOException {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        Parent root = FXMLLoader.load(Objects.requireNonNull(App.class.getClassLoader().getResource("fxml/userClose.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
        back3.getScene().getWindow().hide();
    }

}
