<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window>
	<adsm:form action="/tributos/manterArquivoFronteiraRapidaOrigem">

		<adsm:textbox label="manifestoViagem" labelWidth="20%" property="manifestoViagem" dataType="text" size="5" maxLength="3" width="8%" />
		<adsm:textbox property="manifestoViagem" dataType="text" size="10" maxLength="12" width="11%" />
		<adsm:lookup property="manifestoViagem" dataType="text" style="width:15px" width="20%" action="" service="" />

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid paramId="id" paramProperty="id" showCheckbox="false">
		<adsm:gridColumn title="manifestoViagem" property="manifestoViagem" width="34%"/>
		<adsm:gridColumn title="dataEmissao" property="dataEmissao" width="33%"/>
		<adsm:gridColumn title="situacao" property="situacao" width="33%"/>
		<adsm:buttonBar>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>