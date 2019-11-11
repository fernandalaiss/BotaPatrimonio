package br.com.botapatrimonio;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.List;

public class Bot {
    private TelegramBot bot;
    //gerencia o recebimento de mensagens
    private GetUpdatesResponse updatesResponse;
    //gerencia o envio de respostas
    private SendResponse sendResponse;
    //gerencia o envio de ações do chat
    private BaseResponse baseResponse;
    private int offset;

    public void executaBot(){
        bot = TelegramBotAdapter.build("1034189549:AAHhdnEg7oz1objbfumXCWuS2PpqmsH-SP0");

        //controle de offset, ID atribuído a cada mensagem recebida
        offset = 0;

        //loop para executar escuta infinita de novas mensagens
        while(true){
            updateList(1);
        }
    }

    public void updateList(int caso){
        //averigua mensagens pendentes no telegram
        updatesResponse = bot.execute(new GetUpdates().limit(100).offset(offset));
        //Lista de mensagens recebidas
        List<Update> updates = updatesResponse.updates();
        //análise de cada mensagem
        for (Update update : updates) {
            if(caso == 1){
                tratarUpdate(update);
            }else if(caso == 2){

            }
        }
    }

    private void tratarUpdate(Update update) {

        offset = update.updateId()+1;
        Message msg = update.message();

        baseResponse = bot.execute(new SendChatAction(msg.chat().id(), ChatAction.typing.name()));
        baseResponse = bot.execute(new SendMessage(msg.chat().id(),"Escrevendo..."));

        if(msg.equals("/start")){
            baseResponse = bot.execute(new SendMessage(update.message().chat().id(), "Bem vindo ao Bota Patrimônio\n"+getMenu()));

        }else if(msg.text().matches("-?\\d+(\\.\\d+)?")){ // se for um número vai pro switch

            baseResponse = bot.execute(new SendChatAction(msg.chat().id(), ChatAction.typing.name()));
            //enviando resposta
            switch (Integer.valueOf(msg.text())){
                case 1:
                    cadastrarLocalizacao(update);
                    break;
                case 2:
                    cadastrarCategoria(update);
                    break;
                case 3:
                    cadastrarBem(update);
                    break;
                case 4:
                    listarLocalizacoes(update);
                    break;
                case 5:
                    listarCategorias(update);
                    break;
                case 6:
                    listarBensPorLocalizacao(update);
                    break;
                case 7:
                    buscarBemPorCodigo(update);
                    break;
                case 8:
                    buscarBemPorNome(update);
                    break;
                case 9:
                    buscarBemPorDescricao(update);
                    break;
                case 10:
                    movimentarBem(update);
                    break;
                case 11:
                    gerarRelatorio(update);
                    break;
                default:
                    baseResponse = bot.execute(new SendMessage(msg.chat().id(),"O número "+msg.text()+" não equivale a nenhuma opção."));
                    break;
            }

        }else { // se não for um número tratado acima

            //enviando resposta
            baseResponse = bot.execute(new SendChatAction(msg.chat().id(), ChatAction.typing.name()));
            baseResponse = bot.execute(new SendMessage(msg.chat().id(),"Não entendi... Informe o número da opção desejada."));
        }
    }

    private void cadastrarLocalizacao(Update update) {
        baseResponse = bot.execute(new SendMessage(update.message().chat().id(),"Para cadastrar um bem, informe "));
        updateList(2);
    }

    private String getMenu(){
        String menu = "Para realizar uma ação no inventário, informe uma das opções:\n" +
                "1. cadastrar localização (sala, laboratório, auditório, etc.)\n" +
                "2. cadastrar categoria de bem (ex.: móvel, eletrônico, material de limpeza, etc.)\n" +
                "3. cadastrar bem (cadeira, mesa, computador, sabão em pó, etc.)\n" +
                "4. listar localizações\n" +
                "5. listar categorias\n" +
                "6. listar bens de uma localização\n" +
                "7. buscar bem por código\n" +
                "8. buscar bem por nome\n" +
                "9. buscar bem por descricao\n" +
                "10. movimentar bem\n" +
                "11. gerar relatório";
        return menu;
    }
}
