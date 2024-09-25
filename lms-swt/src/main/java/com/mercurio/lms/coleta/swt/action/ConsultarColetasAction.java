package com.mercurio.lms.coleta.swt.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.dto.ConsultarColetaDTO;
import com.mercurio.lms.coleta.model.service.DetalheColetaService;
import com.mercurio.lms.coleta.model.service.FuncionarioRegiaoService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.coleta.model.util.ConstantesColeta;
import com.mercurio.lms.coleta.model.util.TpStatusColetaConstants;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.param.ConsultarUsuarioLMSParam;
import com.mercurio.lms.configuracoes.model.service.FuncionarioService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RegiaoColetaEntregaFilService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.coleta.swt.consultarColetasAction"
 */

public class ConsultarColetasAction {
	
	private PedidoColeta pedidoColeta;
	private PedidoColetaService pedidoColetaService;
	private RegiaoColetaEntregaFilService regiaoColetaEntregaFilService;
	private FilialService filialService;
	private RotaColetaEntregaService rotaColetaEntregaService;
	private DetalheColetaService detalheColetaService;
	private ClienteService clienteService;
	private ServicoService servicoService;
	private FuncionarioRegiaoService funcionarioRegiaoService;
	private FuncionarioService funcionarioService;
	private UsuarioLMSService usuarioLMSService;
	
    public PedidoColeta getPedidoColeta() {
		return pedidoColeta;
	}
    
	public void setPedidoColeta(PedidoColeta pedidoColeta) {
		this.pedidoColeta = pedidoColeta;
	}
	
	public PedidoColetaService getPedidoColetaService() {
		return pedidoColetaService;
	}
	
	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}
	
	public RegiaoColetaEntregaFilService getRegiaoColetaEntregaFilService() {
		return regiaoColetaEntregaFilService;
	}

	public void setRegiaoColetaEntregaFilService(
			RegiaoColetaEntregaFilService regiaoColetaEntregaFilService) {
		this.regiaoColetaEntregaFilService = regiaoColetaEntregaFilService;
	}
	
	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	public FuncionarioService getFuncionarioService() {
		return funcionarioService;
	}

	public void setFuncionarioService(FuncionarioService funcionarioService) {
		this.funcionarioService = funcionarioService;
	}

	public RotaColetaEntregaService getRotaColetaEntregaService() {
		return rotaColetaEntregaService;
	}

	public void setRotaColetaEntregaService(
			RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}

	public DetalheColetaService getDetalheColetaService() {
		return detalheColetaService;
	}

	public void setDetalheColetaService(DetalheColetaService detalheColetaService) {
		this.detalheColetaService = detalheColetaService;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public ServicoService getServicoService() {
		return servicoService;
	}

	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}

	public FuncionarioRegiaoService getFuncionarioRegiaoService() {
		return funcionarioRegiaoService;
	}

	public void setFuncionarioRegiaoService(
			FuncionarioRegiaoService funcionarioRegiaoService) {
		this.funcionarioRegiaoService = funcionarioRegiaoService;
	}

	
	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
        this.getPedidoColetaService().removeById(id);
    }
	
	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 * @param ids lista com as entidades que dever�o ser removida.
	 * 
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	getPedidoColetaService().removeByIds(ids);
    }

    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public Serializable store(PedidoColeta bean) {
        return this.getPedidoColetaService().store(bean);
    }
    
    
    public PedidoColeta findById(java.lang.Long id) {
    	return this.getPedidoColetaService().findById(id);
    }
    
    /**
     * Retorna um map com os objetos a serem mostrados na grid.
     * 
     * @param Map criteria
     * @return
     */
    public ResultSetPage findPaginatedConsultarColetas(Map criteria) {
    	return this.getPedidoColetaService().findPaginatedConsultarColeta(populateConsultarColetaDTO(criteria), FindDefinition.createFindDefinition(criteria));
    }
    
    /**
     * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados 
     * parametros.
     * 
     * @param Map criteria
     * @return
     */
    public Integer getRowCountConsultarColetas(Map criteria) {    	    	
    	return this.getPedidoColetaService().getRowCountConsultarColeta(populateConsultarColetaDTO(criteria));
    }
    
    /**
     * @param criteria
     * @return ConsultarColetaDTO
     */
    private ConsultarColetaDTO populateConsultarColetaDTO(Map criteria){
    	ConsultarColetaDTO consultarColetaDTO = new ConsultarColetaDTO();
    	
    	consultarColetaDTO.setIdFilial(MapUtils.getLong(criteria, "idFilialResponsavel"));
    	consultarColetaDTO.setIdRotaColetaEntrega(MapUtils.getLong(criteria,"idRotaColetaEntrega"));
    	consultarColetaDTO.setIdRegiaoColetaEntregaFil(MapUtils.getLong(criteria,"idRegiaoColetaEntregaFil"));
    	consultarColetaDTO.setIdServico(MapUtils.getLong(criteria,"idServico"));
    	consultarColetaDTO.setIdCliente(MapUtils.getLong(criteria,"idCliente"));
    	consultarColetaDTO.setIdFilialDestino(MapUtils.getLong(criteria,"idFilialDetalhe"));
    	consultarColetaDTO.setIdDestino(MapUtils.getLong(criteria,"idFilialDetalhe"));
    	consultarColetaDTO.setIdUsuario(MapUtils.getLong(criteria,"idUsuario"));
    	consultarColetaDTO.setNrColeta(MapUtils.getLong(criteria,"nrColeta"));
    	consultarColetaDTO.setDhPedidoColetaInicial((DateTime) MapUtils.getObject(criteria, "dhPedidoColetaInicial"));
    	consultarColetaDTO.setDhPedidoColetaFinal((DateTime) MapUtils.getObject(criteria, "dhPedidoColetaFinal"));
    	consultarColetaDTO.setTpPedidoColeta(MapUtils.getString(criteria, "tpPedidoColeta"));
    	
    	/*
    	 * Define tipos de status da coleta. 
    	 */
    	List<String> listStatus = new ArrayList<String>();    	
    	addTpColeta(listStatus, criteria, "tpSCAberto", TpStatusColetaConstants.EM_ABERTO);    	
    	addTpColeta(listStatus, criteria, "tpSCTransmitida", TpStatusColetaConstants.TRANSMITIDA);
    	addTpColeta(listStatus, criteria, "tpSCManifestada",TpStatusColetaConstants.MANIFESTADA);
    	addTpColeta(listStatus, criteria, "tpSCExecutada", TpStatusColetaConstants.EXECUTADA);
    	addTpColeta(listStatus, criteria, "tpSCCancelada", TpStatusColetaConstants.CANCELADA);
    	addTpColeta(listStatus, criteria, "tpSCAguardandoDescarga", TpStatusColetaConstants.AGUARDANDO_DESCARGA);
    	addTpColeta(listStatus, criteria, "tpSCEmDescarga", TpStatusColetaConstants.EM_DESCARGA);
    	addTpColeta(listStatus, criteria, "tpSCNoTerminal", TpStatusColetaConstants.NO_TERMINAL);
    	addTpColeta(listStatus, criteria, "tpSCFinalizada", TpStatusColetaConstants.FINALIZADA);
    	addTpColeta(listStatus, criteria, "tpSCNoManifesto", TpStatusColetaConstants.NO_MANIFESTO);
		
    	consultarColetaDTO.setTpsStatusColeta(listStatus);
		
		return consultarColetaDTO;
    }

	/**
	 * Adiciona um tipo de status de acordo com o valor do multicheckbox do filtro.
	 * 
	 * @param listStatus
	 * @param criteria
	 * @param key
	 * @param tpStatusColeta
	 */
    private void addTpColeta(List<String> listStatus, Map criteria, String key, TpStatusColetaConstants tpStatusColeta){
    	if(MapUtils.getBoolean(criteria, key)){
    		listStatus.add(tpStatusColeta.getValue());
    	}
    }
    
    /**
     * Finds padroes da tela de consultarColeta
     * 
     * @param Map
     * @return
     */
    public List findLookupFilial(Map criteria) {
    	List filiais = this.getFilialService().findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = filiais.iterator(); iter.hasNext();) {
			Filial filial = (Filial) iter.next();
			Map map = new HashMap();
			map.put("idFilial", filial.getIdFilial());
			map.put("sgFilial", filial.getSgFilial());
			map.put("nmPessoa", filial.getPessoa().getNmPessoa());
			map.put("nmFantasia", filial.getPessoa().getNmFantasia());
			retorno.add(map);
		}    	
    	return retorno;
    }
        
    public List findLookupUsuario(Map criteria) {
		ConsultarUsuarioLMSParam cup = new ConsultarUsuarioLMSParam();
		
		cup.setNrMatricula((String)criteria.get("nrMatricula"));
		cup.setNmUsuario((String)criteria.get("nmUsuario"));
		cup.setTpCategoriaUsuario((String)criteria.get("tpCategoriaUsuario"));

        return usuarioLMSService.findLookupSistema(cup);
    }
    
    public List findLookupRotaColetaEntrega(Map criteria){
    	List rotas = this.getRotaColetaEntregaService().findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = rotas.iterator(); iter.hasNext();) {
			RotaColetaEntrega rotaColetaEntrega = (RotaColetaEntrega) iter.next();
			Map map = new HashMap();
			map.put("idRotaColetaEntrega", rotaColetaEntrega.getIdRotaColetaEntrega());
			map.put("nrRota", rotaColetaEntrega.getNrRota());
			map.put("dsRota", rotaColetaEntrega.getDsRota());
			retorno.add(map);
		}
    	return retorno;
    }

    public List findRegiaoColetaEntregaFil(Map criteria) {
    	TypedFlatMap tfmCriteria = new TypedFlatMap();
    	return this.getFuncionarioRegiaoService().findRegiaoColetaEntregaByFilial(tfmCriteria);
    }
    
    public List findDetalheColetas(Map criteria) {
    	return this.getDetalheColetaService().find(criteria);
    }
    
    public List findLookupCliente(Map criteria){
    	List clientes = this.getClienteService().findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = clientes.iterator(); iter.hasNext();) {
			Cliente cliente = (Cliente) iter.next();
			Map map = new HashMap();
			map.put("idCliente", cliente.getIdCliente());
			map.put("nmPessoa", cliente.getPessoa().getNmPessoa());
			map.put("nrIdentificacao", cliente.getPessoa().getNrIdentificacao());
			map.put("nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(cliente.getPessoa()));
			retorno.add(map);
		}
    	return retorno;
    }
    
    /**
     * Retorna o idServico e dsServico.
     * @param criteria
     * @return
     */
    public List findServico(Map criteria) {
    	List servicos = this.getServicoService().find(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = servicos.iterator(); iter.hasNext();) {
			Servico servico = (Servico) iter.next();
			Map map = new HashMap();
			map.put("idServico", servico.getIdServico());
			map.put("dsServico", servico.getDsServico());
			retorno.add(map);
		}
    	return retorno;
    }
    
    /**
     * Busca o usuario da sessao e retorna para a tela
     * 
     * @return
     */
    public Map findFilialUsuarioLogado() {
    	Map mapSessao = new HashMap();
    	
    	Filial filial = SessionUtils.getFilialSessao();    	    	
    	mapSessao.put("idFilialSessao", filial.getIdFilial());
    	mapSessao.put("sgFilialSessao", filial.getSgFilial());
    	mapSessao.put("nmFantasiaSessao", filial.getPessoa().getNmFantasia());
    	
    	DateTime dataHoraAtualMenos30Dias = JTDateTimeUtils.getDataHoraAtual();
    	dataHoraAtualMenos30Dias = dataHoraAtualMenos30Dias.minusDays(30);
    	mapSessao.put("dataHoraAtualMenos30Dias", dataHoraAtualMenos30Dias);
    	mapSessao.put("dataHoraAtual", JTDateTimeUtils.getDataHoraAtual());
    	    	
    	return mapSessao;
    }

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}
}
