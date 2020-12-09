package xadrez_pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;

public class Rei extends PecaXadrez{
	
	private PartidaXadrez partida;

	public Rei(Tabuleiro tabuleiro, Cor cor, PartidaXadrez partida) {
		super(tabuleiro, cor);
		this.partida = partida;
	
	}

	@Override
	public String toString() {
		return "R";
		
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinha()][getTabuleiro().getColuna()];
		Posicao p = new Posicao(0,0);
		// acima
		p.setValor(posicao.getLinha() - 1, posicao.getColuna());
		if (getTabuleiro().existePosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		// abaixo
		p.setValor(posicao.getLinha() + 1, posicao.getColuna());
		if (getTabuleiro().existePosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		// esquerda
		p.setValor(posicao.getLinha(), posicao.getColuna() - 1);
		if (getTabuleiro().existePosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		// direita
		p.setValor(posicao.getLinha(), posicao.getColuna() + 1);
		if (getTabuleiro().existePosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}		
	    // no
		p.setValor(posicao.getLinha() - 1, posicao.getColuna() - 1);
		if (getTabuleiro().existePosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}		
		// ne
		p.setValor(posicao.getLinha() - 1, posicao.getColuna() + 1);
		if (getTabuleiro().existePosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		// so
		p.setValor(posicao.getLinha() + 1, posicao.getColuna() - 1);
		if (getTabuleiro().existePosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		// se
		p.setValor(posicao.getLinha() + 1, posicao.getColuna() + 1);
		if (getTabuleiro().existePosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//roque
		if (getContadorMovimento() == 0 && ! partida.isCheque()) {
			//lado do rei
			Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 3);
			if (testarTorreRoque(p1)) {
				Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				Posicao p3 = new Posicao(posicao.getLinha(), posicao.getColuna() + 2);
				if (getTabuleiro().peca(p2) == null && getTabuleiro().peca(p3) == null) {
					mat[posicao.getLinha()][posicao.getColuna() + 2] = true;
				}
			}
			//lado da rainha
			Posicao p4 = new Posicao(posicao.getLinha(), posicao.getColuna() - 4);
			if (testarTorreRoque(p4)) {
				Posicao p5 = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				Posicao p6 = new Posicao(posicao.getLinha(), posicao.getColuna() - 2);
				Posicao p7 = new Posicao(posicao.getLinha(), posicao.getColuna() - 3);
				if (getTabuleiro().peca(p5) == null && getTabuleiro().peca(p6) == null && getTabuleiro().peca(p7) == null) {
					mat[posicao.getLinha()][posicao.getColuna() - 2] = true;
				}
			}
			
		}
		
		return mat;
	}
	
	private boolean podeMover(Posicao posicao) {
		PecaXadrez pecaXadrez = (PecaXadrez) getTabuleiro().peca(posicao);
		if (pecaXadrez == null || pecaXadrez.getCor() != getCor()) {
			return true;
		}
		return false;
	}
	
	private boolean testarTorreRoque(Posicao posicao) {
		PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
		if (p != null && p instanceof Torre && p.getCor() == getCor() && p.getContadorMovimento() == 0) {
			return true;
		}
		return false;
	}
	
}
