package org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Period {
    private int value;
    private ChronoUnit unit;
}
