package com.mercurio.lms.configuracoes.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.Funcionario;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FuncionarioDAO extends BaseCrudDao<Funcionario, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Funcionario.class;
    }

    protected void initFindLookupLazyProperties(Map lazyFindLookup) {
    	lazyFindLookup.put("usuario", FetchMode.JOIN);
    }

    /**
     * Busca um funcionario pelo numero da sua matricula.
     * 
     * @param nrMatricula
     * @return <code>null</code> caso o Funcionario não seja encontrado.
     * @throws org.hibernate.NonUniqueResultException caso seja encontrado mais de um Funcionario com a mesma matricula.
     */
    public Funcionario findByNrMatricula(String nrMatricula) {
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.add(Restrictions.eq("nrMatricula", nrMatricula));
    	List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    	if (result.isEmpty())
    		return null;
    	return (Funcionario) result.get(0);
    }

    
    public Funcionario findByNrIdentificacao(String nrIdentificacao) {
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.add(Restrictions.eq("nrCpf", nrIdentificacao));
    	List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    	if (result.isEmpty())
    		return null;
    	return (Funcionario) result.get(0);
    }
    
    public Funcionario findByIdUsuario(Long idUsuario) {
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.add(Restrictions.eq("usuario.idUsuario", idUsuario));
    	List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    	if (result.isEmpty())
    		return null;
    	return (Funcionario) result.get(0);
    }
    
    public List findUsuarioByFuncao(Long idFilial, String...cdFuncao) {
    	ProjectionList projections = Projections.projectionList()
			.add(Projections.property("u.idUsuario"), "idUsuario")
			.add(Projections.property("u.nmUsuario"), "nmUsuario")
			.add(Projections.property("u.dsEmail"), "usuDsEmail")
			.add(Projections.property("f.nrMatricula"), "nrMatricula")
			.add(Projections.property("f.dsEmail"), "funDsEmail")
			.add(Projections.property("f.cdFuncao"), "cdFuncao");

    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "f");
    	dc.setProjection(projections);
    	dc.createAlias("f.usuario", "u");
    	dc.createAlias("f.filial", "ff");
    	/** where */
    	dc.add(Restrictions.ne("f.tpSituacaoFuncionario", "D"));
    	dc.add(Restrictions.eq("ff.idFilial",idFilial));
    	dc.add(Restrictions.in("f.cdFuncao",cdFuncao));
    	dc.addOrder(Order.asc("f.cdFuncao"));
    	dc.setResultTransformer(new AliasToEntityMapResultTransformer());

    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
    public List findUsuarioByFilial(Long idUsuario, Long idFilial, List cdFuncao) {
    	ProjectionList projections = Projections.projectionList()
			.add(Projections.property("u.idUsuario"), "idUsuario")
			.add(Projections.property("u.nmUsuario"), "nmUsuario")
			.add(Projections.property("u.dsEmail"), "usuDsEmail")
			.add(Projections.property("f.nrMatricula"), "nrMatricula")
			.add(Projections.property("f.dsEmail"), "funDsEmail")
			.add(Projections.property("f.cdFuncao"), "cdFuncao");

    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "f");
    	dc.setProjection(projections);
    	dc.createAlias("f.usuario", "u");
    	dc.createAlias("f.filial", "ff");
    	/** where */
    	dc.add(Restrictions.ne("f.tpSituacaoFuncionario", "D"));
    	dc.add(Restrictions.eq("u.idUsuario",idUsuario));
    	dc.add(Restrictions.eq("ff.idFilial",idFilial));
    	dc.add(Restrictions.in("f.cdFuncao",cdFuncao));
    	dc.addOrder(Order.asc("f.cdFuncao"));
    	dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());

    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
    /**
     * Busca funcionarios gerentes pela regional
     * @param idFilial
     * @return
     */
    public List findGerenteByRegional(Long idRegional, String cdFuncao) {
    	StringBuilder projection = new StringBuilder()
			.append("new Map(")
			.append(" usu.idUsuario as idUsuario")
			.append(",fun.nrMatricula as nrMatricula")
			.append(",usu.nmUsuario as nmUsuario")
			.append(",usu.dsEmail as usuDsEmail")
			.append(",fun.dsEmail as funDsEmail")
			.append(",fun.cdFuncao as cdFuncao")
			.append(")");

    	StringBuilder from = new StringBuilder()
    		.append(Funcionario.class.getName()).append(" fun")
    		.append(" inner join fun.usuario as usu ")
    		.append(" inner join usu.filial as fil ")
    		.append(" inner join fil.regionalFiliais as regfil ")
    		.append(" inner join regfil.regional as reg");

    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection(projection.toString());
    	sql.addFrom(from.toString());
    	sql.addCriteria("reg.idRegional","=",idRegional);
    	sql.addCriteria("fun.tpSituacaoFuncionario","<>","D");
    	//gerente da regional
    	sql.addCriteria("fun.cdFuncao","=",cdFuncao);

    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }

    public List findUsuarioByRegional(Long idUsuario, Long idRegional, String cdFuncao) {
    	StringBuilder projection = new StringBuilder()
			.append("new Map(")
			.append(" usu.idUsuario as idUsuario")
			.append(",fun.nrMatricula as nrMatricula")
			.append(",usu.nmUsuario as nmUsuario")
			.append(",usu.dsEmail as usuDsEmail")
			.append(",fun.dsEmail as funDsEmail")
			.append(",fun.cdFuncao as cdFuncao")
			.append(")");

    	StringBuilder from = new StringBuilder()
    		.append(Funcionario.class.getName()).append(" fun")
    		.append(" inner join fun.usuario as usu ")
    		.append(" inner join usu.filial as fil ")
    		.append(" inner join fil.regionalFiliais as regfil ")
    		.append(" inner join regfil.regional as reg");

    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection(projection.toString());
    	sql.addFrom(from.toString());
    	sql.addCriteria("usu.idUsuario","=",idUsuario);
    	sql.addCriteria("reg.idRegional","=",idRegional);
    	sql.addCriteria("fun.tpSituacaoFuncionario","<>","D");
    	//gerente da regional
    	sql.addCriteria("fun.cdFuncao","=",cdFuncao);

    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }
}