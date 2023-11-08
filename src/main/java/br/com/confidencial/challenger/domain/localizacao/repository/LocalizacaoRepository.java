package br.com.confidencial.challenger.domain.localizacao.repository;

import br.com.confidencial.challenger.domain.localizacao.Localizacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {


    Optional<Localizacao> findById(Long id);
    Page<Localizacao> findByPlaca(String tag, Pageable pageable);
    List<Localizacao> findByPlaca(String tag);
    List<Localizacao> findByData(LocalDateTime date);
    List<Localizacao> findByDataBetween(LocalDateTime dateIni,LocalDateTime dateFim);

    List<Localizacao> findByDataBetweenAndPlaca(LocalDateTime dateIni,LocalDateTime dateFim,String tag);

}
