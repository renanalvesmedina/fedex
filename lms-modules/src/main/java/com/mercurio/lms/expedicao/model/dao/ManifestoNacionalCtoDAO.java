package com.mercurio.lms.expedicao.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.municipios.model.Filial;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ManifestoNacionalCtoDAO extends BaseCrudDao<ManifestoNacionalCto, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ManifestoNacionalCto.class;
    }

    /**
	 * Recebe um conjunto de ids para atualizar com S ou N (incluir / retirar da fronteira rápida)
	 * @param idsManifestoNacionalCto
	 */
    public void storeConhecimentosFronteiraRapida (List idsManifestoNacionalCto){
    	ManifestoNacionalCto manifestoNacionalCto;
    	for(int i = 0; i < idsManifestoNacionalCto.size(); i++){
    		manifestoNacionalCto = new ManifestoNacionalCto();

    		/** Resgata o idmanifestoNacionalCto do TypedFlatMap */
    		manifestoNacionalCto.setIdManifestoNacionalCto((Long) idsManifestoNacionalCto.get(i));

    		/** Carrega o bean ManifestoNacionalCto com o idManifestoNacionalCto atual da iteração */
    		manifestoNacionalCto =  (ManifestoNacionalCto) findById(manifestoNacionalCto.getIdManifestoNacionalCto());

    		/** Inverte o atributo blGeraFronteiraRapida (se é S, passa para N, e vice-versa)*/
    		this.updateManifestoNacionalCtoByFronteiraRapida(manifestoNacionalCto);
    	}
    }
    
    /**
     * Método responsável inverter o atributo blGeraFronteiraRapida
     * @param manifestoNacionalCto
     * @return ManifestoNacionalCto
     */ 
    public void updateManifestoNacionalCtoByFronteiraRapida(ManifestoNacionalCto manifestoNacionalCto){
    	if(manifestoNacionalCto.getBlGeraFronteiraRapida().booleanValue()){
    		manifestoNacionalCto.setBlGeraFronteiraRapida(Boolean.FALSE);
    	}else{
    		manifestoNacionalCto.setBlGeraFronteiraRapida(Boolean.TRUE);
    	}
    	getAdsmHibernateTemplate().flush();
    }

    /**
     * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
     * @param idConhecimento
     * @param idManifestoViagemNacional
     * @return ManifestoNacionalCto
     */
    public ManifestoNacionalCto findManifestoNacionalCto(Long idConhecimento, Long idManifestoViagemNacional) {
    	DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("manifestoViagemNacional", "mvn");
		dc.createAlias("conhecimento", "c");
		dc.add(Restrictions.eq("c.idDoctoServico", idConhecimento));
		dc.add(Restrictions.eq("mvn.id", idManifestoViagemNacional));
		return (ManifestoNacionalCto) getAdsmHibernateTemplate().findUniqueResult(dc);
    }

    /**
     * Conhecimentos Ordenados para Impressao do Manifesto.
     * @author Andre Valadas
     * @param idManifestoViagemNacional
     * @return
     */
    public List findConhecimentos(Long idManifestoViagemNacional){
    	/** PROJECTION */
    	StringBuilder projection = new StringBuilder();
    	projection.append("mnCto, cto");
    	/** FROM */
    	StringBuilder from = new StringBuilder();
    	from.append(getPersistentClass().getName()).append(" as mnCto")
			.append(" join mnCto.conhecimento as cto")
			.append(" join fetch cto.filialByIdFilialOrigem as cfo")
			.append(" join fetch cto.filialByIdFilialDestino as cfd")
			.append(" join fetch cfd.pessoa as pfdc")
			.append(" join fetch cto.clienteByIdClienteRemetente as cli")
			.append(" join fetch cli.pessoa as pes")
			.append(" join fetch mnCto.manifestoViagemNacional as mvn")
			.append(" join fetch mvn.manifesto as m")
			.append(" join fetch m.filialByIdFilialDestino as mfd")
			.append(" join fetch mfd.pessoa as pfdm")
			.append(" left join fetch cto.clienteByIdClienteDestinatario as cd")
			.append(" left join fetch cd.pessoa as cdp")
			.append(" left join fetch cto.clienteByIdClienteConsignatario as cc")
			.append(" left join fetch cc.pessoa as ccp")
			.append(" left join fetch cto.clienteByIdClienteRedespacho as ch")
			.append(" left join fetch ch.pessoa as chp")
			.append(" left join fetch cto.municipioByIdMunicipioEntrega as ce");

    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection(projection.toString());
    	hql.addFrom(from.toString());
    	/** WHERE */
    	hql.addCriteria("mvn.id","=",idManifestoViagemNacional);
    	/** ORDER BY */
    	hql.addOrderBy("pfdc.nmFantasia");
    	hql.addOrderBy("pes.nmPessoa");

    	/**
    	 * Aninhamento do Conhecimento ao ManifestoNacionalCto.
    	 * O Fetch do Hibernate nao esta trazendo relacionado esses POJOs(Problema ja visto com Felipe);
    	 */
    	List result = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	List manifestoNavionalCtos = new ArrayList(result.size());
    	if (!result.isEmpty()) {
    		for (Iterator iter = result.iterator(); iter.hasNext();) {
    			Object[] elements = (Object[]) iter.next();
				ManifestoNacionalCto manifestoCto = (ManifestoNacionalCto) elements[0];
				manifestoCto.setConhecimento((Conhecimento)elements[1]);
				manifestoNavionalCtos.add(manifestoCto);
			}
    	}
    	return manifestoNavionalCtos;
    }

    public List findManifestoNacionalCtosByIdManifestoViagemNacional(Long idManifestoViagemNacional) {
    	StringBuffer sb = new StringBuffer()
    		.append("select ")
    		.append("manc as manifestoNacionalCto ")
    		.append("from "+ManifestoNacionalCto.class.getName()+" manc ")
    		.append("inner join fetch manc.conhecimento ")
    		.append("where manc.manifestoViagemNacional.idManifestoViagemNacional = ? ");
    	return getAdsmHibernateTemplate().find(sb.toString(), new Object[] {idManifestoViagemNacional});
    }

    public ManifestoViagemNacional findManifestoViagemAbertoByDoctoServico(Conhecimento doctoServico, Filial filial) {
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = "select mvn from "+ManifestoNacionalCto.class.getName()+" manc "
                + " inner join manc.manifestoViagemNacional mvn "
                + " inner join mvn.manifesto m "
                + " where ";
        if (filial != null){
            hql+= " m.filialByIdFilialDestino = :filial and ";
            params.put("filial", filial);
        }
        hql+= " manc.conhecimento = :conhecimento "
        + " and m.tpStatusManifesto not in ('FE','CA') "
        ;
        
        params.put("conhecimento", doctoServico);
        List retorno = getAdsmHibernateTemplate().findByNamedParam(hql, params);
        if (retorno != null && retorno.size() > 0){
            return (ManifestoViagemNacional) retorno.get(0);
        }
        return null;
    }
    
    
}