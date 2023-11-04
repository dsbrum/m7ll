package br.com.confidencial.challenger.controller;

import br.com.confidencial.challenger.domain.localizacao.dtos.LocalizacaoResponseDTO;
import br.com.confidencial.challenger.domain.localizacao.service.LocalizacaoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("localizacao")
@SecurityRequirement(name = "bearer-key")
public class LocalizacaoController {

    @Autowired
    private LocalizacaoService service;
    @GetMapping("/{tag}")
    public Page<LocalizacaoResponseDTO> movimentacaoCliente(@PathVariable String tag,
                                                            @PageableDefault(sort = "data_operacao",//
                                                                  direction = Sort.Direction.DESC, //
                                                                  page = 0,//
                                                                  size = 10) Pageable paginacao) {

        return service.getLocalizationByTagPaginated(tag,paginacao);
    }
}
