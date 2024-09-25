package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.expedicao.nfeconjugada.canc.NFECanc;
import br.com.tntbrasil.integracao.domains.expedicao.nfeconjugada.defs.NFEConjugadaType;
import br.com.tntbrasil.integracao.domains.expedicao.nfeconjugada.envio.NFEDadosRPS;
import br.com.tntbrasil.integracao.domains.expedicao.nfeconjugada.envio.NFEEndereco;
import br.com.tntbrasil.integracao.domains.expedicao.nfeconjugada.envio.NFEEnvio;
import br.com.tntbrasil.integracao.domains.expedicao.nfeconjugada.envio.NFEPessoa;
import br.com.tntbrasil.integracao.domains.expedicao.nfeconjugada.inut.NFEInut;

public class NFEConjugadaServiceHelper {
	
	/**
	 * Converte para um DTO de integracao os dados do banco de dados.
	 * 
	 * @param listDados
	 * @return NFEConjugada
	 */
	public static NFEEnvio getEnvio(List<Map<String, Object>> listDados){
		Map<String, Object> dados = listDados.get(0);
						
		NFEEnvio nfeEnvio = getDadosEnvio(dados);
		nfeEnvio.setEmitente(getDadosEmitente(dados));
		nfeEnvio.setDestinatario(getDadosDestinatario(dados));
		nfeEnvio.setDadosRps(getDadosRPS(dados));
				
		return nfeEnvio;		
	}

	public static NFECanc getCancelar(List<Map<String, Object>> listDados) {
		if(listDados == null || listDados.isEmpty()){			
			return null;
		}
		
		return getDadosCancelar(listDados.get(0));
	}
	
	public static NFEInut getInutilizar(List<Map<String, Object>> listDados) {
		if(listDados == null || listDados.isEmpty()){			
			return null;
		}
		
		return getDadosInutilizar(listDados.get(0));
	}		
		
	private static NFEEnvio getDadosEnvio(Map<String, Object> dados){
		NFEEnvio nfeEnvio = new NFEEnvio();
		
		nfeEnvio.setTpDocumento(NFEConjugadaType.getType(MapUtils.getString(dados, "tpDocumento")));
		nfeEnvio.setIdEmitenteUfNfe(MapUtils.getString(dados, "idEmitenteUfNfe"));
		nfeEnvio.setIdChaveAcessoNf(MapUtils.getString(dados, "idChaveAcessoNf"));
		nfeEnvio.setNaturezaOperacao(MapUtils.getString(dados, "naturezaOperacao"));
		nfeEnvio.setTpFormaPagamento(MapUtils.getString(dados, "tpFormaPagamento"));
		nfeEnvio.setTpModeloDocumentoFiscal(MapUtils.getInteger(dados, "tpModeloDocumentoFiscal"));
		nfeEnvio.setSerieDocumentoFiscal(MapUtils.getString(dados, "serieDocumentoFiscal"));
		nfeEnvio.setDhEmissao((DateTime) MapUtils.getObject(dados, "dhEmissao"));
		nfeEnvio.setTipoOperacao(MapUtils.getInteger(dados, "tipoOperacao"));
		nfeEnvio.setIdLocalDestinoOperacao(MapUtils.getInteger(dados, "idLocalDestinoOperacao"));
		nfeEnvio.setTpFormatoImpressaoDanfe(MapUtils.getInteger(dados, "tpFormatoImpressaoDanfe"));
		nfeEnvio.setTpFormaEmissaoNfe(MapUtils.getInteger(dados, "tpFormaEmissaoNfe"));
		nfeEnvio.setDvChaveAcessoNfe(MapUtils.getInteger(dados, "dvChaveAcessoNfe"));
		nfeEnvio.setTpAmbiente(MapUtils.getString(dados, "tpAmbiente"));
		nfeEnvio.setFinalidadeNfe(MapUtils.getInteger(dados, "finalidadeNfe"));
		nfeEnvio.setTpIndicadorOperacao(MapUtils.getInteger(dados, "tpIndicadorOperacao"));
		nfeEnvio.setTpIndicadorPresencaComprador(MapUtils.getInteger(dados, "tpIndicadorPresencaComprador"));
		nfeEnvio.setTpProcessoEmissaoNfe(MapUtils.getInteger(dados, "tpProcessoEmissaoNfe"));	
		nfeEnvio.setIdProdutoServico(MapUtils.getString(dados, "idProdutoServico"));
		nfeEnvio.setDescProdutoServico(MapUtils.getString(dados, "descProdutoServico"));
		nfeEnvio.setUnComercial(MapUtils.getString(dados, "unComercial"));
		nfeEnvio.setQtComercial(MapUtils.getInteger(dados, "qtComercial"));
		nfeEnvio.setVlUnitarioComercializacao((BigDecimal) MapUtils.getObject(dados, "vlUnitarioComercializacao"));
		nfeEnvio.setVlTotalBrutoProdutoServico((BigDecimal)MapUtils.getObject(dados, "vlTotalBrutoProdutoServico"));
		nfeEnvio.setUnTributavel(MapUtils.getString(dados, "unTributavel"));
		nfeEnvio.setQtTributavel(MapUtils.getInteger(dados, "qtTributavel"));
		nfeEnvio.setVlUnitarioTributacao((BigDecimal)MapUtils.getObject(dados, "vlUnitarioTributacao"));
		nfeEnvio.setTpTotalNfe(MapUtils.getInteger(dados, "tpTotalNfe"));
		nfeEnvio.setVlBaseCalculoISSQN((BigDecimal)MapUtils.getObject(dados, "vlBaseCalculoISSQN"));
		nfeEnvio.setVlAliquotaISSQN((BigDecimal)MapUtils.getObject(dados, "vlAliquotaISSQN"));
		nfeEnvio.setVlISSQN((BigDecimal)MapUtils.getObject(dados, "vlISSQN"));
		nfeEnvio.setIdListaServicos((BigDecimal) MapUtils.getObject(dados, "idListaServicos"));
		nfeEnvio.setIdTributacaoISSQN(MapUtils.getString(dados, "idTributacaoISSQN"));
		nfeEnvio.setVlTotalServicos((BigDecimal) MapUtils.getObject(dados, "vlTotalServicos"));
		nfeEnvio.setVlBaseCalculoISS((BigDecimal)MapUtils.getObject(dados, "vlBaseCalculoISS"));
		nfeEnvio.setVlTotalISS((BigDecimal)MapUtils.getObject(dados, "vlTotalISS"));
		nfeEnvio.setVlPisServicos((BigDecimal) MapUtils.getObject(dados, "vlPisServicos"));
		nfeEnvio.setVlCofinsServicos((BigDecimal) MapUtils.getObject(dados, "vlCofinsServicos"));
		nfeEnvio.setDhPrestacaoServico((YearMonthDay) MapUtils.getObject(dados, "dhPrestacaoServico"));
		nfeEnvio.setVlTotalRetencaooISS((BigDecimal) MapUtils.getObject(dados, "vlTotalRetencaooISS"));
		nfeEnvio.setIdRegimeEspecialTributacao(MapUtils.getInteger(dados, "idRegimeEspecialTributacao"));
		nfeEnvio.setTpModalidadeFrete(MapUtils.getInteger(dados, "tpModalidadeFrete"));
		nfeEnvio.setNrDocumentoFiscal(MapUtils.getString(dados, "nrDocumentoFiscal"));
		nfeEnvio.setIdMonitoramento(MapUtils.getLong(dados, "idMonitoramento"));
		nfeEnvio.setNrCfop(MapUtils.getShort(dados, "nrCfop"));
		nfeEnvio.setObservacao(getObservacao(MapUtils.getString(dados, "txtObsAutomatica"), MapUtils.getString(dados, "observacao")));
		
		return nfeEnvio;
	}
		
	private static String getObservacao(String txtObsAutomatica, String observacao){
		StringBuilder obs = new StringBuilder();
		obs.append(txtObsAutomatica);
		
		if(observacao != null && !observacao.isEmpty()){
			obs.append("\n");
			obs.append(observacao);
		}
		
		return obs.toString();
	}
	
	private static void defineCpfOrCnpj(NFEPessoa nfePessoa, String tpPessoa, String cpfCnpj){
		if("F".equals(tpPessoa)){
			nfePessoa.setCpf(cpfCnpj);
		} else {
			nfePessoa.setCnpj(cpfCnpj);
		}
	}
	
	private static NFEPessoa getDadosEmitente(Map<String, Object> dados){
		NFEPessoa nfePessoa = new NFEPessoa();		
		nfePessoa.setNmPessoa(MapUtils.getString(dados, "nmPessoaEmitente"));
		nfePessoa.setNmFantasia(MapUtils.getString(dados, "nmFantasiaEmitente"));
		nfePessoa.setIe(MapUtils.getString(dados, "ieEmitente"));
		nfePessoa.setIndicadorIe(MapUtils.getString(dados, "indicadorIeEmitente"));
		nfePessoa.setIm(MapUtils.getString(dados, "imEmitente"));
		nfePessoa.setCnaeFiscal(MapUtils.getString(dados, "cnaeFiscalEmitente"));
		nfePessoa.setIdRegimeTributario(MapUtils.getString(dados, "idRegimeTributarioEmitente"));
		nfePessoa.setInscricaoSuframa(MapUtils.getString(dados, "inscricaoSuframaEmitente"));
		nfePessoa.setImTomador(MapUtils.getString(dados, "imEmitente"));
		nfePessoa.setEmail(MapUtils.getString(dados, "emailEmitente"));	
		
		defineCpfOrCnpj(nfePessoa, MapUtils.getString(dados, "tpPessoaEmitente"), MapUtils.getString(dados, "cpfCnpjEmitente"));				
		
		NFEEndereco nfeEndereco = new NFEEndereco();
		nfeEndereco.setLogradouro(MapUtils.getString(dados, "logradouroEmitente"));
		nfeEndereco.setNumero(MapUtils.getString(dados, "numeroEmitente"));
		nfeEndereco.setComplemento(MapUtils.getString(dados, "complementoEmitente"));
		nfeEndereco.setBairro(MapUtils.getString(dados, "bairroEmitente"));
		nfeEndereco.setIdMunicipio(MapUtils.getString(dados, "idMunicipioEmitente"));
		nfeEndereco.setNmMunicipio(MapUtils.getString(dados, "nmMunicipioEmitente"));
		nfeEndereco.setSiglaUf(MapUtils.getString(dados, "siglaUfEmitente"));
		nfeEndereco.setIdCep(MapUtils.getString(dados, "idCepEmitente"));
		nfeEndereco.setIdPais(MapUtils.getString(dados, "idPaisEmitente"));
		nfeEndereco.setNmPais(MapUtils.getString(dados, "nmPaisEmitente"));
		nfeEndereco.setTelefone(MapUtils.getString(dados, "telefoneEmitente"));
		
		nfePessoa.setEndereco(nfeEndereco);
		
		return nfePessoa;
	}
	
	private static NFEPessoa getDadosDestinatario(Map<String, Object> dados){
		NFEPessoa nfePessoa = new NFEPessoa();
		nfePessoa.setNmPessoa(MapUtils.getString(dados, "nmPessoaDestinatario"));
		nfePessoa.setNmFantasia(MapUtils.getString(dados, "nmFantasiaDestinatario"));
		nfePessoa.setIe(MapUtils.getString(dados, "ieDestinatario"));
		nfePessoa.setIndicadorIe(MapUtils.getString(dados, "indicadorIeDestinatario"));
		nfePessoa.setCnaeFiscal(MapUtils.getString(dados, "cnaeFiscalDestinatario"));
		nfePessoa.setIdRegimeTributario(MapUtils.getString(dados, "idRegimeTributarioDestinatario"));
		nfePessoa.setInscricaoSuframa(MapUtils.getString(dados, "inscricaoSuframaDestinatario"));
		nfePessoa.setImTomador(MapUtils.getString(dados, "imTomador"));
		nfePessoa.setEmail(MapUtils.getString(dados, "emailDestinatario"));	
		
		defineCpfOrCnpj(nfePessoa, MapUtils.getString(dados, "tpPessoaDestinatario"), MapUtils.getString(dados, "cpfCnpjDestinatario"));		
		
		NFEEndereco nfeEndereco = new NFEEndereco();		
		nfePessoa.setEndereco(nfeEndereco);
		nfeEndereco.setLogradouro(MapUtils.getString(dados, "logradouroDestinatario"));
		nfeEndereco.setNumero(MapUtils.getString(dados, "numeroDestinatario"));
		nfeEndereco.setComplemento(MapUtils.getString(dados, "complementoDestinatario"));
		nfeEndereco.setBairro(MapUtils.getString(dados, "bairroDestinatario"));
		nfeEndereco.setIdMunicipio(MapUtils.getString(dados, "idMunicipioDestinatario"));
		nfeEndereco.setNmMunicipio(MapUtils.getString(dados, "nmMunicipioDestinatario"));
		nfeEndereco.setSiglaUf(MapUtils.getString(dados, "siglaUfDestinatario"));
		nfeEndereco.setIdCep(MapUtils.getString(dados, "idCepDestinatario"));
		nfeEndereco.setIdPais(MapUtils.getString(dados, "idPaisDestinatario"));
		nfeEndereco.setNmPais(MapUtils.getString(dados, "nmPaisDestinatario"));
		nfeEndereco.setTelefone(MapUtils.getString(dados, "telefoneDestinatario"));

		return nfePessoa;
	}
	
	private static NFEDadosRPS getDadosRPS(Map<String, Object> dados){
		NFEDadosRPS nfeDadosRPS = new NFEDadosRPS();
		nfeDadosRPS.setNrRotaEntrega(MapUtils.getString(dados, "rpsNrRotaEntrega"));
		nfeDadosRPS.setOtd((YearMonthDay) MapUtils.getObject(dados, "rpsOtd"));
		nfeDadosRPS.setNotasFiscaisOriginarias(MapUtils.getString(dados, "rpsNotasFiscaisOriginarias"));
		nfeDadosRPS.setLocalEntrega(MapUtils.getString(dados, "rpsLocalEntrega"));
		nfeDadosRPS.setPesoDeclarado((BigDecimal) MapUtils.getObject(dados, "rpsPesoDeclarado"));
		nfeDadosRPS.setPesoAferido((BigDecimal) MapUtils.getObject(dados, "rpsPesoAferido"));
		nfeDadosRPS.setPesoCubado((BigDecimal) MapUtils.getObject(dados, "rpsPesoCubado"));
		nfeDadosRPS.setPesoFaturado((BigDecimal) MapUtils.getObject(dados, "rpsPesoFaturado"));
		nfeDadosRPS.setCubagem((BigDecimal) MapUtils.getObject(dados, "rpsCubagem"));
		nfeDadosRPS.setVolumes((BigDecimal) MapUtils.getObject(dados, "rpsVolumes"));
		nfeDadosRPS.setNatureza(MapUtils.getString(dados, "rpsNatureza"));
		nfeDadosRPS.setVlMercadoria((BigDecimal) MapUtils.getObject(dados, "vlMercadoria"));
		nfeDadosRPS.setIssqnRetido(MapUtils.getString(dados, "issqnRetido"));
		nfeDadosRPS.setVlLiquido((BigDecimal) MapUtils.getObject(dados, "vlLiquido"));
		
		nfeDadosRPS.setRemetente(getDadosRpsRemetente(dados));
		nfeDadosRPS.setDestinatario(getDadosRpsDestinatario(dados));
		
		return nfeDadosRPS;
	}
	
	private static NFEPessoa getDadosRpsRemetente(Map<String, Object> dados){
		NFEPessoa nfePessoa = new NFEPessoa();
		nfePessoa.setCnpj(MapUtils.getString(dados, "rpsCpfCnpjRemetente"));
		nfePessoa.setNmPessoa(MapUtils.getString(dados, "rpsNmPessoaRemetente"));
		nfePessoa.setIm(MapUtils.getString(dados, "rpsImRemetente"));
		
		NFEEndereco nfeEndereco = new NFEEndereco();
		
		nfePessoa.setEndereco(nfeEndereco);
		nfeEndereco.setLogradouro(MapUtils.getString(dados, "rpsEnderecoRemetente"));
		nfeEndereco.setNmMunicipio(MapUtils.getString(dados, "nmMunicipioRemetente"));
		nfeEndereco.setSiglaUf(MapUtils.getString(dados, "rpsSiglaUfRemetente"));
		nfeEndereco.setIdCep(MapUtils.getString(dados, "rpsCepRemetente"));

		return nfePessoa;
	}
	
	private static NFEPessoa getDadosRpsDestinatario(Map<String, Object> dados){
		NFEPessoa nfePessoa = new NFEPessoa();
		nfePessoa.setCnpj(MapUtils.getString(dados, "rpsCpfCnpjDestinatario"));
		nfePessoa.setNmPessoa(MapUtils.getString(dados, "rpsNmPessoaDestinatario"));
		nfePessoa.setIm(MapUtils.getString(dados, "rpsImDestinatario"));
		
		NFEEndereco nfeEndereco = new NFEEndereco();
		
		nfePessoa.setEndereco(nfeEndereco);
		nfeEndereco.setLogradouro(MapUtils.getString(dados, "rpsEnderecoDestinatario"));
		nfeEndereco.setNmMunicipio(MapUtils.getString(dados, "nmMunicipioDestinatario"));
		nfeEndereco.setSiglaUf(MapUtils.getString(dados, "rpsSiglaUfDestinatario"));
		nfeEndereco.setIdCep(MapUtils.getString(dados, "rpsCepDestinatario"));

		return nfePessoa;
	}

	private static NFECanc getDadosCancelar(Map<String, Object> dados){
		NFECanc nfeCanc = new NFECanc();
				
		nfeCanc.setVersao(MapUtils.getString(dados, "versao"));
		nfeCanc.setTpOperacao(MapUtils.getString(dados, "tpOperacao"));
		nfeCanc.setIdChave(MapUtils.getString(dados, "idChave"));
		nfeCanc.setTpAmbiente(MapUtils.getString(dados, "tpAmbiente"));
		nfeCanc.setNmServico(MapUtils.getString(dados, "nmServico"));
		nfeCanc.setNrChaveNFE(MapUtils.getString(dados, "nrChaveNFE"));
		nfeCanc.setNrProtocoloAutorizacao(MapUtils.getString(dados, "nrProtocoloAutorizacao"));
		nfeCanc.setNmJustificativa(MapUtils.getString(dados, "nmJustificativa"));
		
		return nfeCanc;
	}
	
	private static NFEInut getDadosInutilizar(Map<String, Object> dados){
		NFEInut nfeInut = new NFEInut();
				
		nfeInut.setVersao(MapUtils.getString(dados, "versao"));
		nfeInut.setTpOperacao(MapUtils.getString(dados, "tpOperacao"));
		nfeInut.setIdChave(MapUtils.getString(dados, "idChave"));
		nfeInut.setTpAmbiente(MapUtils.getString(dados, "tpAmbiente"));		
		nfeInut.setNmServico(MapUtils.getString(dados, "nmServico"));		
		nfeInut.setIdUfSolicitante(MapUtils.getString(dados, "idUfSolicitante"));
		nfeInut.setAnoNumeracao(MapUtils.getString(dados, "anoNumeracao"));
		nfeInut.setCnpjEmitente(MapUtils.getString(dados, "cnpjEmitente"));		
		nfeInut.setNrModeloNfe(MapUtils.getString(dados, "nrModeloNfe"));
		nfeInut.setNrSerieNfe(MapUtils.getString(dados, "nrSerieNfe"));
		nfeInut.setNrInicialNfe(MapUtils.getString(dados, "nrInicialNfe"));
		nfeInut.setNrFinalNfe(MapUtils.getString(dados, "nrFinalNfe"));
		nfeInut.setNmJustificativa(MapUtils.getString(dados, "nmJustificativa"));
		
		return nfeInut;
	}
	
}