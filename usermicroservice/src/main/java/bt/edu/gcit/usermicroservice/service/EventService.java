package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.entity.Event;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {
    Event createEvent(Event event, MultipartFile image);

    List<Event> getAllEvents();

    Event getEventById(Long id);

    Event updateEvent(Long id, Event event, MultipartFile image);

    void deleteEvent(Long id);
}
