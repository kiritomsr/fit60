package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.MySession;
import com.sixty.Service.UserMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TrainerInfoChangeController {

    @FXML private TextField newPhoneNo;
    @FXML private Label phoneNoError;
    @FXML private TextField newEmail;
    @FXML private Label EmailError;
    @FXML private RadioButton male;
    @FXML private RadioButton female;
    @FXML private Label SexError;
    @FXML private Button uploadNewUserPhoto;
    @FXML private ImageView newUserPhoto;
    @FXML private Label upload;
    private final Desktop desktop = Desktop.getDesktop();
    String newPhoto;
    private MySession mySession = MySession.mySession;

    public void changeUserPhoneNo() throws IOException {
        String phoneNo = newPhoneNo.getText();
        if (!(phoneNo.matches("^[0-9]{11}$"))) {
            phoneNoError.setText("Mobile phone number should be 11 digits.");
            phoneNoError.setVisible(true);
        }
        else{
            phoneNoError.setText("Modified successfully");
            UserMapper.changeTrainerPhone(mySession.getTrainer(),phoneNo);
            mySession.refresh(mySession.getTrainer());
        }
    }
    public void changeUserEmail() throws IOException {
        String email = newEmail.getText();
        if (!(email.matches("[a-zA-Z0-9_]{1,12}+@[a-zA-Z]+(\\.[a-zA-Z]+){1,3}"))) {
            EmailError.setText("The mailbox format is incorrect.");
        } else {
            EmailError.setText("Modified successfully");
            UserMapper.changeTrainerEmail(mySession.getTrainer(),email);
            mySession.refresh(mySession.getTrainer());
        }
    }
    public void changeUserSex() throws IOException {
        if ((!(male.isSelected())) && (!(female.isSelected()))) {
            SexError.setText("Please select gender.");
        } else {
            SexError.setText("Modified successfully");
            if (male.isSelected()){
                UserMapper.changeTrainerGender(mySession.getTrainer(),"male");
                mySession.refresh(mySession.getTrainer());
            }
            else {
                UserMapper.changeTrainerGender(mySession.getTrainer(),"female");
                mySession.refresh(mySession.getTrainer());
            }
        }
    }
    public void changeAvata() throws IOException {
        final Stage stage = new Stage();
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./src/main/resources/picture/UserPhoto"));
        uploadNewUserPhoto.setOnAction((final ActionEvent e) -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                openFile(file);
            }
        });
    }

    private void openFile(File file) {
        EventQueue.invokeLater(() -> {
            try {
                desktop.open(file);
                String string = file.toURI().toString();
                Image image = new Image(string);
                newUserPhoto.setImage(image);
                newPhoto = newUserPhoto.getImage().getUrl();
            } catch (IOException ex) {
                Logger.getLogger(RegisterController.
                        class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        });
    }

    public void changeUserPhoto() throws IOException {
        String url = newPhoto.split("/")[newPhoto.split("/").length-1];
        UserMapper.changeTrainerPhoto(mySession.getTrainer(),url);
        mySession.refresh(mySession.getTrainer());
        upload.setText("Modified successfully");
    }

    public void backToPersonalInfo() throws IOException {
        App.jump("trainerInfo");
    }
}
