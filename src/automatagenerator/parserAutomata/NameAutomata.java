package automatagenerator.parserAutomata;

/**
 *
 * @author Hayden
 */
public class NameAutomata 
{
    // An enumerated type for the states of a finite state automata
    private enum State
    {
        // set of non final states
        START (Type.NON_FINAL),
        FIRST_CHAR (Type.NON_FINAL),
        NAME_PARSING (Type.NON_FINAL),
        

        // set of final states
        FINAL (Type.FINAL),

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
    StringBuilder name;
    public static String parse(String input)
    {
        return new NameAutomata().evaluate(input);
    }
    
    public String evaluate(String input)
    {
        return evaluate(input, new IntPtr());
    }
    public String evaluate(String input, IntPtr position)
    {
        State currentState = State.START;
        name = new StringBuilder();
        for (;currentState.type == State.Type.NON_FINAL && position.dereference < input.length(); position.dereference++)
        {
            char symbol = input.charAt(position.dereference);
            currentState = transition(currentState, symbol);
        } // end for
        return (currentState.type == State.Type.FINAL) ? name.toString() : null;
    }

    public State transition(State state, char symbol)
    {
        switch (state)
        {
            case START:
                if ('(' == symbol)
                {
                    state = State.FIRST_CHAR;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case FIRST_CHAR:
                if ('A' <= symbol && symbol <= 'Z')
                {
                    state = State.NAME_PARSING;
                    name.append(symbol);
                }
                else if ('a' <= symbol && symbol <= 'z')
                {
                    state = State.NAME_PARSING;
                    name.append(symbol);
                }
                else if (')' == symbol)
                {
                    state = State.FINAL;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case NAME_PARSING:
                if (')' == symbol)
                {
                    state = State.FINAL;
                }
                else if ('A' <= symbol && symbol <= 'Z')
                {
                    state = State.NAME_PARSING;
                    name.append(symbol);
                }
                else if ('a' <= symbol && symbol <= 'z')
                {
                    state = State.NAME_PARSING;
                    name.append(symbol);
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case FINAL:
                state = State.UNDEFINED_TRANSITION;
                break;
            default:
                state = State.UNDEFINED_TRANSITION;
        } // end switch
        return state;
    } // end transition()
    
    public String toString()
    {
        return "Name";
    }
} // end 
