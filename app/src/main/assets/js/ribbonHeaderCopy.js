var ribbonHeader = [{
    title: 'File',
    items: [{
        xtype: 'form',
        items: [{
            xtype: 'button',
            text: 'Print',
            handler: function() {
                var me = this,
                    tab = me.up('tabpanel').getActiveTab();
                //tab.items.each(function(c){c.disable()});
                /*setTimeout(function(){
                    tab.items.each(function(c){c.enable()});
                },9000);*/
            }
        },
        {
            xtype: 'button',
            text: 'Export to PDF'
        },
        {
            xtype: 'button',
            text: 'Export to MsExcel'
        },
        {
            xtype: 'button',
            text: 'Export to MsProject'
        }
        ]
    }],
    itemId: 'file'
}, {
    title: 'Start',
    itemId: 'start',
    //width: 500,
    items:[{
        width: 150, //Profile section
        items: [
              {
                    xtype: 'button',
                    text: 'Profile',
                    height: 25,
                    iconCls : 'profile-button-image',
                    handler: function() {
                        var me = this,
                            tab = me.up('tabpanel').getActiveTab();
                        //tab.items.each(function(c){c.disable()});
                        /*setTimeout(function(){
                         tab.items.each(function(c){c.enable()});
                         },9000);*/
                    }
                },
            {
                xtype: 'button',
                text: 'Save Settings',
                height: 25,
                iconCls : 'save-setting-button-image',
                handler: function() {
                    var me = this,
                        tab = me.up('tabpanel').getActiveTab();
                    //tab.items.each(function(c){c.disable()});
                    /*setTimeout(function(){
                     tab.items.each(function(c){c.enable()});
                     },9000);*/
                }
            },
            {
                xtype: 'button',
                text: 'DeleteCurrentProfile',
                height: 25,
                iconCls: 'delete-profile-button-image',
                handler: function() {
                    var me = this,
                        tab = me.up('tabpanel').getActiveTab();
                    //tab.items.each(function(c){c.disable()});
                    /*setTimeout(function(){
                     tab.items.each(function(c){c.enable()});
                     },9000);*/
                }
            }

        ]
    },
    {
        width: 150, //Filtesrs
        items: [
            {
                xtype: 'button',
                text: 'Profile',
                height: 25,
                iconCls : 'profile-button-image',
                handler: function() {
                    var me = this,
                        tab = me.up('tabpanel').getActiveTab();
                    //tab.items.each(function(c){c.disable()});
                    /*setTimeout(function(){
                     tab.items.each(function(c){c.enable()});
                     },9000);*/
                }
            },
            {
                xtype: 'button',
                text: 'Save Settings',
                height: 25,
                iconCls : 'save-setting-button-image',
                handler: function() {
                    var me = this,
                        tab = me.up('tabpanel').getActiveTab();
                    //tab.items.each(function(c){c.disable()});
                    /*setTimeout(function(){
                     tab.items.each(function(c){c.enable()});
                     },9000);*/
                }
            },
            {
                xtype: 'button',
                text: 'DeleteCurrentProfile',
                height: 25,
                iconCls: 'delete-profile-button-image',
                handler: function() {
                    var me = this,
                        tab = me.up('tabpanel').getActiveTab();
                    //tab.items.each(function(c){c.disable()});
                    /*setTimeout(function(){
                     tab.items.each(function(c){c.enable()});
                     },9000);*/
                }
            }
        ]
    }]
}, {
    title: 'View',
    html: 'Tickets',
    itemId: 'view'
}, {
    title: 'Help',
    html: 'Tickets',
    itemId: 'help'
}];