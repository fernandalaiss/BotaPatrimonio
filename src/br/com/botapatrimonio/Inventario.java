package br.com.botapatrimonio;
import java.util.*;

public class Inventario {
    Map<String,Localizacao> localizacaoList;
    Map<String,CategoriaDeBem> categoriaDeBemList;
    Map<String, Bem> bemMap;
    //talvez criar outros map com chaves necessárias para busca ou buscar outra solução

    Inventario(){
        localizacaoList = new HashMap<String,Localizacao>();
        categoriaDeBemList = new HashMap<String,CategoriaDeBem>();
        bemMap = new HashMap<String, Bem>();

    }

    public void cadastrarLocalizacao(Localizacao localizacao){
        localizacao.setCodigo("loc0"+String.valueOf(localizacaoList.size()));
        localizacaoList.put(localizacao.getCodigo(),localizacao);
    }

    public void cadastrarCategoriaDeBem(CategoriaDeBem categoriaDeBem){
        categoriaDeBem.setCodigo("cat0"+String.valueOf(categoriaDeBemList.size()));
        categoriaDeBemList.put(categoriaDeBem.getCodigo(),categoriaDeBem);
    }

    public void cadastrarBem(Bem bem){
        bem.setCodigo("bem0"+String.valueOf(bemMap.size()));
        bem.getLocalizacao().addBem(bem);
        bemMap.put(bem.getCodigo(),bem);
    }

    public Localizacao getLocalizacao(String msg) {
        msg = msg.replace("/","");
        return localizacaoList.get(msg);
    }

    public CategoriaDeBem getCategoriaDeBem(String msg) {
        msg = msg.replace("/","");
        return categoriaDeBemList.get(msg);
    }

    public String listarLocalizacoes(){
        String list = "Código - Nome - Descrição";
        for (String c : localizacaoList.keySet()) {
            Localizacao l =localizacaoList.get(c);
            list = list.concat("\n/"+l.getCodigo()+" - "+l.getNome()+" - "+l.getDescricao());
        }
        return list;
    }

    public String listarCategoriasDeBem(){
        String list = "Código - Nome - Descrição";
        for (String c : categoriaDeBemList.keySet()) {
            CategoriaDeBem cat = categoriaDeBemList.get(c);
            list = list.concat("\n/"+cat.getCodigo()+" - "+cat.getNome()+" - "+cat.getDescricao());
        }
        return list;
    }


/*

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
