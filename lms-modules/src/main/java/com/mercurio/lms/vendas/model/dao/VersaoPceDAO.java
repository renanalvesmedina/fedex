package com.mercurio.lms.vendas.model.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.TelefoneContato;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.vendas.model.VersaoContatoPce;
import com.mercurio.lms.vendas.model.VersaoDescritivoPce;
import com.mercurio.lms.vendas.model.VersaoPce;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class VersaoPceDAO extends BaseCrudDao<VersaoPce, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return VersaoPce.class;
    }

    protected void initFindByIdLazyProperties(Map fetchModes) {
    	fetchModes.put("cliente",FetchMode.JOIN);
    	fetchModes.put("cliente.pessoa",FetchMode.JOIN);
    	super.initFindByIdLazyProperties(fetchModes);
    }
    
    protected void initFindPaginatedLazyProperties(Map fetchModes) {
    	fetchModes.put("cliente",FetchMode.JOIN);
    	fetchModes.put("cliente.pessoa",FetchMode.JOIN);
    	super.initFindPaginatedLazyProperties(fetchModes);
    }
    
    public List nextValNrVersaoByCliente(Long idCliente) {
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.setProjection(Projections.max("nrVersaoPce"));
    	dc.add(Restrictions.eq("cliente.idCliente",idCliente));
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
    //FUNÇÕES RELACIONADAS A DF02
    public void removeById(Long id) {
    	VersaoPce bean = (VersaoPce)findById(id);
		bean.getVersaoDescritivoPces().clear();
		getAdsmHibernateTemplate().delete(bean);
	}
    public VersaoPce store(VersaoPce bean, ItemList items) {
        super.store(bean);
    	removeVersaoDescritivoPce(items.getRemovedItems());
    	storeVersaoDescritivoPce(items.getNewOrModifiedItems());
        return bean;
    }
    
	public int removeByIds(List ids) {
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			Long id = (Long) iter.next();
			removeById(id);
		}
		return ids.size();
	}

	public void storeVersaoDescritivoPce(List newOrModifiedItems) {
		for(Iterator ie = newOrModifiedItems.iterator(); ie.hasNext();) {
			VersaoDescritivoPce vdp = (VersaoDescritivoPce)ie.next();
			if (vdp.getIdVersaoDescritivoPce() != null)
				removeNetosByIdFilho(vdp.getIdVersaoDescritivoPce());
			this.store(vdp);
			if (vdp.getVersaoContatoPces() != null) {
				for (Iterator ie2 = vdp.getVersaoContatoPces().iterator(); ie2.hasNext();) {
					VersaoContatoPce vcp = (VersaoContatoPce)ie2.next();
					vcp.setIdVersaoContatoPce(null);
					vcp.setVersaoDescritivoPce(vdp);
					this.store(vcp);
				}
			}
		}
		
	}
	
	public void removeNetosByIdFilho(Long idFilho) {
		DetachedCriteria dc = DetachedCriteria.forClass(VersaoContatoPce.class,"VCP")
						   .setProjection(Projections.property("VCP.idVersaoContatoPce"))
						   .createAlias("VCP.versaoDescritivoPce","VDP")
						   .add(Restrictions.eq("VDP.idVersaoDescritivoPce",idFilho));
		List rs = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if (rs.size() > 0)
			getAdsmHibernateTemplate().removeByIds("delete from " + VersaoContatoPce.class.getName() + " as vcpce where vcpce.id in(:id) ", rs);
		rs = null;
	}

	public void removeVersaoDescritivoPce(List removeItems) {
		getAdsmHibernateTemplate().deleteAll(removeItems);
	}

    public List findVersaoDescritivoPceByIdVersaoPce(Long idVersaoPce) {
    	StringBuffer sb = new StringBuffer(250);
    	List rs =  getAdsmHibernateTemplate().find(sb.append("from ").append(VersaoDescritivoPce.class.getName()).append(" AS VDP ")
    											 .append("inner join fetch VDP.versaoPce AS VP ")
    											 .append("inner join fetch VDP.descritivoPce AS DP ")
    											 .append("inner join fetch DP.ocorrenciaPce AS OP ")
    											 .append("inner join fetch OP.eventoPce AS EP ")
    											 .append("inner join fetch EP.processoPce AS PP ")
    											 .append("where VP.idVersaoPce = ?").toString(),idVersaoPce);
    	for(Iterator ie = rs.iterator();ie.hasNext();) {
    		 VersaoDescritivoPce bean = (VersaoDescritivoPce)ie.next();
    		 bean.setVersaoContatoPces(findContatosByDescritivo(bean.getIdVersaoDescritivoPce()));
    	}	
    	return rs;
    }
    
    public List findContatosByDescritivo(Long idVersaoDescritivoPce) {
    	DetachedCriteria dc = DetachedCriteria.forClass(VersaoContatoPce.class);
    	dc.createAlias("contato","contato");
    	dc.createAlias("usuario","usuario");
    	dc.add(Restrictions.eq("versaoDescritivoPce.idVersaoDescritivoPce",idVersaoDescritivoPce));
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

	public Integer getRowCountVersaoDescritivoPceByIdVersaoPce(Long idVersaoPce) {
		StringBuffer sb = new StringBuffer(250);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sb.append("from ").append(VersaoDescritivoPce.class.getName()).append(" AS VDP ")
																				 .append("inner join fetch VDP.versaoPce AS VP ")
																				 .append("inner join fetch VDP.descritivoPce AS DP ")
																				 .append("inner join fetch DP.ocorrenciaPce AS OP ")
																				 .append("inner join fetch OP.eventoPce AS EP ")
																				 .append("inner join fetch EP.processoPce AS PP ")
																				 .append("where VP.idVersaoPce = ?").toString(), 
															    				new Object[] {idVersaoPce});
	}
	public List findTelefoneContatoByTpUso(Long idContato,String[] tpUso) {
		DetachedCriteria dc = DetachedCriteria.forClass(TelefoneContato.class);
						 dc.createAlias("telefoneEndereco","t");
						 dc.createAlias("contato","c");
						 dc.add(Restrictions.in("t.tpUso",tpUso));
						 dc.add(Restrictions.eq("c.idContato",idContato));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	public List findTelefoneContatoByTpTelefone(Long idContato,String[] tpTelefone) {
		DetachedCriteria dc = DetachedCriteria.forClass(TelefoneContato.class);
						 dc.createAlias("telefoneEndereco","t");
						 dc.createAlias("contato","c");
						 if (tpTelefone != null && tpTelefone.length > 0)
							 dc.add(Restrictions.in("t.tpTelefone",tpTelefone));
						 dc.add(Restrictions.eq("c.idContato",idContato));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	public int getRowCountEnderecoPessoaByContato(Long idContato) {
		DetachedCriteria dc = DetachedCriteria.forClass(EnderecoPessoa.class);
						 dc.setProjection(Projections.count("idEnderecoPessoa"));
						 dc.createAlias("telefoneEnderecos","te");
						 dc.createAlias("te.telefoneContatos","tc");
					 	 dc.createAlias("tc.contato","co");
						 dc.add(Restrictions.eq("co.idContato",idContato));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc).intValue();
	}
	public List findVersaoPceVigente(VersaoPce vp){
		DetachedCriteria dc = createDetachedCriteria();
		if(vp.getIdVersaoPce() != null)
			dc.add(Restrictions.ne("idVersaoPce",vp.getIdVersaoPce()));
		dc.add(Restrictions.eq("cliente.idCliente", vp.getCliente().getIdCliente()));
		dc = JTVigenciaUtils.getDetachedVigencia(dc,vp.getDtVigenciaInicial(),vp.getDtVigenciaFinal());
		return findByDetachedCriteria(dc);
	}
	
}