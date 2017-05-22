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

    ArrayList<Character> caracteres = new ArrayList<>();
    TreeMap<Character, Integer> mapa_a = new TreeMap<>();
    TreeMap<Character, Integer> mapa_b = new TreeMap<>();
    
    String a,b;

    public Hilo(String a,String b) {


        this.a=a;
        this.b=b;

    }


    @Override
    public void run() {
        T(a,b);

    }

    public synchronized void T(String a, String b) {

        int Na = 0, Nb = 0, Nc = 0;
        Na=contarCaracteres(a, mapa_a);
        Nb=contarCaracteres(b, mapa_b);
        Nc=Comparacion_Formulas(mapa_a, mapa_b);
        mapa_a.clear();
        mapa_b.clear();
         T=(float)Nc/(Na+Nb-Nc);

    }

    public int Comparacion_Formulas(TreeMap<Character,Integer> A, TreeMap <Character,Integer> B) {
        int comunes = 0;

        for (Map.Entry<Character, Integer> mA : A.entrySet()) {
            for (Map.Entry<Character, Integer> mB : B.entrySet()) {
                if (mA.getKey().equals(mB.getKey())) {
                    if (mA.getValue() <= mB.getValue() ) {
                        comunes += mA.getValue();
                    } else {
                        comunes += mB.getValue();
                    }
                }
            }
        }
        return comunes;
    }

    public int contarCaracteres(String formula,TreeMap<Character,Integer> tabla) {
        int cont;
         

        for (int i = 0; i < formula.length(); i++) {

            caracteres.add(formula.charAt(i));
        }
        for (char expresion : caracteres) {
            cont = 0;
            switch (expresion) {
                case '@':
                    tabla.put(expresion, 1);

                    break;

                default:
                    for(int i=0;i<caracteres.size();i++){
                    if (expresion==caracteres.get(i)) {
                            cont++;
                            
                        
                    }
                    
                    }
                    tabla.put(expresion, cont);
                        
                    break;

            }
        }
        cont=0;
        for(Map.Entry<Character,Integer> t:tabla.entrySet()){
        
            cont+=t.getValue();
        }
        caracteres.clear();
        return cont;
        
    }

}
