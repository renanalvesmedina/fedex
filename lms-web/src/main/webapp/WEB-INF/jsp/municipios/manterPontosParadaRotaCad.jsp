<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterPontosParadaRotaAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/municipios/manterPontosParadaRota" idProperty="idPontoParada" onDataLoadCallBack="pontosParadaRotaDataLoad">

		<adsm:hidden property="pessoa.idPessoa" serializable="true"/>
		<adsm:hidden property="pessoa.nmPessoa" serializable="true"/>
		<adsm:hidden property="tpSituacaoMunicipios" serializable="false" value="A"/>

        <adsm:textbox dataType="text" property="nmPontoParada" size="40" maxLength="50" label="local" required="true" onchange="setaNmPessoa(this);"/>

		<adsm:lookup service="lms.municipios.manterPontosParadaRotaAction.findLookupMunicipio" required="true" dataType="text" disabled="false" property="municipio" criteriaProperty="nmMunicipio" label="municipio" size="30" maxLength="30" action="/municipios/manterMunicipios" labelWidth="15%" width="85%" idProperty="idMunicipio" exactMatch="false" minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa" />			
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa" />						
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="unidadeFederativa.pais.idPais" />			
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.siglaDescricao" modelProperty="unidadeFederativa.siglaDescricao" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" />
			<adsm:propertyMapping relatedProperty="municipio.nmMunicipio" modelProperty="nmMunicipio"/>
			<adsm:propertyMapping criteriaProperty="tpSituacaoMunicipios" modelProperty="tpSituacao"/>
		</adsm:lookup>

		<adsm:hidden property="municipio.unidadeFederativa.idUnidadeFederativa" serializable="true"/>		
		<adsm:hidden property="municipio.unidadeFederativa.sgUnidadeFederativa" serializable="false"/>		
		<adsm:hidden property="municipio.unidadeFederativa.nmUnidadeFederativa" serializable="false"/>				
		<adsm:textbox dataType="text" property="municipio.unidadeFederativa.siglaDescricao" label="uf" size="30" disabled="true" />
		<adsm:hidden property="municipio.unidadeFederativa.pais.idPais" serializable="false"/>
		<adsm:textbox dataType="text" property="municipio.unidadeFederativa.pais.nmPais" label="pais" disabled="true" size="40"/>

		<adsm:hidden property="rodovia.tpSituacao" value="A" serializable="false" />

		<adsm:lookup service="lms.municipios.manterPontosParadaRotaAction.findLookupRodovia" dataType="text" 
			property="rodovia" idProperty="idRodovia" 
			criteriaProperty="sgRodovia" label = "rodovia" 
			size="10" maxLength="10" width="35%" 
			action="/municipios/manterRodovias">
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="pais.idPais"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="pais.nmPais" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="rodovia.tpSituacao" modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="rodovia.dsRodovia" modelProperty="dsRodovia" />
			<adsm:textbox dataType="text" property="rodovia.dsRodovia" size="26" disabled="true" serializable="false" />
		</adsm:lookup>
		<adsm:textbox dataType="decimal" property="nrKm" label="km" maxLength="6" size="15" mask="###,###" />

		<adsm:checkbox property="blAduana" label="aduana" onclick="onBlAduanaClick();"/>
		
		<adsm:section caption="aduana" />
		
		<adsm:textbox dataType="text" property="sgAduana" label="sigla" maxLength="10" size="10" />
		<adsm:textbox dataType="integer" property="cdAduana" label="codigo" maxLength="7" size="7" />
		
		<adsm:buttonBar>
			<adsm:button caption="enderecos" action="/configuracoes/manterEnderecoPessoa" cmd="main">
				<adsm:linkProperty src="idPontoParada" target="pessoa.idPessoa"/>			
				<adsm:linkProperty src="nmPontoParada" target="pessoa.nmPessoa"/>			
				<adsm:linkProperty src="municipio.idMunicipio" target="municipio.idMunicipio"/>
				<adsm:linkProperty src="municipio.nmMunicipio" target="municipio.nmMunicipio"/>

				<adsm:linkProperty src="municipio.unidadeFederativa.idUnidadeFederativa" target="municipio.unidadeFederativa.idUnidadeFederativa"/>
				<adsm:linkProperty src="municipio.unidadeFederativa.sgUnidadeFederativa" target="municipio.unidadeFederativa.sgUnidadeFederativa"/>				
				<adsm:linkProperty src="municipio.unidadeFederativa.nmUnidadeFederativa" target="municipio.unidadeFederativa.nmUnidadeFederativa"/>	
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>
			</adsm:button>
			
			<adsm:button caption="contatos" action="/configuracoes/manterContatos" cmd="main">
				<adsm:linkProperty src="idPontoParada" target="pessoa.idPessoa"/>			
				<adsm:linkProperty src="nmPontoParada" target="pessoa.nmPessoa"/>			
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>
			</adsm:button>
 			
 			<adsm:button caption="inscricoesEstaduais" id="buttonInscricaoEstadual" action="/configuracoes/manterInscricoesEstaduais" cmd="main">
				<adsm:linkProperty src="idPontoParada" target="pessoa.idPessoa"/>				
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>						
			</adsm:button>	
			
			<adsm:storeButton/>
			<adsm:newButton onclick="desabilitaCamposAduana()" id="botaoLimpar"/>
			<adsm:removeButton/>
		</adsm:buttonBar>
		<adsm:hidden property="labelPessoa"/>
		<script>
		var labelPessoaTemp = '<adsm:label key="pontoParada"/>';		
		</script>
	</adsm:form>
</adsm:window>
<script>
   function verificaBlAduanaChecado(){
   		if(document.getElementById("blAduana").checked == false){
			document.getElementById("sgAduana").required = "false";
			document.getElementById("cdAduana").required = "false";
			setDisabled("sgAduana",true);
			setDisabled("cdAduana",true);
		}else{
			setDisabled("sgAduana",false);
			setDisabled("cdAduana",false);
			document.getElementById("sgAduana").required = "true";
			document.getElementById("cdAduana").required = "true";
		}
		
   }
   
   function blAduanaDefault(){
   		document.getElementById("sgAduana").required = "false";
		document.getElementById("cdAduana").required = "false";
		setDisabled("sgAduana",true);
		setDisabled("cdAduana",true);
   }

	function desabilitaCamposAduana(){
		newButtonScript();
		blAduanaDefault();
	} 
	
	function initWindow(eventObj){
		if(eventObj.name != "gridRow_click" && eventObj.name != "storeButton" ){
		    blAduanaDefault();
		}
	}	
	
	function pontosParadaRotaDataLoad_cb(data,exception){
		onDataLoad_cb(data,exception);
		if(exception == undefined){
			verificaBlAduanaChecado();
		}
	}

    document.getElementById("labelPessoa").masterLink = "true";
    
    function setaNmPessoa(e) {
    	if (e.value != "")
    		setElementValue(document.getElementById("pessoa.nmPessoa"), e.value);
    }
    
    
    function onBlAduanaClick() {
        resetValue(document.getElementById("sgAduana"));
    	resetValue(document.getElementById("cdAduana"));
    	verificaBlAduanaChecado();
     }
    
    function pageLoad_cb(data,erro) {
    	if (document.getElementById("idPontoParada").value != "" )
    		setElementValue(document.getElementById("pessoa.idPessoa"), document.getElementById("idPontoParada").value );
    		
		onPageLoad_cb(data,erro);
	    setElementValue(document.getElementById("labelPessoa"),labelPessoaTemp);
	    blAduanaDefault();
    }
    
</script>