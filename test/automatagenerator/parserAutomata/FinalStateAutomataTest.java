package automatagenerator.parserAutomata;

import java.util.HashSet;

/**
 *
 * @author Hayden
 */
public class FinalStateAutomataTest 
{
    public static void main(String[] args)
    {
        test();
    }

    private static void test()
    {
        FinalStateAutomata test = new FinalStateAutomata();
        boolean result = true;
        
        result &= unitTest("", false, test);
        result &= unitTest("()", false, test);
        result &= unitTest("(1)", true, test);
        result &= unitTest("(1 2 3 4123)", true, test);
        
        System.out.printf("%s\n", (result) ? "All tests passed" : "A test failed");
    }
    
    
    private static boolean unitTest(String input, boolean expected ,FinalStateAutomata test)
    {
        HashSet<Integer> output = test.evaluate(input, new IntPtr());
        System.out.printf("Automata      : %s\n",test);
        System.out.printf("String        : \"%s\"\n",input);
        System.out.printf("String parsed : %s\n", output != null ? "Yes" : "No");
        System.out.printf("Output        : %s\n", output);
        System.out.printf("Test Passed   : %s\n", (output != null) == expected ? "passed":"failed" );
        System.out.println();
        return (output != null) == expected;
    }    
}
