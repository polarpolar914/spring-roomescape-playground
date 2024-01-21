package roomescape;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Reservation> findAllReservations() {
        ReservationQueryingDAO reservationQueryingDAO = ReservationQueryingDAO.getInstance(jdbcTemplate);
        return reservationQueryingDAO.findAllReservations();
    }

    public Long createReservation(Reservation reservation) {
        ReservationUpdatingDAO reservationUpdatingDAO = ReservationUpdatingDAO.getInstance(jdbcTemplate);
        return reservationUpdatingDAO.insertWithKeyHolder(reservation);
    }

    public boolean deleteReservation(Long id) {
        ReservationUpdatingDAO reservationUpdatingDAO = ReservationUpdatingDAO.getInstance(jdbcTemplate);
        return reservationUpdatingDAO.delete(id);
    }
}
