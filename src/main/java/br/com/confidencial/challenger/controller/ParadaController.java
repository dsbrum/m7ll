package br.com.confidencial.challenger.controller;

import br.com.confidencial.challenger.domain.localizacao.dtos.LocalizacaoResponseDTO;
import br.com.confidencial.challenger.domain.localizacao.service.LocalizacaoService;
import br.com.confidencial.challenger.domain.parada.service.ParadaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("parking")
public class ParadaController {

    @Autowired
    private ParadaService service;
    @GetMapping("/")
    public Page<?> paradaCliente(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataIni,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
                                                      @RequestParam(required = false) String tag,
                                                            @PageableDefault(sort = "data_operacao",//
                                                                  direction = Sort.Direction.DESC, //
                                                                  page = 0,//
                                                                  size = 10) Pageable paginacao) {

        return service.getParadasPoiCliente(dataIni,dataFim,tag,paginacao);
    }
}
