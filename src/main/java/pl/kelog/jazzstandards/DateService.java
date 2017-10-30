package pl.kelog.jazzstandards;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DateService {
    
    public LocalDate now() {
        return LocalDate.now();
    }
}
