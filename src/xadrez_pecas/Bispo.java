package xadrez_pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Bispo extends PecaXadrez{

	public Bispo(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}
	
	@Override
	public String toString() {
		return "B";
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinha()][getTabuleiro().getColuna()];
		
		Posicao p = new Posicao(0,0);
		
		// nw
		p.setValor(posicao.getLinha() - 1, posicao.getColuna() - 1);
		while (getTabuleiro().existePosicao(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValor(posicao.getLinha() - 1, posicao.getColuna() - 1);
		}
		if (getTabuleiro().existePosicao(p) && existePecaOponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
				
		// ne
		p.setValor(posicao.getLinha() - 1, posicao.getColuna() + 1);
		while (getTabuleiro().existePosicao(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValor(posicao.getLinha() - 1, posicao.getColuna() + 1);
		}
		if (getTabuleiro().existePosicao(p) && existePecaOponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
				
		// se
		p.setValor(posicao.getLinha() + 1, posicao.getColuna() + 1);
		while (getTabuleiro().existePosicao(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValor(posicao.getLinha() + 1, posicao.getColuna() + 1);
		}
		if (getTabuleiro().existePosicao(p) && existePecaOponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
				
		// sw
		p.setValor(posicao.getLinha() + 1, posicao.getColuna() - 1);
		while (getTabuleiro().existePosicao(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValor(posicao.getLinha() + 1, posicao.getColuna() - 1);
		}
		if (getTabuleiro().existePosicao(p) && existePecaOponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}		
		
		return mat;
	}

}
