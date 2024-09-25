package com.mercurio.lms.seguros.model.service;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.seguros.model.ProcessoSinistro;
import com.mercurio.lms.seguros.model.ReciboReembolsoProcesso;
import com.mercurio.lms.seguros.model.dao.ReciboReembolsoProcessoDAO;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.seguros.reciboReembolsoProcessoService"
 */
public class ReciboReembolsoProcessoService extends CrudService<ReciboReembolsoProcesso, Long> {

	private ConversaoMoedaService conversaoMoedaService;
	private MoedaService moedaService;

	/**
	 * Recupera uma instância de <code>ReciboReembolsoProcesso</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public ReciboReembolsoProcesso findById(java.lang.Long id) {
        return (ReciboReembolsoProcesso)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ReciboReembolsoProcesso bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setReciboReembolsoProcessoDAO(ReciboReembolsoProcessoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ReciboReembolsoProcessoDAO getReciboReembolsoProcessoDAO() {
        return (ReciboReembolsoProcessoDAO) getDao();
    }
    
    /**
     * Método store da DF2.
     * @param reciboReembolsoProcesso
     * @param itemList
     * @author luisfco
     * @return
     */
    public java.io.Serializable storeReciboReembolsoProcesso(ReciboReembolsoProcesso reciboReembolsoProcesso, ItemList itemList) {
    	
    	boolean masterIdIsNull = reciboReembolsoProcesso.getIdReciboReembolsoProcesso() == null;
    	
    	try {

    		return getReciboReembolsoProcessoDAO().storeReciboReembolsoProcesso(reciboReembolsoProcesso, itemList);
	    	
    	} catch (RuntimeException re) {
    		rollbackMasterState(reciboReembolsoProcesso, masterIdIsNull, re);
            itemList.rollbackItemsState();            
    		throw re;
		}
    }
    
    /**
     * Calcula o valor total de reembolso do processo de sinistro, já convertendo para a moeda do processo de sinistro.
     * Utiliza como data de cotação, a data do sinistro.
     * @param idProcessoSinistro
     * @return
     */
    public BigDecimal findCalculaSomatorioVlReembolsosProcesso(ProcessoSinistro processoSinistro) {
    	
    	Long idMoedaSinistro = processoSinistro.getMoeda().getIdMoeda();
    	BigDecimal soma = new BigDecimal(0.00);
    	
    	Iterator it = findRecibosByIdProcessoSinistro(processoSinistro.getIdProcessoSinistro()).iterator();
    	while (it.hasNext()) {
    		    		
    		ReciboReembolsoProcesso rrp = (ReciboReembolsoProcesso)it.next();
    		    		
        	// se a moeda do processo é diferente da moeda do recibo, entao converte valores para a data do sinistro
        	Long idMoedaRecibo = rrp.getMoeda().getIdMoeda();
        	BigDecimal vlReembolsado = null;
        	if (!idMoedaRecibo.equals(idMoedaSinistro)) {
        		vlReembolsado = converteValor(idMoedaRecibo, idMoedaSinistro, processoSinistro.getDhSinistro().toYearMonthDay(), rrp.getVlReembolso());
        	} else {
        		vlReembolsado = rrp.getVlReembolso();
        	}
    		soma = soma.add(vlReembolsado);
    	}
    	return soma;
    }

    /**
     * Converte o valor especificado na data de cotação especificada, da moeda origem para a moeda destino. 
     * @param idMoedaOrigem
     * @param idMoedaDestino
     * @param dtCotacao
     * @param valor
     * @return
     */
    private BigDecimal converteValor(Long idMoedaOrigem, Long idMoedaDestino, YearMonthDay dtCotacao, BigDecimal valor) {
    	
    	Long idPais = SessionUtils.getMoedaSessao().getIdMoeda();
    	BigDecimal resultado = null;

    	try {
    		resultado = conversaoMoedaService.findConversaoMoeda(idPais, idMoedaOrigem, idPais, idMoedaDestino, dtCotacao, valor);
    	} catch (BusinessException be) {
			Moeda moedaOrigem = moedaService.findById(idMoedaOrigem); 
			Moeda moedaDestino = moedaService.findById(idMoedaDestino);
			String data = JTFormatUtils.format(dtCotacao);
			String sgMoedaOrigem = moedaOrigem.getSiglaSimbolo();
			String sgMoedaDestino = moedaDestino.getSiglaSimbolo();
			String nmPais = SessionUtils.getPaisSessao().getNmPais().getValue();
			throw new BusinessException("LMS-22016", new Object[]{sgMoedaOrigem, sgMoedaDestino, data, nmPais});
		}
    	
    	return resultado;
    }

    /**
     * Remove filhos e pais em cascata de acordo com os ids dos pais.
     * @param ids
     */
    public void removeByIdsCustom(List ids) {
    	getReciboReembolsoProcessoDAO().removeByIdsCustom(ids);
    }    
    
    public ResultSetPage findPaginatedCustom(TypedFlatMap tfm) {
        return getReciboReembolsoProcessoDAO().findPaginatedCustom(tfm, FindDefinition.createFindDefinition(tfm));
    }

    public Integer getRowCountCustom(TypedFlatMap tfm) {
    	return getReciboReembolsoProcessoDAO().getRowCountCustom(tfm);
    }

    public List findRecibosByIdProcessoSinistro(Long idProcessoSinistro) {    	
    	return getReciboReembolsoProcessoDAO().findRecibosByIdProcessoSinistro(idProcessoSinistro); 
    }

	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}

	public List findSomaValoresReembolsoReciboReembolso(Long idProcessoSinistro) {
		return getReciboReembolsoProcessoDAO().findSomaValoresReembolsoReciboReembolso(idProcessoSinistro);
	}
	
}