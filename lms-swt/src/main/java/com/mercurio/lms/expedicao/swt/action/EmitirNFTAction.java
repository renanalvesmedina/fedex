package com.mercurio.lms.expedicao.swt.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.report.EmitirNFTService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @spring.bean id="lms.expedicao.swt.emitirNFTAction"
 */

public class EmitirNFTAction {
	private static final String REMETENTE = "Remetente";
	private static final String DESTINATARIO = "Destinatario";
	
	private EmitirNFTService emitirNFTService;
	private ConhecimentoService conhecimentoService;
	private PessoaService pessoaService;
	private EnderecoPessoaService enderecoPessoaService;

	public Map findFilialSession() {
		Map result = new TypedFlatMap();
		Filial filial = SessionUtils.getFilialSessao();
		result.put("idFilial", filial.getIdFilial());
		result.put("sgFilial", filial.getSgFilial());
		result.put("nmFantasia", filial.getPessoa().getNmFantasia());
		return result;
	}

	public List findNFT(Map criteria) {
		Long nrConhecimento = (Long) criteria.get("nrDoctoServico");
		Long idFilialOrigem = (Long) criteria.get("idFilialOrigem");
		
		List result = conhecimentoService.findByNrConhecimentoIdFilialOrigem(nrConhecimento, idFilialOrigem, "E", ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE);
		result = conhecimentoService.findEnderecosClientesConhecimentos(result);
		
		String nrFormatado = FormatUtils.formatDecimal("00000000", nrConhecimento);
		for (Iterator iter = result.iterator(); iter.hasNext();) {
			Map map = (Map) iter.next();
			map.put("nrDoctoServico", nrFormatado);
			
			ajustaPessoa(map, REMETENTE);
			ajustaPessoa(map, DESTINATARIO);
		}
		return result;
	}

	/**
	 * Reemissao Nota Fiscal de Transporte
	 * @param parameters
	 * @return
	 */
	public Map reemitirNFT(Map parameters) {
		Long nrProximoFormulario = (Long) parameters.get("nrProximoFormulario");
		String dsMacAddress = (String) parameters.get("dsMacAddress");

		//Reemissao
		String nft = emitirNFTService.executeEmitirNFT(
			(Long) parameters.get("idDoctoServico"),
			ConstantesExpedicao.CD_REEMISSAO,
			nrProximoFormulario,
			dsMacAddress);

		Map result = new HashMap();
		result.put("nft", nft);
		return result;
	}
	
	public Map findDadosPessoas(Map criteria) {
		Map<String, Object> result = new HashMap<String, Object>();
		Long idPessoaRemetente = MapUtilsPlus.getLong(criteria, "idPessoaRemetente");
		Long idPessoaDestinatario = MapUtilsPlus.getLong(criteria, "idPessoaDestinatario");
		Pessoa remetente = pessoaService.findById(idPessoaRemetente);
		Pessoa destinatario = pessoaService.findById(idPessoaDestinatario);
		ajustaPessoa(result, remetente, REMETENTE);
		ajustaPessoa(result, destinatario, DESTINATARIO);
		return result;
	}
	
	/*
	 * METODOS PRIVADOS
	 */
	private void ajustaPessoa(Map dados, String tipo) {
		Map pessoa = (Map) dados.remove(tipo.toLowerCase());
		Map endereco = (Map) pessoa.get("endereco");
		
		Map tpIdentificacao = (Map) pessoa.get("tpIdentificacao");
		String nrIdentificacao = FormatUtils.formatIdentificacao((String) tpIdentificacao.get("value"), (String) pessoa.get("nrIdentificacao"));
		
		dados.put("nrIdentificacao" + tipo, nrIdentificacao);
		dados.put("nmPessoa" + tipo, pessoa.get("nmPessoa"));
		ajustaEndereco(dados, endereco, tipo);
	}
	
	private void ajustaPessoa(Map dados, Pessoa pessoa, String tipo) {
		String nrIdentificacao = FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(), pessoa.getNrIdentificacao());
		dados.put("nrIdentificacao" + tipo, nrIdentificacao);
		dados.put("nmPessoa" + tipo, pessoa.getNmPessoa());
		EnderecoPessoa ep = enderecoPessoaService.findEnderecoPessoaPadrao(pessoa.getIdPessoa());
		ajustaEndereco(dados, PessoaUtils.getEnderecoPessoa(ep), tipo);
	}
	
	private void ajustaEndereco(Map dados, Map endereco, String tipo) {
		dados.put("dsEndereco" + tipo, endereco.get("dsTipoLogradouro") + " " + endereco.get("dsEndereco"));
		dados.put("nrEndereco" + tipo, endereco.get("nrEndereco"));
		dados.put("dsComplemento" + tipo, endereco.get("dsComplemento"));
		dados.put("nmMunicipio" + tipo, endereco.get("nmMunicipio"));
		dados.put("sgUnidadeFederativa" + tipo, endereco.get("sgUnidadeFederativa"));
		dados.put("nrCep" + tipo, endereco.get("nrCep"));
	}
	
	/*
	 * GETTERS E SETTERS
	 */
	public void setEmitirNFTService(EmitirNFTService emitirNFTService) {
		this.emitirNFTService = emitirNFTService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
}