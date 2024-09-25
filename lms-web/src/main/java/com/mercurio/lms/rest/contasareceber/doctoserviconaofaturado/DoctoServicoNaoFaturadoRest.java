package com.mercurio.lms.rest.contasareceber.doctoserviconaofaturado;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.RegionalFilialService;
import com.mercurio.lms.municipios.model.service.RegionalService;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.contasareceber.doctoserviconaofaturado.dto.DoctoServicoNaoFaturadoDTO;
import com.mercurio.lms.rest.contasareceber.doctoserviconaofaturado.dto.DoctoServicoNaoFaturadoFilterDTO;
import com.mercurio.lms.util.session.SessionUtils;
import org.apache.commons.io.FileUtils;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/contasareceber/doctoServicoNaoFaturado")
public class DoctoServicoNaoFaturadoRest
		extends
		LmsBaseCrudReportRest<DoctoServicoNaoFaturadoDTO, DoctoServicoNaoFaturadoDTO, DoctoServicoNaoFaturadoFilterDTO> {

	@InjectInJersey
	private ServicoService servicoService;
	
	@InjectInJersey
	private RegionalFilialService regionalFilialService;

	@InjectInJersey
	private RegionalService regionalService;

	@InjectInJersey
	private DoctoServicoService doctoServicoService;

	private List<DoctoServicoNaoFaturadoDTO> data;

	@InjectInJersey
	private IntegracaoJmsService integracaoJmsService;

	@InjectInJersey
	private ConfiguracoesFacade configuracoesFacade;

	@Override
	protected List<Map<String, String>> getColumns() {
		List<Map<String, String>> list = new ArrayList<>();
		list.add(getColumn("lbTpLayoutDocumento", "tipodocumento"));
		list.add(getColumn("filialOrigem2", "filialdeorigem"));
		list.add(getColumn("nrDocumento", "numerodocumento"));
		list.add(getColumn("filialDestino", "filialdedestino"));
		list.add(getColumn("dataEmissao", "datadeemissao"));
		list.add(getColumn("cnpjDevedor","cnpjdevedor"));
		list.add(getColumn("Razaosocialdevedor","razaosocialdevedor"));
		list.add(getColumn("tipoPessoaDevedor","tipopessoadevedor"));
		list.add(getColumn("tpCliente","tipocliente"));
		list.add(getColumn("valorDevido","valordevido"));
		list.add(getColumn("Numdoceletronico","numdoceletronico"));
		list.add(getColumn("Situacaodoceletronico","situacaodoceletronico"));
		list.add(getColumn("tipoFrete","tipofrete"));
		list.add(getColumn("tipoConhecimento","tipoconhecimento"));
		list.add(getColumn("tipoCalculo","tipocalculo"));
		list.add(getColumn("modal","modal"));
		list.add(getColumn("abrangencia","abrangencia"));
		list.add(getColumn("servico","servico"));
		list.add(getColumn("dataEntrega","datadeentrega"));
		list.add(getColumn("Valormercadoria","valormercadoria"));
		list.add(getColumn("Valorfretetotal","valorfretetotal"));
		list.add(getColumn("valorFreteLiquido","valorfreteliquido"));
		list.add(getColumn("vlrIcms","valoricms"));
		list.add(getColumn("PICMS","icms"));
		list.add(getColumn("icmsSt","valoricmsst"));
		list.add(getColumn("pesoAferido","pesoaferido"));
		list.add(getColumn("pesoReal","pesoreal"));
		list.add(getColumn("qtdVolumes","qtdvolumes"));
		list.add(getColumn("notasFiscais","notasfiscais"));
		list.add(getColumn("CNPJremetente","cnpjremetente"));
		list.add(getColumn("Razaosocialremetente","razaosocialremetente"));
		list.add(getColumn("municipioRemetente","municipioremetente"));
		list.add(getColumn("ufRemetente","ufRemetente"));
		list.add(getColumn("CNPJdestinatario","cnpjdestinatario"));
		list.add(getColumn("Razaosocialdestinatario","razaosocialdestinatario"));
		list.add(getColumn("municipioDestinatario","municipiodestinatario"));
		list.add(getColumn("ufDestinatario","ufdestinatario"));
		list.add(getColumn("tipoCobranca","tipocobranca"));
		list.add(getColumn("clienteComPreFatura","clientecomprefatura"));
		list.add(getColumn("classificacaoCliente","classificacaocliente"));
		list.add(getColumn("diCobranca","divisaocobranca"));
		list.add(getColumn("Periodicidadedadi","periodicidadedadivisao"));
		list.add(getColumn("cobrancaCentralizada","cobrancacentralizada"));
		list.add(getColumn("Filialresponsavelcliente","filialresponsavelcliente"));
		list.add(getColumn("regionalCobranca","regionalcobranca"));
		list.add(getColumn("filialCobranca","filialcobranca"));
		list.add(getColumn("faturasAnteriores","faturasanteriores"));
		list.add(getColumn("estadoCobranca","situacaocobrancafrete"));
		list.add(getColumn("bloqueado","bloqueado"));
		list.add(getColumn("possuiAgendasTransf","possuiagendastransf"));
		list.add(getColumn("possuiTransPendentes","possuitransfpendentes"));
		list.add(getColumn("dtNatura","dtnatura"));
		list.add(getColumn("chaveCTE","chavecte"));
		return list;
	}

	protected List<Map<String, Object>> findDataForReport(DoctoServicoNaoFaturadoFilterDTO filter) {
		validateRequiredFields(filter);
		TypedFlatMap filters = convertFilterToTypedFlatMap(filter);

		List<Map<String, Object>> list = doctoServicoService.findDoctoServicoNaoFaturadoReport(filters);
		String message = configuracoesFacade.getMensagem("LMS-36421");

		if(list.size() > 0) {
			try {
				Boolean exibeDtNatura = (filter).getBlExportaDtNatura();
				File file = createFileColumnDTNatura(list, exibeDtNatura);
				message = configuracoesFacade.getMensagem("LMS-36420").replace(":file", file.getName());
				IntegracaoJmsService.JmsMessageSender msg = integracaoJmsService.createMessage(Queues.RELATORIOS_NAO_FATURADOS, Normalizer.normalize(new String(FileUtils.readFileToByteArray(file)), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));
				msg.addHeader("filename", file.getName());
				integracaoJmsService.storeMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				message = configuracoesFacade.getMensagem("LMS-36422");
			}
		}

		Mail mail = doctoServicoService.createMail(SessionUtils.getUsuarioLogado().getDsEmail(), null, "Relatório Não Faturados", message, new ArrayList<>());
		IntegracaoJmsService.JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);

		return null;
	}

	@Override
	protected DoctoServicoNaoFaturadoDTO findById(Long id) {
		return null;
	}

	@Override
	protected Long store(DoctoServicoNaoFaturadoDTO bean) {
		return null;
	}

	@Override
	protected void removeById(Long id) {
	}

	@Override
	protected void removeByIds(List<Long> ids) {
	}

	@Override
	protected List<DoctoServicoNaoFaturadoDTO> find(DoctoServicoNaoFaturadoFilterDTO filter) {
		validateRequiredFields(filter);
		List<Map<String, Object>> d = doctoServicoService.findDoctoServicoNaoFaturado( convertFilterToTypedFlatMap(filter) );
		data = convertToDoctoServicoNaoFaturadoDTO( d ) ;
		return data;
	}

	void validateRequiredFields(DoctoServicoNaoFaturadoFilterDTO filter){
		TypedFlatMap maps = convertFilterToTypedFlatMap(filter);
		maps.remove("_currentPage");
		maps.remove("_pageSize");
		boolean hasData = false;

		for (Object o : maps.values()) {
			if (o != null) {
				hasData = true;
				break;
			}
		}

		if("MTZ".equals(SessionUtils.getFilialSessao().getSgFilial()) ){
			if ( !hasData ){
				throw new BusinessException("LMS-00055");
			}
		}else if ( maps.getLong("idRegional") == null){
			throw new BusinessException("LMS-36335");
		}
	}
	
	private TypedFlatMap convertFilterToTypedFlatMap(DoctoServicoNaoFaturadoFilterDTO filter) {
		TypedFlatMap mapReturn = super.getTypedFlatMapWithPaginationInfo(filter);
		
		if ( filter.getDevedoresExcluir() != null ){
			mapReturn.put("devedoresExcluir", filter.getDevedoresExcluir());
		}
		
		if ( filter.getDevedoresListar() != null ){
			mapReturn.put("devedoresListar", filter.getDevedoresListar());
		}
		if ( filter.getFilial() != null ){
			mapReturn.put("filial", filter.getFilial().getIdFilial());
		}
		if ( filter.getTpCliente() != null ){
			mapReturn.put("tpCliente", filter.getTpCliente().getValue());
		}
		if ( filter.getClassificacaoCliente() != null ){
			mapReturn.put("classificacaoCliente", filter.getClassificacaoCliente().getValue());
		}
		if ( filter.getTpCobranca() != null ){
			mapReturn.put("tpCobranca", filter.getTpCobranca().getValue());
		}
		if ( filter.getBlCobrancaCentralizada() != null ){
			mapReturn.put("blCobrancaCentralizada", filter.getBlCobrancaCentralizada().getValue());
		}
		if ( filter.getBlPreFatura() != null ){
			mapReturn.put("blPreFatura", filter.getBlPreFatura().getValue());
		}
		if ( filter.getIdRegional() != null ){
			mapReturn.put("idRegional", filter.getIdRegional());
		}
		if ( filter.getIdFilialCobranca() != null ){
			mapReturn.put("idFilialCobranca", filter.getIdFilialCobranca().getIdFilial());
		}
		if ( filter.getDtVigenciaInicial() != null ){
			mapReturn.put("dtVigenciaInicial", filter.getDtVigenciaInicial());
		}
		if ( filter.getDtVigenciaFinal() != null ){
			mapReturn.put("dtVigenciaFinal", filter.getDtVigenciaFinal());
		}
		if ( filter.getIdServico() != null ){
			mapReturn.put("idServico", filter.getIdServico());
		}
		if ( filter.getTpDocumento() != null ){
			mapReturn.put("tpDocumento", filter.getTpDocumento().getValue());
		}
		if ( filter.getTpFrete() != null ){
			mapReturn.put("tpFrete", filter.getTpFrete().getValue());
		}
		if ( filter.getTpConhecimento() != null ){
			mapReturn.put("tpConhecimento", filter.getTpConhecimento().getValue());
		}
		if ( filter.getTpCalculo() != null ){
			mapReturn.put("tpCalculo", filter.getTpCalculo().getValue());
		}
		if ( filter.getBlBloqueado() != null ){
			mapReturn.put("blBloqueado", filter.getBlBloqueado().getValue());
		}
		if ( filter.getBlAgendaTransferencia() != null ){
			mapReturn.put("blAgendaTransferencia", filter.getBlAgendaTransferencia().getValue());
		}
		if ( filter.getBlTransferenciaPendentes() != null ){
			mapReturn.put("blTransferenciaPendentes", filter.getBlTransferenciaPendentes().getValue());
		}
		if ( filter.getFilialCobDifResp() != null ){
			mapReturn.put("filialCobDifResp", filter.getFilialCobDifResp().getValue());
		}
		if ( filter.getEstadoCobranca() != null ){
			mapReturn.put("estadoCobranca", filter.getEstadoCobranca().getValue());
		}
		return mapReturn;
	}

	private List<DoctoServicoNaoFaturadoDTO> convertToDoctoServicoNaoFaturadoDTO (List<Map<String, Object>> list) {
		List<DoctoServicoNaoFaturadoDTO> dispositivos = new ArrayList<>();
		for (Map<String, Object> item : list) {
			DoctoServicoNaoFaturadoDTO doctoServicoNaoFaturadoDTO = new DoctoServicoNaoFaturadoDTO();
			doctoServicoNaoFaturadoDTO.setVlDevido((String)item.get("vl_devido"));
			doctoServicoNaoFaturadoDTO.setFilialCobranca((String) item.get("sg_filial_cob") );
			doctoServicoNaoFaturadoDTO.setFilialResp((String) item.get("sg_filial_resp") );
			doctoServicoNaoFaturadoDTO.setDevedor((String) item.get("devedor"));
			doctoServicoNaoFaturadoDTO.setDtEntrega( (String) item.get("dtEntrega") );
			doctoServicoNaoFaturadoDTO.setTpConhecimento( (String) item.get("tpConhecimento") );
			doctoServicoNaoFaturadoDTO.setTpFrete( (String) item.get("tpFrete") );
			doctoServicoNaoFaturadoDTO.setDtEmissao( (String) item.get("dtEmissao") );
			doctoServicoNaoFaturadoDTO.setTpDocumento( (String) item.get("tpDocumento") );
			doctoServicoNaoFaturadoDTO.setChaveCTE( (String) item.get("chaveCTE") );

			doctoServicoNaoFaturadoDTO.setDoctoServico( (String) item.get("tpDocumento")+" "+(String) item.get("sg_filial")+" "+(String) item.get("nr_docto_servico")   );
			
			dispositivos.add(doctoServicoNaoFaturadoDTO);
		}
		return dispositivos;
	}
	

	@Override
	protected Integer count(DoctoServicoNaoFaturadoFilterDTO filter) {
		return 0;
	}

	@POST
	@Path("carregarValoresPadrao")
	public Response carregarValoresPadrao() {
		TypedFlatMap retorno = new TypedFlatMap();

		Filial filialLogada = SessionUtils.getFilialSessao();
		TypedFlatMap retornoFilial = new TypedFlatMap();
		retorno.put("idFilial", filialLogada.getIdFilial());
		retorno.put("sgFilial", filialLogada.getSgFilial());
		retorno.put("nmFilial", filialLogada.getPessoa().getNmFantasia());
		retorno.put("idRegional", regionalFilialService.findRegional(SessionUtils.getFilialSessao().getIdFilial()).getIdRegional());

		List<Map<String, Object>> regionais = regionalService
				.findRegionaisVigentes();
		retorno.put("regionais", regionais);

		List<Map<String, Object>> services = servicoService.findAllAtivo();
		retorno.put("servicos", services);

		return Response.ok(retorno).build();
	}
	
	@Override
	@POST
	@Path("reportCsv")
	public Response reportCsvRest(DoctoServicoNaoFaturadoFilterDTO filter) {
		if (Boolean.FALSE.equals(isValidLimit(filter, ROW_LIMIT, count(filter)))) {
			return getException();
		}

		findDataForReport(filter);
		return Response.ok().build();
	}
}
