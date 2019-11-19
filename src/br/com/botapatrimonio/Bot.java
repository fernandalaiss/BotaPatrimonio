package br.com.botapatrimonio;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.List;
import java.util.function.ToDoubleBiFunction;

public class Bot {
    private Status status;
    private TelegramBot bot;
    private GetUpdatesResponse updatesResponse;  //gerencia o recebimento de mensagens
    private SendResponse sendResponse;           //gerencia o envio de respostas
    private BaseResponse baseResponse;           //gerencia o envio de ações do chat
    private int offset;
    static final String BOTTOKEN = "1034189549:AAHhdnEg7oz1objbfumXCWuS2PpqmsH-SP0";
    private Inventario inventario;
    private Localizacao localizacao;
    private Bem bem;
    private CategoriaDeBem categoriaDeBem;


    public void executaBot(){
        status = Status.NULL;
        bot = TelegramBotAdapter.build(BOTTOKEN);
        inventario = new Inventario();

        //controle de offset, ID atribuído a cada mensagem recebida
        offset = 0;

        //loop para executar escuta infinita de novas mensagens
        while(true){
            //averigua mensagens pendentes no telegram
            updatesResponse = bot.execute(new GetUpdates().limit(100).offset(offset));
            //Lista de mensagens recebidas
            List<Update> updates = updatesResponse.updates();
            //análise de cada mensagem
            for (Update update : updates) {
                tratarUpdate(update);
            }
        }
    }

    private void tratarUpdate(Update update) {

        offset = update.updateId()+1;
        String msg = update.message().text();
        Long chat = update.message().chat().id();

        baseResponse = bot.execute(new SendChatAction(chat, ChatAction.typing.name()));
        baseResponse = bot.execute(new SendMessage(chat,"Escrevendo..."));

        if(status == Status.NULL){
            baseResponse = bot.execute(new SendChatAction(chat, ChatAction.typing.name()));
            switch (msg){
                case "/start":
                    baseResponse = bot.execute(new SendMessage(chat, getComandos()));
                    break;
                case "/cadastrar_localizacao":
                    cadastrarLocalizacao(update);
                    break;
                case "/cadastrar_categoria_de_bem":
                    cadastrarCategoriaDeBem(update);
                    break;
                case "/cadastrar_bem":
                    cadastrarBem(update);
                    break;
                case "/listar_localizacoes":
                    baseResponse = bot.execute(new SendMessage(chat, listarLocalizacoes()));
                    break;
                case "/listar_categorias":
                    baseResponse = bot.execute(new SendMessage(chat, listarCategorias()));
                    break;
                case "/listar_bens_de_uma_localizacao":
                    listarBensDeLocalizacoes(update);
                    break;
                case "/buscar_bem_por_codigo":
                   buscarBemPorCodigo(update);
                    break;
                case "/buscar_bem_por_nome":
                    buscarBemPorNome(update);
                    break;
                case "/buscar_bem_por_descricao":
                    buscarBemPorDescricao(update);
                    break;
                case "/movimentar_bem":
                    movimentarBem(update);
                    break;
                case "/gerar_relatorio":
                    baseResponse = bot.execute(new SendMessage(chat, gerarRelatorio()));
                    break;
                default:
                    baseResponse = bot.execute(new SendMessage(chat,"Não entendi... Utilize um dos comandos" +
                            " abaixo para realizar alguma operação.\n"));
                    baseResponse = bot.execute(new SendMessage(chat,getComandos()));
                    break;
            }
        }else{
            switch (status){
                case LOCAL_ESPERANDO_NOME:
                    localizacao.setNome(msg);
                    baseResponse = bot.execute(new SendMessage(chat, "Descrição?"));
                    status = Status.LOCAL_ESPERANDO_DESCRICAO;
                    break;
                case LOCAL_ESPERANDO_DESCRICAO:
                    localizacao.setDescricao(msg);
                    inventario.cadastrarLocalizacao(localizacao);
                    baseResponse = bot.execute(new SendMessage(chat, "Localização cadastrada."));
                    status = Status.NULL;
                    break;
                case CATEGORIA_ESPERANDO_NOME:
                    categoriaDeBem.setNome(msg);
                    baseResponse = bot.execute(new SendMessage(chat, "Descrição?"));
                    status = Status.CATEGORIA_ESPERANDO_DESCRICAO;
                    break;
                case CATEGORIA_ESPERANDO_DESCRICAO:
                    categoriaDeBem.setDescricao(msg);
                    inventario.cadastrarCategoriaDeBem(categoriaDeBem);
                    baseResponse = bot.execute(new SendMessage(chat, "Categoria de bem cadastrada."));
                    status = Status.NULL;
                    break;
                case BEM_ESPERANDO_NOME:
                    bem.setNome(msg);
                    baseResponse = bot.execute(new SendMessage(chat, "Descrição?"));
                    status = Status.BEM_ESPERANDO_DESCRICAO;
                    break;
                case BEM_ESPERANDO_DESCRICAO:
                    bem.setDescricao(msg);
                    baseResponse = bot.execute(new SendMessage(chat, "Localização? Clique no link da localização desejada:"));
                    baseResponse = bot.execute(new SendMessage(chat, listarLocalizacoes()));
                    status = Status.BEM_ESPERANDO_LOCALIZACAO;
                    break;
                case BEM_ESPERANDO_LOCALIZACAO:
                    localizacao = inventario.getLocalizacao(msg);
                    bem.setLocalizacao(localizacao);
                    baseResponse = bot.execute(new SendMessage(chat, "Categoria? Clique no link da categoria desejada:"));
                    baseResponse = bot.execute(new SendMessage(chat, listarCategorias()));
                    status = Status.BEM_ESPERANDO_CATEGORIA;
                    break;
                case BEM_ESPERANDO_CATEGORIA:
                    categoriaDeBem = inventario.getCategoriaDeBem(msg);
                    bem.setCategoria(categoriaDeBem);
                    inventario.cadastrarBem(bem);
                    baseResponse = bot.execute(new SendMessage(chat, "Bem cadastrado."));
                    status = Status.NULL;
                    break;
                case LISTAR_BENS_ESPERANDO_LOCALIZACAO:
                    localizacao = inventario.getLocalizacao(msg);
                    baseResponse = bot.execute(new SendMessage(chat, localizacao.getBens()));
                    status = Status.NULL;
                    break;
                case MOVIMENTAR_BEM_ESPERANDO_BEM:
                    bem = inventario.getBem(msg);
                    baseResponse = bot.execute(new SendMessage(chat, "Localização? Clique no link da localização desejada:"));
                    baseResponse = bot.execute(new SendMessage(chat, listarLocalizacoes()));
                    status = Status.MOVIMENTAR_BEM_ESPERANDO_LOCALIZACAO;
                    break;
                case MOVIMENTAR_BEM_ESPERANDO_LOCALIZACAO:
                    localizacao = inventario.getLocalizacao(msg);
                    inventario.movimentarBem(bem, localizacao);
                    baseResponse = bot.execute(new SendMessage(chat, "Bem movido."));
                    status = Status.NULL;
                    break;
                case BUSCA_ESPERANDO_CODIGO:
                    bem = inventario.buscarBemPorCodigo(msg);
                    baseResponse = bot.execute(new SendMessage(chat, "Olá, seu bem está em:"+ bem.getLocalizacao().getNome()));
                    status = Status.NULL;

                    break;
                case BUSCA_ESPERAND_NOME:
                    bem = inventario.buscarBemPorNome(msg);
                    baseResponse = bot.execute(new SendMessage(chat, "Olá, seu bem está em:"+ bem.getLocalizacao().getNome()));
                    status = Status.NULL;

                    break;
                case BUSCA_ESPERANDO_DESCRICAO:
                    bem = inventario.buscarBemPorDescricao(msg);
                    baseResponse = bot.execute(new SendMessage(chat, "Olá, seu bem está em:"+ bem.getLocalizacao().getNome()));
                    status = Status.NULL;

                    break;
            }
            if (status == Status.NULL){
                baseResponse = bot.execute(new SendMessage(chat, getComandos()));
            }
        }
    }

    private void cadastrarLocalizacao(Update update) {
        baseResponse = bot.execute(new SendMessage(update.message().chat().id(),"Cadastrando Localização, informe o que for " +
                "pedido..."));
        baseResponse = bot.execute(new SendMessage(update.message().chat().id(),"Nome?"));
        status = Status.LOCAL_ESPERANDO_NOME;
        localizacao = new Localizacao();
    }

    private void cadastrarCategoriaDeBem(Update update) {
        baseResponse = bot.execute(new SendMessage(update.message().chat().id(),"Cadastrando Categoria de Bem, informe o que " +
                "for pedido..."));
        baseResponse = bot.execute(new SendMessage(update.message().chat().id(),"Nome?"));
        status = Status.CATEGORIA_ESPERANDO_NOME;
        categoriaDeBem = new CategoriaDeBem();
    }

    private void cadastrarBem(Update update) {
        baseResponse = bot.execute(new SendMessage(update.message().chat().id(),"Cadastrando Bem, informe o que for " +
                "pedido..."));
        baseResponse = bot.execute(new SendMessage(update.message().chat().id(),"Nome?"));
        status = Status.BEM_ESPERANDO_NOME;
        bem = new Bem();
    }

    private String listarLocalizacoes() {
        return inventario.listarLocalizacoes();
    }

    private String listarCategorias() {
        return inventario.listarCategoriasDeBem();
    }

    private void listarBensDeLocalizacoes(Update update) {
        baseResponse = bot.execute(new SendMessage(update.message().chat().id(),"Clique no link da localização desejada:\n"));
        baseResponse = bot.execute(new SendMessage(update.message().chat().id(),listarLocalizacoes()));
        status = Status.LISTAR_BENS_ESPERANDO_LOCALIZACAO;
    }

    private String listarBens() {
        return inventario.listarBens();
    }

    private void buscarBemPorCodigo(Update update){
        baseResponse = bot.execute(new SendMessage(update.message().chat().id(),"Porfavor, digite o codigo do bem a ser procurado"));
        status = status.BUSCA_ESPERANDO_CODIGO;
    }
    private void buscarBemPorNome(Update update){
        baseResponse = bot.execute(new SendMessage(update.message().chat().id(),"Porfavor, digite o nome do bem a ser procurado"));
        status = status.BUSCA_ESPERAND_NOME;
    }private void buscarBemPorDescricao(Update update){
        baseResponse = bot.execute(new SendMessage(update.message().chat().id(),"Porfavor, digite a descricao do bem a ser procurado"));
        status = status.BUSCA_ESPERANDO_DESCRICAO;

    }

    private void movimentarBem(Update update) {
        baseResponse = bot.execute(new SendMessage(update.message().chat().id(), "Movimentando Bem, informe o que for " +
                "pedido..."));
        baseResponse = bot.execute(new SendMessage(update.message().chat().id(), "Clique no link do bem desejado:\n"));
        baseResponse = bot.execute(new SendMessage(update.message().chat().id(),listarBens()));
        status = Status.MOVIMENTAR_BEM_ESPERANDO_BEM;
    }

    private String gerarRelatorio() {
        return inventario.gerarRelatorio();
    }

    private String getComandos(){
        return "Lista de comandos do Bota Patrimônio:\n" +
                "/start - exibe lista de comandos\n" +
                "/cadastrar_localizacao\n" +
                "/cadastrar_categoria_de_bem\n" +
                "/cadastrar_bem\n" +
                "/listar_localizacoes\n" +
                "/listar_categorias\n" +
                "/listar_bens_de_uma_localizacao\n" +
                "/buscar_bem_por_codigo\n" +
                "/buscar_bem_por_nome\n" +
                "/buscar_bem_por_descricao\n" +
                "/movimentar_bem\n" +
                "/gerar_relatorio";
    }
}
