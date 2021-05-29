package com.sixty.Controller;

import com.sixty.App;
import com.sixty.Pojo.*;
import com.sixty.Service.TrainerInfoMapper;
import com.sixty.Service.UserMapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TrainerListController implements Initializable {
    @FXML private Pagination trainerPagination;
    @FXML private ToolBar trainer;
    @FXML private ChoiceBox chooseTrainerTypes;
    @FXML private TextField inputTrainerKeyword;

    @FXML private Pane trainerPane1;
    @FXML private Pane trainerPane2;
    @FXML private Pane trainerPane3;
    @FXML private Pane trainerPane4;
    @FXML private Pane trainerPane5;
    @FXML private Pane trainerPane6;
    ArrayList<Pane> trainerPanes = new ArrayList<>();

    @FXML private Label trainerType1;
    @FXML private Label trainerType2;
    @FXML private Label trainerType3;
    @FXML private Label trainerType4;
    @FXML private Label trainerType5;
    @FXML private Label trainerType6;
    ArrayList<Label> trainerTypes = new ArrayList<>();

    @FXML private Label trainerName1;
    @FXML private Label trainerName2;
    @FXML private Label trainerName3;
    @FXML private Label trainerName4;
    @FXML private Label trainerName5;
    @FXML private Label trainerName6;
    ArrayList<Label> trainerNames = new ArrayList<>();

    @FXML private ImageView trainerPhoto1;
    @FXML private ImageView trainerPhoto2;
    @FXML private ImageView trainerPhoto3;
    @FXML private ImageView trainerPhoto4;
    @FXML private ImageView trainerPhoto5;
    @FXML private ImageView trainerPhoto6;
    ArrayList<ImageView> trainerPhotos = new ArrayList<>();
//    private SourceDataViewModel viewModel = SourceDataViewModel.getInstance();
    private MySession mySession = MySession.mySession;
    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        trainerPagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        initTrainers();
        showTrainer(TrainerInfoMapper.getTrainerIntroList());
        chooseTrainerTypes.setItems(FXCollections.observableArrayList("All","Yoga","HIIT","Run","Strength"));
        chooseTrainerTypes.setValue("All");
        chooseType("");
    }

    public void chooseType(String keyword) throws Exception {
        chooseTrainerTypes.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends  Number> ov,
                 Number old_val, Number new_val) ->{
                    int i = new_val.intValue();
                    if (i==0){
                        try {
                            ArrayList<TrainerInfo> trainers = TrainerInfoMapper.findTrainerIntroByKeywords(keyword);
                            show(trainers);}
                        catch (IOException e) { e.printStackTrace(); } catch (Exception e) { e.printStackTrace(); }
                    }
                    else if(i==1){
                        try { ArrayList<TrainerInfo> trainers = TrainerInfoMapper.searchComprehensive(keyword,"Yoga");
                            show(trainers); } catch (IOException e) { e.printStackTrace(); }
                        catch (Exception e) { }
                    }
                    else if (i==2){
                        try { ArrayList<TrainerInfo> trainers = TrainerInfoMapper.searchComprehensive(keyword,"HIIT");
                            show(trainers); } catch (IOException e) { e.printStackTrace(); }
                        catch (Exception e) { }
                    }
                    else if (i==3){
                        try { ArrayList<TrainerInfo> trainers = TrainerInfoMapper.searchComprehensive(keyword,"Run");
                            show(trainers); } catch (IOException e) { e.printStackTrace(); }
                        catch (Exception e) { }
                    }
                    else if (i==4){
                        try { ArrayList<TrainerInfo> trainers = TrainerInfoMapper.searchComprehensive(keyword,"Strength");
                            show(trainers); } catch (IOException e) { e.printStackTrace(); }
                        catch (Exception e) { }
                    }
                });
    }

    public  void TrainerKeywordsTyped() throws  Exception{
        String type = (String) chooseTrainerTypes.getValue();
        String keyword = inputTrainerKeyword.getText();
        if (type.equals("All")){
            if (keyword.equals("")){
                ArrayList<TrainerInfo> trainers = TrainerInfoMapper.getTrainerIntroList();
                showTrainer(trainers);
            }
            else {
                ArrayList<TrainerInfo> trainers = TrainerInfoMapper.findTrainerIntroByKeywords(keyword);
                if (!(trainers.size() ==0)){
                    showTrainer(trainers);
                }
                else {
                    for (int i=0;i<trainers.size();i++){
                        trainerPanes.get(i).setVisible(false);
                    }
                }
            }
        }
        else if (type.equals("Yoga")){
            if (keyword.equals("")){
                try {
                    ArrayList<TrainerInfo> trainers = TrainerInfoMapper.findTrainerIntroByType("Yoga");
                    show(trainers);
                } catch (IOException e) { e.printStackTrace(); } catch (Exception e) { e.printStackTrace(); }
            }
            else {
                ArrayList<TrainerInfo> trainers = TrainerInfoMapper.searchComprehensive(keyword,"Yoga");
                show(trainers);
            }
        }
        else if (type.equals("HIIT")) {
            if (keyword.equals("")){
                try {
                    ArrayList<TrainerInfo> trainers = TrainerInfoMapper.findTrainerIntroByType("HIIT");
                    show(trainers);
                } catch (IOException e) { e.printStackTrace(); } catch (Exception e) { e.printStackTrace(); }
            }
            else {
                ArrayList<TrainerInfo> trainers = TrainerInfoMapper.searchComprehensive(keyword,"HIIT");
                show(trainers);
            }
        }
        else if (type.equals("Run")) {
            if (keyword.equals("")){
                try {
                    ArrayList<TrainerInfo> trainers = TrainerInfoMapper.findTrainerIntroByType("Run");
                    show(trainers);
                } catch (IOException e) { e.printStackTrace(); } catch (Exception e) { e.printStackTrace(); }
            }
            else {
                ArrayList<TrainerInfo> trainers = TrainerInfoMapper.searchComprehensive(keyword,"Run");
                show(trainers);
            }
        }
        else if (type.equals("Strength")) {
            if (keyword.equals("")){
                try {
                    ArrayList<TrainerInfo> trainers = TrainerInfoMapper.findTrainerIntroByType("Strength");
                    show(trainers);
                } catch (IOException e) { e.printStackTrace(); } catch (Exception e) { e.printStackTrace(); }
            }
            else {
                ArrayList<TrainerInfo> trainers = TrainerInfoMapper.searchComprehensive(keyword,"Strength");
                show(trainers);
            }
        }
        chooseType(keyword);
    }

    public void show(ArrayList<TrainerInfo> trainers) throws  Exception{
        if (!(trainers.size() ==0)){
            showTrainer(trainers);
        }
        else {
            for (int i=0;i<trainerPanes.size();i++){
                trainerPanes.get(i).setVisible(false);
            }
        }
    }

    public void showTrainerDetail(int i) throws IOException {
        String name = trainerNames.get(i).getText().split(": ")[1];
        TrainerInfo trainerInfo = TrainerInfoMapper.findTrainerIntroByRealName(name);
        mySession.setTrainer(UserMapper.findTrainerByName(trainerInfo.getTrainerName()));
        mySession.setTrainerInfo(trainerInfo);
        App.jump("trainerDetail");
    }

    public void showTrainer(ArrayList<TrainerInfo> trainers) throws Exception{
        int listNum = trainers.size();
        int pageNum = (listNum%6==0)?listNum/6:listNum/6 +1;

        trainerPagination.setPageCount(pageNum);//设置页数

        trainerPagination.setPageFactory(new Callback<Integer, Node>() {
            @SneakyThrows
            @Override
            public Node call(Integer param) {

                VBox box = new VBox(5);
                if ((param+1)!=pageNum||trainers.size()%6==0){
                    for (int i=0;i<6;i++){
                        trainerPhotos.get(i).setImage(new Image("file:src/main/resources/picture/TrainerPhoto/"+UserMapper.findTrainerProtoUrlByName(trainers.get(6*param+i).getTrainerName())));
                        trainerTypes.get(i).setText("Type: "+trainers.get(6*param+i).getType());
                        trainerNames.get(i).setText("Name: "+trainers.get(6*param+i).getRealName());
                        trainerPanes.get(i).setVisible(true);
                    }
                }
                else {
                    for (int i=0;i<(trainers.size()%6);i++){
                        trainerPhotos.get(i).setImage(new Image("file:src/main/resources/picture/TrainerPhoto/"+UserMapper.findTrainerProtoUrlByName(trainers.get(6*param+i).getTrainerName())));
                        trainerTypes.get(i).setText("Type: "+trainers.get(6*param+i).getType());
                        trainerNames.get(i).setText("Name: "+trainers.get(6*param+i).getRealName());
                        trainerPanes.get(i).setVisible(true);
                    }
                    for (int i=(trainers.size()%6);i<6;i++){
                        trainerPanes.get(i).setVisible(false);
                    }
                }
                return box;
            }
        });
    }

    public void initTrainers(){
        trainerPanes.add(trainerPane1);
        trainerPanes.add(trainerPane2);
        trainerPanes.add(trainerPane3);
        trainerPanes.add(trainerPane4);
        trainerPanes.add(trainerPane5);
        trainerPanes.add(trainerPane6);

        trainerTypes.add(trainerType1);
        trainerTypes.add(trainerType2);
        trainerTypes.add(trainerType3);
        trainerTypes.add(trainerType4);
        trainerTypes.add(trainerType5);
        trainerTypes.add(trainerType6);

        trainerNames.add(trainerName1);
        trainerNames.add(trainerName2);
        trainerNames.add(trainerName3);
        trainerNames.add(trainerName4);
        trainerNames.add(trainerName5);
        trainerNames.add(trainerName6);

        trainerPhotos.add(trainerPhoto1);
        trainerPhotos.add(trainerPhoto2);
        trainerPhotos.add(trainerPhoto3);
        trainerPhotos.add(trainerPhoto4);
        trainerPhotos.add(trainerPhoto5);
        trainerPhotos.add(trainerPhoto6);
    }

    @FXML public void showTrainerDetail1() throws IOException { showTrainerDetail(0);  }
    @FXML public void showTrainerDetail2() throws IOException { showTrainerDetail(1);  }
    @FXML public void showTrainerDetail3() throws IOException { showTrainerDetail(2);  }
    @FXML public void showTrainerDetail4() throws IOException { showTrainerDetail(3);  }
    @FXML public void showTrainerDetail5() throws IOException { showTrainerDetail(4);  }
    @FXML public void showTrainerDetail6() throws IOException { showTrainerDetail(5);  }

    @FXML
    public void jumpToVideo() throws IOException {
        App.jump("showVideo");
    }

    @FXML
    public void jumpToCenter() throws IOException {
        App.jump("showCenter");
    }
}
