package com.pc.rbac_system.mapper;

import com.pc.rbac_system.model.EnglishWord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EnglishWordMapper {

    void addEnglishWord(EnglishWord englishWord);

    List<EnglishWord> findAllEnglishWord(Integer currentPage, Integer maxSize, String words);

    Integer deleteEnglishWord(Long wordId);

    Integer updateEnglishWord(EnglishWord englishWord);

    EnglishWord findEnglishWordById(Long id);

    Integer englishAgree(@Param("word") Long wordId,@Param("count") Integer agree);

    List<EnglishWord> findEnglishWordsByStudentId(Long studentId);

    List<EnglishWord> myFavoriteEnglishWords(@Param("userId") Long studentId,@Param("words") String words);

    Integer setFavoriteEnglishWords(@Param("userId") Long userId,@Param("wordId") Long wordId,@Param("option") Byte option);

    Integer setAgreeRelation(@Param("userId") Long userId, @Param("wordId") Long wordId, @Param("agree") Integer agree);

    List<EnglishWord> findAllAgreeEnglishWords(Long userId);
}
