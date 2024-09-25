package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.RelacaoPagtoParcial;
import com.mercurio.lms.contasreceber.model.dao.RelacaoPagtoParcialDAO;

public class RelacaoPagtoParcialService extends CrudService<RelacaoPagtoParcial, Long> {

	public void setRelacaoPagtoParcialDAO(RelacaoPagtoParcialDAO dao) {
		setDao(dao);
	}

	private RelacaoPagtoParcialDAO getRelacaoPagtoParcialDAO() {
		return (RelacaoPagtoParcialDAO) getDao();
	}

	public boolean validateRelacaoPagamentoParcial(Long idFatura) {
		return getRelacaoPagtoParcialDAO().validateRelacaoPagamentoParcial(idFatura);
	}

	public List<RelacaoPagtoParcial> findByIdFatura(Long idFatura) {
		return getRelacaoPagtoParcialDAO().findByIdFatura(idFatura);	
	}
	
	public BigDecimal findByIdFaturaTotalvlPagamento(Long idFatura) {
		return getRelacaoPagtoParcialDAO().findByIdFaturaTotalvlPagamento(idFatura);	
	}
	
	
	public List findRecebimentosParciais(TypedFlatMap criteria) {
		List ret = new ArrayList();
		List<RelacaoPagtoParcial> relacoes = findByIdFatura(criteria.getLong("idFatura"));
		for (RelacaoPagtoParcial relacaoPagtoParcial : relacoes) {
			TypedFlatMap relacao = new TypedFlatMap();
			relacao.put("relacao.sgFilial", relacaoPagtoParcial.getFilial().getSgFilial());
			relacao.put("relacao.nrRelacao", relacaoPagtoParcial.getNrRelacao());
			relacao.put("relacao.vlPagamento", relacaoPagtoParcial.getVlPagamento());
			relacao.put("relacao.vlJuros", relacaoPagtoParcial.getVlJuros());
			relacao.put("relacao.vlDesconto", relacaoPagtoParcial.getVlDesconto());
			relacao.put("relacao.dtPagamento", relacaoPagtoParcial.getDtPagamento());
			relacao.put("relacao.nmBanco", relacaoPagtoParcial.getBanco().getNmBanco());
			relacao.put("relacao.dtEnvioContabilidade", relacaoPagtoParcial.getDtEnvioContabilidade());
			relacao.put("relacao.nrLoteContabilJDE", relacaoPagtoParcial.getNrLoteContabilJDE());
			relacao.put("relacao.obRelacao", relacaoPagtoParcial.getObRelacao());
			ret.add(relacao);
		}
		if (relacoes.isEmpty()) {
			return Collections.EMPTY_LIST;
		} else {
			return ret;
		}
	}

	/**
	 * LMS-6106 - Busca <tt>RelacaoPagtoParcial</tt> para a fatura relacionada a
	 * um <tt>Boleto</tt> especificado pelo id.
	 * 
	 * @param idBoleto
	 * @return lista de pagamentos parciais
	 */
	public List<RelacaoPagtoParcial> findByIdboleto(Long idBoleto) {
		return getRelacaoPagtoParcialDAO().findByIdboleto(idBoleto);
	}
}
