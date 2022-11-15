package com.ohj.excel;

import com.alibaba.excel.EasyExcel;

public class TestRead {
    public static void main(String[] args) {
        //设置文件名称和路径
        String fileName="/Users/hipopaaaa/Desktop/ohj.xlsx";

        //调用方法进行读操作 sheet()表示读第一个sheet
        EasyExcel.read(fileName,User.class,new ExcelListener()).sheet().doRead();
    }
}
