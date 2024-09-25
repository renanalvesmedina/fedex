package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCeCC;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.ParcelaTabelaCeCCDAO;

import java.util.Collections;

/**
 * Classe de servi�o para CRUD:
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.fretecarreteirocoletaentrega.parcelaTabelaCeCCService"
 */
public class ParcelaTabelaCeCCService extends CrudService<ParcelaTabelaCeCC, Long> {

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @param Inst�ncia do DAO.
	 */
	public void setParcelaTabelaCeCCDAO(ParcelaTabelaCeCCDAO parcelaTabelaCeCCDAO) {
		setDao(parcelaTabelaCeCCDAO);
	}

	private ParcelaTabelaCeCCDAO getParcelaTabelaCeCCDAO() {
		return (ParcelaTabelaCeCCDAO) getDao();
	}

	@Override
	public Serializable store(ParcelaTabelaCeCC bean) {
		return super.store(bean);
	}

	@SuppressWarnings("unchecked")
	public void remove(Long idTabelaColetaEntrega) {
		getDao().removeById(idTabelaColetaEntrega);
    }

	public void removeByIdControleCarga(Long idControleCarga) {
		getParcelaTabelaCeCCDAO().removeByIdControleCarga(idControleCarga);
	}

	public ParcelaTabelaCeCC findMaiorParcela(List<ParcelaTabelaCeCC> parcelas) {
		if (parcelas == null || parcelas.isEmpty()) {
			return new ParcelaTabelaCeCC();
		}

		sortParcelasByIdParcelaTabelaCe(parcelas);
		sortParcelasByTotal(parcelas);

		return parcelas.get(0);
	}

	private void sortParcelasByIdParcelaTabelaCe(List<ParcelaTabelaCeCC> parcelas) {
		Collections.sort(parcelas, new Comparator<ParcelaTabelaCeCC>() {
			public int compare(ParcelaTabelaCeCC o1, ParcelaTabelaCeCC o2) {
				return o1.getParcelaTabelaCe().getIdParcelaTabelaCe().compareTo(
						o2.getParcelaTabelaCe().getIdParcelaTabelaCe());
			}
		});
	}

	private void sortParcelasByTotal(List<ParcelaTabelaCeCC> parcelas) {
		Collections.sort(parcelas, new Comparator<ParcelaTabelaCeCC>() {
			public int compare(ParcelaTabelaCeCC o1, ParcelaTabelaCeCC o2) {
				return o2.getVlParcela().multiply(o2.getQtParcela()).compareTo(
						o1.getVlParcela().multiply(o1.getQtParcela()));
			}
		});
	}

}
