package org.onebusaway.transit_data_federation.impl.otp;

import org.onebusaway.transit_data_federation.impl.otp.graph.EdgeNarrativeImpl;
import org.opentripplanner.routing.core.Edge;
import org.opentripplanner.routing.core.EdgeNarrative;
import org.opentripplanner.routing.core.State;
import org.opentripplanner.routing.core.StateEditor;
import org.opentripplanner.routing.core.TraverseOptions;
import org.opentripplanner.routing.core.Vertex;

public class OBAState extends State {

  int maxBlockSequence;

  long initialWaitTime;

  TripSequence tripSequence;

  boolean lookaheadItinerary;

  public OBAState(long time, Vertex vertex, TraverseOptions opt) {
    super(time, vertex, opt);
  }

  @Override
  public State createState(long time, Vertex vertex, TraverseOptions options) {
    return new OBAState(time, vertex, options);
  }

  @Override
  public StateEditor edit(Edge e) {
    return new OBAStateEditor(this, e);
  }

  @Override
  public StateEditor edit(Edge e, EdgeNarrative en) {
    if (en instanceof EdgeNarrativeImpl) {
      EdgeNarrativeImpl impl = (EdgeNarrativeImpl) en;
      if (getOptions().isArriveBy())
        impl.setToVertex(vertex);
      else
        impl.setFromVertex(vertex);
    }
    return new OBAStateEditor(this, e, en);
  }

  public int getMaxBlockSequence() {
    return maxBlockSequence;
  }

  public long getInitialWaitTime() {
    return initialWaitTime;
  }

  public TripSequence getTripSequence() {
    return tripSequence;
  }

  public boolean isLookaheadItinerary() {
    return lookaheadItinerary;
  }
}
