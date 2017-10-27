package pl.kelog.jazzstandards;

import org.springframework.stereotype.Service;
import pl.kelog.jazzstandards.domain.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class InMemoryDataService implements DataService {
    
    private final List<Song> songs = new ArrayList<>();
    
    @Override
    public List<Song> findAll() {
        return Collections.unmodifiableList(songs);
    }
    
    @Override
    public void create(String title, String backingTrackUrl) {
        songs.add(Song.create(title, backingTrackUrl));
    }
    
    @Override
    public void saveTodayPractice(long songId) {
        Song song = findById(songId);
        song.addPractice();
    }
    
    private Song findById(long songId) {
        return songs.stream().filter(s -> s.getId() == songId).findFirst().orElseThrow(SongNotFoundException::new);
    }
    
    private static class SongNotFoundException extends RuntimeException {
    }
}
