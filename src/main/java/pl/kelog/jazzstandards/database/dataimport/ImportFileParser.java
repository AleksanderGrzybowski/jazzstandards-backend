package pl.kelog.jazzstandards.database.dataimport;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

import static java.text.MessageFormat.format;

@Service
@Slf4j
public class ImportFileParser {
    
    private static final Type LIST_TYPE = new TypeToken<List<SongImportDto>>() {
    }.getType();
    
    public List<SongImportDto> parse(String filename) {
        log.info(format("Parsing file {0}...", filename));
        
        try {
            List<SongImportDto> songImportDtos = new Gson().fromJson(new FileReader(filename), LIST_TYPE);
            log.info(format("Parsed successfully, {0} songs found.", songImportDtos.size()));
            return songImportDtos;
        } catch (FileNotFoundException | JsonParseException e) {
            log.error("Parse failure", e);
            throw new SongImportException(e);
        }
    }
}
