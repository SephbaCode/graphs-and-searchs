package firs_graph.model;
import java.util.*;

public class Graph {

    private Map <Integer, Node> nodes;

    // esto define la bidireccionalidad del grafo
    public  boolean isDirected = false;

    public Graph() {
        this.nodes = new HashMap<>();
    }

    public void addNode(int id) {
        this.nodes.putIfAbsent(id, new Node(id));
        // Si la clave (key) no existe en el HashMap, se inserta la clave con el valor (value)
        //Si la clave ya existe, NO se sobrescribe el valor asociado.
        //Devuelve el valor previamente asociado a la clave, o null si la clave no estaba en el mapa.
    }

    public void addLink(int id_from, int id_toward, int weigth) {
        Node nodeFrom = this.nodes.get(id_from);
        Node nodeToward = this.nodes.get(id_toward);

        if (nodeFrom != null && nodeToward != null) {
            nodeFrom.add_linked(nodeToward, weigth);
            if (!this.isDirected){
                // Esta se da por que el grafo es no dirigido
                nodeToward.add_linked(nodeFrom, weigth);
            }

        }
    }

    public void print_graph(){
        for (Node node : this.nodes.values()) {
            System.out.println(node.id + ": " + node.print_linked());
        }
    }




}
