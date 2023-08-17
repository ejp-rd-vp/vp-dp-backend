package org.ejprarediseases.vpdpbackend.notification.v1.service;

import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.Period;
import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.enums.HttpStatusCategory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.enums.HttpStatusCategory.*;

@Service
public class NotificationService {
    public final Period DEFAULT_PERIOD = new Period(1, HOURS);

    public final List<HttpStatusCategory> ACCEPTED_HTTP_CATEGORIES =
            Arrays.asList(INFORMATIONAL_RESPONSE, SUCCESSFUL_RESPONSE);

    public final List<HttpStatusCategory> UNACCEPTED_HTTP_CATEGORIES =
            Arrays.asList(REDIRECTION_RESPONSE, CLIENT_ERROR_RESPONSE, SERVER_ERROR_RESPONSE);
}
