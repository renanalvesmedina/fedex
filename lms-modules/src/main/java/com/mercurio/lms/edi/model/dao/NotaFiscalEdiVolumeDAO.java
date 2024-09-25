package com.mercurio.lms.edi.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.edi.model.NotaFiscalEdi;
import com.mercurio.lms.edi.model.NotaFiscalEdiVolume;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class NotaFiscalEdiVolumeDAO extends BaseCrudDao<NotaFiscalEdiVolume, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return NotaFiscalEdiVolume.class;
    }

	public Long findSequence(){		
		return Long.valueOf(getSession().createSQLQuery("select NOTA_FISCAL_EDI_VOLUME_SQ.nextval from dual").uniqueResult().toString());
	}	
    
    public List findByCodigoVolumeByIdNotaFiscalEdi(String codigoVolume, Long idNotaFiscalEdi) {
    	StringBuilder query = new StringBuilder()
    	.append(" from " + getPersistentClass().getName() + " as nfev ")
    	.append(" where nfev.codigoVolume = :codigoVolume " )
    	.append(" and nfev.notaFiscalEdi.idNotaFiscalEdi = :idNotaFiscalEdi " );
    	
    	Map criteria = new HashMap();
    	criteria.put("codigoVolume", Long.valueOf(codigoVolume));
    	criteria.put("idNotaFiscalEdi", idNotaFiscalEdi);
    	
    	return getAdsmHibernateTemplate().findByNamedParam(query.toString(), criteria);
    }
    
    

    public List<NotaFiscalEdiVolume> findByIdNotaFiscalEdi(Long idNotaFiscalEdi) {
    	StringBuilder query = new StringBuilder()
    	.append(" from " + getPersistentClass().getName() + " as nfev ")
    	.append(" where nfev.notaFiscalEdi.idNotaFiscalEdi = :idNotaFiscalEdi " )
    	.append(" and nfev.codigoVolume is not null " );
    	
    	Map criteria = new HashMap();
    	criteria.put("idNotaFiscalEdi", idNotaFiscalEdi);
    	
    	return getAdsmHibernateTemplate().findByNamedParam(query.toString(), criteria);
    }

    public void removeByIdNotaFiscalEdi(List<Long> list) {
    	StringBuilder deleteQuery = new StringBuilder();
    	deleteQuery.append("delete from " + getPersistentClass().getName() + " as nfev ");
    	deleteQuery.append("where nfev.notaFiscalEdi.idNotaFiscalEdi in (");
    	deleteQuery.append("select nfe.idNotaFiscalEdi from " + NotaFiscalEdi.class.getName() + " as nfe where (nfe.nrNotaFiscal, nfe.cnpjReme) in ");
    	deleteQuery.append("(select nfe1.nrNotaFiscal, nfe1.cnpjReme from " + NotaFiscalEdi.class.getName() + " as nfe1 where nfe1.idNotaFiscalEdi in (:id))) ");

    	while(list.size() > 1000){
    		List<Long> sublist = new ArrayList<Long>(list.subList(0, 999));
    		getAdsmHibernateTemplate().removeByIds(deleteQuery.toString(), sublist);
    		list.removeAll(sublist);
    	}
    	
    	getAdsmHibernateTemplate().removeByIds(deleteQuery.toString(), list);
    }
    
    public boolean existsVolumePendenteGM(final Long idNotaFiscalEdi){
    	String sql = "select count(*) "
			+ " from nota_fiscal_edi_volume nfev "
			+ " where nfev.nfed_id_nota_fiscal_edi = :idNotaFiscalEdi "
			+ " and exists (select 1 from volume vol inner join carregamento c on c.id_carregamento = vol.id_carregamento where vol.codigo_volume = nfev.codigo_volume and (c.codigo_status != 3 and c.codigo_status != 4)) ";
    	Long qtdRows = Long.valueOf(getSession().createSQLQuery(sql).setParameter("idNotaFiscalEdi", idNotaFiscalEdi).uniqueResult().toString());
		return qtdRows > 0;
    }

    /**
     * Referente ao jira LMS-2784
     * 
     * Retorna o(s) carregamento(s) relacionadas ao volumes das notas fiscais volume passado como parametro.
     * Resultado utilizado para verificar se todos os volumes das notas fiscais que foram selecionadas para atualização pertençam ao mesmo carregamento.
     * Caso a lista retornada tenha o size >1 existem volumes com carregamentos diferentes
     * 
     * @param idsNotaFiscalEdi
     * @return
     */
	public List findIdCarregamentoPlacaERota(List<Long> idsNotaFiscalEdi) {
		
		String sql = "select distinct c.id_carregamento, c.placa_veiculo, c.rota_carregamento "
				+ " from nota_fiscal_edi_volume nfev, " 
				+ " volume vol, "
				+ " carregamento c "
				+ " where vol.codigo_volume = nfev.codigo_volu me " 
				+ " and c.id_carregamento    = vol.id_carregamento "
				+ " and nfev.nfed_id_nota_fiscal_edi in (:idsNotaFiscalEdi)  ";
	    
		List retorno = getSession().createSQLQuery(sql).setParameterList("idsNotaFiscalEdi", idsNotaFiscalEdi).list();
		
	    return retorno;
	}
	
	public Integer countNotaFiscalVolumeByNotaFiscalEDI(Long idNotaFiscalEdi) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT nfev ");
		hql.append("FROM ").append(getPersistentClass().getSimpleName()).append(" AS nfev ");
		hql.append("WHERE ");
		hql.append(" nfev.notaFiscalEdi.idNotaFiscalEdi =:idNotaFiscalEdi");
		
		TypedFlatMap parametersValues = new TypedFlatMap();
		parametersValues.put("idNotaFiscalEdi", idNotaFiscalEdi);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), parametersValues);
	}
	
    
	public List<NotaFiscalEdiVolume> findNotaFiscalEdiByUltimoIdImportado(Long idNotaFiscalEdi) {
    	StringBuilder query = new StringBuilder()
    	.append(" from " + getPersistentClass().getName() + " as nfev ")
    	.append(" where nfev.notaFiscalEdi.idNotaFiscalEdi > :idNotaFiscalEdi " )
    	.append(" and nfev.codigoVolume is not null " );
    	
    	Map criteria = new HashMap();
    	criteria.put("idNotaFiscalEdi", idNotaFiscalEdi);
    	
    	return getAdsmHibernateTemplate().findByNamedParam(query.toString(), criteria);
		
    }
    
}