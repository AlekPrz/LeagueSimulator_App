package pjwstk.praca_inzynierska.symulatorligipilkarskiej.helpingMethods;

public class Counter
{
    private int count;

    public Counter()
    {
        count = 0;
    }


    public Counter(int init)

    {
        count = init;
    }

    public int get()
    {

        return count;
    }

    public void clear()
    {
        count = 0;
    }

    public void increment()
    {
        System.out.println(count);
        count++;
        System.out.println(count);

    }

    public String toString()
    {
        return ""+count;
    }
}