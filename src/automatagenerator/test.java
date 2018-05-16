package automatagenerator;

/**
 *
 * @author Hayden
 */
public class test
{
    // An enumerated type for the states of a finite state automata
    private enum State
    {
        // set of non final states
        STARTING_STATE (Type.NON_FINAL),
        UNARY (Type.NON_FINAL),

        // set of final states
        ZERO_PARSED (Type.FINAL),
        NON_ZERO_PARSED (Type.FINAL),

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

    public boolean evaluate(String input)
    {
        return evaluate(input, 0);
    }
    public boolean evaluate(String input, int position)
    {
        State currentState = State.STARTING_STATE;
        for (;currentState.type == State.Type.NON_FINAL && position < input.length(); position++)
        {
            char symbol = input.charAt(position);
            currentState = transition(currentState, symbol);
        } // end for
        return (currentState.type == State.Type.FINAL);
    }

    public State transition(State state, char symbol)
    {
        switch (state)
        {
            case STARTING_STATE:
                if ('+' == symbol)
                {
                    state = State.STARTING_STATE;
                }
                if ('0' == symbol)
                {
                    state = State.STARTING_STATE;
                }
                if ('1' <= symbol && symbol <= '9')
                {
                    state = State.STARTING_STATE;
                }
                if ('-' == symbol)
                {
                    state = State.STARTING_STATE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case UNARY:
                if ('0' == symbol)
                {
                    state = State.UNARY;
                }
                if ('1' <= symbol && symbol <= '9')
                {
                    state = State.UNARY;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case ZERO_PARSED:
                    state = State.UNDEFINED_TRANSITION;
                break;
            case NON_ZERO_PARSED:
                if ('0' <= symbol && symbol <= '9')
                {
                    state = State.NON_ZERO_PARSED;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            default:
                state = State.UNDEFINED_TRANSITION;
        } // end switch
        return state;
    } // end transition()
} // end 