package com.pc.rbac_system.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * @author reuben.ym
 * @Description: 富文本Html处理器，主要处理图片及编码
 * @create 21.2.25 17:45
 */
public class RichHtmlHandler {

    private Document doc = null;
    private String html;

    private String docSrcParent = "";
    private String docSrcLocationPrex = "";
    private String nextPartId;
    private String shapeidPrex;
    private String spidPrex;
    private String typeid;

    private String handledDocBodyBlock;
    private List<String> docBase64BlockResults = new ArrayList<>();
    private List<String> xmlImgRefs = new ArrayList<>();

    public String getDocSrcLocationPrex() {
        return docSrcLocationPrex;
    }

    public void setDocSrcLocationPrex(String docSrcLocationPrex) {
        this.docSrcLocationPrex = docSrcLocationPrex;
    }

    public String getNextPartId() {
        return nextPartId;
    }

    public void setNextPartId(String nextPartId) {
        this.nextPartId = nextPartId;
    }

    public String getHandledDocBodyBlock() {
        String raw = WordHtmlGeneratorHelper.string2Ascii(doc.getElementsByTag("body").html());
        return raw.replace("=3D", "=").replace("=", "=3D");
    }

    public String getRawHandledDocBodyBlock() {
        String raw = doc.getElementsByTag("body").html();
        return raw.replace("=3D", "=").replace("=", "=3D");
    }

    public List<String> getDocBase64BlockResults() {
        return docBase64BlockResults;
    }

    public List<String> getXmlImgRefs() {
        return xmlImgRefs;
    }

    public String getShapeidPrex() {
        return shapeidPrex;
    }

    public void setShapeidPrex(String shapeidPrex) {
        this.shapeidPrex = shapeidPrex;
    }

    public String getSpidPrex() {
        return spidPrex;
    }

    public void setSpidPrex(String spidPrex) {
        this.spidPrex = spidPrex;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getDocSrcParent() {
        return docSrcParent;
    }

    public void setDocSrcParent(String docSrcParent) {
        this.docSrcParent = docSrcParent;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public RichHtmlHandler(String html) {
        doc = Jsoup.parse(wrapHtml(html));
    }

    public void reInit(String html) {
        doc = null;
        doc = Jsoup.parse(wrapHtml(html));
        docBase64BlockResults.clear();
        xmlImgRefs.clear();
    }

    /**
     * @Description: 获得已经处理过的HTML文件
     */
    public void handledHtml(boolean isWebApplication) {
        Elements imags = doc.getElementsByTag("img");

        if (imags == null || imags.size() == 0) {
            // 返回编码后字符串
            return;
            //handledDocBodyBlock = WordHtmlGeneratorHelper.string2Ascii(html);
        }

        // 转换成word mht 能识别图片标签内容，去替换html中的图片标签

        for (Element item : imags) {
            // 把文件取出来
            String srcRealPath = item.attr("src");

//			if (isWebApplication) {
//				String contentPath=RequestResponseContext.getRequest().getContextPath();
//				if(!StringUtils.isEmpty(contentPath)){
//					if(src.startsWith(contentPath)){
//						src=src.substring(contentPath.length());
//					}
//				}
//
//				srcRealPath = RequestResponseContext.getRequest().getSession()
//						.getServletContext().getRealPath(src);
//
//			}

            File imageFile = new File(srcRealPath);
            String imageFileShortName = imageFile.getName();
            // 获取文件后缀名
            String fileName = srcRealPath.substring(srcRealPath.lastIndexOf("\\") + 1);
            String fileTypeName = fileName.substring(fileName.lastIndexOf(".") + 1);
            String docFileName = "image" + UUID.randomUUID().toString().replaceAll("-", "") + "."
                    + fileTypeName;
            String srcLocationShortName = docSrcParent + "/" + docFileName;
            String styleAttr = item.attr("style"); // 样式
            //高度
            String imagHeightStr = item.attr("height");
            if (StringUtils.isEmpty(imagHeightStr)) {
                imagHeightStr = getStyleAttrValue(styleAttr, "height");
            }
            //宽度
            String imagWidthStr = item.attr("width");
            if (StringUtils.isEmpty(imagHeightStr)) {
                imagHeightStr = getStyleAttrValue(styleAttr, "width");
            }

            imagHeightStr = imagHeightStr.replace("px", "");
            imagWidthStr = imagWidthStr.replace("px", "");
            if (StringUtils.isEmpty(imagHeightStr)) {
                //去得到默认的文件高度
                imagHeightStr = "0";
            }
            if (StringUtils.isEmpty(imagWidthStr)) {
                imagWidthStr = "0";
            }
            int imageHeight = Integer.parseInt(imagHeightStr);
            int imageWidth = Integer.parseInt(imagWidthStr);

            // 得到文件的word mht的body块
            String handledDocBodyBlock = WordImageConvector.toDocBodyBlock(srcRealPath,
                    imageFileShortName, imageHeight, imageWidth, styleAttr,
                    srcLocationShortName, shapeidPrex, spidPrex, typeid);

            item.parent().append(handledDocBodyBlock);
            item.remove();
            // 去替换原生的html中的imag

            String base64Content = null;
            try {
                base64Content = WordImageConvector
                        .imageToBase64(srcRealPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String contextLoacation = docSrcLocationPrex + "/" + docSrcParent
                    + "/" + docFileName;

            String docBase64BlockResult = WordImageConvector
                    .generateImageBase64Block(nextPartId, contextLoacation,
                            fileTypeName, base64Content);
            docBase64BlockResults.add(docBase64BlockResult);

            String imagXMLHref = "<o:File HRef=3D\"" + docFileName + "\"/>";
            xmlImgRefs.add(imagXMLHref);

        }

    }

    private String getStyleAttrValue(String style, String attributeKey) {
        if (StringUtils.isEmpty(style)) {
            return "";
        }

        // 以";"分割
        String[] styleAttrValues = style.split(";");
        for (String item : styleAttrValues) {
            // 在以 ":"分割
            String[] keyValuePairs = item.split(":");
            if (attributeKey.equals(keyValuePairs[0])) {
                return keyValuePairs[1];
            }
        }

        return "";
    }

    private String wrapHtml(String html) {
        // 因为传递过来都是不完整的doc
        return "<html>" +
                "<body>" +
                html +
                "</body>" +
                "</html>";
    }

}
