package com.sixty.Service;

import com.alibaba.fastjson.JSONObject;
import com.sixty.Pojo.TrainerInfo;

import java.io.*;
import java.util.ArrayList;

public class TrainerInfoMapper {
    public static ArrayList<TrainerInfo> getTrainerIntroList() throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("src/main/resources/data/trainerInfo"))));
        ArrayList<TrainerInfo> trainerInfoArrayList = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null){
            TrainerInfo trainerInfo = JSONObject.parseObject(line, TrainerInfo.class);
            trainerInfoArrayList.add(trainerInfo);
        }

        return trainerInfoArrayList;

    }

    public static TrainerInfo findTrainerIntroByName(String trainerName) throws IOException {

        ArrayList<TrainerInfo> trainerInfoArrayList = getTrainerIntroList();
        for(TrainerInfo trainerInfo : trainerInfoArrayList){

            if(trainerInfo.getTrainerName().equals(trainerName)) {
                return trainerInfo;
            }
        }
        return null;
    }

    public static void changeTrainerPrice(TrainerInfo trainerInfo, float price) throws IOException {
        deleteTrainerIntro(trainerInfo);
        trainerInfo.setPrice(price);
        addTrainerIntro(trainerInfo);
    }

    public static void changeTrainerDiscount(TrainerInfo trainerInfo, float discount) throws IOException {
        deleteTrainerIntro(trainerInfo);
        trainerInfo.setDiscount(discount);
        addTrainerIntro(trainerInfo);
    }

    public static TrainerInfo findTrainerIntroByRealName(String trainerName) throws IOException {

        ArrayList<TrainerInfo> trainerInfoArrayList = getTrainerIntroList();
        for(TrainerInfo trainerInfo : trainerInfoArrayList){

            if(trainerInfo.getRealName().equals(trainerName)) {
                return trainerInfo;
            }
        }
        return null;
    }

    public static ArrayList<TrainerInfo> findTrainerIntroByKeywords(String keywords) throws IOException {

        ArrayList<TrainerInfo> trainerInfoArrayList = getTrainerIntroList();
        ArrayList<TrainerInfo> trainerFindByKeywords = new ArrayList<>();
        for(TrainerInfo trainerInfo : trainerInfoArrayList){

            if(trainerInfo.getProfile().contains(keywords)) {
                trainerFindByKeywords.add(trainerInfo);
            }
        }
        return trainerFindByKeywords;
    }

    public static ArrayList<TrainerInfo> findTrainerIntroByType(String type) throws IOException {
        ArrayList<TrainerInfo> trainerInfoArrayList = getTrainerIntroList();
        ArrayList<TrainerInfo> trainerFindByType = new ArrayList<>();
        for(TrainerInfo trainerInfo : trainerInfoArrayList){
            //判断类别是否相同
            if(trainerInfo.getType().equals(type)) {
                trainerFindByType.add(trainerInfo);
            }
        }
        return trainerFindByType;
    }

    public static ArrayList<TrainerInfo> searchComprehensive(String keyword, String type) throws IOException {
        ArrayList<TrainerInfo> trainerInfoArrayList = getTrainerIntroList();
        ArrayList<TrainerInfo> trainers = new ArrayList<>();
        for(TrainerInfo trainerInfo : trainerInfoArrayList){
            //判断类别是否相同
            if((trainerInfo.getType().equals(type))&&(trainerInfo.getProfile().contains(keyword))) {
                trainers.add(trainerInfo);
            }
        }
        return trainers;
    }

    public static void addTrainerIntro(TrainerInfo newTrainerInfo) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream((new File("src/main/resources/data/trainerInfo")),true)));
        bw.write(JSONObject.toJSONString(newTrainerInfo));
        bw.write("\n");
        bw.close();
    }

    public static void deleteTrainerIntro(TrainerInfo trainerInfo) throws IOException{
        ArrayList<TrainerInfo> trainerInfoArrayList = getTrainerIntroList();
        trainerInfoArrayList.removeIf(trainerIntro1 -> trainerIntro1.getTrainerName().equals(trainerInfo.getTrainerName()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/resources/data/trainerInfo")));

        for(TrainerInfo trainerInfo1 : trainerInfoArrayList){
            bw.write(JSONObject.toJSONString(trainerInfo1));
            bw.write("\n");
        }

        bw.close();
    }

    public static void recharge(TrainerInfo trainerInfo, float money) throws IOException {
        deleteTrainerIntro(trainerInfo);
        trainerInfo.setBalance(trainerInfo.getBalance()+money);
        addTrainerIntro(trainerInfo);
    }


}
