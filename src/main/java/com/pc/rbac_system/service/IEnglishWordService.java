package com.pc.rbac_system.service;

import com.github.pagehelper.PageInfo;
import com.pc.rbac_system.model.EnglishWord;

import java.util.List;


public interface IEnglishWordService {
    PageInfo findAllEnglishWord(Integer currentPage, Integer maxSize, String words);
    EnglishWord addEnglishWord(EnglishWord englishWord);

    Boolean deleteEnglishWord(Long wordId);

    Boolean updateEnglishWord(EnglishWord englishWord);

    EnglishWord findEnglishWordById(Long id);

    Boolean EnglishAgree(Long userId,Long wordId,Integer agree);

    PageInfo findEnglishWordsByStudentId(Integer currentPage, Integer maxSize, Long studentId);

    PageInfo myFavoriteEnglishWords(Integer currentPage, Integer maxSize, Long userId, String words);

    Boolean setFavoriteEnglishWords(Long userId, Long wordId, Byte option);

    PageInfo findAllAgreeEnglishWords(Integer currentPage, Integer maxSize, Long userId);
}
