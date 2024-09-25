package com.mercurio.lms.tabelaprecos.model.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico;
import com.mercurio.lms.tabelaprecos.model.dao.ProdutoEspecificoDAO;
import com.mercurio.lms.util.FormatUtils;

/**
 * Classe de serviço para CRUD:
 *
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tabelaprecos.produtoEspecificoService"
 */
public class ProdutoEspecificoService extends CrudService<ProdutoEspecifico, Long> {


	/**
	 * Recupera uma instância de <code>ProdutoEspecifico</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    @Override
	public ProdutoEspecifico findById(java.lang.Long id) {
        return (ProdutoEspecifico)super.findById(id);
    }

    /**
	 * Recupera uma instância de <code>ProdutoEspecifico</code> a partir do número da tarifa específica.
	 *
	 * @param nrTarifa representa a entidade que deve ser localizada.
	 * @return Instância que possui a nrTarifa informado.
	 * @throws
	 */
    public ProdutoEspecifico findByNrTarifa(Short nrTarifa) {
        return getProdutoEspecificoDAO().findByNrTarifa(nrTarifa);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    @Override
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
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    @Override
	public java.io.Serializable store(ProdutoEspecifico bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     *
     * @param Instância do DAO.
     */
    public void setProdutoEspecificoDAO(ProdutoEspecificoDAO dao) {
        setDao( dao );
    }

    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ProdutoEspecificoDAO getProdutoEspecificoDAO() {
        return (ProdutoEspecificoDAO) getDao();
    }

    /**
     * Busca dados para a combo de produto específico
     *
     * @return
     */
	public List findAllAtivo() {
		String nrTarifaEspecifica = null;
		List result  = getProdutoEspecificoDAO().findAllAtivo();
		for (Iterator iter = result.iterator(); iter.hasNext();) {
			Map map = (Map) iter.next();
			nrTarifaEspecifica = FormatUtils.formatDecimal("000", (Number)map.get("nrTarifaEspecifica"));
			map.put("nrTarifaEspecifica", nrTarifaEspecifica);
			map.put("nmTarifaEspecifica", nrTarifaEspecifica + "-" + map.get("dsProdutoEspecifico"));
		}
		return result;
	}
}