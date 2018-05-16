package automataTests;

import automata.definition.FiniteAutomata;
import automata.definition.RegexParser;
import automata.definition.Transition;
import automata.definition.TransitionCondition;
import static java.util.Arrays.asList;
import java.util.HashSet;

/**
 *
 * @author Hayden
 */
public class RegexParserTest 
{
    public static void main(String[] args)
    {
        run();
    }
    
    public static void run()
    {
        //testParser("ab * * ab * | ab + 12 +", "ababababab12");
        //testParser("a a + *", "aaaa");
        testParser("a 2 |", "2");
        Transition test1 = new Transition(new HashSet<>(asList(0, 1)),'1',new HashSet<>(asList(0, 1)));
        Transition test2 = new Transition(new HashSet<>(asList(0, 1)),'1',new HashSet<>(asList(0, 1)));
        System.out.printf("%s %s %s\n",test1, test2, test1.equals(test2));
        TransitionCondition test3 = new TransitionCondition('1','2');
        TransitionCondition test4= new TransitionCondition('2','1');
        System.out.printf("%s %s %s\n",test3,test4,test3.equals(test4));
        testParser("1 2 | 3 | *", "12312311132131231231231231231231231231231232131");
        //testParser("1 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 0 | *", "3214675124675214762142136574276541267514275612476847126821486714286721367571264876124986721478561218267192488271467812457667812218974162875178216892474681207864126782147586");
//        // [1..9]*[a..j]*
        testParser("1 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 0 | * a b | c | d | e | f | g | h | i | j | * +", "3214675124675214762142136574276541267514275612476847126821486714286721367571264876124986721478561218267192488271467812457667812218974162875178216892474681207864126782147586ababaaaacacaccacacaabbcc");
        testParser("a b + ?", "");
        testParser("0 9 - ?", "5");
        testParser("0 9 - * a z - +", "0k");
    }
    
    public static void testParser(String input, String test)
    {
        RegexParser rr = new RegexParser();
        FiniteAutomata aa = rr.evaluate(input);
        System.out.printf("%s\n", aa);
        System.out.printf("%s\n", aa.evaluate(test));
        
        System.out.println();
    }
}