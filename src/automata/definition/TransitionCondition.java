package automata.definition;

import java.util.function.Predicate;

/**
 *
 * @author Hayden
 */
public class TransitionCondition implements Predicate<Character>
{
    public Character lBound;
    public Character rBound;

    public TransitionCondition()
    {
        lBound = 0;
        rBound = 0;
    }
    public TransitionCondition(Character symbol)
    {
        lBound = symbol;
        rBound = symbol;
    }
    public TransitionCondition(Character ll, Character rr)
    {
        lBound = ll;
        rBound = rr;
    }

    @Override
    public boolean test(Character t)
    {
        return (lBound <= t && t <= rBound);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }

        final TransitionCondition other = (TransitionCondition) obj;
        if (lBound.charValue() != other.lBound.charValue())
        {
            return false;
        }
        if (rBound.charValue() != other.rBound.charValue())
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        if (lBound.equals(rBound))
            return String.format("\'%s\'", lBound);
        else
            return String.format("\'%s\'..\'%s\'", lBound,rBound);
    }
}