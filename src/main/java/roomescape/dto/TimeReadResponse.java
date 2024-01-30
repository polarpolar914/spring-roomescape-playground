package roomescape.dto;

import java.util.List;
import roomescape.domain.Time;

public class TimeReadResponse {
    private final List<Time> times;

    public TimeReadResponse(List<Time> times) {
        this.times = times;
    }
}