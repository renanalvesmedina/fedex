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
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
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
	 * Recupera uma instância de <code>AliquotaInssPessoaFisica</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public AliquotaInssPessoaFisica findById(java.lang.Long id) {
        return (AliquotaInssPessoaFisica)super.findById(id);
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
    public java.io.Serializable store(AliquotaInssPessoaFisica bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setAliquotaInssPessoaFisicaDAO(AliquotaInssPessoaFisicaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private AliquotaInssPessoaFisicaDAO getAliquotaInssPessoaFisicaDAO() {
        return (AliquotaInssPessoaFisicaDAO) getDao();
    }
    
    /**
     * Calcula o valor o valor máximo a recolher de acordo com o percentual da aliquota e do valor do salário base informados
     * @param map 'pcAliquota' - Percentual da Aliquota, 'vlSalarioBase' - Valor do Salário Base
     * @return Valor Máximo a Recolher
     */
    public String findValorMaximoARecolher(Map map){
        
        BigDecimal pcAliquota = new BigDecimal((String)map.get("pcAliquota"));
        BigDecimal vlSalarioBase = new BigDecimal((String)map.get("vlSalarioBase"));
        
        BigDecimal retorno = vlSalarioBase.multiply(pcAliquota.divide(new BigDecimal(100),2,BigDecimal.ROUND_DOWN));
        
        return retorno.divide(new BigDecimal(1),2,BigDecimal.ROUND_DOWN).toString().replaceAll("\\.",",");
        
    }
    
}