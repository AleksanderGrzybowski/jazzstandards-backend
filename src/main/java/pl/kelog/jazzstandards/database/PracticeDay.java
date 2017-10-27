package pl.kelog.jazzstandards.database;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class PracticeDay {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;
    
    private LocalDate day;
    
    public PracticeDay() {
    }
}
