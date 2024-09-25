package com.mercurio.lms.portaria.model.dao.suggestion;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.lms.portaria.model.service.utils.ConstantesFrotaPlacaChegadaSaidaSuggest;
import com.mercurio.lms.util.model.dao.AbstractSQLBuilder;

public class SaidaColetaEntregaSQLBuilder extends AbstractSQLBuilder {
	
	private static final String SITUACAO_CONTROLE_CARGA_AGUARDANDO_SAIDA_PARA_ENTREGA= "AE";
	
	private Long idFilial;
	private String placaOuFrota;
	
	public SaidaColetaEntregaSQLBuilder(Long idFilial, String placaOuFrota) {
		this.idFilial = idFilial;
		this.placaOuFrota = placaOuFrota;
	}

	@Override
	protected String getSQL() {
		StringBuilder controleCargaSQL = new StringBuilder();
		controleCargaSQL.append("SELECT ")
						.append("CC.ID_CONTROLE_CARGA as idControleCarga, ")
						.append("' - ' || FIO.SG_FILIAL || ' ' || lpad(CC.NR_CONTROLE_CARGA, 8, '0') as dsControleCarga, ")
						.append("CC.ID_CONTROLE_CARGA || '|C' as idControle, ")
						.append("NULL as idOrdemSaida, ")
						.append("MT.NR_FROTA as nrFrota, ")
					    .append("MT.NR_IDENTIFICADOR as nrIdentificador, ")
					    .append("MT.ID_MEIO_TRANSPORTE as idMeioTransporte, ")
					    .append(ConstantesFrotaPlacaChegadaSaidaSuggest.TIPO_SAIDA_SQL_STRING + " as tipo, ")
					    .append(ConstantesFrotaPlacaChegadaSaidaSuggest.TIPO_SAIDA_LABEL_SQL_STRING + " as tipoLabel, ")
					    .append(ConstantesFrotaPlacaChegadaSaidaSuggest.SUBTIPO_COLETA_ENTREGA_SQL_STRING + " as subTipo, ")
					    .append(ConstantesFrotaPlacaChegadaSaidaSuggest.SUBTIPO_COLETA_ENTREGA_LABEL_SQL_STRING + " as subTipoLabel ")					    
					    .append("FROM CONTROLE_CARGA CC, ")
					    .append("MEIO_TRANSPORTE MT, ")
					    .append("FILIAL FIO " )
					    .append("WHERE 1 = 1 ")
					    .append("AND CC.ID_FILIAL_ORIGEM = FIO.ID_FILIAL ")
					    .append("AND CC.ID_TRANSPORTADO = MT.ID_MEIO_TRANSPORTE ")
					    .append("AND CC.TP_STATUS_CONTROLE_CARGA = :tpStatusControleCargaSaidaCE ")
					    .append("AND CC.ID_FILIAL_ATUALIZA_STATUS = :idFilial ")
						.append("AND (LOWER(MT.NR_FROTA) LIKE LOWER(:placaOuFrota || '%') OR LOWER(MT.NR_IDENTIFICADOR) LIKE LOWER(:placaOuFrota || '%')) ");
		
		StringBuilder ordemSaidaSQL = new StringBuilder();
		ordemSaidaSQL.append("SELECT ")
					 .append("NULL as idControleCarga, ")
					 .append("NULL as dsControleCarga, ")
					 .append("OS.ID_ORDEM_SAIDA || '|O' as idControle, ")
					 .append("OS.ID_ORDEM_SAIDA as idOrdemSaida, ")
					 .append("MT.NR_FROTA as nrFrota, ")
				     .append("MT.NR_IDENTIFICADOR as nrIdentificador, ")
				     .append("MT.ID_MEIO_TRANSPORTE as idMeioTransporte, ")
				     .append(ConstantesFrotaPlacaChegadaSaidaSuggest.TIPO_SAIDA_SQL_STRING + " as tipo, ")
				     .append(ConstantesFrotaPlacaChegadaSaidaSuggest.TIPO_SAIDA_LABEL_SQL_STRING + " as tipoLabel, ")
				     .append(ConstantesFrotaPlacaChegadaSaidaSuggest.SUBTIPO_ORDEM_SAIDA_SQL_STRING + " as subTipo, ")
				     .append(ConstantesFrotaPlacaChegadaSaidaSuggest.SUBTIPO_ORDEM_SAIDA_LABEL_SQL_STRING + " as subTipoLabel ")				     
				     .append("FROM ORDEM_SAIDA OS, ")
				     .append("MEIO_TRANSPORTE_RODOVIARIO MTR, ")
				     .append("MEIO_TRANSPORTE MT ")
				     .append("WHERE 1=1 ")
				     .append("AND OS.ID_MEIO_TRANSPORTE = MTR.ID_MEIO_TRANSPORTE ")
				     .append("AND MTR.ID_MEIO_TRANSPORTE = MT.ID_MEIO_TRANSPORTE ")
				     .append("AND OS.DH_SAIDA IS NULL ")
				     .append("AND OS.ID_FILIAL_ORIGEM = :idFilial ")
					 .append("AND (LOWER(MT.NR_FROTA) LIKE LOWER(:placaOuFrota || '%') OR LOWER(MT.NR_IDENTIFICADOR) LIKE LOWER(:placaOuFrota || '%')) ");
		
		StringBuilder saidaColetaEntregaSQL = new StringBuilder();
		saidaColetaEntregaSQL.append(controleCargaSQL)
							.append(" UNION ")
							.append(ordemSaidaSQL);
		
		return saidaColetaEntregaSQL.toString();
	}

	@Override
	protected Map<String, Object> getParametros() {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idFilial", idFilial);
		parametros.put("placaOuFrota", placaOuFrota);
		parametros.put("tpStatusControleCargaSaidaCE", SITUACAO_CONTROLE_CARGA_AGUARDANDO_SAIDA_PARA_ENTREGA);
		return parametros;
	}

}
