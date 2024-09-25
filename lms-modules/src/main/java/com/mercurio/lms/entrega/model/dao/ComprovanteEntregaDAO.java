package com.mercurio.lms.entrega.model.dao;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.entrega.model.ComprovanteEntrega;


/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring.
 * 
 */
public class ComprovanteEntregaDAO extends BaseCrudDao<ComprovanteEntrega, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ComprovanteEntrega.class;
	}

	public ComprovanteEntrega store(ComprovanteEntrega comprovanteEntrega) {
		super.store(comprovanteEntrega);
		getAdsmHibernateTemplate().flush();
		return comprovanteEntrega;
	}

	public byte[] findAssinaturaByDoctoServico(Long idDoctoServico) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idDoctoServico", idDoctoServico);

		StringBuffer hql = new StringBuffer()
				.append(" select ce ")
				.append(" from ").append(ComprovanteEntrega.class.getName()).append(" as ce ")
				.append(" where ce.idDoctoServico = :idDoctoServico ");
		List<ComprovanteEntrega> comprovantes = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), map);

		if (CollectionUtils.isEmpty(comprovantes)) {
			return null;
		}
		if (comprovantes.get(0).getAssinatura() == null) {
			return null;
		}

		byte[] assinatura = converterAssinatura(comprovantes);
		return assinatura;
	}

	private byte[] converterAssinatura(List<ComprovanteEntrega> comprovantes) {
		byte[] assinatura = new byte[0];

		try {
			Blob blob = comprovantes.get(0).getAssinatura();
			int blobLength = (int) blob.length();
			assinatura = blob.getBytes(1, blobLength);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Erro ao converter assinatura em formato Blob para array de bytes", e);
		}
		return assinatura;
	}
}