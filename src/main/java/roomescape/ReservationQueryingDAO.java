package roomescape;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReservationQueryingDAO {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> {
        Reservation reservation = new Reservation(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getString("date"), resultSet.getString("time"));
        return reservation;
    };

    public ReservationQueryingDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAllReservations() {
        String sql = "select * from reservation";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }
}
