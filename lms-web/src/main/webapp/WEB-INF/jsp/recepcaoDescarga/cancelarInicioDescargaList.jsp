<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.recepcaodescarga.cancelarInicioDescargaAction">
	<adsm:form action="/recepcaoDescarga/cancelarInicioDescarga" >

		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="tpAcesso" value="F" />

		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false" />
		<adsm:lookup dataType="text" label="controleCargas" required="true"
					 property="controleCarga.filialByIdFilialOrigem"
				 	 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.recepcaodescarga.cancelarInicioDescargaAction.findLookupFilial" 
					 action="/municipios/manterFiliais" 
				     onchange="return sgFilialOnChangeHandler();" 
					 onDataLoadCallBack="disableNrControleCarga"
					 popupLabel="pesquisarFilial"
					 size="3" maxLength="3" 
					 labelWidth="19%" 
					 width="32%" 
					 picker="false" 
					 serializable="false" >
			<adsm:propertyMapping modelProperty="flagBuscaEmpresaUsuarioLogado" criteriaProperty="flagBuscaEmpresaUsuarioLogado"/> 
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" />
			<adsm:propertyMapping modelProperty="tpAcesso" criteriaProperty="tpAcesso"/>

	 		<adsm:lookup dataType="integer" property="controleCarga" 
	 					 idProperty="idControleCarga" 
	 					 criteriaProperty="nrControleCarga" 
						 service="lms.recepcaodescarga.cancelarInicioDescargaAction.findControleCarga" 
						 action="/carregamento/manterControleCargas" 
						 popupLabel="pesquisarControleCarga"
						 onPopupSetValue="loadDataByIdControleCarga"
						 onDataLoadCallBack="loadDataByIdControleCarga"
						 size="9" 
						 maxLength="8" 
						 mask="00000000" 
						 disabled="true" 
						 serializable="true" >
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial"/>
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial"/>
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" criteriaProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia"/>

				<adsm:propertyMapping modelProperty="tpControleCarga" relatedProperty="tpControleCarga"/>
				<adsm:propertyMapping modelProperty="tpStatusControleCarga" relatedProperty="tpStatusControleCarga"/>
					  
				<adsm:propertyMapping modelProperty="nrFrotaTransporte" relatedProperty="nrFrotaTransporte"/>
				<adsm:propertyMapping modelProperty="nrIdentificadorTransporte" relatedProperty="nrIdentificadorTransporte"/>
				
				<adsm:propertyMapping modelProperty="nrFrotaSemiReboque" relatedProperty="nrFrotaSemiReboque"/>
				<adsm:propertyMapping modelProperty="nrIdentificadorSemiReboque" relatedProperty="nrIdentificadorSemiReboque"/>
					  
				<adsm:propertyMapping modelProperty="chegadaPortaria" relatedProperty="chegadaPortaria"/>
				<adsm:propertyMapping modelProperty="inicioDescarga" relatedProperty="inicioDescarga"/>  

			</adsm:lookup>
		</adsm:lookup>
		
		<adsm:textbox label="tipo" labelWidth="15%" width="34%" disabled="true"
		              property="tpControleCarga"
		              dataType="text"/>
        <adsm:textbox label="status" labelWidth="19%" width="81%" disabled="true"
                      property="tpStatusControleCarga" 
                      dataType="text"/>

		<adsm:textbox label="meioTransporte" labelWidth="19%" width="32%" disabled="true" 
					  property="nrFrotaTransporte" 
					  serializable="false"
					  dataType="text"
					  size="6" 
					  maxLength="6">
			<adsm:textbox dataType="text" property="nrIdentificadorTransporte" size="25" maxLength="25" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox label="semiReboque" labelWidth="15%" width="34%" disabled="true" 
					  property="nrFrotaSemiReboque" serializable="false"
					  dataType="text"
					  size="6" 
					  maxLength="6">
			<adsm:textbox dataType="text" property="nrIdentificadorSemiReboque" size="25" maxLength="25" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox label="chegadaPortaria" labelWidth="19%" width="32%" disabled="true"
		              property="chegadaPortaria" 
		              dataType="JTDateTimeZone"  
		              picker="false"/>
		              
		<adsm:textbox label="inicioDescarga" labelWidth="15%" width="34%" disabled="true" 
		              property="inicioDescarga"
		              dataType="JTDateTimeZone"
                      picker="false"/>

		<adsm:combobox label="motivoCancelamento" labelWidth="19%" width="81%" boxWidth="200"
		               property="motivoCancelDescarga.idMotivoCancelDescarga" 
		               optionLabelProperty="dsMotivo" 
		               optionProperty="idMotivoCancelDescarga"
		               service="lms.recepcaodescarga.cancelarInicioDescargaAction.findComboMotivoCancelamentoDescarga" 
		               onlyActiveValues="true"
		               required="true"
		               />
		               
		<adsm:textarea label="observacoes" 
		               property="obMotivoCancelamentoDescarga" 
		               maxLength="200" 
		               columns="100" 
		               rows="3"
		               labelWidth="19%" 
		               width="85%" />

		<adsm:buttonBar>
			<adsm:button caption="cancelarInicioDescargaBotao" 
			             id="btnCancelarInicioDescarga" 
			             buttonType="storeButton" 
			             service="lms.recepcaodescarga.cancelarInicioDescargaAction.executeCancelarInicioDescarga"
			             callbackProperty="cancelarInicioDescarga"
			             />
			<adsm:resetButton id="btnLimpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript" >
	function initWindow(eventObj) {
		document.getElementById("controleCarga.nrControleCarga").required="true";
		setDisabled("controleCarga.idControleCarga", false);
		setDisabled("controleCarga.nrControleCarga", true);
	}

    /**
     * Função para mostrar a mensagem de sucesso
     */
    function cancelarInicioDescarga_cb(data, error) {
     	if (error != undefined) {
    		alert(error);
    	} else {
    		setElementValue('tpStatusControleCarga', data._value);
    		showSuccessMessage();
    		setFocus("btnLimpar", false);
    	}
   		return false;    	
    }

    /**
     *Função onChange para verificar se pode limpar a tela
     */
    function onChangeControleCarga() {
        var isChanged = controleCarga_nrControleCargaOnChangeHandler();
        
        var nrControleCarga = getElementValue("controleCarga.nrControleCarga");
        
        if(nrControleCarga=="") {
            limpaCampos();
        }
        
        return isChanged;
    }

    function limpaCampos() {
    	cleanButtonScript(this.document)
    }

	/**
	 * Controla o objeto de controle carga
	 */	
	function sgFilialOnChangeHandler() {
		if (getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial")=="") {
			disableNrControleCarga(true);
			resetValue("controleCarga.idControleCarga");
			limpaCampos();
		} else {
			disableNrControleCarga(false);
		}
		return lookupChange({e:document.forms[0].elements["controleCarga.filialByIdFilialOrigem.idFilial"]});
	}
	
	function disableNrControleCarga_cb(data, error) {
		if (data.length==0) {
		    disableNrControleCarga(false);
		}
		return lookupExactMatch({e:document.getElementById("controleCarga.filialByIdFilialOrigem.idFilial"), data:data});
	}
	
	/**
	 * Chama a consulta de 'findCarregamentoDescargaByNrControleCarga' a partir de um dos dados retornados 
	 * da lookup
	 */
	function loadDataByIdControleCarga(data) {
		var map = new Array();
		setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", data.filialByIdFilialOrigem.sgFilial);
		setNestedBeanPropertyValue(map, "filialByIdFilialOrigem.idFilial", data.filialByIdFilialOrigem.idFilial);
		setNestedBeanPropertyValue(map, "idControleCarga", data.idControleCarga);
		disableNrControleCarga(false);
		var sdo = createServiceDataObject("lms.recepcaodescarga.cancelarInicioDescargaAction.findControleCarga", "loadDataByIdControleCarga", map);
    	xmit({serviceDataObjects:[sdo]});
    	
	}
	
	/**
	 * 
	 */
	function loadDataByIdControleCarga_cb(data, error){
		var r = controleCarga_nrControleCarga_exactMatch_cb(data);
		//Verifica se este objeto e nulo
		if (r==true){
			if (data[0]!=undefined) {
	        	var criteria = new Array();
	            // Monta um map
	            setNestedBeanPropertyValue(criteria, "idControleCarga", data[0].idControleCarga);
	        	
	            var sdo = createServiceDataObject("lms.recepcaodescarga.cancelarInicioDescargaAction.findControleCargaById", "resultLoadDataByIdControleCarga", criteria);
	            xmit({serviceDataObjects:[sdo]});
			}
		} else {
			resetValue('controleCarga.filialByIdFilialOrigem.idFilial');
			resetValue('controleCarga.idControleCarga');
			setDisabled('controleCarga.nrControleCarga', true);
			setFocusOnFirstFocusableField(document);
			return false;
		}
		return r;
	}

    function resultLoadDataByIdControleCarga_cb(data, error) {
        if(error!=undefined) {
            alert(error);
            resetValue('controleCarga.nrControleCarga');
            return false;
        }
        setElementValue("tpControleCarga", data.tpControleCarga);
        setElementValue("tpStatusControleCarga", data.tpStatusControleCarga);
        setElementValue("nrFrotaTransporte", data.nrFrotaTransporte);
        setElementValue("nrIdentificadorTransporte", data.nrIdentificadorTransporte);
        setElementValue("nrFrotaSemiReboque", data.nrFrotaSemiReboque);
        setElementValue("nrIdentificadorSemiReboque", data.nrIdentificadorSemiReboque);
       	setElementValue('chegadaPortaria', setFormat(document.getElementById("chegadaPortaria"), data.chegadaPortaria));
       	setElementValue('inicioDescarga', setFormat(document.getElementById("inicioDescarga"), data.inicioDescarga));
		setDisabled("controleCarga.nrControleCarga", false);
    }
	
  	function disableNrControleCarga(disable) {
		setDisabled(document.getElementById("controleCarga.idControleCarga"), disable);
	}
	
</script>