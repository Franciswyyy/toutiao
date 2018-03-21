package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.News;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    //把获取news单独提取出来写成方法，书写规范
    private List<ViewObject> getNews(int id, int offset, int limit){
        List<News> newsList = newsService.getLatestNews(id, offset, limit);

        List<ViewObject> vos = new ArrayList<>();
        for(News news : newsList){
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(news.getId()));
            vos.add(vo);
        }
        return vos;
    }




    //通过访问首页到链接
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model,
                        @RequestParam(value ="pop",defaultValue = "0") int pop) {

        model.addAttribute("vos", getNews(0, 0 ,10));
        model.addAttribute("pop", pop);
        return "home";
        //Velocity默认返回是vm，需要修改默认返回是html
    }

    //通过用户来访问  也能看的到链接
    //即没有userid则全部显示，有的话就以用户来显示，这也就是之前写的newsdao查询的语句
    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId){
        model.addAttribute("vos", newsService.getLatestNews(userId, 0, 10));
        return "home";
    }


}
