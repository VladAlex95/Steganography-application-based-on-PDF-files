package steganografie;

import static steganografie.ImagineDigitala.DIM;

public class Haar {
  static final float RAD2=(float)Math.sqrt(2);
  //pentru eficiență
  static float salvare[]=new float[DIM/2];

  static void descompImag(float tab[][], int h){
    while(h>1){
      for(int i=0; i<h; i++)
        pasDescLin(tab[i], h);
      for(int i=0; i<h; i++)
        pasDesCol(tab, h, i);
      h/=2;
    }
  }

  static void pasDescLin(float c[], int h){
    for(int i=0; i<h/2; i++){
      salvare[i]=(c[2*i]-c[2*i+1])/RAD2;//Math.sqrt(2)//salvez detaliile
      c[i]=(c[2*i]+c[2*i+1])/RAD2;
    }
    System.arraycopy(salvare, 0, c, h/2, h/2);
  }

  static void pasDesCol(float c[][], int h, int col){
    for(int i=0; i<h/2; i++){
      salvare[i]=(c[2*i][col]-c[2*i+1][col])/RAD2;
      //salvez detaliile
      c[i][col]=(c[2*i][col]+c[2*i+1][col])/RAD2;
    }
    for(int i=0; i<h/2; i++)
      c[h/2+i][col]=salvare[i];
  }

  static void descompImagS(float tab[][], int dim){
    for(int h=dim; h>1; h/=2)
     for(int i=0; i<dim; i++)
      pasDescLin(tab[i], h);
    
    for(int h=dim; h>1; h/=2)
      for(int i=0; i<dim; i++)
        pasDesCol(tab, h, i);
  }

  static void refaceImag(float tab[][], int dim){
  //reface imaginea ce a fost desompusă nestandardH
    for(int h=2; h<=dim; h*=2){
      for(int i=0; i<h; i++)
        pasRefCol(tab, h, i);
      for(int i=0; i<h; i++)
        pasRefLin(tab[i],h);
    }
  }

  static void pasRefCol(float c[][], int h, int col){
    for(int i=0; i<h/2; i++)
      salvare[i]=c[i][col];//salvez mediile
    for(int i=0; i<h/2; i++){
      c[2*i][col]=(salvare[i]+c[h/2+i][col])/RAD2;
      c[2*i+1][col]=(salvare[i]-c[h/2+i][col])/RAD2;
    }
  }
  
  static void pasRefLin(float c[], int h){
    System.arraycopy(c, 0, salvare, 0, h/2);
    //salvez mediile
    for(int i=0; i<h/2; i++){
      c[2*i]=(salvare[i]+c[h/2+i])/RAD2;
      c[2*i+1]=(salvare[i]-c[h/2+i])/RAD2;
    }
  }

  static void refaceImagS(float tab[][], int dim){
    //refacere standard imagine
//System.out.println("abcd");
    for(int h=2; h<=dim; h*=2)
      for(int i=0; i<dim; i++)
        pasRefCol(tab, h, i);

    for(int h=2; h<=dim; h*=2)
      for(int i=0; i<dim; i++)
        pasRefLin(tab[i], h);
    

  }  

//  static void cuantif(float tab[][], int DIM, int minCo){
//    for(int i=0; i<DIM; i++)
//      for(int j=0; j<DIM; j++)
//        if(Math.abs(tab[i][j])<minCo)
//          tab[i][j]=0;
//  }

}
