package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez_pecas.Bispo;
import xadrez_pecas.Cavalo;
import xadrez_pecas.Peao;
import xadrez_pecas.Rainha;
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
		posicionarNovaPeca('d', 1, new Rainha(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO, this));
		posicionarNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO, this));
		posicionarNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETO));
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
		if (verificarChequeMate(oponente(jogadorAtual)) == true){
			chequeMate = true;
		} else {
			proximoTurno();
		}		
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
		PecaXadrez pecaOrigem = (PecaXadrez)tabuleiro.removerPeca(origem);
		pecaOrigem.aumentarContadorMov();
		Peca pecaCapturada = tabuleiro.removerPeca(destino);
		tabuleiro.posicionarPeca(pecaOrigem, destino);
		if (pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}
		// roque lado do rei
		if (pecaOrigem instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removerPeca(origemT);
			tabuleiro.posicionarPeca(torre, destinoT);
			torre.aumentarContadorMov();
		}
		// roque lado do rei
		if (pecaOrigem instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removerPeca(origemT);
			tabuleiro.posicionarPeca(torre, destinoT);
			torre.aumentarContadorMov();
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
		PecaXadrez p = (PecaXadrez)tabuleiro.removerPeca(destino);
		p.diminuirContadorMov();
		tabuleiro.posicionarPeca(p, origem);
		if (pecaCapturada != null) {
			tabuleiro.posicionarPeca(pecaCapturada, destino);
			pecasCapturadas.remove(pecaCapturada);
			pecasNoTabuleiro.add(pecaCapturada);			
		}
		// roque lado do rei
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removerPeca(destinoT);
			tabuleiro.posicionarPeca(torre, origemT);
			torre.diminuirContadorMov();
		}
		// roque lado do rei
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removerPeca(destinoT);
			tabuleiro.posicionarPeca(torre, origemT);
			torre.diminuirContadorMov();
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

