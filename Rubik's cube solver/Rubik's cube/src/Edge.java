import java.util.ArrayList;

public class Edge {
    private ArrayList<ArrayList<Pixel>> pixels = new ArrayList<>();
    private final int dimension;

    public Edge(int dimension, String color) {
        this.dimension = dimension;

        for (int row = 0; row < dimension; row++) {
            ArrayList<Pixel> tmp = new ArrayList<>(); // нужен сугубо для заполнения аррейлиста

            for (int column = 0; column < dimension; column++) {
                tmp.add(new Pixel(color));
            }

            pixels.add(tmp);
        }
    }

    public ArrayList<ArrayList<Pixel>> getPixels() {
        return pixels;
    }

    /*
        Вспомогательный метод для копирования содержимого из одного листа в другой
    */
    public void changePixels(ArrayList<ArrayList<String>> changedPixels) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                pixels.get(i).get(j).setColor(changedPixels.get(i).get(j));
            }
        }
    }

    /*
        Метод для вывода содержимого ребра в консоль
     */
    public void showEdge(String name) { // метод, призванный упростить вывод кубика / избежать повторение кода
        System.out.println("Current edge: " + name);

        for (int i = 0; i < dimension; i++) {
            for (Pixel j : pixels.get(i)) {
                System.out.print(j.getColor() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}