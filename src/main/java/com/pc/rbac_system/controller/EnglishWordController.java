package com.pc.rbac_system.controller;

import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.model.EnglishWord;
import com.pc.rbac_system.service.IEnglishWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/word")
public class EnglishWordController {
    @Autowired
    IEnglishWordService englishWordService;


    @GetMapping("/findAllEnglishWords/{currentPage}/{maxSize}/{words}")
    public Result findAllEnglishWord(@PathVariable Integer currentPage,@PathVariable Integer maxSize,@PathVariable String words){
        return Result.success(englishWordService.findAllEnglishWord(currentPage,maxSize,words));
    }
    @PostMapping("/addEnglishWord")
    public Result addEnglishWord(@RequestBody EnglishWord englishWord){
        return Result.success(englishWordService.addEnglishWord(englishWord));
    }
    @DeleteMapping("/deleteEnglishWord/{wordId}")
    public Result deleteEnglishWord(@PathVariable Long wordId){
        return Result.success(englishWordService.deleteEnglishWord(wordId));
    }

    @PostMapping("/updateEnglishWord")
    public Result updateEnglishWord(@RequestBody EnglishWord englishWord){
        return Result.success(englishWordService.updateEnglishWord(englishWord));
    }

    @GetMapping("/findEnglishWordById/{wordId}")
    public Result findEnglishWordById(@PathVariable Long wordId){
        return Result.success(englishWordService.findEnglishWordById(wordId));
    }

    @GetMapping("/findEnglishWordsByStudentId/{currentPage}/{maxSize}/{studentId}")
    public Result findEnglishWordsByStudentId(@PathVariable Integer currentPage,@PathVariable Integer maxSize,@PathVariable Long studentId){
        return Result.success(englishWordService.findEnglishWordsByStudentId(currentPage,maxSize,studentId));
    }

    @PostMapping("/setEnglishWordAgree/{userId}/{wordId}/{count}")
    public Result englishAgree(@PathVariable Long userId,@PathVariable Long wordId, @PathVariable Integer count){
        return Result.success(englishWordService.EnglishAgree(userId,wordId,count));
    }

    @GetMapping("/findAllAgreeEnglishWords/{currentPage}/{maxSize}/{userId}")
    public Result findAllAgreeEnglishWords(@PathVariable Integer currentPage,@PathVariable Integer maxSize,@PathVariable Long userId){
        return Result.success(englishWordService.findAllAgreeEnglishWords(currentPage,maxSize,userId));
    }

    @GetMapping("/myFavoriteEnglishWords/{currentPage}/{maxSize}/{userId}/{words}")
    public Result FindFavoriteEnglishWords(@PathVariable Integer currentPage,@PathVariable Integer maxSize,@PathVariable Long userId,@PathVariable String words){
        return Result.success(englishWordService.myFavoriteEnglishWords(currentPage,maxSize,userId,words));
    }

    @PutMapping("/setFavoriteEnglishWords/{userId}/{wordId}/{option}")
    public Result setFavoriteEnglishWords(@PathVariable Long userId,@PathVariable Long wordId,Byte option){
        return Result.success(englishWordService.setFavoriteEnglishWords(userId,wordId,option));
    }

}
