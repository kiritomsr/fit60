package com.sixty.Service;

import com.alibaba.fastjson.JSONObject;
import com.sixty.Pojo.User;
import com.sixty.Pojo.Trainer;

import java.io.*;
import java.util.ArrayList;

public class UserMapper {
    public static ArrayList<User> getUserList() throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("src/main/resources/data/user"))));
        ArrayList<User> users = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null){
            User user = JSONObject.parseObject(line, User.class);
            users.add(user);
        }

        return users;

    }

    public static User findUserByName(String username) throws IOException {

        ArrayList<User> users = getUserList();
        for(User user : users){

            if(user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static void addUser(User newUser) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream((new File("src/main/resources/data/user")),true)));
        bw.write(JSONObject.toJSONString(newUser));
        bw.write("\n");
        bw.close();
    }

    public static void deleteUser(User user) throws IOException{
        ArrayList<User> users = getUserList();
        users.removeIf(user1 -> user1.getUsername().equals(user.getUsername()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/resources/data/user")));

        for(User user1 : users){
            bw.write(JSONObject.toJSONString(user1));
            bw.write("\n");
        }

        bw.close();
    }

    public static ArrayList<Trainer> getTrainerList() throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("src/main/resources/data/trainer"))));
        ArrayList<Trainer> trainers = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null){
            Trainer trainer = JSONObject.parseObject(line, Trainer.class);
            trainers.add(trainer);
        }

        return trainers;

    }

    public static Trainer findTrainerByName(String username) throws IOException {

        ArrayList<Trainer> trainers = getTrainerList();
        for(Trainer trainer: trainers){

            if(trainer.getUsername().equals(username)) {
                return trainer;
            }
        }
        return null;
    }

    public static void addTrainer(Trainer newTrainer) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream((new File("src/main/resources/data/trainer")),true)));
        bw.write(JSONObject.toJSONString(newTrainer));
        bw.write("\n");
        bw.close();

    }

    public static void deleteTrainer(Trainer trainer) throws IOException{
        ArrayList<Trainer> trainers = getTrainerList();
        trainers.removeIf(trainer1 -> trainer1.getUsername().equals(trainer.getUsername()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/resources/data/trainer")));

        for(Trainer trainer1 : trainers){
            bw.write(JSONObject.toJSONString(trainer1));
            bw.write("\n");
        }

        bw.close();
    }

    public static String findTrainerProtoUrlByName(String trainerName) throws IOException {

        ArrayList<Trainer> trainerArrayList = getTrainerList();
        for(Trainer trainer: trainerArrayList){

            if(trainer.getUsername().equals(trainerName)) {
                return trainer.getPhotoUrl();
            }
        }
        return null;
    }

    public static void changeUserPhone(User user, String phone)throws IOException{
        deleteUser(user);
        user.setPhone(phone);
        addUser(user);
    }

    public static void changeUserEmail(User user, String email)throws IOException{
        deleteUser(user);
        user.setEmail(email);
        addUser(user);
    }

    public static void changeUserGender(User user, String sex)throws IOException{
        deleteUser(user);
        user.setSex(sex);
        addUser(user);
    }

    public static void changeUserPhoto(User user, String photo)throws IOException{
        deleteUser(user);
        user.setPhotoUrl(photo);
        addUser(user);
    }

    public static void changeTrainerPhone(Trainer trainer, String phone)throws IOException{
        deleteTrainer(trainer);
        trainer.setPhone(phone);
        addTrainer(trainer);
    }

    public static void changeTrainerEmail(Trainer trainer, String email)throws IOException{
        deleteTrainer(trainer);
        trainer.setEmail(email);
        addTrainer(trainer);
    }

    public static void changeTrainerGender(Trainer trainer, String sex)throws IOException{
        deleteTrainer(trainer);
        trainer.setSex(sex);
        addTrainer(trainer);
    }

    public static void changeTrainerPhoto(Trainer trainer, String photo)throws IOException{
        deleteTrainer(trainer);
        trainer.setPhotoUrl(photo);
        addTrainer(trainer);
    }
}
