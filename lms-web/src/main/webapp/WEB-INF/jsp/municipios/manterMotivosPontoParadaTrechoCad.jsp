<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterMotivosPontoParadaTrechoAction" >
	<adsm:form action="/municipios/manterMotivosPontoParadaTrecho" idProperty="idMotivoParadaPontoTrecho" onDataLoadCallBack="motivosLoad" >
		<adsm:hidden property="pontoParadaTrecho.idPontoParadaTrecho" value="1" />

		<adsm:textbox dataType="integer" label="rota" property="rota.nrRota" disabled="true" mask="0000" size="5" labelWidth="22%" width="32%" serializable="false">
			<adsm:textbox dataType="text" property="rota.dsRota" disabled="true" size="30" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox dataType="text" label="trecho" property="trechoRotaIdaVolta.dsTrechoRotaIdaVolta"
				size="10" disabled="true" labelWidth="22%" width="60%" serializable="false" />
      	<adsm:textbox dataType="text" property="pontoParadaTrecho.pessoa.nmPessoa" label="pontoParadaTrecho"
      			labelWidth="22%" width="28%" size="30" disabled="true" serializable="false" />

	   	<adsm:combobox
	   		label="motivoParada"
	   		property="motivoParada.idMotivoParada"
	   		optionProperty="idMotivoParada"
	   		optionLabelProperty="dsMotivoParada"
	   		service="lms.municipios.manterMotivosPontoParadaTrechoAction.findMotivoParadaCombo"
	   		onlyActiveValues="true"
	   		required="true"
	   		width="35%"
	   		boxWidth="200"
	   	/>

       	<adsm:range label="vigencia" labelWidth="22%" width="45%">
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
<script language="javascript" type="text/javascript">
	/**
	 * Retorna estado dos campos como foram carregados na p�gina.
	 */
	function estadoNovo() {
		setDisabled(document,false);
		setDisabled("rota.dsRota",true);
		setDisabled("trechoRotaIdaVolta.dsTrechoRotaIdaVolta",true);
		setDisabled("pontoParadaTrecho.pessoa.nmPessoa",true);
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
	 * Ao carregar os dados, � tratado o retorno da valida��o de vig�ncia no detalhamento:
	 */
	function motivosLoad_cb(data,exception) {
		onDataLoad_cb(data,exception);
		comportamentoDetalhe(data);
	}

	/**
	 * Ap�s salvar, deve-se carregar o valor da vig�ncia inicial detalhada.
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
</script> 