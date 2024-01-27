package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.domain.Time;
import roomescape.service.TimeService;

@Controller
public class TimeController {
    @Autowired
    private TimeService timeService;

    @GetMapping("/times")
    public ResponseEntity<List<Time>> read() {
        List<Time> times = timeService.findAllTime();
        return ResponseEntity.ok().body(times);
    }

    @PostMapping("/times")
    public ResponseEntity<Time> create(final @Valid @RequestBody Time timeAddRequest) {
        Time time = timeService.createTime(timeAddRequest);
        return ResponseEntity.created(URI.create("/times/" + time.getId()))
                .contentType(MediaType.APPLICATION_JSON).body(time);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = timeService.deleteTime(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}