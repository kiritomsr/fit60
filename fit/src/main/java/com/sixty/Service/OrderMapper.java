package com.sixty.Service;

import com.alibaba.fastjson.JSONObject;
import com.sixty.Pojo.Order;
import com.sixty.Pojo.Schedule;

import java.io.*;
import java.util.ArrayList;

public class OrderMapper {
    public static ArrayList<Order> getOrderList() throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("src/main/resources/data/order"))));
        ArrayList<Order> orders = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null){
            Order order = JSONObject.parseObject(line, Order.class);
            orders.add(order);
        }

        return orders;

    }

    public static ArrayList<Order> findOrderByName(String username) throws IOException {
        ArrayList<Order> orders = getOrderList();
        ArrayList<Order> orders2 = new ArrayList<>();
        for(Order order : orders){
            if(order.getUsername().equals(username)) {
                orders2.add(order);
            }
        }
        return orders2;
    }

    public static Order findOrderByNameAndTime(String username, String time) throws Exception{
        ArrayList<Order> orders = findOrderByName(username);

        for(Order order : orders){
            if(order.getTime().equals(time)) {
                return order;
            }
        }
        return null;
    }

    public static ArrayList<Order> findOrderByObject(String object) throws IOException {
        ArrayList<Order> orders = getOrderList();
        ArrayList<Order> orders2 = new ArrayList<>();
        for(Order order : orders){
            if(order.getObject().equals(object)) {
                orders2.add(order);
            }
        }
        return orders2;
    }

    public static void addOrder(Order order) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream((new File("src/main/resources/data/order")),true)));
        bw.write(JSONObject.toJSONString(order));
        bw.write("\n");
        bw.close();
    }
    
    public static void deleteOrderByTime(Order order) throws IOException{
        ArrayList<Order> orders = getOrderList();
        orders.removeIf(order1 -> order1.getTime().equals(order.getTime()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/resources/data/order")));
        for(Order order1 : orders){
            bw.write(JSONObject.toJSONString(order1));
            bw.write("\n");
        }
        bw.close();
    }

    public static void cancelOrder(Order order) throws IOException{
        deleteOrderByTime(order);
        order.setState("canceled");
        addOrder(order);
    }

}
