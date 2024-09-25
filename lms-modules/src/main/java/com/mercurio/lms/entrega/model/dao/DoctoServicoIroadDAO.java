package com.mercurio.lms.entrega.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.entrega.model.DoctoServicoIroad;
import com.mercurio.lms.expedicao.model.DoctoServico;

public class DoctoServicoIroadDAO extends BaseCrudDao<DoctoServicoIroad, Long>{

    protected final Class getPersistentClass() {
        return DoctoServicoIroad.class;
    }
    
    public List findDoctoServicoIroadByNrVolumeEmbarque(String nrVolumeEmbarque) {
        StringBuilder query = new StringBuilder()
                .append(" select new map( docIroad.nrSequenciaRota as nrSequenciaRota, ")
                .append("                 docIroad.dsRotaIroad as dsRotaIroad, ")
                .append("                 docIroad.dhInclusao as dhInclusao, ")
                .append("                 loc.dsLocalizacaoMercadoria as dsLocalizacaoMercadoria, ")
                .append("                 filLoc.sgFilial as filialLocalizacao ")
                .append("               ) ")
                .append(" from DoctoServicoIroad as docIroad, ")
                .append(" DoctoServico as doc, ")
                .append(" NotaFiscalConhecimento as nfc, ")
                .append(" VolumeNotaFiscal as vnf, ")
                .append(" LocalizacaoMercadoria as loc, ")
                .append(" Filial as filLoc ")
                .append(" where docIroad.doctoServico.id = doc.idDoctoServico ")
                .append(" and doc.idDoctoServico = nfc.conhecimento.id ")
                .append(" and doc.localizacaoMercadoria.id = loc.id ")
                .append(" and doc.filialLocalizacao.id = filLoc.id ")
                .append(" and nfc.id = vnf.notaFiscalConhecimento.id ")
                .append(" and vnf.nrVolumeEmbarque = ? ");
        return getAdsmHibernateTemplate().find(query.toString(), new Object[]{nrVolumeEmbarque});
    }


    public Long findDoctoServicoUpload(String sgFilial, String nrDocumento){
    	    	StringBuilder sql = new StringBuilder();
    	sql.append(" select ds.id_docto_servico as idDoctoServico") 
			.append(" from ")
			.append("   docto_servico ds, ")
		    .append("   filial f ")
		    .append(" where ")
		    .append(" ds.id_filial_origem = f.id_filial ")
		    .append(" and ds.ID_FILIAL_LOCALIZACAO = ds.ID_FILIAL_DESTINO ")
		    .append(" and ds.ID_LOCALIZACAO_MERCADORIA = 123 ")
		    .append(" and f.id_empresa = 361 ")
		    .append(" and f.SG_FILIAL = :sgFilial ")
		    .append(" and ds.NR_DOCTO_SERVICO = :nrDoctoServico ");
    	
    	
    	
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("sgFilial", sgFilial);
    	params.put("nrDoctoServico", new Long(nrDocumento));
    	
    	List<Object[]> result = getAdsmHibernateTemplate().findBySql(sql.toString(), params, new ConfigureSqlQuery(){

			@Override
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("idDoctoServico", Hibernate.LONG);
				
			}
    		
    	});
    	if (result != null && !result.isEmpty()){
    		Object id = result.get(0);
    		return (Long)id;
    	}
    	
    	return null;
    }

	public DoctoServicoIroad findByDoctoServico(DoctoServico doctoServico) {
		StringBuilder hql = new StringBuilder();
		hql.append("select dsi from DoctoServicoIroad dsi where dsi.doctoServico = :doctoServico");
		
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("doctoServico", doctoServico);
		
		List<DoctoServicoIroad> result = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
		if (result != null && !result.isEmpty()){
			return result.get(0);
		}
		
		return null;
	}
}
