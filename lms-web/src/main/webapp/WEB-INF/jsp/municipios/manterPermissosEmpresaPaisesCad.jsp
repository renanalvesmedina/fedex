<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.permissoEmpresaPaisService" onPageLoad="permissos_pageLoad">
	<adsm:form action="/municipios/manterPermissosEmpresaPaises" idProperty="idPermissoEmpresaPais" service="lms.municipios.permissoEmpresaPaisService.findByIdDetalhamento" onDataLoadCallBack="pageLoad">
		<adsm:hidden property="tpSituacao" value="A"/>
		<adsm:lookup service="lms.municipios.empresaService.findLookup" dataType="text" minLengthForAutoPopUpSearch="5" 
					 exactMatch="true" property="empresa" idProperty="idEmpresa" criteriaProperty="pessoa.nrIdentificacao" 
					 required="true"
					 label="empresa" action="/municipios/manterEmpresas" size="20" labelWidth="20%" width="75%" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"  >
				  <adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>	 
                  <adsm:propertyMapping relatedProperty="empresa.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
                  <adsm:propertyMapping relatedProperty="empresa.tpSituacao" modelProperty="tpSituacao.value"/>
                  <adsm:hidden property="empresa.tpSituacao"/>
		  		  <adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa"  size="30" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:lookup service="lms.municipios.paisService.findLookup" dataType="text" property="paisByIdPaisOrigem" criteriaProperty="nmPais" idProperty="idPais" label="paisOrigemDestino" action="/municipios/manterPaises" size="30" maxLength="50" labelWidth="20%" width="30%" required="true" exactMatch="false" minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
		</adsm:lookup>

		<adsm:lookup service="lms.municipios.paisService.findLookup" dataType="text" property="paisByIdPaisDestino" criteriaProperty="nmPais" idProperty="idPais" label="paisOrigemDestino" action="/municipios/manterPaises" size="30" maxLength="50" labelWidth="20%" width="30%" required="true" exactMatch="false" minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
		</adsm:lookup>
		
		<adsm:textbox dataType="integer" property="nrPermisso" size="10" maxLength="5" minValue="0" label="numeroPermisso" labelWidth="20%" width="30%" required="true"/>
		<adsm:textbox dataType="text" property="nrPermissoMic" size="10" maxLength="30"  label="numeroPermissoMIC" labelWidth="20%" width="30%" required="true"/>	
		
		<adsm:range label="vigencia" labelWidth="20%" width="80%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
		<adsm:buttonBar>
			<adsm:storeButton callbackProperty="afterStore" service="lms.municipios.permissoEmpresaPaisService.storeMap" />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
		<script language="javascript">		 
	
	function permissos_pageLoad(){
		onPageLoad();
		estadoNovo();
	}
	
	 /**
	 * Retorna estado dos campos como foram carregados na página.
	 */
	function estadoNovo() {		
		setDisabled(document,false);
		setDisabled("empresa.idEmpresa", document.getElementById("empresa.idEmpresa").masterLink == "true");
		setDisabled("empresa.pessoa.nmPessoa", true);
		setDisabled("__buttonBar:0.removeButton",true);
	}
	
	/**
	 * Habilitar campos se o registro estiver vigente.
	 */
	function habilitarCampos() {
		setDisabled("dtVigenciaFinal",false);
	}
	
	/**
	 * Ao carregar os dados, é tratado o retorno da validação de vigência no detalhamento:
	 */
	function pageLoad_cb(data,exception) {
		onDataLoad_cb(data,exception);
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		validaAcaoVigencia(acaoVigenciaAtual, null);
	}
	
	function validaAcaoVigencia(acaoVigenciaAtual, tipoEvento){
		if (acaoVigenciaAtual == 0) {
			estadoNovo();
			setDisabled("__buttonBar:0.removeButton",false);
			if(tipoEvento == "" ||  tipoEvento == null)
    			setFocusOnFirstFocusableField(document);
     		else
    			setFocus(document.getElementById("__buttonBar:0.newButton"),false);
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(document,true);
			setDisabled("__buttonBar:0.newButton",false);
			setDisabled("__buttonBar:0.storeButton",false);
			habilitarCampos();
			if(tipoEvento == "" ||  tipoEvento == null)
    			setFocusOnFirstFocusableField(document);
     		else
    			setFocus(document.getElementById("__buttonBar:0.newButton"),false);
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(document,true);
			setDisabled("__buttonBar:0.newButton",false);
			setFocus(document.getElementById("__buttonBar:0.newButton"),false);
		}
	}
	
	function afterStore_cb(data,exception,key) {
    	store_cb(data,exception,key);
		if (exception == undefined){
	     	var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
	     	var store = "true";
		 	validaAcaoVigencia(acaoVigenciaAtual, store);
		}
    }

	function initWindow(eventObj) {
		if (eventObj.name != "gridRow_click" && eventObj.name != "newButton_click" && eventObj.name != "storeButton")
			estadoNovo();
		if (eventObj.name == "newButton_click"){
			estadoNovo();
			setFocus(document.getElementById("empresa.pessoa.nrIdentificacao"));
		}

	}
		</script>
 </adsm:form>
</adsm:window>
