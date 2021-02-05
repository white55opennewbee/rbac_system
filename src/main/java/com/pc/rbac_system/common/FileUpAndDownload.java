package com.pc.rbac_system.common;

import cn.hutool.core.lang.UUID;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

public class FileUpAndDownload {
    private static String UUIDFileName(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    //单文件上传
    public static Result ImguploadFile(MultipartFile file,String storePath){
        String fileName = null;
        try {
            InputStream inputStream = file.getInputStream();
            String name = file.getOriginalFilename();
            String extendsName = name.substring(name.lastIndexOf(".")+1);
            if (extendsName.equalsIgnoreCase("jpg")||extendsName.equalsIgnoreCase("png")||extendsName.equalsIgnoreCase("bmp")){
                fileName = UUIDFileName() +"."+ extendsName;
                String storeFile = storePath+"/"+fileName;
                File filePath = new File(storePath);
                if (!filePath.exists()){
                    filePath.mkdirs();
                }
                //写文件
                OutputStream outputStream = new FileOutputStream(storeFile);
                byte[] b = new byte[1024];
                int len = -1;
                while ((len = inputStream.read(b))!=-1){
                    outputStream.write(b,0,len);
                }
                inputStream.close();
                outputStream.close();
            }else {
                return Result.error(CodeMsg.IMG_TYPE_NOT_SURPPORT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success(fileName);

    }

//    public static Result MultiImguploadFile(HttpServletRequest req,String storePath){
//        //存储写入文件后的文件的位置以及文件名//用于返回
//        List<String> FileName = new ArrayList<>();
//
//        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
//        ServletFileUpload upload = new ServletFileUpload(fileItemFactory);
//        try {
//            List<FileItem> items = upload.parseRequest(null); // 参数为requestContext 不知道怎么获取
//            for (FileItem item:items){
//                if (!item.isFormField()){
//                    //获取文件流
//                    InputStream in = item.getInputStream();
//                    //生成新名字
//                    String name = item.getName();
//                    String extendsName = name.substring(name.lastIndexOf(".")+1);
//                    if (!extendsName.equalsIgnoreCase("jpg")||!extendsName.equalsIgnoreCase("png")||!extendsName.equalsIgnoreCase("bmp")){
//                        return Result.error(CodeMsg.IMG_TYPE_NOT_SURPPORT);
//                    }
//                    String fileName = UUIDFileName();
//                    String storeFile = storePath+"/"+fileName+"."+extendsName;
//
//                    File filePath = new File(storePath);
//                    if (!filePath.exists()){
//                        filePath.mkdirs();
//                    }
//                    //写文件
//                    OutputStream outputStream = new FileOutputStream(storeFile);
//                    byte[] b = new byte[1024];
//                    int len = -1;
//                    while ((len = in.read(b))!=-1){
//                        outputStream.write(b,0,len);
//                    }
//                    in.close();
//                    outputStream.close();
//                    FileName.add(fileName);
//                }
//
//            }
//        } catch (FileUploadException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return Result.success(FileName);
//    }

    public void FileDownload(){

    }
}
