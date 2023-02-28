import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author Drumstyle92
 */
public class Main {
    public static void main(String[] args) {

        // il nome del file in PDF
        String fileName = "Dino_curriculum_IT_2023.pdf";
        PDDocument document;

        try {

            // stampare il PDF in console
            document = PDDocument.load(new File(fileName));
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            System.out.println(text);

            // prendere i file immagine(tutto il curriculum)
            PDDocument document2 = PDDocument.load(new File(
                    "Dino_curriculum_IT_2023.pdf"));
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document2.getNumberOfPages(); ++page) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300);
                ImageIO.write(bim, "PNG", new File("curriculum_" + (page + 1) + ".png"));
            }
            document2.close();

            // Carica il file PNG
            File inputFile2 = new File("curriculum_1.png");

            // Crea un nuovo documento Word
            XWPFDocument document4 = new XWPFDocument();

            // Crea un nuovo paragrafo e aggiungi il testo
            XWPFParagraph paragraph = document4.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("Il mio curriculum:");

            // Aggiungi l'immagine come un'immagine inline
            int format = Document.PICTURE_TYPE_PNG;
            byte[] imageData = IOUtils.toByteArray(new FileInputStream(inputFile2));
            run = paragraph.createRun();
            run.addPicture(new ByteArrayInputStream(imageData),
                    format, "curriculum.png", Units.toEMU(400), Units.toEMU(600));

            // Salva il documento Word
            FileOutputStream out = new FileOutputStream(new File("curriculum2023.docx"));
            document4.write(out);
            out.close();
            document.close();

            /*
             Intercettiamo le eccezioni per errori di input/output se si tenta di leggere
             o scrivere un file che non esiste oppure non si ha accesso e InvalidFormatException
             se si tenta di utilizzare un formato di dati non valido.
             */
        } catch (IOException | InvalidFormatException e) {

            throw new RuntimeException(e);

        }

    }

}



