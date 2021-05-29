package com.sixty.Pojo;
import lombok.Data;

@Data
public class Order {
    private String username;
    private String time;
    private String type;
    private String object;
    private float price;
    private String state;
}
