package com.blinkfox.javadoc.controller;

import com.blinkfox.javadoc.service.RecordService;

import java.util.List;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 首页根控制器.
 *
 * @author blinkfox on 2019-05-22.
 */
@Slf4j
@Controller
@RequestMapping
public class IndexController {

    @Resource
    private RecordService recordService;

    /**
     * index.html 首页请求.
     *
     * @return 字符串
     */
    @GetMapping
    public ModelAndView index(ModelAndView modelView) {
        modelView.setViewName("index");
        return modelView;
    }

    /**
     * 转向首页 doc.html.
     *
     * @return doc.html
     */
    @GetMapping("/docs/{groupId}/{artifactId}")
    public ModelAndView getDocs(ModelAndView modelView,
            @PathVariable("groupId") String groupId,
            @PathVariable("artifactId") String artifactId) {
        // 查询出该 groupId 和 artifactId 对应的最近的 20 个 version，如果 version 为空就默认选取一个最近的.
        List<String> versions = recordService.findVersionsByGroupIdAndArtifactId(groupId, artifactId);
        if (!CollectionUtils.isEmpty(versions)) {
            modelView.addObject("version", versions.get(0));
            modelView.addObject("versions", versions);
        }

        // 设置需要返回渲染的数据.
        modelView.addObject("groupId", groupId);
        modelView.addObject("artifactId", artifactId);
        modelView.addObject("artifactIds", recordService.findArtifactIdsByGroupId(groupId));
        modelView.setViewName("doc");
        return modelView;
    }

    /**
     * 转向首页 doc.html.
     *
     * @return doc.html
     */
    @GetMapping("/docs/{groupId}/{artifactId}/{version}")
    public ModelAndView getDocs(ModelAndView modelView,
            @PathVariable("groupId") String groupId,
            @PathVariable("artifactId") String artifactId,
            @PathVariable("version") String version) {
        // 设置需要返回渲染的数据.
        modelView.addObject("groupId", groupId);
        modelView.addObject("artifactId", artifactId);
        modelView.addObject("version", version);
        modelView.addObject("artifactIds", recordService.findArtifactIdsByGroupId(groupId));
        modelView.addObject("versions", recordService.findVersionsByGroupIdAndArtifactId(groupId, artifactId));
        modelView.setViewName("doc");
        return modelView;
    }

}
