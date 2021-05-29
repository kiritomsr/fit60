package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.MySession;
import com.sixty.Pojo.Order;
import com.sixty.Pojo.Schedule;
import com.sixty.Service.OrderMapper;
import com.sixty.Service.ScheduleMapper;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class UserOrderController implements Initializable {
    @FXML private Button back;

    @FXML private TableColumn<Order,String> time;
    @FXML private TableColumn<Order,String> type;
    @FXML private TableColumn<Order, Float> amount;
    @FXML private TableColumn<Order,String> state;
    @FXML private TableView<Order> tableView;

    private MySession mySession = MySession.mySession;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Order> orders = OrderMapper.findOrderByName(mySession.getUser().getUsername());


        final ObservableList<Order> data = FXCollections.observableArrayList(orders);

        //将数据关联到表格中的列
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        amount.setCellValueFactory(new PropertyValueFactory<>("price"));
        state.setCellValueFactory(new PropertyValueFactory<>("state"));

        tableView.setItems(data);

    }

    @FXML
    public void backToIndex() throws IOException {
        App.jump("showCenter");
    }
}
