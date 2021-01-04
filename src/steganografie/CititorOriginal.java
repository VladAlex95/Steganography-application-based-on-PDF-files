package steganografie;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

public class CititorOriginal extends PDFTextStripper {
    
    String fisier;
    StringBuffer caractere;
    
    public CititorOriginal(String fisier) throws IOException {
        this.fisier=fisier;
        caractere=new StringBuffer();
    }

    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        String s;
        for (TextPosition text : textPositions) {
//            if(text.toString().equals(System.getProperty("line.separator"))){
//                s="\n";
//                System.out.println("---");
//            }
//else

            s=text.getUnicode();
            //if(s.isEmpty())
            //    System.out.println("---");//s="\n";          
            caractere.append(s);
            //System.out.print(s+", "+(int)s.charAt(0));
            //System.out.println(" --> " + Math.round(text.getFontSize()));
        }
    }
    
    public void citeste() throws IOException {
        //PDDocument document=null;
        try {
          document = PDDocument.load(new File(fisier));
          setSortByPosition(true);
          setStartPage(0);
          setEndPage(document.getNumberOfPages());
          Writer dumm = new OutputStreamWriter(new ByteArrayOutputStream());
          writeText(document, dumm);
        }
        finally {
          if (document != null) {
            document.close();
          }
        }
    }
    
    void test() throws IOException{
        
        //PDDocument document = PDDocument.load(new File("my2.pdf"));
        //PDFTextStripper stripper = new PDFTextStripper();
        String[] lines;
        String text = getText(document);
        lines = text.split(System.getProperty("line.separator"));
        document.close();
    }
    
}

