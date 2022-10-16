package atm.money;

public record Banknote(int nominal, String name) implements Comparable<Banknote>, Cloneable {

    @Override
    public String toString() {
        return "Banknote[" +
                "nominal=" + nominal + ", " +
                "name=" + name + ']';
    }

    @Override
    public int compareTo(Banknote o) {
        return this.nominal - o.nominal;
    }


    @Override
    public Banknote clone() {
        try {
            return (Banknote) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
