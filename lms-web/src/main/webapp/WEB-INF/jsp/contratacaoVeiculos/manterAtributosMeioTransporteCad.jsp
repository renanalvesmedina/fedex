<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contratacaoveiculos.atributoMeioTransporteService" onPageLoadCallBack="atributoMeioTransporteOnPageLoad">
	<adsm:form action="/contratacaoVeiculos/manterAtributosMeioTransporte" idProperty="idAtributoMeioTransporte" onDataLoadCallBack="myOnDataLoad">
		<adsm:textbox dataType="integer" property="cdAtributoMeioTransporte" label="codigo" maxLength="10" size="10" labelWidth="18%" width="32%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" property="dsAtributoMeioTransporte" label="descricaoAtributo" maxLength="60" size="28" required="true" labelWidth="18%" width="32%"/>
	 	<adsm:combobox property="tpComponente" label="componente" domain="DM_TIPO_ATRIBUTO_MEIO_TRANSPORTE" required="true" onchange="onSelectComp(); resetAfterSelectComp();" width="32%" labelWidth="18%"/>
		<adsm:combobox property="tpInformacao" label="formato" domain="DM_TIPO_DADO_ATRIBUTO_VEICULO" onchange="onSelectForm();" width="32%" labelWidth="18%"/>
		<adsm:textbox dataType="integer" property="nrTamanho" label="tamanho" maxLength="18" size="28" width="32%" labelWidth="18%"/>
		<adsm:textbox dataType="integer" property="nrDecimais" label="decimais" maxLength="2" size="28" width="32%" labelWidth="18%"/>
		<adsm:textbox dataType="text" property="dsGrupo" label="grupo" maxLength="60" size="28" required="true" width="32%" labelWidth="18%"/>
		<adsm:textbox dataType="integer" property="nrOrdem" label="ordem" maxLength="3" size="28" required="true" width="32%" labelWidth="18%"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" required="true" labelWidth="18%" width="32%"/>
		<adsm:buttonBar>
			<adsm:button caption="salvar" id="salvar" buttonType="saveButton" onclick="return validateStore()" disabled="false"/>
			<adsm:newButton/>
			<adsm:button id="botaoExcluir" caption="excluir" buttonType="removeButton" onclick="removeButtonScript('lms.contratacaoveiculos.atributoMeioTransporteService.removeById', 'myNewButtonScript', 'idAtributoMeioTransporte', this.document)"/>
		</adsm:buttonBar>
		<script language="javascript">
		
		function atributoMeioTransporteOnPageLoad_cb(data,exception){
			onPageLoad_cb(data, exception);
			onSelectComp();
		}
		
		//funcao chamada no botao excluir
		function myNewButtonScript_cb(data,exception){
		     if (exception != undefined){
		     	alert(exception);
		     }else{
		        newButtonScript();
		     	setDisabled("tpInformacao",true);
		     	setDisabled("nrDecimais",true);
		     	setDisabled("nrTamanho",true);
		     }
		}
		
		function myStoreButton_cb(data, exception){
		    store_cb(data,exception);
		}
		
		function resetAfterSelectComp() {
			var selectedType = getElementValue("tpComponente");
			if (selectedType == '') {
				setElementValue("tpInformacao", "");
				setElementValue("nrTamanho", "");
				setElementValue("nrDecimais", "");
				return;
			}
			if ((selectedType == 'A') || (selectedType == 'V')) {
				setElementValue("tpInformacao", "N");
				setElementValue("nrTamanho", "");
			}
			if (selectedType == 'C') {
				setElementValue("nrDecimais", "");
				setElementValue("nrTamanho", "");
				
			}
			if (selectedType == 'M') {
				setElementValue("tpInformacao", "C");
				setElementValue("nrDecimais", "");
				setElementValue("nrTamanho", "");
				setElementValue("nrDecimais", "");
			}
			if (selectedType == 'S') {
				setElementValue("tpInformacao", "C");
				setElementValue("nrDecimais", "");
				setElementValue("nrTamanho", "1");
			}
		
		}
		
		
		// Função que desabilita o campo tamanho quando o tipo selecionado é Data/Hora
		function onSelectComp() {
			var selectedType = getElementValue("tpComponente");
			if (selectedType == '') {
				//setElementValue("tpInformacao", "");
				setDisabled("tpInformacao",true);
				//setElementValue("nrTamanho", "");
				setDisabled("nrTamanho",true);
				//setElementValue("nrDecimais", "");
				setDisabled("nrDecimais",true);
				return;
			}
			if ((selectedType == 'A') || (selectedType == 'V')) {
				//setElementValue("tpInformacao", "N");
				setDisabled("tpInformacao",true)
				setDisabled("nrTamanho",false);
				setDisabled("nrDecimais",false);
				//setElementValue("nrTamanho", "");
			}
			if (selectedType == 'C') {
				setDisabled("tpInformacao",false);
				setDisabled("nrTamanho",false);
				setDisabled("nrDecimais",false);
				//setElementValue("nrDecimais", "");
				//setElementValue("nrTamanho", "");
				
			}
			if (selectedType == 'M') {
				//setElementValue("tpInformacao", "C");
				setDisabled("tpInformacao",true)
				setDisabled("nrTamanho",false);
				//setElementValue("nrDecimais", "");
				setDisabled("nrDecimais",true);
				//setElementValue("nrTamanho", "");
				//setElementValue("nrDecimais", "");
			}
			if (selectedType == 'S') {
				//setElementValue("tpInformacao", "C");
				//setElementValue("nrDecimais", "");
				setDisabled("tpInformacao",true)
				//setElementValue("nrTamanho", "1");
				setDisabled("nrTamanho",true);
				setDisabled("nrDecimais",true);
			}
		}
		
		function onSelectForm() {
			var selectedType = getElementValue("tpInformacao");
			if (selectedType == '') {
				setDisabled("nrTamanho",true);
				setDisabled("nrDecimais",true);
				setElementValue("nrDecimais", "");				
				setElementValue("nrTamanho", "");				
				return;
			}
			if (selectedType == 'C') {
				setDisabled("nrTamanho",false);
				setElementValue("nrDecimais", "");
				setDisabled("nrDecimais",true);
			}
			if (selectedType == 'N') {
				setDisabled("nrTamanho",false);
				setDisabled("nrDecimais",false);
			}
			if (selectedType == 'D') {
				setElementValue("nrTamanho", "");
				setDisabled("nrTamanho",true);
				setElementValue("nrDecimais", "");
				setDisabled("nrDecimais",true);
			}
		}
		function validateStore() {
		var result = true;
		var tpInformacao = getElementValue("tpInformacao");
		var tpComponente = getElementValue("tpComponente");
		var tamanho = getElementValue("nrTamanho");  
			if (!validateForm(document.getElementById('form_idAtributoMeioTransporte')))
					return false;
			if (tpComponente == 'C')
				if (tpInformacao == ''){
				alert('<adsm:label key="LMS-26015"/>');
					setFocus(document.getElementById("tpInformacao"));
					return false;
					}
				
			if (tpInformacao != 'D')
				if (tamanho <= 0){
					alert('<adsm:label key="LMS-26002"/>');
					result = false;
					setFocus(document.getElementById("nrTamanho"));
					}
		    if (result == true){
		         storeButtonScript('lms.contratacaoveiculos.atributoMeioTransporteService.store', "myStoreButton", document.getElementById('form_idAtributoMeioTransporte'));
				 
				return result;
			}
			return false;
		}
		
		
		function initWindow(eventObj) {
		if ((eventObj.name == "newButton_click") || (eventObj.name == "tab_click")){
			onSelectComp();
			resetAfterSelectComp();
			}
		}
		
		function myOnDataLoad_cb(dados,erros){
			onDataLoad_cb(dados,erros);
		
			var idAtributoMeioTransporte = getNestedBeanPropertyValue(dados, "idAtributoMeioTransporte");
	
			if (idAtributoMeioTransporte != '')
				setElementValue(document.getElementById("cdAtributoMeioTransporte"), idAtributoMeioTransporte);
		
		
		onSelectComp();
		}
		
		</script>
	</adsm:form>
</adsm:window>   