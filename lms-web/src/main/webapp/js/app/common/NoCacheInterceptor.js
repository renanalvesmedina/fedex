
var NoCacheInterceptor = function() {
	return {
		request: function (config) {
            var separator = config.url.indexOf("?") === -1 ? "?" : "&";
			if(config.method=="GET" && config.url.indexOf("rest") > -1 && config.url.indexOf("/rest/config/recursoMensagem") == -1){
				config.url = config.url+separator+"noCache=" + new Date().getTime();
			}
			if (config.url.indexOf(contextPath) === 0 && config.url.indexOf(".html") > -1) {
				config.url = config.url+separator+"v=" + chaveVersion;
			}
			return config;
		}
	};
};

            