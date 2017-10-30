package pl.kelog.jazzstandards.database;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Song {
    
    @Id
    @GeneratedValue
    private Long id;
    
    private String title, backingTrackUrl;
    
    @OneToMany(mappedBy = "song", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<PracticeDay> practiceLog = new HashSet<>();
}
