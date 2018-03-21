package com.nowcoder.controller;

import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    //首先用户提交，需要它的用户名,密码,是否记住
    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="rember", defaultValue = "0") int rememberme
                      ){
        try{
            Map<String, Object> map = userService.register(username, password);
 //{"code":0, "msg": "xxx"}  这种json串
            if(map.isEmpty()){
                return ToutiaoUtil.getJSONString(0, "注册成功");  //没问题就是0，map就是空
            }else{
                return ToutiaoUtil.getJSONString(1, map);
            }

        }catch (Exception e){
            logger.error("注册异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常");
        }

    }


    //RequestMapping(path = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value="rember", defaultValue = "0") int rememberme) {

        try{
            Map<String, Object> map = userService.login(username, password);
            if(map.isEmpty()){
                return ToutiaoUtil.getJSONString(0, "登录成功");
            }else{
                return ToutiaoUtil.getJSONString(1, map);
            }

        }catch (Exception e){
            logger.error("注册异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常");
        }
    }

}
