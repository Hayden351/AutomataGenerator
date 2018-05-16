package automatagenerator.parserAutomata;

import java.util.HashMap;

/**
 *
 * @author Hayden
 */
public class RenamedStatesAutomata implements Automata
{
    // An enumerated type for the states of a finite state automata
    private enum State
    {
        // set of non final states
        START_STATE (Type.NON_FINAL),
        LEFT_PARENTHESIS_PARSED (Type.NON_FINAL),
        ZERO_PARSED (Type.NON_FINAL),
        NON_ZERO_PARSED (Type.NON_FINAL),
        TILDE (Type.NON_FINAL),
        SYMBOLS_PARSED (Type.NON_FINAL),
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
    HashMap<Integer,String> list = new HashMap<>();
    Integer value = 0;
    StringBuilder name = new StringBuilder();
    
    public HashMap<Integer,String> evaluate(String input)
    {
        return evaluate(input, new IntPtr());
    }
    
    public HashMap<Integer,String> evaluate(String input, IntPtr pos)
    {
        int start = pos.dereference;
        currentState = State.START_STATE;    
        
        list = new HashMap<>();
        value = 0;
        name = new StringBuilder();
        
        for (; currentState.type == State.Type.NON_FINAL && pos.dereference < input.length(); pos.dereference++)
        {
            char symbol = input.charAt(pos.dereference);
            currentState = transition(currentState, symbol);
        } // end for
        switch (currentState.type)
        {
            case FINAL:
                list.put(value,name.toString());
                break;
        }
        // hack for when the input is the empty string
        // TODO: generalized way to deal with the empty string
        //       or structure to exclude this component
        if (start + 2 >= pos.dereference)
            return new HashMap<>(); 
        
        return (currentState.type == State.Type.FINAL) ? list : null;
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
                    if (' ' == symbol)
                    {
                        currentState = State.LEFT_PARENTHESIS_PARSED;
                    }
                    else if ('0' == symbol)
                    {
                        currentState = State.ZERO_PARSED;
                        value = symbol - '0';
                    }
                    else if ('1' <= symbol && symbol <= '9')
                    {
                        currentState = State.NON_ZERO_PARSED;
                        value = symbol - '0';
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
                case ZERO_PARSED:
                    if ('~' == symbol)
                    {
                        currentState = State.TILDE;
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
                        value *= 10;
                        value += symbol - '0';
                    }
                    else if ('~' == symbol)
                    {
                        currentState = State.TILDE;
                    }
                    else
                    {
                        currentState = State.UNDEFINED_TRANSITION;
                    }
                    break;
                case TILDE:
                    if ('A' <= symbol && symbol <= 'Z')
                    {
                        currentState = State.SYMBOLS_PARSED;
                        name.append(symbol);
                    }
                    else if ('_' == symbol)
                    {
                        currentState = State.SYMBOLS_PARSED;
                        name.append(symbol);
                    }
                    else
                    {
                        currentState = State.UNDEFINED_TRANSITION;
                    }
                    break;
                case SYMBOLS_PARSED:
                    if ('A' <= symbol && symbol <= 'Z')
                    {
                        currentState = State.SYMBOLS_PARSED;
                        name.append(symbol);
                    }
                    else if ('_' == symbol)
                    {
                        currentState = State.SYMBOLS_PARSED;
                        name.append(symbol);
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
                        list.put(value,name.toString());
                        name = new StringBuilder();
                        currentState = State.ZERO_PARSED;
                        value = symbol - '0';
                    }
                    else if ('1' <= symbol && symbol <= '9')
                    {
                        list.put(value,name.toString());
                        name = new StringBuilder();
                        currentState = State.NON_ZERO_PARSED;
                        value = symbol - '0';
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
        return "Renames";
    }
} // end 
