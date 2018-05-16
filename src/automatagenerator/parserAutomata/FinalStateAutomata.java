package automatagenerator.parserAutomata;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Hayden
 */

public class FinalStateAutomata implements Automata
{
    // An enumerated type for the states of a finite state automata
    private enum State
    {
        // set of non final states
        START_STATE (Type.NON_FINAL),
        LEFT_PARENTHESIS_PARSED (Type.NON_FINAL),
        ZERO_PARSED (Type.NON_FINAL),
        NON_ZERO_PARSED (Type.NON_FINAL),
        SPACES (Type.NON_FINAL),

        // set of final states
        FINAL_STATE (Type.FINAL),

        // set of error states
        UNDEFINED_TRANSITION (Type.ERROR);

        // indicates whether the state is a final, nonfinal or error state 
        private enum Type
        {
            FINAL,
            NON_FINAL,
            ERROR;    
        }

        private final Type type;

        // Initialize state with its type
        State(Type type)
        {
            this.type = type;
        }
    }
    State currentState = State.START_STATE;
    Integer state = 0;
    HashSet<Integer> states = new HashSet<>();
    // we do not know if the input string represents an empty list
    boolean empty = false;
    //public Pair<Pair<Integer,Boolean>,ArrayList<Integer>> evaluate(String input, int pos)
    public HashSet<Integer> evaluate(String input)
    {
        return evaluate(input, new IntPtr());
    }
    public HashSet<Integer> evaluate(String input, IntPtr pos)
    {
        currentState = State.START_STATE;
        state = 0;
        states = new HashSet<>();
        for (; currentState.type == State.Type.NON_FINAL && pos.dereference < input.length(); pos.dereference++)
        {
            char symbol = input.charAt(pos.dereference);
            currentState = transition(currentState, symbol);
        } // end for
        
        switch(currentState.type)
        {
            case FINAL:
                if (!empty)
                    states.add(state);
        }
        return (currentState.type == State.Type.FINAL) ? states : null;
                //new Pair<Pair<Integer,Boolean>,ArrayList<Integer>>(new Pair<>(pos, currentState.type == State.Type.FINAL),states);
    } // end evaluate()
    
    public State transition(State currentState, char symbol)
    {
        switch (currentState)
            {
                case START_STATE:
                    if (' ' == symbol)
                    {
                        currentState = State.START_STATE;
                    }
                    else if ('(' == symbol)
                    {
                        currentState = State.LEFT_PARENTHESIS_PARSED;
                    }
                    else
                    {
                        currentState = State.UNDEFINED_TRANSITION;
                    }
                    break;
                case LEFT_PARENTHESIS_PARSED:
                    if (')' == symbol)
                    {
                        // string represents an empty list
                        empty = true;
                        currentState = State.FINAL_STATE;
                    }
                    else if (' ' == symbol)
                    {
                        currentState = State.LEFT_PARENTHESIS_PARSED;
                    }
                    else if ('0' == symbol)
                    {
                        currentState = State.ZERO_PARSED;
                        state = symbol - '0';
                        
                    }
                    else if ('1' <= symbol && symbol <= '9')
                    {
                        currentState = State.NON_ZERO_PARSED;
                        state = symbol - '0';
                    }
                    else
                    {
                        currentState = State.UNDEFINED_TRANSITION;
                    }
                    break;
                case ZERO_PARSED:
                    if (' ' == symbol)
                    {
                        currentState = State.SPACES;
                    }
                    else if (')' == symbol)
                    {
                        currentState = State.FINAL_STATE;
                    }
                    else
                    {
                        currentState = State.UNDEFINED_TRANSITION;
                    }
                    break;
                case NON_ZERO_PARSED:
                    if ('0' <= symbol && symbol <= '9')
                    {
                        currentState = State.NON_ZERO_PARSED;
                        state *= 10;
                        state += symbol - '0';
                    }
                    else if (' ' == symbol)
                    {
                        currentState = State.SPACES;
                    }
                    else if (')' == symbol)
                    {
                        currentState = State.FINAL_STATE;
                    }
                    else
                    {
                        currentState = State.UNDEFINED_TRANSITION;
                    }
                    break;
                case SPACES:
                    if ('0' == symbol)
                    {
                        states.add(state);
                        currentState = State.ZERO_PARSED;
                        state = symbol - '0';
                    }
                    else if ('1' <= symbol && symbol <= '9')
                    {
                        states.add(state);
                        currentState = State.NON_ZERO_PARSED;
                        state = symbol - '0';
                    }
                    else if (' ' == symbol)
                    {
                        currentState = State.SPACES;
                    }
                    else if (')' == symbol)
                    {
                        currentState = State.FINAL_STATE;
                    }
                    else
                    {
                        currentState = State.UNDEFINED_TRANSITION;
                    }
                    break;
                default:
                    currentState = State.UNDEFINED_TRANSITION;
                    break;
            } // end switch
        return currentState;
    }
    
    @Override
    public String toString()
    {
        return "Final";
    }
} // end 