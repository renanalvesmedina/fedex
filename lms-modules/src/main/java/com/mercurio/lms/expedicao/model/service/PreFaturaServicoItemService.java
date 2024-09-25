package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.expedicao.model.PreFaturaServicoItem;
import com.mercurio.lms.expedicao.model.dao.PreFaturaServicoItemDAO;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;

public class PreFaturaServicoItemService extends CrudService<PreFaturaServicoItem, Long> {
	
	public ResultSetPage<Map<String, Object>> findPaginatedAprovacaoPreFaturaItem(PaginatedQuery paginatedQuery) {
		return getPreFaturaServicoItemDAO().findPaginatedAprovacaoPreFaturaDetalhe(paginatedQuery);
	}
	
	public void storePreFaturaServicoItem(List<Map<String, Object>> preFaturaServicoItens) {
		
		for (Map<String, Object> item : preFaturaServicoItens) {
			String tpAcao = (String) item.get("tpSituacao");
			String tpSituacao = (String) item.get("tpSituacao");
			Long idPreFaturaServicoItem = (Long) item.get("idPreFaturaServicoItem");
			BigDecimal valor = null;
			Long idMotivo = (Long) item.get("idMotivo");
			
			if(ConstantesExpedicao.TP_ACAO_APROVAR_ITEM_PRE_FATURA.equals(tpAcao)) {
				tpSituacao = ConstantesExpedicao.TP_SITUACAO_ITEM_PRE_FATURA_APROVADO;				
			} else if(ConstantesExpedicao.TP_ACAO_APROVAR_COM_DESCONTO_ITEM_PRE_FATURA.equals(tpAcao)) { 
				valor = (BigDecimal) item.get("vlCobranca");
				tpSituacao = ConstantesExpedicao.TP_SITUACAO_ITEM_PRE_FATURA_APROVADO_COM_DESCONTO;
			} else if (ConstantesExpedicao.TP_ACAO_REPROVAR_ITEM_PRE_FATURA.equals(tpSituacao)) {
				tpSituacao = ConstantesExpedicao.TP_SITUACAO_ITEM_PRE_FATURA_REPROVADO;
			} else if (ConstantesExpedicao.TP_ACAO_POSTERGAR_ITEM_PRE_FATURA.equals(tpSituacao)) {
				tpSituacao = ConstantesExpedicao.TP_SITUACAO_ITEM_PRE_FATURA_POSTERGADO;
			}
			
			storeSitucaoValorMotivoById(idPreFaturaServicoItem, tpSituacao, valor, idMotivo);
		}
	}
	
	public void storeSitucaoValorMotivoById(Long id, String situacao, BigDecimal valor, Long idMotivo) {
		getPreFaturaServicoItemDAO().storeSitucaoValorMotivoById(id, situacao, valor, idMotivo);
	}
	
	public List<Map<String, Object>> findReportEmissaoPreFaturaItem(Long idPreFatura, String tpRelatorio) {
		return getPreFaturaServicoItemDAO().findReportEmissaoPreFaturaItem(idPreFatura, tpRelatorio);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, Object>> findReportEmissaoPreFatura(Long idPreFatura) {
		List<Map<String, Object>> result = getPreFaturaServicoItemDAO().findReportEmissaoPreFatura(idPreFatura);
		Collections.sort(result, new Comparator() {
			public int compare(Object obj1, Object obj2) {
				Map<String, Object> ocorrenciaColeta1 = (Map<String, Object>)obj1;
				Map<String, Object> ocorrenciaColeta2 = (Map<String, Object>)obj2;
        		return ocorrenciaColeta1.get("dsParcelaPreco").toString().toLowerCase().compareTo(ocorrenciaColeta2.get("dsParcelaPreco").toString().toLowerCase());  		
			}    		
    	});
		return result;
	}

	public boolean executeExistsPreFaturaServicoItemByOrdemServicoItem(Long idOrdemServicoItem, DomainValue tpSituacao) {
		return getPreFaturaServicoItemDAO().executeExistsPreFaturaServicoItemByOrdemServicoItem(idOrdemServicoItem, tpSituacao);
	}

	public boolean executeExistsPreFaturaServicoItemByServicoGeracaoAutomatica(Long idServicoGeracaoAutomatica, DomainValue tpSituacao) {
		return getPreFaturaServicoItemDAO().executeExistsPreFaturaServicoItemByServicoGeracaoAutomatica(idServicoGeracaoAutomatica, tpSituacao);
	}
	
	public void setPreFaturaServicoItemDAO(PreFaturaServicoItemDAO preFaturaServicoItemDAO) {
		setDao( preFaturaServicoItemDAO );
	}

	private PreFaturaServicoItemDAO getPreFaturaServicoItemDAO() {
		return (PreFaturaServicoItemDAO) getDao();
	}
	
	@Override
	public Serializable findById(Long id) {
		return super.findById(id);
	}
}