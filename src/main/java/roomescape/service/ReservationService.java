package roomescape.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.dto.ReservationAddRequest;
import roomescape.dto.ReservationReadResponse;
import roomescape.exception.NotFoundReservationException;
import roomescape.dao.ReservationQueryingDAO;
import roomescape.dao.ReservationUpdatingDAO;
import roomescape.dao.TimeQueryingDAO;
import roomescape.domain.Reservation;

@Service
public class ReservationService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ReservationReadResponse findAllReservations() {
        ReservationQueryingDAO reservationQueryingDAO = ReservationQueryingDAO.getInstance(jdbcTemplate);
        List<Reservation> reservations = reservationQueryingDAO.findAllReservations();
        ReservationReadResponse reservationReadResponse = ReservationReadResponse.toDto(reservations);
        return reservationReadResponse;
    }

    public Reservation createReservation(ReservationAddRequest reservationAddRequest) {
        TimeQueryingDAO timeQueryingDAO = TimeQueryingDAO.getInstance(jdbcTemplate);
        ReservationUpdatingDAO reservationUpdatingDAO = ReservationUpdatingDAO.getInstance(jdbcTemplate);
        Reservation reservation = Reservation.toEntity(-1, reservationAddRequest.getName(), reservationAddRequest.getDate(),
                timeQueryingDAO.findTimeById(reservationAddRequest.getTime()));

        Long id = reservationUpdatingDAO.insertWithKeyHolder(reservation);
        reservation.setId(id);

        return reservation;
    }

    public boolean deleteReservation(Long id) {
        ReservationUpdatingDAO reservationUpdatingDAO = ReservationUpdatingDAO.getInstance(jdbcTemplate);
        return reservationUpdatingDAO.delete(id);
    }

    @ExceptionHandler(NotFoundReservationException.class)
    public ResponseEntity handleNotFoundReservationException(NotFoundReservationException e) {
        return ResponseEntity.badRequest().build();
    }
}
