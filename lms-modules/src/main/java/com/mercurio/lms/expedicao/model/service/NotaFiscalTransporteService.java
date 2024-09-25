package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.CalculoNFT;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ParcelaServico;
import com.mercurio.lms.expedicao.util.CalculoFreteUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tributos.model.service.AliquotaIssMunicipioServService;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para digitação da nota fiscal de transporte:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.notaFiscalTransporteService"
 */
public class NotaFiscalTransporteService extends AbstractConhecimentoNormalService {
	private UnidadeFederativaService unidadeFederativaService;
	private AliquotaIssMunicipioServService aliquotaIssMunicipioServService;

	/**
	 * Seta Dados 
	 * @param conhecimento
	 */
	public Map validateNotaPrimeiraFase(Conhecimento conhecimento, CalculoNFT calculoNFT) {
		validatePrimeiraFase(conhecimento, calculoNFT);
		return validateEmissaoNFT(conhecimento);
	}

	/**
	 * Validações
	 * @param conhecimento
	 */
	public void validateNotaSegundaFase(Conhecimento conhecimento, CalculoFrete calculoFrete) {
		validateSegundaFase(conhecimento, calculoFrete);
	}

	/**
	 * Regra ValidaNota 2
	 * 
	 * ­ Verificar se esta filial pode emitir nota fiscal de transporte. 
	 * Verificar se o parâmetro 'IMPRIME_NF_TRANSP' da tabela PARAMETROS_FILIAL, possui valor 'S' 
	 * na tabela CONTEUDO_PARAMETRO_FILIAL. Se o parâmetro for 'N', mostrar a mensagem: 
	 * LMS-04139 - 'Esta filial não está configurada para emitir Nota Fiscal de Transporte'.
	 * 
	 */
	private Map validateEmissaoNFT(Conhecimento conhecimento) {
		String permiteEmissao = String.valueOf(configuracoesFacade.getValorParametro(SessionUtils.getFilialSessao().getIdFilial(), ConstantesExpedicao.NM_PARAMETRO_IMPRIME_NF_TRANSP));
		if("N".equals(permiteEmissao)) {
			throw new BusinessException("LMS-04139");
		}
		Municipio municipioColeta = conhecimento.getMunicipioByIdMunicipioColeta();
		Municipio municipioEntrega = conhecimento.getMunicipioByIdMunicipioEntrega();

		Long idServicoTributo = LongUtils.getLong((BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.ID_SERVICO_TRIBUTO_NFT));
		Long idMunicipioSede = SessionUtils.getFilialSessao().getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio();
		Long idMunicipioServico = municipioEntrega.getIdMunicipio();
		TypedFlatMap emiteNfServico = aliquotaIssMunicipioServService.findEmiteNfServico(null, idServicoTributo, idMunicipioSede, idMunicipioServico);
		boolean blEmiteNota = Boolean.TRUE.equals(emiteNfServico.getBoolean("BlEmiteNota"));
		Map parametros = new HashMap();
		parametros.put("blEmiteNota", blEmiteNota);
		
		if(! blEmiteNota) {
			throw new BusinessException("LMS-04097");
		}

		if(municipioColeta.getIdMunicipio().equals(municipioEntrega.getIdMunicipio())) {
			return parametros;
		}
		if(!municipioColeta.getUnidadeFederativa().getIdUnidadeFederativa().equals(municipioEntrega.getUnidadeFederativa().getIdUnidadeFederativa())) {
			throw new BusinessException("LMS-04138");
		}
		if(unidadeFederativaService.getRowCountIncideIss(municipioColeta.getUnidadeFederativa().getIdUnidadeFederativa(), Boolean.TRUE).intValue() == 0) {
			throw new BusinessException("LMS-04138");
		}
		
		return parametros;
	}

	/** Método que executa o calculo manual do frete.
	 * - Verificar se o valor calculado como Frete Peso está menor que o valor mínimo de
	 * frete (PARAMETRO_GERAL.DS_CONTEUDO) cadastrado na tabela PARAMETRO_GERAL
	 * (PARAMETRO_GERAL.NM_PARAMETRO_GERAL = "VLR_MINIMO_FRETE"), se for menor visualizar mensagem LMS-04081;
	 * - Calcular o total do frete com o mesmo valor visualizado na linha da GRID Cálculo do frete;
	 * - Apresentar o total da Nota como a soma dos valores do total do frete e do total dos tributos;
	 *
	 * autor Julio Cesar Fernandes Corrêa
	 * 19/12/2005
	 * @param parameters
	 * @param conhecimento
	 * @return
	 */
	public CalculoNFT executeCalculoFreteManual(TypedFlatMap parameters, Conhecimento conhecimento, CalculoNFT calculo) {
		calculo.resetParcelasGerais();

		BigDecimal vlTotalFrete = parameters.getBigDecimal("vlParcela");
		ParcelaPreco parcelaPreco = new ParcelaPreco();
		parcelaPreco.setIdParcelaPreco(parameters.getLong("idParcela"));
		parcelaPreco.setNmParcelaPreco(parameters.getVarcharI18n("dsParcela"));
		parcelaPreco.setCdParcelaPreco(ConstantesExpedicao.CD_FRETE_PESO);
		ParcelaServico parcelaServico = new ParcelaServico(parcelaPreco, vlTotalFrete, vlTotalFrete);
		calculo.addParcelaGeral(parcelaServico);

		configureCalculoFrete(conhecimento, calculo);
		documentoServicoFacade.executeCalculoNotaFiscalTransporte(calculo);
		CalculoFreteUtils.copyResult(conhecimento, calculo);
		return calculo;
	}
	
	/**
	 * Método criado para externalizar lógica do método protected AbstractConhecimentoNormalService.setLocalEntrega
	 */
	public void setLocalEntregaConhecimentoNFT(Conhecimento cto) {
		setLocalEntrega(cto);
	}
	
	/**
	 * Método criado para externalizar lógica do método protected AbstractConhecimentoNormalService.storeConhecimento
	 */
	public void storeConhecimentoNFT(Conhecimento conhecimento, String tipo) {
		storeConhecimento(conhecimento, tipo);
	}

	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}
	public void setAliquotaIssMunicipioServService(AliquotaIssMunicipioServService aliquotaIssMunicipioServService) {
		this.aliquotaIssMunicipioServService = aliquotaIssMunicipioServService;
	}
}