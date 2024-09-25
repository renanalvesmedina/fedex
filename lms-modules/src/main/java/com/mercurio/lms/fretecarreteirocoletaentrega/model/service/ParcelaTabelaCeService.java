package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.coleta.model.ManifestoColeta;
import com.mercurio.lms.coleta.model.service.ManifestoColetaService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.ParcelaTabelaCeDAO;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.fretecarreteirocoletaentrega.parcelaTabelaCeService"
 */
public class ParcelaTabelaCeService extends CrudService<ParcelaTabelaCe, Long> {

    private ManifestoService manifestoService;
    private ManifestoColetaService manifestoColetaService;

	/**
     * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
     * @param idSolicitacaoContratacao
     * @return <b>List of ParcelaTabelaCe</b>
     */
    public List<ParcelaTabelaCe> findParcelasTabelaColetaEntrega(Long idSolicitacaoContratacao) {
    	return getParcelaTabelaCeDAO().findParcelasTabelaColetaEntrega(idSolicitacaoContratacao);
    }

	/**
	 * Recupera uma instância de <code>ParcelaTabelaCe</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public ParcelaTabelaCe findById(java.lang.Long id) {
        return (ParcelaTabelaCe)super.findById(id);
    }

    public List findParcelaTabelaCeByTpParcela(String tpParcela,Long idParcelaTabelaCE) {
    	return getParcelaTabelaCeDAO().findParcelaTabelaCeByTpParcela(tpParcela,idParcelaTabelaCE,null,null,null,null);
    }
    
    public ParcelaTabelaCe findParcelaTabelaCeByTpParcelaAndTabelaCE(String tpParcela,Long idTabelaColetaEntrega) {
    	List rs = getParcelaTabelaCeDAO().findParcelaTabelaCeByTpParcela(tpParcela,null,idTabelaColetaEntrega,null,null,null);
    	if (rs.isEmpty())
    		return null;
    	return (ParcelaTabelaCe)rs.get(0);
    }
    
    
    public ParcelaTabelaCe findParcelaTabelaCeByTpParcela(String tpParcela,Long idParcelaTabelaCE,Long idTabelaColetaEntrega,Long idTipoMeioTransporte,YearMonthDay vigenteEm, Long idFilial) {
    	List rs = getParcelaTabelaCeDAO().findParcelaTabelaCeByTpParcela(tpParcela,idParcelaTabelaCE,idTabelaColetaEntrega,idTipoMeioTransporte,vigenteEm,idFilial);
    	if (rs.isEmpty())
    		return null;
    	return (ParcelaTabelaCe)rs.get(0);
    }
    
    /**
     * Método utilizado pela Integração.
     * 
     * @param tabelaColetaEntrega Pojo de tabela de coleta/entrega
     * Restringir por:<br>
     * 		- filial.idFilial<br>
     * 		- tabelaColetaEntrega.tipoMeioTransporte<br>
     * 		- tabelaColetaEntrega.tpRegistro<br>
     * 		se tpRegistro for Eventual:<br>
     * 			- tabelaColetaEntrega.meioTransporteRodoviario<br>
     * 		se tpRegistro for Agregado:<br>
     * 			- tabelaColetaEntrega.tipoTabelaColetaEntrega<br>
     * 		- tabelaColetaEntrega.dtVigenciaInicial <= dtVigencia<br>
     * 		- tabelaColetaEntrega.dtVigenciaFinal > dtVigencia<br>
     * 		- tpParcela<br>
     * @param dtVigencia data de vigência
     * @param tpParcela tipo de parcela
     * @return Objeto ParcelaTabelaCe preenchido.
     */
    public ParcelaTabelaCe findParcelaTabelaCe(TabelaColetaEntrega tabelaColetaEntrega,
    		YearMonthDay dtVigencia, String tpParcela) {
    	return getParcelaTabelaCeDAO().findParcelaTabelaCe(tabelaColetaEntrega,dtVigencia,tpParcela);
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
    public java.io.Serializable store(ParcelaTabelaCe bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setParcelaTabelaCeDAO(ParcelaTabelaCeDAO dao) {
        setDao( dao );
    }
    
    public boolean validateParcelaParaClienteEspecifico(ControleCarga controleCarga, ParcelaTabelaCe parcelaTabelaCe) {
            Cliente cliente = parcelaTabelaCe.getTabelaColetaEntrega().getCliente();

            if (cliente == null) {
                return true;
            }

            for (Manifesto manifesto : controleCarga.getManifestos()) {
                if (manifestoService.validateIfManifestoEntregaValidoParaCliente(cliente, manifesto)) {
                    return true;
                }
            }

            for (ManifestoColeta manifesto : controleCarga.getManifestoColetas()) {
                if (manifestoColetaService.validateIfIsManifestoColetaValidoParaCliente(cliente, manifesto)) {
                    return true;
                }
            }

        return false;
    }

    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ParcelaTabelaCeDAO getParcelaTabelaCeDAO() {
        return (ParcelaTabelaCeDAO) getDao();
    }

    public boolean validateParcela(ParcelaTabelaCe parcelaTabelaCe){
    	// Os atributos da Entidade Parcela são do tipo BigDecimal, porém com máscara (0.00) setados pela tela.
		BigDecimal maskedValue = new BigDecimal("0.00");

    	return (parcelaTabelaCe.getVlSugerido() != null && !maskedValue.equals(parcelaTabelaCe.getVlSugerido()))
    	        || (parcelaTabelaCe.getVlMaximoAprovado() != null
    	                && !maskedValue.equals(parcelaTabelaCe.getVlMaximoAprovado()))
    	        || (parcelaTabelaCe.getVlNegociado() != null && !maskedValue.equals(parcelaTabelaCe.getVlNegociado()));
    }

    public List<ParcelaTabelaCe> findParcelaTabelaCeByIdNotaCredito(Long idNotaCredito) {
		return getParcelaTabelaCeDAO().findParcelaTabelaCeByIdNotaCredito(idNotaCredito);
	}

    public List findParcelaTabelaCeByGroupTpParcelas(List tpParcelas, Long idTabelaColetaEntrega) {
        return getParcelaTabelaCeDAO().findParcelaTabelaCeByGroupTpParcelas(tpParcelas, idTabelaColetaEntrega);
    }

	public List<ParcelaTabelaCe> findParcelasColetaEntrega(List<Long> idsTabelaColetaEntrega) {
		return getParcelaTabelaCeDAO().findParcelasColetaEntrega(idsTabelaColetaEntrega);
	}

	public ParcelaTabelaCe findByTabelaColetaEntrega(TabelaColetaEntrega tabelaColetaEntrega) {
		return getParcelaTabelaCeDAO().findByTabelaColetaEntrega(tabelaColetaEntrega);
	}

	public List<ParcelaTabelaCe> findListByTabelaColetaEntrega(TabelaColetaEntrega tabelaColetaEntrega) {
		return getParcelaTabelaCeDAO().findListByTabelaColetaEntrega(tabelaColetaEntrega);
	}

    public void setManifestoService(ManifestoService manifestoService) {
        this.manifestoService = manifestoService;
    }

    public void setManifestoColetaService(ManifestoColetaService manifestoColetaService) {
        this.manifestoColetaService = manifestoColetaService;
    }

}