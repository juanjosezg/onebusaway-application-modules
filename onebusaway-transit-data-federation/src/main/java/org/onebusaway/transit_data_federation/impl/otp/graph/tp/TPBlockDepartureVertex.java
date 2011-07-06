package org.onebusaway.transit_data_federation.impl.otp.graph.tp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.onebusaway.transit_data_federation.impl.otp.GraphContext;
import org.onebusaway.transit_data_federation.impl.otp.graph.SearchLocal;
import org.onebusaway.transit_data_federation.services.realtime.ArrivalAndDepartureInstance;
import org.onebusaway.transit_data_federation.services.tripplanner.TransferPatternService;
import org.opentripplanner.routing.core.Edge;
import org.opentripplanner.routing.core.HasEdges;

public class TPBlockDepartureVertex extends AbstractTPPathStateVertex implements
    SearchLocal {

  private final ArrivalAndDepartureInstance _departure;

  private final ArrivalAndDepartureInstance _arrival;

  private Object _searchLocalValue = null;

  public TPBlockDepartureVertex(GraphContext context, TPState pathState,
      ArrivalAndDepartureInstance departure, ArrivalAndDepartureInstance arrival) {
    super(context, pathState, true);
    _departure = departure;
    _arrival = arrival;
  }

  public ArrivalAndDepartureInstance getDeparture() {
    return _departure;
  }

  public ArrivalAndDepartureInstance getArrival() {
    return _arrival;
  }

  /****
   * {@link HasEdges} Interface
   ****/

  @Override
  public Collection<Edge> getIncoming() {

    List<Edge> edges = new ArrayList<Edge>();

    if (_pathState.hasTransfers()) {
      TransferPatternService tpService = _context.getTransferPatternService();
      List<TPState> transferStates = _pathState.getTransferStates(tpService);
      for (TPState nextState : transferStates) {
        Edge edge = new TPTransferEdge(_context, _pathState, nextState,
            _departure, _arrival, true);
        edges.add(edge);
      }
    }

    if (_pathState.isExitAllowed()) {
      TPDepartureVertex from = new TPDepartureVertex(_context, _pathState);
      Edge edge = new TPFreeEdge(_context, from, this);
      return Arrays.asList(edge);
    }

    return edges;
  }

  @Override
  public Collection<Edge> getOutgoing() {
    TPBlockHopEdge edge = new TPBlockHopEdge(_context, _pathState, _departure,
        _arrival);
    edge.setFromVertex(this);
    return Arrays.asList((Edge) edge);
  }

  @Override
  public String toString() {
    return "TPBlockDepartureVertex(" + _pathState.toString() + ")";
  }

  /****
   * Search Local
   ****/

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getSearchLocalValue() {
    return (T) _searchLocalValue;
  }

  @Override
  public <T> void setSearchLocalValue(T value) {
    _searchLocalValue = value;
  }
}
