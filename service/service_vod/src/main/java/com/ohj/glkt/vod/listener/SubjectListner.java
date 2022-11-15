package com.ohj.glkt.vod.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.ohj.ggkt.model.vod.Subject;
import com.ohj.ggkt.vo.vod.SubjectEeVo;
import com.ohj.glkt.utils.copy.CopyUtil;
import com.ohj.glkt.vod.mapper.SubjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//监听器，用于把excel表格的内容导入subject表
public class SubjectListner extends AnalysisEventListener<SubjectEeVo> {


    @Autowired
    private SubjectMapper subjectMapper;

    //从第二行开始 一行一行读取
    @Override
    public void invoke(SubjectEeVo subjectEeVo, AnalysisContext analysisContext) {
        Subject subject = CopyUtil.copyBean(subjectEeVo, Subject.class);
        //存到数据库
        subjectMapper.insert(subject);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
