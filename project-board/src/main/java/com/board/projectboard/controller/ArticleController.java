package com.board.projectboard.controller;

import com.board.projectboard.domain.type.SearchType;
import com.board.projectboard.dto.response.ArticleResponse;
import com.board.projectboard.dto.response.ArticleWithCommentsResponse;
import com.board.projectboard.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@RequestMapping("/articles")
@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public String articles(@RequestParam(required = false) SearchType searchType ,
                           @RequestParam(required = false) String searchValue,
                           @PageableDefault(size = 10 , sort="createdAt",direction = Sort.Direction.DESC) Pageable pageable,
                           ModelMap map)
    {
        Page<ArticleResponse> articles = articleService.searchArticles(searchType, searchValue, pageable).map(ArticleResponse::from);
//        articles.forEach(o-> System.out.println("왜그럼??"+o.title()));
        map.addAttribute("articles", articles);
        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String articles(@PathVariable Long articleId, ModelMap map) {
    ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(articleService.getArticle(articleId));
        map.addAttribute("article", article);
        map.addAttribute("articleComments", article.articleCommentsResponse());
        return "articles/detail";
    }

}
