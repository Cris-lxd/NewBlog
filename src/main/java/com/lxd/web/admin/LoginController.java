package com.lxd.web.admin;

import com.lxd.po.User;
import com.lxd.service.BlogService;
import com.lxd.service.UserService;
import com.lxd.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Cris on 2020/3/23
 */
@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private BlogService blogService;


    @GetMapping()            //默认使用全局  作用是第一次访问时返回到配置的页面
    public String loginPage() {
        return "admin/login";
    }

    /*
     * 登录
     * */
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model,
                        RedirectAttributes attributes) {    //拿到session
        User user = userService.checkUser(username, password);    //调用判断
        if (user != null) {
            user.setPassword(null);
            session.setAttribute("user", user);
            //return "/admin/index";
            model.addAttribute("recommendBlogs1", blogService.listRecommendBlogTop(3));
            return "admin/index";
        } else {
            attributes.addFlashAttribute("message", "用户名和密码错误");
            return "redirect:/admin";     //重定向到登录页面
        }
    }

    /*
     *注销用户
     */
    @GetMapping("/logout")      //注销到logout页面，重定向到登录页面
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }

    @RequestMapping("/toAdmin")
    public String toAdmin(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return "admin/login";
    }

    /**
     * 注册
     * @param username
     * @param password
     * @param invitedCode
     * @return
     */
    @ResponseBody
    @RequestMapping("/register")
    public String register(String username, String password,String email,String invitedCode) throws ParseException {
        if(!"5208".equals(invitedCode)){
            return "请输入正确邀请码";
        }
        User user = new User();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        Long id = Long.valueOf(sdf.format(new Date()));
        user.setId(id);
        user.setUsername(username);
        user.setPassword(MD5Utils.code(password));
        user.setEmail(email);
        user.setCreatTime(new Date());
        int i = userService.addUser(user);
        String res = i == 1 ? "注册成功" : "注册失败";
        return res;
    }
}
