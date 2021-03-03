package com.pc.rbac_system.common;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;


/**
 * @author reuben.ym
 * @create 21.3.3 15:06
 */
class WordGeneratorWithFreemarkerTest {

    @Test
    void createDoc() {
        HashMap<String, Object> dataMap = new HashMap<>();
        // 标题
        dataMap.put("title", ("日报"));
        dataMap.put("teacher", ("老师"));
        dataMap.put("coach", ("教练"));
        dataMap.put("dailyTitle", ("日报标题"));
        dataMap.put("dailyBody", ("日报内容"));
        dataMap.put("studentName", ("学生姓名"));
        dataMap.put("teamName", ("小组名称"));
        dataMap.put("subDate", ("提交日期"));
        dataMap.put("teacherName", ("潘金莲"));
        dataMap.put("coachName", ("西门庆"));
        dataMap.put("dailyTitleValue", WordHtmlGeneratorHelper.string2Ascii("大脑腐写真集"));
//        dataMap.put("body", "啥都没学会<p><img src=\"http://img.t.sinajs.cn/t4/appstyle/expression/ext/normal/50/pcmoren_huaixiao_org.png\" alt=\"[坏笑]\" data-w-e=\"1\"><img src=\"d:/1.jpg\" style=\"max-width: 100%;\">&nbsp;sadasdasda<br></p>");

        String body ="啥都没学会<img style='height:100px;width:200px;display:block;' src='D:\\2.jpg' alt=\"[坏笑]\" data-w-e=\"1\"><img src='D:\\2.jpg' style=\"max-width: 100%;\">&nbsp;sadasdasda<br>";

        RichHtmlHandler handler = new RichHtmlHandler(body);
        // 文件名称
        String wordFileName = "dailyTemp";
        // 必须和模板一致
        handler.setDocSrcLocationPrex("file:///C:/1FBBA210");
        handler.setDocSrcParent(String.format("%s.files", wordFileName));
        // 必须和模板一致
        handler.setNextPartId("01D70C21.BAE36DC0");
        handler.setShapeidPrex("_x56fe__x7247__x0020");
        handler.setSpidPrex("_x0000_i");
        handler.setTypeid("#_x0000_t75");

        handler.handledHtml(false);
        dataMap.put("body", handler.getHandledDocBodyBlock());
        StringBuilder handledBase64Block = new StringBuilder();
        if (handler.getDocBase64BlockResults() != null
                && handler.getDocBase64BlockResults().size() > 0) {
            for (String item : handler.getDocBase64BlockResults()) {
                handledBase64Block.append(item).append("\n");
            }
        }

        dataMap.put("imagesBase64String", handledBase64Block.toString());
        StringBuilder xmlimaHref = new StringBuilder();

        if (handler.getXmlImgRefs() != null
                && handler.getXmlImgRefs().size() > 0) {
            for (String item : handler.getXmlImgRefs()) {
                xmlimaHref.append(item).append("\n");
            }
        }
        dataMap.put("imagesXmlHrefString", xmlimaHref.toString());


        dataMap.put("studentNameValue", WordHtmlGeneratorHelper.string2Ascii("武松"));
        dataMap.put("teamNameValue", WordHtmlGeneratorHelper.string2Ascii("上山打老hu"));
        dataMap.put("subDateValue", WordHtmlGeneratorHelper.string2Ascii("2020-1-5"));

        String docFilePath = "d:\\temp" + (int) (Math.random() * 100000) + ".doc";
        File f = new File(docFilePath);


        OutputStream out;

        try {
            out = new FileOutputStream(f);
            WordGeneratorWithFreemarker.createDoc(dataMap, "dailyTemp", out);
        } catch (FileNotFoundException ignored) {

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}