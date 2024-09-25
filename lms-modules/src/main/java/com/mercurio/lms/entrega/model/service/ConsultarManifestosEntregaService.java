package com.mercurio.lms.entrega.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.model.dao.ManifestoEntregaDAO;
import com.mercurio.lms.municipios.model.RegiaoColetaEntregaFil;
import com.mercurio.lms.util.FormatUtils;
/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.consultarManifestosEntregaService"
 */
public class ConsultarManifestosEntregaService {
	
	private ManifestoEntregaDAO manifestoEntregaDAO;
	
	public void setDAO(ManifestoEntregaDAO dao) {
		this.manifestoEntregaDAO = dao;
	}
	
	private ManifestoEntregaDAO getDAO() {
		return manifestoEntregaDAO;
	}
	
	/**
	 * Consulta as informações a serem apresentadas na grid da 'Consulta de Manifestos'.
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedConsultaManifesto(TypedFlatMap criteria) {
		ResultSetPage rsp = getDAO().findPaginatedConsultaManifesto(criteria,FindDefinition.createFindDefinition(criteria));
		
		FilterResultSetPage frsp = new FilterResultSetPage(rsp) {
			public Map filterItem(Object item) {
				Object[] obj = (Object[])item;
				Map map = new HashMap();
				map.put("idManifestoEntrega",obj[0]);
    			map.put("sgFilialManifesto",obj[1]);
    			map.put("nrManifestoEntrega",obj[2]);
    			map.put("tpManifesto",obj[3]);
    			map.put("dhEmissao",obj[4]);
    			map.put("sgFilialOrigem",obj[5]);
    			map.put("nrControleCarga",obj[6]);
    			map.put("nrFrota",obj[7]);
    			map.put("nrIdentificador",obj[8]);
    			map.put("nrFrotaSemi",obj[9]);
    			map.put("nrIdentificadorSemi",obj[10]);
    			map.put("dhSaidaColetaEntrega",obj[11]);
    			map.put("dhChegadaColetaEntrega",obj[12]);
    			map.put("dhFechamento",obj[13]);
    			map.put("tpStatusManifesto",obj[14]);
    			map.put("dsEquipe",obj[15]);
    			map.put("qtDocumentos",obj[16]);
    			map.put("nrRota",obj[17]);
    			map.put("dsRota",obj[18]);
    			map.put("dsRegiaoColetaEntregaFil",obj[19]);
    			map.put("idFilialManifesto",obj[20]);
    			map.put("nmFantasiaFilialManifesto",obj[21]);
    			return map;
			}
    	};

    	return (ResultSetPage)frsp.doFilter();
	}
        
    public Integer getRowCountConsultaManifesto(TypedFlatMap criteria) {
    	return getDAO().getRowCountConsultaManifesto(criteria);
    }
    
    /**
	 * Consulta as informações a serem apresentadas na grid da 'Consulta de Manifestos / Documentos'.
	 * Deve receber um map com informações de paginação e um Long com chave 'idManifestoEntrega'.
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedDoctosServico(TypedFlatMap criteria) {
		Long idManifestoEntrega = criteria.getLong("idManifestoEntrega");
		return getDAO().findPaginatedDoctosServico(idManifestoEntrega,FindDefinition.createFindDefinition(criteria));
	}
    
	/**
	 * 
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountDoctosServico(TypedFlatMap criteria) {
		Long idManifestoEntrega = criteria.getLong("idManifestoEntrega");
		return getDAO().getRowCountDoctosServico(idManifestoEntrega);
    }
	
	/**
	 * Consulta manifestoEntregaDocumento para apresentar no detalhamento da aba 'Documentos
	 * da tela 'Consultar Manifesto'.
	 * @param idManifestoEntregaDocumento
	 * @return
	 */
	public Map findByIdManifestoEntregaDocumento(Long idManifestoEntregaDocumento) {
		return getDAO().findByIdManifestoEntregaDocumento(idManifestoEntregaDocumento);
	}
	
    /**
     * Retorna todos os dados do detalhamento da tela 'Consultar Manifesto'.
     * O método visou performance e encontra-se bem procedural.
     * @param idManifestoEntrega
     * @return Map com os dados a serem apresentados na tela.
     */
    public Map findByIdConsultaManifesto(Long idManifestoEntrega) {
    	Map retorno = getDAO().findByIdConsultaManifesto(idManifestoEntrega);
    	
    	DomainValue tpManifestoEntrega = (DomainValue)retorno.get("tpManifestoEntrega");
    	if (tpManifestoEntrega != null)
    		retorno.put("tpManifestoEntrega",tpManifestoEntrega.getDescription().getValue());
    	DomainValue tpStatusManifesto = (DomainValue)retorno.get("tpStatusManifesto");
    	if (tpStatusManifesto != null)
    		retorno.put("tpStatusManifesto",tpStatusManifesto.getDescription().getValue());
    	
    	retorno.put("qtDocumentos",getDAO().findQtDocumentos(idManifestoEntrega));
    	retorno.put("qtEntregas",getDAO().findQtEntregas(idManifestoEntrega));
    	
    	DomainValue tpIdentificacaoCliente = (DomainValue)retorno.get("cliente_pessoa_tpIdentificacao");
    	String nrIdentificacaoCliente = (String)retorno.get("cliente_pessoa_nrIdentificacao");
		if (tpIdentificacaoCliente != null && StringUtils.isNotBlank(nrIdentificacaoCliente)) {
			retorno.put("cliente_pessoa_nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(tpIdentificacaoCliente,
					nrIdentificacaoCliente));
		}
    	
    	Long idControleCarga = (Long)retorno.get("controleCarga_idControleCarga");
    	if (idControleCarga != null) {
    		retorno.put("dsEquipe",getDAO().findDsEquipeByControleCarga(idControleCarga));
    		
    		List equipe = getDAO().findIntegrantesEquipeControleCarga(idControleCarga);
    		retorno.put("integrantesEquipe",equipe);
    		
    		List newLacres = new ArrayList();
    		List lacres = getDAO().findLacresControleCarga(idControleCarga);
    		if (!lacres.isEmpty()) {
    			Iterator iLacres = lacres.iterator();
    			while (iLacres.hasNext()) {
    				Map lacre = (Map)iLacres.next();
    				DomainValue tpStatusLacre = (DomainValue)lacre.get("tpStatusLacre");
    				String descricao = null;
    				if(lacre.get("nrLacres") != null){
    					descricao = lacre.get("nrLacres") + " - " + tpStatusLacre.getDescription().getValue();
    				} else {
    					descricao = " - " + tpStatusLacre.getDescription().getValue();
    				}
    				lacre.put("descricao",descricao);
    				newLacres.add(lacre);
    			}
    		}
    		retorno.put("lacres",newLacres);
    	}
    	
    	Long idRotaColetaEntrega = (Long)retorno.get("rotaColetaEntrega_idRotaColetaEntrega");
    	if (idRotaColetaEntrega != null) {
    		List regioes = getDAO().findRegioesRotaColetaEntrega(idRotaColetaEntrega);
    		if (!regioes.isEmpty()) {
    			RegiaoColetaEntregaFil regiao = (RegiaoColetaEntregaFil)regioes.get(0);
    			retorno.put("dsRegiao",regiao.getDsRegiaoColetaEntregaFil());
    		}
    	}
    	
    	return retorno;
    }
	
}
