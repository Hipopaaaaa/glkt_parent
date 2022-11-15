package com.ohj.glkt.vod.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohj.ggkt.model.dto.vod.TeacherDTO;
import com.ohj.ggkt.model.vod.Teacher;
import com.ohj.ggkt.vo.vod.TeacherQueryVo;
import com.ohj.glkt.utils.copy.CopyUtil;
import com.ohj.glkt.utils.result.Result;
import com.ohj.glkt.vod.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author ohj
 * @since 2022-11-09
 */
@Api(tags = "讲师管理")
@RestController
@RequestMapping("/admin/vod/teacher")

public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation("查询所有讲师")
    @GetMapping("findAll")
    public Result<List> findAllTeacher(){
        List<Teacher> list = teacherService.list();
        return Result.ok(list);
    }

    @ApiOperation("删除教师")
    @DeleteMapping("remove/{id}")
    public Result removeTeacher(@PathVariable("id")String id){
        boolean isSuccess = teacherService.removeById(id);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("条件查询分页")
    @PostMapping("findQueryPage/{current}/{limit}")
    public Result findQueryPage(@PathVariable("current")Long current,
                                @PathVariable("limit")Long limit,
                                @RequestBody(required = false) TeacherQueryVo teacherQueryVo){
        Page teahcerPage = teacherService.findQueryPage(current, limit, teacherQueryVo);

        if (teahcerPage.getRecords().isEmpty()){
           return Result.fail();
       }else {
           return Result.ok(teahcerPage);
       }

    }

    @ApiOperation("添加讲师")
    @PostMapping("saveTeacher")
    public Result saveTeacher(@RequestBody TeacherDTO teacherDTO){
        Teacher teacher = CopyUtil.copyBean(teacherDTO, Teacher.class);
        boolean isSuccess = teacherService.save(teacher);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("根据id查询讲师")
    @GetMapping("get/{id}")
    public Result getId(@PathVariable("id")String id){
        Teacher teacher = teacherService.getById(id);
        if(teacher!=null){
            TeacherDTO teacherDTO = CopyUtil.copyBean(teacher, TeacherDTO.class);
            return Result.ok(teacherDTO);
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("修改讲师")
    @PutMapping("update")
    public Result update(@RequestBody Teacher teacher){
        boolean isSuccess = teacherService.updateById(teacher);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("批量删除")
    @DeleteMapping("removeBatch")
    public Result removeBatch(@RequestBody List<String> idList){
        boolean isSuccess = teacherService.removeByIds(idList);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

}

