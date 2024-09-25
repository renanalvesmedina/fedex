package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.AjusteDB;
import com.mercurio.lms.configuracoes.model.AjusteDBBind;
import com.mercurio.lms.configuracoes.model.dao.AjusteContingenciaDAO;

public class AjusteContingenciaService extends CrudService<AjusteDB, Long> {

    @SuppressWarnings("unchecked")
    public int executeQuery(Map<String, Object> criteria) {
        if (criteria != null && criteria.containsKey("idAjusteDb")) {
            AjusteDB ajuste = findById(Long.parseLong(criteria.get("idAjusteDb").toString()));
            Map<String, Object> parameters = new HashMap<String, Object>();

            if (criteria.containsKey("binds")) {
                List<Map<String, Object>> binds = (List<Map<String, Object>>) criteria.get("binds");

                for (Map<String, Object> bind : binds) {
                    validateBind(bind);
                    parameters.put(getBindNameById(Long.parseLong(bind.get("idAjusteDbBind").toString())), bind.get("vlBind"));
                }
            }

            return getAjusteContingenciaDAO().executeQuery(ajuste.getDsConteudo(), parameters);
        }

        return 0;
    }

    private void validateBind(Map<String, Object> bind) {
        if (!bind.containsKey("vlBind") || bind.get("vlBind") == null
                || bind.get("vlBind").toString().trim().equals("")) {
            throw new BusinessException("LMS-27114");
        }
    }

    private String getBindNameById(Long id) {
        return getAjusteContingenciaDAO().findBindById(id).getNmBind();
    }

    @Override
    public AjusteDB findById(Long id) {
        return (AjusteDB) super.findById(id);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ResultSetPage<AjusteDB> findPaginated(Map criteria) {
        return getAjusteContingenciaDAO().findPaginated(criteria);
    }

    public Integer getRowCount() {
        return getAjusteContingenciaDAO().getRowCount();
    }

    public Map<String, Object> generateMapById(Long id) {
        AjusteDB ajuste = findById(id);

        if (ajuste != null) {
            Map<String, Object> ajusteMap = new HashMap<String, Object>();
            List<Map<String, Object>> binds = new ArrayList<Map<String,Object>>();

            if (ajuste.getBinds() != null) {
                for (AjusteDBBind bind : ajuste.getBinds()) {
                    Map<String, Object> bindMap = new HashMap<String, Object>();
                    bindMap.put("idAjusteDbBind", bind.getIdAjusteDBBind());
                    bindMap.put("nmBind", bind.getNmBind());
                    bindMap.put("nrTipo", bind.getNrTipo().getChave());
                    binds.add(bindMap);
                }
            }

            ajusteMap.put("idAjusteDb", ajuste.getIdAjusteDB());
            ajusteMap.put("nmAjuste", ajuste.getNmAjuste());
            ajusteMap.put("dsConteudo", ajuste.getDsConteudo());
            ajusteMap.put("blExecutaTrigger", ajuste.getBlExecutaTrigger());
            ajusteMap.put("binds", binds);

            return ajusteMap;
        }

        return null;
    }

    private AjusteContingenciaDAO getAjusteContingenciaDAO() {
        return (AjusteContingenciaDAO) getDao();
    }

    public void setAjusteContingenciaDAO(AjusteContingenciaDAO ajusteContingenciaDAO) {
        setDao(ajusteContingenciaDAO);
    }

}
