package ua.com.hedgehogsoft.oculus.print;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.hedgehogsoft.oculus.model.Constructor;
import ua.com.hedgehogsoft.oculus.model.Order;
import ua.com.hedgehogsoft.oculus.repository.ConstructorRepository;

import javax.swing.table.TableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Component
public class ReportPrinter extends AbstractReportPrinter {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Autowired
    private ConstructorRepository constructorRepository;

    @Override
    protected String resolveFileName(String... args) {
        return "Отчет.pdf";
    }

    @Override
    protected void setContent(Document document, String... args) throws DocumentException {
        List<Constructor> constructors = constructorRepository.findAll();
        Map<Constructor, List<Order>> constructorArchiveOrders =
                constructors.stream().sorted(comparing(Constructor::getName)).collect(toMap(Function.identity(),
                        (constructor) -> constructor.getOrders().stream().filter(Order::isArchive)
                                .sorted(comparing(Order::getPlannedDate)).collect(toList()), (o1, o2) -> o2, LinkedHashMap::new));

        Paragraph paragraph = new Paragraph();
        paragraph.setFont(font);
        paragraph.setSpacingAfter(1);
        paragraph.setSpacingBefore(1);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setIndentationLeft(1);
        paragraph.setIndentationRight(1);

        Paragraph dateParagraph = new Paragraph();
        dateParagraph.setFont(font);
        dateParagraph.setSpacingAfter(10);
        dateParagraph.setSpacingBefore(5);
        dateParagraph.setIndentationRight(10);
        dateParagraph.setAlignment(Element.ALIGN_RIGHT);
        Chunk dateChunk = new Chunk("Дата: " + LocalDate.now().format(formatter));
        dateParagraph.add(dateChunk);

        Paragraph paragraph1 = new Paragraph();
        paragraph1.setFont(font);
        paragraph1.setSpacingAfter(3);
        paragraph1.setSpacingBefore(3);
        paragraph1.setAlignment(Element.ALIGN_CENTER);
        Chunk chunk1 = new Chunk("Архив");
        paragraph1.add(chunk1);

        Paragraph paragraph2 = new Paragraph();
        paragraph2.setFont(font);
        paragraph2.setSpacingAfter(3);
        paragraph2.setSpacingBefore(3);
        paragraph2.setAlignment(Element.ALIGN_CENTER);
        Chunk chunk2 = new Chunk("графика планирования и отслеживания работы конструкторов КБ");
        paragraph2.add(chunk2);

        Paragraph paragraph3 = new Paragraph();
        paragraph3.setFont(font);
        paragraph3.setSpacingAfter(3);
        paragraph3.setSpacingBefore(3);
        paragraph3.setAlignment(Element.ALIGN_CENTER);
        Chunk chunk4 = new Chunk("за период " + LocalDate.now());
        paragraph3.add(chunk4);

        paragraph.add(dateParagraph);
        paragraph.add(paragraph1);
        paragraph.add(paragraph2);
        paragraph.add(paragraph3);

        document.add(paragraph);

        /*******************************************************************************************/

        font.setSize(12);
        greenFont.setSize(12);
        redFont.setSize(12);

        PdfPTable pdfTable = new PdfPTable(6);
        pdfTable.setWidths(new int[]{6, 4, 4, 4, 4, 6});
        pdfTable.setWidthPercentage(90);

        PdfPCell cell;
        cell = new PdfPCell(new Phrase("Фамилия", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfTable.addCell(cell);

        cell = new PdfPCell(new Phrase("№ заказа", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Плановая дата исполнения", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Фактическая дата исполнения", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Шифр", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Изделие", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfTable.addCell(cell);

        for (Map.Entry<Constructor, List<Order>> entry : constructorArchiveOrders.entrySet()) {
            Constructor constructor = entry.getKey();
            List<Order> orders = entry.getValue();
            cell = new PdfPCell(new Phrase(constructor.getName(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            if (!orders.isEmpty()) {
                cell.setRowspan(orders.size());
            }
            pdfTable.addCell(cell);
            if (orders.isEmpty()) {
                for (int i = 0; i < 5; i++) {
                    cell = new PdfPCell();
                    pdfTable.addCell(cell);
                }
            }
            for (Order order : orders) {
                cell = new PdfPCell(new Phrase(order.getOrderNumber(), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfTable.addCell(cell);

                cell = new PdfPCell(new Phrase(order.getPlannedDate().format(formatter), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfTable.addCell(cell);

                if (order.isInTime()) {
                    cell = new PdfPCell(new Phrase(order.getActualDate().format(formatter), greenFont));
                } else {
                    cell = new PdfPCell(new Phrase(order.getActualDate().format(formatter), redFont));
                }
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfTable.addCell(cell);

                cell = new PdfPCell(new Phrase(order.getCipher(), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfTable.addCell(cell);

                cell = new PdfPCell(new Phrase(order.getProductName(), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfTable.addCell(cell);
            }
        }

        document.add(pdfTable);
        document.close();
    }
}
