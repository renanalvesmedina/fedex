package com.mercurio.lms.indenizacoes.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacao;
import com.mercurio.lms.indenizacoes.model.service.DoctoServicoIndenizacaoService;
import com.mercurio.lms.indenizacoes.model.service.ReciboIndenizacaoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.indenizacoes.informarPagamentoAction"
 */

public class InformarPagamentoAction extends CrudAction {
	private FilialService filialService;
	private ReciboIndenizacaoService reciboIndenizacaoService;
	private HistoricoFilialService historicoFilialService;
	private DoctoServicoIndenizacaoService doctoServicoIndenizacaoService;
	private ClienteService clienteService;

	private DoctoServicoIndenizacaoService getDoctoServicoIndenizacaoService() {
		return doctoServicoIndenizacaoService;
	}

	public void setDoctoServicoIndenizacaoService(
			DoctoServicoIndenizacaoService doctoServicoIndenizacaoService) {
		this.doctoServicoIndenizacaoService = doctoServicoIndenizacaoService;
	}

	private ReciboIndenizacaoService getReciboIndenizacaoService() {
		return reciboIndenizacaoService;
	}

	public void setReciboIndenizacaoService(
			ReciboIndenizacaoService reciboIndenizacaoService) {
		this.reciboIndenizacaoService = reciboIndenizacaoService;
	}

	private FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	/**
	 * 
	 * @param tfm
	 * @return
	 */
	public ResultSetPage findDoctosServicosByRim(TypedFlatMap tfm ) {
		Long idRim = tfm.getLong("reciboIndenizacao.idReciboIndenizacao");
		return getDoctoServicoIndenizacaoService().findDoctoServicosByIdReciboIndenizacao(idRim, FindDefinition.createFindDefinition(tfm));
	}

	public Integer getRowCountDoctosServicosByRim(TypedFlatMap tfm ) {
		Long idRim = tfm.getLong("reciboIndenizacao.idReciboIndenizacao");
		return getDoctoServicoIndenizacaoService().getRowCountDoctoServicosByIdReciboIndenizacao(idRim);
	}

    /**
     * Consulta a filial pela sigla informada 
     * @param map
     * @return
     */
    public List findLookupFilial(Map map) {
    	FilterList filter = new FilterList(getFilialService().findLookup(map)) {
			public Map filterItem(Object item) {
				Filial filial = (Filial)item;
    			TypedFlatMap typedFlatMap = new TypedFlatMap();
	    		typedFlatMap.put("idFilial", filial.getIdFilial());
		    	typedFlatMap.put("sgFilial",  filial.getSgFilial());
		    	typedFlatMap.put("pessoa.nmFantasia",  filial.getPessoa().getNmFantasia());
				return typedFlatMap;
			}
    	};
    	return (List)filter.doFilter();
    }
    
    /**
     * M�todo que busca o recibo indenifa��o e os campos necess�rios para a 
     * tela de liberar rim 
     * @param criteria
     * @return
     */
    public List findReciboIndenizacao(TypedFlatMap criteria) {
    	Long idFilial = criteria.getLong("filial.idFilial");
    	Integer nrReciboIndenizacao = criteria.getInteger("nrReciboIndenizacao");
    	Long idReciboIndenizacao = criteria.getLong("idReciboIndenizacao");
    	List result = this.getReciboIndenizacaoService().findReciboIndenizacaoToProcessosRim(idFilial, nrReciboIndenizacao, idReciboIndenizacao);
    	TypedFlatMap typedFlatMap = new TypedFlatMap();
    	List retorno = new ArrayList();
    	if (result.size()>0){
    		typedFlatMap = (TypedFlatMap)result.get(0);
    		
    		String favorecidoNrIdentificacao = typedFlatMap.getString("pessoaByIdFavorecido.nrIdentificacao");
    		String favorecidoTpIdentificacao = typedFlatMap.getString("pessoaByIdFavorecido.tpIdentificacao.value");
    		String favorecidoNrIdentificacaoFormatado = "";
    		
    		if (StringUtils.isNotBlank(favorecidoNrIdentificacao) && StringUtils.isNotBlank(favorecidoTpIdentificacao)) {
    			favorecidoNrIdentificacaoFormatado = FormatUtils.formatIdentificacao(favorecidoTpIdentificacao, favorecidoNrIdentificacao);
    		}
    		
    		typedFlatMap.put("pessoaByIdFavorecido.nrIdentificacaoFormatado", favorecidoNrIdentificacaoFormatado);
    		
    		String beneficiarioNrIdentificacao = typedFlatMap.getString("pessoaByIdBeneficiario.nrIdentificacao");
    		String beneficiarioTpIdentificacao = typedFlatMap.getString("pessoaByIdBeneficiario.tpIdentificacao.value");
    		String beneficiarioNrIdentificacaoFormatado = FormatUtils.formatIdentificacao(beneficiarioTpIdentificacao, beneficiarioNrIdentificacao);
    		typedFlatMap.put("pessoaByIdBeneficiario.nrIdentificacaoFormatado", beneficiarioNrIdentificacaoFormatado);
    		
    		typedFlatMap.put("habilitaConfirmar", this.getReciboIndenizacaoService().executeConfirmaPagamento(typedFlatMap.getLong("idReciboIndenizacao")));
    		
    		retorno.add(typedFlatMap);
    	}
    	
    	return retorno;
    }
    
    public TypedFlatMap getDataAtual() {
    	TypedFlatMap result = new TypedFlatMap();
    	result.put("dtAtual", JTDateTimeUtils.getDataAtual());
    	return result;
    }
    
    public TypedFlatMap executeInformaPagamento(TypedFlatMap tfm) {
    	TypedFlatMap result = new TypedFlatMap();
    	Long idReciboIndenizacao = tfm.getLong("reciboIndenizacao.idReciboIndenizacao");
    	
    	ReciboIndenizacao reciboIndenizacao = this.getReciboIndenizacaoService().findById(idReciboIndenizacao);
    	if (this.getReciboIndenizacaoService().executeConfirmaPagamento(reciboIndenizacao)) {
        	
    	YearMonthDay dtPagamentoEfetuado = tfm.getYearMonthDay("dtPagamentoEfetuado");
    	this.getReciboIndenizacaoService().executeInformaPagamento( idReciboIndenizacao, dtPagamentoEfetuado );
    	ReciboIndenizacao rim = this.getReciboIndenizacaoService().findById(idReciboIndenizacao);
    	result.put("tpStatusIndenizacao", rim.getTpStatusIndenizacao());
    	result.put("tpSituacaoWorkflow", rim.getTpSituacaoWorkflow());
        	return result;	
    	} else {
    		throw new BusinessException("LMS-21085", new Object[] {reciboIndenizacao.getFilial().getSgFilial(), reciboIndenizacao.getNrReciboIndenizacao()});
    }
    	
    }
    
    public TypedFlatMap executeInformaPagamentoLote(TypedFlatMap tfm) {
    	String ltReciboIndenizacao = tfm.getString("ltReciboIndenizacao");
    	this.getReciboIndenizacaoService().executeInformaPagamentoLote(ltReciboIndenizacao);
    	return tfm;
    }

	public HistoricoFilialService getHistoricoFilialService() {
		return historicoFilialService;
	}

	public void setHistoricoFilialService(
			HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}  
    
    
}