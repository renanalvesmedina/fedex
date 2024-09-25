package com.mercurio.lms.contasreceber.model.service;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.contasreceber.model.AgendaTransferencia;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.dao.AgendaTransferenciaDAO;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Classe de serviço para CRUD:   
 * <p>
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 *
 * @spring.bean id="lms.contasreceber.agendaTransferenciaService"
 */
public class AgendaTransferenciaService extends CrudService<AgendaTransferencia, Long> {
	
	private static final String DM_TIPO_DOCUMENTO_SERVICO = "DM_TIPO_DOCUMENTO_SERVICO";
	private Logger log = LogManager.getLogger(this.getClass());
	private DoctoServicoService doctoServicoService;
	private FilialService filialService;
	private DevedorDocServFatService devedorDocServFatService;
	private PessoaService pessoaService;
	private DescontoService descontoService;
	private BloqueioFaturamentoService bloqueioFaturamentoService;
	private ParametroGeralService parametroGeralService;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private MotivoTransferenciaService motivoTransferenciaService;
	private ConfiguracoesFacade configuracoesFacade;
	private ClienteService clienteService;
	private DivisaoClienteService divisaoClienteService;
	private DomainValueService domainValueService;

	/**
	 * Recupera uma instância de <code>AgendaTransferencia</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    @Override
    public AgendaTransferencia findById(java.lang.Long id) {
        return (AgendaTransferencia)super.findById(id);
    }
    
    public Integer getRowCount(Long idFilialOrigem, Long idFilialDestino, Long idClienteOrigem, Long idClienteDestino, String tpOrigem, Long idMotivo, Long idDevedorDocServFat, String tpDocumentoServico, Long idFilialOrigemDocumentoServico) {
    	return this.getAgendaTransferenciaDAO().getRowCount(idFilialOrigem, idFilialDestino, idClienteOrigem, idClienteDestino, tpOrigem, idMotivo, idDevedorDocServFat, tpDocumentoServico, idFilialOrigemDocumentoServico);
    }    
    
    public ResultSetPage findPaginated(Long idFilialOrigem, Long idFilialDestino, Long idClienteOrigem, Long idClienteDestino, String tpOrigem, Long idMotivo, Long idDevedorDocServFat, String tpDocumentoServico, Long idFilialOrigemDocumentoServico, FindDefinition findDef) {
    	return this.getAgendaTransferenciaDAO().findPaginated(idFilialOrigem, idFilialDestino, idClienteOrigem, idClienteDestino, tpOrigem, idMotivo, idDevedorDocServFat, tpDocumentoServico, idFilialOrigemDocumentoServico, findDef);
    }
    
    /**
     * Retorna a lista de Agenda de Transferencia do devedor informado.
     * 
     * @param idDevedor Long
     * @return List
     * @author Mickaël Jalbert
     * 17/03/2006
     */
    public List findByDevedorDocServFat(Long idDevedor) {
		return (List)this.getAgendaTransferenciaDAO().findByDevedorDocServFat(idDevedor, null);
    }
    
    public Object findByIdSpecific(Serializable id) {
		List list = (List)this.getAgendaTransferenciaDAO().findByIdSpecific(id);
		if (!list.isEmpty()) {
			return (Map) list.get(0);
		} else {
			return null;
		}
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    @Override
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }
    
    @Override
    protected AgendaTransferencia beforeInsert(AgendaTransferencia bean) {       	
    	// Atribuir o valor Manual(M) para o tipo de origem quando ele inserido a partir da tela.
    	DomainValue domainValue = new DomainValue();
    	domainValue.setValue("M");
    	((AgendaTransferencia)bean).setTpOrigem(domainValue);
    	
    	return super.beforeInsert(bean);
    }
    
    @Override
    protected AgendaTransferencia beforeStore(AgendaTransferencia bean) {
    	AgendaTransferencia agendaTransferencia = (AgendaTransferencia)bean;
    	
    	
    	//Buscar a pessoa do cliente
    	Pessoa pessoa = this.pessoaService.findById(agendaTransferencia.getCliente().getIdCliente());
    	//O cliente destino não pode ser da mercúrio.
    	if (pessoaService.validatePessoaFromMercurio(pessoa.getNrIdentificacao()).equals(Boolean.TRUE)){
    		DoctoServico doctoServico = doctoServicoService.findByIdWithRemetenteDestinatario(agendaTransferencia.getDevedorDocServFat().getDoctoServico().getIdDoctoServico());
    		
    		//Se o remetente e o destinatário não são da mercúrio, na pode ter transferência
    		if(pessoaService.validatePessoaFromMercurio(doctoServico.getClienteByIdClienteRemetente().getPessoa().getNrIdentificacao()).equals(Boolean.FALSE)
    			&& pessoaService.validatePessoaFromMercurio(doctoServico.getClienteByIdClienteDestinatario().getPessoa().getNrIdentificacao()).equals(Boolean.FALSE)
    		) {
    			throw new BusinessException("LMS-36018");
    		}
    		
    	}
    	
    	pessoaService.isCometaOrAracatuba(agendaTransferencia.getCliente().getPessoa().getNrIdentificacao());
    	
    	//A filial de origem não pode ser igual à filial de destino
    	if (agendaTransferencia.getFilialByIdFilialOrigem().getIdFilial().equals(agendaTransferencia.getFilialByIdFilialDestino().getIdFilial())){
    		throw new BusinessException("LMS-36025");
    	}
    	
    	DevedorDocServFat devedorDocServFat = this.devedorDocServFatService.findById(agendaTransferencia.getDevedorDocServFat().getIdDevedorDocServFat());    	    					
			
		// Valida se o documento tem desconto vinculado. se tiver, aborta
		if (descontoService.validatePossuiDesconto(devedorDocServFat.getDoctoServico().getIdDoctoServico())){
			throw new BusinessException("LMS-36144");
		}
		
		//Valida se não tem transferências pendentes e regras de disponibilidade do documento de serviço
		devedorDocServFatService.validateDisponibilidadeDevedorDocServFat(devedorDocServFat, agendaTransferencia.getIdAgendaTransferencia());
		
		//Verifica se o usuário tem direito de modificar o documento de serviço
		this.doctoServicoService.validatePermissaoDocumentoUsuario(agendaTransferencia.getDevedorDocServFat().getDoctoServico().getIdDoctoServico(), devedorDocServFat.getFilial().getIdFilial());
    	return super.beforeStore(bean);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    @Override
    public java.io.Serializable store(AgendaTransferencia bean) {    	
    	this.doctoServicoService.validatePermissaoDocumentoUsuario(bean.getDevedorDocServFat().getDoctoServico().getIdDoctoServico(), bean.getDevedorDocServFat().getFilial().getIdFilial());
    	bloqueioFaturamentoService.validateByIdDevedorDocServFat(bean.getDevedorDocServFat().getIdDevedorDocServFat());
        return super.store(bean);
    }

    boolean isNumber(String text){
		try{
			Integer.parseInt(text);
			return true;
		}catch(Exception e){
			return false;
		}
	}
    
    
    public TypedFlatMap storeGeraAnalisePlanilha(TypedFlatMap criteria){
        StringBuilder errors = new StringBuilder();
    	try {
			String file = new String(Base64Util.decode(criteria.getString("arquivo")));
			String[] lines = file.split("\n");
			
			Map<String, String> tpDocByDescriptionMap = this.createMapDomainTpDocumento();
			
			for(int i=1; i < lines.length; i++){
				
				int line = i+1;
				String[] rows = lines[i].split(";",-1);

				
				if ( rows.length < 4 || !isNumber(rows[2]) || rows[0].isEmpty() || rows[1].isEmpty() || rows[2].isEmpty()|| rows[3].isEmpty()){
					errors.append(configuracoesFacade.getMensagem("LMS-36317", new String[]{""+line})+"\n"); 
					continue;
				}
				String tpDocumento = rows[0].trim().replace("\r", "").replace("\t", "");
				String filialOrigem = rows[1].trim().replace("\r", "").replace("\t", "");
				String numero = rows[2].trim().replace("\r", "").replace("\t", "");
				String cnpj = rows[3].trim().replace("\r", "").replace("\t", "");
				String divisao = "";
				if ( rows.length == 5){
                    divisao = rows[4].trim().replace("\r", "").replace("\t", "").toLowerCase();
				}
				
				List filiais = filialService.findFilialBySgEmpresaLookup(filialOrigem, SessionUtils.getFilialSessao().getEmpresa().getIdEmpresa());
				if ( filiais == null || filiais.isEmpty() ){
					errors.append("Linha "+line+" "+configuracoesFacade.getMensagem("LMS-36275", new Object[]{filialOrigem})+"\n");
					continue;
				}
				Map<String,Object> filiaisMap = (Map<String, Object>) filiais.get(0);
				Filial filial = filialService.findById((Long) filiaisMap.get("idFilial"));
				Cliente cliente = clienteService.findByNrIdentificacao(cnpj);
				if ( cliente == null ){
					errors.append(configuracoesFacade.getMensagem("LMS-36319", new String[]{""+line,cnpj})+"\n");
					continue;
				}
				Long idCliente = cliente.getIdCliente();
				if ( cliente.getClienteMatriz() != null ){
					idCliente = cliente.getClienteMatriz().getIdCliente();
				}
				DivisaoCliente divisaoCliente = null;
				if ( !divisao.isEmpty() ){
                    divisaoCliente = divisaoClienteService.findDivisaoClienteByClienteAndSituacao(idCliente, divisao, "A");
					if ( divisaoCliente == null ){
						errors.append(configuracoesFacade.getMensagem("LMS-36320", new String[]{""+line,divisao})+"\n");
						continue;
					}
				}else{
					List divisoes = divisaoClienteService.findDivisaoClienteByClienteAndSituacao(idCliente, "A");
					if ( divisoes != null && !divisoes.isEmpty()  ){
						if ( divisoes.size() > 1 ){
							errors.append(configuracoesFacade.getMensagem("LMS-36321", new String[]{""+line,cnpj})+"\n");
							continue;
						}
						divisaoCliente = (DivisaoCliente) divisoes.get(0);
					}
				}
				
				String domain = tpDocByDescriptionMap.get(tpDocumento.toUpperCase());
				
				Long doctoServico = doctoServicoService.findDoctoServicoByTpDoctoByIdFilialOrigemByNrDocto(domain, filial.getIdFilial(), Long.valueOf(numero));
				if ( doctoServico == null ){
					errors.append(configuracoesFacade.getMensagem("LMS-36318", new String[]{""+line,rows[0],rows[1],rows[2]})+" \n");
					continue;
				}
				DoctoServico docto = doctoServicoService.findById(doctoServico);
                if ("NTE".equals(docto.getTpDocumentoServico().getValue()) || "NSE".equals(docto.getTpDocumentoServico().getValue())) {
					MonitoramentoDocEletronico monitoramento = monitoramentoDocEletronicoService.findMonitoramentoDocEletronicoByIdDoctoServico(doctoServico);
                    if (monitoramento != null && !"A".equals(monitoramento.getTpSituacaoDocumento().getValue())) {
						errors.append("Linha "+line+": "+configuracoesFacade.getMensagem("LMS-36282")+" \n");
						continue;	
					}
				}
				DevedorDocServFat devedorDoc = null;
				List doctos = devedorDocServFatService.findDevedorDocServFatByDoctoServico(docto.getIdDoctoServico());
				if ( doctos != null || !doctos.isEmpty() ){
					Map<String,Object> doctServFatMap = (Map<String,Object>) doctos.get(0);
					devedorDoc = devedorDocServFatService.findById((Long)doctServFatMap.get("idDevedorDocServFat"));
				}
				AgendaTransferencia agenda = new AgendaTransferencia();
				agenda.setCliente(cliente);
				agenda.setDevedorDocServFat(devedorDoc);
				agenda.setFilialByIdFilialOrigem(filialService.findFilialUsuarioLogado());
				agenda.setFilialByIdFilialDestino(cliente.getFilialByIdFilialCobranca());
				agenda.setMotivoTransferencia(motivoTransferenciaService.findById(Long.valueOf(parametroGeralService.findByNomeParametro("ID_MOTIVO_TRANSF_PLANILHA").getDsConteudo())));
				agenda.setDtPrevistaTransferencia(new YearMonthDay());
				agenda.setTpOrigem(new DomainValue("M"));
				agenda.setObAgendaTransferencia( configuracoesFacade.getMensagem("LMS-36322"));
				agenda.setDivisaoCliente( divisaoCliente);
				try{
					store(agenda);
				}catch(BusinessException b){
					errors.append("Linha "+line+": "+configuracoesFacade.getMensagem(b.getMessageKey())+" \n");
                    log.warn(b);
				}catch(Exception e){
					errors.append("Linha "+line+": "+ e.getMessage()+" \n");
                    log.warn(e);
				}
			}
		} catch (IOException e) {
            log.error(e);
			log.error(e);
		}
    	if ( errors.toString().isEmpty() ){
    		return null;
    	}
    	criteria.put("erro", errors.toString());
    	return criteria;
	}

	public Map<String, String> createMapDomainTpDocumento() {
		Map<String, String> tpDocByDescriptionMap = new TypedFlatMap();
		
		List<DomainValue> domainValues = domainValueService.findDomainValues(DM_TIPO_DOCUMENTO_SERVICO);
		for (DomainValue domainValue : domainValues) {
			tpDocByDescriptionMap.put(domainValue.getDescriptionAsString().toUpperCase(), domainValue.getValue());
		}
		return tpDocByDescriptionMap;
	}

    /**
     * @see this.validateDevedorDocServFat(String)
     */
    public void validateDevedorDocServFat(Long idDevedorDocServFat, Long idAgendaTransferencia){
    	String sgFilialOrigem = getAgendaTransferenciaDAO().findSgFilialOrigemByIdDevedor(idDevedorDocServFat, idAgendaTransferencia);
    	validateDevedorDocServFat(sgFilialOrigem);
    	
    }     

    /**
     * Valida se não existe transferência pendentes do devedor informado.
     * 
     * @param sgFilialOrigem
     * @author Mickaël Jalbert
     * 06/03/2006
     */
    public void validateDevedorDocServFat(String sgFilialOrigem){    	
    	if (StringUtils.isNotBlank(sgFilialOrigem)){
    		throw new BusinessException("LMS-36022", new Object[]{sgFilialOrigem});
    	}
    }
    
    /**
     * Método responsável por buscar AgendaTranferencia de acordo com os filtros  
     *
     * @param idFilialOrigem
     * @param tpOrigem
     * @return Lista de AgendaTranferencia
     */
    public List findAgendaTranferencia(Long idFilialOrigem, String tpOrigem){
		return getAgendaTransferenciaDAO().findAgendaTranferencia(idFilialOrigem, tpOrigem);
	}

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param dao Instância do DAO.
     */
    public void setAgendaTransferenciaDAO(AgendaTransferenciaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private AgendaTransferenciaDAO getAgendaTransferenciaDAO() {
        return (AgendaTransferenciaDAO) getDao();
    }

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setDevedorDocServFatService(DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setDescontoService(DescontoService descontoService) {
		this.descontoService = descontoService;
	}	

	public void setBloqueioFaturamentoService(BloqueioFaturamentoService bloqueioFaturamentoService) {
		this.bloqueioFaturamentoService = bloqueioFaturamentoService;
	}

	public void setMonitoramentoDocEletronicoService(
			MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

	public void setParametroGeralService(
			ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setMotivoTransferenciaService(
			MotivoTransferenciaService motivoTransferenciaService) {
		this.motivoTransferenciaService = motivoTransferenciaService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setDivisaoClienteService(
			DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
   }
