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
    
    @Test // Inspired in example on Table 3.4 of DavidLosThesis
    public void testDavidLoExampleTable34() throws InterruptedException {
        System.out.println("davidLo Examples - chapter 3");
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
       
        Map<Integer, State<String>> states;
       
        states = new HashMap<>();
       
        for (State<String> st : automaton.getDelta().keySet()){
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
}
