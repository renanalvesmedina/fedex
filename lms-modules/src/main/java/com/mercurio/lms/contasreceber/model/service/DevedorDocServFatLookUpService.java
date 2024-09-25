package com.mercurio.lms.contasreceber.model.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.contasreceber.model.dao.DevedorDocServFatLookUpDAO;
import com.mercurio.lms.contasreceber.model.param.DevedorDocServFatLookupParam;
import com.mercurio.lms.contasreceber.util.SituacaoDevedorDocServFatLookup;
import com.mercurio.lms.util.FormatUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.devedorDocServFatLookUpService"
 */

public class DevedorDocServFatLookUpService {
	
	private DevedorDocServFatLookUpDAO devedorDocServFatLookUpDAO;
	
	/**
	 * @see findDevedorDocServFat(DevedorDocServFatLookupParam ddsfParam, String msgException)
	 */
	public List findDevedorDocServFat(DevedorDocServFatLookupParam ddsfParam){
		return findDevedorDocServFat(ddsfParam, null);
	}
	
	
	/**
	 * Retorna a lista de devedor a partir dos dados informado.
	 * 
	 * @author Mickaël Jalbert
	 * 
	 * @param Long nrDocumento
	 * @param String tpDocumento
	 * @param Long idFilial
	 * @param Long idCliente
	 * @param String tpSituacaoAprovacao
	 * @return List
	 * */	
	public List findDevedorDocServFat(DevedorDocServFatLookupParam ddsfParam, String msgException){
		SituacaoDevedorDocServFatLookup sddsf = null;
		
		if (ddsfParam.getTpSituacaoDevedorDocServFatValido() !=null){
			/** Configura as situações da fatura que serão usadas no filtro de acordo com o parâmetro da tela */
			sddsf = new SituacaoDevedorDocServFatLookup(ddsfParam.getTpSituacaoDevedorDocServFatValido());
			/** Zerar esta propriedade para ela não filtrar na hora de executar o hql */
			ddsfParam.setTpSituacaoDevedorDocServFatValido(null);
		}
		
		List lstDevedor = this.devedorDocServFatLookUpDAO.findDevedorDocServFat(ddsfParam);
		
		/** Se só tem uma fatura */
		if (!lstDevedor.isEmpty() && lstDevedor.size() == 1 && msgException != null && sddsf != null){
			Map mapDevedor = (Map)lstDevedor.get(0);
			
			/**
			 * Validar se a situação do devedor retornada faz parte da lista de situação validas,
			 * senão, lançar uma exception
			 */
			if (!sddsf.validateTpSituacaoDevedor(((DomainValue)mapDevedor.get("tpSituacaoCobranca")).getValue())){
				throw new BusinessException(msgException);
			}
		}
		
		return formatIdentificacao(lstDevedor);		
	}
	
	private List formatIdentificacao(List lstDevedor){
		for (Iterator iter = lstDevedor.iterator(); iter.hasNext();) {
			Map map = (Map)iter.next();
			String nrIdentificacaoResponsavelAnterior = null;
			
			if( map.get("tpIdentificacaoResponsavelAnterior") != null && map.get("nrIdentificacaoResponsavelAnterior") != null ){
				nrIdentificacaoResponsavelAnterior = FormatUtils.formatIdentificacao(((DomainValue)map.get("tpIdentificacaoResponsavelAnterior")).getValue(), (String)map.get("nrIdentificacaoResponsavelAnterior"));
			} 			
			
			map.put("nrIdentificacaoResponsavelAnterior", nrIdentificacaoResponsavelAnterior);			
		}	
		return lstDevedor;
	}
	
	
	/**
	 * Retorna a lista de devedores a partir dos dados informado montando os hql a 
	 * partir do tipo de documento informado. O parametro tpDocumento é obrigatório!
	 * Busca para Manter Desconto
	 * 
	 * @author José Rodrigo Moraes
	 * 
	 * @param Long nrDocumento Número do documento informado
	 * @param String tpDocumento Tipo do Documento CTR - CRT - NFS e NFT
	 * @param Long idFilial Identificador da filial de origem associada ao documento de serviço
	 * @return List
	 * 
	 */		
	public ResultSetPage findPaginated(DevedorDocServFatLookupParam devedorDocServFatLookupParam, FindDefinition findDef){
		ResultSetPage rsp = this.devedorDocServFatLookUpDAO.findPaginated(devedorDocServFatLookupParam, findDef);
		
		rsp.setList(formatIdentificacao(rsp.getList()));
		
		return rsp;
	}
	
	/**
	 * LMS-3069
	 * 
	 * @param params
	 * @return
	 * @author WagnerFC
	 */
	public Boolean validateDataEntregaCtrc(Long idDoctoServico){
		return this.devedorDocServFatLookUpDAO.validateDataEntregaCtrc(idDoctoServico);
	}
	
	public Integer getRowCount(DevedorDocServFatLookupParam devedorDocServFatLookupParam){
		return this.devedorDocServFatLookUpDAO.getRowCount(devedorDocServFatLookupParam);
	}

	public void setDevedorDocServFatLookUpDAO(
			DevedorDocServFatLookUpDAO devedorDocServFatLookUpDAO) {
		this.devedorDocServFatLookUpDAO = devedorDocServFatLookUpDAO;
	}
}
