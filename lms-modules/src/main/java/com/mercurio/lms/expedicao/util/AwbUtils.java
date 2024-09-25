/**
 * 
 */
package com.mercurio.lms.expedicao.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.AwbEmbalagem;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.seguros.model.ApoliceSeguro;
import com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.PessoaUtils;

/**
 * Métodos utilitarios para manipulação de AWBs.
 * 
 * @author Luis Carlos Poletto
 * 
 */
public class AwbUtils {
	
	private static final int PAD_NR_AWB = 6;
	private static final String EMPTY = "";
	private static final int INICIO_SERIE = 0;
	private static final int FINAL_NR_AWB = 10;
	private static final int TAMANHO_NR_AWB = 11;
	private static final int INICIO_NR_AWB = 4;
	private static final boolean ESQUERDA = true;

	/**
	 * <p>
	 * Retorna o objeto AWB armazenado na sessao.
	 * 
	 * <p>
	 * Caso nao exista uma AWB na sessao serah gerada uma excessao avisando que
	 * a mesma expirou.
	 * 
	 * @return AWB da sessao.
	 */
	public static Awb getAwbInSessionExpire() {
		Awb awb = (Awb) SessionContext.get(ConstantesExpedicao.AWB_IN_SESSION);
		if (awb == null) {
			throw new BusinessException("LMS-04124");
		}
		return awb;
	}

	/**
	 * <p>
	 * Retorna o objeto AWB armazenado na sessao.
	 * 
	 * <p>
	 * Caso nao exista uma AWB na sessao um novo objeto serah armazenado na
	 * mesma e retornado.
	 * 
	 * @return AWB da sessao.
	 */
	public static Awb getAwbInSession() {
		Awb awb = (Awb) SessionContext.get(ConstantesExpedicao.AWB_IN_SESSION);
		if (awb == null) {
			awb = new Awb();
			setAwbInSession(awb);
		}
		return awb;
	}

	/**
	 * Seta o objeto AWB recebido na sessao.
	 * 
	 * @param awb
	 *            awb para ser setado na sessao.
	 */
	public static void setAwbInSession(Awb awb) {
		ExpedicaoUtils.setTpDocumentoInSession(ConstantesExpedicao.AIRWAY_BILL);
		SessionContext.set(ConstantesExpedicao.AWB_IN_SESSION, awb);
	}

	/**
	 * Remove o objeto AWB da sessao.
	 */
	public static void removeAwbFromSession() {
		SessionContext.remove(ConstantesExpedicao.AWB_IN_SESSION);
	}
	
	/**
	 * 
	 * Formata o numero do AWB da seguinte maneira:
	 * <p>
	 * <code>nrAwb + " - " + dvAwb</code>
	 * 
	 * @param nrAwb
	 * @param dvAwb
	 * @return
	 */
	public static String formatNrAwb(Long nrAwb, Integer dvAwb) {
		StringBuilder awb = new StringBuilder();
		boolean first = true;
		if(nrAwb != null) {
			String fNrAwb = FormatUtils.completaDados(nrAwb, "0", 7, 0, ESQUERDA);
			awb.append(fNrAwb);
			first = false;
		}
		if(dvAwb != null) {
			if(!first) {
				awb.append("-");
			}
			awb.append(dvAwb);
		}
		return awb.toString();
	}	
	
	public static String getNrAwbSemFormatacao(Long nrAwb) {
		return FormatUtils.completaDados(nrAwb, "0", TAMANHO_NR_AWB, INICIO_SERIE, ESQUERDA);
	}
	
	public void prepareNrAwb(Map criteria) {
		Long nrAwb = (Long) criteria.get("nrAwb");
		if (nrAwb != null) {
			String sNrAwb = nrAwb.toString();
			sNrAwb = FormatUtils.completaDados(sNrAwb, "0", AwbUtils.TAMANHO_NR_AWB, 0, Boolean.TRUE);
			
			if (sNrAwb != null && sNrAwb.length() == AwbUtils.TAMANHO_NR_AWB) {
				String dsSerieAwb = StringUtils.substring(sNrAwb, AwbUtils.INICIO_SERIE, AwbUtils.INICIO_NR_AWB);
				String dvAwb = StringUtils.substring(sNrAwb, AwbUtils.FINAL_NR_AWB, AwbUtils.TAMANHO_NR_AWB);
				sNrAwb = StringUtils.substring(sNrAwb, AwbUtils.INICIO_NR_AWB, AwbUtils.FINAL_NR_AWB);
				criteria.put("nrAwb", Long.valueOf(sNrAwb));
				criteria.put("dsSerieAwb", dsSerieAwb);
				criteria.put("dvAwb", Integer.valueOf(dvAwb));
			}
		}
	}
	
	public static Long getNrAwb(Long nrAwb) {
		Long soNrAwb = null;
		if(nrAwb != null) {
			String nrAwbCompleto = nrAwb.toString();
			nrAwbCompleto = FormatUtils.completaDados(nrAwbCompleto, "0", TAMANHO_NR_AWB, INICIO_SERIE, ESQUERDA);
			soNrAwb = extractNrAwb(nrAwbCompleto);
		}
		return soNrAwb;
	}
	
	public static String getNrAwb(Awb awb){
		return getNrAwb(awb.getDsSerie(), awb.getNrAwb(), awb.getDvAwb());
	}
	
	public static String getNrAwbFormated(Awb awb){
		return getNrAwbFormated(awb.getDsSerie(), awb.getNrAwb(), awb.getDvAwb());
	}
	
	public static String getSgEmpresaAndNrAwbFormated(Awb awb){
		Empresa e = awb.getCiaFilialMercurio().getEmpresa();
		return e.getSgEmpresa() + " " + getNrAwbFormated(awb.getDsSerie(), awb.getNrAwb(), awb.getDvAwb());
	}
	
	public static String getNrAwb(String dsSerie, Long nrAwb, Integer dvAwb){
		return (dsSerie != null ? StringUtils.leftPad(dsSerie, INICIO_NR_AWB, '0') : "") + 
				 (nrAwb != null ? StringUtils.leftPad(nrAwb.toString(), PAD_NR_AWB, '0') : "") + 
				 (dvAwb != null ? dvAwb.toString() : "");
	}
	
	public static String getNrAwbFormated(String dsSerie, Long nrAwb, Integer dvAwb){
		return String.format("%s.%s-%s", dsSerie != null ? StringUtils.leftPad(dsSerie, INICIO_NR_AWB, '0') : "", 
										   nrAwb != null ? StringUtils.leftPad(nrAwb.toString(), PAD_NR_AWB, '0') : "", 
						                   dvAwb != null ? dvAwb.toString() : "");
	}
	
	public static Awb splitNrAwb(String nrAwb){
		return splitNrAwb(new Awb(), nrAwb);
	}
		
	public static Awb splitNrAwb(Awb awb, String nrAwb){
		if(nrAwb != null && !EMPTY.equals(nrAwb)){
			String nrAwbAux = complitNrAwb(nrAwb);
			awb.setNrAwb(extractNrAwb(nrAwbAux));
			awb.setDsSerie(extractDsSerie(nrAwbAux));
			awb.setDvAwb(extractDvAwb(nrAwbAux));
		}
		return awb;
	}

	public static Map<String, Object> splitNrAwbToMap(String nrAwb){
		Map<String, Object> map = new HashMap<String, Object>(); 
		if(nrAwb != null && !EMPTY.equals(nrAwb)){
			String nrAwbAux = complitNrAwb(nrAwb);
			map.put("nrAwb", extractNrAwb(nrAwbAux));
			map.put("dsSerie", extractDsSerie(nrAwbAux));
			map.put("dvAwb", extractDvAwb(nrAwbAux));
		}
		return map;
	}

	private static Integer extractDvAwb(String nrAwb) {
		return Integer.valueOf(nrAwb.substring(FINAL_NR_AWB));
	}

	private static Long extractNrAwb(String nrAwb) {
		return Long.valueOf(nrAwb.substring(INICIO_NR_AWB, FINAL_NR_AWB));
	}
	
	private static String extractDsSerie(String nrAwb) {
		return nrAwb.substring(INICIO_SERIE, INICIO_NR_AWB);
	}
	
	private static String complitNrAwb(String nrAwb) {
		return StringUtils.leftPad(nrAwb, TAMANHO_NR_AWB, '0');
	}
	
	public static String getPreAwbOrAwb(DomainValue tpStatusAwb, String nrAwbCompleto, String sgEmpresa) {
		StringBuilder sgEmpresaAndNrAwb = new StringBuilder(sgEmpresa);
		
		if (ConstantesAwb.TP_STATUS_AWB.equals(tpStatusAwb.getValue()) && StringUtils.isNotEmpty(sgEmpresa)) {
			Awb awb = splitNrAwb(nrAwbCompleto);
			sgEmpresaAndNrAwb.append(" ").append(getNrAwbFormated(awb));
		}else{
			sgEmpresaAndNrAwb.append(" ").append(nrAwbCompleto);
		}
		
		return sgEmpresaAndNrAwb.toString();
	}
	
	public static TypedFlatMap getPreAwb(Awb awb, TypedFlatMap result) {

		result.put("awb.idAwb", awb.getIdAwb());
		result.put("dhDigitacao", awb.getDhDigitacao());
		result.put("qtVolumes", awb.getQtVolumes());
		result.put("psCubado", awb.getPsCubado());
		result.put("psReal", awb.getPsTotal());
		result.put("tpFrete", awb.getTpFrete().getValue());
		result.put("tpStatusAwb", awb.getTpStatusAwb().getValue());
		result.put("vlFrete", awb.getVlFrete());
		result.put("dsVooPrevisto", awb.getDsVooPrevisto());
		result.put("dhPrevistaSaida", awb.getDhPrevistaSaida());
		result.put("dhPrevistaChegada", awb.getDhPrevistaChegada());
		result.put("obAwb", awb.getObAwb());

		Aeroporto aeroportoEscala = awb.getAeroportoByIdAeroportoEscala();
		if(aeroportoEscala != null) {
			result.put("aeroportoByIdAeroportoEscala.siglaDescricao", aeroportoEscala.getSiglaDescricao());
			result.put("aeroportoByIdAeroportoEscala.idAeroporto", aeroportoEscala.getIdAeroporto());
			result.put("aeroportoByIdAeroportoEscala.sgAeroporto", aeroportoEscala.getSgAeroporto());
			result.put("aeroportoByIdAeroportoEscala.pessoa.nmPessoa", aeroportoEscala.getPessoa().getNmPessoa());
		}

		Pessoa pessoa = awb.getClienteByIdClienteExpedidor().getPessoa();
		InscricaoEstadual inscricaoEstadual = awb.getInscricaoEstadualExpedidor();
		result.put("clienteByIdClienteRemetente.idCliente", awb.getClienteByIdClienteExpedidor().getIdCliente());
		result.put("clienteByIdClienteRemetente.pessoa.nrIdentificacao", pessoa.getNrIdentificacao());
		result.put("clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(awb.getClienteByIdClienteExpedidor().getPessoa()));
		result.put("clienteByIdClienteRemetente.pessoa.nmPessoa", pessoa.getNmPessoa());
		result.put("clienteByIdClienteRemetente.endereco", PessoaUtils.getEnderecoPessoa(pessoa.getEnderecoPessoa()));
		if(inscricaoEstadual != null) {
			result.put("clienteByIdClienteRemetente.idInscricaoEstadual", inscricaoEstadual.getIdInscricaoEstadual());
			result.put("clienteByIdClienteRemetente.nrInscricaoEstadual", inscricaoEstadual.getNrInscricaoEstadual());
		}

		pessoa = awb.getClienteByIdClienteTomador().getPessoa();
		inscricaoEstadual = awb.getInscricaoEstadualTomador();
		result.put("clienteByIdClienteTomador.idCliente", awb.getClienteByIdClienteTomador().getIdCliente());
		result.put("clienteByIdClienteTomador.pessoa.nrIdentificacao", pessoa.getNrIdentificacao());
		result.put("clienteByIdClienteTomador.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(awb.getClienteByIdClienteTomador().getPessoa()));
		result.put("clienteByIdClienteTomador.pessoa.nmPessoa", pessoa.getNmPessoa());
		result.put("clienteByIdClienteTomador.endereco", PessoaUtils.getEnderecoPessoa(pessoa.getEnderecoPessoa()));
		if(inscricaoEstadual != null) {
			result.put("clienteByIdClienteTomador.idInscricaoEstadual", inscricaoEstadual.getIdInscricaoEstadual());
			result.put("clienteByIdClienteTomador.nrInscricaoEstadual", inscricaoEstadual.getNrInscricaoEstadual());
		}

		pessoa = awb.getClienteByIdClienteDestinatario().getPessoa();
		inscricaoEstadual = awb.getInscricaoEstadualDestinatario();
		result.put("clienteByIdClienteDestinatario.idCliente", awb.getClienteByIdClienteDestinatario().getIdCliente());
		result.put("clienteByIdClienteDestinatario.pessoa.nrIdentificacao", awb.getClienteByIdClienteDestinatario().getPessoa().getNrIdentificacao());
		result.put("clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(awb.getClienteByIdClienteDestinatario().getPessoa()));
		result.put("clienteByIdClienteDestinatario.pessoa.nmPessoa", awb.getClienteByIdClienteDestinatario().getPessoa().getNmPessoa());
		result.put("clienteByIdClienteDestinatario.endereco", PessoaUtils.getEnderecoPessoa(pessoa.getEnderecoPessoa()));
		if(inscricaoEstadual != null) {
			result.put("clienteByIdClienteDestinatario.idInscricaoEstadual", inscricaoEstadual.getIdInscricaoEstadual());
			result.put("clienteByIdClienteDestinatario.nrInscricaoEstadual", inscricaoEstadual.getNrInscricaoEstadual());
		}

		result.put("aeroportoByIdAeroportoOrigem.idAeroporto", awb.getAeroportoByIdAeroportoOrigem().getIdAeroporto());
		result.put("aeroportoByIdAeroportoOrigem.sgAeroporto", awb.getAeroportoByIdAeroportoOrigem().getSgAeroporto());
		result.put("aeroportoByIdAeroportoOrigem.pessoa.nmPessoa", awb.getAeroportoByIdAeroportoOrigem().getPessoa().getNmPessoa());

		result.put("aeroportoByIdAeroportoDestino.idAeroporto", awb.getAeroportoByIdAeroportoDestino().getIdAeroporto());
		result.put("aeroportoByIdAeroportoDestino.sgAeroporto", awb.getAeroportoByIdAeroportoDestino().getSgAeroporto());
		result.put("aeroportoByIdAeroportoDestino.pessoa.nmPessoa", awb.getAeroportoByIdAeroportoDestino().getPessoa().getNmPessoa());

		result.put("naturezaProduto.idNaturezaProduto", awb.getNaturezaProduto().getIdNaturezaProduto());

		if(awb.getTarifaSpot() != null) {
			result.put("tarifaSpot.dsSenha", awb.getTarifaSpot().getDsSenha());
		}

		result.put("ciaFilialMercurio.empresa.pessoa.nmPessoa", awb.getCiaFilialMercurio().getEmpresa().getPessoa().getNmPessoa());

		ProdutoEspecifico produtoEspecifico = awb.getProdutoEspecifico();
		if(produtoEspecifico != null) {
			result.put("produtoEspecifico.idProdutoEspecifico", produtoEspecifico.getIdProdutoEspecifico());
		}

		List<AwbEmbalagem> awbEmbalagems = awb.getAwbEmbalagems();
		if(awbEmbalagems != null && !awbEmbalagems.isEmpty()) {
			AwbEmbalagem awbEmbalagem = awb.getAwbEmbalagems().get(0);
			if(awbEmbalagem != null) {
				result.put("embalagem.idEmbalagem", awbEmbalagem.getEmbalagem().getIdEmbalagem());
			}
		}

		result.put("nrCcTomadorServico", awb.getNrCcTomadorServico());
		result.put("nrLvSeguro", awb.getNrLvSeguro());
		
		ApoliceSeguro apoliceSeguro = awb.getApoliceSeguro();
		if(apoliceSeguro != null) {
			result.put("dsApolice", apoliceSeguro.getNrApolice());
		}

		return result;
	}
}
