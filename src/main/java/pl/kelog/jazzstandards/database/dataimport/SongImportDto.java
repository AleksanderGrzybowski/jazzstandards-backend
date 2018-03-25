package pl.kelog.jazzstandards.database.dataimport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class SongImportDto {
    public String title;
    public String backingTrackUrl;
}
