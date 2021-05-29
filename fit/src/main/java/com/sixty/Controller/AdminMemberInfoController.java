package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.User;
import com.sixty.Service.UserMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMemberInfoController implements Initializable {
    @FXML
    private TableView<User> memberTable;
    @FXML private TableColumn<User, String> usernameM;
    @FXML private TableColumn<User, String> phoneM;
    @FXML private TableColumn<User, String> emailM;
    @FXML private TableColumn<User, String> genderM;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showMember();
    }
    public void showMember() throws Exception {
        ObservableList<User> userObservableList = FXCollections.observableArrayList(UserMapper.getUserList());
        memberTable.setItems(userObservableList);
        usernameM.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        phoneM.setCellValueFactory(new PropertyValueFactory<User, String>("phone"));
        emailM.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        genderM.setCellValueFactory(new PropertyValueFactory<User, String>("sex"));
    }
    public void jumpToTrainerInfo() throws IOException{
        App.jump("adminTrainerInfo");
    }
    public void jumpToAddVideo() throws IOException{
        App.jump("adminAddVideo");
    }
    public void jumpToVideoManagement() throws IOException{
        App.jump("adminVideo");
    }
    public void jumpToIncome() throws IOException{
        App.jump("adminIncome");
    }
    public void adminLogout() throws IOException {
        App.jump("login");
    }
}
