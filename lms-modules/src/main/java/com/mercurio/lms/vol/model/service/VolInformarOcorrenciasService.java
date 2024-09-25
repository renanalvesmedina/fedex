package com.mercurio.lms.vol.model.service;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.joda.time.DateTime;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vol.model.VolEventosCelular;
import com.mercurio.lms.vol.model.VolTiposEventos;
import com.mercurio.lms.vol.utils.VolFomatterUtil;

import br.com.tntbrasil.integracao.domains.entrega.retornofedex.RetornoOcorrenciaFedexDMN;
/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vol.volInformarOcorrenciasService"
 */
public class VolInformarOcorrenciasService {
	private static final long CD_EVENTO_CHEGADA_NO_CLIENTE = 30;
	private static final String OBS_CHEGADA_NO_CLIENTE_VIA_VOL = "Chegada no cliente via VOL";
	private static final short CHEGADA_NO_CLIENTE = (short) 96;
	private static final String DATETIME_WITH_SECONDS_PATTERN = "dd/MM/yyyy HH:mm:ss";
	private static final String USUARIO_SESSAO = "usuarioSessao";
	
	private VolTiposEventosService volTiposEventosService; 
	private VolEventosCelularService volEventosCelularService;
	private MeioTransporteService meioTransporteService;
	private ConhecimentoService conhecimentoService; 
	private PedidoColetaService pedidoColetaService; 
	private ControleCargaService controleCargaService;
	private UsuarioService usuarioService;
	private PaisService paisService;
	private VolDadosSessaoService volDadosSessaoService;
	private FilialService filialService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
	private ManifestoEntregaService manifestoEntregaService;
	private ManifestoService manifestoService;
	
	public void executeInformarOcorrencia(TypedFlatMap tfm){
		Long idMeioTransporte = tfm.getLong("idMeioTransporte");		
		Long idControleCarga = tfm.getLong("idControleCarga");		
		Long idConhecimento = tfm.getLong("idConhecimento");
		Long cdEvento = tfm.getLong("cdEvento");
		Long idPedidoColeta = tfm.getLong("idPedidoColeta");
		
    	/**
    	 * seta os dados na sessão
    	 */
		Filial filial = filialService.findById(tfm.getLong("idFilial"));
		Usuario usuario = usuarioService.findUsuarioByLogin(tfm.getString(USUARIO_SESSAO) != null ? tfm.getString(USUARIO_SESSAO) : "vol");
		Pais pais = paisService.findByIdPessoa( filial.getIdFilial() );
		volDadosSessaoService.setDadosSessaoBanco(usuario, filial, pais);
		
		DateTime dhSolicitacao;
		if(tfm.getString("dhSolicitacao") != null){
			dhSolicitacao = VolFomatterUtil.formatStringToDateTime(tfm.getString("dhSolicitacao"));
		}else{
			dhSolicitacao = JTDateTimeUtils.getDataHoraAtual();
		}
		
		VolTiposEventos volTiposEventos = findTipoEvento(cdEvento);
		if (volTiposEventos==null){
			//FIXME eventos do celular não correspondem ao do sistema
			return;
		}			
		MeioTransporte meioTransporte = meioTransporteService.findByIdInitLazyProperties(idMeioTransporte, false);		
		VolEventosCelular volEventosCelular = new VolEventosCelular();
		
		ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);
		
		if (idConhecimento!=null){
			Conhecimento conhecimento = conhecimentoService.findByIdInitLazyProperties(idConhecimento, false);
			volEventosCelular.setConhecimento(conhecimento);
			
			checkEventoChegadaNoCliente(cdEvento, filial, conhecimento, dhSolicitacao);
		}else if (idPedidoColeta != null) {
			PedidoColeta pedidoColeta = pedidoColetaService.findById(idPedidoColeta);
			volEventosCelular.setPedidoColeta(pedidoColeta);
		}

		volEventosCelular.setDhSolicitacao(dhSolicitacao);
		volEventosCelular.setMeioTransporte(meioTransporte);
		volEventosCelular.setVolTiposEvento(volTiposEventos);		
		volEventosCelular.setTpOrigem(new DomainValue("C"));
		volEventosCelular.setControleCarga(controleCarga);
		
		volEventosCelularService.store(volEventosCelular);
	}

	private void checkEventoChegadaNoCliente(Long cdEvento, Filial filial,Conhecimento conhecimento, DateTime dhSolicitacao) {
		if(cdEvento != null && cdEvento == CD_EVENTO_CHEGADA_NO_CLIENTE){
			createEventoChegadaNoClienteViaVol(filial, conhecimento, dhSolicitacao);
		}
	}

	private void createEventoChegadaNoClienteViaVol(Filial filial, Conhecimento conhecimento, DateTime dhSolicitacao) {
		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(CHEGADA_NO_CLIENTE, conhecimento.getIdDoctoServico(), filial.getIdFilial(),
				buildNrDocumento(conhecimento), dhSolicitacao, null, OBS_CHEGADA_NO_CLIENTE_VIA_VOL, conhecimento.getTpDocumentoServico().getValue(), 
				Boolean.FALSE, Boolean.FALSE);
	}
	
	private String buildNrDocumento(Conhecimento conhecimento) {
		return conhecimento.getFilialOrigem().getSgFilial() + " " + conhecimento.getNrDoctoServico();
	}
	
	public VolTiposEventos findTipoEvento(Long nmCodigo){
		Map criteria = new HashedMap();
		criteria.put("nmCodigo",nmCodigo);
		List list = volTiposEventosService.find(criteria);
		if (list.size()>0)
			return (VolTiposEventos) list.get(0);
		return null;
	}
	
	public void executeInformarOcorrenciaIntegracaoFedex(RetornoOcorrenciaFedexDMN retornoOcorrenciaFedexDMN){
		
		Long idDoctoServico = retornoOcorrenciaFedexDMN.getIdDoctoServico();
		List<ManifestoEntregaDocumento> manifestos = manifestoEntregaDocumentoService.findManifestoSemOcorrenciaEntregaByIdDoctoServico(idDoctoServico, new String[]{"TC", "TE", "ED", "AD"});
		ManifestoEntregaDocumento med = manifestos.get(0);
		ManifestoEntrega manifestoEntrega = manifestoEntregaService.findById(med.getManifestoEntrega().getIdManifestoEntrega());
		Manifesto manifesto = (Manifesto) manifestoService.findById(manifestoEntrega.getManifesto().getIdManifesto());   
		Long idControleCarga = manifesto.getControleCarga().getIdControleCarga();
		ControleCarga controleCarga = controleCargaService.findById(idControleCarga);
		Long idMeioTransporte = controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte();
		Long idFilial = controleCarga.getFilialByIdFilialOrigem().getIdFilial();
		DateTime dhOcorrencia = JTDateTimeUtils.formatStringToDateTimeWithSeconds(retornoOcorrenciaFedexDMN.getListOcorrencias().get(0).getDataRegistroOcorrencia());
		
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.put("idMeioTransporte", idMeioTransporte);
		tfm.put("idControleCarga", idControleCarga);
		tfm.put("idConhecimento", idDoctoServico);
		tfm.put("cdEvento", CD_EVENTO_CHEGADA_NO_CLIENTE);
		tfm.put("idFilial", idFilial);
		tfm.put("dhSolicitacao", JTDateTimeUtils.formatDateTimeToString(dhOcorrencia, DATETIME_WITH_SECONDS_PATTERN));
		tfm.put("usuarioSessao", "integracao");
		
		List<VolEventosCelular> listEventoCelular = volEventosCelularService.findEventoCelular(idDoctoServico, idControleCarga);
		if(CollectionUtils.isEmpty(listEventoCelular)){
			this.executeInformarOcorrencia(tfm);
		}
		
	}
	
	public void executeInformarOcorrenciaPorEDIParaBaixaParceiro(TypedFlatMap parceiro) {
		this.executeInformarOcorrencia(parceiro);
	}
	
	public void setVolEventosCelularService(VolEventosCelularService volEventosCelularService) {
		this.volEventosCelularService = volEventosCelularService;
	}
	public void setVolTiposEventosService(VolTiposEventosService volTiposEventosService) {
		this.volTiposEventosService = volTiposEventosService;
	}
	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}	
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
}
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
	public void setVolDadosSessaoService(VolDadosSessaoService volDadosSessaoService) {
		this.volDadosSessaoService = volDadosSessaoService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}

	public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
		this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
	}

	public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
		this.manifestoEntregaService = manifestoEntregaService;
	}

	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
}
