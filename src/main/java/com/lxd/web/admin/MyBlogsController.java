package com.lxd.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Author: Cris
 * Date: 2021/08/13
 * Time: 16:14
 * Project: demo
 * Description：
 **/
@RequestMapping("/admin")
@Controller
public class MyBlogsController {

    @RequestMapping("/myBlogs")
    public ModelAndView myBlogs(ModelAndView mav){
        mav.setViewName("/admin/myblogs");
        return mav;
    }
}