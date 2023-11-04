package br.com.confidencial.challenger.domain.localizacao.service;


import br.com.confidencial.challenger.domain.localizacao.Localizacao;
import br.com.confidencial.challenger.domain.localizacao.dtos.LocalizacaoResponseDTO;
import br.com.confidencial.challenger.domain.localizacao.repository.LocalizacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LocalizacaoService {
    @Autowired
    private LocalizacaoRepository locRepo;
    public Page<LocalizacaoResponseDTO> getLocalizationByTagPaginated(String tag, Pageable paginacao) {
        Page<Localizacao> localizacoes = locRepo.findLocalizacaoByPlaca(tag, paginacao);
        return localizacoes.map(LocalizacaoResponseDTO::new);
    }
}
