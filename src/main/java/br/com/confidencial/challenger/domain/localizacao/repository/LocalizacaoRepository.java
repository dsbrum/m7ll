package br.com.confidencial.challenger.domain.localizacao.repository;

import br.com.confidencial.challenger.domain.localizacao.Localizacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {
    Page<Localizacao> findLocalizacaoByPlaca(String tag, Pageable pageable);
}
