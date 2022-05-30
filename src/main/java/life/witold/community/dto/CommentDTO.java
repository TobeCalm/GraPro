package life.witold.community.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private  int parentId;
    private  String content;
    private  Integer type;
}
