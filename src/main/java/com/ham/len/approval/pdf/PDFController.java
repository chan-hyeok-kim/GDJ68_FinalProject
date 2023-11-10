package com.ham.len.approval.pdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;

import com.ham.len.approval.ApprovalVO;
import com.ham.len.commons.FileDownView;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.font.FontProvider;

import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;

import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import com.nimbusds.jose.util.Resource;

import lombok.RequiredArgsConstructor;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pdf/*")
public class PDFController {
	
    @Autowired
    private final HtmlToPdf htmlToPdf;
  
    private final HtmlToPdf2 htmlToPdf2;
    
    @Autowired
    private FileDownView fileDownView;
    
    @RequestMapping("download")
    public void pdfDownload(HttpServletResponse response,ApprovalVO approvalVO) {
          //approvalVO.getApprovalContents();
    	String html=htmlToPdf.getHtml("");
        // 미리 준비한 DTO 선언
        PdfVO pdfVO = new PdfVO();
        // pdf 파일이 저장될 경로 ( Mac 기준 )
        //pdfVO.setPdfFilePath("/Users/jeon/Documents/JavaProject/Blog/pdf/");

        // pdf 파일이 저장될 경로 ( Windows 기준 )
         pdfVO.setPdfFilePath("D:\\note\\pdf");

        // pdf 파일명 ( 테스트를 위해 랜덤으로 생성 )
        pdfVO.setPdfFileName(new Random().nextInt() + ".pdf");
        // itextPdfDto.setPdfFileName("test.pdf");

        // getHtml 에서 호출될 코드명
        pdfVO.setPdfCode("hyeok");

        // ======================= PDF 존재 유무 체크 =======================
        // 없다면 PDF 파일 만들기
        File file = htmlToPdf.checkPDF(pdfVO,html);
        int fileSize = (int) file.length();
        // ===============================================================


        // ===============================================================
        // 파일 다운로드를 위한 header 설정
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename="+pdfVO.getPdfFileName()+";");
        response.setContentLengthLong(fileSize);
        response.setStatus(HttpServletResponse.SC_OK);
        // ===============================================================

        // 파일 다운로드
        BufferedInputStream in = null;
        BufferedOutputStream out = null;

        // PDF 파일을 버퍼에 담은 후 다운로드
        try{
            in = new BufferedInputStream(new FileInputStream(file));
            out = new BufferedOutputStream(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            byte[] buffer = new byte[4096];
            int read = 0;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                Objects.requireNonNull(out).flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @RequestMapping("down2")
    public void pdf(HttpServletResponse response,ApprovalVO approvalVO) throws Exception{
      htmlToPdf2.down(response, approvalVO);
    	
    }
    
    @RequestMapping("down")
    @ResponseBody
    public String getPdf(ApprovalVO approvalVO,Model model)throws Exception{
    	    
    	    String fileNm= UUID.randomUUID().toString()+'_'+"sample.pdf";
    	    String path="D:\\"+fileNm;
    	    model.addAttribute("path", path);
    	  //  String path="/static/pdf/"+fileNm;
    	    File file = new File(path); 
    	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	    //한국어를 표시하기 위해 폰트 적용 
    		String BODY=approvalVO.getApprovalContents();    		
    		String dest="D:\\sample1235.PDF";
//    	    String FONT = "D:\\malgun.ttf";
    	    String FONT="static/fonts/malgun.ttf";
    	    //ConverterProperties : htmlconverter의 property를 지정하는 메소드인듯
    	    ConverterProperties properties = new ConverterProperties();
    	    FontProvider fontProvider = new DefaultFontProvider(false, false, false);
    	    FontProgram fontProgram = FontProgramFactory.createFont(FONT);
    	    fontProvider.addFont(fontProgram);
    	    properties.setFontProvider(fontProvider);

    	    
    	    
    	    //pdf 페이지 크기를 조정
    	    List<IElement> elements = HtmlConverter.convertToElements(BODY, properties);
    
    	 //   PdfWriter writer= new PdfWriter(baos);
    	    PdfWriter writer= new PdfWriter(new FileOutputStream(file));
    	    PdfDocument pdf = new PdfDocument(writer);
    	    Document document = new Document(pdf);
    	    //setMargins 매개변수순서 : 상, 우, 하, 좌
    	    document.setMargins(50, 0, 50, 0);
    	    for (IElement element : elements) {
    	      document.add((IBlockElement) element);
    	    }
    	    document.close();
    	    writer.close();
    	   
    	   return path;
    	
    	    
    }
    
    @RequestMapping("down1")
    @ResponseBody
    public String getPdf(ApprovalVO approvalVO,HttpServletResponse response)throws Exception{
    	    
    	    String fileNm= UUID.randomUUID().toString()+'_'+"sample.pdf";
    	  //  String path="D:\\"+fileNm;
    	    
    	    String path="D:\\"+fileNm;
    	    File file = new File(path); 
    	    //한국어를 표시하기 위해 폰트 적용 
    		String BODY=approvalVO.getApprovalContents();    		
    		String dest="D:\\sample1235.PDF";
//    	    String FONT = "D:\\malgun.ttf";
    	    String FONT="static/fonts/malgun.ttf";
    	    //ConverterProperties : htmlconverter의 property를 지정하는 메소드인듯
    	    ConverterProperties properties = new ConverterProperties();
    	    FontProvider fontProvider = new DefaultFontProvider(false, false, false);
    	    FontProgram fontProgram = FontProgramFactory.createFont(FONT);
    	    fontProvider.addFont(fontProgram);
    	    properties.setFontProvider(fontProvider);

    	    // 파일 다운로드 설정
    		response.setContentType("application/pdf");
    		String fileName = URLEncoder.encode("한글파일명", "UTF-8"); // 파일명이 한글일 땐 인코딩 필요
    		response.setHeader("Content-Transper-Encoding", "binary");
    		response.setHeader("Content-Disposition", "inline; filename=" + fileNm + ".pdf");
    	    
FileInputStream fi = new FileInputStream(file);
    		
    		//클라이언트로 전송
    		OutputStream os = response.getOutputStream();
    		
    
 
    	    
    	    //pdf 페이지 크기를 조정
    	    List<IElement> elements = HtmlConverter.convertToElements(BODY, properties);
    
    	    PdfWriter writer= new PdfWriter(response.getOutputStream());
    	    
    	
    		
    	 //   PdfWriter writer= new PdfWriter(new FileOutputStream(file));
    	    PdfDocument pdf = new PdfDocument(writer);
    	    Document document = new Document(pdf);
    	    //setMargins 매개변수순서 : 상, 우, 하, 좌
    	    document.setMargins(50, 0, 50, 0);
    	    for (IElement element : elements) {
    	      document.add((IBlockElement) element);
    	    }
    	    document.close();
    	    writer.close();

    		
    		FileCopyUtils.copy(fi, os);
//    		fi에서 읽어들인걸 os로 전송하자
    		
//    		자원 해제
    		os.close();
    		fi.close();
    	   
    	    return path;
    	    
    }
    
    @RequestMapping("down3")
    public void getPdf1(ApprovalVO approvalVO, HttpServletResponse response) throws Exception {
        String fileNm = UUID.randomUUID().toString() + "_" + "sample.pdf";
        String path = "D:\\" + fileNm; // 이 경로가 허용되는지 확인 필요

        String fontPath = "static/fonts/malgun.ttf"; // 폰트 파일 경로
        String body = approvalVO.getApprovalContents();

        ConverterProperties properties = new ConverterProperties();
        FontProvider fontProvider = new DefaultFontProvider(false, false, false);
        FontProgram fontProgram = FontProgramFactory.createFont(fontPath);
        fontProvider.addFont(fontProgram);
        properties.setFontProvider(fontProvider);

        List<IElement> elements = HtmlConverter.convertToElements(body, properties);

        File file = new File(path);
        PdfWriter writer = new PdfWriter(new FileOutputStream(file));
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.setMargins(50, 0, 50, 0);
        for (IElement element : elements) {
            document.add((IBlockElement) element);
        }
        document.close();
        writer.close();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=sample.pdf");

        FileInputStream inputStream = new FileInputStream(file);
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
        inputStream.close();
    }
}