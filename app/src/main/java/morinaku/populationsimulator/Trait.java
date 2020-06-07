package morinaku.populationsimulator;

public class Trait implements Cloneable {
    int ID;
    int value;
    String info;

    public Trait(int a, int b, String c) {
        this.ID = a;
        this.value = b;
        this.info = c;
    }

    public int getID() {
        return ID;
    }
    public int getValue() {
        return value;
    }
    public String getInfo() {
        return info;
    }

    public void setValue(int a) {
        value = a;
    }
    public void adjValue(int a) {
        value += a;
    }

    @Override
    protected Trait clone() throws CloneNotSupportedException {
        Trait clone = null;
        try
        {
            clone = (Trait) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new RuntimeException(e);
        }
        return clone;
    }
}