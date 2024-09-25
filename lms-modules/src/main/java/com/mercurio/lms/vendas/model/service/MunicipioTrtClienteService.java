package com.mercurio.lms.vendas.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.MunicipioTrtCliente;
import com.mercurio.lms.vendas.model.TrtCliente;
import com.mercurio.lms.vendas.model.dao.MunicipioTrtClienteDAO;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.CampoHistoricoWorkflow;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.TabelaHistoricoWorkflow;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.ocorrenciaClienteService"
 */
public class MunicipioTrtClienteService extends CrudService<MunicipioTrtCliente, Long> {
	private static final String APROVADO = "A";

	private static final String CARACTER_DATA_EM_BRANCO_OU_SEM_FIM = "-";

	private static final int FIRST = 0;
	
	private ClienteService clienteService;	
	private MunicipioService municipioService;
	private MunicipioTrtClienteService municipioTrtClienteService; 
	private ConfiguracoesFacade configuracoesFacade;
	private WorkflowPendenciaService workflowPendenciaService;
	private PessoaService pessoaService;
	private TrtClienteService trtClienteService;
	
	/**
	 * Recupera uma inst�ncia de <code>OcorrenciaCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	@Override
	public MunicipioTrtCliente findById(java.lang.Long id) {
		return (MunicipioTrtCliente)super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	@Override
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
	@Override
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
	@Override
	public java.io.Serializable store(MunicipioTrtCliente bean) {
		return super.store(bean);
	}
	
	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setMunicipioTrtClienteDAO(MunicipioTrtClienteDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private MunicipioTrtClienteDAO getMunicipioTrtClienteDAO() {
		return (MunicipioTrtClienteDAO) getDao();
	}
	
	public List<MunicipioTrtCliente> findTRTVigenteParaCliente(Long idClienteDevedor){
		return getMunicipioTrtClienteDAO().findTRTVigenteParaCliente(idClienteDevedor);
	}
	
	public String cobraPorTabelaMunicipio(Long idMunicipioEntrega, Long tabelaPreco) {
		List<Object[]> cobraMunicipio = getMunicipioTrtClienteDAO().cobraPorTabelaMunicipio(idMunicipioEntrega, tabelaPreco);
		if(cobraMunicipio.size() < 1){
			return null;
		}
		return (String)((Object)cobraMunicipio.get(FIRST));
	}

	public List<MunicipioTrtCliente> cobraPorTabela(Long tabelaPreco) {
		List<Object[]> cobraMunicipio = getMunicipioTrtClienteDAO().cobraPorTabela( tabelaPreco);
		if(cobraMunicipio.size() > 0){
			List<MunicipioTrtCliente> municipios = new ArrayList<MunicipioTrtCliente>();
			for(Object[] municipioObj : cobraMunicipio){
				if(municipioObj.length == 2){
					MunicipioTrtCliente municipioTRT = new MunicipioTrtCliente();
					municipioTRT.setBlCobraTrt((Boolean) "S".equals((String)municipioObj[0]));
					
					Municipio municipio = new Municipio();
					municipio.setIdMunicipio((Long) municipioObj[1]);
					municipioTRT.setMunicipio(municipio);
					
					municipios.add(municipioTRT);
				}
			}
			return municipios ;
		}
		return null;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public java.io.Serializable store(TypedFlatMap map){
		TypedFlatMap retorno = new TypedFlatMap();
		
		Long idTrtCliente = map.getLong("idTrtCliente");
		TrtCliente trtCliente = new TrtCliente();
		
		if(map.getLong("idTabelaPreco") != null){
			trtCliente.setIdTrtCliente(idTrtCliente); 
			trtCliente.setIdTabelaPreco(map.getLong("idTabelaPreco"));
			trtCliente.setTpSituacaoAprovacao(new DomainValue(APROVADO)); 
			trtCliente.setTrtClienteOriginal(null);
			trtCliente.setDtVigenciaInicial(map.getYearMonthDay("dtVigenciaInicial"));
			trtCliente.setDtVigenciaFinal(map.getYearMonthDay("dtVigenciaFinal") == null ? JTDateTimeUtils.MAX_YEARMONTHDAY : map.getYearMonthDay("dtVigenciaFinal"));
		}
		
		if(map.getLong("cliente.idCliente") != null){
			Cliente cliente = new Cliente();
			cliente.setIdCliente(map.getLong("cliente.idCliente"));
			trtCliente.setCliente(cliente);
			
			trtCliente.setDtVigenciaInicial(JTDateTimeUtils.MAX_YEARMONTHDAY);
			trtCliente.setDtVigenciaFinal(JTDateTimeUtils.MAX_YEARMONTHDAY);

			if(idTrtCliente != null){
				TrtCliente trtClienteOriginal = new TrtCliente();
				trtClienteOriginal.setIdTrtCliente(idTrtCliente);
				trtCliente.setTrtClienteOriginal(trtClienteOriginal);
				retorno.put("possuiFilhoEmAprovacao", true);
			}

			trtCliente.setDtVigenciaInicialSolicitada(map.getYearMonthDay("dtVigenciaInicialSolicitada"));
			if (map.getYearMonthDay("dtVigenciaFinalSolicitada") != null){
				trtCliente.setDtVigenciaFinalSolicitada(map.getYearMonthDay("dtVigenciaFinalSolicitada"));
			} else {
				trtCliente.setDtVigenciaFinalSolicitada(JTDateTimeUtils.MAX_YEARMONTHDAY);
			}
		}
		
		trtClienteService.store(trtCliente);
		storeMunicipiosTrtCliente(trtCliente, (List<TypedFlatMap>)map.get("municipioCobraTRT"));
		
		trtCliente.setDsMotivoSolicitacao(map.getString("dsMotivoSolicitacao"));
		
		if(map.getLong("cliente.idCliente") != null){
			generatePendenciaWorkflow(trtCliente);
		}
		
		if(idTrtCliente == null){
			retorno.put("idTrtCliente", trtCliente.getIdTrtCliente());
			retorno.put("tpSituacaoAprovacao", trtCliente.getTpSituacaoAprovacao().getValue());
		}
    	
 		return retorno;
	}

	/**
	 * Remove todos os registros de MunicipioTrtCliente relacionados com a
	 * TrtCliente informada, e cria novos registros de acordo com a lista
	 * municipioCobraTRT.
	 * 
	 * @param trtCliente
	 * @param municipioCobraTRT
	 */
	private void storeMunicipiosTrtCliente(TrtCliente trtCliente, List<TypedFlatMap> municipioCobraTRT){
		getMunicipioTrtClienteDAO().removeMunicipioTrtClienteByIdTrtCliente(trtCliente.getIdTrtCliente());
		
		if(municipioCobraTRT != null){
			List<MunicipioTrtCliente> municipiosTrtCliente = new ArrayList<MunicipioTrtCliente>();
			
			for (TypedFlatMap mapMunicipioTRTCliente : municipioCobraTRT) {
				MunicipioTrtCliente municipioTrtCliente = new MunicipioTrtCliente();
				municipioTrtCliente.setTrtCliente(trtCliente);
				Municipio municipio = new Municipio();
				municipio.setIdMunicipio(mapMunicipioTRTCliente.getLong("municipio.idMunicipio"));
				municipioTrtCliente.setMunicipio(municipio);
				municipioTrtCliente.setBlCobraTrt(mapMunicipioTRTCliente.getBoolean("blCobraTrt.value"));
				municipiosTrtCliente.add(municipioTrtCliente);
			}
			
			storeAll(municipiosTrtCliente);
		}
	}
	
	private void generatePendenciaWorkflow(TrtCliente trtCliente) {
		PendenciaHistoricoDTO pendenciaHistoricoDTO = new PendenciaHistoricoDTO();
		pendenciaHistoricoDTO.setIdFilial(SessionUtils.getFilialSessao().getIdFilial());
		pendenciaHistoricoDTO.setNrTipoEvento(ConstantesWorkflow.NR148_APROVACAO_TRT_CLIENTE);
		pendenciaHistoricoDTO.setIdProcesso(trtCliente.getIdTrtCliente());
		pendenciaHistoricoDTO.setDsProcesso(getDsProcessoWK(trtCliente));
		pendenciaHistoricoDTO.setDhLiberacao(JTDateTimeUtils.getDataHoraAtual());
		pendenciaHistoricoDTO.setTabelaHistoricoWorkflow(TabelaHistoricoWorkflow.TRT_CLIENTE);
		pendenciaHistoricoDTO.setCampoHistoricoWorkflow(CampoHistoricoWorkflow.TRTC);
		pendenciaHistoricoDTO.setDsObservacao(trtCliente.getDsMotivoSolicitacao());
		
		Pendencia pendencia = workflowPendenciaService.generatePendenciaHistorico(pendenciaHistoricoDTO);
		updateTRTCliente(trtCliente, pendencia);
	}

	private void updateTRTCliente(TrtCliente trtCliente, Pendencia pendencia) {
		trtCliente.setPendencia(pendencia);
		trtCliente.setTpSituacaoAprovacao(pendencia.getTpSituacaoPendencia());
		trtClienteService.store(trtCliente);
	}
	
	private String getDsProcessoWK(TrtCliente trtCliente) {
		Pessoa pessoa = pessoaService.findById(trtCliente.getCliente().getIdCliente());
		return configuracoesFacade.getMensagem("LMS-01256", new Object[] { 
				getDataFormatadaDsProcessoWK(trtCliente.getDtVigenciaInicialSolicitada()), 
				getDataFormatadaDsProcessoWK(trtCliente.getDtVigenciaFinalSolicitada()),
				FormatUtils.formatIdentificacao(pessoa),
				pessoa.getNmPessoa(),
				trtCliente.getDsMotivoSolicitacao() });
	}
	
	private String getDataFormatadaDsProcessoWK(YearMonthDay data){
		if(data != null && !data.equals(JTDateTimeUtils.MAX_YEARMONTHDAY)){
			return JTDateTimeUtils.formatDateYearMonthDayToString(data);
		} else {
			return CARACTER_DATA_EM_BRANCO_OU_SEM_FIM;
		}
	}
	
	public List<MunicipioTrtCliente> findByIdTrtCliente(Long idTrtCliente){
		return getMunicipioTrtClienteDAO().findByIdTrtCliente(idTrtCliente);
	}

	public List<Map<String, Object>> findMunicipioTrtClienteByIdTrtCliente(Long idTrtCliente){
		return getMunicipioTrtClienteDAO().findMunicipioTrtClienteByIdTrtCliente(idTrtCliente);
	}
	
	public ClienteService getClienteService() {
		return clienteService;
	}
	

	public Long findTabelaPadraoModal(Long idServico){
		List<Object[]> idServicoReturn = getMunicipioTrtClienteDAO().findTabelaPadraoModal(idServico);
		if(idServicoReturn.size() < 1){
			return null;
		}
		return (Long)((Object)idServicoReturn.get(FIRST));
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public MunicipioService getMunicipioService() {
		return municipioService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

	public MunicipioTrtClienteService getMunicipioTrtClienteService() {
		return municipioTrtClienteService;
	}

	public void setMunicipioTrtClienteService(
			MunicipioTrtClienteService municipioTrtClienteService) {
		this.municipioTrtClienteService = municipioTrtClienteService;
	}

	public TrtClienteService getTrtClienteService() {
		return trtClienteService;
	}

	public void setTrtClienteService(TrtClienteService trtClienteService) {
		this.trtClienteService = trtClienteService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	

}