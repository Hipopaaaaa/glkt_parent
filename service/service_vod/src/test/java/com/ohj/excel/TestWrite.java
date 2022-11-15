package com.ohj.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestWrite {
    public static void main(String[] args) {
        //设置文件名称和路径
        String fileName="/Users/hipopaaaa/Desktop/ohj.xlsx";

        //调用方法
        EasyExcel.write(fileName, User.class)
                .sheet("写操作")
                .doWrite(data());
    }

    //循环设置 要添加的数据
    private static List<User> data(){
        List<User> userList=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId(i);
            user.setName("ohj："+i);
            userList.add(user);
        }
        return userList;
    }
}
