package roomescape;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @GetMapping("/reservation")
    public String reservation() {
        return "new-reservation";
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> read() {
        List<Reservation> reservations = reservationService.findAllReservations();
        return ResponseEntity.ok().body(reservations);
    }

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> create(@RequestBody Reservation reservation) {
        if (reservation.getName().isEmpty()) {
            return handleNotFoundReservationException(new NotFoundReservationException("Name of Reservation is empty"));
        }
        if (reservation.getDate().isEmpty()) {
            return handleNotFoundReservationException(new NotFoundReservationException("Date of Reservation is empty"));
        }
        if (reservation.getTime() == null) {
            return handleNotFoundReservationException(new NotFoundReservationException("Time of Reservation is empty"));
        }

        Long id = reservationService.createReservation(reservation);
        reservation.setId(id);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId()))
                .contentType(MediaType.APPLICATION_JSON).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean exist = reservationService.deleteReservation(id);
        if (!exist) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @ExceptionHandler(NotFoundReservationException.class)
    public ResponseEntity handleNotFoundReservationException(NotFoundReservationException e) {
        return ResponseEntity.badRequest().build();
    }

}
