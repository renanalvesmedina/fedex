package com.mercurio.lms.portaria.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.portaria.model.AcaoIntegracao;
import com.mercurio.lms.vendas.model.GrupoEconomico;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AcaoIntegracaoDAO  extends BaseCrudDao<AcaoIntegracao, Long>{

	@Override
	protected Class getPersistentClass() {
		return AcaoIntegracao.class;
	}

	public List<AcaoIntegracao> findAcaoIntegracaoPorEvento(Filial filialByIdFilialOrigem,
																Long idUfFilialOrigem,
																Filial filialByIdFilialDestino,
																Long idUfFilialDestino,
																DomainValue tpModal, 
																String tpDocumento,
																GrupoEconomico grupoEconomico, 
																Long idUfCliente) {
		
		Map parameters = new HashMap();
		StringBuffer hql = new StringBuffer();
		hql.append("select acaointegracao from ").append(getPersistentClass().getSimpleName()).append(" as acaointegracao where ");
		
		// origem
		hql.append(" ( (acaointegracao.unidadeFederativaOrigem.id = :ID_UNIDADE_FEDERATIVA_ORIGEM and acaointegracao.blUfOrigemDiferenteDe = :FALSE) or (acaointegracao.unidadeFederativaOrigem.id != :ID_UNIDADE_FEDERATIVA_ORIGEM and acaointegracao.blUfOrigemDiferenteDe = :TRUE) or acaointegracao.unidadeFederativaOrigem = null ) ")
			.append(" and (acaointegracao.filialOrigem.id = :ID_FILIAL_ORIGEM or  acaointegracao.filialOrigem = null )");
		
		// Se TP_DOCUMENTO é ‘CTR’ e BL_REMETENTE_ORIGEM é SIM, entao validar que o ID_CLIENTE tenha seu endereço padrão na unidade federativa igual a ID_UNIDADE_FEDERATIVA_ORIGEM
		// ou seja: se nao blRemetenteOrigem então não precisa a validacao não é necessária. 
		if("CTR".equals(tpDocumento) && idUfCliente != null){
			hql.append(" and ( acaointegracao.blRemetenteOrigem != :TRUE or ( acaointegracao.unidadeFederativaOrigem.id = :UNIDADE_FEDERATIVA_CLIENTE ) ) ");
			parameters.put("UNIDADE_FEDERATIVA_CLIENTE", idUfCliente);
		}

		// destino
		hql.append(" and ( (acaointegracao.unidadeFederativaDestino.id = :ID_UNIDADE_FEDERATIVA_DESTINO and acaointegracao.blUfDestinoDiferenteDe = :FALSE) or (acaointegracao.unidadeFederativaDestino.id != :ID_UNIDADE_FEDERATIVA_DESTINO and acaointegracao.blUfDestinoDiferenteDe = :TRUE) or acaointegracao.unidadeFederativaDestino = null ) ")
			.append(" and (acaointegracao.filialDestino.id = :ID_FILIAL_DESTINO or  acaointegracao.filialDestino = null )");
		
		//modal
		hql.append(" and (acaointegracao.tpModal = :TP_MODAL or acaointegracao.tpModal = null ) ");
		// tipo documento
		hql.append(" and acaointegracao.tpDocumento = :TP_DOCUMENTO ");
		//Grupo Econômico
		if(grupoEconomico != null){
			hql.append(" and (acaointegracao.grupoEconomico.id = :ID_GRUPO_ECONOMICO or acaointegracao.grupoEconomico = null ) ");
			parameters.put("ID_GRUPO_ECONOMICO", grupoEconomico.getIdGrupoEconomico());
		}
		
		parameters.put("ID_UNIDADE_FEDERATIVA_ORIGEM", idUfFilialOrigem);
		parameters.put("ID_FILIAL_ORIGEM", filialByIdFilialOrigem.getIdFilial());
		
		parameters.put("ID_UNIDADE_FEDERATIVA_DESTINO", idUfFilialDestino);
		parameters.put("ID_FILIAL_DESTINO", filialByIdFilialDestino.getIdFilial());
		
		parameters.put("TP_MODAL", tpModal.getValue());
		parameters.put("TP_DOCUMENTO", tpDocumento);
		
		
		parameters.put("TRUE", Boolean.TRUE);
		parameters.put("FALSE", Boolean.FALSE);
		
		
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parameters);
	}
	
	public List<AcaoIntegracao> findByTpDocumento(String tpDocumento) {
		SqlTemplate hql = new SqlTemplate();		
		hql.addProjection("acaoIntegracao");		
		hql.addLeftOuterJoin(getPersistentClass().getName(),"acaoIntegracao");		
		hql.addCriteria("acaoIntegracao.tpDocumento","=",tpDocumento);		
		return (List)getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());			
	}

	public AcaoIntegracao findByProcesso(String dsProcesso) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(AcaoIntegracao.class, "ai")		
		.add(Restrictions.eq("ai.dsProcessoIntegracao", dsProcesso));

		return (AcaoIntegracao)getAdsmHibernateTemplate().findUniqueResult(dc);		
}

}
