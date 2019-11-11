package br.com.botapatrimonio;
import java.util.*;

public class Inventario {
    Set<Localizacao> localizacaoList;
    Set<CategoriaDeBem> categoriaDeBemList;
    Map<String, Bem> bemMap;
    //talvez criar outros map com chaves necessárias para busca ou buscar outra solução

    Inventario(){
        localizacaoList = new HashSet<Localizacao>();
        categoriaDeBemList = new HashSet<CategoriaDeBem>();
        bemMap = new HashMap<String, Bem>();
    }

    public void cadastrarLocalizacao(){

    }

    public void cadastrarCategoriaDeBem(){

    }

    public void cadastrarBem(){

    }
/*
    public Set listarLocalizacoes(){

    }

    public Set listarCategoriasDeBem(){

    }

    public Set listarBensDeLocalizacao(Localizacao local){

    }

    public Bem buscarBemPorCodigo(String codigo){

    }

    public Bem buscarBemPorNome(String nome){

    }

    public Bem buscarBemPorDescricao(){

    }

    public void movimentarBem(Localizacao outroLocal){

    }

    public String gerarRelatorio(){

    }

 */
}
