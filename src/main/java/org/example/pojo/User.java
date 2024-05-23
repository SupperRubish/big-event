package org.example.pojo;



import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class User {
    @NonNull
    private Integer id;//主键ID
    private String username;//用户名
    @JsonIgnore//让springMVC把当前对象转换成json时忽略这个属性
    private String password;//密码
    @NotEmpty
    @Pattern(regexp = "^.{1,10}$")
    private String nickname;//昵称
    @NotEmpty
    @Email
    private String email;//邮箱
    private String userPic;//用户头像地址
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
}
