package com.ohj.glkt.vod.service;


import cn.hutool.http.server.HttpServerResponse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ohj.ggkt.model.vod.Subject;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author ohj
 * @since 2022-11-12
 */
public interface SubjectService extends IService<Subject> {

    List<Subject> getSubjectList(String id);

    void exportData(HttpServletResponse response);

    boolean importData(MultipartFile file);

    Subject getBySubjectParentId(Long subjectParentId);
}
