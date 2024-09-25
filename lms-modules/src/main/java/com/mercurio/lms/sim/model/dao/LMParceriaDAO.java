package com.mercurio.lms.sim.model.dao;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.model.CtoCtoCooperada;
import com.mercurio.lms.expedicao.model.NotaFiscalCtoCooperada;
import com.mercurio.lms.expedicao.model.ParcelaCtoCooperada;
/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LMParceriaDAO extends AdsmDao {
	
	public List findCooperadaByIdConhecimento(Long idDoctoServico) {
		StringBuilder hql = new StringBuilder()
		.append("select new map(pessoa.nrIdentificacao as nrIdentificacaoCooperada,\n")
		.append("               pessoa.nmPessoa as nmFantasiaCooperada,\n")
		.append("               pessoa.tpIdentificacao as tpIdentificacaoCooperada,\n")
		.append("               moeda.dsSimbolo as dsSimboloMoeda,\n")
		.append("               fil.sgFilial as sgFilialConhecimento,\n")
		.append("               cooperada.nrCtoCooperada as nrCtoCooperada,\n")
		.append("               cooperada.dhEmissao as dhEmissao,\n")
		.append("               cooperada.vlFrete as vlFrete,\n")
		.append("               cooperada.tpConhecimento as tpConhecimento,\n")
		.append("               cooperada.psAforado as psAforado,\n")
		.append("               cooperada.idCtoCtoCooperada as idCtoCtoCooperada,\n")
		.append("               "+PropertyVarcharI18nProjection.createProjection("nProd.dsNaturezaProduto")+" as dsNaturezaProduto,\n")
		.append("               pesRem.tpIdentificacao as tpIdentificacaoRem,\n")
		.append("               pesRem.nrIdentificacao as nrIdentificacaoRem,\n")
		.append("               pesRem.nmPessoa as nmPessoaRem,\n")
		.append("               pesDest.tpIdentificacao as tpIdentificacaoDest,\n")
		.append("               pesDest.nrIdentificacao as nrIdentificacaoDest,\n")
		.append("               pesDest.nmPessoa as nmPessoaDest,\n")
		.append("               cooperada.dtPrevisaoEntrega as dtPrevisaoEntrega,\n")
		.append("               "+PropertyVarcharI18nProjection.createProjection("locMerc.dsLocalizacaoMercadoria")+" as dsLocalizacaoMercadoria)\n")
		.append("  from ").append(CtoCtoCooperada.class.getName()).append(" cooperada\n")
		.append("  join cooperada.conhecimento conh\n")
		.append("  join cooperada.filialByIdFilial fil\n")
		.append("  join fil.empresa emp\n")
		.append("  left outer join emp.pessoa pessoa\n")
		.append("  left outer join cooperada.naturezaProduto nProd\n")
		.append("  left outer join cooperada.clienteByIdRemetente rem\n")
		.append("  left outer join rem.pessoa pesRem\n")
		.append("  left outer join cooperada.clienteByIdDestinatario dest\n")
		.append("  left outer join dest.pessoa pesDest\n")
		.append("  left outer join conh.localizacaoMercadoria locMerc\n")
		.append("  left outer join conh.moeda moeda\n")
		.append(" where conh.idDoctoServico = ").append(idDoctoServico);
		
		return getAdsmHibernateTemplate().find(hql.toString());
		
	}
	
		
	public List findPaginatedIntegrantes(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(" +
				"rem.idCliente as idClienteRem, " + 
				"pesRem.tpIdentificacao as tpIdentificacaoRem, " +
				"pesRem.nrIdentificacao as nrIdentificacaoRem, " +
				"pesRem.nmPessoa as nmPessoaRem, " +
				"munRem.nmMunicipio||' - '||ufRem.sgUnidadeFederativa||'/'||"+PropertyVarcharI18nProjection.createProjection("paisRem.nmPais")+" as municipioRem, " +
				
				"dest.idCliente as idClienteDest, " + 
				"pesDest.tpIdentificacao as tpIdentificacaoDest, " +
				"pesDest.nrIdentificacao as nrIdentificacaoDest, " +
				"pesDest.nmPessoa as nmPessoaDest, " +
				"munDest.nmMunicipio||' - '||ufDest.sgUnidadeFederativa||'/'||"+PropertyVarcharI18nProjection.createProjection("paisDest.nmPais")+" as municipioDest, " +
				
				"consi.idCliente as idClienteConsi, " + 
				"pesConsi.tpIdentificacao as tpIdentificacaoConsi, " +
				"pesConsi.nrIdentificacao as nrIdentificacaoConsi, " +
				"pesConsi.nmPessoa as nmPessoaConsi, " +
				"munConsi.nmMunicipio||' - '||ufConsi.sgUnidadeFederativa||'/'||"+PropertyVarcharI18nProjection.createProjection("paisConsi.nmPais")+" as municipioConsi, " +
				
				"redes.idCliente as idClienteRedes, " + 
				"pesRedes.tpIdentificacao as tpIdentificacaoRedes, " +
				"pesRedes.nrIdentificacao as nrIdentificacaoRedes, " +
				"pesRedes.nmPessoa as nmPessoaRedes, " +
				"munRedes.nmMunicipio||' - '||ufRedes.sgUnidadeFederativa||'/'||"+PropertyVarcharI18nProjection.createProjection("paisRedes.nmPais")+" as municipioRedes, " +
				
				"dev.idCliente as idClienteDev, " + 
				"pesDev.tpIdentificacao as tpIdentificacaoDev, " +
				"pesDev.nrIdentificacao as nrIdentificacaoDev, " +
				"pesDev.nmPessoa as nmPessoaDev, " +
				"munDev.nmMunicipio||' - '||ufDev.sgUnidadeFederativa||'/'||"+PropertyVarcharI18nProjection.createProjection("paisDev.nmPais")+" as municipioDev)");
		
		hql.addFrom(CtoCtoCooperada.class.getName() +" cooperada " +
				"left outer join cooperada.conhecimento conhe " +
				
				"left outer join cooperada.clienteByIdRemetente rem " +
				"left outer join rem.pessoa pesRem " +
				"left outer join pesRem.enderecoPessoa endPesRem " +
				"left outer join endPesRem.municipio munRem " +
				"left outer join munRem.unidadeFederativa ufRem " +
				"left outer join ufRem.pais paisRem " +
				
				"left outer join cooperada.clienteByIdDestinatario dest " +
				"left outer join dest.pessoa pesDest " +
				"left outer join pesDest.enderecoPessoa endPesDest " +
				"left outer join endPesDest.municipio munDest " +
				"left outer join munDest.unidadeFederativa ufDest " +
				"left outer join ufDest.pais paisDest " +
				
				
				"left outer join cooperada.clienteByIdConsignatario consi " +
				"left outer join consi.pessoa pesConsi " +
				"left outer join pesConsi.enderecoPessoa endPesConsi " +
				"left outer join endPesConsi.municipio munConsi " +
				"left outer join munConsi.unidadeFederativa ufConsi " +
				"left outer join ufConsi.pais paisConsi " +
				
				"left outer join cooperada.clienteByIdRedespacho redes " +
				"left outer join redes.pessoa pesRedes " +
				"left outer join pesRedes.enderecoPessoa endPesRedes " +
				"left outer join endPesRedes.municipio munRedes " +
				"left outer join munRedes.unidadeFederativa ufRedes " +
				"left outer join ufRedes.pais paisRedes " +
				
				"left outer join cooperada.clienteByIdDevedor dev " +
				"left outer join dev.pessoa pesDev " +
				"left outer join pesDev.enderecoPessoa endPesDev " +
				"left outer join endPesDev.municipio munDev " +
				"left outer join munDev.unidadeFederativa ufDev " +
				"left outer join ufDev.pais paisDev ");
		
		hql.addCriteria("conhe.idDoctoServico","=",idDoctoServico);
		
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	public List findNotaFiscalByIdConhecimento(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(nfCto.nrNotaFiscal as nrNotaFiscal, " +
				"nfCto.dsSerie as dsSerie, " +
				"nfCto.dtEmissao as dtEmissao, " +
				"nfCto.qtVolumes as qtVolumes, " +
				"nfCto.psMercadoria as psMercadoria, " +
				"nfCto.idNotaFiscalCtoCooperada as idNotaFiscalCtoCooperada, " +
				"moeda.dsSimbolo as dsSimbolo, " +
				"moeda.sgMoeda as sgMoeda, " +
				"nfCto.vlTotal as vlTotal)");
		
		hql.addFrom(NotaFiscalCtoCooperada.class.getName()+ " nfCto " +
				"join nfCto.ctoCtoCooperada cooperada " +
				"left outer join cooperada.conhecimento conh " +
				"left outer join conh.moeda moeda");
		
		hql.addCriteria("conh.idDoctoServico","=", idDoctoServico);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	public List findNotaFiscalByIdCooperada(Long idCooperada){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(" +
				"sum(nfCto.qtVolumes) as qtVolumes, " +
				"sum(nfCto.psMercadoria) as psMercadoria, " +
				"sum(nfCto.vlTotal) as vlTotal)");
		
		hql.addFrom(NotaFiscalCtoCooperada.class.getName()+ " nfCto " +
				"join nfCto.ctoCtoCooperada cooperada ");
		
		hql.addCriteria("cooperada.idCtoCtoCooperada","=", idCooperada);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	public Map findDadosCalculoByIdConhecimento(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(max("+PropertyVarcharI18nProjection.createProjection("np.dsNaturezaProduto")+") as dsNaturezaProduto, " +
				"sum(nfCto.vlTotal) as valorMercadoria, " +
				"max(moeda.dsSimbolo) as dsSimbolo, " +
				"sum(nfCto.psMercadoria) as pesoReal, " +
				"max(cooperada.psAforado) as psAforado, " +
				"sum(nfCto.qtVolumes) as qtVolumes, " +
				"max(cooperada.qtNfs) as qtNfs)");
		
		hql.addFrom(CtoCtoCooperada.class.getName()+ " cooperada " +
				"left outer join cooperada.conhecimento conh " +
				"left outer join conh.moeda moeda " +
				"left outer join cooperada.naturezaProduto np " +
				"left outer join cooperada.notaFiscalCtoCooperadas nfCto ");
		
		hql.addCriteria("conh.idDoctoServico","=", idDoctoServico);
		
		return (Map)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
	}
	
	public Map findOutrosByIdConhecimento(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(cooperada.dhInclusao as dhInclusao, " +
				"usuario.nmUsuario as nmUsuario, " +
				"cooperada.tpModal as tpModal)");
		
		hql.addFrom(CtoCtoCooperada.class.getName()+ " cooperada " +
				"left outer join cooperada.conhecimento conh " +
				"left outer join cooperada.usuario usuario ");
		
		hql.addCriteria("conh.idDoctoServico","=", idDoctoServico);
		
		return (Map)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
	}
	
	public List findPaginatedDadosFrete(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map("+PropertyVarcharI18nProjection.createProjection("parPre.nmParcelaPreco")+" as nmParcelaPreco, " +
				"parcela.vlParcela as vlParcela, " +
				"parcela.idParcelaCtoCooperada as idParcelaCtoCooperada, " +
				"moeda.sgMoeda as sgMoeda, " +
				"moeda.dsSimbolo as dsSimbolo, " +
				"moeda.dsSimbolo as moeda)");
		
		hql.addFrom(ParcelaCtoCooperada.class.getName()+" parcela " +
				"join parcela.parcelaPreco parPre " +
				"join parcela.ctoCtoCooperada cooperada " +
				"left outer join cooperada.conhecimento conh " +
				"left outer join conh.moeda moeda ");
		
		hql.addCriteria("conh.idDoctoServico","=",idDoctoServico);
		hql.addCriteria("parPre.tpParcelaPreco","<>","S");
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	public Map findVlFreteDadosFrete(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(cto.vlFrete as vlFrete, moeda.dsSimbolo as moeda)");
		
		hql.addFrom(CtoCtoCooperada.class.getName()+" cto " +
				"left outer join cto.conhecimento conh " +
				"left outer join conh.moeda moeda");
		
		hql.addCriteria("conh.idDoctoServico","=",idDoctoServico);
				
		return (Map)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
	}

}
