package automataTests;

import automata.definition.Transition;
import automata.definition.FiniteAutomata;
import static java.util.Arrays.asList;
import java.util.HashSet;

/**
 *
 * @author Hayden
 */
public class EpsilonClosureTest 
{
    public static final char eps = '\u03B5';
    
    public static void main(String[] args)
    {
        FiniteAutomata test = new FiniteAutomata();
        test.states.addAll(new HashSet<Integer>(asList(0,1,2,3,4)));
        test.initialState.addAll(new HashSet<Integer>(asList(0)));
        test.finalStates.addAll(new HashSet<Integer>(asList(1,2,3)));
        test.inputAlphabet.addAll(new HashSet<Character>(asList('a','b')));
        test.delta.add(new Transition(0,eps,1));
        test.delta.add(new Transition(0,eps,2));
        
        test.delta.add(new Transition(1,eps,3));
        test.delta.add(new Transition(2,eps,0));
        test.delta.add(new Transition(2,eps,5));
        test.delta.add(new Transition(2,eps,6));
        test.epsilonClosure(new HashSet<Integer>(asList(0)));
        System.out.println(test);
    }
}
