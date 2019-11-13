package br.com.botapatrimonio;

public class Bem {
    private String codigo;
    private String nome;
    private String descricao;
    private Localizacao localizacao;
    private CategoriaDeBem categoria;

    public Bem(){}

    public Bem(String codigo, String nome, String descricao, Localizacao localizacao, CategoriaDeBem categoria) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.categoria = categoria;
    }

    public String getCodigo() { return codigo; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Localizacao getLocalizacao() { return localizacao; }

    public void setLocalizacao(Localizacao localizacao) { this.localizacao = localizacao; }

    public CategoriaDeBem getCategoria() { return categoria; }

    public void setCategoria(CategoriaDeBem categoria) { this.categoria = categoria; }

    public void setCodigo(String s) {
        codigo = s;
    }
}
