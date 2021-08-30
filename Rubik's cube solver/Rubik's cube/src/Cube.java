import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Cube extends JPanel {
    final int dimension; // размерность куба - 3х3

    final private Edge front; // F have green color
    final private Edge up; // U have white color
    final private Edge back; // B have blue color
    final private Edge down; // D have yellow color
    final private Edge left; // L have orange color
    final private Edge right; // R have red color

    public Cube(int dimension) {
        this.dimension = dimension;

        front = new Edge(dimension, "G");
        up = new Edge(dimension, "W");
        back = new Edge(dimension, "B");
        down = new Edge(dimension, "Y");
        left = new Edge(dimension, "O");
        right = new Edge(dimension, "R");
    }

    private void paintEdge(Graphics2D g, int startX, int startY, int rectSize, Edge edge) {
        int x = startX;
        int y = startY;

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                if (edge.getPixels().get(row).get(column).getColor().equals("G")) {
                    g.setColor(Color.GREEN);
                }
                if (edge.getPixels().get(row).get(column).getColor().equals("W")) {
                    g.setColor(Color.WHITE);
                }
                if (edge.getPixels().get(row).get(column).getColor().equals("B")) {
                    g.setColor(Color.BLUE);
                }
                if (edge.getPixels().get(row).get(column).getColor().equals("O")) {
                    g.setColor(new Color(255, 137, 0));
                }
                if (edge.getPixels().get(row).get(column).getColor().equals("Y")) {
                    g.setColor(Color.YELLOW);
                }
                if (edge.getPixels().get(row).get(column).getColor().equals("R")) {
                    g.setColor(Color.RED);
                }

                g.fillRect(x, y, rectSize, rectSize);

                if (column != 2 && column != 5) {
                    x += rectSize;
                } else {
                    x = startX;
                    y += rectSize;
                }
            }
            g.setColor(Color.BLACK);
        }
        g.drawRect(startX, startY, rectSize*3, rectSize*3);
        // длинные слева направо
        g.drawLine(100, 240, 460, 240);
        g.drawLine(100, 280, 460, 280);
        // короткие слева направо
        g.drawLine(220, 120, 340, 120);
        g.drawLine(220, 160, 340, 160);
        g.drawLine(220, 360, 340, 360);
        g.drawLine(220, 400, 340, 400);
        g.drawLine(220, 480, 340, 480);
        g.drawLine(220, 520, 340, 520);
        // короткие сверху вниз
        g.drawLine(140, 200, 140, 320);
        g.drawLine(180, 200, 180, 320);
        g.drawLine(380, 200, 380, 320);
        g.drawLine(420, 200, 420, 320);
        // длинные сверху вниз
        g.drawLine(260, 80, 260, 560);
        g.drawLine(300, 80, 300, 560);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g1 = (Graphics2D) g;
        g1.setStroke(new BasicStroke(2));

        paintEdge(g1, 100, 200, 40, right);
        paintEdge(g1, 220, 80, 40, front);
        paintEdge(g1, 220, 200, 40, up);
        paintEdge(g1, 340, 200, 40, left);
        paintEdge(g1, 220, 320, 40, back);
        paintEdge(g1, 220, 440, 40, down);

    }

    /*
            Т.к изначально кубик в идеальном положении, этот метод будет его "тасовать"
        */
    public void doShuf(int shuf) {
        for (;shuf > 0; shuf /= 10) {
            switch (shuf % 10) {
                case 0:
                    this.rotateR(0);
                    break;
                case 1:
                    this.rotateL(0);
                    break;
                case 2:
                    this.rotateB(0);
                    break;
                case 3:
                    this.rotateD(0);
                    break;
                case 4:
                    this.rotateF(0);
                    break;
                case 5:
                    this.rotateU(0);
                    break;
                case 6:
                    this.rotateU(0);
                    this.rotateD(0);
                    break;
                case 7:
                    this.rotateR(0);
                    this.rotateL(0);
                    break;
                case 8:
                    this.rotateF(0);
                    this.rotateB(0);
                    break;
                case 9:
                    this.rotateU(0);
                    this.rotateD(0);
                    this.rotateR(0);
                    this.rotateL(0);
                    this.rotateF(0);
                    this.rotateB(0);
                    break;
            }
        }
    }
    public void shuffle() {
        int a = 10000000;
        int b = 99999999;
        int shuf = a + (int)(Math.random() * b);
        //System.out.println(shuf);
        doShuf(shuf);
    }

    /*
        Метод ниже осуществляет поворот(по часовой/против) внутри самой грани
        я не стал его вносить в класс ребра, потому что повороты(какие бы они ни были)
        должны быть привязаны к кубу!
     */

    public ArrayList<ArrayList<String>> beginRotations(Edge edge, int direction) { // вспомогательный метод для изменения положения квадратов внутри самой грани
        ArrayList<ArrayList<String>> changed = new ArrayList<>();
        ArrayList<String> tmp = new ArrayList<>();

        if (direction == 1) {
            for (int i = 0; i < 3; i++) {
                for (int j = 2; j > -1; j--) {
                    tmp.add(edge.getPixels().get(j).get(i).getColor());
                }
                changed.add((ArrayList<String>)tmp.clone());
                tmp.clear();
            }
        } else {
            for (int i = 2; i > -1; i--) {
                for (int j = 0; j < 3; j++) {
                    tmp.add(edge.getPixels().get(j).get(i).getColor());
                }
                changed.add((ArrayList<String>)tmp.clone());
                tmp.clear();
            }
        }
        return changed;
    }

    /*
        Метод ниже(плюс его перегрузка) - обобщение поворота правой и левой грани.
        Поскольку у них виднеются явные общие моменты при повороте, решено было вынести в отдельный метод
        для уменьшения говнокода + лучшей читаемости
    */

    private void rightAndLeftRotations(Edge currEdge, Edge colorEdge, int index) {
        currEdge.getPixels().get(0).get(index).setColor(colorEdge.getPixels().get(0).get(index).getColor());
        currEdge.getPixels().get(1).get(index).setColor(colorEdge.getPixels().get(1).get(index).getColor());
        currEdge.getPixels().get(2).get(index).setColor(colorEdge.getPixels().get(2).get(index).getColor());
    }

    private void rightAndLeftRotations(Edge currEdge, ArrayList<String> colorArr, int index) {
        currEdge.getPixels().get(0).get(index).setColor(colorArr.get(0));
        currEdge.getPixels().get(1).get(index).setColor(colorArr.get(1));
        currEdge.getPixels().get(2).get(index).setColor(colorArr.get(2));
    }

    /*
        Здесь представлены методы для поворрота граней
        Всего можно будет повернуть 9 граней, каждую по часовой и против: (1 - по часовой, 0 - против часовой)
            R - правая грань:
            L - левая грань
            U - передняя грань
            D - нижняя грань
            F - передняя грань
            B - задняя грань
            M - середина между правой и левой гранями(повороты как у L)
            S - середина между передней и задней гранями(повороты как у F)
            E - середина между верхней и нижней гранями(повороты как у нижней грани)
    */

    public void edgeRepaintWithSleep() {
        repaint();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void rotateR(int direction) { // поворот правой грани
        right.changePixels((ArrayList<ArrayList<String>>) this.beginRotations(right, direction).clone());

        ArrayList<String> tmp = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tmp.add(front.getPixels().get(i).get(0).getColor());
        }

        if (direction == 1) { // clockwise rotation R
            rightAndLeftRotations(front, down, 0);
            rightAndLeftRotations(down, back, 0);
            rightAndLeftRotations(back, up, 0);
            rightAndLeftRotations(up, tmp, 0);
        } else { // counter clockwise rotation R
            rightAndLeftRotations(front, up, 0);
            rightAndLeftRotations(up, back,  0);
            rightAndLeftRotations(back, down,  0);
            rightAndLeftRotations(down, tmp,  0);
        }
        edgeRepaintWithSleep();
    }

    public void rotateL(int direction) { // поворот левой грани
        left.changePixels((ArrayList<ArrayList<String>>) this.beginRotations(left, direction).clone());

        ArrayList<String> tmp = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tmp.add(front.getPixels().get(i).get(2).getColor());
        }

        if (direction == 1) { // clockwise rotation R
            rightAndLeftRotations(front, up, 2);
            rightAndLeftRotations(up, back, 2);
            rightAndLeftRotations(back, down, 2);
            rightAndLeftRotations(down, tmp, 2);
        } else { // counter clockwise rotation R
            rightAndLeftRotations(front, down, 2);
            rightAndLeftRotations(down, back, 2);
            rightAndLeftRotations(back, up, 2);
            rightAndLeftRotations(up, tmp, 2);
        }
        edgeRepaintWithSleep();
    }

    public void rotateU(int direction) {
        up.changePixels((ArrayList<ArrayList<String>>) this.beginRotations(up, direction).clone());

        ArrayList<String> tmp = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tmp.add(front.getPixels().get(2).get(i).getColor());
        }

        if (direction == 1) { // ну это жесть говнокод пошел, есть идеи, как вынести в отдельный метод, но имхо он сам по себе будет громоздкий + я запутаюсь)
            front.getPixels().get(2).get(0).setColor(right.getPixels().get(2).get(2).getColor());
            front.getPixels().get(2).get(1).setColor(right.getPixels().get(1).get(2).getColor());
            front.getPixels().get(2).get(2).setColor(right.getPixels().get(0).get(2).getColor());

            right.getPixels().get(0).get(2).setColor(back.getPixels().get(0).get(0).getColor());
            right.getPixels().get(1).get(2).setColor(back.getPixels().get(0).get(1).getColor());
            right.getPixels().get(2).get(2).setColor(back.getPixels().get(0).get(2).getColor());

            back.getPixels().get(0).get(0).setColor(left.getPixels().get(2).get(0).getColor());
            back.getPixels().get(0).get(1).setColor(left.getPixels().get(1).get(0).getColor());
            back.getPixels().get(0).get(2).setColor(left.getPixels().get(0).get(0).getColor());

            left.getPixels().get(0).get(0).setColor(tmp.get(0));
            left.getPixels().get(1).get(0).setColor(tmp.get(1));
            left.getPixels().get(2).get(0).setColor(tmp.get(2));
        } else { // альтернативный говнокод
            front.getPixels().get(2).get(0).setColor(left.getPixels().get(0).get(0).getColor()); /// !!!!!!!!!!!!!!!!!!!!
            front.getPixels().get(2).get(1).setColor(left.getPixels().get(1).get(0).getColor());
            front.getPixels().get(2).get(2).setColor(left.getPixels().get(2).get(0).getColor());

            left.getPixels().get(0).get(0).setColor(back.getPixels().get(0).get(2).getColor());
            left.getPixels().get(1).get(0).setColor(back.getPixels().get(0).get(1).getColor());
            left.getPixels().get(2).get(0).setColor(back.getPixels().get(0).get(0).getColor());

            back.getPixels().get(0).get(0).setColor(right.getPixels().get(0).get(2).getColor());
            back.getPixels().get(0).get(1).setColor(right.getPixels().get(1).get(2).getColor());
            back.getPixels().get(0).get(2).setColor(right.getPixels().get(2).get(2).getColor());

            right.getPixels().get(0).get(2).setColor(tmp.get(2));
            right.getPixels().get(1).get(2).setColor(tmp.get(1));
            right.getPixels().get(2).get(2).setColor(tmp.get(0));
        }
        edgeRepaintWithSleep();
    }

    public void rotateD(int direction) {
        down.changePixels((ArrayList<ArrayList<String>>) this.beginRotations(down, direction).clone());

        ArrayList<String> tmp = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tmp.add(front.getPixels().get(0).get(i).getColor());
        }

        if (direction == 1) { // продолжаю говнокодить, мб, когда-нибудь вытащу в отдельный метод, но уже на релизе программы)))
            front.getPixels().get(0).get(0).setColor(left.getPixels().get(0).get(2).getColor());
            front.getPixels().get(0).get(1).setColor(left.getPixels().get(1).get(2).getColor());
            front.getPixels().get(0).get(2).setColor(left.getPixels().get(2).get(2).getColor());

            left.getPixels().get(0).get(2).setColor(back.getPixels().get(2).get(2).getColor());
            left.getPixels().get(1).get(2).setColor(back.getPixels().get(2).get(1).getColor());
            left.getPixels().get(2).get(2).setColor(back.getPixels().get(2).get(0).getColor());

            back.getPixels().get(2).get(0).setColor(right.getPixels().get(0).get(0).getColor());
            back.getPixels().get(2).get(1).setColor(right.getPixels().get(1).get(0).getColor());
            back.getPixels().get(2).get(2).setColor(right.getPixels().get(2).get(0).getColor());

            right.getPixels().get(0).get(0).setColor(tmp.get(2));
            right.getPixels().get(1).get(0).setColor(tmp.get(1));
            right.getPixels().get(2).get(0).setColor(tmp.get(0));
        } else { // без комментариев(троллю)
            front.getPixels().get(0).get(0).setColor(right.getPixels().get(2).get(0).getColor());
            front.getPixels().get(0).get(1).setColor(right.getPixels().get(1).get(0).getColor());
            front.getPixels().get(0).get(2).setColor(right.getPixels().get(0).get(0).getColor());

            right.getPixels().get(0).get(0).setColor(back.getPixels().get(2).get(0).getColor());
            right.getPixels().get(1).get(0).setColor(back.getPixels().get(2).get(1).getColor());
            right.getPixels().get(2).get(0).setColor(back.getPixels().get(2).get(2).getColor());

            back.getPixels().get(2).get(0).setColor(left.getPixels().get(2).get(2).getColor());
            back.getPixels().get(2).get(1).setColor(left.getPixels().get(1).get(2).getColor());
            back.getPixels().get(2).get(2).setColor(left.getPixels().get(0).get(2).getColor());

            left.getPixels().get(0).get(2).setColor(tmp.get(0));
            left.getPixels().get(1).get(2).setColor(tmp.get(1));
            left.getPixels().get(2).get(2).setColor(tmp.get(2));
        }
        edgeRepaintWithSleep();
    }

    public void rotateF(int direction) {
        front.changePixels((ArrayList<ArrayList<String>>) this.beginRotations(front, direction).clone());

        ArrayList<String> tmp = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tmp.add(up.getPixels().get(0).get(i).getColor());
        }

        if (direction == 1) {
            up.getPixels().get(0).get(0).setColor(left.getPixels().get(0).get(0).getColor());
            up.getPixels().get(0).get(1).setColor(left.getPixels().get(0).get(1).getColor());
            up.getPixels().get(0).get(2).setColor(left.getPixels().get(0).get(2).getColor());

            left.getPixels().get(0).get(0).setColor(down.getPixels().get(2).get(2).getColor());
            left.getPixels().get(0).get(1).setColor(down.getPixels().get(2).get(1).getColor());
            left.getPixels().get(0).get(2).setColor(down.getPixels().get(2).get(0).getColor());

            down.getPixels().get(2).get(0).setColor(right.getPixels().get(0).get(2).getColor()); // мб тут ошибка появится, хз
            down.getPixels().get(2).get(1).setColor(right.getPixels().get(0).get(1).getColor());
            down.getPixels().get(2).get(2).setColor(right.getPixels().get(0).get(0).getColor());

            right.getPixels().get(0).get(0).setColor(tmp.get(0));
            right.getPixels().get(0).get(1).setColor(tmp.get(1));
            right.getPixels().get(0).get(2).setColor(tmp.get(2));

        } else {
            up.getPixels().get(0).get(0).setColor(right.getPixels().get(0).get(0).getColor());
            up.getPixels().get(0).get(1).setColor(right.getPixels().get(0).get(1).getColor());
            up.getPixels().get(0).get(2).setColor(right.getPixels().get(0).get(2).getColor());

            right.getPixels().get(0).get(0).setColor(down.getPixels().get(2).get(2).getColor());
            right.getPixels().get(0).get(1).setColor(down.getPixels().get(2).get(1).getColor());
            right.getPixels().get(0).get(2).setColor(down.getPixels().get(2).get(0).getColor());

            down.getPixels().get(2).get(2).setColor(left.getPixels().get(0).get(0).getColor());
            down.getPixels().get(2).get(1).setColor(left.getPixels().get(0).get(1).getColor());
            down.getPixels().get(2).get(0).setColor(left.getPixels().get(0).get(2).getColor());

            left.getPixels().get(0).get(0).setColor(tmp.get(0));
            left.getPixels().get(0).get(1).setColor(tmp.get(1));
            left.getPixels().get(0).get(2).setColor(tmp.get(2));
        }
        edgeRepaintWithSleep();
    }

    public void rotateB(int direction) {
        back.changePixels((ArrayList<ArrayList<String>>) this.beginRotations(back, direction).clone());

        ArrayList<String> tmp = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tmp.add(up.getPixels().get(2).get(i).getColor());
        }

        // код в ифах ниже ну прям просится в отдельный метод(но делать пока этого не буду, принцип говнокодера)
        if (direction == 1) {
            up.getPixels().get(2).get(0).setColor(right.getPixels().get(2).get(0).getColor());
            up.getPixels().get(2).get(1).setColor(right.getPixels().get(2).get(1).getColor());
            up.getPixels().get(2).get(2).setColor(right.getPixels().get(2).get(2).getColor());

            right.getPixels().get(2).get(0).setColor(down.getPixels().get(0).get(2).getColor());
            right.getPixels().get(2).get(1).setColor(down.getPixels().get(0).get(1).getColor());
            right.getPixels().get(2).get(2).setColor(down.getPixels().get(0).get(0).getColor());

            down.getPixels().get(0).get(0).setColor(left.getPixels().get(2).get(2).getColor());
            down.getPixels().get(0).get(1).setColor(left.getPixels().get(2).get(1).getColor());
            down.getPixels().get(0).get(2).setColor(left.getPixels().get(2).get(0).getColor());

            left.getPixels().get(2).get(0).setColor(tmp.get(0));
            left.getPixels().get(2).get(1).setColor(tmp.get(1));
            left.getPixels().get(2).get(2).setColor(tmp.get(2));
        } else {
            up.getPixels().get(2).get(0).setColor(left.getPixels().get(2).get(0).getColor());
            up.getPixels().get(2).get(1).setColor(left.getPixels().get(2).get(1).getColor());
            up.getPixels().get(2).get(2).setColor(left.getPixels().get(2).get(2).getColor());

            left.getPixels().get(2).get(0).setColor(down.getPixels().get(0).get(2).getColor());
            left.getPixels().get(2).get(1).setColor(down.getPixels().get(0).get(1).getColor());
            left.getPixels().get(2).get(2).setColor(down.getPixels().get(0).get(0).getColor());

            down.getPixels().get(0).get(0).setColor(right.getPixels().get(2).get(2).getColor());
            down.getPixels().get(0).get(1).setColor(right.getPixels().get(2).get(1).getColor());
            down.getPixels().get(0).get(2).setColor(right.getPixels().get(2).get(0).getColor());

            right.getPixels().get(2).get(0).setColor(tmp.get(0));
            right.getPixels().get(2).get(1).setColor(tmp.get(1));
            right.getPixels().get(2).get(2).setColor(tmp.get(2));
        }
        edgeRepaintWithSleep();
    }

    public void rotateM(int direction) {
        ArrayList<String> tmp = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tmp.add(front.getPixels().get(i).get(1).getColor()); // СЕРЕДИНА Я СКАЗАЛ БУДЕТ БРАТЬСЯ ;%;%;
        }

        if (direction == 1) {
            rightAndLeftRotations(front, up, 1);
            rightAndLeftRotations(up, back, 1);
            rightAndLeftRotations(back, down, 1);
            rightAndLeftRotations(down, tmp, 1);
        } else {
            rightAndLeftRotations(front, down, 1);
            rightAndLeftRotations(down, back, 1);
            rightAndLeftRotations(back, up, 1);
            rightAndLeftRotations(up, tmp, 1);
        }
        edgeRepaintWithSleep();
    }

    public void rotateS(int direction) {
        ArrayList<String> tmp = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tmp.add(up.getPixels().get(1).get(i).getColor());
        }

        if (direction == 1) {
            up.getPixels().get(1).get(0).setColor(left.getPixels().get(1).get(0).getColor());
            up.getPixels().get(1).get(1).setColor(left.getPixels().get(1).get(1).getColor());
            up.getPixels().get(1).get(2).setColor(left.getPixels().get(1).get(2).getColor());

            left.getPixels().get(1).get(0).setColor(down.getPixels().get(1).get(2).getColor());
            left.getPixels().get(1).get(1).setColor(down.getPixels().get(1).get(1).getColor());
            left.getPixels().get(1).get(2).setColor(down.getPixels().get(1).get(0).getColor());

            down.getPixels().get(1).get(0).setColor(right.getPixels().get(1).get(2).getColor()); // мб тут ошибка появится, хз
            down.getPixels().get(1).get(1).setColor(right.getPixels().get(1).get(1).getColor());
            down.getPixels().get(1).get(2).setColor(right.getPixels().get(1).get(0).getColor());

            right.getPixels().get(1).get(0).setColor(tmp.get(0));
            right.getPixels().get(1).get(1).setColor(tmp.get(1));
            right.getPixels().get(1).get(2).setColor(tmp.get(2));

        } else {
            up.getPixels().get(1).get(0).setColor(right.getPixels().get(1).get(0).getColor());
            up.getPixels().get(1).get(1).setColor(right.getPixels().get(1).get(1).getColor());
            up.getPixels().get(1).get(2).setColor(right.getPixels().get(1).get(2).getColor());

            right.getPixels().get(1).get(0).setColor(down.getPixels().get(1).get(2).getColor());
            right.getPixels().get(1).get(1).setColor(down.getPixels().get(1).get(1).getColor());
            right.getPixels().get(1).get(2).setColor(down.getPixels().get(1).get(0).getColor());

            down.getPixels().get(1).get(2).setColor(left.getPixels().get(1).get(0).getColor());
            down.getPixels().get(1).get(1).setColor(left.getPixels().get(1).get(1).getColor());
            down.getPixels().get(1).get(0).setColor(left.getPixels().get(1).get(2).getColor());

            left.getPixels().get(1).get(0).setColor(tmp.get(0));
            left.getPixels().get(1).get(1).setColor(tmp.get(1));
            left.getPixels().get(1).get(2).setColor(tmp.get(2));
        }
        edgeRepaintWithSleep();
    }

    public void rotateE(int direction) {
        ArrayList<String> tmp = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tmp.add(front.getPixels().get(1).get(i).getColor());
        }

        if (direction == 1) { // продолжаю говнокодить, мб, когда-нибудь вытащу в отдельный метод, но уже на релизе программы)))
            front.getPixels().get(1).get(0).setColor(left.getPixels().get(0).get(1).getColor());
            front.getPixels().get(1).get(1).setColor(left.getPixels().get(1).get(1).getColor());
            front.getPixels().get(1).get(2).setColor(left.getPixels().get(2).get(1).getColor());

            left.getPixels().get(0).get(1).setColor(back.getPixels().get(1).get(2).getColor());
            left.getPixels().get(1).get(1).setColor(back.getPixels().get(1).get(1).getColor());
            left.getPixels().get(2).get(1).setColor(back.getPixels().get(1).get(0).getColor());

            back.getPixels().get(1).get(0).setColor(right.getPixels().get(0).get(1).getColor());
            back.getPixels().get(1).get(1).setColor(right.getPixels().get(1).get(1).getColor());
            back.getPixels().get(1).get(2).setColor(right.getPixels().get(2).get(1).getColor());

            right.getPixels().get(0).get(1).setColor(tmp.get(2));
            right.getPixels().get(1).get(1).setColor(tmp.get(1));
            right.getPixels().get(2).get(1).setColor(tmp.get(0));
        } else { // без комментариев(троллю)
            front.getPixels().get(1).get(0).setColor(right.getPixels().get(2).get(1).getColor());
            front.getPixels().get(1).get(1).setColor(right.getPixels().get(1).get(1).getColor());
            front.getPixels().get(1).get(2).setColor(right.getPixels().get(0).get(1).getColor());

            right.getPixels().get(0).get(1).setColor(back.getPixels().get(1).get(0).getColor());
            right.getPixels().get(1).get(1).setColor(back.getPixels().get(1).get(1).getColor());
            right.getPixels().get(2).get(1).setColor(back.getPixels().get(1).get(2).getColor());

            back.getPixels().get(1).get(0).setColor(left.getPixels().get(2).get(1).getColor());
            back.getPixels().get(1).get(1).setColor(left.getPixels().get(1).get(1).getColor());
            back.getPixels().get(1).get(2).setColor(left.getPixels().get(0).get(1).getColor());

            left.getPixels().get(0).get(1).setColor(tmp.get(0));
            left.getPixels().get(1).get(1).setColor(tmp.get(1));
            left.getPixels().get(2).get(1).setColor(tmp.get(2));
        }
        edgeRepaintWithSleep();
    }

    public void makeCross() {
        greenCross();
        redCross();
        blueCross();
        orangeCross();
    }
    private void greenCross() {
        boolean find = false;
        if (front.getPixels().get(0).get(1).getColor().equals("G") && down.getPixels().get(2).get(1).getColor().equals("W") ||
                front.getPixels().get(0).get(1).getColor().equals("W") && down.getPixels().get(2).get(1).getColor().equals("G")) {
            find = true;
        }
        if (right.getPixels().get(1).get(0).getColor().equals("G") && down.getPixels().get(1).get(0).getColor().equals("W") ||
                right.getPixels().get(1).get(0).getColor().equals("W") && down.getPixels().get(1).get(0).getColor().equals("G")) {
            find = true;
        }
        if (back.getPixels().get(2).get(1).getColor().equals("G") && down.getPixels().get(0).get(1).getColor().equals("W") ||
                back.getPixels().get(2).get(1).getColor().equals("W") && down.getPixels().get(0).get(1).getColor().equals("G")) {
            find = true;
        }
        if (left.getPixels().get(1).get(2).getColor().equals("G") && down.getPixels().get(1).get(2).getColor().equals("W") ||
                left.getPixels().get(1).get(2).getColor().equals("W") && down.getPixels().get(1).get(2).getColor().equals("G")) {
            find = true;
        }
        if (find) {
            while (!(front.getPixels().get(0).get(1).getColor().equals("G") && down.getPixels().get(2).get(1).getColor().equals("W") ||
                    front.getPixels().get(0).get(1).getColor().equals("W") && down.getPixels().get(2).get(1).getColor().equals("G"))) {
                rotateD(0);
            }
        } else {
            while (true) {
                if (front.getPixels().get(1).get(0).getColor().equals("G") && right.getPixels().get(0).get(1).getColor().equals("W") ||
                        front.getPixels().get(1).get(0).getColor().equals("W") && right.getPixels().get(0).get(1).getColor().equals("G")) {
                    rotateF(1);
                    break;
                }
                if (front.getPixels().get(1).get(2).getColor().equals("G") && left.getPixels().get(0).get(1).getColor().equals("W") ||
                        front.getPixels().get(1).get(2).getColor().equals("W") && left.getPixels().get(0).get(1).getColor().equals("G")) {
                    rotateF(0);
                    break;
                }
                if (front.getPixels().get(2).get(1).getColor().equals("G") && up.getPixels().get(0).get(1).getColor().equals("W") ||
                        front.getPixels().get(2).get(1).getColor().equals("W") && up.getPixels().get(0).get(1).getColor().equals("G")) {
                    rotateF(1);
                    rotateF(1);
                    break;
                }

                if (right.getPixels().get(2).get(1).getColor().equals("G") && back.getPixels().get(1).get(0).getColor().equals("W") ||
                        right.getPixels().get(2).get(1).getColor().equals("W") && back.getPixels().get(1).get(0).getColor().equals("G")) {
                    rotateR(1);
                    break;
                }
                if (right.getPixels().get(1).get(2).getColor().equals("G") && up.getPixels().get(1).get(0).getColor().equals("W") ||
                        right.getPixels().get(1).get(2).getColor().equals("W") && up.getPixels().get(1).get(0).getColor().equals("G")) {
                    rotateR(1);
                    rotateR(1);
                    break;
                }

                if (back.getPixels().get(1).get(2).getColor().equals("G") && left.getPixels().get(2).get(1).getColor().equals("W") ||
                        back.getPixels().get(1).get(2).getColor().equals("W") && left.getPixels().get(2).get(1).getColor().equals("G")) {
                    rotateB(1);
                    break;
                }
                if (back.getPixels().get(0).get(1).getColor().equals("G") && up.getPixels().get(2).get(1).getColor().equals("W") ||
                        back.getPixels().get(0).get(1).getColor().equals("W") && up.getPixels().get(2).get(1).getColor().equals("G")) {
                    rotateB(1);
                    rotateB(1);
                    break;
                }
                if (left.getPixels().get(1).get(0).getColor().equals("G") && up.getPixels().get(1).get(2).getColor().equals("W") ||
                        left.getPixels().get(1).get(0).getColor().equals("W") && up.getPixels().get(1).get(2).getColor().equals("G")) {
                    rotateL(1);
                    rotateL(1);
                    break;
                }
            }
            while (!(front.getPixels().get(0).get(1).getColor().equals("G") && down.getPixels().get(2).get(1).getColor().equals("W") ||
                    front.getPixels().get(0).get(1).getColor().equals("W") && down.getPixels().get(2).get(1).getColor().equals("G"))) {
                rotateD(0);
            }
        }
        if (front.getPixels().get(0).get(1).getColor().equals("G") && down.getPixels().get(2).get(1).getColor().equals("W")) {
            rotateF(1);
            rotateF(1);
        } else {
            rotateD(1);
            rotateR(1);
            rotateF(0);
            rotateR(0);
        }
    }
    private void redCross (){
        boolean find = false;
        if (front.getPixels().get(0).get(1).getColor().equals("R") && down.getPixels().get(2).get(1).getColor().equals("W") ||
                front.getPixels().get(0).get(1).getColor().equals("W") && down.getPixels().get(2).get(1).getColor().equals("R")) {
            find = true;
        }
        if (right.getPixels().get(1).get(0).getColor().equals("R") && down.getPixels().get(1).get(0).getColor().equals("W") ||
                right.getPixels().get(1).get(0).getColor().equals("W") && down.getPixels().get(1).get(0).getColor().equals("R")) {
            find = true;
        }
        if (back.getPixels().get(2).get(1).getColor().equals("R") && down.getPixels().get(0).get(1).getColor().equals("W") ||
                back.getPixels().get(2).get(1).getColor().equals("W") && down.getPixels().get(0).get(1).getColor().equals("R")) {
            find = true;
        }
        if (left.getPixels().get(1).get(2).getColor().equals("R") && down.getPixels().get(1).get(2).getColor().equals("W") ||
                left.getPixels().get(1).get(2).getColor().equals("W") && down.getPixels().get(1).get(2).getColor().equals("R")) {
            find = true;
        }
        if (find) {
            while (!(right.getPixels().get(1).get(0).getColor().equals("R") && down.getPixels().get(1).get(0).getColor().equals("W") ||
                    right.getPixels().get(1).get(0).getColor().equals("W") && down.getPixels().get(1).get(0).getColor().equals("R"))) {
                rotateD(0);
            }
        } else {
            boolean changed = false;
            while (true) {
                if (front.getPixels().get(1).get(0).getColor().equals("R") && right.getPixels().get(0).get(1).getColor().equals("W") ||
                        front.getPixels().get(1).get(0).getColor().equals("W") && right.getPixels().get(0).get(1).getColor().equals("R")) {
                    if (front.getPixels().get(2).get(1).getColor().equals("G") && up.getPixels().get(0).get(1).getColor().equals("W") ||
                            front.getPixels().get(2).get(1).getColor().equals("W") && up.getPixels().get(0).get(1).getColor().equals("G")) {
                        changed = true;
                    }
                    rotateF(1);
                    if (changed) {
                        rotateD(0);
                        rotateF(0);
                    }
                    break;
                }
                if (front.getPixels().get(1).get(2).getColor().equals("R") && left.getPixels().get(0).get(1).getColor().equals("W") ||
                        front.getPixels().get(1).get(2).getColor().equals("W") && left.getPixels().get(0).get(1).getColor().equals("R")) {
                    if (front.getPixels().get(2).get(1).getColor().equals("G") && up.getPixels().get(0).get(1).getColor().equals("W") ||
                            front.getPixels().get(2).get(1).getColor().equals("W") && up.getPixels().get(0).get(1).getColor().equals("G")) {
                        changed = true;
                    }
                    rotateF(0);
                    if (changed) {
                        rotateD(0);
                        rotateF(1);
                    }
                    break;
                }
                if (front.getPixels().get(2).get(1).getColor().equals("R") && up.getPixels().get(0).get(1).getColor().equals("W") ||
                        front.getPixels().get(2).get(1).getColor().equals("W") && up.getPixels().get(0).get(1).getColor().equals("R")) {
                    rotateF(1);
                    rotateF(1);
                    break;
                }

                if (right.getPixels().get(2).get(1).getColor().equals("R") && back.getPixels().get(1).get(0).getColor().equals("W") ||
                        right.getPixels().get(2).get(1).getColor().equals("W") && back.getPixels().get(1).get(0).getColor().equals("R")) {
                    rotateR(1);
                    break;
                }
                if (right.getPixels().get(1).get(2).getColor().equals("R") && up.getPixels().get(1).get(0).getColor().equals("W") ||
                        right.getPixels().get(1).get(2).getColor().equals("W") && up.getPixels().get(1).get(0).getColor().equals("R")) {
                    rotateR(1);
                    rotateR(1);
                    break;
                }

                if (back.getPixels().get(1).get(2).getColor().equals("R") && left.getPixels().get(2).get(1).getColor().equals("W") ||
                        back.getPixels().get(1).get(2).getColor().equals("W") && left.getPixels().get(2).get(1).getColor().equals("R")) {
                    rotateB(1);
                    break;
                }
                if (back.getPixels().get(0).get(1).getColor().equals("R") && up.getPixels().get(2).get(1).getColor().equals("W") ||
                        back.getPixels().get(0).get(1).getColor().equals("W") && up.getPixels().get(2).get(1).getColor().equals("R")) {
                    rotateB(1);
                    rotateB(1);
                    break;
                }
                if (left.getPixels().get(1).get(0).getColor().equals("R") && up.getPixels().get(1).get(2).getColor().equals("W") ||
                        left.getPixels().get(1).get(0).getColor().equals("W") && up.getPixels().get(1).get(2).getColor().equals("R")) {
                    rotateL(1);
                    rotateL(1);
                    break;
                }
            }
            while (!(right.getPixels().get(1).get(0).getColor().equals("R") && down.getPixels().get(1).get(0).getColor().equals("W") ||
                    right.getPixels().get(1).get(0).getColor().equals("W") && down.getPixels().get(1).get(0).getColor().equals("R"))) {
                rotateD(0);
            }
        }
        if (right.getPixels().get(1).get(0).getColor().equals("R") && down.getPixels().get(1).get(0).getColor().equals("W")) {
            rotateR(1);
            rotateR(1);
        } else {
            rotateD(1);
            rotateB(1);
            rotateR(0);
            rotateB(0);
        }
    }
    private void blueCross() {
        boolean find = false;
        if (front.getPixels().get(0).get(1).getColor().equals("B") && down.getPixels().get(2).get(1).getColor().equals("W") ||
                front.getPixels().get(0).get(1).getColor().equals("W") && down.getPixels().get(2).get(1).getColor().equals("B")) {
            find = true;
        }
        if (right.getPixels().get(1).get(0).getColor().equals("B") && down.getPixels().get(1).get(0).getColor().equals("W") ||
                right.getPixels().get(1).get(0).getColor().equals("W") && down.getPixels().get(1).get(0).getColor().equals("B")) {
            find = true;
        }
        if (back.getPixels().get(2).get(1).getColor().equals("B") && down.getPixels().get(0).get(1).getColor().equals("W") ||
                back.getPixels().get(2).get(1).getColor().equals("W") && down.getPixels().get(0).get(1).getColor().equals("B")) {
            find = true;
        }
        if (left.getPixels().get(1).get(2).getColor().equals("B") && down.getPixels().get(1).get(2).getColor().equals("W") ||
                left.getPixels().get(1).get(2).getColor().equals("W") && down.getPixels().get(1).get(2).getColor().equals("B")) {
            find = true;
        }
        if (find) {
            while (!(back.getPixels().get(2).get(1).getColor().equals("B") && down.getPixels().get(0).get(1).getColor().equals("W") ||
                    back.getPixels().get(2).get(1).getColor().equals("W") && down.getPixels().get(0).get(1).getColor().equals("B"))) {
                rotateD(0);
            }
        } else {
            boolean changed = false;
            while (true) {
                if (front.getPixels().get(1).get(0).getColor().equals("B") && right.getPixels().get(0).get(1).getColor().equals("W") ||
                        front.getPixels().get(1).get(0).getColor().equals("W") && right.getPixels().get(0).get(1).getColor().equals("B")) {
                    if (front.getPixels().get(2).get(1).getColor().equals("G") && up.getPixels().get(0).get(1).getColor().equals("W") ||
                            front.getPixels().get(2).get(1).getColor().equals("W") && up.getPixels().get(0).get(1).getColor().equals("G")) {
                        changed = true;
                    }
                    rotateF(1);
                    if (changed) {
                        rotateD(0);
                        rotateF(0);
                    }
                    break;
                }
                if (front.getPixels().get(1).get(2).getColor().equals("B") && left.getPixels().get(0).get(1).getColor().equals("W") ||
                        front.getPixels().get(1).get(2).getColor().equals("W") && left.getPixels().get(0).get(1).getColor().equals("B")) {
                    if (front.getPixels().get(2).get(1).getColor().equals("G") && up.getPixels().get(0).get(1).getColor().equals("W") ||
                            front.getPixels().get(2).get(1).getColor().equals("W") && up.getPixels().get(0).get(1).getColor().equals("G")) {
                        changed = true;
                    }
                    rotateF(0);
                    if (changed) {
                        rotateD(0);
                        rotateF(1);
                    }
                    break;
                }
                if (front.getPixels().get(2).get(1).getColor().equals("B") && up.getPixels().get(0).get(1).getColor().equals("W") ||
                        front.getPixels().get(2).get(1).getColor().equals("W") && up.getPixels().get(0).get(1).getColor().equals("B")) {
                    rotateF(1);
                    rotateF(1);
                    break;
                }

                if (right.getPixels().get(2).get(1).getColor().equals("B") && back.getPixels().get(1).get(0).getColor().equals("W") ||
                        right.getPixels().get(2).get(1).getColor().equals("W") && back.getPixels().get(1).get(0).getColor().equals("B")) {
                    if (right.getPixels().get(1).get(2).getColor().equals("R") && up.getPixels().get(1).get(0).getColor().equals("W") ||
                            right.getPixels().get(1).get(2).getColor().equals("W") && up.getPixels().get(1).get(0).getColor().equals("R")) {
                        changed = true;
                    }
                    rotateR(1);
                    if (changed) {
                        rotateD(0);
                        rotateR(0);
                    }
                    break;
                }
                if (right.getPixels().get(1).get(2).getColor().equals("B") && up.getPixels().get(1).get(0).getColor().equals("W") ||
                        right.getPixels().get(1).get(2).getColor().equals("W") && up.getPixels().get(1).get(0).getColor().equals("B")) {
                    rotateR(1);
                    rotateR(1);
                    break;
                }

                if (back.getPixels().get(1).get(2).getColor().equals("B") && left.getPixels().get(2).get(1).getColor().equals("W") ||
                        back.getPixels().get(1).get(2).getColor().equals("W") && left.getPixels().get(2).get(1).getColor().equals("B")) {
                    rotateB(1);
                    break;
                }
                if (back.getPixels().get(0).get(1).getColor().equals("B") && up.getPixels().get(2).get(1).getColor().equals("W") ||
                        back.getPixels().get(0).get(1).getColor().equals("W") && up.getPixels().get(2).get(1).getColor().equals("B")) {
                    rotateB(1);
                    rotateB(1);
                    break;
                }
                if (left.getPixels().get(1).get(0).getColor().equals("B") && up.getPixels().get(1).get(2).getColor().equals("W") ||
                        left.getPixels().get(1).get(0).getColor().equals("W") && up.getPixels().get(1).get(2).getColor().equals("B")) {
                    rotateL(1);
                    rotateL(1);
                    break;
                }
            }
            while (!(back.getPixels().get(2).get(1).getColor().equals("B") && down.getPixels().get(0).get(1).getColor().equals("W") ||
                    back.getPixels().get(2).get(1).getColor().equals("W") && down.getPixels().get(0).get(1).getColor().equals("B"))) {
                rotateD(0);
            }
        }
        if (back.getPixels().get(2).get(1).getColor().equals("B") && down.getPixels().get(0).get(1).getColor().equals("W")) {
            rotateB(1);
            rotateB(1);
        } else {
            rotateD(1);
            rotateL(1);
            rotateB(0);
            rotateL(0);
        }
    }
    private void orangeCross (){
        boolean find = false;
        if (front.getPixels().get(0).get(1).getColor().equals("O") && down.getPixels().get(2).get(1).getColor().equals("W") ||
                front.getPixels().get(0).get(1).getColor().equals("W") && down.getPixels().get(2).get(1).getColor().equals("O")) {
            find = true;
        }
        if (right.getPixels().get(1).get(0).getColor().equals("O") && down.getPixels().get(1).get(0).getColor().equals("W") ||
                right.getPixels().get(1).get(0).getColor().equals("W") && down.getPixels().get(1).get(0).getColor().equals("O")) {
            find = true;
        }
        if (back.getPixels().get(2).get(1).getColor().equals("O") && down.getPixels().get(0).get(1).getColor().equals("W") ||
                back.getPixels().get(2).get(1).getColor().equals("W") && down.getPixels().get(0).get(1).getColor().equals("O")) {
            find = true;
        }
        if (left.getPixels().get(1).get(2).getColor().equals("O") && down.getPixels().get(1).get(2).getColor().equals("W") ||
                left.getPixels().get(1).get(2).getColor().equals("W") && down.getPixels().get(1).get(2).getColor().equals("O")) {
            find = true;
        }
        if (find) {
            while (!(left.getPixels().get(1).get(2).getColor().equals("O") && down.getPixels().get(1).get(2).getColor().equals("W") ||
                    left.getPixels().get(1).get(2).getColor().equals("W") && down.getPixels().get(1).get(2).getColor().equals("O"))) {
                rotateD(0);
            }
        } else {
            boolean changed = false;
            while (true) {
                if (front.getPixels().get(1).get(0).getColor().equals("O") && right.getPixels().get(0).get(1).getColor().equals("W") ||
                        front.getPixels().get(1).get(0).getColor().equals("W") && right.getPixels().get(0).get(1).getColor().equals("O")) {
                    if (front.getPixels().get(2).get(1).getColor().equals("G") && up.getPixels().get(0).get(1).getColor().equals("W") ||
                            front.getPixels().get(2).get(1).getColor().equals("W") && up.getPixels().get(0).get(1).getColor().equals("G")) {
                        changed = true;
                    }
                    rotateF(1);
                    if (changed) {
                        rotateD(0);
                        rotateF(0);
                    }
                    break;
                }
                if (front.getPixels().get(1).get(2).getColor().equals("O") && left.getPixels().get(0).get(1).getColor().equals("W") ||
                        front.getPixels().get(1).get(2).getColor().equals("W") && left.getPixels().get(0).get(1).getColor().equals("O")) {
                    if (front.getPixels().get(2).get(1).getColor().equals("G") && up.getPixels().get(0).get(1).getColor().equals("W") ||
                            front.getPixels().get(2).get(1).getColor().equals("W") && up.getPixels().get(0).get(1).getColor().equals("G")) {
                        changed = true;
                    }
                    rotateF(0);
                    if (changed) {
                        rotateD(0);
                        rotateF(1);
                    }
                    break;
                }
                if (front.getPixels().get(2).get(1).getColor().equals("O") && up.getPixels().get(0).get(1).getColor().equals("W") ||
                        front.getPixels().get(2).get(1).getColor().equals("W") && up.getPixels().get(0).get(1).getColor().equals("O")) {
                    rotateF(1);
                    rotateF(1);
                    break;
                }

                if (right.getPixels().get(2).get(1).getColor().equals("O") && back.getPixels().get(1).get(0).getColor().equals("W") ||
                        right.getPixels().get(2).get(1).getColor().equals("W") && back.getPixels().get(1).get(0).getColor().equals("O")) {
                    if (right.getPixels().get(1).get(2).getColor().equals("R") && up.getPixels().get(1).get(0).getColor().equals("W") ||
                            right.getPixels().get(1).get(2).getColor().equals("W") && up.getPixels().get(1).get(0).getColor().equals("R")) {
                        changed = true;
                    }
                    rotateR(1);
                    if (changed) {
                        rotateD(0);
                        rotateR(0);
                    }
                    break;
                }
                if (right.getPixels().get(1).get(2).getColor().equals("O") && up.getPixels().get(1).get(0).getColor().equals("W") ||
                        right.getPixels().get(1).get(2).getColor().equals("W") && up.getPixels().get(1).get(0).getColor().equals("O")) {
                    rotateR(1);
                    rotateR(1);
                    break;
                }

                if (back.getPixels().get(1).get(2).getColor().equals("O") && left.getPixels().get(2).get(1).getColor().equals("W") ||
                        back.getPixels().get(1).get(2).getColor().equals("W") && left.getPixels().get(2).get(1).getColor().equals("O")) {
                    if (back.getPixels().get(0).get(1).getColor().equals("B") && up.getPixels().get(2).get(1).getColor().equals("W") ||
                            back.getPixels().get(0).get(1).getColor().equals("W") && up.getPixels().get(2).get(1).getColor().equals("B")) {
                        changed = true;
                    }
                    rotateB(1);
                    if (changed) {
                        rotateD(0);
                        rotateB(0);
                    }
                    break;
                }
                if (back.getPixels().get(0).get(1).getColor().equals("O") && up.getPixels().get(2).get(1).getColor().equals("W") ||
                        back.getPixels().get(0).get(1).getColor().equals("W") && up.getPixels().get(2).get(1).getColor().equals("O")) {
                    rotateB(1);
                    rotateB(1);
                    break;
                }
                if (left.getPixels().get(1).get(0).getColor().equals("O") && up.getPixels().get(1).get(2).getColor().equals("W") ||
                        left.getPixels().get(1).get(0).getColor().equals("W") && up.getPixels().get(1).get(2).getColor().equals("O")) {
                    rotateL(1);
                    rotateL(1);
                    break;
                }
            }
            while (!(left.getPixels().get(1).get(2).getColor().equals("O") && down.getPixels().get(1).get(2).getColor().equals("W") ||
                    left.getPixels().get(1).get(2).getColor().equals("W") && down.getPixels().get(1).get(2).getColor().equals("O"))) {
                rotateD(0);
            }
        }
        if (left.getPixels().get(1).get(2).getColor().equals("O") && down.getPixels().get(1).get(2).getColor().equals("W")) {
            rotateL(1);
            rotateL(1);
        } else {
            rotateD(1);
            rotateF(1);
            rotateL(0);
            rotateF(0);
        }
    }
    private boolean checkUp() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!up.getPixels().get(i).get(j).getColor().equals("W")) {
                    return false;
                }
            }
        }
        return true;
    }
    private boolean checkFrontDownCorner(String c1, String c2, String c3) {
        if (front.getPixels().get(0).get(2).getColor().equals(c1) || front.getPixels().get(0).get(2).getColor().equals(c2) || front.getPixels().get(0).get(2).getColor().equals(c3)) {
            if (front.getPixels().get(0).get(2).getColor().equals(c1)) {
                if (left.getPixels().get(0).get(2).getColor().equals(c2) && down.getPixels().get(2).get(2).getColor().equals(c3) || left.getPixels().get(0).get(2).getColor().equals(c3) && down.getPixels().get(2).get(2).getColor().equals(c2)) {
                    return true;
                }
            }
            if (front.getPixels().get(0).get(2).getColor().equals(c2)) {
                if (left.getPixels().get(0).get(2).getColor().equals(c1) && down.getPixels().get(2).get(2).getColor().equals(c3) || left.getPixels().get(0).get(2).getColor().equals(c3) && down.getPixels().get(2).get(2).getColor().equals(c1)) {
                    return true;
                }
            }
            if (front.getPixels().get(0).get(2).getColor().equals(c3)) {
                return left.getPixels().get(0).get(2).getColor().equals(c2) && down.getPixels().get(2).get(2).getColor().equals(c1) || left.getPixels().get(0).get(2).getColor().equals(c1) && down.getPixels().get(2).get(2).getColor().equals(c2);
            }
        }
        return false;
    }
    private boolean checkFrontUpCorner(String c1, String c2, String c3) {
        if (front.getPixels().get(2).get(2).getColor().equals(c1) || front.getPixels().get(2).get(2).getColor().equals(c2) || front.getPixels().get(2).get(2).getColor().equals(c3)) {
            if (front.getPixels().get(2).get(2).getColor().equals(c1)) {
                if (left.getPixels().get(0).get(0).getColor().equals(c2) && up.getPixels().get(0).get(2).getColor().equals(c3) || left.getPixels().get(0).get(0).getColor().equals(c3) && up.getPixels().get(0).get(2).getColor().equals(c2)) {
                    return true;
                }
            }
            if (front.getPixels().get(2).get(2).getColor().equals(c2)) {
                if (left.getPixels().get(0).get(0).getColor().equals(c1) && up.getPixels().get(0).get(2).getColor().equals(c3) || left.getPixels().get(0).get(0).getColor().equals(c3) && up.getPixels().get(0).get(2).getColor().equals(c1)) {
                    return true;
                }
            }
            if (front.getPixels().get(2).get(2).getColor().equals(c3)) {
                return left.getPixels().get(0).get(0).getColor().equals(c2) && up.getPixels().get(0).get(2).getColor().equals(c1) || left.getPixels().get(0).get(0).getColor().equals(c1) && up.getPixels().get(0).get(2).getColor().equals(c2);
            }
        }
        return false;
    }
    private boolean checkRightDownCorner(String c1, String c2, String c3) {
        if (right.getPixels().get(0).get(0).getColor().equals(c1) || right.getPixels().get(0).get(0).getColor().equals(c2) || right.getPixels().get(0).get(0).getColor().equals(c3)) {
            if (right.getPixels().get(0).get(0).getColor().equals(c1)) {
                if (front.getPixels().get(0).get(0).getColor().equals(c2) && down.getPixels().get(2).get(0).getColor().equals(c3) || front.getPixels().get(0).get(0).getColor().equals(c3) && down.getPixels().get(2).get(0).getColor().equals(c2)) {
                    return true;
                }
            }
            if (right.getPixels().get(0).get(0).getColor().equals(c2)) {
                if (front.getPixels().get(0).get(0).getColor().equals(c1) && down.getPixels().get(2).get(0).getColor().equals(c3) || front.getPixels().get(0).get(0).getColor().equals(c3) && down.getPixels().get(2).get(0).getColor().equals(c1)) {
                    return true;
                }
            }
            if (right.getPixels().get(0).get(0).getColor().equals(c3)) {
                return front.getPixels().get(0).get(0).getColor().equals(c2) && down.getPixels().get(2).get(0).getColor().equals(c1) || front.getPixels().get(0).get(0).getColor().equals(c1) && down.getPixels().get(2).get(0).getColor().equals(c2);
            }
        }
        return false;
    }
    private boolean checkRightUpCorner(String c1, String c2, String c3) {
        if (right.getPixels().get(0).get(2).getColor().equals(c1) || right.getPixels().get(0).get(2).getColor().equals(c2) || right.getPixels().get(0).get(2).getColor().equals(c3)) {
            if (right.getPixels().get(0).get(2).getColor().equals(c1)) {
                if (front.getPixels().get(2).get(0).getColor().equals(c2) && up.getPixels().get(0).get(0).getColor().equals(c3) || front.getPixels().get(2).get(0).getColor().equals(c3) && up.getPixels().get(0).get(0).getColor().equals(c2)) {
                    return true;
                }
            }
            if (right.getPixels().get(0).get(2).getColor().equals(c2)) {
                if (front.getPixels().get(2).get(0).getColor().equals(c1) && up.getPixels().get(0).get(0).getColor().equals(c3) || front.getPixels().get(2).get(0).getColor().equals(c3) && up.getPixels().get(0).get(0).getColor().equals(c1)) {
                    return true;
                }
            }
            if (right.getPixels().get(0).get(2).getColor().equals(c3)) {
                return front.getPixels().get(2).get(0).getColor().equals(c2) && up.getPixels().get(0).get(0).getColor().equals(c1) || front.getPixels().get(2).get(0).getColor().equals(c1) && up.getPixels().get(0).get(0).getColor().equals(c2);
            }
        }
        return false;
    }
    private boolean checkBackDownCorner(String c1, String c2, String c3) {
        if (back.getPixels().get(2).get(0).getColor().equals(c1) || back.getPixels().get(2).get(0).getColor().equals(c2) || back.getPixels().get(2).get(0).getColor().equals(c3)) {
            if (back.getPixels().get(2).get(0).getColor().equals(c1)) {
                if (right.getPixels().get(2).get(0).getColor().equals(c2) && down.getPixels().get(0).get(0).getColor().equals(c3) || right.getPixels().get(2).get(0).getColor().equals(c3) && down.getPixels().get(0).get(0).getColor().equals(c2)) {
                    return true;
                }
            }
            if (back.getPixels().get(2).get(0).getColor().equals(c2)) {
                if (right.getPixels().get(2).get(0).getColor().equals(c1) && down.getPixels().get(0).get(0).getColor().equals(c3) || right.getPixels().get(2).get(0).getColor().equals(c3) && down.getPixels().get(0).get(0).getColor().equals(c1)) {
                    return true;
                }
            }
            if (back.getPixels().get(2).get(0).getColor().equals(c3)) {
                return right.getPixels().get(2).get(0).getColor().equals(c2) && down.getPixels().get(0).get(0).getColor().equals(c1) || right.getPixels().get(2).get(0).getColor().equals(c1) && down.getPixels().get(0).get(0).getColor().equals(c2);
            }
        }
        return false;
    }
    private boolean checkBackUpCorner(String c1, String c2, String c3) {
        if (back.getPixels().get(0).get(0).getColor().equals(c1) || back.getPixels().get(0).get(0).getColor().equals(c2) || back.getPixels().get(0).get(0).getColor().equals(c3)) {
            if (back.getPixels().get(0).get(0).getColor().equals(c1)) {
                if (right.getPixels().get(2).get(2).getColor().equals(c2) && up.getPixels().get(2).get(0).getColor().equals(c3) || right.getPixels().get(2).get(2).getColor().equals(c3) && up.getPixels().get(2).get(0).getColor().equals(c2)) {
                    return true;
                }
            }
            if (back.getPixels().get(0).get(0).getColor().equals(c2)) {
                if (right.getPixels().get(2).get(2).getColor().equals(c1) && up.getPixels().get(2).get(0).getColor().equals(c3) || right.getPixels().get(2).get(2).getColor().equals(c3) && up.getPixels().get(2).get(0).getColor().equals(c1)) {
                    return true;
                }
            }
            if (back.getPixels().get(0).get(0).getColor().equals(c3)) {
                return right.getPixels().get(2).get(2).getColor().equals(c2) && up.getPixels().get(2).get(0).getColor().equals(c1) || right.getPixels().get(2).get(2).getColor().equals(c1) && up.getPixels().get(2).get(0).getColor().equals(c2);
            }
        }
        return false;
    }
    private boolean checkLeftDownCorner(String c1, String c2, String c3) {
        if (left.getPixels().get(2).get(2).getColor().equals(c1) || left.getPixels().get(2).get(2).getColor().equals(c2) || left.getPixels().get(2).get(2).getColor().equals(c3)) {
            if (left.getPixels().get(2).get(2).getColor().equals(c1)) {
                if (back.getPixels().get(2).get(2).getColor().equals(c2) && down.getPixels().get(0).get(2).getColor().equals(c3) || back.getPixels().get(2).get(2).getColor().equals(c3) && down.getPixels().get(0).get(2).getColor().equals(c2)) {
                    return true;
                }
            }
            if (left.getPixels().get(2).get(2).getColor().equals(c2)) {
                if (back.getPixels().get(2).get(2).getColor().equals(c1) && down.getPixels().get(0).get(2).getColor().equals(c3) || back.getPixels().get(2).get(2).getColor().equals(c3) && down.getPixels().get(0).get(2).getColor().equals(c1)) {
                    return true;
                }
            }
            if (left.getPixels().get(2).get(2).getColor().equals(c3)) {
                return back.getPixels().get(2).get(2).getColor().equals(c2) && down.getPixels().get(0).get(2).getColor().equals(c1) || back.getPixels().get(2).get(2).getColor().equals(c1) && down.getPixels().get(0).get(2).getColor().equals(c2);
            }
        }
        return false;
    }
    private boolean checkLeftUpCorner(String c1, String c2, String c3) {
        if (left.getPixels().get(2).get(0).getColor().equals(c1) || left.getPixels().get(2).get(0).getColor().equals(c2) || left.getPixels().get(2).get(0).getColor().equals(c3)) {
            if (left.getPixels().get(2).get(0).getColor().equals(c1)) {
                if (back.getPixels().get(0).get(2).getColor().equals(c2) && up.getPixels().get(2).get(2).getColor().equals(c3) || back.getPixels().get(0).get(2).getColor().equals(c3) && up.getPixels().get(2).get(2).getColor().equals(c2)) {
                    return true;
                }
            }
            if (left.getPixels().get(2).get(0).getColor().equals(c2)) {
                if (back.getPixels().get(0).get(2).getColor().equals(c1) && up.getPixels().get(2).get(2).getColor().equals(c3) || back.getPixels().get(0).get(2).getColor().equals(c3) && up.getPixels().get(2).get(2).getColor().equals(c1)) {
                    return true;
                }
            }
            if (left.getPixels().get(2).get(0).getColor().equals(c3)) {
                return back.getPixels().get(0).get(2).getColor().equals(c2) && up.getPixels().get(2).get(2).getColor().equals(c1) || back.getPixels().get(0).get(2).getColor().equals(c1) && up.getPixels().get(2).get(2).getColor().equals(c2);
            }
        }
        return false;
    }
    public void makeUpCorners() {
        while (!checkUp()) {
            makeFrontCorner();
            makeRightCorner();
            makeBackCorner();
            makeLeftCorner();
        }

    }
    private void makeFrontCorner() {
        boolean find = false;
        if (checkFrontDownCorner("W", "G", "O")) {
            find = true;
        }
        if (checkRightDownCorner("W", "G", "O")) {
            find = true;
        }
        if (checkBackDownCorner("W", "G", "O")) {
            find = true;
        }
        if (checkLeftDownCorner("W", "G", "O")) {
            find = true;
        }
        if (find) {
            while (!checkFrontDownCorner("W", "G", "O")) {
                rotateD(0);
            }
        } else {
            while (true) {
                if (checkFrontUpCorner("W", "G", "O")) {
                    rotateF(0);
                    rotateD(0);
                    rotateF(1);
                    break;
                }
                if (checkRightUpCorner("W", "G", "O")) {
                    rotateR(0);
                    rotateD(0);
                    rotateR(1);
                    break;
                }
                if (checkBackUpCorner("W", "G", "O")) {
                    rotateB(0);
                    rotateD(0);
                    rotateB(1);
                    break;
                }
                if (checkLeftUpCorner("W", "G", "O")) {
                    rotateL(0);
                    rotateD(0);
                    rotateL(1);
                    break;
                }
            }
            while (!checkFrontDownCorner("W", "G", "O")) {
                rotateD(0);
            }
        }
        if (front.getPixels().get(0).get(2).getColor().equals("G")) {
            rotateL(1);
            rotateD(1);
            rotateL(0);
        }
        if (front.getPixels().get(0).get(2).getColor().equals("W")) {
            rotateF(0);
            rotateD(0);
            rotateF(1);
        }
        if (front.getPixels().get(0).get(2).getColor().equals("O")) {
            rotateF(0);
            rotateR(0);
            rotateD(1);
            rotateD(1);
            rotateR(1);
            rotateF(1);
        }
    }
    private void makeRightCorner() {
        boolean find = false;
        if (checkFrontDownCorner("W", "G", "R")) {
            find = true;
        }
        if (checkRightDownCorner("W", "G", "R")) {
            find = true;
        }
        if (checkBackDownCorner("W", "G", "R")) {
            find = true;
        }
        if (checkLeftDownCorner("W", "G", "R")) {
            find = true;
        }
        if (find) {
            while (!checkRightDownCorner("W", "G", "R")) {
                rotateD(0);
            }
        } else {
            while (true) {
                if (checkFrontUpCorner("W", "G", "R")) {
                    rotateF(0);
                    rotateD(0);
                    rotateF(1);
                    break;
                }
                if (checkRightUpCorner("W", "G", "R")) {
                    rotateR(0);
                    rotateD(0);
                    rotateR(1);
                    break;
                }
                if (checkBackUpCorner("W", "G", "R")) {
                    rotateB(0);
                    rotateD(0);
                    rotateB(1);
                    break;
                }
                if (checkLeftUpCorner("W", "G", "R")) {
                    rotateL(0);
                    rotateD(0);
                    rotateL(1);
                    break;
                }
            }
            while (!checkRightDownCorner("W", "G", "R")) {
                rotateD(0);
            }
        }
        if (right.getPixels().get(0).get(0).getColor().equals("R")) {
            rotateF(1);
            rotateD(1);
            rotateF(0);
        }
        if (right.getPixels().get(0).get(0).getColor().equals("W")) {
            rotateR(0);
            rotateD(0);
            rotateR(1);
        }
        if (right.getPixels().get(0).get(0).getColor().equals("G")) {
            rotateR(0);
            rotateB(0);
            rotateD(1);
            rotateD(1);
            rotateB(1);
            rotateR(1);
        }
    }
    private void makeBackCorner() {
        boolean find = false;
        if (checkFrontDownCorner("W", "B", "R")) {
            find = true;
        }
        if (checkRightDownCorner("W", "B", "R")) {
            find = true;
        }
        if (checkBackDownCorner("W", "B", "R")) {
            find = true;
        }
        if (checkLeftDownCorner("W", "B", "R")) {
            find = true;
        }
        if (find) {
            while (!checkBackDownCorner("W", "B", "R")) {
                rotateD(0);
            }
        } else {
            while (true) {
                if (checkFrontUpCorner("W", "B", "R")) {
                    rotateF(0);
                    rotateD(0);
                    rotateF(1);
                    break;
                }
                if (checkRightUpCorner("W", "B", "R")) {
                    rotateR(0);
                    rotateD(0);
                    rotateR(1);
                    break;
                }
                if (checkBackUpCorner("W", "B", "R")) {
                    rotateB(0);
                    rotateD(0);
                    rotateB(1);
                    break;
                }
                if (checkLeftUpCorner("W", "B", "R")) {
                    rotateL(0);
                    rotateD(0);
                    rotateL(1);
                    break;
                }
            }
            while (!checkBackDownCorner("W", "B", "R")) {
                rotateD(0);
            }
        }
        if (back.getPixels().get(2).get(0).getColor().equals("B")) {
            rotateR(1);
            rotateD(1);
            rotateR(0);
        }
        if (back.getPixels().get(2).get(0).getColor().equals("W")) {
            rotateB(0);
            rotateD(0);
            rotateB(1);
        }
        if (back.getPixels().get(2).get(0).getColor().equals("R")) {
            rotateB(0);
            rotateL(0);
            rotateD(1);
            rotateD(1);
            rotateL(1);
            rotateB(1);
        }
    }
    private void makeLeftCorner() {
        boolean find = false;
        if (checkFrontDownCorner("W", "B", "O")) {
            find = true;
        }
        if (checkRightDownCorner("W", "B", "O")) {
            find = true;
        }
        if (checkBackDownCorner("W", "B", "O")) {
            find = true;
        }
        if (checkLeftDownCorner("W", "B", "O")) {
            find = true;
        }
        if (find) {
            while (!checkLeftDownCorner("W", "B", "O")) {
                rotateD(0);
            }
        } else {
            while (true) {
                if (checkFrontUpCorner("W", "B", "O")) {
                    rotateF(0);
                    rotateD(0);
                    rotateF(1);
                    break;
                }
                if (checkRightUpCorner("W", "B", "O")) {
                    rotateR(0);
                    rotateD(0);
                    rotateR(1);
                    break;
                }
                if (checkBackUpCorner("W", "B", "O")) {
                    rotateB(0);
                    rotateD(0);
                    rotateB(1);
                    break;
                }
                if (checkLeftUpCorner("W", "B", "O")) {
                    rotateL(0);
                    rotateD(0);
                    rotateL(1);
                    break;
                }
            }
            while (!checkLeftDownCorner("W", "B", "O")) {
                rotateD(0);
            }
        }
        if (left.getPixels().get(2).get(2).getColor().equals("O")) {
            rotateB(1);
            rotateD(1);
            rotateB(0);
        }
        if (left.getPixels().get(2).get(2).getColor().equals("W")) {
            rotateL(0);
            rotateD(0);
            rotateL(1);
        }
        if (left.getPixels().get(2).get(2).getColor().equals("B")) {
            rotateL(0);
            rotateF(0);
            rotateD(1);
            rotateD(1);
            rotateF(1);
            rotateL(1);
        }
    }
    public void makeO2() {
        makeFrontLeft();
        makeFrontRight();
        makeBackLeft();
        makeBackRight();
    }
    private void makeFrontLeft() {
        boolean find = false;
        if (front.getPixels().get(0).get(1).getColor().equals("G") && down.getPixels().get(2).get(1).getColor().equals("O") ||
                front.getPixels().get(0).get(1).getColor().equals("O") && down.getPixels().get(2).get(1).getColor().equals("G")) {
            find = true;
        }
        if (right.getPixels().get(1).get(0).getColor().equals("G") && down.getPixels().get(1).get(0).getColor().equals("O") ||
                right.getPixels().get(1).get(0).getColor().equals("O") && down.getPixels().get(1).get(0).getColor().equals("G")) {
            find = true;
        }
        if (back.getPixels().get(2).get(1).getColor().equals("G") && down.getPixels().get(0).get(1).getColor().equals("O") ||
                back.getPixels().get(2).get(1).getColor().equals("O") && down.getPixels().get(0).get(1).getColor().equals("G")) {
            find = true;
        }
        if (left.getPixels().get(1).get(2).getColor().equals("G") && down.getPixels().get(1).get(2).getColor().equals("O") ||
                left.getPixels().get(1).get(2).getColor().equals("O") && down.getPixels().get(1).get(2).getColor().equals("G")) {
            find = true;
        }
        if (find) {
            while (!(front.getPixels().get(0).get(1).getColor().equals("G") && down.getPixels().get(2).get(1).getColor().equals("O") ||
                    front.getPixels().get(0).get(1).getColor().equals("O") && down.getPixels().get(2).get(1).getColor().equals("G"))) {
                rotateD(0);
            }
        } else {
            while (true) {
                if (front.getPixels().get(1).get(0).getColor().equals("G") && right.getPixels().get(0).get(1).getColor().equals("O") ||
                        front.getPixels().get(1).get(0).getColor().equals("O") && right.getPixels().get(0).get(1).getColor().equals("G")) {
                    rotateD(0);
                    rotateR(0);
                    rotateD(1);
                    rotateR(1);
                    rotateD(1);
                    rotateF(1);
                    rotateD(0);
                    rotateF(0);
                    break;
                }
                if (front.getPixels().get(1).get(2).getColor().equals("G") && left.getPixels().get(0).get(1).getColor().equals("O") ||
                        front.getPixels().get(1).get(2).getColor().equals("O") && left.getPixels().get(0).get(1).getColor().equals("G")) {
                    rotateD(1);
                    rotateL(1);
                    rotateD(0);
                    rotateL(0);
                    rotateD(0);
                    rotateF(0);
                    rotateD(1);
                    rotateF(1);
                    break;
                }

                if (right.getPixels().get(2).get(1).getColor().equals("G") && back.getPixels().get(1).get(0).getColor().equals("O") ||
                        right.getPixels().get(2).get(1).getColor().equals("O") && back.getPixels().get(1).get(0).getColor().equals("G")) {
                    rotateD(0);
                    rotateB(0);
                    rotateD(1);
                    rotateB(1);
                    rotateD(1);
                    rotateR(1);
                    rotateD(0);
                    rotateR(0);
                    break;
                }

                if (back.getPixels().get(1).get(2).getColor().equals("G") && left.getPixels().get(2).get(1).getColor().equals("O") ||
                        back.getPixels().get(1).get(2).getColor().equals("O") && left.getPixels().get(2).get(1).getColor().equals("G")) {
                    rotateD(0);
                    rotateL(0);
                    rotateD(1);
                    rotateL(1);
                    rotateD(1);
                    rotateB(1);
                    rotateD(0);
                    rotateB(0);
                    break;
                }
            }
            while (!(front.getPixels().get(0).get(1).getColor().equals("G") && down.getPixels().get(2).get(1).getColor().equals("O") ||
                    front.getPixels().get(0).get(1).getColor().equals("O") && down.getPixels().get(2).get(1).getColor().equals("G"))) {
                rotateD(0);
            }
        }
        if (front.getPixels().get(0).get(1).getColor().equals("G")){
            rotateD(1);
            rotateL(1);
            rotateD(0);
            rotateL(0);
            rotateD(0);
            rotateF(0);
            rotateD(1);
            rotateF(1);
        } else {
            rotateD(0);
            rotateD(0);
            rotateF(0);
            rotateD(1);
            rotateF(1);
            rotateD(1);
            rotateL(1);
            rotateD(0);
            rotateL(0);
        }
    }
    private void makeFrontRight() {
        boolean find = false;
        if (front.getPixels().get(0).get(1).getColor().equals("G") && down.getPixels().get(2).get(1).getColor().equals("R") ||
                front.getPixels().get(0).get(1).getColor().equals("R") && down.getPixels().get(2).get(1).getColor().equals("G")) {
            find = true;
        }
        if (right.getPixels().get(1).get(0).getColor().equals("G") && down.getPixels().get(1).get(0).getColor().equals("R") ||
                right.getPixels().get(1).get(0).getColor().equals("R") && down.getPixels().get(1).get(0).getColor().equals("G")) {
            find = true;
        }
        if (back.getPixels().get(2).get(1).getColor().equals("G") && down.getPixels().get(0).get(1).getColor().equals("R") ||
                back.getPixels().get(2).get(1).getColor().equals("R") && down.getPixels().get(0).get(1).getColor().equals("G")) {
            find = true;
        }
        if (left.getPixels().get(1).get(2).getColor().equals("G") && down.getPixels().get(1).get(2).getColor().equals("R") ||
                left.getPixels().get(1).get(2).getColor().equals("R") && down.getPixels().get(1).get(2).getColor().equals("G")) {
            find = true;
        }
        if (find) {
            while (!(front.getPixels().get(0).get(1).getColor().equals("G") && down.getPixels().get(2).get(1).getColor().equals("R") ||
                    front.getPixels().get(0).get(1).getColor().equals("R") && down.getPixels().get(2).get(1).getColor().equals("G"))) {
                rotateD(0);
            }
        } else {
            while (true) {
                if (front.getPixels().get(1).get(0).getColor().equals("G") && right.getPixels().get(0).get(1).getColor().equals("R") ||
                        front.getPixels().get(1).get(0).getColor().equals("R") && right.getPixels().get(0).get(1).getColor().equals("G")) {
                    rotateD(0);
                    rotateR(0);
                    rotateD(1);
                    rotateR(1);
                    rotateD(1);
                    rotateF(1);
                    rotateD(0);
                    rotateF(0);
                    break;
                }
                if (front.getPixels().get(1).get(2).getColor().equals("G") && left.getPixels().get(0).get(1).getColor().equals("R") ||
                        front.getPixels().get(1).get(2).getColor().equals("R") && left.getPixels().get(0).get(1).getColor().equals("G")) {
                    rotateD(1);
                    rotateL(1);
                    rotateD(0);
                    rotateL(0);
                    rotateD(0);
                    rotateF(0);
                    rotateD(1);
                    rotateF(1);
                    break;
                }

                if (right.getPixels().get(2).get(1).getColor().equals("G") && back.getPixels().get(1).get(0).getColor().equals("R") ||
                        right.getPixels().get(2).get(1).getColor().equals("R") && back.getPixels().get(1).get(0).getColor().equals("G")) {
                    rotateD(0);
                    rotateB(0);
                    rotateD(1);
                    rotateB(1);
                    rotateD(1);
                    rotateR(1);
                    rotateD(0);
                    rotateR(0);
                    break;
                }

                if (back.getPixels().get(1).get(2).getColor().equals("G") && left.getPixels().get(2).get(1).getColor().equals("R") ||
                        back.getPixels().get(1).get(2).getColor().equals("R") && left.getPixels().get(2).get(1).getColor().equals("G")) {
                    rotateD(0);
                    rotateL(0);
                    rotateD(1);
                    rotateL(1);
                    rotateD(1);
                    rotateB(1);
                    rotateD(0);
                    rotateB(0);
                    break;
                }
            }
            while (!(front.getPixels().get(0).get(1).getColor().equals("G") && down.getPixels().get(2).get(1).getColor().equals("R") ||
                    front.getPixels().get(0).get(1).getColor().equals("R") && down.getPixels().get(2).get(1).getColor().equals("G"))) {
                rotateD(0);
            }
        }
        if (front.getPixels().get(0).get(1).getColor().equals("G")){
            rotateD(0);
            rotateR(0);
            rotateD(1);
            rotateR(1);
            rotateD(1);
            rotateF(1);
            rotateD(0);
            rotateF(0);
        } else {
            rotateD(1);
            rotateD(1);
            rotateF(1);
            rotateD(0);
            rotateF(0);
            rotateD(0);
            rotateR(0);
            rotateD(1);
            rotateR(1);
        }
    }
    private void makeBackLeft() {
        boolean find = false;
        if (front.getPixels().get(0).get(1).getColor().equals("B") && down.getPixels().get(2).get(1).getColor().equals("R") ||
                front.getPixels().get(0).get(1).getColor().equals("R") && down.getPixels().get(2).get(1).getColor().equals("B")) {
            find = true;
        }
        if (right.getPixels().get(1).get(0).getColor().equals("B") && down.getPixels().get(1).get(0).getColor().equals("R") ||
                right.getPixels().get(1).get(0).getColor().equals("R") && down.getPixels().get(1).get(0).getColor().equals("B")) {
            find = true;
        }
        if (back.getPixels().get(2).get(1).getColor().equals("B") && down.getPixels().get(0).get(1).getColor().equals("R") ||
                back.getPixels().get(2).get(1).getColor().equals("R") && down.getPixels().get(0).get(1).getColor().equals("B")) {
            find = true;
        }
        if (left.getPixels().get(1).get(2).getColor().equals("B") && down.getPixels().get(1).get(2).getColor().equals("R") ||
                left.getPixels().get(1).get(2).getColor().equals("R") && down.getPixels().get(1).get(2).getColor().equals("B")) {
            find = true;
        }
        if (find) {
            while (!(back.getPixels().get(2).get(1).getColor().equals("B") && down.getPixels().get(0).get(1).getColor().equals("R") ||
                    back.getPixels().get(2).get(1).getColor().equals("R") && down.getPixels().get(0).get(1).getColor().equals("B"))) {
                rotateD(0);
            }
        } else {
            while (true) {
                if (front.getPixels().get(1).get(0).getColor().equals("B") && right.getPixels().get(0).get(1).getColor().equals("R") ||
                        front.getPixels().get(1).get(0).getColor().equals("R") && right.getPixels().get(0).get(1).getColor().equals("B")) {
                    rotateD(0);
                    rotateR(0);
                    rotateD(1);
                    rotateR(1);
                    rotateD(1);
                    rotateF(1);
                    rotateD(0);
                    rotateF(0);
                    break;
                }
                if (front.getPixels().get(1).get(2).getColor().equals("B") && left.getPixels().get(0).get(1).getColor().equals("R") ||
                        front.getPixels().get(1).get(2).getColor().equals("R") && left.getPixels().get(0).get(1).getColor().equals("B")) {
                    rotateD(1);
                    rotateL(1);
                    rotateD(0);
                    rotateL(0);
                    rotateD(0);
                    rotateF(0);
                    rotateD(1);
                    rotateF(1);
                    break;
                }

                if (right.getPixels().get(2).get(1).getColor().equals("B") && back.getPixels().get(1).get(0).getColor().equals("R") ||
                        right.getPixels().get(2).get(1).getColor().equals("R") && back.getPixels().get(1).get(0).getColor().equals("B")) {
                    rotateD(0);
                    rotateB(0);
                    rotateD(1);
                    rotateB(1);
                    rotateD(1);
                    rotateR(1);
                    rotateD(0);
                    rotateR(0);
                    break;
                }

                if (back.getPixels().get(1).get(2).getColor().equals("B") && left.getPixels().get(2).get(1).getColor().equals("R") ||
                        back.getPixels().get(1).get(2).getColor().equals("R") && left.getPixels().get(2).get(1).getColor().equals("B")) {
                    rotateD(0);
                    rotateL(0);
                    rotateD(1);
                    rotateL(1);
                    rotateD(1);
                    rotateB(1);
                    rotateD(0);
                    rotateB(0);
                    break;
                }
            }
            while (!(back.getPixels().get(2).get(1).getColor().equals("B") && down.getPixels().get(0).get(1).getColor().equals("R") ||
                    back.getPixels().get(2).get(1).getColor().equals("R") && down.getPixels().get(0).get(1).getColor().equals("B"))) {
                rotateD(0);
            }
        }
        if (back.getPixels().get(2).get(1).getColor().equals("B")){
            rotateD(1);
            rotateR(1);
            rotateD(0);
            rotateR(0);
            rotateD(0);
            rotateB(0);
            rotateD(1);
            rotateB(1);
        } else {
            rotateD(0);
            rotateD(0);
            rotateB(0);
            rotateD(1);
            rotateB(1);
            rotateD(1);
            rotateR(1);
            rotateD(0);
            rotateR(0);
        }
    }
    private void makeBackRight() {
        boolean find = false;
        if (front.getPixels().get(0).get(1).getColor().equals("B") && down.getPixels().get(2).get(1).getColor().equals("O") ||
                front.getPixels().get(0).get(1).getColor().equals("O") && down.getPixels().get(2).get(1).getColor().equals("B")) {
            find = true;
        }
        if (right.getPixels().get(1).get(0).getColor().equals("B") && down.getPixels().get(1).get(0).getColor().equals("O") ||
                right.getPixels().get(1).get(0).getColor().equals("O") && down.getPixels().get(1).get(0).getColor().equals("B")) {
            find = true;
        }
        if (back.getPixels().get(2).get(1).getColor().equals("B") && down.getPixels().get(0).get(1).getColor().equals("O") ||
                back.getPixels().get(2).get(1).getColor().equals("O") && down.getPixels().get(0).get(1).getColor().equals("B")) {
            find = true;
        }
        if (left.getPixels().get(1).get(2).getColor().equals("B") && down.getPixels().get(1).get(2).getColor().equals("O") ||
                left.getPixels().get(1).get(2).getColor().equals("O") && down.getPixels().get(1).get(2).getColor().equals("B")) {
            find = true;
        }
        if (find) {
            while (!(back.getPixels().get(2).get(1).getColor().equals("B") && down.getPixels().get(0).get(1).getColor().equals("O") ||
                    back.getPixels().get(2).get(1).getColor().equals("O") && down.getPixels().get(0).get(1).getColor().equals("B"))) {
                rotateD(0);
            }
        } else {
            while (true) {
                if (front.getPixels().get(1).get(0).getColor().equals("B") && right.getPixels().get(0).get(1).getColor().equals("O") ||
                        front.getPixels().get(1).get(0).getColor().equals("O") && right.getPixels().get(0).get(1).getColor().equals("B")) {
                    rotateD(0);
                    rotateR(0);
                    rotateD(1);
                    rotateR(1);
                    rotateD(1);
                    rotateF(1);
                    rotateD(0);
                    rotateF(0);
                    break;
                }
                if (front.getPixels().get(1).get(2).getColor().equals("B") && left.getPixels().get(0).get(1).getColor().equals("O") ||
                        front.getPixels().get(1).get(2).getColor().equals("O") && left.getPixels().get(0).get(1).getColor().equals("B")) {
                    rotateD(1);
                    rotateL(1);
                    rotateD(0);
                    rotateL(0);
                    rotateD(0);
                    rotateF(0);
                    rotateD(1);
                    rotateF(1);
                    break;
                }

                if (right.getPixels().get(2).get(1).getColor().equals("B") && back.getPixels().get(1).get(0).getColor().equals("O") ||
                        right.getPixels().get(2).get(1).getColor().equals("O") && back.getPixels().get(1).get(0).getColor().equals("B")) {
                    rotateD(0);
                    rotateB(0);
                    rotateD(1);
                    rotateB(1);
                    rotateD(1);
                    rotateR(1);
                    rotateD(0);
                    rotateR(0);
                    break;
                }

                if (back.getPixels().get(1).get(2).getColor().equals("B") && left.getPixels().get(2).get(1).getColor().equals("O") ||
                        back.getPixels().get(1).get(2).getColor().equals("O") && left.getPixels().get(2).get(1).getColor().equals("B")) {
                    rotateD(0);
                    rotateL(0);
                    rotateD(1);
                    rotateL(1);
                    rotateD(1);
                    rotateB(1);
                    rotateD(0);
                    rotateB(0);
                    break;
                }
            }
            while (!(back.getPixels().get(2).get(1).getColor().equals("B") && down.getPixels().get(0).get(1).getColor().equals("O") ||
                    back.getPixels().get(2).get(1).getColor().equals("O") && down.getPixels().get(0).get(1).getColor().equals("B"))) {
                rotateD(0);
            }
        }
        if (back.getPixels().get(2).get(1).getColor().equals("B")){
            rotateD(0);
            rotateL(0);
            rotateD(1);
            rotateL(1);
            rotateD(1);
            rotateB(1);
            rotateD(0);
            rotateB(0);
        } else {
            rotateD(1);
            rotateD(1);
            rotateB(1);
            rotateD(0);
            rotateB(0);
            rotateD(0);
            rotateL(0);
            rotateD(1);
            rotateL(1);
        }
    }
    public void makeYellowCross() {
        makeWrongYellowCross();
        makeRightYellowCross();
        while (!(front.getPixels().get(0).get(1).getColor().equals("G"))) {
            rotateD(1);
        }
    }
    private void makeWrongYellowCross() {
        while (!(front.getPixels().get(0).get(1).getColor().equals("Y") && down.getPixels().get(2).get(1).getColor().equals("G") ||
                front.getPixels().get(0).get(1).getColor().equals("G") && down.getPixels().get(2).get(1).getColor().equals("Y")) || !(left.getPixels().get(1).get(2).getColor().equals("Y") && down.getPixels().get(1).get(2).getColor().equals("O") ||
                left.getPixels().get(1).get(2).getColor().equals("O") && down.getPixels().get(1).get(2).getColor().equals("Y")) || !(back.getPixels().get(2).get(1).getColor().equals("Y") && down.getPixels().get(0).get(1).getColor().equals("B") ||
                back.getPixels().get(2).get(1).getColor().equals("B") && down.getPixels().get(0).get(1).getColor().equals("Y")) || !(right.getPixels().get(0).get(1).getColor().equals("Y") && down.getPixels().get(2).get(1).getColor().equals("R") ||
                right.getPixels().get(0).get(1).getColor().equals("R") && down.getPixels().get(2).get(1).getColor().equals("Y"))) {
            makeWrongGreenCross();
            makeWrongOrangeCross();
            makeWrongBlueCross();
            makeWrongRedCross();
        }
    }
    private void makeWrongGreenCross() {
        if (!(front.getPixels().get(0).get(1).getColor().equals("Y") && down.getPixels().get(2).get(1).getColor().equals("G") ||
                front.getPixels().get(0).get(1).getColor().equals("G") && down.getPixels().get(2).get(1).getColor().equals("Y"))) {
            rotateD(1);
            rotateF(1);
            rotateL(1);
            rotateD(1);
            rotateL(0);
            rotateD(0);
            rotateF(0);
        }
    }
    private void makeWrongOrangeCross() {
        if (!(left.getPixels().get(1).get(2).getColor().equals("Y") && down.getPixels().get(1).get(2).getColor().equals("O") ||
                left.getPixels().get(1).get(2).getColor().equals("O") && down.getPixels().get(1).get(2).getColor().equals("Y"))) {
            rotateD(1);
            rotateL(1);
            rotateB(1);
            rotateD(1);
            rotateB(0);
            rotateD(0);
            rotateL(0);
        }
    }
    private void makeWrongBlueCross() {
        if (!(back.getPixels().get(2).get(1).getColor().equals("Y") && down.getPixels().get(0).get(1).getColor().equals("B") ||
                back.getPixels().get(2).get(1).getColor().equals("B") && down.getPixels().get(0).get(1).getColor().equals("Y"))) {
            rotateD(1);
            rotateB(1);
            rotateR(1);
            rotateD(1);
            rotateR(0);
            rotateD(0);
            rotateB(0);
        }
    }
    private void makeWrongRedCross() {
        if (!(right.getPixels().get(0).get(1).getColor().equals("Y") && down.getPixels().get(2).get(1).getColor().equals("R") ||
                right.getPixels().get(0).get(1).getColor().equals("R") && down.getPixels().get(2).get(1).getColor().equals("Y"))) {
            rotateD(1);
            rotateR(1);
            rotateF(1);
            rotateD(1);
            rotateF(0);
            rotateD(0);
            rotateR(0);
        }
    }
    private void makeRightYellowCross() {
        if (down.getPixels().get(2).get(1).getColor().equals("G")) {
            makeFrontCrossRotates();
            rotateD(0);
        }
        if (down.getPixels().get(1).get(2).getColor().equals("O")) {
            makeLeftCrossRotates();
            rotateD(0);
            makeLeftCrossRotates();
        }
        if (down.getPixels().get(0).get(1).getColor().equals("B")) {
            makeBackCrossRotates();
            rotateD(0);
            makeBackCrossRotates();
        }
        if (down.getPixels().get(1).get(0).getColor().equals("R")) {
            makeRightCrossRotates();
            rotateD(0);
            makeRightCrossRotates();
        }
        while (!(front.getPixels().get(0).get(1).getColor().equals("G")) || down.getPixels().get(2).get(1).getColor().equals("G") ) {
            rotateD(1);
        }
        if (down.getPixels().get(2).get(1).getColor().equals("G") || down.getPixels().get(1).get(0).getColor().equals("R") || down.getPixels().get(0).get(1).getColor().equals("B") || down.getPixels().get(1).get(2).getColor().equals("O")) {
            makeRightYellowCross();
        }
    }
    private void makeFrontCrossRotates() {
        rotateL(1);
        rotateE(0);
        rotateL(1);
        rotateE(0);
        rotateL(1);
        rotateE(0);
        rotateL(1);
        rotateE(0);
    }
    private void makeLeftCrossRotates() {
        rotateB(1);
        rotateE(0);
        rotateB(1);
        rotateE(0);
        rotateB(1);
        rotateE(0);
        rotateB(1);
        rotateE(0);
    }
    private void makeBackCrossRotates() {
        rotateR(1);
        rotateE(0);
        rotateR(1);
        rotateE(0);
        rotateR(1);
        rotateE(0);
        rotateR(1);
        rotateE(0);
    }
    private void makeRightCrossRotates() {
        rotateF(1);
        rotateE(0);
        rotateF(1);
        rotateE(0);
        rotateF(1);
        rotateE(0);
        rotateF(1);
        rotateE(0);
    }
    public void makeDownCorners() {
        makeWrongDownCorners();
        makeGoodDownCorners();
    }
    private void makeWrongDownCorners() {
        if (!checkFrontDownCorner("Y", "G", "O") || !checkBackDownCorner("B", "Y", "R") || !checkLeftDownCorner("Y",  "B", "O") || !checkRightDownCorner("G", "Y", "R")) {
            if (checkFrontDownCorner("Y", "G", "O")) {
                rotateD(1);
                rotateL(1);
                rotateD(0);
                rotateR(0);
                rotateD(1);
                rotateL(0);
                rotateD(0);
                rotateR(1);
            } else {
                if (checkBackDownCorner("B", "Y", "R")) {
                    rotateD(1);
                    rotateR(1);
                    rotateD(0);
                    rotateL(0);
                    rotateD(1);
                    rotateR(0);
                    rotateD(0);
                    rotateL(1);
                } else {
                    if (checkLeftDownCorner("Y",  "B", "O")) {
                        rotateD(1);
                        rotateB(1);
                        rotateD(0);
                        rotateF(0);
                        rotateD(1);
                        rotateB(0);
                        rotateD(0);
                        rotateF(1);
                    } else {
                        if (checkRightDownCorner("G", "Y", "R")) {
                            rotateD(1);
                            rotateF(1);
                            rotateD(0);
                            rotateB(0);
                            rotateD(1);
                            rotateF(0);
                            rotateD(0);
                            rotateB(1);
                        } else {
                            rotateD(1);
                            rotateL(1);
                            rotateD(0);
                            rotateR(0);
                            rotateD(1);
                            rotateL(0);
                            rotateD(0);
                            rotateR(1);
                        }
                    }
                }
            }
            makeWrongDownCorners();
        }
    }
    public void makeGoodDownCorners() {
        while (!(checkFrontDownCorner("Y", "G", "O") && down.getPixels().get(2).get(2).getColor().equals("Y"))) {
            rotateL(0);
            rotateU(0);
            rotateL(1);
            rotateU(1);
        }
        rotateD(1);
        while (!(checkFrontDownCorner("Y", "B", "O") && down.getPixels().get(2).get(2).getColor().equals("Y"))) {
            rotateL(0);
            rotateU(0);
            rotateL(1);
            rotateU(1);
        }
        rotateD(1);
        while (!(checkFrontDownCorner("Y", "B", "R") && down.getPixels().get(2).get(2).getColor().equals("Y"))) {
            rotateL(0);
            rotateU(0);
            rotateL(1);
            rotateU(1);
        }
        rotateD(1);
        while (!(checkFrontDownCorner("Y", "G", "R") && down.getPixels().get(2).get(2).getColor().equals("Y"))) {
            rotateL(0);
            rotateU(0);
            rotateL(1);
            rotateU(1);
        }
        rotateD(1);
    }

    public void solve() {
        this.shuffle();
        this.makeCross();
        this.makeUpCorners();
        this.makeO2();
        this.makeYellowCross();
        this.makeDownCorners();
    }

    public void showCube() {
        // Показ начинается с передней грани и т.д
        front.showEdge("Front");
        up.showEdge("Up");
        back.showEdge("Back");
        down.showEdge("Down");
        left.showEdge("Left");
        right.showEdge("Right");
    }
}