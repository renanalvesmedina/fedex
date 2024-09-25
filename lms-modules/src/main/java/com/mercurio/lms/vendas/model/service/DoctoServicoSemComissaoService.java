package com.mercurio.lms.vendas.model.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.edw.EdwService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.DoctoServicoSemComissao;
import com.mercurio.lms.vendas.model.dao.DoctoServicoSemComissaoDAO;

public class DoctoServicoSemComissaoService extends CrudService<DoctoServicoSemComissao, Long> {
	
	private UsuarioLMSService usuarioLMSService;
	private EdwService edwService;

 	public void storeNaoComissionaveis(List<Long> naoComissionaveisList, Long idExecutivo, YearMonthDay dtCompetencia) {
 		
 		UsuarioLMS usuarioInclusao = usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario());
		YearMonthDay inicioMesProducao = new YearMonthDay(edwService.findCalendarioTNTByData(dtCompetencia).getDataInicioMesProducao());

		List<DoctoServicoSemComissao> listDoctoServicoSemComissao = new ArrayList<DoctoServicoSemComissao>();
		for (Long idDoctoServico : naoComissionaveisList) {
			listDoctoServicoSemComissao.add(createDoctoServicoSemComissao(idDoctoServico, inicioMesProducao, idExecutivo, usuarioInclusao));
		}
		storeAll(listDoctoServicoSemComissao);
 	}
 	
	public void removeNaoComissionaveis(List<Long> naoComissionaveisList) {
		
		List<Long> listDoctoServicoNaoComissionaveis = new ArrayList<Long>();
		for (Long idDoctoServico : naoComissionaveisList) {
			List<DoctoServicoSemComissao> doctoList = find(idDoctoServico, null, null);
			if (doctoList.size() > 0) {
				listDoctoServicoNaoComissionaveis.add(doctoList.get(0).getIdDoctoServicoSemComissao());
			}
		}
		removeByIds(listDoctoServicoNaoComissionaveis);
		
	}

	public void setDoctoServicoSemComissaoDao(DoctoServicoSemComissaoDAO dao) {
		setDao(dao);
	}

	public DoctoServicoSemComissaoDAO getDoctoServicoSemComissaoDao() {
		return (DoctoServicoSemComissaoDAO) getDao();
	}

	public List<DoctoServicoSemComissao> find(Long idDoctoServicoSemComissao, Long idExecutivo, YearMonthDay dtCompetencia) {
		return getDoctoServicoSemComissaoDao().find(idDoctoServicoSemComissao, idExecutivo, dtCompetencia);
	}

	@Override
	public void removeById(Long id) {
		getDoctoServicoSemComissaoDao().removeById(id);
	}

	public Integer findCount(Long idDoctoServicoSemComissao, Long idExecutivo, YearMonthDay dtCompetencia) {
		return getDoctoServicoSemComissaoDao().findCount(idDoctoServicoSemComissao, idExecutivo, dtCompetencia);
	}

	private DoctoServicoSemComissao createDoctoServicoSemComissao(Long idDoctoServico, YearMonthDay dtCompetencia, Long idExecutivo, UsuarioLMS usuarioInclusao) {
		DoctoServicoSemComissao docto = new DoctoServicoSemComissao();
		
		DoctoServico doctoServico = new DoctoServico();
		doctoServico.setIdDoctoServico(idDoctoServico);
		docto.setDoctoServico(doctoServico);
		
		UsuarioLMS executivo = new UsuarioLMS();
		executivo.setIdUsuario(idExecutivo);
		docto.setExecutivo(executivo);		

		docto.setDtCompetencia(dtCompetencia);
		docto.setUsuarioInclusao(usuarioInclusao);
		docto.setDtInclusao(JTDateTimeUtils.getDataAtual());
		
		return docto;
	}
	
	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	public void setEdwService(EdwService edwService) {
		this.edwService = edwService;
	}
	
}
