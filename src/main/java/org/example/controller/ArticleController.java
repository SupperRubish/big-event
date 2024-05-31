package org.example.controller;

import org.example.pojo.Article;
import org.example.pojo.PageBean;
import org.example.pojo.Result;
import org.example.service.ArticleService;
import org.example.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@Validated
@CrossOrigin//支持跨域
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/add")
    public Result add(@RequestBody @Validated Article article){
        articleService.add(article);
        return Result.success();
    }
    @GetMapping("/list")
    public Result<PageBean<Article>> list(Integer pageNum,Integer pageSize,@RequestParam(required = false)Integer categoryId,@RequestParam(required = false)String state){
        PageBean<Article> pb = articleService.list(pageNum,pageSize,categoryId,state);
        return Result.success(pb);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Article article){
        articleService.update(article);
        return Result.success();
    }

    @GetMapping("/detail")
    public Result<Article> detail(@RequestParam Integer id){
        Article article = articleService.findArticleById(id);
        return Result.success(article);

    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam Integer id){
        articleService.deleteArticleById(id);
        return Result.success();
    }




}
