package pl.kelog.jazzstandards;

import lombok.Data;
import org.springframework.web.bind.annotation.*;
import pl.kelog.jazzstandards.database.Song;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@ResponseBody
@RequestMapping("/api")
public class AppController {
    
    private final AppService appService;
    
    public AppController(AppService appService) {
        this.appService = appService;
    }
    
    @RequestMapping(value = "/songs", method = RequestMethod.GET)
    public List<SongDto> listSongs() {
        return appService.findAll().stream().map(SongDto::new).collect(toList());
    }
    
    @RequestMapping(value = "/songs", method = RequestMethod.POST)
    public void createSong(@RequestBody SongDto dto) {
        appService.create(dto.title, dto.backingTrackUrl);
    }
    
    @RequestMapping(value = "/songs/{id}/practice", method = RequestMethod.PUT)
    public void logPractice(@PathVariable Long id) {
        appService.saveTodayPractice(id);
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
            practiceLog = song.getPracticeLog().stream()
                    .map(practice -> practice.getDay().toString())
                    .collect(toList());
        }
    }
    
}
