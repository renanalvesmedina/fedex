package com.mercurio.lms.vendas.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * 
 * @author Rafael Fernando Kohlrausch <rafaelfernando@cwi.com.br>
 * 
 * @spring.bean id="lms.vendas.emitirRelatorioClientesComExcecaoService"
 * @spring.property name="reportName" value=
 *                  "com/mercurio/lms/vendas/report/emitirRelatorioClientesComExcecao.jasper"
 */
public class EmitirRelatorioClientesComExcecaoService extends ReportServiceSupport {

	@SuppressWarnings("unchecked")
	public JRReportDataObject execute(Map criteria) throws Exception {
		TypedFlatMap map = new TypedFlatMap(criteria);
		SqlTemplate sql = createSqlTemplate(map);

		// Seta os parametros
		Map parametersReport = new HashMap();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, criteria.get("formatoRelatorio"));

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		jr.setParameters(parametersReport);
		return jr;
	}

	private SqlTemplate createSqlTemplate(TypedFlatMap criteria) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();
		sql.setDistinct();
		
		/** SELECT */
		sql.addProjection("REGIONAL.ID_REGIONAL", "ID_REGIONAL");
		sql.addProjection("REGIONAL.DS_REGIONAL", "DS_REGIONAL");
		sql.addProjection("REGIONAL.SG_REGIONAL", "SG_REGIONAL");
		sql.addProjection("FILIAL.ID_FILIAL", "ID_FILIAL");
		sql.addProjection("PESSOA_FILIAL.NM_FANTASIA", "DS_FILIAL");
		sql.addProjection("FILIAL.SG_FILIAL", "SG_FILIAL");
		sql.addProjection("PESSOA.NR_IDENTIFICACAO", "NR_IDENTIFICACAO");
		sql.addProjection("PESSOA.TP_IDENTIFICACAO", "TP_IDENTIFICACAO");
		sql.addProjection("PESSOA.NM_PESSOA", "NM_PESSOA");
		sql.addProjection("USUARIO.NM_USUARIO", "NM_USUARIO_PROMOTOR");
		sql.addProjection("DIVISAO_CLIENTE.CD_DIVISAO_CLIENTE", "CD_DIVISAO_CLIENTE");
		sql.addProjection("DIVISAO_CLIENTE.DS_DIVISAO_CLIENTE", "DS_DIVISAO_CLIENTE");
		sql.addProjection("TIPO_TABELA_PRECO.TP_TIPO_TABELA_PRECO", "TP_TIPO_TABELA_PRECO");
		sql.addProjection("TIPO_TABELA_PRECO.NR_VERSAO", "NR_VERSAO");
		sql.addProjection("SUBTIPO_TABELA_PRECO.TP_SUBTIPO_TABELA_PRECO", "TP_SUBTIPO_TABELA_PRECO");
		sql.addProjection("REPLACE(REPLACE(SEGMENTO_MERCADO.DS_SEGMENTO_MERCADO_I, '¦'), 'pt_BR»')", "DS_SEGMENTO_MERCADO_I");
		sql.addProjection("TABELA_DIVISAO_CLIENTE.NR_FATOR_DENSIDADE", "NR_FATOR_DENSIDADE");
		sql.addProjection("REPLACE(REPLACE(VALOR_DOMINIO.DS_VALOR_DOMINIO_I, '¦'), 'pt_BR»')", "PESO_BASE_PARA_CALCULO");
		sql.addProjection("TABELA_DIVISAO_CLIENTE.NR_FATOR_CUBAGEM", "NR_FATOR_CUBAGEM");
		sql.addProjection("TABELA_DIVISAO_CLIENTE.BL_OBRIGA_DIMENSOES", "BL_OBRIGA_DIMENSOES");
		sql.addProjection("DIVISAO_CLIENTE.TP_SITUACAO", "TP_SITUACAO");
		sql.addProjection("CLIENTE.ID_CLIENTE", "ID_CLIENTE");
		sql.addProjection("CLIENTE.BL_UTILIZA_PESO_EDI", "BL_UTILIZA_PESO_EDI");
		sql.addProjection("CLIENTE.BL_PALETE_FECHADO", "BL_PALETE_FECHADO");
		sql.addProjection("CLIENTE.BL_LIBERA_ETIQUETA_EDI", "BL_LIBERA_ETIQUETA_EDI");
		sql.addProjection("CLIENTE.BL_PESAGEM_OPCIONAL", "BL_PESAGEM_OPCIONAL");
		
		String clienteComEdi = criteria.getString("clienteComEdi");
		if("N".equals(clienteComEdi)){
			sql.addProjection("'N'", "CLIENTE_COM_EDI");
		} else {
			sql.addProjection("CASE WHEN CLIENTE.ID_CLIENTE = CLIENTE_EDI_FILIAL_EMBARC.CLIE_PESS_ID_PESSOA THEN 'S' ELSE 'N' END as CLIENTE_COM_EDI");
		}
		
		String freteTonelada = criteria.getString("freteTonelada");
		if (freteTonelada == null || "T".equals(freteTonelada)) {
			/*
			 * Apresentar Sim se os registros encontrados em
			 * PARAMETRO_CLIENTE.PC_FRETE_PERCENTUAL > 0 e
			 * PARAMETRO_CLIENTE.PS_FRETE_PERCENTUAL = 0 e PARAMETRO_CLIENTE.
			 * DT_VIGENCIA_INICIAL <= SYSDATE e
			 * PARAMETRO_CLIENTE.DT_VIGENCIA_FINAL >= SYSDATE, para
			 * PARAMETRO_CLIENTE.ID_TABELA_DIVISAO_CLIENTE =
			 * TABELA_DIVISAO_CLIENTE. ID_TABELA_DIVISAO_CLIENTE e
			 * PARAMETRO_CLIENTE.ID_TABELA_PRECO = TABELA_PRECO.ID_TABELA_PRECO,
			 * caso contrário apresentar Não
			 */
			sql.addProjection("DECODE((SELECT COUNT(*) \n" 
					+ " 				FROM PARAMETRO_CLIENTE pc \n"
					+ " 				WHERE pc.id_tabela_divisao_cliente = TABELA_DIVISAO_CLIENTE.id_tabela_divisao_cliente \n" 
					+ " 				AND pc.id_tabela_preco = TABELA_PRECO.id_tabela_preco \n"
					+ " 				AND pc.dt_vigencia_inicial <= sysdate and pc.dt_vigencia_final >= sysdate \n" 
					+ " 				AND pc.pc_frete_percentual > 0 \n"
					+ " 				AND pc.ps_frete_percentual = 0), 0, 'N','S') TAB_PERC_SEM_FRETE_TON \n");
		} else if ("S".equals(freteTonelada)) {
			sql.addProjection("'S'", "TAB_PERC_SEM_FRETE_TON");
		} else if ("N".equals(freteTonelada)) {
			sql.addProjection("'N'", "TAB_PERC_SEM_FRETE_TON");
		}
		
		/** FROM */
		sql.addFrom("REGIONAL");
		sql.addFrom("REGIONAL_FILIAL");
		sql.addFrom("FILIAL");
		sql.addFrom("CLIENTE");
		sql.addFrom("PESSOA");
		sql.addFrom("PESSOA", "PESSOA_FILIAL");
		sql.addFrom("DIVISAO_CLIENTE");
		sql.addFrom("TABELA_DIVISAO_CLIENTE");
		sql.addFrom("TABELA_PRECO");
		sql.addFrom("SERVICO");
		sql.addFrom("TIPO_TABELA_PRECO");
		sql.addFrom("SUBTIPO_TABELA_PRECO");
		sql.addFrom("PROMOTOR_CLIENTE");
		sql.addFrom("USUARIO");
		sql.addFrom("SEGMENTO_MERCADO");
		sql.addFrom("DOMINIO");
		sql.addFrom("VALOR_DOMINIO");
		
		if(!"N".equals(clienteComEdi)){
			sql.addFrom("CLIENTE_EDI_FILIAL_EMBARC");
		}
		
		/** JOIN */
		sql.addJoin("REGIONAL_FILIAL.ID_REGIONAL", "REGIONAL.ID_REGIONAL");
		sql.addJoin("FILIAL.ID_FILIAL", "REGIONAL_FILIAL.ID_FILIAL");
		sql.addJoin("FILIAL.ID_FILIAL", "PESSOA_FILIAL.ID_PESSOA");
		sql.addJoin("CLIENTE.ID_FILIAL_ATENDE_COMERCIAL", "FILIAL.ID_FILIAL");
		sql.addJoin("CLIENTE.ID_CLIENTE", "PESSOA.ID_PESSOA");
		sql.addJoin("DIVISAO_CLIENTE.ID_CLIENTE", "CLIENTE.ID_CLIENTE");
		sql.addJoin("TABELA_DIVISAO_CLIENTE.ID_DIVISAO_CLIENTE", "DIVISAO_CLIENTE.ID_DIVISAO_CLIENTE");
		sql.addJoin("TABELA_DIVISAO_CLIENTE.ID_TABELA_PRECO", "TABELA_PRECO.ID_TABELA_PRECO");
		sql.addJoin("TABELA_DIVISAO_CLIENTE.ID_SERVICO", "SERVICO.ID_SERVICO");
		sql.addJoin("TABELA_PRECO.ID_TIPO_TABELA_PRECO", "TIPO_TABELA_PRECO.ID_TIPO_TABELA_PRECO");
		sql.addJoin("TABELA_PRECO.ID_SUBTIPO_TABELA_PRECO", "SUBTIPO_TABELA_PRECO.ID_SUBTIPO_TABELA_PRECO");
		sql.addJoin("PROMOTOR_CLIENTE.ID_CLIENTE", "CLIENTE.ID_CLIENTE");
		sql.addJoin("PROMOTOR_CLIENTE.ID_USUARIO", "USUARIO.ID_USUARIO");
		sql.addJoin("CLIENTE.ID_SEGMENTO_MERCADO", "SEGMENTO_MERCADO.ID_SEGMENTO_MERCADO");
		sql.addJoin("VALOR_DOMINIO.ID_DOMINIO", "DOMINIO.ID_DOMINIO");
		sql.addJoin("CLIENTE.TP_PESO_CALCULO", "VALOR_DOMINIO.VL_VALOR_DOMINIO");
		

		/** WHERE */
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		sql.addCustomCriteria("CLIENTE.TP_CLIENTE IN ('S','F')");
		sql.addCustomCriteria("DOMINIO.NM_DOMINIO  = 'DM_TIPO_PESO_CALCULO'");
		sql.addCustomCriteria("PROMOTOR_CLIENTE.DT_FIM_PROMOTOR IS NULL");
		sql.addCustomCriteria("DIVISAO_CLIENTE.TP_SITUACAO = 'A'");
		sql.addCriteria("REGIONAL_FILIAL.DT_VIGENCIA_INICIAL", "<=", dataAtual, YearMonthDay.class);
		sql.addCriteria("REGIONAL_FILIAL.DT_VIGENCIA_FINAL", ">=", dataAtual, YearMonthDay.class);
				
		Long idRegional = criteria.getLong("idRegional");
		if (idRegional != null) {
			sql.addCriteria("REGIONAL.ID_REGIONAL", "=", idRegional);
			sql.addFilterSummary("regional",  criteria.getString("dsRegional"));
		}
		
		Long idFilial = criteria.getLong("idFilial");
		if(idFilial != null){
			sql.addCriteria("FILIAL.ID_FILIAL", "=", idFilial); 
			sql.addFilterSummary("filial", criteria.getString("sgFilial"));
		}
		
		String tipoModal = criteria.getString("tipoModal");
		if(tipoModal != null){
			sql.addCriteria("SERVICO.TP_MODAL", "=", tipoModal); 
			sql.addFilterSummary("modal", criteria.getString("dsModal"));
		}
		
		String pesoBaseParaCalculo = criteria.getString("pesoBaseParaCalculo");
		if(pesoBaseParaCalculo != null && !"T".equals(pesoBaseParaCalculo)){
			if("N".equals(pesoBaseParaCalculo)){
				sql.addCustomCriteria("CLIENTE.TP_PESO_CALCULO IN ('C', 'F', 'D')");
			} else{ 
				sql.addCriteria("CLIENTE.TP_PESO_CALCULO", "=", pesoBaseParaCalculo);
			}
			sql.addFilterSummary("tipoPesoCalculo", criteria.getString("dsPesoBaseParaCalculo"));
		}
		
		if(freteTonelada != null && !"T".equals(freteTonelada)){
			if("S".equals(freteTonelada)){
				sql.addCustomCriteria("EXISTS (SELECT 1 FROM PARAMETRO_CLIENTE pc2 \n"
						+ "WHERE pc2.id_tabela_divisao_cliente = TABELA_DIVISAO_CLIENTE.id_tabela_divisao_cliente \n"
						+ "AND pc2.id_tabela_preco = TABELA_PRECO.id_tabela_preco \n"
						+ "AND pc2.dt_vigencia_inicial <= sysdate  \n"
						+ "AND pc2.dt_vigencia_final >= sysdate \n"
						+ "AND pc2.pc_frete_percentual > 0 \n"
						+ "AND pc2.ps_frete_percentual = 0)");
				sql.addFilterSummary("tabelaPercentualSemFreteTonelada", "Sim");
			} else if("N".equals(freteTonelada)){
				sql.addCustomCriteria("NOT EXISTS (SELECT 1 FROM PARAMETRO_CLIENTE pc2 \n"
						+ "WHERE pc2.id_tabela_divisao_cliente = TABELA_DIVISAO_CLIENTE.id_tabela_divisao_cliente \n"
						+ "AND pc2.id_tabela_preco = TABELA_PRECO.id_tabela_preco \n"
						+ "AND pc2.dt_vigencia_inicial <= sysdate  \n"
						+ "AND pc2.dt_vigencia_final >= sysdate \n"
						+ "AND pc2.pc_frete_percentual > 0 \n"
						+ "AND pc2.ps_frete_percentual = 0)");
				sql.addFilterSummary("tabelaPercentualSemFreteTonelada", "Não");
			}
		}
		
		String paleteFechado = criteria.getString("paleteFechado");
		if(paleteFechado != null && !"T".equals(paleteFechado)){
			if("S".equals(paleteFechado)){
				sql.addCriteria("CLIENTE.BL_PALETE_FECHADO", "=", "S");
				sql.addFilterSummary("paleteFechado", "Sim");
			} else if("N".equals(paleteFechado)){
				sql.addCustomCriteria("(CLIENTE.BL_PALETE_FECHADO = 'N' OR CLIENTE.BL_PALETE_FECHADO IS NULL)");
				sql.addFilterSummary("paleteFechado", "Não");
			}
		}
		
		if(clienteComEdi != null && !"T".equals(clienteComEdi)){
			if("S".equals(clienteComEdi)){
				sql.addJoin("CLIENTE.ID_CLIENTE", "CLIENTE_EDI_FILIAL_EMBARC.CLIE_PESS_ID_PESSOA");
				sql.addFilterSummary("clienteComEdi", "Sim");
			} else if("N".equals(clienteComEdi)){
				sql.addCustomCriteria("NOT EXISTS (SELECT 1 FROM CLIENTE_EDI_FILIAL_EMBARC CEFE WHERE CEFE.CLIE_PESS_ID_PESSOA = CLIENTE.ID_CLIENTE)");
				sql.addFilterSummary("clienteComEdi", "Não");
			}
		} else {
			/*
			 * Realiza um LEFT JOIN.
			 */
			sql.addJoin("CLIENTE.ID_CLIENTE", "CLIENTE_EDI_FILIAL_EMBARC.CLIE_PESS_ID_PESSOA (+)");
		}
		
		String liberaEmbarqueSemLabeling = criteria.getString("liberaEmbarqueSemLabeling");
		if(liberaEmbarqueSemLabeling != null && !"T".equals(liberaEmbarqueSemLabeling)){
			if("S".equals(liberaEmbarqueSemLabeling)){
				sql.addCriteria("CLIENTE.BL_LIBERA_ETIQUETA_EDI", "=", "S");
				sql.addFilterSummary("liberaEtiquetaEdi", "Sim");
			} else if("N".equals(liberaEmbarqueSemLabeling)){
				sql.addCustomCriteria("(CLIENTE.BL_LIBERA_ETIQUETA_EDI = 'N' OR CLIENTE.BL_LIBERA_ETIQUETA_EDI IS NULL)"); 
				sql.addFilterSummary("liberaEtiquetaEdi", "Não");
			}
		}

		String utilizaPesoInformadoEdi = criteria.getString("utilizaPesoInformadoEdi");
		if(utilizaPesoInformadoEdi != null && !"T".equals(utilizaPesoInformadoEdi)){
			if("S".equals(utilizaPesoInformadoEdi)){
				sql.addCriteria("CLIENTE.BL_UTILIZA_PESO_EDI", "=", "S");
				sql.addFilterSummary("utilizaPesoInformadoEdi", "Sim");
			} else if("N".equals(utilizaPesoInformadoEdi)){
				sql.addCustomCriteria("(CLIENTE.BL_UTILIZA_PESO_EDI = 'N' OR CLIENTE.BL_UTILIZA_PESO_EDI IS NULL)");
				sql.addFilterSummary("utilizaPesoInformadoEdi", "Não");
			}
		}
		
		String pesagemOpcional = criteria.getString("pesagemOpcional");
		if(pesagemOpcional != null && !"T".equals(pesagemOpcional)){
			if("S".equals(pesagemOpcional)){
				sql.addCriteria("CLIENTE.BL_PESAGEM_OPCIONAL", "=", "S");
				sql.addFilterSummary("pesagemOpcional", "Sim");
			} else if("N".equals(pesagemOpcional)){
				sql.addCustomCriteria("(CLIENTE.BL_PESAGEM_OPCIONAL = 'N' OR CLIENTE.BL_PESAGEM_OPCIONAL IS NULL)");
				sql.addFilterSummary("pesagemOpcional", "Não");
			}
		}
		
		String fatorCubagem = criteria.getString("fatorCubagem");
		if (fatorCubagem != null && !"T".equals(fatorCubagem)) {

			if (tipoModal != null) {
				if ("A".equals(tipoModal)) {
					if("P".equals(fatorCubagem)){
						sql.addCustomCriteria("(TABELA_DIVISAO_CLIENTE.NR_FATOR_CUBAGEM = 167 or TABELA_DIVISAO_CLIENTE.NR_FATOR_CUBAGEM is null)");
					} else if("D".equals(fatorCubagem)){
						sql.addCriteria("TABELA_DIVISAO_CLIENTE.NR_FATOR_CUBAGEM", "<>", "167");
					}
				} else if ("R".equals(tipoModal)) {
					if("P".equals(fatorCubagem)){
						sql.addCustomCriteria("(TABELA_DIVISAO_CLIENTE.NR_FATOR_CUBAGEM = 300 or TABELA_DIVISAO_CLIENTE.NR_FATOR_CUBAGEM is null)");
					} else if("D".equals(fatorCubagem)){
						sql.addCriteria("TABELA_DIVISAO_CLIENTE.NR_FATOR_CUBAGEM", "<>", "300");
					}
				}
				
				sql.addFilterSummary("fatorCubagem", criteria.get("dsFatorCubagem"));
			}
		}
		
		String fatorDensidade = criteria.getString("fatorDensidade");
		if(fatorDensidade != null && !"T".equals(fatorDensidade)){
			if("S".equals(fatorDensidade)){
				sql.addCustomCriteria("TABELA_DIVISAO_CLIENTE.NR_FATOR_DENSIDADE IS NOT NULL");
				sql.addFilterSummary("fatorDensidade", "Sim");
			} else if("N".equals(fatorDensidade)){
				sql.addCustomCriteria("TABELA_DIVISAO_CLIENTE.NR_FATOR_DENSIDADE IS NULL");
				sql.addFilterSummary("fatorDensidade", "Não");
			}
		}
		
		String cubagemObrigatoria = criteria.getString("cubagemObrigatoria");
		if(cubagemObrigatoria != null && !"T".equals(cubagemObrigatoria)){
			if("S".equals(cubagemObrigatoria)){
				sql.addCriteria("TABELA_DIVISAO_CLIENTE.BL_OBRIGA_DIMENSOES", "=", "S");
				sql.addFilterSummary("cubagemObrigatoria", "Sim");
			} else if("N".equals(cubagemObrigatoria)){
				sql.addCustomCriteria("(TABELA_DIVISAO_CLIENTE.BL_OBRIGA_DIMENSOES = 'N' OR TABELA_DIVISAO_CLIENTE.BL_OBRIGA_DIMENSOES IS NULL)");
				sql.addFilterSummary("cubagemObrigatoria", "Não");
			}
		}
		
		/** ORDER BY */
		sql.addOrderBy("REGIONAL.SG_REGIONAL");
		sql.addOrderBy("FILIAL.SG_FILIAL");
		sql.addOrderBy("PESSOA.NM_PESSOA");

		return sql;
	}
}
