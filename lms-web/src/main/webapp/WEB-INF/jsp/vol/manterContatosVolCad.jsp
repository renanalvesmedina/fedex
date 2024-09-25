<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>		
		  
<adsm:window service="lms.vol.manterContatosVolAction" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vol/manterContatosVol" idProperty="idContato">
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
		             maxLength="3" required="true">
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
        
        <adsm:hidden property="pessoa.tpPessoa" value="J" /> 
       	<adsm:lookup property="pessoa" dataType="text" 
					 idProperty="idPessoa" 
					 criteriaProperty="nrIdentificacao"  
					 relatedCriteriaProperty="nrIdentificacaoFormatado"
					 action="/configuracoes/manterPessoas" 
					 service="lms.vol.manterContatosVolAction.findLookupEmpresa" 
					 label="empresa" size="18" maxLength="20" labelWidth="15%" width="85%"
					 serializable="true"
					 required="true" >
			<adsm:propertyMapping modelProperty="nmPessoa" relatedProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="pessoa.tpPessoa" criteriaProperty="pessoa.tpPessoa" />
			<adsm:propertyMapping modelProperty="nmPessoa" criteriaProperty="pessoa.nmPessoa" disable="false"/>	
			<adsm:propertyMapping modelProperty="pessoa.nrIdentificacao" criteriaProperty="pessoa.nrIdentificacao" disable="false"/>

			<adsm:textbox property="pessoa.nmPessoa" dataType="text" size="30" disabled="true" serializable="false" />
		</adsm:lookup>
		 
		<adsm:textbox property="nmContato" label="contato" dataType="text" maxLength="60" size="47" required="true" width="350" />	
		<adsm:checkbox property="blAtivo" label="ativo" width="100"/>			
		<adsm:textbox property="dsDepartamento" label="departamento" dataType="text" maxLength="60" size="40" width="350"/>							
		<adsm:textbox property="dsFuncao" label="funcao" dataType="text" maxLength="60" size="25" width="300" labelWidth="80"/>	
		<adsm:textbox property="dsEmail"  label="email" dataType="email" maxLength="100" size="35" required="true" width="300" />							
		<adsm:textarea property="obContato" label="observacao" maxLength="500" columns="80" rows="5" width="500"/>			
						
		<adsm:buttonBar> 
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
	
		
</adsm:window>

<script type="text/javascript">
	
	document.getElementById("pessoa.nrIdentificacao").onblur = onblurNrIdentificacao;
	document.getElementById("nmContato").onblur = onblurNmContato;
	//document.getElementById("dsEmail").onblur = validaEmailContato;
	
	function onblurNrIdentificacao( data, error ){
		if( (document.getElementById("nmContato").value != "") && ( document.getElementById("pessoa.idpessoa").value != "")  ) {
			document.getElementById("pessoa.nrIdentificacao").onblur = null;
			document.getElementById("nmContato").onblur = null;
			validaNomeContato(data, error); 
		}
	}
	
	function onblurNmContato( data, error ){
		if( (document.getElementById("nmContato").value != "") && ( document.getElementById("pessoa.idpessoa").value != "")  ) {
			document.getElementById("pessoa.nrIdentificacao").onblur = null;
			document.getElementById("nmContato").onblur = null;
			validaNomeContato(data, error);
		}
	}
	
	/**
	* verifica se o nome do contato já está cadastrado para outra empresa 
	* 
	**/
	function validaNomeContato(data, error) {
			var data = new Array();
			data.idContato = document.getElementById("idContato").value;
			data.nmContato = document.getElementById("nmContato").value;
			data.idPessoa = document.getElementById("pessoa.idpessoa").value;
			var sdo = createServiceDataObject("lms.vol.manterContatosVolAction.validaNomeContato","mensagemContato",data);
    		xmit({serviceDataObjects:[sdo]});
	}
	
	function mensagemContato_cb(data, error){
		if (error != undefined) {
	     	alert(error);
	     	setElementValue("nmContato", "");
	     	setFocus(document.getElementById("nmContato"));
     	}
     	
     	document.getElementById("pessoa.nrIdentificacao").onblur = onblurNrIdentificacao;
		document.getElementById("nmContato").onblur = onblurNmContato;
	}
	
	
	/**
	* verifica se o email já está cadastrado para outro contato
	*
	**/
	function validaEmailContato(data, error) {
		if( document.getElementById("dsEmail").value != "" ) {
			var data = new Array();
			data.idContato = document.getElementById("idContato").value;
			data.dsEmail = document.getElementById("dsEmail").value;
			var sdo = createServiceDataObject("lms.vol.manterContatosVolAction.validaEmailContato","mensagemEmailContato",data);
    		xmit({serviceDataObjects:[sdo]});
   		}
	}
	
	function mensagemEmailContato_cb(data, error){
		if (error != undefined) {
	     	alert(error);
	     	setFocus(document.getElementById("dsEmail"));
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
		if (isLookup()) {
			onPageLoad_cb(data,error);
	    } else {
	    	var data = new Array();
			var sdo = createServiceDataObject("lms.vol.manterContatosVolAction.findDataSession","loadFilialUsuario",data);
    		xmit({serviceDataObjects:[sdo]});
	    }
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