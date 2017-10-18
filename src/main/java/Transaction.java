import lombok.Getter;

@Getter
public class Transaction {
    private Trader trader;
    private int year;
    private int value;

    public Transaction(Trader trader, int year, int value) {
        this.trader = trader;
        this.year = year;
        this.value = value;
    }

    @Override
    public String toString() {
        return "{" + this.trader + ", " +
                "year:" + this.year + ", " +
                "value:" + this.value + "}";
    }
}
