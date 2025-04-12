package firs_graph.model;

import java.util.HashMap;
import java.util.Map;

public class Node {

    public int id;
    private Map<Node, Integer> linked;

    public Node(int id) {
        this.id = id;
        this.linked = new HashMap<>();
    }

    public void add_linked(Node new_linked_node, int weight) {
        // solo se cree un enlace con un nodso si no existe un enlace existente
        if (!this.linked.containsKey(new_linked_node)) {
            this.linked.put(new_linked_node, weight);
        }
    }


    public String print_linked() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nodo ").append(this.id).append(" está ligado a:\n");

        for (Map.Entry<Node, Integer> entry : linked.entrySet()) {
            sb.append(" → Nodo ").append(entry.getKey().id)
                    .append(" con peso ").append(entry.getValue())
                    .append("\n");
        }

        return sb.toString();
    }

}
