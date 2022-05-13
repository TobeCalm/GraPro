package life.witold.community.controller;

import life.witold.community.dto.PaginationDTO;
import life.witold.community.mapper.UserMapper;
import life.witold.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(
                        Model model,
                        @RequestParam(name="page", defaultValue = "1") Integer page,
                        @RequestParam(name="size", defaultValue = "8") Integer size)
    {
        PaginationDTO paginationDTO = questionService.list(page, size);
        model.addAttribute("paginationDTO", paginationDTO);
        return "index";
    }
}
