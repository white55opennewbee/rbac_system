package com.pc.rbac_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EnglishWord extends BaseModel {
    private String englishWord;
    private String chinese;
    private String author;
    private Long agree_count;
    private Long studentId;
}
