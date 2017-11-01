package pl.kelog.jazzstandards;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import pl.kelog.jazzstandards.database.PracticeDay;
import pl.kelog.jazzstandards.database.PracticeDayRepository;
import pl.kelog.jazzstandards.database.Song;
import pl.kelog.jazzstandards.database.SongRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AppServiceTest {
    
    final AppService appService;
    
    final DateService dateService;
    final SongRepository songRepositoryMock;
    final PracticeDayRepository practiceDayRepositoryMock;
    
    final LocalDate NOW = LocalDate.of(2017, Month.OCTOBER, 31);
    
    public AppServiceTest() {
        dateService = mock(DateService.class);
        when(dateService.now()).thenReturn(NOW);
        
        songRepositoryMock = mock(SongRepository.class);
        practiceDayRepositoryMock = mock(PracticeDayRepository.class);
        
        this.appService = new AppService(dateService, songRepositoryMock, practiceDayRepositoryMock);
    }
    
    @Test
    public void should_list_all_songs_from_the_database() {
        Song sampleSong = new Song();
        sampleSong.setTitle("Autumn leaves");
        sampleSong.setBackingTrackUrl("http://123.pl");
        List<Song> songs = singletonList(sampleSong);
        
        when(songRepositoryMock.findAll()).thenReturn(songs);
        
        List<Song> listedSongs = appService.findAll();
        
        assertThat(listedSongs).isEqualTo(songs);
        verify(songRepositoryMock, atLeastOnce()).findAll();
    }
    
    @Test(expected = AppService.ValidationException.class)
    public void should_not_allow_to_create_song_with_no_title() {
        appService.create(null, "http://123.pl");
    }
    
    @Test(expected = AppService.ValidationException.class)
    public void should_not_allow_to_create_song_with_empty_title() {
        appService.create("", "http://123.pl");
    }
    
    @Test(expected = AppService.ValidationException.class)
    public void should_not_allow_to_create_song_with_no_url() {
        appService.create("Autumn leaves", null);
    }
    
    @Test(expected = AppService.ValidationException.class)
    public void should_not_allow_to_create_song_with_empty_url() {
        appService.create("Autumn leaves", "");
    }
    
    @Test
    public void should_create_a_valid_song() {
        appService.create("Autumn leaves", "http://123.pl");
        
        ArgumentCaptor<Song> captor = ArgumentCaptor.forClass(Song.class);
        verify(songRepositoryMock, times(1)).save(captor.capture());
        Song created = captor.getValue();
        
        assertThat(created.getTitle()).isEqualTo("Autumn leaves");
        assertThat(created.getBackingTrackUrl()).isEqualTo("http://123.pl");
        assertThat(created.getPracticeLog()).isEmpty();
    }
    
    @Test(expected = AppService.SongNotFoundException.class)
    public void should_not_toggle_today_practice_if_id_of_nonexistent_song_is_given() {
        appService.toggleTodayPractice(123);
    }
    
    @Test
    public void should_delete_today_practice_if_there_is_already_a_practice_from_today() {
        Song song = new Song();
        song.setId(1L);
        
        PracticeDay todaysPractice = new PracticeDay();
        todaysPractice.setDay(NOW);
        todaysPractice.setSong(song);
        
        song.getPracticeLog().add(todaysPractice);
        
        when(songRepositoryMock.getOne(1L)).thenReturn(song);
        
        appService.toggleTodayPractice(1);
        
        verify(practiceDayRepositoryMock, times(1)).delete(todaysPractice);
    }
    
    @Test
    public void should_save_today_practice_if_there_is_not_already_there_a_practice_from_today() {
        Song song = new Song();
        song.setId(1L);
        
        when(songRepositoryMock.getOne(1L)).thenReturn(song);
    
        appService.toggleTodayPractice(1);
        
        ArgumentCaptor<PracticeDay> captor = ArgumentCaptor.forClass(PracticeDay.class);
        verify(practiceDayRepositoryMock, times(1)).save(captor.capture());
        
        PracticeDay createdPracticeDay = captor.getValue();
        assertThat(createdPracticeDay.getSong()).isEqualTo(song);
        assertThat(createdPracticeDay.getDay()).isEqualTo(NOW);
    }
    
    @Test
    public void should_delete_all_songs() {
        List<Song> songs = asList(new Song(), new Song());
        when(songRepositoryMock.findAll()).thenReturn(songs);
        
        appService.deleteAllData();
    
        verify(songRepositoryMock, times(1)).delete(songs);
    }
}