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
    private static Configuration configuration = null;
    private static Map<String, Template> allTemplates = null;

    static {
        configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding("utf-8");
        configuration.setClassicCompatible(true);
        configuration.setClassForTemplateLoading(WordGeneratorWithFreemarker.class, "/wordModel");
        allTemplates = new HashMap<String, Template>();
        try {
//            allTemplates.put("dailyModelmht2", configuration.getTemplate("dailyModelmht2.ftl"));
            allTemplates.put("dailyTemp", configuration.getTemplate("dailyTemp.ftl"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private WordGeneratorWithFreemarker() {

    }

    public static void createDoc(Map<String, Object> dataMap, String templateName, OutputStream out) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
        //String name = "d:\\temp" + (int) (Math.random() * 100000) + ".doc";
        System.out.println(allTemplates);
        Template t = allTemplates.get(templateName);

//        Template t = configuration.getTemplate(templateName);
//        System.out.println(t.toString());
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
        return;
    }


    public static void main(String[] args) throws IOException {
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        // 标题
        dataMap.put("title",("日报"));
        dataMap.put("teacher",("老师"));
        dataMap.put("coach",("教练"));
        dataMap.put("dailyTitle",("日报标题"));
        dataMap.put("dailybody",("日报内容"));
        dataMap.put("studentName",("学生姓名"));
        dataMap.put("teamName",("小组名称"));
        dataMap.put("subDate",("提交日期"));
        dataMap.put("teacherName", ("潘金莲"));
        dataMap.put("coachName", ("西门庆"));
        dataMap.put("dailyTitleValue", WordHtmlGeneratorHelper.string2Ascii("大脑腐写真集"));
        dataMap.put("body", "啥都没学会<p><img src=\"http://img.t.sinajs.cn/t4/appstyle/expression/ext/normal/50/pcmoren_huaixiao_org.png\" alt=\"[坏笑]\" data-w-e=\"1\"><img src=\"d:/1.jpg\" style=\"max-width: 100%;\">&nbsp;sadasdasda<br></p>");

        dataMap.put("studentNameValue", WordHtmlGeneratorHelper.string2Ascii("武松"));
        dataMap.put("teamNameValue", WordHtmlGeneratorHelper.string2Ascii("上山打老hu"));
        dataMap.put("subDateValue", WordHtmlGeneratorHelper.string2Ascii("2020-1-5"));

        String docFilePath = "d:\\temp" + (int) (Math.random() * 100000) + ".doc";
        File f = new File(docFilePath);


        OutputStream out;
        out = new FileOutputStream(f);
        WordGeneratorWithFreemarker.createDoc(dataMap, "dailyTemp", out);
//        WordGeneratorWithFreemarker.createDoc(dataMap, "doczhanglogn", out);

//        try {
//            out = new FileOutputStream(f);
//            WordGeneratorWithFreemarker.createDoc(dataMap, "dailyModelmht", out);
//
//        } catch (FileNotFoundException e) {
//
//        } catch (MalformedTemplateNameException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

}  