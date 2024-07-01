package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.service.traducao.ConsultaMyMemory;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity // Indica que é uma tabela no banco de dados
@Table(name = "series")// Usado para expecificar que o nome da tabela se não por padrão será o nome da classe
public class Serie {

    @Id // Indica que é a chave primaria do banco
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY, que é o auto-incremental. Então, se temos um valor inteiro, ele gerará a sequência;
    private Long id;
    @Column(name = "titulo", unique = true) // Usado para expecificar que o nome da coluna se não por padrão será o nome da coluna,  unique = true: está coluna nunca pode se repetir
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    @Enumerated(EnumType.STRING) // Indica que é um Enum para o Banco, EnumType.STRING: que grava a informação como uma string.
    private Categoria genero;
    private String atores;
    private String poster;
    private String sinopse;

//    @Transient // Indica para não salvar no banco de dados
    @OneToMany(mappedBy = "serie") // Indica que é Um para muitos na tabela, mappedBy: indicando o a referencia do outro mapeamento: ManyToAny na outra classe
    List<Episodio> episodios = new ArrayList<>();

    public Serie() {} // construtor padrão(contrutor vazio)

    public Serie(DadosSerie dadosSerie) {
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0); // como se fosse um try cat colocando um valor padrão de 0 caso não consiga converter
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim()); // faz uma conversão pelo metodo do Enum
        this.atores = dadosSerie.atores();
        this.poster = dadosSerie.poster();
        this.sinopse = ConsultaMyMemory.obterTraducao(dadosSerie.sinopse()).trim();
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        this.episodios = episodios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    @Override
    public String toString() {
        return
                "genero=" + genero +
                ", titulo='" + titulo + '\'' +
                ", totalTemporadas=" + totalTemporadas +
                ", avaliacao=" + avaliacao +
                ", atores='" + atores + '\'' +
                ", poster='" + poster + '\'' +
                ", sinopse='" + sinopse + '\'';
    }
}
