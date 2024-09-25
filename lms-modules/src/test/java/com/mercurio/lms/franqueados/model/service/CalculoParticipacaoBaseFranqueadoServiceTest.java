package com.mercurio.lms.franqueados.model.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.YearMonthDay;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.franqueados.model.DoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.FixoFranqueado;
import com.mercurio.lms.franqueados.model.Franquia;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.vendas.model.Cliente;

public class CalculoParticipacaoBaseFranqueadoServiceTest {

	private static final YearMonthDay APRIL_05 = new YearMonthDay(2014, 04, 5);
	private static final YearMonthDay APRIL_07 = new YearMonthDay(2014, 04, 7);
	private static final YearMonthDay APRIL_03 = new YearMonthDay(2014, 04, 3);
	private static final YearMonthDay APRIL_06 = new YearMonthDay(2014, 04, 6);
	private static final YearMonthDay APRIL_04 = new YearMonthDay(2014, 04, 4);
	@Mock private FixoFranqueadoService fixoFranqueadoService;
	private Conhecimento conhecimento;
	
	private DoctoServicoFranqueado doctoServicoFranqueado; 
	private Cliente cliente;
	private Municipio municipio;
	private Franquia franquia;
	
	private List<DoctoServicoFranqueado> lstDoctoServicoCifFob;
	private YearMonthDay dtInicioCompetencia;
	
    @BeforeClass 
    protected void setup() {
        MockitoAnnotations.initMocks(this);
	}
    
    @BeforeMethod
    protected void setupMethod(){
		lstDoctoServicoCifFob = new ArrayList<DoctoServicoFranqueado>();
		dtInicioCompetencia = null;
		
		doctoServicoFranqueado = new DoctoServicoFranqueado();
		cliente = new Cliente();
		municipio = new Municipio(); 
		franquia = new Franquia();
    }
	
	
	//CIF Expedido
	public void calculaParticipacaoBaseRegraCifExpedidoCliente() {

		// CIF_EXPEDIDO
		franquia.setIdFranquia(403L);
		doctoServicoFranqueado.setTpFrete(new DomainValue("CE")); //TP_FRETE IN ('FE', 'CR', 'FR', 'CE', 'FL')
		doctoServicoFranqueado.setVlBaseCalculo(new BigDecimal(75.50));
		doctoServicoFranqueado.setFranquia(franquia);
		dtInicioCompetencia = APRIL_05;

		cliente.setIdCliente(12345L);
		municipio.setIdMunicipio(null);
		conhecimento = new Conhecimento();
		conhecimento.setIdDoctoServico(1234L);
		
		doctoServicoFranqueado.setConhecimento(conhecimento);
		
		lstDoctoServicoCifFob.add(doctoServicoFranqueado);

	}

	public void calculaParticipacaoBaseRegraCifExpedidoMunicipio() {

	
		// CIF_EXPEDIDO
		franquia.setIdFranquia(403L);
		doctoServicoFranqueado.setTpFrete(new DomainValue("CE")); //TP_FRETE IN ('FE', 'CR', 'FR', 'CE', 'FL')
		doctoServicoFranqueado.setVlBaseCalculo(new BigDecimal(50.50));
		doctoServicoFranqueado.setFranquia(franquia);
		dtInicioCompetencia = APRIL_05;

		cliente.setIdCliente(null);
		municipio.setIdMunicipio(23456L);
		conhecimento = new Conhecimento();
		conhecimento.setIdDoctoServico(1234L);

		
		doctoServicoFranqueado.setConhecimento(conhecimento);
		
		lstDoctoServicoCifFob.add(doctoServicoFranqueado);

	}

	public void calculaParticipacaoBaseRegraCifExpedidoNull() {

	
		// CIF_EXPEDIDO
		franquia.setIdFranquia(403L);
		doctoServicoFranqueado.setTpFrete(new DomainValue("CE")); //TP_FRETE IN ('FE', 'CR', 'FR', 'CE', 'FL')
		doctoServicoFranqueado.setVlBaseCalculo(new BigDecimal(100.50));
		doctoServicoFranqueado.setFranquia(franquia);
		dtInicioCompetencia = APRIL_05;

		cliente.setIdCliente(null);
		municipio.setIdMunicipio(null);
		conhecimento = new Conhecimento();
		conhecimento.setIdDoctoServico(1234L);

		doctoServicoFranqueado.setConhecimento(conhecimento);

		lstDoctoServicoCifFob.add(doctoServicoFranqueado);

	}

	//FOB Expedido
	public void calculaParticipacaoBaseRegraFobExpedidoCliente() {

		franquia.setIdFranquia(403L);
		doctoServicoFranqueado.setTpFrete(new DomainValue("FE")); //TP_FRETE IN ('FE', 'CR', 'FR', 'CE', 'FL')
		doctoServicoFranqueado.setVlBaseCalculo(new BigDecimal(75.50));
		doctoServicoFranqueado.setFranquia(franquia);
		dtInicioCompetencia = APRIL_05;

		cliente.setIdCliente(12345L);
		municipio.setIdMunicipio(null);
		conhecimento = new Conhecimento();
		conhecimento.setIdDoctoServico(1234L);
		
		doctoServicoFranqueado.setConhecimento(conhecimento);

		lstDoctoServicoCifFob.add(doctoServicoFranqueado);

	}
	
	public void calculaParticipacaoBaseRegraFobExpedidoMunicipio() {
	
		// CIF_EXPEDIDO
		franquia.setIdFranquia(403L);
		doctoServicoFranqueado.setTpFrete(new DomainValue("FE")); //TP_FRETE IN ('FE', 'CR', 'FR', 'CE', 'FL')
		doctoServicoFranqueado.setVlBaseCalculo(new BigDecimal(50.50));
		doctoServicoFranqueado.setFranquia(franquia);
		dtInicioCompetencia = APRIL_05;

		cliente.setIdCliente(null);
		municipio.setIdMunicipio(23456L);
		conhecimento = new Conhecimento();
		conhecimento.setIdDoctoServico(21234L);

		doctoServicoFranqueado.setConhecimento(conhecimento);

		lstDoctoServicoCifFob.add(doctoServicoFranqueado);
	}

	public void calculaParticipacaoBaseRegraFobExpedidoNull() {
	
		// CIF_EXPEDIDO
		franquia.setIdFranquia(403L);
		doctoServicoFranqueado.setTpFrete(new DomainValue("FE")); //TP_FRETE IN ('FE', 'CR', 'FR', 'CE', 'FL')
		doctoServicoFranqueado.setVlBaseCalculo(new BigDecimal(100.50));
		doctoServicoFranqueado.setFranquia(franquia);
		dtInicioCompetencia = APRIL_05;

		cliente.setIdCliente(null);
		municipio.setIdMunicipio(null);
		conhecimento = new Conhecimento();
		conhecimento.setIdDoctoServico(1234L);

		doctoServicoFranqueado.setConhecimento(conhecimento);

		lstDoctoServicoCifFob.add(doctoServicoFranqueado);

	}

	//CIF Recebido
	@Test
	public void calculaParticipacaoBaseRegraCifRecebidoCliente() {

		// CIF_EXPEDIDO
		franquia.setIdFranquia(403L);
		doctoServicoFranqueado.setTpFrete(new DomainValue("CR")); //TP_FRETE IN ('FE', 'CR', 'FR', 'CE', 'FL')
		doctoServicoFranqueado.setVlBaseCalculo(new BigDecimal(75.50));
		doctoServicoFranqueado.setFranquia(franquia);
		dtInicioCompetencia = APRIL_05;

		cliente.setIdCliente(12345L);
		municipio.setIdMunicipio(null);
		conhecimento = new Conhecimento();
		conhecimento.setIdDoctoServico(1234L);
		
		doctoServicoFranqueado.setConhecimento(conhecimento);

		lstDoctoServicoCifFob.add(doctoServicoFranqueado);

	}
	
	public void calculaParticipacaoBaseRegraCifRecebidoMunicipio() {
	
		// CIF_EXPEDIDO
		franquia.setIdFranquia(403L);
		doctoServicoFranqueado.setTpFrete(new DomainValue("CR")); //TP_FRETE IN ('FE', 'CR', 'FR', 'CE', 'FL')
		doctoServicoFranqueado.setVlBaseCalculo(new BigDecimal(50.50));
		doctoServicoFranqueado.setFranquia(franquia);
		dtInicioCompetencia = APRIL_05;

		cliente.setIdCliente(null);
		municipio.setIdMunicipio(23456L);
		conhecimento = new Conhecimento();
		conhecimento.setIdDoctoServico(1234L);

		doctoServicoFranqueado.setConhecimento(conhecimento);

		lstDoctoServicoCifFob.add(doctoServicoFranqueado);

		
	}

	public void calculaParticipacaoBaseRegraCifRecebidoNull() {
	
		// CIF_EXPEDIDO
		franquia.setIdFranquia(403L);
		doctoServicoFranqueado.setTpFrete(new DomainValue("CR")); //TP_FRETE IN ('FE', 'CR', 'FR', 'CE', 'FL')
		doctoServicoFranqueado.setVlBaseCalculo(new BigDecimal(100.50));
		doctoServicoFranqueado.setFranquia(franquia);
		dtInicioCompetencia = APRIL_05;

		cliente.setIdCliente(null);
		municipio.setIdMunicipio(null);
		conhecimento = new Conhecimento();
		conhecimento.setIdDoctoServico(1234L);

		doctoServicoFranqueado.setConhecimento(conhecimento);

		lstDoctoServicoCifFob.add(doctoServicoFranqueado);

	}

	
	//CIF Recebido
	@Test
	public void calculaParticipacaoBaseRegraFobRecebidoCliente() {

		// CIF_EXPEDIDO
		franquia.setIdFranquia(403L);
		doctoServicoFranqueado.setTpFrete(new DomainValue("FR")); //TP_FRETE IN ('FE', 'CR', 'FR', 'CE', 'FL')
		doctoServicoFranqueado.setVlBaseCalculo(new BigDecimal(75.50));
		doctoServicoFranqueado.setFranquia(franquia);
		dtInicioCompetencia = APRIL_05;

		cliente.setIdCliente(12345L);
		municipio.setIdMunicipio(null);
		conhecimento = new Conhecimento();
		conhecimento.setIdDoctoServico(12345L);

		doctoServicoFranqueado.setConhecimento(conhecimento);

		lstDoctoServicoCifFob.add(doctoServicoFranqueado);

	}
	
	@Test
	public void calculaParticipacaoBaseRegraFobRecebidoMunicipio() {
	
		// CIF_EXPEDIDO
		franquia.setIdFranquia(403L);
		doctoServicoFranqueado.setTpFrete(new DomainValue("FR")); //TP_FRETE IN ('FE', 'CR', 'FR', 'CE', 'FL')
		doctoServicoFranqueado.setVlBaseCalculo(new BigDecimal(50.50));
		doctoServicoFranqueado.setFranquia(franquia);
		dtInicioCompetencia = APRIL_05;

		cliente.setIdCliente(null);
		municipio.setIdMunicipio(23456L);
		conhecimento = new Conhecimento();
		conhecimento.setIdDoctoServico(21234L);

		doctoServicoFranqueado.setConhecimento(conhecimento);

		lstDoctoServicoCifFob.add(doctoServicoFranqueado);

	}

	public void calculaParticipacaoBaseRegraFobRecebidoNull() {
	
		// CIF_EXPEDIDO
		franquia.setIdFranquia(403L);
		doctoServicoFranqueado.setTpFrete(new DomainValue("FR")); //TP_FRETE IN ('FE', 'CR', 'FR', 'CE', 'FL')
		doctoServicoFranqueado.setVlBaseCalculo(new BigDecimal(100.50));
		doctoServicoFranqueado.setFranquia(franquia);
		dtInicioCompetencia = APRIL_05;

		cliente.setIdCliente(null);
		municipio.setIdMunicipio(null);
		conhecimento = new Conhecimento();
		conhecimento.setIdDoctoServico(1234L);

		doctoServicoFranqueado.setConhecimento(conhecimento);

		lstDoctoServicoCifFob.add(doctoServicoFranqueado);

	}
	
	private ArrayList<FixoFranqueado> buildFixosFranqueado(YearMonthDay vigenciaInicial, YearMonthDay VigenciaFinal) {
		FixoFranqueado fixoFranqueado = new FixoFranqueado();
		fixoFranqueado.setDtVigenciaInicial(vigenciaInicial);
		fixoFranqueado.setDtVigenciaFinal(VigenciaFinal);

		ArrayList<FixoFranqueado> fixosFranqueado = new ArrayList<FixoFranqueado>();
		fixosFranqueado.add(fixoFranqueado);
		return fixosFranqueado;
	}
	
	private List<FixoFranqueado> retornaFixoFranqueado() {
		List<FixoFranqueado> lstFixo = new ArrayList<FixoFranqueado>();
		FixoFranqueado ff = new FixoFranqueado();
		
/*		cliente = new Cliente();
		municipio = new Municipio();
		cliente.setIdCliente(12345L);
		municipio.setIdMunicipio(123L);
*/
		// Cliente
		ff.setIdFixoFrq(1);
		ff.setCliente(cliente);
		ff.setMunicipio(null);
		ff.setVlColeta(new BigDecimal(0.50));
		ff.setVlEntrega(new BigDecimal(99.50));
		lstFixo.add(ff);

		// Município
		ff = new FixoFranqueado();
		ff.setIdFixoFrq(2);
		ff.setCliente(null);
		ff.setMunicipio(municipio);
		ff.setVlColeta(new BigDecimal(1.50));
		ff.setVlEntrega(new BigDecimal(100.50));
		lstFixo.add(ff);

		// null
		ff = new FixoFranqueado();
		ff.setCliente(null);
		ff.setMunicipio(null);
		ff.setIdFixoFrq(3);
		ff.setVlColeta(new BigDecimal(0.50));
		ff.setVlEntrega(new BigDecimal(99.50));
		lstFixo.add(ff);

		// Município
		ff = new FixoFranqueado();
		ff.setIdFixoFrq(4);
		ff.setCliente(null);
		ff.setMunicipio(municipio);
		ff.setVlColeta(new BigDecimal(2.50));
		ff.setVlEntrega(new BigDecimal(200.50));
		lstFixo.add(ff);

		// null
		ff = new FixoFranqueado();
		ff.setCliente(null);
		ff.setMunicipio(null);
		ff.setIdFixoFrq(5);
		ff.setVlColeta(new BigDecimal(0.50));
		ff.setVlEntrega(new BigDecimal(99.50));
		lstFixo.add(ff);

		// Cliente
		ff = new FixoFranqueado();
		ff.setIdFixoFrq(6);
		ff.setMunicipio(null);
		ff.setCliente(cliente);
		ff.setVlColeta(new BigDecimal(0.50));
		ff.setVlEntrega(new BigDecimal(99.50));
		lstFixo.add(ff);

		return lstFixo;
	}
}
