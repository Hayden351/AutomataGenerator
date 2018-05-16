package automatagenerator.parserAutomata;

/**
 *
 * @author Hayden
 */
public class CompositeAutomataTest 
{
    public static void main(String[] args)
    {
        test();
    }

    private static void test()
    {
        CompositeAutomata test = new CompositeAutomata();
        boolean result = true;
        result &= unitTest("", false, test);
        
        result &= unitTest("asdf",false,test);
        
        result &= unitTest("(0)(1 2)(0~START_STATE 1~ZERO_PARSED 2~NON_ZERO_PARSED)(0~  ~0 0~00~1 0~19~2 2~09~2)", true, test);
        
        result &= unitTest("(0)(1 2 3 4)(0~START_STATE 1~ZERO_PARSED 2~NON_ZERO_PARSED)(0~31~1 0~  ~0 0~00~1 0~19~2 2~09~2 0~ab~2)",true, test);
        
        System.out.printf("%s\n", (result) ? "All tests passed" : "A test failed");
    }
    
    
    private static boolean unitTest(String input, boolean expected, CompositeAutomata test)
    {
        CompositePayload output = test.evaluate(input);
        System.out.printf("Automata      : %s\n",test);
        System.out.printf("String        : \"%s\"\n",input);
        System.out.printf("String parsed : %s\n", output != null ? "Yes" : "No");
        
        // print output       
        if (output == null)
            System.out.printf("Output        : %s\n", output);
        else
        {
            System.out.printf("Output        : %s\n", output.hm);
            System.out.printf("%s\n", output.fa);
        }
        
        System.out.printf("Test Passed   : %s\n", (output != null) == expected ? "passed":"failed" );
        System.out.println();
        return (output != null) == expected;
    }    
}