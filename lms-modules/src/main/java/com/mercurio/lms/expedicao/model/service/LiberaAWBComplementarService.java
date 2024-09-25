package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.LiberaAWBComplementar;
import com.mercurio.lms.expedicao.model.dao.LiberaAWBComplementarDAO;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.tabelaprecos.model.service.TarifaSpotService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class LiberaAWBComplementarService  extends CrudService<LiberaAWBComplementar, Long> {
	private TarifaSpotService tarifaSpotService;
	private AwbService awbService;
	
	public LiberaAWBComplementar storeLiberacao(LiberaAWBComplementar liberaAWBComplementar) {
		UsuarioLMS usuario = new UsuarioLMS();
		usuario.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		liberaAWBComplementar.setUsuarioLiberador(usuario);
		liberaAWBComplementar.setDhLiberacao(JTDateTimeUtils.getDataHoraAtual());
		liberaAWBComplementar.setDsSenha(LongUtils.getLong(tarifaSpotService.gerarCodigoLiberacao()).toString());
		Long id = (Long)super.store(liberaAWBComplementar);

		liberaAWBComplementar = (LiberaAWBComplementar) super.findByIdInitLazyProperties(id, true);
		return liberaAWBComplementar;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> findMapAwbOriginal(Map<String, Object> criteria) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<LiberaAWBComplementar> listaLiberaAWBComplementar = super.find(criteria);
		if (listaLiberaAWBComplementar != null	&& !listaLiberaAWBComplementar.isEmpty()) {
			Awb awbOriginal = listaLiberaAWBComplementar.get(0).getAwbOriginal();

			if (awbOriginal.getDsSerie() != null) {
				result.put("serieAwbComplementado", awbOriginal.getDsSerie());
			}
			
			if (awbOriginal.getNrAwb() != null && awbOriginal.getDvAwb() != null) {
				result.put("nrAwbComplementado", AwbUtils.getNrAwbFormated(awbOriginal));
			}
		}
		return result;
	}
	
	@Override
	public Serializable findById(Long id) {
		return super.findById(id);
	}
	
	@Override
	public Serializable store(LiberaAWBComplementar bean) {
		return super.store(bean);
	}
	
	@SuppressWarnings("unchecked")
	public void validateAwbAComplementar(Long idLiberaAWBComplementar, Long idAwb) {
		Awb awb = awbService.findById(idAwb);
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("awbOriginal.idAwb", idAwb);
		
		List<LiberaAWBComplementar> liberacoes = this.find(criteria);
		
		if (liberacoes != null && !liberacoes.isEmpty()) {
			for (LiberaAWBComplementar liberaAWBComplementar : liberacoes) {
				if (idLiberaAWBComplementar == null || liberaAWBComplementar.getIdLiberaAWBComplementar().compareTo(idLiberaAWBComplementar) != 0) {
					throw new BusinessException("LMS-04445", new Object[] {AwbUtils.getSgEmpresaAndNrAwbFormated(awb), liberaAWBComplementar.getDsSenha(), JTFormatUtils.format(liberaAWBComplementar.getDhLiberacao())});
				}
			}
		}

		if (awb.getTpAwb() == null || !ConstantesExpedicao.TP_AWB_NORMAL.equals(awb.getTpAwb().getValue())) {
			throw new BusinessException("LMS-04446");
		}
		
		if (awb.getTpStatusAwb() == null || !ConstantesExpedicao.TP_STATUS_AWB_EMITIDO.equals(awb.getTpStatusAwb().getValue())) {
			throw new BusinessException("LMS-04448");
		}
	}
	
	/**
	 * Remove a instancia especifica do bean pelo id
	 * e os filhos.
	 */
	@Override
	public void removeById(Long id) {
		LiberaAWBComplementar liberaAWBComplementar = (LiberaAWBComplementar) super.findById(id);
		
		if(liberaAWBComplementar.getAwbComplementar() != null) {
			throw new BusinessException("LMS-04443");
		}
		
		getLiberaAWBComplementarDAO().removeById(id);
	}
	
	@Override
	public void removeByIds(List<Long> ids) {
		for (Long id : ids) {
			this.removeById(id);
		}
	}
	

	public Map<String, Object> findMapById(Long id) {
		LiberaAWBComplementar liberaAWBComplementar = (LiberaAWBComplementar) super.findById(id);
		Awb awbOrig = liberaAWBComplementar.getAwbOriginal();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idLiberaAWBComplementar", liberaAWBComplementar.getIdLiberaAWBComplementar());
		map.put("idFilial", liberaAWBComplementar.getFilialSolicitante().getIdFilial());
		map.put("sgFilial", liberaAWBComplementar.getFilialSolicitante().getSgFilial());
		map.put("nmFantasia", liberaAWBComplementar.getFilialSolicitante().getPessoa().getNmFantasia());
		map.put("nrMatricula", liberaAWBComplementar.getUsuarioSolicitante().getUsuarioADSM().getNrMatricula());
		map.put("idUsuario", liberaAWBComplementar.getUsuarioSolicitante().getIdUsuario());
		map.put("nmUsuario", liberaAWBComplementar.getUsuarioSolicitante().getUsuarioADSM().getNmUsuario());
		map.put("nrUsuarioLiberacao",  liberaAWBComplementar.getUsuarioLiberador().getUsuarioADSM().getNrMatricula());
		map.put("nmUsuarioLiberacao", liberaAWBComplementar.getUsuarioLiberador().getUsuarioADSM().getNmUsuario());
		map.put("idEmpresa", liberaAWBComplementar.getEmpresa().getIdEmpresa());
		map.put("nmPessoa", liberaAWBComplementar.getEmpresa().getPessoa().getNmPessoa());
		map.put("sgEmpresa", liberaAWBComplementar.getEmpresa().getSgEmpresa());
		map.put("idAwb", awbOrig.getIdAwb());
		map.put("nrAwb", AwbUtils.getNrAwb(awbOrig));
		map.put("dsSerieAwb", awbOrig.getDsSerie());
		map.put("motivo", liberaAWBComplementar.getDsMotivo());
		map.put("dhLiberacao", liberaAWBComplementar.getDhLiberacao());
		map.put("dsSenha", liberaAWBComplementar.getDsSenha());
		map.put("tpStatusAwb", awbOrig.getTpStatusAwb().getValue());
		
		return map;
	};
	
	public void setLiberaAWBComplementarDAO(LiberaAWBComplementarDAO dao) {
		setDao(dao);
	}
	
	private LiberaAWBComplementarDAO getLiberaAWBComplementarDAO() {
		return (LiberaAWBComplementarDAO) getDao();
	}

	public void setTarifaSpotService(TarifaSpotService tarifaSpotService) {
		this.tarifaSpotService = tarifaSpotService;
	}

	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}

	public LiberaAWBComplementar findByIdAwbComplementado(Long idAwb) {
		return getLiberaAWBComplementarDAO().findByIdAwbComplementado(idAwb);
	}
	
	public LiberaAWBComplementar findByIdAwbComplementar(Long idAwb) {
		return getLiberaAWBComplementarDAO().findByIdAwbComplementar(idAwb);
	}

}

