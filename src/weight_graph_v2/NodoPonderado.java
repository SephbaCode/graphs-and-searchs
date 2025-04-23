package weight_graph_v2;

import generic.Nodo;

import java.util.Map;

public class NodoPonderado extends Nodo<String> {

    private int ValorEuristico;
    private Map<String, Integer> conexionesValores;


    public NodoPonderado(String id, int ValorEuristico) {
        super(id);
        this.ValorEuristico = ValorEuristico;
        this.conexionesValores = new java.util.LinkedHashMap<>();
    }

    public void agregarConexionSaliente(NodoPonderado nodo, Integer peso_arista) {

        super.agregarConexionSaliente(nodo);

        this.conexionesValores.putIfAbsent(nodo.getId(), peso_arista);
    }

    public int getValorEuristico() {
        return ValorEuristico;
    }
    public void setHeuristica(int ValorEuristico) {
        this.ValorEuristico = ValorEuristico;
    }

    public int getValorConexion(String idDestino) {
        return conexionesValores.getOrDefault(idDestino, -1);
    }

    public void setValorEuristico(int ValorEuristico) {
        this.ValorEuristico = ValorEuristico;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Nodo ")
                .append("id=").append(getId()).append('\n')
                .append("ValorEuristico=").append(ValorEuristico).append('\n')
                .append(conexionesToString());
        sb.append("\n");
        return sb.toString();
    }

    private String conexionesToString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : conexionesValores.entrySet()) {
            sb.append("Destino: ").append(entry.getKey()).append(", Peso: ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    public Map<String, Integer> obtenerValoresConexiones() {
        return conexionesValores;
    }

}
