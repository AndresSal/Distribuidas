
package jaccard_tanimoto;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 *
 * @author joselimaico andressalazar
 */
public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        //instancia de clase Quimicos
        Quimicos q = new Quimicos();
        //llamada a la función leer_info
        q.leer_info("//src//Formato//ZINC_chemicals.tsv");
        //llamada a la función imprimir
        q.imprimir();
    }

}
