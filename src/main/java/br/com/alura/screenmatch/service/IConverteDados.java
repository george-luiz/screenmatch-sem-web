package br.com.alura.screenmatch.service;

public interface IConverteDados {

    <T> T obterDados(String json, Class<T> classe); //<T> T: Irá retornar um tipo generico
}
