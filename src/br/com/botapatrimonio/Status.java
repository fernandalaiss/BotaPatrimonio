package br.com.botapatrimonio;

public enum Status {
    NULL,

    BEM_ESPERANDO_NOME,
    BEM_ESPERANDO_DESCRICAO,
    BEM_ESPERANDO_LOCALIZACAO,
    BEM_ESPERANDO_CATEGORIA,

    LOCAL_ESPERANDO_NOME,
    LOCAL_ESPERANDO_DESCRICAO,

    CATEGORIA_ESPERANDO_NOME,
    CATEGORIA_ESPERANDO_DESCRICAO,

    LISTAR_BENS_ESPERANDO_LOCALIZACAO,


    MOVIMENTAR_BEM_ESPERANDO_BEM,
    MOVIMENTAR_BEM_ESPERANDO_LOCALIZACAO,

    BUSCA_ESPERAND_NOME,
    BUSCA_ESPERANDO_CODIGO,
    BUSCA_ESPERANDO_DESCRICAO;
}
