import javax.swing.*;
import java.awt.*;

public class Solver {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setSize(560, 660);
        window.setBackground(Color.WHITE);
        window.setVisible(true);
        Cube cube = new Cube(3);
        window.add(cube).setVisible(true);
        cube.solve();
        /*
        cube.showCube();
        System.out.println("AFTER RIGHT ROTATION");
        //cube.shuffle();
        cube.doShuf(26722221);
        cube.showCube();
        cube.makeCross();
        System.out.println("AFTER CROSS");
        cube.showCube();
        System.out.println("AFTER CORNERS");
        cube.makeUpCorners();
        cube.showCube();
        System.out.println("AFTER O2");
        cube.makeO2();
        cube.showCube();
        System.out.println("AFTER YELLOW CROSS");
        cube.makeYellowCross();
        cube.showCube();
        System.out.println("AFTER SOLVE");
        cube.makeDownCorners();
        cube.showCube();
        */
    }
}