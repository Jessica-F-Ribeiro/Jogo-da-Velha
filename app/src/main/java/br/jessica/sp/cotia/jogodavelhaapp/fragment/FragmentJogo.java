package br.jessica.sp.cotia.jogodavelhaapp.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

import br.jessica.sp.cotia.jogodavelhaapp.R;
import br.jessica.sp.cotia.jogodavelhaapp.databinding.FragmentJogoBinding;
import br.jessica.sp.cotia.jogodavelhaapp.util.PrefsUtil;

public class FragmentJogo extends Fragment {

    // variavel para acessar os elementos na view
    private FragmentJogoBinding binding;
    // vetor para agrupar os botões
    private Button[] botoes;
    // variavel que representa o tabuleiro
    private String[][] tabuleiro;
    // variavel para os simbolos
    private String simbJog1, simbJog2, simbolo;
    // variavel random para sortear quem começa
    private Random random;
    // variavel para contar o numero de jogadas
    private int numJogadas = 0;
    // variaveis para o placar
    private int placarJog1 = 0, placarJog2 = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // habilita o menu neste fragment
        setHasOptionsMenu(true);
        // instancia o binding
        binding = FragmentJogoBinding.inflate(inflater, container, false);

        // instancia o vetor
        botoes = new Button[9];
        //agrupa os Botões no vetor
        botoes[0] = binding.bt00;
        botoes[1] = binding.bt01;
        botoes[2] = binding.bt02;
        botoes[3] = binding.bt10;
        botoes[4] = binding.bt11;
        botoes[5] = binding.bt12;
        botoes[6] = binding.bt20;
        botoes[7] = binding.bt21;
        botoes[8] = binding.bt22;

        // associa o listener aos botões
        for (Button bt : botoes){
            bt.setOnClickListener(listenerBotoes);
        }
        // inicializa o tabuleiro
        tabuleiro = new String[3][3];

        // preencher o tabuleiro com String vazia = ""
        for (String[] vetor : tabuleiro){
            Arrays.fill(vetor, "");
        }

        //instancia o Random
        random = new Random();

        // define os simbolos dos jogadores
        simbJog1 = PrefsUtil.getSimboloJog1(getContext());
        simbJog2 = PrefsUtil.getSimboloJog2(getContext());

        // altera o simbolo do jogador no placar
        binding.tvJog1.setText(getResources().getString(R.string.jogador1, simbJog1));
        binding.tvJog2.setText(getResources().getString(R.string.jogador2, simbJog2));

        // chama o metodo - sorteia quem inicia o jogo
        sorteia();

        //atualizaVez
        atualizaVez();

        // retorna a view do Fragment
        return binding.getRoot();
    }

    private void sorteia(){
        // caso o  random gere um valor verdadeiro, jogador 1 começa
        if (random.nextBoolean()){
            simbolo = simbJog1;
        }else{
            simbolo = simbJog2;
        }
    }

    private void atualizaVez(){
        // verifica de quem é a vez e acende o placar o jogador em questao
        if (simbolo.equals(simbJog1)){
            binding.linearJog1.setBackgroundColor(getResources().getColor(R.color.claro));
            binding.linearJog2.setBackgroundColor(getResources().getColor(R.color.white));
        }else{
            binding.linearJog2.setBackgroundColor(getResources().getColor(R.color.claro));
            binding.linearJog1.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    private void resetar(){
        // zera o tabuleiro
        for (String[] vetor : tabuleiro){
            Arrays.fill(vetor, "");
        }
        // percorre o vetor de botoes resetando-os
        for (Button botao : botoes){
            botao.setBackgroundColor(getResources().getColor(R.color.purple_500));
            botao.setText("");
            botao.setClickable(true);
        }
        // sorteia quem ira iniciar o proximo jogo
        sorteia();
        // atualiza a vez no placar
        atualizaVez();
        // zerar o numero de jogadas
        numJogadas = 0;
    }

    private boolean venceu(){
        // verifica se venceu nas linhas
        for (int i = 0; i < 3; i++){
            if (tabuleiro[i][0].equals(simbolo)
                    && tabuleiro[i][1].equals(simbolo)
                    && tabuleiro[i][2].equals(simbolo)){
                return true;
            }
        }

        // verifica se venceu na coluna
        for (int i = 0; i < 3; i++){
            if (tabuleiro[0][i].equals(simbolo)
                    && tabuleiro[1][i].equals(simbolo)
                    && tabuleiro[2][i].equals(simbolo)){
                return true;
            }
        }
        // verifica se venceu na diagonal
        if (tabuleiro[0][0].equals(simbolo)
                && tabuleiro[1][1].equals(simbolo)
                && tabuleiro[2][2].equals(simbolo)){
            return true;
        }
        if (tabuleiro[0][2].equals(simbolo)
                && tabuleiro[1][1].equals(simbolo)
                && tabuleiro[2][0].equals(simbolo)){
            return true;
        }
        return false;
    }

    private void atualizarPlacar(){
        binding.tvPlacarJog1.setText(placarJog1+"");
        binding.tvPlacarJog2.setText(placarJog2+"");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // verifica qual botao foi clicado no menu
        switch (item.getItemId()){
            // caso tenha clicado no reset
            case R.id.menu_resetar:
                placarJog1 = 0;
                placarJog2 = 0;
                resetar();
                atualizarPlacar();
                break;
                // caso tenha clicado no preferences
            case R.id.menu_prefs:
                NavHostFragment.findNavController(FragmentJogo.this).
                        navigate(R.id.action_fragmentJogo_to_prefFragment);
                break;
        }
        return true;
    }

    private View.OnClickListener listenerBotoes = btPress -> {
        // incrementa as jogadas
        numJogadas++;

        //pega o nome do botão
        String nomeBotao = getContext().getResources().getResourceName(btPress.getId());
        //extrai os dois ultimos caracteries do nomeBotao
        String posicao = nomeBotao.substring(nomeBotao.length()-2);

        // extrai a posicao em linha e coluna
        int linha = Character.getNumericValue(posicao.charAt(0));
        int coluna = Character.getNumericValue(posicao.charAt(1));

        // marca no tabuleiro o simbolo que foi jogado
        tabuleiro[linha][coluna] = simbolo;

        // faz um casting de View para BUtton
        Button botao = (Button) btPress;
        // trocar o texto do botao que foi clicado
        botao.setText(simbolo);

        // desabilitar o botao
        botao.setClickable(false);
        // troca o background do botao
        botao.setBackgroundColor(Color.rgb(178, 235, 242));

        //verifica se venceu
        if (numJogadas >= 5 && venceu()){
            // exibe um toast informando que o vencedor venceu
            Toast.makeText(getContext(),R.string.venceu, Toast.LENGTH_LONG).show();
            // verifica quem venceu e atualiza o placarc
            if (simbolo.equals(simbJog1)){
                placarJog1++;
            }else{
                placarJog2++;
            }
            // atualiza o placar
            atualizarPlacar();
            // reseta o tabuleiro
            resetar();
        }else if (numJogadas == 9){
            // exibe um toast informando que deu velha
            Toast.makeText(getContext(),R.string.deuvelha, Toast.LENGTH_LONG).show();
            // reseta o tabuleiro
            resetar();
        }else{
            // inverter a vez
            simbolo = simbolo.equals(simbJog1) ? simbJog2 : simbJog1;
            // atualiza a vez
            atualizaVez();
        }
    };
}