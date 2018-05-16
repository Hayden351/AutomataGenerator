package automatagenerator.parserAutomata;

import automata.definition.Transition;
import java.util.HashSet;

/**
 *
 * @author Hayden
 */
public class TransitionAutomataTest 
{
    
    public static void main(String[] args)
    {
        test();
    }

    private static void test()
    {
        
        TransitionAutomata test = new TransitionAutomata();
        boolean result = true;
        result &= unitTest("",false, test);
        result &= unitTest("1",false, test);
        result &= unitTest("(1~ab~3)",true, test);
        result &= unitTest("(1~ab ~3)",false, test);
        result &= unitTest("(1~ab~3 1~11~1)",true, test);
        result &= unitTest("(1~ab~3 1~11~1 2~22~2234241)",true, test);
        result &= unitTest("(1~ab~3 124121~111~42131 2~22~2234241)",false, test);
        result &= unitTest("(1~ab~3 124121~11~42 2~22~2234241)",true, test);   
        System.out.printf("%s\n", (result) ? "All tests passed" : "A test failed");
    }

    private static boolean unitTest(String input, boolean expected ,TransitionAutomata test)
    {
        HashSet<Transition> output = test.evaluate(input, new IntPtr());
        System.out.printf("Automata      : %s\n",test);
        System.out.printf("String        : \"%s\"\n",input);
        System.out.printf("String parsed : %s\n", output != null);
        System.out.printf("Output        : %s\n", output);
        System.out.printf("Test Passed   : %s\n", (output != null) == expected ? "passed":"failed" );
        System.out.println();
        return (output != null) == expected;
    }   
}
