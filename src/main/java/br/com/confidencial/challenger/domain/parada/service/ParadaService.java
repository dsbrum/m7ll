package br.com.confidencial.challenger.domain.parada.service;

import br.com.confidencial.challenger.domain.localizacao.Localizacao;
import br.com.confidencial.challenger.domain.localizacao.service.LocalizacaoService;
import br.com.confidencial.challenger.domain.poi.BasePOI;
import br.com.confidencial.challenger.domain.poi.rule.FormartTimeStrategy;
import br.com.confidencial.challenger.domain.poi.rule.AreaCheckStrategy;
import br.com.confidencial.challenger.domain.poi.service.BasePoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParadaService {

    @Autowired
    private final AreaCheckStrategy areaStrgy;

    @Autowired
    private final FormartTimeStrategy formartTimeStrgy;


    @Autowired
    private LocalizacaoService locService;

    @Autowired
    private BasePoiService basePoiService;

    public ParadaService(AreaCheckStrategy areaStrgy, FormartTimeStrategy formartTimeStrgy) {
        this.areaStrgy = areaStrgy;
        this.formartTimeStrgy = formartTimeStrgy;
    }


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
        Map<String, Map<String, String>> reportForAllPoi = getAll(listaDePontos, localizacaos);
        return new PageImpl<>(new ArrayList<>(reportForAllPoi.entrySet()));
    }
    @Cacheable("paradasPoiCliente")
    public Map<String, Map<String, String>> getAll(List<BasePOI> listaDePontos, List<Localizacao> posicoes) {
        Map<String, Map<String, Long>> tempoPorPOIPlaca = new HashMap<>();

        for (int i = 0; i < posicoes.size() - 1; i++) {
            var posicaoAtual = posicoes.get(i);
            var posicaoSeguinte = posicoes.get(i + 1);

            for (BasePOI poi : listaDePontos) {
                if (areaStrgy.checkInside(
                        Double.parseDouble(posicaoAtual.getLatitude()),
                        Double.parseDouble(posicaoAtual.getLongitude()),
                        Double.parseDouble(poi.getLatitude()),
                        Double.parseDouble(poi.getLongitude()), poi.getRaio())) {

                    processTempoPorPlaca(tempoPorPOIPlaca, posicaoAtual, posicaoSeguinte, poi);
                }
            }
        }

        return formatResponseTimes(tempoPorPOIPlaca);
    }

    private void processTempoPorPlaca(
            Map<String, Map<String, Long>> tempoPorPOIPlaca,
            Localizacao posicaoAtual,
            Localizacao posicaoSeguinte,
            BasePOI poi) {
        if (posicaoAtual.getPlaca().equalsIgnoreCase(posicaoSeguinte.getPlaca())) {
            Map<String, Long> tempoPorPOI = tempoPorPOIPlaca.computeIfAbsent(
                    posicaoAtual.getPlaca(),
                    k -> new HashMap<>()
            );

            long tempoDentroDoPOI = getTimeMiliSeconds(posicaoSeguinte.getData()) - getTimeMiliSeconds(posicaoAtual.getData());
            tempoPorPOI.put(poi.getNome(), tempoPorPOI.getOrDefault(poi.getNome(), 0L) + tempoDentroDoPOI);
        }
    }

    public Map<String, Map<String, String>> formatResponseTimes(Map<String, Map<String, Long>> responseTimes) {
        Map<String, Map<String, String>> formattedResponseTimes = new HashMap<>();

        for (Map.Entry<String, Map<String, Long>> entry : responseTimes.entrySet()) {
            String outerKey = entry.getKey();
            Map<String, Long> innerMap = entry.getValue();
            Map<String, String> formattedInnerMap = new HashMap<>();

            for (Map.Entry<String, Long> innerEntry : innerMap.entrySet()) {
                String innerKey = innerEntry.getKey();
                long milliseconds = innerEntry.getValue();
                String formattedTime = formartTimeStrgy.formatter(milliseconds);
                formattedInnerMap.put(innerKey, formattedTime);
            }
            formattedResponseTimes.put(outerKey, formattedInnerMap);
        }

        return formattedResponseTimes;
    }

    private static long getTimeMiliSeconds(LocalDateTime time) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(time, ZoneId.systemDefault());
        return zonedDateTime.toInstant().toEpochMilli();
    }
}
