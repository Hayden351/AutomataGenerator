package automata.definition;

import static java.util.Arrays.asList;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 *
 * @author Hayden
 */

// denotes a transition in state machine
public class Transition implements Comparable<Transition>, 
                                   BiPredicate<HashSet<Integer>, Character>
{
    
    public HashSet<Integer> antecedent;
    public HashSet<TransitionCondition> incidence;
    public HashSet<Integer> consequent;
    
    public Transition(Integer aa, Character ii, Integer cc)
    {
        antecedent = new HashSet<>(asList(aa));
        incidence = new HashSet<TransitionCondition>(asList(new TransitionCondition(ii,ii)));
        consequent = new HashSet<>(asList(cc));
    }
    public Transition(Integer aa, Character ii, HashSet<Integer> cc)
    {
        antecedent = new HashSet<>(asList(aa));
        incidence = new HashSet<TransitionCondition>(asList(new TransitionCondition(ii,ii)));
        consequent = new HashSet<>(cc);
    }
    public Transition(Integer aa, Character iiL, Character iiR, Integer cc)
    {
        antecedent = new HashSet<>(asList(aa));
        incidence = new HashSet<TransitionCondition>(asList(new TransitionCondition(iiL,iiR)));
        consequent = new HashSet<>(asList(cc));
    }
    public Transition(Integer aa, Character iiL, Character iiR, HashSet<Integer> cc)
    {
        antecedent = new HashSet<>(asList(aa));
        incidence = new HashSet<TransitionCondition>(asList(new TransitionCondition(iiL,iiR)));
        consequent = new HashSet<>(cc);
    }
    public Transition(Integer aa, HashSet<TransitionCondition> ii, Integer cc)
    {
        antecedent = new HashSet<>(asList(aa));
        incidence = new HashSet<TransitionCondition>(ii);
        consequent = new HashSet<>(asList(cc));
    }
    public Transition(Integer aa, HashSet<TransitionCondition> ii, HashSet<Integer> cc)
    {
        antecedent = new HashSet<>(asList(aa));
        incidence = new HashSet<TransitionCondition>(ii);
        consequent = new HashSet<>(cc);
    }
    public Transition(HashSet<Integer> aa, Character ii, Integer cc)
    {
        antecedent = new HashSet<>(aa);
        incidence = new HashSet<TransitionCondition>(asList(new TransitionCondition(ii,ii)));
        consequent = new HashSet<>(asList(cc));
    }
    public Transition(HashSet<Integer> aa, Character ii, HashSet<Integer> cc)
    {
        antecedent = new HashSet<>(aa);
        incidence = new HashSet<TransitionCondition>(asList(new TransitionCondition(ii,ii)));
        consequent = new HashSet<>(cc);
    }
    public Transition(HashSet<Integer> aa, Character iiL, Character iiR, Integer cc)
    {
        antecedent = new HashSet<>(aa);
        incidence = new HashSet<TransitionCondition>(asList(new TransitionCondition(iiL,iiR)));
        consequent = new HashSet<>(asList(cc));
    }
    public Transition(HashSet<Integer> aa, Character iiL, Character iiR, HashSet<Integer> cc)
    {
        antecedent = new HashSet<>(aa);
        incidence = new HashSet<TransitionCondition>(asList(new TransitionCondition(iiL,iiR)));
        consequent = new HashSet<>(cc);
    }
    public Transition(HashSet<Integer> aa, HashSet<TransitionCondition> ii, Integer cc)
    {
        antecedent = new HashSet<>(aa);
        incidence = new HashSet<TransitionCondition>(ii);
        consequent = new HashSet<>(asList(cc));
    }
    public Transition(HashSet<Integer> aa, HashSet<TransitionCondition> ii, HashSet<Integer> cc)
    {
        antecedent = new HashSet<>(aa);
        incidence = new HashSet<TransitionCondition>(ii);
        consequent = new HashSet<>(cc);
    }
    
    public static int godelPair(int x, int y)
    {
        return ((int)Math.round(Math.pow(2,x))) * (2 * y + 1) - 1;
    }
    
    @Override
    // this is modified pairing function to avoid integer overflow
    public int hashCode()
    {
        //return godelPair(godelPair(godelPair(antecedent.hashCode() % 23,consequent.hashCode() % 127) % 23,incidence.hashCode() % 127) % 23,incidence.hashCode() % 127);
        
        int value = godelPair(godelPair(godelPair(antecedent.iterator().next() % 23,consequent.iterator().next() % 127) % 23,(int)incidence.iterator().next().lBound % 127) % 23,(int)incidence.iterator().next().rBound % 127);
        return value;
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
        
        final Transition other = (Transition)obj;
        if (!Objects.equals(this.antecedent, other.antecedent))
        {
            return false;
        }
//        if (!Objects.equals(this.incidence, other.incidence))
//        {
//            return false;
//        }
        if (!incidence.iterator().next().equals(other.incidence.iterator().next()))
            return false;
        return Objects.equals(this.consequent, other.consequent);
    }

    @Override
    public int compareTo(Transition o)
    {   
        if (this.equals(o))
            return 0;
        int val = hashCode() - o.hashCode();
        if (val == 0)
            return 42;
        return val;
    }
    
    @Override
    public String toString()
    {
        return String.format("(%s %s %s)", antecedent, incidence, consequent);
    }

    // returns true if there exists a rule that will fire on the character and
    // will return false otherwise
    public boolean test(Integer state, Character sym)
    {
        return test(new HashSet<>(asList(state)),sym);
    }
    
    @Override
    public boolean test(HashSet<Integer> states, Character sym)
    {
        // tests to see if character satisfies at least one of the conditions
        // to transition
        for (Integer state : states)
            if (antecedent.contains(state))
                if (t(sym))
                    return true;
        return false;
    }
    
    public boolean t(char sym)
    {
        for (TransitionCondition ii : incidence)    
            if (ii.test(sym))
                return true;
        return false;
    }
}