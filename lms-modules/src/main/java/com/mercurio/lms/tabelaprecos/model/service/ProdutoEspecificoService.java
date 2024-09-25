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
 * Classe de servi�o para CRUD:
 *
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tabelaprecos.produtoEspecificoService"
 */
public class ProdutoEspecificoService extends CrudService<ProdutoEspecifico, Long> {


	/**
	 * Recupera uma inst�ncia de <code>ProdutoEspecifico</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    @Override
	public ProdutoEspecifico findById(java.lang.Long id) {
        return (ProdutoEspecifico)super.findById(id);
    }

    /**
	 * Recupera uma inst�ncia de <code>ProdutoEspecifico</code> a partir do n�mero da tarifa espec�fica.
	 *
	 * @param nrTarifa representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui a nrTarifa informado.
	 * @throws
	 */
    public ProdutoEspecifico findByNrTarifa(Short nrTarifa) {
        return getProdutoEspecificoDAO().findByNrTarifa(nrTarifa);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    @Override
	public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    @Override
	public java.io.Serializable store(ProdutoEspecifico bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @param Inst�ncia do DAO.
     */
    public void setProdutoEspecificoDAO(ProdutoEspecificoDAO dao) {
        setDao( dao );
    }

    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ProdutoEspecificoDAO getProdutoEspecificoDAO() {
        return (ProdutoEspecificoDAO) getDao();
    }

    /**
     * Busca dados para a combo de produto espec�fico
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