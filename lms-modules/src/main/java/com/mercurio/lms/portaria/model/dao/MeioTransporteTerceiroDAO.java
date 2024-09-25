package com.mercurio.lms.portaria.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.portaria.model.MeioTransporteTerceiro;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MeioTransporteTerceiroDAO extends BaseCrudDao<MeioTransporteTerceiro, Long>
{

	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("modeloMeioTransporte", FetchMode.JOIN);
		lazyFindLookup.put("modeloMeioTransporte.tipoMeioTransporte", FetchMode.JOIN);
		lazyFindLookup.put("modeloMeioTransporte.marcaMeioTransporte", FetchMode.JOIN);
		
	}
	
	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return MeioTransporteTerceiro.class;
    }


}