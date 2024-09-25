package com.mercurio.lms.configuracoes.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.UsuarioAdsm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class UsuarioAdsmDAO extends BaseCrudDao<UsuarioAdsm, Long> {
    private static final Integer ZERO = 0;
    private static final Integer UM = 1;

    @Transactional
    protected Class getPersistentClass() {
        return UsuarioAdsm.class;
    }

    public List findSuggestUsuario(String value, Integer limiteRegistro){
        StringBuilder hql = new StringBuilder("SELECT new Map(")
                .append("u.nmUsuario AS nmUsuario, ")
                .append("u.idUsuario AS id, ")
                .append("u.login AS login, ")
                .append("u.loginIdFedex AS loginIdFedex, ")
                .append("u.nrMatricula AS nrMatricula, ")
                .append("CONCAT(u.login, ' - ', u.nmUsuario) AS usuarioSuggest) ")
                .append("FROM ").append(getPersistentClass().getName()).append(" AS u ")
                .append("WHERE 1 = 1 ");
        if(limiteRegistro != null && limiteRegistro == UM) {
            hql.append("AND (u.login = '").append(value).append("' ")
               .append("OR UPPER(u.nmUsuario) = UPPER('").append(value).append("') ");
        }else {
            hql.append("AND (u.login LIKE '%").append(value).append("%' ")
               .append("OR UPPER(u.nmUsuario) LIKE UPPER('%").append(value).append("%') ");
        }
        hql.append("OR u.loginIdFedex = '").append(value).append("' ")
           .append("OR u.nrMatricula = '").append(value).append("') ");
        if(limiteRegistro != null && limiteRegistro > ZERO){
            hql.append("AND rownum <=").append(limiteRegistro).append(" ");
        }
        return getAdsmHibernateTemplate().find(hql.toString());
    }

    public UsuarioAdsm findById(Long id) {
        return super.findById(id);
    }

}
