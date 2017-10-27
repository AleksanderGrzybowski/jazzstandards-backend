package pl.kelog.jazzstandards;

import pl.kelog.jazzstandards.domain.Song;

import java.util.List;

public interface DataService {
    List<Song> findAll();
    
    void create(String title, String backingTrackUrl);
    
    void saveTodayPractice(long songId);
}
