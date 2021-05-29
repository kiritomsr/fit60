package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.Schedule;
import com.sixty.Pojo.MySession;
import com.sixty.Service.ScheduleMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class TrainerScheduleController {

    @FXML
    private TableView<Schedule> trainerSchedule;
    @FXML
    private TableColumn<Schedule, String> user;
    @FXML
    private TableColumn<Schedule, String> date;
    @FXML
    private TableColumn<Schedule, String> time;
    @FXML
    private TableColumn<Schedule, String> demand;
    @FXML
    private Button back;

    private MySession mySession = MySession.mySession;


    public void initialize() throws IOException {
        ArrayList<Schedule> schedule2 = ScheduleMapper.findScheduleListByName(mySession.getTrainer().getUsername());
        Collections.sort(schedule2,new UserScheduleController.sortByDateAndTime());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
        Date nowDate = new Date();
        String d = dateFormat.format(nowDate);
        String t = dateFormat2.format(nowDate);
        //String d = "2021-04-16";
        //String t = "10:00";
        ScheduleMapper.deleteUserScheduleBefore(schedule2,d,t);
        final ObservableList<Schedule> data = FXCollections.observableArrayList(schedule2);

        //将数据关联到表格中的列
        user.setCellValueFactory(new PropertyValueFactory<>("username"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        demand.setCellValueFactory(new PropertyValueFactory<>("demand"));
        date.setSortType(TableColumn.SortType.DESCENDING);
        time.setSortType(TableColumn.SortType.ASCENDING);
        trainerSchedule.setItems(data);
    }

    public void goToInformation()throws IOException {
        App.jump("trainerInfo");
    }

    public void goToIntroduction()throws IOException {
        App.jump("trainerIntro");
    }

    public void goToBalance()throws IOException {
        App.jump("trainerBalance");
    }

    public void goToTimeTable()throws IOException {
        App.jump("trainerTimeTable");
    }
}
