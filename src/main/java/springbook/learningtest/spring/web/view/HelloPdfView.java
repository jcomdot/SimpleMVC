package springbook.learningtest.spring.web.view;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

public class HelloPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		BaseFont baseFont = null;
		try {
			baseFont = BaseFont.createFont("c:/windows/fonts/H2GTRM.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Font font = new Font(baseFont, 12);
		
		Chapter chapter = new Chapter(new Paragraph("Spring Message(스프링 메시지 ♥♣∑ウサギ大嫌い～～～っ‼)", font), 1);
		chapter.add(new Paragraph((String) model.get("message"), font));

		document.add(chapter);

	}

}
