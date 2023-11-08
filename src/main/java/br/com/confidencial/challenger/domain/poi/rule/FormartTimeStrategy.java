package br.com.confidencial.challenger.domain.poi.rule;

import org.springframework.stereotype.Component;

@Component
public interface FormartTimeStrategy {
    String formatter(long milliseconds);
}
