package br.com.botapatrimonio;

import java.util.HashSet;
import java.util.Set;

public class Localizacao {
    private String codigo;
    private String nome;
    private String descricao;
    private Set<Bem> bensContidos;

    public Localizacao(){
        bensContidos = new HashSet<Bem>();
    }

    public void addBem(Bem bem){
        bensContidos.add(bem);
    }

    public void removeBem(Bem bem) {
        bensContidos.remove(bem);
    }

    public String getBens(){
        String list = "Código - Nome";
        return list.concat(fgetBens(""));
    }

    String fgetBens(String prefix) {
        String list = "";

        for (Bem b:bensContidos) {
            list = list.concat("\n"+prefix+b.getCodigo()+" - "+b.getNome());
        }
        return list;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
