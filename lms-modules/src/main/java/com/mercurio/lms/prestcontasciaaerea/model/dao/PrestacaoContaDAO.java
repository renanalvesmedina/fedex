package com.mercurio.lms.prestcontasciaaerea.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PrestacaoContaDAO extends BaseCrudDao<PrestacaoConta, Long>
{
	
	/**
	 * Busca as Prestacoes de Conta pelos parametros informados.<BR>
	 *@author Robson Edemar Gehl
	 * @param idCiaAerea
	 * @param idFilial
	 * @param nrPrestacaoConta
	 * @param dtInicial
	 * @param dtFinal
	 * @return
	 */
	public List findPrestacaoContaByUnique(Long idCiaAerea, Long idFilial, Long nrPrestacaoConta, YearMonthDay dtInicial, YearMonthDay dtFinal){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pc");
		
		dc.createAlias("pc.ciaFilialMercurio", "ciaFilial");
		dc.createAlias("ciaFilial.filial", "filial");
		dc.createAlias("ciaFilial.empresa", "empresa");
		
		if (idCiaAerea != null){
			dc.add(Restrictions.eq("empresa.id", idCiaAerea));
		}
		
		if (idFilial != null){
			dc.add(Restrictions.eq("filial.id", idFilial));
		}
		
		if (nrPrestacaoConta != null){
			dc.add(Restrictions.eq("pc.nrPrestacaoConta", nrPrestacaoConta));
		}
		
		if (dtInicial != null){
			dc.add(Restrictions.eq("pc.dtInicial", dtInicial));
		}
		
		if (dtFinal != null){
			dc.add(Restrictions.eq("pc.dtFinal", dtFinal));
		}
		
		return findByDetachedCriteria(dc);
	}
	
	/**
	 * Busca Empresa da Cia Filial Mercurio da Prestação de Conta.<BR>
	 *@author Robson Edemar Gehl
	 * @param idPrestacaoConta
	 * @return
	 */
	public Empresa findEmpresa(Long idPrestacaoConta){

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pc");

		dc.setProjection(Projections.property("ciaFilial.empresa"));

		dc.createAlias("pc.ciaFilialMercurio", "ciaFilial");

		List list = findByDetachedCriteria(dc);

		if (!list.isEmpty()){
			return (Empresa) list.get(0);
		}

		return null;
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return PrestacaoConta.class;
    }

    /**
     * Busca o id da prestação de contas que deve ser excluída
     * @param idEmpresa
     * @param nrPrestacaoConta
     * @return Id da prestacao de contas
     */
    public PrestacaoConta findPrestacaoContaDesmarcar(Long idEmpresa, Long nrPrestacaoConta, Long idFilialUserLogged) {
        
        DetachedCriteria dc = DetachedCriteria.forClass(PrestacaoConta.class,"pc");
        
        dc.createAlias("pc.ciaFilialMercurio","cfm");
        dc.createAlias("cfm.empresa","e");
        dc.createAlias("cfm.filial","f");
        
        dc.add(Restrictions.eq("e.id",idEmpresa));
        dc.add(Restrictions.eq("f.id",idFilialUserLogged));
        dc.add(Restrictions.eq("pc.nrPrestacaoConta", nrPrestacaoConta));
                
        dc.add(Restrictions.leProperty("cfm.dtVigenciaInicial", "pc.dtInicial"));
        dc.add(Restrictions.geProperty("cfm.dtVigenciaFinal", "pc.dtInicial")); 
        
        
        List prestacaoContaList = findByDetachedCriteria(dc);
        
        PrestacaoConta pc = null;
        
        if( !prestacaoContaList.isEmpty() ){
            pc = (PrestacaoConta) prestacaoContaList.get(0);   
        if( pc != null ){
                return pc;
        }
        }
        return null;
    }  
}