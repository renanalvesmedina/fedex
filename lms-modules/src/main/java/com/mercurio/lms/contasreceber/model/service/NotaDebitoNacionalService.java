package com.mercurio.lms.contasreceber.model.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.ItemNotaDebitoNacional;
import com.mercurio.lms.contasreceber.model.NotaDebitoNacional;
import com.mercurio.lms.contasreceber.model.dao.NotaDebitoNacionalDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * @author Rafael Andrade de Oliveira
 * @since 27/04/2006
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.notaDebitoNacionalService"
 */
public class NotaDebitoNacionalService extends CrudService<NotaDebitoNacional, Long> {

	DevedorDocServFatService devedorDocServFatService;
	
	FaturaService faturaService;

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setDevedorDocServFatService(DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	/**
	 * Recupera uma instância de <code>NotaDebitoNacional</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public NotaDebitoNacional findById(java.lang.Long id) {
        return (NotaDebitoNacional)super.findById(id);
    }
    
    public List findNrNotaDebitoNac(Map map){
    	return getNotaDebitoNacionalDAO().findNrNotaDebitoNac(map);
    }
    
    /**
     * Busca uma Lista de NotasDebitoNacional de acordo com os critérios de pesquisa
     * @param tpDocumentoServico Tipo de documento de serviço - NDN
     * @param idFilial Identificador da filial de origem
     * @param nrDocumento Número do documento de serviço
     * @return Lista de NotasDebitoNacional
     */
    public List findNotaDebitoNacional(String tpDocumentoServico, Long idFilial, Long nrDocumento){
    	return getNotaDebitoNacionalDAO().findNotaDebitoNacional(tpDocumentoServico,idFilial,nrDocumento);
    }

	@Override
	protected NotaDebitoNacional beforeUpdate(NotaDebitoNacional bean) {

		NotaDebitoNacional ndn = (NotaDebitoNacional) bean;
		
		/** Caso a tpSituacaoCobranca do devedorDocServFat seja 'Cancelado'*/
		if( ndn.getTpSituacaoNotaDebitoNac().getValue().equals("C")){
			//Atualizar o devedordocservfat para 'Cancelado'
			DevedorDocServFat ddsf = devedorDocServFatService.findDevedorDocServFatByIdDoctoServico(ndn.getIdDoctoServico());
			ddsf.setTpSituacaoCobranca(new DomainValue("N"));
			devedorDocServFatService.store(ddsf);
			
		}
		
		return super.beforeUpdate(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(NotaDebitoNacional bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setNotaDebitoNacionalDAO(NotaDebitoNacionalDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private NotaDebitoNacionalDAO getNotaDebitoNacionalDAO() {
        return (NotaDebitoNacionalDAO) getDao();
    }
    
	/**
     * Verifica as situações da nota de débito nacional.
     * Se a Situação de Desconto ou a situacao de Cancelamento for "Em aprovação" 
     * ou a situação da Nota de débito nacional for "Cancelada" lança a <BR>
     * exception LMS-36195 : Não é possível alterar ou excluir essa nota de débito 
     * pois a mesma está em aprovação ou cancelada.
     * 
     * @author José Rodrigo Moraes
     * @since 31/08/2006
     * 
     * @param tpSituacaoDesconto Situação de desconto
     * @param tpSituacaoCancelamento Situação de cancelamento
     * @param tpSituacaoNotaDebitoNac Situação da nota de débito
     * @throws LMS-36195
     */
    public void validaAlteracaoExclusaoNotaDebito(DomainValue tpSituacaoDesconto, DomainValue tpSituacaoCancelamento, DomainValue tpSituacaoNotaDebitoNac) {
    	if( verificaSituacoes(tpSituacaoDesconto,tpSituacaoCancelamento,tpSituacaoNotaDebitoNac) ){
			throw new BusinessException("LMS-36195");
		}
    }

	protected NotaDebitoNacional beforeStore(NotaDebitoNacional bean, ItemList items, ItemListConfig itemListConfig) {
    	
    	Long idClientePai = bean.getCliente().getIdCliente();
    	Long masterId     = bean.getIdDoctoServico();
    	
    	//Regra 3.2.1    	
    	validaAlteracaoExclusaoNotaDebito(bean.getTpSituacaoDesconto(),bean.getTpSituacaoCancelamento(),bean.getTpSituacaoNotaDebitoNac());
    	
    	//Regra 3.2.2
    	verificaSituacaoFatura(bean);
    	
    	List lstRemovesItmems = items.getRemovedItems();
    	
    	//Tirar a nota de debito nacional da fatura nas fatura excluidas
    	for (Iterator iter = lstRemovesItmems.iterator(); iter.hasNext();) {
			ItemNotaDebitoNacional itemNotaDebitoNacional = (ItemNotaDebitoNacional) iter.next();
			
			itemNotaDebitoNacional.getFatura().setNotaDebitoNacional(null);
			
			faturaService.storeBasic(itemNotaDebitoNacional.getFatura());
		}
    	
    	//Calcular o valor total dos documentos filhos
		for (Iterator iter = items.iterator(masterId, itemListConfig); iter.hasNext();) {
			
			ItemNotaDebitoNacional itemNotaDebitoNacional = (ItemNotaDebitoNacional)iter.next();
			
			verificaIgualdadeCliente(idClientePai,itemNotaDebitoNacional.getFatura().getCliente().getIdCliente());
			
		}
		
		return (NotaDebitoNacional)super.beforeStore(bean);
	}

    /**
     * Verifica se a nota de débito está relacionada a uma fatura e se a mesma não está 
     * cancelada.
     * Caso afirmativo lança a exception LMS-36196 ('Não é possível cancelar da nota de 
     * débito pois a mesma está vinculada a uma fatura.')
     * 
     * @author José Rodrigo Moraes
     * @since 31/08/2006
     * 
     * @param fatura Fatura relacionada a nota de débito
     */
    protected void verificaSituacaoFatura(NotaDebitoNacional bean) {
    	
    	if( bean.getIdDoctoServico() != null ){
    		Map mapa = (Map) devedorDocServFatService.findDevedorDocServFatByDoctoServico(bean.getIdDoctoServico()).get(0);
    		DomainValue dv = (DomainValue) mapa.get("tpSituacaoCobranca");
    		
    		//Teste da situacao de cobranca do documento de serviço faz exatamente a mesma coisa
    		//que testar a situação da fatura <> 'CA' e <> 'IN'
    		if (dv.getValue().equalsIgnoreCase("F") ){//Em fatura
        		throw new BusinessException("LMS-36196");
        	}
    	}    
    	
	}

	/**
     * Verifica as situações da nota de débito nacional.
     * Se a Situação de Desconto ou a situacao de Cancelamento for "Em aprovação" 
     * ou a situação da Nota de débito nacional for "Cancelada" retorna <code>true</code><br>
     * caso contrário retorna <code>false</code>
     * 
     * @author José Rodrigo Moraes
     * @since 31/08/2006      
     * 
     * @param tpSituacaoDesconto Situação de desconto
     * @param tpSituacaoCancelamento Situação de cancelamento
     * @param tpSituacaoNotaDebitoNac Situação da nota de débito
     */
	private boolean verificaSituacoes(DomainValue tpSituacaoDesconto, DomainValue tpSituacaoCancelamento, DomainValue tpSituacaoNotaDebitoNac) {
		
		boolean retorno = false;
		
		if((tpSituacaoDesconto != null && tpSituacaoDesconto.getValue().equalsIgnoreCase("E")) ||//Em aprovação
		   (tpSituacaoCancelamento != null && tpSituacaoCancelamento.getValue().equalsIgnoreCase("E")) ||//Em aprovação
		   (tpSituacaoNotaDebitoNac != null && tpSituacaoNotaDebitoNac.getValue().equalsIgnoreCase("C"))){
			retorno = true;
		}
		
		return retorno;
		
	}

	/**
     * Compara dois ids de clientes para verificar se são iguais
     * 
     * @author José Rodrigo Moraes
     * @since  01/09/2006
     *  
     * @param idClientePai Identificador do Cliente informado na classe pai
     * @param idClienteFilho Identificador do cliente da fatura
     * @throws BusinessException("LMS-36177")
     */
	private void verificaIgualdadeCliente(Long idClientePai, Long idClienteFilho) {		
		if( idClientePai.compareTo(idClienteFilho) != 0 ){
			throw new BusinessException("LMS-36177");
		}
	}

	/**
	 * Retorna o tpSituacao da nota de débito nacional informada.
	 * 
	 * @author Mickaël Jalbert
	 * @since 10/01/2007
	 * 
	 * @param Long idNotaDebitoNacional
	 * @return DomainValue
	 * */	
	public DomainValue findTpSituacaoByNotaDebitoNacional(Long idNotaDebitoNacional) {
		return getNotaDebitoNacionalDAO().findTpSituacaoByNotaDebitoNacional(idNotaDebitoNacional);
	}	
    
}