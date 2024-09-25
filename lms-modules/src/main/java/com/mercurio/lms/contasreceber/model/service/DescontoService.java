package com.mercurio.lms.contasreceber.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.contasreceber.model.Desconto;
import com.mercurio.lms.contasreceber.model.DescontoAnexo;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.MotivoDesconto;
import com.mercurio.lms.contasreceber.model.dao.DescontoDAO;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.questionamentoFaturas.model.service.QuestionamentoFaturasService;
import com.mercurio.lms.questionamentofaturas.model.QuestionamentoFatura;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.PrazoVencimento;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.descontoService"
 */
public class DescontoService extends CrudService<Desconto, Long> {
    
    private DomainValueService domainValueService;
    private WorkflowPendenciaService workflowPendenciaService;
    private ConfiguracoesFacade configuracoesFacade;
    private DevedorDocServFatService devedorDocServFatService;
    private MotivoDescontoService motivoDescontoService;
    private ConhecimentoService conhecimentoService;
    private NotaFiscalServicoService notaFiscalServicoService;
	private FaturaService faturaService;
	private QuestionamentoFaturasService questionamentoFaturasService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private DoctoServicoService doctoServicoService;
	private DescontoAnexoService descontoAnexoService;
	
	public Desconto findByIdSimple(Long id){
		return (Desconto)getDescontoDAO().getAdsmHibernateTemplate().get(Desconto.class, id);
	}
	
	/**
	 * Recupera uma instância de <code>Desconto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public Desconto findById(java.lang.Long id) {
    	
    	Desconto desconto = null;
        
    	List retorno = (List) getDescontoDAO().findByIdList(id);
        
    	DevedorDocServFat ddsf = null;    	
    	Cliente c = null;
    	Filial filialDevedor = null;
    	Pessoa p = null;
    	Filial filialOrigem = null;
    	MotivoDesconto motivoDesconto = null;
        
        for (Iterator iter = retorno.iterator(); iter.hasNext();) {
			
        	ListOrderedMap element = (ListOrderedMap) iter.next();
			
        	filialDevedor = new Filial();
        	filialDevedor.setIdFilial(Long.valueOf(((Number)element.get("ID_FILIAL_DEVEDOR")).longValue()));
        	filialDevedor.setSgFilial((String)element.get("SG_FILIAL_DEVEDOR"));
        	
        	Pessoa pesFilDevedor = new Pessoa();
        	pesFilDevedor.setNmFantasia((String)element.get("NM_FILIAL_DEVEDOR"));
        	
        	filialDevedor.setPessoa(pesFilDevedor);
        	
        	c = new Cliente();                            
        	c.setIdCliente(Long.valueOf(((Number)element.get("ID_CLIENTE")).longValue()));
        	
        	p = new Pessoa();                            
        	p.setNrIdentificacao(FormatUtils.formatIdentificacao((String) element.get("TP_IDENTIFICACAO"),(String)element.get("NR_IDENTIFICACAO")));
        	p.setNmPessoa((String)element.get("NM_PESSOA"));
        	
        	c.setPessoa(p);
        	
        	Servico servico = new Servico();
        	
        	servico.setIdServico(Long.valueOf(((Number)element.get("ID_SERVICO")).longValue()));
        	DomainValue dvModal = new DomainValue();
        	dvModal.setValue((String)element.get("TP_MODAL"));
        	servico.setTpModal(dvModal);

        	DomainValue dvAbrangencia = new DomainValue();
        	dvAbrangencia.setValue((String)element.get("TP_ABRANGENCIA"));
        	servico.setTpAbrangencia(dvAbrangencia);
          
        	Moeda m = new Moeda();
        	m.setIdMoeda(Long.valueOf(((Number)element.get("ID_MOEDA")).longValue()));
        	m.setSgMoeda((String)element.get("SG_MOEDA"));
        	m.setDsSimbolo((String)element.get("DS_SIMBOLO"));
        	
        	filialOrigem = new Filial();
        	filialOrigem.setIdFilial(Long.valueOf(((Number)element.get("ID_FILIAL")).longValue()));
        	filialOrigem.setSgFilial((String)element.get("SG_FILIAL"));
          
        	DoctoServico ds = new DoctoServico();
        	ds.setIdDoctoServico(Long.valueOf(((Number)element.get("ID_DOCTO_SERVICO")).longValue()));
        	DomainValue dvTipoDocumento = new DomainValue();
        	dvTipoDocumento.setValue((String)element.get("TP_DOCUMENTO_SERVICO"));
        	ds.setTpDocumentoServico(dvTipoDocumento);
        	
        	ds.setMoeda(m);                            
        	ds.setServico(servico);
        	ds.setFilialByIdFilialOrigem(filialOrigem);
        	
        	ddsf = new DevedorDocServFat();
        	ddsf.setIdDevedorDocServFat(Long.valueOf(((Number)element.get("ID_DEVEDOR_DOC_SERV_FAT")).longValue()));
        	ddsf.setVlDevido((BigDecimal)element.get("VL_DEVIDO"));
          
        	ddsf.setDoctoServico(ds);
        	ddsf.setCliente(c);
        	ddsf.setFilial(filialDevedor);
        	ddsf.setCliente(c);
        	
        	motivoDesconto = new MotivoDesconto();
        	motivoDesconto.setIdMotivoDesconto(Long.valueOf(((Number)element.get("ID_MOTIVO_DESCONTO")).longValue()));
        	motivoDesconto.setDsMotivoDesconto(new VarcharI18n((String)element.get("DS_MOTIVO_DESCONTO")));
        	
        	desconto = new Desconto();
        	desconto.setIdDesconto(Long.valueOf(((Number)element.get("ID_DESCONTO")).longValue()));
        	desconto.setObDesconto((String)element.get("OB_DESCONTO"));
        	desconto.setObAcaoCorretiva((String) element.get("OB_ACAO_CORRETIVA") );
        	DomainValue dvTipoAprovacao = new DomainValue();
        	dvTipoAprovacao.setValue((String)element.get("TP_SITUACAO_APROVACAO"));
        	desconto.setTpSituacaoAprovacao(dvTipoAprovacao);

        	desconto.setVlDesconto((BigDecimal)element.get("VL_DESCONTO"));
        	
        	desconto.setDevedorDocServFat(ddsf);                            
        	desconto.setNrDocumento(FormatUtils.formataNrDocumento(((Number)element.get("NR_DOCUMENTO")).toString(),
        														   (String)element.get("TP_DOCUMENTO_SERVICO")));
        	
        	desconto.setPercentualDesconto(calculaPorcentagemDesconto((BigDecimal)element.get("VL_DESCONTO"),
        											                  (BigDecimal)element.get("VL_DEVIDO")));
        	
        	desconto.setMotivoDesconto(motivoDesconto);
        	desconto.setDevedorDocServFat(ddsf);
        	
        	if( element.get("ID_PENDENCIA") != null ){
        		desconto.setIdPendencia(Long.valueOf(((Number)element.get("ID_PENDENCIA")).longValue()));
		}
        
        	
        	DomainValue dvSetorCausadorAbatimento = new DomainValue();
        	dvSetorCausadorAbatimento.setValue((String)element.get("TP_SETOR_CAUSADOR_ABATIMENTO"));
        	desconto.setTpSetorCausadorAbatimento(dvSetorCausadorAbatimento);
        	
        	desconto.setVersao( (Integer) element.get("D.NR_VERSAO") );
        	
        	
		}
        
        return desconto;
    }
        
    /**
     * Retorna o desconto com o devedor_doc_serv_fat.
     * 
     * @param Long idDesconto
     * @return Desconto
     * */
    public Desconto findByIdWithFaturaAndBoleto(Long idDesconto){
    	return this.getDescontoDAO().findByIdWithDevedor(idDesconto);
    }
    
    /**
     * Retorna a lista de descontos da fatura informada.
     * 
     * @author Mickaël Jalbert
     * @since 19/04/2006
     * 
     * @param Long idFatura
     * @return List
     * */
    public List findByFatura(Long idFatura){
    	return this.getDescontoDAO().findByFatura(idFatura);
    }
    
    /**
     * Retorna true se existe um desconto pendente de aprovação ligado a fatura informada.
     * 
     * @author Mickaël Jalbert
     * @since 28/09/2006
     * 
     * @param Long idFatura
     * @return Boolean
     * */
    public Boolean existsDescontoPendenteByFatura(Long idFatura){
    	return this.getDescontoDAO().existsDescontoPendenteByFatura(idFatura);
    }    
    
    
    /**
     * Retorna o desconto por devedorDocServFat.
     * 
     * @author Mickaël Jalbert
     * 
     * @param Long idDevedorDocServFat
     * 
     * @return Desconto
     * */
    public Desconto findByDevedorDocServFat(Long idDevedorDocServFat){
    	if (idDevedorDocServFat != null){
	    	List lstDesconto = this.getDescontoDAO().findByDevedorDocServFatSituacao(idDevedorDocServFat, null);
	    	
	    	if (lstDesconto.size() == 1){
	    		return (Desconto)lstDesconto.get(0);  		
	    	} else {    	
	    		return null;
	    	}
    	} else {
    		return null;
    	}
    }
    
    public Desconto findPendenteByDevedorDocServFat(Long idDevedorDocServFat){
    	List lstDesconto = this.getDescontoDAO().findByDevedorDocServFatSituacao(idDevedorDocServFat, "E");
    	
    	if (lstDesconto.size() == 1){
    		return (Desconto)lstDesconto.get(0);  		
    	} else {    	
    		return null;
    	}    	
    }
    
    /**
     * Retorna o desconto a partir de um idDoctoServico.
     * @param idDoctoServico
     * @return
     */
    public Desconto findByDoctoServico(Long idDoctoServico){
    	return getDescontoDAO().findByDoctoServico(idDoctoServico);
    }
    
    /**
     * Valida se tem um desconto pendente, se tem, lança uma exception
     * 
     * @author Mickaël Jalbert
     * @since 19/04/2006
     * 
     * @param Long idDevedorDocServFat
     * @return Boolean
     * 
     * @exception BusinessException("LMS-36010")
     * */
    public Boolean validateDescontoPendenteWorkflow(Long idDevedorDocServFat){
    	Desconto desconto = findByDevedorDocServFat(idDevedorDocServFat);
    	return validateDescontoPendenteWorkflow(desconto);
    }
    
    /**
     * Valida se tem um desconto pendente, se tem, lança uma exception
     * 
     * @author Mickaël Jalbert
     * @since 19/04/2006
     * 
     * @param Long idDevedorDocServFat
     * @return Boolean
     * 
     * @exception BusinessException("LMS-36010")
     * */
    public Boolean validateDescontoPendenteWorkflow(Desconto desconto){
    	if (desconto != null) {
	    	String tpSituacao = desconto.getTpSituacaoAprovacao().getValue();
	    	
	    	//Se a situação de aprovação do desconto for igual a 'Em aprovação', lançar uma exception
	    	if (tpSituacao != null && tpSituacao.equals("E")){
	    		throw new BusinessException("LMS-36010");
	    	}
    	}
    	
    	return Boolean.TRUE;
    }       
    
    /**
     * Gera um desconto a partir dos dados informado.
     * 
     * @author Mickaël Jalbert
     * 16/03/2006
     * 
     * @param Long idDevedorDocServFat
     * @return Desconto
     * */
    public void generateDesconto(Long idDevedorDocServFat, Long idMotivoDesconto, BigDecimal vlDesconto, String obDesconto) {
    	DevedorDocServFat devedorDocServFat = this.devedorDocServFatService.findByIdInitLazyProperties(idDevedorDocServFat, false);
    	MotivoDesconto motivoDesconto = this.motivoDescontoService.findById(idMotivoDesconto);
    	
    	Desconto desconto = findByDevedorDocServFat(idDevedorDocServFat);
    	
    	if (desconto == null){
    		desconto = new Desconto();
    	}
    	
    	desconto.setDevedorDocServFat(devedorDocServFat);
    	desconto.setMotivoDesconto(motivoDesconto);
    	desconto.setVlDesconto(vlDesconto);
    	desconto.setObDesconto(obDesconto);    	
    	desconto.setTpSituacaoAprovacao(new DomainValue("E"));
    	
    	desconto.setPercentualDesconto(this.calculaPorcentagemDesconto(vlDesconto, devedorDocServFat.getVlDevido()));
    	
    	this.store(desconto);
    } 
    

    /**
     * Através do valor do desconto e do valor do documento calcula o valor equivalente ao desconto em porcentagem
     * @param vlDesconto Valor a ser descontado do valor do documento
     * @param vlDocumento Valor do documento
     * @return Valor do desconto em porcentagem
     */
    public BigDecimal calculaPorcentagemDesconto(BigDecimal vlDesconto, BigDecimal vlDocumento) {
        if( (vlDocumento == null || vlDesconto == null) || CompareUtils.gt(vlDesconto,vlDocumento) ) {
            throw new BusinessException("LMS-36003");
        }
        BigDecimal percentual = ((BigDecimal)vlDesconto.divide(vlDocumento,5, RoundingMode.HALF_UP)).multiply(new BigDecimal(100));

        return percentual;
    }    

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
    	Desconto d = findById(id);
    	Long idPendencia = null;
    	if(d != null){
    		idPendencia = d.getIdPendencia();
		
    	if(idPendencia != null){
				QuestionamentoFatura questionamentoFatura = questionamentoFaturasService.findById(idPendencia);
    			if (!"ACO".equals(questionamentoFatura.getTpSituacao().getValue()) && !"CAN".equals(questionamentoFatura.getTpSituacao().getValue())){
					questionamentoFaturasService.storeCancelarQuestionamento(questionamentoFatura, "");
    }
	    			}

    	List<DescontoAnexo> anexos = descontoAnexoService.findDescontoAnexosByIdDesconto(d.getIdDesconto());
    	if(anexos != null){
    		for (Object anexo: anexos){
    			descontoAnexoService.removeById(((DescontoAnexo)anexo).getIdDescontoAnexo());
    		}
    	}
    	super.removeById(d.getIdDesconto());
    }
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
		 for (Object idDesconto : ids) {
			 removeById((Long)idDesconto);
    }
    }

	@Override
	public Serializable store(Desconto bean) {
		return store(bean, null);
	}
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 * Se é um desconto novo, cria uma pendencia no workflow.
     * Busca o id da pendencia seta no Desconto, troca a situação da aprovação e salva o desconto
	 * Se estiver rodando pela integração não faz validação se o documento está liquidado.
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	// FIXME corrigir para retornar o ID
    public Desconto store(Desconto bean, ItemList listDescontos ) {
        DevedorDocServFat devedorDocServFat = devedorDocServFatService.findByIdWithDocumento(bean.getDevedorDocServFat().getIdDevedorDocServFat());
        
        if(!SessionUtils.isIntegrationRunning()){ 
        if( !devedorDocServFat.getTpSituacaoCobranca().getValue().equals("P") && 
            !devedorDocServFat.getTpSituacaoCobranca().getValue().equals("C") ){
			throw new BusinessException("LMS-36006");
		}
        }
    	
        bean.setDevedorDocServFat(devedorDocServFat);
        Long idPendenciaAntigo = bean.getIdPendencia();
        if(!SessionUtils.isIntegrationRunning()){ 
        	//LMS-2770 / Se for nulo não é proveniente da tela manter descontos e a validação não deve ser feita
        	if( listDescontos != null && validaNovoQuestionamento(bean) ){
				if(!existeAnexoDesconto(bean.getIdDesconto(), listDescontos)){
					listDescontos.rollbackItemsState();
    				throw new BusinessException("LMS-42045");
	    		}
			}
        storeQuestionamentoFatura(bean);
        }
        flush();
        super.store(bean);
        
        
        List<Serializable> idDescontoAnexosGerados = new ArrayList<Serializable>();
        if (listDescontos != null){
        	for (Object descontoAnexoO : listDescontos.getNewOrModifiedItems()) {
        		DescontoAnexo descontoAnexo = (DescontoAnexo) descontoAnexoO;
        		idDescontoAnexosGerados.add(getDescontoAnexoService().store(descontoAnexo));
    		}
    		
        	for (Object descontoAnexoO : listDescontos.getRemovedItems()) {
        		DescontoAnexo descontoAnexo = (DescontoAnexo) descontoAnexoO;
        		getDescontoAnexoService().removeById(descontoAnexo.getIdDescontoAnexo());
        		
    	}
    	
        	if (( idPendenciaAntigo == null && bean.getIdPendencia() != null ) 
    				|| (idPendenciaAntigo != null && idPendenciaAntigo.compareTo(bean.getIdPendencia()) != 0)
    			){
    			List<DescontoAnexo> listFaturaAnexo = descontoAnexoService.findDescontoAnexosByIdDesconto(bean.getIdDesconto());
    			for (DescontoAnexo desconto : listFaturaAnexo) {
    				if(!idDescontoAnexosGerados.contains(desconto.getIdDescontoAnexo())){
    					desconto.setDhEnvioQuestFat(null);
					getDescontoAnexoService().store(desconto);
				}
    		}
    		}
		}
        
        
       return bean;
		}
        
	private boolean existeAnexoDesconto(Long idDesconto, ItemList anexos) {
        
		if(anexos.isInitialized()){
    		if(anexos.getNewOrModifiedItems() != null && !anexos.getNewOrModifiedItems().isEmpty()){
    			for (Object descontoAnexoO : anexos.getNewOrModifiedItems()) {
    				DescontoAnexo descontoAnexo = (DescontoAnexo) descontoAnexoO;
    				if(descontoAnexo.getBlEnvAnexoQuestFat()){
    					return true;
    }
    			}
    		}
    	}
        
    	List<DescontoAnexo> listaDescontoAnexo = new ArrayList<DescontoAnexo>(); 
		if(idDesconto != null){
			listaDescontoAnexo = descontoAnexoService.findDescontoAnexosByIdDesconto(idDesconto);
		}
			
		if(anexos.isInitialized()){
			for (Object descontoAnexoO : anexos.getRemovedItems()) {
				DescontoAnexo descontoAnexo = (DescontoAnexo) descontoAnexoO;
				
				if(descontoAnexo.getIdDescontoAnexo() != null){
					compareById : for (int i = 0; i < listaDescontoAnexo.size(); i++) {
						if(listaDescontoAnexo.get(i).getIdDescontoAnexo() != null && listaDescontoAnexo.get(i).getIdDescontoAnexo().equals(descontoAnexo.getIdDescontoAnexo())){
							listaDescontoAnexo.remove(i);
							break compareById;
						}
					}
				}
			}
		}
		
		if(!listaDescontoAnexo.isEmpty()){
			for (DescontoAnexo descontoAnexo : listaDescontoAnexo) {
				if(descontoAnexo.getBlEnvAnexoQuestFat()){
					return true;
				}
			}
		}
		return false;
	}

	private boolean validaNovoQuestionamento(Desconto desconto) {
		//Retorna o id e o valor do desconto
    	Desconto descontoOld = findByIdDevedorDocServFatDisconnected(desconto.getDevedorDocServFat().getIdDevedorDocServFat());
    	
    	if (descontoOld != null){
    		if (!(descontoOld.getIdDesconto()).equals(desconto.getIdDesconto())){
    			return false;
    		}
    	}
    	
		if (findValorMinimoDocumentoDesconto().compareTo(desconto.getVlDesconto()) < 0){
			if (descontoOld == null || "R".equals(desconto.getTpSituacaoAprovacao().getValue()) ||
					(descontoOld != null && 
						((desconto.getVlDesconto().compareTo(descontoOld.getVlDesconto()) > 0) ||
							(!"A".equals(desconto.getTpSituacaoAprovacao().getValue()) && 
									(	desconto.getVlDesconto().compareTo(descontoOld.getVlDesconto()) < 0 || 
											!desconto.getTpSetorCausadorAbatimento().getValue().equals(descontoOld.getTpSetorCausadorAbatimento().getValue()) ||
											!desconto.getMotivoDesconto().equals(descontoOld.getMotivoDesconto()) ||
											!desconto.getObAcaoCorretiva().equals(descontoOld.getObAcaoCorretiva()) ||
											(desconto.getObDesconto() != null && descontoOld.getObDesconto() == null) ||
											(desconto.getObDesconto() == null && descontoOld.getObDesconto() != null ) ||
											(desconto.getObDesconto() != null && descontoOld.getObDesconto() != null && !desconto.getObDesconto().equals(descontoOld.getObDesconto()))
									)
							)
						)
					)
				){
				return true;
			}
		}
		
		return false;
	}

	private String storeQuestionamentoFatura(Desconto desconto) {
            
		String tpSituacaoAprovacao = null;
		
		//Retorna o id e o valor do desconto
    	Desconto descontoOld = findByIdDevedorDocServFatDisconnected(desconto.getDevedorDocServFat().getIdDevedorDocServFat());
    	
    	//Se já existia um desconto para este Desconto
    	if (descontoOld != null){
    		//Se o desconto achado for diferente daquele que vem da tela, lançar uma exception
    		if (!(descontoOld.getIdDesconto()).equals(desconto.getIdDesconto())){
    			throw new BusinessException("LMS-36144");	
    		} else {
    			desconto.setVersao(descontoOld.getVersao());
    		}
    	}
    	
		if (findValorMinimoDocumentoDesconto().compareTo(desconto.getVlDesconto()) < 0){
        	// Caso não exista um desnconto anterior, gerar pendencia
        	// Caso exista um desconto anterior, e o valor tenha aumentado, gerar pendencia
        	// Caso exista um desconto anterior, e a situação de aprovação não seja 'A' e
        	// o valor tenha diminuido, gerar pendencia.
        	if (descontoOld == null || "R".equals(desconto.getTpSituacaoAprovacao().getValue()) ||
            		(descontoOld != null && 
	            		((desconto.getVlDesconto().compareTo(descontoOld.getVlDesconto()) > 0) ||
	            		(!"A".equals(desconto.getTpSituacaoAprovacao().getValue()) && 
	            		(	desconto.getVlDesconto().compareTo(descontoOld.getVlDesconto()) < 0 || 
	            			!desconto.getTpSetorCausadorAbatimento().getValue().equals(descontoOld.getTpSetorCausadorAbatimento().getValue()) ||
	            			!desconto.getMotivoDesconto().equals(descontoOld.getMotivoDesconto()) ||
	            			!desconto.getObAcaoCorretiva().equals(descontoOld.getObAcaoCorretiva()) ||
	            			!desconto.getObDesconto().equals(descontoOld.getObDesconto())
	            		)
	            	)))){
        				DomainValue tpDocumento = new DomainValue(questionamentoFaturasService.convertDocServicoDocQuest(desconto.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico().getValue()));
        		
		        		QuestionamentoFatura questionamentoFatura = new QuestionamentoFatura();
		        		questionamentoFatura.setBlConcedeAbatimentoSol(true);
		        		questionamentoFatura.setMotivoDesconto(desconto.getMotivoDesconto());
		        		questionamentoFatura.setTpSetorCausadorAbatimento(desconto.getTpSetorCausadorAbatimento());
		        		questionamentoFatura.setObAbatimento(desconto.getObDesconto());
		        		questionamentoFatura.setObAcaoCorretiva(desconto.getObAcaoCorretiva());
		        		questionamentoFatura.setVlAbatimentoSolicitado( desconto.getVlDesconto());

						// Pega a data de emissão do documento desde DoctoServiço.
		        		String data = desconto.getDevedorDocServFat()
								.getDoctoServico().getDhEmissao().toLocalDate().toString("dd/MM/yyyy");
						DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
						YearMonthDay dataEmissao = new YearMonthDay(formatter.parseDateTime(data));
						
						questionamentoFatura.setDtEmissaoDocumento(dataEmissao);
		        		
		        		questionamentoFatura.setDoctoServico(desconto.getDevedorDocServFat().getDoctoServico());
		        		questionamentoFatura.setTpDocumento(tpDocumento);
		        		
		        		desconto.setIdPendencia( (Long) questionamentoFaturasService.storeGenericQuestionamentoFatura(questionamentoFatura, desconto.getDevedorDocServFat().getFilial(), 
								        						desconto.getDevedorDocServFat().getCliente(), 
								        						desconto.getDevedorDocServFat().getVlDevido(),
								        						desconto.getIdPendencia()));
		        		
		        		 tpSituacaoAprovacao = "E";//Em aprovação
            }
        } else {
        	cancelWorkflow(desconto);
        	tpSituacaoAprovacao = "A";//Aprovado
        }
        
        if (tpSituacaoAprovacao != null){
        	desconto.setTpSituacaoAprovacao(domainValueService.findDomainValueByValue("DM_STATUS_WORKFLOW",tpSituacaoAprovacao));
        }

		return tpSituacaoAprovacao;
    }

    /**
     * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
     * @param desconto Desconto a ser salvo
     * @return Id do desconto salvo
     */
    public java.io.Serializable storePadrao(Desconto desconto){
    	getDescontoDAO().getSessionFactory().getCurrentSession().flush();
    	getDescontoDAO().getSessionFactory().getCurrentSession().clear();
        Serializable retorno = super.store(desconto);
        return retorno;
    }

    /**
     * Salva o desconto.
     *
     * @author Hector Julian Esnaola Junior
     * @since 27/07/2007
     *
     * @param desconto
     * @return
     *
     */
    public Desconto storeBasic(Desconto desconto) {
    	return getDescontoDAO().storeBasic(desconto);
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param dao Instância do DAO.
     */
    public void setDescontoDAO(DescontoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DescontoDAO getDescontoDAO() {
        return (DescontoDAO) getDao();
    }

	public FaturaService getFaturaService() {
		return faturaService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}
    
    /**
     * Busca os dados do responsável e o valor do documento
     * @param tfm Critérios de pesquisa
     * @return Desconto com os dados necessários aninhados
     */
    public java.io.Serializable findDadosClienteEValores(TypedFlatMap tfm) {
    	
    	List descontos = getDescontoDAO().findDadosClienteEValores(tfm);
        
        DevedorDocServFat ddsf = null;
        Desconto desconto = null;
        
        for (Iterator iter = descontos.iterator(); iter.hasNext();) {
        	
        	ListOrderedMap element = (ListOrderedMap) iter.next();
        	
        	ddsf = new DevedorDocServFat();
        	ddsf.setIdDevedorDocServFat(Long.valueOf(((Number)element.get("ID_DEVEDOR_DOC_SERV_FAT")).longValue()));
        	
        	Filial filialDevedor = new Filial();
        	filialDevedor.setIdFilial(Long.valueOf(((Number)element.get("ID_FILIAL")).longValue()));
        	filialDevedor.setSgFilial((String)element.get("SG_FILIAL_COBRANCA"));
        	
        	Pessoa pesFilCobranca = new Pessoa();
        	pesFilCobranca.setNmFantasia((String)element.get("NM_FILIAL_COBRANCA"));        	
        	
        	filialDevedor.setPessoa(pesFilCobranca);
        	
        	ddsf.setFilial(filialDevedor);
        	
        	Cliente c = new Cliente();
        	
        	c.setIdCliente(Long.valueOf(((Number)element.get("ID_CLIENTE")).longValue()));
        	
        	Pessoa p = new Pessoa();
        	
        	p.setNrIdentificacao(FormatUtils.formatIdentificacao((String) element.get("TP_IDENTIFICACAO"),(String)element.get("NR_IDENTIFICACAO")));
        	p.setNmPessoa((String)element.get("NM_PESSOA"));
        	
        	Moeda m = new Moeda();
        	m.setIdMoeda(Long.valueOf(((Number)element.get("ID_MOEDA")).longValue()));
        	m.setSgMoeda((String)element.get("SG_MOEDA"));
        	m.setDsSimbolo((String)element.get("DS_SIMBOLO"));
        	
        	ddsf.setVlDevido(new BigDecimal(((Number)element.get("VL_DEVIDO")).doubleValue()));
        	
        	Servico servico = new Servico();
        	servico.setIdServico(Long.valueOf(((Number)element.get("ID_SERVICO")).longValue()));
        	DomainValue dvModal = new DomainValue();
        	dvModal.setValue((String)element.get("TP_MODAL"));
        	servico.setTpModal(dvModal);

        	DomainValue dvAbrangencia = new DomainValue();
        	dvAbrangencia.setValue((String)element.get("TP_ABRANGENCIA"));
        	servico.setTpAbrangencia(dvAbrangencia);

        	DoctoServico ds = new DoctoServico();
        	
        	ds.setMoeda(m);
        	c.setPessoa(p);
        	ds.setServico(servico);
        	
        	ddsf.setDoctoServico(ds);
        	ddsf.setCliente(c);
        	
        	desconto = new Desconto();
        	desconto.setDevedorDocServFat(ddsf);
        	
        	desconto.setNrDocumento(((Number)element.get("NR_DOCUMENTO")).toString());
			
		}
        
        if( desconto == null ){
            throw new BusinessException("LMS-36007");
        }
        
        return desconto;
    } 
    
    /**
	 * @author Mickaël Jalbert
	 * @since 01/12/2006
	 * 
	 * @param desconto
	 */
	private void cancelWorkflow(Desconto desconto) {
		Long idPendencia = desconto.getIdPendencia();
		//Alteração
        if( desconto.getIdDesconto() != null ){
            //tpSituacaoAprovacao = 'E' - Em Aprovação
            if (desconto.getTpSituacaoAprovacao().getValue().equals("E") && idPendencia != null){
            	cancelPendenciaDesconto(desconto);
            }else {
                desconto.setIdPendencia(null);
            }
        }
	}

	/**
	 * Atualiza s
	 * 
	 * @param questionamentoFatura
	 */
	public void executeSynchronizeQuestionamentoFatura(QuestionamentoFatura questionamentoFatura){
		Long idDesconto = getDescontoDAO().findbyIdPendencia(questionamentoFatura.getIdQuestionamentoFatura());
		if(idDesconto != null){
			Desconto desconto = getDescontoDAO().findById(idDesconto);
			if(getFaturaService().isQuestionamentoFatura(desconto.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico())){
				if( !desconto.getTpSetorCausadorAbatimento().getValue().equals(questionamentoFatura.getTpSetorCausadorAbatimento().getValue()) ||
					! desconto.getMotivoDesconto().equals(questionamentoFatura.getMotivoDesconto())){
					desconto.setTpSetorCausadorAbatimento(questionamentoFatura.getTpSetorCausadorAbatimento());
					desconto.setMotivoDesconto(questionamentoFatura.getMotivoDesconto());
					getDao().store(desconto);
				}
			}
		}
	}
	
	public List findPendentesByDevedorDocServFat(Long idDevedorDocServFat){ 
	     if (idDevedorDocServFat != null){ 
	    	 List lstDesconto = this.getDescontoDAO().findByPendentesDevedorDocServFatSituacao(idDevedorDocServFat); 
	    	 return lstDesconto; 
	     } else { 
	    	 return null; 
	     } 
	}
	
	/**
     * Retorna a soma do valor devido e do valor do desconto por fatura.
     * 
     * @param Long idFatura
     * @return TypedFlatMap
     * */    
    public TypedFlatMap findSomaByFatura(Long idFatura){
    	flush();
    	return getDescontoDAO().findSomaByFatura(idFatura);
    }
    

    public void setDomainValueService(DomainValueService domainValueService) {
        this.domainValueService = domainValueService;
    }

    public void setWorkflowPendenciaService(
            WorkflowPendenciaService workflowPendenciaService) {
        this.workflowPendenciaService = workflowPendenciaService;
    }

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setDevedorDocServFatService(
			DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public void setMotivoDescontoService(MotivoDescontoService motivoDescontoService) {
		this.motivoDescontoService = motivoDescontoService;
	}

	/**
	 * Busca o número de registros que será exibido na grid
	 * @param tfm Critérios de pesquisa : tpDesconto e idReciboDemonstrativo
	 * @return Inteiro representando o número de resultados da pesquisa para a grid
	 */
	public Integer getRowCountByReciboDemonstrativo(TypedFlatMap tfm) {		
		return getDescontoDAO().getRowCountByReciboDemonstrativo(tfm);
	}

	/**
	 * Faz a pesquisa que será exibida na grid
	 * @param tfm Critérios de pesquisa : tpDesconto e idReciboDemonstrativo
	 * @return ResultSetPage Dados da pesquisa e dados de paginação
	 */
	public ResultSetPage findPaginatedByReciboDemonstrativo(TypedFlatMap tfm) {		
		return getDescontoDAO().findPaginatedByReciboDemonstrativo(tfm);
	}

	/**
	 * Busca um Recibo Desconto ou um Demonstrativo Desconto
	 * @param tfm idReciboDemonstrativo : Identificador do objeto de busca
	 *            tpDescontoSelecionado : tipo de desconto selecionado (Recibo ou Demonstrativo)
	 * @return Map de dados
	 */
	public Object[] findByIdFromReciboDemonstrativo(Long idReciboDemonstrativo, String tpDescontoSelecionado) {
		return getDescontoDAO().findByIdFromReciboDemonstrativo(idReciboDemonstrativo, tpDescontoSelecionado);
	}

	/**
	 * Método chamado ao se cancelar um tipo desconto (Recibo ou Demonstrativo)
	 * @param idReciboDemonstrativo Identificador do tipo de desconto
	 * @param tpDesconto <code>R</code> para Recibo Desconto, <code>D</code> para Demonstrativo Desconto
	 */
	public void executeCancelar(Long idReciboDemonstrativo, String tpDesconto) {
		
		List descontos = findDescontosByReciboDesconto(idReciboDemonstrativo, tpDesconto);
		
		for (Iterator iter = descontos.iterator(); iter.hasNext();) {
			
			Desconto element = (Desconto) iter.next();
			
			if( tpDesconto.equals("R") ){
				element.setReciboDesconto(null);
			} else if( tpDesconto.equals("D") ) {
				element.setDemonstrativoDesconto(null);				
			}
			
			storePadrao(element);
			
		}
	}
	
	public BigDecimal findValorDesconto(Long idDesconto){
		return getDescontoDAO().findValorDesconto(idDesconto);
	}
	
	/**
	 * Retorna a lista de desconto do recibo informado passando por parametro o tipo (se é um recibo ou um demostrativo)
	 * 
	 * @author Mickaël Jalbert
	 * @since 13/07/2006
	 * 
	 * @param Long idReciboDemonstrativo
	 * @param String tpDesconto
	 * 
	 * @return List
	 */
	public List findDescontosByReciboDesconto(Long idReciboDemonstrativo, String tpDesconto){
		return getDescontoDAO().findDescontosByReciboDesconto(idReciboDemonstrativo, tpDesconto);
	}

	/**
	 * Busca dados para a listagem da aba Documento de Serviço
	 * @param idReciboDemonstrativo Identificador do tipo de desconto (Recibo ou Demonstrativo)
	 * @param tpDesconto Tipo de desconto (Recibo ou Demonstrativo)
	 * @return Lista de dados
	 */
	public ResultSetPage findDadosListagemAbaDocumentosServico(TypedFlatMap tfm) {
		return getDescontoDAO().findDadosListagemAbaDocumentosServico(tfm);
	}

	/**
	 * Busca quantos dados serão exibidos na listagem da aba Documento de Serviço
	 * @param tfm Critérios de pesquisa
	 * @return Inteiro representando a quantidade de dados resultante da pesquisa
	 */
	public Integer getRowCountDadosListagemAbaDocumentosServico(TypedFlatMap tfm) {
		return getDescontoDAO().getRowCountDadosListagemAbaDocumentosServico(tfm);
	}

	/**
	 * Retorna true se o documento de serviço possui descontos vinculados a ele
	 * @param idDoctoServico
	 * @return
	 */
	public boolean validatePossuiDesconto(Long idDoctoServico){
		return getDescontoDAO().validatePossuiDesconto(idDoctoServico);
	}
	
	/**
	 * @author José Rodrigo Moraes
	 * @since  01/06/2006
	 * 
     * Verifica se o documento de serviço informado está Emitido ou Bloqueado (CTRC,NFS,NFT)
     * @param idDoctoServico Identificador do Documento de servico 
     * @param tpDocumentoServico Tipo do documento de serviço
     * @return <code>true</code> se o documento de serviço está Emitido ou Bloqueado, caso contrário levanta a BusinessException LMS-36142
     * @exception com.mercurio.adsm.framework.BusinessException LMS-36142
     */
	public Boolean validateDoctoEmitidoBloqueado(Long idDoctoServico, String tpDocumentoServico) {
		boolean alerta = false;
		if (tpDocumentoServico.equals("CTR") || tpDocumentoServico.equals("NFT") || tpDocumentoServico.equals("NTE")
				|| tpDocumentoServico.equals("CTE")) {
			final Conhecimento conhecimento = conhecimentoService.findByIdInitLazyProperties(idDoctoServico, false);
			if( !conhecimento.getTpSituacaoConhecimento().getValue().equals("E") && !conhecimento.getTpSituacaoConhecimento().getValue().equals("B") ){
				alerta = true;
			}	
		} else if (tpDocumentoServico.equals("NFS") || tpDocumentoServico.equals("NSE")) {
			final NotaFiscalServico notaFiscalServico = notaFiscalServicoService.findById(idDoctoServico);
			if( !notaFiscalServico.getTpSituacaoNf().getValue().equals("E") ){
				alerta = true;
			}
		}		
		
		if( alerta ){
			throw new BusinessException("LMS-36142");
		}
		return Boolean.TRUE;
	}
	
	public BigDecimal findValorMinimoDocumentoDesconto(){
		return (BigDecimal)configuracoesFacade.getValorParametro("VL_MINIMO_DOCUMENTO_DESCONTO");
	}
	
	/**
	 * Atualiza os descontos da fatura informada com a situação informada quando não tem pendencias associadas
	 * 
	 * @author Mickaël Jalbert
	 * @since 22/11/2006
	 * 
	 * @param Long idFatura
	 * @param String tpSituacaoAprovacao
	 */
	public void updateDescontoByIdFatura(Long idFatura, String tpSituacaoAprovacao){
		getDescontoDAO().updateDescontoByIdFatura(idFatura, tpSituacaoAprovacao);
	}
	
	/**
	 * Remover os descontos pendente da fatura informada
	 * 
	 * @author Mickaël Jalbert
	 * @since 22/11/2006
	 * 
	 * @param Long idFatura
	 */
	public void removeDescontoByIdFatura(Long idFatura){
		getDescontoAnexoService().removeDescontoAnexoByIdFatura(idFatura);
		getDescontoDAO().removeDescontoByIdFatura(idFatura);
	}
	
    /**
     * Retorna o desconto do devedor informado
     * 
     * @author Mickaël Jalbert
     * @since 22/12/2006
     * 
     * @param Long idDevedorDocServFat
     * @return Desconto
     */
    public Desconto findByIdDevedorDocServFatDisconnected(Long idDevedorDocServFat){
    	return getDescontoDAO().findByIdDevedorDocServFatDisconnected(idDevedorDocServFat);
    }
    
    
    /**
     * Retorna a soma do valor dos descontos aprovados por fatura.
     * 
     * @author Mickaël Jalbert
     * @since 04/01/2006
     * 
     * @param Long idFatura
     * @return List
     * */
    public BigDecimal findSomaAprovadoByFatura(Long idFatura){
    	return getDescontoDAO().findSomaAprovadoByFatura(idFatura);
    }
    
    /**
     * Retorna a lista de desconto do reciboDesconto informado.
     * 
     * @author Mickaël Jalbert
     * @since 16/01/2007
     * 
     * @param idReciboDesconto
     * 
     * @return lista de desconto
     */ 
    public List findByReciboDesconto(Long idReciboDesconto){
    	return getDescontoDAO().findByReciboDesconto(idReciboDesconto);
    }    
	
    /**
     * Obtem o Desconto através do DevedorDocServFat
     * @param  idDevedor
     * @return Desconto
     */
	public Desconto findDescontoByIdDevedor(Long idDevedor){
		return getDescontoDAO().findDescontoByIdDevedor(idDevedor);
	}	
	
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setNotaFiscalServicoService(
			NotaFiscalServicoService notaFiscalServicoService) {
		this.notaFiscalServicoService = notaFiscalServicoService;
	}

	public void setQuestionamentoFaturasService(
			QuestionamentoFaturasService questionamentoFaturasService) {
		this.questionamentoFaturasService = questionamentoFaturasService;
	}
    
	public void cancelPendenciaDesconto(Desconto desconto) {
		if(desconto.getIdPendencia() != null){
			if( faturaService.isQuestionamentoFatura(desconto.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico()) ){
				QuestionamentoFatura questionamentoFatura = questionamentoFaturasService.findById(desconto.getIdPendencia());
				questionamentoFaturasService.storeCancelarQuestionamento(questionamentoFatura, "");
			} else {
				workflowPendenciaService.cancelPendencia( desconto.getIdPendencia() );
			}
		}
	}
    
	public List<Desconto> findDescontoByDoctoServico(Long idDoctoServico){
		return getDescontoDAO().findDescontoByDoctoServico(idDoctoServico);
	}
	
	/**
	 * 
	 * @param idDevedorDocServFat
	 * @return
	 */
    public Desconto findByIntegracao(Long idDevedorDocServFat){
    	return this.getDescontoDAO().findByIntegracao(idDevedorDocServFat);
    }
	
	public ConteudoParametroFilialService getConteudoParametroFilialService() {
		return conteudoParametroFilialService;
	}

	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public Long findbyIdPendencia(Long idPendencia) {
		return getDescontoDAO().findbyIdPendencia(idPendencia);
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}

	public void setDescontoAnexoService(DescontoAnexoService descontoAnexoService) {
		this.descontoAnexoService = descontoAnexoService;
	}

	public DescontoAnexoService getDescontoAnexoService() {
		return descontoAnexoService;
	}

}