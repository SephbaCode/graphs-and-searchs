package weigth_graph;

import generic.Arista;
import generic.Nodo;


import java.util.LinkedHashMap;
import java.util.Map;

public class NodoPonderado<T> extends Nodo<T> {

    private Map<T, Arista<T>> conexionesEntrantes;
    private Map<T, Arista<T>> conexionesSalientes;

    public NodoPonderado(T id) {
        super(id);
        this.conexionesEntrantes = new LinkedHashMap<>();
        this.conexionesSalientes = new LinkedHashMap<>();
    }

    // Metodo para agregar una conexión saliente con un peso
    public void agregarConexionSaliente(NodoPonderado<T> nodoDestino, int peso) {
        conexionesSalientes.put(nodoDestino.getId(), new Arista<>(nodoDestino, peso));
    }

    // Metodo para agregar una conexión entrante con un peso
    public void agregarConexionEntrante(NodoPonderado<T> nodoOrigen, int peso) {
        conexionesEntrantes.put(nodoOrigen.getId(), new Arista<>(nodoOrigen, peso));
    }

    // Obtener el peso de una conexión saliente
    public Integer obtenerPesoConexionSaliente(T idDestino) {
        Arista<T> arista = conexionesSalientes.get(idDestino);
        return (arista != null) ? arista.getPeso() : null;
    }

    // Obtener el peso de una conexión entrante
    public Integer obtenerPesoConexionEntrante(T idOrigen) {
        Arista<T> arista = conexionesEntrantes.get(idOrigen);
        return (arista != null) ? arista.getPeso() : null;
    }

    @Override
    public String toString() {
        return "NodoPonderado{" +
                "id=" + getId() +
                ", conexionesEntrantes=" + conexionesEntrantes.keySet() +
                ", conexionesSalientes=" + conexionesSalientes.keySet() +
                '}';
    }
}
