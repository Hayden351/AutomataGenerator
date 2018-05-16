package automataTests;

import automata.definition.RegularOperations;
import automata.definition.FiniteAutomata;

/**
 *
 * @author Hayden
 */
public class EmptyTests 
{
    public static void main(String[] args)
    {
        FiniteAutomata empty = RegularOperations.empty();
        FiniteAutomata abc = RegularOperations.generateStringMatcher("abc");
        System.out.println(empty);
        System.out.println();
        System.out.println(abc);
        System.out.println();
        System.out.println(RegularOperations.or(empty, abc));
        System.out.println();
        System.out.println(RegularOperations.or(empty, abc).getDeterministic());
        System.out.println();
        System.out.println(empty.getDeterministic());
    }
}
