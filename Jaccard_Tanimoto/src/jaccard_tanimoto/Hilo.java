/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaccard_tanimoto;
import static jaccard_tanimoto.Quimico.mat;

/**
 *
 * @author andres
 */
public class Hilo implements Runnable {
    String sentencia;

    public Hilo(String sentencia) {
        this.sentencia = sentencia;
    }

    public Hilo() {
    }
    
    
    
     private synchronized void leer_info(String sentencia) 
    {
//        esperarXsegundos();
        String token [] = sentencia.split("\t");
        token[3] = limpiar_formula(token[3]);
        mat.put(token[1].trim(), token[3].trim());
    }
        
    private String limpiar_formula(String formula)
    {
        formula=formula.replaceAll("[+|\\-|=|\\[|\\]|//(|//)|\\\\|[0-9]]","");
        return formula;
    }

    @Override
    public void run() {
        leer_info(sentencia);
    }
    
    private void esperarXsegundos()
    {
        try
        {
            Thread.sleep(3*1000);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
}


