package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.MySession;
import com.sixty.Pojo.Order;
import com.sixty.Pojo.Schedule;
import com.sixty.Service.OrderMapper;
import com.sixty.Service.ScheduleMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class TrainerBalanceController {

    @FXML private TableView<Order> trainerOrder;
    @FXML private TableColumn<Order, String> orderTime;
    @FXML private TableColumn<Order, String> orderUser;
    @FXML private TableColumn<Order, String> orderPrice;
    @FXML private TableColumn<Order, String> orderState;
    @FXML private Label trainerBalance;

    private MySession mySession = MySession.mySession;


    public void initialize() throws IOException {

        trainerBalance.setText(String.valueOf(mySession.getTrainerInfo().getBalance()));
        ArrayList<Order> orders = OrderMapper.findOrderByObject(mySession.getTrainer().getUsername());

//        Collections.sort(orders,new sortByDateAndTime());
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        DateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
//        Date nowDate = new Date();
//        String d = dateFormat.format(nowDate);
//        String t = dateFormat2.format(nowDate);
        //String d = "2021-04-16";
        //String t = "10:00";
//        orders = ScheduleMapper.deleteUserScheduleBefore(orders,d,t);
        final ObservableList<Order> data = FXCollections.observableArrayList(orders);

        //将数据关联到表格中的列
        orderTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        orderUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        orderPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        orderState.setCellValueFactory(new PropertyValueFactory<>("state"));
        trainerOrder.setItems(data);
    }

    public void goToInformation()throws IOException {
        App.jump("trainerInfo");
    }

    public void goToIntroduction()throws IOException {
        App.jump("trainerIntro");
    }
    public void goToSchedule()throws IOException {
        App.jump("trainerSchedule");
    }
    public void goToTimeTable()throws IOException {
        App.jump("trainerTimeTable");
    }
}
