<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">

	/**
	 * Carrega os dados da tela pai
	 */
	function loadWindowData() {
	
		setDisabled("fechar", false);
	
		//carrega os objetos
		var parentWindow = dialogArguments.window.document;
		setElementValue("idOcorrenciaNaoConformidade", parentWindow.getElementById("idOcorrenciaNaoConformidade").value);
		
		//Segue o fluxo		
		onPageLoad();
		
		var idOcorrenciaNaoConformidadeValue = getElementValue("idOcorrenciaNaoConformidade");		
		var sdo = createServiceDataObject("lms.rnc.exibirDisposicaoAction.findDisposicaoByIdOcorrenciaNaoConformidade", "loadFieldsFromOcorrencia", {idOcorrenciaNaoConformidade:idOcorrenciaNaoConformidadeValue});
		xmit({serviceDataObjects:[sdo]});		
	}
	
	/**
	 * Callback da funcao de loadWindowData.
	 *
	 * @param data dados de retorno
	 * @param error msg de erro
	 */
	function loadFieldsFromOcorrencia_cb(data, error) {		
		if (data.idDisposicao!=undefined){
			setElementValue("idDisposicao", data.idDisposicao);
			setElementValue("idOcorrenciaNaoConformidade", data.idOcorrenciaNaoConformidade);
			setElementValue("dsDisposicao", data.dsDisposicao);
			setElementValue('dhDisposicao', setFormat(document.getElementById("dhDisposicao"), data.dhDisposicao));
			setElementValue("dsMotivo", data.dsMotivo);
			setElementValue("nmUsuario", data.nmUsuario);
			setElementValue("dsCausaNaoConformidade", data.dsCausaNaoConformidade);
			setElementValue("dsCausaNc", data.dsCausaNc);
			acoesCorretivasListboxDef.renderOptions(data);
		}
	}

</script>
<adsm:window service="lms.rnc.registrarDisposicaoAction" onPageLoad="loadWindowData">
	<adsm:form action="/rnc/registrarDisposicao" idProperty="idDisposicao">
	
		<adsm:section caption="disposicao"/>
		<adsm:hidden property="idOcorrenciaNaoConformidadeLocMerc"/>
		
		<adsm:hidden property="idOcorrenciaNaoConformidade"/>
		<adsm:textbox dataType="JTDateTimeZone" property="dhDisposicao" label="dataHoraDisposicao" labelWidth="20%" width="30%" disabled="true" picker="false"/>	
		<adsm:textbox dataType="text" property="dsMotivo" label="motivoDisposicao" labelWidth="20%" width="30%" size="32" disabled="true"/>
		<adsm:textbox dataType="text" property="nmUsuario" label="usuarioResponsavel" size="23" labelWidth="20%" width="30%" disabled="true"/>
		<adsm:textarea property="dsDisposicao" label="descricaoDisposicao" labelWidth="20%" width="80%" columns="108" rows="4" maxLength="500" disabled="true"/>
		<adsm:complement label="causaNaoConformidade" width="30%" labelWidth="20%" separator="branco" >
			<adsm:textbox property="dsCausaNaoConformidade" style="vertical-align:top" dataType="text" disabled="true" maxLength="50" size="40"/>
			<adsm:textarea property="dsCausaNc" maxLength="200" columns="40" rows="3" disabled="true"/>
		</adsm:complement>

		<adsm:listbox property="acoesCorretivas" optionProperty="idAcaoCorretiva" optionLabelProperty="dsAcaoCorretiva" label="acoesCorretivas" labelWidth="17%" width="33%" size="5" boxWidth="201" serializable="false"/>
		<adsm:buttonBar>
			<adsm:button id="fechar" caption="fechar" onclick="returnToParent();"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

	/**
	 * fecha a atual janela
	 */
	function returnToParent(){
		self.close();
	}
	
</script>