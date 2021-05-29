package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.MySession;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserCenterController implements Initializable {
    @FXML private ImageView userPhoto;
    @FXML private ImageView vip;
    @FXML private Label userName;

    private MySession mySession = MySession.mySession;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //mainTab.getSelectionModel().select(2);
        //center.setGraphic(new ImageView(new Image("picture/icon/close.png")));
        //show center page
        //center.setTextFill(Color.valueOf("1addc0"));
        showCenter();
    }

    public void showCenter() throws IOException {
        userName.setText(mySession.getUser().getUsername());
        userPhoto.setImage(new Image("file:src/main/resources/picture/UserPhoto/"+mySession.getUser().getPhotoUrl()));
        if (mySession.getUserInfo().getBalance() > 0) {
            vip.setImage(new Image("file:src/main/resources/picture/icon/vip.png"));
        }
    }

    public void PersonalInfoClicked() throws IOException {
        App.jump("userInfo");
    }

    public void PhysicalInfoClicked() throws IOException {
        App.jump("userPhy");
    }

    public void ScheduleClicked() throws IOException {
        App.jump("userSchedule");
    }

    public void RechargeClicked() throws IOException {
        App.jump("recharge");
    }

    public void OrderClicked() throws IOException {
        App.jump("order");
    }

    public void timeTableClicked() throws IOException {
        App.jump("userTimeTable");
    }

    public void CollectionClicked() throws IOException {
        App.jump("showCollection");
    }

    public void LogoutClicked() throws IOException{
        mySession = null;
        App.jump("login");
    }

    @FXML
    public void jumpToVideo() throws IOException {
        App.jump("showVideo");
    }

    @FXML
    public void jumpToTrainer() throws IOException {
        App.jump("showTrainer");
    }
}
