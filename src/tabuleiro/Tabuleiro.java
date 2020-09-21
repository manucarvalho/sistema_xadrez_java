package tabuleiro;

public class Tabuleiro {
	
	private int linha;
	private int coluna;
	private Peca[][] pecas;
		
	public Tabuleiro(int linha, int coluna) {
		if (linha < 1 || coluna < 1) {
			throw new ExcecaoTabuleiro("Erro na criação do tabuleiro: é preciso ter pelo menos 1 linha e 1 coluna.");
		}
		this.linha = linha;
		this.coluna = coluna;
		pecas = new Peca[linha][coluna];
	}
	
	public int getLinha() {
		return linha;
	}
	
	public int getColuna() {
		return coluna;
	}
	
	public Peca peca(int linha, int coluna) {
		if (existePosicao(linha, coluna) == false) {
			throw new ExcecaoTabuleiro("Posição fora do tabuleiro");
		}
		return pecas[linha][coluna];
	}
	
	public Peca peca(Posicao posicao) {
		if (existePosicao(posicao) == false) {
			throw new ExcecaoTabuleiro("Posição fora do tabuleiro");
		}
		return pecas[posicao.getLinha()][posicao.getColuna()];
	}
	
	public void posicionarPeca(Peca peca, Posicao posicao) {
		if (temUmaPeca(posicao) == true) {
			throw new ExcecaoTabuleiro("Já existe uma peça na posição " + posicao);
		}
		pecas[posicao.getLinha()][posicao.getColuna()] = peca;
		peca.posicao = posicao;
	}
	
	public Peca removerPeca(Posicao posicao) {
		if (existePosicao(posicao) == false) {
			throw new ExcecaoTabuleiro("Posição fora do tabuleiro");
		}
		if (peca(posicao) == null) {
			return null;
		}
		Peca aux = peca(posicao);
		aux.posicao = null;
		pecas[posicao.getLinha()][posicao.getColuna()] = null;
		return aux;
	}
	
	private boolean existePosicao(int linha, int coluna) {
		return linha >= 0 && linha < this.linha && coluna >= 0 && coluna < this.coluna;
	}
	
	public boolean existePosicao(Posicao posicao) {
		return existePosicao(posicao.getLinha(), posicao.getColuna());
	}
	
	public boolean temUmaPeca(Posicao posicao) {
		if (existePosicao(posicao) == false) {
			throw new ExcecaoTabuleiro("Posição fora do tabuleiro");
		}
		return peca(posicao) != null;
	}
	
	
}
