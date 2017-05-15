/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaccard_tanimoto;

import java.util.ArrayList;
import static jaccard_tanimoto.Quimico.mat;

/**
 *
 * @author andres
 */
public class QuimicoThread extends Thread implements Runnable
{
    
    private ArrayList<String>leer_info;
    private int indice;

    public QuimicoThread(ArrayList<String> leer_info, int indice)
    {
        this.leer_info = leer_info;
        this.indice = indice;
    }
    
    public void run()
    {
        getInfo(leer_info);
    }
    
    private synchronized void getInfo(ArrayList<String> leer_info)
    {
        for (int i = indice; i < leer_info.size();i+=2)
        {
//            this.esperarXsegundos();
            String[] token = leer_info.get(i).split("\t");
            token[3] = limpiar_formula(token[3]);
            mat.put(token[1].trim(), token[3].trim());
        }
        mat.remove("chemical_id","formula");
    }
    
    private String limpiar_formula(String formula)
    {
        formula=formula.replaceAll("[+|\\-|=|\\[|\\]|//(|//)|\\\\|[0-9]]", "");
        return formula;
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
