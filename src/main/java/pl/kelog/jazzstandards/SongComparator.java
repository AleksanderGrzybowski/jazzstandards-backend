package pl.kelog.jazzstandards;

import org.springframework.stereotype.Component;
import pl.kelog.jazzstandards.database.PracticeDay;
import pl.kelog.jazzstandards.database.Song;

import java.time.LocalDate;
import java.util.Comparator;

@Component
public class SongComparator implements Comparator<Song> {
    
    private final int TIMESPAN_DAYS = 14;
    
    private final DateService dateService;
    
    public SongComparator(DateService dateService) {
        this.dateService = dateService;
    }
    
    @Override
    public int compare(Song o1, Song o2) {
        return score(o2) - score(o1);
    }
    
    private int score(Song song) {
        int weight = 1;
        int score = 0;
        for (LocalDate iterator = dateService.now().minusDays(TIMESPAN_DAYS);
             iterator.isBefore(dateService.now()) || iterator.isEqual(dateService.now());
             iterator = iterator.plusDays(1)) {
            
            for (PracticeDay log : song.getPracticeLog()) {
                if (log.getDay().equals(iterator)) {
                    score += Math.pow(2, weight);
                    break;
                }
            }
            weight++;
        }
        return score;
    }
}
