/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaccard_tanimoto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author andres
 */
public class Quimico {
    
    
    ArrayList <String> info_compuestos = new ArrayList<>();//ArrayList almacena el ID y la fórmula de cada compuesto en el archivo tsv. 
    ArrayList <String> formulas = new ArrayList<>();
    
    String token[];
    String [] aux= new String [200];
    HashMap <String,String> mat = new HashMap<>();
    
    
    HashMap <String[],Float> comparaciones = new HashMap<>(); 
    
    
    
    public void leer_info(String dir) 
    {
         try 
         {
                info_compuestos = (ArrayList<String>) Files.readAllLines(Paths.get(System.getProperty("user.dir") + dir));
                
                for(int i =0; i< info_compuestos.size();i++)
                {
                    token = info_compuestos.get(i).split("\t");
                    token[1] = limpiar_formula(token[1]);
                    
                    mat.put(token[0].trim(), token[1].trim());
                }
                
                    mat.remove("chemical_id","formula");
            
            
                for (Map.Entry<String,String> mat : mat.entrySet()) 
                {
                    String id = mat.getKey();
                    String formula =mat.getValue();
                    //System.out.println(id+"-"+formula);
                }
                
         } 
         
         catch (IOException e) 
         {
             System.out.println("no se pudo leer el archivo "+e);
         }
    
    }

    private String limpiar_formula(String formula)
    {
        formula=formula.replaceAll("[+|\\-|=|\\[|\\]|//(|//)|\\\\|[0-9]]","");
        return formula;
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
    
    public void ObtenerFormula()
    {
       formulas=new ArrayList<String>();
        for (Map.Entry<String,String> mat: mat.entrySet())
        {
            formulas.add(mat.getValue());
            
        }
        
        //Impresion
        Iterator iterador = formulas.iterator();
        
        System.out.println("Formulas: ");
        while(iterador.hasNext())
        {
            Object objeto = iterador.next();
            System.out.println(objeto);
        }
    }
    
    
    
    int a = 0;
    
    public void ManejoCaracteres()
    {        
        for (int i=0;i<aux.length;i++)
        {

            if(aux[i]!=null)
            {
                int Na = 0;
//                System.out.println("\n Formula actual: "+aux[i].toString()+"\n");
//                System.out.println("\n Numero de caracteres en la formula actual: \n");
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
                        System.out.println("\n Nb de la formula actual: "+Nb+"\n");
                        int Nc = 0;
                        Nc = Comparacion_Formulas(mapa_a, mapa_b);
                        System.out.println("\n Nc: "+Nc);
                        float T = (float)Nc/(Na+Nb-Nc); 
                        
                        String []caracteres= new String[2];
                        for (Map.Entry<String,String> mp:mat.entrySet())
                        {
                            String formula = mp.getValue();
                                if(formula.compareTo(aux[i]) == 0)
                                {
                                    caracteres[0]=mp.getKey();
                                }
                                if (formula.compareTo(aux[j]) == 0)
                                {
                                    caracteres[1]=mp.getKey();
                                }
                            
                        }
                        
                        comparaciones.put(caracteres, T);
                    }
                }
            }

        }
        for (Map.Entry<String[],Float> ent:comparaciones.entrySet())
        {
            String [] compuestos=ent.getKey();
            float CBT =ent.getValue();
            System.out.println(Arrays.toString(compuestos)+" - "+CBT);
        }
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
        
        for (Map.Entry<Character, Integer>tab:tabla.entrySet())
        {
            Character caracter = tab.getKey();
            int repeticion = tab.getValue();
            System.out.println(caracter+"-"+repeticion);
        }
        
        return tabla;
        
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
}
