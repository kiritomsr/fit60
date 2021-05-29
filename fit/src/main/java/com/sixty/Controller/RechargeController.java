package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.MySession;
import com.sixty.Pojo.Order;
import com.sixty.Service.OrderMapper;
import com.sixty.Service.UserInfoMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RechargeController {
    @FXML
    private Label balance;
    @FXML
    private Label balanceInfo;
    @FXML
    private RadioButton recharge1;
    @FXML
    private RadioButton recharge2;
    @FXML
    private RadioButton recharge3;
    @FXML
    private RadioButton recharge4;

    private MySession mySession = MySession.mySession;

    @FXML
    public void BackClicked() throws IOException{
        App.jump("showCenter");
    }
    @FXML
    public void RechargeClicked() throws IOException{

        float recharge;
        if(!(recharge1.isSelected()||recharge2.isSelected()||recharge3.isSelected()||recharge4.isSelected())){
            balanceInfo.setTextFill(Paint.valueOf("#e51616"));
            return;
        }
        else if(recharge1.isSelected()) recharge = 68;
        else if(recharge2.isSelected()) recharge = 168;
        else if(recharge3.isSelected()) recharge = 328;
        else if(recharge4.isSelected()) recharge = 648;
        else return;

        UserInfoMapper.recharge(mySession.getUserInfo(), recharge);

        mySession.refresh(mySession.getUserInfo());
        balanceInfo.setTextFill(Paint.valueOf("#3ae8c8"));
        balanceInfo.setText("Recharge successfully.");
        balance.setText(mySession.getUserInfo().getBalance()+"");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowDate = new Date();
        String bookTime = dateFormat.format(nowDate);

        Order order = new Order();
        order.setUsername(mySession.getUser().getUsername());
        order.setTime(bookTime);
        order.setType("recharge");
        order.setObject("System");
        order.setPrice(recharge);
        order.setState("paid");
        OrderMapper.addOrder(order);

    }


    public void initialize() {
        balance.setText(mySession.getUserInfo().getBalance()+"");
    }
}
