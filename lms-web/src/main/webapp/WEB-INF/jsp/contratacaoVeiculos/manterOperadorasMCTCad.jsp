<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	/**
	 * Seta id da pessoa após inserir um registro.
	 * Desabilita tipo e número de identificação após inserir um registro.
	 */
	function operadoraMctLoad_cb(data, exception){
		onDataLoad_cb(data, exception);
		var idPessoa = getNestedBeanPropertyValue(data, "idOperadoraMct");
		
		document.getElementById("pessoa.nrIdentificacao").serializable = "true";
		
		if (idPessoa != undefined){
			setElementValue("pessoa.idPessoa", idPessoa);
			setDisabled("pessoa.tpIdentificacao", true );
			setDisabled("pessoa.nrIdentificacao", true );
			setFocusOnFirstFocusableField(document);			
		}
	}
	
	function operadoraMCT_pageLoad_cb(data){
		onPageLoad_cb(data);
		setElementValue(document.getElementById("labelPessoa"),operadoraMCT);
		changeTypePessoaWidget(
				{tpTipoElement:document.getElementById("pessoa.tpPessoa"),
				 tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'),
				 numberElement:document.getElementById('pessoa.nrIdentificacao'),tabCmd:'cad'});
	}
	
	function pageLoadOperadora() {
   		onPageLoad();
 		initPessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa")
   				   ,tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
      			   ,numberElement:document.getElementById("pessoa.idPessoa")});
	}
	 
	
</script>
<adsm:window service="lms.contratacaoveiculos.operadoraMctService" onPageLoadCallBack="operadoraMCT_pageLoad" onPageLoad="pageLoadOperadora">
	<adsm:form action="contratacaoVeiculos/manterOperadorasMCT" idProperty="idOperadoraMct" onDataLoadCallBack="operadoraMctLoad" >
		<adsm:hidden property="pessoa.tpPessoa" value="J" />
		<adsm:hidden property="labelPessoa" />
		
		<adsm:complement label="identificacao" required="true">
            <adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad" required="false"/>
            <adsm:lookup definition="IDENTIFICACAO_PESSOA"
            		service="lms.contratacaoveiculos.operadoraMctService.validateIdentificacao"
					onDataLoadCallBack="pessoaCallback" required="false"/>
		</adsm:complement>
		<adsm:textbox dataType="text" property="pessoa.nmPessoa" label="razaoSocial" maxLength="50"
				size="50" labelWidth="12%" width="38%" required="true" depends="pessoa.nrIdentificacao" />
	
		<adsm:textbox dataType="text" property="dsHomepage" label="homepage" maxLength="120" size="41" />
		<adsm:textbox dataType="email" property="pessoa.dsEmail" labelWidth="12%" label="email" maxLength="60" size="35" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" required="true" />
		
		<adsm:buttonBar>
			<adsm:button caption="inscricoesEstaduais" id="buttonInscricaoEstadual" action="/configuracoes/manterInscricoesEstaduais" cmd="main">
				<adsm:linkProperty src="idOperadoraMct" target="pessoa.idPessoa"/>			
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>						
			</adsm:button>
			<adsm:button caption="enderecos" onclick="parent.parent.redirectPage('configuracoes/manterEnderecoPessoa.do?cmd=main' + buildLinkPropertiesQueryString_enderecos());"/>
			<adsm:button caption="contatos" onclick="parent.parent.redirectPage('configuracoes/manterContatos.do?cmd=main' + buildLinkPropertiesQueryString_contatos());"/>
			<adsm:storeButton  callbackProperty="customStore"/>
			<adsm:newButton />
			<adsm:removeButton service="lms.contratacaoveiculos.operadoraMctService.removeOperadoraMctById" />
		</adsm:buttonBar>
		<script>
			var operadoraMCT = '<adsm:label key="operadoraMCT"/>';
		</script>
	</adsm:form>
</adsm:window>

<script>
	
	document.getElementById("labelPessoa").masterLink = "true";
	
	/**
	 * Ao detalhar um registro deve desabilitar tipo e número de identificação.
	 */
	function initWindow(eventObj) {
		if (eventObj.name == "tab_click" || eventObj.name == "newButton_click" || eventObj.name == "removeButton") {
			setDisabled(document.getElementById("pessoa.tpIdentificacao"), false); 
			setFocusOnFirstFocusableField(document);
				
		} else if (eventObj.name == "gridRow_click" || eventObj.name == "storeButton") {
			setDisabled(document.getElementById("pessoa.tpIdentificacao"), true); 
			setDisabled(document.getElementById("pessoa.nrIdentificacao"), true); 			
			if (eventObj.name == "storeButton")
				setFocusOnNewButton(document);
			else 
				setFocusOnFirstFocusableField(document);
		} 

	}

	function pessoaCallback_cb(data,error) {
		if (error != undefined) {
			alert(error);
			resetValue("pessoa.nrIdentificacao");
			setFocus(document.getElementById('pessoa.nrIdentificacao'));
			return false;
		}
		return pessoa_nrIdentificacao_exactMatch_cb(data);
	}
 
	function buildLinkPropertiesQueryString_enderecos() {
	
		var qs = "";
		if (document.getElementById("pessoa.idPessoa").type == 'checkbox') {
			qs += "&pessoa.idPessoa=" + document.getElementById("pessoa.idPessoa").checked;
		} else {
			qs += "&pessoa.idPessoa=" + document.getElementById("pessoa.idPessoa").value;
		}
		if (document.getElementById("pessoa.nrIdentificacao").type == 'checkbox') {
			qs += "&pessoa.nrIdentificacao=" + document.getElementById("pessoa.nrIdentificacao").checked;
		} else {
			qs += "&pessoa.nrIdentificacao=" + document.getElementById("pessoa.nrIdentificacao").value;
		}
		if (document.getElementById("pessoa.nmPessoa").type == 'checkbox') {
			qs += "&pessoa.nmPessoa=" + document.getElementById("pessoa.nmPessoa").checked;
		} else {
			qs += "&pessoa.nmPessoa=" + document.getElementById("pessoa.nmPessoa").value;
		}
		qs+= "&labelPessoaTemp=" + operadoraMCT;
		return qs; 
	}

	
	// ButtonTag.linkProperties support function for dynamic queryString build
	function buildLinkPropertiesQueryString_contatos() {
	
		var qs = "";
		if (document.getElementById("pessoa.idPessoa").type == 'checkbox') {
			qs += "&pessoa.idPessoa=" + document.getElementById("pessoa.idPessoa").checked;
		} else {
			qs += "&pessoa.idPessoa=" + document.getElementById("pessoa.idPessoa").value;
		}
		if (document.getElementById("pessoa.nrIdentificacao").type == 'checkbox') {
			qs += "&pessoa.nrIdentificacao=" + document.getElementById("pessoa.nrIdentificacao").checked;
		} else {
			qs += "&pessoa.nrIdentificacao=" + document.getElementById("pessoa.nrIdentificacao").value;
		}
		if (document.getElementById("pessoa.nmPessoa").type == 'checkbox') {
			qs += "&pessoa.nmPessoa=" + document.getElementById("pessoa.nmPessoa").checked;
		} else {
			qs += "&pessoa.nmPessoa=" + document.getElementById("pessoa.nmPessoa").value;
		}
		qs+= "&labelPessoaTemp=" + operadoraMCT;
		return qs;
	}
	
	function customStore_cb(data,exception) {
		if (exception != undefined) {
			alert(exception);
			return false; 
		}
		
		store_cb(data,exception);
						
		if (data != undefined) {
			setElementValue('idOperadoraMct',getNestedBeanPropertyValue(data,'idOperadoraMct'));
			setElementValue('pessoa.idPessoa',getNestedBeanPropertyValue(data,'idOperadoraMct'));
		}
		
	}
</script>