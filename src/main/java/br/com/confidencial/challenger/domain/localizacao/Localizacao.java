package br.com.confidencial.challenger.domain.localizacao;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
@Entity
@Table(name = "localizacao")
@Data
public class Localizacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "placa", nullable = false, length = 255)
    private String placa;

    @Column(name = "data_posicao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPosicao;

    @Column(name = "velocidade", nullable = false)
    private int velocidade;

    @Column(name = "longitude", nullable = false, length = 20)
    private String longitude;

    @Column(name = "latitude", nullable = false, length = 20)
    private String latitude;

    @Column(name = "ignicao", nullable = false)
    private boolean ignicao;
}
