package br.com.confidencial.challenger.domain.poi.service;

import br.com.confidencial.challenger.domain.localizacao.Localizacao;
import br.com.confidencial.challenger.domain.localizacao.dtos.LocalizacaoResponseDTO;
import br.com.confidencial.challenger.domain.localizacao.repository.LocalizacaoRepository;
import br.com.confidencial.challenger.domain.poi.BasePOI;
import br.com.confidencial.challenger.domain.poi.dtos.BasePOIResponseDTO;
import br.com.confidencial.challenger.domain.poi.repository.BasePoiRepository;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BasePoiService {
    @Autowired
    BasePoiRepository poiRepo;
    @Autowired
    private LocalizacaoRepository locRepo;
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
            List<Localizacao> localizacoesDentroDoRaio = localizacoes.stream()
                    .filter(localizacao -> {
                        double lon = Double.parseDouble(localizacao.getLongitude());
                        double lat = Double.parseDouble(localizacao.getLatitude());
                        double haversine = haversine(latPoi, lonPoi, lat, lon);
                        double raio = basePOI.getRaio();
                        return haversine <= raio;
                    })
                    .collect(Collectors.toList());

        }

        }

    }
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double radius = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return radius * c;
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
