package br.com.confidencial.challenger.controller;

import br.com.confidencial.challenger.domain.parada.service.ParadaService;
import br.com.confidencial.challenger.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("parking")
@SecurityRequirement(name = "bearer-key")
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

        Page<?> paradasPoiCliente = service.getParadasPoiCliente(dataIni, dataFim, tag, paginacao);
        if(!paradasPoiCliente.isEmpty()){
            return paradasPoiCliente;
        }

         throw  new NotFoundException("Dados não encontrado na base!");
    }
}
