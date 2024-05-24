package org.example.controller;

import org.example.pojo.Result;
import org.example.pojo.User;
import org.example.service.UserService;
import org.example.utils.JwtUtil;
import org.example.utils.Md5Util;
import org.example.utils.ThreadLocalUtil;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Validated
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,12, TimeUnit.HOURS);
            return Result.success(token);
        }
        return Result.error("密码错误");
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo(/*@RequestHeader(name="Authorization") String token*/){
        //根据用户名查用户
//        Map<String,Object> map = JwtUtil.parseToken(token);
//        String username = (String)map.get("username");
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }
    @PostMapping("/update")
    public Result update(@RequestBody @Validated User user){
        Map<String,Object> map = ThreadLocalUtil.get();
        user.setId((Integer) map.get("id"));
        userService.update(user);
        return Result.success();
    }
    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }
    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String,String> params,@RequestHeader("Authorization") String token){
        //校验参数
        String oldPwd = params.get("old_pwd");
        String rePwd = params.get("re_pwd");
        String newPwd = params.get("new_pwd");

        if(!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(rePwd) || !StringUtils.hasLength(newPwd)){
            return Result.error("缺少必要参数");
        }
        //校验原密码是否正确
        //调用userService根据用户名拿到原密码，再和oldpwd比对
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User loginUser = userService.findByUserName(username);
        if(!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))){
            return Result.error("原密码不正确");
        }
        //校验新密码和确认新密码是否一致
        if(!rePwd.equals(newPwd)){
            return Result.error("再次填写的密码不一致");
        }

        //调用service完成密码更新
        userService.updatePwd(newPwd);

        //删除对应的token
        ValueOperations<String, String> operation = stringRedisTemplate.opsForValue();
        operation.getOperations().delete(token);

        return Result.success();

    }
}
