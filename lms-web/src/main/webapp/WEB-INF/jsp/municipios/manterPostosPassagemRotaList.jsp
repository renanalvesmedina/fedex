<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	function desabilitaDescricaoPostoPassagem(){
		onPageLoad();
		setDisabled("postoPassagem.idPostoPassagem", false);
	    setDisabled("postoPassagem.tpPostoPassagem.description", true);
	}
</script>
<%--<adsm:window title="consultarPostosPassagemRota" service="lms.municipios.postoPassagemRotaColEntService">--%>
<adsm:window title="consultarPostosPassagemRota" service="lms.municipios.manterPostosPassagemRotaAction"
		onPageLoad="desabilitaDescricaoPostoPassagem"
		onPageLoadCallBack="pageLoadCustom" >
	<adsm:form action="/municipios/manterPostosPassagemRota" idProperty="idPostoPassagemRotaColEnt">
		
		<adsm:hidden property="idFilialSessao" serializable="false" />
		<adsm:hidden property="sgFilialSessao" serializable="false" />
		<adsm:hidden property="nmFilialSessao" serializable="false" />
		<adsm:hidden property="tpEmpresaMercurioValue" value="M" />

		<adsm:lookup dataType="text" property="rotaColetaEntrega.filial" idProperty="idFilial" criteriaProperty="sgFilial"
    			service="lms.municipios.manterPostosPassagemRotaAction.findLookupFilial" action="/municipios/manterFiliais"
    			size="3" maxLength="3" label="filial" labelWidth="17%" width="33%" minLengthForAutoPopUpSearch="3" exactMatch="true" required="true" disabled="true">
    		<adsm:propertyMapping criteriaProperty="tpEmpresaMercurioValue" modelProperty="empresa.tpEmpresa" />
         	<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" inlineQuery="false"/>
			<adsm:textbox dataType="text" property="rotaColetaEntrega.filial.pessoa.nmFantasia" size="32" disabled="true" serializable="false" required="false" />
	    </adsm:lookup>

		<adsm:lookup service="lms.municipios.manterPostosPassagemRotaAction.findLookupRotaColetaEntrega" property="rotaColetaEntrega" idProperty="idRotaColetaEntrega" criteriaProperty="nrRota" dataType="integer" labelWidth="17%" label="numeroRota" size="5" maxLength="5" width="33%" action="/municipios/manterRotaColetaEntrega" exactMatch="true">
        	<adsm:propertyMapping relatedProperty="rotaColetaEntrega.dsRota" modelProperty="dsRota" inlineQuery="false"/>
	        <adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" size="30" disabled="true" serializable="false"/>
			<adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.idFilial" modelProperty="filial.idFilial" />
			<adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.sgFilial" modelProperty="filial.sgFilial" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" inlineQuery="false"/>			
			<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.idFilial" modelProperty="filial.idFilial" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.sgFilial" modelProperty="filial.sgFilial" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" blankFill="false"/>			
			
        </adsm:lookup>
        
        
        <adsm:lookup dataType="text" property="postoPassagem"  idProperty="idPostoPassagem" criteriaProperty="tpPostoPassagem.description" 
        		action="/municipios/manterPostosPassagem" service="lms.municipios.manterPostosPassagemRotaAction.findLookupByFormaCobranca"
        		size="30" minLengthForAutoPopUpSearch="3" 
        		label="postoPassagem" labelWidth="17%" width="33%">
        	<adsm:propertyMapping relatedProperty="postoPassagem.municipio.nmMunicipio" modelProperty="municipio.nmMunicipio" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.tpSentidoCobranca.description" modelProperty="tpSentidoCobranca.description" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.rodovia.sgRodovia" modelProperty="rodovia.sgRodovia" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.nrKm" modelProperty="nrKm" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.concessionaria.pessoa.nrIdentificacaoFormatado" modelProperty="concessionaria.pessoa.nrIdentificacaoFormatado"/>
        	<adsm:propertyMapping relatedProperty="postoPassagem.concessionaria.pessoa.nmPessoa" modelProperty="concessionaria.pessoa.nmPessoa"/>
	    </adsm:lookup>
        
        <adsm:textbox dataType="text" property="postoPassagem.concessionaria.pessoa.nrIdentificacaoFormatado" size="18" label="concessionaria" labelWidth="17%"  disabled="true" width="15%" serializable="false"/>
		<adsm:textbox dataType="text" property="postoPassagem.concessionaria.pessoa.nmPessoa" size="21" disabled="true" width="15%" serializable="false"/>
        
        
        <adsm:textbox dataType="text" property="postoPassagem.municipio.nmMunicipio"         label="localizacao"     size="30" labelWidth="17%" width="33%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" property="postoPassagem.tpSentidoCobranca.description" label="sentido"         size="30" labelWidth="17%" width="33%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" property="postoPassagem.rodovia.sgRodovia"           label="rodovia"           size="30" labelWidth="17%" width="33%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" property="postoPassagem.nrKm"                        label="km"                size="10" labelWidth="17%" width="33%" disabled="true" serializable="false"/>
        
        
        <adsm:range label="vigencia" labelWidth="17%" width="73%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="postoPassagemRotaColEnt"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form> 

	<adsm:grid property="postoPassagemRotaColEnt" idProperty="idPostoPassagemRotaColEnt" rows="9" 
			   selectionMode="check" scrollBars="horizontal" unique="true" 
			   gridHeight="180"
			   service="lms.municipios.manterPostosPassagemRotaAction.findPaginatedCustom"
			   rowCountService="lms.municipios.manterPostosPassagemRotaAction.getRowCountCustom">
		<adsm:gridColumnGroup customSeparator=" - ">		
				<adsm:gridColumn title="rota" property="rotaColetaEntrega_nrRota" width="60"/>
				<adsm:gridColumn title="" property="rotaColetaEntrega_dsRota" width="175"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="postoPassagem" property="postoPassagem_tpPostoPassagem" isDomain="true" width="180" />
		<adsm:gridColumn title="municipio" property="postoPassagem_municipio_nmMunicipio" width="180" />
		<adsm:gridColumn title="sentido" property="postoPassagem_tpSentidoCobranca" isDomain="true" width="180" />
		<adsm:gridColumn title="rodovia" property="postoPassagem_rodovia_sgRodovia" width="100" />
		<adsm:gridColumn title="km" property="postoPassagem_nrKm" width="80" align="right"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="90" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="80" dataType="JTDate"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script type="text/javascript">
<!--

	document.getElementById("idFilialSessao").masterLink = true;
	document.getElementById("sgFilialSessao").masterLink = true;
	document.getElementById("nmFilialSessao").masterLink = true;
	
	function pageLoadCustom_cb(data,error) {
		onPageLoad_cb(data,error);
		findFilialUsuarioLogado();
	}

	function findFilialUsuarioLogado() {
		var sdo = createServiceDataObject("lms.municipios.manterPostosPassagemRotaAction.findFilialUsuarioLogado",
				"findFilialUsuarioLogado",undefined);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findFilialUsuarioLogado_cb(data,error) {
		setElementValue("idFilialSessao",getNestedBeanPropertyValue(data,"idFilial"));
		setElementValue("sgFilialSessao",getNestedBeanPropertyValue(data,"sgFilial"));
		setElementValue("nmFilialSessao",getNestedBeanPropertyValue(data,"pessoa.nmFantasia"));
		if (!document.getElementById('rotaColetaEntrega.filial.idFilial').masterLink) {
			populateFilial();
		}
	}

	function populateFilial() {
		setElementValue("rotaColetaEntrega.filial.idFilial",getElementValue("idFilialSessao"));
		setElementValue("rotaColetaEntrega.filial.sgFilial",getElementValue("sgFilialSessao"));
		setElementValue("rotaColetaEntrega.filial.pessoa.nmFantasia",getElementValue("nmFilialSessao"));
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			if (!document.getElementById('rotaColetaEntrega.filial.idFilial').masterLink) {
				populateFilial();
			}
		}
	}

//-->
</script>