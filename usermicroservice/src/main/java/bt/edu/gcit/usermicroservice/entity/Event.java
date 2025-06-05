package bt.edu.gcit.usermicroservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String location;
    private String date;
    private String time;
    private String eventType;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;

    @Builder.Default
    private Boolean isActive = true;
}
