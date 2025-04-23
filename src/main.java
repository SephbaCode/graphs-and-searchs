import Jugs_problem.JugsProblemGraph;
import generic.Analizador;
import weight_graph_v2.*;

import java.util.Arrays;
import java.util.List;

public class main {

    public static void main(String[] args) {

        //grafosCiegas();




        //grafoEuristico();
        //cargaGrafo();
        //probarAEstrella();
        GrafoPonderado grafo = new GrafoPonderado();

        // Crear nodos con sus valores heurísticos
        NodoPonderado nodoS0 = new NodoPonderado("S", 30);
        NodoPonderado nodoS1 = new NodoPonderado("d", 20);
        NodoPonderado nodoS2 = new NodoPonderado("f", 10);
        NodoPonderado nodoS3 = new NodoPonderado("m", 5);
        NodoPonderado nodoS4 = new NodoPonderado("g", 10);
        NodoPonderado nodoS5 = new NodoPonderado("c", 5);
        NodoPonderado nodoS6 = new NodoPonderado("b", 20);
        NodoPonderado nodoS7 = new NodoPonderado("h", 10);
        NodoPonderado nodoS8 = new NodoPonderado("e", 0);

        // Agregar nodos al grafo
        grafo.agregarNodo(nodoS0);
        grafo.agregarNodo(nodoS1);
        grafo.agregarNodo(nodoS2);
        grafo.agregarNodo(nodoS3);
        grafo.agregarNodo(nodoS4);
        grafo.agregarNodo(nodoS5);
        grafo.agregarNodo(nodoS6);
        grafo.agregarNodo(nodoS7);
        grafo.agregarNodo(nodoS8);

        // Conectar nodos con pesos (costes)
        grafo.conectar("S", "d", 5);    // OP1
        grafo.conectar("S", "m", 3);   // OP2
        grafo.conectar("S", "f", 4);   // OP3
        grafo.conectar("d", "h", 7);  // OP4
        grafo.conectar("d", "c", 6);   // OP5
        grafo.conectar("d", "m", 4);  // OP6
        grafo.conectar("m", "c", 3);   // OP7
        grafo.conectar("m", "b", 7);   // OP8
        grafo.conectar("m", "g", 4);   // OP9
        grafo.conectar("g", "b", 9);   // OP10
        grafo.conectar("g", "e", 12);   // OP10
        grafo.conectar("c", "h", 2);
        grafo.conectar("c", "e", 9);
        grafo.conectar("c", "b", 5);
        grafo.conectar("b", "e", 6);
        grafo.conectar("b", "c", 2);   // OP10

        //busquedas a ciegas
        System.out.println("=======================================================================================");
        System.out.println("Busqueda por amplitud");
        Analizador.busquedaAmplitud(grafo, "S", "e");
        System.out.println("=======================================================================================");
        System.out.println("Busqueda por profundidad");
        Analizador.busquedaProfundidad(grafo, "S", "e");
        System.out.println("=======================================================================================");
        System.out.println("Busqueda por profundidad iterativa");
        Analizador.busquedaPorNivelesDeProfundidad(grafo, "S", "e", 7);
        System.out.println("=======================================================================================");
        System.out.println("Busqueda bidireccional");
        Analizador.busquedaBidireccional(grafo, "S", "e");
        System.out.println("=======================================================================================");
        System.out.println("Busqueda de costo uniforme");
        List<String> camino = EuristicSearchs.busquedaCostoUniforme(grafo, "S", "e");
        System.out.println("Camino más corto: " + camino);


        // Realizar búsqueda heurística
        System.out.println("=======================================================================================");
        System.out.println("Busqueda por gradiente");
        EuristicSearchs.BusquedaGradiente(grafo, "S", "e", "min");
        System.out.println("=======================================================================================");
        System.out.println("Busqueda por Primero el mejor");
        EuristicSearchs.BusquedaPrimeroElMejor(grafo, "S", "e", "min");
        System.out.println("=======================================================================================");
        System.out.println("Busqueda A*");
        EuristicSearchs.BusquedaAEstrella(grafo, "S", "e");
        System.out.println("=======================================================================================");









    }

    private static void grafosCiegas ( ) {
        JugsProblemGraph jugsProblemGraph = new JugsProblemGraph();

        jugsProblemGraph.generate_graph();

        // Definimos el nodo inicial y el objetivo
        List<Integer> estadoInicial = Arrays.asList(0, 0);
        List<Integer> estadoObjetivo = Arrays.asList(4, 0);

        System.out.println("=======================================================================================");
        Analizador.busquedaAmplitud(jugsProblemGraph, estadoInicial, estadoObjetivo);
        System.out.println("=======================================================================================");
        Analizador.busquedaProfundidad(jugsProblemGraph, estadoInicial, estadoObjetivo);
        System.out.println("=======================================================================================");
        Analizador.busquedaBidireccional(jugsProblemGraph, estadoInicial, estadoObjetivo);
        System.out.println("=======================================================================================");
        Analizador.busquedaPorNivelesDeProfundidad(jugsProblemGraph, estadoInicial, estadoObjetivo,7);
    }

    private static void grafoEuristico ( ) {
        GrafoPonderado grafo = new GrafoPonderado();

        // Crear nodos
        NodoPonderado nodoA = new NodoPonderado("A", 0);
        NodoPonderado nodoB = new NodoPonderado("B", 1);
        NodoPonderado nodoC = new NodoPonderado("C", 2);
        NodoPonderado nodoD = new NodoPonderado("D", 3);
        NodoPonderado nodoE = new NodoPonderado("E", 4);

        // Agregar nodos al grafo
        grafo.agregarNodo(nodoA);
        grafo.agregarNodo(nodoB);
        grafo.agregarNodo(nodoC);
        grafo.agregarNodo(nodoD);
        grafo.agregarNodo(nodoE);

        // Conectar nodos con pesos
        grafo.conectar("A", "B", 1);
        grafo.conectar("A", "C", 4);
        grafo.conectar("B", "C", 2);
        grafo.conectar("B", "D", 5);
        grafo.conectar("C", "D", 1);
        grafo.conectar("D", "E", 3);

        // Imprimir el grafo
        System.out.println(grafo.toString());

        // Realizar búsqueda heurística
        System.out.println("=======================================================================================");
        System.out.println("Busqueda por gradiente");
        EuristicSearchs.BusquedaGradiente(grafo, "A", "E", "min");

    }

    private static void cargaGrafo(){
        GrafoPonderado grafo = new GrafoPonderado();
        String archivo = "src/data/grafo_500_nodos.csv";
        grafo.cargarGrafo500DesdeCSV(archivo);

        //System.out.println(grafo);

        // Realizar búsqueda heurística
        System.out.println("=======================================================================================");
        System.out.println("Busqueda por gradiente: minimo valor euristico");
        EuristicSearchs.BusquedaGradiente(grafo, "N112", null, "min");
        System.out.println("=======================================================================================");
        //eliminar este campo si debe detenerse cuando la mejor opcion sea un ciclo
        System.out.println("Busqueda por gradiente: maximo valor euristico Evade ciclos");
        //EuristicSearchs.BusquedaGradienteIgnoraCiclos(grafo, "N112", "N265", "max");
        System.out.println("=======================================================================================");
        System.out.println("Busqueda por Primero el mejor");
        EuristicSearchs.BusquedaPrimeroElMejor(grafo, "N112", "N265", "min");


    }

    private static void probarAEstrella () {
        GrafoPonderado grafo = new GrafoPonderado();

        // Crear nodos con sus valores heurísticos
        NodoPonderado nodoS0 = new NodoPonderado("S", 30);
        NodoPonderado nodoS1 = new NodoPonderado("d", 20);
        NodoPonderado nodoS2 = new NodoPonderado("f", 10);
        NodoPonderado nodoS3 = new NodoPonderado("m", 5);
        NodoPonderado nodoS4 = new NodoPonderado("g", 10);
        NodoPonderado nodoS5 = new NodoPonderado("c", 5);
        NodoPonderado nodoS6 = new NodoPonderado("b", 20);
        NodoPonderado nodoS7 = new NodoPonderado("h", 10);
        NodoPonderado nodoS8 = new NodoPonderado("e", 0);

        // Agregar nodos al grafo
        grafo.agregarNodo(nodoS0);
        grafo.agregarNodo(nodoS1);
        grafo.agregarNodo(nodoS2);
        grafo.agregarNodo(nodoS3);
        grafo.agregarNodo(nodoS4);
        grafo.agregarNodo(nodoS5);
        grafo.agregarNodo(nodoS6);
        grafo.agregarNodo(nodoS7);
        grafo.agregarNodo(nodoS8);

        // Conectar nodos con pesos (costes)
        grafo.conectar("S", "d", 5);    // OP1
        grafo.conectar("S", "m", 3);   // OP2
        grafo.conectar("S", "f", 4);   // OP3
        grafo.conectar("d", "h", 7);  // OP4
        grafo.conectar("d", "c", 6);   // OP5
        grafo.conectar("d", "m", 4);  // OP6
        grafo.conectar("m", "c", 3);   // OP7
        grafo.conectar("m", "b", 7);   // OP8
        grafo.conectar("m", "g", 4);   // OP9
        grafo.conectar("g", "b", 9);   // OP10
        grafo.conectar("g", "e", 12);   // OP10
        grafo.conectar("c", "h", 2);
        grafo.conectar("c", "e", 9);
        grafo.conectar("c", "b", 5);
        grafo.conectar("b", "e", 6);
        grafo.conectar("b", "c", 2);   // OP10
        // OP10
        // OP10
        // OP10
        // OP10

        EuristicSearchs.BusquedaAEstrella(grafo, "S", "e");
    }


}
