package com.ohj.glkt.vod.controller;


import cn.hutool.http.server.HttpServerResponse;
import com.ohj.ggkt.model.vod.Subject;
import com.ohj.glkt.utils.result.Result;
import com.ohj.glkt.vod.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author ohj
 * @since 2022-11-12
 */
@Api(tags = "课程分类管理")
@RestController
@RequestMapping("/admin/vod/subject")

public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    //课程分类列表
    //懒加载，每次查询一层数据，根据表中的 parent_id字段来实现
    @ApiOperation("课程分类列表")
    @GetMapping("getChildSubject/{id}")
    public Result getChildSubject(@PathVariable("id")String id){
        List<Subject> subjectList= subjectService.getSubjectList(id);
        if(!subjectList.isEmpty()){
            return Result.ok(subjectList);
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("课程分类导出")
    @GetMapping("exportData")
    public void exportData(HttpServletResponse response){
        subjectService.exportData(response);
    }

    @ApiOperation("课程分类导入")
    @PostMapping("importData")
    public Result importData(MultipartFile file){
       boolean isSuccess= subjectService.importData(file);
       if(isSuccess){
           return Result.ok();
       }else {
           return Result.fail();
       }
    }
}

