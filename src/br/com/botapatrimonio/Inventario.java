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

    public void cadastrarLocalizacao(Localizacao localizacao){
        localizacaoList.add(localizacao);
    }

    public void cadastrarCategoriaDeBem(CategoriaDeBem categoriaDeBem){
        categoriaDeBem.setCodigo("cat0"+String.valueOf(categoriaDeBemList.size()));
        categoriaDeBemList.add(categoriaDeBem);
    }

    public void cadastrarBem(Bem bem){
        bem.setCodigo("bem0"+String.valueOf(bemMap.size()));
        bemMap.put(bem.getCodigo(),bem);
    }
/*
    public Localizacao getLocalizacao(String msg) {

    }

    public CategoriaDeBem getCategoriaDeBem(String msg) {
    }

    public String listarLocalizacoes(){
        for (Localizacao localizacao : localizacaoList) {

        }
    }

    public String listarCategoriasDeBem(){

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
