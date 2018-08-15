package com.nowcoder.interceptor;

import com.nowcoder.dao.LoginTicketDao;
import com.nowcoder.dao.UserDao;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import sun.rmi.runtime.Log;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

// 登陆验证
@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginTicketDao loginTicketDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //从cookie里取ticket来判断,  因为ticket可能是伪造的
        String ticket = null;
        if (httpServletRequest.getCookies() != null) {
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        if(ticket != null){
            LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);
            //用户没登录， 在这之前，说明过期了， 状态不等于0， 也是过期的
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
                return true;   //就直接过去了，
            }

            //如果我知道有这个人，就需要记录下来这个人了。用户保存起来
            User user = userDao.selectById(loginTicket.getUserId());
//            httpServletRequest.setAttribute   //不可取，因为后面service还要用到这个用户，随时要知道这个线程调用的用户是谁
            hostHolder.setUser(user);   //把这次请求的这个用户存起来
        }
        //return false;  //请求之前返回false就直接退出来了，不用请求了
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //在渲染之前，把用户存储进来，则在渲染的页面上就可以用user了
        //在页面上，如果有用户，则显示名字，没有的话则显示登陆
        if (modelAndView != null && hostHolder.getUser() != null) {
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //收尾工作
        hostHolder.clear();   //否则每次登陆都放进去就要炸了
    }
}
