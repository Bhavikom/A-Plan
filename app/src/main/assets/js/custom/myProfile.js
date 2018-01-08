//General filter window configuration
var MyProfile = {
    isLoaded : false,
    myWindow : null,
    open: function() {
        if(MyProfile.isLoaded) {
            MyProfile.getUserDetail();
            MyProfile.myWindow.show();
            return false;
        }
        MyProfile.myWindow = new Ext.Window({
            title: __('PROFILE'),
            layout:'form',
            width: (Ext.getBody().getViewSize().width < 400) ? Ext.getBody().getViewSize().width * 0.8 : 400,
            closeAction:'close',
            closable: true,
            modal: true,
            target : document.getElementById('buttonId'),
            plain: true,
            /*close: function () {
              console.log("Closed");
            },*/
            listeners: {
                afterRender: function() {
                    MyProfile.isLoaded = true;
                    MyProfile.getUserDetail();
                }
            },
            items: [{
                id: 'userName',
                disabled: true,
                xtype : 'textfield',
                fieldLabel: __('USER_NAME')
            },{
                id: 'password',
                xtype : 'textfield',
                fieldLabel: __('PASSWORD')
            },{
                id: 'fullName',
                xtype : 'textfield',
                fieldLabel: __('FULL_NAME')
            },{
                id: 'emailProfile',
                xtype : 'textfield',
                fieldLabel: __('EMAIL')
            }],
            buttons: [{
                text: __('Cancel'),
                handler: function(){
                    MyProfile.myWindow.close();
                }
            },{
                text: __('Save'),
                handler: function(){
                    MyProfile.saveUserDetail();
                    MyProfile.myWindow.close();
                }
            }],
            buttonAlign: 'center'
        }).show();
    },
    saveUserDetail: function() {
        Ext.MessageBox.show({ msg: 'Saving user detail, please wait...', progressText: 'Loading...', width: 300,
            wait: true, waitConfig: { interval: 100 }
        });
        var settings = Config.Data.RequestheaderSettings;
        settings.url = "Save/UserDetail";
        settings.headers.Authorization = getQueryVariable('token');
        var form = new FormData();
        form.append("OrgId", getQueryVariable('orgId'));
        form.append("FullName", Ext.getCmp('fullName').getValue());
        form.append("UserName", Ext.getCmp('userName').getValue());
        form.append("Email", Ext.getCmp('emailProfile').getValue());
        if(Ext.getCmp('password').getValue().length > 0) {
            form.append("Password", Ext.getCmp('password').getValue());
        }
        form.append("Id", getQueryVariable('token'));
        settings.data = form;
        var ajaxCall = $.ajax(settings).done(function (response) {
            var Statusheaders = ajaxCall.getResponseHeader("x-status");
            if(Statusheaders && Statusheaders > 0) {
                var msg = GET_ERROR_DETAIL(Statusheaders);
                Ext.MessageBox.hide();
                Ext.Msg.alert('Error', msg);
            } else {
                var responseDetail = JSON.parse(response);
                if(responseDetail.Payload) {
                    if(responseDetail.Payload.hasOwnProperty("Users")) {
                        if(responseDetail.Payload.Users.length == 1) {
                            var userDetail = responseDetail.Payload.Users[0];
                            if(userDetail.Id > 0) {
                                Ext.getCmp('userName').setValue(userDetail.UserName);
                                //Ext.getCmp('userName').setValue();
                                Ext.getCmp('fullName').setValue(userDetail.FullName);
                                Ext.getCmp('emailProfile').setValue(userDetail.Email);
                            }
                        }
                    }
                }
                Ext.MessageBox.hide();
            }
        }).fail(function(response){
            Ext.MessageBox.hide();
            var Statusheaders = ajaxCall.getResponseHeader("x-status");
            if(Statusheaders > 0) {
                var msg = GET_ERROR_DETAIL(Statusheaders);
                Ext.Msg.alert('Error', msg);
            }
            var SingleError = {"Id" : GUID(), "ErrorCode": Statusheaders, "Message": response.statusText};
            InsertLog(SingleError);
        });
    },
    getUserDetail: function() {
        Ext.MessageBox.show({ msg: __('FETCHING_USER_DETAIL', " ") + __("PLEASE_WAIT"), progressText: __("LOADING"), width: 300,
             wait: true, waitConfig: { interval: 100 }
        });
        var settings = Config.Data.RequestheaderSettings;
        settings.url = "Get/UserDetail";
        settings.headers.Authorization = getQueryVariable('token');
        var form = new FormData();
        form.append("OrgId", getQueryVariable('orgId'));
        settings.data = form;
        var ajaxCall = $.ajax(settings).done(function (response) {
            var Statusheaders = ajaxCall.getResponseHeader("x-status");
            if(Statusheaders && Statusheaders > 0) {
                var msg = GET_ERROR_DETAIL(Statusheaders);
                Ext.MessageBox.hide();
                Ext.Msg.alert('Error', msg);
            } else {
                var responseDetail = JSON.parse(response);
                if(responseDetail.Payload) {
                    if(responseDetail.Payload.hasOwnProperty("Users")) {
                        if(responseDetail.Payload.Users.length == 1) {
                            var userDetail = responseDetail.Payload.Users[0];
                            if(userDetail.Id > 0) {
                                Ext.getCmp('userName').setValue(userDetail.UserName);
                                //Ext.getCmp('userName').setValue();
                                Ext.getCmp('fullName').setValue(userDetail.FullName);
                                Ext.getCmp('emailProfile').setValue(userDetail.Email);
                            }
                        }
                    }
                }
                Ext.MessageBox.hide();
            }
        }).fail(function(response){
            Ext.MessageBox.hide();
            var Statusheaders = ajaxCall.getResponseHeader("x-status");
            if(Statusheaders > 0) {
                var msg = GET_ERROR_DETAIL(Statusheaders);
                Ext.Msg.alert('Error', msg);
            }
            var SingleError = {"Id" : GUID(), "ErrorCode": Statusheaders, "Message": response.statusText};
            InsertLog(SingleError);
        });
    }
};