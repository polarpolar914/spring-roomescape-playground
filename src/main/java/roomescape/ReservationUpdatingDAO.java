package roomescape;

import java.sql.PreparedStatement;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationUpdatingDAO {
    private static ReservationUpdatingDAO instance = null;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> {
        Reservation reservation = new Reservation(resultSet.getLong("id"), resultSet.getString("name"),
                resultSet.getString("date"), resultSet.getString("time"));
        return reservation;
    };

    protected ReservationUpdatingDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static ReservationUpdatingDAO getInstance(JdbcTemplate jdbcTemplate) {
        if (instance == null) {
            instance = new ReservationUpdatingDAO(jdbcTemplate);
        }
        return instance;
    }

    public Long insertWithKeyHolder(Reservation reservation) {
        String sql = "insert into reservation (name, date, time) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, reservation.getName());
            preparedStatement.setString(2, reservation.getDate());
            preparedStatement.setString(3, reservation.getTime());
            return preparedStatement;
        }, keyHolder);

        reservation.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return keyHolder.getKey().longValue();
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        int num = jdbcTemplate.update(sql, id);
        if (num == 0) {
            return false;
        }
        return true;
    }
}
