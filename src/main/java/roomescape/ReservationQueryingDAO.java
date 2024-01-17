package roomescape;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationQueryingDAO {
    private static ReservationQueryingDAO instance = null;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> {
        Reservation reservation = new Reservation(resultSet.getLong("id"), resultSet.getString("name"),
                resultSet.getString("date"), resultSet.getString("time"));
        return reservation;
    };

    protected ReservationQueryingDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static ReservationQueryingDAO getInstance(JdbcTemplate jdbcTemplate) {
        if (instance == null) {
            instance = new ReservationQueryingDAO(jdbcTemplate);
        }
        return instance;
    }

    public List<Reservation> findAllReservations() {
        String sql = "select * from reservation";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }
}
