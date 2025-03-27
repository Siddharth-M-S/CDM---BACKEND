package com.admin.service.order_service;

import com.admin.entity.OrderEntity;

import com.admin.entity.OrderItemEntity;

import com.admin.repository.OrdersRepository;

import com.lowagie.text.*;

import com.lowagie.text.pdf.PdfPCell;

import com.lowagie.text.pdf.PdfPTable;

import com.lowagie.text.pdf.PdfWriter;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;



import com.lowagie.text.Document;

import java.io.IOException;

import java.util.List;

@Service

public class OrderPdfService {

    private final OrdersRepository ordersRepository;

    public OrderPdfService(OrdersRepository ordersRepository) {

        this.ordersRepository = ordersRepository;

    }

public void generateOrderPdf(Long orderId, HttpServletResponse response) throws IOException, DocumentException {

    OrderEntity order = ordersRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

    // Using try-with-resources for the Document object
    try (Document document = new Document()) {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=order_" + orderId + ".pdf");

        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Order Details", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("\nOrder ID: " + order.getId()));
        document.add(new Paragraph("Order Date: " + order.getOrderDate()));
        document.add(new Paragraph("Total Price: " + order.getTotalPrice()));
        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);

        addTableHeader(table);
        addRows(table, order.getItems());

        document.add(table);
    } catch (Exception e) {
        // Handle exceptions here
        e.printStackTrace();
        throw new IOException("Error generating PDF: " + e.getMessage(), e);
    }
}

    private void addTableHeader(PdfPTable table) {

        String[] headers = {"Car Name", "Quantity", "Unit Price", "Total Price"};

        for (String header : headers) {

            PdfPCell cell = new PdfPCell(new Phrase(header));


            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(cell);

        }

    }

    private void addRows(PdfPTable table, List<OrderItemEntity> items) {

        for (OrderItemEntity item : items) {

            PdfPCell carNameCell = new PdfPCell(new Phrase(item.getCar().getName()));
            carNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(carNameCell);

            PdfPCell quantityCell = new PdfPCell(new Phrase(String.valueOf(item.getQuantity())));
            quantityCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(quantityCell);

            PdfPCell unitPriceCell = new PdfPCell(new Phrase(item.getCar().getPrice().toString()));
            unitPriceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(unitPriceCell);

            PdfPCell totalPriceCell = new PdfPCell(new Phrase(
                    item.getCar().getPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())).toString()));
            totalPriceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(totalPriceCell);

        }

    }

}
