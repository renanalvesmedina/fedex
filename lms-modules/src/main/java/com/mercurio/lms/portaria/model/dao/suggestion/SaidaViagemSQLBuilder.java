package com.mercurio.lms.portaria.model.dao.suggestion;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.lms.portaria.model.service.utils.ConstantesFrotaPlacaChegadaSaidaSuggest;
import com.mercurio.lms.util.model.dao.AbstractSQLBuilder;

public class SaidaViagemSQLBuilder extends AbstractSQLBuilder {
	
	private static final String SITUACAO_CONTROLE_CARGA_AGUARDANDO_SAIDA_PARA_VIAGEM = "AV";
	private static final String BL_TRECHO_DIRETO_S = "S";
	
	private Long idFilial;
	private String placaOuFrota;
	
	public SaidaViagemSQLBuilder(Long idFilial, String placaOuFrota) {
		this.idFilial = idFilial;
		this.placaOuFrota = placaOuFrota;
	}
	
	@Override
	protected String getSQL() {
		StringBuilder saidaViagemSQL = new StringBuilder();
		saidaViagemSQL.append("SELECT ")
		   .append("CC.ID_CONTROLE_CARGA as idControleCarga, ")
		   .append("' - ' || FIO.SG_FILIAL || ' ' || lpad(CC.NR_CONTROLE_CARGA, 8, '0') as dsControleCarga, ")
		   .append("CC.ID_CONTROLE_CARGA || '|V' as idControle, ")
		   .append("NULL as idOrdemSaida, ")
		   .append("MT.NR_FROTA as nrFrota, ")
		   .append("MT.NR_IDENTIFICADOR as nrIdentificador, ")
		   .append("MT.ID_MEIO_TRANSPORTE as idMeioTransporte, ")
		   .append(ConstantesFrotaPlacaChegadaSaidaSuggest.TIPO_SAIDA_SQL_STRING + " as tipo, ")
		   .append(ConstantesFrotaPlacaChegadaSaidaSuggest.TIPO_SAIDA_LABEL_SQL_STRING + " as tipoLabel, ")
		   .append(ConstantesFrotaPlacaChegadaSaidaSuggest.SUBTIPO_VIAGEM_SQL_STRING + " as subTipo, ")
		   .append(ConstantesFrotaPlacaChegadaSaidaSuggest.SUBTIPO_VIAGEM_LABEL_SQL_STRING + " as subTipoLabel ")
		   .append("FROM CONTROLE_CARGA CC, ")
		   .append("MEIO_TRANSPORTE MT, ")
		   .append("CONTROLE_TRECHO CT, " )
		   .append("FILIAL FIO " )
		   .append("WHERE 1=1 ")
		   .append("AND CC.ID_FILIAL_ORIGEM = FIO.ID_FILIAL ")
		   .append("AND CC.ID_TRANSPORTADO = MT.ID_MEIO_TRANSPORTE ")
		   .append("AND CC.ID_CONTROLE_CARGA = CT.ID_CONTROLE_CARGA ")
		   .append("AND CT.ID_FILIAL_ORIGEM = CC.ID_FILIAL_ATUALIZA_STATUS ")
		   .append("AND CC.TP_STATUS_CONTROLE_CARGA = :tpStatusControleCargaSaidaViagem ")
		   .append("AND CT.BL_TRECHO_DIRETO = :blTrechoDiretoSaidaViagem ")
		   .append("AND CC.ID_FILIAL_ATUALIZA_STATUS = :idFilial " )
		   .append("AND (LOWER(MT.NR_FROTA) LIKE LOWER(:placaOuFrota || '%') OR LOWER(MT.NR_IDENTIFICADOR) LIKE LOWER(:placaOuFrota || '%')) ")
		   .append("AND (EXISTS")
		   .append("(SELECT ID_MANIFESTO ")
		   .append("FROM MANIFESTO ")
		   .append("WHERE CC.ID_CONTROLE_CARGA = MANIFESTO.ID_CONTROLE_CARGA")
		   .append("))");
		return saidaViagemSQL.toString();
	}

	@Override
	protected Map<String, Object> getParametros() {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("tpStatusControleCargaSaidaViagem", SITUACAO_CONTROLE_CARGA_AGUARDANDO_SAIDA_PARA_VIAGEM);
		parametros.put("blTrechoDiretoSaidaViagem", BL_TRECHO_DIRETO_S);
		parametros.put("idFilial", idFilial);
		parametros.put("placaOuFrota", placaOuFrota);
		return parametros;
	}

}
