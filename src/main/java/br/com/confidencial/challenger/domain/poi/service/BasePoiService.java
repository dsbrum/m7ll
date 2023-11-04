package br.com.confidencial.challenger.domain.poi.service;

import br.com.confidencial.challenger.domain.poi.BasePOI;
import br.com.confidencial.challenger.domain.poi.repository.BasePoiRepository;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BasePoiService {
    @Autowired
    BasePoiRepository poiRepo;
    public Optional<BasePOI> getBasePoiPorLongELat(String longitude,String latitude){
        return poiRepo.findByLongitudeAndLatitude(longitude,latitude);
    }

}
