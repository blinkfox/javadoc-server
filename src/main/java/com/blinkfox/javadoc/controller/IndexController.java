package com.blinkfox.javadoc.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
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

    /**
     * index.html 首页请求.
     *
     * @return 字符串
     */
    @GetMapping
    public String index() {
        return "index.html";
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
        modelView.setViewName("doc");

        // 设置需要返回渲染的部分参数.
        modelView.addObject("groupId", groupId);
        modelView.addObject("artifactId", artifactId);
        modelView.addObject("version", version);
        return modelView;
    }

}
