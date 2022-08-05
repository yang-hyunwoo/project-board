package com.board.projectboard.controller;


import com.board.projectboard.dto.UserAccountDto;
import com.board.projectboard.dto.request.ArticleCommentRequest;
import com.board.projectboard.dto.request.ArticleRequest;
import com.board.projectboard.dto.security.BoardPrincipal;
import com.board.projectboard.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public String postNewArticleComment(ArticleCommentRequest articleCommentRequest ,
                                        @AuthenticationPrincipal BoardPrincipal boardPrincipal)
    { 
        //TODO : 인증 정보를 넣어 줘야 한다.
        articleCommentService.saveArticleComment(articleCommentRequest.toDto(boardPrincipal.toDto()));

        return "redirect:/articles/" +articleCommentRequest.articleId() ;
    }

    @PostMapping ("/{commentId}/delete")
    public String deleteArticleComment(@PathVariable Long commentId ,
                                       Long articleId ,
                                       @AuthenticationPrincipal BoardPrincipal boardPrincipal)
    {
        articleCommentService.deleteArticleComment(commentId,boardPrincipal.getUsername());

        return "redirect:/articles/"+articleId;
    }
}
