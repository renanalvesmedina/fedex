package com.mercurio.lms.expedicao.edi.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.edi.model.NotaFiscalEdiVolume;
import com.mercurio.lms.edi.model.dao.NotaFiscalEdiVolumeDAO;

/**
 * Classe de servi�o para CRUD: 
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.edi.notaFiscalExpedicaoEDIVolumeService"
 */
public class NotaFiscalExpedicaoEDIVolumeService extends CrudService<NotaFiscalEdiVolume, Long> {

	/**
	 * Recupera uma inst�ncia de <code>NotaFiscalEdiVolume</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public NotaFiscalEdiVolume findById(java.lang.Long id) {
		return (NotaFiscalEdiVolume)super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	public void removeByIdNotaFiscalEdi(List<Long> list) {
		getNotaFiscalEdiVolumeDAO().removeByIdNotaFiscalEdi(list);
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
	public java.io.Serializable store(NotaFiscalEdiVolume bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setNotaFiscalEdiVolumeDAO(NotaFiscalEdiVolumeDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private NotaFiscalEdiVolumeDAO getNotaFiscalEdiVolumeDAO() {
		return (NotaFiscalEdiVolumeDAO) getDao();
	}

	public List findAllEntities() {
		return getNotaFiscalEdiVolumeDAO().findAllEntities();
	}
	
	public List findByCodigoVolumeByIdNotaFiscalEdi(String codigoVolume, Long idNotaFiscalEdi) {
		return getNotaFiscalEdiVolumeDAO().findByCodigoVolumeByIdNotaFiscalEdi(codigoVolume, idNotaFiscalEdi);
	}

	public List<NotaFiscalEdiVolume> findByIdNotaFiscalEdi(Long idNotaFiscalEdi) {
		return getNotaFiscalEdiVolumeDAO().findByIdNotaFiscalEdi(idNotaFiscalEdi);
	}
	
	public boolean existsVolumePendenteGM(final Long idNotaFiscalEdi){
		return getNotaFiscalEdiVolumeDAO().existsVolumePendenteGM(idNotaFiscalEdi);
	}

	/**
     * Referente ao jira LMS-2784
     * 
     * Retorna o(s) carregamento(s) relacionadas ao volumes das notas fiscais volume passado como parametro.
     * Resultado utilizado para verificar se todos os volumes das notas fiscais que foram selecionadas para atualiza��o perten�am ao mesmo carregamento.
     * Caso a lista retornada tenha o size >1 existem volumes com carregamentos diferentes
     * 
     * @param idsNotaFiscalEdi
     * @return
     */
	public List findIdCarregamentoPlacaERota(List<Long> idsNotaFiscalEdi) {
		return getNotaFiscalEdiVolumeDAO().findIdCarregamentoPlacaERota(idsNotaFiscalEdi);
	}
	
	
}