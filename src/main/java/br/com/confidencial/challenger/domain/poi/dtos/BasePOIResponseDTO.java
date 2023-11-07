package br.com.confidencial.challenger.domain.poi.dtos;

import java.util.List;

public record BasePOIResponseDTO (String poi,String raio, String placa, List<String> tempo){}
