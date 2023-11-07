package br.com.confidencial.challenger.domain.poi.rule;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static br.com.confidencial.challenger.domain.poi.consts.POIConsts.*;

@Component
public class TimeAccum implements TimeCheckStrategy{

    @Override
    public String getTime(String dtini, String dtfim) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATERN_TIME_2);
        LocalDateTime dateTimeInicio = LocalDateTime.parse(dtini, formatter);
        LocalDateTime dateTimeFim = LocalDateTime.parse(dtfim, formatter);
        Duration duracao = Duration.between(dateTimeInicio, dateTimeFim);
        long horas = duracao.toHours();
        long minutos = duracao.toMinutes() % 60;
        long segundos = duracao.getSeconds() % 60;
        return String.format(PARTERN_TIME_SF1, horas, minutos, segundos);
    }
}
