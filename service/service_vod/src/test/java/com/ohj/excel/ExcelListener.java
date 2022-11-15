package com.ohj.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

//对应表的监听器
public class ExcelListener extends AnalysisEventListener<User> {

    //一行一行读取excel内容，把每行的内容封装到user对象
    //从excel第二行开始读取，因为第一行是表头
    @Override
    public void invoke(User user, AnalysisContext analysisContext) {
        System.out.println("读取到的一行内容："+user.toString() );
    }

    //读取表头内容
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头："+headMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
