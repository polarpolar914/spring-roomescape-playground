package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Time;
import roomescape.dto.TimeAddRequest;
import roomescape.dto.TimeAddResponse;
import roomescape.service.TimeService;

@RestController
public class TimeController {
    @Autowired
    private TimeService timeService;

    @GetMapping("/times")
    public ResponseEntity<List<Time>> read() {
        List<Time> timeReadResponse = timeService.findAllTime();
        return ResponseEntity.ok().body(timeReadResponse);
    }

    @PostMapping("/times")
    public ResponseEntity<TimeAddResponse> create(final @Valid @RequestBody TimeAddRequest timeAddRequest) {
        Time time = timeService.createTime(timeAddRequest);
        TimeAddResponse timeAddResponse = TimeAddResponse.toDto(time.getId(), time.getTime());
        return ResponseEntity.created(URI.create("/times/" + time.getId()))
                .contentType(MediaType.APPLICATION_JSON).body(timeAddResponse);
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