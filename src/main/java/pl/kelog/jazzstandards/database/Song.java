package pl.kelog.jazzstandards.database;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
    
    @OneToMany(mappedBy = "song")
    private Set<PracticeDay> practiceLog = new HashSet<>();
}
