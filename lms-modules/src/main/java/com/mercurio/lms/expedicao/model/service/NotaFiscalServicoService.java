package com.mercurio.lms.expedicao.model.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.service.DevedorDocServFatService;
import com.mercurio.lms.expedicao.model.ImpostoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.model.ParcelaDoctoServico;
import com.mercurio.lms.expedicao.model.ServAdicionalDocServ;
import com.mercurio.lms.expedicao.model.dao.NotaFiscalServicoDAO;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.tributos.model.service.AliquotaIssMunicipioServService;
import com.mercurio.lms.tributos.model.service.CalcularPisCofinsCsllIrInssService;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.service.CotacaoService;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.notaFiscalServicoService"
 */
public class NotaFiscalServicoService extends CrudService<NotaFiscalServico, Long> {
	private ServAdicionalDocServService servAdicionalDocServService;
	private CotacaoService cotacaoService;
	private EnderecoPessoaService enderecoPessoaService;
	private ObservacaoDoctoServicoService observacaoDoctoServicoService;
	private DevedorDocServFatService devedorDocServFatService;
	private CalcularPisCofinsCsllIrInssService calcularPisCofinsCsllIrInssService;
	private DoctoServicoService doctoServicoService;
	private AliquotaIssMunicipioServService aliquotaIssMunicipioServService;
	private PessoaService pessoaService;
	private ConfiguracoesFacade configuracoesFacade;
	private ParcelaPrecoService parcelaPrecoService;
	private ParcelaDoctoServicoService parcelaDoctoServicoService;

	/**
	 * Retorna o Número da Nota Fiscal de Serviço.<BR>
	 * @param idNotaFiscalServico
	 * @return
	 */
	public Long findNrNotaFiscalServicoById(Long idNotaFiscalServico){
		return getNotaFiscalServicoDAO().findNrNotaFiscalServicoById(idNotaFiscalServico);
	}

	public List<Map<String, Object>> findNotaFiscalServicoByDevedorDocServFat(Map<String, Object> map) {
		return getNotaFiscalServicoDAO().findNotaFiscalServicoDevedorDocServFat(map);
	}

	public ResultSetPage findPaginatedDevedorDocServFat(Map<String, Object> map) {
		return getNotaFiscalServicoDAO().findPaginatedDevedorDocServFat(map, FindDefinition.createFindDefinition(map));
	}

	public Integer getRowCountDevedorDocServFat(Map<String, Object> criteria) {
		return getNotaFiscalServicoDAO().getRowCountDevedorDocServFat(criteria);
	}

	/**
	 * Recupera um mapa de <code>NotaFiscalServico</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public Map<String, Object> findNotaFiscalServicoById(Long id) {
		List<Map<String, Object>> notaFiscalServico = getNotaFiscalServicoDAO().findNotaFiscalServicoById(id);
		if(!notaFiscalServico.isEmpty()) {
			Map<String, Object> result = notaFiscalServico.get(0);
			Cotacao cotacao = cotacaoService.findByIdDoctoServico(id);
			if(cotacao != null) {
				result.put("cotacao_idCotacao", cotacao.getIdCotacao());
				result.put("cotacao_nrCotacao", cotacao.getNrCotacao());
				result.put("cotacao_sgFilial", cotacao.getFilial().getSgFilial());
			}
			ServAdicionalDocServ servAdicionalDocServ = servAdicionalDocServService.findByIdDoctoServico(id);
			if(servAdicionalDocServ!=null) {
				result.put("servicoAdicional_qtDias",servAdicionalDocServ.getQtDias());
				result.put("servicoAdicional_qtColetas",servAdicionalDocServ.getQtColetas());
				result.put("servicoAdicional_qtPaletes",servAdicionalDocServ.getQtPaletes());
				result.put("servicoAdicional_qtSegurancasAdicionais",servAdicionalDocServ.getQtSegurancasAdicionais());
				result.put("servicoAdicional_nrKmRodado",servAdicionalDocServ.getNrKmRodado());
				result.put("servicoAdicional_dtPrimeiroCheque",servAdicionalDocServ.getDtPrimeiroCheque());
				result.put("servicoAdicional_qtCheques",servAdicionalDocServ.getQtCheques());
				result.put("servicoAdicional_idServicoAdicional",servAdicionalDocServ.getServicoAdicional().getIdServicoAdicional());
				result.put("servicoAdicional_vlMercadoria",servAdicionalDocServ.getVlMercadoria());
				result.put("servicoAdicional_vlFrete",servAdicionalDocServ.getVlFrete());
				
			}

			List<ParcelaDoctoServico> listParcelaDoctoServico = parcelaDoctoServicoService.findByIdDoctoServico(id);
			if( listParcelaDoctoServico != null && listParcelaDoctoServico.size() > 0  ){
				result.put("parcelaPreco_idParcelaPreco",listParcelaDoctoServico.get(0).getParcelaPreco().getIdParcelaPreco());
				result.put("parcelaPreco_cdParcelaPreco",listParcelaDoctoServico.get(0).getParcelaPreco().getCdParcelaPreco());
			}
			
			Long idCliente = MapUtils.getLong(result,"idCliente");
			DomainValue tpPessoa = MapUtilsPlus.getDomainValue(result,"tpPessoa");
			DomainValue tpIdentificacao = MapUtilsPlus.getDomainValue(result, "clienteByIdClienteDestinatario_pessoa_tpIdentificacao");
			String nrIdentificacao = MapUtilsPlus.getString(result, "clienteByIdClienteDestinatario_pessoa_nrIdentificacao");
			/** Identificacao Formatada */
			result.put("clienteByIdClienteDestinatario_pessoa_nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(tpIdentificacao.getValue(), nrIdentificacao));

			Map<String, Object> enderecoPessoaFull = enderecoPessoaService.findEnderecoByIdPessoaTpPessoa(tpPessoa.getValue(),idCliente);
			Map<String, Object> endereco = (Map<String, Object>)enderecoPessoaFull.get("endereco");
			if(endereco != null) {
				result.put("clienteByIdClienteDestinatario_endereco_dsEndereco",endereco.get("dsEndereco"));
				result.put("clienteByIdClienteDestinatario_endereco_nrEndereco",endereco.get("nrEndereco"));
				result.put("clienteByIdClienteDestinatario_endereco_dsComplemento",endereco.get("dsComplemento"));
				result.put("clienteByIdClienteDestinatario_endereco_nmMunicipio",endereco.get("nmMunicipio"));
				result.put("clienteByIdClienteDestinatario_endereco_sgUnidadeFederativa",endereco.get("sgUnidadeFederativa"));
				result.put("clienteByIdClienteDestinatario_endereco_nrCep",endereco.get("nrCep"));
			}
			int obsCount = 0;
			List<ObservacaoDoctoServico> laux = observacaoDoctoServicoService.findByIdDoctoServico(id);
			for(ObservacaoDoctoServico ods : laux) {
				result.put("obs"+obsCount,ods.getDsObservacaoDoctoServico());
				obsCount++;
			}

			notaFiscalServico = AliasToNestedMapResultTransformer.getInstance().transformListResult(notaFiscalServico);
			return (Map<String, Object>)notaFiscalServico.get(0); 
		}
		return null;
	}

	public NotaFiscalServico removeCancelaNF(Long idNotaFiscalServico) {
		if (!devedorDocServFatService.findCtrcFaturado(idNotaFiscalServico)) {
			throw new BusinessException("LMS-04115");
		}

		NotaFiscalServico notaFiscalServico = findById(idNotaFiscalServico);
		if(!SessionUtils.getFilialSessao().getIdFilial().equals(notaFiscalServico.getFilialByIdFilialOrigem().getIdFilial())){
			throw new BusinessException("LMS-04104");
		}
		if(!ConstantesExpedicao.DOCUMENTO_SERVICO_EMITIDO.equals(notaFiscalServico.getTpSituacaoNf().getValue())) {
			throw new BusinessException("LMS-04103");
		}
		/** Verificar se a data de emissão da Nota Fiscal (DOCTO_SERVICO. 
		 *  DH_EMISSAO) é menor que o mês atual e o dia atual é posterior ao 
		 *  quinto dia do mês. Se for exibir a mensagem LMS-04212.  */
		if(notaFiscalServico.getDhEmissao().getMonthOfYear() != JTDateTimeUtils.getDataHoraAtual().getMonthOfYear()
			&& JTDateTimeUtils.getDataHoraAtual().getDayOfMonth() > 5) {
			throw new BusinessException("LMS-04212");
		}

		notaFiscalServico.setTpSituacaoNf(new DomainValue(ConstantesExpedicao.DOCUMENTO_SERVICO_CANCELADO));
		notaFiscalServico.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
		notaFiscalServico.setDhCancelamento(JTDateTimeUtils.getDataHoraAtual());
		store(notaFiscalServico);

		List<ImpostoServico> impostosServico = notaFiscalServico.getImpostoServicos();
		ImpostoServico pis = null;
		ImpostoServico cofins = null;
		ImpostoServico csll = null;
		ImpostoServico ir = null;
		ImpostoServico inss = null;

		for(ImpostoServico impostoServico : impostosServico) {
			String tpImposto = impostoServico.getTpImposto().getValue();
			if(ConstantesExpedicao.CD_PIS.equals(tpImposto)) {
				pis = impostoServico;
			} else if(ConstantesExpedicao.CD_COFINS.equals(tpImposto)) {
				cofins = impostoServico;
			} else if(ConstantesExpedicao.CD_CSLL.equals(tpImposto)) {
				csll = impostoServico;
			} else if(ConstantesExpedicao.CD_IMPOSTO_DE_RENDA.equals(tpImposto)) {
				ir = impostoServico;
			} else if(ConstantesExpedicao.CD_INSS.equals(tpImposto)) {
				inss = impostoServico;
			}
		}

		Long idCliente = doctoServicoService.findIdClienteDestinatarioById(idNotaFiscalServico);
		if (pessoaService.validateTipoPessoa(idCliente, "J")){
			calcularPisCofinsCsllIrInssService.estornarPisCofinsCsllIrInssPessoaJudirica(
				doctoServicoService.findIdClienteDestinatarioById(idNotaFiscalServico),
				"OU",
				notaFiscalServico.getDhEmissao().toYearMonthDay(),
				(pis!=null ? pis.getVlBaseEstorno() : BigDecimalUtils.ZERO),
				(pis!=null ? pis.getVlImposto() : BigDecimalUtils.ZERO),
				(cofins!=null ? cofins.getVlBaseEstorno() : BigDecimalUtils.ZERO),
				(cofins!=null ? cofins.getVlImposto() : BigDecimalUtils.ZERO),
				(csll!=null ? csll.getVlBaseEstorno() : BigDecimalUtils.ZERO),
				(csll!=null ? csll.getVlImposto() : BigDecimalUtils.ZERO),
				(ir!=null ? ir.getVlBaseEstorno() : BigDecimalUtils.ZERO),
				(ir!=null ? ir.getVlImposto() : BigDecimalUtils.ZERO),
				(inss!=null ? inss.getVlBaseEstorno() : BigDecimalUtils.ZERO),
				(inss!=null ? inss.getVlImposto() : BigDecimalUtils.ZERO)
			);
		}

		List<DevedorDocServFat> devedoresDocServFat = notaFiscalServico.getDevedorDocServFats();
		DevedorDocServFat devedorDocServFat = devedoresDocServFat.get(0);
		devedorDocServFat.setTpSituacaoCobranca(new DomainValue("L"));
		devedorDocServFat.setDtLiquidacao( YearMonthDay.fromDateFields(notaFiscalServico.getDhEmissao().toDate()));
		devedorDocServFatService.store(devedorDocServFat);
		return notaFiscalServico;
	}

	public NotaFiscalServico findById(java.lang.Long id) {
		return (NotaFiscalServico)super.findById(id);
	}

	public List<NotaFiscalServico> findByNrnotaFiscalByFilial(Long nrNotaFiscal, Long idFilial) {
		return getNotaFiscalServicoDAO().findByNrnotaFiscalByFilial(nrNotaFiscal, idFilial);
	}

	public List<NotaFiscalServico> findDocumentosServico(Long idNotaFiscalServico) {
		return getNotaFiscalServicoDAO().findDocumentosServico(idNotaFiscalServico);
	}

	public void findPossibilidadeEmissaoNotaFiscal(Long idFilial) {
		String blEmissaoNotaFiscal = (String)configuracoesFacade.getValorParametro(idFilial, "IMPRIME_NF_SERVICO");
		if(!"S".equalsIgnoreCase(blEmissaoNotaFiscal)) {
			throw new BusinessException("LMS-04096");
		}
	}

	public void validateMunicipioEmiteNFServicoAdicional(Long idServicoAdicional, Long idMunicipioSede, Long idMunicipioServico){
		TypedFlatMap emiteNfServico = aliquotaIssMunicipioServService.findEmiteNfServico(idServicoAdicional, null, idMunicipioSede, idMunicipioServico);
		if(Boolean.FALSE.equals(emiteNfServico.get("BlEmiteNota"))) {
			throw new BusinessException("LMS-04097");
		}
	}
	
	public List<Map<String, Object>> findByNrNotaFiscalServicoIdFilialOrigem(Long nrNotaFiscalServico, Long idFilial, String tpDocumentoServico) {
		List<Map<String, Object>> result = getNotaFiscalServicoDAO().findByNrNotaFiscalServicoIdFilialOrigem(nrNotaFiscalServico, idFilial, tpDocumentoServico);
		for(Map<String, Object> item : result) {
			Timestamp timestamp = (Timestamp)item.remove("dhEmissao");
			DateTime dateTime = JodaTimeUtils.toDateTime(getNotaFiscalServicoDAO().getAdsmHibernateTemplate(), timestamp);
			item.put("dhEmissao", dateTime);
		}
		return result;
	}

	/**
	 * Apaga uma entidade através do Id.
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(NotaFiscalServico bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * @param Instância do DAO.
	 */
	public void setNotaFiscalServicoDAO(NotaFiscalServicoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 * @return Instância do DAO.
	 */
	private NotaFiscalServicoDAO getNotaFiscalServicoDAO() {
		return (NotaFiscalServicoDAO) getDao();
	}

	/**
	 * getRowCountNFS.
	 * @author Andre Valadas
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountNFS(TypedFlatMap criteria) {
		return getNotaFiscalServicoDAO().getRowCountNFS(criteria);
	}

	/**
	 * findPaginatedNFS.
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedNFS(TypedFlatMap criteria) {
		ResultSetPage rsp = getNotaFiscalServicoDAO().findPaginatedNFS(criteria, FindDefinition.createFindDefinition(criteria));
		List<Map<String, Object>> toReturn = rsp.getList();
		for (Map<String, Object> data : toReturn) {
			Long idServicoAdicional = MapUtils.getLong(data, "idServicoAdicional");
			if(idServicoAdicional != null) {
				ParcelaPreco parcelaPreco = parcelaPrecoService.findByIdServicoAdicional(idServicoAdicional);
				data.put("dsServicoAdicional", parcelaPreco.getDsParcelaPreco().getValue());
			}
		}
		return rsp;
	}

	/**
	 * Busca os documentos de servico (Notas Fiscais Servico - NFS)
	 * a partir do id do Manifesto.
	 */
	public List<NotaFiscalServico> findNotasFiscaisServicoByIdManifesto(Long idManifesto){
		return getNotaFiscalServicoDAO().findNotasFiscaisServicoByIdManifesto(idManifesto);
	}

	
	/**
	 * Busca os dados da Nota Fiscal Servico para enviar para enviar para a NDDigital, gerando dessa forma a nota fiscal de serviço
	 * eletronica - NSE (Nota de serviço eletronica)
	 * @param idNotaFiscalServico
	 * @return
	 */
	public NotaFiscalServico findDadosNseToNotaFiscalEletronica(Long idNotaFiscalServico){
		return getNotaFiscalServicoDAO().findDadosNseToNotaFiscalEletronica(idNotaFiscalServico);
	}

	/**
	 * Retorna a situacao da nota fiscal de servico informado
	 * @author Mickaël Jalbert
	 * @since 10/01/2007
	 * @param Long idNotaFiscalServico
	 * @return DomainValue
	 */
	public DomainValue findTpSituacaoById(Long idNotaFiscalServico){
		return getNotaFiscalServicoDAO().findTpSituacaoById(idNotaFiscalServico);
	}	
	public void setAliquotaIssMunicipioServService(AliquotaIssMunicipioServService aliquotaIssMunicipioServService) {
		this.aliquotaIssMunicipioServService = aliquotaIssMunicipioServService;
	}
	public void setCotacaoService(CotacaoService cotacaoService) {
		this.cotacaoService = cotacaoService;
	}
	public void setServAdicionalDocServService(ServAdicionalDocServService servAdicionalDocServService) {
		this.servAdicionalDocServService = servAdicionalDocServService;
	}
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	public void setCalcularPisCofinsCsllIrInssService(CalcularPisCofinsCsllIrInssService calcularPisCofinsCsllIrInssService) {
		this.calcularPisCofinsCsllIrInssService = calcularPisCofinsCsllIrInssService;
	}
	public void setDevedorDocServFatService(DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}
	public void setObservacaoDoctoServicoService(ObservacaoDoctoServicoService observacaoDoctoServicoService) {
		this.observacaoDoctoServicoService = observacaoDoctoServicoService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}

	public void setParcelaDoctoServicoService(
			ParcelaDoctoServicoService parcelaDoctoServicoService) {
		this.parcelaDoctoServicoService = parcelaDoctoServicoService;
	}
}