/*
 * Copyright (C) 2011 anti
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

import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
class KTBucket<T> {

    private List<KTail<T>> ktails;

    private int k;

    public KTBucket(final int k) {
        this.ktails = new LinkedList<KTail<T>>();
        this.k = k;
    }

    public void preceede(Step<T> step, double probability) {
        for (KTail<T> str : this.ktails) {
            if (str.size() <= this.k) {
                str.preceede(step, probability);
            }
        }
    }

    public void add(Step<T> step, double probability) {
        this.ktails.add(new KTail<T>(step, probability));
    }

    public void add(KTail<T> str) {
        this.ktails.add(str);
    }

    public void addAll(KTBucket<T> anotherBucket) {
        this.ktails.addAll(anotherBucket.getKTails());
    }

    public List<KTail<T>> getKTails() {
        return Collections.unmodifiableList(this.ktails);
    }

    public KTBucket<T> getMostProbable(final double s) {
        double intersum = 0.0;
        KTBucket<T> result = new KTBucket<T>(this.k);
        Collections.sort(this.ktails, new Comparator<KTail<T>>() {

            @Override
            public int compare(KTail<T> o1, KTail<T> o2) {
                if (o1.getProbability() > o2.getProbability()) {
                    return -1;
                } else if (o1.getProbability() < o2.getProbability()) {
                    return 1;
                }
                return 0;
            }
        });
        Iterator<KTail<T>> it = this.ktails.iterator();
        while ((intersum < s) && (it.hasNext())) {
            KTail<T> t = it.next();
            result.add(t);
            intersum += t.getProbability();
        }
        return result;
    }

    public boolean areSubset(KTBucket<T> anotherBucket) {
        if (this.ktails.isEmpty() && !anotherBucket.getKTails().isEmpty()) {
            return false;
        }
        return (anotherBucket.getKTails().containsAll(this.ktails));
    }

    @Override
    public String toString() {
        return "KTBucket{" + "k-tails=" + ktails + '}';
    }
}
