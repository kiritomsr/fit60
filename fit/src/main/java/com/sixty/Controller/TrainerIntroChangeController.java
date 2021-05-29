package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.MySession;
import com.sixty.Pojo.TrainerInfo;
import com.sixty.Service.TrainerInfoMapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TrainerIntroChangeController implements Initializable {
    @FXML
    private TextField name;
    @FXML
    private ChoiceBox<String> type;
    @FXML
    private TextArea profile;
    @FXML
    private Button back;
    @FXML
    private Button save;

    private MySession mySession = MySession.mySession;
    public void backToIntroduction() throws IOException{
        App.jump("trainerIntro");
    }

    @FXML
    public void backToTrainerIntroduction() throws IOException{
        TrainerInfo trainerInfo = mySession.getTrainerInfo();
        TrainerInfoMapper.deleteTrainerIntro(trainerInfo);

        trainerInfo.setTrainerName(mySession.getTrainer().getUsername());
        trainerInfo.setRealName(name.getText());
        trainerInfo.setType(type.getValue()+"");
        trainerInfo.setProfile(profile.getText());

        TrainerInfoMapper.addTrainerIntro(trainerInfo);

        App.jump("trainerIntro");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        type.setItems(FXCollections.observableArrayList("HIIT","Strength","Yoga","Run"));
    }
}
