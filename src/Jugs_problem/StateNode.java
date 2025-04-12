package Jugs_problem;

import generic.Nodo;

import java.util.*;

public class StateNode extends Nodo<List<Integer>> {

    private final String description;

    public StateNode(String description, int jug1, int jug2) {
        super(Arrays.asList(jug1, jug2));  // Usamos esto como el ID del nodo
        this.description = description;
    }

    //Las funciones para agregar una conexión entrante y saliente son heredadas de la clase Nodo


    public int get_jug(int jug) {
        return getId().get(jug);
    }
    @Override
    public String toString() {
        return getId().toString();
    }
}
