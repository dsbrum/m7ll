package br.com.confidencial.challenger.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.confidencial.challenger.controller.BasePoiController;
import br.com.confidencial.challenger.domain.poi.BasePOI;
import br.com.confidencial.challenger.domain.poi.dtos.BasePOIMap;
import br.com.confidencial.challenger.domain.poi.service.BasePoiService;
import br.com.confidencial.challenger.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

public class BasePoiControllerTest {

    @InjectMocks
    private BasePoiController basePoiController;

    @Mock
    private BasePoiService basePoiService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPoiComPoiEncontrado() {
        var longitude = "-51.566944";
        var latitude = "-25.414167";
        BasePOI basePoi = getBasePoi("PONTO 3", 10, longitude, latitude);
        when(basePoiService.getBasePoiPorLongELat(longitude, latitude)).thenReturn(Optional.of(basePoi));

        ResponseEntity<?> response = basePoiController.poi(longitude, latitude);

        assertTrue(response.getBody() instanceof BasePOI);
    }

    @Test
    public void testPoiComPoiNaoEncontrado() {
        when(basePoiService.getBasePoiPorLongELat("longitude", "latitude")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> basePoiController.poi("longitude", "latitude"));
        assertEquals("Ponto de interesse não encontrado!", exception.getMessage());
    }


    @Test
    public void testImportCSVFileComSucesso() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(basePoiService.processarArquivoCSV(file)).thenReturn(true);

        ResponseEntity<?> response = basePoiController.importCSVFile(file);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Importação bem-sucedida", response.getBody());
    }

    @Test
    public void testImportCSVFileComErro() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(basePoiService.processarArquivoCSV(file)).thenReturn(false);

        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> basePoiController.importCSVFile(file));
        assertEquals("Erro na importação!", exception.getMessage());
    }

    private static BasePOI getBasePoi(String nome, int raio, String longitude, String latitude) {
        var poi = new BasePOI();
        poi.setNome(nome);
        poi.setRaio(raio);
        poi.setLongitude(longitude);
        poi.setLatitude(latitude);
        return poi;
    }

    private Map<String, List<BasePOIMap>> mockReport() {
        HashMap<String, List<BasePOIMap>> map = new HashMap<>();
        map.put("PONTO 1", Collections.emptyList());
        map.put("PONTO 2", Collections.emptyList());
        return map;
    }
}

