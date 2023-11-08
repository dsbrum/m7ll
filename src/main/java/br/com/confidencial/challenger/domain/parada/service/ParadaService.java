package br.com.confidencial.challenger.domain.parada.service;

import br.com.confidencial.challenger.domain.localizacao.Localizacao;
import br.com.confidencial.challenger.domain.localizacao.dtos.LocalizacaoResponseDTO;
import br.com.confidencial.challenger.domain.localizacao.repository.LocalizacaoRepository;
import br.com.confidencial.challenger.domain.localizacao.service.LocalizacaoService;
import br.com.confidencial.challenger.domain.poi.BasePOI;
import br.com.confidencial.challenger.domain.poi.dtos.BasePOIMap;
import br.com.confidencial.challenger.domain.poi.service.BasePoiService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParadaService {

    @Autowired
    private LocalizacaoService locService;

    @Autowired
    private BasePoiService basePoiService;


    public Page<?> getParadasPoiCliente(LocalDateTime dataIni, LocalDateTime dataFim, String tag, Pageable paginacao) {
        var listaDePontos = basePoiService.getBasePoi();
        List<Localizacao> localizacaos;

        if (dataIni != null && dataFim != null) {
            if (tag != null) {
                localizacaos = locService.getLocalizationByDataBetweenAndPlaca(dataIni, dataFim, tag);
            } else {
                localizacaos = locService.getLocalizationByDataBetween(dataIni, dataFim);
            }
        } else if (tag != null) {
            localizacaos = locService.getLocalizationByTag(tag);
        } else {
            return Page.empty();
        }
        Map<String, List<BasePOIMap>> reportForAllPoi = basePoiService.getReportForAllPoi(listaDePontos, localizacaos);
        return new PageImpl<>(new ArrayList<>(reportForAllPoi.entrySet()));
    }
}
