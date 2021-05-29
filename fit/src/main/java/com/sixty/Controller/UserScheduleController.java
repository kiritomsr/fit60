package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.Order;
import com.sixty.Pojo.Schedule;
import com.sixty.Pojo.MySession;
import com.sixty.Service.OrderMapper;
import com.sixty.Service.ScheduleMapper;
import com.sixty.Service.TrainerInfoMapper;
import com.sixty.Service.UserInfoMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserScheduleController implements Initializable {
    @FXML private Button back;
    @FXML private TableColumn<Schedule,String> trainer;
    @FXML private TableColumn<Schedule,String> date;
    @FXML private TableColumn<Schedule,String> time;
    @FXML private TableColumn<Schedule,String> type;
    @FXML private TableColumn<Schedule,String> demand;
    @FXML private TableColumn<Schedule,String> state;
    @FXML private TableColumn<Schedule, String> edit;
    @FXML private TableView<Schedule> scheduleTableView;

    private MySession mySession = MySession.mySession;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drawTable();
    }

    public void drawTable() throws IOException {
        ArrayList<Schedule> schedules = ScheduleMapper.findScheduleListByName(mySession.getUser().getUsername());

        schedules.sort(new sortByDateAndTime());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
        Date nowDate = new Date();
        String d = dateFormat.format(nowDate);
        String t = dateFormat2.format(nowDate);
        //String d = "2021-04-16";
        //String t = "10:00";
        ScheduleMapper.deleteUserScheduleBefore(schedules, d, t);
        ScheduleMapper.deleteUserScheduleCancelled(schedules);

        final ObservableList<Schedule> data = FXCollections.observableArrayList(schedules);

        //将数据关联到表格中的列
        trainer.setCellValueFactory(new PropertyValueFactory<>("trainerName"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        demand.setCellValueFactory(new PropertyValueFactory<>("demand"));
        state.setCellValueFactory(new PropertyValueFactory<>("state"));

        scheduleTableView.setItems(data);

        //cancel schedule
        edit.setCellFactory(col->{
            return new TableCell<Schedule, String>() {
                @Override
                protected void updateItem(String s, boolean b) {
                    super.updateItem(s, b);
                    int row = this.getIndex();
                    Button button = new Button("Cancel");
                    button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @SneakyThrows
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            Schedule schedule = data.get(row);
                            Order order = OrderMapper.findOrderByNameAndTime(schedule.getUsername(), schedule.getOrderTime());
                            ScheduleMapper.cancelSchedule(schedule);
                            OrderMapper.cancelOrder(order);
                            UserInfoMapper.recharge(mySession.getUserInfo(), order.getPrice());
                            mySession.refresh(mySession.getUserInfo());

                            TrainerInfoMapper.recharge(TrainerInfoMapper.findTrainerIntroByName(order.getObject()), -mySession.getTrainerInfo().getPrice());

                            drawTable();
                        }
                    });
                    if(b){
                        setText(null);
                        setGraphic(null);
                    } else {
                        this.setGraphic(button);
                    }
                }
            };
        });
    }

    @FXML
    public void backToIndex() throws IOException {
        App.jump("showCenter");
    }

    static class sortByDateAndTime implements Comparator{

        @Override
        public int compare(Object o1, Object o2) {
            Schedule m1=(Schedule)o1;
            Schedule m2=(Schedule)o2;
            if(m1.getDate().equals(m2.getDate())){
                return m1.getTime().compareTo(m2.getTime());
            }
            else return m1.getDate().compareTo(m2.getDate());
        }
    }
}
