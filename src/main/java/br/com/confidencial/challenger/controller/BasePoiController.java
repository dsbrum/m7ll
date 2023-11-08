package br.com.confidencial.challenger.controller;

import br.com.confidencial.challenger.domain.poi.service.BasePoiService;
import br.com.confidencial.challenger.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("poi")
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
        var poiListReturn = service.getReportTimePorPOI(poi);
        if(!poiListReturn.isEmpty()){
            return ResponseEntity.ok(poiListReturn);
        }
        throw new NotFoundException("Ponto de interesse não encontrado!");
    }
    @GetMapping("/")
    public ResponseEntity<?> poi() {
        var poiListReturn = service.getReportForAllPoi();
        if(!poiListReturn.isEmpty()){
            return ResponseEntity.ok(poiListReturn);
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
