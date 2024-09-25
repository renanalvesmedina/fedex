package com.mercurio.lms.expedicao.model.dao;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.TBDatabaseInputCTE;

public class TbDatabaseInputCTEDAO extends BaseCrudDao<TBDatabaseInputCTE, Long> {
	
	private static final String FROM = "from ";
	private static final String WHERE = "where ";

	@Override
	protected Class<TBDatabaseInputCTE> getPersistentClass() {
		return TBDatabaseInputCTE.class;
	}


	/**
	 * @param idEnvioDocEletronicoE
	 * @return
	 */
	public TBDatabaseInputCTE findByIdEnvioDocEletronicoE(Long idEnvioDocEletronicoE) {

		StringBuilder hql = new StringBuilder();
		hql.append("select tb ");
		hql.append(FROM);
		hql.append(TBDatabaseInputCTE.class.getSimpleName() + " tb ");
		hql.append(WHERE);
		hql.append("tb.id = ? ");

		List<Object> param = new ArrayList<Object>();
		param.add(idEnvioDocEletronicoE);

		return (TBDatabaseInputCTE) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), param.toArray());

	}

	/**
	 * Busca as informações do CTE para tela Carta de correçao
	 *
	 * @param idDoctoServico
	 * @return
	 */
	public TBDatabaseInputCTE findXmlByIdDoctoServico(Long idDoctoServico) {
		StringBuilder hql = new StringBuilder();
		hql.append("select tb ");
		hql.append(FROM);
		hql.append(TBDatabaseInputCTE.class.getSimpleName() + " tb, ");
		hql.append(MonitoramentoDocEletronico.class.getSimpleName() + " mde ");
		hql.append(" join mde.doctoServico ds ");
		hql.append(WHERE);
		hql.append("tb.id = mde.idEnvioDocEletronicoE ");
		hql.append("and ds.id= ?");

		List<Object> param = new ArrayList<Object>();
		param.add(idDoctoServico);

		TBDatabaseInputCTE tb = (TBDatabaseInputCTE) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), param.toArray());
		
		if(tb!=null){
			return tb;
		} else {
			return findXmlMDEByIdDoctoServico( idDoctoServico);
		}
	}
	
	private TBDatabaseInputCTE findXmlMDEByIdDoctoServico(Long idDoctoServico) {
		TBDatabaseInputCTE tbRetorno = new TBDatabaseInputCTE();
		
		StringBuilder hql = new StringBuilder();
		hql.append("select mde ");
		hql.append(FROM);		
		hql.append(MonitoramentoDocEletronico.class.getSimpleName() + " mde ");
		hql.append(" join mde.doctoServico ds ");
		hql.append(WHERE);	
		hql.append("mde.dsDadosDocumento is not null ");
		hql.append("and ds.id= ?");

		List<Object> param = new ArrayList<Object>();
		param.add(idDoctoServico);

		MonitoramentoDocEletronico mde = (MonitoramentoDocEletronico) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), param.toArray());
		if (mde != null){
			tbRetorno.setDocumentData(mde.getDsDadosDocumento());
		}else{
			return null;			
		}		
		return tbRetorno;
	}
	
	public void updateStatusReenvio(final Long idTBDatabaseInputCTE) {
		HibernateCallback hcb = new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = " update tbdatabaseinput_cte set status = :STATUS where id = :ID";

				SQLQuery query = session.createSQLQuery(sql);
				query.setLong("ID", idTBDatabaseInputCTE);
				query.setInteger("STATUS", 0);

				query.executeUpdate();
				return null;
			}

		};

		getAdsmHibernateTemplate().execute(hcb);
	}
}
