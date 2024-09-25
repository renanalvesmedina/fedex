<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	onPageLoad="myOnPageLoad">
	<adsm:form
		action="/expedicao/manterCRTAduanas">
		<adsm:section
			caption="aduanas18"
			width="46"/>

		<adsm:label
			key="branco"
			style="border:none;"
			width="1%" />

		<adsm:listbox
			label="aduana"
			property="aduanasCtoInternacional"
			optionProperty="idPontoParada"
			service="lms.expedicao.manterCRTAduanasAction.findPontosParadaCombo"
			size="5"
			onContentChange="notifyModified"
			labelWidth="8%"
			boxWidth="300">
			<adsm:lookup
				property="aduana"
				idProperty="idAduanaCtoInternacional"
				criteriaProperty="nmPontoParada"
				action="/municipios/manterPontosParadaRota"
				cmd="list"
				service="lms.expedicao.manterCRTAduanasAction.findComboPontoParada"
				dataType="text"
				serializable="false"
				size="35"
				maxLength="60">

				<adsm:propertyMapping
					criteriaProperty="blAduana"
					modelProperty="blAduana"/>
			</adsm:lookup>
		</adsm:listbox>

		<adsm:hidden
			property="blAduana"
			value="S"/>

		<adsm:buttonBar
			freeLayout="true">
			<adsm:button
				caption="salvar"
				onclick="salvar()"
				id="btnSalvar"/>
			<adsm:button
				caption="fechar"
				onclick="self.close()"
				id="btnFechar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	function salvar(){
		var objList = getElement('aduanasCtoInternacional');
		var objToSubmit = [];

		for(var i = 0; i < objList.length; i++){
			var obj = {
				idPontoParada : objList[i].value
				,dsPontoParada : objList[i].text
			};
			objToSubmit.push(obj);
		}

		var data = {
			aduanasCtoInternacional : objToSubmit
		}

		var service = 'lms.expedicao.manterCRTAduanasAction.storeInSession';
		var sdo = createServiceDataObject(service, 'storeSession', data);
		xmit({serviceDataObjects:[sdo]});
	}
	/************************************************************\
	*
	\************************************************************/
	function storeSession_cb(data,erros){
		if (erros != undefined){
			alert(erros);
			return false;
		}
		notifyModified();
		self.close();
	}
	/************************************************************\
	*
	\************************************************************/
	function myOnPageLoad(){
		onPageLoad();
		var modo = dialogArguments.MODO_TELA;

		if(modo == 'VISUALIZACAO'){
			setDisabled(document, true);
		} else {
			setDisabled('btnSalvar', false);
		}
		setDisabled('btnFechar', false);
		setFocusOnFirstFocusableField();
    }
    /************************************************************\
	*
	\************************************************************/
    function notifyModified(){
    	window.returnValue = "MODIFICOU_DADOS";
    }
</script>