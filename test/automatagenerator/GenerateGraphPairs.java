package automatagenerator;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hayden
 */
public class GenerateGraphPairs 
{
    public static void main(String[] args)
    {
        String input = "(GraphParser)(0)(0)(0~START_STATE 1~LEFT_PARENTHESIS 2~LEFT_ZERO 3~LEFT_POSITIVE 3~CONNECTIVE 4~RIGHT_ZERO 5~RIGHT_POSITIVE 6~RIGHT_PARENTHESIS)(0~  ~0 0~((~1 1~00~2 1~19~3 2~  ~4 3~09~3 3~  ~4)";
        try
        {
            AutomataGenerator.generateAutomata(input, System.out);
        } 
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(AutomataGeneratorTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
