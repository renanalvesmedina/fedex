<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
//funcao chamada no callback do form e do store
  function zonaOnDataload_cb(data, exception) {
    onDataLoad_cb(data,exception);
    setaDsTpSituacao(data);
  }
  function zonaStoreButton_cb(data, exception) {
    store_cb(data,exception);
    setaDsTpSituacao(data);
  }
  function setaDsTpSituacao(data){
    var dsTpSituacaoZona = getNestedBeanPropertyValue(data,"tpSituacao.value");
    if (dsTpSituacaoZona == 'I'){
    	document.getElementById("dsTpSituacaoZona").value = "Inativo";
    }else{
    	document.getElementById("dsTpSituacaoZona").value = "Ativo";
    }
 }

</script>
<adsm:window service="lms.municipios.zonaService" >
	<adsm:form idProperty="idZona" action="/municipios/manterZonas" onDataLoadCallBack="zonaOnDataload">
		<adsm:hidden property="dsTpSituacaoZona" serializable="false"/>
		<adsm:textbox dataType="text" required="true" property="dsZona" label="descricao" maxLength="60" size="60" width="85%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" required="true" label="situacao" />
		<adsm:buttonBar>
			<adsm:button caption="servico" action="/municipios/manterZonasServicos" cmd="main" >
				<adsm:linkProperty src="idZona,dsZona" target="zona.idZona" disabled="true" />
				<adsm:linkProperty src="dsTpSituacaoZona" target="dsTpSituacaoZona" disabled="true" />
			</adsm:button>
			<adsm:storeButton callbackProperty="zonaStoreButton"/>
			<adsm:newButton/>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>