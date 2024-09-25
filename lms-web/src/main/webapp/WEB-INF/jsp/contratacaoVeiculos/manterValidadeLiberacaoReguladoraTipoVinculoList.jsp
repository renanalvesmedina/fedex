<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterValidadeLiberacaoReguladoraTipoVinculo" service="lms.contratacaoveiculos.regraLiberacaoReguladoraService">
   <adsm:form action="/contratacaoVeiculos/manterValidadeLiberacaoReguladoraTipoVinculo" idProperty="idRegraLiberacaoReguladora" >
	   <adsm:combobox property="reguladoraSeguro.idReguladora" boxWidth="200" label="reguladora"  service="lms.seguros.reguladoraSeguroService.find" optionLabelProperty="pessoa.nmPessoa" optionProperty="idReguladora" labelWidth="20%" width="80%"  />
	   <adsm:combobox property="tpVinculo" label="tipoVinculo" domain="DM_TIPO_VINCULO_CARRETEIRO" labelWidth="20%" width="30%" />
	   <adsm:combobox property="blLiberacaoPorViagem" label="liberacaoViagem"  domain="DM_SIM_NAO" labelWidth="20%" width="30%" />	
       <adsm:range label="vigencia" width="80%" labelWidth="20%" >
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
       </adsm:range>				
	   <adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="regraLiberacaoReguladora"/>
			<adsm:resetButton />
		</adsm:buttonBar> 
	</adsm:form>
	<adsm:grid idProperty="idRegraLiberacaoReguladora" property="regraLiberacaoReguladora" 
			   defaultOrder="reguladoraSeguro_pessoa_.nmPessoa,tpVinculo,dtVigenciaInicial" 
			   gridHeight="220" unique="true" rows="11" scrollBars="horizontal" >
		<adsm:gridColumn title="reguladora" property="reguladoraSeguro.pessoa.nmPessoa" width="250" />	
		<adsm:gridColumn title="servicoLiberacao" property="reguladoraSeguro.nmServicoLiberacaoPrestado" width="160" />	
		<adsm:gridColumn title="tipoVinculo" property="tpVinculo" isDomain="true" width="90" />
		<adsm:gridColumn title="validade" property="qtMesesValidade" dataType="integer" unit="meses" width="115" />		
		<adsm:gridColumn title="liberacaoViagem" property="blLiberacaoPorViagem" renderMode="image-check" width="130" />
		<adsm:gridColumn title="quantidadeViagensAnoLiberacaoAutomatica" property="qtViagensAnoLiberacao" width="245" dataType="integer" />		
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="90" align="center" />
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="90" align="center" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
