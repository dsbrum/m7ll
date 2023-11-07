package br.com.confidencial.challenger.domain.localizacao.service;

import br.com.confidencial.challenger.domain.localizacao.Localizacao;
import br.com.confidencial.challenger.domain.localizacao.dtos.LocalizacaoResponseDTO;
import br.com.confidencial.challenger.domain.localizacao.repository.LocalizacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalizacaoServiceTest {

    @InjectMocks
    private LocalizacaoService localizacaoService;

    @Mock
    private LocalizacaoRepository localizacaoRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testGetLocalizationResponseByTag() {
        List<Localizacao> localizacoes = Arrays.asList(new Localizacao(), new Localizacao());
        Mockito.when(localizacaoRepository.findByPlaca("tag")).thenReturn(localizacoes);

        List<LocalizacaoResponseDTO> result = localizacaoService.getLocalizationResponseByTag("tag");

        assertEquals(localizacoes.size(), result.size());
    }

    @Test
    public void testGetLocalizationByData() {
        LocalDateTime date = LocalDateTime.now();
        List<Localizacao> localizacoes = Arrays.asList(new Localizacao(), new Localizacao());
        Mockito.when(localizacaoRepository.findByData(date)).thenReturn(localizacoes);

        List<LocalizacaoResponseDTO> result = localizacaoService.getLocalizationByData(date);

        assertEquals(localizacoes.size(), result.size());
    }

    @Test
    public void testGetLocalizationByDataBetween() {
        LocalDateTime dateIni = LocalDateTime.now();
        LocalDateTime dateFim = LocalDateTime.now();
        List<Localizacao> localizacoes = Arrays.asList(new Localizacao(), new Localizacao());
        Mockito.when(localizacaoRepository.findByDataBetween(dateIni, dateFim)).thenReturn(localizacoes);

        List<Localizacao> result = localizacaoService.getLocalizationByDataBetween(dateIni, dateFim);

        assertEquals(localizacoes.size(), result.size());
    }

    @Test
    public void testGetLocalizationByDataBetweenAndPlaca() {
        LocalDateTime dateIni = LocalDateTime.now();
        LocalDateTime dateFim = LocalDateTime.now();
        String tag = "tag";
        List<Localizacao> localizacoes = Arrays.asList(new Localizacao(), new Localizacao());
        Mockito.when(localizacaoRepository.findByDataBetweenAndPlaca(dateIni, dateFim, tag)).thenReturn(localizacoes);

        List<Localizacao> result = localizacaoService.getLocalizationByDataBetweenAndPlaca(dateIni, dateFim, tag);

        assertEquals(localizacoes.size(), result.size());
    }

    @Test
    public void testGetLocalizationByTag() {
        List<Localizacao> localizacoes = Arrays.asList(new Localizacao(), new Localizacao());
        Mockito.when(localizacaoRepository.findByPlaca("tag")).thenReturn(localizacoes);

        List<Localizacao> result = localizacaoService.getLocalizationByTag("tag");

        assertEquals(localizacoes.size(), result.size());
    }
}
