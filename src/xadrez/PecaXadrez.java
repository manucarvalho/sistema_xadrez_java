package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public abstract class PecaXadrez extends Peca{
	
	private Cor cor;
	private int contadorMovimento;
	
	public int getContadorMovimento() {
		return contadorMovimento;
	}
		
	public PecaXadrez(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}	
	
	public PosicaoXadrez getPosicaoXadrez() {
		return PosicaoXadrez.daPosicao(posicao);
	}

	protected boolean existePecaOponente(Posicao posicao) {
		PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
		if (p != null && p.getCor() != cor) {
			return true;
		}
		return false;
	}
	
	public void aumentarContadorMov() {
		contadorMovimento++;
	}
	
	public void diminuirContadorMov() {
		contadorMovimento--;
	}
	
}
