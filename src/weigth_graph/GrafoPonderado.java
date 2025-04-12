package weigth_graph;


import generic.Grafo;
import generic.Nodo;

import java.util.Collection;

public class GrafoPonderado<T> extends Grafo<T> {

    // Metodo para conectar dos nodos con un peso específico
    public void conectar(T idOrigen, T idDestino, int peso) {
        NodoPonderado<T> origen = (NodoPonderado<T>) obtenerNodo(idOrigen);
        NodoPonderado<T> destino = (NodoPonderado<T>) obtenerNodo(idDestino);

        if (origen != null && destino != null) {
            origen.agregarConexionSaliente(destino, peso);
            destino.agregarConexionEntrante(origen, peso);
        }
    }

    // Metodo para obtener el peso de la conexión entre dos nodos
    public Integer obtenerPesoConexion(T idOrigen, T idDestino) {
        NodoPonderado<T> origen = (NodoPonderado<T>) obtenerNodo(idOrigen);
        if (origen != null) {
            return origen.obtenerPesoConexionSaliente(idDestino);
        }
        return null;
    }

    // Metodo para imprimir el grafo
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Grafo Ponderado:\n");
        for (Nodo<T> nodo : obtenerTodosLosNodos()) {
            sb.append(nodo.toString()).append("\n");
        }
        return sb.toString();
    }
}
