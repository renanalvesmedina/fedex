package com.mercurio.lms.indenizacoes.model.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.indenizacoes.model.LoteJdeRim;
import com.mercurio.lms.indenizacoes.model.dao.LoteJDERimDAO;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.indenizacoes.loteJDERimService"
 */
public class LoteJDERimService extends CrudService<LoteJdeRim, Long> {
	
	private LoteJDERimDAO loteJDERimDAO;
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dti = (String) criteria.remove("dtInicial");
		String dtf = (String) criteria.remove("dtFinal");
		String nrLote = (String) criteria.remove("nrLoteJdeRim");
		Date inicio = null;
		Date fim = null;
		
		try {
			inicio = sdf.parse(dti);
			fim = sdf.parse(dtf);
			if(fim != null){
				fim.setHours(23);
				fim.setMinutes(59);
			}
		} catch (ParseException e) {
		}			
		
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("dhLoteJdeRim.value"), "dhLoteJdeRim")
			.add(Projections.property("idLoteJdeRim"))
			.add(Projections.sum("rim.vlIndenizacao"), "vlIndenizacao")
			.add(Projections.property("idLoteJdeRim"), "nrLoteJdeRim")
			.add(Projections.groupProperty("idLoteJdeRim"))
			.add(Projections.groupProperty("dhLoteJdeRim.value"));
		
		DetachedCriteria dc = DetachedCriteria.forClass(LoteJdeRim.class)
			.setProjection(pl)
			.createAlias("recibosIndenizacao", "rim")
			.addOrder(Order.desc("dhLoteJdeRim.value"))
			.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		
		
		
		if(null != inicio && null != fim){	
			dc.add(Restrictions.between("dhLoteJdeRim.value", inicio, fim));
		}else{
			if(null != inicio) dc.add(Restrictions.ge("dhLoteJdeRim.value",inicio)); 		
			if(null != fim) dc.add(Restrictions.le("dhLoteJdeRim.value", fim));		
		}
		if(!"".equals(nrLote)){	
			dc.add(Restrictions.eq("idLoteJdeRim", Long.valueOf(nrLote)));
		}
		
		FindDefinition fd = FindDefinition.createFindDefinition(criteria);
		fd.setDetachedCriteria(dc);
		
		return loteJDERimDAO.findPaginated(criteria, fd);
	}
	
	@Override
	public Integer getRowCount(Map criteria) {
		return loteJDERimDAO.getRowCount(criteria);
	}

	public LoteJDERimDAO getLoteJDERimDAO() {
		return loteJDERimDAO;
	}

	public void setLoteJDERimDAO(LoteJDERimDAO loteJDERimDAO) {
		this.loteJDERimDAO = loteJDERimDAO;
	}

}