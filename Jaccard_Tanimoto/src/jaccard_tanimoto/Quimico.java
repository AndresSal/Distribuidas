/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaccard_tanimoto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author andres
 */
public class Quimico {
    //ArrayList almacena el ID y la fórmula de cada compuesto en el archivo tsv. 
    ArrayList <String> info_compuestos = new ArrayList<>();
    
    
    public static HashMap <String,String> mat = new HashMap<>();
    public static HashMap <String,Float> comparaciones = new HashMap<>(); 
    //public static TreeMap <String,Float> comparaciones = new TreeMap<>(); 


    
    
    
    
    
    public void leer_info(String dir) throws InterruptedException
    {
         try 
         {
                //el contenido de la cadena de texto es almacenado en un Arraylist
                info_compuestos = (ArrayList<String>) Files.readAllLines(Paths.get(System.getProperty("user.dir") + dir));
               for(int i=0 ; i<info_compuestos.size();i++){
               String token [] =info_compuestos.get(i) .split("\t");
               token[3] = limpiar_formula(token[3]);
        mat.put(token[1].trim(), token[3].trim());
               }
                 
//        
                
                //executor realiza una gestión dinámica de los threads dque se le hayan asignado, en este caso 4.
                ExecutorService executor = Executors.newFixedThreadPool(8);
                ExecutorService executor1 = Executors.newFixedThreadPool(8);
               
                //realizar una evaluación de cada elemento contenido en el Arraylist.
                //for (String sentencia:info_compuestos)
                //{
                    //se instancia un nuevo objeto de tilo Thread
                    Runnable hilo = new Hilo();
                    
                    //ejecutar la funcion run del Thread.
                    executor.execute(hilo);
                //}
                //cerrar el ejecutor
                executor.shutdown();
                
                while(!executor.isTerminated())
                
                {
                    //se espera que terminen de ejecutarse todos los procesos para continuar a las siguientes instrucciones.
                }
                
               
                
                //remover los titulos del hashmap.
               // mat.remove("chemical_id");
               Runnable quimicothread=new QuimicoThread();
               executor1.execute(quimicothread);
               executor1.shutdown();
               while(!executor1.isTerminated()){
               
               }

         } 
         
                  
         catch (IOException e) 
         {
             //la lectura del archivo se controla con un try catch, si no existe el documento, imprime excepción
             System.out.println("no se pudo leer el archivo "+e);
         }
    
    }
    
    public void imprimir(){
    
           for (Map.Entry<String,Float> ent:comparaciones.entrySet())
        {
            float CJT=ent.getValue();
            String compuestos =ent.getKey();
            DecimalFormat df = new DecimalFormat("0.00");
//            System.out.println(Arrays.toString(compuestos)+" - "+df.format(CBT));
            System.out.println(compuestos+" - "+df.format(CJT));
            
                    
        }
    System.out.println(comparaciones.size());
    }
    
    private String limpiar_formula(String formula)
    {
        formula=formula.replaceAll("[+|\\-|=|\\[|\\]|//(|//)|\\\\|[0-9]]","");
        return formula;
    }


        

  
}
