package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.MySession;
import com.sixty.Pojo.Trainer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class TrainerInfoController {
    @FXML private Label trainerName;
    @FXML private Label trainerPhone;
    @FXML private Label trainerEmail;
    @FXML private Label trainerSex;
    @FXML private ImageView trainerPhoto;

    private MySession mySession = MySession.mySession;

    public void initialize() {
        Trainer trainer = mySession.getTrainer();
        trainerName.setText(trainer.getUsername());
        trainerPhone.setText(trainer.getPhone());
        trainerEmail.setText(trainer.getEmail());
        trainerSex.setText(trainer.getSex());
        trainerPhoto.setImage(new Image("file:src/main/resources/picture/TrainerPhoto/"+ trainer.getPhotoUrl()));
    }

    public void changeInformation()throws IOException {
        App.jump("trainerInfoChange");
    }

    public void goToIntroduction()throws IOException {
        App.jump("trainerIntro");
    }

    public void goToSchedule()throws IOException {
        App.jump("trainerSchedule");
    }

    public void goToBalance()throws IOException {
        App.jump("trainerBalance");
    }
    public void goToTimeTable()throws IOException {
        App.jump("trainerTimeTable");
    }
    public void LogoutClicked() throws IOException{
        mySession = null;
        App.jump("login");
    }

}
