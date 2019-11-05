package br.com.botapatrimonio;

public class Bem {
    private String codigo;
    private String nome;
    private String descricao;
    private String localizacao;
    private String categoria;

    public String getCodigo() { return codigo; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getLocalizacao() { return localizacao; }

    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public String getCategoria() { return categoria; }

    public void setCategoria(String categoria) { this.categoria = categoria; }
}
