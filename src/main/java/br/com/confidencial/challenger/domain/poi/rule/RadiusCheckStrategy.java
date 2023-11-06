package br.com.confidencial.challenger.domain.poi.rule;

import org.springframework.stereotype.Component;

@Component
public interface RadiusCheckStrategy {
    double calculateDistance(double lat1, double lon1, double lat2, double lon2);
}
