package br.com.confidencial.challenger.domain.localizacao.service;

import br.com.confidencial.challenger.domain.localizacao.Localizacao;
import br.com.confidencial.challenger.domain.localizacao.dtos.LocalizacaoResponseDTO;
import br.com.confidencial.challenger.domain.localizacao.repository.LocalizacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocalizacaoService {
    @Autowired
    private LocalizacaoRepository locRepo;
    public Page<LocalizacaoResponseDTO> getLocalizationByTagPaginated(String tag, Pageable paginacao) {
        Page<Localizacao> localizacoes = locRepo.findByPlaca(tag, paginacao);
        return localizacoes.map(LocalizacaoResponseDTO::new);
    }
    public List<LocalizacaoResponseDTO> getLocalizationResponseByTag(String tag) {
        return locRepo.findByPlaca(tag).stream().map(LocalizacaoResponseDTO::new).collect(Collectors.toList());
    }
    public List<LocalizacaoResponseDTO> getLocalizationByData(LocalDateTime date) {
        return locRepo.findByData(date).stream().map(LocalizacaoResponseDTO::new).collect(Collectors.toList());
    }
    public List<Localizacao> getLocalizationByDataBetween(LocalDateTime dateIni,LocalDateTime dateFim) {
        return locRepo.findByDataBetween(dateIni,dateFim);
    }
    public List<Localizacao> getLocalizationByDataBetweenAndPlaca(LocalDateTime dateIni,LocalDateTime dateFim,String tag) {
        return locRepo.findByDataBetweenAndPlaca(dateIni,dateFim,tag);
    }
    public List<Localizacao> getLocalizationByTag(String tag) {
        return locRepo.findByPlaca(tag);
    }
}
