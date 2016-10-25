package ua.com.hedgehogsoft.oculus.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class AbstractReportPrinter
{
    protected Font font;
    protected Font greenFont;
    protected Font redFont;
    protected String fileName;

    @Value("${local.fonts}")
    private String fontLocation;

    public File print(String... args)
    {
        return print(false, args);
    }

    public File print(boolean isWeb, String... args)
    {
        fileName = resolveFileName(args);
        File file;
        if (isWeb)
        {
            file = new File(fileName);
        }
        else
        {
            file = new File(System.getProperty("report.folder") + System.getProperty("file.separator") + fileName);
        }
        try
        {
            BaseFont bf = BaseFont.createFont(fontLocation, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            font = new Font(bf);
            font.setSize(14);

            greenFont = new Font(font);
            greenFont.setColor(BaseColor.GREEN);

            redFont = new Font(font);
            redFont.setColor(BaseColor.RED);

            Document document = new Document(PageSize.A4, 0, 0, 30, 30);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            setContent(document, args);
            document.close();
        }
        catch (IOException | DocumentException e)
        {
            System.err.println("Can't print file [" + fileName + "]" + e);
        }
        return file;
    }

    protected abstract String resolveFileName(String... args);

    protected abstract void setContent(Document document, String... args) throws DocumentException;
}