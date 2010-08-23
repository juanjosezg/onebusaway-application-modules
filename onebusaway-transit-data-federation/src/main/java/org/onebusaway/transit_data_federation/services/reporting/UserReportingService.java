package org.onebusaway.transit_data_federation.services.reporting;

import java.util.List;

import org.onebusaway.gtfs.model.AgencyAndId;
import org.onebusaway.transit_data.model.TripProblemReportBean;

public interface UserReportingService {

  void reportProblemWithTrip(TripProblemReportBean problem);

  public List<TripProblemReportBean> getAllTripProblemReportsForTripId(
      AgencyAndId tripId);

  public TripProblemReportBean getTripProblemReportForId(long id);

  public void deleteTripProblemReportForId(long id);

}
