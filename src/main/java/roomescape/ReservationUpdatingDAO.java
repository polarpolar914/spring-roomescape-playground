package roomescape;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Repository
public class ReservationUpdatingDAO {
    private static ReservationUpdatingDAO instance = null;
    private final JdbcTemplate jdbcTemplate;

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
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time", reservation.getTime());

        Number key = jdbcInsert.executeAndReturnKey(namedParameters);

        reservation.setId(key.longValue());

        return key.longValue();
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        int num = jdbcTemplate.update(sql, id);
        if (num == 0) { // 삭제할 대상이 없는 경우
            handleNotFoundReservationException(new NotFoundReservationException("Reservation is not found in DB with id: " + id));
            return false;
        }
        return true;
    }

    @ExceptionHandler(NotFoundReservationException.class)
    public ResponseEntity handleNotFoundReservationException(NotFoundReservationException e) {
        return ResponseEntity.badRequest().build();
    }
}
