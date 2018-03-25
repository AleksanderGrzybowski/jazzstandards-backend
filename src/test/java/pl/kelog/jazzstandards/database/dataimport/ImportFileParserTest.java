package pl.kelog.jazzstandards.database.dataimport;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ImportFileParserTest {
    
    private ImportFileParser parser;
    
    @Before
    public void setup() {
        parser = new ImportFileParser();
    }
    
    @Test
    public void should_read_json_file() throws Exception {
        String path = createImportFile("[ \n" +
                "  {\n" +
                "    \"title\" : \"Autumn leaves\",\n" +
                "    \"backingTrackUrl\": \"http://some.url.com/\"\n" +
                "  }\n" +
                "]");
        
        List<SongImportDto> songsList = parser.parse(path);
        
        assertThat(songsList).hasSize(1);
        SongImportDto songDto = songsList.get(0);
       
        assertThat(songDto.getTitle()).isEqualTo("Autumn leaves");
        assertThat(songDto.getBackingTrackUrl()).isEqualTo("http://some.url.com/");
    }
    
    @Test
    public void should_fail_to_read_invalid_file() throws Exception {
        String path = createImportFile("not a json");
        
        assertThatThrownBy(() -> parser.parse(path)).isInstanceOf(SongImportException.class);
    }
    
    private String createImportFile(String content) throws Exception {
        File file = File.createTempFile("jazzstandards-test", ".json");
        FileUtils.write(file, content, StandardCharsets.UTF_8);
        return file.getAbsolutePath();
    }
}