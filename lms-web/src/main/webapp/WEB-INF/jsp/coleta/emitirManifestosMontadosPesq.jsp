<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.coleta.emitirManifestosMontadosAction" onPageLoadCallBack="carregaMoedaSessao">
	<adsm:form action="/coleta/emitirManifestosMontados">
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="tpAcesso" value="F" serializable="false"/>        		
		
		<adsm:lookup label="filial" dataType="text" size="3" maxLength="3" width="85%" required="true"
					 property="filial" exactMatch="true" 
					 onPopupSetValue="validaAcessoFilial"
					 service="lms.coleta.emitirManifestosMontadosAction.findLookupFiliaisPorUsuario" 
                     action="/municipios/manterFiliais"
                     idProperty="idFilial"
                     criteriaProperty="sgFilial">
                     
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>					 
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"/>                     			
			
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" serializable="false" size="30" maxLength="30" disabled="true"/>
        </adsm:lookup>

        
		<adsm:lookup label="rota" width="85%" size="3" maxLength="3" serializable="true"
					 exactMatch="true"
					 dataType="integer" 
					 property="rotaColetaEntrega" 
					 idProperty="idRotaColetaEntrega" 
					 criteriaProperty="nrRota"
					 service="lms.coleta.emitirManifestosMontadosAction.findLookupRotaColetaEntrega" 
					 action="/municipios/manterRotaColetaEntrega">
 		    <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
 		    <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial" />
 		    <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
		    
        	<adsm:propertyMapping modelProperty="dsRota" relatedProperty="rotaColetaEntrega.dsRota"/>
        	<adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" disabled="true" size="50" serializable="true"/>
        </adsm:lookup>	

    
		<adsm:combobox label="moeda" width="85%"
					   property="moeda.idMoeda" 
					   service="lms.coleta.emitirManifestosMontadosAction.findComboMoeda" 
					   optionProperty="idMoeda" 
					   optionLabelProperty="siglaSimbolo" 
					   required="true"
					   onchange="onChangeComboMoeda()"
					   onlyActiveValues="true"
					   />
		<adsm:hidden property="dsSimboloMoedaHidden" serializable="true"/>
		
    	<adsm:combobox label="formatoRelatorio" width="85%"
		               domain="DM_FORMATO_RELATORIO" renderOptions="true"
		               property="tpFormatoRelatorio"
		               defaultValue="pdf"
		               required="true"
		/>	
				

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.coleta.emitirManifestosMontadosAction" /> 
			<adsm:button caption="limpar" disabled="false" onclick="limpaTela()" buttonType="resetButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
document.getElementById("filial.sgFilial").serializable = true;

function limpaTela() {
	cleanButtonScript(this.document);
    carregaDadosFilial();
    carregaMoedaSessao_cb(undefined,undefined);
}

function carregaMoedaSessao_cb(data, error){
     carregaDadosFilial();
	var criteria = new Array();
    // para carregar a moeda que está na sessão do usuário
    var sdo = createServiceDataObject("lms.coleta.emitirManifestosMontadosAction.findMoedaSessao", "resultadoFindMoeda", criteria);
    xmit({serviceDataObjects:[sdo]});
}

/**
 * Cb da chamada para buscar a moeda da sessão do usuario
 */
 function resultadoFindMoeda_cb(data, error) {
	if(data.idMoeda==undefined || data.idMoeda=="") {
		return false;
	} else {
		setElementValue('moeda.idMoeda', data.idMoeda);
		setElementValue('dsSimboloMoedaHidden', data.dsSimbolo);
	}
}

/**
 * Verifica se o usuario tem acesso a filial selecionada na popup de filial.
 * Função necessária pois quando é selecionado um item na popup não é chamado
 * o serviço definido na lookup.
 */
 function validaAcessoFilial(data) {
	var criteria = new Array();
    // Monta um map
    setNestedBeanPropertyValue(criteria, "idFilial", data.idFilial);
    setNestedBeanPropertyValue(criteria, "sgFilial", data.sgFilial);
        	
    var sdo = createServiceDataObject("lms.coleta.emitirManifestosMontadosAction.findLookupFiliaisPorUsuario", "resultadoValidaAcessoFilial", criteria);
    xmit({serviceDataObjects:[sdo]});
}

/**
 * Função que trata o retorno da função validaAcessoFilial.
 */
 function resultadoValidaAcessoFilial_cb(data, error) {

	if (error != undefined) {
		alert(error);
		resetValue(document.getElementById("filial.sgFilial"), this.document);
		resetValue(document.getElementById("filial.idFilial"), this.document);
		resetValue(document.getElementById("filial.pessoa.nmFantasia"), this.document);
		
	   setFocus(document.getElementById("filial.sgFilial"));
	   
		return false;
	} else {
		filial_sgFilial_exactMatch_cb(data, error);
	}
}

function onChangeComboMoeda() {
	var comboMoeda= document.getElementById("moeda.idMoeda");
	setElementValue('dsSimboloMoedaHidden', comboMoeda.options[comboMoeda.selectedIndex].text);
}
/**
 * Carrega a filial que o usuario está logado
 * 
 */
 function carregaDadosFilial() {
	var criteria = new Array();
	
    var sdo = createServiceDataObject("lms.coleta.emitirManifestosMontadosAction.getFilialUsuarioLogado", "resultadoBuscaFilial", criteria);
    xmit({serviceDataObjects:[sdo]});
}

function resultadoBuscaFilial_cb(data, error) {
   
	if(data.idFilial==undefined || data.idFilial=="") {
		return false;
	} else {
		setElementValue('filial.idFilial', data.idFilial);
		setElementValue('filial.sgFilial', data.sgFilial);
		setElementValue('filial.pessoa.nmFantasia', data.pessoa.nmFantasia);
	}
}
</script>