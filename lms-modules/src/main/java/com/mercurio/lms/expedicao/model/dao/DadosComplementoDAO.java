package com.mercurio.lms.expedicao.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.DadosComplemento;

/**
 * DAO pattern.
 * <p>
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 *
 * @spring.bean
 */
public class DadosComplementoDAO extends BaseCrudDao<DadosComplemento, Long> {

    private static final String ID_CONHECIMENTO = "idConhecimento";

    /**
     * Nome da classe que o DAO é responsável por persistir.
     */
    protected final Class getPersistentClass() {
        return DadosComplemento.class;
    }

    public List findIdsByIdConhecimento(Long idConhecimento) {
        StringBuilder query = new StringBuilder()
                .append("select pojo.idDadosComplemento ")
                .append("from ").append(DadosComplemento.class.getName()).append(" as pojo ")
                .append("join pojo.conhecimento as c ")
                .append("where c.id = :idConhecimento");

        return getAdsmHibernateTemplate().findByNamedParam(query.toString(), ID_CONHECIMENTO, idConhecimento);
    }

    public DadosComplemento findByIdConhecimentoTpRegistro(Long idConhecimento, String tpRegistro) {
    	return findByIdConhecimentoTpRegistro(idConhecimento, new String[] {tpRegistro});
    }
    
    public DadosComplemento findByIdConhecimentoTpRegistro(Long idConhecimento, String[] tpRegistro) {
        DetachedCriteria dc = DetachedCriteria.forClass(DadosComplemento.class, "dc")
                .createAlias("dc.conhecimento", "con")
                .createAlias("dc.informacaoDoctoCliente", "idc")
                .add(Restrictions.eq("con.id", idConhecimento))
                .add(Restrictions.in("idc.dsCampo", tpRegistro));

        List<DadosComplemento> list = findByDetachedCriteria(dc);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public DadosComplemento findByIdConhecimentoDocServico(Long idConhecimento, String dsCampo) {
        DetachedCriteria dc = DetachedCriteria.forClass(DadosComplemento.class, "dc")
                .createAlias("dc.conhecimento", "con")
                .createAlias("dc.informacaoDocServico", "ids")
                .add(Restrictions.eq("con.id", idConhecimento))
                .add(Restrictions.eq("ids.dsCampo", dsCampo));

        List<DadosComplemento> list = findByDetachedCriteria(dc);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public DadosComplemento findByIdConhecimentoDocCliente(Long idConhecimento, String dsCampo) {
        StringBuilder query = new StringBuilder()
                .append("  from ").append(DadosComplemento.class.getName()).append(" as dc ")
                .append("    join dc.informacaoDoctoCliente as idc ")
                .append(" where dc.conhecimento.id = ? ")
                .append("   and idc.blIndicadorNotaFiscal = 'N' ")
                .append("   and idc.dsCampo = ?");

        return (DadosComplemento) getAdsmHibernateTemplate().findUniqueResult(query.toString(), new Object[]{idConhecimento, dsCampo});
    }

    public List<Map<String, Object>> findAllDadosComplementoByIdConhecimentoDsCampos(Long idConhecimento, List<String> dsCampos) {
        StringBuilder query = new StringBuilder()
                .append("select new map( idc.dsCampo as key, dc.dsValorCampo as value ) ")
                .append("  from ").append(DadosComplemento.class.getName()).append(" as dc ")
                .append("    inner join dc.informacaoDoctoCliente as idc ")
                .append(" where dc.conhecimento.id = :idConhecimento ")
                .append("   and idc.dsCampo in (:dsCampos)")
                .append("union ")
                .append("select new map( idc.dsCampo as key, dc.dsValorCampo as value ) ")
                .append("  from ").append(DadosComplemento.class.getName()).append(" as dc ")
                .append("    inner join dc.informacaoDoctoServico as ids ")
                .append(" where dc.conhecimento.id = :idConhecimento ")
                .append("   and ids.dsCampo in (:dsCampos)");
        return getAdsmHibernateTemplate().findByNamedParam(query.toString(), new String[]{ID_CONHECIMENTO, "dsCampos"}, new Object[]{idConhecimento, dsCampos});
    }

    public List<Map<String, Object>> findByIdConhecimentoDocCliente(Long idConhecimento, List<String> dsCampos) {
        StringBuilder query = new StringBuilder()
                .append("select new map( idc.dsCampo as key, dc.dsValorCampo as value ) ")
                .append("  from ").append(DadosComplemento.class.getName()).append(" as dc ")
                .append("    left outer join dc.informacaoDoctoCliente as idc ")
                .append(" where dc.conhecimento.id = :idConhecimento ")
                .append("   and idc.dsCampo in (:dsCampos)");

        return getAdsmHibernateTemplate().findByNamedParam(query.toString(), new String[]{ID_CONHECIMENTO, "dsCampos"}, new Object[]{idConhecimento, dsCampos});
    }

    public DadosComplemento findByIdNFDocCliente(Long idNotaFiscalConhecimento, String dsCampo) {
        StringBuilder query = new StringBuilder()
                .append("select dc ")
                .append("  from ").append(DadosComplemento.class.getName()).append(" as dc ")
                .append("  join dc.nfDadosComps as nfdc ")
                .append(" where nfdc.notaFiscalConhecimento.id = ? ")
                .append("   and dc.informacaoDoctoCliente.dsCampo = ?");

        return (DadosComplemento) getAdsmHibernateTemplate().findUniqueResult(query.toString(), new Object[]{idNotaFiscalConhecimento, dsCampo});
    }

    public List findByConhecimento(Long idConhecimento) {
        DetachedCriteria dc = createDetachedCriteria()
                .setFetchMode("informacaoDoctoCliente", FetchMode.JOIN)
                .add(Restrictions.eq("conhecimento.id", idConhecimento));
        return findByDetachedCriteria(dc);
    }
    
	@SuppressWarnings("unchecked")
	public List<DadosComplemento> findVolTotalizadorByConhecimento(Long idConhecimento) {
		StringBuilder query = new StringBuilder()
	        .append("select dc ")
	        .append("from ").append(DadosComplemento.class.getName()).append(" as dc ")
	        .append("join dc.conhecimento as c ")
	        .append("join dc.informacaoDoctoCliente as idc ")
	        .append("where c.id = :idConhecimento")
			.append(" and idc.dsCampo = :dsCampo");
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(ID_CONHECIMENTO, idConhecimento);
		parameters.put("dsCampo", "VOLTOTALIZ");
		
		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parameters);
	}
}