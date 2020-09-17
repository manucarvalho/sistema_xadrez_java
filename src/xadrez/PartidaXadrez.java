package xadrez;

import tabuleiro.Tabuleiro;
import xadrez_pecas.Rei;
import xadrez_pecas.Torre;

public class PartidaXadrez {

	private Tabuleiro tabuleiro;
	
	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8,8);
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
	}
}
