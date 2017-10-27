package pl.kelog.jazzstandards.domain;

import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Song {
    
    @Getter
    private Long id;
    
    @Getter
    private String title, backingTrackUrl;
    
    private List<PracticeDay> practiceLog = new ArrayList<>();
    
    public static Song create(String title, String backingTrackUrl) {
        Song song = new Song();
        song.title = title;
        song.backingTrackUrl = backingTrackUrl;
        song.id = (long) (10000 * Math.random());
        return song;
    }
    
    public void addPractice() {
        if (practiceLog.stream().filter(practice -> practice.getDay().equals(LocalDate.now())).count() == 0) {
            practiceLog.add(PracticeDay.create(this));
        }
    }
    
    public List<PracticeDay> getSortedPracticeLog() {
        return Collections.unmodifiableList(practiceLog);
    }
}
