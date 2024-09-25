package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ParcelaTabelaCeDAO extends BaseCrudDao<ParcelaTabelaCe, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ParcelaTabelaCe.class;
    }

    /**
     * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
     * @param idSolicitacaoContratacao
     * @return <b>List of ParcelaTabelaCe</b>
     */
    public List<ParcelaTabelaCe> findParcelasTabelaColetaEntrega(Long idSolicitacaoContratacao) {
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.createAlias("tabelaColetaEntrega", "tce");
    	dc.add(Restrictions.eq("tce.solicitacaoContratacao.id", idSolicitacaoContratacao));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

    public List findParcelaTabelaCeByTpParcela(String tpParcela,Long idParcelaTabelaCE,Long idTabelaColetaEntrega,Long idTipoMeioTransporte,YearMonthDay vigenteEm, Long idFilial) {
    	DetachedCriteria dc = DetachedCriteria.forClass(ParcelaTabelaCe.class,"PTC")
    						.createAlias("PTC.tabelaColetaEntrega","TCE")
    						.add(Restrictions.eq("PTC.tpParcela",tpParcela));
    	
    	if (idParcelaTabelaCE != null) {
			dc.add(Restrictions.ne("PTC.idParcelaTabelaCe",idParcelaTabelaCE))
				.createAlias("TCE.tipoTabelaColetaEntrega","TTCE")
				.add(Restrictions.eq("TTCE.blNormal",Boolean.TRUE));
    	}
    	if (vigenteEm != null)
    		dc.add(Restrictions.le("TCE.dtVigenciaInicial",vigenteEm))
    			.add(Restrictions.or(Restrictions.ge("TCE.dtVigenciaFinal",vigenteEm), Restrictions.isNull("TCE.dtVigenciaFinal") ) )
    			.add(Restrictions.eq("TCE.tpRegistro","A"))
    			.createAlias("TCE.tipoTabelaColetaEntrega","TTCE")
    			.add(Restrictions.eq("TTCE.blNormal",Boolean.TRUE));
    		
    	
    	
    	if (idTipoMeioTransporte != null)
    		dc.add(Restrictions.eq("TCE.tipoMeioTransporte.id",idTipoMeioTransporte));
    	
    	if (idFilial != null)
    		dc.add(Restrictions.eq("TCE.filial.id",idFilial));

    	if (idTabelaColetaEntrega != null)
			dc.add(Restrictions.eq("TCE.idTabelaColetaEntrega",idTabelaColetaEntrega));
    	
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
    public List findParcelaTabelaCeByGroupTpParcelas(List tpParcela,Long idTabelaColetaEntrega) {
        DetachedCriteria dc = DetachedCriteria.forClass(ParcelaTabelaCe.class,"PTC")
                            .createAlias("PTC.tabelaColetaEntrega","TCE")
                            .add(Restrictions.eq("TCE.idTabelaColetaEntrega",idTabelaColetaEntrega))
                            .add(Restrictions.in("PTC.tpParcela", tpParcela));
        return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

	public List findByTabelaColetaEntrega(Long idTabelaColetaEntrega) {
        DetachedCriteria dc = DetachedCriteria.forClass(ParcelaTabelaCe.class,"PTC")
                            .createAlias("PTC.tabelaColetaEntrega","TCE")
                            .add(Restrictions.eq("TCE.idTabelaColetaEntrega",idTabelaColetaEntrega));
        return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
    
	/**
     * Método utilizado pela Integração.
     * 
     * @param tabelaColetaEntrega Pojo de tabela de coleta/entrega
     * Restringir por:<br>
     * 		- filial.idFilial<br>
     * 		- tabelaColetaEntrega.tipoMeioTransporte<br>
     * 		- tabelaColetaEntrega.tpRegistro<br>
     * 		se tpRegistro for Eventual:<br>
     * 			- tabelaColetaEntrega.meioTransporteRodoviario<br>
     * 		se tpRegistro for Agregado:<br>
     * 			- tabelaColetaEntrega.tipoTabelaColetaEntrega<br>
     * 		- tabelaColetaEntrega.dtVigenciaInicial <= dtVigencia<br>
     * 		- tabelaColetaEntrega.dtVigenciaFinal > dtVigencia<br>
     * 		- tpParcela<br>
     * @param dtVigencia data de vigência
     * @param tpParcela tipo de parcela
     * @return Objeto ParcelaTabelaCe preenchido.
     */
    public ParcelaTabelaCe findParcelaTabelaCe(TabelaColetaEntrega tabelaColetaEntrega,
    		YearMonthDay dtVigencia, String tpParcela) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"PTC")
				.createAlias("PTC.tabelaColetaEntrega", "TCE");
				
		try {
			dc.add(Restrictions.eq("PTC.tpParcela", tpParcela));    	

			dc.add(Restrictions.eq("TCE.filial.id",tabelaColetaEntrega.getFilial().getIdFilial()));
			
			if (dtVigencia != null) {
				dc.add(Restrictions.le("TCE.dtVigenciaInicial", dtVigencia));
				dc.add(Restrictions.ge("TCE.dtVigenciaFinal", dtVigencia));
			}
			
			String tpRegistro = tabelaColetaEntrega.getTpRegistro().getValue();
			dc.add(Restrictions.eq("TCE.tpRegistro",tpRegistro));
			
			// Dependendo do tipo de registro, faz filtros diferentes.
			if (tpRegistro.equals("E")) {
				if( tabelaColetaEntrega.getMeioTransporteRodoviario() != null &&
					tabelaColetaEntrega.getMeioTransporteRodoviario().getIdMeioTransporte()!= null )
						dc.add(Restrictions.eq("TCE.meioTransporteRodoviario.id",
								tabelaColetaEntrega.getMeioTransporteRodoviario().getIdMeioTransporte()));
			} else if (tpRegistro.equals("A")) {
				if( tabelaColetaEntrega.getTipoTabelaColetaEntrega() != null &&
					tabelaColetaEntrega.getTipoTabelaColetaEntrega().getIdTipoTabelaColetaEntrega() != null )
						dc.add(Restrictions.eq("TCE.tipoTabelaColetaEntrega.id",
								tabelaColetaEntrega.getTipoTabelaColetaEntrega().getIdTipoTabelaColetaEntrega()));
			}
			
			if( tabelaColetaEntrega.getTipoMeioTransporte() != null && 
				tabelaColetaEntrega.getTipoMeioTransporte().getIdTipoMeioTransporte() != null ){
					dc.add(Restrictions.eq("TCE.tipoMeioTransporte.id",
						tabelaColetaEntrega.getTipoMeioTransporte().getIdTipoMeioTransporte()));
			}
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Verifique os valores carregados no POJO TabelaColetaEntrega. " +
					"Há um contrato e vários valores devem ser preenchidos.", e);
		}		

		return (ParcelaTabelaCe) getAdsmHibernateTemplate().findUniqueResult(dc);
    }
	
	public List<ParcelaTabelaCe> findParcelasColetaEntrega(List<Long> idsTabelaColetaEntrega) {
		DetachedCriteria  criteria = createDetachedCriteria();
		criteria.add(Restrictions.in("idParcelaTabelaCe", idsTabelaColetaEntrega));
		return getAdsmHibernateTemplate().findByCriteria(criteria);
	}
	
	public ParcelaTabelaCe findByTabelaColetaEntrega(TabelaColetaEntrega tabelaColetaEntrega) {
		DetachedCriteria  criteria = createDetachedCriteria();
		criteria.add(Restrictions.eq("tabelaColetaEntrega", tabelaColetaEntrega));
		return (ParcelaTabelaCe) getAdsmHibernateTemplate().findUniqueResult(criteria);
	}

	public List<ParcelaTabelaCe> findListByTabelaColetaEntrega(TabelaColetaEntrega tabelaColetaEntrega) {
		DetachedCriteria  criteria = createDetachedCriteria();
		criteria.add(Restrictions.eq("tabelaColetaEntrega", tabelaColetaEntrega));
		return getAdsmHibernateTemplate().findByCriteria(criteria);
	}
	
	public List<ParcelaTabelaCe> findParcelaTabelaCeByIdNotaCredito(Long idNotaCredito) {
		StringBuffer hql = new StringBuffer();
		hql.append("select ptce ");
		hql.append("from ParcelaTabelaCe as ptce ");
		hql.append("join ptce.notaCreditoParcelas as ncpa ");
		hql.append("join ncpa.notaCredito as nocr ");
		hql.append("where nocr.idNotaCredito = ? ");
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idNotaCredito});
	}
}