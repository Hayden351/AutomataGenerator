package automatagenerator;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hayden
 */
public class AutomataGeneratorTester
{
    public static void main(String[] args) 
    {
        String input;
        input = "(Test)(0)(2 3)(0~STARTING_STATE 1~UNARY 2~ZERO_PARSED 3~NON_ZERO_PARSED)(1~00~2 1~19~3 3~09~3 0~++~1 0~--~1 0~00~2 0~19~3)";
        //input = "(NameAutomata)(0)(2)(0~START 1~NAME_PARSING 2~FINAL)(0~((~1 1~AZ~1 1~az~1 1~))~2)";
        //input = "(MenuSystem)(0)(3)(0~START_MENU 3~EXIT 1~PLAY_MENU 2~COLLECTION_MENU)(0~11~1 0~22~2 0~33~3)";
        input = "(Test)(0)(4)(0~START_STATE 1~ZERO 2~NON_ZERO 3~SPACE_PARSED 4~NEW_LINE)(0~  ~0 0~00~1 0~19~2 1~  ~3 2~09~2 2~  ~3 1~**~4 4~  ~4 2~**~4 3~00~1 3~19~2)";
        input = "(NSPAutomata)(0)(1 2 3 4 5 6)(0~START_STATE 1~READING_EMPLOYEE_NUMBER 2~EMPLOYEE_NUMBER_READ 3~READ_NUMBER 4~READ_NUMBER_SINGLE_DIGIT 5~READ_NUMBER_SECOND_DIGIT 6~READ_SHIFT_NUMBER)(0~  ~0 0~19~1 1~09~1 1~  ~2 2~  ~2 2~11~3 2~29~4 3~01~5 4~  ~6 5~  ~6 6~  ~6 6~11~3 6~29~4)";
//        input = "(PairReader)()(8)()(0~((~1 1~00~2 1~19~3 2~,,~4 3~09~3 3~,,~4 4~09~5 5~09~5 5~..~6 6~09~7 7~09~7 7~))~8 8~,,~0)";
//        input = "(JavaDFA)(0)(3)()(0~bb~1 0~aa~2 1~aa~2 1~bb~3 2~aa~3 2~bb~1 3~ab~0)";
        input = "(IntAutomata)(0)(1 2)(0~START 1~ZERO_PARSED 2~NON_ZERO_PARSED)(0~00~1 0~19~2 2~09~2)";
        input = "(DecimalAutomata)(0)(2 3 5)(0~START 6~UNARY_PLUS 1~UNARY_MINUS 2~ZERO_PARSED 3~NON_ZERO_PARSED 4~DECIMAL_POINT 5~DECIMAL_EXPANSION)(0~++~6 0~--~1 0~00~2 0~19~3 1~00~2 1~19~3 6~00~2 6~19~3 2~..~4 3~09~3 3~..~4 4~09~5 5~09~5)";
        input = "(AllocationAutomata)(0)(2 5 6)(0~PARSE_ID 1~ 2~C 3~D 4~E 5~F 6~G)(0~19~1 1~&&~1 1~--~2 1~++~3 2~&&~2 3~^^~4 4~^^~4 4~19~5 5~09~5 5~&&~6)";
        
        
        input = "(AllocationAutomata)(0)(5 6 7)(0~START_STATE 1~ID_ZERO 2~ID_NON_ZERO 3~ID_SPACING 4~ALLOCATION 5~DEALLOCATION 6~SIZE_ZERO 7~SIZE_NON_ZERO)"
                + "(0~SS~0 0~00~1 0~19~2 1~SS~3 2~09~2 2~SS~3 3~SS~3 3~++~4 3~--~5 4~SS~4 5~SS~5 4~00~6 4~19~7 6~SS~6 7~SS~7)";
        
        // W is tabs/spaces N is new line
        input = "(AllocationAutomata)(0)(5 9 11 12)(0~START_STATE 1~MEMORY_SIZE 2~POST_MEMORY_SPACING 3~MINIMUM_SIZE 4~POST_MINIMUM_SPACING 5~REQUEST_TOKEN 6~ID_ZERO 7~ID_NON_ZERO 8~POST_ID_SPACING 9~DEALLOCATION 10~ALLOCATION 11~ALLOCATION_SIZE 12~POST_ALLOCATION_SPACING)"
                + "(0~19~1 1~09~1 1~WW~2 2~WW~2 2~19~3 3~09~3 3~WW~4 3~NN~5 4~WW~4 4~NN~5 5~WW~5 5~00~6 5~19~7 6~WW~8 7~09~7 7~WW~8 8~WW~8 8~--~9 8~++~10 9~WW~9 9~NN~5 10~WW~10 10~19~11 11~09~11 11~NN~5 11~WW~12 12~WW~12 12~NN~5)";
        
        // 1 0 identifier
        // (0~START_STATE 
        //  1~IDENTIFIER
        //  2~ZERO 
        //  3~ONE
        //  4~IMPLICATION_ONE
        //  5~IMPLICATION_TWO
        //  6~AND
        //  7~OR 
        //  8~LEFT_PARENTHESIS
        //  9~RIGHT_PARENTHESIS
        // 10~DELIMITING_WHITESPACE
        // 11~NEGATION)
        input = "(BooleanExpressionEvaluator)(0)(1 2 3 5 6 7 8 9 10 11)"
                + "(0~START_STATE 1~IDENTIFIER 2~ZERO 3~ONE 4~IMPLICATION_ONE 5~IMPLICATION_TWO 6~AND 7~OR 8~LEFT_PARENTHESIS 9~RIGHT_PARENTHESIS 10~DELIMITING_WHITESPACE 11~NEGATION)"
                + "(0~**~0 0~az~1 0~AZ~1 1~0z~1 0~00~2 0~11~3 0~--~4 0~&&~6 0~||~7 0~((~8 0~))~9 10~az~1 10~AZ~1 10~00~2 10~11~3 10~--~4 10~&&~6 10~||~7 10~((~8 10~))~9 4~>>~5 1~**~10 2~**~10 3~**~10 5~**~10 6~**~10 7~**~10 8~**~10 9~**~10 10~**~10 0~!!~11 10~!!~11 11~az~1 11~AZ~1 11~00~2 11~11~3)";
        input = "(BooleanExpressionParser)"
    /*start*/ + "(0)" 
    /*final*/ + "(1 2 3 4 5)"
    /*names*/ + "(0~START_STATE 1~ZERO_TOKEN 2~ONE_TOKEN 3~AND_TOKEN 4~NEGATION_TOKEN 5~WHITESPACE)"
    /*delta*/ + "(0~**~0 0~00~1 0~11~2 0~&&~3 0~!!~4 1~00~1 1~11~2 1~&&~3 1~!!~4 2~00~1 2~11~2 2~&&~3 2~!!~4 3~00~1 3~11~2 3~&&~3 3~!!~4 4~00~1 4~11~2 4~&&~3 4~!!~4 5~00~1 5~11~2 5~&&~3 5~!!~4 1~**~5 2~**~5 3~**~5 4~**~5 5~**~5)";
        try
        {
            // "C:\\Users\\Hayden\\OneDrive\\NBMisc\\AutomataCTest\\main.c"
            AutomataGenerator.generateAutomata(input, System.out);
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(AutomataGeneratorTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
