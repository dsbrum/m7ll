package br.com.confidencial.challenger.domain.poi.rule;

import org.springframework.stereotype.Component;

@Component
public interface AreaCheckStrategy {
    boolean checkInside(double veiculoLatitude, double veiculoLongitude, double latitude, double longitude, double raio);
}
