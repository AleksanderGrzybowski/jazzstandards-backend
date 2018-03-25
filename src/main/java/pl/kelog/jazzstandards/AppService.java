package pl.kelog.jazzstandards;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.kelog.jazzstandards.database.PracticeDay;
import pl.kelog.jazzstandards.database.PracticeDayRepository;
import pl.kelog.jazzstandards.database.Song;
import pl.kelog.jazzstandards.database.SongRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AppService {
    
    private final DateService dateService;
    
    private final SongRepository songRepository;
    private final PracticeDayRepository practiceDayRepository;
    
    AppService(DateService dateService, SongRepository songRepository, PracticeDayRepository practiceDayRepository) {
        this.dateService = dateService;
        this.songRepository = songRepository;
        this.practiceDayRepository = practiceDayRepository;
    }
    
    List<Song> findAll() {
        return songRepository.findAll();
    }
    
    void create(String title, String backingTrackUrl) {
        validateNotNullAndNotEmpty(title, backingTrackUrl);
        
        Song song = new Song();
        song.setTitle(title);
        song.setBackingTrackUrl(backingTrackUrl);
        songRepository.save(song);
    }
    
    void toggleTodayPractice(long songId) {
        Song song = findOrThrow(songId);
    
        Optional<PracticeDay> practiceDay = findTodaysPractice(song);
        if (practiceDay.isPresent()) {
            practiceDayRepository.delete(practiceDay.get());
        } else {
            PracticeDay newPracticeDay = new PracticeDay();
            newPracticeDay.setSong(song);
            newPracticeDay.setDay(dateService.now());
            practiceDayRepository.save(newPracticeDay);
        }
    }
    
    private Optional<PracticeDay> findTodaysPractice(Song song) {
        return song.getPracticeLog().stream()
                .filter(practice -> practice.getDay().equals(dateService.now()))
                .findFirst();
    }
    
    private Song findOrThrow(long songId) {
        return Optional.ofNullable(songRepository.getOne(songId)).orElseThrow(SongNotFoundException::new);
    }
    
    private void validateNotNullAndNotEmpty(String... params) {
        for (String param : params) {
            if (param == null || param.isEmpty()) {
                throw new ValidationException();
            }
        }
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    static class ValidationException extends RuntimeException {
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    static class SongNotFoundException extends RuntimeException {
    }
}
