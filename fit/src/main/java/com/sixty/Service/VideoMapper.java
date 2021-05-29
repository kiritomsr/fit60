package com.sixty.Service;

import com.alibaba.fastjson.JSONObject;
import com.sixty.Pojo.Video;

import java.io.*;
import java.util.ArrayList;

public class VideoMapper {
    public static ArrayList<Video> getVideoList() throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("src/main/resources/data/video"))));
        ArrayList<Video> videos = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null){
            Video video = JSONObject.parseObject(line, Video.class);
            videos.add(video);
        }

        return videos;

    }

    public static Video findVideoByTitle(String title) throws IOException {

        ArrayList<Video> videos = getVideoList();
        for(Video video : videos){

            if(video.getTitle().equals(title)) {
                return video;
            }
        }
        return null;
    }

    public static ArrayList<Video> selectVideoByKeyword(ArrayList<Video> videos, String keyword) {

        ArrayList<Video> videos1 = new ArrayList<Video>();
        for(Video video : videos){
            if(video.getTitle().contains(keyword)) {
                videos1.add(video);
            }
        }
        return videos1;
    }

    public static ArrayList<Video> selectVideoByType(ArrayList<Video> videos, String type) {
        ArrayList<Video> videos1 = new ArrayList<>();
        for(Video video : videos){
            if(video.getType().equals(type)) {
                videos1.add(video);
            }
        }
        return videos1;
    }

    public static ArrayList<Video> selectVideoLiked(ArrayList<Video> videos, String username) {
        ArrayList<Video> videos1 = new ArrayList<>();
        for(Video video : videos){
            if(isLiked(video, username)) {
                videos1.add(video);
            }
        }
        return videos1;
    }

    public static Boolean checkTitle(String title) throws IOException {
        if(findVideoByTitle(title) == null){
            return true;
        }
        return false;
    }

    public static boolean isLiked(Video video, String username){
        boolean flag;
        ArrayList<String> likes = video.getLikes()!=null?video.getLikes():new ArrayList<>();
        if(likes.contains(username)){
            flag = true;
        }else {
            flag = false;
        }
        return flag;
    }

    public static void changeLike(Video video, String username) throws IOException {

        ArrayList<String> likes = video.getLikes()!=null?video.getLikes():new ArrayList<>();
        deleteVideo(video);
        if(isLiked(video, username)){
            likes.remove(username);
        }else {
            likes.add(username);
        }
        video.setLikes(likes);
        addVideo(video);

    }

    public static void addVideo(Video video) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream((new File("src/main/resources/data/video")),true)));
        bw.write(JSONObject.toJSONString(video));
        bw.write("\n");
        bw.close();
    }

    public static void deleteVideo(Video video) throws IOException{
        ArrayList<Video> videos = getVideoList();
        videos.removeIf(video1 -> video1.getTitle().equals(video.getTitle()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/resources/data/video")));

        for(Video video1 : videos){
            bw.write(JSONObject.toJSONString(video1));
            bw.write("\n");
        }
        bw.close();
    }

    public static void main(String[] args) throws IOException {
//        Video video = new Video();
//        video.setTitle("video01");
//        video.setType("type01");
//        video.setUrl("src/main/resources/video/v1.mp4");
//        addVideo(video);

    }
}
