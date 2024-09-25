package com.mercurio.lms.configuracoes.model.dao;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.lms.configuracoes.model.PessoaLog;
import com.mercurio.lms.util.FormatUtils;

/**
 * @spring.bean 
 */
public class PessoaLogDAO extends BaseCrudDao<PessoaLog, Long> {

	protected final Class getPersistentClass() {
		return PessoaLog.class;
	}	
	
	@Override
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		ResultSetPage rsp = super.findPaginated(criteria, findDef);
		
		FilterResultSetPage filter = new FilterResultSetPage(rsp){
			public Map filterItem(Object item){
				PessoaLog log = (PessoaLog) item;
				HashMap map = new HashMap();
				map.put("idPessoaLog", log.getIdPessoaLog());
				map.put("idPessoa", log.getPessoa().getIdPessoa());
				map.put("blAtualizacaoCountasse", log.getBlAtualizacaoCountasse());
				map.put("dhInclusao", log.getDhInclusao());
				map.put("dsEmail", log.getDsEmail());
				map.put("dsOrgaoEmissorRg", log.getDsOrgaoEmissorRg());
				map.put("dtEmissaoRg", log.getDtEmissaoRg());
				map.put("nmFantasia", log.getNmFantasia());
				map.put("nmPessoa", log.getNmPessoa());
				map.put("nrIdentificacao", FormatUtils.formatIdentificacao(log.getTpIdentificacao(),log.getNrIdentificacao()));
				map.put("nrInscricaoMunicipal", log.getNrInscricaoMunicipal());
				map.put("nrRg", log.getNrRg());
				map.put("tpIdentificacao", log.getTpIdentificacao());
				
				
				map.put("dhLog", log.getDhLog());
				map.put("loginLog", log.getLoginLog());
				map.put("opLog", log.getOpLog());
				map.put("tpOrigemLog", log.getTpOrigemLog());
				return map;
			}
		};
		return (ResultSetPage)filter.doFilter();
	}
}
