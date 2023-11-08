package br.com.confidencial.challenger.domain.poi.rule;

import org.springframework.stereotype.Component;

@Component
public class HHMMSSFormatter implements FormartTimeStrategy{

    @Override
    public String formatter(long milliseconds) {
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / 60000) % 60;
        long hours = milliseconds / 3600000;

        StringBuilder formattedTime = new StringBuilder(8);  // HH:MM:SS
        formattedTime.append(String.format("%03d", hours)).append(':');
        formattedTime.append(String.format("%02d", minutes)).append(':');
        formattedTime.append(String.format("%02d", seconds));

        return formattedTime.toString();
    }
}
