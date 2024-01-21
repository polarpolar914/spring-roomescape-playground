package roomescape;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TimeQueryingDAO {
    private static TimeQueryingDAO instance = null;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Time> timeRowMapper = (resultSet, rowNum) -> {
        Time time = new Time(resultSet.getLong("id"), resultSet.getString("time"));
        return time;
    };

    protected TimeQueryingDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static TimeQueryingDAO getInstance(JdbcTemplate jdbcTemplate) {
        if (instance == null) {
            instance = new TimeQueryingDAO(jdbcTemplate);
        }
        return instance;
    }

    public List<Time> findAllTimes() {
        String sql = "select * from time";
        return jdbcTemplate.query(sql, timeRowMapper);
    }
}
