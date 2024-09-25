package com.mercurio.lms.vendas.model.dao;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.ClienteLog;

/**
 * @spring.bean 
 */
public class ClienteLogDAO extends BaseCrudDao<ClienteLog, Long> {

	protected final Class getPersistentClass() {

		return ClienteLog.class;
	}

	@Override
	public ResultSetPage findPaginated(Map criteria,FindDefinition findDef) {
		ResultSetPage rsp = super.findPaginated(criteria, findDef);
		HashMap map = new HashMap();
		map.put("cliente.pessoa", FetchMode.SELECT);
		map.put("clienteResponsavelFrete.pessoa", FetchMode.SELECT);
		this.initilizeResultSetPage(rsp, map);
		
		FilterResultSetPage frsp = new FilterResultSetPage(rsp){
			public Map filterItem(Object item) {
				ClienteLog cl = (ClienteLog)item;
				TypedFlatMap row = new TypedFlatMap();
				row.put("opLog",cl.getOpLog());
				row.put("dhLog",cl.getDhLog());
				row.put("tpOrigemLog", cl.getTpOrigemLog());
				row.put("loginLog", cl.getLoginLog());
				row.put("cliente.pessoa.nmPessoa", cl.getCliente().getPessoa().getNmPessoa());
				row.put("cliente.pessoa.nrIdentificacaoFormatado", cl.getCliente().getPessoa().getNrIdentificacaoFormatado());
				row.put("usuarioInclusao.nmUsuario", cl.getUsuarioInclusao().getNmUsuario());
				row.put("clienteResponsavelFrete.pessoa.nmPessoa", cl.getClienteResponsavelFrete().getPessoa().getNmPessoa());
				row.put("filialCobranca.sgFilial",cl.getFilialCobranca().getSgFilial());
				row.put("filialAtendeOperacional.sgFilial", cl.getFilialAtendeOperacional().getSgFilial());
				row.put("filialAtendeComercial.sgFilial", cl.getFilialAtendeComercial().getSgFilial());
				row.put("tpSituacao", cl.getTpSituacao());
				row.put("tpCliente", cl.getTpCliente());
				row.put("blGeraReciboFreteEntrega", cl.isBlGeraReciboFreteEntrega());
				row.put("blPermanente",cl.isBlPermanente());
				row.put("blResponsavelFrete", cl.isBlResponsavelFrete());
				row.put("blBaseCalculo", cl.isBlBaseCalculo());
				row.put("blCobraReentrega", cl.isBlCobraReentrega());
				row.put("blCobraDevolucao", cl.isBlCobraDevolucao());
				row.put("blColetaAutomatica", cl.isBlColetaAutomatica());
				row.put("blFobDirigido", cl.isBlFobDirigido());
				row.put("blPesoAforadoPedagio", cl.isBlPesoAforadoPedagio());
				row.put("blIcmsPedagio", cl.isBlIcmsPedagio());
				row.put("blIndicadorProtesto",cl.isBlIndicadorProtesto());
				row.put("pcDescontoFreteCif", cl.getPcDescontoFreteCif());
				row.put("pcDescontoFreteFob", cl.getPcDescontoFreteFob());
				row.put("nrCasasDecimaisPeso", cl.getNrCasasDecimaisPeso());
				row.put("blMatriz", cl.isBlMatriz());
				row.put("dtGeracao", cl.getDtGeracao());
				row.put("blObrigaRecebedor", cl.isBlObrigaRecebedor());
				row.put("moedaLimCred.dsMoeda", cl.getMoedaLimCred()!=null? cl.getMoedaLimCred().getDsMoeda():"");
				row.put("moedaLimDoctos.dsMoeda", cl.getMoedaLimDoctos()!=null?cl.getMoedaLimDoctos().getDsMoeda():"");
				row.put("moedaFatPrev.dsMoeda",cl.getMoedaFatPrev()!=null?cl.getMoedaFatPrev().getDsMoeda():"");
				row.put("moedaSaldoAtual.dsMoeda",cl.getMoedaSaldoAtual()!=null?cl.getMoedaSaldoAtual().getDsMoeda():"");
				row.put("segmentoMercado.dsSegmentoMercado", cl.getSegmentoMercado()!=null?cl.getSegmentoMercado().getDsSegmentoMercado():"");
				row.put("regionalComercial.sgRegional", cl.getRegionalComercial()!=null?cl.getRegionalComercial().getSgRegional():"");
				row.put("regionalOperacional.sgRegional", cl.getRegionalOperacional()!=null?cl.getRegionalOperacional().getSgRegional():"");
				row.put("regionalFinanceiro.sgRegional", cl.getRegionalFinanceiro()!=null?cl.getRegionalFinanceiro().getSgRegional():"");
				row.put("cedente.dsCedente",cl.getCedente()!=null?cl.getCedente().getDsCedente():"");
				row.put("banco.dsBanco", cl.getBanco()!= null?cl.getBanco().getNmBanco():"");
				row.put("ramoAtividade.dsRamoAtividade", cl.getRamoAtividade()!=null?cl.getRamoAtividade().getDsRamoAtividade():"");
				row.put("grupoEconomico.dsGrupoEconomico", cl.getGrupoEconomico()!=null?cl.getGrupoEconomico().getDsGrupoEconomico():"");
				row.put("nrConta", cl.getNrConta());
				row.put("tpDificuldadeColeta", cl.getTpDificuldadeColeta());
				row.put("tpDificuldadeEntrega", cl.getTpDificuldadeEntrega());
				row.put("tpDificuldadeClassificacao", cl.getTpDificuldadeClassificacao());
				row.put("blEmiteBoletoCliDestino", cl.isBlEmiteBoletoCliDestino());
				row.put("vlLimiteCredito", cl.getVlLimiteCredito());
				row.put("nrDiasLimiteDebito", cl.getNrDiasLimiteDebito());
				row.put("vlFaturamentoPrevisto", cl.getVlFaturamentoPrevisto());
				row.put("vlSaldoAtual", cl.getVlSaldoAtual());
				row.put("pcJuroDiario", cl.getPcJuroDiario());
				row.put("vlLimiteDocumentos", cl.getVlLimiteDocumentos());
				row.put("dtUltimoMovimento", cl.getDtUltimoMovimento());
				row.put("dtFundacaoEmpresa", cl.getDtFundacaoEmpresa());
				row.put("tpCobranca", cl.getTpCobranca());
				row.put("tpMeioEnvioBoleto", cl.getTpMeioEnvioBoleto());
				row.put("dsSite", cl.getDsSite());
				row.put("obCliente", cl.getObCliente());
				row.put("tpAtividadeEconomica", cl.getTpAtividadeEconomica());
				row.put("blAgrupaFaturamentoMes", cl.isBlAgrupaFaturamentoMes());
				row.put("tpFormaArredondamento", cl.getTpFormaArredondamento());
				row.put("tpLocalEmissaoConReent", cl.getTpLocalEmissaoConReent());
				row.put("blAgrupaNotas", cl.isBlAgrupaNotas());
				row.put("blCadastradoColeta",cl.isBlCadastradoColeta());
				row.put("tpPeriodicidadeTransf", cl.getTpPeriodicidadeTransf());
				row.put("blRessarceFreteFob", cl.isBlRessarceFreteFob());
				row.put("blPreFatura", cl.isBlPreFatura());
				row.put("blFaturaDocsEntregues", cl.isBlFaturaDocsEntregues());
				row.put("blCobrancaCentralizada", cl.isBlCobrancaCentralizada());
				row.put("blFaturaDocsConferidos", cl.isBlFaturaDocsConferidos());
				row.put("blAgendamentoPessoaFisica", cl.isBlAgendamentoPessoaFisica());
				row.put("blAgendamentoPessoaJuridica", cl.isBlAgendamentoPessoaJuridica());
				row.put("blFronteiraRapida", cl.isBlFronteiraRapida());
				row.put("blOperadorLogistico", cl.isBlOperadorLogistico());
				row.put("tpFrequenciaVisita", cl.getTpFrequenciaVisita());
				row.put("observacaoConhecimento.dsObservacaoConhecimento", cl.getObservacaoConhecimento()!=null?cl.getObservacaoConhecimento().getDsObservacaoConhecimento():"");
				row.put("blFaturaDocReferencia", cl.isBlFaturaDocReferencia());
				row.put("clienteMatriz.pessoa.nmPessoa", cl.getClienteMatriz()!=null?cl.getClienteMatriz().getPessoa().getNmPessoa():"");
				row.put("blDificuldadeEntrega", cl.isBlDificuldadeEntrega());
				row.put("blRetencaoComprovanteEnt", cl.isBlRetencaoComprovanteEnt());
				row.put("blDivulgaLocalizacao",cl.isBlDivulgaLocalizacao());
				return row;
			}
		};
		
		return (ResultSetPage)frsp.doFilter();
	}	
	
	
}
