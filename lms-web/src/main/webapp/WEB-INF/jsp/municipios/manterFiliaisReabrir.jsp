<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
		function pageLoad_cb() {
			onPageLoad_cb();
			var url = new URL(document.location.href);
			setElementValue("idFilial",url.parameters['id']);
		}

		function storeCallBack_cb(data,exception) {
			if (exception) {
				alert(exception)
				return;
			}
			dialogArguments.window.populaGrid_cb(data);
			window.self.close();
		}

//-->
</script>
<adsm:window service="lms.municipios.manterFiliaisAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/municipios/manterFiliais" idProperty="idFilial">

		<adsm:section caption="reabrirFilial" width="50%"/>

		<adsm:combobox property="tpFilial" label="tipoFilial" domain="DM_TIPO_FILIAL" required="true" width="80%" labelWidth="18%" onlyActiveValues="true" boxWidth="150"/>

        <adsm:textbox dataType="JTDate" property="dtPrevisaoOperacaoInicial" required="true" label="dataPrevistaAbertura" width="80%" labelWidth="18%"/>

		<adsm:buttonBar>
			<adsm:storeButton caption="confirmar" service="lms.municipios.manterFiliaisAction.reabrirFilial" callbackProperty="storeCallBack"/>
			<adsm:button caption="fechar" id="botaoFechar" onclick="self.close();" disabled="false"/>
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>
