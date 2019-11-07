package br.com.botapatrimonio;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        TelegramBot bot = TelegramBotAdapter.build("1034189549:AAHhdnEg7oz1objbfumXCWuS2PpqmsH-SP0");
        //gerencia o recebimento de mensagens
        GetUpdatesResponse updatesResponse;
        //gerencia o envio de respostas
        SendResponse sendResponse;
        //gerencia o envio de ações do chat
        BaseResponse baseResponse;

        //controle de offset, ID atribuído a cada mensagem recebida
        int offset = 0;

        //loop para executar escuta infinita de novas mensagens
        while (true){
            //averigua mensagens pendentes no telegram
            updatesResponse = bot.execute(new GetUpdates().limit(100).offset(offset));
            //Lista de mensagens recebidas
            List<Update> updates = updatesResponse.updates();
            //análise de cada mensagem
            for (Update update : updates) {
                offset = update.updateId()+1;
                System.out.println("Recebi a mensagem "+update.message().text());
                //enviando "Escrevendo..."
                baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
                baseResponse = bot.execute(new SendMessage(update.message().chat().id(),"Escrevendo..."));

                //enviando resposta
                baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
                baseResponse = bot.execute(new SendMessage(update.message().chat().id(),"Não entendi..."));
            }
        }

    }
}
