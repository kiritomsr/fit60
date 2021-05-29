package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.Trainer;
import com.sixty.Pojo.TrainerInfo;
import com.sixty.Service.TrainerInfoMapper;
import com.sixty.Service.UserMapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminTrainerInfoController implements Initializable {

    @FXML private Label trainerAccount;
    @FXML private Label trainerInformation;
    @FXML private TableView<Trainer> trainerTable;
    @FXML private TableColumn<Trainer, String> usernameT;
    @FXML private TableColumn<Trainer, String> phoneT;
    @FXML private TableColumn<Trainer, String> emailT;
    @FXML private TableColumn<Trainer, String> genderT;

    @FXML private TableView<TrainerInfo> trainerTableInfo;
    @FXML private TableColumn<TrainerInfo, String> usernameCol;
    @FXML private TableColumn<TrainerInfo, String> realNameCol;
    @FXML private TableColumn<TrainerInfo, String> typeCol;
    @FXML private TableColumn<TrainerInfo, Float> balanceCol;
    @FXML private TableColumn<TrainerInfo, Float> priceCol;
    @FXML private TableColumn<TrainerInfo, Float> discountCol;
    @FXML private TableColumn<TrainerInfo, String> profileCol;

    EventHandler<MouseEvent> trainerTableClick;
    EventHandler<KeyEvent> trainerTableEnter;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showTrainer();
    }

    public void showTrainer() throws Exception {
        ObservableList<Trainer> trainerObservableList = FXCollections.observableArrayList(UserMapper.getTrainerList());
        trainerTable.setItems(trainerObservableList);
        trainerTable.setEditable(true);
        usernameT.setCellValueFactory(new PropertyValueFactory("username"));
        phoneT.setCellValueFactory(new PropertyValueFactory("phone"));
        emailT.setCellValueFactory(new PropertyValueFactory("email"));
        genderT.setCellValueFactory(new PropertyValueFactory("sex"));

        class EditingCell extends TableCell<Trainer, String> {
            private TextField textField;
            private TrainerInfo trainerInfo;
            private boolean getPrice;
            private float price;

            @SneakyThrows
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                int row = this.getIndex();
                if(!getPrice){
                    if(row>=trainerObservableList.size() || row <= 0) return;
                    this.trainerInfo = TrainerInfoMapper.findTrainerIntroByName(trainerObservableList.get(row).getUsername());
                    this.price = trainerInfo.getPrice();
                    System.out.println(trainerObservableList.get(row).getUsername()+": "+price);

                    getPrice = true;
                }
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        if (textField != null) {
                            textField.setText(String.valueOf(price));
                        }
                        setText(null);
                        setGraphic(textField);
                    } else {
                        setText(String.valueOf(price));
                        setGraphic(null);
                    }
                }
            }

            @Override
            public void startEdit() {
                if (!isEmpty()) {
                    super.startEdit();
                    createTextField();
                    setText(null);
                    setGraphic(textField);
                    textField.setText(String.valueOf(price));
                    textField.selectAll();
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(String.valueOf(price));
                setGraphic(null);
            }

            @SneakyThrows
            @Override
            public void commitEdit(String s) {
                super.commitEdit(s);
                price = Float.parseFloat(textField.getText());
//                TrainerInfoMapper.changeTrainerPrice(trainerInfo, price);
                setText(String.valueOf(price));
                setGraphic(null);
            }

            private void createTextField() {
                textField = new TextField(String.valueOf(price));
                textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
                textField.focusedProperty().addListener(
                        (ObservableValue<? extends Boolean> arg0,
                         Boolean arg1, Boolean arg2) -> {
                            if (!arg2) {
                                commitEdit(textField.getText());
                            }
                        });
            }
        }

//        priceT.setCellFactory((TableColumn<Trainer,String> p) -> new EditingCell());
    }

    public void showTrainerInfo() throws Exception {
        ObservableList<TrainerInfo> trainerObservableList = FXCollections.observableArrayList(TrainerInfoMapper.getTrainerIntroList());
        trainerTableInfo.setItems(trainerObservableList);
        trainerTableInfo.setEditable(true);

        usernameCol.setCellValueFactory(new PropertyValueFactory("trainerName"));
        realNameCol.setCellValueFactory(new PropertyValueFactory("realName"));
        typeCol.setCellValueFactory(new PropertyValueFactory("type"));
        balanceCol.setCellValueFactory(new PropertyValueFactory("balance"));
        priceCol.setCellValueFactory(new PropertyValueFactory("price"));
        discountCol.setCellValueFactory(new PropertyValueFactory("discount"));
        profileCol.setCellValueFactory(new PropertyValueFactory("profile"));

        class EditingCell extends TableCell<TrainerInfo, Float> {
            private TextField textField;
            private TrainerInfo trainerInfo;
            private boolean getPrice;
            private float price;

            @SneakyThrows
            @Override
            public void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                int row = this.getIndex();
                if(!getPrice){
                    if(row>=trainerObservableList.size() || row < 0) return;
                    this.trainerInfo = trainerObservableList.get(row);
                    this.price = trainerInfo.getPrice();
                    getPrice = true;
                }
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        if (textField != null) {
                            textField.setText(String.valueOf(price));
                        }
                        setText(null);
                        setGraphic(textField);
                    } else {
                        setText(String.valueOf(price));
                        setGraphic(null);
                    }
                }
            }

            @Override
            public void startEdit() {
                if (!isEmpty()) {
                    super.startEdit();
                    createTextField();
                    setText(null);
                    setGraphic(textField);
                    textField.setText(String.valueOf(price));
                    textField.selectAll();
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(String.valueOf(price));
                setGraphic(null);
            }

            @SneakyThrows
            @Override
            public void commitEdit(Float s) {
                super.commitEdit(Float.valueOf(s));
                price = Float.parseFloat(textField.getText());
                TrainerInfoMapper.changeTrainerPrice(trainerInfo, price);
                System.out.println(price);
                setText(String.valueOf(price));
                setGraphic(null);
            }

            private void createTextField() {
                textField = new TextField(String.valueOf(price));
                textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
                textField.focusedProperty().addListener(
                        (ObservableValue<? extends Boolean> arg0,
                         Boolean arg1, Boolean arg2) -> {
                            if (!arg2) {
                                commitEdit(Float.valueOf(textField.getText()));
                            }
                        });
            }
        }
        priceCol.setCellFactory((TableColumn<TrainerInfo,Float> p) -> new EditingCell());

        class EditingCell2 extends TableCell<TrainerInfo, Float> {
            private TextField textField;
            private TrainerInfo trainerInfo;
            private boolean getDiscount;
            private float discount;

            @SneakyThrows
            @Override
            public void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                int row = this.getIndex();
                if(!getDiscount){
                    if(row>=trainerObservableList.size() || row < 0) return;
                    this.trainerInfo = trainerObservableList.get(row);
                    this.discount = trainerInfo.getDiscount();
                    getDiscount = true;
                }
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        if (textField != null) {
                            textField.setText(String.valueOf(discount));
                        }
                        setText(null);
                        setGraphic(textField);
                    } else {
                        setText(String.valueOf(discount));
                        setGraphic(null);
                    }
                }
            }

            @Override
            public void startEdit() {
                if (!isEmpty()) {
                    super.startEdit();
                    createTextField();
                    setText(null);
                    setGraphic(textField);
                    textField.setText(String.valueOf(discount));
                    textField.selectAll();
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(String.valueOf(discount));
                setGraphic(null);
            }

            @SneakyThrows
            @Override
            public void commitEdit(Float s) {
                super.commitEdit(Float.valueOf(s));
                discount = Float.parseFloat(textField.getText());
                TrainerInfoMapper.changeTrainerDiscount(trainerInfo, discount);
                System.out.println(discount);
                setText(String.valueOf(discount));
                setGraphic(null);
            }

            private void createTextField() {
                textField = new TextField(String.valueOf(discount));
                textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
                textField.focusedProperty().addListener(
                        (ObservableValue<? extends Boolean> arg0,
                         Boolean arg1, Boolean arg2) -> {
                            if (!arg2) {
                                commitEdit(Float.valueOf(textField.getText()));
                            }
                        });
            }
        }
        discountCol.setCellFactory((TableColumn<TrainerInfo,Float> p) -> new EditingCell2());
    }

    @FXML
    public void trainerAccount() throws Exception {
        trainerAccount.setTextFill(Color.web("#47e5ce"));
        trainerInformation.setTextFill(Color.WHITE);
        showTrainer();
        trainerTable.setVisible(true);
        trainerTableInfo.setVisible(false);
    }

    @FXML
    public void trainerInformation() throws Exception {
        trainerInformation.setTextFill(Color.web("#47e5ce"));
        trainerAccount.setTextFill(Color.WHITE);
        showTrainerInfo();
        trainerTable.setVisible(false);
        trainerTableInfo.setVisible(true);
    }

    public void jumpToMemberInfo() throws IOException {
        App.jump("adminMemberInfo");
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
