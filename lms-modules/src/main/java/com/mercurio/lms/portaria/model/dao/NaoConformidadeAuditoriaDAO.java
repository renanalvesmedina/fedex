package com.mercurio.lms.portaria.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.portaria.model.NaoConformidadeAuditoria;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class NaoConformidadeAuditoriaDAO extends BaseCrudDao<NaoConformidadeAuditoria, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return NaoConformidadeAuditoria.class;
    }

	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("naoConformidade",FetchMode.JOIN);
		map.put("naoConformidade.filial",FetchMode.JOIN);
		map.put("naoConformidade.doctoServico",FetchMode.JOIN);
		map.put("naoConformidade.doctoServico.filialByIdFilialOrigem",FetchMode.JOIN);
		map.put("naoConformidade.doctoServico.conhecimento",FetchMode.JOIN);
		map.put("naoConformidade.doctoServico.ctoInternacional",FetchMode.JOIN);
		map.put("naoConformidade.doctoServico.mda",FetchMode.JOIN);
		super.initFindPaginatedLazyProperties(map);
	}
    
	protected void initFindByIdLazyProperties(Map map) {
		map.put("naoConformidade",FetchMode.JOIN);
		map.put("naoConformidade.filial",FetchMode.JOIN);		
		map.put("naoConformidade.doctoServico",FetchMode.JOIN);
		map.put("naoConformidade.doctoServico.filialByIdFilialOrigem",FetchMode.JOIN);
		map.put("naoConformidade.doctoServico.conhecimento",FetchMode.JOIN);
		map.put("registroAuditoria",FetchMode.JOIN);
		super.initFindByIdLazyProperties(map);
	}



}