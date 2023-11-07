package br.com.confidencial.challenger.controller;

import br.com.confidencial.challenger.controller.BasePoiController;
import br.com.confidencial.challenger.domain.poi.BasePOI;
import br.com.confidencial.challenger.domain.poi.dtos.BasePOIMap;
import br.com.confidencial.challenger.domain.poi.service.BasePoiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
    public void testGetPoiByLongAndLat() {
        String longitude = "10.12345";
        String latitude = "20.67890";
        Optional<BasePOI> expectedPoi = Optional.of(new BasePOI());

        when(basePoiService.getBasePoiPorLongELat(longitude, latitude)).thenReturn(expectedPoi);

        ResponseEntity<?> result = basePoiController.poi(longitude, latitude);

        verify(basePoiService, times(1)).getBasePoiPorLongELat(longitude, latitude);

        assertEquals(ResponseEntity.ok(expectedPoi), result);
    }


    @Test
    public void testGetPoiByPoi() {
        String poi = "PONTO 1";
        List<BasePOIMap> expectedValue = new ArrayList<>();

        when(basePoiService.getReportTimePorPOI(poi)).thenReturn(expectedValue);

        ResponseEntity result = basePoiController.poi(poi);

        verify(basePoiService, times(1)).getReportTimePorPOI(poi);


        assertEquals(ResponseEntity.ok(expectedValue), result);
    }


    @Test
    public void testGetAllPoiReports() {
        Map<String, List<BasePOIMap>> expectedReport = createSampleReport(); // Crie um relatório de exemplo

        when(basePoiService.getReportForAllPoi()).thenReturn(expectedReport);

        ResponseEntity result = basePoiController.poi();

        verify(basePoiService, times(1)).getReportForAllPoi();

        assertEquals(ResponseEntity.ok(expectedReport), result);
    }

    private Map<String, List<BasePOIMap>> createSampleReport() {
        return null;
    }
}
