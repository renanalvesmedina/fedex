<%-- @ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="org.apache.commons.beanutils.*" --%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>		
		  
<adsm:window service="lms.vol.manterContatosVolAction" onPageLoadCallBack="myPageLoad" >
	<adsm:form action="/vol/manterContatosVol">
		<adsm:hidden property="tpAcessoFilial" serializable="false" value="F"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="true"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>
		
		<adsm:lookup label="filialResponsavel" width="85%" labelWidth="15%" 
				     property="filial"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.vol.manterContatosVolAction.findLookupFilialByUsuarioLogado" 
		             dataType="text"
		             size="3" 
		             maxLength="3" > 
            <adsm:propertyMapping criteriaProperty="tpAcessoFilial" modelProperty="tpAcesso" /> 
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
        	<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
            <adsm:textbox dataType="text" 
            			  property="filial.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="50" 
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>
       
        <adsm:hidden property="cnpj" serializable="true"/>
        <adsm:hidden property="pessoa.tpPessoa" value="J" /> 
        <adsm:hidden property="pessoa.tpIdentificacao" serializable="false" />
       	<adsm:lookup property="pessoa" dataType="text" 
					 idProperty="idPessoa" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="nrIdentificacaoFormatado"
					 action="/configuracoes/manterPessoas" 
					 service="lms.vol.manterContatosVolAction.findLookupEmpresa" 
					 label="empresa" size="18" maxLength="20" labelWidth="15%" width="45%"
					 serializable="true"
					 afterPopupSetValue="habilitaCheckBox"
					 onDataLoadCallBack="habilitaCheckBox">
			<adsm:propertyMapping modelProperty="nmPessoa" relatedProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="tpIdentificacao.value" relatedProperty="pessoa.tpIdentificacao" />	
			<adsm:propertyMapping modelProperty="pessoa.tpPessoa" criteriaProperty="pessoa.tpPessoa" />
			<adsm:propertyMapping modelProperty="nmPessoa" criteriaProperty="pessoa.nmPessoa" disable="false"/>	
			<adsm:propertyMapping modelProperty="pessoa.tpIdentificacao" criteriaProperty="pessoa.tpIdentificacao" disable="false"/>	
			<adsm:propertyMapping modelProperty="pessoa.nrIdentificacao" criteriaProperty="pessoa.pessoa.nrIdentificacao" disable="false"/>
			<adsm:textbox property="pessoa.nmPessoa" dataType="text" size="30" disabled="true" serializable="false" />
		</adsm:lookup>
		
		<adsm:checkbox property="cnpjParcial" label="cnpjParcial" labelWidth="10%" width="10%" disabled="true"/>		
		<adsm:textbox property="nmContato" label="contato" dataType="text" maxLength="60" size="60"  width="400"/>	
		<adsm:textbox property="dsEmail" label="emailContato" dataType="text" maxLength="60" size="60"  width="450"/>
		<adsm:combobox property="ativo" labelWidth="10%" width="200" label="status" domain="DM_SIM_NAO" style="width:150px;"/>
			
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="contatosVol"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>	
	
	<adsm:grid property="contatosVol" idProperty="idContato" rows="10" unique="true" gridHeight="200"
	    selectionMode="check" service="lms.vol.manterContatosVolAction.findPaginatedContatosVol" 
	    rowCountService="lms.vol.manterContatosVolAction.getRowCountContatosVol" scrollBars="horizontal" >
		<adsm:gridColumn property="nmContato" title="contato" width="150" />
	    <adsm:gridColumn property="dsEmail" title="email" width="150" />
	    <adsm:gridColumn property="nrIdentificacao" title="empresa" width="120" align="right" mask=""/>
 		<adsm:gridColumn property="nmPessoa" title="" width="200" />
		<adsm:gridColumn property="dsDepartamento" title="departamento" width="100" /> 
		<adsm:gridColumn property="dsFuncao" title="funcao" width="80" />
		<adsm:gridColumn property="obContato" title="observacao" width="250" />
		<adsm:gridColumn title="ativo" property="blAtivo" renderMode="image-check" width="40" />
		
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:grid>
</adsm:window>	

<script type="text/javascript">

	document.getElementById("pessoa.pessoa.nrIdentificacao").onblur = desabilitaCheckBox;
	
	function habilitaCheckBox_cb (data,error){
		if (error != undefined) {
			  alert(error);
			  return false;
		 }
		 
		var r = pessoa_pessoa_nrIdentificacao_exactMatch_cb(data) 
		 
		if ( ( r == true ) &&  (data[0].tpIdentificacao.value == "CNPJ") ) {
			  setElementValue("cnpj", data[0].nrIdentificacao);
			  setFocus(document.getElementById("cnpjParcial"));
			  document.getElementById("cnpjParcial").disabled=false;
		} else 
			if ( r == true ) {
				setElementValue("pessoa.pessoa.nrIdentificacao", getFormattedValue(data[0].tpIdentificacao.value, data[0].nrIdentificacao, "", true));
				setElementValue("pessoa.nmPessoa", data[0].nmPessoa);
				setElementValue("pessoa.idPessoa", data[0].idPessoa);
				document.getElementById("cnpjParcial").disabled=true;
				document.getElementById("cnpjParcial").checked=null;
		} 	
	}


	function habilitaCheckBox (data,error){
		if ( (data.nrIdentificacao != "") && (data.tpIdentificacao.value == "CNPJ") ) {
			setElementValue("cnpj", data.nrIdentificacao);
			document.getElementById("cnpjParcial").disabled=false;
		}
		
	}
	
	function desabilitaCheckBox(){
		if(document.getElementById("pessoa.pessoa.nrIdentificacao").value == ""){ 
				document.getElementById("cnpjParcial").disabled=true;
				document.getElementById("cnpjParcial").checked=null;
		}
	}

	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click" || eventObj.name == "tab_click" || eventObj.name == "newButton_click") {
			fillDataUsuario();
			filial_sgFilialOnChangeHandler();
			setFocus(document.getElementById("filial.sgFilial"));			
		}
	}

	function myPageLoad_cb(data,error) { 
	    	var data = new Array();
			var sdo = createServiceDataObject("lms.vol.manterContatosVolAction.findDataSession","loadFilialUsuario",data);
    		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Retorna o parametro 'mode' que contem o modo em que a tela esta sendo utilizada.
	 * Caso mode seja igual a 'lookup' significa que a tela esta sendo aberta por uma lookup.
	 */
	function isLookup() {
		var url = new URL(parent.location.href);
		var mode = url.parameters["mode"];
		if ((mode!=undefined) && (mode=="lookup")) return true;
		return false;
	}
	
		/**
	 * Carrega os dados de filial do usuario logado
	 */
	var dataUsuario;
	function loadFilialUsuario_cb(data, error) {
		dataUsuario = data;
		fillDataUsuario();
		onPageLoad_cb(data,error);
	}
	
		/**
	 * Faz o callBack do carregamento da pagina
	 */
	function loadPage_cb(data, error) {
		setDisabled("filial.idFilial", false);
		document.getElementById("filial.sgFilial").disabled=false;
		document.getElementById("filial.sgFilial").focus;
	}
	
	/**
	* Preenche os campos relacionados com o usuario.
	*/
	function fillDataUsuario() {
		if(dataUsuario){
			setElementValue("filial.idFilial", dataUsuario.filial.idFilial);
			setElementValue("filial.sgFilial", dataUsuario.filial.sgFilial);
			setElementValue("filial.pessoa.nmFantasia", dataUsuario.filial.pessoa.nmFantasia);
		}
	}
	
</script>
	