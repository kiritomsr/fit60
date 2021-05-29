package com.sixty.Service;

import com.alibaba.fastjson.JSONObject;
import com.sixty.Pojo.Schedule;

import java.io.*;
import java.util.ArrayList;

public class ScheduleMapper {
    public static ArrayList<Schedule> getScheduleList() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("src/main/resources/data/schedule"))));
        ArrayList<Schedule> schedules = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null){
            Schedule schedule = JSONObject.parseObject(line, Schedule.class);
            schedules.add(schedule);
        }

        return schedules;
    }

    public static ArrayList<Schedule> findScheduleListByName(String username) throws IOException {
        ArrayList<Schedule> schedules = getScheduleList();
        ArrayList<Schedule> schedules2 = new ArrayList<>();
        for(Schedule schedule : schedules){

            if(schedule.getUsername().equals(username)) {
                schedules2.add(schedule);
            }
        }
        return schedules2;
    }

    public static ArrayList<Schedule> findUncanceledScheduleByName(String username) throws IOException {
        ArrayList<Schedule> schedules = getScheduleList();
        ArrayList<Schedule> schedules2 = new ArrayList<>();
        for(Schedule schedule : schedules){
            if(schedule.getUsername().equals(username) && !schedule.getState().equals("canceled")) {
                schedules2.add(schedule);
            }
        }
        return schedules2;
    }

    public static ArrayList<Schedule> findScheduleListByNameAndDate(String username, String date) throws Exception{
        ArrayList<Schedule> schedules = findScheduleListByName(username);
        ArrayList<Schedule> schedules2 = new ArrayList<>();
        for(Schedule schedule : schedules){

            if(schedule.getDate().equals(date)) {
                schedules2.add(schedule);
            }
        }
        return schedules2;

    }

    public static void addSchedule(Schedule newSchedule) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream((new File("src/main/resources/data/schedule")),true)));
        bw.write(JSONObject.toJSONString(newSchedule));
        bw.write("\n");
        bw.close();
    }

    public static void cancelSchedule(Schedule schedule)throws IOException{
        deleteSchedule(schedule);
        schedule.setState("canceled");
        addSchedule(schedule);

    }

    public static void deleteSchedule(Schedule schedule) throws IOException{
        ArrayList<Schedule> schedules = findScheduleListByName(schedule.getUsername());
        schedules.removeIf(schedule1 -> schedule1.equals(schedule));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/resources/data/schedule")));
        for(Schedule schedule1 : schedules){
            bw.write(JSONObject.toJSONString(schedule1));
            bw.write("\n");
        }
        bw.close();
    }

    public static void deleteUserScheduleBefore(ArrayList<Schedule>  userSchedules, String date, String time) throws IOException {
        ArrayList<Schedule> userScheduleList = new ArrayList<>();
        for (Schedule schedule : userSchedules) {
            if (schedule.getDate().compareTo(date) < 0) {

                userScheduleList.add(schedule);
            }
            if (schedule.getDate().compareTo(date) == 0) {
                if (schedule.getTime().compareTo(time) < 0) {
                    userScheduleList.add(schedule);
                }
            }
        }
        userSchedules.removeAll(userScheduleList);
    }

    public static void deleteUserScheduleCancelled(ArrayList<Schedule>  userSchedules) throws IOException {
        ArrayList<Schedule> userScheduleList = new ArrayList<>();
        for (Schedule schedule : userSchedules) {
            if (!schedule.getState().equals("submit")) {
                //System.out.println("1");
                userScheduleList.add(schedule);
            }
        }
        userSchedules.removeAll(userScheduleList);
    }

}
