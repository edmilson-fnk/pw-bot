package poring.world.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Jokes {

    public static String BALT_JOKE = "Olha que bonitinho e uma foto tua mais jovem né <@661669474633515058>";
    public static String AIAI_MTOBOM = "AHhahAHAH Aiii aiii , muito bom!!";

    public static Map<String, String> CUSTOM_JOKES = new HashMap<String, String>(){{
        this.put("bahia", "Bahia: Que madrugada?");
        this.put("dashe2", "tá certo! :handshake:");
        this.put("dashe", "Boa tarde, Tiago!\nTudo bem?\nFoi um erro, mas já foi corrigido!\nObrigado por avisar!");
        this.put("maveco", "eu falei!");
        this.put("dud", "vê lá se teu c* tá limpo");
        this.put("balt", BALT_JOKE);
        this.put("balthorm", BALT_JOKE);
        this.put("bom", AIAI_MTOBOM);
        this.put("mtobom", AIAI_MTOBOM);
        this.put("mto bom", AIAI_MTOBOM);
    }};

    public static String getRandomJoke() {
        int rnd = new Random().nextInt(ALL_JOKES.length);
        return ALL_JOKES[rnd];
    }

    public static String getNamedJoke(String name) {
        return CUSTOM_JOKES.getOrDefault(name, getRandomJoke());
    }

    public static final String[] ALL_JOKES = new String[]{
            "A mãe do André, do Pierre e do Deniro... é a Maya???",
            "Ando muito estressado. Devem ser essas poções da fúria...",
            "As Plantas Infernais dos Bioquímicos são atacadas por Moscas Infernais?",
            "Au Au Au eu sou o carnissau!!! E sou mal!!!",
            "Bafomé Jr... por que não bafinho? Aposto que eles não escovam os dentes~",
            "Cautela e caldo de galinha não faz mal a ninguém... exceto à galinha!",
            "Chon Chon? Mon Mon Fon Fon !!!",
            "Chuva de meteor...**CORRE, CORRE, TÁ CAINDO!**",
            "Coitado do Pé-Grande. Pegou um resfriado porque só anda descalço...",
            "Combo Triplo vem com refrigerante e fritas?",
            "Como o Mímico venceu o Espadachim? Dando o golpe do baú!",
            "Como se chama um Andarilho com Aumentar Agilidade? Um Correrilho!",
            "Destino nas Cartas! ...Sorte! Saída livre da prisão!",
            "Devagar se vai ao longe... mas demora um tempão! Prefiro usar um teleporte",
            "Dois amigos passeando em Lutie quando um deles exclama: 'HATII!' e o amigo responde: 'Saúde!'",
            "Esses Shuras são bipolares... uma hora estão Zen, outra estão em Fúria!!",
            "Esses dias sonhei com um cavalo estranho, acho que foi um Pesadelo.",
            "Esses dias vi um Sacerdote usar Lex Divina em uma planta e ela virou uma MUDA!",
            "Estava pensando... Metaler é um Rocker que gosta de heavy metal?",
            "Estou pedindo a um tempão e esse Arcebispo não me cura! Que ABsurdo!",
            "Estão chovendo Porings e Lunáticos. E daí?",
            "Eu sou tão popular! Até os monstros vêm a mim!",
            "Falei pra você não tomar aquelas Poções da Recuperação, agora você tá quase repetindo de ano!",
            "HADOUKEN! hummm ainda não....",
            "Hoje é festa lá no PvP! Pode aparecer! Vai ter briga até o amanhecer!",
            "Há, miau miau! Pegadinha de Malangdo!",
            "Ira de...Nossa...esse Thor deve estar MUITO bravo!",
            "Leite acaba muito rápido... deviam vender o longa-vida!",
            "Lá vem o Mestre Taekwon dizer que a culpa é das estrelas!",
            "Magnus Exorcismusmusmusmusmusmus...",
            "Me dê umas bananas! Irei enfrentar várias Cãibras!",
            "Meninos, se continuarem com essa mania de chuva de flechas vão pegar um resfriado perigoso!",
            "Meu prato predileto? Ora, uma omelete com ovo de Andre",
            "Morrer na WoE é igual a consórcio: quando menos espera é contemplado.",
            "No PvP, todo dia é um 7x1 diferente.",
            "Não deixa o bardo morrer! Deixa o bardo tocar! O bardo toca o Bragi! Bragi pra gente spammar!",
            "Não gosto da Ilha das Tartarugas... lá tem muito Agressor...",
            "Não namore uma feiticeira! Quando o amor acaba, ela te desencanta e te tira tudo!",
            "Não sou Atroce, mas sou monstrão! BIRL!!",
            "Não! Barreira de Gelo Não! Eu pedi em cubos!",
            "O Peco Peco chama-se Peco Peco só porque faz Peco Peco? Não entendi...",
            "O Sapo de Roda não lava o pé. Não lava porque não quer... Bom, ele também não mora na lagoa...",
            "O oposto do Metaling é o Pagodeling?",
            "O que não pode faltar nos livros de culinária? Gergeling!",
            "O que o Guardião Real foi fazer no médico? Exame de Toque!!",
            "O que o bongun disse pra Eggyra? Se você continuar de olho na Munak, te transformo em Magnolia!!",
            "O que o morcego disse para o outro? \"Você me parece Familiar.\"",
            "O que um Serial Killer foi fazer de manhã? Matar a fome.",
            "O que um filhote do lobo do deserto falou para o outro???\"au au\"",
            "Odaliscas....Homens são capazes de perder seus Emperiums por elas....",
            "Ohhh não, um esqueleto. Melhor chamar um super-herói!",
            "Olha o churrasco!!! Solta Barreira de Fogo ae Bruxo!",
            "Os Cavaleiros deviam chamar Pecoleiros, porque nenhum deles anda a cavalo!",
            "Os carrinhos dos mercadores deveriam ser maiores para pegarmos uma carona",
            "Os esporos são cogumelos azuis geneticamente alterados.",
            "Ota, ota, ota, o meu forte é a rima!!!",
            "Outro dia encontrei o Rocker. Até agora estou procurando o Pagoder e nada...",
            "Oí, ói, ói, eu sou um super-herói!!!",
            "Pelos poderes de Graysk... Oooops, acho que isso não vai funcionar",
            "Picky são Peco Pecos subdesenvolvidos",
            "Pirulito que bate-bate, pirulito que já bateu, pode não acreditar, mas quem te congelou fui eu!",
            "Poeeeeeeeeiraaaaaa, Poeeeeeeeeiraaaaaaa levantou em Pronteraaaaa.",
            "Por que a professora usou os Óculos Vermelhos? Para vermelhor! Haha!",
            "Por que o Anolian tirou o filhote da escola? Porque ele réptil de ano!",
            "Por que os Mercenários conseguem procurar pedras? Por causa da Perícia em Katar!",
            "Poring parece geleia de morango. Drops, de laranja. Qual será o sabor do Marin?",
            "Poring.. Poporing.. o que vem depois? Popopoporing?!",
            "Porque a Abelha Rainha foi devorada? Porque ela pousou em uma Rosa Selvagem!",
            "Porque o mercador atravessou a ponte de Aldebaran? Ora, pra chegar do outro lado",
            "Po, porque ninguém mata Creamy? Ah, porque Creamy não compensa.",
            "Quais os monstros mais radicais de Rune-Midgard? O Rocker e o Metaller!",
            "Qual a diferença entre vocês e um picolé? O palito!",
            "Qual o monstro que tem problema de hipertensão? o SALgueiro!",
            "Qual é o mais calmo dos orcs? O Zen-orc !!",
            "Que calor! Quem abriu esses Portões do Inferno?",
            "Quero vê-la Sohee quero vê-la mobá quero ver o seu Choko dançar sem parar",
            "Rajada Congelante !! Opa, não posso usar isso... mas funcionou!",
            "Sabe o que a Alice faz quando está com cabelo embaraçado? Aliza.",
            "Sabe o que o Aprendiz rebelde foi fazer no Vulcão de Thor? Fugir de Kasa!",
            "Sabe o que o aprendiz disse por Bafomé? Nada... não deu tempo!!!",
            "Sabe o tipo sanguíneo dos Arcebispos? AB!",
            "Sabe porque a Dona Risadinha vive rindo? Acho que ela também não...",
            "Sabe porque os bruxos gostam de congelar os outros? Porque eles são pé frio.",
            "Sabe porque os lunáticos têm este nome? Porque eles vieram da Lua!",
            "Sabe qual a língua dos Poring? Porês. E dos Poporings? Poporês!",
            "Sabe qual foi a maior criação dos anfíbios? O Sapo de Roda!!!",
            "Sabe qual o único monstro que nunca perde o horário? Oras, o Alarme !!!!",
            "Sabe qual programa de comédia mais famoso de Rune Midgard? Eremes e Venatu!",
            "Sabe qual é a flor favorita da Senhora Orc? A orc-ídea!",
            "Sabe qual é a musa dos Porings? Jennifer Jeloppis",
            "Sapo de Roda~Roda~Roda~Roda~Pé~Pé~Sapo não é peixe~Carangejo peixe é~",
            "Se Alberta não tivesse saída para o mar ela se chamaria Felchada?",
            "Se Poring Noel é o monstro do natal, o da páscoa com certeza seria o Poring Pascoalino!!!",
            "Se o filhote de lobo do deserto fosse filho do Garm, ele nasceria com resfriado.",
            "Se o rei fosse feliz e não triste ele se chamaria Felizlan III?",
            "Se um Arcano usar Cometa e atingir um Cavaleiro Rúnico, o Dragão dele vira fóssil?",
            "Se você usar ressuscitar em um Megalodon ele vira uma Fen?",
            "Será que as Kafras são clones? Todas elas têm a mesma cara?",
            "Será que o irmão mais velho dos Yo-yos é aquele gorila grande do cinema?",
            "Será que o rei se chama Tristan III porque ele é triplamente triste?",
            "Será que os Drops são refrescantes?",
            "Será que os Justiceiros já foram presos? Porque eles têm reação em cadeia!",
            "Será que os Lunáticos têm este nome porque eles vieram da Lua?",
            "Sopro de Terra! Fogo! Vento! Gelo! Coração! Eu sou o Capitão Rúnico!",
            "Sou forte. Tiro 100 do Rocker!!!11!",
            "Tempestade, Temporal, Chuva de Flechas... gostam mesmo de um clima ruim hein, arqueiros?",
            "Trovão de Júpiter... é 110 ou 220v?",
            "Um KS incomoda muita gente. Dois KSs incomodam, incomodam muito mais...",
            "Um poring por dia dá saúde e energia!",
            "Um tronco estéril é aquele que não pode ter filhos.",
            "Vida de Trovador não é fácil, ter que andar na área do Temporal é um risco pra saúde!",
            "Você quer moleza então senta no Poring.",
            "Vou pra Arunafeltz! Tu Veins comigo?",
            "Vou te ensinar a fazer ovo frito... pegue o ovo, jogue na panela, Ifrit!",
            "Por que a Isis foi ao cinema? Para Ver It.",
            "Sabe onde tem muito samba? Nos Salgueiros",
            "Qual poring é gago? O poporing!",
            "Se a Planície de Ida fica em Rachel, onde fica a Planície de Volta?",
            "Por que o Priest é a classe mais democrata? Porque ele Kyrie Eleison",
            "Bahia: Que madrugada?"
    };

}
