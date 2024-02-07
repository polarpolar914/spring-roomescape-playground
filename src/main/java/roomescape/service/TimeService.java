package roomescape.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.dto.TimeAddRequest;
import roomescape.dto.TimeReadResponse;
import roomescape.exception.NotFoundTimeException;
import roomescape.dao.TimeQueryingDAO;
import roomescape.dao.TimeUpdatingDAO;
import roomescape.domain.Time;

@Service
public class TimeService {
    private final TimeQueryingDAO timeQueryingDAO;
    private final TimeUpdatingDAO timeUpdatingDAO;

    @Autowired
    public TimeService(TimeQueryingDAO timeQueryingDAO, TimeUpdatingDAO timeUpdatingDAO) {
        this.timeQueryingDAO = timeQueryingDAO;
        this.timeUpdatingDAO = timeUpdatingDAO;
    }

    public TimeReadResponse findAllTime() {
        return TimeReadResponse.toDto(timeQueryingDAO.findAllTimes());
    }

    public Time createTime(TimeAddRequest timeAddRequest) {
        Time time = Time.toEntity(-1L, timeAddRequest.getTime());
        Long id = timeUpdatingDAO.insertWithKeyHolder(time);
        time.setId(id);

        return time;
    }

    public boolean deleteTime(Long id) {
        return timeUpdatingDAO.delete(id);
    }

    @ExceptionHandler(NotFoundTimeException.class)
    public ResponseEntity handleNotFoundTimeException(NotFoundTimeException e) {
        return ResponseEntity.badRequest().build();
    }
}
