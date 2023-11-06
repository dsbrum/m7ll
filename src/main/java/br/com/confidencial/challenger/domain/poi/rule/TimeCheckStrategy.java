package br.com.confidencial.challenger.domain.poi.rule;

import org.springframework.stereotype.Component;

@Component
public interface TimeCheckStrategy {
    String getTime(String dtini,String dtfim);
}
