package morinaku.populationsimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private int INIT_GEN = 100;
    private List<Person> populus;
    private int GEN_COUNT = 0;
    private int DEATHS = 0;
    private int BORN = 0;
    private Random r;
    private DecimalFormat df = new DecimalFormat("0.0");

    private List<Trait> MASTER_T_LIST;
    private int LIST_LOC = 0;

    private int hasX2 = 0;

    Button reset, trait, next;
    TextView updates, lz, zero, one, two, three, four, five, six, seven, eight, nine, ten, gt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populus = new ArrayList<>();
        r = new Random(System.currentTimeMillis());

        for(int i=0;i<INIT_GEN;i++) {
            Person temp = new Person(0);
            Trait a = new Trait(0,r.nextInt(10)+1,"Base");
            Trait b = new Trait(1,r.nextInt(10)+1,"Extra");
            temp.addTrait(a);
            temp.addTrait(b);
            populus.add(temp);
        }

        MASTER_T_LIST = new ArrayList<>();
        MASTER_T_LIST.add(new Trait(0,-1,"Base"));
        MASTER_T_LIST.add(new Trait(1,-1,"Extra"));
        MASTER_T_LIST.add(new Trait(2,-1,"Test"));

        trait = findViewById(R.id.trait);
        next = findViewById(R.id.next);
        reset = findViewById(R.id.reset);

        updates = findViewById(R.id.updates);
        lz = findViewById(R.id.less0);
        zero = findViewById(R.id.zero);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);
        seven = findViewById(R.id.seven);
        eight = findViewById(R.id.eight);
        nine = findViewById(R.id.nine);
        ten = findViewById(R.id.ten);
        gt = findViewById(R.id.great10);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populus.clear();
                DEATHS = 0;
                BORN = 0;

                for(int i=0; i<INIT_GEN; i++) {
                    Person temp = new Person(0);
                    Trait b = new Trait(0,r.nextInt(10)+1,"Base");
                    Trait c = new Trait(1,r.nextInt(10)+1,"Extra");
                    temp.addTrait(b);
                    temp.addTrait(c);
                    populus.add(temp);
                }

                GEN_COUNT = 0;
                trait.setText("Base");
                updates.setText("");
                updates.append("Oldest Generation: 0\n");
                if(DEATHS > 0) {
                    updates.append("Generation "+numberAdjustment(GEN_COUNT)+"\nPopulation Total: "+numberAdjustment(getAlive())+"\nBorn This Generation: "+numberAdjustment(BORN)+"\nDied This Generation: "+numberAdjustment(DEATHS)+"\n\n");
                } else {
                    updates.append("Generation "+numberAdjustment(GEN_COUNT)+"\nPopulation Total: "+numberAdjustment(getAlive())+"\n\n");
                }
                updateFields("Base");
                LIST_LOC = 0;
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get parents paired up
                try {
                    new ChildMaker().execute().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Simulate deaths
                DEATHS = 0;
                for(int j=0;j<populus.size();j++) {
                    if(populus.get(j).getGeneration() < GEN_COUNT-2) {
                        populus.get(j).setDeceased();
                        DEATHS++;
                    } else if(r.nextBoolean()) {
                        populus.get(j).setDeceased();
                        DEATHS++;
                    }
                }

                //Get oldest generation alive
                int gen_cap = -1;
                for(int i=0;i<populus.size();i++) {
                    if(!populus.get(i).getDeceased()) {
                        gen_cap = populus.get(i).getGeneration();
                        break;
                    }
                }

                GEN_COUNT++;
                trait.setText("Base");
                updateFields("Base");
                updates.append("Oldest Generation: "+numberAdjustment(gen_cap)+"\n");

                double percentile = (double)hasX2 / populus.size();
                percentile *= 100;

                updates.append("Percentage X2: "+df.format(percentile)+"%\n");
                if(DEATHS > 0) {
                    updates.append("Generation "+numberAdjustment(GEN_COUNT)+"\nPopulation Total: "+numberAdjustment(getAlive())+"\nBorn This Generation: "+numberAdjustment(BORN)+"\nDied This Generation: "+numberAdjustment(DEATHS)+"\n\n");
                } else {
                    updates.append("Generation "+numberAdjustment(GEN_COUNT)+"\nPopulation Total: "+numberAdjustment(getAlive())+"\n\n");
                }
                LIST_LOC = 0;
            }
        });
        trait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LIST_LOC++;
                if(LIST_LOC == MASTER_T_LIST.size())
                    LIST_LOC = 0;
                updateFields(MASTER_T_LIST.get(LIST_LOC).getInfo());
                trait.setText(MASTER_T_LIST.get(LIST_LOC).getInfo());
            }
        });

        GEN_COUNT = 0;
        trait.setText("Base");
        updates.setText("");
        updates.append("Oldest Generation: 0\n");
        if(DEATHS > 0) {
            updates.append("Generation "+numberAdjustment(GEN_COUNT)+"\nPopulation Total: "+numberAdjustment(getAlive())+"\nBorn This Generation: "+numberAdjustment(BORN)+"\nDied This Generation: "+numberAdjustment(DEATHS)+"\n\n");
        } else {
            updates.append("Generation "+numberAdjustment(GEN_COUNT)+"\nPopulation Total: "+numberAdjustment(getAlive())+"\n\n");
        }
        updateFields("Base");
        LIST_LOC = 0;
    }

    private int getAlive() {
        int alive = 0;

        for(int i=0;i<populus.size();i++) {
            if(!populus.get(i).getDeceased()) {
                alive++;
            }
        }

        return alive;
    }

    private String[] numbersAdjustment(int[] values) {
        String[] adjValues = new String[values.length];
        for(int i=0;i<values.length;i++) {
            if (values[i] >= 1000 && values[i] < 1000000) {
                adjValues[i] = df.format((double) values[i] / 1000) + "k";
            } else if (values[i] >= 1000000) {
                String val = String.valueOf(values[i]).trim();
                String[] vals = val.split("");
                adjValues[i] = vals[1]+"."+vals[2]+vals[3]+"e"+(vals.length-1);
            } else {
                adjValues[i] = String.valueOf(values[i]);
            }
        }
        return adjValues;
    }

    private String numberAdjustment(int values) {
        String adjValues = "";
        if(values >= 1000 && values < 1000000) {
            adjValues = df.format((double)values/1000)+"k";
        } else if (values >= 1000000) {
            String val = String.valueOf(values).trim();
            String[] vals = val.split("");
            adjValues = vals[1]+"."+vals[2]+vals[3]+"e"+(vals.length-1);
        } else {
            adjValues = String.valueOf(values);
        }
        return adjValues;
    }

    private void updateFields(String trait) {
        int[] values = new int[13];
        for (Person x : populus) {
            if (x.getDeceased())
                continue;
            for (Trait y : x.getTraitList()) {
                if (y.getInfo().equals("Test"))
                    hasX2++;
                if (!y.getInfo().equals(trait))
                    continue;
                switch (y.getValue()) {
                    case 0:
                        values[1]++;
                        break;
                    case 1:
                        values[2]++;
                        break;
                    case 2:
                        values[3]++;
                        break;
                    case 3:
                        values[4]++;
                        break;
                    case 4:
                        values[5]++;
                        break;
                    case 5:
                        values[6]++;
                        break;
                    case 6:
                        values[7]++;
                        break;
                    case 7:
                        values[8]++;
                        break;
                    case 8:
                        values[9]++;
                        break;
                    case 9:
                        values[10]++;
                        break;
                    case 10:
                        values[11]++;
                        break;
                    default:
                        if (y.getValue() > 10) {
                            values[12]++;
                        } else if (y.getValue() < 0) {
                            values[0]++;
                        }
                        break;
                }
            }
        }

        //Adjust values for excessive size
        String[] adjValues = new String[13];
        adjValues = numbersAdjustment(values);

        lz.setText(adjValues[0]);
        zero.setText(adjValues[1]);
        one.setText(adjValues[2]);
        two.setText(adjValues[3]);
        three.setText(adjValues[4]);
        four.setText(adjValues[5]);
        five.setText(adjValues[6]);
        six.setText(adjValues[7]);
        seven.setText(adjValues[8]);
        eight.setText(adjValues[9]);
        nine.setText(adjValues[10]);
        ten.setText(adjValues[11]);
        gt.setText(adjValues[12]);
    }

    private class ChildMaker extends AsyncTask<Void, Void, Void> {
        Person husband, wife;

        @Override
        protected void onPreExecute() {
            updates.append("Parents procreating...\n");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            BORN = 0;
            for(int i=0;i<populus.size();i++) {
                if (i % 2 == 0 && populus.get(i).getGeneration() == GEN_COUNT) {
                    husband = populus.get(i);
                }
                if (i % 2 == 1 && populus.get(i).getGeneration() == GEN_COUNT && husband != null) {
                    wife = populus.get(i);
                    int opt = r.nextInt(5) + 1;
                    int change = r.nextInt(3);

                    BORN += opt;

                    //Get list of parent traits
                    List<Trait> traits = new ArrayList<>();
                    traits.addAll(husband.getTraitList());
                    for (Trait a : wife.getTraitList()) {
                        if (!husband.compareTrait(a)) {
                            traits.add(a);
                        }
                    }
                    for (int j = 0; j < opt; j++) {
                        Person child = new Person(GEN_COUNT + 1);
                        synchronized (traits) {
                            int traitSize = 0;
                            for (Trait k : traits) {
                                if (husband.compareTrait(k) && wife.compareTrait(k)) {
                                    traitSize++;
                                    if (r.nextBoolean()) {
                                        //Toast.makeText(MainActivity.this,"Took Husband Trait",Toast.LENGTH_SHORT).show();
                                        int id = k.getID();
                                        Trait x = null;
                                        for (Trait y : husband.getTraitList())
                                            if (y.getID() == id) {
                                                try {
                                                    x = y.clone();
                                                } catch (CloneNotSupportedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        child.addTrait(x);
                                        switch (change) {
                                            case 0:
                                                child.getTraitList().get(traitSize - 1).adjValue(-1);
                                                break;
                                            case 2:
                                                child.getTraitList().get(traitSize - 1).adjValue(1);
                                                break;
                                        }
                                    } else {
                                        //Toast.makeText(MainActivity.this,"Took Wife Trait",Toast.LENGTH_SHORT).show();
                                        int id = k.getID();
                                        Trait z = null;
                                        for (Trait y : wife.getTraitList())
                                            if (y.getID() == id) {
                                                try {
                                                    z = y.clone();
                                                } catch (CloneNotSupportedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        child.addTrait(z);
                                        switch (change) {
                                            case 0:
                                                child.getTraitList().get(traitSize - 1).adjValue(-1);
                                                break;
                                            case 2:
                                                child.getTraitList().get(traitSize - 1).adjValue(1);
                                                break;
                                        }
                                    }
                                } else if (r.nextBoolean()) {
                                    //Toast.makeText(MainActivity.this,"Randomized Solo Trait",Toast.LENGTH_SHORT).show();
                                    traitSize++;
                                    try {
                                        child.addTrait(k.clone());
                                    } catch (CloneNotSupportedException e) {
                                        e.printStackTrace();
                                    }
                                    child.getTraitList().get(traitSize - 1).setValue(r.nextInt(10) + 1);
                                }
                            }
                            if (r.nextBoolean() && !child.compareTrait(MASTER_T_LIST.get(2))) {
                                Trait x = null;
                                try {
                                    x = MASTER_T_LIST.get(2).clone();
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }
                                x.setValue(r.nextInt(10) + 1);
                                child.addTrait(x);
                            }
                            populus.add(child);
                        }
                    }
                }
            }
            return null;
        }
    }
}
