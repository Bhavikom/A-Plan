var Config = {};
var URL = window.location.href;

Config.Data = {
    "HOST_URL" :'',
    "DEFAULT_URL" : "",
    "ENV" : (window.location.href.indexOf("localhost") >= 0) ? "local" : "live",
    "RequestheaderSettings" : {
        "async": true,
        "crossDomain": true,
        "url": "Get/Profile",
        "method": "POST",
        "headers": {
            "cache-control": "no-cache",
            "postman-token": "cf4f08c5-fe6d-1e95-93e0-784d7fd9d5c7"
        },
        "processData": false,
        "contentType": false,
        "mimeType": "multipart/form-data",
        "data": null
    },
    "ExpandedIds" : null,
    "EN_DATE_FORMAT" : "y-m-d H:i:s A",
    "DE_DATE_FORMAT" : "m.d.Y H:i:s A"
};
