package automata.definition;

import java.util.Stack;

/**
 *
 * @author Hayden
 */
public class RegexParser
{
    // An enumerated type for the states of a finite state automata
    private enum State
    {
        // set of non final states
        START_STATE (Type.NON_FINAL),
        TOKEN (Type.NON_FINAL),

        // set of final states
        CONCATENATE (Type.FINAL),
        OR (Type.FINAL),
        REPITITION (Type.FINAL),
        ZERO_OR_ONE (Type.FINAL),
        RANGE (Type.FINAL),

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
    // variable for holding string literal tokens
    StringBuilder value = new StringBuilder();
    // stack augments dfa to a deterministic pushdown automata (dpa)
    Stack<FiniteAutomata> expr = new Stack<>();
    
    State currentState;
    char symbol;
    int pos;
    public FiniteAutomata evaluate(String input)
    {
        currentState = State.START_STATE;
        for (pos = 0; pos < input.length(); pos++)
        {
            symbol = input.charAt(pos);
            currentState = transition(currentState, symbol);
        } // end for
        return currentState.type == State.Type.FINAL ? expr.pop().getDeterministic() : null;
        //return currentState.type == State.Type.FINAL ? Operations.NFAToDFA(expr.pop()) : null;
    } // end evaluate()
    private State transition(State current, char symbol)
    {
        State state;
        switch (current)
        {
            case START_STATE:
                if (' ' == symbol)
                {
                    state = State.START_STATE;
                }
                else if ('|' == symbol)
                {
                    state = State.OR;
                    FiniteAutomata right = expr.pop();
                    FiniteAutomata left = expr.pop();
                    expr.push(RegularOperations.or(left, right).getDeterministic());
                }
                else if ('*' == symbol)
                {
                    state = State.REPITITION;
                    FiniteAutomata operand = expr.pop();
                    expr.push(RegularOperations.repitition(operand).getDeterministic());
                }
                else if ('+' == symbol)
                {
                    state = State.REPITITION;
                    FiniteAutomata right = expr.pop();
                    FiniteAutomata left = expr.pop();
                    expr.push(RegularOperations.concatenate(left, right).getDeterministic());
                }
                else if ('-' == symbol)
                {
                    state = State.RANGE;
                    FiniteAutomata right = expr.pop();
                    FiniteAutomata left = expr.pop();
                    expr.push(RegularOperations.range(left, right).getDeterministic());
                }
                else if ('?' == symbol)
                {
                    state = State.ZERO_OR_ONE;
                    FiniteAutomata operand = expr.pop();
                    expr.push(RegularOperations.zeroOrOne(operand).getDeterministic());
                }
                else
                {
                    state = State.TOKEN;
                    value.append(symbol);
                }
                break;
            case CONCATENATE:
                if (' ' == symbol)
                {
                    state = State.START_STATE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case OR:
                if (' ' == symbol)
                {
                    state = State.START_STATE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case REPITITION:
                if (' ' == symbol)
                {
                    state = State.START_STATE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case ZERO_OR_ONE:
                if (' ' == symbol)
                {
                    state = State.START_STATE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case RANGE:
                if (' ' == symbol)
                {
                    state = State.START_STATE;
                }
                else
                {
                    state = State.UNDEFINED_TRANSITION;
                }
                break;
            case TOKEN:
                if (' ' == symbol)
                {
                    expr.add(RegularOperations.generateStringMatcher(value.toString()));
                    state = State.START_STATE;
                    value = new StringBuilder();
                }
                else
                {
                    state = State.TOKEN;
                    value.append(symbol);
                }
                break;
            default:
                state = State.UNDEFINED_TRANSITION;
                break;
        } // end switch
        return state;
    } // end transition()
} // end regexParser()