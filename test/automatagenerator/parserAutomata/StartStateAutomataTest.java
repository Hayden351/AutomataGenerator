package automatagenerator.parserAutomata;

/**
 *
 * @author Hayden
 */
public class StartStateAutomataTest 
{
    public static void main(String[] args)
    {
        test();
    }

    private static void test()
    {
        
        StartStateAutomata test = new StartStateAutomata();
        boolean result = true;
        result &= unitTest("",false, test);
        
        result &= unitTest("1",false, test);
        result &= unitTest("(0)",true, test);
        result &= unitTest("( 1 ) ",true, test);
        result &= unitTest("       (           1           )           ",true, test);
        result &= unitTest("( 133 ) ",true, test);
        result &= unitTest("( 00001124123 ) ",false, test);
        result &= unitTest("( 01234 ) ",false, test);
        result &= unitTest("( 512341241 ) ",true, test);
        
        System.out.printf("%s\n", (result) ? "All tests passed" : "A test failed");
    }

    private static boolean unitTest(String input, boolean expected ,StartStateAutomata test)
    {
        Integer output = test.evaluate(input, new IntPtr());
        System.out.printf("Automata      : %s\n",test);
        System.out.printf("String        : \"%s\"\n",input);
        System.out.printf("String parsed : %s\n", output != null);
        System.out.printf("Output        : %s\n", output);
        System.out.printf("Test Passed   : %s\n", (output != null) == expected ? "passed":"failed" );
        System.out.println();
        return (output != null) == expected;
    }    
}
