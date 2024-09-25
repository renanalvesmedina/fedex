package com.mercurio.lms.configuracoes.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.AjusteDB;
import com.mercurio.lms.configuracoes.model.service.AjusteContingenciaService;

public class ManterAjusteContingenciaAction extends CrudAction {

    private AjusteContingenciaService ajusteContingenciaService;

    public int executeQuery(Map<String, Object> criteria) {
        return ajusteContingenciaService.executeQuery(criteria);
    }

    public Map<String, Object> findById(Long id) {
        return ajusteContingenciaService.generateMapById(id);
    }

    @Override
    public ResultSetPage<Map<String, Object>> findPaginated(@SuppressWarnings("rawtypes") Map criteria) {
        ResultSetPage<AjusteDB> resultSet = ajusteContingenciaService.findPaginated(criteria);
        List<Map<String, Object>> queries = new ArrayList<Map<String, Object>>();

        for (AjusteDB query : getResultsetAsList(resultSet)) {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("idAjusteDb", query.getIdAjusteDB());
            queryMap.put("nmAjuste", query.getNmAjuste());
            queries.add(queryMap);
        }

        return new ResultSetPage<Map<String, Object>>(resultSet.getCurrentPage(), queries);
    }

    public Integer getRowCount() {
        return ajusteContingenciaService.getRowCount();
    }

    private List<AjusteDB> getResultsetAsList(ResultSetPage<AjusteDB> resultSet) {
        if (resultSet != null) {
            return resultSet.getList();
        }

        return new ArrayList<AjusteDB>();
    }

    public void setAjusteContingenciaService(AjusteContingenciaService ajusteContingenciaService) {
        this.ajusteContingenciaService = ajusteContingenciaService;
    }

}
