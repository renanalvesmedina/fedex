package com.mercurio.lms.vendas.model.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.vendas.model.ValorCampoComplementar;
import com.mercurio.lms.vendas.model.dao.ValorCampoComplementarDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;


/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.valorCampoComplementarService"
 */
public class ValorCampoComplementarService extends CrudService<ValorCampoComplementar, Long> {

	/**
	 * Recupera uma inst�ncia de <code>ValorCampoComplementar</code> a partir do ID.<BR>
	 * O valor (vlValor) � formatado de acordo com a m�scara (dsFormatacao). 
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public ValorCampoComplementar findById(java.lang.Long id) {
		return this.formatValores((ValorCampoComplementar)super.findById(id));
	}

	/**
	 * Formata o vlValor do Valor Campo Complementar, a partir da m�scara em dsFormatacao.<BR>
	 * Dados formatados: datas e n�meros.
	 * @author Robson Edemar Gehl
	 * @param vcc
	 * @return mesma referencia do parametro
	 */
	public ValorCampoComplementar formatValores(ValorCampoComplementar vcc){
		//Para campos Alfanum�ricos n�o h� formata��o para refazer
		Format formatter;
		DateFormat dateFormatter;
		String tpCampoComplementar;
		Date date;
		tpCampoComplementar = vcc.getCampoComplementar().getTpCampoComplementar().getValue();

		if ("N".equals(tpCampoComplementar)){
			//N�o formata campo que n�o tem m�scara, retornando o valor sem 're-formatacao'
			if (vcc.getCampoComplementar().getDsFormatacao() == null) return vcc;

			formatter = new DecimalFormat(vcc.getCampoComplementar().getDsFormatacao());
			//Big Decimal abrangente: Integer, Decimal, etc..
			vcc.setVlValor( formatter.format(new BigDecimal(vcc.getVlValor())) );
		} else if ("D".equals(tpCampoComplementar)) {
			//Formato de data que est� no banco
			dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				//Para fazer a formata��o, � necess�rio antes fazer um parse da String e criar um Date.
				date = dateFormatter.parse(vcc.getVlValor());
				//Agora sim, pode-se fazer a nova formata��o em cima do Date
				dateFormatter = new SimpleDateFormat(vcc.getCampoComplementar().getDsFormatacao());
				//Atribui a nova formata��o
				vcc.setVlValor( dateFormatter.format(date).toString() );
			} catch (ParseException e) {
			}
		}
		return vcc;
	}

	/**
	 * Retorna os valores p�ginados e formatados.
	 * @author Robson Edemar Gehl
	 * @see formatValores(ValorCampoComplementar vcc)
	 * @param criteria
	 */
	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage rsp = super.findPaginated(criteria);
		List list = rsp.getList();

		for (Iterator iter = list.iterator(); iter.hasNext(); ){
			this.formatValores((ValorCampoComplementar) iter.next());
		}
		
		return rsp;
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
	public java.io.Serializable store(ValorCampoComplementar bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setValorCampoComplementarDAO(ValorCampoComplementarDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private ValorCampoComplementarDAO getValorCampoComplementarDAO() {
		return (ValorCampoComplementarDAO) getDao();
	}

}
