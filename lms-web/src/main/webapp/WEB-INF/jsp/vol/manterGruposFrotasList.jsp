<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.manterGruposFrotasAction" onPageLoadCallBack="myPageLoad">
	<adsm:form  action="/vol/manterGruposFrotas">
			<adsm:hidden property="tpAcesso" serializable="false" value="F"/>
	        <adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="true"/>
		    <adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>
			<adsm:lookup label="filial" labelWidth="15%" width="85%"
		             property="filial"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.vol.manterGruposFrotasAction.findLookupFilialByUsuarioLogado" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             exactMatch="true"
		             minLengthForAutoPopUpSearch="3" disabled="true">
		    <adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
        	<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso" />
            <adsm:textbox dataType="text" 
            			  property="filial.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="50" 
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>
		
		<adsm:textbox property="dsNome" dataType="text" label="grupo" maxLength="30" size="35" width="35%" labelWidth="15%"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="grupos"/>
			<adsm:button id="btLimpar" onclick="limpaCampos()" caption="limpar" buttonType="resetButton"/>
		</adsm:buttonBar>
				
	</adsm:form>

	<adsm:grid property="grupos" idProperty="idGrupoFrota" selectionMode="check" 
			   rows="12" gridHeight="150" onRowClick="populaForm" 
			   service="lms.vol.manterGruposFrotasAction.findPaginatedGruposFrota"
			   onDataLoadCallBack="trataBtnAssociarTodos"
			   rowCountService="lms.vol.manterGruposFrotasAction.getRowCountGruposFrota">
		<adsm:gridColumnGroup separatorType="FILIAL">	
		<adsm:gridColumn property="sgFilial" title="filial" width="50" />
		<adsm:gridColumn title="" property="nmFantasia" width="200" align="left" />
		</adsm:gridColumnGroup>	
		<adsm:gridColumn property="dsNome" title="grupo" width="200" />
		<adsm:gridColumn property="qtdFrotasGF" title="qtdFrotasGF" width="145" dataType="integer"/>
		<adsm:gridColumn property="qtdUsuariosGF" title="qtdUsuariosGF" dataType="integer"/>
		<adsm:buttonBar>
		   	<adsm:button caption="associarSelecionados" id="btnAssociarSelecionados" buttonType="removeButton"  onclick="associarUsuarioFrotas();"/>
			<adsm:button caption="associarTodos" id="btnAssociarTodos" buttonType="storeButton" onclick="associarUsuarioFrotas('all');"/>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
	
	<adsm:i18nLabels>
		<adsm:include key="LMS-41040"/>
		<adsm:include key="LMS-41041"/>
		<adsm:include key="LMS-00054"/>
	</adsm:i18nLabels>
	
</adsm:window>
<script>


	/**
	 * Verifica se algum parametro foi enviado para a tela.
	 * Caso a tenha sido enviado significa que a tela esta sendo usada como tela de consulta e sua grid
	 * estara com o click habilitado.
	 */
	function myPageLoad_cb(data,error) {
		if (isLookup()) {
			onPageLoad_cb(data,error);
			setDisabled("btLimpar", false);
			setDisplay("btnAssociarSelecionados", false );
			setDisplay("btnAssociarTodos", false );			
	    } else {
	    	var data = new Array();
			var sdo = createServiceDataObject("lms.vol.manterGruposFrotasAction.findFilialUsuarioLogado", "loadFilialUsuario", data);
    		xmit({serviceDataObjects:[sdo]});
	    }
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
	 * Retorna o parametro 'mode' que contem o modo em que a tela esta sendo utilizada.
	 * Caso mode seja igual a 'lookup' significa que a tela esta sendo aberta por uma lookup.
	 */
	function isLookup() {
		var url = new URL(parent.location.href);
		var mode = url.parameters["mode"];
		if ((mode!=undefined) && (mode=="lookup")) return true;
		return false;
	}

	function initWindow(eventObj) {
		
		if (eventObj.name == "cleanButton_click") {
		    
			fillDataUsuario();
			
			filial_sgFilialOnChangeHandler();	
			
			setFocus(document.getElementById("filial.sgFilial"));
		}
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
	
	function limpaTela() {
		cleanButtonScript(this.document);
		onPageLoad();
	}

	function populaForm(id) {
		var tabGroup = getTabGroup(this.document);
		if(tabGroup) {
			var tabCad = tabGroup.getTab("cad");
			var telaCad = tabCad.tabOwnerFrame;		
			telaCad.onDataLoad(id);
		}
	}
	
	function associarUsuarioFrotas(tipo) {
	   var idsMap = gruposGridDef.getSelectedIds();
		   
	   if (idsMap.ids.length ==0 && tipo == undefined) {
		    alert(i18NLabel.getLabel("LMS-41040"));
	   } else { 
		  var retorno =  showModalDialog('/vol/manterGruposFrotas.do?cmd=usufrota&ids=',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:400px;dialogHeight:200px;');
		  if (retorno == undefined) {
		     return false;
		  } else if (retorno == "") {
		  	 alert(i18NLabel.getLabel("LMS-41041"));
		  	 return false;
		  } else {
			  var data = new Array();
			  data.idUsuario = retorno;
			  data.frota = (tipo == undefined ? idsMap : "");
			  var sdo = createServiceDataObject("lms.vol.manterGruposFrotasAction.executeAssociarUsuarioFrotas", "associarUsuarioFrotas", data);
	    	  xmit({serviceDataObjects:[sdo]});
		  }
       }   
	}
	
	function associarUsuarioFrotas_cb(data,error) {
	   if (error != undefined) {
	      alert(error);
	   } else {
		  alert(i18NLabel.getLabel("LMS-00054"));
		  findButtonScript("grupos", document.forms[0]);
	   }
	   
	}
		
	function trataBtnAssociarTodos_cb(data,error) {
	   setDisabled("btnAssociarTodos",(isLookup() || data == undefined || data.list == undefined));
	}
	function limpaCampos() {
	    var habilita = document.getElementById("btnAssociarTodos").disabled;
    	cleanButtonScript(this.document)
    	setDisabled("btnAssociarTodos",habilita);
    	
    }
</script>