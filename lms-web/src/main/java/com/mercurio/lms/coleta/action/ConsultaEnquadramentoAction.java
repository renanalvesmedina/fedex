package com.mercurio.lms.coleta.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sgr.dto.EnquadramentoRegraDTO;
import com.mercurio.lms.sgr.dto.PlanoGerenciamentoRiscoDTO;
import com.mercurio.lms.sgr.model.service.PlanoGerenciamentoRiscoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.consultaEnquadramentoAction"
 */

public class ConsultaEnquadramentoAction {

	private List<TypedFlatMap> listRisco = null;
	private PlanoGerenciamentoRiscoService planoGerenciamentoRiscoService;
	
	public void setPlanoGerenciamentoRiscoService(PlanoGerenciamentoRiscoService planoGerenciamentoRiscoService) {
		this.planoGerenciamentoRiscoService = planoGerenciamentoRiscoService;
	}


	public Map findCarregaEnquadramento(TypedFlatMap criteria) 
	{

		Map result = new HashMap();
		for (Object obj : listRisco)
		{
			TypedFlatMap map = (TypedFlatMap) obj; 
			if (map.getLong("idEnquadramentoRegra").intValue() == criteria.getLong("idEnquadramentoRegra").intValue())
			{
				result.put("idEnquadramentoRegra", map.getLong("idEnquadramentoRegra"));
				result.put("dsEnquadramentoRegra", map.get("dsEnquadramentoRegra"));
				result.put("idFaixaDeValor", map.get("idFaixaDeValor"));
				if( map.getBigDecimal("vlLimiteMinimo") == null ){
					result.put("vlLimiteMinimo", "");
				}else{
				result.put("vlLimiteMinimo", FormatUtils.formatDecimal("#,###,###,##0.00", map.getBigDecimal("vlLimiteMinimo")));
				}
				if( map.getBigDecimal("vlLimiteMaximo") == null ){
					result.put("vlLimiteMaximo", "");
				}else{
				result.put("vlLimiteMaximo", FormatUtils.formatDecimal("#,###,###,##0.00", map.getBigDecimal("vlLimiteMaximo")));
				}
				result.put("blRequerLiberacaoCemop", map.getBoolean("blRequerLiberacaoCemop"));
				result.put("idMoeda", SessionUtils.getMoedaSessao().getIdMoeda());
				result.put("dsSimbolo", SessionUtils.getMoedaSessao().getSgMoeda() + " " + SessionUtils.getMoedaSessao().getDsSimbolo());
				return result;
			}
		}
		return null;
	}
		

	public ResultSetPage findCarregaEnquadramentos(TypedFlatMap criteria) {
		ResultSetPage resultSetPage = new ResultSetPage(1,listRisco);
		return resultSetPage; 
	}

	public Integer getRowCountEnquadramentos(TypedFlatMap criteria){
		generateExigenciasGerRisco(criteria);
		Integer count = 0;
		if( listRisco != null ){
			count = listRisco.size();
		}
		return count;
	}

	private void generateExigenciasGerRisco(TypedFlatMap criteria){
		listRisco = new ArrayList<TypedFlatMap>();
		Long idControleCarga = criteria.getLong("idControleCarga");
		PlanoGerenciamentoRiscoDTO plano = planoGerenciamentoRiscoService.generateEnquadramentoRegra(
				idControleCarga, Boolean.TRUE, Boolean.FALSE);
		
		Collection<EnquadramentoRegraDTO> enquadramentos = plano.getEnquadramentos();
		for (EnquadramentoRegraDTO enquadramentoRegraDTO : enquadramentos) {
			TypedFlatMap map = new TypedFlatMap();
			map.put("dsEnquadramentoRegra", enquadramentoRegraDTO.getEnquadramentoRegra().getDsEnquadramentoRegra());
			map.put("vlLimiteMinimo", enquadramentoRegraDTO.getFaixaDeValor().getVlLimiteMinimo());
			map.put("vlLimiteMaximo", enquadramentoRegraDTO.getFaixaDeValor().getVlLimiteMaximo());
			map.put("blRequerLiberacaoCemop",enquadramentoRegraDTO.getFaixaDeValor().getBlRequerLiberacaoCemop());
			listRisco.add(map);
		}
	}
}