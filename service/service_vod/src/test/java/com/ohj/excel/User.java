package com.ohj.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class User {
    //设置表头  index属性表示对应excel表格的第几列
    @ExcelProperty(value = "用户编号",index = 0)
    private int id;

    @ExcelProperty(value = "用户名称",index = 1)
    private String name;

}
