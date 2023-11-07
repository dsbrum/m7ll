package br.com.confidencial.challenger.controller;

import br.com.confidencial.challenger.domain.localizacao.Localizacao;
import br.com.confidencial.challenger.domain.localizacao.dtos.LocalizacaoRequestDTO;
import br.com.confidencial.challenger.domain.localizacao.dtos.LocalizacaoResponseDTO;
import br.com.confidencial.challenger.domain.localizacao.service.LocalizacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("localizacao")
public class LocalizacaoController {

    @Autowired
    private LocalizacaoService service;
    @GetMapping("/{tag}")
    public Page<LocalizacaoResponseDTO> movimentacaoClientePorPlaca(@PathVariable String tag,
                                                            @PageableDefault(sort = "data_operacao",//
                                                                  direction = Sort.Direction.DESC, //
                                                                  page = 0,//
                                                                  size = 10) Pageable paginacao) {

        return service.getLocalizationByTagPaginated(tag,paginacao);
    }
    @PostMapping("/")
    public ResponseEntity<?> registraMovimentacaoCliente(@RequestBody @Valid LocalizacaoRequestDTO localizacaoRequestDTO) {
        Optional<Localizacao> localizacao = service.salvarLocalizacao(localizacaoRequestDTO);
        if (localizacao.isPresent()) {
            return ResponseEntity.accepted().body(localizacao.get());
        }
        return ResponseEntity.badRequest().body("Falha ao salvar a localização. Verifique os dados fornecidos.");
    }

}
