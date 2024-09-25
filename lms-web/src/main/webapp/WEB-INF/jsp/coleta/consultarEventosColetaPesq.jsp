<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function loadPage() {
	onPageLoad();
	setMasterLink(document, true);
	limpaTela();
	var idPedidoColeta = getElementValue("idPedidoColeta");
	findPedidoColeta(idPedidoColeta);

	isParentPopUp();
	document.getElementById("pedidoColeta.filialByIdFilialResponsavel.sgFilial").required = true;
	document.getElementById("pedidoColeta.filialByIdFilialResponsavel.idFilial").required = true;
	
}

function findPedidoColeta(idPedidoColeta) {
	if (idPedidoColeta != undefined && idPedidoColeta.length != 0){
		// se idPedidoColeta estiver definido, carregar o PedidoColeta e preencher os dados da filial e numeroColeta
		setDisabled('limpar', true);
	    var data = new Array();
	    setNestedBeanPropertyValue(data, "idPedidoColeta", idPedidoColeta);
    	setElementValue("pedidoColeta.idPedidoColeta",idPedidoColeta);
   	    var sdo = createServiceDataObject("lms.coleta.consultarEventosColetaAction.findPedidoColetaById", "servico",data);    
	    xmit({serviceDataObjects:[sdo]});
	}
}

function servico_cb(data,errorMsg) {	
    var filial = getNestedBeanPropertyValue(data,"filialByIdFilialResponsavel.sgFilial");
    var idfilial = getNestedBeanPropertyValue(data,"filialByIdFilialResponsavel.idFilial");
    var numeroColeta = getNestedBeanPropertyValue(data,"nrColeta");
	if (filial != undefined){
	    setElementValue("pedidoColeta.filialByIdFilialResponsavel.sgFilial",filial);
	    setElementValue("pedidoColeta.filialByIdFilialResponsavel.idFilial",idfilial);
	    setElementValue("pedidoColeta.nrColeta",numeroColeta);
		setDisabled("pedidoColeta.idPedidoColeta", true);
		newFindItem(document.forms[0]);
  	    format(document.getElementById("pedidoColeta.nrColeta"));
	}
}	

/**
 * Verifica se a tela pai esta sendo utilizada como pop-up
 * Caso seja, desabilita o botao de limpar junto com a lupa da lookup de coleta.
 * Alem disso mostra ou esconde o botão Fechar.
 */
function isParentPopUp() {
	if (getElementValue("popUp")=="true" || getElementValue("popUp")== true) {
		setDisabled("pedidoColeta.filialByIdFilialResponsavel.idFilial", true);
		setDisabled("pedidoColeta.filialByIdFilialResponsavel.sgFilial", true);
		setDisabled("pedidoColeta.idPedidoColeta", true);
		setDisabled("limpar", true);
		setDisabled('btnFechar',false);
		setFocusOnFirstFocusableField();
	} else {
		setDisabled("pedidoColeta.nrColeta", true);
		//Aqui devemos esconder o botão "fechar".
		//O setVisibility não pode ser usado pq acaba escondendo toda
		//a barra de botões, e existem outros botões na mesma.
		//Solução: Foi colocado os outros botões na barra superior,
		//deixando apenas o botão "fechar" nesta barra.
		setVisibility('btnFechar', false);
	}
}
</script>

<adsm:window service="lms.coleta.consultarEventosColetaAction" onPageLoad="loadPage">
	<adsm:form action="/coleta/consultarEventosColeta" idProperty="idEventoColeta" >

		<adsm:hidden property="idPedidoColeta"/>
		<adsm:hidden property="popUp"/>

		<adsm:lookup dataType="text" property="pedidoColeta.filialByIdFilialResponsavel"  idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.coleta.consultarEventosColetaAction.findLookupBySgFilial" action="/municipios/manterFiliais" onchange="return sgFilialOnChangeHandler();" 
					 onDataLoadCallBack="loadFilial"
					 label="coleta" size="3" maxLength="3" labelWidth="20%" width="30%" picker="false" serializable="true">

			<adsm:lookup dataType="integer" property="pedidoColeta" idProperty="idPedidoColeta" criteriaProperty="nrColeta"
						 action="/coleta/consultarColetas" cmd="pesq" service="lms.coleta.consultarEventosColetaAction.findLookupPedidoColeta"
						 onDataLoadCallBack="loadColeta" 
						 onPopupSetValue="enableColeta"
						 mask="00000000" 
						 onchange="return sgColetaOnChangeHandler();"
						 exactMatch="false" 
						 required="true" size="10" maxLength="8"> 
						 <adsm:propertyMapping  criteriaProperty="pedidoColeta.filialByIdFilialResponsavel.idFilial"  modelProperty="filialByIdFilialResponsavel.idFilial"/>
						 <adsm:propertyMapping  criteriaProperty="pedidoColeta.filialByIdFilialResponsavel.sgFilial"  modelProperty="filialByIdFilialResponsavel.sgFilial"/>
			</adsm:lookup>
		</adsm:lookup>

        <adsm:section caption="opcaoRelatorio" width="100%" />

    	<adsm:combobox label="formatoRelatorio" labelWidth="20%" width="80%"
		               domain="DM_FORMATO_RELATORIO" renderOptions="true"
		               property="tpFormatoRelatorio"
		               defaultValue="pdf"
		               required="true"
		/>	


		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="visualizarEventosColeta" id="consultarEventosColeta" onclick="validateFilial()" disabled="true"/>
			<adsm:button caption="limpar" id="limpar" onclick="newItem(this.document);" disabled="false"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="eventoColeta" 
	           idProperty="idEventoColeta" 
	           selectionMode="none" 
	           unique="true" rows="12"
	           gridHeight="200" 
	           onRowClick="populaForm" 
	           defaultOrder="dhEvento"
	           service="lms.coleta.consultarEventosColetaAction.findPaginatedConsultaEventosColeta"
	           rowCountService="lms.coleta.consultarEventosColetaAction.getRowCountConsultaEventosColeta"
	           >
		<adsm:gridColumn property="tpEventoColeta" title="evento" width="20%" isDomain="true"/>
		<adsm:gridColumn property="dhEvento" title="dataHora" width="15%" dataType="JTDateTimeZone" align="center"/>
		<adsm:gridColumn property="meioTransporteRodoviario.meioTransporte.nrFrota" title="frota" width="13%"/>
		<adsm:gridColumn property="ocorrenciaColeta.dsDescricaoCompleta" title="ocorrencia" width="26%" />
		<adsm:gridColumn property="usuario.nmUsuario" title="usuario" width="26%" />
	</adsm:grid>
	<adsm:buttonBar>
		<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />	
	</adsm:buttonBar>
</adsm:window>

<script>

	function validateFilial() {		
		var valid = validateTabScript(document.forms[0]);
		if (!valid) {
			return false;
		}
		reportButtonScript('lms.coleta.relatorioEventosColetaService', 'openPdf', this.document.forms[0]);
	}

	function newFindItem(form) {
		findButtonScript('eventoColeta', form);
		if (getElementValue("pedidoColeta.filialByIdFilialResponsavel.sgFilial")=='' || getElementValue("pedidoColeta.nrColeta")=='') {
			limpaTela();	
		} else if (getElementValue("pedidoColeta.filialByIdFilialResponsavel.sgFilial")=='' && getElementValue("pedidoColeta.nrColeta")=='') {
			setDisabled('consultarEventosColeta', true);		
		} else {
			setDisabled('consultarEventosColeta', false);		
		}
	}

	function newItem(form) {
		cleanButtonScript(this.document);
		limpaTela();
		setDisabled("pedidoColeta.nrColeta", true);	
	}

	function sgFilialOnChangeHandler() {
		if (getElementValue("pedidoColeta.filialByIdFilialResponsavel.sgFilial")=='') {
			limpaTela();	
		    setElementValue("pedidoColeta.nrColeta","");		
			setDisabled('pedidoColeta.nrColeta', true);
		} else {
			setDisabled('pedidoColeta.nrColeta', false);
		}
		return lookupChange({e:document.forms[0].elements["pedidoColeta.filialByIdFilialResponsavel.idFilial"]});
	}
	
	function sgColetaOnChangeHandler() {
		if (getElementValue("pedidoColeta.nrColeta")=='') {
			limpaTela();	
		}
		var ret = pedidoColeta_nrColetaOnChangeHandler();
		return ret;
	}

	function loadFilial_cb(data, error) {
		setElementValue("pedidoColeta.nrColeta","");		
		setDisabled('pedidoColeta.nrColeta', false);	
		limpaTela();	
		return pedidoColeta_filialByIdFilialResponsavel_sgFilial_exactMatch_cb(data);
	}
	
	function loadColeta_cb(data, error) {
		limpaTela();
		var ret = pedidoColeta_nrColeta_exactMatch_cb(data);
		if (ret == true) {
			newFindItem(document.forms[0]);		
		}	
	}
	
	function populaForm(valor) {
		return false;
	}
	
	function enableColeta(data){
		var idPedidoColeta = getNestedBeanPropertyValue(data, "idPedidoColeta");
		var idPedidoColetaForm = getElementValue("pedidoColeta.idPedidoColeta");
		var nrColeta = getElementValue("pedidoColeta.nrColeta");
		
		var filial = getNestedBeanPropertyValue(data, "sgFilial");
		var idFilial = getNestedBeanPropertyValue(data, "idFilial");

	    setElementValue("pedidoColeta.filialByIdFilialResponsavel.sgFilial",filial);
	    setElementValue("pedidoColeta.nrColeta",nrColeta);
	    setElementValue("pedidoColeta.idPedidoColeta",idPedidoColeta);
	    setElementValue("pedidoColeta.filialByIdFilialResponsavel.idFilial",idFilial);
	    
		setDisabled('pedidoColeta.nrColeta', false);		
		eventoColetaGridDef.executeSearch(buildFormBeanFromForm(this.document.forms[0]));
		setDisabled('consultarEventosColeta', false);				
	}
	
   function limpaTela() {
		eventoColetaGridDef.resetGrid();			
		setDisabled('consultarEventosColeta', true);		
		setDisabled('limpar', false);		   
   }
   	
   function habilitaConsultarEventosColeta() {
		setDisabled('consultarEventosColeta', true);				
   }
</script>