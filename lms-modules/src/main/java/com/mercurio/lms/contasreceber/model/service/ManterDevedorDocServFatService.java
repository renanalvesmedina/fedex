package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.ItemTransferencia;
import com.mercurio.lms.contasreceber.model.MotivoTransferencia;
import com.mercurio.lms.contasreceber.model.Transferencia;
import com.mercurio.lms.contasreceber.model.dao.DevedorDocServFatDAO;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.manterDevedorDocServFatService"
 */
public class ManterDevedorDocServFatService {
	
	private ConfiguracoesFacade configuracoesFacade;
	private TransferenciaService transferenciaService;
	private ItemTransferenciaService itemTransferenciaService;
	private DevedorDocServFatService devedorDocServFatService;
	private ClienteService clienteService;
	private DoctoServicoService doctoServicoService;	
	private DevedorDocServFatDAO devedorDocServFatDAO;
	private PessoaService pessoaService;
	
    /**
     * Regra 2.6 - No caso da operação ser de Troca
     * @param tfm Dados a serem salvos
     */
    public void storeTrocar(TypedFlatMap tfm) {
    	
    	DevedorDocServFat devedorDocServFat = devedorDocServFatService.findById(tfm.getLong("idDevedorDocServFat"));

    	if (!SessionUtils.getFilialSessao().equals(devedorDocServFat.getFilial())){
    		throw new BusinessException("LMS-36210");
    	}

    	Filial filialOrigem = devedorDocServFat.getFilial();
    	Filial filialDestino = new Filial();
    	
    	String tpDocumento = tfm.getString("tipoDocumento");
    	
    	DivisaoCliente divNovoDevedor = null;
    	if(tfm.getLong("divisaoCliente.idDivisaoCliente") != null){
    		divNovoDevedor = new DivisaoCliente();
        	divNovoDevedor.setIdDivisaoCliente(tfm.getLong("divisaoCliente.idDivisaoCliente"));
    	}
    	
    	Cliente novoDevedor = clienteService.findById(tfm.getLong("novoDevedor.idCliente"));
    	
    	pessoaService.isCometaOrAracatuba(novoDevedor.getPessoa().getNrIdentificacao());
    	    	
    	//Quando é Conhecimento Internacional, não tem transferência 
    	if( tpDocumento.equalsIgnoreCase("CRT") ){    		
    		filialDestino.setIdFilial(novoDevedor.getFilialByIdFilialCobranca().getIdFilial());
    	
    	// Caso não seja conhecimento internacional,
    	// a filial do devedor esteja implantada no Lms
    	// e o parâmetro BL_LMS_INTEGRADO_CORPORATIVO
    	// seja N ou seja S e tipo de documento de serviço
    	// seja CTR, deve gerar a transferência.
    	} else if( lmsImplantadoFilial(devedorDocServFat.getFilial()) ){
    		filialDestino = (Filial) SessionContext.get(SessionKey.FILIAL_KEY);
    		
        	Transferencia transferencia = new Transferencia();
        	ItemTransferencia itemTransferencia = new ItemTransferencia();
        	
        	Long ultTransferencia = configuracoesFacade.incrementaParametroSequencial(filialOrigem.getIdFilial(),"ULT_TRANSFERENCIA", true);
        	
        	transferencia.setNrTransferencia(ultTransferencia);    		
        	
        	//Regra 2.6 - Item A
        	transferencia.setFilialByIdFilialOrigem(filialOrigem);
        	transferencia.setFilialByIdFilialDestino(filialDestino);
        	
        	transferencia.setDtEmissao(JTDateTimeUtils.getDataAtual());
        	transferencia.setTpSituacaoTransferencia(new DomainValue("RE"));//Recebida
        	transferencia.setTpOrigem(new DomainValue("M"));//Manual
        	transferencia.setDtRecebimento(JTDateTimeUtils.getDataAtual());
        	transferencia.setBlIndicadorImpressao(Boolean.TRUE);
        	transferencia.setDhTransmissao(null);
        	
        	transferenciaService.store(transferencia);
        	
        	//Regra 2.6 - Item B    	    	
        	
        	MotivoTransferencia motivoTransferencia = new MotivoTransferencia();
        	motivoTransferencia.setIdMotivoTransferencia(tfm.getLong("motivoTransferencia.idMotivoTransferencia"));
        	
        	Cliente antigoDevedor = new Cliente();
        	antigoDevedor.setIdCliente(tfm.getLong("idDevedorAnterior"));    	
        	
        	itemTransferencia.setDevedorDocServFat(devedorDocServFat);
        	itemTransferencia.setTransferencia(transferencia);
        	itemTransferencia.setClienteByIdNovoResponsavel(novoDevedor);
        	
        	// seta novaDivisao no devedor e no itemTransferencia
        	
            	
            itemTransferencia.setDivisaoClienteNovo(divNovoDevedor);
            itemTransferencia.setMotivoTransferencia(motivoTransferencia);
        	itemTransferencia.setClienteByIdAntigoResponsavel(antigoDevedor);
        	
        	if(tfm.getLong("idDivisaoClienteAnterior") != null){
        		DivisaoCliente divAntigoDevedor = new DivisaoCliente();
        		divAntigoDevedor.setIdDivisaoCliente(tfm.getLong("idDivisaoClienteAnterior"));
        		itemTransferencia.setDivisaoClienteAntigo(divAntigoDevedor);
        	}
        		
        	
        	itemTransferencia.setObItemTransferencia(configuracoesFacade.getMensagem("obItemTransferencia") + " " + tfm.getString("observacaoRecebimento"));
        	
        	itemTransferenciaService.store(itemTransferencia);    		
    	}

    	
    	
    	//Regra 2.6 - Item C
    	if( tpDocumento.equalsIgnoreCase("CRT") ){    		
    		devedorDocServFat.setFilial(novoDevedor.getFilialByIdFilialCobranca());
    	} else {
    		devedorDocServFat.setFilial((Filial) SessionContext.get(SessionKey.FILIAL_KEY));
    	}
    	
    	devedorDocServFat.setCliente(novoDevedor);
    	devedorDocServFat.setVlDevido(tfm.getBigDecimal("valor"));
    	devedorDocServFat.setDivisaoCliente(divNovoDevedor);
    	
    	devedorDocServFatService.store(devedorDocServFat);
    	
    	
    	//Regra 2.6 - Item D
    	atualizaValoresDevidos(tfm);
	}

    public void evict(Object object){
		getDevedorDocServFatDAO().getAdsmHibernateTemplate().evict(object);
	}
    
    /**
     * @author José Rodrigo Moraes
     * @since  28/06/2006
     * 
     * Verifica se a filial de cobrança está com o LMS implantado na data atual
     * 
     * @param filial Filial de cobrança
     * @return <code>true</code> Se a filial fizer parte do LMS e <code>false</code> caso ainda não tenha sido implantado
     */
    private boolean lmsImplantadoFilial(Filial filial) {
    	
    	boolean retorno = false; 
    	
    	YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
    	
    	if( filial.getDtImplantacaoLMS() != null && (JTDateTimeUtils.comparaData(dataAtual,filial.getDtImplantacaoLMS()) >= 0 )){
    		retorno = true;
    	}
    	
		return retorno;
	}

	/**
     * Regra 2.6 - No caso da operação ser Incluir
     * @param tfm Dados a serem salvos
     */	
    public void storeIncluir(TypedFlatMap tfm) {
			
		//Regra 2.6.2 - A
		DevedorDocServFat devedorDocServFat = new DevedorDocServFat();
		
		Cliente novoDevedor = clienteService.findById(tfm.getLong("novoDevedor.idCliente"));
		
		pessoaService.isCometaOrAracatuba(novoDevedor.getPessoa().getNrIdentificacao());
		
		// Seta a divisão caso a mesma exista
		if(tfm.getLong("divisaoCliente.idDivisaoCliente") != null){
			DivisaoCliente divNovoDevedor = new DivisaoCliente();
			divNovoDevedor.setIdDivisaoCliente(tfm.getLong("divisaoCliente.idDivisaoCliente"));
			devedorDocServFat.setDivisaoCliente(divNovoDevedor);
		}		
		
		DoctoServico doctoServico = doctoServicoService.findById(tfm.getLong("idDoctoServico"));
		
		tfm.put("vlDoctoServico",doctoServico.getVlTotalDocServico());		
				
		devedorDocServFat.setFilial(novoDevedor.getFilialByIdFilialCobranca());
		devedorDocServFat.setCliente(novoDevedor);    	
		devedorDocServFat.setDoctoServico(doctoServico);
		
		devedorDocServFat.setVlDevido(tfm.getBigDecimal("valor"));
		devedorDocServFat.setTpSituacaoCobranca(new DomainValue("P"));//Pendente
		
		devedorDocServFatService.store(devedorDocServFat);
		
		tfm.put("idDevedorDocServFat", devedorDocServFat.getIdDevedorDocServFat());
		
		devedorDocServFat = null;
		
		//Regra 2.6 - B
    	atualizaValoresDevidos(tfm);
		
	}
	
	/**
     * Busca os devedores e atualiza/deleta (devedores) no campo vl_devido de acordo com o valor informado na tela para
     * o devedor detalhado. 
     * @param tfm Deve ter tipoDocumento, idDoctoServico, filialOrigem.idFilial, idDevedorDocServFat, vlDoctoServico e valor
     */
    private void atualizaValoresDevidos(TypedFlatMap tfm) {
    	
    	TypedFlatMap map = new TypedFlatMap();
    	
    	String tpDocumento  	 = tfm.getString("tipoDocumento");
    	Long idDoctoServico 	 = tfm.getLong("idDoctoServico");
    	Long idFilialOrigem 	 = tfm.getLong("idFilialOrigem");
    	Long idDevedorDocServFat = tfm.getLong("idDevedorDocServFat");
    	BigDecimal valorTotal    = tfm.getBigDecimal("vlDoctoServico");
    	BigDecimal valor         = tfm.getBigDecimal("valor");
    	Boolean remover          = tfm.getBoolean("remover");
    	
    	DevedorDocServFat devedor = devedorDocServFatService.findByIdInitLazyProperties(idDevedorDocServFat, false);
    	
    	BigDecimal restante = valorTotal;
    	
    	if( valor == null ){
    		valor = devedor.getVlDevido();
    	} else {
    		restante = valorTotal.subtract(valor);
    	}
    	
    	map.put("devedorDocServFat.doctoServico.idDoctoServico",idDoctoServico);
    	map.put("devedorDocServFat.doctoServico.tpDocumentoServico",tpDocumento);
    	map.put("devedorDocServFat.filialByIdFilialOrigem.idFilial",idFilialOrigem);
    	map.put("_currentPage","1");
    	map.put("_pageSize","2");
    	map.put("buscaSimples",Boolean.TRUE);
    	
    	List lst = devedorDocServFatService.findDevedores(map);
    	
    	if( lst.size() > 1 ){
    	
	    	for (Iterator iter = lst.iterator(); iter.hasNext();) {
				
	    		Map element = (Map) iter.next();
				
	    		Long idDevedor = (Long) element.get("idDevedorDocServFat");
	    		
	    		DevedorDocServFat outroDevedor = null;
	    		
	    		if( idDevedor.compareTo(idDevedorDocServFat) == 0 ){
	    			outroDevedor = devedor;
	    		} else {
	    			outroDevedor = devedorDocServFatService.findByIdInitLazyProperties(idDevedor, false); 	    			
	    		}
	    		
	    		if( idDevedor.compareTo(idDevedorDocServFat) != 0 ){	    			
	    			
	    			if( remover != null && remover.booleanValue() ){
	    				outroDevedor.setVlDevido(valorTotal);
	    			} else {
	    				if( restante.compareTo(new BigDecimal(0)) == 0 ){	    						    						
	    					devedorDocServFatService.removeById(idDevedor);
	    				}else {
	    					outroDevedor.setVlDevido(restante);
	    				}
	    			}
	    			devedorDocServFatService.store(outroDevedor);	    			
	    		} else if( remover != null && remover.booleanValue() ){
	    			restante = valorTotal;    						
					devedorDocServFatService.removeById(idDevedor);
	    		}
			}
    	}
	} 
    
    /**
     * Remove um registro de DevedorDocServFat
     * @param id Identificador de DevedorDocServFat
     */
	public void removeById(Long id) {
		
		Map map = devedorDocServFatService.findByIdMaped(id);
		
		if( map != null ){
		
			TypedFlatMap tfm = new TypedFlatMap();
			
	    	tfm.put("tipoDocumento",map.get("tipoDocumento"));
	    	tfm.put("idDoctoServico",(Long)map.get("idDoctoServico"));
	    	tfm.put("idFilialOrigem", (Long)map.get("idFilialOrigem"));
	    	tfm.put("idDevedorDocServFat",(Long)map.get("idDevedorDocServFat"));
	    	tfm.put("vlDoctoServico",(BigDecimal)map.get("vlDoctoServico"));
	    	tfm.put("valor",(BigDecimal)map.get("valor"));
	    	tfm.put("remover",Boolean.TRUE);
			
			atualizaValoresDevidos(tfm);
			
			devedorDocServFatService.removeById(id);
		}
        
    }    
    

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setItemTransferenciaService(
			ItemTransferenciaService itemTransferenciaService) {
		this.itemTransferenciaService = itemTransferenciaService;
	}

	public void setTransferenciaService(TransferenciaService transferenciaService) {
		this.transferenciaService = transferenciaService;
	}

	public void setDevedorDocServFatService(
			DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public DevedorDocServFatDAO getDevedorDocServFatDAO() {
		return devedorDocServFatDAO;
	}

	public void setDevedorDocServFatDAO(DevedorDocServFatDAO devedorDocServFatDAO) {
		this.devedorDocServFatDAO = devedorDocServFatDAO;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}    
}