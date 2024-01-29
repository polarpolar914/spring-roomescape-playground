package roomescape.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.dto.TimeAddRequest;
import roomescape.exception.NotFoundTimeException;
import roomescape.dao.TimeQueryingDAO;
import roomescape.dao.TimeUpdatingDAO;
import roomescape.domain.Time;

@Service
public class TimeService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Time> findAllTime() {
        TimeQueryingDAO timeQueryingDAO = TimeQueryingDAO.getInstance(jdbcTemplate);
        return timeQueryingDAO.findAllTimes();
    }

    public Time createTime(TimeAddRequest timeAddRequest) {
        TimeUpdatingDAO timeUpdatingDAO = TimeUpdatingDAO.getInstance(jdbcTemplate);

        Time time = Time.toEntity(-1L, timeAddRequest.getTime());
        Long id = timeUpdatingDAO.insertWithKeyHolder(time);
        time.setId(id);

        return time;
    }

    public boolean deleteTime(Long id) {
        TimeUpdatingDAO timeUpdatingDAO = TimeUpdatingDAO.getInstance(jdbcTemplate);
        return timeUpdatingDAO.delete(id);
    }

    @ExceptionHandler(NotFoundTimeException.class)
    public ResponseEntity handleNotFoundTimeException(NotFoundTimeException e) {
        return ResponseEntity.badRequest().build();
    }
}
