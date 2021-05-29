package com.sixty.Service;

import com.alibaba.fastjson.JSONObject;
import com.sixty.Pojo.UserInfo;

import java.io.*;
import java.util.ArrayList;

public class UserInfoMapper {

    public static ArrayList<UserInfo> getMemberCenterInfoList() throws IOException {


        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("src/main/resources/data/userInfo"))));
        ArrayList<UserInfo> userInfoList = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null){
            UserInfo userInfo = JSONObject.parseObject(line, UserInfo.class);
            userInfoList.add(userInfo);
        }
        return userInfoList;

    }

    public static UserInfo findMemberCenterInfoByName(String username) throws IOException {

        ArrayList<UserInfo> userInfoList = getMemberCenterInfoList();
        for(UserInfo userInfo : userInfoList){

            if(userInfo.getUsername().equals(username)) {
                return userInfo;

            }
        }
        return null;
    }
    public static void addMemberCenterInfo(UserInfo userInfo) throws  IOException{
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/resources/data/userInfo", true)));

        bw.write(JSONObject.toJSONString(userInfo));
        bw.write("\n");
        bw.close();
    }
    public static void deleteMemberCenterInfo(UserInfo userInfo) throws IOException{
        ArrayList<UserInfo> userInfoArrayList = getMemberCenterInfoList();
        userInfoArrayList.removeIf(memberCenterInfo1 -> memberCenterInfo1.getUsername().equals(userInfo.getUsername()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/resources/data/userInfo")));

        for(UserInfo userInfo1 : userInfoArrayList){
            bw.write(JSONObject.toJSONString(userInfo1));
            bw.write("\n");
        }
        bw.close();
    }
    public static void recharge(UserInfo userInfo, float money) throws IOException {
        deleteMemberCenterInfo(userInfo);
        userInfo.setBalance(userInfo.getBalance()+money);
        addMemberCenterInfo(userInfo);
    }

    public static void addHeightInfo(UserInfo userInfo, double[][] BMI)throws IOException{
        deleteMemberCenterInfo(userInfo);
        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setUsername(userInfo.getUsername());
        newUserInfo.setBmiGroup(BMI);
        addMemberCenterInfo(newUserInfo);
    }
}
