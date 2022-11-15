package com.ohj.glkt.vod.service.impl;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ohj.ggkt.model.vod.Subject;
import com.ohj.ggkt.vo.vod.SubjectEeVo;
import com.ohj.glkt.utils.copy.CopyUtil;
import com.ohj.glkt.utils.exception.GlktException;
import com.ohj.glkt.vod.listener.SubjectListner;
import com.ohj.glkt.vod.mapper.SubjectMapper;
import com.ohj.glkt.vod.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.deploy.net.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author ohj
 * @since 2022-11-12
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private SubjectListner subjectListner;

    @Override
    public List<Subject> getSubjectList(String id) {
        LambdaQueryWrapper<Subject> subjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        subjectLambdaQueryWrapper.eq(StringUtils.isNotBlank(id),Subject::getParentId,id);

        List<Subject> subjectList = subjectMapper.selectList(subjectLambdaQueryWrapper);

        //判断是否有下一层数据
        for (Subject subject : subjectList) {
            subjectLambdaQueryWrapper.clear();

            subjectLambdaQueryWrapper.eq(StringUtils.isNotBlank(subject.getId()+""),Subject::getParentId,subject.getId());
            Integer count = subjectMapper.selectCount(subjectLambdaQueryWrapper);
            if(count>0){
                subject.setHasChildren(true);
            }
        }

        return subjectList;
    }

    @Override
    public void exportData(HttpServletResponse response) {
        try {
            //设置下载信息
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("课程分类", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");

            //查询课程分类表所有的数据
            List<Subject> subjectList = subjectMapper.selectList(null);

            List<SubjectEeVo> subjectEeVoList = subjectList.stream()
                    .map(subject -> {
                        return CopyUtil.copyBean(subject, SubjectEeVo.class);
                    }).collect(Collectors.toList());

            //EasyExcel写操作
            EasyExcel.write(response.getOutputStream(), SubjectEeVo.class).sheet("课程分类").doWrite(subjectEeVoList);
        } catch (Exception e) {
            throw new GlktException(20001,"导出失败");
        }
    }

    @Override
    public boolean importData(MultipartFile file) {

        try {
            EasyExcel.read(file.getInputStream(), SubjectEeVo.class,subjectListner).sheet().doRead();
            return true;
        } catch (IOException e) {
            throw new GlktException(20001,"导入失败");
        }
    }

    @Override
    public Subject getBySubjectParentId(Long subjectParentId) {
        LambdaQueryWrapper<Subject> subjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        subjectLambdaQueryWrapper.eq(StringUtils.isNotBlank(subjectParentId+""),Subject::getParentId,subjectParentId);

        return subjectMapper.selectOne(subjectLambdaQueryWrapper);
    }
}
