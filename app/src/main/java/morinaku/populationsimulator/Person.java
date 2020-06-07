package morinaku.populationsimulator;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private List<Trait> tList;
    int generation;
    private boolean deceased = false;

    public Person(int a){
        this.generation = a;
        this.tList = new ArrayList<>();
    }

    public Person(int a, int b) {
        this.generation = a;
        this.tList = new ArrayList<>();
    }

    public int getGeneration() {
        return generation;
    }
    public List<Trait> getTraitList() {
        return tList;
    }
    public boolean getDeceased() {
        return deceased;
    }

    public void addTrait(Trait a) {
        tList.add(a);
    }
    public void setDeceased() {
        deceased = true;
    }

    public boolean compareTrait(Trait a) {
        for(int i=0;i<tList.size();i++) {
            if(tList.get(i).getID() == a.getID()) {
                return true;
            }
        }
        return false;
    }
}