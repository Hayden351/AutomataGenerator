package automatagenerator.parserAutomata;

/**
 *
 * @author Hayden
 */
public class NameAutomataTest 
{
    public static void main(String[] args)
    {
        test();
    }

    private static void test()
    {
        
        NameAutomata test = new NameAutomata();
        boolean result = true;
        result &= unitTest("",false, test);
        result &= unitTest("(ABC)",true, test);
        result &= unitTest("(abcdeASD)",true, test);
        result &= unitTest("(0~ABD)",false, test);
        result &= unitTest("abc)",false, test);
        result &= unitTest("abc",false, test);
        System.out.printf("%s\n", (result) ? "All tests passed" : "A test failed");
    }

    private static boolean unitTest(String input, boolean expected, NameAutomata test)
    {
        String output = test.evaluate(input);
        System.out.printf("Automata      : %s\n",test);
        System.out.printf("String        : \"%s\"\n",input);
        System.out.printf("String parsed : %s\n", output != null);
        System.out.printf("Output        : %s\n", output);
        System.out.printf("Test Passed   : %s\n", (output != null) == expected ? "passed":"failed" );
        System.out.println();
        return (output != null) == expected;
    }   
}

