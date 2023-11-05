package br.com.confidencial.challenger.domain.poi.repository;

import br.com.confidencial.challenger.domain.localizacao.Localizacao;
import br.com.confidencial.challenger.domain.poi.BasePOI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BasePoiRepository extends JpaRepository<BasePOI, String> {
    @Override
    Optional<BasePOI> findById(String s);

    Optional<BasePOI> findByLongitudeAndLatitude(String longitude,String latitude);
    Optional<BasePOI> findByNome(String name);

}
