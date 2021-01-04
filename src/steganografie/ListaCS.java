package steganografie;

public class ListaCS {
  Nod primul, ultimul, curent;
  float[][] tab;
  EZWInv ezwInv;
  
  ListaCS(float[][] tab, EZWInv ezwInv){
    this.tab=tab;
    this.ezwInv=ezwInv;
    primul=ultimul=curent=null;
  }
  
  void adauga(int i, int j, float val){
    Nod a=new Nod(i,j,val);
    if(ultimul !=null)
      ultimul.urmator=a;
    ultimul=a;
    if(primul==null)
      primul=ultimul;
  }
  
  void reface(){
    
    for(curent=primul; curent!=null;curent=curent.urmator)
      curent.scrie(tab);
      
  }
  
  void ajusteaza(float prag, int nrLSPAnt)
      throws GataCitBiti{
    //ajustează coeficienții din listă ce au fost adăugați
    //la trecerile anterioare, cu valoarea pragului
    int i;
    for(curent=primul,i=0; i<nrLSPAnt; 
        curent=curent.urmator,i++)
      if(ezwInv.citBit()==1)
        if(curent.val>=0)
          curent.val+=prag;
        else
          curent.val-=prag; 
  }
 
}