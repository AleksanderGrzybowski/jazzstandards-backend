package pl.kelog.jazzstandards.database.dataimport;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
class ImportOnStartupRunner implements CommandLineRunner {
    
    private final String importFilename;
    
    private final ImportService importService;
    
    private final ImportFileParser importFileParser;
    
    public ImportOnStartupRunner(
            @Value("${importFilename:#{null}}") String importFilename,
            ImportService importService,
            ImportFileParser importFileParser
    ) {
        this.importService = importService;
        this.importFileParser = importFileParser;
        this.importFilename = importFilename;
    }
    
    @Override
    public void run(String... args) {
        log.info("Checking if should start import process...");
        if (importFilename == null) {
            log.info("No import filename defined, exiting.");
            return;
        }
        
        log.info("Starting import...");
        
        List<SongImportDto> songsToImport = importFileParser.parse(importFilename);
        importService.load(songsToImport);
        
        log.info("Import finished.");
    }
    
}
