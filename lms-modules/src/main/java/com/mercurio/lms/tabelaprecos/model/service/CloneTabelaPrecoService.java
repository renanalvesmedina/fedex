package com.mercurio.lms.tabelaprecos.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.tabelaprecos.model.CloneGrupoRegiaoDTO;
import com.mercurio.lms.tabelaprecos.model.CloneTabelaPrecoDTO;
import com.mercurio.lms.tabelaprecos.model.FaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.Generalidade;
import com.mercurio.lms.tabelaprecos.model.GrupoRegiao;
import com.mercurio.lms.tabelaprecos.model.PrecoFrete;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.TarifaPrecoRota;
import com.mercurio.lms.tabelaprecos.model.ValorFaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.ValorServicoAdicional;
import com.mercurio.lms.tabelaprecos.model.ValorTaxa;
import com.mercurio.lms.tabelaprecos.model.dao.GrupoRegiaoDAO;
import com.mercurio.lms.tabelaprecos.model.dao.RotaPrecoDAO;
import com.mercurio.lms.tabelaprecos.model.dao.TabelaPrecoDAO;
import com.mercurio.lms.tabelaprecos.model.dao.TarifaPrecoRotaDAO;

public class CloneTabelaPrecoService {

	private static final String ERRO_CADASTRO_PARCELAS = "LMS-30073";

	private static Log log = LogFactory.getLog(CloneTabelaPrecoService.class);

	private static final String TIPO_GENERALIDADE = "G";
	private static final String TIPO_VALOR_TAXA = "T";
	private static final String TIPO_SERVICO_ADICIONAL = "S";
	private static final String TIPO_PRECO_FRETE = "P";
	private static final String TIPO_MINIMO_PROGRESSIVO = "M";
	private TabelaPrecoParcelaService tabelaPrecoParcelaService;
	private TabelaPrecoService tabelaPrecoService;

	private TarifaPrecoRotaService tarifaPrecoRotaService;
	private TabelaPrecoDAO tabelaPrecoDAO;
	private GrupoRegiaoService grupoRegiaoService;
	private TrtService trtService;
	private GrupoRegiaoDAO grupoRegiaoDAO;
	private RotaPrecoDAO rotaPrecoDAO;
	private RotaPrecoService rotaPrecoService;
	private TarifaPrecoRotaDAO tarifaPrecoRotaDAO;


	public Boolean executeClonarTabelaPrecoETarifaPrecoRotas(CloneTabelaPrecoDTO cloneTabelaPreco)throws Throwable{
		try{
			executeClonarGrupoRegiao(cloneTabelaPreco);
			executeClonarTabelaPreco(cloneTabelaPreco);
			executeClonarRotasETRT(cloneTabelaPreco);

		}catch (BusinessException e) {
			if("LMS-30064".equals(e.getMessageKey())){
				throw new BusinessException("LMS-30072", e);
			}
			else if(ERRO_CADASTRO_PARCELAS.equals(e.getMessageKey())){
				throw e;
			}
			throw new BusinessException("LMS-30074", e);

		} catch (Exception e) {
			log.error("Erro em executeClonarTabelaPrecoETarifaPrecoRotas. idTabelaBase: " + cloneTabelaPreco.getIdTabelaBase() + ", idTabelaNova: " + cloneTabelaPreco.getIdTabelaNova(),e);
			throw new BusinessException("LMS-30074", e);
		}

		return true;
	}

	public void executeClonarRotasETRT(CloneTabelaPrecoDTO cloneTabelaPreco) throws Exception {
		executeClonarTarifaPrecoRotas(cloneTabelaPreco);
		executeAjustarRotas(cloneTabelaPreco);
		executeCloneTRT(cloneTabelaPreco);
	}

	public Boolean executeClonarTabelaPreco(CloneTabelaPrecoDTO cloneTabelaPreco) throws Exception{

		List<TabelaPrecoParcela> parcelas  = clonarTabelaPreco(cloneTabelaPreco.getIdTabelaBase(), cloneTabelaPreco.getIdTabelaNova());

		for (TabelaPrecoParcela tabelaPrecoParcela : parcelas) {
			tabelaPrecoDAO.getAdsmHibernateTemplate().evict(tabelaPrecoParcela);
			tabelaPrecoParcelaService.store(tabelaPrecoParcela);
		}

		return true;
	}

	public Boolean executeCloneTRT(CloneTabelaPrecoDTO cloneTabelaPreco){
		return trtService.cloneTRTbyTabelaForReajuste(cloneTabelaPreco.getIdTabelaBase(), cloneTabelaPreco.getIdTabelaNova());
	}

	public Boolean executeClonarTarifaPrecoRotas(CloneTabelaPrecoDTO cloneTabelaPreco) throws Exception{
		TabelaPreco tabelaNova = tabelaPrecoService.findByIdTabelaPreco(cloneTabelaPreco.getIdTabelaNova());
		copyTarifaPrecoRotas(cloneTabelaPreco.getIdTabelaBase(), tabelaNova);
		return true;
	}
	public Boolean executeClonarGrupoRegiao(CloneTabelaPrecoDTO cloneTabelaPreco) throws Throwable{
		grupoRegiaoService.generateGruposRegioesAlterandoTabelaPreco(cloneTabelaPreco.getIdTabelaBase(), cloneTabelaPreco.getIdTabelaNova() );
		return true;
	}

	public Boolean executeAjustarRotas(CloneTabelaPrecoDTO cloneTabelaPreco) {
		List<CloneGrupoRegiaoDTO> rotaGrupos = grupoRegiaoDAO.findRotaGruposRegiaoClonados(cloneTabelaPreco.getIdTabelaBase(), cloneTabelaPreco.getIdTabelaNova());
		Map<Long, Long> rotasNovas = insertRotasTabela(rotaGrupos);
		updateTarifaPrecoRota(rotasNovas, cloneTabelaPreco.getIdTabelaNova());

		return true;
	}

	private void updateTarifaPrecoRota(Map<Long, Long> rotasNovas, Long idTabelaNova) {
		for(Entry<Long, Long> rota : rotasNovas.entrySet()){
			tarifaPrecoRotaDAO.updateTarifaPrecoRota(rota.getKey(), rota.getValue(), idTabelaNova);
		}
	}

	private Map<Long, Long> insertRotasTabela(List<CloneGrupoRegiaoDTO> rotaGrupos) {

		Map<Long, Long> mapeamentoClone = new HashMap<Long, Long>();

		for(CloneGrupoRegiaoDTO rotaGrupo : rotaGrupos){
			RotaPreco rota = rotaPrecoService.findById(rotaGrupo.getIdRotaPreco());
			RotaPreco rotaClone = cloneRotaPreco(rota);

			GrupoRegiao grupoRegiaoOrigem = findGrupoRegiao(rotaGrupo.getIdGrupoRegiaoOrigem());
			rotaClone.setGrupoRegiaoOrigem(grupoRegiaoOrigem );

			GrupoRegiao grupoRegiaoDestino = findGrupoRegiao(rotaGrupo.getIdGrupoRegiaoDestino());
			rotaClone.setGrupoRegiaoDestino(grupoRegiaoDestino);

			rotaPrecoDAO.store(rotaClone, true);

			mapeamentoClone.put(rota.getIdRotaPreco(), rotaClone.getIdRotaPreco());
		}
		return mapeamentoClone;
	}

	private GrupoRegiao findGrupoRegiao(Long idGrupo) {
		if(idGrupo != null){
			return grupoRegiaoService.findByIdCrudService(idGrupo);
		}
		return null;
	}

	private RotaPreco cloneRotaPreco(RotaPreco rota) {
		RotaPreco rotaClone = new RotaPreco();
		rotaClone.setTpSituacao(rota.getTpSituacao());
		rotaClone.setZonaByIdZonaOrigem(rota.getZonaByIdZonaOrigem());
		rotaClone.setZonaByIdZonaDestino(rota.getZonaByIdZonaDestino());
		rotaClone.setPaisByIdPaisOrigem(rota.getPaisByIdPaisOrigem());
		rotaClone.setPaisByIdPaisDestino(rota.getPaisByIdPaisDestino());
		rotaClone.setUnidadeFederativaByIdUfOrigem(rota.getUnidadeFederativaByIdUfOrigem());
		rotaClone.setUnidadeFederativaByIdUfDestino(rota.getUnidadeFederativaByIdUfDestino());
		rotaClone.setMunicipioByIdMunicipioOrigem(rota.getMunicipioByIdMunicipioOrigem());
		rotaClone.setMunicipioByIdMunicipioDestino(rota.getMunicipioByIdMunicipioDestino());
		rotaClone.setAeroportoByIdAeroportoOrigem(rota.getAeroportoByIdAeroportoOrigem());
		rotaClone.setAeroportoByIdAeroportoDestino(rota.getAeroportoByIdAeroportoDestino());
		rotaClone.setFilialByIdFilialOrigem(rota.getFilialByIdFilialOrigem());
		rotaClone.setFilialByIdFilialDestino(rota.getFilialByIdFilialDestino());
		rotaClone.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem(rota.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem());
		rotaClone.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino(rota.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino());
		rotaClone.setTipoLocalizacaoMunicipioComercialOrigem(rota.getTipoLocalizacaoMunicipioComercialOrigem());
		rotaClone.setTipoLocalizacaoMunicipioComercialDestino(rota.getTipoLocalizacaoMunicipioComercialDestino());

		return rotaClone;
	}

	private void copyTarifaPrecoRotas(Long idTabelaBase, TabelaPreco tabelaNova) throws Exception {
		List<TarifaPrecoRota> tarifaPrecoRotas = tarifaPrecoRotaService.findByIdTabelaPreco(idTabelaBase);
		for (TarifaPrecoRota tarifaPrecoRota : tarifaPrecoRotas) {
			TarifaPrecoRota copia = (TarifaPrecoRota) BeanUtils.cloneBean(tarifaPrecoRota);
			copia.setIdTarifaPrecoRota(null);
			copia.setTabelaPreco(tabelaNova);

			tarifaPrecoRotaService.validate(copia, idTabelaBase);
			tarifaPrecoRotaService.basicStore(copia);
		}
		tabelaPrecoDAO.getAdsmHibernateTemplate().flush();
	}

	public List<TabelaPrecoParcela> clonarTabelaPreco(Long idTabelaBase, Long idTabelaNova) throws Exception{
		List<TabelaPrecoParcela> result = new ArrayList<TabelaPrecoParcela>();

		result.addAll(clonarTabelaPrecoGeneralidades(idTabelaBase, idTabelaNova));
		result.addAll(clonarTabelaPrecoValorTaxa(idTabelaBase, idTabelaNova));
		result.addAll(clonarTabelaPrecoServicoAdicional(idTabelaBase, idTabelaNova));
		result.addAll(clonarTabelaPrecoPrecoFrete(idTabelaBase, idTabelaNova));
		result.addAll(clonarTabelaPrecoMinimoProgressivo(idTabelaBase, idTabelaNova));

		return result;
	}

	private List<TabelaPrecoParcela> clonarTabelaPrecoMinimoProgressivo(
			Long idTabelaBase, Long idTabelaNova) throws Exception {
		List<TabelaPrecoParcela> tabelaPrecoParcelas = tabelaPrecoParcelaService.findParcelasPrecoByIdTabelaPreco(idTabelaBase, TIPO_MINIMO_PROGRESSIVO);
		if(tabelaPrecoParcelas != null){
			return cloneMinimoProgressivo(tabelaPrecoParcelas, idTabelaNova);
		}
		return new ArrayList<TabelaPrecoParcela>();
	}

	private List<TabelaPrecoParcela> clonarTabelaPrecoPrecoFrete(
			Long idTabelaBase, Long idTabelaNova) throws Exception {
		List<TabelaPrecoParcela> tabelaPrecoParcelas = tabelaPrecoParcelaService.findParcelasPrecoByIdTabelaPreco(idTabelaBase, TIPO_PRECO_FRETE);
		if(tabelaPrecoParcelas != null){
			return clonePrecoFrete(tabelaPrecoParcelas, idTabelaNova);
		}
		return new ArrayList<TabelaPrecoParcela>();
	}

	private List<TabelaPrecoParcela> clonarTabelaPrecoServicoAdicional(
			Long idTabelaBase, Long idTabelaNova) throws Exception {
		List<TabelaPrecoParcela> tabelaPrecoParcelas = tabelaPrecoParcelaService.findParcelasPrecoByIdTabelaPreco(idTabelaBase, TIPO_SERVICO_ADICIONAL);
		if(tabelaPrecoParcelas != null){
			return cloneServicoAdicional(tabelaPrecoParcelas, idTabelaNova);
		}
		return new ArrayList<TabelaPrecoParcela>();
	}

	private List<TabelaPrecoParcela> clonarTabelaPrecoValorTaxa(
			Long idTabelaBase, Long idTabelaNova) throws Exception {
		List<TabelaPrecoParcela> tabelaPrecoParcelas = tabelaPrecoParcelaService.findParcelasPrecoByIdTabelaPreco(idTabelaBase, TIPO_VALOR_TAXA);
		if(tabelaPrecoParcelas != null){
			return cloneValorTaxa(tabelaPrecoParcelas, idTabelaNova);
		}
		return new ArrayList<TabelaPrecoParcela>();
	}

	private List<TabelaPrecoParcela> clonarTabelaPrecoGeneralidades(
			Long idTabelaBase, Long idTabelaNova)  throws Exception{
		List<TabelaPrecoParcela> tabelaPrecoParcelas = tabelaPrecoParcelaService.findParcelasPrecoByIdTabelaPreco(idTabelaBase, TIPO_GENERALIDADE);
		if(tabelaPrecoParcelas != null){
			return cloneGeneralidade(tabelaPrecoParcelas, idTabelaNova);
		}
		return new ArrayList<TabelaPrecoParcela>();
	}

	private List<TabelaPrecoParcela> cloneMinimoProgressivo(
			List<TabelaPrecoParcela> tabelaPrecoParcelas, Long idTabelaNova) throws Exception {
		List<TabelaPrecoParcela> parcelas = new ArrayList<TabelaPrecoParcela>();
		for (TabelaPrecoParcela tabelaPrecoParcela : tabelaPrecoParcelas) {
			TabelaPrecoParcela tpp = clonarTabelaPrecoParcela(tabelaPrecoParcela, idTabelaNova);
			tpp.setPrecoFretes(null);

			List<FaixaProgressiva> faixasProgressivas = tabelaPrecoParcela.getFaixaProgressivas();

			List<FaixaProgressiva> faixasProgressivasNew = clonarMinimoProgressivoFaixasProgressivas(faixasProgressivas, tpp);
			tpp.setFaixaProgressivas(faixasProgressivasNew);

			parcelas.add(tpp);
		}
		return parcelas;
	}

	private List<FaixaProgressiva> clonarMinimoProgressivoFaixasProgressivas(
			List<FaixaProgressiva> faixasProgressivas, TabelaPrecoParcela tpp) throws Exception {
		List<FaixaProgressiva> faixasProgressivasNew = new ArrayList<FaixaProgressiva>();
		if (faixasProgressivas == null){
			return faixasProgressivasNew;
		}
		for (FaixaProgressiva faixaProgressiva : faixasProgressivas) {
			FaixaProgressiva faixaProgressivaNew = (FaixaProgressiva) BeanUtils.cloneBean(faixaProgressiva);
			faixaProgressivaNew.setIdFaixaProgressiva(null);

			List<ValorFaixaProgressiva> valoresFaixaProgressivaNew = clonarMinimoProgressivoValorFaixaProgressivas(faixaProgressiva.getValoresFaixasProgressivas(), faixaProgressivaNew);
			faixaProgressivaNew.setValoresFaixasProgressivas(valoresFaixaProgressivaNew);
			faixaProgressivaNew.setTpSituacao(new DomainValue("A"));

			faixaProgressivaNew.setTabelaPrecoParcela(tpp);
			faixasProgressivasNew.add(faixaProgressivaNew);
		}

		return faixasProgressivasNew;
	}

	private List<ValorFaixaProgressiva> clonarMinimoProgressivoValorFaixaProgressivas(
			List<ValorFaixaProgressiva> valoresFaixaProgressiva, FaixaProgressiva faixaProgressivaNew) throws Exception{
		List<ValorFaixaProgressiva> valoresFaixaProgressivaNew = new ArrayList<ValorFaixaProgressiva>();
		if (valoresFaixaProgressiva == null) {
			return valoresFaixaProgressivaNew;
		}

		for (ValorFaixaProgressiva valorFaixaProgressiva : valoresFaixaProgressiva) {
			if (Boolean.TRUE.equals(valorFaixaProgressiva.getBlPromocional())) {
				continue;
			}

			ValorFaixaProgressiva valorFaixaProgressivaNew =  (ValorFaixaProgressiva) BeanUtils.cloneBean(valorFaixaProgressiva);
			valorFaixaProgressivaNew.setIdValorFaixaProgressiva(null);
			valorFaixaProgressivaNew.setBlPromocional(Boolean.FALSE);
			valorFaixaProgressivaNew.setDtVigenciaPromocaoFinal(null);
			valorFaixaProgressivaNew.setDtVigenciaPromocaoInicial(null);

			valorFaixaProgressivaNew.setFaixaProgressiva(faixaProgressivaNew);

			valoresFaixaProgressivaNew.add(valorFaixaProgressivaNew);
		}
		return valoresFaixaProgressivaNew;
	}

	private List<TabelaPrecoParcela> clonePrecoFrete(
			List<TabelaPrecoParcela> tabelaPrecoParcelas, Long idTabelaNova) throws Exception {
		List<TabelaPrecoParcela> parcelas = new ArrayList<TabelaPrecoParcela>();
		for (TabelaPrecoParcela tabelaPrecoParcela : tabelaPrecoParcelas) {
			TabelaPrecoParcela tpp = clonarTabelaPrecoParcela(tabelaPrecoParcela, idTabelaNova);
			tpp.setFaixaProgressivas(null);

			List<PrecoFrete> pfs = clonarRotasPrecoFrete(tabelaPrecoParcela.getPrecoFretes(), tpp);
			tpp.setPrecoFretes(pfs);
			parcelas.add(tpp);
		}

		return parcelas;
	}

	private List<PrecoFrete> clonarRotasPrecoFrete(
			List<PrecoFrete> precoFretes, TabelaPrecoParcela tpp) throws Exception {
		List<PrecoFrete> pfs = new ArrayList<PrecoFrete>();
		if (precoFretes == null){
			return pfs;
		}

		for(PrecoFrete precoFrete : precoFretes) {
			PrecoFrete pf = (PrecoFrete) BeanUtils.cloneBean(precoFrete);
			pf.setIdPrecoFrete(null);
			pf.setTabelaPrecoParcela(tpp);

			pfs.add(pf);
		}

		return pfs;
	}

	private List<TabelaPrecoParcela> cloneServicoAdicional(
			List<TabelaPrecoParcela> tabelaPrecoParcelas, Long idTabelaNova) throws Exception {
		List<TabelaPrecoParcela> parcelas = new ArrayList<TabelaPrecoParcela>();
		for (TabelaPrecoParcela tabelaPrecoParcela : tabelaPrecoParcelas) {
			TabelaPrecoParcela tpp = clonarTabelaPrecoParcela(tabelaPrecoParcela, idTabelaNova);
			tpp.setPrecoFretes(null);
			tpp.setFaixaProgressivas(null);

			ValorServicoAdicional valorServicoAdicional = tabelaPrecoParcela.getValorServicoAdicional();

			if(valorServicoAdicional == null){
				throw new BusinessException(ERRO_CADASTRO_PARCELAS);
			}
			ValorServicoAdicional vsa = (ValorServicoAdicional) BeanUtils.cloneBean(valorServicoAdicional);
			vsa.setIdValorServicoAdicional(null);
			vsa.setTabelaPrecoParcela(tpp);
			tpp.setValorServicoAdicional(vsa);
			parcelas.add(tpp);
		}
		return parcelas;
	}

	private List<TabelaPrecoParcela> cloneValorTaxa(
			List<TabelaPrecoParcela> tabelaPrecoParcelas, Long idTabelaNova) throws Exception {
		List<TabelaPrecoParcela> parcelas = new ArrayList<TabelaPrecoParcela>();
		for (TabelaPrecoParcela tabelaPrecoParcela : tabelaPrecoParcelas) {
			TabelaPrecoParcela tpp = clonarTabelaPrecoParcela(tabelaPrecoParcela, idTabelaNova);
			tpp.setPrecoFretes(null);
			tpp.setFaixaProgressivas(null);

			ValorTaxa valorTaxa = tabelaPrecoParcela.getValorTaxa();
			if(valorTaxa == null){
				throw new BusinessException(ERRO_CADASTRO_PARCELAS);
			}
			ValorTaxa vt = (ValorTaxa) BeanUtils.cloneBean(valorTaxa);
			vt.setIdValorTaxa(null);
			vt.setTabelaPrecoParcela(tpp);
			tpp.setValorTaxa(vt);
			parcelas.add(tpp);
		}
		return parcelas;
	}

	private List<TabelaPrecoParcela> cloneGeneralidade(
			List<TabelaPrecoParcela> tabelaPrecoParcelas, Long idTabelaNova)  throws Exception{
		List<TabelaPrecoParcela> parcelas = new ArrayList<TabelaPrecoParcela>();

		for (TabelaPrecoParcela tabelaPrecoParcela : tabelaPrecoParcelas) {
			TabelaPrecoParcela tpp = clonarTabelaPrecoParcela(tabelaPrecoParcela, idTabelaNova);
			tpp.setPrecoFretes(null);
			tpp.setFaixaProgressivas(null);

			Generalidade generalidade = tabelaPrecoParcela.getGeneralidade();
			if(generalidade == null){
				throw new BusinessException(ERRO_CADASTRO_PARCELAS);
			}
			Generalidade g = (Generalidade) BeanUtils.cloneBean(generalidade);
			g.setIdGeneralidade(null);
			g.setTabelaPrecoParcela(tpp);
			tpp.setGeneralidade(g);

			parcelas.add(tpp);
		}

		return parcelas;
	}


	private TabelaPrecoParcela clonarTabelaPrecoParcela(
			TabelaPrecoParcela tabelaPrecoParcela, Long idTabelaNova)
			throws Exception {
		TabelaPrecoParcela tpp =  (TabelaPrecoParcela) BeanUtils.cloneBean(tabelaPrecoParcela);
		tpp.setIdTabelaPrecoParcela(null);
		TabelaPreco tp = new TabelaPreco();
		tp.setIdTabelaPreco(idTabelaNova);
		tpp.setTabelaPreco(tp);
		return tpp;
	}

	public void setTabelaPrecoParcelaService(
			TabelaPrecoParcelaService tabelaPrecoParcelaService) {
		this.tabelaPrecoParcelaService = tabelaPrecoParcelaService;
	}

	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}

	public void setTarifaPrecoRotaService(
			TarifaPrecoRotaService tarifaPrecoRotaService) {
		this.tarifaPrecoRotaService = tarifaPrecoRotaService;
	}

	public void setTabelaPrecoDAO(TabelaPrecoDAO tabelaPrecoDAO) {
		this.tabelaPrecoDAO = tabelaPrecoDAO;
	}
	public void setGrupoRegiaoService(GrupoRegiaoService grupoRegiaoService) {
		this.grupoRegiaoService = grupoRegiaoService;
	}
	public void setRotaPrecoDAO(RotaPrecoDAO rotaPrecoDAO) {
		this.rotaPrecoDAO = rotaPrecoDAO;
	}
	public void setGrupoRegiaoDAO(GrupoRegiaoDAO grupoRegiaoDAO) {
		this.grupoRegiaoDAO = grupoRegiaoDAO;
	}
	public void setRotaPrecoService(RotaPrecoService rotaPrecoService) {
		this.rotaPrecoService = rotaPrecoService;
	}
	public void setTarifaPrecoRotaDAO(TarifaPrecoRotaDAO tarifaPrecoRotaDAO) {
		this.tarifaPrecoRotaDAO = tarifaPrecoRotaDAO;
	}
	public void setTrtService(TrtService trtService) {
		this.trtService = trtService;
	}
}
