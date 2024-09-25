/**
 * 
 */
package com.mercurio.lms.tabelaprecos.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.util.FormatUtils;

/**
 * Classe utilitaria para manipulação de informações relacionadas com a Tabela
 * de Preço.
 * 
 * @author Luis Carlos Poletto
 * 
 */
public class TabelaPrecoUtils {
	private static final Pattern CD_TABELA_PRECO_PATTERN = Pattern.compile("^(([A-Z@][0-9]+))-[A-Z0-9]$");

	/**
	 * Formata os dados recebidos no padrão para exibição na tela, por exemplo:
	 * <p>
	 * "M06-X", "M05-A", etc.
	 * 
	 * @param tpTipoTabelaPreco
	 * @param nrVersaoTipoTabelaPreco
	 * @param tpSubtipoTabelaPreco
	 * @return Representação dos dados recebidos por parametro para exibição amigável
	 */
	public static String formatTabelaPrecoString(
			String tpTipoTabelaPreco,
			Integer nrVersaoTipoTabelaPreco,
			String tpSubtipoTabelaPreco
	) {
		StringBuffer sb = new StringBuffer();
		if (tpTipoTabelaPreco == null) {
			throw new IllegalArgumentException("Parâmetro tpTipoTabelaPreco obrigatório!");
		}
		if (StringUtils.isNotBlank(tpTipoTabelaPreco)) {
			sb.append(tpTipoTabelaPreco);
		}
		if (nrVersaoTipoTabelaPreco != null) {
			sb.append(nrVersaoTipoTabelaPreco);
		}
		if (StringUtils.isNotBlank(tpSubtipoTabelaPreco)) {
			sb.append("-");
			sb.append(tpSubtipoTabelaPreco);
		}
		return sb.toString();
	}

	public static String formatTabelaPrecoString(
			VarcharI18n tpTipoTabelaPreco,
			Integer nrVersaoTipoTabelaPreco,
			String tpSubtipoTabelaPreco,
			String dsDescricao,
			VarcharI18n dsServico
	) {
		StringBuffer sb = new StringBuffer();
		if (tpTipoTabelaPreco== null) {
			throw new IllegalArgumentException("Parâmetro tpTipoTabelaPreco obrigatório!");
		}
		sb.append(formatTabelaPrecoString(tpTipoTabelaPreco.getValue(), nrVersaoTipoTabelaPreco, tpSubtipoTabelaPreco));
		if(StringUtils.isNotBlank(dsDescricao)) {
			sb.append(" - ");
			sb.append(dsDescricao);
		}
		if(dsServico!=null) {
			sb.append(" - ");
			sb.append(dsServico.getValue());
		}
		return sb.toString();
	}

	/**
	 * Verifica se um código de tabela de preço é válido.
	 * 
	 * Formatos válidos: M07-X ou M-X (com versão e sem versão)
	 * 
	 * @param code
	 * @return
	 */
	public static boolean validateCdTabelaPreco(String cdTabelaPreco) {
		return CD_TABELA_PRECO_PATTERN.matcher(cdTabelaPreco).matches();
	}

	/**
	 * Desmonta um código de tabela de preço.
	 * 
	 * @param cdTabelaPreco
	 * @return Um mapa contendo os atributos de identificaçao da tabela
	 */
	public static Map<String, Object> unmontCdTabelaPreco(String cdTabelaPreco) {
		Map<String, Object> result = null;
		cdTabelaPreco = cdTabelaPreco.toUpperCase();
		if(validateCdTabelaPreco(cdTabelaPreco)) {
			result = new HashMap<String, Object>(2);
			Map tipoTabelaPreco = new HashMap<String, Object>(2);
			tipoTabelaPreco.put("tpTipoTabelaPreco", cdTabelaPreco.substring(0, 1));
			
			if(cdTabelaPreco.length() == 3) {
				tipoTabelaPreco.put("tpSubtipoTabelaPreco", cdTabelaPreco.substring(2, 3));
			} else {
				int hifen = cdTabelaPreco.indexOf('-');

				tipoTabelaPreco.put("nrVersao", cdTabelaPreco.substring(1, hifen));

				Map subTipoTabelaPreco = new HashMap<String, Object>(1);
				subTipoTabelaPreco.put("tpSubtipoTabelaPreco", cdTabelaPreco.substring(hifen+1));
				result.put("subtipoTabelaPreco", subTipoTabelaPreco);
			}
			result.put("tipoTabelaPreco", tipoTabelaPreco);
		}
		return result;
	}

	/**
	 * Retorna o objeto TabelaPreco armazenado na sessao.
	 * 
	 * Caso nao exista uma TabelaPreco na sessao serah gerada uma excessao avisando que a mesma expirou.
	 * 
	 * @return TabelaPreco da sessao.
	 */
	public static TabelaPreco getTabelaPrecoInSessionExpire() {
		TabelaPreco tabelaPreco = (TabelaPreco) SessionContext.get(ConstantesTabelaPrecos.TABELA_PRECO_IN_SESSION);
		if (tabelaPreco == null) {
			throw new BusinessException("LMS-04124");
		}
		return tabelaPreco;
	}

	/**
	 * Retorna o objeto TabelaPreco armazenado na sessao.
	 * 
	 * Caso nao exista uma TabelaPreco na sessao um novo objeto serah armazenado na mesma e retornado.
	 * 
	 * @return TabelaPreco da sessao.
	 */
	public static TabelaPreco getTabelaPrecoInSession() {
		TabelaPreco tabelaPreco = (TabelaPreco) SessionContext.get(ConstantesTabelaPrecos.TABELA_PRECO_IN_SESSION);
		if (tabelaPreco == null) {
			tabelaPreco = new TabelaPreco();
			setTabelaPrecoInSession(tabelaPreco);
		}
		return tabelaPreco;
	}

	/**
	 * Seta o objeto TabelaPreco recebido na sessao.
	 * 
	 * @param awb awb para ser setado na sessao.
	 */
	public static void setTabelaPrecoInSession(TabelaPreco tabelaPreco) {
		SessionContext.set(ConstantesTabelaPrecos.TABELA_PRECO_IN_SESSION, tabelaPreco);
	}

	/**
	 * Remove o objeto AWB da sessao.
	 */
	public static void removeTabelaPrecoFromSession() {
		SessionContext.remove(ConstantesTabelaPrecos.TABELA_PRECO_IN_SESSION);
	}

}
