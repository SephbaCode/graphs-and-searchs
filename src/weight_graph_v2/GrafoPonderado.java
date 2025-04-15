package weight_graph_v2;

import generic.Grafo;
import generic.Nodo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GrafoPonderado extends Grafo<String> {

    public GrafoPonderado() {
        super();
    }

    public void conectar(String idOrigen, String idDestino, int valor_arista) {
        NodoPonderado nodoOrigen = (NodoPonderado) this.obtenerNodo(idOrigen);
        NodoPonderado nodoDestino = (NodoPonderado) this.obtenerNodo(idDestino);

        if (nodoOrigen != null && nodoDestino != null && !nodoOrigen.equals(nodoDestino)) {
            nodoOrigen.agregarConexionSaliente(nodoDestino, valor_arista);
            nodoDestino.agregarConexionEntrante(nodoOrigen);
        }else{
            System.out.println("Uno de los nodos no existe en el grafo.");
            System.out.println("Origen: " + idOrigen + ", Destino: " + idDestino);
        }
    }

    public int obtenerPesoConexion(String idOrigen, String idDestino) {
        NodoPonderado nodoOrigen = (NodoPonderado) obtenerNodo(idOrigen);
        return nodoOrigen.getValorConexion(idDestino);
    }

    public void cargarGrafoDesdeCSV(String rutaNodos, String rutaAristas) {
        // Implementar la carga del grafo desde un archivo
        // Este metodo debe leer el archivo y crear los nodos y conexiones correspondientes

        //limpiar grafo actual
        this.limpiar();

        try {
            // Cargar nodos
            BufferedReader brNodos = new BufferedReader(new FileReader(rutaNodos));
            String linea;
            boolean esPrimera = true;

            while ((linea = brNodos.readLine()) != null) {
                if (esPrimera) { // Saltar cabecera
                    esPrimera = false;
                    continue;
                }
                String[] partes = linea.split(",");
                String id = partes[0].trim();
                double heuristica = Double.parseDouble(partes[1].trim());
                NodoPonderado newNodo = new NodoPonderado(id, (int) heuristica);
                this.agregarNodo(newNodo);
            }
            brNodos.close();

            // Cargar aristas
            BufferedReader brAristas = new BufferedReader(new FileReader(rutaAristas));
            esPrimera = true;

            while ((linea = brAristas.readLine()) != null) {
                if (esPrimera) { // Saltar cabecera
                    esPrimera = false;
                    continue;
                }
                String[] partes = linea.split(",");
                String origen = partes[0].trim();
                String destino = partes[1].trim();
                int peso = Integer.parseInt(partes[2].trim());
                this.conectar(origen, destino, (int) peso);
            }
            brAristas.close();

            System.out.println("¡Grafo cargado correctamente!");

        } catch (IOException e) {
            System.err.println("Error al leer los archivos CSV: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error de formato numérico en los archivos CSV: " + e.getMessage());
        }
    }

    public Integer getValorEuristico(String id) {
        NodoPonderado nodo = (NodoPonderado) this.obtenerNodo(id);
        return nodo.getValorEuristico();
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Grafo Ponderado:\n");
        for (Nodo<String> nodo :   this.obtenerTodosLosNodos()) {
            sb.append(nodo.toString()).append("\n");
        }
        return sb.toString();
    }

}
