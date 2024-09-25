<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.fretecarreteiroviagem.manterOcorrenciasReciboAction" onPageLoadCallBack="pageLoadCallBackCustom" >
	<adsm:form action="/freteCarreteiroViagem/manterOcorrenciasRecibo" idProperty="idOcorrenciaFreteCarreteiro" >

		<adsm:hidden property="idFilialSessao" />
		<adsm:hidden property="sgFilialSessao" />
		<adsm:hidden property="nmFilialSessao" />

		<adsm:lookup dataType="text" property="reciboFreteCarreteiro.filial" 
				idProperty="idFilial" criteriaProperty="sgFilial"
    			service="lms.fretecarreteiroviagem.manterOcorrenciasReciboAction.findLookupFilial"
    			action="/municipios/manterFiliais" onchange="return filialChange(this);" onPopupSetValue="popupFilial"
    			label="filialOrigem" size="3" maxLength="3" labelWidth="18%" width="32%" 
    			exactMatch="true" required="true" serializable="true" >
         	<adsm:propertyMapping relatedProperty="reciboFreteCarreteiro.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
         	<adsm:propertyMapping relatedProperty="reciboFreteCarreteiro2.filial.sgFilial" modelProperty="sgFilial" />
         	<adsm:textbox dataType="text" property="reciboFreteCarreteiro.filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false" />
	    </adsm:lookup>
 
		<adsm:combobox property="tpReciboFreteCarreteiro" domain="DM_TIPO_RECIBO_PAGAMENTO_FRETE_CARRETEIR" renderOptions="true"
				label="tipoRecibo" labelWidth="18%" width="32%" onchange="return tpReciboChange(this);" />

		<%--Lookup Recibo Frete Carreteiro ------------------------------------------------------------------------------------------------------%>
		<adsm:textbox dataType="text" property="reciboFreteCarreteiro2.filial.sgFilial"
				label="recibo" size="3" maxLength="3" labelWidth="18%" width="32%" cellStyle="vertical-align=bottom;"
				disabled="true" serializable="false" >
	        <adsm:lookup dataType="integer" property="reciboFreteCarreteiro"
					idProperty="idReciboFreteCarreteiro" criteriaProperty="nrReciboFreteCarreteiro"
					service="lms.fretecarreteiroviagem.manterOcorrenciasReciboAction.findLookupRecibo"
					action="/freteCarreteiroViagem/manterRecibos"
					onchange="return onNrReciboChange(this);" onPopupSetValue="popupRecibo"
					onDataLoadCallBack="reciboDataLoad"
					size="10" maxLength="10"  cellStyle="vertical-align=bottom;" exactMatch="true" mask="0000000000"
					picker="true" serializable="true" disabled="true" >
						
	        	<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="reciboFreteCarreteiro.filial.idFilial" />
	        	<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="reciboFreteCarreteiro.filial.sgFilial" 
	        			inlineQuery="false" />       	        	
	        	<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia"
	        			criteriaProperty="reciboFreteCarreteiro.filial.pessoa.nmFantasia" inlineQuery="false" />
	        			
	        	<adsm:propertyMapping modelProperty="filial.idFilial" relatedProperty="reciboFreteCarreteiro.filial.idFilial" blankFill="false" />
	        	<adsm:propertyMapping modelProperty="filial.sgFilial" relatedProperty="reciboFreteCarreteiro.filial.sgFilial" blankFill="false" />
	        	<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" 
	        			relatedProperty="reciboFreteCarreteiro.filial.pessoa.nmFantasia" blankFill="false" />
	        	
	        	<adsm:propertyMapping modelProperty="controleCarga.idFilialOrigem" 
	        			relatedProperty="reciboFreteCarreteiro.controleCarga.filialByIdFilialOrigem.idFilial" />
	        	<adsm:propertyMapping modelProperty="controleCarga.sgFilialOrigem" 
	        			relatedProperty="reciboFreteCarreteiro.controleCarga.filialByIdFilialOrigem.sgFilial" />
	        	<adsm:propertyMapping modelProperty="controleCarga.nrControleCarga" 
	        			relatedProperty="reciboFreteCarreteiro.controleCarga.nrControleCarga" />
	        	<adsm:propertyMapping modelProperty="controleCarga.nmFilialOrigem" 
	        			relatedProperty="reciboFreteCarreteiro.controleCarga.filialByIdFilialOrigem.nmFantasia" />	        	
	        	
	        	<adsm:propertyMapping modelProperty="tpReciboFreteCarreteiro" criteriaProperty="tpReciboFreteCarreteiro" />
	        	<adsm:propertyMapping modelProperty="blComplementar" relatedProperty="blComplementar" />
	        </adsm:lookup>
        </adsm:textbox>
        
        <adsm:checkbox property="blComplementar" serializable="false"
        		label="complementar" labelWidth="18%" width="32%" disabled="true" />
		<%--Lookup Recibo Frete Carreteiro ------------------------------------------------------------------------------------------------------%>
				
		
		<adsm:lookup dataType="text" property="reciboFreteCarreteiro.controleCarga.filialByIdFilialOrigem" 
				idProperty="idFilial" criteriaProperty="sgFilial"
				service="lms.fretecarreteiroviagem.manterOcorrenciasReciboAction.findLookupFilial"
				action="/municipios/manterFiliais" onchange="return filialControleCargaChange(this);"
				label="controleCarga" size="3" maxLength="3" labelWidth="18%" width="32%" picker="false" >
        	
        	<adsm:propertyMapping relatedProperty="reciboFreteCarreteiro.controleCarga.filialByIdFilialOrigem.nmFantasia" 
						modelProperty="pessoa.nmFantasia" />
        	
        	<adsm:lookup dataType="integer"
					property="reciboFreteCarreteiro.controleCarga" idProperty="idControleCarga" criteriaProperty="nrControleCarga"
					service="lms.portaria.manterQuilometragensSaidaChegadaAction.findLookupControleCarga"
					action="carregamento/manterControleCargas" size="8" mask="00000000" disabled="true"
					popupLabel="pesquisarControleCarga" >
				<adsm:propertyMapping criteriaProperty="reciboFreteCarreteiro.controleCarga.filialByIdFilialOrigem.idFilial" 
						modelProperty="filialByIdFilialOrigem.idFilial" />
				<adsm:propertyMapping criteriaProperty="reciboFreteCarreteiro.controleCarga.filialByIdFilialOrigem.sgFilial" 
						modelProperty="filialByIdFilialOrigem.sgFilial" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="reciboFreteCarreteiro.controleCarga.filialByIdFilialOrigem.nmFantasia" 
						modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" inlineQuery="false" />
	        </adsm:lookup>
	        <adsm:hidden property="reciboFreteCarreteiro.controleCarga.filialByIdFilialOrigem.nmFantasia" serializable="false" />
        </adsm:lookup>
		
		<adsm:range label="periodoOcorrencia" width="32%" labelWidth="18%" >
			<adsm:textbox dataType="JTDate" property="dtOcorrenciaFreteCarreteiroInicial" />
			<adsm:textbox dataType="JTDate" property="dtOcorrenciaFreteCarreteiroFinal" />
		</adsm:range>

		
		<adsm:combobox property="tpOcorrencia" domain="DM_TIPO_OCORRENCIA_RECIBO_CARRETEIRO" renderOptions="true"
				label="ocorrencia" labelWidth="18%" width="32%" />

		<adsm:buttonBar freeLayout="true" >
			<adsm:findButton callbackProperty="ocorrenciaFreteCarreteiro" />
			<adsm:resetButton />
		</adsm:buttonBar>
		
		<script>
			<!--
				var lms_00013 = "<adsm:label key="LMS-00013"/>";
			//-->
		</script>
		
	</adsm:form>
	
	<adsm:grid property="ocorrenciaFreteCarreteiro" idProperty="idOcorrenciaFreteCarreteiro" 
			unique="true" scrollBars="horizontal" rows="9" gridHeight="190" selectionMode="none"
			service="lms.fretecarreteiroviagem.manterOcorrenciasReciboAction.findPaginatedCustom"
			rowCountService="lms.fretecarreteiroviagem.manterOcorrenciasReciboAction.getRowCountCustom" >
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="recibo" property="reciboFreteCarreteiro.sgFilial" width="65" />
			<adsm:gridColumn title="" property="reciboFreteCarreteiro.nrReciboFreteCarreteiro" width="35" dataType="text" />
		</adsm:gridColumnGroup> 
		
		<adsm:gridColumn title="tipoRecibo" property="tpRecibo" width="130"/>
		
		<adsm:gridColumn title="situacaoRecibo" property="tpSituacaoRecibo" width="130"/>
		
		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA" >
			<adsm:gridColumn title="controleCarga" property="controleCarga.sgFilialOrigem" width="60" />
			<adsm:gridColumn title="" property="controleCarga.nrControleCarga" width="70" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="dataOcorrencia" property="dtOcorrenciaFreteCarreteiro" width="120" dataType="JTDate" />
		<adsm:gridColumn title="ocorrencia" property="tpOcorrencia" width="150"/>
		
		<adsm:gridColumn title="moeda" property="dsMoeda" width="60" />
		<adsm:gridColumn title="valorRecibo" property="reciboFreteCarreteiro.vlBruto" dataType="currency" width="100" />
		<adsm:gridColumn title="valorDesconto" property="vlDesconto" dataType="currency" width="110" />
		<adsm:gridColumn title="valorLiquido" property="reciboFreteCarreteiro.vlLiquido" dataType="currency" width="80" />
		
		<adsm:gridColumn title="descontoCancelado" property="blDescontoCancelado" width="125" renderMode="image-check" />
		
		<adsm:buttonBar/>
	</adsm:grid>
	
</adsm:window>

<script type="text/javascript">
<!--

	document.getElementById("idFilialSessao").masterLink = true;
	document.getElementById("sgFilialSessao").masterLink = true;
	document.getElementById("nmFilialSessao").masterLink = true;

	function pageLoadCallBackCustom_cb(data,error) {
		onPageLoad_cb(data,error);
		findFilialUsuarioLogado();
	}

	function findFilialUsuarioLogado() {
		var sdo = createServiceDataObject("lms.fretecarreteiroviagem.manterOcorrenciasReciboAction.findFilialUsuarioLogado",
				"findFilialUsuarioLogado",undefined);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findFilialUsuarioLogado_cb(data,error) {
		setElementValue("idFilialSessao",getNestedBeanPropertyValue(data,"idFilial"));
		setElementValue("sgFilialSessao",getNestedBeanPropertyValue(data,"sgFilial"));
		setElementValue("nmFilialSessao",getNestedBeanPropertyValue(data,"pessoa.nmFantasia"));
		if (!document.getElementById('reciboFreteCarreteiro.filial.idFilial').masterLink) {
			populateFilial();
		}
	}

	function populateFilial() {
		setElementValue("reciboFreteCarreteiro.filial.idFilial",getElementValue("idFilialSessao"));
		setElementValue("reciboFreteCarreteiro.filial.sgFilial",getElementValue("sgFilialSessao"));
		setElementValue("reciboFreteCarreteiro2.filial.sgFilial",getElementValue("sgFilialSessao"));
		setElementValue("reciboFreteCarreteiro.filial.pessoa.nmFantasia",getElementValue("nmFilialSessao"));
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			if (!document.getElementById('reciboFreteCarreteiro.filial.idFilial').masterLink) {
				populateFilial();
			}
			setDisabled("reciboFreteCarreteiro.controleCarga.idControleCarga",true);
			setDisabled("reciboFreteCarreteiro.idReciboFreteCarreteiro",true);
		}
	} 
	
	function validateTab() {
		if (validateTabScript(document.forms)){
			var rangeOcorrencia = (getElementValue("dtOcorrenciaFreteCarreteiroInicial") != '' && 
					getElementValue("dtOcorrenciaFreteCarreteiroFinal") != '');
			if (!rangeOcorrencia && getElementValue("reciboFreteCarreteiro.nrReciboFreteCarreteiro") == '') {
				alert(lms_00013 +' '+
					document.getElementById("reciboFreteCarreteiro.nrReciboFreteCarreteiro").label + ', ' +
					document.getElementById("dtOcorrenciaFreteCarreteiroInicial").label + '.');
			} else {
				return true;
			}
		}
		return false;		
	}
	
	function filialChange(e) {
		setDisabled("reciboFreteCarreteiro.controleCarga.idControleCarga",true);
		manipulaEstadoRecibo(e.value != "",undefined);
		return reciboFreteCarreteiro_filial_sgFilialOnChangeHandler();
	}
	
	function filialControleCargaChange(e) {
		setDisabled("reciboFreteCarreteiro.controleCarga.idControleCarga",e.value == "");
		return reciboFreteCarreteiro_controleCarga_filialByIdFilialOrigem_sgFilialOnChangeHandler();
	}

	function onNrReciboChange(e) {
		setDisabled("reciboFreteCarreteiro.controleCarga.idControleCarga",true);
		return reciboFreteCarreteiro_nrReciboFreteCarreteiroOnChangeHandler();
	}
	
	function popupRecibo(data) {
		var idControleCarga = getNestedBeanPropertyValue(data,"controleCarga.idControleCarga");
		setDisabled("reciboFreteCarreteiro.controleCarga.idControleCarga",
				idControleCarga == undefined || idControleCarga == "");
	}
	
	function reciboDataLoad_cb(data) {
		var idControleCarga = getNestedBeanPropertyValue(data,":0.controleCarga.idControleCarga");
		setDisabled("reciboFreteCarreteiro.controleCarga.idControleCarga",
				idControleCarga == undefined || idControleCarga == "");
				
		return reciboFreteCarreteiro_nrReciboFreteCarreteiro_exactMatch_cb(data);
	}
	
	function popupFilial(data) {
		setDisabled("reciboFreteCarreteiro.controleCarga.idControleCarga",true);
		manipulaEstadoRecibo(true,undefined);
	}

	
	function tpReciboChange(elem) {
		comboboxChange({e:elem});
		setDisabled("reciboFreteCarreteiro.controleCarga.idControleCarga",true);
		
		manipulaEstadoRecibo(undefined,elem.value != "");
		
		if (elem.value == 'V') {
			document.getElementById("reciboFreteCarreteiro.idReciboFreteCarreteiro").url = 
					contextRoot + "/freteCarreteiroViagem/manterRecibos.do";
		} else if (elem.value == 'C') {
			document.getElementById("reciboFreteCarreteiro.idReciboFreteCarreteiro").url = 
					contextRoot + "/freteCarreteiroColetaEntrega/manterRecibos.do";
		}
	}
	
	function manipulaEstadoRecibo(blFilialFilled,blTipoFilled) {
		if (blFilialFilled == undefined) {
			blFilialFilled = (getElementValue("reciboFreteCarreteiro.filial.idFilial") != "");
			
		}
		if (blTipoFilled == undefined) {
			blTipoFilled = (getElementValue("tpReciboFreteCarreteiro") != "");		
		}
		
		setDisabled("reciboFreteCarreteiro.idReciboFreteCarreteiro",!(blFilialFilled && blTipoFilled));
	}

//-->
</script>
