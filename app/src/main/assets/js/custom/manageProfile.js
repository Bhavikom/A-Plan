var myProfiles = {
    tabPanel: null,
    windowTabs: null,
    columns: null,
    allProfiles: null,
    currentProfileColulmns: null,
    singleProfileStore: new Ext.data.Store({
        id: "singleProfileStore",
        fields: [
            { name: 'Id', type: 'string' },
            { name: 'Name', type: 'string' },
            { name: 'Show', type: 'int' },
            { name: 'Width', type: 'int' }
        ]
    }),
    allProfileStore: new Ext.data.Store({
        id: "allProfileStore",
        fields: [
            { name: 'Id', type: 'string' },
            { name: 'Name', type: 'string' },
        ]
    }),
    open: function(ColsDetail) {
        myProfiles.singleProfileStore.loadData(ColsDetail);
        myProfiles.windowTabs = [
            {
                title: __('PROFILES'),
                id: "profilesTab",
                itemId: 'profilesTab',
                cls: "top-margin-big customTextBoxContainer",
                items: [
                    {
                        xtype: 'panel',
                        cls: "no-background",
                        id: "gv2",
                        columnWidth: .5,
                        forceFit: true,
                        autoScroll: true,
                        bodyStyle: 'padding: 10px;',
                        height: 400,
                        items: [
                            {
                                xtype: 'gridpanel',
                                layout: 'fit',
                                autoScroll: true,
                                flex: 1,
                                viewConfig: {
                                    plugins: {
                                        ptype: 'gridviewdragdrop',
                                        dragText: 'Drag and drop to reorganize'
                                    }
                                },
                                id: 'profileColumn',
                                store: myProfiles.singleProfileStore,
                                allowDeselect: true,
                                checkOnly: true,
                                mode: 'multi',
                                /*selModel: Ext.create('Ext.selection.CheckboxModel'),*/
                                plugins: [
                                    Ext.create('Ext.grid.plugin.CellEditing', {
                                        clicksToEdit: 1
                                    })
                                ],
                                columns: [
                                    {
                                        flex: 1,
                                        dataIndex: 'Name',
                                        header: __('NAME'),
                                        sortable: false,
                                        editor: {
                                            xtype: 'textfield',
                                            allowBlank: false
                                        }
                                    },
                                    {
                                        header: __("VISIBLE", "?"),
                                        dataIndex: 'Show',
                                        checkOnly: true,
                                        renderer: function(value, meta, record) {
                                            return '<center><input id="' + record.get('Id') + '-id" type="checkbox" ' + (value ? 'checked' : '') + ' onclick="myProfiles.changeShowHideCol(\'' + record.get('Id') + '\')"';
                                            //onclick="changeShowHideCol(\'' + record.get('Id') + '\')>';
                                            //return '<center><input type="checkbox" '+ (value ? 'checked' : '') + ' onclick="var s = Ext.getCmp(\'profileColumn\').store; s.getAt(s.findExact(\'Id\',\'' + record.get('Id') + '\')).set(\'Show\', this.value)"';

                                        }
                                    },
                                /*{
                                xtype: 'checkcolumn',
                                header: 'Visible?',
                                dataIndex: 'Show', // model property to bind to
                                sortable: false,
                                width: 100
                                */
                                /*editor: {
                                xtype: 'checkbox',
                                cls: 'x-grid-checkheader-editor'
                                }*/
                                /*
                                },*/
                                    {
                                    flex: 1,
                                    dataIndex: 'Width',
                                    header: __("WIDTH"),
                                    sortable: false,
                                    editor: {
                                        xtype: 'textfield',
                                        allowBlank: false
                                    }
                                }
                                ]
                            }
                        ]
                    }
                ]
            }
        ];
        myProfiles.tabPanel = new Ext.TabPanel({
            activeTab: 0,
            id: 'myTPanel',
            enableTabScroll: true,
            cls: "",
            items: myProfiles.windowTabs,
            layout: 'anchor',
            listeners: {
                'tabchange': function(tabPanel, tab) {
                    //console.log(tabPanel.id + ' ' + tab.id);

                }
            }
        });
        if (Ext.getCmp("myWindow")) {
            Ext.getCmp("myWindow").destroy();
        }
        var win = new Ext.Window({
            id: 'myWindow2',
            title: __("MANAGE_PROFILE"),
            closable: true,
            modal: true,
            autoScroll: false,
            width: (Ext.getBody().getViewSize().width < 600) ? Ext.getBody().getViewSize().width * 0.8 : 600,
            height: 600,
            items: myProfiles.tabPanel,
            layout: 'anchor',
            maximizable: true,
            constrainHeader: true,
            listeners: {
                afterRender: function() {
                    /*if(myProfiles.allProfiles == null) {
                    myProfiles.getAllProfiles(getQueryVariable("token"));
                    }
                    if(myProfiles.columns == null) {
                    //myProfiles.getColumns();
                    }*/
                },
                beforeRender: function() {

                }
            },
            dockedItems: [
                {
                    xtype: 'toolbar',
                    flex: 1,
                    dock: 'bottom',
                    ui: 'footer',
                    layout: {
                        pack: 'end',
                        type: 'hbox'
                    },
                    items: [
                        {
                            xtype: 'button',
                            text: __("Cancel"),
                            itemId: 'cancel',
                            cls: "center-align",
                            handler: function() {
                                if (myProfiles.checkIfProfileChanged()) {
                                    Ext.Msg.confirm("Confirmation", "Do you want to Save changes?", function(btnText) {
                                        if (btnText === "no") {
                                            win.destroy();
                                        }
                                        else if (btnText === "yes") {
                                            myProfiles.saveProfile(win);
                                        }
                                    }, this);
                                }
                                win.destroy();
                            }
                        },
                        {
                            xtype: 'button',
                            text: __('Ok'),
                            itemId: 'save',
                            cls: "center-align",
                            handler: function() {
                                win.destroy();
                                if (myProfiles.checkIfProfileChanged()) {
                                    myProfiles.saveProfile(win);
                                }
                            }
                        }
                    ]
                }
            ]
        });
        win.show();
    },
    saveProfile: function(win) {
        var colsDetails = [];
        myProfiles.singleProfileStore.each(function(updatedRec) {
            colsDetails.push({ "Id": updatedRec.data.Id, "Name": updatedRec.data.Name, "Show": updatedRec.data.Show, "Width": updatedRec.data.Width });
        });
        var form = new FormData();
        form.append("Id", getQueryVariable('profile'));
        form.append("Columns", JSON.stringify(colsDetails));
        Config.Data.RequestheaderSettings.data = form;
        Config.Data.RequestheaderSettings.url = "Update/Profile";
        $.ajax(Config.Data.RequestheaderSettings).done(function(response) {
            win.destroy();
            var ResponseData = JSON.parse(response);
            window.location.reload();
        });
    },
    checkIfProfileChanged: function() {
        var reloadFlag = false;
        var GanttChartColsDetail = APlan.getColsDetail();
        var updatedStoreColumns = [];
        myProfiles.singleProfileStore.each(function(updatedRec) {
            updatedStoreColumns.push(updatedRec);
        });
        for (var i = 0; i < GanttChartColsDetail.length; i++) {
            var oldRec = GanttChartColsDetail[i];
            if (!reloadFlag && oldRec.Id != "BlankId") {
                if (oldRec.Name != updatedStoreColumns[i].data.Name || oldRec.Width != updatedStoreColumns[i].data.Width || oldRec.Show != updatedStoreColumns[i].data.Show) {
                    console.log(oldRec);
                    console.log(updatedStoreColumns[i].data);
                    reloadFlag = true;
                }
            }
        }
        return reloadFlag;
    },
    setAllProfileStoreData: function(storeData) {
        //myProfiles.allProfileStore.loadData(storeData);
        var ActivePorfileId = myLocalStorage.getActiveProfile(getQueryVariable("token"));
        myProfiles.setProfileColumns(ActivePorfileId);
    },
    setProfileColumns: function(profileId) {
        //var ActivePorfileId = myLocalStorage.getActiveProfile(getQueryVariable("token"))
        if (myProfiles.allProfiles && myProfiles.allProfiles.length > 0) {
            var storeDataToAdd = [];
            Ext.each(myProfiles.allProfiles, function(item) {
                if (item.Id == profileId) {
                    var dataColumns = Ext.decode(item.ColsDetail);
                    Ext.each(dataColumns, function(item2) {
                        if (item2.Name) {
                            storeDataToAdd.push(item2);
                        }
                    });
                }
            });
            myProfiles.currentProfileColulmns = storeDataToAdd;
            myProfiles.singleProfileStore.loadData(storeDataToAdd);
        }
    },
    changeShowHideCol: function(colId) {
        myProfiles.singleProfileStore.each(function(updatedRec) {
            if (colId == updatedRec.data.Id) {
                //Column detail has been changed please reload control
                if (document.getElementById(colId + '-id').checked) {
                    updatedRec.data.Show = 1;
                } else {
                    updatedRec.data.Show = 0;
                }
            }
        });
    },
    deleteProfile: function(profileId, userId) {
        var colsDetails = [];
        var form = new FormData();
        form.append("Id", profileId);
        form.append("UserId", userId);
        Config.Data.RequestheaderSettings.data = form;
        Config.Data.RequestheaderSettings.url = "Delete/Profile";
        $.ajax(Config.Data.RequestheaderSettings).done(function(response) {
            var ResponseData = null;
            if (response) {
                ResponseData = JSON.parse(response);
            }
            if (ResponseData && ResponseData.Payload) {
                APlan.allProfiles.forEach(function(item) {
                    //console.log(item);
                    if (item.Id != profileId) {
                        var queries = {};
                        $.each(document.location.search.substr(1).split('&'), function(c, q) {
                            var i = q.split('=');
                            queries[i[0].toString()] = unescape(i[1].toString()); // change escaped characters in actual format
                        });
                        queries.profile = item.Id;
                        myLocalStorage.setActiveProfile(queries.userId, item.Id);
                        location.href = (window.location.origin + window.location.pathname) + "?profile=" + queries.profile + "&userId=" + queries.userId + "&HOST_URL=" + queries.HOST_URL;
                    }
                });
            } else {
                Ext.Msg.alert('Error', "Can not delete default profile.");
            }
            //window.location.reload();
        });
    }
};