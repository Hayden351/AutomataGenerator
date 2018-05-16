package automata.definition;

import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author Hayden
 */
public class FiniteAutomata 
{
    // epsilon character that consumes 0 characters on a transition
    public static final char EPSILON = '\u03B5';
    
    // a set of integers that reperesent states in the automata
    public HashSet<Integer> states;
    
    // a set of states that represent the inital potentially nodeterministic
    // starting postion
    public HashSet<Integer> initialState;
    
    // if evaluation ends on a final state then the input is accepted
    // otherwise it is rejected
    public HashSet<Integer> finalStates;
    
    // symbols that are recognized by the automata
    public HashSet<Character> inputAlphabet;
    
    // the function that definites every transition from state to state
    public HashSet<Transition> delta;   
    
    public FiniteAutomata()
    {
        states = new HashSet<>();
        initialState = new HashSet<>();
        finalStates = new HashSet<>();
        
        inputAlphabet = new HashSet<>();
        
        delta = new HashSet<>();
    }
    public FiniteAutomata(FiniteAutomata operand)
    {
        states = new HashSet<>(operand.states);
        initialState = new HashSet<>(operand.initialState);
        finalStates = new HashSet<>(operand.finalStates);
        inputAlphabet = new HashSet<>(operand.inputAlphabet);
        delta = new HashSet<>(operand.delta);
        delta = new HashSet<>();
        for (Transition t : operand.delta)
        {
            HashSet<TransitionCondition> incidence = new HashSet<>();
            for (TransitionCondition tc : t.incidence)
                incidence.add(new TransitionCondition(tc.lBound,tc.rBound));
            delta.add(new Transition(
                    new HashSet<>(t.antecedent),
                    new HashSet<>(incidence),
                    new HashSet<>(t.consequent)));
        }
        
    }
    
    
    HashSet<Integer> currentState;
    Integer pos;
    public boolean evaluate(String input)
    {
        // start in initial state
        currentState = initialState;
        
        // transition while there is characters in the string or until error
        // occurs
        for (pos = 0; !currentState.isEmpty() && pos < input.length(); pos++)
            currentState = transition(epsilonClosure(currentState), input.charAt(pos));      
        
        // is accepted if in final state and the current state is not error/empty
        if (!currentState.isEmpty())
            if (containsFinalState(currentState))
                    return true;
        return false;
    }
    
    
    public HashSet<Integer> epsilonClosure(Integer state)
    {
        return epsilonClosure(new HashSet<>(asList(state)));
    }
    public HashSet<Integer> epsilonClosure(HashSet<Integer> state)
    {
        // add current states to the closure
        HashSet<Integer> closure = new HashSet<>(state);
        
        // find epsilon closure of the current set of states
        epsilonClosure(state, closure);
        return closure;
    }
    private void epsilonClosure(HashSet<Integer> states, HashSet<Integer> acc)
    {
        HashSet<Integer> add = transition(states,EPSILON);
        // if state changes perform epsilon closure of added elements
        if (acc.addAll(add))
            epsilonClosure(add, acc);
    }
    
    public HashSet<Integer> transition(HashSet<Integer> state, Character sym)
    {    
        // set of states we are in after the transition
        HashSet<Integer> consequent = new HashSet<>();
        
        // add all states we can traverse to on any transition
        for (Transition tt : delta)
            if (tt.test(state, sym))
            {
                consequent.addAll(tt.consequent);
//                consequent = epsilonClosure(consequent);
            }
        
        // can also reach the states that are connected by epsilon transitions
        return consequent;
    }
    
    // convert current automata into a deterministic one
    public FiniteAutomata getDeterministic()
    {
        // algorithm is dependent on transitions so if there are no
        // transitions bad things happen
        
        // if there are no transitions then there are no epsilon transitions
        // therefore the current automata is already deterministic
        if (delta.isEmpty())
            return new FiniteAutomata(this);
        // have all the data
        
        // accumulator deterministic automata
        FiniteAutomata deterministic = new FiniteAutomata();
        // alphabet is the same as old alphabet
        
        // have a state
        // epsilon closure of state
        // for every character in the input alphabet
        // transition on state
        // generate transition from transitions
        
        
        // generate deterministic transitions from this automatas transitions
        HashSet<Transition> detTransitions = new HashSet<>();
        generateTransitions(initialState, detTransitions);
        
        // In the nfa each state is a set of state translate the sets of states 
        // to a single state in the dfa
        HashMap<HashSet<Integer>, Integer> remapping = new HashMap<>();
        int stateNumber = 0;
        for (Transition tt : detTransitions)
        {
            if (!remapping.containsKey(tt.antecedent))
            {
                remapping.put(tt.antecedent, stateNumber);
                
                deterministic.states.add(stateNumber);
                if (containsFinalState(tt.antecedent))
                    deterministic.finalStates.add(stateNumber);
                
                stateNumber++;
            }
            
            if (!remapping.containsKey(tt.consequent))
            {
                remapping.put(tt.consequent, stateNumber);
                
                deterministic.states.add(stateNumber);
                if (containsFinalState(tt.consequent))
                    deterministic.finalStates.add(stateNumber);
                
                stateNumber++;
            }
        }
        
        // input alphabet is unchanged
        deterministic.inputAlphabet = new HashSet<>(inputAlphabet);
      
        // add initial state
        deterministic.initialState.add(remapping.get(epsilonClosure(initialState)));
        
        // use new states instead of the original sets
        for (Transition tt : detTransitions)
            deterministic.delta.add(new Transition(remapping.get(tt.antecedent),tt.incidence,remapping.get(tt.consequent)));
        
        return deterministic;
    }
    
    /* NFA to DFA conversion algorithm
    start at initial state in the conversion
    
    epsilon closure to keep track of all states we could be in after epsilon
    transition
    
    for each character in the input alphabet generate the corresponding 
    transition
    
    if the transition is a new transition add it and recursively traverse
    that transition to generate new transitions
    
    */
    public void generateTransitions(HashSet<Integer> state, 
                                    HashSet<Transition> detTransitions)
    {
        HashSet<Integer> antecedent = epsilonClosure(state);
        for (char sym : inputAlphabet)
        {
            HashSet<Integer> consequent = transition(antecedent, sym);
            
            // for each state we reach we can also traverse each epsilon transition
            for (Integer value : consequent)
                consequent.addAll(epsilonClosure(value));
            
            // if we're not going anywhere is irrelevant
            if (!(antecedent.isEmpty() || consequent.isEmpty()))
            {
                Transition t = new Transition(antecedent, sym, consequent);
                if (detTransitions.add(t))
                {
                    generateTransitions(consequent, detTransitions);
                }
            }
        }
    }
    
    
    // according to hopcroft's algorithm
    // TODO: complete and test implementation
    public FiniteAutomata minimalDFA()
    {
        FiniteAutomata dfa = this.getDeterministic();
        HashSet<HashSet<Integer>> P = new HashSet<>();
        P.add(new HashSet<>(dfa.finalStates));
        HashSet<Integer> nonFinal = new HashSet<>(dfa.states);
        nonFinal.removeAll(dfa.finalStates);
        P.add(nonFinal);
        
        HashSet<HashSet<Integer>> W = new HashSet<>(asList(new HashSet<>(dfa.finalStates)));
        while (!W.isEmpty())
        {
            HashSet<Integer> A = W.iterator().next();
            W.remove(A);
            for (Character sym : dfa.inputAlphabet)
            {
                HashSet<Integer> X = transition(A,sym);
                
                for (Iterator<HashSet<Integer>> it = P.iterator(); it.hasNext();)
                {
                    HashSet<Integer> Y = it.next();
                    if (!intersect(X,Y).isEmpty() && !difference(Y,X).isEmpty())
                    {
                        P.remove(Y);
                        P.add(intersect(X,Y));
                        P.add(difference(Y,X));
                    }
                    if (W.contains(Y))
                    {
                        W.remove(Y);
                        W.add(intersect(X,Y));
                        W.add(difference(Y,X));
                    }
                    else
                    {
                        if (intersect(X,Y).size() <= difference(Y,X).size())
                        {
                            W.add(intersect(X,Y));
                        }
                        else
                        {
                            W.add(difference(Y,X));
                        }   
                    }
                }
            }
                    
        }
        return null;
    }
    public HashSet<Integer> intersect(HashSet<Integer> left, HashSet<Integer> right)
    {
        HashSet<Integer> result = new HashSet<>();
        for (Integer value : left)
            if (right.contains(value))
                result.add(value);
        return result;
    }
    public HashSet<Integer> difference(HashSet<Integer> left, HashSet<Integer> right)
    {
        HashSet<Integer> result = new HashSet<>(left);
        result.removeAll(right);
        return result;
    }
    // overly elaborate string representation of the finite automata
    @Override
    public String toString()
    {
        //String result = "";
        StringBuilder result = new StringBuilder();
        result.append(String.format("States%14c: %s\n",' ', states));
        result.append(String.format("Initial State%7c: %s\n",' ', initialState));
        result.append(String.format("Final States%8c: %s\n",' ', finalStates));
        result.append(String.format("Input Alphabet%6c: %s\n",' ', inputAlphabet));
        
        // prints out delta transition function right justified and in rows
        // that are, at most, twice as long as the length of the states
        int len = 0;
        boolean first = true;
        int maxLen = states.toString().length() * 2;
        result.append(String.format("Transition function : "));
        int leftPad = "Transition function :".length();
        
        
        // find maximum toString() length of any of the transitions
        int maxTransitionLen = 0;
        for (Transition tt : delta)
            if (maxTransitionLen < tt.toString().length())
                maxTransitionLen = tt.toString().length();

        for (Transition tt : delta)
        {
            if (first)
            {    
                result.append(tt.toString());
                first = false;
                len += maxTransitionLen;
                for (int i = tt.toString().length(); i <= maxTransitionLen;i++)
                    result.append(" ");
            }
            else if (len + maxTransitionLen + 1 <= maxLen)
            {
                result.append(String.format(" %s", tt));
                len += maxTransitionLen + 1;

                for (int i = tt.toString().length(); i <= maxTransitionLen;i++)
                    result.append(" ");
            }
            else
            {
                result.append(String.format("\n"));
                for (int j = 0; j <= leftPad; j++) // why is there a 21?
                    result.append(" ");

                result.append(String.format("%s", tt));
                for (int i = tt.toString().length(); i <= maxTransitionLen;i++)
                    result.append(" ");
                len = maxTransitionLen;
            }
        }
        
        return result.toString();
    }

    private boolean containsFinalState(Iterable<Integer> state)
    {
        for (Integer value : state)
            if (finalStates.contains(value))
                return true;
        return false;
    }
}