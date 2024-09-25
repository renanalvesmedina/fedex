package com.mercurio.lms.carregamento.model.dao;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.RotasExpressas;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RotasExpressasDAO extends BaseCrudDao<RotasExpressas, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return RotasExpressas.class;
    }
    
    
    public List findByNrRotaIdaVolta(Integer nrRotaIdaVolta, String tpAgrupador) {
    	StringBuffer sql = new StringBuffer()
    	.append("from ").append(RotasExpressas.class.getName()).append(" as re ")
    	.append("where ")
    	.append("re.nrRotaLms = ? ").append("and re.status = 1 ");
    	
    	List param = new ArrayList();
    	param.add(nrRotaIdaVolta);

    	if (tpAgrupador != null) {
    		sql.append("and re.tpAgrupador = ? ");
    		param.add(tpAgrupador);
    	}

    	sql.append("order by re.tpAgrupador ");
    	return super.getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
    }


	public List findByNrRotaLmsFilialOrigemFilialDestino(Integer nrRota,Integer nrRotaLms, String filialOrigem, String filialDestino) {
		List param = new ArrayList();
		StringBuffer sql = new StringBuffer()
    	.append("from ").append(RotasExpressas.class.getName()).append(" as re ")
    	.append("where ")
    	.append("re.nrCodigo = ? ")
		.append(" and re.nrRotaLms = ? ")
		.append(" and re.sgUnidOrigem = ? ")
		.append(" and re.sgUnidDestino = ? ");
    	
    	
    	param.add(nrRota);
    	param.add(nrRotaLms);
    	param.add(filialOrigem);
    	param.add(filialDestino);


    	sql.append("order by re.tpAgrupador ");
    	return super.getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}
	
	/**
	 * Método criado pela Integraçao para trazer somente as rotas expressas ativas.
	 * 13/05/2010
	 **/
	public List findByNrRotaLmsFilialOrigemFilialDestinoAtivas(Integer nrRota,Integer nrRotaLms, String filialOrigem, String filialDestino) {
		List param = new ArrayList();
		StringBuffer sql = new StringBuffer()
    	.append("from ").append(RotasExpressas.class.getName()).append(" as re ")
    	.append("where ")
    	.append("re.nrCodigo = ? ")
		.append(" and re.nrRotaLms = ? ")
		.append(" and re.sgUnidOrigem = ? ")
		.append(" and re.sgUnidDestino = ? ")
		.append(" and re.status = 1");
    	
    	
    	param.add(nrRota);
    	param.add(nrRotaLms);
    	param.add(filialOrigem);
    	param.add(filialDestino);


    	sql.append("order by re.tpAgrupador ");
    	return super.getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}
}