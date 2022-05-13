package life.witold.community.dto;

import life.witold.community.model.Question;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO>  questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private List<Integer> pages = new ArrayList<>();

    private Integer totalPage;

    public void setPagination(Integer totalCount, Integer page, Integer size) {
        this.page = page;
        Integer totalPage;
        if(totalCount %size==0){
            totalPage = totalCount / size;
        }
        else{
            totalPage = totalCount / size + 1;
        }
        this.totalPage = totalPage;

        if(page<4){
            for(int i=1;i<page;i++){
                pages.add(i);
            }
        }
        else{
            for(int i=3;i>0;i--){
                pages.add(page-i);
            }
        }
        pages.add(page);
        if(totalPage-page<3){
            for(int i=1;i<=totalPage-page;i++){
                pages.add(page+i);
            }
        }
        else{
            for(int i=1;i<=3;i++){
                pages.add(page+i);
            }
        }


        if(page==1){
            showPrevious = false;
        }
        else{
            showPrevious = true;
        }

        if(page==totalPage){
            showNext = false;
        }
        else{
            showNext =true;
        }

        if(pages.contains(1)){
            showFirstPage = false;
        }
        else{
            showFirstPage = true;
        }

        if(pages.contains(totalPage)){
            showEndPage = false;
        }
        else{
            showEndPage = true;
        }



    }
}
