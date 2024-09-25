<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window>
	<adsm:form action="/freteCarreteiroColetaEntrega/simularAplicarReajustesTabelasFreteHistorico">
		<adsm:masterLink>
			<adsm:masterLinkItem property="filial.descricao" label="filial" />
			<adsm:masterLinkItem property="parametroSimulacaoHistorica.descricao" label="descricao" />
			<adsm:masterLinkItem property="parametroSimulacaoHistorica.simulacaoPercentual" label="tipoSimulacao" />
		</adsm:masterLink>
		<adsm:combobox property="tipoParcela" label="tipoParcela" prototypeValue="Diária por período|Diária por horário de corte|Hora excedente|Quilometragem|Peso|Fração de peso" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="30%" required="true"/>
		<adsm:textbox dataType="currency" property="indiceReajuste" maxLength="21" label="reajuste" size="20" labelWidth="20%" width="30%" required="true" />
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="novaParcela"/>
			<adsm:button caption="salvarParcela"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" unique="true" rows="9" >
		<adsm:gridColumn width="22%" title="parcela" property="parcela" />
		<adsm:gridColumn width="12%" title="franquia" property="franquia" align="right" />
		<adsm:gridColumn width="12%" title="valorReajustado" property="valorReajustado" align="right" />
		<adsm:gridColumn width="10%" title="quantidade" property="quantidade" />
		<adsm:gridColumn width="11%" title="valorAtual" property="valorAtual" align="right" />
		<adsm:gridColumn width="11%" title="totalAtual" property="totalAtual" align="right" />
		<adsm:gridColumn width="11%" title="totalReajuste" property="totalReajuste" align="right" />
		<adsm:gridColumn width="11%" title="reajuste" property="indiceReajuste" align="right" />
		<adsm:buttonBar>
			<adsm:button caption="excluirParcela"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>