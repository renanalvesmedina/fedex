<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contratacaoveiculos.meioTranspProprietarioService" >
	<adsm:form action="/contratacaoVeiculos/manterMeiosTransporteProprietarios" idProperty="idMeioTranspProprietario" >
		
		<adsm:hidden property="dontFillFilial" value="true" />
		<adsm:lookup dataType="text" property="proprietario" idProperty="idProprietario"
				service="lms.contratacaoveiculos.proprietarioService.findLookup"
				action="/contratacaoVeiculos/manterProprietarios" criteriaProperty="pessoa.nrIdentificacao"
				label="proprietario" labelWidth="15%" width="19%" size="20" maxLength="20" onDataLoadCallBack="proprietarioDataLoad" onPopupSetValue="proprietarioPopup">
			<adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa"
					modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping criteriaProperty="dontFillFilial" modelProperty="dontFillFilial" />
			<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" serializable="false" size="50" width="55%" disabled="true"/>
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporte2" idProperty="idMeioTransporte"
				service="lms.contratacaoveiculos.meioTransporteService.findLookup" picker="false"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" criteriaProperty="nrFrota"
				label="meioTransporte" labelWidth="15%" width="85%" size="8" serializable="false" maxLength="6" >
			<adsm:propertyMapping criteriaProperty="meioTransporte.nrIdentificador"
					modelProperty="nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte"
					modelProperty="idMeioTransporte" />
			<adsm:propertyMapping relatedProperty="meioTransporte.nrIdentificador"
					modelProperty="nrIdentificador" />
			<adsm:lookup dataType="text" property="meioTransporte" idProperty="idMeioTransporte"
					service="lms.contratacaoveiculos.meioTransporteService.findLookup" picker="true" maxLength="25"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" criteriaProperty="nrIdentificador"
					size="20" >
				<adsm:propertyMapping criteriaProperty="meioTransporte2.nrFrota"
						modelProperty="nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporte2.idMeioTransporte"
						modelProperty="idMeioTransporte" />	
				<adsm:propertyMapping relatedProperty="meioTransporte2.nrFrota"
						modelProperty="nrFrota" />		
			</adsm:lookup>
		</adsm:lookup>
		
		
		
		
		<adsm:range label="vigencia">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="meioTranspProprietario" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form> 
	<adsm:grid property="meioTranspProprietario" idProperty="idMeioTranspProprietario" unique="true"
			defaultOrder="dtVigenciaInicial:desc" rows="12" >
		<adsm:gridColumn width="60" title="identificacao" property="proprietario.pessoa.tpIdentificacao" isDomain="true" />
		<adsm:gridColumn width="130" title="" property="proprietario.pessoa.nrIdentificacaoFormatado" align="right"/>
		<adsm:gridColumn width="" title="proprietario" property="proprietario.pessoa.nmPessoa" />
		<adsm:gridColumn width="80" title="meioTransporte" property="meioTransporte.nrFrota" />
		<adsm:gridColumn width="80" title="" property="meioTransporte.nrIdentificador" />
		<adsm:gridColumn width="90" title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" />
		<adsm:gridColumn width="90" title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" />
		
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

	function proprietarioDataLoad_cb(data){
		proprietario_pessoa_nrIdentificacao_exactMatch_cb(data);
		var nrIdentificacaoFormatado = getNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacaoFormatado");
		if (nrIdentificacaoFormatado != undefined && nrIdentificacaoFormatado != ''){
			setElementValue("proprietario.pessoa.nrIdentificacao", nrIdentificacaoFormatado);
		}
	}
	
	
	function proprietarioPopup(data){
		var nrIdentificacaoFormatado = getNestedBeanPropertyValue(data, "pessoa.nrIdentificacaoFormatado");
		if (nrIdentificacaoFormatado != undefined && nrIdentificacaoFormatado != ''){
			setNestedBeanPropertyValue(data, "pessoa.nrIdentificacao", nrIdentificacaoFormatado);
		}
	}

</script>
