var ACTIVE_LANG = "DE";
var forGotPasswordWin = new Ext.Window({
    title: __("FORGOT_PASSWORD_SINGLE", null, null, ACTIVE_LANG),
    layout:'form',
    width: 400,
    closeAction: 'close',
    closable: true,
    modal: true,
    target : document.getElementById('buttonId'),
    plain: true,
    listeners: {
        afterRender: function() {

        },
        close: function() {
        }
    },
    items: [{
        id: 'emailProfile',
        xtype : 'textfield',
        fieldLabel: __('EMAIL'),
        listeners: {
            change: function(f, e) {
                var email = Ext.getCmp('emailProfile').value;
                if(loginActions.validateMail(email) == true) {
                    Ext.getCmp('Get_Password').enable();
                } else {
                    Ext.getCmp('Get_Password').disable();
                }
            }
        }
    }],
    buttons: [{
        text: __('Ok'),
        id: 'Get_Password',
        disabled: true,
        handler: function(){
            forGotPasswordWin.close();
            loginActions.forgotPassword(Ext.getCmp('emailProfile').value);
        }
    }],
    buttonAlign: 'center'
});

var loginPanel = new Ext.form.Panel({
    labelWidth: 80,
    frame: true,
    id: 'login_form',
    title: 'A-Plan Login',
    height: 400,
    //width : 425,
    defaultType: 'textfield',
    labelAlign: 'top',
    layout: 'absolute',
    cls: "center-align red-header red-border",
    items: [
        {
            xtype: 'component',
            id: "lang-de",
            autoEl: {
                tag: 'button',
                target: 'A-Plan',
                html: '<img src="resources/images/DE.png" width="20" style="vertical-align: middle">',
            },
            listeners: {
                render: function(c){
                    c.getEl().on({
                        click: function() {
                            ACTIVE_LANG = "DE";
                            $("#lang-de").addClass("active");
                            $("#lang-en").removeClass("active");
                            setLanguage(ACTIVE_LANG);
                        }
                    });
                }
            },
            //x: 200,
            y: 20,
            style: 'margin: 0 auto; right: 50px;background: transparent; border: 0; cursor: pointer;',
            cls: "active active-lang-style"
        },
        {
            xtype: 'component',
            id: "lang-en",
            autoEl: {
                tag: 'button',
                target: 'A-Plan',
                html: '<img src="resources/images/EN.png" width="20" style="vertical-align: middle">',
            },
            listeners: {
                render: function(c){
                    c.getEl().on({
                        click: function() {
                            ACTIVE_LANG = "EN";
                            $("#lang-en").addClass("active");
                            $("#lang-de").removeClass("active");
                            setLanguage(ACTIVE_LANG);
                        }
                    });
                }
            },
            //x: 200,
            y: 20,
            style: 'margin: 0 auto; right: 7px;background: transparent; border: 0; cursor: pointer;',
            cls: "active-lang-style"
        },
        {
            xtype: 'component',
            id: "logo-element",
            autoEl: {
                tag: 'a',
                target: 'A-Plan',
                href: 'http://www.braintool.com',
                html: '<img src="resources/images/APlanLogo.png" width="100">'
            },
            x: 200,
            y: 20,
            style: 'margin: 0 auto;left:30% !important',
        },
        {
            xtype: 'textfield',
            emptyText: __("USER_NAME", null, null, ACTIVE_LANG),
            name: 'loginUsername',
            id: 'login_user',
            x: 125,
            y: 136,
            height: 28,
            width: 250,
            value: '',
            cls: "mobile-device-login-text",
            listeners: {
                change: function(field, value) {
                    if(Ext.getCmp('login_user').value.length > 0 && Ext.getCmp('login_password').value.length > 0 ) {
                        //Ext.get('login_button').setDisabled(false);
                    } else {
                        // Ext.get('login_button').setDisabled(true);
                    }
                }
            }
        },
        {
            xtype: 'textfield',
            emptyText: __("PASSWORD", null, null, ACTIVE_LANG),
            name: 'loginPassword',
            id: 'login_password',
            inputType: 'password',
            height: 28,
            width: 250,
            x: 125,
            y: 176,
            value: "",
            cls : "mobile-device-login-text",
            listeners: {
                change: function(field, value) {
                    if(Ext.getCmp('login_user').value.length > 0 && Ext.getCmp('login_password').value.length > 0 ) {
                        //Ext.get('login_button').setDisabled(false);
                    } else {
                        //Ext.get('login_button').setDisabled(true);
                    }
                }
            }
        },
        {
            xtype: 'button',
            id: 'login_button',
            text: __("LOGIN", null, null, ACTIVE_LANG),
            width: 262,
            height: 33,
            x: 120,
            y: 257,
            formBind: true,
            style: "background-image:url('resources/images/LoginButton.png'); background-color: transparent; border:0;background-size:100%;background-position: 100% 50%;",
            disabled: true,
            cls: "no-hover mobile-device-login-text",
            handler: function() {
                Ext.MessageBox.show({
                    msg: __("PLEASE_WAIT", null, null, ACTIVE_LANG),
                    progressText: __("LOADING", null, null, ACTIVE_LANG),
                    width: 300,
                    wait: true,
                    waitConfig: { interval: 100 }
                });
                loginActions.doLoginToDomain();
            }
        },
        {
            xtype: 'button',
            id: 'forgot_password',
            text: __("FORGOT_PASSWORD", null, null, ACTIVE_LANG),
            width: 262,
            height: 33,
            x: 120,
            y: 300,
            formBind: true,
            cls: "no-hover mobile-device-login-text",
            style: "background-image:url('resources/images/LoginButton.png'); background-color: transparent; border:0;background-size:100%;background-position: 100% 50%;padding-left:25px;",
            disabled: true,
            handler: function() {
                Ext.getCmp('emailProfile').setValue();
                forGotPasswordWin.show();
            }
        },
    ]
});

var loginActions = {
    validateMail: function(email) {
        var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(email);
    },
    forgotPassword: function(email) {
        var settings = Config.Data.RequestheaderSettings;
        settings.url =
            (window.location.origin + window.location.pathname)+ "User/ForgotPassword";
        if(settings.url.indexOf("Control")){
            settings.url = settings.url.replace("Control", "management");
        }
        if(settings.url.indexOf("control")){
            settings.url = settings.url.replace("control", "management");
        }
        var form = new FormData();
        form.append("Email", email);
        form.append("Type", "User");
        settings.data = form;
        Ext.Msg.alert('Success', __("FORGOT_PASSWORD_MAIL_UPDATE", null, null, ACTIVE_LANG));
        var ajaxCall = $.ajax(settings).done(function (response) {
            var Statusheaders = ajaxCall.getResponseHeader("x-status");
            if(Statusheaders && Statusheaders > 0) {
                var msg = GET_ERROR_DETAIL(Statusheaders, ACTIVE_LANG);
                Ext.Msg.alert('Error', msg);
            } else {
                var ResponseData2 = JSON.parse(response);
            }
        }).fail(function(response){
            Ext.MessageBox.hide();
            var Statusheaders = ajaxCall.getResponseHeader("x-status");
            if(Statusheaders > 0) {
                var msg = GET_ERROR_DETAIL(Statusheaders, ACTIVE_LANG);
                Ext.Msg.alert('Error', msg);
            }
            var SingleError = {"Id" : GUID(), "ErrorCode": Statusheaders, "Message": response.statusText};
            InsertLog(SingleError);
        });
    },
    doLoginToDomain: function(ResponseData, settings) {
        var settings = Config.Data.RequestheaderSettings;
        settings.url = (window.location.origin + window.location.pathname)+ "Authenticate";
        if(settings.url.indexOf("Control")){
            settings.url = settings.url.replace("Control", "management");
        }
        if(settings.url.indexOf("control")){
            settings.url = settings.url.replace("control", "management");
        }
        var form = new FormData();
        form.append("UserName", Ext.getCmp('login_user').value);
        form.append("Password", Ext.getCmp('login_password').value);
        form.append("DeviceType", "Web");
        form.append("DeviceToken", null);
        form.append("AppVersion", "1");
        settings.data = form;
        var ajaxCall = $.ajax(settings).done(function (response) {
            var Statusheaders = ajaxCall.getResponseHeader("x-status");
            if(Statusheaders && Statusheaders > 0) {
                var msg = GET_ERROR_DETAIL(Statusheaders, ACTIVE_LANG);
                Ext.MessageBox.hide();
                Ext.Msg.alert('Error', msg);
            } else {
                var ResponseData2 = JSON.parse(response);
                if(ResponseData2 && ResponseData2.Payload) {
                    APlan.loginWindow.hide();
                    Ext.MessageBox.show({
                        msg: __("WE_ARE_SETTING_UP_PROFILE", " ", null, ACTIVE_LANG) +  __("PLEASE_WAIT", null, null, ACTIVE_LANG),
                        progressText: __("LOADING", null, null, ACTIVE_LANG),
                        width: 300,
                        wait: true,
                        waitConfig: { interval: 100 }
                    });

                    loginActions.getAllProfiles(settings, ResponseData2);
                }
            }
        }).fail(function(response){
            Ext.MessageBox.hide();
            if(response.status > 0) {
                Ext.Msg.alert('Error', response.statusText);
            }
            var Statusheaders = ajaxCall.getResponseHeader("x-status");
            if(Statusheaders > 0) {
                var msg = GET_ERROR_DETAIL(Statusheaders, ACTIVE_LANG);
                Ext.Msg.alert('Error', msg);
            }
        });
    },
    getAllProfiles: function(settings, ResponseData) {
        var userId = ResponseData.Payload.LoggedInUser.Id.toString().length;
        //settings.url = settings.url = (window.location.origin + window.location.pathname).replace("Control", "management") + "Get/Profiles";
        settings.url = (window.location.origin + window.location.pathname)+ "Get/Profiles";
        if(settings.url.indexOf("Control")){
            settings.url = settings.url.replace("Control", "management");
        }
        if(settings.url.indexOf("control")){
            settings.url = settings.url.replace("control", "management");
        }
        var e = eval(function(p,a,c,k,e,d){e=function(c){return c};if(!''.replace(/^/,String)){while(c--){d[c]=k[c]||c}k=[function(e){return d[e]}];e=function(){return'\\w+'};c=1};while(c--){if(k[c]){p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c])}}return p}('1.0.2.4+1.0.2.3.6().5+1.0.2.3',7,7,'Payload|ResponseData|LoggedInUser|Id|AccessToken|length|toString'.split('|'),0,{}))
        settings.headers.Authorization = e + eval(function(p,a,c,k,e,d){e=function(c){return c};if(!''.replace(/^/,String)){while(c--){d[c]=k[c]||c}k=[function(e){return d[e]}];e=function(){return'\\w+'};c=1};while(c--){if(k[c]){p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c])}}return p}('1.0(2 3().4()/5)',6,6,'round|Math|new|Date|getTime|1000'.split('|'),0,{}));
        var form = new FormData();
        form.append("Lang", ACTIVE_LANG);
        settings.data = form;
        var ajaxCall = $.ajax(settings).done(function (response) {
            var Statusheaders = ajaxCall.getResponseHeader("x-status");
            if(Statusheaders && Statusheaders > 0) {
                var msg = GET_ERROR_DETAIL(Statusheaders, ACTIVE_LANG);
                Ext.MessageBox.hide();
                Ext.Msg.alert('Error', msg);
            } else {
                Ext.MessageBox.show({
                    msg: __("WE_ARE_SETTING_UP_PROFILE", " ") +  __("PLEASE_WAIT", null, null, ACTIVE_LANG),
                    progressText: __("LOADING", null, null, ACTIVE_LANG),
                    width: 300,
                    wait: true,
                    waitConfig: { interval: 100 }
                });

                var ResponseData2 = JSON.parse(response);
                var profileId = 0;
                if(ResponseData2 && ResponseData2.Payload) {
                    $.each(ResponseData2.Payload, function(item){
                        this.ColsDetail = "";
                        if(this.Lang == ACTIVE_LANG) {
                            if(profileId == 0)
                                profileId = this.Id;
                        }
                    });
                    if(profileId == 0) {
                        profileId = ResponseData2.Payload[0].Id;
                    }
                    var organizationId = ResponseData.Payload.Token + Date.now();
                    Ext.MessageBox.show({
                        msg: __("WE_ARE_SETTING_UP_PROFILE", " ", null, ACTIVE_LANG) +  __("PLEASE_WAIT", null, null, ACTIVE_LANG),
                        progressText: __("LOADING",  null, null, ACTIVE_LANG),
                        width: 300,
                        wait: true,
                        waitConfig: { interval: 100 }
                    });
                    Ext.MessageBox.hide();
                    var urlString = "?profile="+ profileId + "&token=" + e+Date.now() + "&HOST_URL=" + window.location.origin + window.location.pathname.replace("Control/", "management")+ "/" + "&orgId=" + organizationId + "&isWebViewer=1&Control=1"+"&Lang="+ACTIVE_LANG;
                    if(urlString.indexOf("Control/")){
                        urlString = urlString.replace("Control/", "management/");
                    }
                    if(urlString.indexOf("control/")){
                        urlString = urlString.replace("control/", "management/");
                    }
                    if(localStorage) {
                        localStorage.setItem("user", urlString);
                    }
                    location.href = window.location.href + urlString;
                    Ext.MessageBox.hide();
                } else {
                    Ext.MessageBox.hide();
                }
            }
        }).fail(function(response){
            Ext.MessageBox.hide();
            var Statusheaders = ajaxCall.getResponseHeader("x-status");
            if(Statusheaders > 0) {
                var msg = GET_ERROR_DETAIL(Statusheaders, ACTIVE_LANG);
                Ext.Msg.alert('Error', msg);
            }
            var SingleError = {"Id" : GUID(), "ErrorCode": Statusheaders, "Message": response.statusText};
            InsertLog(SingleError);
        });
    }
};
function setLanguage (LANG_CODE) {
    Ext.getCmp('login_password').emptyText = __("PASSWORD", null, null, LANG_CODE);
    Ext.getCmp('login_user').emptyText = __("USER_NAME", null, null, LANG_CODE);
    Ext.getCmp('forgot_password').setText(__("FORGOT_PASSWORD", null, null, LANG_CODE));

    //Apply emptyText change
    Ext.getCmp('login_user').applyEmptyText();
    Ext.getCmp('login_password').applyEmptyText();
}
