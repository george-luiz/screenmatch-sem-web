package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) //JsonIgnoreProperties: ignora o que vem da API que não estiver mapeado nesta classe
public record DadosSerie (
        @JsonAlias("Title") String titulo, //JsonAlias irá buscar na API exatamente o Title e irá incluir no titulo
        @JsonAlias("totalSeasons") Integer totalTemporadas,
        @JsonAlias("imdbRating") String avaliacao) {

}
