<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contratacaoveiculos.meioTranspRodoMotoristaService" onPageLoadCallBack="pageLoadCustom" >
	<adsm:form action="/contratacaoVeiculos/manterMeiosTransporteRodoviariosMotorista" idProperty="idMeioTranspRodoMotorista" >
		
		<adsm:hidden property="motorista.tpVinculo" />
		<adsm:hidden property="meioTransporteRodoviario.meioTransporte.tpVinculo" />
		
		<adsm:lookup 
			dataType="text"
			property="motorista"
			idProperty="idMotorista"
			service="lms.contratacaoveiculos.motoristaService.findLookupMotorista"
			action="/contratacaoVeiculos/manterMotoristas" 
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" 
			maxLength="20"
			label="motorista"
			labelWidth="23%"
			width="77%"
			size="20"
			exactMatch="false"
			minLengthForAutoPopUpSearch="5">
			<adsm:propertyMapping criteriaProperty="motorista.tpVinculo" modelProperty="tpVinculo" />
			<adsm:propertyMapping relatedProperty="motorista.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="motorista.pessoa.nmPessoa" size="35" serializable="false" disabled="true" />
		</adsm:lookup>
 
		<adsm:lookup dataType="text" property="meioTransporteRodoviario2" idProperty="idMeioTransporte"
				service="lms.contratacaoveiculos.meioTransporteRodoviarioService.findLookup" picker="false"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota"
				label="meioTransporte" labelWidth="23%" width="77%" size="8" serializable="false" maxLength="6">
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte"
					modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
					
			<adsm:lookup dataType="text" property="meioTransporteRodoviario" idProperty="idMeioTransporte"
					service="lms.contratacaoveiculos.meioTransporteRodoviarioService.findLookup" picker="true"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador"
					size="20" maxLength="25" >
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte"
						modelProperty="idMeioTransporte" />	
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />		
			</adsm:lookup>
		</adsm:lookup>

		<adsm:range label="vigencia" labelWidth="23%" width="60%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="meioTranspRodoMotorista" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="meioTranspRodoMotorista" idProperty="idMeioTranspRodoMotorista" unique="true"
			defaultOrder="motorista_pessoa_.nmPessoa,meioTransporteRodoviario_meioTransporte_.nrFrota" rows="12" >
		
		<adsm:gridColumn width="40" title="identificacao" property="motorista.pessoa.tpIdentificacao" isDomain="true" />
		<adsm:gridColumn width="110" title="" property="motorista.pessoa.nrIdentificacaoFormatado" align="right"/>
		<adsm:gridColumn width="" title="motorista" property="motorista.pessoa.nmPessoa" />
		<adsm:gridColumn width="70" title="meioTransporte" property="meioTransporteRodoviario.meioTransporte.nrFrota" />
		<adsm:gridColumn width="70" title="" property="meioTransporteRodoviario.meioTransporte.nrIdentificador" />
		<adsm:gridColumn width="100" title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" />
		<adsm:gridColumn width="100" title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" />
		
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--
	document.getElementById("motorista.tpVinculo").masterLink = true;
	
	function pageLoadCustom_cb(data) {
		onPageLoad_cb(data);
		
		// Se vier da tela de Meio de Transporte com vínculo Próprio, só permite motorista Funcionário.
		document.getElementById("motorista.tpVinculo").masterLink = true;
		var tpVinculoMeioTransporte = getElementValue("meioTransporteRodoviario.meioTransporte.tpVinculo");
		if (tpVinculoMeioTransporte == 'P') {
			setElementValue("motorista.tpVinculo",'F');
		} else {
			resetValue("motorista.tpVinculo");
		}
	}
	
//-->
</script>