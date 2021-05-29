package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.MySession;
import com.sixty.Pojo.Schedule;
import com.sixty.Service.ScheduleMapper;
import com.sixty.Service.TrainerInfoMapper;
import com.sixty.Service.UserInfoMapper;
import com.sixty.Service.UserMapper;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

public class TrainerTableController implements Initializable {

    @FXML private GridPane timeTable;
    int page = 0;
    private MySession mySession = MySession.mySession;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        drawGrid();

    }

    public void drawGrid() throws IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-calendar.get(Calendar.DAY_OF_WEEK) + page*7);

        String[] dates = new String[7];

        for (int i=0; i<7; i++){
            calendar.add(Calendar.DATE,1);
            dates[i] = new SimpleDateFormat( "MM.dd").format(calendar.getTime());
            getLable(1, i+1).setText(dates[i]);
        }

        ArrayList<Schedule> schedules = ScheduleMapper.findScheduleListByName(mySession.getTrainer().getUsername());
        ScheduleMapper.deleteUserScheduleCancelled(schedules);
        for(Schedule schedule: schedules){

            String dateSchedule = schedule.getDate().split("-")[1]+"."+schedule.getDate().split("-")[2];
            for(int i=0; i<7; i++){
                if(dateSchedule.equals(dates[i])){
                    String text = schedule.getUsername()+"\n"+schedule.getType();

                    Label label = new Label(text);
                    label.setStyle("-fx-font-family: 'Comic Sans MS';-fx-font-size: 18;");
                    label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @SneakyThrows
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            mySession.setUser(UserMapper.findUserByName(schedule.getUsername()));
                            mySession.setUserInfo(UserInfoMapper.findMemberCenterInfoByName(schedule.getUsername()));
                            App.jump("trainerPhy");
                        }
                    });
                    String time = schedule.getTime();
                    int j;
                    if(time.equals("09:00-11:00")) j=0;
                    else if(time.equals("14:00-16:00")) j=1;
                    else j=2;
                    timeTable.add(label, i+1, j+2);
                    break;
                }
            }

        }
    }

    @FXML
    public void lastWeek() throws IOException {
        page = page-1;
        deleteLable();
        drawGrid();
    }

    @FXML
    public void nextWeek() throws IOException {
        page = page+1;
        deleteLable();
        drawGrid();
    }

    public Label getLable(final int row, final int column) {
        int x,y;
        for (Node node : timeTable.getChildren()) {

                x = GridPane.getRowIndex(node)==null?0:GridPane.getRowIndex(node);
                y = GridPane.getColumnIndex(node)==null?0:GridPane.getColumnIndex(node);
                Label label = (Label) node;
                if(x==row && y==column) return label;
        }
        return null;
    }

    public void deleteLable() {
        timeTable.getChildren().removeIf(node ->
                node instanceof Label && GridPane.getRowIndex(node)!=null && GridPane.getRowIndex(node)>=2 && GridPane.getColumnIndex(node)!=null && GridPane.getColumnIndex(node)>=1
        );

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
    public void goToBalance()throws IOException {
        App.jump("trainerBalance");
    }
}
