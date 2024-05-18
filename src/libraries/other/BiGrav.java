package libraries.other;

import java.util.ArrayList;

public class BiGrav<VERTEX, EDGE> extends ArrayList<BiGrav.BiVertex<VERTEX, EDGE>> {
    public void addVertex(VERTEX value) {
        add(new BiVertex(value));
    }

    public void addEdge(int from, int to, EDGE value) {
        get(from).edges.add(new BiEdge(to, value));
        get(to).edges.add(new BiEdge(from, value));
    }
    public void put(int eFrom, int eTo, int vertex, EDGE v1, EDGE v2) {
        for (int i = 0; i < get(eFrom).edges.size(); i++) {
            if (get(eFrom).edges.get(i).to == eTo) {
                get(eFrom).edges.get(i).to = vertex;
                get(vertex).edges.add(new BiEdge<>(eFrom, v1));
                break;
            }
        }

        for (int i = 0; i < get(eTo).edges.size(); i++) {
            if (get(eTo).edges.get(i).to == eFrom) {
                get(eTo).edges.get(i).to = vertex;
                get(vertex).edges.add(new BiEdge<>(eTo, v2));
                break;
            }
        }
    }
    public void deleteEdge(int from, int to) {
        for (int i = 0; i < get(from).edges.size(); i++) {
            if (get(from).edges.get(i).to == to) {
                get(from).edges.remove(i);
                break;
            }
        }
        for (int i = 0; i < get(to).edges.size(); i++) {
            if (get(to).edges.get(i).to == from) {
                get(to).edges.remove(i);
                break;
            }
        }
    }

    public ArrayList<ArrayList<Integer>> findCycles(int vertex) {
        ArrayList<ArrayList<Integer>> cycles = new ArrayList<>();

        ArrayList<Integer> revisionNumbers = new ArrayList<>();

        for (int i = 0; i < get(vertex).edges.size(); i++) {
            if (!revisionNumbers.contains(get(vertex).edges.get(i).to)) {
                ArrayList<ArrayList<Integer>> cyclesTmp = findAllWays(get(vertex).edges.get(i).to, vertex);
                for (ArrayList<Integer> way : cyclesTmp) {
                    if (way.size() > 2) {
                        cycles.add(way);
                        revisionNumbers.add(way.get(1));
                    }
                }
            }
        }

        return cycles;
    }
    public ArrayList<ArrayList<Integer>> findAllWays(int from, int to) {
        ArrayList<ArrayList<Integer>> ways = findAllWays(from, to, new ArrayList<>(size()));

        return ways;
    }
    private ArrayList<ArrayList<Integer>> findAllWays(int from, int to, ArrayList<Integer> way) {
        ArrayList<ArrayList<Integer>> ways = new ArrayList<>();
        if (from == to) {
            ways.add(new ArrayList<>());
            ways.get(0).add(from);
            return ways;
        }
        way.add(from);

        for (var edge : get(from).edges) {
            if (!way.contains(edge.to)) {
                ArrayList<ArrayList<Integer>> tmp = findAllWays(edge.to, to, new ArrayList<>(way));
                if (!tmp.isEmpty()) {
                    for (var w : tmp) {
                        w.add(from);
                    }
                    ways.addAll(tmp);
                }
            }
        }
        return ways;
    }

    public static class BiVertex<V, E> {
        public ArrayList<BiEdge<E>> edges = new ArrayList<>();

        public V value;

        public BiVertex(V value) {
            this.value = value;
        }
    }

    public static class BiEdge<V> {
        public int to;
        public V value;

        public BiEdge(int to, V value) {
            this.to = to;
            this.value = value;
        }
    }
}

/*
public class Grav<vertex, edge> extends ArrayList<Grav<vertex, edge>.Vertex>{
    public int addVertex(vertex value){
        add(new Vertex(value));
        return size() - 1;
    }
    public void addEdge(int from, int to, edge value){
        Edge edge = new Edge(from, to, value);
        get(from).to.add(edge);
        get(to).from.add(edge);
    }

    public ArrayList<Way> findCyclesDirected(){
        ArrayList<Way> cycles = new ArrayList<>();
        for (Vertex v : this){
            WaysSearcher ws = new WaysSearcher();
            for (Edge edge : v.to) {
                Way cycle = ws.findDirectedWay(edge.to, edge.from, (a, b) -> 0);
                if (cycle != null)
                    cycles.add(cycle);
            }
        }
        return cycles;
    }

    public void disuniteVertexes(Edge oldEdge, int disunitingVertex, edge v1, edge v2){
        disuniteEdges(oldEdge, new Edge(oldEdge.from, disunitingVertex, v1), new Edge(disunitingVertex, oldEdge.to, v2));
    }

    private void disuniteEdges(Edge oldEdge, Edge newEdge1, Edge newEdge2){
        for (int i = 0; i < get(oldEdge.from).to.size(); i++){
            if (get(oldEdge.from).to.get(i) == oldEdge) {
                get(oldEdge.from).to.set(i, newEdge1);
                get(newEdge1.to).from.add(newEdge1);
                break;
            }
        }
        for (int i = 0; i < get(oldEdge.to).from.size(); i++){
            if (get(oldEdge.to).from.get(i) == oldEdge) {
                get(oldEdge.to).from.set(i, newEdge2);
                get(newEdge1.to).from.add(newEdge1);
                break;
            }
        }
    }

    public class Vertex{
        public ArrayList<Edge> to = new ArrayList<>(), from = new ArrayList<>();
        public vertex value;

        public Edge getHeadEdge(){
            return to.get(0);
        }

        private Vertex(vertex aValue){
            value = aValue;
        }
    }
    public class Edge{
        public int from, to;
        public edge value;

        Edge(int aFrom, int aTo, edge aValue){
            from = aFrom;
            to = aTo;
            value = aValue;
        }
    }

    // don't work in multi thread;
    public class WaysSearcher {
        private boolean[] completed;
        private Comparator<Edge> comparator;

        public Way findDirectedWay(int from, int to, Comparator<Edge> comparator) {
            completed = new boolean[size()];
            this.comparator = comparator;
            Way way = continueDirectedSearch(from, to);
            if (way.isEmpty())
                return null;
            return way;
        }
        public Way findUndirectedWay(int from, int to, Comparator<Edge> comparator) {
            completed = new boolean[size()];
            this.comparator = comparator;
            Way way = continueUndirectedSearch(from, to);
            if (way.isEmpty())
                return null;
            return way;
        }

        private Way continueDirectedSearch(int from, int to){
            completed[from] = true;
            Way way = new Way(); way.add(from);
            if (from == to)
                return way;
            ArrayList<Edge> sortedList = new ArrayList<>(get(from).to);
            sortedList.sort(comparator);
            for (Edge edge : sortedList){
                if (completed[edge.to]) {
                    if (way.addAll(continueDirectedSearch(edge.to, to))) {
                        return way;
                    }
                }
            }
            return new Way();
        }
        private Way continueUndirectedSearch(int from, int to){
            completed[from] = true;
            Way way = new Way(); way.add(from);
            if (from == to)
                return way;
            ArrayList<Edge> sortedList = new ArrayList<>(get(from).to);
            sortedList.addAll(get(from).from);
            sortedList.sort(comparator);
            for (Edge edge : sortedList){
                if (!completed[edge.to]) {
                    if (way.addAll(continueUndirectedSearch(edge.to, to))) {
                        return way;
                    }
                }
                if (!completed[edge.from]) {
                    if (way.addAll(continueUndirectedSearch(edge.from, to))) {
                        return way;
                    }
                }
            }
            return new Way();
        }
    }

    public class Way {
        public ArrayList<vertex> values = new ArrayList<>();
        public ArrayList<Integer> indexes = new ArrayList<>();

        public void add(int index){
            indexes.add(index);
            values.add(get(index).value);
        }
        public boolean addAll(Way w){
            return indexes.addAll(w.indexes) && values.addAll(w.values);
        }
        public boolean isEmpty(){
            return indexes.isEmpty();
        }
    }
}
*/