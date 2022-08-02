package com.board.projectboard.service;

import com.board.projectboard.domain.Article;
import com.board.projectboard.domain.type.SearchType;
import com.board.projectboard.dto.ArticleDto;
import com.board.projectboard.dto.ArticleUpdateDto;
import com.board.projectboard.dto.ArticleWithCommentsDto;
import com.board.projectboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String search_keyword, Pageable pageable) {
        if(search_keyword == null || search_keyword.isBlank()){
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }
        return switch (searchType) {
            case TITLE -> articleRepository.findByTitleContaining(search_keyword,pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(search_keyword,pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(search_keyword,pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(search_keyword,pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtag("#"+search_keyword,pageable).map(ArticleDto::from);
        };
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }

    public void saveArticle(ArticleDto dto) {
        articleRepository.save(dto.toEntity());
    }

    public void updateArticle(ArticleDto dto) {
        try{
            Article article= articleRepository.getReferenceById(dto.id());
            if(dto.title() != null) {article.setTitle(dto.title()); }
            if(dto.content() != null) {article.setContent(dto.content()); }
            article.setHashtag(dto.hashtag());
        } catch (EntityNotFoundException e){
                log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다 - dto : {}",dto);
        }

    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }
}

