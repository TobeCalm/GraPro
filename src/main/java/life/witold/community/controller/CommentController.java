package life.witold.community.controller;

import life.witold.community.dto.CommentDTO;
import life.witold.community.dto.ResultDTO;
import life.witold.community.exception.CustomizeErrorCode;
import life.witold.community.mapper.CommentMapper;
import life.witold.community.model.Comment;
import life.witold.community.model.CommentExample;
import life.witold.community.model.QuestionExample;
import life.witold.community.model.User;
import life.witold.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class CommentController {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentService commentService;


    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        Comment comment = new Comment();
        CommentExample ce = new CommentExample();
        Integer count = (int)commentMapper.countByExample(ce);
        int id = count + 1;
        comment.setId(id);

        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment);
        return ResultDTO.okOf();
    }

}
