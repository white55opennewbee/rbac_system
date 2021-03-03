package com.pc.rbac_system.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.rbac_system.mapper.EnglishWordMapper;
import com.pc.rbac_system.model.EnglishWord;
import com.pc.rbac_system.service.IEnglishWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnglishWordServiceImpl implements IEnglishWordService {
    @Autowired
    EnglishWordMapper englishWordMapper;
    @Override
    public PageInfo findAllEnglishWord(Integer currentPage, Integer maxSize, String words) {
        PageHelper.startPage(currentPage,maxSize);
        List<EnglishWord> allEnglishWord = englishWordMapper.findAllEnglishWord(currentPage, maxSize, words);
        PageInfo info = new PageInfo(allEnglishWord);
        return info;
    }

    @Override
    public EnglishWord addEnglishWord(EnglishWord englishWord) {
        englishWordMapper.addEnglishWord(englishWord);
        return englishWord;
    }

    @Override
    public Boolean deleteEnglishWord(Long wordId) {
       return englishWordMapper.deleteEnglishWord(wordId)>0;
    }

    @Override
    public Boolean updateEnglishWord(EnglishWord englishWord) {
        return englishWordMapper.updateEnglishWord(englishWord)>0;
    }

    @Override
    public EnglishWord findEnglishWordById(Long id){
        return englishWordMapper.findEnglishWordById(id);
    }

    @Override
    @Transactional
    public Boolean EnglishAgree(Long userId,Long wordId,Integer agree) {
        Integer effectRow  = englishWordMapper.setAgreeRelation(userId,wordId,agree);
        Integer integer = englishWordMapper.englishAgree(wordId, agree);
        return effectRow+integer>1;
    }

    @Override
    public PageInfo findEnglishWordsByStudentId(Integer currentPage, Integer maxSize, Long studentId) {
        PageHelper.startPage(currentPage,maxSize,true,false,true);
        List<EnglishWord> lists = englishWordMapper.findEnglishWordsByStudentId(studentId);
        PageInfo info = new PageInfo(lists);
        return info;
    }

    @Override
    public PageInfo myFavoriteEnglishWords(Integer currentPage, Integer maxSize, Long userId, String words) {
        PageHelper.startPage(currentPage,maxSize,true,false,true);
        List<EnglishWord> lists = englishWordMapper.myFavoriteEnglishWords(userId,words);
        PageInfo info = new PageInfo(lists);
        return info;
    }

    @Override
    public Boolean setFavoriteEnglishWords(Long userId, Long wordId, Byte option) {
        return englishWordMapper.setFavoriteEnglishWords(userId,wordId,option)>0;
    }

    @Override
    public PageInfo findAllAgreeEnglishWords(Integer currentPage, Integer maxSize, Long userId) {
        PageHelper.startPage(currentPage,maxSize,true,false,true);
        List<EnglishWord> list = englishWordMapper.findAllAgreeEnglishWords(userId);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }
}
