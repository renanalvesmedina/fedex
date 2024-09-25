package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.param.LinhaSqlClienteFaturamentoParam;
import com.mercurio.lms.contasreceber.model.param.LinhaSqlFaturamentoParam;
import com.mercurio.lms.contasreceber.model.param.SqlFaturamentoParam;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.faturamentoEspecialService"
 */
public class FaturamentoEspecialService extends FaturamentoService {
	
	protected Integer executeFaturamento(SqlFaturamentoParam param) throws SQLException {
		Integer qtDocumentos = 0;
		
		List lstCliente = getFaturamentoDAO().findCliente(param);
		
		for (Iterator iter = lstCliente.iterator(); iter.hasNext();) {
			LinhaSqlClienteFaturamentoParam linha = (LinhaSqlClienteFaturamentoParam) iter.next();
			
			List lstAgrupador = getFaturamentoDAO().findAgrupamento(linha.getIdFormaAgrupamento());			
			param.setIdCliente(linha.getIdCliente());
			param.setIdDivisaoCliente(linha.getIdDivisaoCliente());
			param.setIdAgrupamentoCliente(linha.getIdAgrupamento());
			param.setIdFormaAgrupamento(linha.getIdFormaAgrupamento());
			param.setLstAgrupador(lstAgrupador);
			
			qtDocumentos += executeSql(param, getSql(param));
		}
		
		return qtDocumentos;
	}

	protected void mountLinhaSqlFaturamento(LinhaSqlFaturamentoParam linha, Object[] element) throws SQLException {
		int qtAgrupador = element.length-16;
		
		Object[] lstAgrupamento = new Object[qtAgrupador];
		
		if (qtAgrupador > 0) { 
			System.arraycopy(element, 16, lstAgrupamento, 0, qtAgrupador);
		}
		
		linha.setLstAgrupamento(lstAgrupamento);
	}
	
	private SqlTemplate getSql(SqlFaturamentoParam param) {
		return new GerarSqlFaturamentoEspecialService().generateSql(param);
	}
	
	protected boolean groupByFatura(LinhaSqlFaturamentoParam linha, LinhaSqlFaturamentoParam linhaAnterior) {
		
		//Por cada agrupamento
		for (int i = 0; i < linha.getLstAgrupamento().length; i++) {
			Object object = (Object)linha.getLstAgrupamento()[i];
			Object objectAnterior = (Object)linhaAnterior.getLstAgrupamento()[i];
			//Se o novo objeto não é nulo e o anterior é nulo -> criar uma nova fatura
			// OU
			//Se o novo objeto é nulo e o anterior não é nulo -> criar uma nova fatura			
			if ((object != null && objectAnterior == null) || (object == null && objectAnterior != null)){
				return true;
			}
			
			//Se o objeto atual e o objeto anterior não são nulo, comparar um com agrupadorAnterior ao outro para saber se tem 
			//que criar uma nova fatura
			if (object != null && objectAnterior != null){
				if (object instanceof Long){
					if (!((Long)object).equals((Long)objectAnterior)){
						return true;
					}
				} else if (object instanceof String){
					if (!((String)object).equals((String)objectAnterior)){
						return true;
					}
				} else if (object instanceof BigDecimal){
					if (!((BigDecimal)object).equals((BigDecimal)objectAnterior)){
						return true;
					}
				} else if (object instanceof Date){
					if (!((Date)object).equals((Date)objectAnterior)){
						return true;
					}
				}
			}
		}
		
		return false;
	}
}