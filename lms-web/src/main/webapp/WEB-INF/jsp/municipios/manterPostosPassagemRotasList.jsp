<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function desabilitaLookupPostoPassagem(){
	onPageLoad();
}
function initWindow(eventObj){
	if(eventObj.name == 'cleanButton_click'){
		var dtVigenciaInicial = document.getElementById("dtVigenciaInicial").value;
		var dtVigenciaFinal = document.getElementById("dtVigenciaFinal").value;
		setFocus(document.getElementById("postoPassagem_lupa"), false);
		findFilialUsuario();
	}
	
}

function setaFocoLupa_cb() {
	onPageLoad_cb();
	setMasterLink(document);
	setDisabled("postoPassagem.idPostoPassagem", false);
    setDisabled("postoPassagem.tpPostoPassagem.description", true);
    setFocus(document.getElementById("postoPassagem_lupa"), false);
    findFilialUsuario();
}


function findFilialUsuario(){
		_serviceDataObjects = new Array();
	   	addServiceDataObject(createServiceDataObject("lms.municipios.manterPostoPassagemTrechoAction.findFilialUsuarioLogado", "onDataLoad", new Array()));
	  	xmit();
}

</script>
<adsm:window title="consultarPostosPassagemMunicipios" service="lms.municipios.manterPostoPassagemTrechoAction" onPageLoad="desabilitaLookupPostoPassagem" onPageLoadCallBack="setaFocoLupa">
	<adsm:form action="/municipios/manterPostosPassagemRotas" idProperty="idPostoPassagemTrecho" height="103">
		<adsm:i18nLabels>
				<adsm:include key="LMS-00013"/>
                <adsm:include key="filialOrigem"/>
                <adsm:include key="filialDestino"/>
                <adsm:include key="postoPassagem"/>
        </adsm:i18nLabels>
		
		<adsm:hidden property="tpEmpresaMercurioValue" value="M" serializable="false" />
		
		<adsm:lookup 
	        action="/municipios/manterFiliais" 
	        dataType="text" 
	        property="filialOrigem" 
	        idProperty="idFilial" 
	        criteriaProperty="sgFilial" 
	        service="lms.municipios.manterPostoPassagemTrechoAction.findLookupFilial" 
	        label="filialOrigem" labelWidth="17%" size="3" width="33%" maxLength="3" >
	       
	        <adsm:propertyMapping relatedProperty="filialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialOrigem.pessoa.nmFantasia" size="30" disabled="true"/>
        </adsm:lookup>
        
        <adsm:hidden property="tpAcesso" value="A" serializable="false"/>
        <adsm:lookup 
	        action="/municipios/manterFiliais" 
	        dataType="text" 
	        property="filialDestino" 
	        idProperty="idFilial" 
	        criteriaProperty="sgFilial" 
	        service="lms.municipios.manterPostoPassagemTrechoAction.findLookupFilial" 
	        label="filialDestino" labelWidth="17%" size="3" width="30%" maxLength="3" >
	     
	     	<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"/>
	        <adsm:propertyMapping relatedProperty="filialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" size="30" disabled="true"/>
        </adsm:lookup>
		
		<adsm:lookup 
		  	service="lms.municipios.manterPostoPassagemTrechoAction.findLookupPostoPassagem" 
			dataType="text" 
			property="postoPassagem" 
			criteriaProperty="tpPostoPassagem.description" 
			idProperty="idPostoPassagem" 
			labelWidth="17%"  
			label="postoPassagem" 
			size="35" maxLength="50" 
			width="83%" action="/municipios/manterPostosPassagem" disabled="true">
			<adsm:propertyMapping relatedProperty="postoPassagem.municipio.nmMunicipio" modelProperty="municipio.nmMunicipio" />
	        <adsm:propertyMapping relatedProperty="postoPassagem.municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" />
	        <adsm:propertyMapping relatedProperty="postoPassagem.municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" />
	        <adsm:propertyMapping relatedProperty="postoPassagem.municipio.unidadeFederativa.pais.nmPais" modelProperty="municipio.unidadeFederativa.pais.nmPais" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.dsSentidoCobranca" modelProperty="tpSentidoCobranca.description" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.rodovia.sgRodovia" modelProperty="rodovia.sgRodovia" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.concessionaria.pessoa.nmPessoa" modelProperty="concessionaria.pessoa.nmPessoa"/>
        	<adsm:propertyMapping relatedProperty="postoPassagem.concessionaria.pessoa.nrIdentificacaoFormatado" modelProperty="concessionaria.pessoa.nrIdentificacaoFormatado"/>
        	<adsm:propertyMapping relatedProperty="postoPassagem.nrKm" modelProperty="nrKm" />
	    </adsm:lookup>
	    
	    <adsm:textbox dataType="text" property="postoPassagem.concessionaria.pessoa.nrIdentificacaoFormatado" size="18" label="concessionaria" labelWidth="17%"  disabled="true" width="15%" serializable="false"/>
		<adsm:textbox dataType="text" property="postoPassagem.concessionaria.pessoa.nmPessoa" size="45" disabled="true" width="68%" serializable="false"/>
		
        <adsm:textbox dataType="text" property="postoPassagem.municipio.nmMunicipio" label="localizacao" size="35" maxLength="35" labelWidth="17%" width="33%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" property="postoPassagem.dsSentidoCobranca" label="sentido" size="35" maxLength="35" labelWidth="17%" width="33%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" property="postoPassagem.municipio.unidadeFederativa.sgUnidadeFederativa" label="uf" size="3" maxLength="3" labelWidth="17%" width="5%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" property="postoPassagem.municipio.unidadeFederativa.nmUnidadeFederativa" size="27" width="28%" disabled="true" serializable="false"/>
		
		<adsm:textbox dataType="text" property="postoPassagem.municipio.unidadeFederativa.pais.nmPais" label="pais" size="35" maxLength="35" labelWidth="17%" width="30%" disabled="true" serializable="false"/>
		
		<adsm:textbox dataType="text" property="postoPassagem.rodovia.sgRodovia" label="rodovia" size="35" maxLength="35" labelWidth="17%" width="33%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="integer" mask="##,###.#" property="postoPassagem.nrKm" label="km" size="10" maxLength="10" labelWidth="17%" width="30%" disabled="true" serializable="false"/>
		
		<adsm:range label="vigencia" labelWidth="17%" width="33%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
        
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" buttonType="findButton" disabled="false" onclick="validateDados();" />
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idPostoPassagemTrecho" property="postoPassagemTrecho" scrollBars="horizontal" rows="9" gridHeight="180" unique="true" >
		<adsm:gridColumn title="postoPassagem" property="tpPostoPassagem" width="180" isDomain="true"/>
		<adsm:gridColumn title="filialOrigem" property="filOrigem" width="180" />
		<adsm:gridColumn title="filialDestino" property="filDestino" width="180"/>
		<adsm:gridColumn title="localizacao" property="nmMunicipio" width="180" />
		<adsm:gridColumn title="uf" property="sgUnidadeFederativa" width="50" />
		<adsm:gridColumn title="pais" property="nmPais" width="180" />
		<adsm:gridColumn title="sentido" property="tpSentidoCobranca" width="140" isDomain="true"/>
		<adsm:gridColumn title="rodovia" property="sgRodovia" width="100" />
		<adsm:gridColumn title="km" property="nrKm" width="100" dataType="integer" mask="##,###.#"/>
		<adsm:gridColumn title="concessionaria" property="concessionaria" width="200" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="90" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="80" dataType="JTDate"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

document.getElementById("tpEmpresaMercurioValue").masterLink = true;

function validateDados() {
		var ret = validateTabScript(document.forms); 
		if(ret == true)
			findButtonScript('postoPassagemTrecho', document.forms[0]);
		return false;
}
function validateTab() {
		if (validateTabScript(document.forms)) {
			if (
					(getElementValue("filialOrigem.idFilial") != "") ||
					(getElementValue("filialDestino.idFilial") != "") ||
					(getElementValue("postoPassagem.idPostoPassagem") != "")  ) 
				{
					return true;
				} else {
				
					alert(i18NLabel.getLabel("LMS-00013")
								+ i18NLabel.getLabel("filialOrigem")+ ", "
								+ i18NLabel.getLabel("filialDestino") + ", "
								+ i18NLabel.getLabel("postoPassagem")+ "."
								);
									
					return false;
                }
            }
		return false;
    }	
</script>