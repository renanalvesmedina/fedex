package com.mercurio.lms.workflow.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.workflow.model.GrupoAprovadores;
import com.mercurio.lms.workflow.model.UsuarioAprovador;
import com.mercurio.lms.workflow.model.dto.GrupoAprovadoresDTO;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class GrupoAprovadoresDAO extends BaseCrudDao<GrupoAprovadores, Long> {

    private static final String AND_TP_SITUACAO= "AND ga.tpSituacao = '";

    /**
     * Nome da classe que o DAO é responsável por persistir.
     */
    protected Class getPersistentClass() {
        return GrupoAprovadores.class;
    }

    public List findAllGruposAprovadores(GrupoAprovadoresDTO filter){
        StringBuilder hql = getSelectBase();
        hql.append("WHERE ga.tpSituacao = '").append(filter.getTpSituacao()).append("'");
        return getAdsmHibernateTemplate().find(hql.toString());
    }

    public List findByNomeUsuarios(GrupoAprovadoresDTO filter){
        StringBuilder hql = getSelectBase();
        hql.append("WHERE ga.usuario.nmUsuario LIKE '%").append(filter.getNmUsuario()).append("%' ")
                .append("OR ga.usuario.login LIKE '%").append(filter.getNmUsuario()).append("%' ")
                .append(AND_TP_SITUACAO).append(filter.getTpSituacao()).append("'");
        return getAdsmHibernateTemplate().find(hql.toString());
    }

    public List findByDescGrupoAprovadores(GrupoAprovadoresDTO filter){
        StringBuilder hql = getSelectBase();
        hql.append("WHERE LOWER(ga.perfil.dsPerfil) LIKE '%").append(filter.getDsPerfil().toLowerCase()).append("%' ")
                .append(AND_TP_SITUACAO).append(filter.getTpSituacao()).append("'");
        return getAdsmHibernateTemplate().find(hql.toString());
    }

    public List findByNomeUsuarioAndDescGrupoAprovadores(GrupoAprovadoresDTO filter){
        StringBuilder hql = getSelectBase();
        hql.append("WHERE ga.usuario.nmUsuario LIKE '%").append(filter.getNmUsuario()).append("%' ")
                .append("OR ga.usuario.login LIKE '%").append(filter.getNmUsuario()).append("%' ")
                .append("AND LOWER(ga.perfil.dsPerfil) LIKE '%").append(filter.getDsPerfil().toLowerCase()).append("%' ")
                .append(AND_TP_SITUACAO).append(filter.getTpSituacao()).append("'");
        return getAdsmHibernateTemplate().find(hql.toString());
    }

    public List findByIdUsuarioPerfilUsuario(Long idUsuario){
        StringBuilder hql = getSelectBase()
                .append("WHERE ga.usuario.id = ").append(idUsuario);
        return getAdsmHibernateTemplate().find(hql.toString());
    }

    public List findByIdUsuarioAndIdPerfilUsuario(Long idUsuario, Long idPerfil){
        StringBuilder hql = getSelectBase()
                .append("WHERE ga.usuario.id = ").append(idUsuario)
                .append(" AND pe.idPerfil = ").append(idPerfil);
        return getAdsmHibernateTemplate().find(hql.toString());
    }

    public boolean storeGrupoAprovadores(final List persistence){
        getAdsmHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                for (int i = 0; i < persistence.size(); i++) {
                    final GrupoAprovadores grupo = (GrupoAprovadores) persistence.get(i);
                    session.save(grupo);
                }
                return persistence;
            }
        });
        return true;
    }

    public void executeRemoveById(Long id){
        StringBuilder hql = new StringBuilder()
                .append("delete from GrupoAprovadores where idPerfilUsuario = ").append(id);
        executeHql(hql.toString(), new ArrayList());
    }

    private StringBuilder getSelectBase(){
        return new StringBuilder()
                .append("select new Map(")
                .append("ga.idPerfilUsuario as idPerfilUsuario, ")
                .append("pe.idPerfil as idPerfil, ")
                .append("pe.dsPerfil as dsPerfil, ")
                .append("pe.tpSituacao as tpSituacaoPerfil, ")
                .append("us.idUsuario as idUsuario, ")
                .append("us.nmUsuario as nmUsuario, ")
                .append("us.login as login, ")
                .append("us.dsEmail as dsEmail, ")
                .append("ga.tpSituacao as tpSituacao, ")
                .append("concat(us.login, ' - ', us.nmUsuario) AS usuarioSuggest) ")
                .append("from ").append(getPersistentClass().getName()).append(" as ga ")
                .append("join ga.perfil as pe ")
                .append("join ga.usuario as us ");

    }

    public List findUsuarioEqualLogin(String usuario){
        StringBuilder hql = getSelectBase()
                .append("WHERE LOWER(us.login) = '").append(usuario.toLowerCase()).append("'");
        return getAdsmHibernateTemplate().find(hql.toString());

    }

    public List findSuggestUsuario(String login){
        StringBuilder hql = new StringBuilder()
                .append("SELECT new Map(")
                .append("u.nmUsuario AS nmUsuario, ")
                .append("u.login AS login, ")
                .append("CONCAT(u.login, ' - ', u.nmUsuario) AS usuarioSuggest) ")
                .append("FROM ").append(UsuarioAprovador.class.getName()).append(" AS u ")
                .append("WHERE u.login LIKE '%").append(login).append("%'");
        return getAdsmHibernateTemplate().find(hql.toString());
    }

    public List<Map<String, String>> getValorDominio(){

        final StringBuilder hql = new StringBuilder()
                .append("SELECT VD.VL_VALOR_DOMINIO AS VALOR, ")
                .append("SUBSTR(REGEXP_SUBSTR(VD.DS_VALOR_DOMINIO_I, 'pt_BR»[^¦]+'), ")
                .append("INSTR(REGEXP_SUBSTR(VD.DS_VALOR_DOMINIO_I, 'pt_BR»[^¦]+'), 'pt_BR»')+")
                .append("LENGTH('pt_BR»')) AS DESCRICAO ")
                .append("FROM DOMINIO D, VALOR_DOMINIO VD ")
                .append("WHERE D.ID_DOMINIO = VD.ID_DOMINIO ")
                .append("AND D.NM_DOMINIO = 'DM_STATUS'");

        final HibernateCallback hcb = new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws SQLException {
                SQLQuery query = session.createSQLQuery(hql.toString());
                return query.list();
            }
        };
        return (List) getHibernateTemplate().execute(hcb);

    }

}
