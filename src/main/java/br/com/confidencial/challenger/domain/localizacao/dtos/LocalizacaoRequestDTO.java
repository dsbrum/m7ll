package br.com.confidencial.challenger.domain.localizacao.dtos;

import java.time.LocalDateTime;

public record LocalizacaoRequestDTO (String placa,
                                     LocalDateTime data,
                                     int velocidade,
                                     String longitude,
                                     String latitude,
                                     boolean ignicao){
}
