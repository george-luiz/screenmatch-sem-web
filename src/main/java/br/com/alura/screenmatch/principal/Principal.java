package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.service.ConsumoAPI;

import java.util.Scanner;

public class Principal {

    private static final String ENDERECO = "https://www.omdbapi.com/?t=";
    private static final String API_KEY = "&apikey=6585022c";
    private ConsumoAPI consumo = new ConsumoAPI();
    Scanner leitura = new Scanner(System.in);

    public void exibirMenu() {
        System.out.println("Digite o nome da serie");
        String nomeSerie = leitura.nextLine();
        String json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

    }

}
