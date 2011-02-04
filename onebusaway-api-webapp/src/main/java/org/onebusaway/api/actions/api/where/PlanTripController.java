package org.onebusaway.api.actions.api.where;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.onebusaway.api.actions.api.ApiActionSupport;
import org.onebusaway.api.model.transit.BeanFactoryV2;
import org.onebusaway.api.model.transit.ItineraryV2BeanFactory;
import org.onebusaway.api.model.transit.tripplanning.ItinerariesV2Bean;
import org.onebusaway.exceptions.ServiceException;
import org.onebusaway.transit_data.model.tripplanning.ConstraintsBean;
import org.onebusaway.transit_data.model.tripplanning.ItinerariesBean;
import org.onebusaway.transit_data.services.TransitDataService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.conversion.annotations.TypeConversion;

public class PlanTripController extends ApiActionSupport {

  private static final long serialVersionUID = 1L;

  private static final int V2 = 2;

  private TransitDataService _transitDataService;

  private double _latFrom;

  private double _lonFrom;

  private double _latTo;

  private double _lonTo;

  private ConstraintsBean _constraints = new ConstraintsBean();

  public PlanTripController() {
    super(V2);
  }

  @Autowired
  public void setTransitDataService(TransitDataService transitDataService) {
    _transitDataService = transitDataService;
  }

  public void setLatFrom(double latFrom) {
    _latFrom = latFrom;
  }

  public void setLonFrom(double lonFrom) {
    _lonFrom = lonFrom;
  }

  public void setLatTo(double latTo) {
    _latTo = latTo;
  }

  public void setLonTo(double lonTo) {
    _lonTo = lonTo;
  }

  @TypeConversion(converter = "org.onebusaway.presentation.impl.conversion.DateTimeConverter")
  public void setTime(Date time) {
    _constraints.setTime(time.getTime());
  }

  public void setArriveBy(boolean arriveBy) {
    _constraints.setArriveBy(arriveBy);
  }

  public void setResultCount(int resultCount) {
    _constraints.setResultCount(resultCount);
  }

  public void setUseRealTime(boolean useRealTime) {
    _constraints.setUseRealTime(useRealTime);
  }
  
  public void setMode(List<String> modes) {
    _constraints.setModes(new HashSet<String>(modes));
  }

  public void setWalkSpeed(double walkSpeed) {
    _constraints.setWalkSpeed(walkSpeed);
  }

  public void setWalkReluctance(double walkReluctance) {
    _constraints.setWalkReluctance(walkReluctance);
  }

  public void setMaxWalkingDistance(double maxWalkingDistance) {
    _constraints.setMaxWalkingDistance(maxWalkingDistance);
  }

  public void setInitialWaitReluctance(double initialWaitReluctance) {
    _constraints.setInitialWaitReluctance(initialWaitReluctance);
  }

  public void setWaitReluctance(double waitReluctance) {
    _constraints.setWaitReluctance(waitReluctance);
  }

  public void setMinTransferTime(int minTransferTime) {
    _constraints.setMinTransferTime(minTransferTime);
  }

  public void setTransferCost(int transferCost) {
    _constraints.setTransferCost(transferCost);
  }

  public void setMaxTransfers(int maxTransfers) {
    _constraints.setMaxTransfers(maxTransfers);
  }

  public DefaultHttpHeaders index() throws IOException, ServiceException {

    if (_constraints.getTime() == 0)
      _constraints.setTime(System.currentTimeMillis());

    ItinerariesBean itineraries = _transitDataService.getItinerariesBetween(
        _latFrom, _lonFrom, _latTo, _lonTo, _constraints);

    BeanFactoryV2 factory = getBeanFactoryV2();
    ItineraryV2BeanFactory itineraryFactory = new ItineraryV2BeanFactory(
        factory);

    ItinerariesV2Bean bean = itineraryFactory.getItineraries(itineraries);
    return setOkResponse(factory.entry(bean));
  }

}