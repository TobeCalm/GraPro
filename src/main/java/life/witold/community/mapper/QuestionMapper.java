package life.witold.community.mapper;

import life.witold.community.dto.QuestionDTO;
import life.witold.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into QUESTION(title, description,gmt_create,gmt_modified,creator,tag) values (#{title}, #{description}, #{gmtCreate}, #{gmtModified},#{creator},#{tag})")
    void create(Question question);

    @Select("select * from QUESTION limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offset, @Param(value="size") Integer size);

    @Select("select count(1) from QUESTION")
    Integer count();

    @Select("select * from QUESTION where creator = #{accountId} limit #{offset},#{size}")
    List<Question> listByUserId(@Param("accountId") String userId,@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);
    @Select("select count(1) from QUESTION where creator= #{accountId}")
    Integer countByUserId(@Param("accountId") String userId);

    @Select("select * from QUESTION where id = #{id}" )
    Question getById(@Param("id") Integer id);

    @Update("update question set title = #{title}, description = #{description}, gmt_modified = #{gmtModified}, tag = #{tag} where id = #{id}")
    void update(Question question);
}
