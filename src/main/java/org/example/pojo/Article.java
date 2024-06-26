package org.example.pojo;


import lombok.Data;
import org.example.anno.State;
import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
@Data
public class Article {
    private Integer id;//主键ID
    @NotEmpty
    @Pattern(regexp = "^.{1,10}$")
    private String title;//文章标题
    @NotEmpty
    private String content;//文章内容
    @URL
    private String coverImg;//封面图像
    @State
    private String state;//发布状态 已发布|草稿
    private Integer categoryId;//文章分类id
//    @NotNull
    private Integer createUser;//创建人ID
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
}
