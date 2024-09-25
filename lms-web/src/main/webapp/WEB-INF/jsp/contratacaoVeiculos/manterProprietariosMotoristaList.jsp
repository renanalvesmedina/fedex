<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		if (getElementValue("motorista.pessoa.nrIdentificacao") != "" && getElementValue("motorista.pessoa.nmPessoa") == "")
			lookupChange({e:document.forms[0].elements["motorista.idMotorista"],forceChange:true});
	}
//-->
</script>
<adsm:window title="consultarMotoristasProprietario" service="lms.contratacaoveiculos.proprietarioMotoristaService" onPageLoadCallBack="pageLoad">
	
	<adsm:form action="/contratacaoVeiculos/manterProprietariosMotorista" idProperty="idProprietarioMotorista">
	
		<adsm:lookup property="proprietario" service="lms.contratacaoveiculos.proprietarioService.findLookup" idProperty="idProprietario"  dataType="text" 
					criteriaProperty="pessoa.nrIdentificacao" label="proprietario" size="21" maxLength="20" 
					action="/contratacaoVeiculos/manterProprietarios" width="19%" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado">				
         	<adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
	    </adsm:lookup>
	     
		<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="50" maxLength="50" width="50%" disabled="true" />
		
		<adsm:lookup property="motorista" service="lms.contratacaoveiculos.motoristaService.findLookup" idProperty="idMotorista" dataType="text" 
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" label="motorista" size="21" maxLength="20" action="/contratacaoVeiculos/manterMotoristas" width="19%" >
		 	<adsm:propertyMapping relatedProperty="motorista.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
	    </adsm:lookup>
	    
		<adsm:textbox dataType="text" property="motorista.pessoa.nmPessoa" size="30" maxLength="50" width="50%" disabled="true"  />
	
		<adsm:range label="vigencia" width="85%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
        
		<adsm:buttonBar freeLayout="true"> 
			<adsm:findButton callbackProperty="proprietarioMotorista"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idProprietarioMotorista" rows="11" scrollBars="horizontal" defaultOrder="proprietario_pessoa_.nmPessoa, motorista_pessoa_.nmPessoa"
				property="proprietarioMotorista" gridHeight="220" unique="true">
		<adsm:gridColumn title="identificacao"  property="proprietario.pessoa.tpIdentificacao" isDomain="true" width="60"/>
		<adsm:gridColumn title="" 				property="proprietario.pessoa.nrIdentificacaoFormatado" width="130" align="right"/>
		<adsm:gridColumn title="proprietario"   property="proprietario.pessoa.nmPessoa" width="160" />
		<adsm:gridColumn title="identificacao"  property="motorista.pessoa.tpIdentificacao" isDomain="true" width="60"/>
		<adsm:gridColumn title="" 				property="motorista.pessoa.nrIdentificacaoFormatado" width="130" align="right"/>
		<adsm:gridColumn title="motorista" 		property="motorista.pessoa.nmPessoa" width="160" />
		<adsm:gridColumn title="tipoVinculo" 	property="motorista.tpVinculo" width="100" isDomain="true"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="100"/>
		<adsm:gridColumn title="vigenciaFinal" 	property="dtVigenciaFinal"  dataType="JTDate" width="100"/>
	
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar> 
	
	</adsm:grid>
	
</adsm:window>