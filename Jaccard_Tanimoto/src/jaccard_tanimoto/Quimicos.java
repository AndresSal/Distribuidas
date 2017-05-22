/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author joselimaico
 */
public class Quimicos {

    ArrayList<String> info_compuestos = new ArrayList<>();
    public static TreeMap<String, String> mat = new TreeMap<>();
    TreeMap<String, Float> comparaciones = new TreeMap<>();
    String token[];
    public static float T = 0;

    public void leer_info(String dir) throws InterruptedException {
        try {

            info_compuestos = (ArrayList<String>) Files.readAllLines(Paths.get(System.getProperty("user.dir") + dir));
            ExecutorService executor = Executors.newCachedThreadPool();
            ExecutorService executor1 = Executors.newCachedThreadPool();
            ExecutorService executor2 = Executors.newCachedThreadPool();
            ExecutorService executor3 = Executors.newCachedThreadPool();
            for (int i = 0; i < info_compuestos.size(); i++) {
                token = info_compuestos.get(i).split("\t");
                token[3] = limpiar_formula(token[3]);
                mat.put(token[1].trim(), token[3].trim());
            }
            mat.remove("chemical_id");

            info_compuestos.clear();

            for (Entry<String, String> m1 : mat.entrySet()) {

                info_compuestos.add(m1.getValue());

            }
            List keys = new ArrayList(mat.keySet());

            for (int i = 0; i < (int) info_compuestos.size() / 4; i++) {
                for (int j = i + 1; j < (int) info_compuestos.size() / 4; j++) {
                    Runnable hilo = new Hilo(info_compuestos.get(i), info_compuestos.get(j));
                    executor.execute(hilo);

                    if (T >= 0.5) {
                        comparaciones.put(keys.get(i) + "\t" + keys.get(j), T);
                    }

                }
            }
            executor.shutdown();
            for (int i = (int) info_compuestos.size() / 4; i < (int) (info_compuestos.size()) / 2; i++) {
                for (int j = i + 1; j < (int) (info_compuestos.size()) / 2; j++) {
                    Runnable hilo = new Hilo(info_compuestos.get(i), info_compuestos.get(j));
                    executor1.execute(hilo);

                    if (T >= 0.5) {
                        comparaciones.put(keys.get(i) + "\t" + keys.get(j), T);
                    }

                }
            }
            executor1.shutdown();

            for (int i = (int) (info_compuestos.size()) / 2; i < (3 * info_compuestos.size()) / 4; i++) {
                for (int j = i + 1; j < (3 * info_compuestos.size()) / 4; j++) {
                    Runnable hilo = new Hilo(info_compuestos.get(i), info_compuestos.get(j));
                    executor2.execute(hilo);

                    if (T >= 0.5) {
                        comparaciones.put(keys.get(i) + "\t" + keys.get(j), T);
                    }

                }
            }
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

            System.out.println(comparaciones.size());

        } catch (IOException e) {
            System.out.println("no se pudo leer el archivo " + e);
        }

    }

    private String limpiar_formula(String formula) {

        formula = formula.replaceAll("[+|\\-|=|\\[|\\]|//(|//)|\\\\|[0-9]]", "");
        return formula;
    }

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
