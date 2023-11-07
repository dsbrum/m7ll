package br.com.confidencial.challenger.domain.localizacao.dtos;

import br.com.confidencial.challenger.domain.localizacao.Localizacao;

import java.time.LocalDateTime;
import java.util.Date;


public record  LocalizacaoResponseDTO(String placa,
                                      LocalDateTime data,
                                      int velocidade,
                                      String longitude,
                                      String latitude,
                                      boolean ignicao) {
    public LocalizacaoResponseDTO(Localizacao localizacao){
        this(localizacao.getPlaca(),
                localizacao.getData(),
                localizacao.getVelocidade(),localizacao.getLongitude(),
                localizacao.getLatitude(), localizacao.isIgnicao());
    }
}
