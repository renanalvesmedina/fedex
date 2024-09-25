<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/municipios/manterRotasVolta">
	    <adsm:lookup service="" dataType="integer" width="85%" property="filial.id"  criteriaProperty="filial.codigo" label="rotaVolta" size="4" maxLength="2" action="/municipios/manterFiliais">
                  <adsm:listbox property="" size="3" prototypeValue="PA|CX" optionLabelProperty="2|1"  optionProperty="dd12dsa" service="false" useRowspan="false" required="true" boxWidth="50"/>
        </adsm:lookup>
		<adsm:textbox dataType="text" label="distancia" property="distancia" maxLength="18" size="18" required="true" unit="km" width="46%"/>
				<adsm:lookup service="" dataType="integer" property="moedaId"  criteriaProperty="moeda.descricao" label="moeda" size="18" maxLength="2" action="/configuracoes/manterMoedas" />
		<adsm:textbox dataType="currency" label="freteKm" property="freteKm" maxLength="18" size="18" />
		<adsm:textbox dataType="text" label="freteCarreteiro" property="" maxLength="18" size="18" width="63%" disabled="true"/>
		<adsm:textarea property="itinerario" label="itinerario" maxLength="150" rows="2" columns="75" width="63%"/>
		<adsm:textarea property="observacao" label="observacao" maxLength="600" rows="4" columns="75" width="63%"/>
		<adsm:buttonBar>
			<adsm:button caption="trechosRota" action="/municipios/manterTrechosRotaViagem" cmd="main"/>
			<adsm:button caption="salvar"/>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
   