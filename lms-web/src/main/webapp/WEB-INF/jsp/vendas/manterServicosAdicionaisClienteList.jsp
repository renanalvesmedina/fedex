<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterServicosAdicionaisClienteAction" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterServicosAdicionaisCliente">
		<adsm:hidden property="tabelaDivisaoCliente.idTabelaDivisaoCliente"/>
		<adsm:hidden property="sgMoeda"/>
		<adsm:hidden property="dsSimbolo"/>
		
		<adsm:complement label="cliente" labelWidth="15%" width="40%">
			<adsm:textbox property="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao" dataType="text" size="20" disabled="true" serializable="false"/>
			<adsm:textbox property="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa" dataType="text" size="28" disabled="true" serializable="false"/>
		</adsm:complement>
		<adsm:textbox labelWidth="15%" width="30%" label="divisao" property="tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente" dataType="text" size="30" disabled="true" serializable="false"/>

		<adsm:complement label="tabelaBase" labelWidth="15%" width="40%">
			<adsm:hidden property="tabelaDivisaoCliente.tabelaPreco.idTabelaPreco" value="0" serializable="false"/>
			<adsm:textbox property="tabelaDivisaoCliente.tabelaPreco.tabelaPrecoString" dataType="text" size="10" disabled="true" serializable="false"/>
			<adsm:textbox property="tabelaDivisaoCliente.tabelaPreco.dsDescricao" dataType="text" size="30" disabled="true" serializable="false"/>
		</adsm:complement>
		<adsm:textbox labelWidth="15%" width="30%" label="servico" property="tabelaDivisaoCliente.servico.dsServico" dataType="text" size="30" disabled="true" serializable="false"/>

		<adsm:textbox labelWidth="15%" width="40%" label="moeda" property="tabelaDivisaoCliente.tabelaPreco.moeda.sgMoeda.siglaSimbolo" dataType="text" size="9" disabled="true" serializable="false"/>
		<adsm:combobox labelWidth="15%" width="30%" label="servicoAdicional" property="parcelaPreco.idParcelaPreco" service="lms.vendas.manterServicosAdicionaisClienteAction.findTabelaPrecoParcelaCombo" optionProperty="parcelaPreco.idParcelaPreco" optionLabelProperty="parcelaPreco.nmParcelaPreco" boxWidth="200" autoLoad="true">
			<adsm:hidden property="parcelaPreco.tpParcelaPreco" value="S" serializable="false"/>
			<adsm:propertyMapping criteriaProperty="tabelaDivisaoCliente.tabelaPreco.idTabelaPreco" modelProperty="tabelaPreco.idTabelaPreco"/>
			<adsm:propertyMapping criteriaProperty="parcelaPreco.tpParcelaPreco" modelProperty="parcelaPreco.tpParcelaPreco"/>
		</adsm:combobox>

		<adsm:combobox labelWidth="15%" width="40%" label="indicador" property="tpIndicador" domain="DM_INDICADOR_PARAMETRO_CLIENTE"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="servicoAdicionalCliente"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="servicoAdicionalCliente" idProperty="idServicoAdicionalCliente" defaultOrder="parcelaPreco_.nmParcelaPreco" unique="true" gridHeight="220" rows="11">
		<adsm:gridColumn title="numeroProposta" property="idProposta" width="15%" align="right" />
		<adsm:gridColumn title="servicoAdicional" property="parcelaPreco.nmParcelaPreco" width="50%"/>
		<adsm:gridColumn title="indicador" property="tpIndicador" width="10%" isDomain="true"/>
		<adsm:gridColumn title="valorIndicador" property="vlValorFormatado" width="25%" align="right"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script language="javascript" type="text/javascript">
function myPageLoad_cb() {
   onPageLoad_cb();
   notifyElementListeners({e:document.getElementById("parcelaPreco.tpParcelaPreco")});
   //setElementValue("tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo",getElementValue("sgMoeda")+' '+getElementValue("dsSimbolo"));
}

function initWindow(eventObj) {
	var tabGroup = getTabGroup(this.document);		
	tabGroup.setDisabledTab("destinatarios", true);
		
	if (eventObj.name == "cleanButton_click") {
		notifyElementListeners({e:document.getElementById("parcelaPreco.tpParcelaPreco")});
	}
}
</script>
