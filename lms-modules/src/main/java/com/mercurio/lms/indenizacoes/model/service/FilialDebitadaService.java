package com.mercurio.lms.indenizacoes.model.service;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.indenizacoes.model.FilialDebitada;
import com.mercurio.lms.indenizacoes.model.dao.FilialDebitadaDAO;
import com.mercurio.lms.util.BigDecimalUtils;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.indenizacoes.filialDebitadaService"
 */
public class FilialDebitadaService extends CrudService<FilialDebitada, Long> {


	/**
	 * Recupera uma inst�ncia de <code>FilialDebitada</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public FilialDebitada findById(java.lang.Long id) {
        return (FilialDebitada)super.findById(id);
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
    	for (Iterator iter=ids.iterator(); iter.hasNext();) {
    		Long id = (Long)iter.next();

    		FilialDebitada filialDebitada = findById(id);
        	List listaFilialDebitada = findByIdReciboIndenizacao(filialDebitada.getReciboIndenizacao().getIdReciboIndenizacao());

        	for (Iterator iterFilialDebitada = listaFilialDebitada.iterator(); iterFilialDebitada.hasNext();) {
    			FilialDebitada fd = (FilialDebitada)iterFilialDebitada.next();
    			if (fd.getFilial().getIdFilial().equals(filialDebitada.getFilial().getIdFilial())) {
    				super.removeById(fd.getIdFilialDebitada());
    			}
    		}
    	}
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(FilialDebitada bean) {    	
    	Long idReciboIndenizacao = bean.getReciboIndenizacao().getIdReciboIndenizacao();
    	Long idFilial = bean.getFilial().getIdFilial();
    	Long idDoctoServicoIndenizacao = null;     	
    	if (bean.getDoctoServicoIndenizacao()!=null) {
    		idDoctoServicoIndenizacao = bean.getDoctoServicoIndenizacao().getIdDoctoServicoIndenizacao();
    	}

    	// verifica unique key
    	FilialDebitada filialDebitada = findFilialDebitadaByReciboIndenizacaoFilialDoctoServicoIndenizacao(idReciboIndenizacao, idFilial, idDoctoServicoIndenizacao);
    	if (filialDebitada!=null && !filialDebitada.getIdFilialDebitada().equals(bean.getIdFilialDebitada())) {
    		throw new BusinessException("LMS-21036");
    	}
    	getFilialDebitadaDAO().getAdsmHibernateTemplate().evict(filialDebitada);

   		bean.setIdFilialDebitada((Long)super.store(bean));

    	BigDecimal sumPcDebitado = findSumPcDebitado(idReciboIndenizacao);
    	if (BigDecimalUtils.HUNDRED.compareTo(sumPcDebitado) < 0){
    		throw new BusinessException("LMS-21057");
    	}
    	return bean.getIdFilialDebitada();
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setFilialDebitadaDAO(FilialDebitadaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private FilialDebitadaDAO getFilialDebitadaDAO() {
        return (FilialDebitadaDAO) getDao();
    }
    
    public ResultSetPage findPaginatedFilialDebitadaReciboIndenizacao(TypedFlatMap tfm) {
    	return getFilialDebitadaDAO().findPaginatedFilialDebitadaReciboIndenizacao(tfm, FindDefinition.createFindDefinition(tfm));
    }
    
    public Integer getRowCountFilialDebitadaReciboIndenizacao(TypedFlatMap tfm) {
    	return getFilialDebitadaDAO().getRowCountFilialDebitadaReciboIndenizacao(tfm);
    } 

    /**
     * Remove as filiais debitadas do recibo de indenizacao
     * @param idReciboIndenizacao
     */
    public void removeByIdReciboIndenizacao(Long idReciboIndenizacao) {
    	getFilialDebitadaDAO().removeByIdReciboIndenizacao(idReciboIndenizacao);
    }
    
    /**
     * Consulta se j� existe Filial Debitada para o idReciboIndenizacao, idFilial e idDoctoServicoIndenizacao.
     * Utilizado para validar a unique key (idReciboIndenizacao, idFilial e idDoctoServicoIndenizacao) antes da inser��o.
     * @param idReciboIndenizacao
     * @param idFilial
     * @param idDoctoServicoIndenizacao
     * @return True se j� existe um registro.
     */
    public FilialDebitada findFilialDebitadaByReciboIndenizacaoFilialDoctoServicoIndenizacao(Long idReciboIndenizacao, Long idFilial, Long idDoctoServicoIndenizacao) {
    	return getFilialDebitadaDAO().findFilialDebitadaByReciboIndenizacaoFilialDoctoServicoIndenizacao(idReciboIndenizacao, idFilial, idDoctoServicoIndenizacao);
    }

    /**
     * Obt�m as filiais debitadas do recibo de indenizacao
     * @param idReciboIndenizacao
     * @return
     */
    public List findByIdReciboIndenizacao(Long idReciboIndenizacao) {
    	return getFilialDebitadaDAO().findByIdReciboIndenizacao(idReciboIndenizacao);
    }
    
    /**
     * Obt�m a some do percentual debitado das filiais debitadas do recibo de indenizacao
     * @param idReciboIndenizacao
     * @return
     */
    public BigDecimal findSumPcDebitado(Long idReciboIndenizacao) {
    	return getFilialDebitadaDAO().findSumPcDebitado(idReciboIndenizacao);
    }


   }