package com.sixty.Pojo;
import lombok.Data;

import java.util.ArrayList;

@Data
public class Video {
    private String title;
    private String type;
    private String url;
    private ArrayList<String> likes;

    public static String[] types = new String[]{
            "Yoga","HIIT","Run","Strength"
    };
}
