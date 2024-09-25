<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="controleCargasColetaEntregaRota">
	<adsm:form action="/coleta/manterManifestosColeta">	
		<adsm:lookup dataType="text" property="rotaColetaEntrega" label="rotaColetaEntrega" action="/municipios/manterRotaColetaEntrega" cmd="main" criteriaProperty="" service="" required="true" labelWidth="20%" width="80%"/>
	</adsm:form>
	<adsm:grid idProperty="id" property="id" >
		<adsm:gridColumn width="40%" title="controleCarga" property="controleCarga"/>
		<adsm:gridColumn width="30%" title="veiculo" property="veiculo"/>
		<adsm:gridColumn width="30%" title="semiReboque" property="semiReboque"/>
	</adsm:grid>
	<adsm:buttonBar> 
		<adsm:button caption="fechar"/>
	</adsm:buttonBar>
</adsm:window>
