package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.Desconto;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.dao.DevedorDocServFatDAO;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.CartaCorrecaoService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoInternacionalService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalServicoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.sim.model.dao.LMIntegranteDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.devedorDocServFatService"
 */
public class DevedorDocServFatService extends CrudService<DevedorDocServFat, Long> {

	private ConhecimentoService conhecimentoService;
	private NotaDebitoNacionalService notaDebitoNacionalService;
	private CtoInternacionalService ctoInternacionalService;
	private NotaFiscalServicoService notaFiscalServicoService;
	private AgendaTransferenciaService agendaTransferenciaService;
	private CartaCorrecaoService cartaCorrecaoService;
	private TransferenciaService transferenciaService;
	private DescontoService descontoService;
	private LMIntegranteDAO lmIntegranteDao;
	private ConfiguracoesFacade configuracoesFacade;
	private DoctoServicoService doctoServicoService;
	private BloqueioFaturamentoService bloqueioFaturamentoService;
	private ParametroGeralService parametroGeralService;
	
	
	private DomainValueService domainValueService;
	public void setDomainValueService(DomainValueService domainValueService){
		this.domainValueService = domainValueService;
	}
	
	public void clear() {
		getDevedorDocServFatDAO().clear();
	}
	
	/**
	 * Recupera uma instância de <code>DevedorDocServFat</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public DevedorDocServFat findById(java.lang.Long id) {
        return (DevedorDocServFat)super.findById(id);
    }
    
    public DevedorDocServFat findByIdInitLazyProperties(Long id, boolean initializeLazyProperties) {
    	return (DevedorDocServFat)super.findByIdInitLazyProperties(id, initializeLazyProperties);
    }
    
    /**
     * Retorna o devedorDocServFat com o desconto e o doctoServico 'fetchado' do
     * idDevedorDocServFat informado 
     * 
     * @author Mickaël Jalbert
     * @since 10/07/2006
     * 
     * @param Long idDevedorDocServFat
     * 
     * @return DevedorDocServFat
     */
    public DevedorDocServFat findByIdWithDocumentoAndDesconto(Long idDevedorDocServFat) {
        return getDevedorDocServFatDAO().findByIdWithDocumentoAndDesconto(idDevedorDocServFat);
    }    
    
    /**
     * Retorna o devedorDocServFat com o doctoServicoFin 'fetchado' do
     * idDevedorDocServFat informado 
     * 
     * @author Mickaël Jalbert
     * @since 02/01/2007
     * 
     * @param Long idDevedorDocServFat
     * 
     * @return DevedorDocServFat
     */
	public DevedorDocServFat findByIdWithDocumentoFin(Long idDevedorDocServFat) {
		return getDevedorDocServFatDAO().findByIdWithDocumentoFin(idDevedorDocServFat);
	}    
    
    /**
     * Retorna o devedorDocServFat o doctoServico 'fetchado' do
     * idDevedorDocServFat informado 
     * 
     * @author Mickaël Jalbert
     * @since 04/12/2006
     * 
     * @param Long idDevedorDocServFat
     * 
     * @return DevedorDocServFat
     */
    public DevedorDocServFat findByIdWithDocumento(Long idDevedorDocServFat) {
        return getDevedorDocServFatDAO().findByIdWithDocumento(idDevedorDocServFat);
    }    
    
	/**
	 * Retorna a lista de ids devedor por fatura informada.
	 *
	 * @author Mickaël Jalbert
	 * 11/05/2006
	 *
	 * @param Long idFatura
	 * @return List
	 */
    public List findIdsByFatura(Long idFatura) {
        return this.getDevedorDocServFatDAO().findIdsByFatura(idFatura);
    }     
    
	/**
	 * Retorna a lista de devedor por fatura informada.
	 *
	 * @author Mickaël Jalbert
	 * 15/03/2006
	 *
	 * @param Long idFatura
	 * @return List
	 */
    public List findByFatura(Long idFatura) {
        return this.getDevedorDocServFatDAO().findByFatura(idFatura);
    }    
    
    /**
     * Busca a lista de devedores (DevedorDocServFat) para a ET Manter Devedores do Documento de Serviço
     * @param tfm Critérios de Pesquisa
     * @return ResultSetPage com a lista de devedores e dados de paginação
     */
    public List findDevedores(TypedFlatMap tfm){
    	
    	String tpDocumentoServico = tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico");
    	Long idDoctoServico = tfm.getLong("devedorDocServFat.doctoServico.idDoctoServico");
    	Boolean buscaSimples = tfm.getBoolean("buscaSimples");
    	
    	if( buscaSimples == null ){
    		buscaSimples = Boolean.FALSE;
    	}
    	
    	// Valida se existe devedor diferente de em carteira ou pendente
    	if (this.findDevedorSituacaoCobranca(idDoctoServico)){
    		throw new BusinessException("LMS-36006");
    	}
    	
    	// Se o documento de serviço tiver um desconto vinculado, aborta
    	if (descontoService.validatePossuiDesconto(idDoctoServico)){
    		throw new BusinessException("LMS-36144");
    	}
    	
    	//Regra 2.1
    	//Se for conhecimento
    	if (tpDocumentoServico.equalsIgnoreCase("CTR") || tpDocumentoServico.equalsIgnoreCase("NFT") ||
    			tpDocumentoServico.equalsIgnoreCase("CTE") || tpDocumentoServico.equalsIgnoreCase("NTE")){
    		
    		List lst = getDevedorDocServFatDAO().findDevedoresByConhecimento(tfm);
    		
    		for (Iterator iter = lst.iterator(); iter.hasNext();) {
				
    			Map	element = (Map) iter.next();
    			
    			//Verifica se existe agenda de transferencia pendente
    			agendaTransferenciaService.validateDevedorDocServFat((Long)element.get("idDevedorDocServFat"), null);
    			//Verifica se existe transferencia pendente
    			transferenciaService.validateDevedorDocServFat((Long)element.get("idDevedorDocServFat"));
    			
    			DomainValue dvSituacaoConhecimento = (DomainValue)element.get("tpSituacaoConhecimento");
    			
    			if( !buscaSimples.booleanValue() ){

	    			if( !dvSituacaoConhecimento.getValue().equalsIgnoreCase("E") && !dvSituacaoConhecimento.getValue().equalsIgnoreCase("B")){
	    				throw new BusinessException("LMS-36007");
	    			}	    	
    			}
    			
    			
    			DomainValue dvIdentificacao = (DomainValue)element.get("tpIdentificacao");
    			element.put("devedor",FormatUtils.formatIdentificacao(dvIdentificacao.getValue(),(String)element.get("nrIdentificacao")) + 
    					              " - " + 
    					              element.get("nmPessoa"));
				
			}
    		
    		return lst; 
    		
    	}else 
    		//Cto Internacional
    		if (tpDocumentoServico.equalsIgnoreCase("CRT")){
    		
    		List lst = getDevedorDocServFatDAO().findDevedoresByCtoInternacional(tfm);
    		
    		for (Iterator iter = lst.iterator(); iter.hasNext();) {
				
    			Map	element = (Map) iter.next();
    			
    			DomainValue dvSituacaoCtoInternacional = (DomainValue)element.get("tpSituacaoCrt");
    			
    			if( !buscaSimples.booleanValue() ){
    			
	    			if( !dvSituacaoCtoInternacional.getValue().equalsIgnoreCase("E")){
	    				throw new BusinessException("LMS-36007");
	    			}
	    			
	    			TypedFlatMap typedFlatMap = new TypedFlatMap();
	    			typedFlatMap.put("ctoInternacional.idDoctoServico",(Long)element.get("idCtoInternacional"));
	    			
	    			List cartasCorrecao = cartaCorrecaoService.find(typedFlatMap);
	    			
	    			if( cartasCorrecao == null || cartasCorrecao.isEmpty() ){
	    				throw new BusinessException("LMS-36023");
	    			}
    			}    			
    			
    			
    			DomainValue dv = (DomainValue)element.get("tpIdentificacao");
				
    			element.put("devedor",FormatUtils.formatIdentificacao(dv.getValue(),(String)element.get("nrIdentificacao")) + 
    					              " - " + 
    					              element.get("nmPessoa"));
				
			}
    		
    		return lst;    		
    		
    	}else 
    		//Nota Fiscal Serviço
    		if (tpDocumentoServico.equalsIgnoreCase("NFS") || tpDocumentoServico.equalsIgnoreCase(ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA)){
    		
    		List lst = getDevedorDocServFatDAO().findDevedoresByNotaFiscalServico(tfm);
    		
    		for (Iterator iter = lst.iterator(); iter.hasNext();) {
				
    			Map	element = (Map) iter.next();
    			
    			//Verifica se existe agenda de transferencia pendente
    			agendaTransferenciaService.validateDevedorDocServFat((Long)element.get("idDevedorDocServFat"), null);
    			//Verifica se existe transferencia pendente
    			transferenciaService.validateDevedorDocServFat((Long)element.get("idDevedorDocServFat"));    			
    			
    			DomainValue dvSituacaoNotaFiscal = (DomainValue)element.get("tpSituacaoNf");
    			
    			if( !buscaSimples.booleanValue() ){
    			
	    			if( !dvSituacaoNotaFiscal.getValue().equalsIgnoreCase("E")){
	    				throw new BusinessException("LMS-36007");
	    			}
    			}
    			
    			DomainValue dv = (DomainValue)element.get("tpIdentificacao");
				
    			element.put("devedor",FormatUtils.formatIdentificacao(dv.getValue(),(String)element.get("nrIdentificacao")) + 
    					              " - " + 
    					              element.get("nmPessoa"));
				
			}
    		
    		return lst;    		
    		    		
    	}
    	
    	return null;
    }
    
    /**
     * Busca a quantidade de registros que serão exibidos na listagem de devedores (DevedorDocServFat) para a ET Manter Devedores do Documento de Serviço
     * @param tfm Critérios de Pesquisa 
     * @return Inteiro representando a quantidade de registros a serem exibidos
     */
    public Integer getRowCountDevedores(Map map){
    	
    	TypedFlatMap tfm = (TypedFlatMap) map;
    	
    	String tpDocumentoServico = tfm.getString("doctoServico.tpDocumentoServico");
    	
		if (tpDocumentoServico.equalsIgnoreCase("CTR") || tpDocumentoServico.equalsIgnoreCase("NFT")
				|| tpDocumentoServico.equalsIgnoreCase("CTE") || tpDocumentoServico.equalsIgnoreCase("NTE")) {
    		return getDevedorDocServFatDAO().getRowCountDevedoresByConhecimento(map);
    	}else if (tpDocumentoServico.equalsIgnoreCase("CRT")){
    		return getDevedorDocServFatDAO().getRowCountDevedoresByCtoInternacional(map);
		} else if (tpDocumentoServico.equalsIgnoreCase("NFS") || tpDocumentoServico.equalsIgnoreCase("NSE")) {
    		return getDevedorDocServFatDAO().getRowCountDevedoresByNotaFiscalServico(map);
    	}
    	
    	return null;
    	
    }
    
    /**
     * Valida se o devedor e o documento de servico informado estão disponivel para mudança
     * Obs: o documento de serviço tem que vir 'fetchado' (com fetch)
     * 
     * @author Mickaël Jalbert
     * @since 05/05/2006
     * 
     * @param DevedorDocServFat devedorDocServFat
     * */
    public void validateDisponibilidadeDevedorDocServFat(DevedorDocServFat devedorDocServFat){
    	doctoServicoService.validatePermissaoDocumentoUsuario(devedorDocServFat.getDoctoServico().getIdDoctoServico(), devedorDocServFat.getFilial().getIdFilial());
    	
    	validateDevedorDocServFatSemFilial(devedorDocServFat, null);
    }    
    
    /**
     * Valida se o devedor e o documento de servico informado estão disponivel para mudança
     * Obs: o documento de serviço tem que vir 'fetchado' (com fetch)
     * 
     * @author Mickaël Jalbert
     * @since 05/05/2006
     * 
     * @param DevedorDocServFat devedorDocServFat
     * */
    public void validateDisponibilidadeDevedorDocServFat(DevedorDocServFat devedorDocServFat, Long idAgendaTransferencia){
    	doctoServicoService.validatePermissaoDocumentoUsuario(devedorDocServFat.getDoctoServico().getIdDoctoServico(), devedorDocServFat.getFilial().getIdFilial());
    	
    	validateDevedorDocServFatSemFilial(devedorDocServFat, idAgendaTransferencia);
    }    
	

    
    /**
     * Valida se o devedor e o documento de servico informado estão disponivel para mudança
     * Obs: o documento de serviço tem que vir 'fetchado' (com fetch)
     * 
     * @author Mickaël Jalbert
     * @since 05/05/2006
     * 
     * @param DevedorDocServFat devedorDocServFat
     * */
    public void validateDevedorDocServFatSemFilial(DevedorDocServFat devedorDocServFat, Long idAgendaTransferencia){
    	validateDevedorDocServFat(devedorDocServFat, idAgendaTransferencia);
    	
    	descontoService.validateDescontoPendenteWorkflow(devedorDocServFat.getIdDevedorDocServFat());
    }
    
    
    
    /**
     * Valida se o devedor e o documento de servico informado está disponivel para mudança
     * Obs: o documento de serviço tem que vir 'fetchado' (com fetch)
     * 
     * @author Mickaël Jalbert
     * @since 05/05/2006
     * 
     * @param DevedorDocServFat devedorDocServFat
     * */
    public void validateDevedorDocServFat(DevedorDocServFat devedorDocServFat, Long idAgendaTransferencia){
    	String tpSituacao = devedorDocServFat.getTpSituacaoCobranca().getValue();   	
    	
    	//Se a situação de cobrança do devedor for diferente a 'Pendente' e 'Em carteira', lançar uma exception
    	if (!tpSituacao.equals("P") && !tpSituacao.equals("C")) {
    		throw new BusinessException("LMS-36006");
    	}  	
    	
    	if (tpSituacao.equals("P")) {
    		doctoServicoService.validatePermissaoDocumentoUsuarioIgnoreMatriz(devedorDocServFat.getDoctoServico().getIdDoctoServico(), devedorDocServFat.getFilial().getIdFilial());;
    	} 
    	
    	String tpDocumento = devedorDocServFat.getDoctoServico().getTpDocumentoServico().getValue();
    	
    	//Se o tipo de documento for 'Conhecimento'
    	if (tpDocumento.equals("CTR") || tpDocumento.equals("NFT") || 
    			tpDocumento.equals("CTE") || tpDocumento.equals("NTE") ){
    		Map mapConhecimento = conhecimentoService.findTpFreteTpSituacaoByIdConhecimento(devedorDocServFat.getDoctoServico().getIdDoctoServico());
    		
    		tpSituacao = ((DomainValue)mapConhecimento.get("tpSituacaoConhecimento")).getValue();
    		
    		//Se a situação do conhecimento for diferente de 'Emitido' e 'Bloqueado', lançar uma exception
    		if (!tpSituacao.equals("E") && !tpSituacao.equals("B")){
    			throw new BusinessException("LMS-36007");
    		} 
    	}
    	
    	//Se o tipo de documento for 'Nota fiscal de serviço'
    	if ( tpDocumento.equals("NFS") || tpDocumento.equals(ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA)){
    		tpSituacao = notaFiscalServicoService.findTpSituacaoById(devedorDocServFat.getDoctoServico().getIdDoctoServico()).getValue();
    		
    		//Se a situação da nota fiscal de serviço for diferente de 'Emitido', lançar uma exception
    		if (!tpSituacao.equals("E")){
    			throw new BusinessException("LMS-36007");
    		}
    	}
    	
    	//Se o tipo de documento for 'CTO internacional'
    	if (tpDocumento.equals("CRT")){
    		tpSituacao = ctoInternacionalService.findTpSituacaoByCto(devedorDocServFat.getDoctoServico().getIdDoctoServico()).getValue();
    		
    		//Se a situação do CTO internacional for diferente de 'Emitido', lançar uma exception
    		if (!tpSituacao.equals("E")){
    			throw new BusinessException("LMS-36007");
    		}
    	}
    	
    	//Se o tipo de documento for 'Nota de debito nacional'
    	if (tpDocumento.equals("NDN")){
    		tpSituacao = notaDebitoNacionalService.findTpSituacaoByNotaDebitoNacional(devedorDocServFat.getDoctoServico().getIdDoctoServico()).getValue();
    		
    		//Se a situação da nota de debito nacional for diferente de 'Emitido', lançar uma exception
    		if (!tpSituacao.equals("E")){
    			throw new BusinessException("LMS-36096");
    		}
    	}   
    	
    	agendaTransferenciaService.validateDevedorDocServFat(devedorDocServFat.getIdDevedorDocServFat(), idAgendaTransferencia);
    	transferenciaService.validateDevedorDocServFat(devedorDocServFat);
    	bloqueioFaturamentoService.validateByIdDevedorDocServFat(devedorDocServFat.getIdDevedorDocServFat());
    	validateDescontoDevedor(devedorDocServFat);
    }

	private void validateDescontoDevedor(DevedorDocServFat devedorDocServFat) {
		Desconto desconto = devedorDocServFat.getDesconto();     
		// Caso exista desconto, e a situação do mesmo não seja 
    	// 'A', lançar exceção.
    	if (desconto != null
    			&& desconto.getIdPendencia() != null
    			&& desconto.getTpSituacaoAprovacao() != null
    			&& !"A".equals(desconto.getTpSituacaoAprovacao().getValue())) {
    		throw new BusinessException("LMS-36010");
    	}
	}    
    
    
    /**
     * Retorna uma lista de array da nota fiscal de conhecimento informado filtrado pelo cliente
     * 
     * @author Mickaël Jalbert
     * @since 15/05/2006
     * 
     * @param Integer nrNotaFiscal
     * @param Long idCliente
     * 
     * @return List
     * */
    public List findByNotaFiscalConhecimento(Long nrNotaFiscal, Long idCliente, YearMonthDay dataEmissao, Long idFilial){
    	return getDevedorDocServFatDAO().findByNotaFiscalConhecimento(nrNotaFiscal, idCliente, dataEmissao, idFilial);    	
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
    public java.io.Serializable store(DevedorDocServFat bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setDevedorDocServFatDAO(DevedorDocServFatDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DevedorDocServFatDAO getDevedorDocServFatDAO() {
        return (DevedorDocServFatDAO) getDao();
    }
    
    /**
     * 
     * @param map
     * @return
     */
    public List findNrDocumentoCTR(TypedFlatMap map){
    	map.put("desconto.tpSituacaoAprovacao", "E");
    	return conhecimentoService.findConhecimentoByDevedorDocServFat(map);
    }

    public List findNrDocumentoNDN(TypedFlatMap map){
    	map.put("desconto.tpSituacaoAprovacao", "E");
    	return notaDebitoNacionalService.findNrNotaDebitoNac(map);
    }
    
    public List findNrDocumentoCRT(TypedFlatMap map){
    	map.put("desconto.tpSituacaoAprovacao", "E");
    	return ctoInternacionalService.findNrCrt(map);
    }
    
    public List findNrDocumentoNFS(TypedFlatMap map){
    	map.put("desconto.tpSituacaoAprovacao", "E");
    	return notaFiscalServicoService.findNotaFiscalServicoByDevedorDocServFat(map);
    }
    
	public List findIdsByIdDoctoServico(Long idDoctoServico)
	{
		return getDevedorDocServFatDAO().findIdsByIdDoctoServico(idDoctoServico);
	}

	public void validateCTRCFaturado(Long idDoctoServico) {
		if (!getDevedorDocServFatDAO().findCtrcFaturado(idDoctoServico))
			throw new BusinessException("LMS-04115");
	}

	public boolean findCtrcFaturado(Long idDoctoServico) {
		return getDevedorDocServFatDAO().findCtrcFaturado(idDoctoServico);

	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setNotaDebitoNacionalService(
			NotaDebitoNacionalService notaDebitoNacionalService) {
		this.notaDebitoNacionalService = notaDebitoNacionalService;
	}

	public void setCtoInternacionalService(
			CtoInternacionalService ctoInternacionalService) {
		this.ctoInternacionalService = ctoInternacionalService;
	}

	public void setNotaFiscalServicoService(
			NotaFiscalServicoService notaFiscalServicoService) {
		this.notaFiscalServicoService = notaFiscalServicoService;
	}

	public void setAgendaTransferenciaService(
			AgendaTransferenciaService agendaTransferenciaService) {
		this.agendaTransferenciaService = agendaTransferenciaService;
	}

	public void setCartaCorrecaoService(CartaCorrecaoService cartaCorrecaoService) {
		this.cartaCorrecaoService = cartaCorrecaoService;
	}

	/**
     * Busca dados do devedor
     * @param id Identificador do Devedor detalhado
     * @return Mapa de dados do detalhamento do Devedor
     */
	public Map findByIdMaped(Long id) {
		return this.getDevedorDocServFatDAO().findByIdMaped(id);
	}
	
	/**
	 * Busca o devedor associado ao Documento de Serviço e ao Cliente
	 * @param idDoctoServico Identificador do Documento de Serviço
	 * @param idCliente Identificador do Cliente
	 * @return DevedorDocServFat associado ao documento de serviço e ao cliente
	 */
	public DevedorDocServFat findDevedorByIdDoctoServicoIdCliente(Long idDoctoServico, Long idCliente){
		return this.getDevedorDocServFatDAO().findDevedorByIdDoctoServicoIdCliente(idDoctoServico, idCliente);
	}	
	
	/**
     * 
     * @param criteria
     * @return
     */
    public List findDevedorDocServFatByDoctoServico(Long idDoctoServico){
    	return getDevedorDocServFatDAO().findDevedorDocServFatByDoctoServico(idDoctoServico);
    }
    
    /**
	 * Método responsável por buscar o detalhamento de um devedorDocServfat para a tela de devedores
	 * 
	 * @param idDevedorDocServFat
	 * @return
	 */
    public Map findDevedorDocServFatDetail(TypedFlatMap criteria){
    	
    	Object[] obj = getDevedorDocServFatDAO().findDevedorDocServFatDetail(criteria.getLong("idDevedorDocServFat"));
    	
		Map map = new HashMap();

		if(obj[0] != null){
			String nrIdentificacao = (String) obj[0];
			String tpIdentificacao = (String) obj[1];
			map.put("identificacao", FormatUtils.formatIdentificacao(tpIdentificacao, nrIdentificacao));
		}else{
			map.put("identificacao", "");
		}
		
		map.put("nmPessoa",obj[2]);
		map.put("dtVencimento",obj[3]);
		map.put("dtLiquidacao", obj[4]);
		
		if(obj[5] != null){
			map.put("tpSituacaoCobranca", (domainValueService.findDomainValueByValue("DM_STATUS_COBRANCA_DOCTO_SERVICO", (String)obj[5])).getDescription());
			map.put("tpSituacaoCobrancaHidden", ((String)obj[5]));
		}else{
			map.put("tpSituacaoCobranca", "");
		}
		
		map.put("fatura", obj[6]);
		map.put("redeco", obj[7]);
		map.put("recibo", obj[8]);
		map.put("boleto", obj[9]);
		map.put("relacaoCobranca", obj[10]);
		map.put("bdm", obj[11]);
		map.put("vlDesconto", obj[12]);
		map.put("dsMotivoDesconto", obj[13]);
		map.put("vlDevido", obj[14]);
		map.put("valorPago", obj[15]);
		
		if(obj[16] != null){
			map.put("tpMotivoDesconto", (domainValueService.findDomainValueByValue("DM_SETOR_CAUSADOR", (String)obj[16])).getDescription());
		}else{
			map.put("tpMotivoDesconto", "");
		}
		
		if(obj[17] != null){
			map.put("tpSituacaoAprovacao", (domainValueService.findDomainValueByValue("DM_STATUS_WORKFLOW", (String)obj[17])).getDescription());
		}else{
			map.put("tpSituacaoAprovacao", "");
		}
		
		map.put("nrNotaDebitoNac", obj[18]);
		map.put("idFatura", obj[19]);
		map.put("idBdm", obj[20]);
		map.put("idRelCob", obj[21]);
		
		if (obj[22] != null){
			map.put("finalidadeRedeco", domainValueService.findDomainValueByValue("DM_FINALIDADE_REDECO", (String)obj[22]).getDescription());
		}
		
		map.put("filialCobranca", (String)obj[23]);
		
		map.put("nrFat", (Long)obj[24]);
		map.put("sgFilialFat", (String)obj[25]);
		map.put("nmFantasiaFat", (String)obj[26]);
		
		map.put("nrRc", (Long)obj[27]);
		map.put("sgFilialRc", (String)obj[28]);
		map.put("nmFantasiaRc", (String)obj[29]);
		
		map.put("nrBdm", (Long)obj[30]);
		map.put("sgFilialBdm", (String)obj[31]);
		map.put("nmFantasiaBdm", (String)obj[32]);
		
		Desconto desconto = descontoService.findByDevedorDocServFat((Long)obj[33]);
		
		//Se tem desconto e o desconto é aprovado
		if (desconto != null){
			if (desconto.getReciboDesconto() != null){
				map.put("doctoDesconto", configuracoesFacade.getMensagem("recibo") + " " + desconto.getReciboDesconto().getFilial().getSgFilial() + " " + desconto.getReciboDesconto().getNrReciboDesconto());
			} else if (desconto.getDemonstrativoDesconto() != null){
				map.put("doctoDesconto", configuracoesFacade.getMensagem("demonstrativo") + " " + desconto.getDemonstrativoDesconto().getFilial().getSgFilial() + " " + desconto.getDemonstrativoDesconto().getNrDemonstrativoDesconto());
			} 
		}
		
		if(obj[34] != null)
			map.put("dsDivisaoCliente", (String)obj[34]);
		else
			map.put("dsDivisaoCliente", "");
		
		map.put("dsTipoLogradouro", (String)obj[35]);
		map.put("dsEndereco", (String)obj[36]);
		map.put("nrEndereco", (String)obj[37]);
		map.put("dsComplemento", (String)obj[38]);
		map.put("dsBairro", (String)obj[39]);
		map.put("nmMunicipio", (String)obj[40]);
		map.put("sgUnidadeFederativa", (String)obj[41]);
		map.put("nmPais", (String)obj[42]);
		map.put("nrCep", (String)obj[43]);
		
		map.put("nrTelefone", (String)obj[44]);
		map.put("nrDdd", (String)obj[45]);
		map.put("nrDdi", (String)obj[46]);	
		
		map.put("idFilialRc", (Long)obj[47]);
		map.put("idFilialBdm", (Long)obj[48]);
		
		map.put("recebimentosParciais", obj[49]);
		
		// Se o Documento estiver liquidado o Saldo Devedor é ZERO
		if (obj[4] == null) {
			map.put("saldoDevedor", obj[50]);
		} else {
			map.put("saldoDevedor", BigDecimal.ZERO);
		}
		
		return map;
    }
    
    /**
     * @author José Rodrigo Moraes
     * @since 31/05/2006
     * 
     * Retorna somente um devedorDocServFat sem nenhum outro objeto associado junto
     * @param idDevedorDocServFat Identificador do Devedor
     * @return DevedorDocServFat
     */
    public DevedorDocServFat findByIdSimplificado(Long idDevedorDocServFat){
    	return getDevedorDocServFatDAO().findByIdSimplificado(idDevedorDocServFat);
    }
    
    /**
     * @author edenilsonf
     * @param idDoctoServico
     * @return true se existe devedor diferente de em carteira ou pendente
     */
    public boolean findDevedorSituacaoCobranca(Long idDoctoServico){
    	return getDevedorDocServFatDAO().findDevedorSituacaoCobranca(idDoctoServico);
    }
    
    /**
     * Retorna a lista dos devedores do redeco (passando por fatura ou recibo), vem o desconto
     * 'fetchado'
     * 
     * @author Mickaël Jalbert
     * @since 13/07/2006
     * 
     * @param Long idDoctoServico
     * 
     * @return List
     */
    public List findByRedeco(Long idRedeco, Boolean blInnerJoinDesconto){
    	List lstDevedor = getDevedorDocServFatDAO().findByRedeco(idRedeco, blInnerJoinDesconto);
    	
    	return lstDevedor;
    }
    
    /**
     * Atualiza todos os devedores com a situação de cobrança informada da fatura informada.
     * 
     * @author Mickaël Jalbert
     * @since 20/07/2006
     * 
     * @param Long idFatura
     * @param String tpSituacaoCobranca
     */
    public void executeUpdateSituacaoByIdFatura(Long idFatura, String tpSituacaoCobranca){
    	getDevedorDocServFatDAO().executeUpdateSituacaoByIdFatura(idFatura, tpSituacaoCobranca);
    }
    
    /**
     * Retorna o devedor do manifesto de viagem informado
     * 
     * @author Mickaël Jalbert
     * @since 06/10/2006
     * 
     * @param Long idManifesto
     * @return DevedorDocServFat
     */
    public DevedorDocServFat findByManifestoNacionalCto(Long idManifestoNacionalCto){
    	return getDevedorDocServFatDAO().findByManifestoNacionalCto(idManifestoNacionalCto);
    }
    
    /**
     * Retorna o devedore do manifesto de entrega informado
     * 
     * @author Mickaël Jalbert
     * @since 06/10/2006
     * 
     * @param Long idManifesto
     * @return DevedorDocServFat
     */
    public DevedorDocServFat findByManifestoEntregaDocumento(Long idManifestoEntregaDocumento){
    	return getDevedorDocServFatDAO().findByManifestoEntregaDocumento(idManifestoEntregaDocumento);
    }

	public void setTransferenciaService(TransferenciaService transferenciaService) {
		this.transferenciaService = transferenciaService;
	}

	public void setDescontoService(DescontoService descontoService) {
		this.descontoService = descontoService;
	}
	public List findDevedoresByIdDoctoServico(Long idDoctoServico){
		return lmIntegranteDao.findDevedoresByIdDoctoServico(idDoctoServico);
	}

	public void setLmIntegranteDao(LMIntegranteDAO lmIntegranteDao) {
		this.lmIntegranteDao = lmIntegranteDao;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
 
	public void setBloqueioFaturamentoService(BloqueioFaturamentoService bloqueioFaturamentoService) {
		this.bloqueioFaturamentoService = bloqueioFaturamentoService;
	}
	
	public void setParametroGeralServiceService(ParametroGeralService parametroGeralServiceService) {
		this.parametroGeralService = parametroGeralServiceService;
	}
 
	  /**
     * Busca o devedorDocServFat de acordo com o idDoctoServico
     *
     * @author Hector Julian Esnaola Junior
     * @since 18/01/2007
     *
     * @param idDoctoServico
     * @return
     *
     */
    public DevedorDocServFat findDevedorDocServFatByIdDoctoServico(Long idDoctoServico){
    	return getDevedorDocServFatDAO().findDevedorDocServFatByIdDoctoServico(idDoctoServico);
    }
    
    /**
     * Liquida os devedores da fatura
     *
     * @author Hector Julian Esnaola Junior
     * @since 29/01/2007
     *
     * @param idDevedorDocServFat
     *
     */
    public void liquidaDevedorDocServFat(Long idFatura, YearMonthDay dtLiquidacao, String tpSituacaoCobranca){
    	getDevedorDocServFatDAO().liquidaDevedorDocServFat(idFatura, dtLiquidacao, tpSituacaoCobranca);
    }
    
    /**
     * Busca o devedorDocServFat de acordo com o nrDoctoServico e a filialOrigem do documento de serviço
     *
     * @author Hector Julian Esnaola Junior
     * @since 18/01/2007
     *
     * @param nrDoctoServico
     * @param idFilialOrigem
     * @return
     *
     */
    public DevedorDocServFat findDevedorDocServFatByNrDoctoServicoAndIdFilialOrigem(Long nrDoctoServico, Long idFilialOrigem){
    	return getDevedorDocServFatDAO().findDevedorDocServFatByNrDoctoServicoAndIdFilialOrigem(nrDoctoServico, idFilialOrigem);
    }
    
    /**
     * Busca o devedorDocServFat de acordo com os parâmetros
     * @param idDoctoServico
     * @return
     */
    public DevedorDocServFat findDevedorDocServFatByIntegracao(Long nrConhecimento, Long idFilial, String tpDocumentoServico){
    	return getDevedorDocServFatDAO().findDevedorDocServFatByIntegracao(nrConhecimento, idFilial, tpDocumentoServico);
    }

    public void generateDevedorDocServFat(DoctoServico doctoServico){
    	YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
    	DevedorDocServFat ddsf = new DevedorDocServFat();

    	String idEmpresaMercurio = parametroGeralService.findSimpleConteudoByNomeParametro(ConstantesExpedicao.ID_EMPRESA_MERCURIO);		
		Cliente cliente = new Cliente();
		cliente.setIdCliente(Long.valueOf(idEmpresaMercurio));
		ddsf.setCliente(cliente);
		
		ddsf.setFilial(doctoServico.getFilialByIdFilialOrigem());
		ddsf.setDoctoServico(doctoServico);
		ddsf.setVlDevido(BigDecimal.ZERO);
		ddsf.setTpSituacaoCobranca(new DomainValue(ConstantesExpedicao.DOCTO_SERVICO_LIQUIDADO));
		ddsf.setDtLiquidacao(dataAtual);
		
		store(ddsf);
	}
    
	/**
	 * Ref.	LMS-3910
	 * 
	 * @param idDevedor
	 * @return Boolean
	 */
	public Boolean findDevedorSituCobrancaCP(Long idDevedor) {
		return getDevedorDocServFatDAO().findDevedorSituCobrancaCP(idDevedor);
	}

}