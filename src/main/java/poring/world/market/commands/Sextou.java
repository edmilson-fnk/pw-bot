package poring.world.market.commands;

import static poring.world.constants.Constants.SLEEPING;

import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.market.Command;
import poring.world.watcher.Watcher;

import java.util.*;

import com.vdurmont.emoji.EmojiParser;

public class Sextou extends Command {

    public static final String[] DIA_DE = new String[]{
        "dia de comprar moonlight flower no leilão",
        "dia de mandar o Bahia se lascar",
        "dia de mandar o Balt gastar zeny na troca",
        "dia de derrubar pau com o Dud",
        "dia de perguntar cadê o dano do Dashe",
        "dia de evento MVP, CORRE",
        "dia de beber e logar no rag pra refinar",
        "dia de torrar salário",
        "dia de dar bicuda em rapariga",
        "dia de fumar cigarro ao contrário",
        "dia de botar a muda pra falar",
        "dia de mandar foto pra ex",
        "dia de farra no motel",
        "dia de ficar pelado com três no motel",
        "dia de voltar pra casa latindo",
        "dia de ficar em casa vendo netflix",
        "com S de sei-nem-o-que-tô-fazendo-da-vida",
        "com S de sem dinheiro, sem disposição e sem coragem",
        "com S de SHURRASCO",
        "com S de SERVEJA",
        "com S de SEM ZENY E NEM BCC",
        "com S de Só um depósito de 5 mil reais pra salvar minha conta",
        "dia de mandar mensagem pra todos os contatinhos",
        "dia de ativar o modo Fábio Assunção",
        "tá feliz???? Abre a carteira, que passa"
    };

    private String getSextou() {
        return DIA_DE[new Random().nextInt(DIA_DE.length)];
    }

    @Override
    public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.HOUR_OF_DAY, -3);
        if (today.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            event.getChannel().sendMessage(
                    String.format("Hoje é sexta-feira, %s. SEXTOU!", this.getSextou())
            );
        } else {
            event.getMessage().addReaction(EmojiParser.parseToUnicode(SLEEPING));
        }
    }

    @Override
    public String getHelp() {
        return "";
    }

    @Override
    public List<String> getQueries() {
        return new ArrayList<>();
    }
}
