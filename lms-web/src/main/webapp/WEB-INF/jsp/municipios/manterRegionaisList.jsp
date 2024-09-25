<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.regionalService" onPageLoadCallBack="pageLoad">
	<adsm:form idProperty="idRegional" action="/municipios/manterRegionais">
		<adsm:textbox dataType="text" property="sgRegional" label="sigla" maxLength="3" size="3" labelWidth="17%" onchange="onSgRegionalChange();"/>
		<adsm:textbox dataType="text" property="dsRegional" label="descricao" maxLength="60" size="37" labelWidth="13%" />
		
		<adsm:hidden property="cd_funcao" value="" serializable="false"/>
		<adsm:hidden property="ds_funcao" value="" serializable="false"/>
		
		<adsm:lookup label="responsavelRegional" size="16" maxLength="16" labelWidth="17%" width="16%" 
					 dataType="text" property="usuario" idProperty="idUsuario" criteriaProperty="nrMatricula" 
					 service="lms.municipios.manterRegionaisAction.findLookupUsuarioFuncionario" 
				     action="/configuracoes/consultarFuncionariosView" serializable="true">
				     
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario"/>
			<adsm:propertyMapping criteriaProperty="cd_funcao" modelProperty="codFuncao.codigo" disable="false" inlineQuery="false"/>
		    <adsm:propertyMapping criteriaProperty="cd_funcao" modelProperty="codFuncao.idCodigo" disable="false" inlineQuery="false"/>
		    <adsm:propertyMapping criteriaProperty="ds_funcao" modelProperty="codFuncao.nome" disable="false" inlineQuery="false"/>
		    
		</adsm:lookup>
		<adsm:textbox dataType="text" property="usuario.nmUsuario" size="30" maxLength="45" disabled="true" width="67%" serializable="false"/>
        
		<adsm:range label="vigencia" labelWidth="17%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
		
 		<adsm:buttonBar freeLayout="true">
 			
 			<adsm:findButton callbackProperty="regional" />
			<adsm:resetButton /> 
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="regional" idProperty="idRegional" selectionMode="check" gridHeight="200" rows="12" unique="true" 
			   service="lms.municipios.manterRegionaisAction.findPaginatedCustom"
			   rowCountService="lms.municipios.manterRegionaisAction.getRowCountCustom">
		<adsm:gridColumn title="sigla" dataType="text" property="sgRegional" width="7%" />
		<adsm:gridColumn title="descricao" dataType="text"  property="dsRegional" width="35%" />
		<adsm:gridColumn title="responsavelRegional" dataType="text" property="nmUsuario" width="34%" />
		<adsm:gridColumn title="vigenciaInicial" dataType="JTDate" property="dtVigenciaInicial" width="12%"  />
		<adsm:gridColumn title="vigenciaFinal" dataType="JTDate" property="dtVigenciaFinal" width="12%"  />
		<adsm:buttonBar>
			
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
function onSgRegionalChange() {
	document.getElementById("sgRegional").value = document.getElementById("sgRegional").value.toUpperCase();
}
	
    function pageLoad_cb(data) {
    	onPageLoad_cb(data);
	    var remoteCall = {serviceDataObjects:new Array()};
		remoteCall.serviceDataObjects.push(createServiceDataObject("lms.municipios.regionalService.findParameterFuncionario", "funcao", {value:"CD_GERENTE_REGIONAL"}));
		xmit(remoteCall);
		document.getElementById("ds_funcao").masterLink = "true";
		document.getElementById("cd_funcao").masterLink = "true";
    }
    
    function funcao_cb(data,exception) {
    	if  (exception) {
    		alert(exception);
    		return;
    	}
    	onDataLoad_cb(data);
    }
</script>