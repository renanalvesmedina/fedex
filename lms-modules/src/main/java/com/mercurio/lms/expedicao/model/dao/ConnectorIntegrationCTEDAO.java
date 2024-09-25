package com.mercurio.lms.expedicao.model.dao;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.AdiantamentoTrecho;
import com.mercurio.lms.expedicao.model.ConnectorIntegrationCTE;
import com.mercurio.lms.expedicao.model.TBDatabaseInputCTE;

public class ConnectorIntegrationCTEDAO extends BaseCrudDao<TBDatabaseInputCTE, Long>{
	
	@Override
	protected Class<ConnectorIntegrationCTE> getPersistentClass() {
		return ConnectorIntegrationCTE.class;
	}
	
	public List<ConnectorIntegrationCTE> findRetornosPendentes(){
		String sql = "from ConnectorIntegrationCTE ci where ci.status = 0 order by kind";
		return (List<ConnectorIntegrationCTE>) getAdsmHibernateTemplate().find(sql);
	}

	public void updateStatusConnectorIntegration(Long connectorIntegrationCTEID) {
		//update tem melhor performance
		String sql = "update "
    	+ "ConnectorIntegrationCTE as cn "
    	+ "set cn.status = 1 "
    	+ "where cn.connectorIntegrationCTEID = ? ";

    	List param = new ArrayList();
    	param.add(connectorIntegrationCTEID);

    	super.executeHql(sql, param);
	}
	
	public void removeByIdConnectorIntegration(ConnectorIntegrationCTE connectorIntegration) {
    	StringBuffer sql = new StringBuffer()
	    	.append("delete from ")
	    	.append(ConnectorIntegrationCTE.class.getName()).append(" as cicte ")
	    	.append(" where cicte.id = ? ");

    	List param = new ArrayList();
    	param.add(connectorIntegration.getConnectorIntegrationCTEID());

    	super.executeHql(sql.toString(), param);
    }
	
}
