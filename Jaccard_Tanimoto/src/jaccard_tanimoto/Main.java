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
    public static void main(String[] args) {
        // TODO code application logic here
        Quimico q = new Quimico();
    
        q.leer_info("//src//jaccard_tanimoto//chemicals.tsv");

        //q.ObtenerFormula();
        q.ObtenerFormulaporArray();
        
        q.ManejoCaracteres();
    }
    
}
