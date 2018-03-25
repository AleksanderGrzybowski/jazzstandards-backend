package pl.kelog.jazzstandards.database.dataimport;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import pl.kelog.jazzstandards.database.Song;
import pl.kelog.jazzstandards.database.SongRepository;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class ImportServiceTest {
    
    private SongRepository songRepositoryMock;
    
    private ImportService importService;
    
    @Before
    public void setup() {
        songRepositoryMock = mock(SongRepository.class);
        importService = new ImportService(songRepositoryMock);
    }
    
    @Test
    public void should_add_new_song_that_is_not_present_in_database() {
        when(songRepositoryMock.findByTitle("Misty")).thenReturn(null);
        
        importService.load(singletonList(
                new SongImportDto("Misty", "http://url.com")
        ));
    
        ArgumentCaptor<Song> captor = ArgumentCaptor.forClass(Song.class);
        verify(songRepositoryMock, times(1)).save(captor.capture());
    
        Song saved = captor.getValue();
        assertThat(saved.getTitle()).isEqualTo("Misty");
        assertThat(saved.getBackingTrackUrl()).isEqualTo("http://url.com");
    }
    
    @Test
    public void should_not_alter_a_song_if_it_is_present_in_database_with_the_same_track_url() {
        Song existingSong = createSong();
        when(songRepositoryMock.findByTitle("Misty")).thenReturn(existingSong);
        
        importService.load(singletonList(
                new SongImportDto("Misty", "http://url.com")
        ));
        
        verify(songRepositoryMock, never()).save(any(Song.class));
    }
    
    @Test
    public void should_change_songs_track_url_if_it_differs_from_the_one_in_database() {
        Song existingSong = createSong();
        when(songRepositoryMock.findByTitle("Misty")).thenReturn(existingSong);
        
        importService.load(singletonList(
                new SongImportDto("Misty", "http://newurl.com")
        ));
        
        assertThat(existingSong.getBackingTrackUrl()).isEqualTo("http://newurl.com");
    }
    
    private Song createSong() {
        Song existingSong = new Song();
        existingSong.setTitle("Misty");
        existingSong.setBackingTrackUrl("http://url.com");
        return existingSong;
    }
}