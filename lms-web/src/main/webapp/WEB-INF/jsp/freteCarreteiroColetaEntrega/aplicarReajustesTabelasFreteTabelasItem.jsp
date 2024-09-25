<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window>
	<adsm:form action="/freteCarreteiroColetaEntrega/aplicarReajustesTabelasFreteTabelas">
		<adsm:masterLink>
			<adsm:masterLinkItem property="tipoTabela" label="tipoTabela" />
			<adsm:masterLinkItem property="tipoMeioTransporte" label="tipoMeioTransporte" />
		</adsm:masterLink>

		<adsm:combobox property="parcela" label="parcela" service="" optionLabelProperty="" optionProperty="" prototypeValue="Diária por período|Diária por horário de corte|Horário excedente|Quilômetro|Evento|Frete peso|Percentual sobre frete|Percentual sobre valor da mercadoria" width="60%" required="true"/>
		<adsm:lookup service="" dataType="text" property="moeda.id" size="20" criteriaProperty="moeda.codigo" label="moeda" action="/configuracoes/manterMoedas" width="60%"/>
		<adsm:textbox dataType="text" property="reajusteBruto" label="reajusteBruto" size="10" />
		<adsm:textbox dataType="text" property="percentualReajuste" label="percentualReajuste"  size="10"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="novaParcela"/>
			<adsm:button caption="salvarParcela"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" gridHeight="190" showCheckbox="true">
		<adsm:gridColumn title="parcela" property="par" width="40%"/>
		<adsm:gridColumn title="reajusteBruto" property="reajBruto" width="30%" align="right"/>
		<adsm:gridColumn title="percentualReajuste" property="reajPerc" width="30%" align="right"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="excluirParcela"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
