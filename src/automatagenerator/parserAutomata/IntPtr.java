package automatagenerator.parserAutomata;

/**
 *
 * @author Hayden
 */
public class IntPtr 
{
    public Integer dereference = 0;

    public IntPtr()
    {
        dereference = 0;
    }
    public IntPtr(int i)
    {
        dereference = i;
    }
public String toString(){return dereference.toString();}}
