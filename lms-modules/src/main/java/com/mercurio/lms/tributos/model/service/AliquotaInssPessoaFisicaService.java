package com.mercurio.lms.tributos.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.AliquotaInssPessoaFisica;
import com.mercurio.lms.tributos.model.dao.AliquotaInssPessoaFisicaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tributos.aliquotaInssPessoaFisicaService"
 */
public class AliquotaInssPessoaFisicaService extends CrudService<AliquotaInssPessoaFisica, Long> {


	/**
	 * Busca Aliquota vigente.<BR>
	 *@author Robson Edemar Gehl
	 * @param data
	 * @return
	 */
	public AliquotaInssPessoaFisica findAliquotaVigente(YearMonthDay data){
		return getAliquotaInssPessoaFisicaDAO().findAliquotaVigente(data);
	}
	
	/**
	 * Recupera uma inst�ncia de <code>AliquotaInssPessoaFisica</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public AliquotaInssPessoaFisica findById(java.lang.Long id) {
        return (AliquotaInssPessoaFisica)super.findById(id);
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
    public java.io.Serializable store(AliquotaInssPessoaFisica bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setAliquotaInssPessoaFisicaDAO(AliquotaInssPessoaFisicaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private AliquotaInssPessoaFisicaDAO getAliquotaInssPessoaFisicaDAO() {
        return (AliquotaInssPessoaFisicaDAO) getDao();
    }
    
    /**
     * Calcula o valor o valor m�ximo a recolher de acordo com o percentual da aliquota e do valor do sal�rio base informados
     * @param map 'pcAliquota' - Percentual da Aliquota, 'vlSalarioBase' - Valor do Sal�rio Base
     * @return Valor M�ximo a Recolher
     */
    public String findValorMaximoARecolher(Map map){
        
        BigDecimal pcAliquota = new BigDecimal((String)map.get("pcAliquota"));
        BigDecimal vlSalarioBase = new BigDecimal((String)map.get("vlSalarioBase"));
        
        BigDecimal retorno = vlSalarioBase.multiply(pcAliquota.divide(new BigDecimal(100),2,BigDecimal.ROUND_DOWN));
        
        return retorno.divide(new BigDecimal(1),2,BigDecimal.ROUND_DOWN).toString().replaceAll("\\.",",");
        
    }
    
}