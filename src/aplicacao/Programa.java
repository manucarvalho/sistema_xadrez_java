package aplicacao;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import xadrez.ExcecaoXadrez;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;

public class Programa {

	public static void main(String[] args) {
		
		Scanner ler = new Scanner(System.in);

		PartidaXadrez partidaXadrez = new PartidaXadrez();
		List<PecaXadrez> pecasCapturadas = new ArrayList<>();
		
		while (partidaXadrez.isChequeMate() == false) {
			try {
				UI.limparTela();
				UI.imprimirPartida(partidaXadrez, pecasCapturadas);
				System.out.println();
				System.out.println("Origem: ");
				PosicaoXadrez origem = UI.lerPosicaoXadrez(ler);
				boolean[][] mat = partidaXadrez.movimentosPossiveis(origem);
				UI.limparTela();
				UI.imprimirTabuleiro(partidaXadrez.getPecas(), mat);
				System.out.println();
				System.out.println("Destino: ");
				PosicaoXadrez destino = UI.lerPosicaoXadrez(ler);
				
				PecaXadrez pecaCapturada = partidaXadrez.fazerMovimentoXadrez(origem, destino);
				if (pecaCapturada != null) {
					pecasCapturadas.add(pecaCapturada);
				}
			}
			catch (ExcecaoXadrez e) {
				System.out.println(e.getMessage());
				ler.nextLine();
			}
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				ler.nextLine();
			}
		}
		UI.limparTela();
		UI.imprimirPartida(partidaXadrez, pecasCapturadas);
	}

}
