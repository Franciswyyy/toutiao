package com.nowcoder.dao;

import com.nowcoder.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    String TABLE_NAME = "user";
    String INSET_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id, name, password, salt, head_url";

    @Insert({"insert into ", TABLE_NAME, "(", INSET_FIELDS,
            ") values (#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);
}
