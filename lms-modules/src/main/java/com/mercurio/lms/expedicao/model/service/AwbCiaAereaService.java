package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.CtoAwb;
import com.mercurio.lms.expedicao.model.LiberaAWBComplementar;
import com.mercurio.lms.expedicao.model.dao.AwbDAO;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConstantesAwb;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.CiaFilialMercurio;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.CiaFilialMercurioService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * @author Giuliano Roberto Costa
 *
 * @spring.bean id="lms.expedicao.awbCiaAereaService"
 */
public class AwbCiaAereaService extends CrudService<Awb, Long> {
	private ConfiguracoesFacade configuracoesFacade;
	private AwbService awbService;
	private CtoAwbService ctoAwbService;
	private CiaFilialMercurioService ciaFilialMercurioService;
	private LiberaAWBComplementarService liberaAWBComplementarService;
	private PedidoColetaService pedidoColetaService;
	private AwbOcorrenciaService awbOcorrenciaService;

	@Override
	public Awb beforeStore(Awb bean) {
		//LMS-4913
		if (ConstantesExpedicao.TP_AWB_NORMAL.equals(bean.getTpAwb())) {
			if(bean == null || bean.getCtoAwbs().size() == 0){
				throw new BusinessException("LMS-04130");
			}
		}

		Long nrAwb = bean.getNrAwb();
		CiaFilialMercurio cfm = ciaFilialMercurioService.findById(bean.getCiaFilialMercurio().getIdCiaFilialMercurio());
		Long idEmpresa = cfm.getEmpresa().getIdEmpresa();
		String dsSerie = bean.getDsSerie();
		Integer digito = bean.getDvAwb();

		List awbs = awbService.findByNrAwbByEmpresa(dsSerie, nrAwb, idEmpresa, digito);
		if(awbs != null && !awbs.isEmpty()){
			throw new BusinessException("LMS-04022");
		}

		validateFields(bean);
		return super.beforeStore(bean);
	}

	private void validateFields(Awb awb){
		String label = "";
		boolean isValid = false;

		if(isNull(awb.getCiaFilialMercurio())){
			label = "ciaAerea";
		}else if(isNull(awb.getFilialByIdFilialOrigem())){
			label = "filialOrigem";
		}else if(isNull(awb.getFilialByIdFilialDestino())){
			label = "filialDestino";
		}else if(isNull(awb.getAeroportoByIdAeroportoOrigem())){
			label = "aeroportoDeOrigem";
		}else if(isNull(awb.getAeroportoByIdAeroportoDestino())){
			label = "aeroportoDeDestino";
		}else if(isNull(awb.getMoeda())){
			label = "moeda";
		}else if(isNull(awb.getQtVolumes())){
			label = "volumes";
		}else if(isNull(awb.getPsTotal())){
			label = "pesoTotal";
		}else if(isNull(awb.getClienteByIdClienteExpedidor())){
			label = "expedidor";
		}else if(isNull(awb.getClienteByIdClienteDestinatario())){
			label = "destinatario";
		}else if(isNull(awb.getDhDigitacao())){
			label = "dhDigitacao";
		}else if(isNull(awb.getTpFrete())){
			label = "tipoFrete";
		}else if(isNull(awb.getNrAwb())){
			label = "numeroAWBCiaAerea";
		}else if(isGreaterThanZero(awb.getQtVolumes())){
			label = "qtVolumes";
		}else{
			isValid = true;
		}

		if(!isValid){
			throw new BusinessException("requiredField", new Object[]{configuracoesFacade.getMensagem(label)});
		}
	}

	private boolean isNull(Object obj) {
		boolean isNull = obj == null;
		if(!isNull && obj instanceof String){
			isNull = obj.toString().trim().equals("");
		}
		return isNull;
	}

	private boolean isGreaterThanZero(Object obj) {
		boolean isUnValidNumber = obj == null;
		if(!isUnValidNumber && obj instanceof Integer){
			isUnValidNumber = !(BigDecimalUtils.gtZero(new BigDecimal(((Integer)obj).intValue())));
		}
		return isUnValidNumber;
	}
	
	private Cliente getCliente(Long idCliente) {
		Cliente cliente = new Cliente();
		cliente.setIdCliente(idCliente);
		return cliente;
	}

	public java.io.Serializable storePreAwb(
			Long idCiaFilialMercurio,
			Long idFilialOrigem,
			Long idFilialDestino,
			Long idAeroportoOrigem,
			Long idAeroportoDestino,
			Long idMoeda,
			Long idClienteRemetente,
			Long idClienteDestinatario,
			Double vlFrete,
			Integer dvAwb,
			DateTime dhEmissao,
			DateTime dhDigitacao,
			DateTime dhPrevistaChegada,
			DateTime dhPrevistaSaida,
			String dsSerie,
			Long nrAwb,
			String dsVooPrevisto,
			String obAwb,
			List<CtoAwb> ctosAwb,
			BigDecimal psTotal,
			BigDecimal psCubado,
			Integer qtVolumes,
			BigDecimal pcAliquotaIcms,
			BigDecimal vlIcms,
			Long idInscricaoEstadualExpedidor,
			Long idInscricaoEstadualDestinatario,
			DomainValue tpLocalEmissao,
			String nrChave,
			String tpAwb,
			Long idLiberacaoAwb,
			Long idAwbSubstituido,
			String dsJustificativaPrejuizoAwb,
			Usuario usuarioJustificativaPrejuizoAwb, 
			Usuario usuarioInclusaoAwb,
			Long idClienteTomador,
			Long idInscricaoEstadualTomador,
			Long nrContaCorrenteTomador
			) {

		Awb awb = new Awb();

		if(idAeroportoOrigem != null) {
			Aeroporto aeroportoOrigem = new Aeroporto();
			aeroportoOrigem.setIdAeroporto(idAeroportoOrigem);
			awb.setAeroportoByIdAeroportoOrigem(aeroportoOrigem);
		}

		if(idAeroportoDestino != null) {
			Aeroporto aeroportoDestino = new Aeroporto();
			aeroportoDestino.setIdAeroporto(idAeroportoDestino);
			awb.setAeroportoByIdAeroportoDestino(aeroportoDestino);
		}
		
		if (idInscricaoEstadualExpedidor != null) {
			InscricaoEstadual inscricaoEstadual = new InscricaoEstadual();
			inscricaoEstadual.setIdInscricaoEstadual(idInscricaoEstadualExpedidor);
			awb.setInscricaoEstadualExpedidor(inscricaoEstadual);
		}
		
		if (idInscricaoEstadualDestinatario != null) {
			InscricaoEstadual inscricaoEstadual = new InscricaoEstadual();
			inscricaoEstadual.setIdInscricaoEstadual(idInscricaoEstadualDestinatario);
			awb.setInscricaoEstadualDestinatario(inscricaoEstadual);
		}

		if (idInscricaoEstadualTomador != null) {
			InscricaoEstadual inscricaoEstadual = new InscricaoEstadual();
			inscricaoEstadual.setIdInscricaoEstadual(idInscricaoEstadualTomador);
			awb.setInscricaoEstadualTomador(inscricaoEstadual);
		}

		setAwbSubstituidoToStore(awb, idAwbSubstituido);
		awb.setDsJustificativaPrejuizo(dsJustificativaPrejuizoAwb);
		awb.setUsuarioJustificativa(usuarioJustificativaPrejuizoAwb);		

		awb.setClienteByIdClienteExpedidor(getCliente(idClienteRemetente));
		awb.setClienteByIdClienteDestinatario(getCliente(idClienteDestinatario));
		awb.setClienteByIdClienteTomador(getCliente(idClienteTomador));

		Filial filialOrigem = new Filial();
		filialOrigem.setIdFilial(idFilialOrigem);
		awb.setFilialByIdFilialOrigem(filialOrigem);

		Filial filialDestino = new Filial();
		filialDestino.setIdFilial(idFilialDestino);
		awb.setFilialByIdFilialDestino(filialDestino);

		CiaFilialMercurio ciaFilialMercurio = new CiaFilialMercurio();
		ciaFilialMercurio.setIdCiaFilialMercurio(idCiaFilialMercurio);
		awb.setCiaFilialMercurio(ciaFilialMercurio);

		Moeda moeda = new Moeda();
		moeda.setIdMoeda(idMoeda);
		awb.setMoeda(moeda);

		awb.setCtoAwbs(ctosAwb);
		awb.setQtVolumes(qtVolumes);
		awb.setDvAwb(dvAwb);
		awb.setDhEmissao(dhEmissao);
		awb.setUsuarioInclusao(usuarioInclusaoAwb);
		awb.setTpFrete(new DomainValue("C"));
		awb.setDsSerie(dsSerie);
		awb.setNrAwb(nrAwb);
		awb.setTpStatusAwb(new DomainValue("E"));
		awb.setBlColetaCliente(Boolean.FALSE);
		awb.setDsVooPrevisto(dsVooPrevisto);
		awb.setTpLocalEmissao(new DomainValue("C"));
		awb.setDhDigitacao(dhDigitacao);
		awb.setObAwb(obAwb);
		awb.setDhPrevistaChegada(dhPrevistaChegada);
		awb.setDhPrevistaSaida(dhPrevistaSaida);
		awb.setVlFrete(BigDecimalUtils.getBigDecimal(vlFrete));
		awb.setPsTotal(psTotal);
		awb.setPsCubado(psCubado);
		awb.setPcAliquotaICMS(pcAliquotaIcms);
		awb.setVlICMS(vlIcms);
		awb.setTpLocalEmissao(tpLocalEmissao);
		awb.setNrChave(nrChave);
		awb.setTpAwb(new DomainValue(tpAwb));
		awb.setTpLocalizacao(new DomainValue(ConstantesAwb.AGUARDANDO_EMBARQUE));
		awb.setNrCcTomadorServico(nrContaCorrenteTomador);
	
		if (ConstantesExpedicao.TP_AWB_NORMAL.equals(tpAwb)) {
			awb.setCtoAwbs(awbService.executeCalcularRateioCtoAwb(awb, ctosAwb, null));
		}
		
		Long id = (Long) super.store(awb);
		
		if (ConstantesExpedicao.TP_AWB_COMPLEMENTAR.equals(tpAwb)) {
			LiberaAWBComplementar liberaAWBComplementar = (LiberaAWBComplementar) liberaAWBComplementarService.findById(idLiberacaoAwb);
			liberaAWBComplementar.setAwbComplementar(awb);
			liberaAWBComplementarService.store(liberaAWBComplementar);
			
			Awb awbOriginal = liberaAWBComplementar.getAwbOriginal();
			ctoAwbService.storeAll(awbService.executeCalcularRateioCtoAwb(awb, awbOriginal.getCtoAwbs(), awbOriginal));
		}
		
		awbService.generateEventoAwbEmitido(awb);
		return id;
	}

	private void setAwbSubstituidoToStore(Awb awb, Long idAwbSubstituido) {
		if(idAwbSubstituido != null){
			Awb susbtituido = new Awb();
			susbtituido.setIdAwb(idAwbSubstituido);
			awb.setAwbSubstituido(susbtituido);
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public Map validateLiberacaoAwbComplementar(String dsSenha, Long idEmpresa) {
		
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("dsSenha", dsSenha);
		List<LiberaAWBComplementar> liberacoes = liberaAWBComplementarService.find(criteria);
		
		if (liberacoes == null || liberacoes.isEmpty()) {
			throw new BusinessException("LMS-00061");
		}

		LiberaAWBComplementar liberaAWBComplementar =  liberacoes.get(0);
		
		if (idEmpresa == null || liberaAWBComplementar.getEmpresa().getIdEmpresa().compareTo(idEmpresa) != 0) {
			throw new BusinessException("LMS-04449");
		}

		if (liberaAWBComplementar.getAwbComplementar() != null) {
			throw new BusinessException("LMS-04447", new String[] { AwbUtils.getNrAwbFormated(liberaAWBComplementar.getAwbOriginal()) });
		}
		
		Map awb = new HashMap<String, Object>();
		awb.put("idLiberacaoAwb", liberaAWBComplementar.getIdLiberaAWBComplementar());
		awb.put("nrAwb", AwbUtils.getNrAwbFormated(liberaAWBComplementar.getAwbOriginal()));
		awb.put("dsSenha", liberaAWBComplementar.getDsSenha());
		
		return awb;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setAwbDAO(AwbDAO dao){
		setDao( dao );
	}

	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}

	public void setCiaFilialMercurioService(CiaFilialMercurioService ciaFilialMercurioService) {
		this.ciaFilialMercurioService = ciaFilialMercurioService;
	}
    
	public void setLiberaAWBComplementarService(
			LiberaAWBComplementarService liberaAWBComplementarService) {
		this.liberaAWBComplementarService = liberaAWBComplementarService;
	}

	public PedidoColetaService getPedidoColetaService() {
		return pedidoColetaService;
	}

	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}

	public AwbOcorrenciaService getAwbOcorrenciaService() {
		return awbOcorrenciaService;
	}

	public void setAwbOcorrenciaService(AwbOcorrenciaService awbOcorrenciaService) {
		this.awbOcorrenciaService = awbOcorrenciaService;
	}

	public void setCtoAwbService(CtoAwbService ctoAwbService) {
		this.ctoAwbService = ctoAwbService;
	}
	
}