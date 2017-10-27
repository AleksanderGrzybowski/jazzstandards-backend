package pl.kelog.jazzstandards.database;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

/**
 * https://www.thoughts-on-java.org/persist-localdate-localdatetime-jpa/
 */
@SuppressWarnings("unused")
@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {
    
    @Override
    public Date convertToDatabaseColumn(LocalDate date) {
        return (date == null ? null : Date.valueOf(date));
    }
    
    @Override
    public LocalDate convertToEntityAttribute(Date sqlDate) {
        return (sqlDate == null ? null : sqlDate.toLocalDate());
    }
}