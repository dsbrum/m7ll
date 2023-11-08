package br.com.confidencial.challenger.controller;

import br.com.confidencial.challenger.domain.poi.service.BasePoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("poi")
public class BasePoiController {

    @Autowired
    private BasePoiService service;
    @GetMapping("/{longitude}/{latitude}")
    public ResponseEntity poi(@PathVariable String longitude, @PathVariable String latitude) {
        return ResponseEntity.ok(service.getBasePoiPorLongELat(longitude,latitude));
    }
    @GetMapping("/{poi}")
    public ResponseEntity poi(@PathVariable String poi) {
        return ResponseEntity.ok(service.getReportTimePorPOI(poi));
    }
    @GetMapping("/")
    public ResponseEntity poi() {
        return ResponseEntity.ok(service.getReportForAllPoi());
    }

    @RequestMapping(
            path = "/importcsv",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importCSVFile(
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
