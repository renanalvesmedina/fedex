<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	/**
	 * Seta id da pessoa após inserir um registro.
	 * Desabilita tipo e número de identificação após inserir um registro.
	 */
	function aeroportoLoad_cb(data, exception){

		onDataLoad_cb(data, exception);
		
		onDataLoadCallbackPessoaWidget({tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"), 
										numberElement:document.getElementById("pessoa.nrIdentificacao")});
		
		var idPessoa = getNestedBeanPropertyValue(data, "idAeroporto");
		if (idPessoa != undefined){
			setElementValue("pessoa.idPessoa", idPessoa);
			setDisabled("pessoa.tpIdentificacao", true ); 
			setDisabled("pessoa.nrIdentificacao", true );
		}
		setFocusOnFirstFocusableField();
	} 
	
	function pageLoadCustom_cb(data,error) {
		onPageLoad_cb(data,error);
		
		changeTypePessoaWidget(
				{tpTipoElement:document.getElementById("pessoa.tpPessoa"),
				 tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'),
				 numberElement:document.getElementById('pessoa.nrIdentificacao'),tabCmd:'cad'});
	}
	
	function storeCallBack_cb(data, exception) {
		store_cb(data, exception);
		var idPessoa = getNestedBeanPropertyValue(data, "idAeroporto");
		if (idPessoa != undefined){
			setElementValue("pessoa.idPessoa", idPessoa);
		}
		setFocusOnNewButton();
	} 
	 
	function pageLoadAeroporto() {
   		onPageLoad();
 		initPessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa")
   				   ,tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
      			   ,numberElement:document.getElementById("pessoa.idPessoa")});
	}
</script>
<adsm:window service="lms.municipios.manterAeroportosAction" onPageLoadCallBack="pageLoadCustom" onPageLoad="pageLoadAeroporto">
	<adsm:form action="/municipios/manterAeroportos" idProperty="idAeroporto" service="lms.municipios.manterAeroportosAction.findByIdDetalhado" onDataLoadCallBack="aeroportoLoad">
		<adsm:hidden property="pessoa.tpPessoa" value="J"/>
		<adsm:hidden property="labelPessoa" serializable="false" value="Aeroporto"/>
		<adsm:hidden property="siglaDescricao" serializable="false"/>
		
		<adsm:complement label="identificacao" labelWidth="15%" width="35%"> 
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad" required="false" onchange="return changeTpIdentificacao(this);" />
			<adsm:lookup definition="IDENTIFICACAO_PESSOA" 
					service="lms.municipios.manterAeroportosAction.validateIdentificacao"
					onDataLoadCallBack="pessoaCallback"  required="false"
					onchange="return changeNrIdentificacao(this);"/>
		</adsm:complement> 
		
		<adsm:textbox dataType="text" property="pessoa.nmPessoa" label="nome" size="35" maxLength="50" required="true"/>
		<adsm:hidden property="pessoa.dsEmail" serializable="true" />
		<adsm:hidden property="pessoa.nmFantasia" serializable="true"/>

		<adsm:textbox dataType="text" property="sgAeroporto" onchange="validaSigla()" label="sigla" size="3" maxLength="3" required="true"/>
		<adsm:textbox dataType="text" property="cdCidade" label="codigoCidade" size="3" maxLength="3" required="true"/>


		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="true"/>
		<adsm:hidden property="tpAcesso" value="A" serializable="true"/>
		<adsm:lookup service="lms.municipios.manterAeroportosAction.findLookupFilial" dataType="text" 
				property="filial" idProperty="idFilial" criteriaProperty="sgFilial"
				label="filialResponsavel" size="3" maxLength="3" action="/municipios/manterFiliais" width="35%"
				exactMatch="true" cellStyle="vertical-align:bottom" >
			
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"  />
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"  />
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"  />
			
         	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
         	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" 
         			size="30" maxLength="50" disabled="true" serializable="false" />
	    </adsm:lookup>

		
		<adsm:checkbox label="taxaTerrestreObrigatoria" property="blTaxaTerrestreObrigatoria" cellStyle="vertical-align:bottom" />

		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" required="true"/>

		<adsm:buttonBar>
			<adsm:button caption="ciasAereas" action="municipios/manterFiliaisCiaAerea" cmd="main" boxWidth="83">
				<adsm:linkProperty src="idAeroporto" target="aeroporto.idAeroporto" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="aeroporto.pessoa.nmPessoa" />
				<adsm:linkProperty src="sgAeroporto" target="aeroporto.sgAeroporto" />
			</adsm:button>
			<adsm:button caption="enderecos" action="configuracoes/manterEnderecoPessoa" cmd="main" boxWidth="70" >
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp" disabled="true"/>
				<adsm:linkProperty src="pessoa.idPessoa" target="pessoa.idPessoa" disabled="true"/>
				<adsm:linkProperty src="pessoa.tpIdentificacao" target="pessoa.tpIdentificacao" disabled="true"/>
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" disabled="true"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" disabled="true"/>
			</adsm:button>
			<adsm:storeButton callbackProperty="storeCallBack"/>
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	document.getElementById("pessoa.nrIdentificacao").serializable = true;

	/**
	 * Ao detalhar um registro deve desabilitar tipo e número de identificação.
	 */
	function initWindow(eventObj) {
		var blDetalhamento = (eventObj.name == "gridRow_click" || eventObj.name == "storeButton");
		setDisabled(document.getElementById("pessoa.tpIdentificacao"),blDetalhamento);
		setDisabled(document.getElementById("pessoa.nrIdentificacao"),true);
		setFocusOnFirstFocusableField();
	}
	
	function pessoaCallback_cb(data,error) {
		if (error != undefined) {
			alert(error);
			resetValue("pessoa.nrIdentificacao");
			setFocus(document.getElementById('pessoa.nrIdentificacao'));	
			return false;
		}
		if (data != undefined && data.length == 1){
			setElementValue("pessoa.nmFantasia",data[0].nmFantasia);
			setElementValue("pessoa.dsEmail",data[0].dsEmail);
		}	
		return pessoa_nrIdentificacao_exactMatch_cb(data);
	}	
	
	function validaSigla(){
		var sigla = document.getElementById("sgAeroporto")
		sigla.value = sigla.value.toUpperCase();
	}
	
	function changeTpIdentificacao(elem) {
		if (elem.value == "") {
			resetValue("pessoa.nmFantasia");
			resetValue("pessoa.dsEmail");
		}
		return changeIdentificationTypePessoaWidget({tpIdentificacaoElement:elem, numberElement:document.getElementById('pessoa.idPessoa'), tabCmd:'cad'});
	}
	
	function changeNrIdentificacao(elem) {
		if (elem.value == "") {
			resetValue("pessoa.nmFantasia");
			resetValue("pessoa.dsEmail");
		}
		return pessoa_nrIdentificacaoOnChangeHandler();
	}

</script>