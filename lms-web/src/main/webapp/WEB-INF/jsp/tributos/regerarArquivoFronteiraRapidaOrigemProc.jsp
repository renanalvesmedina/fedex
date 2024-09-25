<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window>
	<adsm:form action="/tributos/regerarArquivoFronteiraRapidaOrigem">

		<adsm:textbox label="manifestoViagem" labelWidth="20%" property="manifestoViagem" dataType="text" size="5" maxLength="3" width="8%" />
		<adsm:textbox property="manifestoViagem" dataType="text" size="10" maxLength="12" width="11%" />
		<adsm:lookup property="manifestoViagem" dataType="text" style="width:15px" width="20%" action="" service="" required="true" />

		<adsm:buttonBar >
			<adsm:button caption="regerar"/>
		</adsm:buttonBar>
	</adsm:form>

</adsm:window>