package roomescape.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
import roomescape.dto.ReservationAddRequest;
import roomescape.domain.Reservation;
import roomescape.service.ReservationService;

@Controller
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> read() {
        List<Reservation> reservations = reservationService.findAllReservations();
        return ResponseEntity.ok().body(reservations);
    }

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> create(final @Valid @RequestBody ReservationAddRequest reservationAddRequest) {
        Reservation reservation = reservationService.createReservation(reservationAddRequest);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId()))
                .contentType(MediaType.APPLICATION_JSON).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(final @PathVariable @Positive Long id) {
        boolean exist = reservationService.deleteReservation(id);
        if (!exist) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}
