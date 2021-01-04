package steganografie;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class TextPDF {

    String text;
    TextCodificat tc;
    PDFont pdfFont= PDType1Font.TIMES_ROMAN;//.HELVETICA;
    static float fontSize = 10;
    Random rand=new Random();
    PDDocument doc;
    float margin;
    float width;
    float startX;
    float startY;
    float leading = 1f * fontSize;
    PDPage page;
    PDRectangle mediabox;
    static final int RANDURI=65;
    static int cod=0;
    static int capacitate;
    EZW ezwY, ezwC1, ezwC2;
    static int totBitiAntet=3*32*4;//intregii sunt pe 32 biti, se pastreaza datele EZWY, EZWC1. EZWC2 (praginitial, DIM, media, totalBiti)
    byte[] antet=new byte[totBitiAntet];
    int nrBitAntet;
    
    void scrieAntetul(){
        nrBitAntet=0;
        
        scrieA(ezwY.pragInitial);
        scrieA(ezwY.DIM);
        scrieA(ezwY.nrTotalBiți);
        scrieA(ezwY.media);

        scrieA(ezwC1.pragInitial);
        scrieA(ezwC1.DIM);
        scrieA(ezwC1.nrTotalBiți);
        scrieA(ezwC1.media);
        
        scrieA(ezwC2.pragInitial);
        scrieA(ezwC2.DIM);
        scrieA(ezwC2.nrTotalBiți);
        scrieA(ezwC2.media);
        
        //for(int i:antet)
        //    System.out.println(i);
        
    }    
    
    void scrieA(int val){
        int masca; 
        for(int i=0; i<32; i++){
            masca = 0x80000000 >>> i;
            if((masca&val)==0)
                antet[nrBitAntet]=0;
            else
                antet[nrBitAntet]=1;
            nrBitAntet++;   
        }
    }
    
    static void actCod(){
        cod=(cod+1)%8;
        //System.out.println("urmeaza "+(cod+1));
    }
    
    TextPDF(String text, boolean Verifica) throws IOException{
        this.text=text;
        capacitate = 0;
        tc = new TextCodificat(); //aici se va găsi textul codificat
        char c;
        for(int i=0; i<text.length(); i++){
            c=text.charAt(i);
            if(c==' '){
                capacitate++;
            }
        }
    }

    int totalBitiCodif;
    int urmatorulBit(){
        int n = totalBitiCodif++;
        if(n < totBitiAntet)
            return antet[n];
        if(n < totBitiAntet+ezwY.nrTotalBiți)
            return ezwY.bufCodif[n-totBitiAntet];
        if(n<totBitiAntet+ezwY.nrTotalBiți+ezwC1.nrTotalBiți)
            return ezwC1.bufCodif[n-totBitiAntet-ezwY.nrTotalBiți];
        if(n<totBitiAntet+ezwY.nrTotalBiți+ezwC1.nrTotalBiți+ezwC2.nrTotalBiți)
            return ezwC2.bufCodif[n-totBitiAntet-ezwY.nrTotalBiți-ezwC1.nrTotalBiți];
        return Math.random()<0.5?0:1;//s-au terminat datele, umplu fișierul cu date aleatoare
    }
    
    int urmatorulNrSpatii(){
        return urmatorulBit()*4+urmatorulBit()*2+urmatorulBit()+1;//returnează numere între 1 și 8
    }
 
    TextPDF(String text, EZW ezwY, EZW ezwC1, EZW ezwC2) throws IOException{
        this.ezwY=ezwY;
        this.ezwC1=ezwC1;
        this.ezwC2=ezwC2;
        
        scrieAntetul();
        
        totalBitiCodif=0;
        this.text=text;
        tc = new TextCodificat(); //aici se va găsi textul codificat
        char c;
        int n;
        for(int i=0; i<text.length(); i++){
            c=text.charAt(i);
            if(c==' '){
                int nrSpatii=urmatorulNrSpatii();
                //int nrSpatii=cod+1;
                //System.out.println("\n----------------");
                //System.out.println("--- Nr spații = "+nrSpatii);
                actCod();
                int dimSpatiu=(int)fontSize/nrSpatii;
                for(int k=0, suma=0; k<nrSpatii; k++){
                    suma+=dimSpatiu;
                    if(k==nrSpatii-1){
                        dimSpatiu+=fontSize-suma;
                        //if(nrSpatii > 5)//se aplica doar pentru 111115, 1111114, 11111113
                        //    tc.adauga(' ',pdfFont, dimSpatiu);//adaug o dublura, pentru ca e o anomalie in pdfbox
                    }
                    tc.adauga(' ',pdfFont, dimSpatiu);
                    //System.out.println(" ---> adaugat "+dimSpatiu);
                }
            }
            else
                tc.adauga(c,pdfFont, fontSize);
        }

        doc = new PDDocument();
        //pdfFont = PDType0Font.load(doc, new File("c:/windows/fonts/times.ttf"));
        //pdfFont = PDTrueTypeFont.loadTTF(doc, new File("c:/windows/fonts/Georgia.ttf"));
        pdfFont = PDType0Font.load(doc, new File("c:/windows/fonts/Georgia.ttf"));
        
        page = new PDPage();
        mediabox = page.getMediaBox();
        margin = 72;
        width = mediabox.getWidth() - 2 * margin;//lățime pagină
        startX = mediabox.getLowerLeftX() + margin;
        startY = mediabox.getUpperRightY() - margin;
    }

    
    TextPDF(String text) throws IOException{
        this.text=text;
        tc = new TextCodificat(); //aici se va găsi textul codificat
        char c;
        int n;
        for(int i=0; i<text.length(); i++){
            c=text.charAt(i);
            if(c==' '){
                int nrSpatii=cod+1;
                //System.out.println("\n----------------");
                //System.out.println("--- Nr spații = "+nrSpatii);
                actCod();
                int dimSpatiu=(int)fontSize/nrSpatii;
                for(int k=0, suma=0; k<nrSpatii; k++){
                    suma+=dimSpatiu;
                    if(k==nrSpatii-1){
                        dimSpatiu+=fontSize-suma;
                        //if(nrSpatii > 5)//se aplica doar pentru 111115, 1111114, 11111113
                        //    tc.adauga(' ',pdfFont, dimSpatiu);//adaug o dublura, pentru ca e o anomalie in pdfbox
                    }
                    tc.adauga(' ',pdfFont, dimSpatiu);
                    //System.out.println(" ---> adaugat "+dimSpatiu);
                }
            }
            else
                tc.adauga(c,pdfFont, fontSize);
        }

        doc = new PDDocument();
        //pdfFont = PDType0Font.load(doc, new File("c:/windows/fonts/times.ttf"));
        //pdfFont = PDTrueTypeFont.loadTTF(doc, new File("c:/windows/fonts/Georgia.ttf"));
        pdfFont = PDType0Font.load(doc, new File("c:/windows/fonts/Georgia.ttf"));
        
        page = new PDPage();
        mediabox = page.getMediaBox();
        margin = 72;
        width = mediabox.getWidth() - 2 * margin;//lățime pagină
        startX = mediabox.getLowerLeftX() + margin;
        startY = mediabox.getUpperRightY() - margin;
    }
    
    void incheieLiniile() throws IOException{
       
        float lungimeLinie=0, lungimeCuvant;
        for(int i=0; i<tc.lungime(); i=tc.urmatorulCuvant(i)){
            lungimeCuvant=tc.lungimeCuvant(i);
            if(lungimeLinie + lungimeCuvant > width){
                tc.incheieLinia(i);
                lungimeLinie=0;
                //System.out.println("\n--------------");
                i++;
            }
            //System.out.print("i = "+i+", cuvant = ");
            //tc.afisCuvant(i);
            //System.out.println(", lungime linie până la cuvânt = "+lungimeLinie
            //    +", lungime cuvânt = "+lungimeCuvant
            //    +", lungime spații = "+tc.lungimeSpatii(i));
            lungimeLinie+=(lungimeCuvant+tc.lungimeSpatii(i));
        } 
    }
    
    void scrieFisierPDF() throws IOException{
        int randuri;
        PDPage page;
        int i = 0;
       
        
        while( i< tc.lungime()){
            randuri=0;
            page = new PDPage();
            doc.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(doc, page);
            contentStream.beginText();
            contentStream.newLineAtOffset(startX, startY);
            CarCodificat cc;

            for (; i < tc.lungime(); i++) {
                cc=tc.get(i);
                //System.out.println(cc.car+"-->"+cc.dim);
                if(cc.car=='\n'){
                    contentStream.newLineAtOffset(0, -leading);
                    randuri++;
                    if(randuri>=RANDURI)
                        break;
                }
                else{
                    contentStream.setFont(pdfFont, cc.dim);
                    contentStream.showText(cc.car + "");
                    //System.out.println(cc.car+"-->"+cc.dim);
                }
            }
            contentStream.endText();
            contentStream.close();
        }

        doc.save(new File("aranjat.pdf"));
        doc.close();
    }
    
    void afiseaza(){
        System.out.println("\ntextul este:");
        System.out.println(text);
    }
    


    
}
