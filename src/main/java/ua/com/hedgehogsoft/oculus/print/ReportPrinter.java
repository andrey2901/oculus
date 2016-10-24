package ua.com.hedgehogsoft.oculus.print;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.springframework.stereotype.Component;

import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReportPrinter  extends AbstractReportPrinter{
    @Override
    protected String resolveFileName(String... args)
    {
        return "Отчет.pdf";
    }

    @Override
    protected void setContent(Document document, String... args) throws DocumentException
    {
        String dateFrom = null;
        String dateTo = null;

        Paragraph paragraph = new Paragraph();
        paragraph.setFont(font);
        paragraph.setSpacingAfter(1);
        paragraph.setSpacingBefore(1);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setIndentationLeft(1);
        paragraph.setIndentationRight(1);

        Paragraph paragraph1 = new Paragraph();
        paragraph1.setFont(font);
        paragraph1.setSpacingAfter(3);
        paragraph1.setSpacingBefore(3);
        paragraph1.setAlignment(Element.ALIGN_CENTER);
        Chunk chunk1 = new Chunk("Надходження");
        paragraph1.add(chunk1);

        Paragraph paragraph2 = new Paragraph();
        paragraph2.setFont(font);
        paragraph2.setSpacingAfter(3);
        paragraph2.setSpacingBefore(3);
        paragraph2.setAlignment(Element.ALIGN_CENTER);
        Chunk chunk2 = new Chunk("поживних середовищ і хімреактивів, лабораторного скла\n"
                + "до Централізованої баклабораторії Лівобережжя\nКЗ \"Дніпропетровьска міська клінічна лікарня №9 \" ДОР\"");
        paragraph2.add(chunk2);

        Paragraph paragraph3 = new Paragraph();
        paragraph3.setFont(font);
        paragraph3.setSpacingAfter(3);
        paragraph3.setSpacingBefore(3);
        paragraph3.setAlignment(Element.ALIGN_CENTER);
        Chunk chunk4 = new Chunk("з " + dateFrom + " до " + dateTo);
        paragraph3.add(chunk4);

        paragraph.add(paragraph1);
        paragraph.add(paragraph2);
        paragraph.add(paragraph3);

        document.add(paragraph);

        /*******************************************************************************************/

        /*font.setSize(10);
        TableModel model = table.getModel();
        PdfPTable pdfTable = new PdfPTable(model.getColumnCount() - 2);
        pdfTable.setWidths(new int[] {2, 21, 4, 6, 4, 4, 4});
        pdfTable.setWidthPercentage(90);

        PdfPCell cell = null;
        cell = new PdfPCell(new Phrase("№ з/п", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Найменування предметів закупівель", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Одиниця виміру", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Дата надходження", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Ціна, грн./од.", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Кількість, од.", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Сума, грн.", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfTable.addCell(cell);

        Map<String, List<PdfPCell>> groupedCells = new HashMap<>();

        double common = 0d;

        for (int row = 0; row < model.getRowCount(); row++)
        {
            String group = (String) model.getValueAt(row, 8);
            if (!groupedCells.containsKey(group))
            {
                cell = new PdfPCell(new Phrase(group, font));
                cell.setColspan(7);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                groupedCells.put(group, new ArrayList<>());
                groupedCells.get(group).add(cell);
            }
            List<PdfPCell> cells = groupedCells.get(group);
            for (int column = 0; column < model.getColumnCount(); column++)
            {
                switch (column)
                {
                    case 0:
                        cell = null;
                        cells.add(cell);
                        break;
                    case 1:
                    case 2:
                        cell = new PdfPCell(new Phrase((String) model.getValueAt(row, column), font));
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cells.add(cell);
                        break;
                    case 3:
                        cell = new PdfPCell(new Phrase((String) model.getValueAt(row, column), font));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cells.add(cell);
                        break;
                    case 4:
                    case 5:
                        cell = new PdfPCell(
                                new Phrase((String) Double.toString((double) model.getValueAt(row, column)), font));
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cells.add(cell);
                        break;
                    case 6:
                        cell = new PdfPCell(
                                new Phrase((String) Double.toString((double) model.getValueAt(row, column)), font));
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cells.add(cell);
                        common += (double) model.getValueAt(row, column);
                        break;
                }
            }
        }

        int counter = 0;
        counter = orderedPrint(pdfTable, groupedCells, SourceType.BUDGET, counter);
        counter = orderedPrint(pdfTable, groupedCells, SourceType.MECENAT, counter);
        counter = orderedPrint(pdfTable, groupedCells, SourceType.DEZINFECTOR, counter);
        counter = orderedPrint(pdfTable, groupedCells, SourceType.PROVISOR, counter);

        cell = new PdfPCell(new Phrase("Всього", font));
        cell.setColspan(6);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfTable.addCell(cell);

        cell = new PdfPCell(new Phrase((String) Double.toString(common), font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfTable.addCell(cell);

        document.add(pdfTable);

        /*******************************************************************************************/

        document.add(Chunk.NEWLINE);

        Paragraph subscribeParagraph = new Paragraph();
        subscribeParagraph.setFont(font);

        Paragraph responsibleParagraph = new Paragraph();
        responsibleParagraph.setFont(font);
        responsibleParagraph.setSpacingAfter(0);
        responsibleParagraph.setSpacingBefore(5);
        responsibleParagraph.setAlignment(Element.ALIGN_RIGHT);
        Chunk responsibleChunk = new Chunk(
                "Матеріально відповідальна особа                                                                                         ");
        responsibleParagraph.add(responsibleChunk);

        Paragraph laboratoryParagraph = new Paragraph();
        laboratoryParagraph.setFont(font);
        laboratoryParagraph.setSpacingAfter(3);
        laboratoryParagraph.setSpacingBefore(0);
        laboratoryParagraph.setAlignment(Element.ALIGN_RIGHT);
        Chunk labVacancyChunk = new Chunk("Лаборант з бактеріології      ");
        Chunk labUnderlineChunk = new Chunk("                                       ");
        labUnderlineChunk.setUnderline(0.1f, -0.5f);
        Chunk labNameChunk = new Chunk("          Н.В. Нагорна            ");
        laboratoryParagraph.add(labVacancyChunk);
        laboratoryParagraph.add(labUnderlineChunk);
        laboratoryParagraph.add(labNameChunk);

        Paragraph deputyParagraph = new Paragraph();
        deputyParagraph.setFont(font);
        deputyParagraph.setSpacingAfter(5);
        deputyParagraph.setSpacingBefore(5);
        deputyParagraph.setAlignment(Element.ALIGN_RIGHT);
        Chunk deputyVacancyChunk = new Chunk("Зав.ЦБакЛЛ      ");
        Chunk deputyUnderlineChunk = new Chunk("                                       ");
        deputyUnderlineChunk.setUnderline(0.1f, -0.5f);
        Chunk deputyNameChunk = new Chunk("    Л.М. Москаленко            ");
        deputyParagraph.add(deputyVacancyChunk);
        deputyParagraph.add(deputyUnderlineChunk);
        deputyParagraph.add(deputyNameChunk);

        subscribeParagraph.add(deputyParagraph);
        subscribeParagraph.add(responsibleParagraph);
        subscribeParagraph.add(laboratoryParagraph);

        document.add(subscribeParagraph);
    }
}
