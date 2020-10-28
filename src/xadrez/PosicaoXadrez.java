package xadrez;

import tabuleiro.Posicao;

public class PosicaoXadrez {
	
	private int linha;
	private char coluna;
	
	public PosicaoXadrez() {
		
	}

	public PosicaoXadrez( char coluna, int linha) {
		if (linha < 1 || linha >8 || coluna < 'a' || coluna > 'h') {
			throw new ExcecaoXadrez("Erro na posição de xadrez, deve estar entre a1 e h8.");
		}
		this.linha = linha;
		this.coluna = coluna;
	}

	public int getLinha() {
		return linha;
	}

	public char getColuna() {
		return coluna;
	}

	protected Posicao paraPosicao() {
		return new Posicao(8 - linha, coluna - 'a');
	}
	
	protected static PosicaoXadrez daPosicao(Posicao posicao) {
		return new PosicaoXadrez((char)('a' + posicao.getColuna()), 8 - posicao.getLinha());
	}
	
	
	public String toString() {
		return "" + coluna + linha;
	}

}
