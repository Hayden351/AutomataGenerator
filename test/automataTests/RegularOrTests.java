package automataTests;

import automata.definition.FiniteAutomata;
import static java.util.Arrays.asList;
import java.util.HashSet;


/**
 *
 * @author Hayden
 */
public class RegularOrTests 
{
    public static void main(String[] args)
    {
        FiniteAutomata test = automata.definition.RegularOperations.or(automata.definition.RegularOperations.generateStringMatcher("abc"), automata.definition.RegularOperations.generateStringMatcher("123"));
        System.out.printf("Testing\n");
        System.out.printf("%s\n", test);
        
        System.out.printf("\n%s\n",test.epsilonClosure(new HashSet<>(asList(4))));
        unitTest("", test);
        unitTest("a", test);
        unitTest("ab", test);
        unitTest("ac", test);
        unitTest("abc", test);
        unitTest("abca", test);
        unitTest("abcb", test);
        unitTest("abcc", test);
    }

    private static void unitTest(String input, FiniteAutomata test)
    {
        System.out.printf("%s : %s\n", input, test.evaluate(input));
        
        
    }
}
