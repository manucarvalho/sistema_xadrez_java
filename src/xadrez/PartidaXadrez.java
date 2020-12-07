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
	private boolean chequeMate;
			
	public boolean isCheque() {
		return cheque;
	}
	
	public boolean isChequeMate() {
		return chequeMate;
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
		posicionarNovaPeca('h', 7, new Torre(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('d', 1, new Torre(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('b', 8, new Torre(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('a', 8, new Rei(tabuleiro, Cor.PRETO));
	}
	
	public PecaXadrez fazerMovimentoXadrez(PosicaoXadrez posicaoOrigem, PosicaoXadrez posicaoDestino) {
		Posicao origem = posicaoOrigem.paraPosicao();
		Posicao destino = posicaoDestino.paraPosicao();
		validarPosicaoOrigem(origem);
		validarPosicaoDestino(origem, destino);
		Peca pecaCapturada = fazerMovimento(origem, destino);
		if (verificarCheque(jogadorAtual) == true) {
			desfazerMovimento(origem, destino, pecaCapturada);
			throw new ExcecaoXadrez("Voc� n�o pode se colocar em cheque.");
		}
		cheque = (verificarCheque(oponente(jogadorAtual))) ? true : false;
		if (verificarChequeMate(oponente(jogadorAtual)) == true){
			chequeMate = true;
		} else {
			proximoTurno();
		}		
		return (PecaXadrez) pecaCapturada;
	}
	
	private void validarPosicaoOrigem(Posicao posicao) {
		if (tabuleiro.temUmaPeca(posicao) == false) {
			throw new ExcecaoXadrez("N�o tem pe�a na posi��o de origem");
		}
		if (tabuleiro.peca(posicao).existeAlgumMovimentoPossivel() == false) {
			throw new ExcecaoXadrez("N�o existe movimentso poss�veis para a pe�a escolhida");
		}
		if (jogadorAtual != ((PecaXadrez)tabuleiro.peca(posicao)).getCor()) {
			throw new ExcecaoXadrez("A pe�a escolhida n�o � sua");
		}
	}
	
	private Peca fazerMovimento(Posicao origem, Posicao destino) {
		PecaXadrez pecaOrigem = (PecaXadrez)tabuleiro.removerPeca(origem);
		pecaOrigem.aumentarContadorMov();
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
	 		throw new ExcecaoXadrez("A pe�a escolhida n�o pode ser movida para a posi��o destino.");
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
		PecaXadrez p = (PecaXadrez)tabuleiro.removerPeca(destino);
		p.diminuirContadorMov();
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
		throw new IllegalStateException("N�o tem um rei " + cor + " no tabuleiro.");
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
	
	private boolean verificarChequeMate(Cor cor) {
		if (verificarCheque(cor) == false) {
			return false;
		}
		List<Peca> pecas = pecasNoTabuleiro.stream().filter(x->((PecaXadrez)x).getCor() == cor).collect(Collectors.toList());
		for (Peca p : pecas) {
			boolean[][] mat = p.movimentosPossiveis();
			for (int i=0; i<tabuleiro.getLinha();i++) {
				for (int j=0; j<tabuleiro.getColuna();j++) {
					if (mat[i][j]) {
						Posicao origem = ((PecaXadrez)p).getPosicaoXadrez().paraPosicao();
						Posicao destino = new Posicao(i,j);
						Peca capturada = fazerMovimento(origem,destino);
						boolean testeCheque = verificarCheque(cor);
						desfazerMovimento(origem, destino, capturada);
						if (testeCheque == false) {
							return false;
						}						
					}
				}					
			}
		}
		return true;
	}
}

