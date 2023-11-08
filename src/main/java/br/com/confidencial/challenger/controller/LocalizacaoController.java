package br.com.confidencial.challenger.controller;

import br.com.confidencial.challenger.controller.dto.ResponseResult;
import br.com.confidencial.challenger.domain.localizacao.Localizacao;
import br.com.confidencial.challenger.domain.localizacao.dtos.LocalizacaoRequestDTO;
import br.com.confidencial.challenger.domain.localizacao.dtos.LocalizacaoResponseDTO;
import br.com.confidencial.challenger.domain.localizacao.service.LocalizacaoService;
import br.com.confidencial.challenger.exceptions.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
        Page<LocalizacaoResponseDTO> localizationByTagPaginated = service.getLocalizationByTagPaginated(tag, paginacao);
            if(!localizationByTagPaginated.isEmpty()){
                return localizationByTagPaginated;
            }
        throw new NotFoundException("Localizações não encontradas para o cliente!");

    }
    @PostMapping("/")
    public ResponseEntity<?> registraMovimentacaoCliente(@RequestBody @Valid LocalizacaoRequestDTO localizacaoRequestDTO) {
        Optional<Localizacao> localizacao = service.salvarLocalizacao(localizacaoRequestDTO);

        if (localizacao.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseResult<>(localizacao.get()));
        }

        throw new UnsupportedOperationException("Falha ao cadastrar a localização. Verifique as informações fornecidas.");

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeMovimentacaoCliente(@PathVariable int id) {
        if (service.removerLocalizacao(id)) {
            return ResponseEntity.accepted().build();
        }
        throw new NotFoundException("Falha ao remover a localização. Verifique o ID fornecido.");
    }

}
