package com.example.app_dorothy.adapter;
import java.util.Random;

public class TextoAdapter {

    static int  selecionado;

    static String[] arr = {
            "Na rua esteja sempre alerta, observe o ambiente e as pessoas a sua volta. Demonstre estar atento, para intimidar possíveis agressores.",
            "Ande acompanhado e acompanhe seus amigos, especialmente à noite.",
            "Ao caminhar, prefira andar mais próximo ao meio da rua, evite os cantos e lugares mais escuros",
            "Evitar permanecer desnecessariamente em lugares abertos",
            "Identifique em seus trajetos cotidianos possíveis pontos de vulnerabilidade.",
            "Se sentir que alguém o segue, tente desviar do seu caminho original e evite entrar em casa; nessas horas é melhor entrar num estabelecimento comercial ou na casa de um vizinho.",
            "Evite entrar em embates ou reagir a provocações, xingamentos e insultos.",
            "Se sentir que alguém o segue, não entre em vias estreitas e/ou pouco iluminadas e, se possível, saia da calçada.",
            "Evite fone de ouvido com volume extremo, para não se distrair onde tiver aglomeração de pessoas ou em transportes públicos, especialmente se estive só.",
            "Caso presencie alguma situação de violência, tente prestar apoio, desde que sua segurança não seja ameaçada, procure alertar outras pessoas ao redor.",
            "Ajude quem estiver em perigo, mas tente fazê-lo na companhia de uma ou mais pessoas.",
            "Em alguns casos, ligar a câmera do celular, para registrar o que está acontecendo, também pode ser uma forma de intimidar o agressor.",
            "Tente confundir o agressor para evitar a agressão, respondendo coisas desconexas.",
            "Caso esteja em risco, tente chamar a atenção de outras pessoas.",
            "Caso passe por alguma situação de violência, assalto, discussão ou briga, procure uma delegacia e faça o registro, sempre!",
            "O contato físico deve ser a última opção, esteja atento para objetos comuns (como mochilas, livros e até um casaco) que podem evitar um golpe ou minimizar o dano que ele possa causar.",
            "Se possível filme ou peça para alguém filmar a situação, facilitando a identificação dos agressores.",
            "Se possível filme ou peça para alguém filmar a situação, facilitando a identificação dos agressores.",
            "Troque de assento ou de vagão se perceber que alguém está tentando tocar em você ou em seus pertences.",
            "Evite ficar só em pontos de ônibus, especialmente à noite. Procure um ponto mais movimentado e iluminado sempre que possível.",
            "No transporte procure sentar no corredor, para ter controle de quem senta ao seu lado ou caso precise trocar de lugar."

    };

    public static String texto_random() {

        Random r = new Random();
        int aleatorio = r.nextInt(arr.length - 1);
        selecionado = aleatorio;
        return arr[aleatorio];
    }

    public  static String Proximo(){
        selecionado ++;
        if(selecionado >= arr.length ){
            selecionado = 0;
        }
        return  arr[selecionado];
    }

    public static  String Voltar(){
        selecionado --;
        if(selecionado <0 ){
            selecionado = arr.length-1;
        }
        return  arr[selecionado];
    }
}
