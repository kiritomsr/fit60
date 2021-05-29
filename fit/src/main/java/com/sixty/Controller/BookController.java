package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.MySession;
import com.sixty.Pojo.Order;
import com.sixty.Pojo.Schedule;
import com.sixty.Pojo.TrainerInfo;
import com.sixty.Service.OrderMapper;
import com.sixty.Service.TrainerInfoMapper;
import com.sixty.Service.UserInfoMapper;
import com.sixty.Service.ScheduleMapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

public class BookController implements Initializable {
    @FXML
    private Button back;
    @FXML
    private Label bookInfo;
    @FXML
    private Label price;
    @FXML
    private Label balance;
    @FXML
    private TextArea demand;
    @FXML
    private DatePicker date;
    @FXML
    private ChoiceBox time;
    @FXML
    private Button recharge;
    private TrainerInfo trainerInfo;
    private MySession mySession = MySession.mySession;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        trainerInfo = mySession.getTrainerInfo();
        price.setText(trainerInfo.getPrice() * trainerInfo.getDiscount() + "");

        balance.setText(mySession.getUserInfo().getBalance()+"");

        date.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @SneakyThrows
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
                chooseDate();
            }
        });
    }

    public void bookAndPay() throws IOException{

        if((mySession.getUserInfo().getBalance()-trainerInfo.getPrice() * trainerInfo.getDiscount())<=0) {
            bookInfo.setText("Your balance isn't enough, please go to recharge.");
            return;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowDate = new Date();
        String bookTime = dateFormat.format(nowDate);
        Schedule schedule = new Schedule();
        schedule.setDate(date.getValue()+"");
        schedule.setTime(time.getValue()+"");
        schedule.setUsername(mySession.getUser().getUsername());
        schedule.setTrainerName(mySession.getTrainerInfo().getTrainerName());
        schedule.setDemand(demand.getText());
        schedule.setType(mySession.getTrainerInfo().getType());
        schedule.setOrderTime(bookTime);
        schedule.setState("submit");
        ScheduleMapper.addSchedule(schedule);
        bookInfo.setText("Book successfully.");

        balance.setText((mySession.getUserInfo().getBalance() - trainerInfo.getPrice() * trainerInfo.getDiscount()) + "");
        UserInfoMapper.recharge(mySession.getUserInfo(), -trainerInfo.getPrice()*trainerInfo.getDiscount());
        mySession.refresh(mySession.getUserInfo());

        TrainerInfoMapper.recharge(mySession.getTrainerInfo(), trainerInfo.getPrice() * trainerInfo.getDiscount());
        mySession.refresh(mySession.getTrainerInfo());

        Order order = new Order();
        order.setUsername(mySession.getUser().getUsername());
        order.setTime(bookTime);
        order.setType("book");
        order.setObject(mySession.getTrainer().getUsername());
        order.setPrice(trainerInfo.getPrice()*trainerInfo.getDiscount());
        order.setState("paid");
        OrderMapper.addOrder(order);

        String[] bookTimeNew = {"09:00-11:00","14:00-16:00","19:00-21:00"};
        ArrayList<String> bookTime2 = new ArrayList<>(Arrays.asList(bookTimeNew));
        bookTime2.remove(time.getValue());
        time.setItems(FXCollections.observableArrayList(bookTime2));

    }

    public void chooseDate() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
        Date nowDate = new Date();
        String d = dateFormat.format(nowDate);
        String t = dateFormat2.format(nowDate);

        String[] bookTime = {"09:00-11:00","14:00-16:00","19:00-21:00"};
        ArrayList<String> bookTime2 = new ArrayList<String>(Arrays.asList(bookTime));

        String chooseDate = date.getValue()+"";
        ArrayList<Schedule> trainerScheduleList = ScheduleMapper.findScheduleListByNameAndDate(mySession.getTrainer().getUsername(), chooseDate);
        ScheduleMapper.deleteUserScheduleCancelled(trainerScheduleList);
        ArrayList<Schedule> scheduleList = ScheduleMapper.findScheduleListByNameAndDate(mySession.getUser().getUsername(),chooseDate);
        ScheduleMapper.deleteUserScheduleCancelled(scheduleList);

        if(chooseDate.compareTo(d)<0){
            time.setItems(FXCollections.observableArrayList());
        }
        //System.out.println(chooseDate.compareTo(d));
         else if(chooseDate.compareTo(d)==0){
             System.out.println(".........."+t);
            if(t.compareTo("09:00")<0){
                time.setItems(FXCollections.observableArrayList(bookTime));
            }
            if(t.compareTo("09:00")>=0&&t.compareTo("14:00")<0){
                time.setItems(FXCollections.observableArrayList("14:00-16:00","19:00-21:00"));
            }
            if(t.compareTo("14:00")>=0&&t.compareTo("21:00")<0){
                time.setItems(FXCollections.observableArrayList("19:00-21:00"));
            }
            if(t.compareTo("21")>=0){
                time.setItems(FXCollections.observableArrayList());
            }
        }
        else if(trainerScheduleList.size() != 3|| scheduleList.size() != 3){
            //System.out.println("trainer");
            for(int i=0;i<trainerScheduleList.size();i++){
                bookTime2.remove(trainerScheduleList.get(i).getTime());
            }
            for(int i = 0; i< scheduleList.size(); i++){
                System.out.println(scheduleList.get(i).getTime());
                bookTime2.remove(scheduleList.get(i).getTime());
            }
            //System.out.println(bookTime2);
            time.setItems(FXCollections.observableArrayList(bookTime2));
        }

    }

    public void backToTrainerDetail() throws IOException{
        App.jump("trainerDetail");
    }

    public void rechargeClicked() throws IOException{
        App.jump("recharge");
    }
}
