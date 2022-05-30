package life.witold.community.service;

import life.witold.community.enums.CommentTypeEnum;
import life.witold.community.exception.CustomizeErrorCode;
import life.witold.community.exception.CustomizeException;
import life.witold.community.mapper.CommentMapper;
import life.witold.community.mapper.QuestionMapper;
import life.witold.community.model.Comment;
import life.witold.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;


    @Transactional
    public void insert(Comment comment){
        if(comment.getParentId()==null|| comment.getParentId()==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_WRONG);
        }

        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            commentMapper.insert(comment);
        }
        else{
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);

            }
            commentMapper.insert(comment);
            questionService.incComment(question.getId());
        }
    }
}
