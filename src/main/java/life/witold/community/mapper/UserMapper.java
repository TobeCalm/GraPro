package life.witold.community.mapper;

import life.witold.community.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

@Mapper
public interface UserMapper {
    @Insert("insert into GITHUBUSER(name,account_id,token,gmt_create,gmt_modified,avatar_url) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Select("select * from GITHUBUSER where token = #{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from GITHUBUSER where account_id = #{id}")
    User findByID(@Param("id")String id);

    @Update("update GITHUBUSER set name = #{name}, token = #{token}, gmt_modified = #{gmtModified}, avatar_url = #{avatarUrl} where account_id = #{accountId}")
    void update(User user);


}
