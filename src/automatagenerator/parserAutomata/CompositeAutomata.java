package automatagenerator.parserAutomata;

import automata.definition.FiniteAutomata;
import automata.definition.Transition;
import automata.definition.TransitionCondition;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Hayden
 */
public class CompositeAutomata implements Automata
{
    NameAutomata na = new NameAutomata();
    StartStateAutomata ssa = new StartStateAutomata();
    FinalStateAutomata fsa = new FinalStateAutomata();
    RenamedStatesAutomata rsa = new RenamedStatesAutomata();
    TransitionAutomata ta = new TransitionAutomata();

    public static CompositePayload parse(String input)
    {
        return new CompositeAutomata().evaluate(input);
    }

    public static CompositePayload parse(String input, IntPtr ptr)
    {
        return new CompositeAutomata().evaluate(input, ptr);
    }
    public CompositePayload evaluate(String input)
    {
        return evaluate(input, new IntPtr(0));
    }
    public CompositePayload evaluate(String input, IntPtr ptr)
    {
        // new finite automat to accumulate result
        FiniteAutomata result = new FiniteAutomata();
        
        
        HashMap<Integer,String> stateRename = new HashMap<>();
        String name;
        
        // parse out name of the class
        if ((name = na.evaluate(input, ptr)) == null)
            return null;
        
        // parse out the initial state of the automata TODO: fix this null check
        Integer initial = ssa.evaluate(input, ptr);
        if (initial == null)
            return null;
        result.initialState = new HashSet<>();
        result.initialState.add(initial);
        
        // parse out all final states of the automata
        if ((result.finalStates = fsa.evaluate(input, ptr)) == null)
            return null;
        
        // parse out mappings from state to names
        if ((stateRename = rsa.evaluate(input, ptr)) == null)
            return null;
        
        // parse out the delta transition functuion
        if ((result.delta = ta.evaluate(input, ptr)) == null)
            return null;
    
        // add final and initial states if they are not added already
        result.states.addAll(result.finalStates);
        result.states.addAll(result.initialState);
        
        //
        for (Transition tt : result.delta)
        {
            for (TransitionCondition ii : tt.incidence)   
            {
                for (char sym = ii.lBound; sym <= ii.rBound; sym++)
                {
                    result.inputAlphabet.add(sym);
                }
            }
            result.states.addAll(tt.antecedent);
            result.states.addAll(tt.consequent);
        }
        return new CompositePayload(result,stateRename, name);

    }
    /*
    public CompositePayload evaluate(String input, IntPtr ptr)
    {
        FiniteAutomata result = new FiniteAutomata();
        HashMap<Integer,String> stateRename = new HashMap<>();
        String name;
        if ((name = na.evaluate(input, ptr)) != null)
            if ((result.initialState = new HashSet<Integer>(asList(ssa.evaluate(input, ptr))))!= null)
                if ((result.finalStates = fsa.evaluate(input, ptr)) != null)
                    if ((stateRename = rsa.evaluate(input, ptr))!= null)
                        if ((result.delta = ta.evaluate(input, ptr)) != null)
                        {
                            if (result.initialState.iterator().next().equals(new Integer(-1))) // TODO: put this somwhere else
                                result.initialState = new HashSet<Integer>();
                            
                            result.states.addAll(result.finalStates);
                            result.states.addAll(result.initialState);
                            for (Transition tt : result.delta)
                            {
                                for (TransitionCondition ii : tt.incidence)   
                                {
                                    for (char sym = ii.lBound; sym <= ii.rBound; sym++)
                                    {
                                        result.inputAlphabet.add(sym);
                                    }
                                }
                                result.states.addAll(tt.antecedent);
                                result.states.addAll(tt.consequent);
                            }
                            return new CompositePayload(result,stateRename, name);
                        }
        return null;
    }
    */
    public String toString()
    {
        return String.format("(%s, %s, %s, %s)", ssa,fsa,rsa,ta);
    }
}
