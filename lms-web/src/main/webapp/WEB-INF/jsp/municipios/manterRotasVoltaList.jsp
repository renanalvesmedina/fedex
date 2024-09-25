<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarRotasVolta">
	<adsm:form action="/municipios/manterRotasVolta">
	    <adsm:lookup service="" dataType="integer" property="filial.id" width="85%" criteriaProperty="filial.codigo" label="rotaVolta" size="4" maxLength="2" action="/municipios/manterFiliais">
                  <adsm:listbox property="" size="3" prototypeValue="PA|CX" optionLabelProperty="2|1"  optionProperty="dd12dsa" service="false" useRowspan="false" required="true" boxWidth="50"/>
        </adsm:lookup>
		<adsm:textbox dataType="text" label="distancia" property="distancia" maxLength="18" size="18" unit="km" width="46%"/>
		<adsm:textbox dataType="currency" label="freteKm" property="freteKm" maxLength="18" size="18" />
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" selectionMode="check" gridHeight="200" unique="true">
		<adsm:gridColumn title="rota" property="dadosFiliaisRota" />
		<adsm:gridColumn title="distancia" property="distancia" width="15%"  />
		<adsm:gridColumn title="freteKm" property="freteKm" width="15%" align="right" />
		<adsm:buttonBar>
			<adsm:button caption="excluir" /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

