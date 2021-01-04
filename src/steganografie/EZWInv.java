package steganografie;

import java.util.Arrays;

public class EZWInv {

  float prag;//pragul
  int nrBitCit;

  int nrLSP;
  ListaCS listaCS;

  //EZW ezw;
  float[][] tab;
  int DIM;
  int nrTotalBiti;
  int media;
//  pragInitialY=citesteA();
//            DIMY=citesteA();
//            nrTotalBitiY=citesteA();
//            mediaY=citesteA();
  EZWInv(float[][]tab, int pragInitial, int DIM, int nrTotalBiti, int media){
    //this.ezw=ezw;
    this.tab=tab;
    prag=pragInitial;
    this.DIM=DIM;
    this.nrTotalBiti=nrTotalBiti;
    this.media=media;
    //tab=new float[DIM][DIM];
  }

  void ref(){
   ;
      
      
    nrLSP=0;
    nrBitCit=0;
    listaCS = new ListaCS(tab, this);
    int nrLSPAnt=0;

    try{
    
      do{
        decodifica(0,1);
        decodifica(1,0);
        decodifica(1,1);
     
       
        listaCS.ajusteaza(prag,nrLSPAnt);
                
        nrLSPAnt=nrLSP;
        

        
        prag/=2;
      }while(true);
  
    }catch(GataCitBiti g){
      
      for (float[] linie : tab)
        Arrays.fill(linie, 0);
        
      
      
      tab[0][0]=media;//preiau media generala
      listaCS.reface();

      //System.out.println("DIM="+DIM);
      Haar.refaceImag(tab, DIM);
    }
  }

  void decodifica(int k, int l)throws GataCitBiti{
    int b1,b2;
    b1=citBit();
    b2=citBit();

    if(b1==0 && b2==0)//am citit A
      return;

    if(b1==1 && b2==0){//am citit P
      listaCS.adauga(k, l, prag);//pun valoarea pragului în listă
      nrLSP++;
    }

    if(b1==1 && b2==1){//am citit N
      listaCS.adauga(k, l, -prag);
      nrLSP++;
    }

    if(k<DIM/2 && l<DIM/2){
      decodifica(2*k,   2*l);
      decodifica(2*k,   2*l+1);
      decodifica(2*k+1, 2*l);
      decodifica(2*k+1, 2*l+1);
    }
  }

  int citBit()throws GataCitBiti{
    if(nrBitCit>=nrTotalBiti){
      throw new GataCitBiti("Gata");
    }
    //return ezw.bufCodif[nrBitCit++];
    //int k1=ezw.bufCodif[nrBitCit++];
    //int k2=Cititor.citBit();
    //System.out.println("k1="+k1+", k2="+k2);
    nrBitCit++;
    return Cititor.citBit();
    //return k2;
  }

}
