package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.Order;
import com.sixty.Pojo.User;
import com.sixty.Service.OrderMapper;
import com.sixty.Service.UserMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.Data;
import lombok.SneakyThrows;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class AdminIncomeController implements Initializable {

    @FXML private AnchorPane anchorPane;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis,yAxis);
        barChart.setTitle("Income Statistics");
        barChart.setStyle("-fx-font-family: 'Comic Sans MS';-fx-font-size: 16;");
        barChart.setLayoutX(100);
        barChart.setLayoutY(70);
        barChart.setPrefWidth(850);
        barChart.setPrefHeight(500);
        xAxis.setLabel("Date");
        yAxis.setLabel("Value");
        //xAxis.setTickLabelFill(Color.valueOf("#47e5ce"));
        xAxis.setTickLabelFill(Color.BLUE);
        xAxis.setTickLabelFont(Font.font("Comic Sans MS"));
        yAxis.setTickLabelFill(Color.BLUE);
        yAxis.setTickLabelFont(Font.font("Comic Sans MS"));
        XYChart.Series rechargeSeries = new XYChart.Series();
        rechargeSeries.setName("recharge");

        XYChart.Series bookSeries = new XYChart.Series();
        bookSeries.setName("book");

        ArrayList<Order> orders = OrderMapper.getOrderList();
        @Data class DateIncome{
            private String date;
            private float recharge=0;
            private float book=0;
        }
        ArrayList<DateIncome> dateIncomes = new ArrayList<>();

        for(Order order: orders){
            String date = order.getTime().split(" ")[0].split("-")[1]+"."+order.getTime().split(" ")[0].split("-")[2];
            boolean newObj = true;
            for (DateIncome dateIncome: dateIncomes){
                if(dateIncome.getDate().equals(date)) {
                    if(order.getType().equals("recharge")) dateIncome.setRecharge(dateIncome.getRecharge()+order.getPrice());
                    if(order.getType().equals("book")) dateIncome.setBook(dateIncome.getBook()+order.getPrice());
                    newObj = false;
                }
            }
            if(newObj){
                DateIncome dateIncome = new DateIncome();
                dateIncome.setDate(date);

                if(order.getType().equals("recharge")) dateIncome.setRecharge(dateIncome.getRecharge()+order.getPrice());
                if(order.getType().equals("book")) dateIncome.setBook(dateIncome.getBook()+order.getPrice());

                dateIncomes.add(dateIncome);
            }
        }

        for (DateIncome dateIncome: dateIncomes){
            bookSeries.getData().add(new XYChart.Data(dateIncome.getDate(), dateIncome.getBook()));
            rechargeSeries.getData().add(new XYChart.Data(dateIncome.getDate(), dateIncome.getRecharge()));
        }
        barChart.getData().addAll(rechargeSeries, bookSeries);

        anchorPane.getChildren().add(barChart);

    }

    public void jumpToMemberInfo() throws IOException{
        App.jump("adminMemberInfo");
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
    public void adminLogout() throws IOException {
        App.jump("login");
    }
}
