function addXMLRequestCallback(callback){
    var oldSend, i;
    if( XMLHttpRequest.callbacks ) {
        // we've already overridden send() so just add the callback
        XMLHttpRequest.callbacks.push( callback );
    } else {
        // create a callback queue
        XMLHttpRequest.callbacks = [callback];
        // store the native send()
        oldSend = XMLHttpRequest.prototype.send;
        // override the native open()
        var proxied = XMLHttpRequest.prototype.open;
        var OldRequestheader =  XMLHttpRequest.prototype.setRequestHeader;
        XMLHttpRequest.prototype.open = function() {
            var  host_url = "";
            if(arguments[1].indexOf(".js") != -1) {
                host_url = Config.Data.DEFAULT_URL + arguments[1];
            } else {
                host_url = Config.Data.HOST_URL + arguments[1];
                /*Check if service call is to get Tasks*/
                if(host_url.indexOf('Tasks/Get?') >= 0) {
                    /*Apply ribbon filters in request*/
                    if(host_url.indexOf('filter=') >= 0) {
                        host_url = host_url.replace("filter=", "filter=" + GLOBAL_RIBBON_FILTER);
                    } else {
                        host_url += "&" + "filter=" + GLOBAL_RIBBON_FILTER;
                    }
                    /*Apply general filters in request*/
                    if(GLOBAL_GENERAL_FILTERS) {
                        host_url += "&genFilters="+JSON.stringify(GLOBAL_GENERAL_FILTERS);
                    }
                    /*Apply Profile and user id*/
                    host_url += "&profile="+getQueryVariable('profile');
                    host_url += "&user="+getQueryVariable("token");
                }
            }
            arguments[1] = host_url;
            return proxied.apply(this, arguments);
        };
    }
}
// e.g.
addXMLRequestCallback( function( xhr ) {
    //console.log( xhr.responseText ); // (an empty string)
});
addXMLRequestCallback( function( xhr ) {
});
