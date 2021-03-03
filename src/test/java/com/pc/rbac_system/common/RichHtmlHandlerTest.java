package com.pc.rbac_system.common;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author reuben.ym
 * @create 21.3.3 15:03
 */
class RichHtmlHandlerTest {

    @Test
    void handledHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("啥都没学会<p><img style='height:100px;width:200px;display:block;' src='D:\\2.jpg' alt=\"[坏笑]\" data-w-e=\"1\"><img src='D:\\2.jpg' style=\"max-width: 100%;\">&nbsp;sadasdasda<br></p>");
        RichHtmlHandler handler = new RichHtmlHandler(sb.toString());
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

        //写入文件中，
        try {
            handler.handledHtml(false);

            String logFile = "D:\\log.txt";

            File file = new File(logFile);
            //FileOutputStream out=new FileOutputStream(file);
            FileWriter fw = new FileWriter(file);


            fw.write("======handledDocBody block==========\n");
            fw.write(handler.getHandledDocBodyBlock());

            fw.write("======handledBase64Block==========\n");
            if (handler.getDocBase64BlockResults() != null
                    && handler.getDocBase64BlockResults().size() > 0) {
                for (String item : handler.getDocBase64BlockResults()) {
                    fw.write(item + "\n");
                }
            }
            if (handler.getXmlImgRefs() != null
                    && handler.getXmlImgRefs().size() > 0) {
                fw.write("======xmlimaHref==========\n");
                for (String item : handler.getXmlImgRefs()) {
                    fw.write(item + "\n");
                }
            }

            fw.close();

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}