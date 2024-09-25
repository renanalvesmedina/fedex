package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.InformacaoDocServico;
import com.mercurio.lms.expedicao.model.dao.InformacaoDocServicoDAO;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.informacaoDocServicoService"
 */
public class InformacaoDocServicoService extends CrudService<InformacaoDocServico, Long> {

	public InformacaoDocServico findById(java.lang.Long id) {
		return (InformacaoDocServico)super.findById(id);
	}

	/**
	 * @author Andre Valadas
	 * 
	 * @param dsTipoRegistroComplemento
	 * @param dsCampo
	 * @return InformacaoDocServico
	 */
	public InformacaoDocServico findInformacaoDoctoServico(String dsTipoRegistroComplemento, String dsCampo) {
		return getInformacaoDocServicoDAO().findInformacaoDoctoServico(dsTipoRegistroComplemento, dsCampo);
	}

	public InformacaoDocServico findByDsCampo(String dsCampo) {
		return getInformacaoDocServicoDAO().findByDsCampo(dsCampo);
	}

	public InformacaoDocServico findByDsCampoEqualToDsTipoRegistroComplemento(String dsCampo) {
		return getInformacaoDocServicoDAO().findByDsCampoEqualToDsTipoRegistroComplemento(dsCampo);
	}

	public void setInformacaoDocServicoDAO(InformacaoDocServicoDAO dao) {
		setDao( dao );
	}
	private final InformacaoDocServicoDAO getInformacaoDocServicoDAO() {
		return (InformacaoDocServicoDAO) getDao();
	}
}