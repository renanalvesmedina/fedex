package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.model.Desconto;
import com.mercurio.lms.contasreceber.model.ReciboDesconto;
import com.mercurio.lms.contasreceber.model.dao.ReciboDescontoDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.reciboDescontoService"
 */
public class ReciboDescontoService extends CrudService<ReciboDesconto, Long> {
	
	private DescontoService descontoService;
	private WorkflowPendenciaService workflowPendenciaService;
	private ConfiguracoesFacade configuracoesFacade;

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	/**
	 * Recupera uma inst�ncia de <code>ReciboDesconto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public ReciboDesconto findById(java.lang.Long id) {
        return (ReciboDesconto)super.findById(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ReciboDesconto bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setReciboDescontoDAO(ReciboDescontoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ReciboDescontoDAO getReciboDescontoDAO() {
        return (ReciboDescontoDAO) getDao();
    }

    /**
     * M�todo de cancelamento do Recibo Desconto
     * @param idReciboDemonstrativo Identificador do Recibo Desconto a ser cancelado
     */
	public ReciboDesconto executeCancelar(Long idReciboDemonstrativo, String tpDesconto) {
		
		ReciboDesconto rd = findById(idReciboDemonstrativo);
		
		YearMonthDay dtEmissao = rd.getDtEmissao();
		YearMonthDay dtAtual   = JTDateTimeUtils.getDataAtual();
		
		if( dtEmissao.getMonthOfYear() != dtAtual.getMonthOfYear() ){
			throw new BusinessException("LMS-36074");
		}
		
		verificaFilialReciboDesconto(rd.getFilial());
		
		if (rd.getTpSituacaoAprovacao()!=null){
			if( rd.getTpSituacaoAprovacao().getValue().equals("E") ){//Em aprova��o
				throw new BusinessException("LMS-36133");
			}
		}
		
		
		if( rd.getTpSituacaoReciboDesconto() != null && rd.getTpSituacaoReciboDesconto().getValue().equals("C") ){//cancelado
			throw new BusinessException("LMS-36143");
		}
		
		/*
		 * Valida se o primeiro doc serv j� est� liquidado
		 */
		List descontosList = descontoService.findDescontosByReciboDesconto(idReciboDemonstrativo, tpDesconto);
		Desconto desconto = (Desconto)descontosList.get(0);
		if (desconto.getDevedorDocServFat().getTpSituacaoCobranca().getValue().equalsIgnoreCase("L")){
			throw new BusinessException("LMS-36243");
		}
		
		
		Pendencia pendencia = workflowPendenciaService.generatePendencia(rd.getFilial().getIdFilial(),
																		 ConstantesWorkflow.NR3615_CANC_RECB_DESC,
				                                                         rd.getIdReciboDesconto(),
				                                                         mountDsPendencia(rd, configuracoesFacade.getMensagem("LMS-36240")),
				                                                         JTDateTimeUtils.getDataHoraAtual());		
		
		rd.setPendencia(pendencia);

		//sempre que cancelar um recibo 
		//a situacao aprovacao dele vai para "Em aprova��o"
		rd.setTpSituacaoAprovacao( new DomainValue("E") );

		store(rd);
		
		return rd;
		
	}

	/**
	 * Monta a descri��o da pend�ncia de workflow.
	 * @param rd
	 * @return
	 */
	private String mountDsPendencia(ReciboDesconto rd, String keyMessage) {
		MessageFormat message = new MessageFormat(keyMessage);
		String dsPendencia = message.format(new Object[]{rd.getFilial().getSgFilial() +
				" " +   
				FormatUtils.formataNrDocumento(rd.getNrReciboDesconto().toString(), "RDE")});
		return dsPendencia;
	}
	
	/**	 
	 * Rotina para Aprova��o/Reprova��o do cancelamento do recibo de desconto
	 * @param idsProcesso Identificadores dos processos que geraram o workflow
	 * @param tpsSituacao Situa��o da aprova��o de cada processso
	 */
    public String executeWorkflow(List idsProcesso, List tpsSituacao) {
    	
        if (idsProcesso == null || tpsSituacao == null || idsProcesso.size() != tpsSituacao.size()){
        	return null;
        }
        
        for (int i = 0; i < idsProcesso.size(); i++){
        	
    		ReciboDesconto rd = findById((Long) idsProcesso.get(i));
    		
    		if (((String)tpsSituacao.get(0)).equals("A")) {
    			rd.setTpSituacaoReciboDesconto(new DomainValue("C"));//Cancelado
    			rd.setVlReciboDesconto(new BigDecimal(0));    			    	
    		}
    		
    		rd.setDhTransmissao(null);
    		rd.setTpSituacaoAprovacao(new DomainValue((String)tpsSituacao.get(0)));
    		store(rd);    			
			descontoService.executeCancelar(rd.getIdReciboDesconto(),"R");
    			
        }        
        return null;
    }

	/**
	 * M�todo chamado ao clicar no bot�o Reenviar Recibo na aba Detalhamento
	 * Marca o recibo desconto com a data de transmiss�o como nula
	 * @param idReciboDesconto idReciboDesconto Identificador do Recibo a ser reenviado
	 */
	public void executeReenviarRecibo(Long idReciboDesconto) {
		// Foi feito um evict do objeto para que o Hibernate dispare um update mesmo
		// que o campo DhTransmissao nao tenha altera��o de valores.
		// Isso � necess�rio para que seja disparada uma trigger no banco de dados.
		ReciboDesconto rd = findById(idReciboDesconto);
		verificaFilialReciboDesconto(rd.getFilial());
		getReciboDescontoDAO().getAdsmHibernateTemplate().evict(rd);
		rd.setDhTransmissao(null);
		store(rd);
		getReciboDescontoDAO().getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().flush();
	}
	
	/**
	 * Lan�a a BusinessException : LMS-36075 quando a filial do recibo desconto n�o � a filial Matriz nem a filial do usu�rio logado
	 * @param filial Filial do Recibo Desconto
	 */
	private void verificaFilialReciboDesconto(Filial filial){
		boolean filialMatriz = SessionUtils.isFilialSessaoMatriz();
		
		Filial filialUsuario = SessionUtils.getFilialSessao();
		if( !filialMatriz && !filial.equals(filialUsuario) ){
			throw new BusinessException("LMS-36075");
		}
		
	}
	
	/**
	 * Cancela todos os recibos de descontos do redeco informado
	 * 
	 * @author Micka�l Jalbert
	 * @since 06/07/2006
	 * 
	 * @param Long idRedeco
	 */
	public void cancelRecibosDescontoRedeco(Long idRedeco){
		List lstReciboDesconto = findByRedeco(idRedeco);
		
		for (Iterator iter = lstReciboDesconto.iterator(); iter.hasNext();) {
			ReciboDesconto reciboDesconto = (ReciboDesconto) iter.next();
			
			reciboDesconto.setTpSituacaoReciboDesconto(new DomainValue("C"));
			
			store(reciboDesconto);
			
			List lstDescontos = descontoService.findByReciboDesconto(reciboDesconto.getIdReciboDesconto());
			
			for (Iterator iterator = lstDescontos.iterator(); iterator.hasNext();) {
				Desconto desconto = (Desconto) iterator.next();
				
				desconto.setReciboDesconto(null);
				
				descontoService.storePadrao(desconto);
			}
		}
	}

	/**
	 * Exclui os recibos de descontos do redeco informado
	 * 
	 * @author Micka�l Jalbert
	 * @since 06/07/2006
	 * 
	 * @param Long idRedeco
	 */
	public void removeRecibosDescontoRedeco(Long idRedeco){
		List lstReciboDesconto = findByRedeco(idRedeco);
		
		//Por cada Desconto, desligar todos os descontos ligado com o ReciboDesconto
		//e CANCELAR o ReciboDesconto
		for (Iterator iter = lstReciboDesconto.iterator(); iter.hasNext();) {
			ReciboDesconto reciboDesconto = (ReciboDesconto) iter.next();
			
			if (! reciboDesconto.getTpSituacaoReciboDesconto().getValue().equals("C") ){

			List lstDesconto = descontoService.findDescontosByReciboDesconto(reciboDesconto.getIdReciboDesconto(), "R");
			
			for (Iterator iterator = lstDesconto.iterator(); iterator.hasNext();) {
				Desconto desconto = (Desconto) iterator.next();
				desconto.setReciboDesconto(null);
				
				descontoService.storePadrao(desconto);
			}
			
				reciboDesconto.setTpSituacaoReciboDesconto(new DomainValue("C"));
				reciboDesconto.setVlReciboDesconto(new BigDecimal(0.0));
				
			getReciboDescontoDAO().getAdsmHibernateTemplate().flush();
			
		}
			
	}	
	}	
	
	
	
	/**
	 * Retorna a lista de recibos de desconto do redeco informado
	 * 
	 * @author Micka�l Jalbert
	 * @since 06/07/2006
	 * 
	 * @param Long idRedeco
	 * @return List
	 */
	public List findByRedeco(Long idRedeco){
		return getReciboDescontoDAO().findByRedeco(idRedeco);
	}
	
	/**
	 * Retorna o n�mero de recibo de desconto do redeco informado
	 * 
	 * @author Micka�l Jalbert
	 * @since 07/12/2006
	 * 
	 * @param Long idRedeco
	 * @return Long
	 */
	public Integer getRowCountByIdRedeco(Long idRedeco){
		return getReciboDescontoDAO().getRowCountByIdRedeco(idRedeco);
	}
	
	/**
	 * Carrega o ReciboDesconto de acordo com o n�mero e a sigla da filial
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 09/08/2007
	 *
	 * @param idRedeco
	 * @return
	 *
	 */
	public ReciboDesconto findReciboDescontoByNumeroAndFilial(Long nrReciboDesconto, Long idFilial, String tpSituacao){
		return getReciboDescontoDAO().findReciboDescontoByNumeroAndFilial(nrReciboDesconto, idFilial, tpSituacao);
	}	
	
	public void setDescontoService(DescontoService descontoService) {
		this.descontoService = descontoService;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}	
}