package life.witold.community.service;

import life.witold.community.dto.PaginationDTO;
import life.witold.community.dto.QuestionDTO;
import life.witold.community.exception.CustomizeErrorCode;
import life.witold.community.exception.CustomizeException;
import life.witold.community.mapper.QuestionMapper;
import life.witold.community.mapper.UserMapper;
import life.witold.community.model.QuestionExample;
import life.witold.community.model.Question;
import life.witold.community.model.QuestionExample;
import life.witold.community.model.User;
import life.witold.community.model.UserExample;
import org.apache.ibatis.session.RowBounds;
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
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
        paginationDTO.setPagination(totalCount, page,size);

        Integer offset = size * (page-1);

        if(offset<0){
            offset = 0;
        }

        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(), new RowBounds(offset,size));

        List<QuestionDTO> questionDTOs = new ArrayList<>();

        for(Question question:questions){
            UserExample userExample = new UserExample();
            userExample.createCriteria().andAccountIdEqualTo(question.getCreator());
            List<User> users = userMapper.selectByExample(userExample);
            User user = users.get(0);

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

        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(example);
        
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

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExample, new RowBounds(offset,size));

        List<QuestionDTO> questionDTOs = new ArrayList<>();

        for(Question question:questions){
            UserExample userExample = new UserExample();
            userExample.createCriteria().andAccountIdEqualTo(question.getCreator());
            List<User> users = userMapper.selectByExample(userExample);
            User user = users.get(0);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOs.add(questionDTO);
        }

        paginationDTO.setQuestions(questionDTOs);
        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andIdEqualTo(id);
        List<Question> questions= questionMapper.selectByExampleWithBLOBs(questionExample);

        if(questions.size()==0){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }

        Question question = questions.get(0);
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(question.getCreator());

        List<User> users = userMapper.selectByExample(userExample);
        User user = users.get(0);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            System.out.println("Insert Question");
            QuestionExample ue = new QuestionExample();
            Integer count = (int)questionMapper.countByExample(ue);
            question.setId(count+1);
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        }
        else{
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int update = questionMapper.updateByExampleSelective(updateQuestion, example);
            if(update != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Integer questionId) {
        Question question = questionMapper.selectByPrimaryKey(questionId);
        Question updateQuestion = new Question();
        updateQuestion.setViewCount(question.getViewCount() + 1);
        QuestionExample example = new QuestionExample();
        example.createCriteria().andIdEqualTo(questionId);
        questionMapper.updateByExampleSelective(updateQuestion, example);
    }

    public void incComment(Integer questionId){
        Question quesiton = questionMapper.selectByPrimaryKey(questionId);
        Question updateQuestion = new Question();
        updateQuestion.setCommentCount(quesiton.getCommentCount() + 1);
        QuestionExample example = new QuestionExample();
        example.createCriteria().andIdEqualTo(questionId);
        questionMapper.updateByExampleSelective(updateQuestion, example);
    }

}
