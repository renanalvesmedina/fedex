<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contratacaoveiculos.regraLiberacaoReguladoraService">
	<adsm:form action="/contratacaoVeiculos/manterValidadeLiberacaoReguladoraTipoVinculo" idProperty="idRegraLiberacaoReguladora"
			service="lms.contratacaoveiculos.regraLiberacaoReguladoraService.findByIdDetalhamento" onDataLoadCallBack="reguladoraLoad">
	   <adsm:combobox property="reguladoraSeguro.idReguladora" label="reguladora" boxWidth="351" service="lms.seguros.reguladoraSeguroService.find" optionLabelProperty="pessoa.nmPessoa" optionProperty="idReguladora" onlyActiveValues="true" labelWidth="20%" width="80%" required="true" >
       		<adsm:propertyMapping relatedProperty="reguladoraSeguro.nmServicoLiberacaoPrestado" modelProperty="nmServicoLiberacaoPrestado"/>
       </adsm:combobox>
              
       <adsm:textbox dataType="text" property="reguladoraSeguro.nmServicoLiberacaoPrestado" label="servicoLiberacao" size="66" disabled="true" labelWidth="20%" width="80%"/>
	   <adsm:combobox  property="tpVinculo" label="tipoVinculo" domain="DM_TIPO_VINCULO_CARRETEIRO" required="true" onchange="onSelectType();" labelWidth="20%" width="30%" />
	   <adsm:textbox dataType="integer" property="qtMesesValidade" label="validade" unit="meses" maxValue="99" minValue="1" maxLength="2" size="2" labelWidth="25%" width="25%"/>
	   <adsm:checkbox onclick="validaTpVinculo()" property="blLiberacaoPorViagem" label="liberacaoViagem"  cellStyle="vertical-align:bottom;"  labelWidth="20%" width="30%" />	
       <adsm:textbox dataType="integer" property="qtViagensAnoLiberacao" minValue="1" size="6" maxValue="999999" maxLength="6" label="quantidadeViagensAnoLiberacaoAutomatica" cellStyle="vertical-align:bottom;" labelWidth="25%" width="25%" />
       <adsm:range label="vigencia" width="45%" labelWidth="20%" >
             <adsm:textbox dataType="JTDate" required="true" property="dtVigenciaInicial" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>
		<adsm:buttonBar>
			<adsm:storeButton callbackProperty="afterStore" service="lms.contratacaoveiculos.regraLiberacaoReguladoraService.storeMap" />
			<adsm:newButton id="__botaoNovo" />
			<adsm:removeButton  />
		</adsm:buttonBar>
		<script language="javascript">
		    function validaTpVinculo(){
		    	var selectedType = getElementValue("tpVinculo");
		    	var libViagem = document.getElementById("blLiberacaoPorViagem");
		    	if(selectedType == "E" )
		    	   document.getElementById("qtMesesValidade").value = "";
		    	   if(libViagem.checked == true)
		    		  setDisabled("qtMesesValidade", true); 
		    	    else setDisabled("qtMesesValidade", false); 	  
		    }
		
			function onSelectType() {
				var selectedType = getElementValue("tpVinculo");
				var qtViagemAno = document.getElementById("qtViagensAnoLiberacao");
				var libViagem = document.getElementById("blLiberacaoPorViagem");
				if (selectedType == 'E') {
					setDisabled(libViagem,false);
					setDisabled(qtViagemAno,false);
					return;
				}
				else
				{
					libViagem.checked = false;
					setDisabled(libViagem,true);
					qtViagemAno.value = "";
					setDisabled(qtViagemAno,true);
					setDisabled("qtMesesValidade", false);
				}			
		 }
		 
		 	/**
	 * Retorna estado dos campos como foram carregados na página.
	 */
	function estadoNovo() {
		setDisabled(document,false);
		setDisabled("reguladoraSeguro.nmServicoLiberacaoPrestado",true);
		setDisabled("__buttonBar:0.removeButton",true);
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
	function reguladoraLoad_cb(data,exception) {
		onDataLoad_cb(data,exception);
		comportamentoDetalhe(data);
	}
	
	function comportamentoDetalhe(data) {
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0) {
			estadoNovo();
			setDisabled("__buttonBar:0.removeButton",false);
			onSelectType();
			setFocusOnFirstFocusableField();
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(document,true);
			setDisabled("__botaoNovo",false);
			setDisabled("__buttonBar:0.storeButton",false);
			habilitarCampos();
			setFocusOnFirstFocusableField();
			setDisabled("__buttonBar:0.removeButton",true);
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(document,true);
			setDisabled("__botaoNovo",false);
			setDisabled("__buttonBar:0.removeButton",true);
			setFocusOnNewButton();
		}
		
	}
	
	function afterStore_cb(data,exception,key) {
    	store_cb(data,exception,key);
		if (exception == undefined) {
			comportamentoDetalhe(data);
			setFocusOnNewButton();
		}		
    }

	function initWindow(eventObj) {
		if (eventObj.name != "gridRow_click" && eventObj.name != "storeButton"){
			estadoNovo();
			setFocusOnFirstFocusableField();
		}
		
	}
		</script>
	</adsm:form>
</adsm:window>   