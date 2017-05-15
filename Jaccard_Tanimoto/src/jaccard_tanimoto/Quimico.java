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
    
    String token[], aux[] =  new String[50000];
    public static HashMap <String,String> mat = new HashMap<>();
//    HashMap <String[],Float> comparaciones = new HashMap<>(); 
    TreeMap <String,Float> comparaciones = new TreeMap<>(); 
    
    
    
    
    
    
    public void leer_info(String dir) throws InterruptedException
    {
         try 
         {
                //el contenido de la cadena de texto es almacenado en un Arraylist
                info_compuestos = (ArrayList<String>) Files.readAllLines(Paths.get(System.getProperty("user.dir") + dir));
                
                //executor realiza una gestión dinámica de los threads dque se le hayan asignado, en este caso 4.
                ExecutorService executor = Executors.newFixedThreadPool(4);
                
                //realizar una evaluación de cada elemento contenido en el Arraylist.
                for (String sentencia:info_compuestos)
                {
                    //se instancia un nuevo objeto de tilo Thread
                    Runnable hilo = new Hilo(sentencia);
                    
                    //ejecutar la funcion run del Thread.
                    executor.execute(hilo);
                }
                //cerrar el ejecutor
                executor.shutdown();
                
                while(!executor.isTerminated())
                
                {
                    //se espera que terminen de ejecutarse todos los procesos para continuar a las siguientes instrucciones.
                }
                
                //remover los titulos del hashmap.
                mat.remove("chemical_id","formula");

         } 
         
                  
         catch (IOException e) 
         {
             //la lectura del archivo se controla con un try catch, si no existe el documento, imprime excepción
             System.out.println("no se pudo leer el archivo "+e);
         }
    
    }

//función para obtener las fórmulas de los compuestos    
    public String [] ObtenerFormulaporArray()
    {
        int i=0;
        //recorrido de cada par de llaves y valor del hashmap.
        for(Map.Entry <String,String> mat : mat.entrySet())
        {
            //almacenado en un arreglo la formula de la iteraccion i.
            aux[i]=mat.getValue();
            i++;
        }
        
        return aux;

    }
    
    int a = 0;
 
    
 //funcion principal para el calculo coeficiente Jaccard-Tanimoto.
    
    public void ManejoCaracteres()
    {        
        //iteracción para tomar una fórmula del arreglo generado en la funcion ObtenerFormulaporArray
        for (int i=0;i<aux.length;i++)
        {
            //no considera los nulos del arreglo. 
            if(aux[i]!=null)
            {
                
                int Na = 0;    
                //llamada a la funcion SepararCaracteres
                TreeMap<Character,Integer> mapa_a = SepararCaracteres(aux[i]);
                //llamada a la función NumeroCaracteres
                Na=NumeroCaracteres(mapa_a);
                
                System.out.println("\n Numero de caracteres de "+aux[i]+": "+Na+"\n");
                
                //la formula actual se la compara con el resto de formulas del arreglo
                for (int j = 1; j<aux.length;j++)
                {
                    int Nb = 0;
                    //nuevamente no toma en cuenta los nulos del arreglo
                    if(aux[j]!= null)
                    {
                        //llamada a la funcion SepararCaracteres
                        TreeMap<Character,Integer> mapa_b = SepararCaracteres(aux[j]);
                        //llamada a la función NumeroCaracteres
                        Nb = NumeroCaracteres(mapa_b);
                        
//                        System.out.println("\n Nb de la formula actual: "+Nb+"\n");
                        
                        int Nc = 0;
                        //llamada a la funcion ComparacionFormulas
                        Nc = Comparacion_Formulas(mapa_a, mapa_b);
                        
//                        System.out.println("\n Nc: "+Nc);
                        //llamada a la función CoeficienteJT
                        float T=CoeficienteJT(Na, Nb, Nc);
                        String caracteres= "";
                        
                        //iteracion para obtener el id de cada compuesto
                        for (Entry<String,String> mp:mat.entrySet())
                        {
                            String formula = mp.getValue();
                                if(formula.equals(aux[i]))
                                {
                                    //se añade el id al String 
                                    caracteres+= " "+mp.getKey();
                                }
                                if (formula.equals(aux[j]))
                                {
                                    //se añade el id al String 
                                    caracteres+=" "+mp.getKey();
                                }   
                        }
                        //solo toma valores superiores a 0.5
                        if(T >= 0.5)
                        {
                                //se añade al hashmap final los ids comparados y su respetivo ceficiente.
                                comparaciones.put(caracteres, T);    
                        }
                        
                    }
                }
            }

        }
        
        for (Entry<String,Float> ent:comparaciones.entrySet())
        {
            String compuestos=ent.getKey();
            float CBT =ent.getValue();
            DecimalFormat df = new DecimalFormat("0.00");
//            System.out.println(Arrays.toString(compuestos)+" - "+df.format(CBT));
            System.out.println(compuestos+" - "+df.format(CBT));
        }
        
        
    }

//  funcion calcula el coeficiente JT con datos obtenidos de la funcion ManejoCaracteres
    public float CoeficienteJT(int Na, int Nb, int Nc) 
    {
        float T;
        T = (float) Nc/(Na+Nb-Nc);
        return T;
    } 

//funcion compara el numero de elementos en comun que tienen dos compuestos    
    public int Comparacion_Formulas( TreeMap A, TreeMap B )
    {
        int comunes = 0;
        //los elementos de cada compuesto se encuentran almacenados en dos Hashmaps.
        TreeMap<Character,Integer> mapaA = A;
        TreeMap<Character,Integer> mapaB = B;
        
        //iteracción toma el valor del primer hashmap
        for(Map.Entry<Character,Integer>mA:mapaA.entrySet())
        {
            //iteracción toma el valor del segundo hashmap
            for(Map.Entry<Character,Integer>mB:mapaB.entrySet())
            {       
                //si los elementos del primer y segundo hashmap son iguales
                if(mA.getKey().compareTo(mB.getKey()) == 0)
                {
                    //en caso de coincidencia se elige el menor del numero de repeticiones del elemento 
                    if(mA.getValue()<mB.getValue() || mA.getValue()== mB.getValue())
                    {
                        //el resultado se va sumando conforme cumpla la condición.
                        comunes+=mA.getValue();
                    }
                    else
                    {
                        comunes+=mB.getValue();
                    }
                }
            }
        }
        return comunes;
    }
    
  //funcion que separa los caracteres de una formula pasada como parámetro 
    public TreeMap<Character,Integer> SepararCaracteres(String lyrics)
    {
        int cont;
        //el resultado será almacenado en un hashmap ordenado
        TreeMap <Character, Integer> tabla = new TreeMap<Character,Integer>();        
        //la formula pasada por parámetro se convierte en array
        char []cadena=lyrics.toCharArray();
        //se evalua cada elemento de dicho arreglo
        for(char expresion:cadena)
        {
            cont = 0;
            switch(expresion)
            {
                //en caso de que lea una @, el programa asigna su valor como 1.
                case '@':
                    tabla.put(expresion, 1);
                break;
                    
                default:
                    //recorre nuevamente la cadena en busca de elementos iguales
                    for (int i = 0 ; i < cadena.length ; i++)
                    {
                        // si existe coincidencia va sumando el numero de veces que se repite 
                        if(expresion == cadena[i])
                        {
                            //condicion para que lea hasta el último elemento de la cadena
                           if(i!=cadena.length)
                           {
                               cont++;
                               // se añade como llave el elemento y como valor el numero de veces que se repitio.
                               tabla.put(expresion, cont);
                           }
                           else
                           {
                               cont++;
                           }
                            
                        }
                        //si no hay coincidencia simplemente añade al hashmap el elemento conjunto con el numero de veces que se repitio.
                        else
                        {
                            tabla.put(expresion, cont);
                        }
                    }
                break;
                    
            }
        }
         
        return tabla;
        
    }
    
    //funcion que imprime los caracteres del hashmap de una formula concreta
    public void ImprimirCaracteresTabla(TreeMap tabla)
    {
        
        TreeMap <Character, Integer> resultado = tabla;         
        
        //iteración de cada elemento del hashmap
        for (Map.Entry<Character, Integer>tab:resultado.entrySet())
        {
            //toma la llave del hashmap
            Character caracter = tab.getKey();
            //toma el valor del hashmap
            int repeticion = tab.getValue();
            //imprime por consola la llave y el valor del elemento actual del hashmap.
            System.out.println(caracter+"-"+repeticion);
        }
        
    }
    
    //funcion que suma el total de caracteres de una formula
    public int NumeroCaracteres(TreeMap parametro)
    {
        TreeMap <Character,Integer> mapa=parametro;
        int total = 0;
        //iteracion de cada elemento del hashmap.
        for(Map.Entry<Character, Integer> arbol:mapa.entrySet())
        {
            //se suman los valores de cada registro del hashmap.
            total+=arbol.getValue();
        }
        
        return total;
    }
  
}
