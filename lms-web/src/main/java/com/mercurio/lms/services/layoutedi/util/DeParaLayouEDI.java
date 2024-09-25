package com.mercurio.lms.services.layoutedi.util;

import java.util.ArrayList;
import java.util.List;

import br.com.tntbrasil.integracao.domains.webservice.edi.Complemento;
import br.com.tntbrasil.integracao.domains.webservice.edi.Consignatario;
import br.com.tntbrasil.integracao.domains.webservice.edi.Destinatario;
import br.com.tntbrasil.integracao.domains.webservice.edi.Detalhe;
import br.com.tntbrasil.integracao.domains.webservice.edi.Item;
import br.com.tntbrasil.integracao.domains.webservice.edi.LogDetalhe;
import br.com.tntbrasil.integracao.domains.webservice.edi.NotaFiscal;
import br.com.tntbrasil.integracao.domains.webservice.edi.Redespacho;
import br.com.tntbrasil.integracao.domains.webservice.edi.Registro;
import br.com.tntbrasil.integracao.domains.webservice.edi.Remetente;
import br.com.tntbrasil.integracao.domains.webservice.edi.Tomador;
import br.com.tntbrasil.integracao.domains.webservice.edi.Volume;

public class DeParaLayouEDI {

	public static com.mercurio.lms.layoutedi.model.LogDetalhe deParaLog(LogDetalhe deLogDetalhe) {
		com.mercurio.lms.layoutedi.model.LogDetalhe paraLogDetalhe = new com.mercurio.lms.layoutedi.model.LogDetalhe();
		
		paraLogDetalhe.setIdLogMestre(deLogDetalhe.getIdLogMestre());
		paraLogDetalhe.setObservacao(deLogDetalhe.getObservacao());
		paraLogDetalhe.setStatus(deLogDetalhe.getStatus());
			
		paraLogDetalhe.setRegistros(deParaRegistros(deLogDetalhe.getRegistros()));
		return paraLogDetalhe;
	}

	public static List<com.mercurio.lms.layoutedi.model.Registro> deParaRegistros(List<Registro> deRegistros) {
		List<com.mercurio.lms.layoutedi.model.Registro> paraRegistros = new ArrayList<com.mercurio.lms.layoutedi.model.Registro>();
		for (Registro deRegistro : deRegistros) {
			paraRegistros.add(deParaRegistro(deRegistro));
		}
		return paraRegistros;
	}

	public static com.mercurio.lms.layoutedi.model.Registro deParaRegistro(Registro deRegistro) {
		com.mercurio.lms.layoutedi.model.Registro paraRegistro = new com.mercurio.lms.layoutedi.model.Registro();
		
		if (deRegistro.getRemetente() != null) {
			paraRegistro.setRemetente(deParaRemetente(deRegistro));
		}
		
		if (deRegistro.getDetalhes() != null) {
			paraRegistro.setDetalhes(deParaDetalhes(deRegistro));
		}
		return paraRegistro;
	}

	public static List<com.mercurio.lms.layoutedi.model.Detalhe> deParaDetalhes(Registro deRegistro) {
		List<com.mercurio.lms.layoutedi.model.Detalhe> paraDetalhes = new ArrayList<com.mercurio.lms.layoutedi.model.Detalhe>();
		for (Detalhe deDetalhe : deRegistro.getDetalhes()) {
			paraDetalhes.add(deParaDetalhe(deDetalhe));
		}
		return paraDetalhes;
	}

	public static com.mercurio.lms.layoutedi.model.Detalhe deParaDetalhe(Detalhe deDetalhe) {
		com.mercurio.lms.layoutedi.model.Detalhe paraDetalhe = new com.mercurio.lms.layoutedi.model.Detalhe();
		
		if (deDetalhe.getConsignatario() != null) {
			paraDetalhe.setConsignatario(deParaConsignatario(deDetalhe.getConsignatario()));
		}
		
		if (deDetalhe.getDestinatario() != null) {
			paraDetalhe.setDestinatario(deParaDestinatario(deDetalhe.getDestinatario()));
		}
		
		if (deDetalhe.getRedespacho() != null) {
			paraDetalhe.setRedespacho(deParaRedespacho(deDetalhe.getRedespacho()));
		}
		
		if (deDetalhe.getTomador() != null) {						
			paraDetalhe.setTomador(deParaTomador(deDetalhe.getTomador()));
		}
		
		if (deDetalhe.getNotasFiscais() != null) {						
			paraDetalhe.setNotasFiscais(deParaNotas(deDetalhe));
		}
		return paraDetalhe;
	}

	public static List<com.mercurio.lms.layoutedi.model.NotaFiscal> deParaNotas(Detalhe deDetalhe) {
		List<com.mercurio.lms.layoutedi.model.NotaFiscal> paraNotasFiscais = new ArrayList<com.mercurio.lms.layoutedi.model.NotaFiscal>();

		for (NotaFiscal deNotaFiscal : deDetalhe.getNotasFiscais()) {
			paraNotasFiscais.add(deParaNota(deNotaFiscal));
		}
		return paraNotasFiscais;
	}

	public static com.mercurio.lms.layoutedi.model.NotaFiscal deParaNota(NotaFiscal deNotaFiscal) {
		com.mercurio.lms.layoutedi.model.NotaFiscal paraNota = new com.mercurio.lms.layoutedi.model.NotaFiscal();
		
		paraNota.setAliqIcms(deNotaFiscal.getAliqIcms());
		paraNota.setAliqNf(deNotaFiscal.getAliqNf());
		paraNota.setCfopNf(deNotaFiscal.getCfopNf());
		paraNota.setChaveNfe(deNotaFiscal.getChaveNfe());
		paraNota.setDataEmissaoNf(deNotaFiscal.getDataEmissaoNf());
		paraNota.setDsDivisaoCliente(deNotaFiscal.getDsDivisaoCliente());
		paraNota.setErro(deNotaFiscal.getErro());
		paraNota.setEspecie(deNotaFiscal.getEspecie());
		paraNota.setModalFrete(deNotaFiscal.getModalFrete());
		paraNota.setNatureza(deNotaFiscal.getNatureza());
		paraNota.setNrCtrcSubcontratante(deNotaFiscal.getNrCtrcSubcontratante());
		paraNota.setNrNotaFiscal(deNotaFiscal.getNrNotaFiscal());
		paraNota.setOutrosValores(deNotaFiscal.getOutrosValores());
		paraNota.setPesoCubado(deNotaFiscal.getPesoCubado());
		paraNota.setPesoCubadoTotal(deNotaFiscal.getPesoCubadoTotal());
		paraNota.setPesoReal(deNotaFiscal.getPesoReal());
		paraNota.setPesoRealTotal(deNotaFiscal.getPesoRealTotal());
		paraNota.setPinSuframa(deNotaFiscal.getPinSuframa());
		paraNota.setQtdeVolumes(deNotaFiscal.getQtdeVolumes());
		paraNota.setSerieNf(deNotaFiscal.getSerieNf());
		paraNota.setTarifa(deNotaFiscal.getTarifa());
		paraNota.setTipoFrete(deNotaFiscal.getTipoFrete());
		paraNota.setTipoTabela(deNotaFiscal.getTipoTabela());
		paraNota.setVlrAdeme(deNotaFiscal.getVlrAdeme());
		paraNota.setVlrBaseCalcIcms(deNotaFiscal.getVlrBaseCalcIcms());
		paraNota.setVlrBaseCalcNf(deNotaFiscal.getVlrBaseCalcNf());
		paraNota.setVlrBaseCalcStNf(deNotaFiscal.getVlrBaseCalcStNf());
		paraNota.setVlrCat(deNotaFiscal.getVlrCat());
		paraNota.setVlrDespacho(deNotaFiscal.getVlrDespacho());
		paraNota.setVlrFreteLiquido(deNotaFiscal.getVlrFreteLiquido());
		paraNota.setVlrFretePeso(deNotaFiscal.getVlrFretePeso());
		paraNota.setVlrFreteTotal(deNotaFiscal.getVlrFreteTotal());
		paraNota.setVlrFreteValor(deNotaFiscal.getVlrFreteValor());
		paraNota.setVlrIcms(deNotaFiscal.getVlrIcms());
		paraNota.setVlrIcmsNf(deNotaFiscal.getVlrIcmsNf());
		paraNota.setVlrIcmsStNf(deNotaFiscal.getVlrIcmsStNf());
		paraNota.setVlrItr(deNotaFiscal.getVlrItr());
		paraNota.setVlrPedagio(deNotaFiscal.getVlrPedagio());
		paraNota.setVlrTaxas(deNotaFiscal.getVlrTaxas());
		paraNota.setVlrTotalMerc(deNotaFiscal.getVlrTotalMerc());
		paraNota.setVlrTotalMercTotal(deNotaFiscal.getVlrTotalMercTotal());
		paraNota.setVlrTotProdutosNf(deNotaFiscal.getVlrTotProdutosNf());
		
		if (deNotaFiscal.getComplementos() != null) {
			paraNota.setComplementos(deParaComplementos(deNotaFiscal.getComplementos()));
		}
		
		if (deNotaFiscal.getItens() != null) {
			paraNota.setItens(deParaItens(deNotaFiscal.getItens()));
		}
		
		if (deNotaFiscal.getVolumes() != null) {
			paraNota.setVolumes(deParaVolumes(deNotaFiscal.getVolumes()));
		}
		return paraNota;
	}

	public static List<com.mercurio.lms.layoutedi.model.Complemento> deParaComplementos(List<Complemento> deComplementos) {
		List<com.mercurio.lms.layoutedi.model.Complemento> paraComplementos = new ArrayList<com.mercurio.lms.layoutedi.model.Complemento>();
		for (Complemento deComplemento : deComplementos) {
			paraComplementos.add(deParaComplemento(deComplemento));
		}
		return paraComplementos;
	}

	public static List<com.mercurio.lms.layoutedi.model.Item> deParaItens(List<Item> deItens) {
		List<com.mercurio.lms.layoutedi.model.Item> paraItens = new ArrayList<com.mercurio.lms.layoutedi.model.Item>();
		for (Item deItem : deItens) {
			paraItens.add(deParaItem(deItem));
		}
		return paraItens;
	}

	public static List<com.mercurio.lms.layoutedi.model.Volume> deParaVolumes(List<Volume> deVolumes) {
		List<com.mercurio.lms.layoutedi.model.Volume> paraVolumes = new ArrayList<com.mercurio.lms.layoutedi.model.Volume>();
		for (Volume deVolume : deVolumes) {
			com.mercurio.lms.layoutedi.model.Volume paraVolume = new com.mercurio.lms.layoutedi.model.Volume();
			
			paraVolume.setCodigoVolume(deVolume.getCodigoVolume());
			
			paraVolumes.add(paraVolume);
		}
		return paraVolumes;
	}

	public static com.mercurio.lms.layoutedi.model.Item deParaItem(Item deItem) {
		com.mercurio.lms.layoutedi.model.Item paraItem = new com.mercurio.lms.layoutedi.model.Item();
		
		paraItem.setAlturaItem(deItem.getAlturaItem());
		paraItem.setCodItem(deItem.getCodItem());
		paraItem.setComprimentoItem(deItem.getComprimentoItem());
		paraItem.setLarguraItem(deItem.getLarguraItem());
		paraItem.setPesoCubadoItem(deItem.getPesoCubadoItem());
		paraItem.setPesoRealItem(deItem.getPesoRealItem());
		paraItem.setValorItem(deItem.getValorItem());
		return paraItem;
	}

	public static com.mercurio.lms.layoutedi.model.Complemento deParaComplemento(Complemento deComplemento) {
		com.mercurio.lms.layoutedi.model.Complemento paraComplemento = new com.mercurio.lms.layoutedi.model.Complemento();
		
		paraComplemento.setIndcIdInformacaoDoctoClien(deComplemento.getIndcIdInformacaoDoctoClien());
		paraComplemento.setNomeComplemento(deComplemento.getNomeComplemento());		
		paraComplemento.setValorComplemento(deComplemento.getValorComplemento());		
		return paraComplemento;
	}

	public static com.mercurio.lms.layoutedi.model.Consignatario deParaConsignatario(Consignatario deConsignatario) {
		com.mercurio.lms.layoutedi.model.Consignatario paraConsignatario = new com.mercurio.lms.layoutedi.model.Consignatario();
		
		paraConsignatario.setBairroConsig(deConsignatario.getBairroConsig());
		paraConsignatario.setCepEnderConsig(deConsignatario.getCepEnderConsig());
		paraConsignatario.setCepMunicConsig(deConsignatario.getCepMunicConsig());
		paraConsignatario.setCnpjConsig(deConsignatario.getCnpjConsig());
		paraConsignatario.setEnderecoConsig(deConsignatario.getEnderecoConsig());
		paraConsignatario.setIeConsig(deConsignatario.getIeConsig());
		paraConsignatario.setMunicipioConsig(deConsignatario.getMunicipioConsig());
		paraConsignatario.setNomeConsig(deConsignatario.getNomeConsig());
		paraConsignatario.setUfConsig(deConsignatario.getUfConsig());
		return paraConsignatario;
	}

	public static com.mercurio.lms.layoutedi.model.Destinatario deParaDestinatario(Destinatario deDestinatario) {
		com.mercurio.lms.layoutedi.model.Destinatario paraDestinatario = new com.mercurio.lms.layoutedi.model.Destinatario();
		
		paraDestinatario.setBairroDest(deDestinatario.getBairroDest());
		paraDestinatario.setCepEnderDest(deDestinatario.getCepEnderDest());
		paraDestinatario.setCepMunicDest(deDestinatario.getCepMunicDest());
		paraDestinatario.setCnpjDest(deDestinatario.getCnpjDest());
		paraDestinatario.setEnderecoDest(deDestinatario.getEnderecoDest());
		paraDestinatario.setIeDest(deDestinatario.getIeDest());
		paraDestinatario.setMunicipioDest(deDestinatario.getMunicipioDest());
		paraDestinatario.setNomeDest(deDestinatario.getNomeDest());
		paraDestinatario.setUfDest(deDestinatario.getUfDest());
		return paraDestinatario;
	}

	public static com.mercurio.lms.layoutedi.model.Redespacho deParaRedespacho(Redespacho deRedespacho) {
		com.mercurio.lms.layoutedi.model.Redespacho paraRedespacho = new com.mercurio.lms.layoutedi.model.Redespacho();
		
		paraRedespacho.setBairroRedesp(deRedespacho.getBairroRedesp());
		paraRedespacho.setCepEnderRedesp(deRedespacho.getCepEnderRedesp());
		paraRedespacho.setCepMunicRedesp(deRedespacho.getCepMunicRedesp());
		paraRedespacho.setCnpjRedesp(deRedespacho.getCnpjRedesp());
		paraRedespacho.setEnderecoRedesp(deRedespacho.getEnderecoRedesp());
		paraRedespacho.setIeRedesp(deRedespacho.getIeRedesp());
		paraRedespacho.setMunicipioRedesp(deRedespacho.getMunicipioRedesp());
		paraRedespacho.setNomeRedesp(deRedespacho.getNomeRedesp());
		paraRedespacho.setUfRedesp(deRedespacho.getUfRedesp());
		return paraRedespacho;
	}

	public static com.mercurio.lms.layoutedi.model.Tomador deParaTomador(Tomador deTomador) {
		com.mercurio.lms.layoutedi.model.Tomador paraTomador = new com.mercurio.lms.layoutedi.model.Tomador();
		paraTomador.setBairroTomador(deTomador.getBairroTomador());
		paraTomador.setCepEnderTomador(deTomador.getCepEnderTomador());
		paraTomador.setCepMunicTomador(deTomador.getCepMunicTomador());
		paraTomador.setCnpjTomador(deTomador.getCnpjTomador());
		paraTomador.setEnderecoTomador(deTomador.getEnderecoTomador());
		paraTomador.setIeTomador(deTomador.getIeTomador());
		paraTomador.setMunicipioTomador(deTomador.getMunicipioTomador());
		paraTomador.setNomeTomador(deTomador.getNomeTomador());
		paraTomador.setUfTomador(deTomador.getUfTomador());
		return paraTomador;
	}

	public static com.mercurio.lms.layoutedi.model.Remetente deParaRemetente(Registro deRegistro) {
		Remetente deRemetente = deRegistro.getRemetente();
		com.mercurio.lms.layoutedi.model.Remetente paraRemetente = new com.mercurio.lms.layoutedi.model.Remetente();
		
		paraRemetente.setBairroReme(deRemetente.getBairroReme());
		paraRemetente.setCepEnderReme(deRemetente.getCepEnderReme());
		paraRemetente.setCepMuniReme(deRemetente.getCepMuniReme());
		paraRemetente.setCnpjReme(deRemetente.getCnpjReme());
		paraRemetente.setEnderecoReme(deRemetente.getEnderecoReme());
		paraRemetente.setIeReme(deRemetente.getIeReme());
		paraRemetente.setMunicipioReme(deRemetente.getMunicipioReme());
		paraRemetente.setNomeReme(deRemetente.getNomeReme());
		paraRemetente.setUfReme(deRemetente.getUfReme());
		return paraRemetente;
	}
	
}
