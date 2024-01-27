package roomescape.dao;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.exception.NotFoundTimeException;
import roomescape.domain.Time;

@Repository
public class TimeUpdatingDAO {
    private static TimeUpdatingDAO instance;
    private final JdbcTemplate jdbcTemplate;

    protected TimeUpdatingDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static TimeUpdatingDAO getInstance(JdbcTemplate jdbcTemplate) {
        if (instance == null) {
            instance = new TimeUpdatingDAO(jdbcTemplate);
        }
        return instance;
    }

    public Long insertWithKeyHolder(Time time) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("time")
                .usingGeneratedKeyColumns("id");

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("time", time.getTime());

        Number key = jdbcInsert.executeAndReturnKey(namedParameters);

        time.setId(key.longValue());

        return key.longValue();
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM time WHERE id = ?";
        int num = jdbcTemplate.update(sql, id);
        if (num == 0) { // 삭제할 대상이 없는 경우
            handleNotFoundTimeException(new NotFoundTimeException("Time is not found in DB with id: " + id));
            return false;
        }
        return true;
    }

    @ExceptionHandler(NotFoundTimeException.class)
    public ResponseEntity handleNotFoundTimeException(NotFoundTimeException e) {
        return ResponseEntity.badRequest().build();
    }
}
