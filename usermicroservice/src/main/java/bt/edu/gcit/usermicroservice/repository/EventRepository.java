package bt.edu.gcit.usermicroservice.repository;

import bt.edu.gcit.usermicroservice.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
