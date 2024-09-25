<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window>
	<adsm:form action="/municipios/manterPostosPassagemRotas">
		<adsm:masterLink>
			<adsm:masterLinkItem property="postosPassagem.rota" label="rota" itemWidth="50" />
			<adsm:masterLinkItem property="postosPassagem" label="postosPassagem" itemWidth="50" />
			<adsm:masterLinkItem property="postosPassagem.municipio" label="municipio" itemWidth="100" />
			<adsm:masterLinkItem property="postosPassagem.uf" label="uf" itemWidth="50" />
			<adsm:masterLinkItem property="postosPassagem.pais" label="pais" itemWidth="50" />
		</adsm:masterLink>
		<adsm:combobox property="filial.id" label="filialOrigem" service="" optionLabelProperty="sigla" optionProperty="id" width="33%" required="true" labelWidth="17%"/>
		<adsm:combobox property="filial.id" label="filialDestino" service="" optionLabelProperty="sigla" optionProperty="id" width="33%" required="true" labelWidth="17%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="novoTrecho"/>
			<adsm:button caption="salvarTrecho"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" selectionMode="check" unique="true" rows="10" >
		<adsm:gridColumn title="filialOrigem" property="mu" width="50%" />
		<adsm:gridColumn title="filialDestino" property="ro" width="50%" />
		<adsm:buttonBar>
			<adsm:button caption="excluirTrecho"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>