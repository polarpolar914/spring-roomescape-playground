package roomescape.dto;

import java.util.List;
import roomescape.domain.Reservation;

public class ReservationReadResponse {
    private List<Reservation> reservations;

    public ReservationReadResponse(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
