package com.mercurio.lms.expedicao.model.dao;

import java.util.List;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.NovoDpeDoctoServico;

/**
 * DAO pattern.
 * <p>
 * Esta classe fornece acesso a camada de dados da aplica??o atrav?s do suporte
 * ao Hibernate em conjunto com o Spring. N?o inserir documenta??o ap?s ou
 * remover a tag do XDoclet a seguir.
 *
 * @spring.bean
 */
public class NovoDpeDoctoServicoDAO extends BaseCrudDao<NovoDpeDoctoServico, Long> {

    public NovoDpeDoctoServico findByIdDoctoServico(Long idDoctoServico) {
        StringBuilder sql = new StringBuilder()
                .append("select ds ")
                .append("from ")
                .append(getPersistentClass().getName()).append(" ds ")
                .append("where ")
                .append("ds.idDoctoServico = :idDoctoServico");
        java.util.Map<String, Object> param = new java.util.HashMap<String, Object>();
        param.put("idDoctoServico", idDoctoServico);
        NovoDpeDoctoServico dpe = (NovoDpeDoctoServico) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), param);
        return dpe;
    }

    @Override
    protected Class<NovoDpeDoctoServico> getPersistentClass() {
        return NovoDpeDoctoServico.class;
    }

}
