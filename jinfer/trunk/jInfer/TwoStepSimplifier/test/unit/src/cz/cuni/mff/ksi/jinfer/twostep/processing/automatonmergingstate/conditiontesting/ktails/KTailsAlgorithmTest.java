/*
 * Copyright (C) 2014 otmar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.ktails;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author otmar
 */
public class KTailsAlgorithmTest {

    private Automaton<String> getDavidLosAutomaton() {
        List<String> trace1;
        trace1 = new LinkedList<>(Arrays.asList("A", "B", "C", "D", "E"));

        List<String> trace2;
        trace2 = new LinkedList<>(Arrays.asList("A", "B", "C", "X", "Y"));

        List<String> trace3;
        trace3 = new LinkedList<>(Arrays.asList("A", "E", "B", "D", "E"));

        Automaton<String> automaton;
        automaton = new Automaton<String>(true);

        automaton.buildPTAOnSymbol(trace1);
        automaton.buildPTAOnSymbol(trace2);
        automaton.buildPTAOnSymbol(trace3);
        return automaton;
    }

    @Test // Inspired in example on Table 3.4 of DavidLosThesis
    public void testDavidLoExampleTable34() throws InterruptedException {
        System.out.println("davidLo Examples - chapter 3");

        Automaton<String> automaton = this.getDavidLosAutomaton();

        Map<Integer, State<String>> states;

        states = new HashMap<>();

        for (State<String> st : automaton.getDelta().keySet()) {
            states.put(st.getName(), st);
        }

        assertEquals(12, states.size());

        int k = 2;

        final KTailsAlgorithm<String> alg = new KTailsAlgorithm<>(k);

        List<KTail<String>> sk0 = alg.findKTails(k, states.get(1), automaton.getDelta()).getKTails();
        List<KTail<String>> sk1 = alg.findKTails(k, states.get(2), automaton.getDelta()).getKTails();
        List<KTail<String>> sk2 = alg.findKTails(k, states.get(3), automaton.getDelta()).getKTails();
        List<KTail<String>> sk3 = alg.findKTails(k, states.get(4), automaton.getDelta()).getKTails();
        List<KTail<String>> sk4 = alg.findKTails(k, states.get(5), automaton.getDelta()).getKTails();
        List<KTail<String>> sk5 = alg.findKTails(k, states.get(6), automaton.getDelta()).getKTails();
        List<KTail<String>> sk6 = alg.findKTails(k, states.get(7), automaton.getDelta()).getKTails();
        List<KTail<String>> sk7 = alg.findKTails(k, states.get(8), automaton.getDelta()).getKTails();
        List<KTail<String>> sk8 = alg.findKTails(k, states.get(9), automaton.getDelta()).getKTails();
        List<KTail<String>> sk9 = alg.findKTails(k, states.get(10), automaton.getDelta()).getKTails();
        List<KTail<String>> sk10 = alg.findKTails(k, states.get(11), automaton.getDelta()).getKTails();
        List<KTail<String>> sk11 = alg.findKTails(k, states.get(12), automaton.getDelta()).getKTails();

        assertEquals(0, sk0.size());

        assertEquals(0, sk1.size());

        assertEquals(0, sk2.size());

        assertEquals(2, sk3.size()); // Lo's error
        assertEquals("D", sk3.get(0).getStr().peekFirst().getAcceptSymbol());
        assertEquals("E", sk3.get(0).getStr().peekLast().getAcceptSymbol());
        assertEquals("X", sk3.get(1).getStr().peekFirst().getAcceptSymbol());
        assertEquals("Y", sk3.get(1).getStr().peekLast().getAcceptSymbol());

        assertEquals(1, sk4.size());
        assertEquals("E", sk4.get(0).getStr().peekFirst().getAcceptSymbol());

        assertEquals(0, sk5.size());

        assertEquals(1, sk6.size());
        assertEquals("Y", sk6.get(0).getStr().peekFirst().getAcceptSymbol());

        assertEquals(0, sk7.size());

        assertEquals(0, sk8.size());

        assertEquals(1, sk9.size());
        assertEquals("D", sk9.get(0).getStr().peekFirst().getAcceptSymbol());
        assertEquals("E", sk9.get(0).getStr().peekLast().getAcceptSymbol());

        assertEquals(1, sk10.size());
        assertEquals("E", sk10.get(0).getStr().peekFirst().getAcceptSymbol());

        assertEquals(0, sk11.size());

    }

    // Instance based on Murphy's paper Passively Learning Finite Automata (1996), Figure 2
    private Automaton<String> getMurphysAutomatonFigure2() {
        List<String> trace1;
        trace1 = new LinkedList<>(Arrays.asList("b", "c", "b", "c", "a"));

        List<String> trace2;
        trace2 = new LinkedList<>(Arrays.asList("a", "a", "a", "b", "c", "a"));

        List<String> trace3;
        trace3 = new LinkedList<>(Arrays.asList("a", "a", "b", "c", "b", "c", "a"));

        List<String> trace4;
        trace4 = new LinkedList<>(Arrays.asList("a", "a", "a"));

        Automaton<String> automaton;
        automaton = new Automaton<>(true);

        State<String> s2 = automaton.createNewState();

        automaton.createNewStep("b", automaton.getInitialState(), s2);

        State<String> s3 = automaton.createNewState();

        automaton.createNewStep("c", s2, s3);

        State<String> s4 = automaton.createNewState();

        automaton.createNewStep("b", s3, s4);

        State<String> s5 = automaton.createNewState();

        automaton.createNewStep("c", s4, s5);

        State<String> s6 = automaton.createNewState();

        automaton.createNewStep("a", s5, s6);

        s6.incFinalCount();

        State<String> s7 = automaton.createNewState();

        automaton.createNewStep("a", automaton.getInitialState(), s7);

        State<String> s8 = automaton.createNewState();

        automaton.createNewStep("a", s7, s8);

        automaton.createNewStep("b", s8, s2);

        State<String> s9 = automaton.createNewState();

        s9.incFinalCount();

        automaton.createNewStep("a", s8, s9);

        automaton.createNewStep("b", s9, s4);

        // Note: lambda is NOT part of the alphabet!
        return automaton;
    }

    @Test
    public void MurphyExampleK1() throws InterruptedException {
        Automaton<String> automaton = getMurphysAutomatonFigure2();

        Map<Integer, State<String>> states;

        states = new HashMap<>();

        for (State<String> st : automaton.getDelta().keySet()) {
            states.put(st.getName(), st);
        }

        assertEquals(9, states.size());

        int k = 1;

        final KTailsAlgorithm<String> alg = new KTailsAlgorithm<>(k);

        List<KTail<String>> kt1 = alg.findKTails(k, states.get(1), automaton.getDelta()).getKTails();
        List<KTail<String>> kt2 = alg.findKTails(k, states.get(2), automaton.getDelta()).getKTails();
        List<KTail<String>> kt3 = alg.findKTails(k, states.get(3), automaton.getDelta()).getKTails();
        List<KTail<String>> kt4 = alg.findKTails(k, states.get(4), automaton.getDelta()).getKTails();
        List<KTail<String>> kt5 = alg.findKTails(k, states.get(5), automaton.getDelta()).getKTails();
        List<KTail<String>> kt6 = alg.findKTails(k, states.get(6), automaton.getDelta()).getKTails();
        List<KTail<String>> kt7 = alg.findKTails(k, states.get(7), automaton.getDelta()).getKTails();
        List<KTail<String>> kt8 = alg.findKTails(k, states.get(8), automaton.getDelta()).getKTails();
        List<KTail<String>> kt9 = alg.findKTails(k, states.get(9), automaton.getDelta()).getKTails();

        assertEquals(0, kt1.size());
        assertEquals(0, kt2.size());
        assertEquals(0, kt3.size());
        assertEquals(0, kt4.size());

        assertEquals(1, kt5.size());
        assertTrue(kt5.get(0).equalsToSequence("a"));

        assertEquals(0, kt6.size());
        assertEquals(0, kt7.size());

        assertEquals(1, kt8.size());
        assertTrue(kt8.get(0).equalsToSequence("a"));

        assertEquals(0, kt9.size());

    }

    @Test
    public void MurphyExampleK2() throws InterruptedException {
        Automaton<String> automaton = getMurphysAutomatonFigure2();

        Map<Integer, State<String>> states;

        states = new HashMap<>();

        for (State<String> st : automaton.getDelta().keySet()) {
            states.put(st.getName(), st);
        }

        assertEquals(9, states.size());

        int k = 2;

        final KTailsAlgorithm<String> alg = new KTailsAlgorithm<>(k);

        List<KTail<String>> kt1 = alg.findKTails(k, states.get(1), automaton.getDelta()).getKTails();
        List<KTail<String>> kt2 = alg.findKTails(k, states.get(2), automaton.getDelta()).getKTails();
        List<KTail<String>> kt3 = alg.findKTails(k, states.get(3), automaton.getDelta()).getKTails();
        List<KTail<String>> kt4 = alg.findKTails(k, states.get(4), automaton.getDelta()).getKTails();
        List<KTail<String>> kt5 = alg.findKTails(k, states.get(5), automaton.getDelta()).getKTails();
        List<KTail<String>> kt6 = alg.findKTails(k, states.get(6), automaton.getDelta()).getKTails();
        List<KTail<String>> kt7 = alg.findKTails(k, states.get(7), automaton.getDelta()).getKTails();
        List<KTail<String>> kt8 = alg.findKTails(k, states.get(8), automaton.getDelta()).getKTails();
        List<KTail<String>> kt9 = alg.findKTails(k, states.get(9), automaton.getDelta()).getKTails();

        assertEquals(0, kt1.size());
        assertEquals(0, kt2.size());
        assertEquals(0, kt3.size());
        assertEquals(1, kt4.size());
        assertTrue(kt4.get(0).equalsToSequence("c", "a"));

        assertEquals(1, kt5.size());
        assertTrue(kt5.get(0).equalsToSequence("a"));

        assertEquals(0, kt6.size());
        assertEquals(1, kt7.size());
        assertTrue("a", kt7.get(0).equalsToSequence("a", "a"));

        assertEquals(1, kt8.size());
        assertTrue(kt8.get(0).equalsToSequence("a"));

        assertEquals(0, kt9.size());
    }

    @Test
    public void MurphyExampleK3() throws InterruptedException {
        Automaton<String> automaton = getMurphysAutomatonFigure2();

        Map<Integer, State<String>> states;

        states = new HashMap<>();

        for (State<String> st : automaton.getDelta().keySet()) {
            states.put(st.getName(), st);
        }

        assertEquals(9, states.size());

        int k = 3;

        final KTailsAlgorithm<String> alg = new KTailsAlgorithm<>(k);

        List<KTail<String>> kt1 = alg.findKTails(k, states.get(1), automaton.getDelta()).getKTails();
        List<KTail<String>> kt2 = alg.findKTails(k, states.get(2), automaton.getDelta()).getKTails();
        List<KTail<String>> kt3 = alg.findKTails(k, states.get(3), automaton.getDelta()).getKTails();
        List<KTail<String>> kt4 = alg.findKTails(k, states.get(4), automaton.getDelta()).getKTails();
        List<KTail<String>> kt5 = alg.findKTails(k, states.get(5), automaton.getDelta()).getKTails();
        List<KTail<String>> kt6 = alg.findKTails(k, states.get(6), automaton.getDelta()).getKTails();
        List<KTail<String>> kt7 = alg.findKTails(k, states.get(7), automaton.getDelta()).getKTails();
        List<KTail<String>> kt8 = alg.findKTails(k, states.get(8), automaton.getDelta()).getKTails();
        List<KTail<String>> kt9 = alg.findKTails(k, states.get(9), automaton.getDelta()).getKTails();

        assertEquals(1, kt1.size());
        assertTrue(kt1.get(0).equalsToSequence("a", "a", "a"));

        assertEquals(0, kt2.size());

        assertEquals(1, kt3.size());
        assertTrue(kt3.get(0).equalsToSequence("b", "c", "a"));

        assertEquals(1, kt4.size());
        assertTrue(kt4.get(0).equalsToSequence("c", "a"));

        assertEquals(1, kt5.size());
        assertTrue(kt5.get(0).equalsToSequence("a"));

        assertEquals(0, kt6.size());

        assertEquals(1, kt7.size());
        assertTrue(kt7.get(0).equalsToSequence("a", "a"));

        assertEquals(1, kt8.size());
        assertTrue(kt8.get(0).equalsToSequence("a"));

        assertEquals(1, kt9.size());
        assertTrue(kt9.get(0).equalsToSequence("b", "c", "a"));
    }

    @Test
    public void MurphyExampleK4() throws InterruptedException {
        Automaton<String> automaton = getMurphysAutomatonFigure2();

        Map<Integer, State<String>> states;

        states = new HashMap<>();

        for (State<String> st : automaton.getDelta().keySet()) {
            states.put(st.getName(), st);
        }

        assertEquals(9, states.size());

        int k = 4;

        final KTailsAlgorithm<String> alg = new KTailsAlgorithm<>(k);

        List<KTail<String>> kt1 = alg.findKTails(k, states.get(1), automaton.getDelta()).getKTails();
        List<KTail<String>> kt2 = alg.findKTails(k, states.get(2), automaton.getDelta()).getKTails();
        List<KTail<String>> kt3 = alg.findKTails(k, states.get(3), automaton.getDelta()).getKTails();
        List<KTail<String>> kt4 = alg.findKTails(k, states.get(4), automaton.getDelta()).getKTails();
        List<KTail<String>> kt5 = alg.findKTails(k, states.get(5), automaton.getDelta()).getKTails();
        List<KTail<String>> kt6 = alg.findKTails(k, states.get(6), automaton.getDelta()).getKTails();
        List<KTail<String>> kt7 = alg.findKTails(k, states.get(7), automaton.getDelta()).getKTails();
        List<KTail<String>> kt8 = alg.findKTails(k, states.get(8), automaton.getDelta()).getKTails();
        List<KTail<String>> kt9 = alg.findKTails(k, states.get(9), automaton.getDelta()).getKTails();

        assertEquals(1, kt1.size());
        assertTrue(kt1.get(0).equalsToSequence("a", "a", "a"));

        assertEquals(1, kt2.size());
        assertTrue(kt2.get(0).equalsToSequence("c", "b", "c", "a"));

        assertEquals(1, kt3.size());
        assertTrue(kt3.get(0).equalsToSequence("b", "c", "a"));

        assertEquals(1, kt4.size());
        assertTrue(kt4.get(0).equalsToSequence("c", "a"));

        assertEquals(1, kt5.size());
        assertTrue(kt5.get(0).equalsToSequence("a"));

        assertEquals(0, kt6.size());

        assertEquals(1, kt7.size());
        assertTrue(kt7.get(0).equalsToSequence("a", "a"));

        assertEquals(2, kt8.size());
        assertTrue(kt8.get(0).equalsToSequence("a"));
        assertTrue(kt8.get(1).equalsToSequence("a", "b", "c", "a"));

        assertEquals(1, kt9.size());
        assertTrue(kt9.get(0).equalsToSequence("b", "c", "a"));
    }

    @Test
    public void testDavidLosMerge() throws InterruptedException {
        Automaton<String> automaton = this.getDavidLosAutomaton();

        KTailsAlgorithm<String> alg = new KTailsAlgorithm<>(2);
        
        alg.mergeStates(automaton);
        
        // Lo's example seems wrong, it should have 5 states.
        assertEquals(5, automaton.getDelta().size());
    }
    
    
    @Test
    public void testMurphysAutomatonMergeK1() throws InterruptedException{
        Automaton<String> automaton = this.getMurphysAutomatonFigure2();
        
        KTailsAlgorithm<String> alg = new KTailsAlgorithm<>(1);
        
        alg.mergeStates(automaton);
        
        // There is one less merge due to absence of lambda in this alphabet
        assertEquals(2, automaton.getDelta().size());
    }
    
    @Test
    public void testMurphysAutomatonMergeK2() throws InterruptedException{
        Automaton<String> automaton = this.getMurphysAutomatonFigure2();
        
        KTailsAlgorithm<String> alg = new KTailsAlgorithm<>(2);
        
        alg.mergeStates(automaton);
        
        // There is one less merge due to absence of lambda in this alphabet
        assertEquals(4, automaton.getDelta().size());
    }
    
    @Test
    public void testMurphysAutomatonMergeK3() throws InterruptedException{
        Automaton<String> automaton = this.getMurphysAutomatonFigure2();
        
        KTailsAlgorithm<String> alg = new KTailsAlgorithm<>(3);
        
        alg.mergeStates(automaton);
        
        // There is one less merge due to absence of lambda in this alphabet
        assertEquals(6, automaton.getDelta().size());
    }
    
    @Test
    public void testMurphysAutomatonMergeK4() throws InterruptedException{
        Automaton<String> automaton = this.getMurphysAutomatonFigure2();
        
        KTailsAlgorithm<String> alg = new KTailsAlgorithm<>(4);
        
        alg.mergeStates(automaton);
        
        // There is one less merge due to absence of lambda in this alphabet
        assertEquals(8, automaton.getDelta().size());
    }
}   
