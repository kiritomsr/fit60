package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.UserInfo;
import com.sixty.Pojo.MySession;
import com.sixty.Service.UserInfoMapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserPhyController implements Initializable {
    @FXML private Label userHeight;
    @FXML private Label userWeight;
    @FXML private LineChart lineChart;
    @FXML private Button back2;

    @FXML private TextField UserHeight;
    @FXML private TextField UserWeight;
    @FXML private Button changeUserHeight;
    @FXML private Button changeUserWeight;
    @FXML private Label UserBMIValue;
    @FXML private AnchorPane a;
//    private SourceDataViewModel viewModel = SourceDataViewModel.getInstance();
    private MySession mySession = MySession.mySession;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserInfo userInfo = mySession.getUserInfo();
        double[][] BMI = userInfo.getBmiGroup();
        float userHeight =(float) BMI[BMI.length-1][0];
        float userWeight= (float) BMI[BMI.length-1][1];
        UserWeight.setText(Float.toString(userWeight));
        UserHeight.setText(Float.toString(userHeight));
        String userBMI = Float.toString(userWeight/(userHeight*userHeight));
        UserBMIValue.setText(userBMI);
        drawBMI(BMI);
    }

    public void drawBMI(double[][] BMI) throws IOException{

        //Defining X axis
        NumberAxis xAxis = new NumberAxis(0,30,1);
        xAxis.setLabel("Time");

        //Defining y axis
        NumberAxis yAxis = new NumberAxis(0,30,5);
        yAxis.setLabel("BMI");

        LineChart lineChart = new LineChart(xAxis,yAxis);
        lineChart.setLayoutX(600.0);
        lineChart.setLayoutY(200.0);
        lineChart.setPrefHeight(300.0);
        lineChart.setPrefWidth(350.0);

        XYChart.Series series = new XYChart.Series();
        series.setName("BMI curve");

        double[] BMI1 = new double [BMI.length];
        for (int i=0;i<BMI.length;i++){
            for (int j = 0; j<2;j++){
                BMI1[i]=(BMI[i][1])/(BMI[i][0]*BMI[i][0]);
                //System.out.println(BMI1[i]);
            }
        }
        for (int i=0;i<BMI1.length;i++){
            series.getData().add(new XYChart.Data(i,BMI1[i]));
        }


        lineChart.getData().add(series);
        a.getChildren().add(lineChart);

    }
    public void changeUserPhsicalInfo() throws IOException{
        UserInfo userInfo = mySession.getUserInfo();
        double[][] BMI = userInfo.getBmiGroup();
        float userHeight =(float) BMI[BMI.length-1][0];
        float userWeight= (float) BMI[BMI.length-1][1];

        String height = UserHeight.getText();
        String weight = UserWeight.getText();



        if ((!(height.equals("")))&&(!(weight.equals("")))&&((userHeight!=Float.parseFloat(height))||(userWeight!=Float.parseFloat(weight)))){

            double[][] BMI1 = userInfo.getBmiGroup();
            double[][] newBMI = new double[BMI1.length+1][2];
            for (int i=0;i<BMI1.length;i++){
                for(int j=0;j<BMI1[i].length;j++){
                    newBMI[i][j] = BMI1[i][j];
                }
            }
            newBMI[BMI1.length][0] = Double.parseDouble(height);

            newBMI[BMI1.length][1] = Double.parseDouble(weight);
            UserInfoMapper.addHeightInfo(mySession.getUserInfo(),newBMI);
            //改变BMI
            float newWeight = Float.parseFloat(weight);
            float newHeight = Float.parseFloat(height);
            String userBMI = Float.toString(newWeight/(newHeight*newHeight));
            UserBMIValue.setText(userBMI);

            drawBMI(newBMI);
        }
    }


    public void backToIndex() throws IOException{
        App.jump("showCenter");
    }
}
