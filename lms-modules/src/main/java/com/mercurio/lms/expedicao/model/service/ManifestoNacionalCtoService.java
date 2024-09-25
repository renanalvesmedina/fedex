package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.dao.ManifestoNacionalCtoDAO;
import com.mercurio.lms.municipios.model.Filial;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.manifestoNacionalCtoService"
 */
public class ManifestoNacionalCtoService extends CrudService<ManifestoNacionalCto, Long> {

	/**
	 * Recupera uma instância de <code>ManifestoNacionalCto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public ManifestoNacionalCto findById(java.lang.Long id) {
		return (ManifestoNacionalCto)super.findById(id);
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

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(ManifestoNacionalCto bean) {
		getDao().store(bean, true);
		return bean.getIdManifestoNacionalCto();
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setManifestoNacionalCtoDAO(ManifestoNacionalCtoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ManifestoNacionalCtoDAO getManifestoNacionalCtoDAO() {
		return (ManifestoNacionalCtoDAO) getDao();
	}

	/**
	 * GT2
	 * Recebe um conjunto de ids para atualizar com S ou N (incluir / retirar da fronteira rápida)
	 * @param idsManifestoNacionalCto
	 */
	public void storeConhecimentosFronteiraRapida(List idsManifestoNacionalCto) {
		getManifestoNacionalCtoDAO().storeConhecimentosFronteiraRapida(idsManifestoNacionalCto);
	}

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param idConhecimento
	 * @param idManifestoViagemNacional
	 * @return ManifestoNacionalCto
	 */
	public ManifestoNacionalCto findManifestoNacionalCto(Long idConhecimento, Long idManifestoViagemNacional) {
		return getManifestoNacionalCtoDAO().findManifestoNacionalCto(idConhecimento, idManifestoViagemNacional);
	}

	public ManifestoViagemNacional findManifestoViagemAbertoByDoctoServico(Conhecimento doctoServico, Filial filial){
	    return getManifestoNacionalCtoDAO().findManifestoViagemAbertoByDoctoServico(doctoServico, filial);
	}

	/**
	 * Conhecimentos Ordenados para Impressao do Manifesto.
	 * @author Andre Valadas
	 * @param idManifestoViagemNacional
	 * @return
	 */
	public List findConhecimentos(Long idManifestoViagemNacional){
		return getManifestoNacionalCtoDAO().findConhecimentos(idManifestoViagemNacional);
	}

	public List findConhecimentosFronteiraRapida(Long idManifestoViagemNacional, Long idConhecimento, Boolean blGeraFronteiraRapida){
		// Implementar no DAO buscando de acordo com os parâmetros
		return null;
	}

	public List findManifestoNacionalCtosByIdManifestoViagemNacional(Long idManifestoViagemNacional) {
		return getManifestoNacionalCtoDAO().findManifestoNacionalCtosByIdManifestoViagemNacional(idManifestoViagemNacional);
	}

}