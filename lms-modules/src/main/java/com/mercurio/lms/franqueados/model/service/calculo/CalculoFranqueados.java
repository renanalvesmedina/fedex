package com.mercurio.lms.franqueados.model.service.calculo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.adsm.framework.BusinessException;

public class CalculoFranqueados {

	private Queue<DocumentoFranqueadoDTO> listConhecimentoFranqueado;
	private Queue<Serializable> documentos;
	private CalculoFranqueadoParametros calculoFranqueadoParametros;

	public CalculoFranqueados(CalculoFranqueadoParametros calculoFranqueadoParametros) {
		super();
		this.calculoFranqueadoParametros = calculoFranqueadoParametros;
		this.listConhecimentoFranqueado = new LinkedList<DocumentoFranqueadoDTO>();
		this.documentos = new LinkedList<Serializable>();
	}

	private void validate() {
		if (calculoFranqueadoParametros.getRepasseFranqueado() == null) {
			throw new BusinessException("LMS-45016", new Object[] { " de franqueados REPASSE_FRQ_SEQ " });
		}
	}

	public void executarCalculo() {
		validate();

		while (!listConhecimentoFranqueado.isEmpty()) {

			DocumentoFranqueadoDTO documento = listConhecimentoFranqueado.poll();

			ICalculoFranquiado calculoFranqueado = documento.createCalculo();
			if (calculoFranqueado != null) {
				calculoFranqueado.setDocumento(documento);
				calculoFranqueado.setIdFranquia(this.calculoFranqueadoParametros.getIdFranquia());
				calculoFranqueado.setParametrosFranqueado(calculoFranqueadoParametros);
				calculoFranqueado.executarCalculo();
				Serializable o = calculoFranqueado.getDocumentoFranqueado();
				if (o != null) {
					documentos.add(o);
				}
				calculoFranqueado = null;
				documento = null;
			}
		}
	}

	/* ==================== Set parametrizações====================== */

	public void addListConhecimentoFranqueado(List<? extends DocumentoFranqueadoDTO> listConhecimentoFranqueado) {
		if (CollectionUtils.isNotEmpty(listConhecimentoFranqueado)) {
			this.listConhecimentoFranqueado.addAll(listConhecimentoFranqueado);
			listConhecimentoFranqueado.clear();
		}
	}

	public Queue<Serializable> getDocumentos() {
		return documentos;
	}

	public void setCalculoFranqueadoParametros(CalculoFranqueadoParametros calculoFranqueadoParametros) {
		this.calculoFranqueadoParametros = calculoFranqueadoParametros;
	}

	public Long getIdFranquia() {
		return calculoFranqueadoParametros.getIdFranquia();
	}

	public CalculoFranqueadoParametros getCalculoFranqueadoParametros() {
		return calculoFranqueadoParametros;
	}

	public void clearDoclumentos() {
		listConhecimentoFranqueado = new LinkedList<DocumentoFranqueadoDTO>();
		documentos = new LinkedList<Serializable>();
	}

}