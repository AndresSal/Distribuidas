/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaccard_tanimoto;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 *
 * @author joselimaico
 */
public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        Quimicos q = new Quimicos();
        q.leer_info("//src//Formato//ZINC_chemicals.tsv");
        q.imprimir();
    }

}
