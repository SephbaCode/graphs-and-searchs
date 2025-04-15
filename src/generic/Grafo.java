package generic;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Collection;

public class Grafo<T> {

    private Map<T, Nodo<T>> nodos;

    public Grafo() {
        this.nodos = new LinkedHashMap<>();
    }

    public void agregarNodo(Nodo<T> nodo) {
        nodos.put(nodo.getId(), nodo);
    }

    public Nodo<T> obtenerNodo(T id) {
        return nodos.get(id);
    }

    public boolean contieneNodo(T id) {
        return nodos.containsKey(id);
    }

    public Collection<Nodo<T>> obtenerTodosLosNodos() {
        return nodos.values();
    }

    public void conectar(T idOrigen, T idDestino) {
        Nodo<T> origen = nodos.get(idOrigen);
        Nodo<T> destino = nodos.get(idDestino);
        if (origen != null && destino != null) {
            origen.agregarConexionSaliente(destino);
            destino.agregarConexionEntrante(origen);
        }
    }

    public void limpiar() {
        nodos.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Grafo:\n");
        for (Nodo<T> nodo : nodos.values()) {
            sb.append(nodo.toString()).append("\n");
        }
        return sb.toString();
    }
}
