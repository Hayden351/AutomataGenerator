package automataTests;

import automata.definition.FiniteAutomata;

/**
 *
 * @author Hayden
 */
public class StringMatchingAutomataTest 
{
    public static void main(String[] args)
    {
        FiniteAutomata test = automata.definition.RegularOperations.generateStringMatcher("abc123");
        System.out.println(test);
    }
}