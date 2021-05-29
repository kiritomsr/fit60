package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.MySession;
import com.sixty.Pojo.TrainerInfo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.io.IOException;

public class TrainerDetailController {
    @FXML public Label name;
    @FXML public Label type;
    @FXML public Label profile;
    @FXML public Label price;
    @FXML public Label price1;
    @FXML public Rectangle discountLine;
    @FXML public ImageView photo;
    @FXML public Button back;
    @FXML public Button bookButton;

//    private SourceDataViewModel viewModel = SourceDataViewModel.getInstance();
private MySession mySession = MySession.mySession;

    public void initialize(){
        TrainerInfo trainerInfo = mySession.getTrainerInfo();
        name.setText(trainerInfo.getRealName());
        type.setText(trainerInfo.getType());
        profile.setText(trainerInfo.getProfile());
        price.setText(trainerInfo.getPrice()+"");

        if(trainerInfo.getDiscount()!=1.0){
            discountLine.setVisible(true);
            price1.setText(trainerInfo.getPrice()*trainerInfo.getDiscount()+"");
        }

        photo.setImage(new Image("file:src/main/resources/picture/TrainerPhoto/"+ mySession.getTrainer().getPhotoUrl()));
    }
    public void bookTrainer() throws IOException{
        App.jump("bookTrainer");
    }
    public void backToIndex() throws IOException{
        App.jump("showTrainer");
    }

}
