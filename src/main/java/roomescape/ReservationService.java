package roomescape;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Service
public class ReservationService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Reservation> findAllReservations() {
        ReservationQueryingDAO reservationQueryingDAO = ReservationQueryingDAO.getInstance(jdbcTemplate);
        return reservationQueryingDAO.findAllReservations();
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
