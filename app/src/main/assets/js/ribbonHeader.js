var ribbonHeader = [/*{
        title: __('FILE'),
        items: [{
            xtype: 'form',
            items: [{
                    xtype: 'button',
                    text: __('PRINT'),
                    handler: function() {
                        var me = this,
                        tab = me.up('tabpanel').getActiveTab();
                    }
                },
                {
                    xtype: 'button',
                    text: __('EXPORT_PDF')
                },
                {
                    xtype: 'button',
                    text: __('EXPORT_TO_MSEXCEL')
                },
                {
                    xtype: 'button',
                    text: __('EXPORT_TO_MSPROJECT')
                }
            ]
        }],
            itemId: 'file'
        },*/
    {
        title: __('START'),
        itemId: 'start',
        cls: "menu-item-container",
        items: [
            {
                xtype: 'container',
                anchor: '100%',
                layout: 'column',
                cls: "startContainer",
                items: [
                    {
                        xtype: 'container',
                        //columnWidth: .28,
                        width: 200,
                        cls: "profileItems",
                        layout: 'anchor',
                        html: "<div class='section-footer-title'>"+__('PROFILE')+"</div>",
                        items: [
                            {
                                anchor: '100%',
                                xtype: 'combo',
                                text: __('PROFILE'),
                                id: "profileSelection",
                                queryMode: 'local',
                                iconCls: "profile-button-image",
                                displayField: 'Name',
                                valueField: 'Id',
                                store: Ext.create('Ext.data.Store', {
                                    id: "allProfilesForCombo",
                                    fields: [{ name: 'Id' }, { name: 'Name'}],
                                    data: null
                                    //myLocalStorage.getAllProfilesFromLocalStorage(getQueryVariable("token"))
                                }),
                                value: getQueryVariable("profile"),
                                cls: "custom-combo display-style top-margin",
                                listeners: {
                                    render: function(c) {
                                        new Ext.ToolTip({
                                            target: c.getEl(),
                                            html: __('PROFILES')
                                        });
                                    },
                                    select: function(combo, record, index) {
                                        var profileId = combo.getValue();
                                        var queries = {};
                                        $.each(document.location.search.substr(1).split('&'), function(c, q) {
                                            var i = q.split('=');
                                            queries[i[0].toString()] = unescape(i[1].toString()); // change escaped characters in actual format
                                        });
                                        queries.profile = profileId;
                                        var webviewer = "";
                                        var control = "";
                                        if (queries.hasOwnProperty("isWebViewer")) {
                                            webviewer = "&isWebViewer=" + queries.isWebViewer;
                                        }
                                        if (queries.hasOwnProperty("Control")) {
                                            control = "&Control=" + queries.Control;
                                        }
                                        location.href = (window.location.origin + window.location.pathname) + "?profile=" + queries.profile + "&token=" + queries.token + "&HOST_URL=" + queries.HOST_URL + "&orgId=" + queries.orgId + webviewer + control + "&Lang=" + queries.Lang;
                                    }
                                }
                            },
                            {
                                //check point
                                anchor: '100%', xtype: 'button', text: __('SAVE_SETTINGS'), cls: "",
                                tooltip: __('TAKE_CHANGES_IN_THE_SETTINGS_OVER_TO_THE_CURRENT_PROFILE'),
                                //" Take changes in the settings over to the current profile",
                                iconCls: "settings-icons",
                                handler: function() {
                                    var form = new FormData();
                                    form.append("Id", getQueryVariable('profile'));
                                    var GeneralFilterData = "";
                                    var RibbonFilterData = "";
                                    if (GLOBAL_GENERAL_FILTERS) {
                                        GeneralFilterData = JSON.stringify(GLOBAL_GENERAL_FILTERS);
                                    }
                                    form.append("GeneralFilter", GeneralFilterData);
                                    if (GLOBAL_RIBBON_FILTER) {
                                        RibbonFilterData = GLOBAL_RIBBON_FILTER;
                                    }
                                    form.append("RibbonFilter", RibbonFilterData);
                                    if (1) {
                                        var responseData = APlan.updateProfile(form, Config.Data.RequestheaderSettings);
                                    }
                                }
                            },
                        /*{ anchor: '100%', xtype: 'button', text: "Save Settings", iconCls: "save-setting-button-image",
                        tooltip: " Use 'Ctrl'+ Click to lock the key Realse of the button with mouse click(withour 'Ctrl')"
                        },*/
                            {anchor: '100%', xtype: 'button',
                                text: __('DELETE_CURRENT_PROFILE'),
                                iconCls: "delete-profile-button-image",
                            tooltip: __('DELETE_CURRENT_PROFILE'),
                            handler: function() {
                                myProfiles.deleteProfile(getQueryVariable('profile'), getQueryVariable("token"));
                            }
                        }
                        ]
                    },
                    {
                        xtype: 'container', //Table
                        cls: "customTextBoxContainerTable center-align",
                        width: 200,
                        //columnWidth:.09,
                        layout: 'anchor',
                        items: [
                            {
                                //Check point
                                anchor: '100%', xtype: 'button',
                                text: __("MANAGE_PROFILE"),
                                cls: "top-margin",
                                tooltip: __('USE_CTRL_CLICK'),
                                iconCls: "settings-icons",
                                handler: function() {
                                    myProfiles.open(APlan.getColsDetail());
                                }
                            },
                            {
                                xtype: 'textfield', anchor: '100%', cls: "top-margin",
                                tooltip: __("SAVE_NEW_PROFILE"),
                                id: "profileName",
                                listeners: {
                                    change: function(f, e) {
                                        var value = Ext.getCmp("profileName").value;
                                        if (value && value.length > 0) {
                                            Ext.getCmp('addNewProfile').enable();
                                        } else {
                                            Ext.getCmp('addNewProfile').disable();
                                        }
                                    }
                                }
                            },
                            {
                                xtype: 'button', anchor: '100%',
                                text: __("ADD_NEW_PROFILE"),
                                iconCls: "newUpdate", cls: "",
                                tooltip: __("ADD_NEW_PROFILE"),
                                disabled: true,
                                id: "addNewProfile",
                                handler: function() {
                                    var value = Ext.getCmp("profileName").value;
                                    if (value && value.length > 0) {
                                        var form = new FormData();
                                        form.append("Name", value);
                                        form.append("Lang", getQueryVariable("Lang"));
                                        Config.Data.RequestheaderSettings.data = form;
                                        Config.Data.RequestheaderSettings.url = "Create/Profile";
                                        Ext.MessageBox.show({
                                            msg: __("CREATING_PROFILE", " ")+ __("PLEASE_WAIT"),
                                            progressText: __("LOADING"),
                                            width: 300,
                                            wait: true,
                                            waitConfig: { interval: 100 }
                                        });
                                        $.ajax(Config.Data.RequestheaderSettings).done(function(response) {
                                            Ext.MessageBox.hide();
                                            Ext.getCmp("profileName").setValue("");
                                            var ResponseData = JSON.parse(response);
                                            if (ResponseData && ResponseData.hasOwnProperty("Payload")) {
                                                if (ResponseData.Payload.hasOwnProperty("Id")) {
                                                    var profileDetail = [];
                                                    profileDetail.push({ "Id": ResponseData.Payload.Id, "Name": ResponseData.Payload.Name });
                                                    Ext.getStore('allProfilesForCombo').loadData(profileDetail, true);
                                                    Ext.create('widget.uxNotification', {
                                                        title: __("PROFILE"),
                                                        width: 300,
                                                        position: 'tr',
                                                        manager: 'instructions',
                                                        cls: 'ux-notification-light',
                                                        iconCls: 'ux-notification-icon-information',
                                                        html: 'Profile saved successfully',
                                                        autoClose: true,
                                                        autoCloseDelay: 5000,
                                                        slideBackDuration: 500,
                                                        slideInAnimation: 'bounceOut',
                                                        slideBackAnimation: 'easeIn'
                                                    }).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        ]
                    },
                    {
                        xtype: 'container', //Date section
                        cls: "date-section border-right center-align",
                        width: 80,
                        //columnWidth:.09,
                        layout: 'anchor',
                        html: "<div class='section-footer-title m-t-12'>"+__("DATE")+"</div>",
                        items: [
                            {
                                xtype: 'button', text: " ", iconCls: "dateOverrun", id: "ShowOverdueFinished",
                                pressed: checkIfGivenRibbonFilterExist("ShowOverdueFinished"),
                                tooltip: __("TOOLTIP_COMPLETION_OVERRUN"),
                                handler: function() {
                                    var setOrUnSetParameter = setBtnClass("ShowOverdueFinished");
                                    SetFiltersDataFromApp("ShowOverdueFinished", null);
                                }
                            },
                            {
                                xtype: 'button', text: " ", iconCls: "taskStarted", id: "ShowStarted",
                                tooltip: __("TOOLTIP_NOT_COMPLETED"),
                                pressed: (checkIfGivenRibbonFilterExist("ShowStarted")),
                                handler: function() {
                                    var setOrUnSetParameter = setBtnClass("ShowStarted");
                                    SetFiltersDataFromApp("ShowStarted", null);
                                }
                            },
                            {
                                xtype: 'button', text: " ", iconCls: "taskCompleted", id: "ShowFinished",
                                tooltip: __("TOOLTIP_COMPLETED"),
                                pressed: (checkIfGivenRibbonFilterExist("ShowFinished")),
                                handler: function() {
                                    var setOrUnSetParameter = setBtnClass("ShowFinished");
                                    SetFiltersDataFromApp("ShowFinished", null);
                                }
                            },

                            {
                                xtype: 'button', text: " ", iconCls: "todayComplete", id: "ShowFinishToday",
                                tooltip: __("TOOLTIP_COMPLETED_TODAY"),
                                pressed: (checkIfGivenRibbonFilterExist("ShowFinishToday")),
                                handler: function() {
                                    var setOrUnSetParameter = setBtnClass("ShowFinishToday");
                                    SetFiltersDataFromApp("ShowFinishToday", null);
                                }
                            },
                            {
                                xtype: 'button', text: " ", iconCls: "taskPending", id: "ShowPending",
                                pressed: (checkIfGivenRibbonFilterExist("ShowPending")),
                                tooltip: __("TOOLTIP_NOT_STARTED"),
                                handler: function() {
                                    var setOrUnSetParameter = setBtnClass("ShowPending");
                                    SetFiltersDataFromApp("ShowPending", null);
                                }
                            },
                            {
                                xtype: 'button', text: " ", iconCls: "taskPassive", id: "ShowPassiv",
                                tooltip: __("TOOLTIP_PASSIVE"),
                                pressed: (checkIfGivenRibbonFilterExist("ShowPassiv")),
                                handler: function() {
                                    var setOrUnSetParameter = setBtnClass("ShowPassiv");
                                    SetFiltersDataFromApp("ShowPassiv", null);
                                }
                            },

                            {
                                xtype: 'button', text: " ", iconCls: "inOneWeekComplete", id: "ShowFinishWeek",
                                tooltip: __("TOOLTIP_COMPLETE_WITHIN_WEEK"),
                                pressed: (checkIfGivenRibbonFilterExist("ShowFinishWeek")),
                                handler: function() {
                                    var setOrUnSetParameter = setBtnClass("ShowFinishWeek");
                                    SetFiltersDataFromApp("ShowFinishWeek", null);
                                }
                            },
                            {
                                xtype: 'button', text: " ", iconCls: "withoutDateTask", id: "ShowNoTime",
                                //tooltip: "Show tasks having no dates",
                                tooltip: __("TOOLTIP_NO_DATE"),
                                pressed: (checkIfGivenRibbonFilterExist("ShowNoTime")),
                                handler: function() {
                                    var setOrUnSetParameter = setBtnClass("ShowNoTime");
                                    SetFiltersDataFromApp("ShowNoTime", null);
                                }
                            },
                            { xtype: 'button', text: " ", iconCls: "taskCancelled", id: "ShowCanceled",
                                tooltip: __("TOOLTIP_CANCELLED"),
                                pressed: (checkIfGivenRibbonFilterExist("ShowCanceled")),
                                handler: function() {
                                    var setOrUnSetParameter = setBtnClass("ShowCanceled");
                                    SetFiltersDataFromApp("ShowCanceled", null);
                                }
                            }
                        ]
                    },
                    {
                        xtype: 'container', //Traffic section
                        cls: "traffic-section border-right center-align",
                        width: 40,
                        //columnWidth:.09,
                        layout: 'anchor',
                        html: "<div class='section-footer-title m-t-12'>"+__("TRAFFIC")+"</div>",
                        items: [
                            {
                                xtype: 'button', text: " ", iconCls: "redStatus", id: "TL_Red",
                                pressed: (checkIfGivenRibbonFilterExist("TL_Red")),
                                tooltip: __("TOOLTIP_RED_STATUS"),
                                handler: function() {
                                    var setOrUnSetParameter = setBtnClass("TL_Red");
                                    SetFiltersDataFromApp("TL_Red", null, setOrUnSetParameter);
                                }
                            },
                            {
                                xtype: 'button', text: " ", iconCls: "yelloStatus", id: "TL_Yellow",
                                pressed: (checkIfGivenRibbonFilterExist("TL_Yellow")),
                                tooltip: __("TOOLTIP_YELLOW_STATUS"),
                                handler: function() {
                                    var setOrUnSetParameter = setBtnClass("TL_Yellow");
                                    SetFiltersDataFromApp("TL_Yellow", null, setOrUnSetParameter);
                                }
                            },
                            {
                                xtype: 'button', text: " ", iconCls: "greenStatus", id: "TL_Green",
                                tooltip: __("TOOLTIP_GREEN_STATUS"),
                                pressed: (checkIfGivenRibbonFilterExist("TL_Green")),
                                handler: function() {
                                    var setOrUnSetParameter = setBtnClass("TL_Green");
                                    SetFiltersDataFromApp("TL_Green", null, setOrUnSetParameter);
                                }
                            }
                        ]
                    },
                    {
                        xtype: 'container', //Status section
                        cls: "status-section border-right center-align",
                        width: 60,
                        html: "<div class='section-footer-title m-t-12'>"+__("STATUS")+"</div>",
                        //columnWidth:.09,
                        layout: 'anchor',
                        items: [
                            { xtype: 'button', text: " ", iconCls: "noticeArrow", id: "ShowArrow",
                                tooltip: __("TOOLTIP_NOTICEARROW"),
                                pressed: (checkIfGivenRibbonFilterExist("ShowArrow")),
                                handler: function() {
                                    var setOrUnSetParameter = setBtnClass("ShowArrow");
                                    SetFiltersDataFromApp("ShowArrow", null, setOrUnSetParameter);
                                }
                            },
                            {
                                xtype: 'button', text: " ", iconCls: "reshow", id: "ShowReminder",
                                tooltip: __("TOOLTIP_RESHOW_DATE"),
                                pressed: (checkIfGivenRibbonFilterExist("ShowReminder")),
                                handler: function() {
                                    var setOrUnSetParameter = setBtnClass("ShowReminder");
                                    SetFiltersDataFromApp("ShowReminder", null, setOrUnSetParameter);
                                }
                            },
                            {
                                xtype: 'button', text: " ", iconCls: "iscriticalTask", id: "ShowCritical",
                                tooltip: __("TOOLTIP_CRITICAL"),
                                pressed: (checkIfGivenRibbonFilterExist("ShowCritical")),
                                handler: function() {
                                    var setOrUnSetParameter = setBtnClass("ShowCritical");
                                    SetFiltersDataFromApp("ShowCritical", null, setOrUnSetParameter);
                                }
                            },
                            {
                                xtype: 'button', text: " ", iconCls: "limitOverrun", id: "ShowOverdueLimit",
                                tooltip: __("TOOLTIP_LIMIT_OVERRUN"),
                                pressed: (checkIfGivenRibbonFilterExist("ShowOverdueLimit")),
                                handler: function() {
                                    var setOrUnSetParameter = setBtnClass("ShowOverdueLimit");
                                    SetFiltersDataFromApp("ShowOverdueLimit", null, setOrUnSetParameter);
                                }
                            },
                            {
                                xtype: 'button', text: " ", iconCls: "isclarificationRequired", id: "ShowQuestion",
                                tooltip: __("TOOLTIP_REQUIRE_CLARIFICATION"),
                                pressed: (checkIfGivenRibbonFilterExist("ShowQuestion")),
                                handler: function() {
                                    var setOrUnSetParameter = setBtnClass("ShowQuestion");
                                    SetFiltersDataFromApp("ShowQuestion", null, setOrUnSetParameter);
                                }
                            },
                        ]
                    },
                    {
                        xtype: 'container', //Open/Close
                        cls: "table-section border-right",
                        width: 110,
                        layout: 'anchor',
                        html: "<div class='section-footer-title'>"+__("OPEN_CLOSE")+"</div>",
                        items: [
                            {
                                xtype: 'button', text: " ", iconCls: "closeAllFolder", id: "closeAllFolder", tooltip: "Collapse All Folder",
                                handler: function() {
                                    var setOrUnSetParameter = setBtnClass("closeAllFolder");
                                    if (Ext.getCmp("closeAllFolder").pressed) {
                                        SetFiltersDataFromApp(null, null, false, true);
                                    } else {
                                        SetFiltersDataFromApp(null, null, false, false);
                                    }
                                }
                            },
                            {
                                xtype: 'button', text: " ", id: "expandAllFolder", iconCls: "expandAllFolder", tooltip: "Expand all folder/projects",
                                handler: function() {
                                    var setOrUnSetParameter = setBtnClass("expandAllFolder");
                                    if (Ext.getCmp("expandAllFolder").pressed) {
                                        SetFiltersDataFromApp(null, null, true);
                                    } else {
                                        SetFiltersDataFromApp(null, null, false);
                                    }
                                }
                            },
                            {
                                xtype: 'combo', width: 50,
                                tooltip: __("EXPAND_BY_LEVEL"),
                                iconCls: "setLevel",
                                store: ['2', '3', '4', '5', '6', '7', '8', '9'],
                                value: "2",
                                autoSelect: true,
                                forceSelection: true,
                                cls: "custom-combo display-style top-margin",
                                matchFieldWidth: false,
                                listConfig: {
                                    listeners: {
                                        beforerender: function(picker) {
                                            picker.minWidth = picker.up('combobox').inputEl.getSize().width + 35;
                                        }
                                    }
                                },
                                listeners: {
                                    render: function(c) {
                                        new Ext.ToolTip({
                                            target: c.getEl(),
                                            html:  __("EXPAND_BY_LEVEL")
                                        });
                                    },
                                    'select': function(t) {
                                        SetFiltersDataFromApp(null, { "Level": t.value });
                                    }
                                }
                            }
                        ]
                    },
                    {
                        xtype: 'container', //Table
                        cls: "table-section border-right",
                        width: 150,
                        //columnWidth:.09,
                        layout: 'anchor',
                        html: "<div class='section-footer-title'>"+__("TABLE")+"</div>",
                        items: [
                            { xtype: 'combobox', anchor: '90%',
                                height: 10,
                                text: " ",
                                tooltip: __("OPTION_RESOLUTION"),
                                id: "Resolution",
                                iconCls: "setLevel",
                                store: __("SELECT_RESOLUTION", null, true),
                                value: myLocalStorage.getResolution(getQueryVariable("token")),
                                autoSelect: true,
                                forceSelection: true,
                                cls: "center-self top-margin custom-combo",
                                listeners: {
                                    'select': function(t) {
                                        myLocalStorage.setResolution(getQueryVariable("token"), t.value);
                                        changeResolution(t.value);
                                    },
                                    render: function(c) {
                                        new Ext.ToolTip({
                                            target: c.getEl(),
                                            html: __("OPTION_RESOLUTION")
                                        });
                                    }
                                }
                            },
                            { xtype: 'button', anchor: '100%',
                                text: __("SHOW_TODAY"),
                                iconCls: "showTodaysTask",
                                tooltip: __("TOOLTIP_SHOW_TODAY"),
                                handler: function() {
                                    ScrollToToday();
                                }
                            }
                        ]
                    },
                    {
                        xtype: 'container', //Table
                        cls: "table-section border-right",
                        width: 200,
                        //columnWidth:.09,
                        html: "<div class='section-footer-title'>"+__("GANTT_CHART")+"</div>",
                        layout: 'anchor',
                        items: [
                            { xtype: 'button', anchor: '100%',
                                text: __("SHOW_RESOURCES"),
                                id: "showresource", iconCls: "showresource",
                                tooltip: __("TOOLTIP_SHOW_RESOURCE"),
                                handler: function() {
                                    setBtnClass("showresource");
                                    if (Ext.getCmp("resourceGV").isVisible()) {
                                        Ext.getCmp("resourceGV").hide();
                                    } else {
                                        Ext.getCmp("resourceGV").show();
                                    }
                                }
                            },
                            {
                                xtype: 'button', anchor: '100%',
                                text: __("GET_LATEST_UPDATE"),
                                iconCls: "newUpdate", cls: "top-margin",
                                tooltip: __("GET_LATEST_UPDATE"),
                                handler: function() {
                                    SetFiltersDataFromApp();
                                    //SetFiltersDataFromApp(null, null, null, null, null, true);
                                }
                            },
                        ]
                    }
                ]
            },
        ]
    },
    {
        title: __("VIEW"),
        itemId: 'view',
        cls: "startContainerContainer",
        items: [
            {
                xtype: 'container',
                anchor: '100%',
                layout: 'column',
                cls: "startContainer",
                items: [
                    {
                        xtype: 'container', //Sorting filters
                        width: 250,
                        cls: "border-right profileItems",
                        layout: 'anchor',
                        items: [
                            {   width: 200,
                                xtype: 'button',
                                text: __("SET_FILTERS"),
                                iconCls: "setFilter", id: "setFilter",
                                tooltip: __("SET_FILTERS"),
                                handler: function() {
                                    GFW.open();
                                }
                            },
                            {   width: 200,
                                xtype: 'button',
                                text: __("FOLDER_FILTER_ON_OFF"),
                                iconCls: "filterOnOff",
                                tooltip: __("FOLDER_FILTER_ON_OFF"),
                                disabled: true, id: "folderFilterOnOff",
                                handler: function() {
                                    includeFolderFilter = false;
                                    SetFiltersDataFromApp(null, GLOBAL_GENERAL_FILTERS, null, null, null);
                                    Ext.getCmp('folderFilterOnOff').disable();
                                    var gridPanel = Ext.ComponentQuery.query("#taskGridPanel")[0];
                                    gridPanel.getSelectionModel().deselectAll();
                                }
                            },
                            {   width: 200,
                                xtype: 'button',
                                text: __("FOLDER_FILTER_ON_OFF"),
                                iconCls: "filterOnOff",
                                tooltip: __("FOLDER_FILTER_ON_OFF"),
                                disabled: true, id: "GeneralFilterOnOff",
                                handler: function() {
                                    includeGeneralFilter = false;
                                    SetFiltersDataFromApp(null, GLOBAL_GENERAL_FILTERS, null, null, null);
                                    Ext.getCmp('GeneralFilterOnOff').disable();
                                    Ext.getCmp("AnyText").setValue("");
                                }
                            }
                        ]
                    },
                /*{
                xtype: 'container', //Gantt-Chart detail
                width: 130,
                cls: " center-align border-right no-right-border left-border-sm",
                layout: 'anchor',
                items: [
                {
                xtype: 'checkbox', anchor: '25%', hideLabel: true, labelSeparator: '', boxLabel: 'Designation', fieldLabel: 'Designation', height: 20, cls: "no-bottom-margin",
                handler: function(checkbox, newValue) {
                var grid = Ext.ComponentQuery.query("#gv")[0];
                if (newValue) {
                grid.leftLabelField.dataIndex = "Name";
                grid.view.refresh();
                }

                     grid.leftLabelField.dataIndex = "StartDate";
                grid.rightLabelField.dataIndex = "EndDate";
                grid.view.refresh();
                }
                },
                { xtype: 'checkbox', anchor: '25%', hideLabel: true, labelSeparator: '', boxLabel: 'Date', fieldLabel: 'Date', height: 20, cls: "no-bottom-margin",
                handler: function(checkbox, newValue) {
                var grid = Ext.ComponentQuery.query("#gv")[0];
                if (newValue) {
                grid.leftLabelField.dataIndex = "Name";
                grid.view.refresh();
                }
                grid.leftLabelField.dataIndex = "StartDate";
                grid.rightLabelField.dataIndex = "EndDate";
                grid.view.refresh();
                }
                },
                ]
                },
                {
                xtype: 'container', //Date section
                cls: "center-align",
                layout: 'anchor',
                items: [
                { xtype: 'checkbox', anchor: '25%', hideLabel: true, labelSeparator: '', boxLabel: 'Week Numbers', fieldLabel: 'Week Numbers', height: 20, cls: "no-bottom-margin",
                handler: function(checkbox, newValue) {
                var grid = Ext.ComponentQuery.query("#gv")[0];
                if (newValue) {
                grid.leftLabelField.dataIndex = "Name";
                grid.view.refresh();
                }
                grid.leftLabelField.dataIndex = "StartDate";
                grid.rightLabelField.dataIndex = "EndDate";
                grid.view.refresh();
                }
                },
                { xtype: 'checkbox', anchor: '25%', hideLabel: true, labelSeparator: '', boxLabel: 'Limits', fieldLabel: 'Limits', height: 20, cls: "no-bottom-margin",
                handler: function(checkbox, newValue) {
                var grid = Ext.ComponentQuery.query("#gv")[0];
                if (newValue) {
                grid.leftLabelField.dataIndex = "Name";
                grid.view.refresh();
                }

                     grid.leftLabelField.dataIndex = "StartDate";
                grid.rightLabelField.dataIndex = "EndDate";
                grid.view.refresh();
                }
                },
                ]
                }*/
                ]
            },
        ]
    }
/*{
title: 'Help',
itemId: 'help',
items: [
{
xtype: 'container',
anchor: '100%',
layout: 'column',
cls: "startContainer",
items: [
{
xtype: 'container', //Profile section
columnWidth: .11,
cls: "border-right profileItems",
layout: 'anchor',
items: [
{ anchor: '100%', xtype: 'button', text: "Contest/Index", iconCls: "contentIndexIcon" },
{ anchor: '100%', xtype: 'button', text: "Find", iconCls: "findIcon" },
]
},
{
xtype: 'container', //Date section
cls: "border-right profileItems",
width: 180,
layout: 'anchor',
items: [
{ xtype: 'button', anchor: '100%', text: "www.braintool.com", iconCls: "webIcon" },
{ xtype: 'button', anchor: '100%', text: "Support", iconCls: "supportIcon" },
{ xtype: 'button', anchor: '100%', text: "Update Available?", iconCls: "updateIcon" },
]
},
]
},
]
}*/
];