<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/municipios/consultarMunicipios" height="210">
		<adsm:textbox dataType="text" property="filial" label="rota" maxLength="50" size="35"  width="70%" disabled="true"/>
		<adsm:textbox dataType="text" property="distanciaChao" label="filialOrigem" maxLength="50" size="35"  width="85%" disabled="true"/>
		<adsm:textbox dataType="text" property="distanciaAsfalto" label="filialDestino" maxLength="50" size="35" width="85%" disabled="true"/>
		<adsm:listbox  property="" width="83%" size="3" label="valorPedagio" prototypeValue="Valor por eixo - seg - 11:00 - 12:00 - Truck - 6,00|Valor por eixo - ter - 11:00 - 12:00 - Truck - 12,00" optionLabelProperty="2|1"  optionProperty="dd12dsa" service="false" useRowspan="false"/>
		<adsm:buttonBar/>
	</adsm:form>
</adsm:window>