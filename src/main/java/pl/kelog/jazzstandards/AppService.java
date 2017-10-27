package pl.kelog.jazzstandards;

import org.springframework.stereotype.Service;
import pl.kelog.jazzstandards.database.PracticeDay;
import pl.kelog.jazzstandards.database.PracticeDayRepository;
import pl.kelog.jazzstandards.database.Song;
import pl.kelog.jazzstandards.database.SongRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AppService {
    
    private final SongRepository songRepository;
    private final PracticeDayRepository practiceDayRepository;
    
    AppService(SongRepository songRepository, PracticeDayRepository practiceDayRepository) {
        this.songRepository = songRepository;
        this.practiceDayRepository = practiceDayRepository;
    }
    
    List<Song> findAll() {
        return songRepository.findAll();
    }
    
    void create(String title, String backingTrackUrl) {
        Song song = new Song();
        song.setTitle(title);
        song.setBackingTrackUrl(backingTrackUrl);
        songRepository.save(song);
    }
    
    void saveTodayPractice(long songId) {
        Song song = findOrThrow(songId);
        if (song.getPracticeLog().stream().filter(practice -> practice.getDay().equals(LocalDate.now())).count() == 0) {
            PracticeDay practiceDay = new PracticeDay();
            practiceDay.setSong(song);
            practiceDay.setDay(LocalDate.now());
            practiceDayRepository.save(practiceDay);
        }
    }
    
    private Song findOrThrow(long songId) {
        return Optional.ofNullable(songRepository.getOne(songId)).orElseThrow(SongNotFoundException::new);
    }
    
    private static class SongNotFoundException extends RuntimeException {
    }
}
