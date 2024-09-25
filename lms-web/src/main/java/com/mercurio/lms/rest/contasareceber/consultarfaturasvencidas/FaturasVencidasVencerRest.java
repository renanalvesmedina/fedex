package com.mercurio.lms.rest.contasareceber.consultarfaturasvencidas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.RegionalFilialService;
import com.mercurio.lms.municipios.model.service.RegionalService;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.contasareceber.consultarfaturasvencidas.dto.FaturasVencidasVencerDTO;
import com.mercurio.lms.rest.contasareceber.consultarfaturasvencidas.dto.FaturasVencidasVencerFilterDTO;
import com.mercurio.lms.util.session.SessionUtils;
 
@Path("/contasareceber/faturasVencidasVencer") 
public class FaturasVencidasVencerRest extends LmsBaseCrudReportRest<FaturasVencidasVencerDTO, FaturasVencidasVencerDTO, FaturasVencidasVencerFilterDTO> { 
 
	@InjectInJersey
	private ServicoService servicoService;
	
	@InjectInJersey
	private RegionalFilialService regionalFilialService;

	@InjectInJersey
	private RegionalService regionalService;
	
	@InjectInJersey
	private FaturaService faturaService;
	
	@Override
	protected Map<String,String> getColumn(String label, String column) {
		Map<String,String> retorno = new HashMap<String, String>();
		retorno.put("title", label);
		retorno.put("column", column);
		return retorno;
	}
	
	
	@POST
	@Path("mostraSQL")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@SuppressWarnings("unchecked")
	public Response mostraSQL(FormDataMultiPart formDataMultiPart)throws IOException {
		FaturasVencidasVencerFilterDTO bean = getModelFromForm(formDataMultiPart,FaturasVencidasVencerFilterDTO.class, "data");
		String sql = faturaService.findFaturasVencidasEAVencerFullSQL(convertFilter(bean));
		Map<String,Object> sqlMap = new HashMap<String, Object>();
		sqlMap.put("sql", sql);
		sqlMap.put("data.sql", sql);
		return Response.ok(sqlMap).build();
	}
	
	@Override 
	protected List<Map<String, String>> getColumns() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		list.add(getColumn("Regional","Regional"));
		list.add(getColumn("Descricao reg.","Descricoreg"));
		list.add(getColumn("Filial","Filial"));
		list.add(getColumn("Numero","Numero"));
		list.add(getColumn("CNPJ","CNPJ"));
		list.add(getColumn("Razao social","Razaosocial"));
		list.add(getColumn("Filial cob. cliente","Filialcobcliente"));
		list.add(getColumn("Tipo pessoa","Tipopessoa"));
		list.add(getColumn("Tipo cliente","Tipocliente"));
		list.add(getColumn("Tipo cobranca","Tipocobranca"));
		list.add(getColumn("Cliente com pre-fatura","Clientecomprefatura"));
		list.add(getColumn("Cobranca centralizada","Cobrancacentralizada"));
		list.add(getColumn("Classificacao cliente","Classificacaocliente"));
		
		list.add(getColumn("Tipo de envio faturamento","Tipodeenviofaturamento"));
		list.add(getColumn("Tipo de envio carta cobranca","Tipodeenviocartacobranca"));
		list.add(getColumn("Tipo de envio Serasa","TipodeenvioSerasa"));
		list.add(getColumn("Tipo de envio cobranca terceira","Tipodeenviocobrancaterceira"));
		
		list.add(getColumn("E-mail faturamento","Emailfaturamento"));
		list.add(getColumn("E-mail cobranca","Emailcobranca"));
		list.add(getColumn("Cedente","Cedente"));
		list.add(getColumn("Modal","Modal"));
		list.add(getColumn("Servico","Servico"));
		list.add(getColumn("Divisao","Divisao"));
		list.add(getColumn("Situacao fatura","Situacaofatura"));
		list.add(getColumn("Situacao aprovacao","Situacaoaprovacao"));
		list.add(getColumn("Tipo documento de servico","Tipodocumentodeservico"));
		list.add(getColumn("Tipo conhecimento","Tipoconhecimento"));
		list.add(getColumn("Tipo frete conhecimento","Tipofreteconhecimento"));
		list.add(getColumn("Tipo calculo","Tipocalculo"));
		list.add(getColumn("Servico adicional","Servicoadicional"));
		list.add(getColumn("AWB","AWB"));
		list.add(getColumn("Fatura impressa","Faturaimpressa"));
		list.add(getColumn("Origem fatura","Origemfatura"));
		list.add(getColumn("Data emissao","Dataemissao"));
		list.add(getColumn("Data vencimento","Datavencimento"));
		list.add(getColumn("Data liquidacao","Dataliquidacao"));
		
		list.add(getColumn("Data neg. Serasa","DatanegSerasa"));
		list.add(getColumn("Data exclusco Serasa","DataexclusaoSerasa"));
		
		list.add(getColumn("Data envio Cob Terceira","DataenvioCobTerceira"));
		list.add(getColumn("Data pagto Cob Terceira","DatapagtoCobTerceira"));
		list.add(getColumn("Data devol Cob Terceira","DatadevolCobTerceira"));
		list.add(getColumn("Data envio mensagem fatura","Dataenviomensagem"));
		list.add(getColumn("Data recebimento mensagem fatura","Datarecebimentomensagem"));
		list.add(getColumn("Data devolucao mensagem fatura","Datadevolucaomensagem"));
		list.add(getColumn("Data erro mensagem fatura","Dataerromensagem"));
		
		
		
		list.add(getColumn("Data envio mensagem cobranca","Dataenviomensagemcobranca"));
		list.add(getColumn("Data recebimento mensagem cobranca","Dtrecmenscobranca"));
		list.add(getColumn("Data devolucao mensagem cobranca","Datadevolucaomensagemcobranca"));
		list.add(getColumn("Data erro mensagem cobranca","Dataerromensagemcobranca"));
		list.add(getColumn("Data envio mensagem cob. terceira","Dataenviomensagemcobterceira"));
		list.add(getColumn("Data recebimento mensagem cob. terceira","Dtrecmenscobterceira"));
		list.add(getColumn("Data devolucao mensagem cob. terceira","Dtdevmenscobterceira"));
		list.add(getColumn("Data erro mensagem cob. terceira","Dataerromensagemcobterceira"));

		list.add(getColumn("Data da pre-fatura","Dataprefatura"));
		list.add(getColumn("Data da importacao","Dataimportacao"));
		list.add(getColumn("Data do envio do aceite","Dataenvioaceite"));
		list.add(getColumn("Data retorno do aceite","Dataretornoaceite"));
		
		
		list.add(getColumn("Qtde documentos","Qtdedocumentos"));
		list.add(getColumn("Total fatura","Totalfatura"));
		list.add(getColumn("Total descontos","Totaldescontos"));
		list.add(getColumn("Total juros","Totaljuros"));
		list.add(getColumn("Valor recebido parcial","Valorrecebidoparcial"));
		list.add(getColumn("Valor saldo","Valorsaldo"));
		list.add(getColumn("Ultimo receb. parcial","Ultimorecebparcial"));
		list.add(getColumn("Filial debitada","Filialdebitada"));
		list.add(getColumn("Setor causador do abatimento","Setorcausadordoabatimento"));
		list.add(getColumn("Motivo do desconto","Motivododesconto"));
		list.add(getColumn("Acao corretiva","Acaocorretiva"));
		list.add(getColumn("Observacoes","Observacoes"));
		list.add(getColumn("Data Tratativa","Datatratativa"));
		list.add(getColumn("Usuario Tratativa","Usuariotratativa"));
		list.add(getColumn("Num. boleto","Numboleto"));
		list.add(getColumn("Situacao boleto","Situacaoboleto"));
		list.add(getColumn("Data emissao boleto","Dataemissaoboleto"));
		list.add(getColumn("Data vcto boleto","Datavctoboleto"));
		list.add(getColumn("Data vcto aprovacao","Datavctoaprovacao"));
		list.add(getColumn("Valor total boleto","Valortotalboleto"));
		list.add(getColumn("Valor desconto boleto","Valordescontoboleto"));
		list.add(getColumn("Valor juro diario","Valorjurodiario"));
		list.add(getColumn("Filial redeco","Filialredeco"));
		list.add(getColumn("Numero redeco","Numeroredeco"));
		list.add(getColumn("Situacao redeco","Situacaoredeco"));
		list.add(getColumn("Finalidade redeco","Finalidaderedeco"));
		list.add(getColumn("Tipo recebimento","Tiporecebimento"));
		list.add(getColumn("Data emissao redeco","Dataemissaoredeco"));
		list.add(getColumn("Data recebimento redeco","Datarecebimentoredeco"));
		list.add(getColumn("Cobrador","Cobrador"));
		return list; 
	} 
 
	@Override 
	protected List<Map<String, Object>> findDataForReport(FaturasVencidasVencerFilterDTO filter) {
		return faturaService.findFaturasVencidasEAVencerFull(convertFilter(filter)); 
	} 
 
	@Override 
	protected FaturasVencidasVencerDTO findById(Long id) {  
		return null; 
	} 
 
	@Override 
	protected Long store(FaturasVencidasVencerDTO bean) { 
		return null; 
	} 
 
	@Override 
	protected void removeById(Long id) { 
	} 
 
	@Override 
	protected void removeByIds(List<Long> ids) { 
	} 
 
	private TypedFlatMap convertFilter(FaturasVencidasVencerFilterDTO filter){
		TypedFlatMap t = new TypedFlatMap();
		if ( filter.getFilialResponsavel() != null )
			t.put("filialResponsavel", filter.getFilialResponsavel().getIdFilial());
		if ( filter.getTipoCliente() != null )
			t.put("tipoCliente", filter.getTipoCliente().getValue());
		if ( filter.getClassificacaoCliente() != null )
			t.put("classificacaoCliente", filter.getClassificacaoCliente().getValue());
		if ( filter.getTipoCobranca() != null )
			t.put("tipoCobranca",filter.getTipoCobranca().getValue());
		if ( filter.getCobrancaCentralizada() != null )
			t.put("cobrancaCentralizada",filter.getCobrancaCentralizada().getValue());
		if ( filter.getClienteComPreFatura() != null )
			t.put("clienteComPreFatura",filter.getClienteComPreFatura().getValue());
		if ( filter.getClienteEmailFaturamento() != null )
			t.put("clienteEmailFaturamento",filter.getClienteEmailFaturamento().getValue());
		if ( filter.getClienteEmailCobranca() != null )
			t.put("clienteEmailCobranca",filter.getClienteEmailCobranca().getValue());
		if ( filter.getDevedoresExcluir() != null )
			t.put("devedoresExcluir", filter.getDevedoresExcluir());
		if ( filter.getDevedoresListar() != null )
			t.put("devedoresListar", filter.getDevedoresListar());
		if ( filter.getIdServico() != null)
			t.put("idServico",filter.getIdServico());
		if ( filter.getDtVigenciaInicial() != null)
			t.put("dtVigenciaInicial",filter.getDtVigenciaInicial());
		if ( filter.getDtVigenciaFinal() != null)
			t.put("dtVigenciaFinal",filter.getDtVigenciaFinal());
		if ( filter.getTipoDocumento() != null )
			t.put("tipoDocumento",filter.getTipoDocumento().getValue());
		if ( filter.getTipoCalculo() != null)
			t.put("tipoCalculo",filter.getTipoCalculo().getValue());
		if ( filter.getTipoFrete() != null){
			t.put("tipoFrete",filter.getTipoFrete().getValue());
		}
		if ( filter.getTipoConhecimento() != null){
			t.put("tipoConhecimento",filter.getTipoConhecimento().getValue());
		}
		if ( filter.getIdRegional() != null){
			t.put("idRegional",filter.getIdRegional());
		}
		if ( filter.getIdFilialCobranca() != null){
			t.put("idFilialCobranca", filter.getIdFilialCobranca().getIdFilial());
		}
		if ( filter.getDtEmissaoFatInicial() != null){
			t.put("dtEmissaoFatInicial",filter.getDtEmissaoFatInicial());
		}
		if ( filter.getDtEmissaoFatFinal() != null){
			t.put("dtEmissaoFatFinal",filter.getDtEmissaoFatFinal());
		}
		if ( filter.getDtVencimentoInicial() != null){
			t.put("dtVencimentoInicial", filter.getDtVencimentoInicial());
		}
		if ( filter.getDtVencimentoFinal() != null){
			t.put("dtVencimentoFinal",filter.getDtVencimentoFinal());
		}
		if ( filter.getDtNegSerasaInicial() != null){
			t.put("dtNegSerasaInicial",filter.getDtNegSerasaInicial());
		}
		if ( filter.getDtNegSerasaFinal() != null){
			t.put("dtNegSerasaFinal",filter.getDtNegSerasaFinal());
		}
		if ( filter.getDtExecSerasaInicial() != null){
			t.put("dtExecSerasaInicial",filter.getDtExecSerasaInicial());
		}
		if ( filter.getDtExecSerasaFinal() != null){
			t.put("dtExecSerasaFinal",filter.getDtExecSerasaFinal());
		}
		
		if ( filter.getDtEnvioCobTerceiraInicial() != null){
			t.put("dtEnvioCobTerceiraInicial",filter.getDtEnvioCobTerceiraInicial());
		}
		if ( filter.getDtEnvioCobTerceiraFinal() != null){
			t.put("dtEnvioCobTerceiraFinal",filter.getDtEnvioCobTerceiraFinal());
		}
		
		if ( filter.getDtPagtoCobTerceiraInicial() != null){
			t.put("dtPagtoCobTerceiraInicial",filter.getDtPagtoCobTerceiraInicial());
		}
		if ( filter.getDtPagtoCobTerceiraFinal() != null){
			t.put("dtPagtoCobTerceiraFinal",filter.getDtPagtoCobTerceiraFinal());
		}
		
		if ( filter.getDtDevolCobTerceiraInicial() != null){
			t.put("dtDevolCobTerceiraInicial",filter.getDtDevolCobTerceiraInicial());
		}
		if ( filter.getDtDevolCobTerceiraFinal() != null){
			t.put("dtDevolCobTerceiraFinal",filter.getDtDevolCobTerceiraFinal());
		}
		
		if ( filter.getDtEnvioInicial() != null){
			t.put("dtEnvioInicial",filter.getDtEnvioInicial());
		}
		if ( filter.getDtEnvioFinal() != null){
			t.put("dtEnvioFinal",filter.getDtEnvioFinal());
		}
		if ( filter.getDtRecebimentoInicial() != null){
			t.put("dtRecebimentoInicial",filter.getDtRecebimentoInicial());
		}
		if ( filter.getDtRecebimentoFinal() != null){
			t.put("dtRecebimentoFinal",filter.getDtRecebimentoFinal());
		}
		if ( filter.getDtDevolucaoInicial() != null){
			t.put("dtDevolucaoInicial",filter.getDtDevolucaoInicial());
		}
		if ( filter.getDtDevolucaoFinal() != null){
			t.put("dtDevolucaoFinal",filter.getDtDevolucaoFinal());
		}
		if ( filter.getDtErroInicial() != null){
			t.put("dtErroInicial",filter.getDtErroInicial());
		}
		if ( filter.getDtErroFinal() != null){
			t.put("dtErroFinal",filter.getDtErroFinal());
		}
		if ( filter.getDtEmissaoBolInicial() != null){
			t.put("dtEmissaoBolInicial", filter.getDtEmissaoBolInicial());
		}
		if ( filter.getDtEmissaoBolFinal() != null){
			t.put("dtEmissaoBolFinal", filter.getDtEmissaoBolFinal());
		}
		if ( filter.getNrFaturaInicial() != null){
			t.put("nrFaturaInicial",filter.getNrFaturaInicial());
		}
		if ( filter.getNrFaturaFinal() != null){
			t.put("nrFaturaFinal",filter.getNrFaturaFinal());
		}
		if ( filter.getPreFatura() != null ){
			t.put("preFatura",filter.getPreFatura());
		}
		if ( filter.getSituacaoFatura() != null){
			t.put("situacaoFatura",filter.getSituacaoFatura().getValue());
		}
		if ( filter.getSituacaoBoleto() != null){
			t.put("situacaoBoleto",filter.getSituacaoBoleto().getValue());
		}
		if ( filter.getModal() != null){
			t.put("modal",filter.getModal().getValue());
		}
		if ( filter.getAbrangencia() != null ){
			t.put("abrangencia",filter.getAbrangencia().getValue());
		}
		if ( filter.getSituacaoAprovacao() != null ){
			t.put("situacaoAprovacao",filter.getSituacaoAprovacao().getValue());
		}
		if ( filter.getComTratativa() != null ){
			t.put("comTratativa",filter.getComTratativa().getValue());
		}
		if ( filter.getDtLiquidacaoInicial() != null){
			t.put("dtLiquidacaoInicial",filter.getDtLiquidacaoInicial());
		}
		if ( filter.getDtLiquidacaoFinal() != null){
			t.put("dtLiquidacaoFinal",filter.getDtLiquidacaoFinal());
		}
		return t;
	}
	
	String retrieveDetailByKey(Map<String, Object> row,String key){
		if ( row.get(key) == null ) {
			return "";
		}
		return row.get(key).toString();
	}
	
	@Override 
	protected List<FaturasVencidasVencerDTO> find(FaturasVencidasVencerFilterDTO filter) {
		List<Map<String, Object>> d = faturaService.findFaturasVencidasEAVencer(convertFilter(filter));
		List<FaturasVencidasVencerDTO> relatorio = new ArrayList<FaturasVencidasVencerDTO>();
		for(Map<String, Object> row : d){
			FaturasVencidasVencerDTO item = new FaturasVencidasVencerDTO();
			item.setFatura(retrieveDetailByKey(row,"Filial")+" "+retrieveDetailByKey(row,"Numero"));
			item.setClienteDevedor(retrieveDetailByKey(row,"CNPJ")+" "+retrieveDetailByKey(row,"Razaosocial"));
			item.setEmissao(retrieveDetailByKey(row,"Dataemissao"));
			item.setVencimento(retrieveDetailByKey(row,"Datavencimento"));
			item.setSituacao(retrieveDetailByKey(row,"Situacaofatura"));
			item.setValorTotal(retrieveDetailByKey(row,"Totalfatura"));
			item.setValorDesconto(retrieveDetailByKey(row,"Totaldescontos"));
			item.setNumeroBoleto(retrieveDetailByKey(row,"Numboleto"));
			relatorio.add(item);
		}
		return relatorio; 
	} 
 
	@Override 
	protected Integer count(FaturasVencidasVencerFilterDTO filter) { 
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

		List<Map<String, Object>> regionais = regionalService.findRegionaisVigentes();
		retorno.put("regionais", regionais);

		List<Map<String, Object>> services = servicoService.findAllAtivo();
		retorno.put("servicos", services);

		return Response.ok(retorno).build();
	}
 
} 
