package com.mercurio.lms.vendas.util;

import com.mercurio.lms.vendas.dto.ClienteDTO;
import org.apache.commons.lang.BooleanUtils;

import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.vendas.model.Cliente;
import org.modelmapper.ModelMapper;

public abstract class ClienteUtils {
	private static String hqlDadosIdentificacao;

	private static final int INSCRICAO_CNPJ_LENGTH = 8;

	public static boolean isParametroClienteEspecial(String tpCliente){
		return ConstantesVendas.CLIENTE_ESPECIAL.equals(tpCliente)
			|| ConstantesVendas.CLIENTE_FILIAL.equals(tpCliente);
	}
	
	public static boolean isClienteEventualOrPotencial(String tpCliente){
		return ConstantesVendas.CLIENTE_EVENTUAL.equals(tpCliente)
				|| ConstantesVendas.CLIENTE_POTENCIAL.equals(tpCliente);
	}
	
	public static boolean isClienteFOBDirigidoRodo(String modal, Boolean fob) {
		return ConstantesExpedicao.MODAL_RODOVIARIO.equals(modal) && BooleanUtils.isTrue(fob);
	}
	
	public static boolean isClienteFOBDirigidoAereo(String modal, Boolean fob) {
		return ConstantesExpedicao.MODAL_AEREO.equals(modal) && BooleanUtils.isTrue(fob);
	}
	
	public static boolean isClienteFobDirigidoRodoOrAereo(String modal, Boolean fobRodo, Boolean fobAereo) {
		return isClienteFOBDirigidoRodo(modal, fobRodo) || isClienteFOBDirigidoAereo(modal, fobAereo);
	}
	
	public static boolean hasCliente(Cliente cliente) {
		return cliente != null && LongUtils.hasValue(cliente.getIdCliente());
	}

	public static String getHQLDadosIdentificacao() {
		
		if(hqlDadosIdentificacao == null) {
			StringBuilder hql = new StringBuilder();
			
			hql.append("select new Map(cl.idCliente as idCliente,");
			hql.append(" idc.idInformacaoDoctoCliente as idInformacaoDoctoCliente,");
			hql.append(" idce.idInformacaoDoctoCliente as informacaoDoctoClienteEDI_idInformacaoDoctoCliente,");
			hql.append(" idcc.idInformacaoDoctoCliente as informacaoDoctoClienteConsolidado_idInformacaoDoctoCliente,");
			hql.append(" cl.tpAgrupamentoEDI as tpAgrupamentoEDI,");
			hql.append(" cl.tpOrdemEmissaoEDI as tpOrdemEmissaoEDI,");
			hql.append(" cl.blNumeroVolumeEDI as blNumeroVolumeEDI,");			
			hql.append(" cl.tpSituacao as tpSituacao,");
			hql.append(" cl.tpCliente as tpCliente,");
			hql.append(" cl.tpClienteSolicitado as tpClienteSolicitado,");
			hql.append(" cl.dtGeracao as dtGeracao,");
			hql.append(" cl.blUtilizaPesoEDI as blUtilizaPesoEDI,");			
			hql.append(" cl.blUtilizaFreteEDI as blUtilizaFreteEDI,");			
			hql.append(" cl.blPesagemOpcional as blPesagemOpcional,");
			hql.append(" cl.blPaleteFechado as blPaleteFechado,");
			hql.append(" cl.blEtiquetaPorVolume as blEtiquetaPorVolume,");
			hql.append(" cl.blMatriz as blMatriz,");
			hql.append(" cl.nrConta as nrConta,");
			hql.append(" cl.dtUltimoMovimento as dtUltimoMovimento,");
			hql.append(" cl.dsSite as dsSite,");
			hql.append(" cl.obCliente as obCliente,");
			hql.append(" cl.obFatura as obFatura,");
			hql.append(" cl.tpAtividadeEconomica as tpAtividadeEconomica,");
			hql.append(" cl.tpFormaCobranca as tpFormaCobranca,");
			hql.append(" cl.dtFundacaoEmpresa as dtFundacaoEmpresa,");
			hql.append(" cl.blNaoAtualizaDBI as blNaoAtualizaDBI,");

			hql.append(" cl.nrInscricaoSuframa as nrInscricaoSuframa,");
			hql.append(" cl.blPermiteCte as blPermiteCte,");
			hql.append(" cl.blSeparaFaturaModal as blSeparaFaturaModal,");
			hql.append(" cl.blObrigaSerie as blObrigaSerie,");
			
			hql.append(" cl.blAgendamento as blAgendamento,");
			hql.append(" cl.blConfAgendamento as blConfAgendamento,");
			hql.append(" cl.blRecolheICMS as blRecolheICMS,");
			
			hql.append(" cl.bldesconsiderarDificuldade as bldesconsiderarDificuldade,");
			hql.append(" cl.blEmissaoDiaNaoUtil as blEmissaoDiaNaoUtil,");
			hql.append(" cl.blEmissaoSabado as blEmissaoSabado,");
			
			hql.append(" cl.blClienteCCT as blClienteCCT,");
			hql.append(" cl.blEmissorNfe as blEmissorNfe,");
			hql.append(" cl.blNfeConjulgada as blNfeConjulgada,");

			hql.append(" cl.blPermiteEmbarqueRodoNoAereo as blPermiteEmbarqueRodoNoAereo,");
			hql.append(" cl.blSeguroDiferenciadoAereo as blSeguroDiferenciadoAereo,");
			
			hql.append(" cl.blAtualizaDestinatarioEdi as blAtualizaDestinatarioEdi,");
			hql.append(" cl.blAtualizaConsignatarioEdi as blAtualizaConsignatarioEdi,");
			hql.append(" cl.blLiberaEtiquetaEdi as blLiberaEtiquetaEdi,");
			hql.append(" cl.blLiberaEtiquetaEdiLinehaul as blLiberaEtiquetaEdiLinehaul,");
			hql.append(" cl.blClientePostoAvancado as blClientePostoAvancado,");
			hql.append(" cl.blAtualizaRazaoSocialDest as blAtualizaRazaoSocialDest,");
			hql.append(" cl.blGerarParcelaFreteValorLiquido as blGerarParcelaFreteValorLiquido,");

			hql.append(" gu.idGrupoEconomico as grupoEconomico_idGrupoEconomico,");
			hql.append(" gu.dsGrupoEconomico as grupoEconomico_dsGrupoEconomico,");
			hql.append(" gu.tpSituacao as grupoEconomico_tpSituacao,");

			hql.append(" sm.idSegmentoMercado as segmentoMercado_idSegmentoMercado,");
			hql.append(" sm.dsSegmentoMercado as segmentoMercado_dsSegmentoMercado,");
			hql.append(" sm.tpSituacao as segmentoMercado_tpSituacao,");

			hql.append(" cl.ramoAtividade.idRamoAtividade as ramoAtividade_idRamoAtividade,");
			hql.append(" cl.segmentoMercado.idSegmentoMercado as segmentoMercado_idSegmentoMercado,");
			hql.append(" cl.usuarioByIdUsuarioInclusao.idUsuario as usuarioByIdUsuarioInclusao_idUsuario,");

			hql.append(" pe.idPessoa as pessoa_idPessoa,");
			hql.append(" pe.nmPessoa as pessoa_nmPessoa,");
			hql.append(" pe.nmFantasia as pessoa_nmFantasia,");
			hql.append(" pe.nrIdentificacao as pessoa_nrIdentificacao,");
			hql.append(" pe.tpPessoa as pessoa_tpPessoa,");
			hql.append(" pe.tpIdentificacao as pessoa_tpIdentificacao,");
			hql.append(" pe.nrRg as pessoa_nrRg,");
			hql.append(" pe.dsOrgaoEmissorRg as pessoa_dsOrgaoEmissorRg,");
			hql.append(" pe.dtEmissaoRg as pessoa_dtEmissaoRg,");
			hql.append(" pe.dsEmail as pessoa_dsEmail,");
			hql.append(" pe.dhInclusao as pessoa_dhInclusao,");
			hql.append(" pe.nrInscricaoMunicipal as pessoa_nrInscricaoMunicipal,");
			hql.append(" pe.blAtualizacaoCountasse as pessoa_blAtualizacaoCountasse,");

			hql.append(" endp.idEnderecoPessoa as pessoa_enderecoPessoa_idEnderecoPessoa,");

			hql.append(" cl.blPermanente as blPermanente,");
			hql.append(" cl.blDivulgaLocalizacao as blDivulgaLocalizacao,");
			hql.append(" cl.blCobraReentrega as blCobraReentrega,");
			hql.append(" cl.nrReentregasCobranca as nrReentregasCobranca,");
			hql.append(" cl.blCobraDevolucao as blCobraDevolucao,");
			hql.append(" cl.blFobDirigido as blFobDirigido,");
			hql.append(" cl.blFobDirigidoAereo as blFobDirigidoAereo,");
			hql.append(" cl.blPesoAforadoPedagio as blPesoAforadoPedagio,");
			hql.append(" cl.blIcmsPedagio as blIcmsPedagio,");
			hql.append(" cl.vlFaturamentoPrevisto as vlFaturamentoPrevisto,");
			hql.append(" cl.tpFormaArredondamento as tpFormaArredondamento,");
			hql.append(" cl.tpEmissaoDoc as tpEmissaoDoc,");
			hql.append(" cl.tpFrequenciaVisita as tpFrequenciaVisita,");
			hql.append(" cl.blOperadorLogistico as blOperadorLogistico,");
			hql.append(" cl.nrCasasDecimaisPeso as nrCasasDecimaisPeso,");
			hql.append(" rc.idRegional as regionalComercial_idRegional,");
			hql.append(" rc.sgRegional as regionalComercial_sgRegional,");
			hql.append(" rc.dsRegional as regionalComercial_dsRegional,");
			hql.append(" fic.idFilial as filialByIdFilialAtendeComercial_idFilial,");
			hql.append(" fic.sgFilial as filialByIdFilialAtendeComercial_sgFilial,");
			hql.append(" fics.idFilial as filialByIdFilialComercialSolicitada_idFilial,");
			hql.append(" fics.sgFilial as filialByIdFilialComercialSolicitada_sgFilial,");			
			
			hql.append(" moe4.idMoeda as moedaByIdMoedaFatPrev_idMoeda,");
			hql.append(" moe4.dsSimbolo as moedaByIdMoedaFatPrev_dsSimbolo,");
			hql.append(" moe4.tpSituacao as moedaByIdMoedaFatPrev_tpSituacao,");
			hql.append(" pec.nmFantasia as filialByIdFilialAtendeComercial_pessoa_nmFantasia,");
			hql.append(" pecs.nmFantasia as filialByIdFilialComercialSolicitada_pessoa_nmFantasia,");
			
			hql.append(" cl.blColetaAutomatica as blColetaAutomatica,");
			hql.append(" cl.tpDificuldadeColeta as tpDificuldadeColeta,");
			hql.append(" cl.tpDificuldadeEntrega as tpDificuldadeEntrega,");
			hql.append(" cl.tpDificuldadeClassificacao as tpDificuldadeClassificacao,");
			hql.append(" cl.tpLocalEmissaoConReent as tpLocalEmissaoConReent,");
			hql.append(" cl.blAgrupaNotas as blAgrupaNotas,");
			hql.append(" cl.blCadastradoColeta as blCadastradoColeta,");
			hql.append(" cl.blAgendamentoPessoaFisica as blAgendamentoPessoaFisica,");
			hql.append(" cl.blAgendamentoPessoaJuridica as blAgendamentoPessoaJuridica,");
			hql.append(" cl.blObrigaRecebedor as blObrigaRecebedor,");
			hql.append(" cl.blDificuldadeEntrega as blDificuldadeEntrega,");
			hql.append(" cl.blRetencaoComprovanteEntrega as blRetencaoComprovanteEntrega,");
			hql.append(" cl.blAceitaFobGeral as blAceitaFobGeral,");
			hql.append(" cl.blClienteEstrategico as blClienteEstrategico,");

			hql.append(" ro.idRegional as regionalOperacional_idRegional,");
			hql.append(" ro.sgRegional as regionalOperacional_sgRegional,");
			hql.append(" ro.dsRegional as regionalOperacional_dsRegional,");
			hql.append(" cl.filialByIdFilialAtendeOperacional.idFilial as filialByIdFilialAtendeOperacional_idFilial,");
			hql.append(" fio.sgFilial as filialByIdFilialAtendeOperacional_sgFilial,");
			hql.append(" peo.nmFantasia as filialByIdFilialAtendeOperacional_pessoa_nmFantasia,");
			hql.append(" cl.filialByIdFilialOperacionalSolicitada.idFilial as filialByIdFilialOperacionalSolicitada_idFilial,");
			hql.append(" fios.sgFilial as filialByIdFilialOperacionalSolicitada_sgFilial,");
			hql.append(" peos.nmFantasia as filialByIdFilialOperacionalSolicitada_pessoa_nmFantasia,");

			hql.append(" cl.blGeraReciboFreteEntrega as blGeraReciboFreteEntrega,");
			hql.append(" cl.blResponsavelFrete as blResponsavelFrete,");
			hql.append(" cl.blBaseCalculo as blBaseCalculo,");
			hql.append(" cl.blIndicadorProtesto as blIndicadorProtesto,");
			hql.append(" cl.pcDescontoFreteCif as pcDescontoFreteCif,");
			hql.append(" cl.pcDescontoFreteFob as pcDescontoFreteFob,");
			hql.append(" cl.blEmiteBoletoCliDestino as blEmiteBoletoCliDestino,");
			hql.append(" cl.blAgrupaFaturamentoMes as blAgrupaFaturamentoMes,");
			hql.append(" cl.tpPeriodicidadeTransf as tpPeriodicidadeTransf,");
			hql.append(" cl.blRessarceFreteFob as blRessarceFreteFob,");
			hql.append(" cl.tpMeioEnvioBoleto as tpMeioEnvioBoleto,");
			hql.append(" cl.tpCobranca as tpCobranca,");
			hql.append(" cl.tpPesoCalculo as tpPesoCalculo,");
			hql.append(" cl.tpCobrancaSolicitado as tpCobrancaSolicitado,");
			hql.append(" cl.tpCobrancaAprovado as tpCobrancaAprovado,");
			hql.append(" cl.nrDiasLimiteDebito as nrDiasLimiteDebito,");
			hql.append(" cl.vlSaldoAtual as vlSaldoAtual,");
			hql.append(" cl.vlLimiteCredito as vlLimiteCredito,");
			hql.append(" cl.blPreFatura as blPreFatura,");
			hql.append(" cl.pcJuroDiario as pcJuroDiario,");
			hql.append(" cl.blFaturaDocsEntregues as blFaturaDocsEntregues,");
			hql.append(" cl.blCobrancaCentralizada as blCobrancaCentralizada,");
			hql.append(" cl.blFaturaDocsConferidos as blFaturaDocsConferidos,");
			hql.append(" cl.blAtualizaDestinatarioEdi as blAtualizaDestinatarioEdi,");
			hql.append(" cl.blAtualizaConsignatarioEdi as blAtualizaConsignatarioEdi,");
			hql.append(" cl.blAtualizaIEDestinatarioEdi as blAtualizaIEDestinatarioEdi,");
			hql.append(" cl.blSemChaveNfeEdi as blSemChaveNfeEdi,");
			hql.append(" cl.blObrigaPesoCubadoEdi as blObrigaPesoCubadoEdi,");
			hql.append(" cl.vlLimiteValorMercadoriaCteAereo as vlLimiteValorMercadoriaCteAereo,");
			hql.append(" cl.vlLimiteValorMercadoriaCteRodo as vlLimiteValorMercadoriaCteRodo,");
			hql.append(" cl.blFronteiraRapida as blFronteiraRapida,");
			hql.append(" cl.blFaturaDocReferencia as blFaturaDocReferencia,");
			hql.append(" cl.blVeiculoDedicado as blVeiculoDedicado,");
			hql.append(" cl.blAgendamentoEntrega as blAgendamentoEntrega,");
			hql.append(" cl.blPaletizacao as blPaletizacao,");
			hql.append(" cl.blCustoDescarga as blCustoDescarga,");
			hql.append(" cl.vlLimiteDocumentos as vlLimiteDocumentos,");
			hql.append(" cl.blEmiteDacteFaturamento as blEmiteDacteFaturamento,");
			hql.append(" cl.blObrigaBO as blObrigaBO,");
			hql.append(" cl.blRemetenteOTD as blRemetenteOTD,");
			hql.append(" cl.blEnviaDacteXmlFat as blEnviaDacteXmlFat, ");
			hql.append(" cl.blEnviaDocsFaturamentoNas as blEnviaDocsFaturamentoNas, ");
			hql.append(" cl.blGeraNovoDPE as blGeraNovoDPE, ");
			hql.append(" cl.blAssinaturaDigital as blAssinaturaDigital, ");			
            hql.append(" cl.blDpeFeriado as blDpeFeriado, ");	
            hql.append(" cl.blObrigaComprovanteEntrega as blObrigaComprovanteEntrega, ");	
            hql.append(" cl.blObrigaRg as blObrigaRg, ");	
            hql.append(" cl.blPermiteBaixaParcial as blPermiteBaixaParcial, ");	
            hql.append(" cl.blObrigaBaixaPorVolume as blObrigaBaixaPorVolume, ");	
            hql.append(" cl.blObrigaQuizBaixa as blObrigaQuizBaixa, ");	
            hql.append(" cl.blObrigaParentesco as blObrigaParentesco, ");
			hql.append(" cl.blDivisao as blDivisao, ");
			hql.append(" cl.blValidaCobrancDifTdeDest as blValidaCobrancDifTdeDest, ");
			hql.append(" cl.blCobrancaTdeDiferenciada as blCobrancaTdeDiferenciada, ");
			
			//LMS-441
			hql.append(" cl.blCalculoArqPreFatura as blCalculoArqPreFatura,");
			hql.append(" rf.idRegional as regionalFinanceiro_idRegional,");
			hql.append(" rf.sgRegional as regionalFinanceiro_sgRegional,");
			hql.append(" rf.dsRegional as regionalFinanceiro_dsRegional,");
			hql.append(" cl.filialByIdFilialCobranca.idFilial as filialByIdFilialCobranca_idFilial,");
			hql.append(" cl.filialByIdFilialCobrancaSolicitada.idFilial as filialByIdFilialCobrancaSolicitada_idFilial,");
			
			hql.append(" fif.sgFilial as filialByIdFilialCobranca_sgFilial,");
			hql.append(" fifs.sgFilial as filialByIdFilialCobrancaSolicitada_sgFilial,");
			hql.append(" ceden.id as cedente_idCedente,");
			hql.append(" cl.cliente.idCliente as cliente_idCliente,");
			hql.append(" pecl.nrIdentificacao as cliente_pessoa_nrIdentificacao,");
			hql.append(" pecl.nmPessoa as cliente_pessoa_nmPessoa,");
			hql.append(" pecl.tpIdentificacao as cliente_pessoa_tpIdentificacao,");
			hql.append(" pef.nmFantasia as filialByIdFilialCobranca_pessoa_nmFantasia,");
			hql.append(" pefs.nmFantasia as filialByIdFilialCobrancaSolicitada_pessoa_nmFantasia,");
			hql.append(" moe1.idMoeda as moedaByIdMoedaLimDoctos_idMoeda,");
			hql.append(" moe1.dsSimbolo as moedaByIdMoedaLimDoctos_dsSimbolo,");
			hql.append(" moe1.tpSituacao as moedaByIdMoedaLimDoctos_tpSituacao,");
			hql.append(" moe2.idMoeda as moedaByIdMoedaSaldoAtual_idMoeda,");
			hql.append(" moe2.dsSimbolo as moedaByIdMoedaSaldoAtual_dsSimbolo,");
			hql.append(" moe2.tpSituacao as moedaByIdMoedaSaldoAtual_tpSituacao,");
			hql.append(" moe3.idMoeda as moedaByIdMoedaLimCred_idMoeda,");
			hql.append(" moe3.dsSimbolo as moedaByIdMoedaLimCred_dsSimbolo,");
			hql.append(" moe3.tpSituacao as moedaByIdMoedaLimCred_tpSituacao,");
			hql.append(" cm.idCliente as clienteMatriz_idCliente,");
			hql.append(" cmp.nrIdentificacao as clienteMatriz_pessoa_nrIdentificacao,");
			hql.append(" cmp.nmPessoa as clienteMatriz_pessoa_nmPessoa,");
			hql.append(" cmp.tpIdentificacao as clienteMatriz_pessoa_tpIdentificacao,");
			hql.append(" oc.idObservacaoConhecimento as observacaoConhecimento_idObservacaoConhecimento,");
			hql.append(" dcr.idDivisaoCliente as divisaoClienteResponsavel_idDivisaoCliente,");
			hql.append(" cl.tpModalObrigaBO as tpModalObrigaBO,");
			hql.append(" cl.blMtzLiberaRIM as blMtzLiberaRIM,");
			hql.append(" cl.obFatura as obFatura,");
			
			hql.append(" cl.blProdutoPerigoso as blProdutoPerigoso,");
			hql.append(" cl.blControladoPoliciaCivil as blControladoPoliciaCivil,");
			hql.append(" cl.blControladoPoliciaFederal as blControladoPoliciaFederal,");
			hql.append(" cl.blControladoExercito as blControladoExercito,");
			hql.append(" cl.blNaoPermiteSubcontratacao as blNaoPermiteSubcontratacao");
			hql.append(") ");

			hql.append("from ").append(Cliente.class.getName());
			hql.append(" cl inner join cl.pessoa as pe");
			hql.append(" left outer join pe.enderecoPessoa as endp");
			hql.append(" left outer join cl.grupoEconomico as gu");
			hql.append(" left outer join cl.segmentoMercado as sm");

			hql.append(" inner join cl.filialByIdFilialAtendeComercial as fic");
			hql.append(" inner join cl.filialByIdFilialAtendeComercial.pessoa as pec");
			hql.append(" left outer join cl.filialByIdFilialComercialSolicitada as fics");
			hql.append(" left outer join cl.filialByIdFilialComercialSolicitada.pessoa as pecs");
			hql.append(" left outer join cl.moedaByIdMoedaFatPrev as moe4");

			hql.append(" inner join cl.filialByIdFilialAtendeOperacional as fio");
			hql.append(" left outer join cl.filialByIdFilialOperacionalSolicitada as fios");
			hql.append(" inner join cl.filialByIdFilialAtendeOperacional.pessoa as peo");
			hql.append(" left outer join cl.filialByIdFilialOperacionalSolicitada.pessoa as peos");
			
			hql.append(" left outer join cl.cedente as ceden");
			hql.append(" inner join cl.filialByIdFilialCobranca as fif");
			hql.append(" left outer join cl.filialByIdFilialCobrancaSolicitada as fifs");
			hql.append(" inner join cl.filialByIdFilialCobranca.pessoa as pef");
			hql.append(" left outer join cl.filialByIdFilialCobrancaSolicitada.pessoa as pefs");
			hql.append(" inner join cl.cliente.pessoa as pecl");
			hql.append(" left outer join cl.moedaByIdMoedaLimDoctos as moe1");
			hql.append(" left outer join cl.moedaByIdMoedaSaldoAtual as moe2");
			hql.append(" left outer join cl.moedaByIdMoedaLimCred as moe3");
			hql.append(" left outer join cl.clienteMatriz as cm");
			hql.append(" left outer join cm.pessoa as cmp");
			hql.append(" left outer join cl.observacaoConhecimento as oc ");
			hql.append(" left outer join cl.regionalComercial as rc ");
			hql.append(" left outer join cl.regionalOperacional as ro ");
			hql.append(" left outer join cl.regionalFinanceiro as rf ");
			hql.append(" left outer join cl.informacaoDoctoCliente as idc ");
			hql.append(" left outer join cl.informacaoDoctoClienteConsolidado as idcc ");
			
			hql.append(" left outer join cl.divisaoClienteResponsavel as dcr");
			hql.append(" left outer join cl.informacaoDoctoClienteEDI as idce ");

			hql.append(" where cl.idCliente").append(" = ?");

			hqlDadosIdentificacao = hql.toString();
		}
		return hqlDadosIdentificacao;
	}

	/**
	 * LMS-7285 - Verifica igualdade para o número de inscrição de dois CNPJ's.
	 * O número de inscrição corresponde aos 8 primeiros dígitos do CNPJ. Os
	 * caracteres não numéricos de cada CNPJ de entrada são desconsiderados na
	 * comparação. Na entrada de dois CNPJ's iguais a <tt>null</tt> a comparação
	 * é considerada <tt>true</tt>.
	 * 
	 * @param cnpj1
	 *            primeiro CNPJ para comparação
	 * @param cnpj2
	 *            segundo CNPJ para comparação
	 * @return <tt>true</tt> se os números de inscrição forem iguais,
	 *         <tt>false</tt> caso contrário
	 */
	public static boolean equalsInscricaoCNPJ(String cnpj1, String cnpj2) {
		if (cnpj1 == null || cnpj2 == null) {
			return cnpj1 == null && cnpj2 == null;
		}
		String inscricao1 = cnpj1.replaceAll("\\D", "").substring(0, Math.min(cnpj1.length(), INSCRICAO_CNPJ_LENGTH));
		String inscricao2 = cnpj2.replaceAll("\\D", "");
		return inscricao2.startsWith(inscricao1);
	}

	/**
	 * @param cliente
	 * @return clienteMatriz se o cliente reponsavel for do tipo Filial de cliente especial senão retorna o próprio cliente
	 */
	public static Cliente getClienteTpClienteFilialClienteEspecial(Cliente cliente) {
		if (ConstantesExpedicao.TP_CLIENTE_FILIAL.equals(cliente.getTpCliente().getValue())) {
			return cliente.getClienteMatriz();
		}
		return cliente;
	}
	
	public static Boolean isClienteComProdutoDiferenciado(Cliente cliente) {
    	Boolean produtoPerigoso = BooleanUtils.toBooleanDefaultIfNull(cliente.getBlProdutoPerigoso(), Boolean.FALSE);
    	Boolean controladoPoliciaCivil = BooleanUtils.toBooleanDefaultIfNull(cliente.getBlControladoPoliciaCivil(), Boolean.FALSE);
    	Boolean controladoPoliciaFederal = BooleanUtils.toBooleanDefaultIfNull(cliente.getBlControladoPoliciaFederal(), Boolean.FALSE);
    	Boolean controladoExercito = BooleanUtils.toBooleanDefaultIfNull(cliente.getBlControladoExercito(), Boolean.FALSE);
    	
    	if (BooleanUtils.isTrue(produtoPerigoso) || BooleanUtils.isTrue(controladoPoliciaCivil)
    			|| BooleanUtils.isTrue(controladoPoliciaFederal || BooleanUtils.isTrue(controladoExercito))) {
			return Boolean.TRUE;
		}
    	
		return Boolean.FALSE;
	}
	
	public static Boolean isClienteComProdutoPerigoso(Cliente cliente) {
		Boolean blProdutoPerigoso = BooleanUtils.toBooleanDefaultIfNull(cliente.getBlProdutoPerigoso(), Boolean.FALSE);
		
    	if (BooleanUtils.isTrue(blProdutoPerigoso)) {
			return Boolean.TRUE;
		}
    	
		return Boolean.FALSE;
	}

	public static ClienteDTO toDTO(Cliente cliente) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(cliente, ClienteDTO.class);
	}

	public static Cliente toEntity(ClienteDTO clienteDTO) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(clienteDTO, Cliente.class);
	}
}
