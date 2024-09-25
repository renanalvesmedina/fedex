package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.ParametroPais;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.ParametroPaisService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.expedicao.DocumentoServicoFacade;
import com.mercurio.lms.expedicao.model.AduanaCtoInt;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.DespachanteCtoInt;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.Dimensao;
import com.mercurio.lms.expedicao.model.DocumentoAnexo;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.model.ParcelaDoctoServico;
import com.mercurio.lms.expedicao.model.ServAdicionalDocServ;
import com.mercurio.lms.expedicao.model.TrechoCtoInt;
import com.mercurio.lms.expedicao.model.dao.CtoInternacionalDAO;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FluxoFilial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.PermissoEmpresaPais;
import com.mercurio.lms.municipios.model.service.McdService;
import com.mercurio.lms.municipios.model.service.PermissoEmpresaPaisService;
import com.mercurio.lms.municipios.model.service.PpeService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;

/**
 * Classe de serviço para CRUD: 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.ctoInternacionalService"
 */
public class CtoInternacionalService extends CrudService<CtoInternacional, Long> {
	private PpeService ppeService;
	private DpeService dpeService;
	private McdService mcdService;
	private ServicoService servicoService;
	private EnderecoPessoaService enderecoPessoaService; 
	private ClienteService clienteService;
	private PermissoEmpresaPaisService permissoEmpresaPaisService;
	private InscricaoEstadualService inscricaoEstadualService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private TrechoCtoIntService trechoCtoIntService;
	private ParcelaDoctoServicoService parcelaDoctoServicoService;
	private DocumentoAnexoService documentoAnexoService;
	private AduanaCtoIntService aduanaCtoIntService;
	private ParametroPaisService parametroPaisService;
	private DocumentoServicoFacade documentoServicoFacade;
	private EmitirDocumentoService emitirDocumentoService;
	private DivisaoClienteService divisaoClienteService;

	/**
	 * Recupera uma instância de <code>CtoInternacional</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public TypedFlatMap findCrtById(java.lang.Long id) {
		return getCtoInternacionalDAO().findCrtById(id);
	}

	public CtoInternacional findById(java.lang.Long id) {
		return (CtoInternacional) super.findById(id);
	}

	public CtoInternacional findByIdDocServico(java.lang.Long id) {
		return getCtoInternacionalDAO().findByIdDocServico(id);
	}

	public ResultSetPage findPaginatedCrt(TypedFlatMap criteria) {
		return getCtoInternacionalDAO().findPaginatedCrt(criteria);
	}

	public ResultSetPage findPaginatedDevedorDocServFat(Map<String, Object> map) {
		return getCtoInternacionalDAO().findPaginatedDevedorDocServFat(map, FindDefinition.createFindDefinition(map));
	}
	
	public Integer getRowCountCrt(TypedFlatMap criteria) {
		return getCtoInternacionalDAO().getRowCountCrt(criteria);
	} 

	public Integer getRowCountDevedorDocServFat(Map<String, Object> criteria) {
		return getCtoInternacionalDAO().getRowCountDevedorDocServFat(criteria);
	}

	public List<Map<String, Object>> findNrCrt(Map<String, Object> map) {
		return getCtoInternacionalDAO().findCtoInternacionalDevedorDocServFat(map);
	}

	public List<CtoInternacional> findByNrCrtByFilial(Long nrCrt, Long idFilial) {
		return getCtoInternacionalDAO().findByNrCrtByFilial(nrCrt, idFilial);
	}

	public List findBySgPaisNrCrtByFilial(String sgPais, Long nrCrt, Long idFilial) {
		return getCtoInternacionalDAO().findBySgPaisNrCrtByFilial(sgPais, nrCrt, idFilial);
	}

	public List findLookup(String sgPais, Long nrCrt, Long idFilial) {
		return getCtoInternacionalDAO().findLookup(sgPais, nrCrt, idFilial);
	}

	public List<CtoInternacional> findDocumentosServico(Long idCtoInternacional) {
		return getCtoInternacionalDAO().findDocumentosServico(idCtoInternacional);
	}
	
	public List findAnexosByIdCtoInternacional(Long idCtoInternacional) {
		return getCtoInternacionalDAO().findAnexosByIdCtoInternacional(idCtoInternacional);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		boolean isSuccess = false;
		CtoInternacional ctoInternacional = findById(id);

		if(isEditavel(ctoInternacional).booleanValue()){
			ParametroPais parametroPais = parametroPaisService.findByIdPais(ctoInternacional.getPaisOrigem().getIdPais(), true);

			if(parametroPais == null) {
				Object[] args = new Object[1];
				args[0] = ctoInternacional.getPaisOrigem().getSgResumida();
				throw new BusinessException("LMS-04196", args);
			}

			if(CompareUtils.eq(ctoInternacional.getNrCrt(), parametroPais.getNrUltimoCrt())){
				parametroPais.setNrUltimoCrt(LongUtils.decrementValue(parametroPais.getNrUltimoCrt()));
				parametroPaisService.store(parametroPais);

				beforeRemoveById(id);
				getCtoInternacionalDAO().removeById(id, true);

				isSuccess = true;
			}
		}
		if(!isSuccess) throw new BusinessException("LMS-04188");
	}
	
	/*
	 * Verifica se o CRT pode ou nao ser editável ou excluido.
	 */
	public Boolean isEditavel(CtoInternacional ctoInternacional){
		Boolean retorno = Boolean.TRUE;
		Filial filialUsuario = SessionUtils.getFilialSessao();
		Filial filialOrigem = ctoInternacional.getFilialByIdFilialOrigem();

		EventoDocumentoServico eventoDocumentoServico = eventoDocumentoServicoService.findUltimoEventoDoctoServico(ctoInternacional.getIdDoctoServico());

		if(eventoDocumentoServico == null) return retorno;//Caso em que o CRT foi salvo mas ainda nao há um evento vinculado a ele.

		Evento evento = eventoDocumentoServico.getEvento();

		if(!"R".equals(evento.getTpEvento().getValue())) retorno = Boolean.FALSE;
		else if(Boolean.TRUE.equals(eventoDocumentoServico.getBlEventoCancelado())) retorno = Boolean.FALSE;
		else if(!(evento.getCdEvento().equals(ConstantesSim.EVENTO_DOCUMENTO_ACERTADO) || 
				evento.getCdEvento().equals(ConstantesSim.EVENTO_DOCUMENTO_EMITIDO))) retorno = Boolean.FALSE;
		else if(!filialUsuario.equals(filialOrigem)) retorno = Boolean.FALSE;

		return	retorno;
	}

	/**
	 * Apaga várias entidades através do Id.
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		if(ids != null)
			for(Long id : ids)
				removeById(id);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public Serializable store(CtoInternacional bean) {
		return super.store(bean);
	}

	/**
	 * Prepara o crt para ser inserido.
	 * 
	 * @param crt
	 * @return
	 */
	private CtoInternacional prepareCrtToInsert(CtoInternacional crt){
		
		DateTime dtAtual = JTDateTimeUtils.getDataHoraAtual();
		
		crt.setDhEmissao(dtAtual);
		crt.setTpSituacaoCrt(new DomainValue("D"));//digitado
		crt.setUsuarioByIdUsuarioInclusao(SessionUtils.getUsuarioLogado());
		crt.setDhInclusao(dtAtual);

		crt.setUsuarioByIdUsuarioAlteracao(null);
		crt.setDhAlteracao(null);

		crt.setTpDocumentoServico(new DomainValue("CRT"));
		crt.setBlBloqueado(Boolean.FALSE);
		crt.setRotaIntervaloCep(null);
		crt.setRotaColetaEntregaByIdRotaColetaEntregaSugerid(null);
		crt.setRotaColetaEntregaByIdRotaColetaEntregaReal(null);
		
		Cliente clienteRemetente = crt.getClienteByIdClienteRemetente();
		clienteRemetente = clienteService.findById(clienteRemetente.getIdCliente());
		crt.setClienteByIdClienteRemetente(clienteRemetente);

		InscricaoEstadual inscricaoEstadualRemetente = inscricaoEstadualService.findByPessoaIndicadorPadrao(clienteRemetente.getIdCliente(), Boolean.TRUE);
		crt.setInscricaoEstadualRemetente(inscricaoEstadualRemetente);

		Cliente clienteDestinatario = crt.getClienteByIdClienteDestinatario();
		clienteDestinatario = clienteService.findById(clienteDestinatario.getIdCliente());
		crt.setClienteByIdClienteDestinatario(clienteDestinatario);

		Cliente clienteConsignatario = crt.getClienteByIdClienteConsignatario();
		if(clienteConsignatario != null) {
			clienteConsignatario = clienteService.findById(clienteConsignatario.getIdCliente());
		}

		InscricaoEstadual inscricaoEstadualDestinatario = inscricaoEstadualService.findByPessoaIndicadorPadrao(crt.getClienteByIdClienteDestinatario().getIdCliente(), Boolean.TRUE);
		crt.setInscricaoEstadualDestinatario(inscricaoEstadualDestinatario);
		
		Filial filialOrigem = crt.getFilialByIdFilialOrigem(); 
		Filial filialDestino = crt.getFilialByIdFilialDestino(); 
		Servico servico = crt.getServico(); 

		EnderecoPessoa enderecoPessoaOrigem = enderecoPessoaService.findByIdPessoa(filialOrigem.getPessoa().getIdPessoa()); 
		EnderecoPessoa enderecoPessoaDestino = enderecoPessoaService.findByIdPessoa(filialDestino.getPessoa().getIdPessoa()); 
		Municipio municipioOrigem = enderecoPessoaOrigem.getMunicipio();
		Municipio municipioDestino = enderecoPessoaDestino.getMunicipio();

		EnderecoPessoa enderecoClienteRemetente = enderecoPessoaService.findByIdPessoa(clienteRemetente.getIdCliente());
		crt.setPaisOrigem(enderecoClienteRemetente.getMunicipio().getUnidadeFederativa().getPais());
		crt.setFilial(filialOrigem);

		crt.setEnderecoPessoa(null);

		FluxoFilial fluxoFilial = mcdService.findFluxoEntreFiliais(
			filialOrigem.getIdFilial(),
			filialDestino.getIdFilial(),
			servico.getIdServico(),
			null
		);
		crt.setFluxoFilial(fluxoFilial);

		crt.setVlBaseCalcImposto(null);
		crt.setNrCfop(null);
		crt.setDhEntradaSetorEntrega(null);
		crt.setTpCalculoPreco(new DomainValue("N"));
		crt.setBlPrioridadeCarregamento(Boolean.FALSE);
		crt.setNrAidf(null);
		crt.setDoctoServicoOriginal(null);
		crt.setPcAliquotaIcms(null);
		crt.setImpressora(null);
		crt.setVlImposto(BigDecimalUtils.ZERO);

		Long idPedidoColeta = null;
		if(crt.getPedidoColeta() != null) {
			idPedidoColeta = crt.getPedidoColeta().getIdPedidoColeta();
		}

		Cliente clienteDevedor = null;
		String tpDevedorFrete = crt.getTpDevedorCrt().getValue();
		if("R".equals(tpDevedorFrete)) {
			clienteDevedor = crt.getClienteByIdClienteRemetente();
		} else if("D".equals(tpDevedorFrete)) {
			clienteDevedor = crt.getClienteByIdClienteDestinatario();
		}

		Map dpeMap = dpeService.executeCalculoDPE(
			clienteRemetente,
			clienteDestinatario,
			clienteDevedor,
			clienteConsignatario,
			null,
			idPedidoColeta,
			servico.getIdServico(),
			municipioOrigem.getIdMunicipio(),
			filialOrigem.getIdFilial(),
			filialDestino.getIdFilial(),
			municipioDestino.getIdMunicipio(),
			enderecoPessoaOrigem.getNrCep(),
			enderecoPessoaDestino.getNrCep(),
			dtAtual
		);

		crt.setDtPrevEntrega((YearMonthDay)dpeMap.get("dtPrazoEntrega"));
		
		crt.setLocalizacaoMercadoria(null);
		
		Pais paisFilialOrigem = municipioOrigem.getUnidadeFederativa().getPais();
		Pais paisFilialDestino = municipioDestino.getUnidadeFederativa().getPais();
		
		PermissoEmpresaPais permissoEmpresaPais = permissoEmpresaPaisService.findByFilialOrigemDestino(paisFilialOrigem.getIdPais(), paisFilialDestino.getIdPais());
		crt.setPermissaoEmpresaPais(permissoEmpresaPais);
		crt.setNrPermisso(permissoEmpresaPais.getNrPermisso());

		crt.setSgPais(municipioOrigem.getUnidadeFederativa().getPais().getSgResumida());
		crt.setEmpresaEntregadora(null);

		BigDecimal pcFreteExportador = executeCalculoPcFreteExportador(crt.getTrechosCtoInternacional(), crt.getVlTotalDocServico());
		crt.setPcFreteExportador(pcFreteExportador);
		
		if(crt.getPedidoColeta() != null){
			//Coloca zero na versao da coleta pra fazer o insert do doctoServico
			PedidoColeta pedidoColeta = crt.getPedidoColeta();
			pedidoColeta.setVersao(IntegerUtils.ZERO);
		}

		//Seta o DoctoServico nas parcelas
		List parcelasDoctoServico = crt.getParcelaDoctoServicos();
		if(parcelasDoctoServico != null && !parcelasDoctoServico.isEmpty()){
			for(Iterator it = parcelasDoctoServico.iterator(); it.hasNext();){
				ParcelaDoctoServico parcelaDoctoServico = (ParcelaDoctoServico)it.next();
				parcelaDoctoServico.setDoctoServico(crt);
				parcelaDoctoServico.setIdParcelaDoctoServico(null);
			}
		}

		//Seta o DoctoServico nas ObservacoesDoctoServico
		List<ObservacaoDoctoServico> observacaoDoctoServicos = crt.getObservacaoDoctoServicos();
		if(observacaoDoctoServicos != null && !observacaoDoctoServicos.isEmpty()){
			for(ObservacaoDoctoServico observacaoDoctoServico : observacaoDoctoServicos) {
				observacaoDoctoServico.setDoctoServico(crt);
				observacaoDoctoServico.setIdObservacaoDoctoServico(null);
			}
		}

		//Seta o DoctoServico nas ObservacoesDoctoServico
		List<AduanaCtoInt> aduanasCtoInt = crt.getAduanasCtoInternacional();
		if(aduanasCtoInt != null) {
			for(AduanaCtoInt aduanaCtoInt : aduanasCtoInt) {
				aduanaCtoInt.setCtoInternacional(crt);
				aduanaCtoInt.setIdAduanaCtoInt(null);
			}
		}

		//Seta o DoctoServico nas ObservacoesDoctoServico
		List<DocumentoAnexo> documentosAnexos = crt.getDocumentosAnexos();
		if(documentosAnexos != null) {
			for(DocumentoAnexo documentoAnexo : documentosAnexos) {
				documentoAnexo.setIdDocumentoAnexo(null);
				documentoAnexo.setCtoInternacional(crt);
			}
		}

		//Seta o DoctoServico nos DespanhantesCtoInt
		List<DespachanteCtoInt> despachantesCtoInt = crt.getDespachantesCtoInternacional();
		if(despachantesCtoInt != null) {
			for(DespachanteCtoInt despachanteCtoInt : despachantesCtoInt) {
				despachanteCtoInt.setCtoInternacional(crt);
				despachanteCtoInt.setIdDespachanteCtoInt(null);
			}
		}

		//Seta o DoctoServico nos trechosCtoInt
		List<TrechoCtoInt> trechosCtoInt = crt.getTrechosCtoInternacional();
		if(trechosCtoInt != null) {
			for(TrechoCtoInt trechoCtoInt : trechosCtoInt) {
				trechoCtoInt.setCtoInternacional(crt);
				trechoCtoInt.getTramoFreteInternacional().setVersao(Integer.valueOf(0));
				trechoCtoInt.setIdTrechoCtoInt(null);
			}
		}

		//Seta o DoctoServico nas dimensoes
		List<Dimensao> dimensoes = crt.getDimensoes();
		if(dimensoes != null) {
			for(Dimensao dimensao : dimensoes) {
				dimensao.setCtoInternacional(crt);
				dimensao.setIdDimensao(null);
			}
		}

		//Seta o DoctoServico nas dimensoes
		List<ServAdicionalDocServ> servsAdicionalDocServ = crt.getServAdicionalDocServs();
		if(servsAdicionalDocServ != null) {
			for(ServAdicionalDocServ servAdicionalDocServ : servsAdicionalDocServ) {
				servAdicionalDocServ.setDoctoServico(crt);
				servAdicionalDocServ.setIdServAdicionalDocServ(null);
			}
		}

		//Calculos feitos para os devedoresDocServ e devedoresDocServFat 
		BigDecimal[] totalRemetenteDestinatario = getTotaisRementeDestinatario(crt.getTrechosCtoInternacional());
		BigDecimal vlTotalRemetente = totalRemetenteDestinatario[0];
		BigDecimal vlTotalDestinatario = totalRemetenteDestinatario[1];

		List<DevedorDocServ> devedorDocServs = getDevedorDocServ(crt, vlTotalRemetente, vlTotalDestinatario);
		crt.setDevedorDocServs(devedorDocServs);

		List<DevedorDocServFat> devedorDocServsFat = getDevedorDocServFat(crt, vlTotalRemetente, vlTotalDestinatario);
		crt.setDevedorDocServFats(devedorDocServsFat);

		//Arredonda os campos decimais para nao dar erro de precisao na inclusao e alteracao.
		crt.setVlDesconto(BigDecimalUtils.round(crt.getVlDesconto()));
		crt.setVlFreteExterno(BigDecimalUtils.round(crt.getVlFreteExterno()));
		crt.setVlMercadoria(BigDecimalUtils.round(crt.getVlMercadoria()));
		crt.setVlTotalDocServico(BigDecimalUtils.round(crt.getVlTotalDocServico()));
		crt.setVlTotalMercadoria(BigDecimalUtils.round(crt.getVlTotalMercadoria()));
		crt.setVlTotalParcelas(BigDecimalUtils.round(crt.getVlTotalParcelas()));
		crt.setVlTotalServicos(BigDecimalUtils.round(crt.getVlTotalServicos()));
		crt.setVlVolume(BigDecimalUtils.round(crt.getVlVolume()));

		emitirDocumentoService.generateProximoNumero(crt);//gera o proximo nrCrt e ja seta no CRT
		crt.setNrDoctoServico(crt.getNrCrt());

		return crt;
	}
	
	/**
	 * Atualiza o Crt excluindo as dependencias de trechos, parcelas, aduanas e documentos anexos.
	 * 
	 * @param crt
	 */
	public java.io.Serializable updateCrt(CtoInternacional crt){
		CtoInternacional crtRetorno = findById(crt.getIdDoctoServico());

		//Excluo as dependencias para depois reinclui-las
		trechoCtoIntService.removeByIdCtoInternacional(crt.getIdDoctoServico(), Boolean.TRUE);
		parcelaDoctoServicoService.removeByIdDoctoServico(crt.getIdDoctoServico(), Boolean.TRUE);
		aduanaCtoIntService.removeByIdCtoInternacional(crt.getIdDoctoServico(), Boolean.TRUE);
		documentoAnexoService.removeByIdDoctoServico(crt.getIdDoctoServico(), Boolean.TRUE);

		//Seta os valores da sessao para serem salvos
		List<TrechoCtoInt> trechosCtoInternacional = crt.getTrechosCtoInternacional();
		if(trechosCtoInternacional != null) {
			for(TrechoCtoInt trechoCtoInt : trechosCtoInternacional) {
				trechoCtoInt.setCtoInternacional(crtRetorno);
				trechoCtoInt.setIdTrechoCtoInt(null);
				trechoCtoInt.getTramoFreteInternacional().setVersao(Integer.valueOf(0));
			}
		}

		List<ParcelaDoctoServico> parcelasDoctoServico = crt.getParcelaDoctoServicos();
		if(parcelasDoctoServico != null && !parcelasDoctoServico.isEmpty()) {
			for(ParcelaDoctoServico parcelaDoctoServico : parcelasDoctoServico) {
				parcelaDoctoServico.setDoctoServico(crtRetorno);
			}
		}

		List<AduanaCtoInt> aduanasCtoInternacional = crt.getAduanasCtoInternacional();
		if(aduanasCtoInternacional != null) {
			for(AduanaCtoInt aduanaCtoInt : aduanasCtoInternacional) {
				aduanaCtoInt.setCtoInternacional(crtRetorno);
				aduanaCtoInt.setIdAduanaCtoInt(null);
			}
		}

		List<DocumentoAnexo> documentosAnexos = crt.getDocumentosAnexos();
		if(documentosAnexos != null) {
			for(DocumentoAnexo documentoAnexo : documentosAnexos) {
				documentoAnexo.setIdDocumentoAnexo(null);
				documentoAnexo.setCtoInternacional(crtRetorno);
			}
		} else {
			documentosAnexos = new ArrayList<DocumentoAnexo>(0);
		}

		crtRetorno.getTrechosCtoInternacional().clear();
		crtRetorno.getTrechosCtoInternacional().addAll(trechosCtoInternacional);
		crtRetorno.getParcelaDoctoServicos().clear();
		crtRetorno.getParcelaDoctoServicos().addAll(parcelasDoctoServico);
		crtRetorno.getAduanasCtoInternacional().clear();
		crtRetorno.getAduanasCtoInternacional().addAll(aduanasCtoInternacional);
		crtRetorno.getDocumentosAnexos().clear();
		crtRetorno.getDocumentosAnexos().addAll(documentosAnexos);

		crtRetorno.setDsDadosRemetente(crt.getDsDadosRemetente());
		crtRetorno.setDsDadosDestinatario(crt.getDsDadosDestinatario());
		crtRetorno.setDsDadosConsignatario(crt.getDsDadosConsignatario());
		crtRetorno.setDsNotificar(crt.getDsNotificar());
		crtRetorno.setDsLocalEntrega(crt.getDsLocalEntrega());
		crtRetorno.setDsLocalCarregamento(crt.getDsLocalCarregamento());
		crtRetorno.setDsLocalEntrega(crt.getDsLocalEntrega());
		crtRetorno.setDsDadosMercadoria(crt.getDsDadosMercadoria());
		crtRetorno.setPsReal(crt.getPsReal());
		crtRetorno.setPsLiquido(crt.getPsLiquido());
		crtRetorno.setVlVolume(crt.getVlVolume());
		crtRetorno.setVlMercadoria(crt.getVlMercadoria());
		crtRetorno.setMoedaValorMercadoria(crt.getMoedaValorMercadoria());
		crtRetorno.setVlTotalDocServico(crt.getVlTotalDocServico());
		crtRetorno.setVlTotalParcelas(crt.getVlTotalParcelas());
		crtRetorno.setVlTotalServicos(crt.getVlTotalServicos());
		crtRetorno.setVlDesconto(crt.getVlDesconto());
		crtRetorno.setPsReferenciaCalculo(crt.getPsReferenciaCalculo());
		crtRetorno.setTabelaPreco(crt.getTabelaPreco());
		crtRetorno.setMoeda(crt.getTabelaPreco().getMoeda());
		crtRetorno.setParametroCliente(crt.getParametroCliente());
		crtRetorno.setDivisaoCliente(crt.getDivisaoCliente());
		crtRetorno.setPcAforo(crt.getPcAforo());

		validateVlRemetenteDestinatario(crtRetorno);

		return store(crtRetorno);
	}

	@Override
	public CtoInternacional beforeInsert(CtoInternacional bean) {
		validateVlRemetenteDestinatario(bean);
		return super.beforeInsert(bean);
	}

	@Override
	public CtoInternacional beforeUpdate(CtoInternacional bean) {
		bean.setUsuarioByIdUsuarioAlteracao(SessionUtils.getUsuarioLogado());
		bean.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
		return super.beforeUpdate(bean);
	}

	/*
	 * Retorna um Array de duas posicoes, com a posicao ZERO = vlTotalRemetente e UM = vlTotalDestinatario 
	 */
	private BigDecimal[] getTotaisRementeDestinatario(List<TrechoCtoInt> trechosCtoInt){
		BigDecimal[] retorno = new BigDecimal[]{BigDecimalUtils.ZERO, BigDecimalUtils.ZERO};
		
		if(trechosCtoInt != null && !trechosCtoInt.isEmpty()){
			BigDecimal vlTotalRemetente = BigDecimalUtils.ZERO;
			BigDecimal vlTotalDestinatario = BigDecimalUtils.ZERO;

			for(TrechoCtoInt trechoCtoInt : trechosCtoInt) {
				vlTotalRemetente = vlTotalRemetente.add(trechoCtoInt.getVlFreteRemetente());
				vlTotalDestinatario = vlTotalDestinatario.add(trechoCtoInt.getVlFreteDestinatario());
			}

			retorno[0] = vlTotalRemetente; 
			retorno[1] = vlTotalDestinatario;
		}

		return retorno;
	}
	
	
	private List<DevedorDocServ> getDevedorDocServ(CtoInternacional ctoInternacional, BigDecimal vlTotalRemetente, BigDecimal vlTotalDestinatario){
		List<DevedorDocServ> devedoresDocServ = new ArrayList<DevedorDocServ>(2);

		if(CompareUtils.gt(vlTotalRemetente, BigDecimalUtils.ZERO)){
			DevedorDocServ devedorDocServ = new DevedorDocServ();
			Cliente cliente = ctoInternacional.getClienteByIdClienteRemetente();

			devedorDocServ.setCliente(cliente);
			devedorDocServ.setDoctoServico(ctoInternacional);
			devedorDocServ.setFilial(cliente.getFilialByIdFilialCobranca());
			devedorDocServ.setVlDevido(vlTotalRemetente);

			devedoresDocServ.add(devedorDocServ);
		}
		
		if(CompareUtils.gt(vlTotalDestinatario, BigDecimalUtils.ZERO)){
			DevedorDocServ devedorDocServ = new DevedorDocServ();
			Cliente cliente = ctoInternacional.getClienteByIdClienteDestinatario();

			devedorDocServ.setCliente(cliente);
			devedorDocServ.setDoctoServico(ctoInternacional);
			devedorDocServ.setFilial(cliente.getFilialByIdFilialCobranca());
			devedorDocServ.setVlDevido(vlTotalDestinatario);

			devedoresDocServ.add(devedorDocServ);
		}

		return devedoresDocServ;
	}
	
	private List<DevedorDocServFat> getDevedorDocServFat(CtoInternacional ctoInternacional, BigDecimal vlTotalRemetente, BigDecimal vlTotalDestinatario){
		List<DevedorDocServFat> devedoresDocServFat = new ArrayList<DevedorDocServFat>(2);
		// TODO: Confirmar se DivisaoCliente deve ser a mesma para os dois Clientes
		if(CompareUtils.gt(vlTotalRemetente, BigDecimalUtils.ZERO)){
			DevedorDocServFat devedorDocServFat = new DevedorDocServFat();
			Cliente cliente = ctoInternacional.getClienteByIdClienteRemetente();

			devedorDocServFat.setCliente(cliente);
			devedorDocServFat.setDoctoServico(ctoInternacional);
			devedorDocServFat.setFilial(cliente.getFilialByIdFilialCobranca());
			devedorDocServFat.setVlDevido(vlTotalRemetente);
			devedorDocServFat.setTpSituacaoCobranca(new DomainValue("P"));
			devedorDocServFat.setDivisaoCliente(getDivisaoClienteDevedor(cliente, ctoInternacional.getDivisaoCliente(), ctoInternacional.getServico()));

			devedoresDocServFat.add(devedorDocServFat);
		}
		
		if(CompareUtils.gt(vlTotalDestinatario, BigDecimalUtils.ZERO)){
			DevedorDocServFat devedorDocServFat = new DevedorDocServFat();
			Cliente cliente = ctoInternacional.getClienteByIdClienteDestinatario();

			devedorDocServFat.setCliente(cliente);
			devedorDocServFat.setDoctoServico(ctoInternacional);
			devedorDocServFat.setFilial(cliente.getFilialByIdFilialCobranca());
			devedorDocServFat.setVlDevido(vlTotalDestinatario);
			devedorDocServFat.setTpSituacaoCobranca(new DomainValue("P"));
			devedorDocServFat.setDivisaoCliente(getDivisaoClienteDevedor(cliente, ctoInternacional.getDivisaoCliente(), ctoInternacional.getServico()));

			devedoresDocServFat.add(devedorDocServFat);
		}
		
		return devedoresDocServFat;
	}

	/**
	 * Define a DivisaoCliente utilizado no objeto <b>DevedorDocServFat</b>
	 * @author Andre Valadas
	 * 
	 * @param cliente
	 * @param divisaoCliente
	 * @param servico
	 * @return
	 */
	private DivisaoCliente getDivisaoClienteDevedor(Cliente cliente, DivisaoCliente divisaoCliente, Servico servico) {
		if(divisaoCliente == null) {
			List<DivisaoCliente> divisoes = divisaoClienteService.findDivisaoCliente(cliente.getIdCliente(), servico.getTpModal(), servico.getTpAbrangencia());
			if(!divisoes.isEmpty()){
				return divisoes.get(0);
			}
		}
		return divisaoCliente;
	}
	
	public BigDecimal executeCalculoPcFreteExportador(List<TrechoCtoInt> trechosCtoInt, BigDecimal vlTotalFrete){
		BigDecimal pcFreteExportador = BigDecimalUtils.ZERO;

		if(trechosCtoInt != null && !trechosCtoInt.isEmpty()){
			BigDecimal vlTotalRemetente = BigDecimalUtils.ZERO;

			for(TrechoCtoInt trechoCtoInt : trechosCtoInt) {
				vlTotalRemetente = vlTotalRemetente.add(trechoCtoInt.getVlFreteRemetente());
			}

			if(!CompareUtils.eq(vlTotalRemetente, BigDecimalUtils.ZERO)){
				pcFreteExportador = BigDecimalUtils.divide(BigDecimalUtils.percent(vlTotalRemetente), vlTotalFrete);
			}
		}

		return BigDecimalUtils.round(pcFreteExportador);
	}

	private void validateVlRemetenteDestinatario(CtoInternacional ctoInternacional){
		List<TrechoCtoInt> trechosCtoInt = ctoInternacional.getTrechosCtoInternacional();
		BigDecimal vlTotalRemetenteDestinatario = BigDecimalUtils.ZERO;

		for(TrechoCtoInt trechoCtoInt : trechosCtoInt) {
			vlTotalRemetenteDestinatario = vlTotalRemetenteDestinatario.add(trechoCtoInt.getVlFreteRemetente());
			vlTotalRemetenteDestinatario = vlTotalRemetenteDestinatario.add(trechoCtoInt.getVlFreteDestinatario());
		}

		if(!CompareUtils.eq(vlTotalRemetenteDestinatario, ctoInternacional.getVlTotalDocServico())){
			throw new BusinessException("LMS-04160");
		}
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setCtoInternacionalDAO(CtoInternacionalDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private CtoInternacionalDAO getCtoInternacionalDAO() {
		return (CtoInternacionalDAO) getDao();
	}

	/**
	 * Busca os documentos de servico (Ctos Internacionais - CRT)
	 * a partir do id do Manifesto de entrega.
	 */
	public List<CtoInternacional> findCtosInternacionaisByIdManifestoEntrega(Long idManifesto){
		return getCtoInternacionalDAO().findCtosInternacionaisByIdManifestoEntrega(idManifesto);
	}

	/**
	 * Busca os documentos de servico (Ctos Internacionais - CRT)
	 * a partir do id do Manifesto de viagem internacional.
	 */
	public List<CtoInternacional> findCtosInternacionaisByIdManifestoViagemInternacional(Long idManifesto){
		return getCtoInternacionalDAO().findCtosInternacionaisByIdManifestoViagemInternacional(idManifesto);
	}

	public List findBySgPaisNrCrtByFilialFull(String sgPais, Long nrCrt, Long idFilialOrigem) {
		return getCtoInternacionalDAO().findBySgPaisNrCrtByFilialFull(sgPais, nrCrt, idFilialOrigem);
	}
	
	/**
	 * Retorna o tpSituacao do cto informado.
	 * 
	 * @author Mickaël Jalbert
	 * @since 10/01/2007
	 * 
	 * @param Long idCto
	 * @return DomainValue
	 * */	
	public DomainValue findTpSituacaoByCto(Long idCto) {
		return getCtoInternacionalDAO().findTpSituacaoByCto(idCto);
	}

	public CalculoFrete calcularFrete(DomainValue tpResponsavelFrete, Long idDivisaoCliente, CtoInternacional ctoInternacional){
		CalculoFrete calculo = new CalculoFrete();

		calculo.setBlCalculaParcelas(Boolean.TRUE);
		calculo.setBlCalculaServicosAdicionais(Boolean.TRUE);
		calculo.setTpConhecimento(ConstantesExpedicao.CONHECIMENTO_NORMAL);
		calculo.setTpCalculo(ConstantesExpedicao.CALCULO_NORMAL);
		calculo.setPsRealInformado(ctoInternacional.getPsReal());
		calculo.setPsCubadoInformado(ctoInternacional.getPsAforado());
		calculo.setQtVolumes(ctoInternacional.getQtVolumes());

		if("R".equals(tpResponsavelFrete.getValue())) {
			calculo.setClienteBase(ctoInternacional.getClienteByIdClienteRemetente());
		} else if("D".equals(tpResponsavelFrete.getValue())) {
			calculo.setClienteBase(ctoInternacional.getClienteByIdClienteDestinatario());
		}

		Cliente clienteRemetente = ctoInternacional.getClienteByIdClienteRemetente();
		Pessoa pessoaRemetente = clienteRemetente.getPessoa();
		EnderecoPessoa enderecoPessoa = pessoaRemetente.getEnderecoPessoa();
		Municipio municipioRemetente = enderecoPessoa.getMunicipio(); 

		Cliente clienteDestinatario = ctoInternacional.getClienteByIdClienteDestinatario();
		Pessoa pessoaDestinatario = clienteDestinatario.getPessoa();
		EnderecoPessoa enderecoDestinatario = pessoaDestinatario.getEnderecoPessoa();
		Municipio municipioDestinatario = enderecoDestinatario.getMunicipio();

		RestricaoRota restricaoRotaOrigem = calculo.getRestricaoRotaOrigem();
		restricaoRotaOrigem.setIdFilial(ctoInternacional.getFilialByIdFilialOrigem().getIdFilial());
		restricaoRotaOrigem.setIdMunicipio(municipioRemetente.getIdMunicipio());

		RestricaoRota restricaoRotaDestino = calculo.getRestricaoRotaDestino();
		restricaoRotaDestino.setIdFilial(ctoInternacional.getFilialByIdFilialDestino().getIdFilial());
		restricaoRotaDestino.setIdMunicipio(municipioDestinatario.getIdMunicipio());

		calculo.setVlMercadoria(ctoInternacional.getVlMercadoria());
		calculo.setIdServico(ctoInternacional.getServico().getIdServico());
		calculo.setServAdicionalDoctoServico(ctoInternacional.getServAdicionalDocServs());
		calculo.setIdDivisaoCliente(idDivisaoCliente);

		Servico servico = servicoService.findById(ctoInternacional.getServico().getIdServico()); 

		calculo.setTpAbrangencia(servico.getTpAbrangencia().getValue());
		calculo.setTpModal(servico.getTpModal().getValue());
		documentoServicoFacade.executeCalculoConhecimentoInternacionalNormal(calculo);
		return calculo;
	}
	
	public Short executeCalculoNrDiasPrevEntrega(Long idMunicipioOrigem, Long idMunicipioDestino, Long idServico, Long idCliente, String cepOrigem, String cepDestino, Long idSegmento, YearMonthDay dtConsulta, String blGeracaoMCD){
		Map<String, Object> prazos = new HashMap<String, Object>();
		prazos = ppeService.executeCalculoPPE(
				idMunicipioOrigem,
				idMunicipioDestino,
				idServico,
				idCliente,
				cepOrigem,
				cepDestino,
				idSegmento,
				dtConsulta,
				blGeracaoMCD
		);
		
		Long prazoEntrega = (Long) prazos.get("nrPrazo");

		double dppe = (double)prazoEntrega.longValue()/24;
		Short diasPPE = Short.valueOf((short)Math.ceil(dppe));

		return diasPPE;
	}
	
	/**
	 * Atualiza a situação da pendência e a data de alteração do documento de serviço especializado pelo CRT.
	 * 
	 * @param List idsCtoInternacional IDs dos CRTs(Long)
	 * @param List tpSituacoesWorkflow Situações das pendências de reemissão dos CRTs(DomainValue) 
	 */
	public void executeWorkflow(List<Long> idsCtoInternacional, List<DomainValue> tpSituacoesWorkflow) {
		CtoInternacional ctoInternacional = null;
		int cont = 0;
		for(Long idCtoInternacional : idsCtoInternacional) {
			ctoInternacional = findById(idCtoInternacional);

			if(ctoInternacional != null) {
				ctoInternacional.setTpSituacaoPendenciaReemissao(tpSituacoesWorkflow.get(cont));

				this.store(ctoInternacional);
			}
		}
	}

	public void setPpeService(PpeService ppeService) {
		this.ppeService = ppeService;
	}
	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}
	public void setMcdService(McdService mcdService) {
		this.mcdService = mcdService;
	}
	public void setDpeService(DpeService dpeService) {
		this.dpeService = dpeService;
	}
	public void setPermissoEmpresaPaisService(PermissoEmpresaPaisService permissoEmpresaPaisService) {
		this.permissoEmpresaPaisService = permissoEmpresaPaisService;
	}
	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}
	public void setTrechoCtoIntService(TrechoCtoIntService trechoCtoIntService) {
		this.trechoCtoIntService = trechoCtoIntService;
	}
	public void setParcelaDoctoServicoService(ParcelaDoctoServicoService parcelaDoctoServicoService) {
		this.parcelaDoctoServicoService = parcelaDoctoServicoService;
	}
	public void setAduanaCtoIntService(AduanaCtoIntService aduanaCtoIntService) {
		this.aduanaCtoIntService = aduanaCtoIntService;
	}
	public void setDocumentoAnexoService(DocumentoAnexoService documentoAnexoService) {
		this.documentoAnexoService = documentoAnexoService;
	}
	public void setParametroPaisService(ParametroPaisService parametroPaisService) {
		this.parametroPaisService = parametroPaisService;
	}
	public void setDocumentoServicoFacade(DocumentoServicoFacade documentoServicoFacade) {
		this.documentoServicoFacade = documentoServicoFacade;
	}
	public void setEmitirDocumentoService(EmitirDocumentoService emitirDocumentoService) {
		this.emitirDocumentoService = emitirDocumentoService;
	}
	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}
}