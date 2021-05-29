package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.MySession;
import com.sixty.Pojo.TrainerInfo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class TrainerIntroController {
    @FXML
    private Label name;
    @FXML
    private Label type;
    @FXML
    private Label profile;
    @FXML
    private Button change;

    private MySession mySession = MySession.mySession;

    public void initialize(){
        TrainerInfo trainerInfo = mySession.getTrainerInfo();
        name.setText(trainerInfo.getRealName());
        type.setText(trainerInfo.getType());
        profile.setText(trainerInfo.getProfile());
    }
    public void changeIntroduction() throws IOException {
        Stage primaryStage=(Stage)change.getScene().getWindow();
        App.jump("trainerIntroChange");
    }

    public void goToInformation()throws IOException {
        App.jump("trainerInfo");
    }

    public void goToSchedule()throws IOException {
        App.jump("trainerSchedule");
    }
    public void goToTimeTable()throws IOException {
        App.jump("trainerTimeTable");
    }
    public void goToBalance()throws IOException {
        App.jump("trainerBalance");
    }
}
