package br.com.confidencial.challenger.controller;

import br.com.confidencial.challenger.domain.parada.service.ParadaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ParadaControllerTest {

    @InjectMocks
    private ParadaController paradaController;

    @Mock
    private ParadaService paradaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testParadaCliente() {
        LocalDateTime dataIni = LocalDateTime.now();
        LocalDateTime dataFim = LocalDateTime.now().plusHours(1);
        String tag = "TEST_TAG";
        Pageable paginacao = Pageable.unpaged();


        Page<?> expectedPage = mock(Page.class);
        when(paradaService.getParadasPoiCliente(dataIni, dataFim, tag, paginacao)).thenAnswer(invocation -> expectedPage);


        Page<?> result = paradaController.paradaCliente(dataIni, dataFim, tag, paginacao);

        verify(paradaService, times(1)).getParadasPoiCliente(dataIni, dataFim, tag, paginacao);

        assertEquals(expectedPage, result);
    }
}
