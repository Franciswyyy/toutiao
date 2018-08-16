package com.nowcoder.service;

import com.nowcoder.dao.NewsDao;
import com.nowcoder.model.News;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;



@Service
public class NewsService {
    @Autowired
    private NewsDao newsDao;


    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDao.selectByUserIdAndOffset(userId, offset, limit);
    }

    public int addNews(News news) {
        newsDao.addNews(news);
        return news.getId();
    }

    public News getById(int newsId) {
        return newsDao.getById(newsId);
    }


    public String saveImage(MultipartFile file) throws IOException {
        // xxx.xxx.jpg
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0) {
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if (!ToutiaoUtil.isFileAllowed(fileExt)) {
            return null;
        }

        //文件名全部重新命名，加上最后的扩展名
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
        Files.copy(file.getInputStream(), new File(ToutiaoUtil.IMAGE_DIR + fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);


        return ToutiaoUtil.TOUTIAO_DOMAIN + "image?name=" + fileName;
    }

    public int updateCommentCount(int id, int count) {
        return newsDao.updateCommentCount(id, count);
    }

    public int updateLikeCount(int id, int count) {
        return newsDao.updateLikeCount(id, count);
    }

}
