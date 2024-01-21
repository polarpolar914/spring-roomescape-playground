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
                resultSet.getString("date"), new Time(resultSet.getLong("time_id"), resultSet.getString("time")));
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
        String sql = "SELECT \n"
                + "    r.id as reservation_id, \n"
                + "    r.name, \n"
                + "    r.date, \n"
                + "    t.id as time_id, \n"
                + "    t.time as time_value \n"
                + "FROM reservation as r inner join time as t on r.time_id = t.id";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }
}
