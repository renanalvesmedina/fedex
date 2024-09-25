<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterTiposVeiculoRotaEventualAction" >
	<adsm:form action="/municipios/manterTiposVeiculoRotaEventual" idProperty="idTipoMeioTranspRotaEvent" onDataLoadCallBack="meioTranspLoad" >
	   	<adsm:hidden property="rotaIdaVolta.idRotaIdaVolta" value="1" />
	   	<adsm:hidden property="rotaIdaVolta.versao" value="1" />
	   	
	    <adsm:textbox dataType="text" label="rota" property="rotaIdaVolta.dsRota"
	    		size="35" disabled="true" labelWidth="20%" width="80%" serializable="false" />
		<adsm:combobox property="tipoMeioTransporte.idTipoMeioTransporte" onchange="onChangeTipoMT(this);"
				optionProperty="idTipoMeioTransporte" optionLabelProperty="dsTipoMeioTransporte"
				service="lms.municipios.manterTiposVeiculoRotaEventualAction.findTipoMeioTransporteCombo" onlyActiveValues="true"
				label="tipoMeioTransporte" labelWidth="20%" width="80%" boxWidth="198" required="true" />
						
		<adsm:hidden property="rotaIdaVolta.idMoedaPais" serializable="true" />
		<adsm:textbox dataType="text" property="rotaIdaVolta.siglaSimbolo" serializable="false"
				size="10" label="moeda" labelWidth="20%" width="80%" disabled="true" />
		
		<adsm:textbox dataType="currency" property="vlPedagio"
				label="valorPedagio" labelWidth="20%" width="30%" style="text-align:right" size="22" />
		
		<adsm:textbox dataType="currency" label="valorFrete" property="vlFrete"
				maxLength="18" size="22" labelWidth="20%" width="30%" required="true" />
		
		<adsm:range label="vigencia" labelWidth="20%" width="80%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
        </adsm:range>
        
		<adsm:buttonBar>
			<adsm:storeButton id="salvar" callbackProperty="afterStore" />
			<adsm:newButton id="novo" />
			<adsm:removeButton id="excluir" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	/**
	 * Retorna estado dos campos como foram carregados na página.
	 */
	function estadoNovo() {
		setDisabled(document,false);
		setDisabled("rotaIdaVolta.dsRota",true);
		setDisabled("vlPedagio",true);
		setDisabled("rotaIdaVolta.siglaSimbolo",true);

		setDisabled("excluir",true);
		setFocusOnFirstFocusableField();
	}
	
	/**
	 * Habilitar campos se o registro estiver vigente.
	 */
	function habilitarCampos() {
		setDisabled("dtVigenciaFinal",false);
		setDisabled("novo",false);
		setDisabled("salvar",false);
	}
	
	/**
	 * Ao carregar os dados, é tratado o retorno da validação de vigência no detalhamento:
	 */
	function meioTranspLoad_cb(data,exception) {
		onDataLoad_cb(data,exception);
				
		comportamentoDetalhe(data);
		
		findValorPegadio();
	}
	
	/**
	 * Após salvar, deve-se carregar o valor da vigência inicial detalhada.
	 */
	function afterStore_cb(data,exception,key){
		store_cb(data,exception,key);	
		if(exception == undefined) {
			comportamentoDetalhe(data);
			setFocusOnNewButton();
		}
	}
	
	function comportamentoDetalhe(data) {
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0) {
			estadoNovo();
			setDisabled("excluir",false);
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(document,true);
			habilitarCampos();
			setFocusOnFirstFocusableField();
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(document,true);
			setDisabled("novo",false);
			setFocusOnNewButton();
		}
	}
	
	/**
	 * Tratamento dos eventos da initWindow.
	 */
	function initWindow(eventObj) {
		if (eventObj.name != "gridRow_click" && eventObj.name != "storeButton")
			estadoNovo();
	}
	
	function onChangeTipoMT(elem) {
		comboboxChange({e:elem});
		findValorPegadio();
	}
	
	function findValorPegadio() {
		var data = new Array();
		setNestedBeanPropertyValue(data,"idTipoMeioTransporte",getElementValue("tipoMeioTransporte.idTipoMeioTransporte"));
		setNestedBeanPropertyValue(data,"idMoedaPais",getElementValue("rotaIdaVolta.idMoedaPais"));
		setNestedBeanPropertyValue(data,"idRotaIdaVolta",getElementValue("rotaIdaVolta.idRotaIdaVolta"));
		
		var sdo = createServiceDataObject("lms.municipios.manterTiposVeiculoRotaEventualAction.findValorPedagio",
				"findValorPegadio",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findValorPegadio_cb(data,error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		if (data != undefined) {
			var vlPedagio = getNestedBeanPropertyValue(data,"vlPedagio");
			setElementValue("vlPedagio",vlPedagio);
		}
	}
	
</script> 