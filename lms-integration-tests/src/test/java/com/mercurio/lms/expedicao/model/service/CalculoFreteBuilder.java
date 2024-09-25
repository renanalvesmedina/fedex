package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;

import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServicoDadosCliente;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.vendas.model.Cliente;

public class CalculoFreteBuilder {

	private CalculoFrete calculoFrete;
	
	private CalculoFreteBuilder() {
		calculoFrete = new CalculoFrete();
		calculoFrete.setBlCalculoFreteTabelaCheia(false);
		calculoFrete.setBlCalculaParcelas(false);
		calculoFrete.setBlCalculaImpostoServico(false);
	}
	
	public static CalculoFreteBuilder novoCalculoFreteCom() {
		return new CalculoFreteBuilder();
	}
	
	public CalculoFreteBuilder abrangenciaNacional() {
		calculoFrete.setTpAbrangencia(ConstantesExpedicao.ABRANGENCIA_NACIONAL);
		return this;
	}
	
	public CalculoFreteBuilder conhecimentoNormal() {
		calculoFrete.setTpConhecimento(ConstantesExpedicao.CONHECIMENTO_NORMAL);
		return this;
	}
	
	public CalculoFreteBuilder conhecimentoRefaturamento() {
		calculoFrete.setTpConhecimento(ConstantesExpedicao.CONHECIMENTO_REFATURAMENTO);
		return this;
	}
	
	public CalculoFreteBuilder conhecimentoDevolucaoParcial() {
		calculoFrete.setTpConhecimento(ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO_PARCIAL);
		return this;
	}
	
	public CalculoFreteBuilder conhecimentoSubstituto() {
		calculoFrete.setTpConhecimento(ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO);
		return this;
	}
	
	public CalculoFreteBuilder modalAereo() {
		calculoFrete.setTpModal(ConstantesExpedicao.MODAL_AEREO);
		return this;
	}
	
	public CalculoFreteBuilder modalRodoviario() {
		calculoFrete.setTpModal(ConstantesExpedicao.MODAL_RODOVIARIO);
		return this;
	}
	
	public CalculoFreteBuilder calculoNormal() {
		calculoFrete.setTpCalculo(ConstantesExpedicao.CALCULO_NORMAL);
		return this;
	}
	
	public CalculoFreteBuilder calculoCotacao() {
		calculoFrete.setTpCalculo(ConstantesExpedicao.CALCULO_COTACAO);
		return this;
	}
	
	public CalculoFreteBuilder calculoManual() {
		calculoFrete.setTpCalculo(ConstantesExpedicao.CALCULO_MANUAL);
		return this;
	}
	
	public CalculoFreteBuilder calculoCortesia() {
		calculoFrete.setTpCalculo(ConstantesExpedicao.CALCULO_CORTESIA);
		return this;
	}
	
	public CalculoFreteBuilder comRestricaoRotaDestino(RestricaoRota restricaoRota) {
		calculoFrete.setRestricaoRotaDestino(restricaoRota);
		return this;
	}
	
	public CalculoFreteBuilder comRestricaoRotaOrigem(RestricaoRota restricaoRota) {
		calculoFrete.setRestricaoRotaOrigem(restricaoRota);
		return this;
	}
	
	public CalculoFreteBuilder comTabelaCheia() {
		calculoFrete.setBlCalculoFreteTabelaCheia(true);
		return this;
	}
	
	public CalculoFreteBuilder freteFOB() {
		calculoFrete.setTpFrete(ConstantesExpedicao.TP_FRETE_FOB);
		return this;
	}
	
	public CalculoFreteBuilder freteCIF() {
		calculoFrete.setTpFrete(ConstantesExpedicao.TP_FRETE_CIF);
		return this;
	}
	
	public CalculoFreteBuilder fretePacotinho() {
		calculoFrete.setTpFrete(ConstantesExpedicao.TP_FRETE_PACOTINHO);
		return this;
	}
	
	public CalculoFreteBuilder paraCliente(Cliente cliente) {
		calculoFrete.setClienteBase(cliente);
		return this;
	}
	
	public CalculoFreteBuilder dadosCliente(DoctoServicoDadosCliente dadosCliente) {
		calculoFrete.setDadosCliente(dadosCliente);
		return this;
	}
	
	public CalculoFreteBuilder servicoRodoviarioNacionalConvencional() {
		calculoFrete.setIdServico(1L);
		return this;
	}
	
	public CalculoFreteBuilder divisaoCliente(long id) {
		calculoFrete.setIdDivisaoCliente(id);
		return this;
	}
	
	public CalculoFreteBuilder conhecimentoNumero(Long idDoctoServico) {
		calculoFrete.setIdDoctoServico(idDoctoServico);
		return this;
	}
	
	public CalculoFreteBuilder conhecimento(Conhecimento conhecimento) {
		calculoFrete.setDoctoServico(conhecimento);
		return this;
	}
	
	public CalculoFreteBuilder calculandoImpostos() {
		calculoFrete.setBlCalculaImpostoServico(true);
		return this;
	}
	
	public CalculoFreteBuilder calculandoValorDasParcelas() {
		calculoFrete.setBlCalculaParcelas(true);
		return this;
	}
	
	public CalculoFreteBuilder comMercadoriasNoValorDe(BigDecimal valor) {
		calculoFrete.setVlMercadoria(valor);
		return this;
	}
	
	public CalculoFreteBuilder comPesoCubadoDe(BigDecimal peso) {
		calculoFrete.setPsCubadoInformado(peso);
		return this;
	}
	
	public CalculoFreteBuilder comPesoRealDe(BigDecimal peso) {
		calculoFrete.setPsRealInformado(peso);
		return this;
	}
	
	public CalculoFreteBuilder volumes(Integer volumes) {
		calculoFrete.setQtVolumes(volumes);
		return this;
	}
	
	public CalculoFrete build() {
		return calculoFrete;
	}
	
	
}
