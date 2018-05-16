package automatagenerator;

import automata.definition.FiniteAutomata;
import automata.definition.Transition;
import automata.definition.TransitionCondition;
import automatagenerator.parserAutomata.CompositeAutomata;
import automatagenerator.parserAutomata.CompositePayload;


import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Objects;

/**
 *
 * @author Hayden
 */
public class AutomataGenerator
{
    private enum ProgrammingLanguage
    {
        JAVA,
        PYTHON,
        C,
    }
    
    public static final ProgrammingLanguage plMode = ProgrammingLanguage.PYTHON;
    
    
    public static FiniteAutomata generateAutomata(
                                            String input, 
                                            PrintStream target) 
                                            throws FileNotFoundException
    {
        CompositePayload data = CompositeAutomata.parse(input);
        // TODO: checking for null doesn't work since an object can be
        // null there is ambiguity
        if (target == null || target.equals(""))
            return data.fa;
        else
            return outputDataToTarget(data, target);
    }
   
    // TODO: I can't even remember what this stuff is for should
    // probably replace with a actually usable interface
    public static FiniteAutomata generateAutomata(String input) throws FileNotFoundException
    {
        CompositePayload cp = CompositeAutomata.parse(input);
        return (cp!=null)?cp.fa:null;
    }
    public static FiniteAutomata generateAutomata(String input, String filePath) throws FileNotFoundException
    {
        return generateAutomata(input, new PrintStream(filePath));
    }
   
    // will output the finite automata to c/java/python code to the
    // given print stream
    private static FiniteAutomata outputDataToTarget(
                    CompositePayload automataData, PrintStream target)
    {
        FiniteAutomata result = new FiniteAutomata(automataData.fa);
        switch (plMode)
        {
            case JAVA:
                outputToJava(automataData,target);
                break;
            case C: 
                outputToC(automataData,target);
                break;
            case PYTHON:
                outputToPython(automataData,target);
                break;
        }
        return result;
    }
    
//    private static FiniteAutomata enumeratedVersion
//                                        (PrintStream target, 
//                                         CompositePayload automataData)
//    {
//        return automataData.fa;
//    }

    private static void outputToJava(CompositePayload automataData, PrintStream target)
    {
        target.printf("public class %s\n", automataData.name);
        target.printf("{\n");
        target.printf("%4c// An enumerated type for the states of a finite state automata\n", ' ');
        target.printf("%4cprivate enum State\n", ' ');
        target.printf("%4c{\n", ' ');
        
        // get maximum length of string
        int max = "UNDEFINED_TRANSITION".length();
        int temp = 0;
        for (Integer state : automataData.fa.states)
            if (automataData.hm.containsKey(state))
                if (max < (temp = automataData.hm.get(state).length()))
                    max = temp;
                else
                    ;
            else
                if (max < (temp = Converter.Converter.convert(
                        String.format("%d", state))
                        .length()))
                    max = temp;
        
                    
                        
        target.printf("%8c// set of non final states\n", ' ');
        for (Integer state : automataData.fa.states)
            if (!automataData.fa.finalStates.contains(state))
            {
                String stateName;
                if (automataData.hm.containsKey(state))
                    stateName = automataData.hm.get(state);
                else
                    stateName = Converter.Converter.convert(String.format("%d", state));
                target.printf("%8c%s",' ', stateName);
                for (int i = stateName.length(); i <= max; i++)
                    target.printf(" ");
                target.printf("(Type.NON_FINAL),\n");
            }
                        
        target.println();
        target.printf("%8c// set of final states\n", ' ');
        for (Integer state : automataData.fa.states)
            if (automataData.fa.finalStates.contains(state))
            {
                String stateName;
                if (automataData.hm.containsKey(state))
                    stateName = automataData.hm.get(state);
                else
                    stateName = Converter.Converter.convert(String.format("%d", state));
                    
                target.printf("%8c%s",' ', stateName);
                for (int i = stateName.length(); i <= max; i++)
                    target.printf(" ");
                target.printf("(Type.FINAL),\n");
            }
        
        target.println();

        target.printf("%8c// set of error states\n", ' ');
        target.printf("%8cUNDEFINED_TRANSITION", ' ');
        for (int i = "UNDEFINED_TRANSITION".length();i <= max;i++)
            target.printf(" ");
        target.printf("(Type.ERROR);\n\n");
        
        target.printf("%8c// indicates whether the state is a final, nonfinal or error state \n", ' ');
        target.printf("%8cprivate enum Type\n", ' ');
        target.printf("%8c{\n", ' ');
        target.printf("%12cFINAL,\n", ' ');
        target.printf("%12cNON_FINAL,\n", ' ');
        target.printf("%12cERROR;    \n", ' ');
        target.printf("%8c}\n\n", ' ');
        target.printf("%8cprivate final Type type;\n\n", ' ');

        target.printf("%8c// Initialize state with its type\n", ' ');
        target.printf("%8cState(Type type)\n", ' ');
        target.printf("%8c{\n", ' ');
        target.printf("%12cthis.type = type;\n", ' ');
        target.printf("%8c}\n", ' ');
        target.printf("%4c}\n\n", ' ');
        
        
        
        
        target.printf("%4cpublic static boolean parse(String input)\n", ' ');
        target.printf("%4c{\n",' ');
        target.printf("%8creturn new %s().evaluate(input);\n",' ',automataData.name);
        target.printf("%4c}\n",' ');
        
        
        target.printf("%4cpublic boolean evaluate(String input)\n", ' ');
        target.printf("%4c{\n",' ');
        target.printf("%8creturn evaluate(input, 0);\n",' ');
        target.printf("%4c}\n",' ');
        target.printf("%4cpublic boolean evaluate(String input, int position)\n", ' ');
        target.printf("%4c{\n",' ');
        
       
        // automata is assumed to be deterministic so initial state would only have one element
        /*
        if (automataData.hm.containsKey(automataData.fa.initialState.iterator().next()))
            target.printf("%8cState currentState = State.%s;\n",' ', automataData.hm.get(automataData.fa.initialState.iterator().next()) );
        else
            target.printf("%8cState currentState = State.%s;\n",' ', automataData.hm.get(automataData.fa.initialState.iterator().next()) );
        */
        target.printf("%8cState currentState = State.%s;\n",' ', 
                automataData.hm.containsKey(automataData.fa.initialState.iterator().next()) ? 
                                                            automataData.hm.get(automataData.fa.initialState.iterator().next())
                                                            :
                                                            Converter.Converter.convert(String.format("%d", 0)));
        
        target.printf("%8cfor (;currentState.type == State.Type.NON_FINAL && position < input.length(); position++)\n",' ');
        target.printf("%8c{\n",' ');
        target.printf("%12cchar symbol = input.charAt(position);\n",' ');
        target.printf("%12ccurrentState = transition(currentState, symbol);\n",' ');
        target.printf("%8c} // end for\n",' ');
        target.printf("%8creturn (currentState.type == State.Type.FINAL);\n",' ');
        target.printf("%4c}\n",' ');
        target.printf("\n");
        
        target.printf("%4cpublic State transition(State state, char symbol)\n", ' ' );
        target.printf("%4c{\n",' ');
        target.printf("%8cswitch (state)\n",' ');
        target.printf("%8c{\n",' ');
        
        for (Integer state : automataData.fa.states)
        {
            target.printf("%12ccase %s:\n",' ', automataData.hm.containsKey(state) ? 
                                                            automataData.hm.get(state)
                                                            :
                                                            Converter.Converter.convert(String.format("%d", state)));
            if (true && countTransitions(automataData.fa, state) > 0)
            {
                boolean first = true;
                for (Transition tr : automataData.fa.delta)
                {
                    if (state.equals(tr.antecedent.iterator().next()))
                    {
                        TransitionCondition tc = tr.incidence.iterator().next();
                        target.printf("%16c%sif (%s)\n",' ',(!first)?"else ": "" ,
                                (Objects.equals(tc.lBound, tc.rBound)) ? 
                                        String.format("'%c' == symbol", tc.lBound) : 
                                        String.format("'%c' <= symbol && symbol <= '%c'", tc.lBound,tc.rBound));

                        target.printf("%16c{\n",' ');
                        int cState = tr.consequent.iterator().next();
                        target.printf("%20cstate = State.%s;\n",' ',automataData.hm.containsKey(cState) ? 
                                                            automataData.hm.get(cState)
                                                            :
                                                            Converter.Converter.convert(String.format("%d", cState)));
                        target.printf("%16c}\n",' ');
                        first = false;
                    }
                }
                target.printf("%16celse\n",' ');
                target.printf("%16c{\n",' ');
                target.printf("%20cstate = State.UNDEFINED_TRANSITION;\n",' ');
                target.printf("%16c}\n",' ');
                
            }
            else
            {
                target.printf("%16cstate = State.UNDEFINED_TRANSITION;\n",' ');
            }
            target.printf("%16cbreak;\n",' ');
        }
        target.printf("%12cdefault:\n",' ');
        target.printf("%16cstate = State.UNDEFINED_TRANSITION;\n",' ');
        
        
        target.printf("%8c} // end switch\n",' ');
        target.printf("%8creturn state;\n",' ');
        target.printf("%4c} // end transition()\n",' ');
        target.printf("} // end %s\n", automataData.name);
        
    }
    private static void outputToC(CompositePayload automataData, PrintStream target)
    {
        target.printf("#include <stdlib.h>\n");
        target.printf("#include <math.h>\n");
        target.printf("#include <string.h>\n\n");
           // define macros
        target.printf("#define pair(X,Y) ((X << 2) + Y)\n");
        target.printf("#define left(Z) (Z >> 2)\n");
        target.printf("#define right(Z) (Z & 0b11)\n\n"); 
        
        // define the types of states
        target.printf("enum type\n");
        target.printf("{\n");
        target.printf("%4cNON_FINAL,\n", ' ');
        target.printf("%4cFINAL,\n",' ');
        target.printf("%4cERROR\n", ' ');
        target.printf("};\n");
        
        // defines the states in the staet machine
        target.printf("typedef enum AutomataStates\n");
        target.printf("{\n");
        int max = "UNDEFINED_TRANSITION".length();
        int temp = 0;
        for (Integer state : automataData.fa.states)
            if (automataData.hm.containsKey(state))
                if (max < (temp = automataData.hm.get(state).length()))
                    max = temp;
                else
                    ;
            else
                if (max < (temp = Converter.Converter.convert(String.format("%d", state)).length()))
                    max = temp;
        target.printf("%4c// set of non final states\n", ' ');
        for (Integer state : automataData.fa.states)
            if (!automataData.fa.finalStates.contains(state))
            {
                String stateName;
                if (automataData.hm.containsKey(state))
                    stateName = automataData.hm.get(state);
                else
                    stateName = Converter.Converter.convert(String.format("%d", state));
                target.printf("%4c%s = ",' ', stateName);
                for (int i = stateName.length(); i <= max; i++)
                    target.printf(" ");
                target.printf("pair(NON_FINAL, %d),\n", state);
            }
        target.println();
        target.printf("%4c// set of final states\n", ' ');
        for (Integer state : automataData.fa.states)
            if (automataData.fa.finalStates.contains(state))
            {
                String stateName;
                if (automataData.hm.containsKey(state))
                    stateName = automataData.hm.get(state);
                else // TODO: i need a better naming convention
                     // Converter.Converter.convert is just silly
                    stateName = Converter.Converter.convert(String.format("%d", state));
                    
                target.printf("%4c%s = ",' ', stateName);
                for (int i = stateName.length(); i <= max; i++)
                    target.printf(" ");
                target.printf("pair(FINAL, %d),\n", state);
            }
        target.println();
        target.printf("%4c// set of error states\n", ' ');
        target.printf("%4cUNDEFINED_TRANSITION = ", ' ');
        for (int i = "UNDEFINED_TRANSITION".length();i <= max;i++)
            target.printf(" ");
        target.printf("pair(ERROR, -1)\n");
        
        target.printf("}State;\n");
        
        
        // define function prototypes for evaluate and transition
        target.printf("int evaluate(char *);\n");
        target.printf("State transition(State, char);\n\n");
        
        
        // define evaluate for running state machine on given string
        target.printf("int evaluate(char * input)\n");
        target.printf("{\n");
        target.printf("%4cState current = %s;\n", ' ', automataData.hm.containsKey(automataData.fa.initialState.iterator().next()) ? automataData.hm.get(automataData.fa.initialState.iterator().next()): Converter.Converter.convert(String.format("%d",automataData.fa.initialState.iterator().next())));
        target.printf("%4cfor (int i = 0 ;right(current) != ERROR && input[i] != '\\0'; i++)\n", ' ');
        target.printf("%4c{\n", ' ');
        target.printf("%8ccurrent = transition(current, input[i]);\n", ' ');
        target.printf("%4c}\n", ' ');
        target.printf("%4creturn right(current) == FINAL;\n", ' ');
        target.printf("}\n");
        
        
        target.printf("\n");
        
        // define delta transition function of a state machine
        target.printf("State transition(State current, char symbol)\n");
        target.printf("{\n");
        target.printf("%4cswitch(current)\n",' ');
        target.printf("%4c{\n", ' ');
        for (Integer state : automataData.fa.states)
        {
            boolean first = true;
            target.printf("%8ccase %s:\n",' ', (automataData.hm.containsKey(state)? automataData.hm.get(state):Converter.Converter.convert(state)));
            for (Transition tt : automataData.fa.delta)
            {
                
                if (tt.antecedent.iterator().next() == state)
                {
                   // target.printf("%s\n", tt);
                    TransitionCondition tc = tt.incidence.iterator().next();
                    
                    target.printf("%12c%sif (%s)\n", ' ', first ? "" : "else ", tc.lBound == tc.rBound ? String.format("'%s' == symbol", tc.lBound) : String.format("'%s' <= symbol && symbol <= '%s'", tc.lBound, tc.rBound));
                    target.printf("%12c{\n", ' ');
                    target.printf("%16ccurrent = %s;\n", ' ', automataData.hm.containsKey(tt.consequent.iterator().next()) ? automataData.hm.get(tt.consequent.iterator().next()) : Converter.Converter.convert(tt.antecedent.iterator().next()));
                    target.printf("%12c}\n",' ');
                    first = false;
                }
            }
            if (!first)
            {
                target.printf("%12celse\n", ' ');
                target.printf("%12c{\n", ' ');
                target.printf("%16ccurrent = UNDEFINED_TRANSITION;\n",' ');
                target.printf("%12c}\n", ' ');
                target.printf("%12cbreak;", ' ');
                target.println();
            }
            else
            {
                target.printf("%12ccurrent = UNDEFINED_TRANSITION;\n",' ');
            }
        }
        target.printf("%8cdefault:\n", ' ');
        target.printf("%12ccurrent = UNDEFINED_TRANSITION;\n", ' ');
        target.printf("%8cbreak;\n", ' ');
        target.printf("%4c}\n", ' ');
        target.printf("%4creturn current;\n", ' ');
        target.print("}\n");
     
    }
    private static void outputToPython(CompositePayload automataData, PrintStream target)
    {
        target.printf("from enum import Enum                             \n", ' ');
        target.println();
        
        target.printf("class Type(Enum):                                 \n", ' ');
        target.printf("%4cNON_FINAL = 0                                \n", ' ');
        target.printf("%4cFINAL = 1                                    \n", ' ');
        target.printf("%4cERROR = 2                                    \n", ' ');
        target.println();
        
        target.printf("class State(Enum):                                \n", ' ');
        int max = "UNDEFINED_TRANSITION".length();
        int temp = 0;
        for (Integer state : automataData.fa.states)
            if (automataData.hm.containsKey(state))
                if (max < (temp = automataData.hm.get(state).length()))
                    max = temp;
                else
                    ;
            else
                if (max < (temp = Converter.Converter.convert(String.format("%d", state)).length()))
                    max = temp;
        target.printf("%4c# set of non final states\n", ' ');
        int count = 0;
        for (Integer state : automataData.fa.states)
            if (!automataData.fa.finalStates.contains(state))
            {
                String stateName;
                if (automataData.hm.containsKey(state))
                    stateName = automataData.hm.get(state);
                else
                    stateName = Converter.Converter.convert(String.format("%d", state));
                target.printf("%4c%s = ",' ', stateName);
                for (int i = stateName.length(); i <= max; i++)
                    target.printf(" ");
                target.printf("(%d, Type.NON_FINAL)\n", count++);
            }
        target.println();
        target.printf("%4c# set of final states\n", ' ');
        count = 0;
        for (Integer state : automataData.fa.states)
            if (automataData.fa.finalStates.contains(state))
            {
                String stateName;
                if (automataData.hm.containsKey(state))
                    stateName = automataData.hm.get(state);
                else
                    stateName = Converter.Converter.convert(String.format("%d", state));
                    
                target.printf("%4c%s = ",' ', stateName);
                for (int i = stateName.length(); i <= max; i++)
                    target.printf(" ");
                target.printf("(%d, Type.FINAL.value)\n", count++);
            }
        target.println();
        target.printf("%4c# set of error states\n", ' ');
        target.printf("%4cUNDEFINED_TRANSITION = ", ' ');
        for (int i = "UNDEFINED_TRANSITION".length();i <= max;i++)
            target.printf(" ");
        target.printf("(0, Type.ERROR.value)\n");

        
        target.println();
        
        
        target.printf("def parse(input):\n");
        target.printf("%4cstate = State.%s\n", ' ', automataData.hm.get(automataData.fa.initialState.iterator().next()));
        target.printf("%4cfor symbol in input:\n", ' ');
        target.printf("%8cstate = transition(state, symbol)\n", ' ');
        target.printf("%4creturn state.value[1] == Type.FINAL.value\n", ' ');
        
        target.println();
        target.printf("def transition(state, symbol):\n");

        boolean firstIf = true;
        for (Integer state : automataData.fa.states)
        {
            target.printf("%4c%s State.%s == state:\n",' ', (firstIf)?"if":"elif",
                                                    automataData.hm.containsKey(state) ? 
                                                            automataData.hm.get(state)
                                                            :
                                                            Converter.Converter.convert(String.format("%d", state)));
            firstIf = false;
            if (countTransitions(automataData.fa, state) > 0)
            {
                boolean first = true;
                for (Transition tr : automataData.fa.delta)
                {
                    if (state.equals(tr.antecedent.iterator().next()))
                    {
                        TransitionCondition tc = tr.incidence.iterator().next();
                        target.printf("%8c%s %s:\n",' ',(!first)?"elif ": "if" ,
                                (Objects.equals(tc.lBound, tc.rBound)) ? 
                                        String.format("ord('%c') == ord(symbol)", tc.lBound) : 
                                        String.format("ord('%c') <= ord(symbol) <= ord('%c')", tc.lBound,tc.rBound));

                        int cState = tr.consequent.iterator().next();
                        target.printf("%12cstate = State.%s\n",' ',automataData.hm.containsKey(cState) ? 
                                                            automataData.hm.get(cState)
                                                            :
                                                            Converter.Converter.convert(String.format("%d", cState)));
                        first = false;
                    }
                }
                target.printf("%8celse:\n",' ');
                target.printf("%12cstate = State.UNDEFINED_TRANSITION\n",' ');
                
            }
            else
            {
                target.printf("%16cstate = State.UNDEFINED_TRANSITION\n",' ');
            }
        }
        target.printf("%4celse:\n",' ');
        target.printf("%8cstate = State.UNDEFINED_TRANSITION\n",' ');
        
        
        target.printf("%4creturn state\n",' ');
    }

    private static int countTransitions(FiniteAutomata fa, Integer state)
    {
        int count = 0;
        for (Transition tt : fa.delta)
            if (tt.antecedent.iterator().next().equals(state))
                count++;
        return count;
    }
}

