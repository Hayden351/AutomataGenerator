package automataTests;

import automata.definition.FiniteAutomata;

/**
 *
 * @author Hayden
 */
public class regularConcatenateTests 
{
    public static void main(String[] args)
    {
        FiniteAutomata test = automata.definition.RegularOperations.concatenate(automata.definition.RegularOperations.generateStringMatcher("123"), automata.definition.RegularOperations.generateStringMatcher("abc"));
        System.out.println(test);
    }
}
