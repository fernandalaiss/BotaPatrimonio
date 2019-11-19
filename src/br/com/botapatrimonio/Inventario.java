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

        inventarioTestSetup();
    }

    public void inventarioTestSetup() {
        Localizacao locA = new Localizacao();
        locA.setCodigo("loc00");
        locA.setNome("localA");
        locA.setDescricao("local teste A");

        Localizacao locB = new Localizacao();
        locB.setCodigo("loc01");
        locB.setNome("localB");
        locB.setDescricao("local teste B");

        localizacaoList.put(locA.getCodigo(), locA);
        localizacaoList.put(locB.getCodigo(), locB);

        CategoriaDeBem cat0 = new CategoriaDeBem();
        cat0.setCodigo("cat00");
        cat0.setNome("categoria0");
        cat0.setDescricao("categoria teste 0");

        CategoriaDeBem cat1 = new CategoriaDeBem();
        cat1.setCodigo("cat01");
        cat1.setNome("categoria1");
        cat1.setDescricao("categoria teste 1");

        categoriaDeBemList.put(cat0.getCodigo(), cat0);
        categoriaDeBemList.put(cat1.getCodigo(), cat1);

        Bem bem = new Bem();
        bem.setCodigo("bem00");
        bem.setNome("bem 0");
        bem.setDescricao("bem teste 0");
        bem.setCategoria(categoriaDeBemList.get("cat00"));
        bem.setLocalizacao(locA);
        locA.addBem(bem);

        bemMap.put(bem.getCodigo(), bem);
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

    public Bem getBem(String msg) {
        msg = msg.replace("/", "");
        return bemMap.get(msg);

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

    public String listarBens(){
        String list = "Código - Nome - Descrição";
        for (String c : bemMap.keySet()) {
            Bem bem = bemMap.get(c);
            list = list.concat("\n/"+bem.getCodigo()+" - "+bem.getNome()+" - "+bem.getDescricao());
        }
        return list;
    }


    public Bem buscarBemPorCodigo(String codigo){
        for (String c : bemMap.keySet()) {
            if(c.equals(codigo)){
                return bemMap.get(c);
            }
        }
        return new Bem();
    }

    public Bem buscarBemPorNome(String nome){
        for (String c : bemMap.keySet()) {
            Bem bem = bemMap.get(c);
            if(bem.getNome().equals(nome)){
                return bem;
            }
        }
        return new Bem();
    }

    public Bem buscarBemPorDescricao(String descricao){
        for (String c : bemMap.keySet()) {
            Bem bem = bemMap.get(c);
            if(bem.getDescricao().equals(descricao)){
                return bem;
            }
        }
        return new Bem();
    }

    public void movimentarBem(Bem bem, Localizacao outroLocal){
        Localizacao localOriginal = bem.getLocalizacao();
        localizacaoList.get(localOriginal.getCodigo()).removeBem(bem);
        localizacaoList.get(outroLocal.getCodigo()).addBem(bem);

        bem.setLocalizacao(outroLocal);
    }

    public String gerarRelatorio(){
        // Local, categoria, nome
        StringBuilder relatorio = new StringBuilder();

        // Local
        relatorio.append("Bens por localização\nCódigo - Nome - Descrição");
        for(String c : localizacaoList.keySet()) {
            relatorio.append("\n"+localizacaoList.get(c).getNome());
            relatorio.append(localizacaoList.get(c).fgetBens("  "));
        }

        // Categoria
        relatorio.append("\n\nBens por categoria\nCódigo - Nome - Descrição");
        for(String c : categoriaDeBemList.keySet()) {

            relatorio.append("\n"+categoriaDeBemList.get(c).getNome());
            for(Map.Entry<String, Bem> entrada : bemMap.entrySet()) {
                Bem bem = entrada.getValue();
                if(bem.getCategoria().getCodigo().contentEquals(categoriaDeBemList.get(c).getCodigo())) {
                    relatorio.append("\n  "+entrada.getKey()+" - "+bem.getNome()+" - "+bem.getDescricao());
                }
            }
        }

        // Nome
        Vector<String> nomesDosBens = new Vector<>();

        for(String c : bemMap.keySet()) {
            String nome = bemMap.get(c).getNome();

            if(nomesDosBens.isEmpty()) {
                nomesDosBens.add(nome);
                continue;
            }

            Iterator i = nomesDosBens.iterator();
            while(i.hasNext()) {
                String nomeNoVector = i.next().toString();
                int diff = nome.compareToIgnoreCase(nomeNoVector);

                if(diff <= 0) {
                    int indiceAtual = nomesDosBens.indexOf(nomeNoVector);
                    nomesDosBens.add(indiceAtual, nome);
                    break;
                }

                if(!i.hasNext()) {
                    nomesDosBens.add(nome);
                    break;
                }
            }
        }

        relatorio.append("\n\nBens por nome");

        Iterator i = nomesDosBens.iterator();
        while (i.hasNext()) {
            relatorio.append("\n  "+i.next().toString());
        }

        return relatorio.toString();
    }
}
