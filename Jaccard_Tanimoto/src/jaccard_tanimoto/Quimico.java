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
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author andres
 */
public class Quimico {
    
    ArrayList <String> info_compuestos = new ArrayList<>();//ArrayList almacena el ID y la fórmula de cada compuesto en el archivo tsv. 
    //ArrayList <String> formulas = new ArrayList<>();//ArrayList almacena solo las formulas de cada compuesto ingresado por tsv
    String token[], aux[];
    public static HashMap <String,String> mat = new HashMap<>();
//    TreeMap <String,Float> comparaciones = new TreeMap<>(); 
    
    HashMap <String[],Float> comparaciones = new HashMap<>(); 
    
    
    
    
    public void leer_info(String dir) throws InterruptedException
    {
         try 
         {
                info_compuestos = (ArrayList<String>) Files.readAllLines(Paths.get(System.getProperty("user.dir") + dir));
                      
                Thread t0 = new Thread(new QuimicoThread(info_compuestos, 0));
                Thread t1 = new Thread(new QuimicoThread(info_compuestos, 1));

                t0.start();
                t1.start();

                t0.join();
                t1.join();
         
         aux = new String[mat.size()];
         } 
         
                  
         catch (IOException e) 
         {
             System.out.println("no se pudo leer el archivo "+e);
         }
    
    }
    
    public String [] ObtenerFormulaporArray()
    {
        int i=0;
        
        for(Map.Entry <String,String> mat : mat.entrySet())
        {
            aux[i]=mat.getValue();
            i++;
        }
        
        return aux;

    }
    
    int a = 0;
    
    public void ManejoCaracteres()
    {        
        
        for (int i=0;i<aux.length;i++)
        {
            if(aux[i]!=null)
            {
                int Na = 0;    
                TreeMap<Character,Integer> mapa_a = SepararCaracteres(aux[i]);
                Na=NumeroCaracteres(mapa_a);
                
                System.out.println("\n Na de la formula actual: "+Na+"\n");
                
                for (int j = 1; j<aux.length;j++)
                {
                    int Nb = 0;
                    
                    if(aux[j]!= null)
                    {
                        TreeMap<Character,Integer> mapa_b = SepararCaracteres(aux[j]);
                        Nb = NumeroCaracteres(mapa_b);
                        
//                        System.out.println("\n Nb de la formula actual: "+Nb+"\n");
                        
                        int Nc = 0;
                        Nc = Comparacion_Formulas(mapa_a, mapa_b);
                        
//                        System.out.println("\n Nc: "+Nc);
                                  
                        String []caracteres= new String[2];
                        //String caracteres= "";
                        
                        for (Entry<String,String> mp:mat.entrySet())
                        {
                            String formula = mp.getValue();
                                if(formula.equals(aux[i]))
                                {
                                    caracteres[0]=mp.getKey();
                                    //caracteres+=" "+mp.getKey();
                                }
                                if (formula.equals(aux[j]))
                                {
                                    caracteres[1]=mp.getKey();
//                                    caracteres+=" "+mp.getKey();
                                }   
                        }
                        
                        if(CoeficienteJT(Na,Nb,Nc) >= 0.5)
                        {
                            if(!caracteres[0].equals(caracteres[1]))
                            {
                                comparaciones.put(caracteres, CoeficienteJT(Na, Nb, Nc));
                            }
                        }
                        
                    }
                }
            }

        }
        
        for (Entry<String[],Float> ent:comparaciones.entrySet())
        {
            String [] compuestos=ent.getKey();
            float CBT =ent.getValue();
            DecimalFormat df = new DecimalFormat("0.00");
            System.out.println(compuestos+" - "+df.format(CBT));
        }
        
        
    }
    
    public float CoeficienteJT(int Na, int Nb, int Nc)
    {
        float T;
        T = (float) Nc/(Na+Nb-Nc);
        return T;
    } 
    
    public int Comparacion_Formulas( TreeMap A, TreeMap B )
    {
        int comunes = 0;
        TreeMap<Character,Integer> mapaA = A;
        TreeMap<Character,Integer> mapaB = B;
        
        for(Map.Entry<Character,Integer>mA:mapaA.entrySet())
        {
            for(Map.Entry<Character,Integer>mB:mapaB.entrySet())
            {       
                if(mA.getKey().compareTo(mB.getKey()) == 0)
                {
                    if(mA.getValue()<mB.getValue() || mA.getValue()== mB.getValue())
                    {
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
    
    public TreeMap<Character,Integer> SepararCaracteres(String lyrics)
    {
        int cont;
        TreeMap <Character, Integer> tabla = new TreeMap<Character,Integer>();        
        char []cadena=lyrics.toCharArray();
        
        for(char expresion:cadena)
        {
            cont = 0;
            switch(expresion)
            {
                case '@':
                    tabla.put(expresion, 1);
                break;
                    
                default:
                    for (int i = 0 ; i < cadena.length ; i++)
                    {
                        if(expresion == cadena[i])
                        {
                           if(i!=cadena.length)
                           {
                               cont++;
                               tabla.put(expresion, cont);
                           }
                           else
                           {
                               cont++;
                           }
                            
                        }
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
    
    public void ImprimirCaracteresTabla(TreeMap tabla)
    {
        TreeMap <Character, Integer> resultado = tabla;         
        
        for (Map.Entry<Character, Integer>tab:resultado.entrySet())
        {
            Character caracter = tab.getKey();
            int repeticion = tab.getValue();
            System.out.println(caracter+"-"+repeticion);
        }
        
    }
    
    public int NumeroCaracteres(TreeMap parametro)
    {
        TreeMap <Character,Integer> mapa=parametro;
        int total = 0;
        
        for(Map.Entry<Character, Integer> arbol:mapa.entrySet())
        {
            total+=arbol.getValue();
        }
        
        return total;
    }
    
    
//    public void ObtenerFormula()
//    {
//       formulas=new ArrayList<String>();
//        for (Map.Entry<String,String> mat: mat.entrySet())
//        {
//            formulas.add(mat.getValue());
//            
//        }
//        
//        //Impresion
//        Iterator iterador = formulas.iterator();
//        
//        System.out.println("Formulas: ");
//        while(iterador.hasNext())
//        {
//            Object objeto = iterador.next();
//            System.out.println(objeto);
//        }
//    }
}
