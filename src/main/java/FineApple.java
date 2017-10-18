import lombok.Getter;

public class FineApple {
    @Getter
    private int weight;
    @Getter
    private Color color;

    public FineApple(int weight) {
        this.weight = weight;
    }

    public FineApple(Color color) {
        this.color = color;
    }

    public FineApple(int weight, Color color) {
        this.weight = weight;
        this.color = color;
    }
}
