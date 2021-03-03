package com.pc.rbac_system.common;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;


/**
 * @author reuben.ym
 * @Description: word导出帮助类
 * @create 21.2.25 17:46
 */
public class WordGeneratorWithFreemarker {
    private static Configuration configuration;
    private static Map<String, Template> allTemplates;

    static {
        configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding("utf-8");
        configuration.setClassicCompatible(true);
//        configuration.setClassForTemplateLoading(WordGeneratorWithFreemarker.class, "/wordModel");
        configuration.setClassForTemplateLoading(WordGeneratorWithFreemarker.class, "/templates");
        allTemplates = new HashMap<>();
        try {
            allTemplates.put("dailyTemp", configuration.getTemplate("dailyTemp.ftl"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private WordGeneratorWithFreemarker() {

    }

    public static void createDoc(Map<String, Object> dataMap, String templateName, OutputStream out) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
        Template t = allTemplates.get(templateName);

        WordHtmlGeneratorHelper.handleAllObject(dataMap);

        try {
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开  
            Writer w = new OutputStreamWriter(out);
            t.process(dataMap, w);
            w.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }


}  