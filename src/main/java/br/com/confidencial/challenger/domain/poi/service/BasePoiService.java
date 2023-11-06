package br.com.confidencial.challenger.domain.poi.service;

import br.com.confidencial.challenger.domain.localizacao.Localizacao;
import br.com.confidencial.challenger.domain.localizacao.repository.LocalizacaoRepository;
import br.com.confidencial.challenger.domain.poi.BasePOI;
import br.com.confidencial.challenger.domain.poi.repository.BasePoiRepository;
import br.com.confidencial.challenger.domain.poi.rule.RadiusCheckStrategy;
import br.com.confidencial.challenger.domain.poi.rule.TimeCheckStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class BasePoiService {

    @Autowired
    private final RadiusCheckStrategy radiusCheckStrg;
    @Autowired
    private final TimeCheckStrategy timeAccumStrg;
    @Autowired
    BasePoiRepository poiRepo;
    @Autowired
    private LocalizacaoRepository locRepo;

    public BasePoiService(RadiusCheckStrategy radiusCheck, TimeCheckStrategy timeAccumStrg) {
        this.radiusCheckStrg = radiusCheck;
        this.timeAccumStrg = timeAccumStrg;
    }


    public Optional<BasePOI> getBasePoiPorLongELat(String longitude,String latitude){
        return poiRepo.findByLongitudeAndLatitude(longitude,latitude);
    }

    public void getReportTimePorPOI(String poi){
        var poiOpt = poiRepo.findByNome(poi);
        if(poiOpt.isPresent()){
            var basePOI = poiOpt.get();
            var lonPoi = Double.parseDouble(basePOI.getLongitude());
            var latPoi = Double.parseDouble(basePOI.getLatitude());
            List<Localizacao> localizacoes = locRepo.findAll();
            List<Localizacao> locationIn = localizacoes.stream()
                    .filter(localizacao -> {
                        double lon = Double.parseDouble(localizacao.getLongitude());
                        double lat = Double.parseDouble(localizacao.getLatitude());
                        double haversine = radiusCheckStrg.calculateDistance(latPoi, lonPoi, lat, lon);
                        double raio = basePOI.getRaio();
                        return haversine <= raio;

                    })
                    .toList();
            Optional<Localizacao> firstPoint = locationIn.stream().findFirst();
            Optional<Localizacao> lastPoint = locationIn.stream()
                    .reduce((first, second) -> second);
            firstPoint.ifPresent(localizacao -> System.out.println(timeAccumStrg.getTime(localizacao.getDataPosicao().toString(), lastPoint.get().getDataPosicao().toString())));
        }



    }

    private String getHoras(String dtini,String dtfim){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime dateTimeInicio = LocalDateTime.parse(dtini, formatter);
        LocalDateTime dateTimeFim = LocalDateTime.parse(dtfim, formatter);
        Duration duracao = Duration.between(dateTimeInicio, dateTimeFim);
        long horas = duracao.toHours();
        long minutos = duracao.toMinutes() % 60;
        long segundos = duracao.getSeconds() % 60;
        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
    }
}
