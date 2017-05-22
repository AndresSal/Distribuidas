
package jaccard_tanimoto;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author joselimaico andressalazar
 */
public class Quimicos {
    
    //ArrayList que almacena el contenido del archivo tsv
    ArrayList<String> info_compuestos = new ArrayList<>();
    //TreeMap que almacena el id y la formula de cada compuesto.
    public static TreeMap<String, String> mat = new TreeMap<>();
    //TreeMap que almacena todas las comparaciones de todos los compuestos
    TreeMap<String, Float> comparaciones = new TreeMap<>();
    //array de Strings que permite el manejo de id's y formulaś y agregarlos al TreeMap mat.
    String token[];
    //el valor del coeficiente JT
    public static float T = 0;

    //función que lee todo el string de entrada y lo almacena en el TreeMap mat.
    public void leer_info(String dir) throws InterruptedException {
        try {
            //lectura de todas las líneas del archivo y los almacena en el arraylist info compuestos
            info_compuestos = (ArrayList<String>) Files.readAllLines(Paths.get(System.getProperty("user.dir") + dir));
            //creación de ejecutores para el uso de threads
            //uso de newCachedThreadPool para la asignación dinámica de los threads.
            ExecutorService executor = Executors.newCachedThreadPool();
            ExecutorService executor1 = Executors.newCachedThreadPool();
            ExecutorService executor2 = Executors.newCachedThreadPool();
            ExecutorService executor3 = Executors.newCachedThreadPool();
            
            //recorrido del contenido del ArrayList 
            for (int i = 0; i < info_compuestos.size(); i++) {
                //fragmenta el string en cada \t
                token = info_compuestos.get(i).split("\t");
                //llamada a la funcion limpiar formula sobre el campo que contenga la formula de un comupesto 
                token[3] = limpiar_formula(token[3]);
                //colocar el id y la formula limpia en el TreeMap mat.
                mat.put(token[1].trim(), token[3].trim());
            }
            //remover los strings chemical_id y formula
            mat.remove("chemical_id");
            
            //se limpia el Arraylist info_compuesto
            info_compuestos.clear();
            
            //almacenar las formulas en el arraylist
            for (Entry<String, String> m1 : mat.entrySet()) {

                info_compuestos.add(m1.getValue());

            }
            //creación de una lista que almacene los id de cada formula del Treemap mat.
            List keys = new ArrayList(mat.keySet());

            //ejecución de los executors, se divide el recorrido de todas las formulas en 4 partes.
            for (int i = 0; i < (int) info_compuestos.size() / 4; i++) {
                //loop que empieza una posición más adelante del elemento actual
                for (int j = i + 1; j < (int) info_compuestos.size() / 4; j++) {
                    //instanciacion de un nuevo hilo
                    Runnable hilo = new Hilo(info_compuestos.get(i), info_compuestos.get(j));
                    //ejecución del metodo run() del hilo
                    executor.execute(hilo);
                    //solo considerar los coeficientes iguales o superiores a 0.5
                    if (T >= 0.5) {
                        //guardar en el Treemap de comparaciones
                        comparaciones.put(keys.get(i) + "\t" + keys.get(j), T);
                    }

                }
            }
            //terminada la ejecución del executor.
            executor.shutdown();
            //ejecución de los executors, se divide el recorrido de todas las formulas en 4 partes.
            for (int i = (int) info_compuestos.size() / 4; i < (int) (info_compuestos.size()) / 2; i++) {
                //loop que empieza una posición más adelante del elemento actual
                for (int j = i + 1; j < (int) (info_compuestos.size()) / 2; j++) {
                    //instanciacion de un nuevo hilo
                    Runnable hilo = new Hilo(info_compuestos.get(i), info_compuestos.get(j));
                    //ejecución del metodo run() del hilo
                    executor1.execute(hilo);
                    //solo considerar los coeficientes iguales o superiores a 0.5
                    if (T >= 0.5) {
                        //guardar en el Treemap de comparaciones
                        comparaciones.put(keys.get(i) + "\t" + keys.get(j), T);
                    }

                }
            }
            //terminada la ejecución del executor.
            executor1.shutdown();
//ejecución de los executors, se divide el recorrido de todas las formulas en 4 partes.
            for (int i = (int) (info_compuestos.size()) / 2; i < (3 * info_compuestos.size()) / 4; i++) {
                 //loop que empieza una posición más adelante del elemento actual
                for (int j = i + 1; j < (3 * info_compuestos.size()) / 4; j++) {
                     //instanciacion de un nuevo hilo
                    Runnable hilo = new Hilo(info_compuestos.get(i), info_compuestos.get(j));
                    //ejecución del metodo run() del hilo
                    executor2.execute(hilo);
                    //solo considerar los coeficientes iguales o superiores a 0.5
                    if (T >= 0.5) {
                        //guardar en el Treemap de comparaciones
                        comparaciones.put(keys.get(i) + "\t" + keys.get(j), T);
                    }

                }
            }
             //terminada la ejecución del executor.
            executor2.shutdown();

            for (int i = (int) (3 * info_compuestos.size()) / 4; i < info_compuestos.size(); i++) {
                for (int j = i + 1; j < info_compuestos.size(); j++) {
                    Runnable hilo = new Hilo(info_compuestos.get(i), info_compuestos.get(j));
                    executor3.execute(hilo);

                    if (T >= 0.5) {
                        comparaciones.put(keys.get(i) + "\t" + keys.get(j), T);
                    }

                }
            }
            executor3.shutdown();
            //imprime el numero de registros que tiene el TreeMap comparaciones
            System.out.println(comparaciones.size());

        } catch (IOException e) {
            System.out.println("no se pudo leer el archivo " + e);
        }

    }
    //funcion que elimina todas los caracteres y números no permitidos
    private String limpiar_formula(String formula) {

        formula = formula.replaceAll("[+|\\-|=|\\[|\\]|//(|//)|\\\\|[0-9]]", "");
        return formula;
    }
    //imprimir en un archivo tvs los 21 primeros registros
    public void imprimir() throws FileNotFoundException {

        FileOutputStream os = new FileOutputStream("solution.tsv");
        PrintStream ps = new PrintStream(os);
        DecimalFormat df = new DecimalFormat("0.00");
        int i = 0;

        for (Map.Entry<String, Float> ent : comparaciones.entrySet()) {
            float CJT = ent.getValue();
            String compuestos = ent.getKey();
            ps.println(compuestos + "\t" + df.format(CJT));
            i++;

            if (i == 21) {
                break;
            }

        }
    }

}
