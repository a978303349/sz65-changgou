package com.changgou.user.dao;
import com.changgou.user.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:liaoyanglin
 * @Description:User的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface UserMapper extends Mapper<User> {
    /**
     * 增加用户积分
     * @param name
     * @param pint
     * @return
     */
    @Update("UPDATE tb_user SET points+#{point} where username=#{username}")
    int addUserPoints(@Param("username") String name,@Param("point") Integer pint);
}
