package br.com.confidencial.challenger.domain.poi.rule;

import org.springframework.stereotype.Component;

@Component
public class RadioCalculator implements AreaCheckStrategy {
    @Override
    public boolean checkInside(double veiculoLatitude,//
                               double veiculoLongitude,//
                               double latitude,//
                               double longitude,//
                               double raio) {//
        double earthRadius = 6371000;
        double dLat = Math.toRadians(veiculoLatitude - latitude);
        double dLon = Math.toRadians(veiculoLongitude - longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(veiculoLatitude))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        return distance <= raio;
    }
}
