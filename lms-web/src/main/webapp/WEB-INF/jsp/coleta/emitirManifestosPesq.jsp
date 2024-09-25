<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.coleta.emitirManifestosAction" onPageLoad="carregaDadosFilial">
	<adsm:form action="/coleta/emitirManifestos">

		<adsm:hidden property="manifestoColeta.filial.idFilial" serializable="true"/>
		<adsm:hidden property="manifestoColeta.filial.pessoa.nmFantasia" serializable="true"/>
		<adsm:hidden property="manifestoColeta.tpStatusManifestoColeta" value="GE" serializable="true"/>
		<adsm:textbox dataType="text" property="manifestoColeta.filial.sgFilial" disabled="true"
					 label="manifesto" labelWidth="31%" width="69%" size="3" maxLength="3" serializable="true">
			<adsm:lookup dataType="integer" property="manifestoColeta" idProperty="idManifestoColeta" criteriaProperty="nrManifesto"
						 action="/coleta/consultarManifestosColeta" cmd="pesq" service="lms.coleta.emitirManifestosAction.findLookupManifestoColeta" 
						 onPopupSetValue="limpaRotaColetaEntrega" onDataLoadCallBack="limpaRotaColetaEntrega" 
						 exactMatch="false" size="15" maxLength="8" mask="00000000" > 
				<adsm:propertyMapping criteriaProperty="manifestoColeta.tpStatusManifestoColeta" modelProperty="tpStatusManifestoColeta" disable="true" /> <%-- inlineQuery="false" --%>
				<adsm:propertyMapping criteriaProperty="manifestoColeta.filial.idFilial" modelProperty="filial.idFilial" disable="true" /> <%-- inlineQuery="false" --%>
 		    	<adsm:propertyMapping criteriaProperty="manifestoColeta.filial.sgFilial" modelProperty="filial.sgFilial" disable="true" /> <%-- inlineQuery="false" --%>
 		    	<adsm:propertyMapping modelProperty="filial.idFilial" relatedProperty="manifestoColeta.filial.idFilial" blankFill="false"/>  
				<adsm:propertyMapping modelProperty="nrManifesto" relatedProperty="manifestoColeta.nrManifesto"/>
			</adsm:lookup>
		</adsm:textbox>

		<adsm:lookup label="rotaColetaEntrega" labelWidth="31%" width="69%" maxLength="3" size="3"
					 dataType="integer" 
					 property="rotaColetaEntrega" 
					 idProperty="idRotaColetaEntrega" 
					 criteriaProperty="nrRota"
					 service="lms.coleta.emitirColetasAbertasAction.findLookupRotaColetaEntrega"
					 onPopupSetValue="limpaManifesto" onDataLoadCallBack="limpaManifesto"
					 action="/municipios/manterRotaColetaEntrega">
			 <adsm:propertyMapping criteriaProperty="manifestoColeta.filial.idFilial" modelProperty="filial.idFilial" />
			<adsm:propertyMapping criteriaProperty="manifestoColeta.filial.sgFilial" modelProperty="filial.sgFilial" />
			<adsm:propertyMapping criteriaProperty="manifestoColeta.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
        	<adsm:propertyMapping modelProperty="dsRota" relatedProperty="rotaColetaEntrega.dsRota"/>
        	<adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" disabled="true" maxLength="50" size="30"/>
        </adsm:lookup>

        <adsm:textbox dataType="integer"
                      onchange="return validaNrFolhasComplementares()" 
                      property="nrFolhasComplementares" 
                      label="folhasComplementares" 
                      labelWidth="31%" width="69%" size="3" maxLength="3"/>

		<adsm:buttonBar>
            <adsm:button buttonType="reportViewerButton" id="btnVisualizar" caption="visualizar" onclick="imprimeRelatorio()"  />
			<adsm:resetButton id="btnLimpar"/>
		</adsm:buttonBar>

    <script type="text/javascript">
        var LMS_02021 = "<adsm:label key="LMS-02021"/>";
        var LMS_02047 = "<adsm:label key="LMS-02047"/>";
    </script>
	</adsm:form>
</adsm:window>
<script type="text/javascript" >

function validaNrFolhasComplementares() {
    var obj = document.getElementById("nrFolhasComplementares");
    obj.minValue="1";
    obj.maxValue="20";
    
    if( !validateMinMaxValue(obj) ) {
        alert(LMS_02047);
        setElementValue(obj,"");
        setFocus(obj);        
        return false;
    }
    
    return true;
}

function imprimeRelatorio() {
    executeReportWithCallback('lms.coleta.emitirManifestosAction', 'verificaEmissao', document.forms[0]);
}

/**
 * Função para verificar se o relatorio foi impresso, caso seja
 * executar o registro da emissão do manifesto de coleta
 */
function verificaEmissao_cb(strFile, error) {
	if (error){
		alert(error);
		return false;
	} 
    openReportWithLocator(strFile._value, error);
    var registraEmissao = confirm(LMS_02021);
    if (registraEmissao) {
        var sdo = createServiceDataObject("lms.coleta.emitirManifestosAction.executeRegistrarEmissaoManifestoColeta", "resultadoRegistraEmissao",buildFormBeanFromForm(this.document.forms[0]));
        xmit({serviceDataObjects:[sdo]});
    } else {
    	document.getElementById('btnLimpar').focus();
    }
}

function resultadoRegistraEmissao_cb(data, error) {
    if (error) {
        alert(error);
        return false;
    } else {
    	showSuccessMessage();
    	document.getElementById('btnLimpar').focus();
    }
    
}

function initWindow(eventObjectName) {
    carregaDadosFilial();
}

/**
 * Carrega a filial que o usuario está logado
 */
 function carregaDadosFilial() {
	var criteria = new Array();
	
    var sdo = createServiceDataObject("lms.coleta.emitirManifestosMontadosAction.getFilialUsuarioLogado", "resultadoBuscaFilial", criteria);
    xmit({serviceDataObjects:[sdo]});
}

function resultadoBuscaFilial_cb(data, error) {
   	if (error){
   		alert(error);
   		return false;
   	}
	if(data.idFilial==undefined || data.idFilial=="") {
		return false;
	} else {
		setElementValue('manifestoColeta.filial.idFilial', data.idFilial);
		setElementValue('manifestoColeta.filial.sgFilial', data.sgFilial);
		setElementValue('manifestoColeta.filial.pessoa.nmFantasia', data.pessoa.nmFantasia);
	}
}

function limpaRotaColetaEntrega_cb(data, error){
	manifestoColeta_nrManifesto_exactMatch_cb(data);
	if (error){
   		alert(error);
   		return false;
   	}
   	limpaRotaColetaEntrega();
}

function limpaRotaColetaEntrega(){
	resetValue('rotaColetaEntrega.idRotaColetaEntrega');
}

function limpaManifesto_cb(data, error){
	rotaColetaEntrega_nrRota_exactMatch_cb(data);
	if (error){
   		alert(error);
   		return false;
   	}
   	limpaManifesto();
}

function limpaManifesto(){
	resetValue('manifestoColeta.idManifestoColeta');
}
</script>
