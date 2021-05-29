package com.sixty.Pojo;

import com.sixty.Service.*;
import lombok.Data;

import java.io.IOException;

@Data
public class MySession {

    public static MySession mySession = new MySession();
    private User user;
    private UserInfo userInfo;
    private Trainer trainer;
    private TrainerInfo trainerInfo;
    private Video video;
    private String location;

    public void refresh(User user) throws IOException {
        this.user = UserMapper.findUserByName(this.getUser().getUsername());
    }

    public void refresh(UserInfo userInfo) throws IOException {
        this.userInfo = UserInfoMapper.findMemberCenterInfoByName(this.getUserInfo().getUsername());
    }

    public void refresh(Trainer trainer) throws IOException {
        this.trainer = UserMapper.findTrainerByName(this.getTrainer().getUsername());
    }

    public void refresh(TrainerInfo trainerInfo) throws IOException {
        this.trainerInfo = TrainerInfoMapper.findTrainerIntroByName(this.getTrainerInfo().getTrainerName());
    }

    public void refresh(Video video) throws IOException {
        this.video = VideoMapper.findVideoByTitle(this.getVideo().getTitle());
    }
}

