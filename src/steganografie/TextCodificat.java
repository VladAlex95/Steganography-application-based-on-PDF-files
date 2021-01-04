package steganografie;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.pdfbox.pdmodel.font.PDFont;

public class TextCodificat {
    
    ArrayList<CarCodificat> caractere=new ArrayList();
    
    TextCodificat(){
        caractere=new ArrayList();
    }
   
    void incheieLinia(int poz){
        caractere.add(poz, new CarCodificat('\n', null, 1));
    }

    float lungimeSpatii(int poz) throws IOException{
     
        float lung=0;
        int i;
        for(i=poz; i<caractere.size(); i++){
            CarCodificat cc=caractere.get(i);
            if(cc.car==' ')
                break;//am ajuns la primul spațiu
        }
        for( ; i<caractere.size(); i++){
            CarCodificat cc=caractere.get(i);
            if(cc.car!=' ')
                break;//am trecut se spații
            lung+=cc.font.getWidth(cc.car)*cc.dim;
        }
        return lung/1000;//la sfarșitul textului va returna 0;
    }
    
    CarCodificat get(int i){
        return caractere.get(i);
    }
    
    //----------------------vechi
    void adauga(char c, PDFont font, float dim){//adaugă un caracter în text
        //fiecare caracter este carecterizat prin dimensiune și font
        //acestea pot fi folosite pentru steganografie
        caractere.add(new CarCodificat(c, font, dim));
        //System.out.println(c+"-->"+dim);
    }
    
    float lungimeCuvant(int poz) throws IOException{
        
        float lung=0;
        for(int i=poz; i<caractere.size(); i++){
            CarCodificat cc=caractere.get(i);
            if(cc.car==' ')
                return lung/1000;
            lung+=cc.font.getWidth(cc.car)*cc.dim;
        }
        return lung/1000;//la sfarșitul textului va returna 0;
    }
    
    int urmatorulCuvant(int poz){//returneaza pozitia la care incepe urmatorul cuvant
        while(poz<caractere.size()){
            CarCodificat cc=caractere.get(poz);
            if(cc.car==' ') 
                break;
            poz++;
        }
        while(poz<caractere.size()){
            CarCodificat cc=caractere.get(poz);
            if(cc.car!=' ')
                break;//am ajuns la primul caracter 
            poz++;
        }
        return poz;
    }
    
    int lungime(){
        return caractere.size();
    }
    
    boolean eSpatiu(int poz){
        return caractere.get(poz).car==' ';
    }
    
    void afisCuvant(int poz){
        while(poz < caractere.size()){
            CarCodificat cc=caractere.get(poz);
            if(cc.car!=' '){
                System.out.print(cc.car);
                poz++;
            }
            else
                return;
        }
    }
}
