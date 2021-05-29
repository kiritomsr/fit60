package com.sixty.Controller;
import com.sixty.App;
import com.sixty.Pojo.*;
import com.sixty.Service.UserInfoMapper;
import com.sixty.Service.TrainerInfoMapper;
import com.sixty.Service.UserMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;



public class RegisterController {
    //JFileChooser
    @FXML private TextField registerUsername;
    @FXML private PasswordField registerPassword;
    @FXML private PasswordField passwordConfirm;
    @FXML private TextField registerPhone;
    @FXML private TextField registerEmail;
    @FXML private RadioButton male;
    @FXML private RadioButton female;
    @FXML private RadioButton memberID;
    @FXML private RadioButton trainerID;
    @FXML private Button registerButton;
    @FXML private Label label1;
    @FXML private Label label2;
    @FXML private Label label3;
    @FXML private Label label4;
    @FXML private Label label5;
    @FXML private Label label6;
    @FXML private Label label7;
    @FXML private Button uploadUserPhoto;
    @FXML private ImageView userPhoto;
    private boolean uploadPhoto = false;


    @FXML
    public void loginButtonClicked() throws Exception {

        String username = registerUsername.getCharacters().toString();
        String password1 = registerPassword.getCharacters().toString();
        String password2 = passwordConfirm.getCharacters().toString();
        String phone = registerPhone.getCharacters().toString();
        String email = registerEmail.getCharacters().toString();

        User user = UserMapper.findUserByName(registerUsername.getCharacters().toString());
        Trainer trainer = UserMapper.findTrainerByName(registerUsername.getCharacters().toString());

        String usernameRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{2,100}$";
        String regex = "[a-zA-Z0-9_]{1,12}+@[a-zA-Z]+(\\.[a-zA-Z]+){1,3}";//邮箱格式

        boolean i1= false,i2= false,i3= false, i4= false,i5= false,i6= false,i7= false;

        if ( username.equals("")) {
            label1.setText("Please enter username.");
            label1.setVisible(true);
        }else if (!(username.matches(usernameRegex))) {
            label1.setText("Username is a combination of letters and numbers. Please enter again.");
            label1.setVisible(true);
        } else {
            i1=true;
            label1.setText("");
        }

        if (password1.equals("")) {
            label2.setText("Please enter password:");
            label2.setVisible(true);
        } else if (!(password1.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])[a-zA-Z0-9]{6,20}$"))) {
            label2.setText("Password is a combination of 6 to 20 lowercase letters, uppercase letters and numbers.");
            label2.setVisible(true);
        } else {
            i2=true;
            label2.setText("");
        }

        if (password2.equals("")) {
            label3.setText("Please enter the password for the second time.");
            label3.setVisible(true);
        } else if (!(password1.equals(password2))) {
            label3.setText("The passwords entered twice are inconsistent. Please enter them again.");
            label3.setVisible(true);
        } else {
            i3=true;
            label3.setText("");
        }

        if (phone.equals("")) {
            label4.setText("Please enter the telephone number.");
            label4.setVisible(true);
        } else if (!(phone.matches("^[0-9]{11}$"))) {
            label4.setText("Mobile phone number should be 11 digits, please enter again.");
            label4.setVisible(true);
        } else {
            i4=true;
            label4.setText("");
        }

        if (email.equals("")) {
            label5.setText("Please enter a mailbox.");
            label5.setVisible(true);
        } else if (!(email.matches(regex))) {
            label5.setText("The mailbox format is incorrect. Please enter again.");
            label5.setVisible(true);
        } else {
            i5=true;
            label5.setText("");
        }

        if ((!(male.isSelected())) && (!(female.isSelected()))) {
            label6.setText("Please select gender.");
            label6.setVisible(true);
        } else {
            i6=true;
            label6.setText("");
        }

        if ((!(memberID.isSelected())) && (!(trainerID.isSelected()))) {
            label7.setText("Please select a user identity.");
            label7.setVisible(true);
        }else if (memberID.isSelected()&&(user != null)) {
            label1.setText("Username already exists, please re-enter.");
            label1.setVisible(true);
        } else if (trainerID.isSelected()&&(trainer!=null)) {
            label1.setText("Username already exists, please re-enter.");
            label1.setVisible(true);
        } else {
            i7=true;
            label1.setText("");
            label7.setText("");
        }

        if(i1&&i2&&i3&&i4&&i5&&i6&&i7&&uploadPhoto){
            if(memberID.isSelected()){
                User newUser = new User();
                if (male.isSelected()){
                    newUser.setSex("male");
                }
                else {
                    newUser.setSex("female");
                }
                newUser.setUsername(username);
                newUser.setPhone(phone);
                newUser.setPassword(password1);
                newUser.setEmail(email);

                String url = userPhoto.getImage().getUrl().split("/")[userPhoto.getImage().getUrl().split("/").length-1];
                newUser.setPhotoUrl(url);
                UserMapper.addUser(newUser);
                UserInfo newUserInfo = new UserInfo();
                newUserInfo.setUsername(username);

                UserInfoMapper.addMemberCenterInfo(newUserInfo);
                App.jump("login");

            }
            else{
                Trainer newTrainer = new Trainer();
                if (male.isSelected()){
                    newTrainer.setSex("male");
                }
                else {
                    newTrainer.setSex("female");
                }
                newTrainer.setUsername(username);
                newTrainer.setPhone(phone);
                newTrainer.setPassword(password1);
                newTrainer.setEmail(email);
                newTrainer.setPhotoUrl(userPhoto.getImage().getUrl());
                UserMapper.addTrainer(newTrainer);

                TrainerInfo newTrainerInfo = new TrainerInfo();
                newTrainerInfo.setTrainerName(username);
                TrainerInfoMapper.addTrainerIntro(newTrainerInfo);
                App.jump("login");
            }
        }
    }

    @FXML
    public void uploadUserPhotoClicked() throws Exception{
    final Stage stage = new Stage();
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./src/main/resources/picture/UserPhoto"));
        uploadUserPhoto.setOnAction((final ActionEvent e) -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                openFile(file);
            }
        });
    }

    private void openFile(File file) {
        EventQueue.invokeLater(() -> {
            try {
                Desktop.getDesktop().open(file);
                String string = file.toURI().toString();
                Image image = new Image(string);
                userPhoto.setImage(image);
                uploadPhoto = true;
            } catch (IOException ex) {
                Logger.getLogger(RegisterController.
                        class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        });
    }

    @FXML
    public void backButtonClicked() throws IOException {
        App.jump("login");
    }
}