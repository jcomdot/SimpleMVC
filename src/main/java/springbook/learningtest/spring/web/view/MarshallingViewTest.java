package springbook.learningtest.spring.web.view;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.xml.MarshallingView;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

public class MarshallingViewTest extends AbstractDispatcherServletTest {

	@Test
	public void marshallingView() throws ServletException, IOException {
		setRelativeLocations("marshallingview.xml");
		initRequest("/hello").addParameter("name", "Spring").runService();
		assertThat(
				getContentAsString().indexOf(
						"<info><message>Hello Spring</message></info>") >= 0,
				is(true));
	}

	@Test
	public void pdfView() throws ServletException, IOException {
		setRelativeLocations("marshallingview.xml");
		initRequest("/helloPdf").addParameter("name", "Spring").runService();

		Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("c:/tmp/tmp.pdf"), "ISO-8859-1"));
		try {
			out.write(getContentAsString());
		} finally {
			out.close();
		}
	}

	@Test
	public void pdfTest() {
		BaseFont baseFont = null;
		try {
			baseFont = BaseFont.createFont("c:/windows/fonts/H2MJSM.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Font font = new Font(baseFont, 9);
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream("c:\\tmp\\tmp3.pdf"));
			document.open();
			document.add(new Paragraph("Hello2 한국World.", font));
			document.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/hello")
	public static class HelloController implements Controller {
		@Resource
		MarshallingView helloMarshallingView;

		@Override
		public ModelAndView handleRequest(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("info", new Info("Hello " + request.getParameter("name")));

			return new ModelAndView(helloMarshallingView, model);
		}
	}

	@RequestMapping("/helloPdf")
	public static class HelloPdfController implements Controller {
		@Autowired
		HelloPdfView helloPdfView;

		@Override
		public ModelAndView handleRequest(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("message", "Hello " + request.getParameter("name"));

			return new ModelAndView(helloPdfView, model);
		}
	}

}
