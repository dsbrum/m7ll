package br.com.confidencial.challenger.controller;

import br.com.confidencial.challenger.domain.localizacao.dtos.LocalizacaoResponseDTO;
import br.com.confidencial.challenger.domain.localizacao.service.LocalizacaoService;
import br.com.confidencial.challenger.domain.poi.BasePOI;
import br.com.confidencial.challenger.domain.poi.service.BasePoiService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("poi")
@SecurityRequirement(name = "bearer-key")
public class BasePoiController {

    @Autowired
    private BasePoiService service;
    @GetMapping("/{longitude}/{latitude}")
    public Optional<BasePOI> poi(@PathVariable String longitude, @PathVariable String latitude) {

        return service.getBasePoiPorLongELat(longitude,latitude);
    }
    @GetMapping("/{poi}")
    public void poi(@PathVariable String poi) {

         service.getReportTimePorPOI(poi);

    }

}
