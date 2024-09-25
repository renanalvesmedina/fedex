package com.mercurio.lms.contasreceber.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.jdbc.core.RowMapper;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.Recibo;
import com.mercurio.lms.contasreceber.model.service.ReciboService;
import com.mercurio.lms.util.MoedaUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Hector Julian Esnaola Junior
 *
 * @spring.bean id="lms.contasreceber.emitirReciboFreteService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirReciboFrete.jasper"
 */
public class EmitirReciboFreteService extends ReportServiceSupport {

	/** Set reciboService - (Inversion of Control) */
	private ReciboService reciboService;
	public void setReciboService(ReciboService reciboService){
		this.reciboService = reciboService;
	}
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		/** Faz um Cast do Map contendo os parâmetros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		/** Instância a classe SqlTemplate, que retorna o sql para geração do relatório */
		SqlTemplate sql = getSqlTemplate(tfm);
		
		Long idRecibo = tfm.getLong("recibo.idRecibo");
		
		//Se o id Recibo veio nulo pegar com esta chave que vem da tela de recibo
		if (idRecibo == null){
			idRecibo = tfm.getLong("idRecibo");
		}
		
		/** Itera o resultSet para atualizar os recibos */
		List lst  = this.iteratorResultSetReport(sql, tfm.getLong("recibo.idRecibo"));

		JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(lst);
		
		JRReportDataObject jr = createReportDataObject(jrMap, parameters);
		
		Map parametersReport = new HashMap();
		
		/** Adiciona os parâmetros de pesquisa no Map */
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		
		/** Adiciona o usuário no Map */
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		
		/** Seta o parâmetro de tipo de arquivo a ser gerado */
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));		
		
		jr.setParameters(parametersReport);
		
		return jr;
	}
	
	/**
	 * Monta o sql principal
	 * @param tfm
	 * @return sql
	 * @throws Exception
	 */		
	private SqlTemplate getSqlTemplate(TypedFlatMap tfm) throws Exception{
		
		SqlTemplate sql = createSqlTemplate();
		Long idRecibo = tfm.getLong("recibo.idRecibo");
		Long idManifesto = tfm.getLong("manifestoEntrega.idManifestoEntrega");
		if (idManifesto == null) {
			idManifesto = tfm.getLong("idManifestoEntrega");
		}
		
		if(idManifesto == null){
			sql.addProjection("distinct rec.id_recibo", "RECIBO");
			sql.addProjection("rec.nr_recibo", "NR_RECIBO");
		} else {
			sql.addProjection("rec.id_recibo", "RECIBO");
			sql.addProjection("fildoc.sg_filial");
			sql.addProjection("ds.nr_docto_servico");
		}
		
		sql.addFrom("recibo rec " +
				"inner join filial filrec on rec.id_filial_emissora = filrec.id_filial " +
				"inner join pessoa p on filrec.id_filial = p.id_pessoa " +
				"inner join endereco_pessoa ep on p.id_endereco_pessoa = ep.id_endereco_pessoa " +
				"inner join fatura_recibo fr on fr.id_recibo = rec.id_recibo " +
				"inner join fatura fat on fat.id_fatura = fr.id_fatura  and fat.tp_abrangencia = 'N' " + 
				"inner join filial filfat on filfat.id_filial = fat.id_filial " +
				"left join manifesto_entrega me on me.id_manifesto_entrega = fat.id_manifesto_entrega " +
				"left join filial filme on filme.id_filial = me.id_filial " +
				"inner join item_fatura if on if.id_fatura = fat.id_fatura and fat.tp_situacao_fatura <> 'CA' " +
				"inner join devedor_doc_serv_fat dev on dev.id_devedor_doc_serv_fat = if.id_devedor_doc_serv_fat " +
				"inner join docto_servico ds on  ds.id_docto_servico = dev.id_docto_servico " +
				"inner join filial fildoc on   fildoc.id_filial = ds.id_filial_origem " +
				"inner join cliente c on c.id_cliente = fat.id_cliente " +
				"inner join pessoa pc on pc.id_pessoa = c.id_cliente " +
				"inner join endereco_pessoa epc on epc.id_endereco_pessoa = pc.id_endereco_pessoa " +
				"inner join tipo_logradouro tl on tl.id_tipo_logradouro = epc.id_tipo_logradouro " +
				"inner join municipio m on m.id_municipio = epc.id_municipio " +
				"inner join unidade_federativa uf on uf.id_unidade_federativa = m.id_unidade_federativa"); 
		
		//Criterio que vem da tela de manter recibo
		if(idRecibo == null) {
			idRecibo = tfm.getLong("idRecibo");
		}
		
		// Caso não seja informado um recibo
		if(idRecibo == null && idManifesto == null)
			sql.addCriteria("rec.tp_situacao_recibo", "=", "D");
		
		sql.addCriteria("rec.tp_situacao_recibo", "<>", "C");
		
		sql.addCriteria("rec.id_recibo", "=", idRecibo);
		sql.addCriteria("filrec.id_filial", "=", tfm.getLong("filialByIdFilialEmissora.idFilial"));
		
		// Caso seja informado um manifesto 
		if(idManifesto != null){
			sql.addCriteria("filme.id_filial", "=", tfm.getLong("manifesto.filial.idFilial"));
			sql.addCriteria("me.id_manifesto_entrega", "=", idManifesto);
			
			sql.addOrderBy("fildoc.sg_filial, ds.nr_docto_servico");
		}
		
		sql.addOrderBy("rec.nr_recibo");
		
		return sql;

	}
	
	/**
	 * Método responsável por iterar os recibos e setar a situação para emitido 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 28/08/2006
	 *
	 * @param sql
	 * @return
	 *
	 */
	private List iteratorResultSetReport(SqlTemplate sql, final Long idReciboTela){
		
		List lst = getJdbcTemplate().query(sql.getSql(), sql.getCriteria(), new RowMapper(){
			
			Map map;
			
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			
				map = new HashMap();
				Long idRecibo = Long.valueOf(rs.getLong("RECIBO"));
				map.put("RECIBO", idRecibo);
				
				/** Caso não seja informado um recibo */
				if(idReciboTela == null){
					/** Edita o recibo passando sua situação para Emitido */
					Recibo recibo = reciboService.findById(idRecibo);
					// Alterar a situação para emitido apenas quando estiver digitado
					if (recibo.getTpSituacaoRecibo().getValue().equals("D")) {
						recibo.setTpSituacaoRecibo(new DomainValue("E"));
						reciboService.storeBasic(recibo);
					}
				}
				
				return map;
			}
			
		});
		
		return lst;
	}
	
	/**
	 * Método invocado pelo subrelatório
	 * @param parameters
	 * @return JRDataSource
	 * @throws Exception
	 */
	public JRDataSource executeSubRelatorioReciboFrete(Object[] parameters) throws Exception {
        
		/** Resgata o id do Recibo que é passado por parâmetro do relatório pai */
        Long idRecibo = (Long)parameters[0];
        
        SqlTemplate sql = createSqlTemplate(); 
		
        sql.addProjection(" (filfat.sg_filial || ' ' || to_char(fat.nr_fatura, '0000000000')) as FATURA," +
				 
        		  " ( " +
				  "     select " +
				  "         fildoc.sg_filial " +
				  "     from " +
				  "			item_fatura iftmp " +
				  "         inner join fatura ftmp on ftmp.id_fatura = iftmp.id_fatura " +
				  "         inner join devedor_doc_serv_fat dev on dev.id_devedor_doc_serv_fat = iftmp.id_devedor_doc_serv_fat " +
				  "         inner join docto_servico ds on  ds.id_docto_servico = dev.id_docto_servico " +
				  "         inner join filial fildoc on   fildoc.id_filial = ds.id_filial_origem " +
				  "     where " +
				  "			ftmp.qt_documentos = 1 " +
				  "         and iftmp.id_fatura = fat.id_fatura " +
				  " ) as FILIAL_DOCTO_SERVICO, " +
				 
				  " ( " +
				  "     select " +
				  "         ds.nr_docto_servico " +
				  "     from " +
				  "			item_fatura iftmp " +
				  "         inner join fatura ftmp on ftmp.id_fatura = iftmp.id_fatura " +
				  "         inner join devedor_doc_serv_fat dev on dev.id_devedor_doc_serv_fat = iftmp.id_devedor_doc_serv_fat " +
				  "         inner join docto_servico ds on  ds.id_docto_servico = dev.id_docto_servico " +
				  "         inner join filial fildoc on   fildoc.id_filial = ds.id_filial_origem " +
				  "     where " +
				  "			ftmp.qt_documentos = 1 " +
				  "         and iftmp.id_fatura = fat.id_fatura " +
				  " ) as NR_DOCTO_SERVICO, " +
				  
				  " ( " +
				  "     select " +
				  PropertyVarcharI18nProjection.createProjection("vd.ds_valor_dominio_i") +
				  "     from " +
				  "			item_fatura iftmp " +
				  "         inner join fatura ftmp on ftmp.id_fatura = iftmp.id_fatura " +
				  "         inner join devedor_doc_serv_fat dev on dev.id_devedor_doc_serv_fat = iftmp.id_devedor_doc_serv_fat " +
				  "         inner join docto_servico ds on  ds.id_docto_servico = dev.id_docto_servico " +
				  "         inner join filial fildoc on   fildoc.id_filial = ds.id_filial_origem " +
				  "			inner join valor_dominio vd on lower(vd.vl_valor_dominio) = lower(ds.tp_documento_servico) " +
				  "			inner join dominio d on vd.id_dominio = d.id_dominio and lower(d.nm_dominio) = lower('DM_TIPO_DOCUMENTO_SERVICO') " +
				  "     where " +
				  "			ftmp.qt_documentos = 1 " +
				  "         and iftmp.id_fatura = fat.id_fatura " +
				  " ) as TP_DOCTO_SERVICO, " +
				  
				  " fat.vl_total as VALOR_FATURA, " +
				  " fat.vl_desconto as VALOR_DESCONTO, " +
				  " fr.vl_juro_recebido as VALOR_JURO, " +
				  " fr.vl_cobrado as TOTAL_RECEBIDO, " +
				  " rec.id_recibo as RECIBO, " +
				  " p.nm_fantasia as CIDADEFILIAL, " +
				  " ( " +
				  "	  select ( '(' || te.nr_ddd || ') ' || te.nr_telefone) " +
				  "   from telefone_endereco te " +
				  "   where te.id_telefone_endereco = " +
				  "            ( " +
				  "                  NVL(" +
				  "						(select max(tetmp.id_telefone_endereco) " +
				  "                 		from telefone_endereco tetmp " +
				  "                 		where tetmp.id_pessoa = p.id_pessoa and tetmp.tp_telefone='C')," +
				  "						( select max(tetmp.id_telefone_endereco)"+                  
                  " 						from telefone_endereco tetmp      "+            
                  "	 						where tetmp.id_pessoa = p.id_pessoa  )) " +
				  "            ) " +
				  " ) as FONEFILIAL, " +
				  
				  " rec.dt_emissao as EMISSAO, " +
				  " (filRec.SG_FILIAL||' '||to_char(rec.nr_recibo, '0000000000')) as NUMERO, " +
				  " rec.ob_recibo as OBS, " +
				  " me.nr_manifesto_entrega as MANIFESTO, " +
				  " filMan.SG_FILIAL as MAN_SG_FILIAL, " +
				  " rec.vl_total_recibo as VALOR, " +
				  " pc.nm_pessoa as NOMECLIENTE, " +
				  " pc.tp_identificacao as TP_IDENTIFICACAO, " +
				  " pc.nr_identificacao as NR_IDENTIFICACAO, " +
				  " ("+PropertyVarcharI18nProjection.createProjection("tl.ds_tipo_logradouro_i") + " || ' ' || epc.ds_endereco || ', ' || epc.nr_endereco || DECODE(epc.ds_complemento, '', '', ' - ' || epc.ds_complemento)) as ENDERECOCLIENTE, " +
				  " m.nm_municipio as MUNICIPIO, " +
        		  " uf.sg_unidade_federativa as UF");

		
        sql.addFrom("recibo rec " +
					"inner join filial filrec on rec.id_filial_emissora = filrec.id_filial " +
					"inner join pessoa p on filrec.id_filial = p.id_pessoa " +
					"inner join endereco_pessoa ep on p.id_endereco_pessoa = ep.id_endereco_pessoa " +
					"inner join fatura_recibo fr on fr.id_recibo = rec.id_recibo " +
					"inner join fatura fat on fat.id_fatura = fr.id_fatura  and fat.tp_abrangencia = 'N' " + // -- and fat.tp_abrangencia = 'N' 
					"inner join filial filfat on filfat.id_filial = fat.id_filial " +
					"left join manifesto_entrega me on me.id_manifesto_entrega = fat.id_manifesto_entrega " +
					"left join filial filMan on filMan.ID_FILIAL = me.ID_FILIAL " +
					"inner join cliente c on c.id_cliente = fat.id_cliente " +
					"inner join pessoa pc on pc.id_pessoa = c.id_cliente " +
					"inner join endereco_pessoa epc on epc.id_endereco_pessoa = pc.id_endereco_pessoa " +
					"inner join tipo_logradouro tl on tl.id_tipo_logradouro = epc.id_tipo_logradouro " +
					"inner join municipio m on m.id_municipio = epc.id_municipio " +
					"inner join unidade_federativa uf on uf.id_unidade_federativa = m.id_unidade_federativa"); 
		
		sql.addCriteria("fr.id_recibo", "=", idRecibo);
		sql.addOrderBy("filfat.sg_filial");		
		sql.addOrderBy("fat.nr_fatura");
		long hash = System.currentTimeMillis();
		sql.addCustomCriteria(hash + "=" + hash);
		
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }

	/**
	 * Formata por extenso o valor passado como parâmetro
	 *
	 * @author José Rodrigo Moraes
	 * @since 12/01/2007
	 *
	 * @param valor
	 * @return
	 */
	public static String formataPorExtenso(Number valor){
		return MoedaUtils.formataPorExtenso(valor,SessionUtils.getMoedaSessao()).toLowerCase();
	}
	
}

