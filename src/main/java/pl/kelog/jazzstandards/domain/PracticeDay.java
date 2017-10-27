package pl.kelog.jazzstandards.domain;

import lombok.Getter;

import java.time.LocalDate;

public class PracticeDay {
    
    private Long id;
    
    private Song song;
    
    @Getter
    private LocalDate day;
    
    public static PracticeDay create(Song song) {
        PracticeDay practiceDay = new PracticeDay();
        practiceDay.song = song;
        practiceDay.day = LocalDate.now();
        practiceDay.id = ((long) (10000 * Math.random()));
        
        return practiceDay;
    }
}
