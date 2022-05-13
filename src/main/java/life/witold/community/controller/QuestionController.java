package life.witold.community.controller;

import life.witold.community.dto.QuestionDTO;
import life.witold.community.mapper.QuestionMapper;
import life.witold.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id") Integer questionId,
                           Model model)
    {
        QuestionDTO questionDTO = questionService.getById(questionId);
        model.addAttribute("question", questionDTO);
        return  "question";
    }
}
