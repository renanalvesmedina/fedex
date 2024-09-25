<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window>
	<adsm:form action="/freteCarreteiroColetaEntrega/aplicarReajustesTabelasFreteTabelas">
		<adsm:combobox property="tipoTabela" label="tipoTabela" service="" optionLabelProperty="" optionProperty="" prototypeValue="" labelWidth="20%" width="60%"/>
		<adsm:combobox property="tipoMeioTransporte" label="tipoMeioTransporte" service="" optionLabelProperty="" optionProperty="" prototypeValue="" labelWidth="20%" width="60%"/>

		<adsm:textbox dataType="date" property="vigenciaReajuste" label="vigenciaReajuste" width="50%" labelWidth="20%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" gridHeight="200" showCheckbox="true">
		<adsm:gridColumn title="tipoTabela" property="tpTab" width="30%"/>
		<adsm:gridColumn title="tipoMeioTransporte" property="tpMT" width="40%"/>
		<adsm:gridColumn title="vigencia" property="vig" width="30%" align="center"/>

		<adsm:buttonBar>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
