package libraries.other;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class GravTest {

    @Test
    public void findAllWays() {
        BiGrav<Integer, Integer> grav = new BiGrav<>();
        ArrayList<ArrayList<Integer>> ways;

        grav.addVertex(0);
        for (int i = 1; i < 10; i++) {
            grav.addVertex(i);
            grav.addEdge(i - 1, i, i);
        }

        grav.addEdge(0, 3, 10);
        grav.addEdge(3, 5, 11);

        grav.addEdge(0, 9, 12);
        // test 1
        ways = grav.findAllWays(0, 9);
        for (var way : ways) {
            System.out.print("Way: ");
            for (var lmn : way) {
                System.out.print(lmn + " ");
            }
            System.out.println();
        }
    }

    @Test
    public void cyclesTest() {
        BiGrav<Integer,Integer> grav;
        ArrayList<ArrayList<Integer>> cycles;

        boolean right;

        // Test 1
        grav = new BiGrav<>();
        for (int i = 0; i < 10; i++) {
            grav.addVertex(i);
        }
        for (int i = 1; i < 10; i++) {
            grav.addEdge(i, i - 1, i);
        }
        grav.addEdge(9, 0, 10);

        grav.addVertex(10);
        grav.addEdge(0, 10, 11);
        grav.addEdge(10, 2, 12);

        cycles = grav.findCycles(0);
        Assert.assertTrue(cycles.size() == 2);
    }
}
