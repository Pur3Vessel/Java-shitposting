public class Pixel { // класс клетки на кубике
    private String color; // у каждой клетки кубика есть свой цвет

    public Pixel(String color) {
        this.color = color; // задаем цвет клетки в конструкторе
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}