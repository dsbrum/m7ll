package br.com.confidencial.challenger.domain.poi.service;

import br.com.confidencial.challenger.domain.localizacao.Localizacao;
import br.com.confidencial.challenger.domain.localizacao.repository.LocalizacaoRepository;
import br.com.confidencial.challenger.domain.poi.BasePOI;
import br.com.confidencial.challenger.domain.poi.dtos.BasePOIMap;
import br.com.confidencial.challenger.domain.poi.repository.BasePoiRepository;
import br.com.confidencial.challenger.domain.poi.rule.RadiusCheckStrategy;
import br.com.confidencial.challenger.domain.poi.rule.TimeCheckStrategy;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
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

    public boolean processarArquivoCSV(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] linha;
            boolean isHeaderRow = true;

            while ((linha = csvReader.readNext()) != null) {
                if (isHeaderRow) {
                    isHeaderRow = false;
                    continue;
                }
                BasePOI poi = parseLinhaParaPOI(linha);
                if (poi != null) {
                    poiRepo.save(poi);
                }
            }
            return true;
        } catch (IOException e) {
            log.error("Erro ao processar o arquivo CSV: " + e.getMessage());
            return false;
        } catch (CsvValidationException e) {
            throw new RuntimeException("Erro na validação do arquivo CSV: " + e.getMessage(), e);
        }
    }

    @Cacheable("paradasPoiCliente")
    public Map<String, List<BasePOIMap>> getReportForAllPoi(List<BasePOI> poiList,List<Localizacao> localizacoes) {
        if(localizacoes.isEmpty()){
            return new HashMap<>();
        }
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

        Optional<Localizacao> firstPointOpt = filteredLocalizacoes.stream().findFirst();
        Optional<Localizacao> lastPointOpt = filteredLocalizacoes.stream().reduce((first, second) -> second);

        String firstPointDateSTR = firstPointOpt.map(localizacao -> localizacao.getData().toString()).orElse("");
        String LastPointDateSTR = lastPointOpt.map(localizacao -> localizacao.getData().toString()).orElse("");

        return filteredLocalizacoes.stream()
                .map(li -> createBasePOIMap(basePOI, li, firstPointDateSTR, LastPointDateSTR))
                .distinct()
                .collect(Collectors.toList());
    }


    private boolean isWithinRadius( double lon,double lat, double lonPoi, double latPoi, double raio) {
        double haversine = radiusCheckStrg.calculateDistance(latPoi, lonPoi, lat, lon);
        return haversine <= raio;
    }

    private BasePOIMap createBasePOIMap(BasePOI basePOI, Localizacao localizacao, String firstPointDate, String lastPointDate) {
        String time = timeAccumStrg.getTime(firstPointDate, lastPointDate);
        return new BasePOIMap(basePOI.getNome(), basePOI.getRaio(), localizacao.getPlaca(), time);
    }

    private BasePOI parseLinhaParaPOI(String[] linha) {
        if (linha.length < 4) {
            log.warn("Linha CSV incompleta: " + Arrays.toString(linha));
            return null;
        }
        BasePOI poi = new BasePOI();
        poi.setNome(linha[0]);
        try {
            poi.setRaio(Integer.parseInt(linha[1]));
            poi.setLatitude(linha[2]);
            poi.setLongitude(linha[3]);
        } catch (NumberFormatException e) {
            log.error("Erro na conversão de dados da linha CSV: " + e.getMessage());
            return null;
        }
        return poi;
    }

}
