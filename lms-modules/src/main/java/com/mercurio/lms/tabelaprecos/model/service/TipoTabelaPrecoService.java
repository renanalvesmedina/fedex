package com.mercurio.lms.tabelaprecos.model.service;

import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.tabelaprecos.model.TipoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.dao.TipoTabelaPrecoDAO;
import com.mercurio.lms.util.session.SessionKey;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tabelaprecos.tipoTabelaPrecoService"
 */
public class TipoTabelaPrecoService extends CrudService<TipoTabelaPreco, Long> {

	public List findTipoTabelaPrecoAtivo() {
		List l = getTipoTabelaPrecoDAO().findAtivos();
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			TypedFlatMap map = (TypedFlatMap) iter.next();
			String	tpTipoTabelaPrecoNrVersao = map.getString("tpTipoTabelaPreco.value");
			Integer nrVersao = map.getInteger("nrVersao");
			if(nrVersao != null)
				tpTipoTabelaPrecoNrVersao += nrVersao;
			map.put("tpTipoTabelaPrecoNrVersao", tpTipoTabelaPrecoNrVersao);
		}
		return l;
	}

	/**
	 * Recupera uma inst�ncia de <code>TipoTabelaPreco</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public TipoTabelaPreco findById(java.lang.Long id) {
        return (TipoTabelaPreco)super.findById(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
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
    public java.io.Serializable store(TipoTabelaPreco bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setTipoTabelaPrecoDAO(TipoTabelaPrecoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private TipoTabelaPrecoDAO getTipoTabelaPrecoDAO() {
        return (TipoTabelaPrecoDAO) getDao();
    }
    
    
    /**
	 * @see com.mercurio.adsm.framework.model.CrudService#beforeStore(java.lang.Object)
	 */
	protected TipoTabelaPreco beforeStore(TipoTabelaPreco tpp) {
		super.beforeStore(tpp);
		tpp.setEmpresaByIdEmpresaLogada((Empresa)SessionContext.get(SessionKey.EMPRESA_KEY));
		return tpp;
	}
	
	/**
	 * Retorna todas as tabelas de preco que possuam a situacao fornecida e que
	 * <strong>NAO<strong> sejam do tipo fornecido.
	 * 
	 * @param tpSituacao
	 * @param tpTipoTabelaPreco
	 * @return
	 */
	public List findByTpSituacaoNotTpTipoTabelaPreco(String tpSituacao, String tpTipoTabelaPreco) {
		return getTipoTabelaPrecoDAO().findByTpSituacaoNotTpTipoTabelaPreco(tpSituacao, tpTipoTabelaPreco);
	}
	
	/**
	 * Retorna todas as tabelas de preco que possuam a situacao fornecida e que
	 * sejam do tipo fornecido.
	 * 
	 * @param tpSituacao
	 * @param tpTipoTabelaPreco
	 * @return
	 */
	public List findByTpSituacaoTpTipoTabelaPreco(String tpSituacao, String tpTipoTabelaPreco) {
		return getTipoTabelaPrecoDAO().findByTpSituacaoTpTipoTabelaPreco(tpSituacao, tpTipoTabelaPreco);
	}
	
	public TipoTabelaPreco findByTipoTabelaBase(String tpTipoTabelaPreco, Integer nrVersao, Long idEmpresaCadastrada, Long idServico, Long idCliente) {
		return getTipoTabelaPrecoDAO().findByTipoTabelaBase(tpTipoTabelaPreco, nrVersao, idEmpresaCadastrada, idServico, idCliente);
	}

	public Integer findByTpTipoTabelaPrecoUltimoNrVersao(String tpTipoTabelaPreco) {
		return getTipoTabelaPrecoDAO().findByTpTipoTabelaPrecoUltimoNrVersao(tpTipoTabelaPreco);
	}

	public boolean findNrVersaoNaoUtilizada(String tpTipoTabelaPreco, int nrVersao){
		boolean retorno = getTipoTabelaPrecoDAO().findNrVersaoNaoUtilizada(tpTipoTabelaPreco, nrVersao);
		if(retorno){
			return true;
		} else {
			return false;
		}
	}
}