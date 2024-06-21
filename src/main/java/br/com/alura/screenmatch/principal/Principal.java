package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private static final String ENDERECO = "https://www.omdbapi.com/?t=";
    private static final String API_KEY = "&apikey=6585022c";

    private static final String SEASON = "&season=";
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    Scanner leitura = new Scanner(System.in);

    public void exibirMenu() {
        System.out.println("Digite o nome da serie");
        String nomeSerie = leitura.nextLine();
        String url = ENDERECO + nomeSerie.replace(" ", "+") + API_KEY;
        String json = consumo.obterDados(url);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);


        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            String enderecoUrl = ENDERECO + nomeSerie.replace(" ", "+") + SEASON + i + API_KEY;
            json = consumo.obterDados(enderecoUrl);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);

        }
        temporadas.forEach(System.out::println);

//        for (int i = 0; i < dados.totalTemporadas(); i++) {
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.println(j + ": " + episodiosTemporada.get(j).titulo());
//            }
//        }

        temporadas.forEach(t -> t.episodios()
                .stream().limit(5)
                .forEach(e -> System.out.println(e.titulo())));


//        List<String> nomes = Arrays.asList("Jacque", "Yasmin", "Paulo", "Rodrigo", "Nico");
//        nomes.forEach(System.out::println);
//        nomes.stream()
//                .sorted() // ordena alfabeticamente.
//                .limit(3) // limita os 3 três primeiros da lista
//                .filter(n -> n.startsWith("N")) // um filtro que começa com a letra N: Filter: permite filtrar os elementos da stream com base em uma condição.
//                .map(n -> n.toUpperCase()) //transforma a lista em uma outra lista com letra maiscula: Map: permite transformar cada elemento da stream em outro tipo de dado
//                .forEach(System.out::println);

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())// flatMap: cria uma lista nova
                //.toList() // a lista fica imutavel en ão pode ser modificada
                .collect(Collectors.toList());// collect: a lista nova pode ser modificada

        System.out.println("\nTop 5 episodios");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A")) // irá filtrar a lista para todos da lista que a avaliação seja diferente que N/A
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        System.out.println("\nlista de episodios: ");
        List<Episodio> episodio = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.temporada(), d))
                )
                .filter(e -> !e.getAvaliacao().equals("N/A"))
                .sorted(Comparator.comparing(Episodio::getAvaliacao).reversed())
//                .limit(10)
                .collect(Collectors.toList());

        episodio.forEach(System.out::println);

//        System.out.println("A partir de qual ano você quer ver os episodios? ");
//        int ano = leitura.nextInt();
//        leitura.nextLine(); // toda vez que usar o nextInt() tem de usar depois o leitura.nextLine() para o Java não se atrapalhar
//
//        LocalDate dataBusca = LocalDate.of(ano,1, 1);
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Deixando a data em formato brasileiro
//
//        episodio.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada" + e.getTemporada() +
//                                " Episodio: " + e.getTitulo() +
//                                " Data lançamento " + e.getDataLancamento().format(formatador)
//                ));


//        System.out.println("*********** Filtro *************");
//        System.out.println("Digite um trecho do titulo do episodio?");
//        String trechoTitulo = leitura.nextLine();
//        Optional<Episodio> episodioBusca = episodio.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .findFirst();
//        if(episodioBusca.isPresent()) {
//            System.out.println("Episodio encontrado!");
//            System.out.println("Temporada: " + episodioBusca.get());
//        } else {
//            System.out.println("Episodio não encontrado!");
//        }

        Map<Integer, Double> avaliacoesPorTemporada = episodio.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println("\navaliacoesPorTemporada: " + avaliacoesPorTemporada);

        DoubleSummaryStatistics est = episodio.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Média" + est.getAverage());
        System.out.println("Melhor episodio" + est.getMax());
        System.out.println("Pior episodio" + est.getMin());
        System.out.println("Quantida episodio" + est.getCount());
    }
}
