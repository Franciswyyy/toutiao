package com.nowcoder.service;

import com.nowcoder.dao.NewsDao;
import com.nowcoder.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


//service层就是调dao层
@Service
public class NewsService {
    @Autowired
    private NewsDao newsDao;

    //最新的新闻读取出来，服务定义的接口通用些
    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDao.selectByUserIdAndOffset(userId, offset, limit);
    }
}
