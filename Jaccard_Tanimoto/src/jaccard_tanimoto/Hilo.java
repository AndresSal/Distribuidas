/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaccard_tanimoto;




import static jaccard_tanimoto.Quimicos.T;
import java.util.ArrayList;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author joselimaico
 */
public class Hilo extends Thread implements Runnable {
    
    //Creacción de ArrayList para el manejo de los caracteres de la formula que se evalua en cada iteracción.
    ArrayList<Character> caracteres = new ArrayList<>();
    //Creacción de 2 treemaps para la comparación de caracteres entre dos formulas.
    TreeMap<Character, Integer> mapa_a = new TreeMap<>();
    TreeMap<Character, Integer> mapa_b = new TreeMap<>();
    
    //formulas que recibirá la funcion que calcula el coeficiente JT.
    String a,b;

    public Hilo(String a,String b) {


        this.a=a;
        this.b=b;

    }


    @Override
    public void run() {
        T(a,b);

    }
//funcion que recibe como entrada dos strings y calcula el coeficiente Jaccard-Tanimoto
    public synchronized void T(String a, String b) {

        int Na = 0, Nb = 0, Nc = 0;
        //llamada a las funciones contaCaracteres y Comparacion_Formulas
        Na=contarCaracteres(a, mapa_a);
        Nb=contarCaracteres(b, mapa_b);
        Nc=Comparacion_Formulas(mapa_a, mapa_b);
        //limpieza de los mapas A y B.
        mapa_a.clear();
        mapa_b.clear();
         T=(float)Nc/(Na+Nb-Nc);

    }

    //funcion que recibe como argumento dos treemaps y devuelve el número de elementos en común.
    public int Comparacion_Formulas(TreeMap<Character,Integer> A, TreeMap <Character,Integer> B) {
        //variable que almacenará el número de caracteres en común.
        int comunes = 0;

        for (Map.Entry<Character, Integer> mA : A.entrySet()) {
            //loop de cada elemento del treemap A.
            for (Map.Entry<Character, Integer> mB : B.entrySet()) {
                //loop de cada elemento del treemap B
                if (mA.getKey().equals(mB.getKey())) {
                    //en caso de que las llaves del elemento actual de ambas treemaps coincidan
                    if (mA.getValue() <= mB.getValue() ) {
                        //el valor menor será añadido a la variable
                        comunes += mA.getValue();
                    } else {
                        comunes += mB.getValue();
                    }
                }
            }
        }
        return comunes;
    }

    //funcion que recibe como parámetros una fórmula y un treemap con todos los caracteres que se hayan contado, retorna el total de caracteres del treemap.
    public int contarCaracteres(String formula,TreeMap<Character,Integer> tabla) {
        int cont;
         //variable que contabilizará el número de caracteres de la formula.

        for (int i = 0; i < formula.length(); i++) {
            //almacenar todos los caracteres de la formula en un ArrayList
            caracteres.add(formula.charAt(i));
        }
        //recorrido de cada caracter de la formula
        for (char expresion : caracteres) {
            cont = 0;
            switch (expresion) {
                    //en caso de ser una @, se añade en la tabla el caracter y el valor de 1 y termina la iteracción.
                case '@':
                    tabla.put(expresion, 1);

                    break;

                default:
                    //para el caso en que no sea @ se vuelve a comparar cada uno de los caracteres del ArrayList
                    for(int i=0;i<caracteres.size();i++){
                    if (expresion==caracteres.get(i)) {
                        // si existe coincidencia se aumenta el contador   
                        cont++;
                            
                        
                    }
                    
                    }
                    //al final colocar el caracter junto con el valor del contador en el treemap.
                    tabla.put(expresion, cont);
                        
                    break;

            }
        }
        //ahora sumará el total de caracteres que tendrá el treemap generado
        cont=0;
        for(Map.Entry<Character,Integer> t:tabla.entrySet()){
        
            cont+=t.getValue();
        }
        
        caracteres.clear();
        //retorno del total de caracteres encontrados en la fórmula.}
        return cont;
        
    }

}
