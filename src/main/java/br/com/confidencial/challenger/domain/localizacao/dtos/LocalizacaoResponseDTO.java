package br.com.confidencial.challenger.domain.localizacao.dtos;

import br.com.confidencial.challenger.domain.localizacao.Localizacao;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;


public record  LocalizacaoResponseDTO(String placa,
                                      Date dataPosicao,
                                      int velocidade,
                                      String longitude,
                                      String latitude,
                                      boolean ignicao) {
    public LocalizacaoResponseDTO(Localizacao localizacao){
        this(localizacao.getPlaca(),
                localizacao.getDataPosicao(),
                localizacao.getVelocidade(),localizacao.getLongitude(),
                localizacao.getLatitude(), localizacao.isIgnicao());
    }
}
