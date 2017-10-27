package pl.kelog.jazzstandards;

import lombok.Data;
import org.springframework.web.bind.annotation.*;
import pl.kelog.jazzstandards.domain.Song;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@ResponseBody
@RequestMapping("/api")
public class AppController {
    
    private final DataService dataService;
    
    public AppController(DataService dataService) {
        this.dataService = dataService;
    }
    
    @RequestMapping(value = "/songs", method = RequestMethod.GET)
    public List<SongDto> listSongs() {
        return dataService.findAll().stream().map(SongDto::new).collect(toList());
    }
    
    @RequestMapping(value = "/songs", method = RequestMethod.POST)
    public void createSong(@RequestBody SongDto dto) {
        dataService.create(dto.title, dto.backingTrackUrl);
    }
    
    @RequestMapping(value = "/songs/{id}/practice", method = RequestMethod.PUT)
    public void logPractice(@PathVariable Long id) {
        dataService.saveTodayPractice(id);
    }
    
    @Data
    private static class SongDto {
        public Long id;
        public String title, backingTrackUrl;
        public List<String> practiceLog;
        
        @SuppressWarnings("unused")
        SongDto() {
        }
        
        SongDto(Song song) {
            this.id = song.getId();
            this.title = song.getTitle();
            this.backingTrackUrl = song.getBackingTrackUrl();
            practiceLog = song.getSortedPracticeLog().stream()
                    .map(practice -> practice.getDay().toString())
                    .collect(toList());
        }
    }
    
}
