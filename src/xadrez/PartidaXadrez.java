package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez_pecas.Rei;
import xadrez_pecas.Torre;

public class PartidaXadrez {

	private Tabuleiro tabuleiro;
	private int turno;
	private Cor jogadorAtual;
		
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
}

