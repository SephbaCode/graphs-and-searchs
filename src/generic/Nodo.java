package generic;

import java.util.LinkedHashMap;
import java.util.Map;

public class Nodo<T> {

    private T id;
    private Map<T, Nodo<T>> conexionesEntrantes;
    private Map<T, Nodo<T>> conexionesSalientes;

    public Nodo(T id) {
        this.id = id;
        this.conexionesEntrantes = new LinkedHashMap<>();
        this.conexionesSalientes = new LinkedHashMap<>();
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public Map<T, Nodo<T>> getConexionesEntrantes() {
        return conexionesEntrantes;
    }

    public Map<T, Nodo<T>> getConexionesSalientes() {
        return conexionesSalientes;
    }

    public void agregarConexionEntrante(Nodo<T> nodo) {
        conexionesEntrantes.put(nodo.getId(), nodo);
    }

    public void agregarConexionSaliente(Nodo<T> nodo) {
        conexionesSalientes.put(nodo.getId(), nodo);
    }

    public boolean tieneConexionCon(T otroId) {
        return conexionesSalientes.containsKey(otroId) || conexionesEntrantes.containsKey(otroId);
    }

    @Override
    public String toString() {
        return "Nodo{" +
                "id=" + id +
                ", entrantes=" + conexionesEntrantes.keySet() +
                ", salientes=" + conexionesSalientes.keySet() +
                '}';
    }
}
