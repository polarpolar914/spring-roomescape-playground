package roomescape;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Service
public class TimeService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Time> findAllTime() {
        TimeQueryingDAO timeQueryingDAO = TimeQueryingDAO.getInstance(jdbcTemplate);
        return timeQueryingDAO.findAllTimes();
    }

    public ResponseEntity<Time> createTime(Time time) {
        TimeUpdatingDAO timeUpdatingDAO = TimeUpdatingDAO.getInstance(jdbcTemplate);

        if (time.getTime().isEmpty()) {
            return handleNotFoundTimeException(new NotFoundTimeException("Time of Time is empty"));
        }

        Long id = timeUpdatingDAO.insertWithKeyHolder(time);
        time.setId(id);

        return ResponseEntity.created(URI.create("/times/" + time.getId()))
                .contentType(MediaType.APPLICATION_JSON).body(time);
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
