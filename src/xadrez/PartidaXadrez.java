package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez_pecas.Rei;
import xadrez_pecas.Torre;

public class PartidaXadrez {

	private Tabuleiro tabuleiro;
	private int turno;
	private Cor jogadorAtual;
	private List<Peca> pecasCapturadas = new ArrayList<>();
	private List<Peca> pecasNoTabuleiro = new ArrayList<>();
	private boolean cheque; 
			
	public boolean isCheque() {
		return cheque;
	}

	public int getTurno() {
		return turno;
	}

	public Cor getJogadorAtual() {
		return jogadorAtual;
	}

	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8,8);
		turno = 1;
		jogadorAtual = Cor.BRANCO;
		configuracaoInicial();
	}
	
	public PecaXadrez[][] getPecas() {
		PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinha()][tabuleiro.getColuna()];
		for (int i=0; i<tabuleiro.getLinha(); i++) {
			for (int j=0; j<tabuleiro.getColuna(); j++) {
				 mat [ i ] [ j ] = (PecaXadrez) tabuleiro.peca( i ,j );
			}
		}
		return mat;
	}
	
	private void posicionarNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.posicionarPeca(peca, new PosicaoXadrez(coluna,linha).paraPosicao());
		pecasNoTabuleiro.add(peca);
	}
	
	private void configuracaoInicial() {
		posicionarNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('d', 1, new Rei(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('d', 8, new Rei(tabuleiro, Cor.PRETO));
	}
	
	public PecaXadrez fazerMovimentoXadrez(PosicaoXadrez posicaoOrigem, PosicaoXadrez posicaoDestino) {
		Posicao origem = posicaoOrigem.paraPosicao();
		Posicao destino = posicaoDestino.paraPosicao();
		validarPosicaoOrigem(origem);
		validarPosicaoDestino(origem, destino);
		Peca pecaCapturada = fazerMovimento(origem, destino);
		if (verificarCheque(jogadorAtual) == true) {
			desfazerMovimento(origem, destino, pecaCapturada);
			throw new ExcecaoXadrez("Você não pode se colocar em cheque.");
		}
		cheque = (verificarCheque(oponente(jogadorAtual))) ? true : false;
		proximoTurno();
		return (PecaXadrez) pecaCapturada;
	}
	
	private void validarPosicaoOrigem(Posicao posicao) {
		if (tabuleiro.temUmaPeca(posicao) == false) {
			throw new ExcecaoXadrez("Não tem peça na posição de origem");
		}
		if (tabuleiro.peca(posicao).existeAlgumMovimentoPossivel() == false) {
			throw new ExcecaoXadrez("Não existe movimentso possíveis para a peça escolhida");
		}
		if (jogadorAtual != ((PecaXadrez)tabuleiro.peca(posicao)).getCor()) {
			throw new ExcecaoXadrez("A peça escolhida não é sua");
		}
	}
	
	private Peca fazerMovimento(Posicao origem, Posicao destino) {
		Peca pecaOrigem = tabuleiro.removerPeca(origem);
		Peca pecaCapturada = tabuleiro.removerPeca(destino);
		tabuleiro.posicionarPeca(pecaOrigem, destino);
		if (pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}
		return pecaCapturada;
	}
	
	private void validarPosicaoDestino(Posicao origem, Posicao destino) {
	 	if (tabuleiro.peca(origem).movimentoPossivel(destino) == false) {
	 		throw new ExcecaoXadrez("A peça escolhida não pode ser movida para a posição destino.");
	 	}
	}
	
	public boolean[][] movimentosPossiveis(PosicaoXadrez origem){
		Posicao posicao = origem.paraPosicao();
		validarPosicaoOrigem(posicao);
		return tabuleiro.peca(posicao).movimentosPossiveis();
	}
	
	private void proximoTurno() {
		turno++;
		jogadorAtual = (getJogadorAtual() == Cor.BRANCO) ? Cor.PRETO :  Cor.BRANCO;
	}
	
	public void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
		Peca p = tabuleiro.removerPeca(destino);
		tabuleiro.posicionarPeca(p, origem);
		if (pecaCapturada != null) {
			tabuleiro.posicionarPeca(pecaCapturada, destino);
			pecasCapturadas.remove(pecaCapturada);
			pecasNoTabuleiro.add(pecaCapturada);			
		}
	}
	
	private Cor oponente(Cor oponente) {
		return (oponente == Cor.BRANCO) ? Cor.PRETO :  Cor.BRANCO;
	}
	
	private PecaXadrez rei(Cor cor) {
		List<Peca> lista = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == cor).collect(Collectors.toList());
		for (Peca p : lista) {
			if (p instanceof Rei) {
				return (PecaXadrez)p;
			}
		}
		throw new IllegalStateException("Não tem um rei " + cor + " no tabuleiro.");
	}
	
	private boolean verificarCheque(Cor cor) {
		Posicao posicaoRei = rei(cor).getPosicaoXadrez().paraPosicao();
		List<Peca> pecasOponente = pecasNoTabuleiro.stream().filter(x->((PecaXadrez)x).getCor() == oponente(cor)).collect(Collectors.toList());
		for (Peca p : pecasOponente) {
			boolean[][] mat = p.movimentosPossiveis();
			if (mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
				return true;
			}
		}
		return false;
	}
}

