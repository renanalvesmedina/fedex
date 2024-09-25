package com.mercurio.lms.contratacaoveiculos.model.service;

import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.ABRANGENCIA_INTERNACIONAL;
import static com.mercurio.lms.util.FormatUtils.fillNumberWithZero;
import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.contratacaoveiculos.model.FluxoContratacao;
import com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.FluxoFilialService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.chaveFluxoContratacaoService"
 */
public class ChaveFluxoContratacaoService {
	private FilialService filialService;
	private FluxoFilialService fluxoFilialService;
	private SolicitacaoContratacaoService solicitacaoContratacaoService;
	private MoedaPaisService moedaPaisService;
	
	/**
	 * 
	 * @param solicitacaoContratacao
	 * @param fluxoContratacao
	 * @param nrPrazo
	 * @return
	 */
	public Object[] calcularChaveFluxoContratacao(SolicitacaoContratacao solicitacaoContratacao, FluxoContratacao fluxoContratacao, Integer nrPrazo) {
		Long idFilialSolicitacao = solicitacaoContratacao.getFilial().getIdFilial();
		Long idFilialOrigemFluxo = fluxoContratacao.getFilialOrigem().getIdFilial();
		Long idFilialDestinoFluxo = fluxoContratacao.getFilialDestino().getIdFilial();
		String tpVinculoContratacao = solicitacaoContratacao.getTpVinculoContratacao().getValue();
		BigDecimal vlFreteMaximoAutorizado = solicitacaoContratacao.getVlFreteMaximoAutorizado();
		BigDecimal pcValorFreteFluxo = fluxoContratacao.getPcValorFrete();
		boolean isFilialOrigemRota = idFilialOrigemFluxo.equals(idFilialSolicitacao);
		BigDecimal vlPostoPassagem = calcularVlPostoPassagem(solicitacaoContratacao, fluxoContratacao);
		String tpAbrangencia = fluxoContratacao.getTpAbrangencia().getValue();
		
		StringBuffer toReturn = new StringBuffer();
		
		YearMonthDay dtViagem;
		if (isFilialOrigemRota) {
			dtViagem = solicitacaoContratacao.getDtViagem();
		} else {
			nrPrazo = getPrazoFluxo(nrPrazo, idFilialOrigemFluxo, idFilialDestinoFluxo);
			dtViagem = somarPrazoDtViagem(solicitacaoContratacao, nrPrazo);
		}
		BigDecimal vlAprovadoTrecho = BigDecimalUtils.round(vlFreteMaximoAutorizado.multiply(pcValorFreteFluxo).divide(BigDecimalUtils.HUNDRED));
		
		toReturn.append(calcularChaveParte1(idFilialOrigemFluxo, toReturn));
		
		toReturn.append(calcularChaveParte2(dtViagem));
		
		toReturn.append(calcularChaveParte3(vlAprovadoTrecho));
		
		toReturn.append(calcularChaveParte4(vlAprovadoTrecho));
		
		toReturn.append(calcularChaveParte6(solicitacaoContratacao));
		
		toReturn.append(calcularChaveParte7(idFilialDestinoFluxo));
		
		toReturn.append(calcularChaveParte8(tpVinculoContratacao, isFilialOrigemRota));
		
		toReturn.append(calcularChaveParte9(vlPostoPassagem));
		
		toReturn.append(calcularChaveParte10(vlPostoPassagem));
		
		toReturn.append(calcularChaveParte11(tpAbrangencia, fluxoContratacao));

		toReturn.insert(12, calcularChaveParte5(toReturn.toString()));
		
		return new Object[]{toReturn.toString(), nrPrazo, dtViagem};
	}

	/**
	 * Retorna o valor do posto de passagem, de acordo com o fluxo
	 * Regra:
	 * Somente a chave com o fluxo que contém a primeira e a última 
	 * filial da rota (maior trecho) terá o valor do posto de passagem, 
	 * as demais chaves terão este valor zerado.
	 * @param solicitacaoContratacao
	 * @param fluxoContratacao
	 * @return
	 */
	private BigDecimal calcularVlPostoPassagem(SolicitacaoContratacao solicitacaoContratacao, FluxoContratacao fluxoContratacao) {
		if ("A".equals(solicitacaoContratacao.getTpAbrangencia().getValue())) {
			if (!"N".equals(fluxoContratacao.getTpAbrangencia().getValue())) {
				return ZERO;
			}
		}
		BigDecimal toReturn = ZERO;
		List<Filial> filiais = solicitacaoContratacaoService.findFiliaisRotaBySolicitacao(solicitacaoContratacao);
		if (filiais.size() > 0) {
			if (filiais.get(0).getIdFilial().equals(fluxoContratacao.getFilialOrigem().getIdFilial()) &&
				filiais.get(filiais.size() - 1).getIdFilial().equals(fluxoContratacao.getFilialDestino().getIdFilial())) {
				toReturn = solicitacaoContratacao.getVlPostoPassagem();
			}
		}
		return toReturn;
	}

	/**
	 * 1) 3 dígitos para CGC filial origem
	 * 		cgc da filial que receberá a chave 
	 * 		(FLUXO_CONTRATACAO.ID_FILIAL_ORIGEM = PESSOA.ID_PESSOA -> PESSOA.NR_IDENTIFICACAO), 
	 * 		são consideradas as últimas 3 posições desconsiderando as 2 posições do dígito verificador.
	 * @param idFilialOrigemFluxo
	 * @param toReturn
	 * @return
	 */
	private String calcularChaveParte1(Long idFilialOrigemFluxo, StringBuffer toReturn) {
		String nrIdentificacaoOrigem = fillNumberWithZero(getNrIdentificacaoFilial(idFilialOrigemFluxo),14);
		return nrIdentificacaoOrigem.substring(nrIdentificacaoOrigem.length() - 5, nrIdentificacaoOrigem.length() - 2);
	}
	
	/**
	 * 2) 2 dígitos para o dia da solicitação
	 * Para a filial origem (FLUXO_CONTRATACAO.ID_FILIAL_ORIGEM) igual a filial da contratação 
	 * (SOLICITACAO_CONTRATACAO.ID_FILIAL), considerar a data da viagem (SOLICITACAO_CONTRATACAO.DT_VIAGEM).
	 * Para as demais, buscar o tempo existente no fluxo de carga (FLUXO_FILIAL,NR_PRAZO) 
	 * para a filial de origem e destino 
	 * (FLUXO_CONTRATACAO.ID_FILIAL_ORIGEM = FLUXO_FILIAL.ID_FILIAL_ORIGEM 
	 *  and FLUXO_CONTRATACAO.ID_FILIAL_DESTINO = FLUXO_FILIAL.ID_FILIAL_DESTINO)
	 * @param dtViagem
	 * @return
	 */
	private String calcularChaveParte2(YearMonthDay dtViagem) {
		return fillNumberWithZero(String.valueOf(dtViagem.getDayOfMonth()), 2);
	}

	/**
	 * 3) 5 dígitos para parte inteira do valor aprovado (SOLICITACAO_CONTRATACAO. VL_FRETE_MAXIMO_AUTORIZADO);
	 * @param vlAprovadoTrecho
	 * @return
	 */
	private String calcularChaveParte3(BigDecimal vlAprovadoTrecho) {
		return fillNumberWithZero(String.valueOf(vlAprovadoTrecho.intValue()), 5);
	}

	/**
	 * 4) 2 dígitos para a parte decimal do valor aprovado (SOLICITACAO_CONTRATACAO. VL_FRETE_MAXIMO_AUTORIZADO);
	 * Observação: considerar a parte do valor aprovado referente ao percentual do trecho 
	 * (VL_FRETE_MAXIMO_AUTORIZADO*FLUXO_CONTRATACAO.PC_VALOR_FRETE/100) 
	 * para os dois itens acima (3 e 4); 
	 * @param vlAprovadoTrecho
	 * @return
	 */
	private String calcularChaveParte4(BigDecimal vlAprovadoTrecho) {
		BigDecimal nrDecimal = vlAprovadoTrecho.subtract(new BigDecimal(vlAprovadoTrecho.intValue()));
		int decimal = nrDecimal.multiply(BigDecimalUtils.HUNDRED).intValue();
		return fillNumberWithZero(String.valueOf(decimal), 2);
	}
	
	/**
	 * 6) 8 dígitos para o ID da solicitação (SOLICITACAO_CONTRATACAO. ID_SOLICITACAO_CONTRATACAO);
	 * @param solicitacaoContratacao
	 * @return
	 */
	private String calcularChaveParte6(SolicitacaoContratacao solicitacaoContratacao) {
		return fillNumberWithZero(solicitacaoContratacao.getIdSolicitacaoContratacao().toString(), 8);
	}

	/**
	 * 7) 3 dígitos para CGC filial destino
	 * cgc da filial que receberá a chave 
	 * (FLUXO_CONTRATACAO.ID_FILIAL_DESTINO = PESSOA.ID_PESSOA -> PESSOA.NR_IDENTIFICACAO), 
	 * são consideradas as últimas 3 posições desconsiderando as 2 posições do dígito verificador.
	 * @param idFilialDestinoFluxo
	 * @return
	 */
	private String calcularChaveParte7(Long idFilialDestinoFluxo) {
		String nrIdentificacaoDestino = fillNumberWithZero(getNrIdentificacaoFilial(idFilialDestinoFluxo),14);
		return nrIdentificacaoDestino.substring(nrIdentificacaoDestino.length() - 5, nrIdentificacaoDestino.length() - 2);
	}

	/**
	 * 8) 1 dígito para tipo de vínculo caso
	 * 	SOLICITACAO_CONTRATACAO.TP_VINCULO = E e filial de origem da rota (primeiro registro do fluxo filial) atribuir 1;
	 * 	SOLICITACAO_CONTRATACAO.TP_VINCULO = A e filial de origem da rota (primeiro registro do fluxo filial) atribuir 2;
	 * 	SOLICITACAO_CONTRATACAO.TP_VINCULO = P e filial de origem da rota (primeiro registro do fluxo filial) atribuir 3;
	 * 	SOLICITACAO_CONTRATACAO.TP_VINCULO = E e filial de meio da rota (não é o primeiro registro do fluxo filial) atribuir 4;
	 * 	SOLICITACAO_CONTRATACAO.TP_VINCULO = A e filial de meio da rota (não é o primeiro registro do fluxo filial) atribuir 5;
	 * 	SOLICITACAO_CONTRATACAO.TP_VINCULO = P e filial de meio da rota (não é o primeiro registro do fluxo filial) atribuir 6;
	 * @param tpVinculoContratacao
	 * @param isFilialOrigemRota
	 * @return
	 */
	private String calcularChaveParte8(String tpVinculoContratacao, boolean isFilialOrigemRota) {
		return getDigitoTpVinculo(tpVinculoContratacao, isFilialOrigemRota);
	}
	
	/**
	 * 9) 3 dígitos para parte inteira do valor do posto de passagem 
	 * (SOLICITACAO_CONTRATACAO.VL_POSTO_PASSAGEM campo da tela). 
	 * Somente a chave com o fluxo que contém a primeira e a última filial da rota (maior trecho) 
	 * terá o valor do posto de passagem, as demais chaves terão este valor zerado.
	 * @return
	 */
	private String calcularChaveParte9(BigDecimal vlPostoPassagem) {
		return fillNumberWithZero(String.valueOf(vlPostoPassagem.intValue()), 3);
	}
	
	/**
	 * 10) 2 dígitos para a parte decimal do valor do posto de passagem 
	 * (SOLICITACAO_CONTRATACAO.VL_POSTO_PASSAGEM). 
	 * Somente a chave com o fluxo que contém a primeira e a útima filial da rota (maior trecho) 
	 * terá o valor do posto de passagem, as demais chaves terão este valor zerado.
	 * @return
	 */
	private String calcularChaveParte10(BigDecimal vlPostoPassagem) {
		BigDecimal nrDecimal = vlPostoPassagem.subtract(new BigDecimal(vlPostoPassagem.intValue()));
		int decimal = nrDecimal.multiply(BigDecimalUtils.HUNDRED).intValue();
		return fillNumberWithZero(String.valueOf(decimal), 2);
	}

	/**
	 * 5) 2 dígitos calculados através do módulo 11, conforme segue:
	 * Supondo chave 002070100000DD000020390032, temos:
	 * 	002	- Filial Origem
	 * 	07	- Dia
	 * 	01000	- Parte inteira do valor da contratação
	 * 	00	- Parte decimal do valor da contratação
	 * 	DD	- serão os dígitos gerados através da regra abaixo.
	 * 	00002039  - ID da solicitação de contratação
	 * 	002	- Filial Destino
	 * 	3	- Tipo de vínculo
	 * 	100 	- Parte inteira do valor do posto de passagem
	 * 	00	- Parte decimal do valor do posto de passagem
	 * 	l_chave = 00207010000000002039003210000
	 * 
	 * Cálculo a ser realizado:
	 * 	((substr(l_chave, 1,1) * 30) + (substr(l_chave, 2,1) * 29) ... (substr(l_chave,29,1) *  2)) mod 11
	 * 	if l_digito1 <2 then
	 * 		l_digito1:=2;
	 * 	else
	 * 		l_digito1:=11 - l_digito1;
	 * 	end if;
	 * 	((substr(l_chave, 1,1) * 31) + (substr(l_chave, 2,1) * 30) ... (substr(l_chave,30,1) *  2) ) mod 11
	 * 	if l_digito2 <2 then
	 * 		l_digito2:=2;
	 * 	else
	 * 		l_digito2:=11 - l_digito2;
	 * 	end if;
	 * @param chave
	 * @return
	 */
	private String calcularChaveParte5(String chave) {
		//Calcula modulo 11 da chave
		String toReturn = String.valueOf(mod11(chave));
		//Calcula modulo 11 da chave com o digito calculado acima na 12 posição
		StringBuffer sb = new StringBuffer(chave);
		sb.insert(12, toReturn);
		return toReturn + String.valueOf(mod11(sb.toString()));
	}
	
	/**
	 * 11) 1 digito que deve ser gerado apenas na chave quando a abrangência do fluxo for
	 * internacional (FLUXO_CONTRATACAO.TP_ABRANGENCIA = ‘I’). Este dígito identifica a moeda. 
	 * Deve ser buscado pela relação entre a moeda utilizada na contratação e a identificação 
	 * da mesma nos parâmetros gerais (SOLICITACAO_CONTRATACAO.ID_MOEDA -> MOEDA.ID_MOEDA -> 
	 * MOEDA.SG_MOEDA -> PARAMETRO_GERAL.NM_PARAMETRO_GERAL -> PARAMETRO_GERAL.DS_CONTEUDO) 
	 * @param chave
	 * @return
	 */
	private String calcularChaveParte11(String tpAbrangencia, FluxoContratacao fluxoContratacao) {
		if (ABRANGENCIA_INTERNACIONAL.equals(tpAbrangencia)) {
			
			Moeda moeda = moedaPaisService.findById(fluxoContratacao.getSolicitacaoContratacao().getMoedaPais().getIdMoedaPais()).getMoeda();
			return fillNumberWithZero(moeda.getNrIsoCode().toString(), 3);
		} else return "";
	}

	private YearMonthDay somarPrazoDtViagem(SolicitacaoContratacao solicitacaoContratacao, Integer nrPrazo) {
		return JTDateTimeUtils.yearMonthDayToDateTime(solicitacaoContratacao.getDtViagem()).plus(nrPrazo.intValue()).toYearMonthDay();
	}

	/**
	 * Retorna o prazo para percorrer o trecho somado ao prazo passado por parâmetro
	 * @param nrPrazo
	 * @param idFilialOrigemFluxo
	 * @param idFilialDestinoFluxo
	 * @return
	 */
	private Integer getPrazoFluxo(Integer nrPrazo, Long idFilialOrigemFluxo, Long idFilialDestinoFluxo) {
		Integer toReturn = Integer.valueOf(nrPrazo);
		List fluxoFilialOrigemDestino = fluxoFilialService.findFluxoFilialByFilialDestinoOrigemServico(idFilialOrigemFluxo, 
				idFilialDestinoFluxo, 
				null, 
				JTDateTimeUtils.getDataAtual());
		
		for (Iterator ie = fluxoFilialOrigemDestino.iterator(); ie.hasNext();) {
			Object[] fluxo = (Object[])ie.next();
			toReturn = Integer.valueOf(toReturn.intValue() + ((Integer) fluxo[1]).intValue());
		}
		return toReturn;
	}

	private String getDigitoTpVinculo(String tpVinculoContratacao, boolean isFilialOrigemRota) {
		String toReturn;
		//caso
		//o	SOLICITACAO_CONTRATACAO.TP_VINCULO = E e filial de origem da rota (primeiro registro do fluxo filial) atribuir 1
		//o	SOLICITACAO_CONTRATACAO.TP_VINCULO = A e filial de origem da rota (primeiro registro do fluxo filial) atribuir 2
		//o	SOLICITACAO_CONTRATACAO.TP_VINCULO = P e filial de origem da rota (primeiro registro do fluxo filial) atribuir 3
		//o	SOLICITACAO_CONTRATACAO.TP_VINCULO = E e filial de meio da rota (não é o primeiro registro do fluxo filial) atribuir 4
		//o	SOLICITACAO_CONTRATACAO.TP_VINCULO = A e filial de meio da rota (não é o primeiro registro do fluxo filial) atribuir 5
		//o	SOLICITACAO_CONTRATACAO.TP_VINCULO = P e filial de meio da rota (não é o primeiro registro do fluxo filial) atribuir 6
		if (isFilialOrigemRota) {
			if ("E".equals(tpVinculoContratacao)) {
				toReturn = "1";
			} else if ("A".equals(tpVinculoContratacao)) {
				toReturn = "2";
			} else {
				toReturn = "3";
			}
		} else {
			if ("E".equals(tpVinculoContratacao)) {
				toReturn = "4";
			} else if ("A".equals(tpVinculoContratacao)) {
				toReturn = "5";
			} else {
				toReturn = "6";
			}
		}
		return toReturn;
	}
	
	private String getNrIdentificacaoFilial(Long idFilial) {
		return filialService.findById(idFilial).getPessoa().getNrIdentificacao();
	}

	private int mod11(String s) {
		int mult = s.length()+1;
		int somatorio = 0;
		for (int i = 0; i < s.length(); i++) {
			somatorio += Integer.parseInt(s.substring(i, i + 1)) * mult;
			mult--;
		}
		int digito1 = somatorio % 11;
		if (digito1 < 2) {
			digito1 = 2;
		} else {
			digito1 = 11 - digito1;
		}
		return digito1;
	}
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setFluxoFilialService(FluxoFilialService fluxoFilialService) {
		this.fluxoFilialService = fluxoFilialService;
	}
	public void setSolicitacaoContratacaoService(SolicitacaoContratacaoService solicitacaoContratacaoService) {
		this.solicitacaoContratacaoService = solicitacaoContratacaoService;
	}
	public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
		this.moedaPaisService = moedaPaisService;
}
	
}
