package steganografie;

public class Nod {

  float val;//pt. transformarea inversă
  int i;//poziția coeficientului în tablou
  int j;
  
  Nod urmator;
    
  Nod(int i, int j, float val){
    this.i=i;
    this.j=j;
    this.val=val;
  }

  void scrie(float[][]tab){
    tab[i][j]=val;
  }
  
}


    

