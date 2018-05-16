package automatagenerator.parserAutomata;

import automata.definition.FiniteAutomata;
import java.util.HashMap;

/**
 *
 * @author Hayden
 */
public class CompositePayload {
    public FiniteAutomata fa;
    public HashMap<Integer, String> hm;
    public String name;
    public CompositePayload(FiniteAutomata first, HashMap<Integer,String> second, String nn)
    {
        fa = first;
        hm = second;
        name = nn;
    }
}
