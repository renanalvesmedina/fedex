<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	/**
	 * Seta id da pessoa após inserir um registro.
	 * Desabilita tipo e número de identificação após inserir um registro.
	 */
	function concessionariaLoad_cb(data, exception){
		onDataLoad_cb(data, exception);
		
		onDataLoadCallbackPessoaWidget({tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"), 
							numberElement:document.getElementById("pessoa.nrIdentificacao")});
		
		var idPessoa = getNestedBeanPropertyValue(data, "idConcessionaria");		
		if (idPessoa != undefined){
			setElementValue("pessoa.idPessoa", idPessoa);
			setDisabled("pessoa.tpIdentificacao", true );
			setDisabled("pessoa.nrIdentificacao", true );
		}
		setFocus(document.getElementById("pessoa.nmPessoa"));		
	}
	
	function pageLoadConcessionaria() {
   		onPageLoad();
 		initPessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa")
   				   ,tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
      			   ,numberElement:document.getElementById("pessoa.idPessoa")});
	}
</script>
<adsm:window service="lms.municipios.manterConcessionariaTaxasPassagemAction" onPageLoadCallBack="concessionariasPageLoad" onPageLoad="pageLoadConcessionaria">
	<adsm:form idProperty="idConcessionaria" action="/municipios/manterConcessionariaTaxasPassagem" onDataLoadCallBack="concessionariaLoad" >
		<adsm:hidden property="pessoa.tpPessoa" value="J" />
		<adsm:hidden property="siglaDescricao" serializable="false" />
		<adsm:hidden property="labelPessoa"/>

        <adsm:complement label="identificacao" labelWidth="18%" width="32%" required="true" >
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad" />
			<adsm:lookup definition="IDENTIFICACAO_PESSOA" 
					service="lms.municipios.concessionariaService.validateIdentificacao"
					onDataLoadCallBack="pessoaCallback" />
		</adsm:complement>
		<adsm:textbox dataType="text" property="pessoa.nmPessoa" 
				label="razaoSocial" maxLength="50" size="35" 
				required="true" labelWidth="18%" width="32%" depends="pessoa.nrIdentificacao"/>
								
		<%--adsm:textbox dataType="text" property="inscricaoEstadual.nrInscricaoEstadual" label="inscricaoEstadual"
				maxLength="20" size="25" labelWidth="18%" width="32%" /--%>
				
		<%--adsm:lookup property="inscricaoEstadual.unidadeFederativa"  idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
					 service="lms.municipios.unidadeFederativaService.findLookup" dataType="text" onPopupSetValue="uf_Popup" required="true"
					 labelWidth="18%" width="32%" label="uf" size="2" maxLength="2" onchange="return uf_onChange(this)" onDataLoadCallBack="uf_DataLoad" 
					 action="/municipios/manterUnidadesFederativas" minLengthForAutoPopUpSearch="2" exactMatch="false">
			<adsm:propertyMapping relatedProperty="inscricaoEstadual.unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:propertyMapping criteriaProperty="inscricaoEstadual.unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" inlineQuery="false" />
			<adsm:textbox dataType="text" property="inscricaoEstadual.unidadeFederativa.nmUnidadeFederativa" size="19" serializable="false" disabled="true" />
		</adsm:lookup>	
		
		<adsm:textbox dataType="text" property="inscricaoEstadual.nrInscricaoEstadual" label="inscricaoEstadual" size="30" maxLength="20" labelWidth="18%" width="32%" onchange="return verificaInscricaoEstadual(this);" disabled="true"/>
				

		<adsm:hidden property="inscricaoEstadual.idInscricaoEstadual" /--%>
		<adsm:textbox dataType="text" property="dsHomePage" label="homepage" maxLength="120" size="41" labelWidth="18%" width="32%" />
		<adsm:textbox dataType="email" property="pessoa.dsEmail" label="email" maxLength="60" size="35" labelWidth="18%" width="32%" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="18%" width="82%" required="true" />
	<adsm:buttonBar>
	    <adsm:button caption="inscricoesEstaduais" id="buttonInscricaoEstadual" action="/configuracoes/manterInscricoesEstaduais" cmd="main">
				<adsm:linkProperty src="pessoa.idPessoa" target="pessoa.idPessoa"/>			
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>						
		</adsm:button>
		<adsm:button caption="postosPassagem" action="municipios/manterPostosPassagem" cmd="main">
			<adsm:linkProperty src="idConcessionaria" target="concessionaria.idConcessionaria" />
			<adsm:linkProperty src="pessoa.nrIdentificacao" target="concessionaria.pessoa.nrIdentificacao" />
			<adsm:linkProperty src="pessoa.nmPessoa" target="concessionaria.pessoa.nmPessoa" />
		</adsm:button>
		<adsm:button caption="enderecos" onclick="parent.parent.redirectPage('configuracoes/manterEnderecoPessoa.do?cmd=main' + buildLinkPropertiesQueryString_enderecos());"/>
		<adsm:button caption="contatos" onclick="parent.parent.redirectPage('configuracoes/manterContatos.do?cmd=main' + buildLinkPropertiesQueryString_contatos());"/>
		<adsm:storeButton callbackProperty="customStore"/>
		<adsm:newButton/>
		<adsm:removeButton/>
	</adsm:buttonBar>
	<script language="javascript" type="text/javascript">
		var concessionaria = '<adsm:label key="concessionaria"/>';
		var labelPessoaTemp = '<adsm:label key="empresa"/>';
	</script>
	</adsm:form>
</adsm:window>

<script language="javascript" type="text/javascript">
	document.getElementById("labelPessoa").masterLink = "true";
	document.getElementById("pessoa.nrIdentificacao").serializable = true;

	function concessionariasPageLoad_cb(data,error){
		onPageLoad_cb(data, error);
		setElementValue(document.getElementById("labelPessoa"),labelPessoaTemp);

		changeTypePessoaWidget(
				{tpTipoElement:document.getElementById("pessoa.tpPessoa"),
				 tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'),
				 numberElement:document.getElementById('pessoa.nrIdentificacao'),tabCmd:'cad'});
	}

	function buildLinkPropertiesQueryString_enderecos() {
		var qs = "";
		if (document.getElementById("idConcessionaria").type == 'checkbox') {
			qs += "&pessoa.idPessoa=" + document.getElementById("idConcessionaria").checked;
		} else {
			qs += "&pessoa.idPessoa=" + document.getElementById("idConcessionaria").value;
		}
		if (document.getElementById("pessoa.tpIdentificacao").type == 'checkbox') {
			qs += "&pessoa.tpIdentificacao=" + document.getElementById("pessoa.tpIdentificacao").checked;
		} else {
			qs += "&pessoa.tpIdentificacao=" + document.getElementById("pessoa.tpIdentificacao").value;
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
		qs+= "&labelPessoaTemp=" + concessionaria;
		return qs;
	}

	function buildLinkPropertiesQueryString_contatos() {
		var qs = "";
		if (document.getElementById("idConcessionaria").type == 'checkbox') {
			qs += "&pessoa.idPessoa=" + document.getElementById("idConcessionaria").checked;
		} else {
			qs += "&pessoa.idPessoa=" + document.getElementById("idConcessionaria").value;
		}
		if (document.getElementById("pessoa.tpIdentificacao").type == 'checkbox') {
			qs += "&pessoa.tpIdentificacao=" + document.getElementById("pessoa.tpIdentificacao").checked;
		} else {
			qs += "&pessoa.tpIdentificacao=" + document.getElementById("pessoa.tpIdentificacao").value;
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
		qs+= "&labelPessoaTemp=" + concessionaria;
		return qs;
	}

	// #####################################################################
	// funções referentes ao comportamento de inscrição estadual
	// #####################################################################
	function verificaInscricaoEstadual(e) {
		if (e.value != "") {

			var sgUf = document.getElementById("inscricaoEstadual.unidadeFederativa.sgUnidadeFederativa").value;
			var nrIns = document.getElementById("inscricaoEstadual.nrInscricaoEstadual").value;
			return validateInscricaoEstadual(sgUf,nrIns);
		}		
		return true;
	}

	function uf_Popup(data) {		
		setDisabled("inscricaoEstadual.nrInscricaoEstadual", false);
		return true;
	}

	function uf_DataLoad_cb(data, error) {
		inscricaoEstadual_unidadeFederativa_sgUnidadeFederativa_exactMatch_cb(data, error);
	}

	function uf_onChange(obj) {
		document.getElementById("inscricaoEstadual.nrInscricaoEstadual").value = "";
		
		if (obj.value != '') {
			setDisabled("inscricaoEstadual.nrInscricaoEstadual", false);			
		} else {
			setDisabled("inscricaoEstadual.nrInscricaoEstadual", true);
		}
		return inscricaoEstadual_unidadeFederativa_sgUnidadeFederativaOnChangeHandler();
	}

	/**
	 * Ao detalhar um registro deve desabilitar tipo e número de identificação.
	 */
	function initWindow(eventObj) {
		if (eventObj.name == "tab_click" || eventObj.name == "newButton_click" || eventObj.name == "removeButton") {
			setDisabled(document.getElementById("pessoa.tpIdentificacao"),false);
			setDisabled(document.getElementById("pessoa.nrIdentificacao"),true);
			//setDisabled(document.getElementById("inscricaoEstadual.nrInscricaoEstadual"), true);
			setFocusOnFirstFocusableField(document);
		} else if (eventObj.name == "gridRow_click") {
			//setDisabled(document.getElementById("inscricaoEstadual.nrInscricaoEstadual"), false); 
			setDisabled(document.getElementById("pessoa.tpIdentificacao"),true);
		}
	}

	function customStore_cb(data,exception) {
		if (data != undefined && ((document.getElementById('idConcessionaria').value) !=
								  (getNestedBeanPropertyValue(data,':idConcessionaria'))))
			document.getElementById('idConcessionaria').value =
					getNestedBeanPropertyValue(data,':idConcessionaria');

		var nrIdentificacaoFormatado = getNestedBeanPropertyValue(data, "pessoa.nrIdentificacaoFormatado");

		if (nrIdentificacaoFormatado != '')
			setElementValue(document.getElementById("pessoa.nrIdentificacao"), nrIdentificacaoFormatado);

		var idPessoa = getNestedBeanPropertyValue(data, "idConcessionaria");		
		if (idPessoa != undefined){
			setElementValue("pessoa.idPessoa", idPessoa);
			setDisabled("pessoa.tpIdentificacao", true );
			setDisabled("pessoa.nrIdentificacao", true );
		}		

		store_cb(data, exception);
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
</script>