package com.toiec.toiec.utils;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

public class WordToHtmlUtils {
    public static String pathSaveImg;

    public static void setPathSaveImg(String pathSaveImg) {
        WordToHtmlUtils.pathSaveImg = pathSaveImg;
    }

    public static String covertToHtml (MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            XWPFDocument document = new XWPFDocument(inputStream);
            StringBuilder htmlContent = new StringBuilder();
            // Xử lý các phần tử trong tài liệu Word
            for (IBodyElement element : document.getBodyElements()) {
                if (element.getElementType() == BodyElementType.PARAGRAPH) {
                    XWPFParagraph paragraph = (XWPFParagraph) element;
                    htmlContent.append(handleParagraph(paragraph));
                }else if (element.getElementType() == BodyElementType.TABLE) {
                    XWPFTable table = (XWPFTable) element;
                    htmlContent.append(handleTable(table));
                }
            }
            return htmlContent.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String handleParagraph(XWPFParagraph paragraph) {
        String paraHtml="";

        // Kiểm tra kiểu danh sách
        if (paragraph.getNumFmt() != null) {
            System.out.println(paragraph.getNumFmt().equalsIgnoreCase("decimal"));

            String numId =paragraph.getNumID().toString();
            String level = paragraph.getNumIlvl().toString();
            paraHtml = String.format("""
                        <p class="tdoc-numls-%s-%s %s"> %s </p>
                    """, numId, level,paragraph.getNumFmt().equalsIgnoreCase("decimal")?"":"tdoc-numlsIcon ", getFormattedRuns(paragraph));
        }
        else {
            // Xử lý đoạn văn thông thường
            String alignment = "left";
            if (paragraph.getAlignment() == ParagraphAlignment.CENTER) {
                alignment = "center";
            } else if (paragraph.getAlignment() == ParagraphAlignment.RIGHT) {
                alignment = "right";
            }
            paraHtml = String.format("""
                        <p class="tdoc-text-%s"> %s </p>
                    """, alignment, getFormattedRuns(paragraph));
        }

        return paraHtml;
    }
    private static String handleTable(XWPFTable table) {
        StringBuilder tableHtml = new StringBuilder();
        tableHtml.append("<table border=\"1\" style=\"border-collapse:collapse; width:100%;\">");

        for (XWPFTableRow row : table.getRows()) {
            tableHtml.append("<tr>");
            for (XWPFTableCell cell : row.getTableCells()) {
                String cellText = cell.getText();
                tableHtml.append("<td>").append(cellText).append("</td>");
            }
            tableHtml.append("</tr>");
        }

        tableHtml.append("</table>");
        return tableHtml.toString();
    }

    // Hàm xử lý các đoạn văn bản để lấy các thông tin format (được định dạng với font chữ, màu sắc, v.v)
    private static String getFormattedRuns(XWPFParagraph paragraph) {
        StringBuilder runsHtml = new StringBuilder();

        for (XWPFRun run : paragraph.getRuns()) {
            List<XWPFPicture> pictures = run.getEmbeddedPictures();
            if (!pictures.isEmpty()) {
                for (XWPFPicture picture : pictures) {
                    runsHtml.append(handleImage(picture));
                }
            } else {
                String color = run.getColor() != null ? run.getColor() : "black";
                String fontSize = run.getFontSize() > 0 ? run.getFontSize() + "pt" : "inherit";
                boolean bold = run.isBold();
                boolean italic = run.isItalic();

                runsHtml.append("<span style=\"color:").append(color)
                        .append("; font-size:").append(fontSize)
                        .append(bold ? "; font-weight:bold;" : "")
                        .append(italic ? " font-style:italic;" : "")
                        .append("\">");
                runsHtml.append(run.text());
                runsHtml.append("</span>");
            }
        }
        return runsHtml.toString();
    }


    // Hàm xử lý ảnh
    private static String handleImage(XWPFPicture picture) {
        try {
            // Đọc dữ liệu ảnh từ tài liệu
            byte[] imageBytes = picture.getPictureData().getData();
            String imgType = picture.getPictureData().getPackagePart().getContentType().split("/")[1];

            // Tạo tên tệp duy nhất để lưu hình ảnh
            String imageFileName = "image_" + System.currentTimeMillis() + "." + imgType;

            // Đường dẫn đầy đủ nơi lưu hình ảnh
            File outputDir = new File(pathSaveImg==null?"media":pathSaveImg);
            if (!outputDir.exists()) {
                outputDir.mkdirs(); // Tạo thư mục nếu chưa tồn tại
            }

            File imageFile = new File(outputDir, imageFileName);
            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                fos.write(imageBytes); // Lưu ảnh vào tệp
            }

            // Trả về URL của hình ảnh
            return String.format("""
                        <img  src="http://127.0.0.1:8080/api/media/image/%s" >
                        """,imageFileName);

        } catch (Exception e) {
            e.printStackTrace();
            return ""; // Trả về chuỗi rỗng nếu có lỗi xảy ra
        }
    }
}
