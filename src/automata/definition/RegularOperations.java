package automata.definition;

import automata.definition.FiniteAutomata;
import automata.definition.Transition;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Hayden
 */
public class RegularOperations 
{
    public static final char eps = '\u03B5';
    
    
    // Given a set S and a value v returns the set (S + v) = {x + v | x in S}
    public static HashSet<Integer> setAdd(HashSet<Integer> set, Integer value)
    {
        HashSet<Integer> result = new HashSet<>();
        for (Integer element : set)
            result.add(element + value);
        return result;
    }
    
    // takes two finite automata and splices the final states of the first to
    // the start state of the second
   public static FiniteAutomata concatenate(FiniteAutomata leftOperand, FiniteAutomata rightOperand)
    {
        FiniteAutomata result = new FiniteAutomata();
        
        // find largest state value in the left operand
        // each state of the right operand is increased by the maximum to
        // eliminate collisions
        int max = (result.states.isEmpty()) ? 0 : result.states.iterator().next();
        
        for (Integer state : result.states)
            if (state >= max)
                max = state + 1;

        // add states of left operatond
        result.states.addAll(leftOperand.states);
        
        
        // add max to the states of the second automata
        result.states.addAll(setAdd(rightOperand.states, max));
     
        // transfer input alphabet
        result.inputAlphabet.addAll(leftOperand.inputAlphabet);
        result.inputAlphabet.addAll(rightOperand.inputAlphabet);
        
        
        // initial state is the initial state of the left operand
        result.initialState = leftOperand.initialState;
        
        // final states are the final states of the right operand
        result.finalStates.addAll(setAdd(rightOperand.finalStates, max));
        
        
        // add transitions
        result.delta.addAll(leftOperand.delta);
        for (Transition tt : rightOperand.delta)
            result.delta.add(new Transition(setAdd(tt.antecedent,max), tt.incidence, setAdd(tt.consequent,max)));
        
        // add epsilon transitions from the final states of the left operand 
        // to initial states of the right operand
        for (Integer f : leftOperand.finalStates)
            result.delta.add(new Transition(f, eps, setAdd(rightOperand.initialState,max)));
        return result;
    }
    
    // given two automata will nodeterministically choose between the two
    public static FiniteAutomata or(FiniteAutomata leftOperand, FiniteAutomata rightOperand)
    {
        // initializes automata to the leftOperand automata
        FiniteAutomata result = new FiniteAutomata(leftOperand);
        
        // generate unused nonnegative state for initial state
        // and 1 spot for initial state
        int max = leftOperand.initialState.iterator().next();
        for (Integer state : result.states)
            if (state > max)
                max = state;
        max++; // for start state
        result.initialState = new HashSet<Integer>(asList(max));
        result.states.addAll(result.initialState);
        max++;
        
        // generate the set of states
        result.states.addAll(setAdd(rightOperand.states, max));
        
        result.finalStates.addAll(setAdd(rightOperand.finalStates, max));

        
        // generate input alphabet
        result.inputAlphabet.addAll(rightOperand.inputAlphabet); 
        
        // generate delta transition function
        
        // add transitions from new start to the old start states
        result.delta.add(new Transition(result.initialState,eps,eps,leftOperand.initialState));
        result.delta.add(new Transition(result.initialState,eps,eps, setAdd(rightOperand.initialState,max)));
        
        // add transitions for right operand
        for (Transition tt : rightOperand.delta)
            result.delta.add(new Transition(setAdd(tt.antecedent,max), tt.incidence, setAdd(tt.consequent,max))); 
        
        return result;
    }
    
    public FiniteAutomata intersect(FiniteAutomata leftOperand, FiniteAutomata rightOperand)
    {
        FiniteAutomata result = new FiniteAutomata();
        
        
        
        return result;
    }
    
    // generates an automata that matches the operand multiple times 
    public static FiniteAutomata repitition(FiniteAutomata operand)
    {
        FiniteAutomata result = new FiniteAutomata(operand);
        
        for (Integer state : result.finalStates)
            result.delta.add(new Transition(state,eps,eps,result.initialState));
        result.finalStates.addAll(result.initialState);
        return result;
    }
    
    // constucts an automata that matches only the given string
    public static FiniteAutomata generateStringMatcher(String input)
    {
        FiniteAutomata result = new FiniteAutomata();
        for (int i = 0; i <= input.length(); i++)          // set of states
            result.states.add(i);
        result.initialState = new HashSet<>(asList(0));    // initial state
        result.finalStates.add(input.length());            // final state
        for (int i = 0; i < input.length();i++)            // input alphabet
            result.inputAlphabet.add(input.charAt(i));
        for (int i = 0; i < input.length(); i++)           // delta transition function
            result.delta.add(new Transition(i,input.charAt(i),i+1));
        return result;
    }
    
    
    public static FiniteAutomata zeroOrOne(FiniteAutomata operand)
    {
        FiniteAutomata result = new FiniteAutomata(operand);
        result.finalStates.addAll(result.initialState);
        return result;
//        return or(operand,empty());
    }
    public static FiniteAutomata range(FiniteAutomata left, FiniteAutomata right)
    {
        Character lBound = left.inputAlphabet.iterator().next();
        Character rBound = right.inputAlphabet.iterator().next();
        FiniteAutomata acc = generateStringMatcher(String.format("%c", rBound));
        for (char sym = lBound; sym < rBound; sym++)
        {
            acc = or(acc,generateStringMatcher(String.format("%c",sym)));
        }
        return acc;
    }
    
    // dfa with no transition 
    public static FiniteAutomata empty()
    {
        FiniteAutomata result = new FiniteAutomata();
        result.states.add(0);
        result.initialState.add(0);
        result.finalStates.add(0);
        return result;
    }
}