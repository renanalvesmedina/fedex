package com.mercurio.lms.rnc.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.rnc.model.FotoOcorrencia;
import com.mercurio.lms.rnc.model.NaoConformidade;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;
import com.mercurio.lms.rnc.model.dao.FotoOcorrenciaDAO;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.rnc.fotoOcorrenciaService"
 */
public class FotoOcorrenciaService extends CrudService<FotoOcorrencia, Long> {

	private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
	
	
	public Serializable storeFotoOcorrencia(FotoOcorrencia fotoOcorrencia) {
		Serializable s = this.store(fotoOcorrencia);
		
		OcorrenciaNaoConformidade onc = ocorrenciaNaoConformidadeService.findById(fotoOcorrencia.getOcorrenciaNaoConformidade().getIdOcorrenciaNaoConformidade());
		NaoConformidade nc = onc.getNaoConformidade();
			
		Long idManifesto = onc.getManifesto() != null ? onc.getManifesto().getIdManifesto() : null;
		Long idDoctoServico = nc.getDoctoServico() != null ? nc.getDoctoServico().getIdDoctoServico() : null;
		Long idMotivoAberturaNc = onc.getMotivoAberturaNc() != null ? onc.getMotivoAberturaNc().getIdMotivoAberturaNc() : null;
		Long idControleCarga = onc.getControleCarga() != null ? onc.getControleCarga().getIdControleCarga() : null;
		Long idEmpresa = onc.getEmpresa() != null ? onc.getEmpresa().getIdEmpresa() : null;
		Long idDescricaoPadraoNc = onc.getDescricaoPadraoNc() != null ? onc.getDescricaoPadraoNc().getIdDescricaoPadraoNc() : null;
		Long idFilialByFilialResponsavel = onc.getFilialByIdFilialResponsavel() != null ? onc.getFilialByIdFilialResponsavel().getIdFilial() : null;
		Long idCausaNaoConformidade = onc.getCausaNaoConformidade() != null ? onc.getCausaNaoConformidade().getIdCausaNaoConformidade() : null;
		Long idMoeda = onc.getMoeda() != null ? onc.getMoeda().getIdMoeda() : null;
		String dsOcorrenciaNc = onc.getDsOcorrenciaNc();
		Boolean blCaixaReaproveitada = onc.getBlCaixaReaproveitada();
		String dsCaixaReaproveitada = onc.getDsCaixaReaproveitada();
		String dsCausaNc = onc.getDsCausaNc();
		BigDecimal vlOcorrenciaNc = onc.getVlOcorrenciaNc();
		Integer qtVolumes = onc.getQtVolumes();
		Long idClienteDestinatarioNc = nc.getClienteByIdClienteDestinatario() != null ? nc.getClienteByIdClienteDestinatario().getIdCliente() : null;
		Long idClienteRemetenteNc = nc.getClienteByIdClienteRemetente() != null ? nc.getClienteByIdClienteRemetente().getIdCliente() : null;
		List listaNotaOcorrenciaNcs = onc.getNotaOcorrenciaNcs();
		String dsMotivoAbertura = onc.getMotivoAberturaNc() != null ? onc.getMotivoAberturaNc().getDsMotivoAbertura().toString() : null;
		List itensNFe= onc.getItemOcorrencias();
		
		ocorrenciaNaoConformidadeService.storeRNC(idManifesto, idDoctoServico, idMotivoAberturaNc, idControleCarga, idEmpresa, 
				idDescricaoPadraoNc, idFilialByFilialResponsavel, idCausaNaoConformidade, idMoeda, dsOcorrenciaNc, blCaixaReaproveitada, 
				dsCaixaReaproveitada, dsCausaNc, vlOcorrenciaNc, qtVolumes, idClienteDestinatarioNc, idClienteRemetenteNc, 
				listaNotaOcorrenciaNcs, dsMotivoAbertura, itensNFe, null, null, onc, null, null);
			
		return s;
	}	
	
	/**
	 * Recupera uma inst�ncia de <code>FotoOcorrencia</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public FotoOcorrencia findById(java.lang.Long id) {
        return (FotoOcorrencia)super.findById(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
    	getFotoOcorrenciaDAO().removeById(id, true);
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
    	getFotoOcorrenciaDAO().removeByIds(ids, true);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(FotoOcorrencia bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setFotoOcorrenciaDAO(FotoOcorrenciaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private FotoOcorrenciaDAO getFotoOcorrenciaDAO() {
        return (FotoOcorrenciaDAO) getDao();
    }

	public OcorrenciaNaoConformidadeService getOcorrenciaNaoConformidadeService() {
		return ocorrenciaNaoConformidadeService;
	}

	public void setOcorrenciaNaoConformidadeService(
			OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService) {
		this.ocorrenciaNaoConformidadeService = ocorrenciaNaoConformidadeService;
	}

	public List findByIdDoctoTpBO(Long idDoctoServico) {
		return getFotoOcorrenciaDAO().findByIdDoctoTpBO(idDoctoServico);
	}
   }