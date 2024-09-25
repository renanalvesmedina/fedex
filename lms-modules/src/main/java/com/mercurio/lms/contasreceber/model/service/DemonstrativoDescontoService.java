package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.DemonstrativoDesconto;
import com.mercurio.lms.contasreceber.model.Desconto;
import com.mercurio.lms.contasreceber.model.dao.DemonstrativoDescontoDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.demonstrativoDescontoService"
 */
public class DemonstrativoDescontoService extends CrudService<DemonstrativoDesconto, Long> {
	
	private DescontoService descontoService;


	public void setDescontoService(DescontoService descontoService) {
		this.descontoService = descontoService;
	}

	/**
	 * Recupera uma instância de <code>DemonstrativoDesconto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public DemonstrativoDesconto findById(java.lang.Long id) {
        return (DemonstrativoDesconto)super.findById(id);
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
    public java.io.Serializable store(DemonstrativoDesconto bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setDemonstrativoDescontoDAO(DemonstrativoDescontoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DemonstrativoDescontoDAO getDemonstrativoDescontoDAO() {
        return (DemonstrativoDescontoDAO) getDao();
    }

	/**
     * Método de cancelamento do Recibo Desconto
     * @param idReciboDemonstrativo Identificador do Recibo Desconto a ser cancelado
     */
	public void executeCancelar(Long idReciboDemonstrativo, String tpDesconto) {
		
		DemonstrativoDesconto dd = findById(idReciboDemonstrativo);
		
		YearMonthDay dtEmissao = dd.getDtEmissao();
		YearMonthDay dtAtual   = JTDateTimeUtils.getDataAtual();

		if( dd.getTpSituacaoDemonstrativoDesc().getValue().equals("C") ){//cancelado
			throw new BusinessException("LMS-36143");
		}
		
		if( dtEmissao.getMonthOfYear() != dtAtual.getMonthOfYear() ){
			throw new BusinessException("LMS-36074");
		}
		
		Filial filialUsuario = SessionUtils.getFilialSessao();
		
		boolean filialMatriz = SessionUtils.isFilialSessaoMatriz();
		
		if( !filialMatriz && !dd.getFilial().equals(filialUsuario) ){
			throw new BusinessException("LMS-36075");
		}
		
		/*
		 * valida se o primeiro doc serv já foi liquidado
		 */
		List descontosList = descontoService.findDescontosByReciboDesconto(idReciboDemonstrativo, tpDesconto);
		Desconto desconto = (Desconto)descontosList.get(0);
		if (desconto.getDevedorDocServFat().getTpSituacaoCobranca().getValue().equalsIgnoreCase("L")){
			throw new BusinessException("LMS-36243");
		}
		
		dd.setTpSituacaoDemonstrativoDesc(new DomainValue("C"));//Cancelado
		dd.setVlDemonstrativoDesconto(new BigDecimal(0));
		
		store(dd);
		
		descontoService.executeCancelar(idReciboDemonstrativo,tpDesconto);
		
	}
	
	/**
     * Carrega o DemonstrativoDesconto de acordo com o número e a sigla da filial
     *
     * @author Hector Julian Esnaola Junior
     * @since 10/08/2007
     *
     * @param nrDemonstrativoDesconto
     * @param idFilial
     * @param tpSituacao
     * @return
     *
     */
	public DemonstrativoDesconto findDemonstrativoDescontoByNumeroAndFilial(Long nrDemonstrativoDesconto, Long idFilial, String tpSituacao){
		return getDemonstrativoDescontoDAO().findDemonstrativoDescontoByNumeroAndFilial(
				nrDemonstrativoDesconto, 
				idFilial, 
				tpSituacao);
	}	
}