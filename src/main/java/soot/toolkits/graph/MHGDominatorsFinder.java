package soot.toolkits.graph;

/*-
 * #%L
 * Soot - a J*va Optimization Framework
 * %%
 * Copyright (C) 2005 Navindra Umanee <navindra@cs.mcgill.ca>
 * Copyright (C) 2007 Eric Bodden
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Calculate dominators for basic blocks.
 * <p>
 * Uses the algorithm contained in Dragon book, pg. 670-1.
 *
 * <pre>
 *       D(n0) := { n0 }
 *       for n in N - { n0 } do D(n) := N;
 *       while changes to any D(n) occur do
 *         for n in N - {n0} do
 *             D(n) := {n} U (intersect of D(p) over all predecessors p of n)
 * </pre>
 *
 * 2007/07/03 - updated to use {@link BitSet}s instead of {@link HashSet}s, as the most expensive operation in this algorithm
 * used to be cloning of the fullSet, which is very cheap for {@link BitSet}s.
 *
 * @author Navindra Umanee
 * @author Eric Bodden
 **/
public class MHGDominatorsFinder<N> implements DominatorsFinder<N> {

  protected DirectedGraph<N> graph;
  protected List<N> heads;
  protected Map<N, BitSet> nodeToFlowSet;
  protected Map<N, Integer> nodeToIndex;
  protected Map<Integer, N> indexToNode;
  protected int lastIndex = 0;

  public MHGDominatorsFinder(DirectedGraph<N> graph) {
    this.graph = graph;
    doAnalysis();
  }

  protected void doAnalysis() {
    heads = graph.getHeads();
    nodeToFlowSet = new HashMap<N, BitSet>();
    nodeToIndex = new HashMap<N, Integer>();
    indexToNode = new HashMap<Integer, N>();

    // build full set
    BitSet fullSet = new BitSet(graph.size());
    fullSet.flip(0, graph.size());// set all to true

    // set up domain for intersection: head nodes are only dominated by themselves,
    // other nodes are dominated by everything else
    for (N o : graph) {
      if (heads.contains(o)) {
        BitSet self = new BitSet();
        self.set(indexOf(o));
        nodeToFlowSet.put(o, self);
      } else {
        nodeToFlowSet.put(o, fullSet);
      }
    }

    boolean changed;
    do {
      changed = false;
      for (N o : graph) {
        if (heads.contains(o)) {
          continue;
        }

        // initialize to the "neutral element" for the intersection
        // this clone() is fast on BitSets (opposed to on HashSets)
        BitSet predsIntersect = (BitSet) fullSet.clone();

        // intersect over all predecessors
        for (N next : graph.getPredsOf(o)) {
          predsIntersect.and(nodeToFlowSet.get(next));
        }

        BitSet oldSet = nodeToFlowSet.get(o);
        // each node dominates itself
        predsIntersect.set(indexOf(o));
        if (!predsIntersect.equals(oldSet)) {
          nodeToFlowSet.put(o, predsIntersect);
          changed = true;
        }
      }
    } while (changed);
  }

  protected int indexOf(N o) {
    Integer index = nodeToIndex.get(o);
    if (index == null) {
      index = lastIndex;
      nodeToIndex.put(o, index);
      indexToNode.put(index, o);
      lastIndex++;
    }
    return index;
  }

  @Override
  public DirectedGraph<N> getGraph() {
    return graph;
  }

  @Override
  public List<N> getDominators(N node) {
    // reconstruct list of dominators from bitset
    List<N> result = new ArrayList<N>();
    BitSet bitSet = nodeToFlowSet.get(node);
    for (int i = 0; i < bitSet.length(); i++) {
      if (bitSet.get(i)) {
        result.add(indexToNode.get(i));
      }
    }
    return result;
  }

  @Override
  public N getImmediateDominator(N node) {
    // root node
    if (heads.contains(node)) {
      return null;
    }

    // could be memoised, I guess

    List<N> dominatorsList = getDominators(node);
    dominatorsList.remove(node);

    N immediateDominator = null;
    for (N dominator : dominatorsList) {
      if (isDominatedByAll(dominator, dominatorsList)) {
        immediateDominator = dominator;
        if (immediateDominator != null) {
          break;
        }
      }
    }
    // NOTE: 'immediateDominator' can be 'null' with postdominators on methods
    // that have multiple points of return.
    return immediateDominator;
  }

  @Override
  public boolean isDominatedBy(N node, N dominator) {
    return getDominators(node).contains(dominator);
  }

  @Override
  public boolean isDominatedByAll(N node, Collection<N> dominators) {
    return getDominators(node).containsAll(dominators);
  }
}
