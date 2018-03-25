package pl.kelog.jazzstandards.database.dataimport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kelog.jazzstandards.database.Song;
import pl.kelog.jazzstandards.database.SongRepository;

import java.util.List;
import java.util.Objects;

import static java.text.MessageFormat.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportService {
    
    private final SongRepository songRepository;
    
    public void load(List<SongImportDto> songsToImport) {
        songsToImport.forEach(this::importSong);
    }
    
    private void importSong(SongImportDto songDto) {
        log.info(format("Importing {0}...", songDto));
        
        Song existingSong = songRepository.findByTitle(songDto.getTitle());
        if (existingSong == null) {
            log.info("No existing found, creating new.");
            songRepository.save(createSong(songDto));
        } else if (needsUrlUpdate(existingSong, songDto)) {
            log.info(format("Updating URL to {0}", songDto.getBackingTrackUrl()));
            existingSong.setBackingTrackUrl(songDto.getBackingTrackUrl());
        } else {
            log.info("No action required.");
        }
        
        log.info(format("Imported {0}.", songDto));
    }
    
    private Song createSong(SongImportDto dto) {
        Song newSong = new Song();
        newSong.setTitle(dto.getTitle());
        newSong.setBackingTrackUrl(dto.getBackingTrackUrl());
        return newSong;
    }
    
    private boolean needsUrlUpdate(Song song, SongImportDto dto) {
        return Objects.equals(song.getTitle(), dto.getTitle())
                && !Objects.equals(song.getBackingTrackUrl(), dto.getBackingTrackUrl());
    }
}
