package automataTests;

import automata.definition.FiniteAutomata;
import automata.definition.RegularOperations;

/**
 *
 * @author Hayden
 */
public class DeterministicTests 
{
    public static void main(String[] args)
    {
        FiniteAutomata test = RegularOperations.or(RegularOperations.generateStringMatcher("asd"), RegularOperations.generateStringMatcher("123"));
        System.out.println(test);
        System.out.println();
        test = test.getDeterministic();
        System.out.println(test);
        
    }
}
