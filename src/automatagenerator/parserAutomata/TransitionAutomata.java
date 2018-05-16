package automatagenerator.parserAutomata;
import automata.definition.TransitionCondition;
import automata.definition.Transition;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.HashSet;

/**
 *
 * @author Hayden
 */
public class TransitionAutomata implements Automata
{
    // An enumerated type for the states of a finite state automata
    private enum State
    {
        // set of non final states
        START_STATE (Type.NON_FINAL),
        LEFT_PARENTHESIS_PARSED (Type.NON_FINAL),
        ZERO_PARSED (Type.NON_FINAL),
        NON_ZERO_INTEGER_PARSED (Type.NON_FINAL),
        LEFT_TILDE (Type.NON_FINAL),
        FIRST_CHAR_PARSED (Type.NON_FINAL),
        SECOND_CHAR_PARSED (Type.NON_FINAL),
        RIGHT_TILDE (Type.NON_FINAL),
        ZERO_PARSED_STATE_TWO (Type.NON_FINAL),
        NON_ZERO_INTEGER_PARSED_STATE_TWO (Type.NON_FINAL),
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

  
    ArrayList<Transition> transitions = new ArrayList<>();
    State currentState = State.START_STATE;
    
    //Transition value = new Transition(new HashSet<Integer>(asList(0)),'\0', HashSet<Integer>(asList(0)));
    Integer src = 0;
    TransitionCondition range = new TransitionCondition('\0');
    Integer dest = 0;
    HashSet<Transition> list = new HashSet<>();
    
    public HashSet<Transition> evaluate(String input)
    {
        return evaluate(input, new IntPtr());
    }
    
    public HashSet<Transition> evaluate(String input, IntPtr pos)
    {
        currentState = State.START_STATE;
        src = 0;
        range = new TransitionCondition('\0');
        dest = 0;
        list = new HashSet<>();
        
        for (;currentState.type == State.Type.NON_FINAL && pos.dereference < input.length(); pos.dereference++)
        {
            char symbol = input.charAt(pos.dereference);
            currentState = transition(currentState, symbol);
        } // end for
        return (currentState.type == State.Type.FINAL) ? list : null;
    } // end evaluate()
 
    private State transition(State currentState, char symbol)
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
                        src = symbol - '0';
                    }
                    else if ('1' <= symbol && symbol <= '9')
                    {
                        currentState = State.NON_ZERO_INTEGER_PARSED;
                        src = symbol - '0';
                    }
                    else
                    {
                        currentState = State.UNDEFINED_TRANSITION;
                    }
                    break;
                case ZERO_PARSED:
                    if ('~' == symbol)
                    {
                        currentState = State.LEFT_TILDE;
                    }
                    else
                    {
                        currentState = State.UNDEFINED_TRANSITION;
                    }
                    break;
                case NON_ZERO_INTEGER_PARSED:
                    if ('0' <= symbol && symbol <= '9')
                    {
                        currentState = State.NON_ZERO_INTEGER_PARSED;
                        src *= 10;
                        src += symbol - '0';
                    }
                    else if ('~' == symbol)
                    {
                        currentState = State.LEFT_TILDE;
                    }
                    else
                    {
                        currentState = State.UNDEFINED_TRANSITION;
                    }
                    break;
                case LEFT_TILDE:
                    currentState = State.FIRST_CHAR_PARSED;
                    range.lBound = symbol;
                    break;
                case FIRST_CHAR_PARSED:
                    currentState = State.SECOND_CHAR_PARSED;
                    range.rBound = symbol;
                    break;
                case SECOND_CHAR_PARSED:
                    if ('~' == symbol)
                    {
                        currentState = State.RIGHT_TILDE;
                    }
                    else
                    {
                        currentState = State.UNDEFINED_TRANSITION;
                    }
                    break;
                case RIGHT_TILDE:
                    if ('0' == symbol)
                    {
                        currentState = State.ZERO_PARSED_STATE_TWO;
                        dest = symbol - '0';
                    }
                    else if ('1' <= symbol && symbol <= '9')
                    {
                        currentState = State.NON_ZERO_INTEGER_PARSED_STATE_TWO;
                        dest = symbol - '0';
                    }
                    else
                    {
                        currentState = State.UNDEFINED_TRANSITION;
                    }
                    break;
                case ZERO_PARSED_STATE_TWO:
                    if (' ' == symbol)
                    {
                        
                        list.add(new Transition(src,new HashSet<TransitionCondition>(asList(range)) ,dest));
                        src = 0;
                        range = new TransitionCondition('\0','\0');
                        dest = 0;
                        
                        currentState = State.SPACES;
                        
                        
                    }
                    else if (')' == symbol)
                    {
                        list.add(new Transition(src,new HashSet<TransitionCondition>(asList(range)) ,dest));
                        src = 0;
                        range = new TransitionCondition('\0','\0');
                        dest = 0;
                        currentState = State.FINAL_STATE;
                    }
                    else
                    {
                        currentState = State.UNDEFINED_TRANSITION;
                    }
                    break;
                case NON_ZERO_INTEGER_PARSED_STATE_TWO:
                    if ('0' <= symbol && symbol <= '9')
                    {
                        
                        
                        currentState = State.NON_ZERO_INTEGER_PARSED_STATE_TWO;
                        dest *= 10;
                        dest += symbol - '0';
                    }
                    else if (' ' == symbol)
                    {
                        list.add(new Transition(src,new HashSet<TransitionCondition>(asList(range)) ,dest));
                        src = 0;
                        range = new TransitionCondition('\0','\0');
                        dest = 0;
                        
                        currentState = State.SPACES;
                    }
                    else if (')' == symbol)
                    {
                        list.add(new Transition(src,new HashSet<TransitionCondition>(asList(range)) ,dest));
                        src = 0;
                        range = new TransitionCondition('\0','\0');
                        dest = 0;
                        
                        currentState = State.FINAL_STATE;
                    }
                    else
                    {
                        currentState = State.UNDEFINED_TRANSITION;
                    }
                    break;
                case SPACES:
                    if (' ' == symbol)
                    {
                        currentState = State.SPACES;
                    }
                    else if ('0' == symbol)
                    {
                        
                        
                        currentState = State.ZERO_PARSED;
                        src = symbol - '0';
                    }
                    else if ('1' <= symbol && symbol <= '9')
                    {
                        currentState = State.NON_ZERO_INTEGER_PARSED;
                        src = symbol - '0';
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
        return "Transitions";
    }
} // end 