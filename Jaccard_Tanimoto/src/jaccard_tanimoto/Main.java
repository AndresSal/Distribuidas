/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaccard_tanimoto;


/**
 *
 * @author andres
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        long t1, t2;
         
        t1=System.currentTimeMillis();
        Quimico q = new Quimico();
    
        q.leer_info("//src//jaccard_tanimoto//ZINC_chemicals_X_2mil.tsv");

      // q.imprimir();
       // QuimicoThread qt= new QuimicoThread();
      // a.put(1, "Hola");
      // Entry<Integer,String> m=(Entry<Integer,String>) a.entrySet();
       //System.out.println(a.values());
        
        
       

        t2=System.currentTimeMillis();
        System.out.println("La tarea se demoró: "+(t2-t1)/1000+" segundos");
    }
    
}
