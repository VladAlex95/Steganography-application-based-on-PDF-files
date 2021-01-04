package steganografie;

public class EZW {
  
  float[][] tab;
  int DIM;
  
  float bpp;//bitrate
  int nrTotalBiți;
  
  byte[] bufCodif;
  int nrBitCodif;//pastrează lungimea codului
  
  float[] LSP;
  int nrLSP;
  
  int media;
  //necesară pentru decodificare

  float n;//pragul
  int pragInitial;
  //necesar pentru decodificare

  
    EZW(){}
  
    void initTab(){
      tab=new float[DIM][DIM];   
    }
    
  EZW(float[][]tab, int dim, float bpp){
    this.tab=tab;
    DIM=dim;
    this.bpp=bpp;
    nrTotalBiți=(int)(DIM*DIM*bpp);
  }

  boolean arboreNul(int i, int j, float tab[][]){
    //verifică dacă i,j e radacina unui arbore nul
    if(i>=DIM/2 || j>=DIM/2)
      return true;//am trecut de ultima subbandă
    else if(Math.abs(tab[i][j]) >= n)
      return false;
    else
      return arboreNul(2*i, 2*j, tab) && 
          arboreNul(2*i, 2*j+1, tab) && 
          arboreNul(2*i+1, 2*j, tab) && 
          arboreNul(2*i+1, 2*j+1, tab);
          //verificăm cei 4 descendenți 
  }

  float retMax(float[][] tab, int DIM){
  
    float max=0,a;
    for(float[] linie:tab)
      for(float x:linie)
        if((a=Math.abs(x))>max)
          max=a;
    return (float)Math.floor(Math.log(max)/Math.log(2));
  }

  void desc(){

    Haar.descompImag(tab, DIM);
    
    bufCodif=new byte[nrTotalBiți];
    LSP = new float[DIM*DIM];
    nrBitCodif=0;
 
    nrLSP=0;
    int nrLSPAnt=0;
    //nr coeficienți adăugați la trecerile anterioare
    media=(int)tab[0][0];
    //salvez media generală
    tab[0][0]=0;//elimin media generală din tablou

    //pasul 1.
    pragInitial=(int)Math.pow(2,retMax(tab, DIM));
    n=pragInitial;
    //salvez pragul inițial
    try{
    
      do{
        codifica(0,1);
        codifica(1,0);
        codifica(1,1);

        
        for(int i=0; i<nrLSPAnt; i++)
        //informații pt. ajustarea coef. din listă
          if(LSP[i]>=n){
            puneBit(1);
            LSP[i]-=n;
          }else
            puneBit(0);
        
        nrLSPAnt=nrLSP;
        //nr de coeficienți ce vor fi trataţi la
        //următoarea parcurgere a pasului 3

        
        n/=2;
      }while(true);
      //bucla se incheie prin lansarea unei exceptii
    
    }catch(GataCitBiti g){}//s-a ajuns bpp dorit
  }


  void codifica(int k, int l)throws GataCitBiti{
    float coef=tab[k][l];
    if(arboreNul(k,l,tab)){
      puneBit(0);//codifică A arbore nul -> (0,0)
      puneBit(0);
      return;
    }
    if (Math.abs(coef) >= n) {
      tab[k][l] = 0;
  
      puneBit(1);
     
      LSP[nrLSP++] = Math.abs(coef) - n;
      
      if (coef >= 0)
        puneBit(0);
      else
        puneBit(1);
    }
    else {
      puneBit(0);
      puneBit(1);
    }
    
    if(k<DIM/2 && l<DIM/2){
     
      codifica(2*k, 2*l);
      codifica(2*k, 2*l+1);
      codifica(2*k+1, 2*l);
      codifica(2*k+1, 2*l+1);
    }
  }

  void puneBit(int bitul)throws GataCitBiti{
    bufCodif[nrBitCodif++]=(byte)bitul;
    if(nrBitCodif>=nrTotalBiți)
      throw new GataCitBiti("Gata");
  }

}
