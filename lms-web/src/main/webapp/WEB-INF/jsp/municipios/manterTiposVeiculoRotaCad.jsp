<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterTiposVeiculoRotaAction" onPageLoadCallBack="pageLoadCustom">
	<adsm:form action="/municipios/manterTiposVeiculoRota" service="lms.municipios.manterTiposVeiculoRotaAction.findByIdDetalhamento" onDataLoadCallBack="vigenciaLoad" idProperty="idRotaTipoMeioTransporte">

       	<adsm:lookup service="lms.municipios.manterTiposVeiculoRotaAction.findLookupFilial" dataType="text" property="rotaColetaEntrega.filial" idProperty="idFilial" criteriaProperty="sgFilial"       	
				label="filial" size="3" maxLength="3" action="/municipios/manterFiliais" width="85%" exactMatch="true" required="true" disabled="true">
         	<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
   			<adsm:textbox dataType="text" property="rotaColetaEntrega.filial.pessoa.nmFantasia" size="30" maxLength="50" disabled="true" serializable="false" />
   			<adsm:propertyMapping criteriaProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
   			<adsm:hidden property="filial.empresa.tpEmpresa" serializable="false" value="M"/>
	    </adsm:lookup>
		
       <adsm:lookup service="lms.municipios.manterTiposVeiculoRotaAction.findLookupRotaColetaEntrega" property="rotaColetaEntrega" idProperty="idRotaColetaEntrega" criteriaProperty="nrRota" dataType="integer" labelWidth="15%" label="numeroRota" size="3" maxLength="3" width="35%" action="/municipios/manterRotaColetaEntrega" minLengthForAutoPopUpSearch="7" exactMatch="true" required="true">
        	<adsm:propertyMapping relatedProperty="rotaColetaEntrega.dsRota" modelProperty="dsRota" />
        	<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.idFilial" modelProperty="filial.idFilial" blankFill="false"/>
        	<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" blankFill="false"/>
        	<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.sgFilial" modelProperty="filial.sgFilial" blankFill="false"/>
        	<adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.idFilial" modelProperty="filial.idFilial"/>
        	<adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
        	<adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.sgFilial" modelProperty="filial.sgFilial"/>
            <adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" size="30" disabled="true" />
       </adsm:lookup>
       
       <adsm:combobox property="tipoMeioTransporte.idTipoMeioTransporte" 
	       		label="tipoMeioTransporte" 
	       		service="lms.municipios.manterTiposVeiculoRotaAction.findTipoMeioTransporte" 
	       		optionLabelProperty="dsTipoMeioTransporte" 
	       		optionProperty="idTipoMeioTransporte" boxWidth="200" 
	       		labelWidth="18%" width="32%" 
	       		required="true" 
	       		onlyActiveValues="true">
			<adsm:propertyMapping relatedProperty="tipoMeioTransporte.dsTipoMeioTransporte" modelProperty="dsTipoMeioTransporte" />
       </adsm:combobox>
       <adsm:hidden property="tipoMeioTransporte.dsTipoMeioTransporte"/>

       <adsm:range label="vigencia" labelWidth="15%" width="85%" required="false" >
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>
          
	<adsm:buttonBar>
			<adsm:button caption="meiosTransportePorRota" id="__veiculosRota" action="municipios/manterVeiculosRota" cmd="main">
				<adsm:linkProperty src="rotaColetaEntrega.dsRota" target="rotaTipoMeioTransporte.rotaColetaEntrega.dsRota"/>
				<adsm:linkProperty src="rotaColetaEntrega.filial.idFilial" target="rotaTipoMeioTransporte.rotaColetaEntrega.filial.idFilial"/>
				<adsm:linkProperty src="rotaColetaEntrega.filial.sgFilial" target="rotaTipoMeioTransporte.rotaColetaEntrega.filial.sgFilial"/>
				<adsm:linkProperty src="rotaColetaEntrega.filial.pessoa.nmFantasia" target="rotaTipoMeioTransporte.rotaColetaEntrega.filial.pessoa.nmFantasia"/>
				<adsm:linkProperty src="rotaColetaEntrega.nrRota" target="rotaTipoMeioTransporte.rotaColetaEntrega.nrRota"/>
				<adsm:linkProperty src="idRotaTipoMeioTransporte" target="rotaTipoMeioTransporte.idRotaTipoMeioTransporte"/>
				<adsm:linkProperty src="tipoMeioTransporte.dsTipoMeioTransporte" target="rotaTipoMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte"/>
				<adsm:linkProperty src="tipoMeioTransporte.idTipoMeioTransporte" target="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"/>
			</adsm:button>
			<adsm:storeButton callbackProperty="afterStore" service="lms.municipios.manterTiposVeiculoRotaAction.storeMap" />
			<adsm:newButton id="__botaoNovo" />
			<adsm:removeButton />
		</adsm:buttonBar>
		<script language="javascript">
		    /**
			* Retorna estado dos campos como foram carregados na página.
			*/
			function estadoNovo() {
				//setDisabled(document,false);
				setDisabled("rotaColetaEntrega.idRotaColetaEntrega",document.getElementById("rotaColetaEntrega.idRotaColetaEntrega").masterLink == "true");
				//setDisabled("rotaColetaEntrega.filial.idFilial",document.getElementById("rotaColetaEntrega.filial.idFilial").masterLink == "true");
				setDisabled("rotaColetaEntrega.filial.pessoa.nmFantasia", true);
				setDisabled("rotaColetaEntrega.dsRota", true );				
				setDisabled("__buttonBar:0.removeButton",true);
				setDisabled("__botaoNovo", false);
				setDisabled("__veiculosRota", true);
			}
	
			/**
			 * Habilitar campos se o registro estiver vigente.
			 */
			function habilitarCampos() {
				setDisabled("dtVigenciaFinal",false);
			}
			
			/**
			 * Ao carregar os dados, é tratado o retorno da validação de vigência no detalhamento:
			 */
			function vigenciaLoad_cb(data,exception,errorCode, eventObj) {
				onDataLoad_cb(data,exception,errorCode, eventObj);
				var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
				validaAcaoVigencia(acaoVigenciaAtual, null);
			}
			
			function validaAcaoVigencia(acaoVigenciaAtual, tipoEvento){
				if (acaoVigenciaAtual == 0) {
					estadoNovo();
					setDisabled("__buttonBar:0.removeButton",false);
					setDisabled("__botaoNovo", false);
					setDisabled("__veiculosRota", false);
					if(tipoEvento == "" ||  tipoEvento == null)
     					setFocusOnFirstFocusableField(document);
				    else
				       setFocusOnNewButton(document);
				} else if (acaoVigenciaAtual == 1) {
					setDisabled(document,true);
					setDisabled("__veiculosRota",false);
					setDisabled("__botaoNovo",false);
					setDisabled("__buttonBar:0.storeButton",false);
					habilitarCampos();
					setDisabled("__buttonBar:0.removeButton",true);
					if(tipoEvento == "" ||  tipoEvento == null)
     					setFocusOnFirstFocusableField(document);
				    else
				       setFocusOnNewButton(document);
				} else if (acaoVigenciaAtual == 2) {
					setDisabled(document,true);
					setDisabled("__botaoNovo",false);
					setDisabled("__veiculosRota",false);
					setDisabled("__buttonBar:0.removeButton",true);
					setFocusOnNewButton(document);
				}
			}
			
			function afterStore_cb(data,exception,key) {
		    	store_cb(data,exception,key);
				if (exception == undefined) {
					var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
					var store = "true";
					validaAcaoVigencia(acaoVigenciaAtual, store);
				}
		    }
		
			function initWindow(eventObj) {
				var blDetalhamento = eventObj.name == "gridRow_click" || eventObj.name == "storeButton";
				if (!blDetalhamento){
					estadoNovo();
					setValues();
					setFocusOnFirstFocusableField();
				}
			}

		function pageLoadCustom_cb() {
			onPageLoad_cb();
			var sdo = createServiceDataObject("lms.portaria.manterTerminaisAction.findFilialUsuarioLogado","findFilialCallBack",null);
			xmit({serviceDataObjects:[sdo]});
		}
		
		var idFilialLogado;
		var sgFilialLogado;
		var nmFilialLogado; 
		
		function findFilialCallBack_cb(data,error) {
			if (error != undefined) {
				alert(error);
				return false;
			}
			if (data != undefined) {
				idFilialLogado = getNestedBeanPropertyValue(data,"idFilial");
				sgFilialLogado = getNestedBeanPropertyValue(data,"sgFilial");
				nmFilialLogado = getNestedBeanPropertyValue(data,"pessoa.nmFantasia");
				setValues();
			}
		}
	
		function setValues() {
			if (idFilialLogado != undefined &&
				sgFilialLogado != undefined &&
				nmFilialLogado != undefined &&
				document.getElementById("rotaColetaEntrega.filial.idFilial").masterLink != "true") {
				setElementValue("rotaColetaEntrega.filial.idFilial",idFilialLogado);
				setElementValue("rotaColetaEntrega.filial.sgFilial",sgFilialLogado);
				setElementValue("rotaColetaEntrega.filial.pessoa.nmFantasia",nmFilialLogado);
			}
		}
</script>
</adsm:form>
</adsm:window>   
