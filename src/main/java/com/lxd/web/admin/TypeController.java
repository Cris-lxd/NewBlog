package com.lxd.web.admin;

import com.lxd.po.Blog;
import com.lxd.po.Type;
import com.lxd.po.User;
import com.lxd.service.BlogService;
import com.lxd.service.TypeService;
import com.lxd.util.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Cris on 2020/3/25
 */
@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;   //把这个接口注入
    @Autowired
    private BlogService blogService;


    /*
     * 按照id进行排序，排序的方式为倒序
     * */
    @GetMapping("/types")
    public String types(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                                Pageable pageable, Model model, @CurrentUser User user) {     //model前端页面展示模型
        model.addAttribute("username",user.getUsername());
        model.addAttribute("page", typeService.listType(pageable,user.getId()));   //页面的分页处理   typeService.listType(pageable)里面的对象是json格式
        model.addAttribute("recommendBlogs1", blogService.listRecommendBlogTop(3,user.getId()));
        return "admin/types";
    }

    @GetMapping("/types/{id}/input")          //为了将原有的内容提取到inp框中
    public String editInput(@PathVariable Long id, Model model,@CurrentUser User user) {
        model.addAttribute("username",user.getUsername());
        model.addAttribute("type", typeService.getType(id));
        model.addAttribute("recommendBlogs1", blogService.listRecommendBlogTop(3,user.getId()));
        return "admin/types-input";
    }

    @GetMapping("/types/input")     //前端页面调用这个方法的写法   th:href="@{/admin/types/input}"
    public String input(Model model,@CurrentUser User user) {
        model.addAttribute("username",user.getUsername());
        model.addAttribute("type", new Type());  //前端types-input需要拿到type值   th:object="${type}"
        model.addAttribute("recommendBlogs1", blogService.listRecommendBlogTop(3,user.getId()));
        return "admin/types-input";
    }

    @PostMapping("/types")    //前段调用方式 method="post" th:action="@{/admin/types}"          //新增方法
    public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes, Model model,@CurrentUser User user) {     //@Valid代表要校验,BindingResult result接受结果
        Type type1 = typeService.getTypeByName(type.getName());
        Long userId = user.getId();
        type.setUserId(userId);
        if (type1 != null) {
            result.rejectValue("name", "nameError", "不能重复添加");      //自定义添加返回的内容
        }
        if (result.hasErrors()) {
            return "admin/types-input";
        }
        Type t = typeService.saveType(type);
        if (t == null) {
            attributes.addFlashAttribute("message", "新增失败");

        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        model.addAttribute("recommendBlogs1", blogService.listRecommendBlogTop(3,user.getId()));
        return "redirect:/admin/types";
    }

    @PostMapping("/types{id}")                                         //更新方法
    public String editPost(@Valid Type type, BindingResult result,
                           @PathVariable Long id,
                           Model model,
                           RedirectAttributes attributes,@CurrentUser User user) {     //@Valid代表要校验,BindingResult result接受结果
        Type type1 = typeService.getTypeByName(type.getName());
        type.setUserId(user.getId());
        if (type1 != null) {
            result.rejectValue("name", "nameError", "不能重复添加");      //自定义添加返回的内容
        }
        if (result.hasErrors()) {
            return "admin/types-input";
        }
        Type t = typeService.updateType(id, type);
        if (t == null) {
            attributes.addFlashAttribute("message", "更新失败");

        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        model.addAttribute("recommendBlogs1", blogService.listRecommendBlogTop(3,user.getId()));
        return "redirect:/admin/types";
    }

    //删除
    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        List<Blog> blogByType = blogService.getBlogByType(id);
        if (blogByType.size() != 0) {
            attributes.addFlashAttribute("message", "该分类下含有博客，无法删除");
            return "redirect:/admin/types";
        }
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "删除成功");

        return "redirect:/admin/types";
    }


}
