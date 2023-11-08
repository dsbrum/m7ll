package br.com.confidencial.challenger.domain.poi.rule;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static br.com.confidencial.challenger.domain.poi.consts.POIConsts.PARTERN_TIME_SF1;
import static br.com.confidencial.challenger.domain.poi.consts.POIConsts.PATERN_TIME_2;

@Component
public class TimeAccum implements TimeCheckStrategy{

    @Override
    public String getTime(String dtini, String dtfim) {
        if (isNullOrEmpty(dtini) || isNullOrEmpty(dtfim)) {
            return "Valores de data vazios.";
        }
        LocalDateTime startTime = parseDateTime(dtini);
        LocalDateTime endTime = parseDateTime(dtfim);
        Duration duration = calculateDurationBetween(startTime, endTime);
        return formatDuration(duration);
    }
    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    private LocalDateTime parseDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATERN_TIME_2);
        return LocalDateTime.parse(dateTime, formatter);
    }

    private Duration calculateDurationBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime);
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        return String.format(PARTERN_TIME_SF1, hours, minutes, seconds);
    }
}
