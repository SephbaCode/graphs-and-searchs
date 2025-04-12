package weigth_graph;

public class EstadoCoste<T> {
    private NodoPonderado<T> nodo;
    private int coste;

    public EstadoCoste(NodoPonderado<T> nodo, int coste) {
        this.nodo = nodo;
        this.coste = coste;
    }

    public NodoPonderado<T> getNodo() {
        return nodo;
    }

    public int getCoste() {
        return coste;
    }
}
