/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaccard_tanimoto;


import static jaccard_tanimoto.Quimico.comparaciones;
import static jaccard_tanimoto.Quimico.mat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author andres
 */
public class Hilo implements Runnable {
    String sentencia;
    Quimico quimico = new Quimico();
  public static ArrayList<String>   aux =  new ArrayList<>();  
     

    public Hilo(String sentencia) {
        this.sentencia = sentencia;
    }

    public Hilo() {
    }
    
    
    
//     private synchronized void leer_info(String sentencia) 
//    {
////        esperarXsegundos();
//        String token [] = sentencia.split("\t");
//        token[1] = limpiar_formula(token[1]);
//        mat.put(token[0].trim(), token[1].trim());
//    }
        
    

    @Override
    public void run() {
        //leer_info(sentencia);
        ObtenerFormulaporArray();
        
    }
    
    private synchronized  void ObtenerFormulaporArray()
    {
        //int i=0;
        //recorrido de cada par de llaves y valor del hashmap.
        for(Map.Entry <String,String> mat : mat.entrySet())
        {
            //almacenado en un arreglo la formula de la iteraccion i.
            aux.add(mat.getValue());
            
        }
        
        //return aux;

    }
   
    
    
   
    
}


