package com.mercurio.lms.municipios.model.service;

import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.municipios.model.ValorTarifaPostoPassagem;
import com.mercurio.lms.municipios.model.dao.ValorTarifaPostoPassagemDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.valorTarifaPostoPassagemService"
 */
public class ValorTarifaPostoPassagemService extends CrudService<ValorTarifaPostoPassagem, Long> {


	/**
	 * Recupera uma inst�ncia de <code>ValorTarifaPostoPassagem</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public ValorTarifaPostoPassagem findById(Long id) {
        return (ValorTarifaPostoPassagem)super.findById(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ValorTarifaPostoPassagem bean) {
        return super.store(bean);
    }
    
    /**
     * Deleta os registros da tabela ValorTarifaPostoPassagem vinculadas ao <BR>
     * idTarifaPostoPassagem passado para fun��o e que id seja diferente de idValorTarifaPostoPassagem
     * @author Samuel Herrmann
     * @param idTarifaPostoPassagem
     * @param idValorTarifaPostoPassagem <u>N�o obrigatorio</u>
     */
    public void deleteVTPPByTPPandId(Long idTarifaPostoPassagem,Long idValorTarifaPostoPassagem) {
 	   List rs = getValorTarifaPostoPassagemDAO().findVTPPByTPPandId(idTarifaPostoPassagem,idValorTarifaPostoPassagem);
 	   for(Iterator ie = rs.iterator(); ie.hasNext();)
 		   getValorTarifaPostoPassagemDAO().remove(ie.next());
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setValorTarifaPostoPassagemDAO(ValorTarifaPostoPassagemDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ValorTarifaPostoPassagemDAO getValorTarifaPostoPassagemDAO() {
        return (ValorTarifaPostoPassagemDAO) getDao();
    }
    
    // busca vlTarifa,idMoeda de todas as tarifas vigentes para aquele postoPassagem, com a forma de cobran�a = tipo de veiculo e para o tipo de meio de transporte passado por parametro
    public List findValorTarifaPostoPassagemByPostoPassagem(Long idPostoPassagem, String formaCobranca, Long idTipoMeioTransporte){
    	return getValorTarifaPostoPassagemDAO().findValorTarifaPostoPassagemByPostoPassagem(idPostoPassagem,formaCobranca,idTipoMeioTransporte);
    }
    
    public List findByTarifaPostoPassagem(Long idTarifaPostoPassagem) {
    	return getValorTarifaPostoPassagemDAO().findByTarifaPostoPassagem(idTarifaPostoPassagem);
    }
    
}