package br.com.confidencial.challenger.domain.poi.service;

import br.com.confidencial.challenger.domain.localizacao.Localizacao;
import br.com.confidencial.challenger.domain.localizacao.dtos.LocalizacaoResponseDTO;
import br.com.confidencial.challenger.domain.localizacao.repository.LocalizacaoRepository;
import br.com.confidencial.challenger.domain.poi.BasePOI;
import br.com.confidencial.challenger.domain.poi.dtos.BasePOIMap;
import br.com.confidencial.challenger.domain.poi.repository.BasePoiRepository;
import br.com.confidencial.challenger.domain.poi.rule.RadiusCheckStrategy;
import br.com.confidencial.challenger.domain.poi.rule.TimeCheckStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BasePoiService {

    @Autowired
    private final RadiusCheckStrategy radiusCheckStrg;
    @Autowired
    private final TimeCheckStrategy timeAccumStrg;
    @Autowired
    private BasePoiRepository poiRepo;
    @Autowired
    private LocalizacaoRepository locRepo;

    public BasePoiService(RadiusCheckStrategy radiusCheck, TimeCheckStrategy timeAccumStrg) {
        this.radiusCheckStrg = radiusCheck;
        this.timeAccumStrg = timeAccumStrg;
    }


    public Optional<BasePOI> getBasePoiPorLongELat(String longitude, String latitude) {
        return poiRepo.findByLongitudeAndLatitude(longitude, latitude);
    }

    public List<BasePOI> getBasePoi() {
        return poiRepo.findAll();
    }
    public Page<BasePOI> getBasePoiPaginated(Pageable paginacao) {
        return poiRepo.findAll(paginacao);
    }

    public List<BasePOIMap> getReportTimePorPOI(String poi) {
        return getReportForAllPoi(poiRepo.findAll(),locRepo.findAll()).get(poi);
    }
    public List<BasePOIMap> getReportTimePorPOI(String poi,List<Localizacao> localizacoes) {
        return getReportForAllPoi(poiRepo.findAll(),localizacoes).get(poi);
    }
    public Map<String, List<BasePOIMap>> getReportForAllPoi() {
        return getReportForAllPoi(poiRepo.findAll(),locRepo.findAll());
    }

    public Map<String, List<BasePOIMap>> getReportForAllPoi(List<BasePOI> poiList,List<Localizacao> localizacoes) {
        return poiList.parallelStream()
                .collect(Collectors.toMap(
                        BasePOI::getNome,
                        basePOI -> getBasePOIMaps(basePOI,localizacoes)
                ))
                .entrySet()
                .stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    private List<BasePOIMap> getBasePOIMaps(BasePOI basePOI, List<Localizacao> localizacoesParameter) {
        double lonPoi = Double.parseDouble(basePOI.getLongitude());
        double latPoi = Double.parseDouble(basePOI.getLatitude());
        double raio = basePOI.getRaio();

        List<Localizacao> filteredLocalizacoes = localizacoesParameter.parallelStream()
                .filter(localizacao -> {
                    double lonLoc = Double.parseDouble(localizacao.getLongitude());
                    double latLoc = Double.parseDouble(localizacao.getLatitude());
                    return isWithinRadius(lonLoc, latLoc, lonPoi, latPoi, raio);
                })
                .toList();

        Optional<Localizacao> firstPoint = filteredLocalizacoes.stream().findFirst();
        Optional<Localizacao> lastPoint = filteredLocalizacoes.stream().reduce((first, second) -> second);

        List<BasePOIMap> result = filteredLocalizacoes.stream()
                .map(li -> createBasePOIMap(basePOI, li, firstPoint, lastPoint))
                .distinct()
                .collect(Collectors.toList());

        return result;
    }

    private boolean isWithinRadius( double lon,double lat, double lonPoi, double latPoi, double raio) {
        double haversine = radiusCheckStrg.calculateDistance(latPoi, lonPoi, lat, lon);
        return haversine <= raio;
    }

    private BasePOIMap createBasePOIMap(BasePOI basePOI, Localizacao localizacao, Optional<Localizacao> firstPoint, Optional<Localizacao> lastPoint) {
        String time = timeAccumStrg.getTime(firstPoint.get().getData().toString(), lastPoint.get().getData().toString());
        return new BasePOIMap(basePOI.getNome(), basePOI.getRaio(), localizacao.getPlaca(), time);
    }

}
