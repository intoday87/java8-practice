import lombok.Builder;
import lombok.Getter;

@Builder
public class Apple {
    @Getter
    private int weight;
    @Getter
    private Color color;
}
