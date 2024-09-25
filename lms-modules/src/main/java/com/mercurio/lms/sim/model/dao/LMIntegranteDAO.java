package com.mercurio.lms.sim.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.expedicao.model.DoctoServico;
/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LMIntegranteDAO extends AdsmDao {

	public List findPaginatedIntegrantes (Long idDoctoServico) {
		StringBuilder hql = new StringBuilder()
		.append("select new map(\n")
		.append("       rem.idCliente as idClienteRem,\n")
		.append("       dest.idCliente as idClienteDest,\n")
		.append("       cons.idCliente as idClienteCons,\n")
		.append("       redes.idCliente as idClienteRedes,\n")
		.append("       pesRem.tpIdentificacao as tpIdentificacaoRem,\n")
		.append("       pesRem.nrIdentificacao as nrIdentificacaoRem,\n")
		.append("       pesRem.nmPessoa as nmPessoaRem,\n")
		.append("       munPesRem.nmMunicipio || ' - ' || ufPesRem.sgUnidadeFederativa || '/' || "+PropertyVarcharI18nProjection.createProjection("paisPesRem.nmPais")+" as municipioRem,\n")
		.append("       pesDest.tpIdentificacao as tpIdentificacaoDest,\n")
		.append("       pesDest.nrIdentificacao as nrIdentificacaoDest,\n")
		.append("       pesDest.nmPessoa as nmPessoaDest,\n")
		.append("       munPesDest.nmMunicipio || ' - ' || ufPesDest.sgUnidadeFederativa || '/' || "+PropertyVarcharI18nProjection.createProjection("paisPesDest.nmPais")+" as municipioDest,\n")
		.append("       pesCons.tpIdentificacao as tpIdentificacaoCons,\n")
		.append("       pesCons.nrIdentificacao as nrIdentificacaoCons,\n")
		.append("       pesCons.nmPessoa as nmPessoaCons,\n")
		.append("       munPesCons.nmMunicipio||' - '||ufPesCons.sgUnidadeFederativa||'/'|| "+PropertyVarcharI18nProjection.createProjection("paisPesCons.nmPais")+" as municipioCons,\n")
		.append("       pesRedes.tpIdentificacao as tpIdentificacaoRedes,\n")
		.append("       pesRedes.nrIdentificacao as nrIdentificacaoRedes,\n")
		.append("       pesRedes.nmPessoa as nmPessoaRedes,\n")
		.append("       munPesRedes.nmMunicipio||' - '||ufPesRedes.sgUnidadeFederativa||'/'|| "+PropertyVarcharI18nProjection.createProjection("paisPesRedes.nmPais")+" as municipioRedes)\n")
		.append("  from ").append(DoctoServico.class.getName()).append(" ds \n")
		.append("  left join ds.clienteByIdClienteRemetente rem \n")
		.append("  left join rem.pessoa pesRem\n")
		.append("  left join pesRem.enderecoPessoa endPesRem\n")
		.append("  left join endPesRem.municipio munPesRem\n")
		.append("  left join munPesRem.unidadeFederativa ufPesRem\n")
		.append("  left join ufPesRem.pais paisPesRem\n")
		.append("  left join ds.clienteByIdClienteDestinatario dest\n")
		.append("  left join dest.pessoa pesDest\n")
		.append("  left join pesDest.enderecoPessoa endPesDest\n")
		.append("  left join endPesDest.municipio munPesDest\n")
		.append("  left join munPesDest.unidadeFederativa ufPesDest\n")
		.append("  left join ufPesDest.pais paisPesDest\n")
		.append("  left join ds.clienteByIdClienteConsignatario cons\n")
		.append("  left join cons.pessoa pesCons\n")
		.append("  left join pesCons.enderecoPessoa endPesCons\n")
		.append("  left join endPesCons.municipio munPesCons\n")
		.append("  left join munPesCons.unidadeFederativa ufPesCons\n")
		.append("  left join ufPesCons.pais paisPesCons\n")
		.append("  left join ds.clienteByIdClienteRedespacho redes\n")
		.append("  left join redes.pessoa pesRedes\n")
		.append("  left join pesRedes.enderecoPessoa endPesRedes\n")
		.append("  left join endPesRedes.municipio munPesRedes\n")
		.append("  left join munPesRedes.unidadeFederativa ufPesRedes\n")
		.append("  left join ufPesRedes.pais paisPesRedes\n")
		.append(" where ds.idDoctoServico = ? \n");
		
		return getAdsmHibernateTemplate().find(hql.toString(), idDoctoServico);
	}
	
	public List findDevedoresByIdDoctoServico(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(" +
				"pesCli.tpIdentificacao as tpIdentificacao, " +
				"cli.idCliente as idCliente, " +
				"pesCli.nrIdentificacao as nrIdentificacao, " +
				"pesCli.nmPessoa as nmPessoa, " +
				"munPesCli.nmMunicipio||' - '||ufPesCli.sgUnidadeFederativa||'/'||"+PropertyVarcharI18nProjection.createProjection("paisPesCli.nmPais")+" as municipio)");
				
		
		hql.addFrom(DevedorDocServFat.class.getName()+ " dev " +
				"join dev.doctoServico ds " +
				"join dev.cliente cli " +
				"left outer join cli.pessoa pesCli " +
				"left outer join pesCli.enderecoPessoa endPesCli " +
				"left outer join endPesCli.municipio munPesCli " +
				"left outer join munPesCli.unidadeFederativa ufPesCli " +
				"left outer join ufPesCli.pais paisPesCli");
		
		hql.addCriteria("ds.idDoctoServico","=",idDoctoServico);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

}
