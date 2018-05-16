package automataTests;


import automata.definition.FiniteAutomata;
import automata.definition.Transition;
import static java.util.Arrays.asList;
import java.util.HashSet;

/**
 *
 * @author Hayden
 */
public class DefinitionTest 
{
    public static void main(String[] args)
    {
        // automata that matches ab(a | b | epsilon)
        FiniteAutomata test = new FiniteAutomata();
        
        for (int i = 0; i <= 21; i++)
            test.states.add(i);
        
        test.initialState = new HashSet<>(asList(0));
        
        test.finalStates.add(2);
        test.finalStates.add(3);
        
        test.inputAlphabet.add('a');
        test.inputAlphabet.add('b');
        
        for (int i = 0; i <= 110; i++)
            test.delta.add(new Transition(i,'a',1));
//        test.delta.add(new Transition(1,'b',2));
//        test.delta.add(new Transition(1,'c',2));
//        test.delta.add(new Transition(2,'a','b',3));
        
        testAutomata(test);
    }
    public static void testAutomata(FiniteAutomata aa)
    {
        System.out.printf("Testing Automata\n%s\n", aa);
        unitTest("a", aa);
        unitTest("b", aa);
//        
//        unitTest("aa", aa);
//        unitTest("ab", aa);
//        unitTest("ba", aa);
//        unitTest("bb", aa);
//        
//        unitTest("aaa", aa);
//        unitTest("aba", aa);
//        unitTest("baa", aa);
//        unitTest("bba", aa);
//        
//        unitTest("aab", aa);
//        unitTest("abb", aa);
//        unitTest("bab", aa);
//        unitTest("bbb", aa);
//        
//        unitTest("aaaa", aa);
//        unitTest("abaa", aa);
//        unitTest("baaa", aa);
//        unitTest("bbaa", aa);
//        
//        unitTest("aaba", aa);
//        unitTest("abba", aa);
//        unitTest("baba", aa);
//        unitTest("bbba", aa);
        
        
    }
    
    public static void unitTest(String input, FiniteAutomata aa)
    {
        System.out.printf("%s : %s\n\n",input, aa.evaluate(input) ? "1" : "0");
    }
}
