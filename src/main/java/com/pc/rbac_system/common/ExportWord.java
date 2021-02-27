package com.pc.rbac_system.common;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

public class ExportWord {
    private Configuration configuration = null;

    public ExportWord(){
        configuration = new Configuration(new Version("2.3.0"));
        configuration.setDefaultEncoding("UTF-8");
    }

//    public static void main(String[] args) {
//        WordTest test = new WordTest();
//        test.createWord();
//    }

//    public void createWord(Map<String,Object> dataMap, HttpServletResponse response){
////        dataMap=new HashMap<String,Object>();
////        getData(dataMap);
//        configuration.setClassForTemplateLoading(this.getClass(), "/wordModel");//模板文件所在路径
//        Template t=null;
//        try {
////            configuration.setDirectoryForTemplateLoading(new File("F:/桌面/123/rbac_system/src/main/resources/wordModel"));
//            t = configuration.getTemplate("/dailyModelVersionOne.ftl","utf-8"); //获取模板文件
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        File outFile = new File("C:\\Users\\吴智渊\\Desktop\\日报.doc");
////        File outFile = new File("F:\\桌面\\123\\rabcWord\\"+dataMap.get("studentName")+".doc"); //导出文件
//        Writer out = null;
//        try {
////            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"), 10240);
//            ServletOutputStream outputStream = response.getOutputStream();
//            out = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"), 10240);
//            t.process(dataMap, out); //将填充数据填入模板文件并输出到目标文件
//            out.flush();
//            out.close();
//        } catch (FileNotFoundException e1) {
//            e1.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (TemplateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }



    public void createWord(Map<String,Object> dataMap,HttpServletResponse response){
        // 标题
        dataMap.put("title",("日报"));
        dataMap.put("teacher",("老师"));
        dataMap.put("coach",("教练"));
        dataMap.put("dailyTitle",("日报标题"));
        dataMap.put("dailybody",("日报内容"));
        dataMap.put("studentName",("学生姓名"));
        dataMap.put("teamName",("小组名称"));
        dataMap.put("subDate",("提交日期"));
//        dataMap.put("teacherName", ("潘金莲"));
//        dataMap.put("coachName", ("西门庆"));
//        dataMap.put("dailyTitleValue", WordHtmlGeneratorHelper.string2Ascii("大脑腐写真集"));
//        dataMap.put("body", "啥都没学会<p><img src=\"http://img.t.sinajs.cn/t4/appstyle/expression/ext/normal/50/pcmoren_huaixiao_org.png\" alt=\"[坏笑]\" data-w-e=\"1\"><img src=\"d:/1.jpg\" style=\"max-width: 100%;\">&nbsp;sadasdasda<br></p>");
//
//        dataMap.put("studentNameValue", WordHtmlGeneratorHelper.string2Ascii("武松"));
//        dataMap.put("teamNameValue", WordHtmlGeneratorHelper.string2Ascii("上山打老hu"));
//        dataMap.put("subDateValue", WordHtmlGeneratorHelper.string2Ascii("2020-1-5"));

//        String docFilePath = "d:\\temp" + (int) (Math.random() * 100000) + ".doc";
//        File f = new File(docFilePath);
//        out = new FileOutputStream(f);
        response.setContentType("application/octet-stream");
//        response.addHeader("Content-Disposition", "attachment;filename=" + new String((dataMap.get("studentNameValue").toString()+".doc").getBytes()));
//        response.addHeader("Content-Disposition", "attachment;filename="+dataMap.get("studentNameValue").toString()+"-"+dataMap.get("subDateValue").toString()+".doc");
        try {
            response.setHeader("Content-Disposition", "inline;fileName=\"" + new String((dataMap.get("studentNameValue").toString()+"-"+dataMap.get("subDateValue").toString()+".doc").getBytes("utf-8"),"ISO8859-1") + "\"");
            System.out.println(response.getHeader("Content-Disposition"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        response.addHeader("Content-Length", "" + file.length());
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            WordGeneratorWithFreemarker.createDoc(dataMap, "dailyTemp", out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public Map getData() {
        Map dataMap = new HashMap();
        dataMap.put("teacherName", "pc");
        dataMap.put("coachName", "lp");
        dataMap.put("title", "骄傲的杰卡斯来得");
//        dataMap.put("body", "啥都没学会<p><img src=\"http://img.t.sinajs.cn/t4/appstyle/expression/ext/normal/50/pcmoren_huaixiao_org.png\" alt=\"[坏笑]\" data-w-e=\"1\"><img src=\"/rbac_static_web/myhtml/img/f30f6656-a3f9-4787-bca4-db88df0bfb0a.png\" style=\"max-width: 100%;\">&nbsp;sadasdasda<br></p>");
        dataMap.put("body", "asdasdasdasdasdas");

        dataMap.put("studentName", "zhanglogn");
        dataMap.put("teamName", "上天组");
        dataMap.put("dailyTime", "2020-1-5");
        return dataMap;
    }
}
