package com.mercurio.lms.contasreceber.model.service;

import java.util.Comparator;
import java.util.Formatter;

import com.mercurio.lms.contasreceber.model.ItemCobranca;

/**
 * Cria um comparator para fazer ordenação: SiglaFilial / NrFatura
 */
public class OrdenaFaturaItemCobranca implements Comparator<ItemCobranca> {

	public int compare(ItemCobranca o1, ItemCobranca o2) {
		
		// Coloca zeros a esquerda (tamanho 15) no nrFatura
		String formataNrFatura01 = new Formatter().format("%1$015d", o1.getFatura().getNrFatura()).toString();
		String formataNrFatura02 = new Formatter().format("%1$015d", o2.getFatura().getNrFatura()).toString();
		
		String ordemO1 = o1.getFatura().getFilialByIdFilial().getSgFilial() + formataNrFatura01; 
		String ordemO2 = o2.getFatura().getFilialByIdFilial().getSgFilial() + formataNrFatura02;
		 
		return ordemO1.compareTo(ordemO2);
	}
}
