package automatagenerator.parserAutomata;

import java.util.HashMap;

/**
 *
 * @author Hayden
 */
public class RenamedStatesAutomataTest 
{

    public static void main(String[] args)
    {
        test();
    }

    private static void test()
    {
        
        RenamedStatesAutomata test = new RenamedStatesAutomata();
        boolean result = true;
        result &= unitTest("",false, test);
        result &= unitTest("(SD~1)",false, test);
        result &= unitTest("(ABD~123)",false, test);
        result &= unitTest("(0~ABD)",true, test);
        result &= unitTest("(123~ABD_GFD)",true, test);
        System.out.printf("%s\n", (result) ? "All tests passed" : "A test failed");
    }

    private static boolean unitTest(String input, boolean expected ,RenamedStatesAutomata test)
    {
        HashMap<Integer,String> output = test.evaluate(input, new IntPtr());
        System.out.printf("Automata      : %s\n",test);
        System.out.printf("String        : \"%s\"\n",input);
        System.out.printf("String parsed : %s\n", output != null);
        System.out.printf("Output        : %s\n", output);
        System.out.printf("Test Passed   : %s\n", (output != null) == expected ? "passed":"failed" );
        System.out.println();
        return (output != null) == expected;
    }   
}
