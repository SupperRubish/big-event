package org.example.controller;

import org.example.pojo.Result;
import org.example.pojo.User;
import org.example.service.UserService;
import org.example.utils.JwtUtil;
import org.example.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^[a-zA-Z0-9_]{5,16}$") String username, @Pattern(regexp = "^[a-zA-Z0-9_]{5,16}$")String password){
            //查询用户
            User u = userService.findByUserName(username);
            if(u==null){
                //注册
                userService.register(username,password);
                return Result.success();
            }else {
                return Result.error("用户名已经被占用");
            }
    }

    @PostMapping("/login")
    public Result login(@Pattern(regexp = "^[a-zA-Z0-9_]{5,16}$")String username, @Pattern(regexp = "^[a-zA-Z0-9_]{5,16}$")String password){
        //根据用户名查询user
        User loginUser = userService.findByUserName(username);
        //判断该用户是否存在
        if(loginUser==null){
            return Result.error("用户名不存在");
        }

        //密码是否正确
        if(Md5Util.getMD5String(password).equals(loginUser.getPassword())){
            //登陆成功
            Map<String,Object> claim = new HashMap<>();
            claim.put("username",loginUser.getUsername());
            claim.put("id",loginUser.getId());
            String token = JwtUtil.genToken(claim);
            return Result.success(token);
        }
        return Result.error("密码错误");
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo(@RequestHeader(name="Authorization") String token){
        //根据用户名查用户
        Map<String,Object> map = JwtUtil.parseToken(token);
        String username = (String)map.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }
}
