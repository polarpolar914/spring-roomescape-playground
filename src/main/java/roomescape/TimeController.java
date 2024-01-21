package roomescape;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TimeController {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    protected TimeController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/time")
    public String time() {
        return "time";
    }

    @GetMapping("/times")
    public ResponseEntity<List<Time>> read() {
        TimeQueryingDAO timeQueryingDAO = TimeQueryingDAO.getInstance(jdbcTemplate);
        List<Time> times = timeQueryingDAO.findAllTimes();
        return ResponseEntity.ok().body(times);
    }

    @PostMapping("/times")
    public ResponseEntity<Time> create(@RequestBody Time time) {
        if (time.getTime().isEmpty()) {
            return handleNotFoundTimeException(new NotFoundTimeException("Time of Time is empty"));
        }

        TimeUpdatingDAO timeUpdatingDAO = TimeUpdatingDAO.getInstance(jdbcTemplate);
        Long id = timeUpdatingDAO.insertWithKeyHolder(time);
        time.setId(id);

        return ResponseEntity.created(URI.create("/times/" + time.getId()))
                .contentType(MediaType.APPLICATION_JSON).body(time);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        TimeUpdatingDAO timeUpdatingDAO = TimeUpdatingDAO.getInstance(jdbcTemplate);
        boolean exist = timeUpdatingDAO.delete(id);
        if (!exist) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @ExceptionHandler(NotFoundTimeException.class)
    public ResponseEntity handleNotFoundTimeException(NotFoundTimeException e) {
        return ResponseEntity.badRequest().build();
    }
}