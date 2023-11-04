package br.com.confidencial.challenger.domain.poi;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "base_poi")
@Data
public class BasePOI {
    @Id
    @Column(name = "nome")
    private String nome;
    @Column(name = "raio", nullable = false)
    private int raio;
    @Column(name = "longitude", nullable = false, length = 20)
    private String longitude;
    @Column(name = "latitude", nullable = false, length = 20)
    private String latitude;
}
