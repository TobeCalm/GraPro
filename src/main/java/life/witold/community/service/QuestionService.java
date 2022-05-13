package life.witold.community.service;

import life.witold.community.dto.PaginationDTO;
import life.witold.community.dto.QuestionDTO;
import life.witold.community.mapper.QuestionMapper;
import life.witold.community.mapper.UserMapper;
import life.witold.community.model.Question;
import life.witold.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();
        paginationDTO.setPagination(totalCount, page,size);

        if(page<1){
            page = 1;
        }

        if(page> paginationDTO.getTotalPage()){
            page = paginationDTO.getTotalPage();
        }

        Integer offset = size * (page-1);
        if(offset<0){
            offset = 0;
        }

        List<Question> questions = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOs = new ArrayList<>();

        for(Question question:questions){
            User user = userMapper.findByID(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOs.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOs);

        return paginationDTO;
    }

    public PaginationDTO list(String userId, Integer page, Integer size){
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.countByUserId(userId);
        paginationDTO.setPagination(totalCount,page,size);

        if(page<1){
            page = 1;
        }

        if(page> paginationDTO.getTotalPage()){
            page = paginationDTO.getTotalPage();
        }

        Integer offset = size * (page-1);
        if(offset<0){
            offset = 0;
        }

        List<Question> questions = questionMapper.listByUserId(userId,offset,size);
        List<QuestionDTO> questionDTOs = new ArrayList<>();

        for(Question question:questions){
            User user = userMapper.findByID(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOs.add(questionDTO);
        }

        paginationDTO.setQuestions(questionDTOs);
        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        User user = userMapper.findByID(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        }
        else{
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.update(question);
        }
    }
}
