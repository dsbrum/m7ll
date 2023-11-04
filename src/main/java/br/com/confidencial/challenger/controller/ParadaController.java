package br.com.confidencial.challenger.controller;

import br.com.confidencial.challenger.domain.localizacao.dtos.LocalizacaoResponseDTO;
import br.com.confidencial.challenger.domain.localizacao.service.LocalizacaoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("parking")
@SecurityRequirement(name = "bearer-key")
public class ParadaController {

    @Autowired
    private LocalizacaoService service;
    @GetMapping("/{tag}")
    public Page<LocalizacaoResponseDTO> paradaCliente(@PathVariable String tag,
                                                            @PageableDefault(sort = "data_operacao",//
                                                                  direction = Sort.Direction.DESC, //
                                                                  page = 0,//
                                                                  size = 10) Pageable paginacao) {

        return service.getLocalizationByTagPaginated(tag,paginacao);
    }
}
