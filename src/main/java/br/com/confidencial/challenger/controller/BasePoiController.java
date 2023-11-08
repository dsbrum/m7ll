package br.com.confidencial.challenger.controller;

import br.com.confidencial.challenger.domain.poi.BasePOI;
import br.com.confidencial.challenger.domain.poi.service.BasePoiService;
import br.com.confidencial.challenger.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("poi")
@SecurityRequirement(name = "bearer-key")
public class BasePoiController {

    @Autowired
    private BasePoiService service;
    @GetMapping("/{longitude}/{latitude}")
    public ResponseEntity<?> poi(@PathVariable String longitude, @PathVariable String latitude) {
        var basePoiPorLongELatOptReturn = service.getBasePoiPorLongELat(longitude, latitude);
        if(basePoiPorLongELatOptReturn.isPresent()){
            return ResponseEntity.ok(basePoiPorLongELatOptReturn.get());
        }
        throw new NotFoundException("Ponto de interesse não encontrado!");
    }
    @GetMapping("/{poi}")
    public ResponseEntity<?> poi(@PathVariable String poi) {
        var poiListReturn = service.getBasePoiByName(poi);
        if(!poiListReturn.isEmpty()){
            return ResponseEntity.ok(poiListReturn);
        }
        throw new NotFoundException("Ponto de interesse não encontrado!");
    }
    @GetMapping("/")
    public Page<BasePOI> poi(@PageableDefault(sort = "nome",//
                                    direction = Sort.Direction.DESC, //
                                    page = 0,//
                                    size = 10) Pageable paginacao) {
        var poiListReturn = service.getBasePoiPaginated(paginacao);
        if(!poiListReturn.isEmpty()){
            return poiListReturn;
        }
        throw new NotFoundException("Ponto de interesse não encontrado!");
    }


    @RequestMapping(
            path = "/importcsv",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importCSVFile(
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("O arquivo está vazio.");
        }

        if(service.processarArquivoCSV(file)){
            return ResponseEntity.ok("Importação bem-sucedida");
        }
        throw new UnsupportedOperationException("Erro na importação!");
    }

}
