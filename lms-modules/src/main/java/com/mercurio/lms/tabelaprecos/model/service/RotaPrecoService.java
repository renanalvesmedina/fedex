package com.mercurio.lms.tabelaprecos.model.service;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.dao.RotaPrecoDAO;
import com.mercurio.lms.tabelaprecos.util.RotaPrecoUtils;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tabelaprecos.rotaPrecoService"
 */
public class RotaPrecoService extends CrudService<RotaPreco, Long> {


    public RotaPreco findByIdCrudService(java.lang.Long id) {
    	return findById(id);
    }

	/**
	 * Recupera uma instância de <code>RotaPreco</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public RotaPreco findById(java.lang.Long id) {
        return (RotaPreco)super.findById(id);
    }
    
    public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getRotaPrecoDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
		List result = new ArrayList(rsp.getList().size());
		for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
			Map map = (Map) iter.next();
			Map rota = AliasToNestedMapResultTransformer.getInstance().transformeTupleMap(map);
			rota.put("destinoString", getRota(map, "Destino"));
			rota.put("origemString", getRota(map, "Origem"));
			result.add(rota);
		}
		rsp.setList(result);
        return rsp;
	}

	private String getRota(Map map, String tipo) {
		StringBuffer rota = new StringBuffer("");
		VarcharI18n valueI18n = (VarcharI18n)map.get("zonaByIdZona" + tipo + "_dsZona");
		if(valueI18n != null)
			rota.append(valueI18n.getValue() + "-");
		valueI18n = (VarcharI18n)map.get("paisByIdPais" + tipo + "_nmPais");
		if(valueI18n != null)
			rota.append(valueI18n.getValue() + "-");
		String value = (String)map.get("unidadeFederativaByIdUf" + tipo + "_sgUnidadeFederativa");
		if(StringUtils.isNotBlank(value))
			rota.append(value + "-");
		value = (String)map.get("filialByIdFilial" + tipo + "_sgFilial");
		if(StringUtils.isNotBlank(value))
			rota.append(value + "-");
		value = (String)map.get("municipioByIdMunicipio" + tipo + "_nmMunicipio");
		if(StringUtils.isNotBlank(value))
			rota.append(value + "-");
		value = (String)map.get("aeroportoByIdAeroporto" + tipo + "_sgAeroporto");
		if(StringUtils.isNotBlank(value))
			rota.append(value + "-");
		valueI18n = (VarcharI18n)map.get("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + "_dsTipoLocalizacaoMunicipio");
		if(valueI18n != null)
			rota.append(valueI18n.getValue() + "-");
		
		String grupoRegiao = (String)map.get("grupoRegiao"+tipo+"_dsGrupoRegiao");
		if(StringUtils.isNotBlank(grupoRegiao)){
			rota.append(grupoRegiao  + "-");
		}
		
		if(rota.length() > 0)
			return rota.substring(0, rota.length() - 1);
		return "";
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

    protected RotaPreco beforeStore(RotaPreco bean) {
    	RotaPreco rp = (RotaPreco)bean;

    	//Valida a rota de preço
    	validaRota(rp);

    	return super.beforeStore(bean);
    }

    private void validaRota(RotaPreco rp) {

    	RestricaoRota restricaoRotaOrigem = RotaPrecoUtils.getRestricaoRotaOrigem(rp);
    	boolean isRotaOrigemValida = RotaPrecoUtils.isRotaValida(restricaoRotaOrigem);

    	RestricaoRota restricaoRotaDestino = RotaPrecoUtils.getRestricaoRotaDestino(rp);
    	boolean isRotaDestinoValida = RotaPrecoUtils.isRotaValida(restricaoRotaDestino);

    	if (!(isRotaOrigemValida && isRotaDestinoValida)){
    		throw new BusinessException("LMS-30001");
    	}

    }

    public boolean checkRotaByRestricaoRotaOrigemDestino(RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino){
    	return getRotaPrecoDAO().checkRotaByRestricaoRotaOrigemDestino(restricaoRotaOrigem, restricaoRotaDestino);
    }
    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(RotaPreco bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setRotaPrecoDAO(RotaPrecoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private RotaPrecoDAO getRotaPrecoDAO() {
        return (RotaPrecoDAO) getDao();
    }
    
    public List findLookup(Map criteria){
		return super.findLookup(criteria);
    }

    public Map findRotaById(Long idRota) {
    	return getRotaPrecoDAO().findRotaById(idRota);
    }
    
    public RotaPreco findRotaPrecoParaImportacaoTabelaPreco(Map<String, Object> parametros) {
    	return getRotaPrecoDAO().findRotaPrecoParaImportacaoTabelaPreco(parametros);
    }
    
    public List<RotaPreco> findRotaPrecoParaMarkup(Long idTabelaPreco, String sgUnidadeFederativaOrigem, String sgAeroportoOrigem, String sgUnidadeFederativaDestino, String sgAeroportoDestino, boolean minimoProgressivo) {
    	return getRotaPrecoDAO().findRotaPrecoParaMarkup(idTabelaPreco, sgUnidadeFederativaOrigem, sgAeroportoOrigem, sgUnidadeFederativaDestino, sgAeroportoDestino, minimoProgressivo);
    }

	public RotaPreco findRotaPrecoByIdTarifaPrecoAndIdTabelaPreco(Long idTarifaPreco, Long idTabelaPreco) {
		return getRotaPrecoDAO().findRotaPrecoByIdTarifaPrecoAndIdTabelaPreco(idTarifaPreco, idTabelaPreco);
	}
}
