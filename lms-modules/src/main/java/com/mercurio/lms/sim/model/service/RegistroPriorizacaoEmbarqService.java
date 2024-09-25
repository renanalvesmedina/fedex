package com.mercurio.lms.sim.model.service;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTimeFieldType;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.sim.model.RegistroPriorizacaoEmbarq;
import com.mercurio.lms.sim.model.dao.RegistroPriorizacaoEmbarqDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
 
/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.sim.registroPriorizacaoEmbarqService"
 */
public class RegistroPriorizacaoEmbarqService extends CrudService<RegistroPriorizacaoEmbarq, Long> {
 
	private DoctoServicoService doctoServicoService;
	/**
	 * Recupera uma inst�ncia de <code>SolicitacaoRetirada</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public RegistroPriorizacaoEmbarq findById(java.lang.Long id) {
        return (RegistroPriorizacaoEmbarq)super.findById(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    @Override
    public void removeById(Long id) {
        getRegistroPriorizacaoEmbarqDAO().removeById(id);
    }

    public void cancelaRegistro(Long idReciboPriorizacaoEmbarque, String obCancelamento) {
    	if (org.apache.commons.lang.StringUtils.isBlank(obCancelamento))
    		throw new BusinessException("LMS-10034");
    	getRegistroPriorizacaoEmbarqDAO().cancelaRegistro(idReciboPriorizacaoEmbarque,obCancelamento);
    }

    public ResultSetPage findPaginated(TypedFlatMap criteria) {
    	return getRegistroPriorizacaoEmbarqDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
    }

    public Integer getRowCount(TypedFlatMap criteria) {
    	return getRegistroPriorizacaoEmbarqDAO().getRowCount(criteria);
    }

    public List findLookup(TypedFlatMap criteria) {
    	return getRegistroPriorizacaoEmbarqDAO().findLookup(criteria);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
    @Override
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
        getRegistroPriorizacaoEmbarqDAO().removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
    public RegistroPriorizacaoEmbarq beforeStore(RegistroPriorizacaoEmbarq bean) {
    	if (bean.getDhRegistroEmbarque() != null
    			&& JTDateTimeUtils.getDataHoraAtual().withField(DateTimeFieldType.secondOfMinute(),0).compareTo(bean.getDhRegistroEmbarque()) > 0)
    		throw new BusinessException("LMS-10033");

    	return super.beforeStore(bean);
    }

    public Integer getRowCountDoctoServicoEntregues(Long idDoctoServico) {
    	return doctoServicoService.getRowCountDoctoServicoEntregues(idDoctoServico);
    }

    public Integer getRowCountDoctoServicoManifesto(Long idDoctoServico) {
    	return getRegistroPriorizacaoEmbarqDAO().getRowCountDoctoServicoManifesto(idDoctoServico);
    }
    
    public List<NotaFiscalConhecimento> findNotasFiscaisByIdDocto(Long[] idDocto) {
    	return getRegistroPriorizacaoEmbarqDAO().findNotasFiscaisByIdDocto(idDocto);
    }
    
    public List<NotaFiscalConhecimento> findNotaFiscaisByIdConhecimento(Long idConhecimento) {
    	return getRegistroPriorizacaoEmbarqDAO().findNotaFiscaisByIdConhecimento(idConhecimento);
    }
    
    public Integer getRowCountDoctoServiceFaltaEntreguar(Long idRegistroPriorizacaoEmbarque) {
    	return getRegistroPriorizacaoEmbarqDAO().getRowCountDoctoServiceFaltaEntreguar(idRegistroPriorizacaoEmbarque);
    }
    
    public java.io.Serializable store(RegistroPriorizacaoEmbarq bean) {
    	bean = getRegistroPriorizacaoEmbarqDAO().updateBean(bean);
    	Serializable id = super.store(bean);
        return id;
    }
    
    @Override
    protected RegistroPriorizacaoEmbarq beforeInsert(RegistroPriorizacaoEmbarq bean) {
    	bean.setDhRegistro(JTDateTimeUtils.getDataHoraAtual());
    	return super.beforeInsert(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setSolicitacaoRetiradaDAO(RegistroPriorizacaoEmbarqDAO dao) {
        setDao(dao);
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private RegistroPriorizacaoEmbarqDAO getRegistroPriorizacaoEmbarqDAO() {
        return (RegistroPriorizacaoEmbarqDAO) getDao();
    }

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
}
