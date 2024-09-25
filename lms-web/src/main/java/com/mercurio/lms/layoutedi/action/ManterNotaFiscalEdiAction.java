package com.mercurio.lms.layoutedi.action;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.edi.model.LogEDIComplemento;
import com.mercurio.lms.edi.model.LogEDIDetalhe;
import com.mercurio.lms.edi.model.NotaFiscalEdi;
import com.mercurio.lms.edi.model.NotaFiscalEdiComplemento;
import com.mercurio.lms.edi.model.NotaFiscalEdiItem;
import com.mercurio.lms.edi.model.NotaFiscalEdiVolume;
import com.mercurio.lms.edi.model.service.ClienteEDIFilialEmbarcadoraService;
import com.mercurio.lms.edi.model.service.ClienteLayoutEDIService;
import com.mercurio.lms.edi.model.service.LogEDIComplementoService;
import com.mercurio.lms.edi.model.service.LogEDIDetalheService;
import com.mercurio.lms.edi.model.service.NotaFiscalEDIComplementoService;
import com.mercurio.lms.edi.model.service.NotaFiscalEDIService;
import com.mercurio.lms.expedicao.edi.model.service.CCEItemService;
import com.mercurio.lms.expedicao.model.CCEItem;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.LiberacaoNotaNatura;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.LiberacaoNotaNaturaService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.layoutedi.model.Complemento;
import com.mercurio.lms.layoutedi.model.Consignatario;
import com.mercurio.lms.layoutedi.model.Destinatario;
import com.mercurio.lms.layoutedi.model.Detalhe;
import com.mercurio.lms.layoutedi.model.Item;
import com.mercurio.lms.layoutedi.model.NotaFiscal;
import com.mercurio.lms.layoutedi.model.Redespacho;
import com.mercurio.lms.layoutedi.model.Registro;
import com.mercurio.lms.layoutedi.model.Remetente;
import com.mercurio.lms.layoutedi.model.Tomador;
import com.mercurio.lms.layoutedi.model.Volume;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.InformacaoDoctoClienteService;


/**
 * 
 * @author ThiagoFA
 * @spring.bean id="lms.layoutedi.ManterNotaFiscalEdiAction"
 */
@ServiceSecurity
public class ManterNotaFiscalEdiAction {
	private Logger log = LogManager.getLogger(this.getClass());
	private NotaFiscalEDIService notaFiscalEDIService;
	private ClienteLayoutEDIService clienteLayoutEDIService;
	private ClienteEDIFilialEmbarcadoraService clienteEDIFilialEmbarcadoraService;
	private LiberacaoNotaNaturaService liberacaoNotaNaturaService;
	private ClienteService clienteService;
	private DoctoServicoService doctoServicoService;
	private FilialService filialService;
	private ParametroGeralService parametroGeralService;
	private CCEItemService cceItemService;
	private NotaFiscalEDIComplementoService notaFiscalEDIComplementoService;
	private PessoaService pessoaService;
	private InformacaoDoctoClienteService informacaoDoctoClienteService;
	private LogEDIDetalheService logEDIDetalheService; 
	private LogEDIComplementoService logEDIComplementoService; 
	
	@MethodSecurity(processGroup = "layoutedi.manterNotaFiscalEdiAction", processName = "gravaNotaFiscal", authenticationRequired=false)
	public String gravaNotaFiscal(Object object) throws Exception{
		String retorno = "true";
		try{
			List<Registro> notaFiscalList = (List<Registro>)object;
			if(notaFiscalList == null){
				return retorno;
			}
				for (Registro registro : notaFiscalList){
					boolean clineteUtilizaPedido = this.isClinteUtilizaPedido(registro.getRemetente());
					for (Detalhe detalhe : registro.getDetalhes()) {
						boolean atualizaNota = clineteUtilizaPedido && this.isNotaFiscal(detalhe);
						Long idInfoDoctoClienteBox = null;
						Long idInfoDoctoClientePedido = null;
						if(clineteUtilizaPedido){
							List<Map<String, Object>> listaInfoDoctoCliente = this.buildIdInfoDoctoCliente(registro.getRemetente().getCnpjReme().toString());
							idInfoDoctoClienteBox = this.obtemIdInfoDoctoCliente(listaInfoDoctoCliente, ConstantesExpedicao.BOX);
							idInfoDoctoClientePedido = this.obtemIdInfoDoctoCliente(listaInfoDoctoCliente, ConstantesExpedicao.PEDIDO);
						}
						Long sequencia = null;
						if(!atualizaNota && detalhe.getNotasFiscais().size() > 1){
							sequencia = notaFiscalEDIService.findSequenciaAgrupamentoSq();
						}
						for (NotaFiscal notaFiscal : detalhe.getNotasFiscais()) {							
							NotaFiscalEdi notaFiscalEDI = null;							
							if(atualizaNota) {	
								String nrPedido = this.obterNumeroPedido(notaFiscal);
								notaFiscalEDI = this.notaFiscalEDIService.findByNrNotaIdentificado(registro.getRemetente().getCnpjReme().toString(), nrPedido );
								if(notaFiscalEDI == null){
									return retorno;	
								}
								LogEDIDetalhe logEDIDetalhe = logEDIDetalheService.findByCnpjRemeNrNota(notaFiscalEDI.getCnpjReme(), notaFiscalEDI.getNrNotaFiscal());
								this.atualizarNotaFiscal(notaFiscalEDI, notaFiscal, registro.getRemetente(), detalhe.getDestinatario(),logEDIDetalhe);
								this.updateDadosNotaFiscal(notaFiscalEDI,nrPedido, idInfoDoctoClienteBox,logEDIDetalhe);
							}else {
								if(clineteUtilizaPedido && this.isPedidoJaRecebido(notaFiscal, idInfoDoctoClientePedido)){
									return retorno;
								}								
								notaFiscalEDI = this.converteNotaFiscal(notaFiscal,
										registro.getRemetente(),
										detalhe.getDestinatario(),
										detalhe.getConsignatario(),
										detalhe.getRedespacho(),
										detalhe.getTomador());
								notaFiscalEDI.setSequenciaAgrupamento(sequencia);
								this.gravarDadosNotaFiscal(notaFiscalEDI, notaFiscal);
							}
						}
					}
				}
			
		}catch (Exception e) {
			log.error(e);
			throw new Exception(this.getStackTrace(e));
		}
		return retorno;		
	}	
	
	private void gravarDadosNotaFiscal(NotaFiscalEdi notaFiscalEDI, NotaFiscal notaFiscal) {
		List<NotaFiscalEdiComplemento> complementos = new ArrayList<NotaFiscalEdiComplemento>();
		List<NotaFiscalEdiItem> itens = new ArrayList<NotaFiscalEdiItem>();
		List<NotaFiscalEdiVolume> volumes = new ArrayList<NotaFiscalEdiVolume>();
		
		if(notaFiscal.getItens() != null){
			for (Item item : notaFiscal.getItens()) {
				NotaFiscalEdiItem notaFiscalEDIItem = this.setaItem(item);
				notaFiscalEDIItem.setNotaFiscalEdi(notaFiscalEDI);
				itens.add(notaFiscalEDIItem);
			}
		}
		if(notaFiscal.getComplementos() != null){
			for (Complemento complemento : notaFiscal.getComplementos()) {
				NotaFiscalEdiComplemento notaFiscalEDIComplemento = this.setaComplemento(complemento);
				notaFiscalEDIComplemento.setNotaFiscalEdi(notaFiscalEDI);
				complementos.add(notaFiscalEDIComplemento);				
			}
		}
		if(notaFiscal.getVolumes() != null){
			for (Volume volume: notaFiscal.getVolumes()) {
				NotaFiscalEdiVolume notaFiscalEDIVolume = this.setaVolume(volume);
				notaFiscalEDIVolume.setNotaFiscalEdi(notaFiscalEDI);
				volumes.add(notaFiscalEDIVolume);
			}
		}
		this.notaFiscalEDIService.store(notaFiscalEDI,complementos,volumes,itens);
	}
	
	private void  updateDadosNotaFiscal(NotaFiscalEdi notaFiscalEDI, String nrPedido, Long idInfoComplement, LogEDIDetalhe logEDIDetalhe) {		
		// atualiza os dados da Nota
		this.notaFiscalEDIService.storeNotaFiscalEdi(notaFiscalEDI);
		this.logEDIDetalheService.storeLogEdiDetalhe(logEDIDetalhe);
		
		// Atualiza os dados do CCE ITEM
		CCEItem cceItem = this.cceItemService.findByChaveNfe(nrPedido);
		if(cceItem == null) {
			return;
		}
		cceItem.setNrChave(notaFiscalEDI.getChaveNfe());
		this.cceItemService.storeCEEItem(cceItem);
		
		// Insere dados complementares para o cce.
		NotaFiscalEdiComplemento item = new NotaFiscalEdiComplemento();
		item.setNotaFiscalEdi(notaFiscalEDI);
		item.setIndcIdInformacaoDoctoClien(idInfoComplement);
		item.setValorComplemento(cceItem.getCce().getIdCCE().toString());
		this.notaFiscalEDIComplementoService.store(item);
		
		LogEDIComplemento logComplemento = new LogEDIComplemento();
		logComplemento.setLogEDIDetalhe(logEDIDetalhe);
		logComplemento.setValorComplemento(item.getValorComplemento());
		InformacaoDoctoCliente idc = informacaoDoctoClienteService.findById(idInfoComplement);
		logComplemento.setNomeComplemento(idc.getDsCampo());
		logEDIComplementoService.store(logComplemento);
	}
	
	private boolean isPedidoJaRecebido(NotaFiscal notaFiscal ,Long idInfoDoctoClientePedido){		
		NotaFiscalEdiComplemento complementoPedido = this.notaFiscalEDIComplementoService.
				                                findByIdInformacaoDocClienteAndValorCompl(idInfoDoctoClientePedido, this.obterNumeroPedido(notaFiscal));		
		if(complementoPedido != null){
			return true;
		}		
		return false;		
	}
	
	private List<Map<String, Object>> buildIdInfoDoctoCliente (String cnpj){
		String cnpjFor = FormatUtils.fillNumberWithZero(cnpj, 14);
	    Pessoa pessoa = pessoaService.findByNrIdentificacao(cnpjFor);	    
	    List<Map<String, Object>> lidc = informacaoDoctoClienteService.findDadosByCliente(pessoa.getIdPessoa());
	    return lidc;
	}
	
	private Long obtemIdInfoDoctoCliente( List<Map<String, Object>> lidc, String valor) {		   

	    for (Map<String, Object> idc : lidc) {
	      String campo = (String) idc.get("dsCampo");	
	      if (campo.toUpperCase().contains(valor.toUpperCase())) {
	        return  Long.valueOf(idc.get("idInformacaoDoctoCliente").toString());
	      }
	    }

	   return null;
	}
	
	private boolean isClinteUtilizaPedido(Remetente remetente) {	
		if(remetente == null) {
			return false;
		}
		String cnpj = remetente.getCnpjReme().toString();
		String cnpjsClientesPedido = (String) parametroGeralService.findSimpleConteudoByNomeParametro(ConstantesExpedicao.CNPJS_CLIENTES_USAM_PEDIDO);
		String cnpjs[] = cnpjsClientesPedido.split(";");
		for (String cnpjCliente : cnpjs) {
			if(cnpj.equals(cnpjCliente)) return true;
		}
		return false;
	}
	
	private boolean isNotaFiscal(Detalhe detalhe) {		
		for (NotaFiscal notaFiscal : detalhe.getNotasFiscais()) {
			if(notaFiscal.getChaveNfe() != null && !notaFiscal.getChaveNfe().isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	private String obterNumeroPedido(NotaFiscal notaFiscal) {		
		if(notaFiscal.getComplementos() == null){
			return null;
		}
		for (Complemento complemento : notaFiscal.getComplementos()) {
			if(complemento.getNomeComplemento() != null && ConstantesExpedicao.PEDIDO.equals(complemento.getNomeComplemento())) {
				return complemento.getValorComplemento();
			}
		}
		return null;
		
		
	}

	@MethodSecurity(processGroup = "layoutedi.manterNotaFiscalEdiAction", processName = "gravaLiberacaoNotaNatura", authenticationRequired=false)
	public String gravaLiberacaoNotaNatura(Object object) throws Exception{
		String retorno = "true";
		try{
			Map<String, Object> mapParametros = (Map<String, Object>) object;
			if(mapParametros != null){
				LiberacaoNotaNatura lnn = new LiberacaoNotaNatura();
				lnn.setCdMotivo((String) mapParametros.get("cdMotivo"));
				lnn.setCdMotivoCn((String) mapParametros.get("cdMotivoCn"));
				if(mapParametros.get("idCliente") != null){
					Cliente cliente = clienteService.findByIdInitLazyProperties((Long) mapParametros.get("idCliente"), false);
					lnn.setCliente(cliente);
				}
				if(mapParametros.get("idDoctoServico") != null){
					DoctoServico doctoServico = doctoServicoService.findById((Long) mapParametros.get("idDoctoServico"));
					lnn.setDoctoServico(doctoServico);
				}
				lnn.setDsBairro((String) mapParametros.get("dsBairro"));
				lnn.setDsCep((String) mapParametros.get("dsCep"));
				lnn.setDsCidade((String) mapParametros.get("dsCidade"));
				lnn.setDsComplemento((String) mapParametros.get("dsComplemento"));
				lnn.setDsEnderecoCn((String) mapParametros.get("dsEnderecoCn"));
				lnn.setDtPedido((YearMonthDay) mapParametros.get("dtPedido"));
				if(mapParametros.get("idFilialDestino") != null){
					Filial filialDestino = filialService.findById((Long) mapParametros.get("idFilialDestino"));
					lnn.setFilialDestino(filialDestino);
				}
				lnn.setNmNomeCn((String) mapParametros.get("nmNomeCn"));
				lnn.setNrNotaFiscal((Integer) mapParametros.get("nrNotaFiscal"));
				lnn.setNrPedido((Long) mapParametros.get("nrPedido"));
				lnn.setNrTelefone1((String) mapParametros.get("nrTelefone1"));
				lnn.setNrTelefone2((String) mapParametros.get("nrTelefone2"));
				lnn.setNrTelefone3((String) mapParametros.get("nrTelefone3"));
				lnn.setSgEstado((String) mapParametros.get("sgEstado"));
				lnn.setSgPais((String) mapParametros.get("sgPais"));
				lnn.setTpOrdem((String) mapParametros.get("tpOrdem"));
				lnn.setTpRegistro((Integer) mapParametros.get("tpRegistro"));
				lnn.setTpServico((String) mapParametros.get("tpServico"));
				if(mapParametros.get("tpSituacao") != null){
					DomainValue tpSituacao = new DomainValue((String) mapParametros.get("tpSituacao"));
					lnn.setTpSituacao(tpSituacao);
				}
				lnn.setTpVolume((String) mapParametros.get("tpVolume"));
				lnn.setVlFrete((BigDecimal) mapParametros.get("vlFrete"));
				
				liberacaoNotaNaturaService.store(lnn);
				
			}
		} catch (Exception e) {
			log.error(e);
			throw new Exception(this.getStackTrace(e));
		}
		return retorno;		
	}

	private String getStackTrace(Exception e){
		StringBuilder retorno = new StringBuilder(e.getMessage());
		try{
			if(e.getStackTrace() != null && e.getStackTrace().length > 0){
				for (int i = 0; i < e.getStackTrace().length; i++) {
					retorno.append(e.getStackTrace()[i]).append("\n");
				}
				
			}
		}catch (Exception e2) {
			log.error(e2);
		}
		return retorno.toString();
	}
	


	
	

	public NotaFiscalEDIService getNotaFiscalEDIService() {
		return notaFiscalEDIService;
	}

	public void setNotaFiscalEDIService(NotaFiscalEDIService notaFiscalEDIService) {
		this.notaFiscalEDIService = notaFiscalEDIService;
	}

	public ClienteLayoutEDIService getClienteLayoutEDIService() {
		return clienteLayoutEDIService;
	}

	public void setClienteLayoutEDIService(ClienteLayoutEDIService clienteLayoutEDIService) {
		this.clienteLayoutEDIService = clienteLayoutEDIService;
	}

	public ClienteEDIFilialEmbarcadoraService getClienteEDIFilialEmbarcadoraService() {
		return clienteEDIFilialEmbarcadoraService;
	}

	public void setClienteEDIFilialEmbarcadoraService(ClienteEDIFilialEmbarcadoraService clienteEDIFilialEmbarcadoraService) {
		this.clienteEDIFilialEmbarcadoraService = clienteEDIFilialEmbarcadoraService;
	}

	public LiberacaoNotaNaturaService getLiberacaoNotaNaturaService() {
		return liberacaoNotaNaturaService;
	}

	public void setLiberacaoNotaNaturaService(
			LiberacaoNotaNaturaService liberacaoNotaNaturaService) {
		this.liberacaoNotaNaturaService = liberacaoNotaNaturaService;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}	

	public CCEItemService getCceItemService() {
		return cceItemService;
	}

	public void setCceItemService(CCEItemService cceItemService) {
		this.cceItemService = cceItemService;
	}
	

	public NotaFiscalEDIComplementoService getNotaFiscalEDIComplementoService() {
		return notaFiscalEDIComplementoService;
	}

	public void setNotaFiscalEDIComplementoService(NotaFiscalEDIComplementoService notaFiscalEDIComplementoService) {
		this.notaFiscalEDIComplementoService = notaFiscalEDIComplementoService;
	}
	

	public PessoaService getPessoaService() {
		return pessoaService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public InformacaoDoctoClienteService getInformacaoDoctoClienteService() {
		return informacaoDoctoClienteService;
	}

	public void setInformacaoDoctoClienteService(InformacaoDoctoClienteService informacaoDoctoClienteService) {
		this.informacaoDoctoClienteService = informacaoDoctoClienteService;
	}
	
	private void atualizarNotaFiscal(NotaFiscalEdi nf, NotaFiscal notaFiscal, Remetente remetente,
			Destinatario destinatario, LogEDIDetalhe logEDIDetalhe) {
		
		nf.setTipoFrete(notaFiscal.getTipoFrete());
		nf.setNrNotaFiscal(notaFiscal.getNrNotaFiscal());
		nf.setVlrTotalMerc(notaFiscal.getVlrTotalMerc());
		nf.setPesoReal(notaFiscal.getPesoReal());
		nf.setPesoCubado(notaFiscal.getPesoCubado());
		nf.setSerieNf(notaFiscal.getSerieNf());
		nf.setChaveNfe(notaFiscal.getChaveNfe());	
		
		logEDIDetalhe.setTipoFrete(nf.getTipoFrete());
		logEDIDetalhe.setNrNotaFiscal(nf.getNrNotaFiscal());
		logEDIDetalhe.setVlrTotalMerc(nf.getVlrTotalMerc());
		logEDIDetalhe.setPesoReal(nf.getPesoReal());
		logEDIDetalhe.setPesoCubado(nf.getPesoCubado());
		logEDIDetalhe.setSerieNf(nf.getSerieNf());
		logEDIDetalhe.setChaveNfe(nf.getChaveNfe());
		
		if(remetente != null){
			nf.setEnderecoReme(remetente.getEnderecoReme());
			nf.setUfReme(remetente.getUfReme());
			nf.setIeReme(remetente.getIeReme());
			nf.setMunicipioReme(remetente.getMunicipioReme());
			nf.setCepEnderReme(remetente.getCepEnderReme());			
			nf.setNomeReme(remetente.getNomeReme());			
			
			logEDIDetalhe.setEnderecoReme(nf.getEnderecoReme());
			logEDIDetalhe.setUfReme(nf.getUfReme());
			logEDIDetalhe.setIeReme(nf.getIeReme());
			logEDIDetalhe.setMunicipioReme(nf.getMunicipioReme());
			logEDIDetalhe.setCepEnderReme(nf.getCepEnderReme());			
			logEDIDetalhe.setNomeReme(nf.getNomeReme());
			
		}
		if(destinatario != null){
			nf.setUfDest(destinatario.getUfDest());
			nf.setCepEnderDest(destinatario.getCepEnderDest());			
			nf.setNomeDest(destinatario.getNomeDest());			
			nf.setIeDest(destinatario.getIeDest());		
			
			logEDIDetalhe.setUfDest(nf.getUfDest());
			logEDIDetalhe.setCepEnderDest(nf.getCepEnderDest());			
			logEDIDetalhe.setNomeDest(nf.getNomeDest());			
			logEDIDetalhe.setIeDest(nf.getIeDest());
		}
		
	}
	private NotaFiscalEdi converteNotaFiscal(
			NotaFiscal notaFiscal, 
			Remetente remetente,
			Destinatario destinatario,
			Consignatario consignatario,
			Redespacho redespacho,
			Tomador tomador){
		
		NotaFiscalEdi nf = new NotaFiscalEdi();	
		nf.setAliqIcms(notaFiscal.getAliqIcms());
		nf.setAliqNf(notaFiscal.getAliqNf());
		nf.setCfopNf(notaFiscal.getCfopNf());
		nf.setChaveNfe(notaFiscal.getChaveNfe());
		nf.setDataEmissaoNf(notaFiscal.getDataEmissaoNf());
		nf.setEspecie(notaFiscal.getEspecie());
		nf.setModalFrete(notaFiscal.getModalFrete());
		nf.setNatureza(notaFiscal.getNatureza());
		nf.setNrNotaFiscal(notaFiscal.getNrNotaFiscal());
		nf.setOutrosValores(notaFiscal.getOutrosValores());
		nf.setPesoCubado(notaFiscal.getPesoCubado());
		nf.setPesoCubadoTotal(notaFiscal.getPesoCubadoTotal());
		nf.setPesoReal(notaFiscal.getPesoReal());
		nf.setPesoRealTotal(notaFiscal.getPesoRealTotal());
		nf.setPinSuframa(notaFiscal.getPinSuframa());
		nf.setQtdeVolumes(notaFiscal.getQtdeVolumes());
		nf.setSerieNf(notaFiscal.getSerieNf());
		nf.setTarifa(notaFiscal.getTarifa());
		nf.setTipoFrete(notaFiscal.getTipoFrete());
		nf.setTipoTabela(notaFiscal.getTipoTabela());
		nf.setVlrAdeme(notaFiscal.getVlrAdeme());
		nf.setVlrBaseCalcIcms(notaFiscal.getVlrBaseCalcIcms());
		nf.setVlrBaseCalcNf(notaFiscal.getVlrBaseCalcNf());
		nf.setVlrBaseCalcStNf(notaFiscal.getVlrBaseCalcStNf());
		nf.setVlrCat(notaFiscal.getVlrCat());
		nf.setVlrDespacho(notaFiscal.getVlrDespacho());
		nf.setVlrFreteLiquido(notaFiscal.getVlrFreteLiquido());
		nf.setVlrFretePeso(notaFiscal.getVlrFretePeso());
		nf.setVlrFreteValor(notaFiscal.getVlrFreteValor());
		nf.setVlrFreteTotal(notaFiscal.getVlrFreteTotal());
		nf.setVlrIcms(notaFiscal.getVlrIcms());
		nf.setVlrIcmsNf(notaFiscal.getVlrIcmsNf());
		nf.setVlrIcmsStNf(notaFiscal.getVlrIcmsStNf());
		nf.setVlrItr(notaFiscal.getVlrItr());
		nf.setVlrPedagio(notaFiscal.getVlrPedagio());
		nf.setVlrTaxas(notaFiscal.getVlrTaxas());
		nf.setVlrTotalMerc(notaFiscal.getVlrTotalMerc());
		nf.setVlrTotalMercTotal(notaFiscal.getVlrTotalMercTotal());
		nf.setVlrTotProdutosNf(notaFiscal.getVlrTotProdutosNf());
		nf.setDsDivisaoCliente(notaFiscal.getDsDivisaoCliente());
		nf.setNrCtrcSubcontratante(notaFiscal.getNrCtrcSubcontratante());
		if(remetente != null){
			nf.setNomeReme(remetente.getNomeReme());
			nf.setIeReme(remetente.getIeReme());
			nf.setEnderecoReme(remetente.getEnderecoReme());
			nf.setBairroReme(remetente.getBairroReme());
			nf.setMunicipioReme(remetente.getMunicipioReme());
			nf.setUfReme(remetente.getUfReme());
			nf.setCepEnderReme(remetente.getCepEnderReme());
			nf.setCepMuniReme(remetente.getCepMuniReme());
			nf.setCnpjReme(remetente.getCnpjReme());
		}
		if(destinatario != null){
			nf.setNomeDest(destinatario.getNomeDest());
			nf.setIeDest(destinatario.getIeDest());
			nf.setEnderecoDest(destinatario.getEnderecoDest());
			nf.setBairroDest(destinatario.getBairroDest());
			nf.setMunicipioDest(destinatario.getMunicipioDest());
			nf.setUfDest(destinatario.getUfDest());
			nf.setCepEnderDest(destinatario.getCepEnderDest());
			nf.setCepMunicDest(destinatario.getCepMunicDest());
			nf.setCnpjDest(destinatario.getCnpjDest());
		}
		if(consignatario != null){
			nf.setNomeConsig(consignatario.getNomeConsig());
			nf.setIeConsig(consignatario.getIeConsig());
			nf.setEnderecoConsig(consignatario.getEnderecoConsig());
			nf.setBairroConsig(consignatario.getBairroConsig());
			nf.setMunicipioConsig(consignatario.getMunicipioConsig());
			nf.setUfConsig(consignatario.getUfConsig());
			nf.setCepEnderConsig(consignatario.getCepEnderConsig());
			nf.setCepMunicConsig(consignatario.getCepMunicConsig());
			nf.setCnpjConsig(consignatario.getCnpjConsig());
		}
		if(redespacho != null){
			nf.setNomeRedesp(redespacho.getNomeRedesp());
			nf.setIeRedesp(redespacho.getIeRedesp());
			nf.setEnderecoRedesp(redespacho.getEnderecoRedesp());
			nf.setBairroRedesp(redespacho.getBairroRedesp());
			nf.setMunicipioRedesp(redespacho.getMunicipioRedesp());
			nf.setUfRedesp(redespacho.getUfRedesp());
			nf.setCepEnderRedesp(redespacho.getCepEnderRedesp());
			nf.setCnpjRedesp(redespacho.getCnpjRedesp());
		}
		if(tomador != null){
			nf.setNomeTomador(tomador.getNomeTomador());
			nf.setIeTomador(tomador.getIeTomador());
			nf.setEnderecoTomador(tomador.getEnderecoTomador());
			nf.setBairroTomador(tomador.getBairroTomador());
			nf.setMunicipioTomador(tomador.getMunicipioTomador());
			nf.setUfTomador(tomador.getUfTomador());
			nf.setCepEnderTomador(tomador.getCepEnderTomador());
			nf.setCepMunicTomador(tomador.getCepMunicTomador());
			nf.setCnpjTomador(tomador.getCnpjTomador());
		}		
		return nf;
	}
	

	private NotaFiscalEdiItem setaItem(Item item) {
		NotaFiscalEdiItem nf = new NotaFiscalEdiItem();
		
		nf.setAlturaItem(item.getAlturaItem());
		nf.setCodItem(item.getCodItem());
		nf.setComprimentoItem(item.getComprimentoItem());
		nf.setLarguraItem(item.getLarguraItem());
		nf.setPesoCubadoItem(item.getPesoCubadoItem());
		nf.setPesoRealItem(item.getPesoRealItem());
		
		//FIXME LMS-4989 este campo deverá ser substituido no futuro pelos campos valor unitário e valor total
		nf.setValorItem(item.getValorItem());
		
		
		return nf;
	}

	private NotaFiscalEdiVolume setaVolume( Volume volume) {
		NotaFiscalEdiVolume nf = new NotaFiscalEdiVolume();
		nf.setCodigoVolume(volume.getCodigoVolume());
		return nf;
	}
	
	
	private NotaFiscalEdiComplemento setaComplemento(Complemento complemento){
		NotaFiscalEdiComplemento nf = new NotaFiscalEdiComplemento();
		nf.setValorComplemento(complemento.getValorComplemento());
		nf.setIndcIdInformacaoDoctoClien(complemento.getIndcIdInformacaoDoctoClien());
		return nf;
	}

	public void setLogEDIDetalheService(LogEDIDetalheService logEDIDetalheService) {
		this.logEDIDetalheService = logEDIDetalheService;
	}
	
	public void setLogEDIComplementoService(
			LogEDIComplementoService logEDIComplementoService) {
		this.logEDIComplementoService = logEDIComplementoService;
}

	
}
