package com.board.projectboard.controller;


import com.board.projectboard.dto.UserAccountDto;
import com.board.projectboard.dto.request.ArticleCommentRequest;
import com.board.projectboard.dto.request.ArticleRequest;
import com.board.projectboard.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/comments")
@Controller
@RequiredArgsConstructor
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;


    @PostMapping("/new")
    public String postNewArticleComment(ArticleCommentRequest articleCommentRequest) {
        //TODO : 인증 정보를 넣어 줘야 한다.
        articleCommentService.saveArticleComment(articleCommentRequest.toDto(UserAccountDto.of(
                "gusdn","pw","gusdn@naver.com","gusdn",null
        )));

        return "redirect:/articles/" +articleCommentRequest.articleId() ;
    }

    @PostMapping ("/{commentId}/delete")
    public String deleteArticleComment(@PathVariable Long commentId , Long articleId) {

        articleCommentService.deleteArticleComment(commentId);

        return "redirect:/articles/"+articleId;
    }
}
