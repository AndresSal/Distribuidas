/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaccard_tanimoto;

import static jaccard_tanimoto.Quimico.comparaciones;
import static jaccard_tanimoto.Quimico.mat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import static jaccard_tanimoto.Hilo.aux;
/**
 *
 * @author andres
 */
public class QuimicoThread extends Thread implements Runnable
{
    
    
    

    public QuimicoThread()
    {
       // ObtenerFormulaporArray();
//        System.out.println(aux[12000]);
//        System.out.println(aux.length);
//        System.out.println(mat.size());
    }
    
    @Override
    public void run()
    {
        
        ManejoCaracteres();
    }
    
     private synchronized void ManejoCaracteres()
    {        
        //iteracción para tomar una fórmula del arreglo generado en la funcion ObtenerFormulaporArray
        for ( int i=0;i<aux.size();i++)
        {
            
            //no considera los nulos del arreglo. 
            //if(aux[i]!=null)
            //{
                
                int Na = 0;    
                //llamada a la funcion SepararCaracteres
                TreeMap<Character,Integer> mapa_a = SepararCaracteres(aux.get(i));
                //llamada a la función NumeroCaracteres
                Na=NumeroCaracteres(mapa_a);
                
                //System.out.println("\n Numero de caracteres de "+formulaA+": "+Na+"\n");
                
                //la formula actual se la compara con el resto de formulas del arreglo
                for (int j=i+1;j<aux.size();j++)
                {
                    int Nb = 0;
                    //nuevamente no toma en cuenta los nulos del arreglo
                    //if(aux[j]!= null)
                   // {
                        //llamada a la funcion SepararCaracteres
                        TreeMap<Character,Integer> mapa_b = SepararCaracteres(aux.get(j));
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
                        
                        
                
                            //caracteres=mat.get(aux.get(i))+"  "+mat.get(aux.get(j));
                            caracteres=getKeysByValue(mat, aux.get(i))+" "+getKeysByValue(mat,aux.get(j));
                        //solo toma valores superiores a 0.5
                        if(T >= 0.5)
                        
                                //se añade al hashmap final los ids comparados y su respetivo ceficiente.
                                comparaciones.put(caracteres, T);    
                        
                        
                    //}
                }
            //}

        }
        
        
        
        
    }//
    
    private float CoeficienteJT(int Na, int Nb, int Nc) 
    {
        float T;
        T = (float) Nc/(Na+Nb-Nc);
        return T;
    }
    
    private int Comparacion_Formulas( TreeMap A, TreeMap B )
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
    private TreeMap<Character,Integer> SepararCaracteres(String lyrics)
    {
        int cont;
        //el resultado será almacenado en un hashmap ordenado
        TreeMap <Character, Integer> tabla = new TreeMap<>();        
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
    
        //funcion que suma el total de caracteres de una formula
    private int NumeroCaracteres(TreeMap parametro)
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
    
    //función para obtener las fórmulas de los compuestos    
    
    
    public static String getKeysByValue(HashMap<String,String> map, String value) {
    String keys = "";
    for (Entry<String,String> entry : map.entrySet()) {
        if (Objects.equals(value, entry.getValue())) {
            keys=entry.getKey();
        }
    }
    return keys;
}
    
}
