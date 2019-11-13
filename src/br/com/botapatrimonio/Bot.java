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
                    status = Status.LOCAL_ESPERANDO_NOME;
                    cadastrarLocalizacao(update);
                    break;
                case "/cadastrar_categoria_de bem":
                    status = Status.CATEGORIA_ESPERANDO_NOME;
                    cadastrarCategoriaDeBem(update);
                    break;
                case "/cadastrar_bem":
                    status = Status.BEM_ESPERANDO_NOME;
                    cadastrarBem(update);
                    break;
                case "/listar_localizacoes":
                    listarLocalizacoes();
                    break;
                case "/listar_categorias":
                    //PEGAR DO INVENTÁRIO;
                    break;
                case "/listar_bens_de_uma_localização":
                    //PEGAR DO INVENTÁRIO;
                    break;
                case "/buscar_bem_por_codigo":
                    //PEGAR DO INVENTÁRIO;
                    break;
                case "/buscar_bem_por_nome":
                    //PEGAR DO INVENTÁRIO;
                    break;
                case "/buscar_bem_por_descricao":
                    //PEGAR DO INVENTÁRIO;
                    break;
                case "/movimentar_bem":
                    //PEGAR DO INVENTÁRIO;
                    break;
                case "/gerar_relatorio":
                    //PEGAR DO INVENTÁRIO;
                    break;
                default:
                    baseResponse = bot.execute(new SendMessage(chat,"Não entendi... Utilize dos " +
                            "um dos comandos abaixo para realizar alguma operação.\n"));
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
                    baseResponse = bot.execute(new SendMessage(chat, listarLocalizacoes()));
                    baseResponse = bot.execute(new SendMessage(chat, "Localização?"));
                    status = Status.BEM_ESPERANDO_LOCALIZACAO;
                    break;
                case BEM_ESPERANDO_LOCALIZACAO:
                    //TODO VERIFICAR SE MSG É VÁLIDO
                    localizacao = inventario.getLocalizacao(msg);
                    bem.setLocalizacao(localizacao);
                    baseResponse = bot.execute(new SendMessage(chat, listarCategorias()));
                    baseResponse = bot.execute(new SendMessage(chat, "Categoria?"));
                    status = Status.BEM_ESPERANDO_CATEGORIA;
                    break;
                case BEM_ESPERANDO_CATEGORIA:
                    //TODO VERIFICAR SE MSG É VÁLIDO
                    categoriaDeBem = inventario.getCategoriaDeBem(msg);
                    bem.setCategoria(categoriaDeBem);
                    inventario.cadastrarBem(bem);
                    baseResponse = bot.execute(new SendMessage(chat, "Bem cadastrado."));
                    status = Status.NULL;
                    break;
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

    private String getComandos(){
        return "Lista de comandos do Bota Patrimônio:\n" +
                "/start - exibe lista de comandos\n" +
                "/cadastrar_localizacao\n" +
                "/cadastrar_bem\n" +
                "/cadastrar_categoria_de_bem\n" +
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
