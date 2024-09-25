package com.mercurio.lms.edi.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.edi.enums.DsCampoLogErrosEDI;
import com.mercurio.lms.edi.model.LogErrosEDI;

public class LogErrosEDIDAO extends BaseCrudDao<LogErrosEDI, Long> {

	@Override
	public Class getPersistentClass() {
		return LogErrosEDI.class;
	}

	public LogErrosEDI findById(Long id) {
		return (LogErrosEDI) super.findById(id);
	}

	public List findNotasEDIParaAjuste(TypedFlatMap criteria){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(" +
				"log.idLogErrosEDI as idLogErrosEDI, " +
				"log.dsTipoProcessamento as dsTipoProcessamento, " +
				"log.dsCampoLogErrosEDI as dsCampoLogErrosEDI, " +
				"log.dsValorErrado as dsValorErrado, " +
				"log.dsValorCorrigido as dsValorCorrigido, " + 
				"log.nrProcessamento as nrProcessamento, " +
				"log.nrNotaFiscal as nrNotaFiscal, " +
				"log.dataEmissaoNf as dataEmissaoNf, " +
				"log.cnpjReme as cnpjReme, " +
				"nfEdi.idNotaFiscalEdi as idNotaFiscalEdi, " +
				"nfEdi.nrNotaFiscal as nrNotaFiscal, " +
				"to_char(nfEdi.dataEmissaoNf, 'dd/MM/YYYY') as dataEmissaoNf, " +
				"nfEdi.nomeDest as nomeDest)");
		
		hql.addFrom(LogErrosEDI.class.getName()+ " log join log.notaFiscalEdi nfEdi");
		
		if(criteria.getString("naoTrazerLogsDoTipo") != null) {
			hql.addRequiredCriteria("log.dsCampoLogErrosEDI", "!=", DsCampoLogErrosEDI.valueOf(criteria.getString("naoTrazerLogsDoTipo")));
		} else {
			hql.addRequiredCriteria("log.dsCampoLogErrosEDI", "=", DsCampoLogErrosEDI.ETIQUETAS);
		}
		hql.addRequiredCriteria("log.nrProcessamento", "=", criteria.getLong("nrProcessamento"));
		hql.addCustomCriteria("log.dsValorCorrigido is null");
		hql.addOrderBy("log.nrOrdemDigitacao");

		List retorno = getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());

		return retorno;
	}
}
