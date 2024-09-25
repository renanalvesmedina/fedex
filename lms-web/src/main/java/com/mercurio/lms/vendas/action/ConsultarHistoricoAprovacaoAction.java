/**
 * 
 */
package com.mercurio.lms.vendas.action;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.util.SimulacaoUtils;
import com.mercurio.lms.workflow.model.service.AcaoService;

/**
 * @author Luis Carlos Poletto
 *  
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.consultarHistoricoAprovacaoAction"
 *
 */
public class ConsultarHistoricoAprovacaoAction extends CrudAction {
	
	private AcaoService acaoService;
	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		Simulacao simulacao = SimulacaoUtils.getSimulacaoInSession();
		if (simulacao.getPendenciaAprovacao() == null) {
			return ResultSetPage.EMPTY_RESULTSET;
		}
		Long idPendencia = simulacao.getPendenciaAprovacao().getIdPendencia();
		criteria.put("idPendencia", idPendencia);
		
		ResultSetPage rsp = getAcaoService().findPaginatedByIdPendencia(criteria);
		
		List result = rsp.getList();
		if (result != null && result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				TypedFlatMap acao = (TypedFlatMap) result.get(i);
				
				StringBuilder dsDescricao = new StringBuilder();
				
				String nrMatricula = acao.getString("usuario.nrMatricula");
				if (StringUtils.isNotBlank(nrMatricula)) {
					dsDescricao.append(nrMatricula);
				}
				
				String nmUsuario = acao.getString("usuario.nmUsuario");
				if (StringUtils.isNotBlank(nmUsuario)) {
					dsDescricao.append(" ").append(nmUsuario);
				}
				
				acao.put("usuario.dsDescricao", dsDescricao);
			}
		}
		
		return rsp;
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		Simulacao simulacao = SimulacaoUtils.getSimulacaoInSession();
		if (simulacao.getPendenciaAprovacao() == null) {
			return IntegerUtils.ZERO;
		}
		Long idPendencia = simulacao.getPendenciaAprovacao().getIdPendencia();
		criteria.put("idPendencia", idPendencia);
		
		return getAcaoService().getRowCountByIdPendencia(criteria);
	}

	/**
	 * @return Returns the acaoService.
	 */
	public AcaoService getAcaoService() {
		return acaoService;
	}

	/**
	 * @param acaoService The acaoService to set.
	 */
	public void setAcaoService(AcaoService acaoService) {
		this.acaoService = acaoService;
	}

}
