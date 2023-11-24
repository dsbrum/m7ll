package br.com.confidencial.challenger.domain.poi.service;

import br.com.confidencial.challenger.domain.poi.BasePOI;
import br.com.confidencial.challenger.domain.poi.repository.BasePoiRepository;
import br.com.confidencial.challenger.exceptions.CsvProcessingException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BasePoiService {

    @Autowired
    private BasePoiRepository poiRepo;


    public Optional<BasePOI> getBasePoiPorLongELat(String longitude, String latitude) {
        return poiRepo.findByLongitudeAndLatitude(longitude, latitude);
    }

    public List<BasePOI> getBasePoi() {
        return poiRepo.findAll();
    }
    public Page<BasePOI> getBasePoiPaginated(Pageable paginacao) {
        return poiRepo.findAll(paginacao);
    }
    public Optional<BasePOI> getBasePoiByName(String name) {
        return poiRepo.findByNome(name);
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
            throw new CsvProcessingException("Erro ao processar o arquivo CSV.");
        } catch (CsvValidationException e) {
            log.error("Erro na validação do arquivo CSV: " + e.getMessage());
            throw new CsvProcessingException("Erro na validação do arquivo CSV.");        }
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
            throw new CsvProcessingException("Erro na conversão de dados da linha CSV.");
        }
        return poi;
    }

}
