package ua.com.hedgehogsoft.oculus.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.com.hedgehogsoft.oculus.model.Constructor;
import ua.com.hedgehogsoft.oculus.model.Order;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class Printer {
    private Font font;
    private Font greenFont;
    private Font redFont;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Autowired
    public Printer(@Value("${local.fonts}") String fontLocation) {
        try {
            BaseFont bf = BaseFont.createFont(fontLocation, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            font = new Font(bf);
            font.setSize(14);

            greenFont = new Font(font);
            greenFont.setColor(BaseColor.GREEN);

            redFont = new Font(font);
            redFont.setColor(BaseColor.RED);
        } catch (IOException | DocumentException e) {
            System.err.println("Can't create printer" + e);
        }
    }

    public ByteArrayOutputStream print(Map<Constructor, List<Order>> constructorsWithOrders, PrintType type) {
        try {
            Document document = new Document(PageSize.A4, 0, 0, 30, 30);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, os);
            document.open();
            setContent(document, constructorsWithOrders, type);
            document.close();
            return os;
        } catch (DocumentException e) {
            System.err.println("Can't print report" + e);
        }
        return null;
    }

    public void setContent(Document document, Map<Constructor, List<Order>> constructorsWithOrders, PrintType type) throws DocumentException {
        switch (type){
            case ARCHIVE:
                printArchive(document, constructorsWithOrders);
                break;
            case WORKBOOK:
                printWorkbook(document, constructorsWithOrders);
                break;
            case REPORT:
                printReport(document, constructorsWithOrders);
                break;
        }
    }

    public void printArchive(Document document, Map<Constructor, List<Order>> constructorsWithOrders) throws DocumentException {
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
        dateParagraph.setIndentationRight(15);
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

        for (Map.Entry<Constructor, List<Order>> entry : constructorsWithOrders.entrySet()) {
            Constructor constructor = entry.getKey();
            java.util.List<Order> orders = entry.getValue();
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
    }

    public void printWorkbook(Document document, Map<Constructor, List<Order>> constructorsWithOrders) throws DocumentException {
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
        dateParagraph.setIndentationRight(15);
        dateParagraph.setAlignment(Element.ALIGN_RIGHT);
        Chunk dateChunk = new Chunk("Дата: " + LocalDate.now().format(formatter));
        dateParagraph.add(dateChunk);

        Paragraph paragraph1 = new Paragraph();
        paragraph1.setFont(font);
        paragraph1.setSpacingAfter(3);
        paragraph1.setSpacingBefore(3);
        paragraph1.setAlignment(Element.ALIGN_CENTER);
        Chunk chunk1 = new Chunk("Рабочий план");
        paragraph1.add(chunk1);

        Paragraph paragraph2 = new Paragraph();
        paragraph2.setFont(font);
        paragraph2.setSpacingAfter(3);
        paragraph2.setSpacingBefore(3);
        paragraph2.setAlignment(Element.ALIGN_CENTER);
        Chunk chunk2 = new Chunk("работы конструкторов КБ");
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

        int rowCount = 10;
        for (Map.Entry<Constructor, List<Order>> entry : constructorsWithOrders.entrySet()) {
            Constructor constructor = entry.getKey();
            java.util.List<Order> orders = entry.getValue();
            cell = new PdfPCell(new Phrase(constructor.getName(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(rowCount);
            pdfTable.addCell(cell);
            for (Order order : orders) {
                Optional<LocalDate> planned = Optional.ofNullable(order.getPlannedDate());
                Optional<LocalDate> actual = Optional.ofNullable(order.getActualDate());

                cell = new PdfPCell(new Phrase(order.getOrderNumber(), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfTable.addCell(cell);

                cell = new PdfPCell(new Phrase(planned.isPresent() ? planned.get().format(formatter) : "", font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfTable.addCell(cell);

                if (order.isInTime()) {
                    cell = new PdfPCell(new Phrase(actual.isPresent() ? actual.get().format(formatter) : "", greenFont));
                } else {
                    cell = new PdfPCell(new Phrase(actual.isPresent() ? actual.get().format(formatter) : "", redFont));
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
            for (int i = 0; i < rowCount - orders.size(); i++) {
                for (int k = 0; k < 5; k++) {
                    cell = new PdfPCell();
                    cell.setMinimumHeight(15);
                    pdfTable.addCell(cell);
                }
            }
        }
        document.add(pdfTable);
    }

    public void printReport(Document document, Map<Constructor, List<Order>> constructorsWithOrders) throws DocumentException {
        LocalDate startDate = null;
        LocalDate endDate = null;
        for (Map.Entry<Constructor, List<Order>> entry : constructorsWithOrders.entrySet()) {
            for (Order order : entry.getValue()) {
                if (order != null) {
                    LocalDate date = order.getActualDate();
                    if (date != null && startDate == null && endDate == null) {
                        startDate = date;
                        endDate = date;
                    }
                    else if(date != null){
                        if(date.isBefore(startDate)){
                            startDate = date;
                        }
                        if(date.isAfter(endDate)){
                            endDate = date;
                        }
                    }
                }
            }
        }

        startDate = startDate.with(firstDayOfMonth());
        endDate = endDate.with(lastDayOfMonth());


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
        Chunk chunk1 = new Chunk("Отчет");
        paragraph1.add(chunk1);

        Paragraph paragraph4 = new Paragraph();
        paragraph4.setFont(font);
        paragraph4.setSpacingAfter(3);
        paragraph4.setSpacingBefore(3);
        paragraph4.setAlignment(Element.ALIGN_CENTER);
        Chunk chunk5 = new Chunk("о выполненных работах");
        paragraph4.add(chunk5);

        Paragraph paragraph2 = new Paragraph();
        paragraph2.setFont(font);
        paragraph2.setSpacingAfter(3);
        paragraph2.setSpacingBefore(3);
        paragraph2.setAlignment(Element.ALIGN_CENTER);
        Chunk chunk2 = new Chunk("по графику планирования и отслеживанию работы конструкторов КБ");
        paragraph2.add(chunk2);

        Paragraph paragraph3 = new Paragraph();
        paragraph3.setFont(font);
        paragraph3.setSpacingAfter(3);
        paragraph3.setSpacingBefore(3);
        paragraph3.setAlignment(Element.ALIGN_CENTER);
        Chunk chunk4 = new Chunk("за период с " + startDate.format(formatter) + " по " + endDate.format(formatter));
        paragraph3.add(chunk4);

        paragraph.add(paragraph1);
        paragraph.add(paragraph4);
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

        for (Map.Entry<Constructor, List<Order>> entry : constructorsWithOrders.entrySet()) {
            Constructor constructor = entry.getKey();
            java.util.List<Order> orders = entry.getValue();
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
    }
}
