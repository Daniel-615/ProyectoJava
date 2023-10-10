package com.mycompany.proyecto.backend;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.IOException;

public class Factura {
    public static void generarFactura() {
        try {
            // Crear un nuevo documento PDF
            PDDocument document = new PDDocument();

            // Agregar una página al documento
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // Crear un objeto PDPageContentStream para escribir en la página
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Configurar la fuente y el tamaño de texto
            PDType0Font font = PDType0Font.load(document, new File("src\\main\\java\\com\\mycompany\\proyecto\\backend\\fonts\\AROneSans-Bold.ttf")); // Reemplaza con la ruta correcta de tu fuente
            int fontSize = 12; // Tamaño de fuente
            contentStream.setFont(font, fontSize);

            // Definir los datos de la factura
            String[] factura = {
                    "Factura #: 123456",
                    "Fecha: 2023-10-06",
                    "",
                    "Descripción             Cantidad  Precio Unitario  Total",
                    "Producto 1                 2         $10             $20",
                    "Producto 2                 1         $15             $15",
                    "Producto 3                 3         $8              $24",
                    "",
                    "Total: $59"
            };

            // Escribir los datos de la factura en el PDF
            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;
            int rows = 0;
            float tableHeight = 20f;
            float rowHeight = 15f;

            for (String line : factura) {
                yPosition -= rowHeight;
                if (yPosition < margin) {
                    // Cambiar de página si es necesario
                    contentStream.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yStart = page.getMediaBox().getHeight() - margin;
                    yPosition = yStart;
                }
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(line);
                contentStream.endText();
                rows++;
            }

            // Cerrar el objeto PDPageContentStream
            contentStream.close();

            // Guardar el documento en un archivo PDF
             // Ruta completa de descarga (carpeta "Descargas" del usuario)
            String rutaDescarga = System.getProperty("user.home") + "/Downloads/" + "factura.pdf";
            document.save(new File(rutaDescarga));

            // Cerrar el documento
            document.close();

            System.out.println("La factura se ha creado correctamente en el archivo factura.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
