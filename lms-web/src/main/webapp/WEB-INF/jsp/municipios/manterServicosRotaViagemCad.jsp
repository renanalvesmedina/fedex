<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterServicosRotaViagemAction" >
	<adsm:form action="/municipios/manterServicosRotaViagem" idProperty="idServicoRotaViagem" onDataLoadCallBack="servicoRotaLoad" >
		<adsm:hidden property="rotaViagem.idRotaViagem" value="1" />
		<adsm:hidden property="rotaViagem.versao" value="-1" />

		<adsm:textbox dataType="text" label="tipoRota" property="rotaViagem.tipoRota" size="35" width="85%" disabled="true" serializable="false" />

		<adsm:textbox dataType="integer" label="rotaIda" property="rotaIda.nrRota" disabled="true" mask="0000" size="5" serializable="false">
			<adsm:textbox dataType="text" property="rotaIda.dsRota" disabled="true" size="30" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox dataType="integer" label="rotaVolta" property="rotaVolta.nrRota" disabled="true" mask="0000" size="5" serializable="false" >
			<adsm:textbox dataType="text" property="rotaVolta.dsRota" disabled="true" size="30" serializable="false"/>
		</adsm:textbox>

		<adsm:combobox property="servico.idServico" label="servico"
			optionProperty="idServico" optionLabelProperty="dsServico" onlyActiveValues="true"
			service="lms.municipios.manterServicosRotaViagemAction.findServicoCombo" boxWidth="200" required="true"
		>
		</adsm:combobox>

		<adsm:range label="vigencia" width="85%">
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
	 * Retorna estado dos campos como foram carregados na página.
	 */
	function estadoNovo() {
		setDisabled(document,false);
		setDisabled("rotaViagem.tipoRota",true);
		setDisabled("rotaIda.dsRota",true);
		setDisabled("rotaVolta.dsRota",true);
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
	function servicoRotaLoad_cb(data,exception) {
		onDataLoad_cb(data,exception);
		comportamentoDetalhe(data);
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
</script>