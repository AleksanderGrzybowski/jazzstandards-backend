package pl.kelog.jazzstandards;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.kelog.jazzstandards.database.PracticeDay;
import pl.kelog.jazzstandards.database.Song;

import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SuppressWarnings("Duplicates")
public class SongComparatorTest {
    
    private static final LocalDate TODAY = LocalDate.of(2017, Month.OCTOBER, 20);
    private static final LocalDate YESTERDAY = TODAY.minusDays(1);
    
    private SongComparator songComparator;
    
    @Before
    public void setup() {
        DateService dateService = Mockito.mock(DateService.class);
        when(dateService.now()).thenReturn(TODAY);
        
        songComparator = new SongComparator(dateService);
    }
    
    //            yesterday today
    //  first                 *
    //  second  
    //
    @Test
    public void test1() {
        Song first = new Song();
        addPracticeToSong(TODAY, first);
        Song second = new Song();
        
        assertThat(
                Stream.of(second, first).sorted(songComparator).collect(toList())
        ).isEqualTo(asList(first, second));
    }
    
    //            yesterday today
    //  first                 *
    //  second         *
    //
    @Test
    public void test2() {
        Song first = new Song();
        addPracticeToSong(TODAY, first);
        Song second = new Song();
        addPracticeToSong(YESTERDAY, second);
        
        assertThat(
                Stream.of(second, first).sorted(songComparator).collect(toList())
        ).isEqualTo(asList(first, second));
    }
    
    //            yesterday today
    //  first          *      *
    //  second         *
    //
    @Test
    public void test3() {
        Song first = new Song();
        addPracticeToSong(YESTERDAY, first);
        addPracticeToSong(TODAY, first);
        Song second = new Song();
        addPracticeToSong(YESTERDAY, second);
        
        assertThat(
                Stream.of(second, first).sorted(songComparator).collect(toList())
        ).isEqualTo(asList(first, second));
    }
    
    //            yesterday today
    //  first          *      
    //  second         
    //
    @Test
    public void test4() {
        Song first = new Song();
        addPracticeToSong(YESTERDAY, first);
        Song second = new Song();
        
        assertThat(
                Stream.of(second, first).sorted(songComparator).collect(toList())
        ).isEqualTo(asList(first, second));
    }
    
    //            yesterday today
    //  first          *      *
    //  second                *
    //
    @Test
    public void test5() {
        Song first = new Song();
        addPracticeToSong(YESTERDAY, first);
        addPracticeToSong(TODAY, first);
        Song second = new Song();
        addPracticeToSong(TODAY, second);
        
        assertThat(
                Stream.of(second, first).sorted(songComparator).collect(toList())
        ).isEqualTo(asList(first, second));
    }
    
    private static void addPracticeToSong(LocalDate date, Song song) {
        PracticeDay practiceDay = new PracticeDay();
        practiceDay.setSong(song);
        practiceDay.setDay(date);
        
        song.getPracticeLog().add(practiceDay);
    }
}