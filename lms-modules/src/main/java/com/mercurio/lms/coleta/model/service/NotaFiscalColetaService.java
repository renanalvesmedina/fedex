package com.mercurio.lms.coleta.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.coleta.model.NotaFiscalColeta;
import com.mercurio.lms.coleta.model.dao.NotaFiscalColetaDAO;
import com.mercurio.lms.configuracoes.model.Contato;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.coleta.notaFiscalColetaService"
 */
public class NotaFiscalColetaService extends CrudService<NotaFiscalColeta, Long> {


	/**
	 * Recupera uma inst�ncia de <code>NotaFiscalColeta</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public NotaFiscalColeta findById(java.lang.Long id) {
        return (NotaFiscalColeta)super.findById(id);
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
    public java.io.Serializable store(NotaFiscalColeta bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setNotaFiscalColetaDAO(NotaFiscalColetaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private NotaFiscalColetaDAO getNotaFiscalColetaDAO() {
        return (NotaFiscalColetaDAO) getDao();
    }
    
	/**
	 * Apaga uma entidade atrav�s do Id do Detalhe de Coleta.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeByIdDetalheColeta(Long idDetalheColeta) {
        this.getNotaFiscalColetaDAO().removeByIdDetalheColeta(idDetalheColeta);
    }    
    
    /**
     * Busca os contatos da nrChave passada por parametro
     * 
     * @param nrChave
     * @return
     */
	public List<Contato> findContatosFromNotaFiscalColeta(String nrChave) {
		return getNotaFiscalColetaDAO().findContatosFromNotaFiscalColeta(nrChave);
	}
}