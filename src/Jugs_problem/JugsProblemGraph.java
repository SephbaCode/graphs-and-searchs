package Jugs_problem;

import generic.Grafo;
import java.util.*;

public class JugsProblemGraph extends Grafo<List<Integer>> {


    public JugsProblemGraph() {
        super();
    }


    public void generate_graph() {
        this.limpiar();

        // query servira para generar el grafo similar al metodo de busqueda por amplitud
        Queue<StateNode> query = new LinkedList<>();

        // nodo inicial
        StateNode initial_node = new StateNode("nodo_inicial", 0, 0);
        this.agregarNodo(initial_node);
        query.add(initial_node);

        while (!query.isEmpty()) {

            StateNode current_node = query.peek();
            System.out.println("El nodo actual es: " + current_node.getId().toString());

            // Operaciones comunes (llenar, vaciar, mover contenido)
            executeOperation(query, current_node, 0, 'a');
            executeOperation(query, current_node, 1, 'b');
            executeOperation(query, current_node, 0, 'c'); // Vaciar jarra 0
            executeOperation(query, current_node, 1, 'd'); // Vaciar jarra 1
            executeOperation(query, current_node, 0, 'e'); // Mover contenido de jarra 0 a jarra 1
            executeOperation(query, current_node, 1, 'f'); // Mover contenido de jarra 1 a jarra 0

            // Eliminar el nodo actual de la cola
            query.poll();
        }
    }

    // Metodo refactorizado para ejecutar las operaciones
    private void executeOperation(Queue<StateNode> query, StateNode current_node, int jarra, char operacion) {
        List<Integer> key = this.getKeyForOperation(current_node, jarra, operacion); // Obtener la clave de la operación
        if (!this.contieneNodo(key)) {  // Si el nodo no existe
            StateNode newNode = new StateNode(String.valueOf(operacion), key.get(0), key.get(1));
            this.agregarNodo(newNode);
            System.out.println("    Se creo el nodo: " + newNode);
            query.add(newNode);
        }
        if (!current_node.getId().equals(key)) { // Compara correctamente los IDs
            this.conectar(current_node.getId(), key);
            System.out.println("        Se conecto con el nodo: " + key);
        }
    }

    // Metodo para obtener la clave según la operación
    private List<Integer> getKeyForOperation(StateNode current_node, int jarra, char operacion) {
        switch (operacion) {
            case 'a': // Llenar jarra 0
                return this.fill_jug(current_node, jarra);
            case 'b': // Llenar jarra 1
                return this.fill_jug(current_node, jarra);
            case 'c': // Vaciar jarra 0
                return this.empty_jug(current_node, jarra);
            case 'd': // Vaciar jarra 1
                return this.empty_jug(current_node, jarra);
            case 'e': // Mover contenido de jarra 0 a jarra 1
                return this.move_content(current_node, jarra);
            case 'f': // Mover contenido de jarra 1 a jarra 0
                return this.move_content(current_node, jarra);
            default:
                throw new IllegalArgumentException("Operación no reconocida");
        }
    }


    // Esta funcion representa la accion de llenar alguna jarra
    private List<Integer> fill_jug(StateNode current_node, int jug_number) {

        int jug_cero = current_node.get_jug(0);
        int jug_one = current_node.get_jug(1);

        if (jug_number == 0) {
            jug_cero = 5;
        } else if (jug_number == 1) {
            jug_one = 3;
        }

        return Arrays.asList(jug_cero, jug_one);
    }

    private List<Integer> empty_jug(StateNode current_node, int jug_number) {

        int jug_cero = current_node.get_jug(0);
        int jug_one = current_node.get_jug(1);

        if (jug_number == 0) {
            jug_cero = 0;
        } else if (jug_number == 1) {
            jug_one = 0;
        }

        return Arrays.asList(jug_cero, jug_one);
    }

    private List<Integer> move_content(StateNode current_node, int type_of_movement) {

        // type_of_movement = 0 -> de jarra 0 a jarra 1
        // type_of_movement = 1 -> de jarra 1 a jarra 0

        int jug_cero = current_node.get_jug(0);
        int jug_one = current_node.get_jug(1);


        if (type_of_movement == 0) {
            if (jug_cero + jug_one > 3) {  // si la suma de las dos jarras es mayor a 3
                jug_cero = jug_cero - (3 - jug_one);
                jug_one = 3;
            } else {                        // si la suma de las dos jarras es menor a 3
                jug_one = jug_cero + jug_one;
                jug_cero = 0;
            }
        } else if (type_of_movement == 1) {
            if (jug_cero + jug_one > 5) {  // si se supera la capacidad de la jarra 0
                jug_one = jug_one - (5 - jug_cero);
                jug_cero = 5;
            } else {
                jug_cero = jug_cero + jug_one;
                jug_one = 0;
            }
        }
        return Arrays.asList(jug_cero, jug_one);
    }


}
