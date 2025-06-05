package bt.edu.gcit.usermicroservice.service.impl;

import bt.edu.gcit.usermicroservice.entity.Event;
import bt.edu.gcit.usermicroservice.repository.EventRepository;
import bt.edu.gcit.usermicroservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public Event createEvent(Event event, MultipartFile image) {
        String imageUrl = storeImage(image);
        event.setImageUrl(imageUrl);
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    @Override
    public Event updateEvent(Long id, Event updatedEvent, MultipartFile image) {
        Event event = getEventById(id);

        event.setTitle(updatedEvent.getTitle());
        event.setLocation(updatedEvent.getLocation());
        event.setDate(updatedEvent.getDate());
        event.setTime(updatedEvent.getTime());
        event.setEventType(updatedEvent.getEventType());
        event.setDescription(updatedEvent.getDescription());
        event.setIsActive(updatedEvent.getIsActive());

        if (image != null && !image.isEmpty()) {
            String imageUrl = storeImage(image);
            event.setImageUrl(imageUrl);
        }

        return eventRepository.save(event);
    }

    @Override
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found");
        }
        eventRepository.deleteById(id);
    }

    private String storeImage(MultipartFile file) {
        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get("uploads/events/" + filename);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            return "/uploads/events/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store event image", e);
        }
    }
}
