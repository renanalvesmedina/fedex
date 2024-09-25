<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.portaria.manterConfiguracaoAuditoriasAction" >
	<adsm:form action="portaria/manterConfiguracaoAuditorias" onDataLoadCallBack="onDataLoadCallback" idProperty="idConfiguracaoAuditoria" service="lms.portaria.manterConfiguracaoAuditoriasAction.findByIdMap">

		<adsm:lookup property="filial" idProperty="idFilial" required="true" criteriaProperty="sgFilial" maxLength="3" service="lms.portaria.manterConfiguracaoAuditoriasAction.findLookupFilial" dataType="text" label="filial" size="3" action="/municipios/manterFiliais" labelWidth="20%" width="80%" minLengthForAutoPopUpSearch="3" exactMatch="true" disabled="false" >
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping criteriaProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" />
			<adsm:hidden property="filial.empresa.tpEmpresa" value="M"/>
		</adsm:lookup>
		
		<adsm:combobox property="tpOperacao" labelWidth="20%" label="tipoOperacao" domain="DM_TIPO_OPERACAO_AUDITORIA" width="30%" required="true" />

		<adsm:range label="horarioAuditoria" labelWidth="20%" width="80%" required="true">
             <adsm:textbox dataType="JTTime" property="hrConfiguracaoInicial" />
             <adsm:textbox dataType="JTTime" property="hrConfiguracaoFinal" />
        </adsm:range>

		<adsm:textbox dataType="integer" property="qtVeiculosProprios" label="quantidadeVeiculosProprios" maxLength="3" size="7" labelWidth="20%" width="30%" cellStyle="vertical-align:bottom;"/>
		<adsm:textbox dataType="integer" property="qtVeiculosTerceiros" label="quantidadeVeiculosTerceiros" maxLength="3" size="7" labelWidth="20%" width="30%" cellStyle="vertical-align:bottom;"/>

		<adsm:textbox dataType="integer" property="nrPrazoAuditoria" label="prazoEntreAuditorias" maxLength="3" size="7" labelWidth="20%" width="30%" unit="dias" required="true"/>
		<adsm:textbox dataType="integer" property="hrTempoAuditoria" label="tempoEntreAuditorias" maxLength="6" size="7" labelWidth="20%" width="30%" unit="h" required="true"/>

		<adsm:range label="vigencia" labelWidth="20%" width="80%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>

		<adsm:buttonBar>
			<adsm:storeButton id="storeButton" callbackProperty="storeCallback"/>
			<adsm:button buttonType="new" caption="limpar" id="newButton" onclick="onNewButtonClick();"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	function storeCallback_cb(data, error){
		store_cb(data, error);
		if (error == undefined && data != undefined){
			var acaoVigenciaAtual = getNestedBeanPropertyValue(data,"acaoVigenciaAtual");
			var store = "true";
			validaAcaoVigencia(acaoVigenciaAtual, store);
		}
	}
	
	function onDataLoadCallback_cb(data, exception,key) {
		onDataLoad_cb(data, exception); 
		if (data != undefined) {
			var acaoVigenciaAtual = getNestedBeanPropertyValue(data,"acaoVigenciaAtual");
			validaAcaoVigencia(acaoVigenciaAtual, null);
		}
	}
	
	function validaAcaoVigencia(acaoVigenciaAtual, tipoEvento){
		    if (acaoVigenciaAtual == 0) {
				estadoNovo();
				if(tipoEvento == "" ||  tipoEvento == null)
     				setFocusOnFirstFocusableField(document);
			    else
			       setFocus(document.getElementById("newButton"), false);
			} else if (acaoVigenciaAtual == 1) {
				setDisabled(document, true);
				setDisabled("newButton", false);
				setDisabled("storeButton", false);
				setDisabled("dtVigenciaFinal", false);
				if(tipoEvento == "" ||  tipoEvento == null)
     				setFocusOnFirstFocusableField(document);
			    else
			       setFocus(document.getElementById("newButton"), false);
			} else if (acaoVigenciaAtual == 2) {
				setDisabled(document, true);
				setDisabled("newButton", false);
				setFocus(document.getElementById("newButton"), false);
			}
	}
	
	function estadoNovo() {
		setDisabled(document, false);		
		setDisabled('filial.pessoa.nmFantasia', true);
		setFocusOnFirstFocusableField();
	}
	
	function onNewButtonClick() {
		newButtonScript(document, true, {name:'newButton_click'});
		estadoNovo();
		setDisabled("removeButton", true);
	}
	
	function initWindow(eventObj) {		
		if (eventObj.name == "tab_click") { 
			estadoNovo();			
			setDisabled("removeButton", true);
		}
    }

</script>