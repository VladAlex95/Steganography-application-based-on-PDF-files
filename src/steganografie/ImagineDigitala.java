package steganografie;

import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImagineDigitala {
  
  static final int DIM=512;
  
  float y[][]=new float[DIM][DIM];//imaginea originală
  float c1[][]=new float[DIM/2][DIM/2];
  float c2[][]=new float[DIM/2][DIM/2];

 

  int pixeliImagine[]=new int[DIM*DIM];
  //imaginea originala în RGB
    
  
  PixelGrabber grabber;

  ColorModel CM=ColorModel.getRGBdefault();

  JFrame interfata;
  
  ImagineDigitala(JFrame interfata) {
    this.interfata=interfata;
  }
  
  ImagineDigitala(String fisierPoza, JFrame interfata) 
    throws InterruptedException{
    this.interfata=interfata;
    ImageIcon ii=new ImageIcon(fisierPoza);
    grabber=new PixelGrabber(ii.getImage().getSource(), 
      0,0,DIM,DIM, pixeliImagine, 0, DIM);
    //argumente:
    //ImageProducer
    //0,0=coordonatele coltului din stanga sus
    //DIM, DIM=latime, inaltime
    //pixeliimagine=tabloul în care se pun datele imaginii
    //0 = offsetul la care se plaseaza primul pixel
    //lungimea unei linii (tabloul nu e bidimensional)
    grabber.grabPixels();
    //plasează pixelii imaginii în tabloul pixeliimagine
   // System.arraycopy(pixeliImagine, 0, pixeliImagineM, 0,
   //   DIM*DIM);//salvez in pixeliImagineM va fi util pt 
    //afisarea histogramelor RGB pt imaginea originala
    separaCulori(pixeliImagine);
    //realizeaza conversia din RGB in YC1C2
  }
  
  void separaCulori(int[] tabPixeli){
  
    int r,g,b;
    for(int i=0; i<DIM; i++){
      for(int j=0; j<DIM; j++){
        r=CM.getRed(tabPixeli[i*DIM+j]);
        g=CM.getGreen(tabPixeli[i*DIM+j]);
        b=CM.getBlue(tabPixeli[i*DIM+j]);
        //r,g,b reprezintă componentele cromatice ale
        //pixelului curent
        y[i][j]=0.299f*r +0.587f*g +0.114f*b;
        //calculăm luminozitatea pixelului curent (y)
        //componentele de crominanță sunt păstrate la
        //rezoluție înjumătățită
        //fiecare element al tablourilor c1 și c2 conține
        //media pentru 4 pixeli învecinți
        if(i%2==0 && j%2==0){
          c1[i/2][j/2]=0;
          c2[i/2][j/2]=0;
        }
        //calculează media grupului de 4 pixeli învecinați
        c1[i/2][j/2]+=(0.5f*r   -0.2f*g    -0.3f*b)/4;
        c2[i/2][j/2]+=(0.3f*r   +0.4f*g    -0.7f*b)/4;
        // matricea de transformare:
        //   0.299    +0.587  +0.114
        //   0.5      -0.2    -0.3 suma liniei=0
        //   0.3      +0.4    -0.7 suma liniei=0
      }
    }
  }
  void compuneCulori(boolean albNegru){
    int r,g,b;
    float yy,cc1,cc2;
    for(int i=0; i<DIM; i++)
      for(int j=0; j<DIM; j++){
        yy=y[i][j];
        if(albNegru)
          cc1=cc2=0;
        else{
          cc1=c1[i/2][j/2];
          cc2=c2[i/2][j/2]; 
        }
        r=corectCapete(Math.round(yy+1.756f*cc1-0.590f*cc2));
        g=corectCapete(Math.round(yy-0.937f*cc1+0.564f*cc2));
        b=corectCapete(Math.round(yy+0.217f*cc1-1.359f*cc2));
        //inversa matricei de transformare
        //  1    +1.756  -0.590
        //  1    -0.937  +0.564
        //  1    +0.217  -1.359


        //fiecare pixel e reprezentat pe 24 de biti
        //primii 8 reprezinta opacitatea
        //urmatorii 8 reprezinta componenta r
        //urmatorii 8 g
        //ultimii 8 b
        pixeliImagine[i*DIM+j]=0xff000000;//opacitatea
        pixeliImagine[i*DIM+j]|=r<<16;//componenta r
        pixeliImagine[i*DIM+j]|=g<<8;//componenta g
        pixeliImagine[i*DIM+j]|=b;//componenta b
        //mai scurt: pixeliImagine[i*DIM+j]=r<<16 | g<<8
        //| b | 0xff000000;
      }
  }
  
  int corectCapete(int v){
 
  
 
    if(v<0)
      return 0;
    if(v>255)
      return 255;
    return v;
  }
  
  void afiseaza(int[]pozaRGB, JLabel eticheta){
    Image imagine = interfata.createImage
    (new MemoryImageSource(DIM,DIM,pozaRGB,0,DIM));
    //argumente: latime, inaltime, pixeli, offset,
    //lungimea unui rand de pixeli
    eticheta.setIcon(new ImageIcon(imagine));  
        //histograma();
        //histogramaRGB();
  } 
}
