package com.mercurio.lms.carregamento.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Classe responsável pela geração de Relatório de Cargas no Terminal (Resumido)
 * Especificação técnica 05.02.01.01
 * 
 * @spring.bean id="lms.carregamento.emitirRelatorioCargasResumidoService"
 * @spring.property name="reportName" value=
 *                  "com/mercurio/lms/carregamento/report/emitirRelatorioCargasResumido.jasper"
 */

public class EmitirRelatorioCargasResumidoService extends
		EmitirRelatorioCargasService {

	/**
     * método responsável por gerar o relatório. 
     */
    public JRReportDataObject execute(Map parameters) throws Exception {
    	TypedFlatMap tfm = (TypedFlatMap)parameters;
    	Long idPaisSessao = SessionUtils.getPaisSessao().getIdPais();

    	StringBuilder sql = new StringBuilder()
    		.append("SELECT * FROM ( ")
    		.append("select ");
    		getDtAgendamentoField(tfm, sql);
    		sql.append("filialDestino.SG_FILIAL as SG_FILIAL_DESTINO, ")
			.append("filialOrigem.ID_FILIAL as ID_FILIAL_ORIGEM, ")
			.append("filialOrigem.SG_FILIAL as SG_FILIAL_ORIGEM, ")
				.append("filialLocalizacao.SG_FILIAL as SG_FILIAL_LOCALIZACAO, ")
				.append(PropertyVarcharI18nProjection.createProjection("localizacoa.DS_LOCALIZACAO_MERCADORIA_I",
						"DS_LOCALIZACAO_MERCADORIA")
						+ ", ")	
			.append("ds.QT_VOLUMES as QT_VOLUMES, ")
			.append("ds.PS_REAL as PS_REAL, ")
			.append("ds.PS_AFORADO as PS_AFORADO, ")

				.append("F_CONV_MOEDA(paisFilialOrigem.ID_PAIS,ds.ID_MOEDA," 
						+ idPaisSessao + "," + tfm.getLong("moeda.idMoeda")
						+ ",?,ds.VL_MERCADORIA) as VL_MERCADORIA, ")
				.append("F_CONV_MOEDA(paisFilialOrigem.ID_PAIS,ds.ID_MOEDA," + idPaisSessao
						+ ","
						+ tfm.getLong("moeda.idMoeda")
						+ ",?,ds.VL_TOTAL_DOC_SERVICO) as VL_TOTAL_DOC_SERVICO, ")

			.append("ds.ID_MOEDA as ID_MOEDA, ")
			.append("( ")
			.append("? - TRUNC(CAST( ")
			.append("(select max(eds.DH_EVENTO) as dtMaior ") 
			.append("from evento_documento_servico eds ")
			.append("inner join evento on (evento.ID_EVENTO = eds.ID_EVENTO) ")
			.append("where ")
			.append("eds.ID_DOCTO_SERVICO = ds.ID_DOCTO_SERVICO ") 
			.append("AND eds.BL_EVENTO_CANCELADO = 'N' ")
			.append("AND evento.ID_LOCALIZACAO_MERCADORIA is not null) ")
			.append("AS DATE)) ")
			.append(") NR_DIAS ")
			.append("from ")
    		.append(getAguardandoAgendamentoTables(tfm))
    		.append(getDocumentosComAgendamentoTables(tfm))
			.append("docto_servico ds ")
			.append("inner join filial filialOrigem on (filialOrigem.ID_FILIAL = ds.ID_FILIAL_ORIGEM) ")
			.append("inner join pessoa pessoaFilialOrigem on (filialOrigem.ID_FILIAL = pessoaFilialOrigem.ID_PESSOA) ")			
			.append("inner join filial filialDestino on (filialDestino.ID_FILIAL = ds.ID_FILIAL_DESTINO) ")
			.append("inner join localizacao_mercadoria lm on (lm.ID_LOCALIZACAO_MERCADORIA = ds.ID_LOCALIZACAO_MERCADORIA) ")
				.append("inner join filial filialLocalizacao on (filialLocalizacao.ID_FILIAL = ds.ID_FILIAL_LOCALIZACAO) ")
				.append("inner join localizacao_mercadoria localizacoa on (localizacoa.ID_LOCALIZACAO_MERCADORIA = ds.ID_LOCALIZACAO_MERCADORIA) ")

			.append("inner join endereco_pessoa enderecoFilialOrigem on (enderecoFilialOrigem.ID_ENDERECO_PESSOA = pessoaFilialOrigem.ID_ENDERECO_PESSOA) ")
			.append("inner join municipio municipioFilialOrigem on (municipioFilialOrigem.ID_MUNICIPIO = enderecoFilialOrigem.ID_MUNICIPIO) ")
			.append("inner join unidade_federativa ufFilialOrigem on (ufFilialOrigem.ID_UNIDADE_FEDERATIVA = municipioFilialOrigem.ID_UNIDADE_FEDERATIVA) ")
			.append("inner join pais paisFilialOrigem on (paisFilialOrigem.ID_PAIS = ufFilialOrigem.ID_PAIS) ")

			.append("left join servico servico on (servico.ID_SERVICO = ds.ID_SERVICO) ")
			.append("left join moeda moeda on (moeda.ID_MOEDA = ds.ID_MOEDA) ")
			.append("left join cliente clienteDestinatario on (clienteDestinatario.ID_CLIENTE = ds.ID_CLIENTE_DESTINATARIO) ")
			.append("left join pessoa pessoaDestinatario on (clienteDestinatario.ID_CLIENTE = pessoaDestinatario.ID_PESSOA) ")
			.append("left join fluxo_filial ff on (ff.ID_FLUXO_FILIAL = ds.ID_FLUXO_FILIAL) ")
			.append("where ")
			.append("ds.TP_DOCUMENTO_SERVICO in ('CRT', 'CTR', 'NFT', 'MDA', 'NTE', 'CTE') ")
			.append("AND ds.NR_DOCTO_SERVICO > 0 ")
    		.append(getAguardandoAgendamentoFilter(tfm))
    		.append(getDocumentosComAgendamentoFilter(tfm));

    	SqlTemplate sqlTemplate = createSqlTemplate();
    	List criterias = new ArrayList();

    	YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();
    	criterias.add(dtAtual);
    	criterias.add(dtAtual);
    	criterias.add(dtAtual);

    	sql.append(populateCriterios(tfm, criterias, sqlTemplate));

		if (("FL").equals(tfm.getString("agrupadoPor"))) {
			sql.append(" order by filialLocalizacao.SG_FILIAL)");
		}else{
    	sql.append(" order by filialDestino.SG_FILIAL ) ");
		}

		populateCriterioDias(sql, criterias, tfm);

		JRReportDataObject jr = executeQuery(sql.toString(),
				criterias.toArray());
		jr.setParameters(getParametrosRelatorio(tfm, sqlTemplate));
		
		if (("xls").equals(tfm.getString("formatoRelatorio"))){
			if (("FD").equals(tfm.getString("agrupadoPor"))) {
				setReportName("com/mercurio/lms/carregamento/report/emitirRelatorioCargasResumidoExcelAgrupadoPorDestino.jasper");
			}else{
				setReportName("com/mercurio/lms/carregamento/report/emitirRelatorioCargasResumidoExcelAgrupadoPorLocalizacao.jasper");
			}
		} else {
			setReportName("com/mercurio/lms/carregamento/report/emitirRelatorioCargasResumido.jasper");
		}			
		
        return jr;
    }
}