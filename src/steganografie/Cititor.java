package steganografie;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

public class Cititor extends PDFTextStripper {
    
    String fisier;
    static ArrayList<Integer> codImagine=new ArrayList();
    
    static int urmBit, urmSet;
    
    static int citBit() throws GataCitBiti{
        if(urmBit==2){
            if(codImagine.size()==0)
                throw new GataCitBiti("Gata");
            urmSet=codImagine.remove(0);
            urmSet--;//in fisier sunt scrise valori [1, 8] le transform in [1, 7]
        }
        
        int masca=1<<urmBit;

        urmBit--;
        if(urmBit < 0)
            urmBit = 2;
        
        if((masca & urmSet) == 0)
            return 0;
        else
            return 1;
    }
    
    public Cititor(String fisier) throws IOException {
        this.fisier=fisier;
    }

    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        char c;
        boolean gata=false;
        boolean font1=false;
        int nrSp=0;
        for (TextPosition text : textPositions) {
            //System.out.print(text.getUnicode());
            c=text.getUnicode().charAt(0);
            //System.out.println(c+"");
            if(c==' '){
                //System.out.println(" --> " + Math.round(text.getFontSize()));
                nrSp++;
                gata=false;
                if(nrSp==1 && Math.round(text.getFontSize())==1)
                    font1=true;
            }
            else{
                if(gata==false){
                    gata=true;
                    if(nrSp!=0){
                        if(font1)
                            nrSp++;
                        //System.out.println("nrSp="+nrSp);
                        codImagine.add(nrSp);
                    }
                    nrSp=0;
                    font1=false;
                }
            }
            //System.out.println(" --> " + text.getFont().getName());
        }
    }
    
    public void citeste() throws IOException {
        //PDDocument document=null;
        codImagine=new ArrayList();
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
        urmBit=2;
        //System.out.println("nr elemente: "+codImagine.size());
        //urmSet=codImagine.remove(0);
        //for(int i=0; i<20; i++)
        //    System.out.println(codImagine.get(i)+"");
        //System.out.println("----------citBit----------");
        //for(int i=0; i<60; i++)
        //    System.out.println(citBit()+"");
        
    }
}

