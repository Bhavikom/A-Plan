//General filter window configuration
var GFW = {
    groupStore : null,
    windowTabs : null,
    tabPanel   : null,
    isDeleteGeneralButtonPressed: false,
    clientStoreResourceStoreResponsibleStore: {
        clientStore: null,
        resourceStore: null,
        responsibleStore: null,
        groupStore: null
    },
    generalFilterAllComponents: [
        { elementId: "AnyText" },
        { elementId: "PrioFrom" },
        { elementId: "PrioTo" },
        { elementId: "Level" },
        { elementId: "IndentNo" },
        { elementId: "TaskName" },
        { elementId: "ClientId" },
        { elementId: "ResponsiblePersonId" },
        { elementId: "ResourceGroups" },
        { elementId: "ResourceId" },
        { elementId: "ChangeBy" },
        //{ elementId: "compareBtn", field: "SearchType", tab: "General filter text" },
        { elementId: "ShowAllSubTask1", field: "ShowAllSubTask" },
        /*Date filter tab */
        {elementId: "Criterion1" },
        { elementId: "Criterion1Type" },
        { elementId: "Criterion1TimeFrom" },
        { elementId: "Criterion1TimeTo" },
        { elementId: "Criterion1FromDate" },
        { elementId: "Criterion1ToDate" },
        { elementId: "Criterion1TimeUnit" },

        { elementId: "Criterion2" },
        { elementId: "Criterion2Type" },
        { elementId: "Criterion2TimeFrom" },
        { elementId: "Criterion2TimeTo" },
        { elementId: "Criterion2FromDate" },
        { elementId: "Criterion2ToDate" },
        { elementId: "Criterion2TimeUnit" },

        //General filter user define
        //{elementId: "displayFieldType", field: "displayFieldType", tab: "General filter text" },
        { elementId: "Text1" },
        { elementId: "Text2" },
        { elementId: "Text3" },
        { elementId: "Text4" },
        { elementId: "Text5" },
        { elementId: "Text6" },
        { elementId: "Text7" },
        { elementId: "Text8" },
        { elementId: "Text9" },
        { elementId: "Text10" },
        { elementId: "Number1" },
        { elementId: "Number2" },
        { elementId: "Number3" },
        { elementId: "Number4" },
        { elementId: "Number5" },
        { elementId: "Number6" },
        { elementId: "Number7" },
        { elementId: "Number8" },
        { elementId: "Number9" },
        { elementId: "Number10" },
        { elementId: "Number10" }
    ],
    open: function() {
        if(Ext.getCmp("myWindow")) { //Window has already open please just show it
            Ext.getCmp("myWindow").show();
            if (GLOBAL_GENERAL_FILTERS) {
                GFW.initialisePreviousValues(GLOBAL_GENERAL_FILTERS);
            }
            GFW.setSelectedResourceGroups();
            GFW.setSelectedGroups();
            return;
        }
        var resourceFolder = new Ext.data.Store({
            proxy: {
                type: 'ajax',
                url: 'Resource/Get',
                reader: {
                    type: 'json',
                    root: 'resources'
                }
            },
            autoLoad: true,
            listeners: {
                beforeload: function(){
                    Ext.MessageBox.show({
                        msg: __("LOADING_RESOURCES", " ") + __("PLEASE_WAIT"),
                        progressText: __("LOADING"),
                        width: 300,
                        wait: true,
                        waitConfig: { interval: 100 }
                    });
                },
                load: function() {
                    Ext.MessageBox.hide();
                    //Fetch all the saved filters, And restore all the element in window in the previous state.
                    GFW.setSelectedResourceGroups();
                }
            }
        });

        var groupStore = new Ext.data.Store({
            id: "grpStore",
            proxy: {
                type: 'ajax',
                url: 'Tasks/Get' + '?noFilter=false',
                reader: {
                    type: 'json',
                    root: 'tasks'
                }
            },
            autoLoad: true,
            listeners: {
                beforeload: function(){
                    Ext.MessageBox.show({
                        msg: __("LOADING_GROUPS", " ") + __("PLEASE_WAIT"),
                        progressText: __("LOADING"),
                        width: 300,
                        wait: true,
                        waitConfig: { interval: 100 }
                    });
                },
                load: function() {
                    Ext.MessageBox.hide();
                    //Fetch all the saved filters, And restore all the element in window in the previous state.
                    GFW.setSelectedGroups();
                }
            }
        });

        GFW.clientStoreResourceStoreResponsibleStore.clientStore = new Ext.data.JsonStore({
            fields: [{name: 'Id', type: 'string'}, {name: 'Name',  type: 'string'} ]
        });

        GFW.clientStoreResourceStoreResponsibleStore.responsibleStore = new Ext.data.JsonStore({
            fields: [{name: 'Id', type: 'string'}, {name: 'Name',  type: 'string'} ]
        });

        GFW.clientStoreResourceStoreResponsibleStore.groupStore = new Ext.data.JsonStore({
            id: "resourceGroupStore",
            fields: [{name: 'Id', type: 'string'}, {name: 'Name',  type: 'string'} ]
        });

        GFW.clientStoreResourceStoreResponsibleStore.resourceStore = new Ext.data.JsonStore({
            fields: [{name: 'ResourceId', type: 'string'}, {name: 'Name',  type: 'string'} ]
        });
        GFW.groupStore = groupStore;
        GFW.windowTabs = [
            {
                title: __("FOLDERS"),
                id: "folderTab",
                itemId: 'folder',
                cls: "top-margin-big customTextBoxContainer",
                items: [
                    {
                        xtype: 'container',
                        anchor: '100%',
                        layout: 'column',
                        cls: "startContainer",
                        items: [
                            {
                                xtype: 'panel',
                                cls: "no-background",
                                title: __("TASK_FOLDER"),
                                id: "gv2",
                                columnWidth: .5,
                                forceFit: true,
                                autoScroll: true,
                                bodyStyle: 'padding: 10px;',
                                height: 300,
                                items: [
                                    {
                                        xtype: 'gridpanel',
                                        flex: 1,
                                        id: 'taskGridPanel',
                                        store: groupStore,
                                        allowDeselect: true,
                                        checkOnly: true,
                                        mode: 'multi',
                                        selModel: Ext.create('Ext.selection.CheckboxModel'),
                                        plugins: [Ext.create('Ext.grid.plugin.CellEditing', {
                                            clicksToEdit: 1
                                        })],
                                        columns: [{ flex: 1, dataIndex: 'Name', header: 'Select All'}],
                                        listeners: {
                                            deselect: function(model, record, index) {
                                                //myLocalStorage.setOrUnsetSelectedGroupsIdsObject(getQueryVariable("token"), record.data.Id);
                                            },
                                            select: function(model, record, index) {
                                                //myLocalStorage.setOrUnsetSelectedGroupsIdsObject(getQueryVariable("token"), record.data.Id);
                                            }
                                        }
                                    }
                                ]
                            },
                            {
                                xtype: 'panel',
                                cls: "no-background",
                                columnWidth: .5,
                                title: __("RESOURCE_FOLDER_GROUPS"),
                                bodyStyle: 'padding: 10px;',
                                forceFit: true,
                                autoScroll: true,
                                height: 300,
                                items: [
                                    {
                                        xtype: 'gridpanel',
                                        flex: 1,
                                        id: 'resourceGridPanel',
                                        store: resourceFolder,
                                        selModel: Ext.create('Ext.selection.CheckboxModel'),
                                        plugins: [Ext.create('Ext.grid.plugin.CellEditing', {
                                            clicksToEdit: 1
                                        })],
                                        columns: [{
                                            flex: 1,
                                            dataIndex: 'Name',
                                            header: __("SELECT_ALL"),
                                        }],
                                        listeners: {
                                            deselect: function(model, record, index) {

                                            },
                                            select: function(model, record, index) {

                                            }
                                        }
                                    }
                                ]
                            }
                        ]
                    }
                ]
            },
            {
                title: __("GEN_FILTER_TEXT"),
                itemId: 'genFilterText',
                id: "genFilterTextTab",
                cls: "top-margin-big customTextBoxContainer",
                autoScroll: true,
                items: [
                    {
                        xtype: 'textfield',
                        fieldLabel: __("ANY_TEXT_FIELD"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "AnyText",
                    },
                    {
                        xtype: 'numberfield',
                        fieldLabel: __("PRI"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "PrioFrom"
                    },
                    {
                        xtype: 'numberfield',
                        fieldLabel: __("TO"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "PrioTo"
                    },
                    {
                        xtype: 'numberfield',
                        fieldLabel: __("LEVEL_TO"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Level"
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("IDENT_NO"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "IndentNo"
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("TASK"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "TaskName"
                    },
                    {
                        xtype: 'combo',
                        fieldLabel: __("CLIENT"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "ClientId",
                        store: GFW.clientStoreResourceStoreResponsibleStore.clientStore,
                        queryMode: 'local',
                        displayField: 'Name',
                        valueField: 'Id',
                    },
                    {
                        xtype: 'combo',
                        fieldLabel: __("RESPONSIBLE"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "ResponsiblePersonId",
                        store: GFW.clientStoreResourceStoreResponsibleStore.responsibleStore,
                        queryMode: 'local',
                        displayField: 'Name',
                        valueField: 'Id',
                    },
                    {
                        xtype: 'combo',
                        fieldLabel: __("GROUP"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "ResourceGroups",
                        store: GFW.clientStoreResourceStoreResponsibleStore.groupStore,
                        queryMode: 'local',
                        displayField: 'Name',
                        valueField: 'Id',
                        listeners: {
                            'select': function(t) {
                                var showComponent = new Array();
                                if (t.value) {
                                    GFW.clientStoreResourceStoreResponsibleStore.groupStore.each(function(rec) {
                                        if(rec.get('Id') == t.value) {
                                            var childrens = rec.get('Childrens');
                                            Ext.getCmp("ResourceId").setValue("");
                                            GFW.clientStoreResourceStoreResponsibleStore.resourceStore.loadData(childrens);
                                        }
                                    });
                                }
                            }
                        }
                    },
                    {
                        xtype: 'combo',
                        fieldLabel: __("RESOURCES"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "ResourceId",
                        store: GFW.clientStoreResourceStoreResponsibleStore.resourceStore,
                        queryMode: 'local',
                        displayField: 'Name',
                        valueField: 'ResourceId',
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("CHANGED_BY"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "ChangeBy"
                    },
                    {
                        xtype: 'radiogroup',
                        fieldLabel: __("COMPARE"),
                        labelStyle: 'text-align: center;',
                        //arrange Radio Buttons into 2 columns
                        columns: 1,
                        itemId: 'compareBtn',
                        id: "compareBtn",
                        listeners: {
                            change: function(field, newValue, oldValue) {
                                Ext.getCmp("compareBtn2").setValue({SearchType2: newValue.SearchType});
                            }
                        },
                        items: [
                            {
                                xtype: 'radiofield',
                                boxLabel: __("ENITRE_CONTENTS_OF_THE_FIELD"),
                                name: 'SearchType',
                                checked: true,
                                inputValue: '1'
                            },
                            {
                                xtype: 'radiofield',
                                boxLabel: __("PART_OF_THE_CONTENTS_OF_THE_FIELD"),
                                name: 'SearchType',
                                inputValue: '2'
                            },
                            {
                                xtype: 'radiofield',
                                boxLabel: __("SEARCH_PATTERN"),
                                name: 'SearchType',
                                inputValue: '3'
                            }
                        ]
                    },
                    {
                        xtype: 'checkbox',
                        boxLabel: __("SHOW_ALL_SUB_TASKS"),
                        labelSeparator: '',
                        fieldLabel: '&nbsp',
                        height: 20, cls: "no-bottom-margin",
                        labelStyle: 'text-align: center;',
                        id: "ShowAllSubTask1",
                        inputValue: true,
                        uncheckedValue: false,
                        listeners: {
                            change: function(field, newValue, oldValue) {
                                Ext.getCmp("ShowAllSubTask1").setValue(newValue);
                                Ext.getCmp("ShowAllSubTask2").setValue(newValue);
                                Ext.getCmp("ShowAllSubTask3").setValue(newValue);
                            }
                        }
                    }
                ]
            },
            {
                title: 'Gen. Filter - Date',
                cls: "top-margin-big customTextBoxContainer",
                id: "genFilterDateTab",
                items: [
                    {
                        xtype: 'combo',
                        fieldLabel: __("CRITERIONs_1"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Criterion1",
                        width: 360,
                        store: __("SELECT_CRITERION", null, true),
                        value: "",
                        listeners: {
                            'select': function(t) {
                                var showComponent = new Array();
                                if (t.value == "(empty)") {
                                    GFW["hideAndShowCrite1"](showComponent, true);
                                } else {
                                    showComponent["Criterion1Type"] = true;
                                    showComponent["Criterion1FromDate"] = true;
                                    GFW["hideAndShowCrite1"](showComponent, true);
                                }
                            }
                        }
                    },
                    {
                        xtype: 'combo',
                        fieldLabel: __("RANGE"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Criterion1Type",
                        store: __("SELECT_CRITERIONTYPE", null, true),
                        value: "Before",
                        hidden: true,
                        listeners: {
                            'select': function(t) {
                                var showComponent = new Array();
                                if (t.value == "Before" ||
                                    t.value == "After" ||
                                    t.value == "On"
                                ) {
                                    showComponent["Criterion1FromDate"] = true;
                                    GFW["hideAndShowCrite1"](showComponent);
                                } else if (t.value == "Between") {
                                    showComponent["Criterion1FromDate"] = true;
                                    showComponent["Criterion1ToDate"] = true;
                                    GFW["hideAndShowCrite1"](showComponent);
                                } else if (t.value == "Time Range") {
                                    showComponent["Criterion1TimeFrom"] = true;
                                    showComponent["Criterion1TimeTo"] = true;
                                    showComponent["Criterion1TimeUnit"] = true;
                                    GFW["hideAndShowCrite1"](showComponent);
                                }
                            }
                        }
                    },
                    {
                        xtype: 'numberfield',
                        hidden: true,
                        fieldLabel: __("TIME_FROM"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Criterion1TimeFrom",
                    },
                    {
                        xtype: 'numberfield',
                        hidden: true,
                        fieldLabel: __("TIME_TO"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Criterion1TimeTo"
                    },
                    {
                        xtype: 'datefield',
                        hidden: true,
                        fieldLabel: __("DATE_FROM"),
                        emptyText: 'dd.mm.yyyy',
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Criterion1FromDate",
                        cls: 'x-border-box, x-border-box',
                        forceSelection:true,
                        format: Config.Data.DE_DATE_FORMAT_WINDOW
                    },
                    {
                        xtype: 'datefield',
                        hidden: true,
                        fieldLabel: __("DATE_TO"),
                        emptyText: 'dd.mm.yyyy',
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Criterion1ToDate",
                        minDate: new Date(),
                        forceSelection:true,
                        format: Config.Data.DE_DATE_FORMAT_WINDOW,
                        listeners: {
                            select: function(combo, value) {

                            }
                        }
                    },
                    {
                        xtype: 'combo',
                        hidden: true,
                        fieldLabel: __("TIME_UNIT"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Criterion1TimeUnit",
                        store: __("SELECT_CRITERION_TIME_UNIT", null, true),
                        value: "Days"
                    },
                    {
                        xtype: 'label',
                        html: '<br>'
                    },
                    {
                        xtype: 'label',
                        html: '<br>'
                    },
                    //Criterion2
                    {
                        xtype: 'combo',
                        fieldLabel: __("CRITERION_2"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        width: 360,
                        id: "Criterion2",
                        store: __("SELECT_CRITERION", null, true),
                        value: "",
                        listeners: {
                            'select': function(t) {
                                var showComponent = new Array();
                                if (t.value == "(empty)") {
                                    GFW["hideAndShowCrite2"](showComponent, true);
                                } else {
                                    showComponent["Criterion2Type"] = true;
                                    showComponent["Criterion2FromDate"] = true;
                                    GFW["hideAndShowCrite2"](showComponent, true);
                                }
                            }
                        }
                    },
                    {
                        xtype: 'combo',
                        hidden: true,
                        fieldLabel: __("RANGE"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Criterion2Type",
                        store: __("SELECT_CRITERIONTYPE", null, true),
                        value: "Before",
                        listeners: {
                            'select': function(t) {
                                var showComponent = new Array();
                                if (t.value == "Before" ||
                                    t.value == "After" ||
                                    t.value == "On"
                                ) {
                                    showComponent["Criterion2FromDate"] = true;
                                    GFW["hideAndShowCrite2"](showComponent);
                                } else if (t.value == "Between") {
                                    showComponent["Criterion2FromDate"] = true;
                                    showComponent["Criterion2ToDate"] = true;
                                    GFW["hideAndShowCrite2"](showComponent);
                                } else if (t.value == "Time Range") {
                                    showComponent["Criterion2TimeFrom"] = true;
                                    showComponent["Criterion2TimeTo"] = true;
                                    showComponent["Criterion2TimeUnit"] = true;
                                    GFW["hideAndShowCrite2"](showComponent);
                                }
                            }
                        }
                    },
                    {
                        xtype: 'numberfield',
                        hidden: true,
                        fieldLabel: __("TIME_FROM"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Criterion2TimeFrom"
                    },
                    {
                        xtype: 'numberfield',
                        hidden: true,
                        fieldLabel: __("TIME_TO"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Criterion2TimeTo"
                    },
                    {
                        xtype: 'datefield',
                        hidden: true,
                        fieldLabel: __("DATE_FROM"),
                        emptyText: 'dd.mm.yyyy',
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Criterion2FromDate",
                        cls: 'x-border-box, x-border-box',
                        forceSelection:true,
                        format: Config.Data.DE_DATE_FORMAT_WINDOW
                    },
                    {
                        xtype: 'datefield',
                        hidden: true,
                        fieldLabel: __("DATE_TO"),
                        emptyText: 'dd.mm.yyyy',
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Criterion2ToDate",
                        minDate: new Date(),
                        forceSelection:true,
                        format: Config.Data.DE_DATE_FORMAT_WINDOW
                    },
                    {
                        xtype: 'combo',
                        hidden: true,
                        fieldLabel: __("TIME_UNIT"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Criterion2TimeUnit",
                        store: __("SELECT_CRITERION_TIME_UNIT", null, true),
                        value: "Days"
                    },
                    {
                        xtype: 'checkbox',
                        hidden: true,
                        hideLabel: false,
                        labelSeparator: '',
                        boxLabel: __("SHOW_ALL_SUB_TASKS"),
                        fieldLabel: '&nbsp',
                        height: 20,
                        cls: "no-bottom-margin",
                        itemId: "ShowAllSubTask2",
                        id: "ShowAllSubTask2",
                        listeners: {
                            change: function(field, newValue, oldValue) {
                                Ext.getCmp("ShowAllSubTask1").setValue(newValue);
                                Ext.getCmp("ShowAllSubTask2").setValue(newValue);
                                Ext.getCmp("ShowAllSubTask3").setValue(newValue);
                            }
                        }
                    }
                ],
                itemId: 'genFilterDate'
            },
            {
                title: __("Gen_Filter_userdef_fields"),
                itemId: 'genFilterFields',
                id: "genFilterFieldsTab",
                cls: "top-margin-big customTextBoxContainer",
                items: [
                    {
                        xtype: 'radiogroup',
                        fieldLabel: '&nbsp',
                        labelSeparator: '',
                        labelStyle: 'text-align: center;',
                        //arrange Radio Buttons into 2 columns
                        columns: 2,
                        itemId: 'displayFieldType',
                        id: "displayFieldType",
                        items: [
                            {
                                xtype: 'radiofield',
                                boxLabel: __("Text"),
                                name: 'displayFieldType',
                                checked: true,
                                inputValue: '1',
                                cls: "margin-right-10",

                            },
                            {
                                xtype: 'radiofield',
                                boxLabel: __("Number"),
                                name: 'displayFieldType',
                                inputValue: '2'
                            }
                        ],
                        listeners: {
                            change: function(field, newValue, oldValue) {
                                var value = newValue.displayFieldType;
                                var hideString = "";
                                var showString = "";
                                if (value == '1') { //Show textboxs
                                    hideString = "Number";
                                    showString = "Text";
                                }
                                if (value == '2') { //Show numbers
                                    hideString = "Text";
                                    showString = "Number";
                                }
                                for (var i = 1; i <= 10; i++) {
                                    Ext.getCmp(hideString + i).hide();
                                    Ext.getCmp(showString + i).show();
                                }
                            }
                        }
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("Text1"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Text1"
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("Text2"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Text2"
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("Text3"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Text3"
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("Text4"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Text4"
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("Text5"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Text5"
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("Text6"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Text6"
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("Text7"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Text7"
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("Text8"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Text8"
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("Text9"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Text9"
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("Text10"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Text10"
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("Number1"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Number1",
                        hidden : true
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("Number2"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Number2",
                        hidden : true
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("Number3"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Number3",
                        hidden : true
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("Number4"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Number4",
                        hidden : true
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("Number5"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Number5",
                        hidden : true
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("Number6"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Number6",
                        hidden : true
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("Number7"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Number7",
                        hidden : true
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("Number8"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Number8",
                        hidden : true
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("Number9"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Number9",
                        hidden : true
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: __("Number10"),
                        labelStyle: 'text-align: center;',
                        height: 22,
                        id: "Number10",
                        hidden : true
                    },
                    {
                        xtype: 'radiogroup',
                        fieldLabel: __("Compare"),
                        labelStyle: 'text-align: center;',
                        //arrange Radio Buttons into 2 columns
                        columns: 1,
                        itemId: 'compareBtn',
                        id: "compareBtn2",
                        listeners: {
                            change: function(field, newValue, oldValue) {
                                Ext.getCmp("compareBtn").setValue({SearchType: newValue.SearchType2});
                            }
                        },
                        items: [
                            {
                                xtype: 'radiofield',
                                boxLabel: __("ENITRE_CONTENTS_OF_THE_FIELD"),
                                name: 'SearchType2',
                                checked: true,
                                inputValue: '1'
                            },
                            {
                                xtype: 'radiofield',
                                boxLabel: __("PART_OF_THE_CONTENTS_OF_THE_FIELD"),
                                name: 'SearchType2',
                                inputValue: '2'
                            },
                            {
                                xtype: 'radiofield',
                                boxLabel: __("SEARCH_PATTERN"),
                                name: 'SearchType2',
                                inputValue: '3'
                            }
                        ]
                    },
                    {
                        xtype: 'checkbox',
                        fieldLabel: '&nbsp',
                        labelSeparator: '',
                        boxLabel: __("SHOW_ALL_SUB_TASKS", ":"),
                        height: 20, cls: "no-bottom-margin",
                        labelStyle: 'text-align: center;',
                        itemId: "ShowAllSubTask3",
                        id: "ShowAllSubTask3",
                        listeners: {
                            change: function(field, newValue, oldValue) {
                                Ext.getCmp("ShowAllSubTask1").setValue(newValue);
                                Ext.getCmp("ShowAllSubTask2").setValue(newValue);
                                Ext.getCmp("ShowAllSubTask3").setValue(newValue);
                            }
                        }
                    }
                ]
            }
        ];

        GFW.tabPanel = new Ext.TabPanel({
            activeTab: 0,
            id: 'myTPanel',
            enableTabScroll: true,
            cls: "",
            items: GFW.windowTabs,
            layout: 'anchor',
            listeners: {
                'tabchange': function (tabPanel, tab) {
                    //console.log(tabPanel.id + ' ' + tab.id);
                    if(tab.id == "folderTab") {
                        Ext.getCmp("dontShowCompletedProj").getEl().show();
                        Ext.getCmp("deleteGenFilter").getEl().hide();
                    } else {
                        Ext.getCmp("dontShowCompletedProj").getEl().hide();
                        Ext.getCmp("deleteGenFilter").getEl().show();
                    }
                }
            }
        });

        var win = new Ext.Window({
            id: 'myWindow',
            title: __('Filters'),
            autoScroll: false,
            width: (Ext.getBody().getViewSize().width < 700) ? Ext.getBody().getViewSize().width * 0.8 :700,
            height: 650,
            items: GFW.tabPanel,
            layout: 'anchor',
            maximizable: true,
            constrainHeader: true,
            closable: true,
            modal: true,
            listeners: {
                afterRender: function() {
                    //Fetch all the saved filters, And restore all the element in window in the previous state.
                    //var userDetail = myLocalStorage.getUserDetailFromLocalStorage(getQueryVariable("token"));
                    var a = GFW.tabPanel.getActiveTab();
                    var idx = GFW.tabPanel.items.indexOf(a);
                    if(idx == 0) {
                        Ext.getCmp("dontShowCompletedProj").getEl().show();
                    }
                    if (GLOBAL_GENERAL_FILTERS) {
                        GFW.initialisePreviousValues(GLOBAL_GENERAL_FILTERS);
                    }
                    var storeData = GFW.getDataForClientsResponsibleResources();
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
                            xtype: 'checkbox',
                            //fieldLabel: 'Do not show completed Projects',
                            labelStyle: 'width:200px',
                            height: 20,
                            itemId: "dontShowCompletedProj",
                            id: "dontShowCompletedProj",
                            hidden : true,
                            boxLabel: __("DONT_SHOW_COMPLETED_PROJECT"),
                            labelSeparator: ''
                        },
                        {
                            xtype: 'button',
                            text: __("DELETE_GENERAL_FILTERS"),
                            itemId: 'deleteGenFilter',
                            id: "deleteGenFilter",
                            align: 'left',
                            cls: "center-align",
                            hidden : true,
                            handler: function() {
                                var operationPerformed = myLocalStorage.removeGeneralFilter(getQueryVariable("token"));
                                GFW.resetToEmpty();
                            }
                        },
                        {
                            xtype: 'button',
                            text: __("Help"),
                            itemId: 'help',
                            align: 'left',
                            cls: "center-align",
                            handler: function() {
                                window.open("http://www.braintool.com/fileadmin/user_upload/A-Plan2016_Manual.pdf#page=78",'_blank');
                                win.hide();
                            }
                        },
                        {
                            xtype: 'button',
                            text: __("Cancel"),
                            itemId: 'cancel',
                            cls: "center-align",
                            handler: function() {
                                GFW.undoNewlyAddedValues();
                                win.hide();
                            }
                        },
                        {
                            xtype: 'button',
                            text: __("Ok"),
                            itemId: 'save',
                            cls: "center-align",
                            handler: function() {
                                if(GFW.checkIfGeneralDateFilterIsDirty()) {
                                    GFW.tabPanel.setActiveTab(2);
                                    Ext.MessageBox.alert('Error', 'Input error. Please fill missing data');
                                } else {
                                    win.hide();
                                    GFW.fireGeneralFilter();
                                }
                            }
                        }
                    ]
                }
            ]
        });
        win.show();
    },
    checkIfGeneralDateFilterIsDirty: function() {
        if(Ext.getCmp("Criterion1Type").getValue() == "Between" && Ext.getCmp("Criterion1").getValue()){
            console.log(1);
            if(!document.getElementById('Criterion1FromDate-inputEl').value ||
                !document.getElementById('Criterion1ToDate-inputEl').value ) {
                return true;
            }
        }
        if(Ext.getCmp("Criterion1Type").getValue() == "Time Range" && Ext.getCmp("Criterion1").getValue()){
            //Then time from and date to must not be null
            if(!Ext.getCmp("Criterion1TimeFrom").getValue() || !Ext.getCmp("Criterion1TimeTo").getValue()) {
                return true;
            }
        }
        if((Ext.getCmp("Criterion1Type").getValue() == "Before" ||
            Ext.getCmp("Criterion1Type").getValue() == "After" ||
            Ext.getCmp("Criterion1Type").getValue() == "On") && Ext.getCmp("Criterion1").getValue()){
            if(document.getElementById('Criterion1ToDate-inputEl') && !document.getElementById('Criterion1FromDate-inputEl').value) {
                return true;
            }
        }
        if(Ext.getCmp("Criterion2Type").getValue() == "Between" && Ext.getCmp("Criterion2").getValue()){
            if( (
                    document.getElementById('Criterion2ToDate-inputEl') &&
                    document.getElementById('Criterion2FromDate-inputEl')
                ) &&
                !document.getElementById('Criterion2FromDate-inputEl').value ||
                !document.getElementById('Criterion2ToDate-inputEl').value ) {
                return true;
            }
        }
        if(Ext.getCmp("Criterion2Type").getValue() == "Time Range" && Ext.getCmp("Criterion2").getValue()){
            //Then time from and date to must not be null
            if(!Ext.getCmp("Criterion2TimeFrom").getValue() || !Ext.getCmp("Criterion2TimeTo").getValue()) {
                return true;
            }
        }
        if((Ext.getCmp("Criterion2Type").getValue() == "Before" ||
            Ext.getCmp("Criterion2Type").getValue() == "After" ||
            Ext.getCmp("Criterion2Type").getValue() == "On") && Ext.getCmp("Criterion2").getValue()){
            if(document.getElementById('Criterion2FromDate-inputEl') && !document.getElementById('Criterion2FromDate-inputEl').value) {
                return true;
            }
        }
        return false;
    },
    undoNewlyAddedValues: function() {
        GFW.resetToEmpty();
    },
    getDataForClientsResponsibleResources: function() {
        Config.Data.RequestheaderSettings.url = "Get/ResponsiblePersonsAndClients";
        $.ajax(Config.Data.RequestheaderSettings).done(function(response) {
            var ResponseData = JSON.parse(response);
            if(ResponseData) {
                GFW.clientStoreResourceStoreResponsibleStore.clientStore.loadData(ResponseData.Payload.Clients);
                GFW.clientStoreResourceStoreResponsibleStore.responsibleStore.loadData(ResponseData.Payload.ResponsiblePersons);
                GFW.clientStoreResourceStoreResponsibleStore.groupStore.loadData(ResponseData.Payload.Resources);
            }
            //return ResponseData;
        }).fail(function(response) {
            var SingleError = { "Id": GUID(), "ErrorCode": response.status, "Message": response.statusText, "URL": getQueryVariable("HOST_URL") };
            InsertLog(SingleError);
            alert('Error! ' + response.statusText + ", "+__("Error_While_loading_clients", ", ")+__("SERVICE_ERROR"));
        });
    },
    resetToEmpty: function() {
        Ext.each(GFW.generalFilterAllComponents, function(item) {
            var fieldValue = null;
            if (item.elementId == "displayFieldType") {
                Ext.getCmp(item.field).setValue({displayFieldType: 1});
            } else if (item.elementId == "compareBtn") {
                Ext.getCmp("compareBtn").setValue({SearchType: 1});
                Ext.getCmp("compareBtn2").setValue({SearchType2: 1});
            } else if (item.elementId == "ShowAllSubTask1") {
                Ext.getCmp("ShowAllSubTask1").setValue(true);
                Ext.getCmp("ShowAllSubTask2").setValue(true);
                Ext.getCmp("ShowAllSubTask3").setValue(true);
            } else {
                Ext.getCmp(item.elementId).setValue("");
            }
        });
        Ext.getCmp("Criterion1Type").hide();
        Ext.getCmp("Criterion2Type").hide();
        Ext.getCmp("Criterion1FromDate").hide();
        Ext.getCmp("Criterion2FromDate").hide();
        Ext.getCmp("Criterion1ToDate").hide();
        Ext.getCmp("Criterion2ToDate").hide();
        Ext.getCmp("Criterion1TimeFrom").hide();
        Ext.getCmp("Criterion2TimeFrom").hide();
        Ext.getCmp("Criterion1TimeTo").hide();
        Ext.getCmp("Criterion2TimeTo").hide();
        Ext.getCmp("Criterion1TimeUnit").hide();
        Ext.getCmp("Criterion2TimeUnit").hide();

        var gridPanel = Ext.ComponentQuery.query("#taskGridPanel")[0];
        gridPanel.getSelectionModel().deselectAll();

        var resourceGridSelection = Ext.ComponentQuery.query("#resourceGridPanel")[0];
        var selectedResourceGroups = resourceGridSelection.getSelectionModel().deselectAll();
    },
    hideTextOrNumber: function(hideString){
        var hide = "";
        var show = "";
        if(hideString == "Number") {
            show = "Text";
            hide = "Number";
        } else {
            show = "Number";
            hide = "Text";
        }
        showString = "Text";
        for (var i = 1; i <= 10; i++) {
            Ext.getCmp(show + i).show();
            Ext.getCmp(hide + i).hide();
        }
    },
    fireGeneralFilter: function() {
        var reloadFilter1 = GFW.checkAndSetGeneralFiltersGroupsAndResource();
        var reloadFilter2 = GFW.setOtherFilters();
        var reloadFilter3 = GFW.checkIfGeneralFilterButtonShouldEnablebutton();
        if (reloadFilter1 || reloadFilter2 || reloadFilter3) {
            SetFiltersDataFromApp(GLOBAL_RIBBON_FILTER, GLOBAL_GENERAL_FILTERS);
        }
    },
    setOtherFilters: function() {
        var shouldReload3 = false;
        Ext.each(GFW.generalFilterAllComponents, function(item) {
            var fieldValue = null;
            if (item.elementId == "compareBtn" || item.elementId == "displayFieldType") {
                fieldValue = Ext.ComponentQuery.query('[name=' + item.field + ']')[0].getGroupValue();
            } else if (item.elementId == "ShowAllSubTask1") {
                fieldValue = Ext.getCmp('ShowAllSubTask1').checked;
                //item.elementId = item.field;
            } else {
                if (item.elementId == "Criterion1FromDate" ||
                    item.elementId == "Criterion1ToDate" ||
                    item.elementId == "Criterion2FromDate" ||
                    item.elementId == "Criterion2ToDate"
                ) {
                    if (Ext.getCmp(item.elementId).getValue()) {
                        fieldValue =  Ext.util.Format.date(Ext.getCmp(item.elementId).getValue(), 'Y-m-d');
                    }
                } else {
                    if(item.elementId == "PrioTo" || item.elementId == "PrioFrom") {
                        if(Ext.getCmp(item.elementId).getValue()) {
                            fieldValue = (Ext.getCmp(item.elementId).getValue()).toString().trim();
                        } else {
                            fieldValue = "";
                        }
                    } else {
                        if(Ext.getCmp(item.elementId).value) {
                            fieldValue = (Ext.getCmp(item.elementId).value).toString().trim();
                        } else {
                            fieldValue = "";
                        }
                    }
                }
            }
            var doNotSave1 = false; //Save current attribute value if value is false
            var doNotSave2 = false; //Save current attribute value if value is false
            if(!Ext.getCmp("Criterion1").getValue()) { //Check if value is not set then set flag to true so value is not saved
                if (item.elementId == "Criterion1Type" ||
                    item.elementId == "Criterion1TimeUnit" ||
                    item.elementId == "Criterion1FromDate" ||
                    item.elementId == "Criterion1ToDate"
                ) {
                    doNotSave1 = true;
                    fieldValue =  "";
                }
            } else { //Dropdown value is selectd now check if all the selected criteria matched or not
                if (item.elementId == "Criterion1Type" ||
                    item.elementId == "Criterion1TimeUnit" ||
                    item.elementId == "Criterion1FromDate" ||
                    item.elementId == "Criterion1ToDate"
                ) {
                    if(Ext.getCmp("Criterion1").getValue() == "(empty)") { //
                        doNotSave1 = true;
                    } else if(Ext.getCmp("Criterion1Type").getValue() == "Between" && Ext.getCmp("Criterion1").getValue() != "(empty)"){ //Check of critearea type
                        //Then date from and date to must not be null
                        if(!document.getElementById('Criterion1FromDate-inputEl').value || !document.getElementById('Criterion1ToDate-inputEl').value) { //Check for date from only
                            doNotSave1 = true;
                        }
                    } else if(Ext.getCmp("Criterion1Type").getValue() == "Time Range" && Ext.getCmp("Criterion1").getValue() != "(empty)"){ //Check of critearea type
                        //Then time from and date to must not be null
                        if(!Ext.getCmp("Criterion1TimeFrom").getValue() || !Ext.getCmp("Criterion1TimeTo").getValue()) { //Check for date from only
                            doNotSave1 = true;
                        }
                    } else if(Ext.getCmp("Criterion1Type").getValue() == "Before" ||
                        Ext.getCmp("Criterion1Type").getValue() == "After" ||
                        Ext.getCmp("Criterion1Type").getValue() == "On" ){ //Check of critearea type
                        if(!document.getElementById('Criterion1FromDate-inputEl').value && Ext.getCmp("Criterion1").getValue() != "(empty)") { //Check for date from only
                            doNotSave1 = true;
                        }
                    }
                }
            }
            if(!Ext.getCmp("Criterion2").getValue()) { //Check if value is not set then set flag to true so value is not saved
                if (item.elementId == "Criterion2Type" ||
                    item.elementId == "Criterion2TimeUnit" ||
                    item.elementId == "Criterion2FromDate" ||
                    item.elementId == "Criterion2ToDate"
                ) {
                    doNotSave2 = true;
                }
            } else { //Dropdown value is selectd now check if all the selected criteria matched or not
                if (item.elementId == "Criterion2Type" ||
                    item.elementId == "Criterion2TimeUnit" ||
                    item.elementId == "Criterion2FromDate" ||
                    item.elementId == "Criterion2ToDate"
                ) {
                    if(Ext.getCmp("Criterion2").getValue() == "(empty)") { //
                        doNotSave2 = true;
                    } else if(Ext.getCmp("Criterion2Type").getValue() == "Between" && Ext.getCmp("Criterion2").getValue() != "(empty)"){ //Check of critearea type
                        //Then date from and date to must not be null
                        if(!Ext.getCmp("Criterion2FromDate").getValue() || !document.getElementById('Criterion2ToDate-inputEl').value) { //Check for date from only
                            doNotSave2 = true;
                        }
                    } else if(Ext.getCmp("Criterion2Type").getValue() == "Time Range" && Ext.getCmp("Criterion2").getValue() != "(empty)"){ //Check of critearea type
                        //Then time from and date to must not be null
                        if(!Ext.getCmp("Criterion2TimeFrom").getValue() || !Ext.getCmp("Criterion2TimeTo").getValue()) { //Check for date from only
                            doNotSave2 = true;
                        }
                    } else if(Ext.getCmp("Criterion2Type").getValue() == "Before" ||
                        Ext.getCmp("Criterion2Type").getValue() == "After" ||
                        Ext.getCmp("Criterion2Type").getValue() == "On" ){ //Check of critearea type
                        if(!document.getElementById('Criterion2FromDate-inputEl').value && Ext.getCmp("Criterion2").getValue() != "(empty)") { //Check for date from only
                            doNotSave2 = true;
                        }
                    }
                }
            }
            var operation = false;
            if(!doNotSave1 || !doNotSave2) {
                operation = GFW.setUnsetGeneralFilterTabFields(item.elementId, fieldValue);
            }
            if (operation) {
                shouldReload3 = true;
            }
        });
        return shouldReload3;
    },
    setUnsetGeneralFilterTabFields: function(key, value){
        var shouldReloadData = false;
        if(GLOBAL_GENERAL_FILTERS) {
            if(GLOBAL_GENERAL_FILTERS.hasOwnProperty(key)) {
                var previousFilters = GLOBAL_GENERAL_FILTERS[key]; //xyz
                if(value && value.length > 0) {
                    if(previousFilters != value) {
                        console.log(key);
                        shouldReloadData = true;
                        GLOBAL_GENERAL_FILTERS[key] = value;
                    }
                } else {
                    if(previousFilters && previousFilters != value) {
                        console.log(key);
                        shouldReloadData = true; //Filter field is empty by user,
                    }
                    delete GLOBAL_GENERAL_FILTERS[key];
                }
            } else {
                //Property does not set please set it.
                if(value && value.length > 0) {
                    GLOBAL_GENERAL_FILTERS[key] = value;
                    shouldReloadData = true;
                    console.log(key);
                }
            }
        } else {
            if(key && value) {
                GLOBAL_GENERAL_FILTERS = {};
                GLOBAL_GENERAL_FILTERS[key] = value;
                shouldReloadData = true;
                console.log(key);
                console.log(value);
            }
        }
        return shouldReloadData;
    },
    checkAndSetGeneralFiltersGroupsAndResource: function() {
        var shouldReload1 = false;
        var shouldReload2 = false;
        var GroupIdsToAddOrRemove = [];
        var gridSelection = Ext.ComponentQuery.query("#taskGridPanel")[0];
        var selectedGroups = gridSelection.getSelectionModel().getSelection();
        Ext.each(selectedGroups, function(item) {
            GroupIdsToAddOrRemove.push(item.data.Id);
        });
        shouldReload1 = GFW.setGeneralAndResourceFilters(GroupIdsToAddOrRemove, "GroupId");

        var resourceGridSelection = Ext.ComponentQuery.query("#resourceGridPanel")[0];
        var selectedResourceGroups = resourceGridSelection.getSelectionModel().getSelection();
        var resourceIdList = [];
        Ext.each(selectedResourceGroups, function(item) {
            resourceIdList.push(item.data.Id);
        });
        shouldReload2 = GFW.setGeneralAndResourceFilters(resourceIdList, "ResourcesIds");
        return (shouldReload1 || shouldReload2);
    },
    setGeneralAndResourceFilters: function(GroupIdsToAddOrRemove, key) {
        var shouldReloadData = false;
        if(GLOBAL_GENERAL_FILTERS && GLOBAL_GENERAL_FILTERS.hasOwnProperty(key)) {
            var previousFilters = GLOBAL_GENERAL_FILTERS[key];
            if(previousFilters)
                previousFilters = previousFilters.split(",");
            var dataToSave = null;
            if(GroupIdsToAddOrRemove.length > 0) {
                Ext.Array.each(GroupIdsToAddOrRemove, function(rec1) {
                    var found = false;
                    Ext.Array.each(previousFilters, function(rec2) {
                        if(rec1 == rec2) {
                            found = true;
                        }
                    });
                    if(found == false) {
                        shouldReloadData = true;
                    }
                });
                dataToSave = GroupIdsToAddOrRemove.join() + ",";
            } else {
                if(previousFilters && previousFilters.length > 1) {
                    shouldReloadData = true;
                    dataToSave =  "";
                }
            }
            GLOBAL_GENERAL_FILTERS[key] = dataToSave;
        } else {
            if(GroupIdsToAddOrRemove.length > 0) {
                dataToSave = GroupIdsToAddOrRemove.join() + ",";
                if(!GLOBAL_GENERAL_FILTERS) {
                    GLOBAL_GENERAL_FILTERS = {};
                }
                shouldReloadData = true;
                GLOBAL_GENERAL_FILTERS[key] = dataToSave;
            }
        }
        return shouldReloadData;
    },

    /*saveGeneralFilterToLocalStorageOld: function() {
     var gridSelection = Ext.ComponentQuery.query("#taskGridPanel")[0];
     var selectedGroups = gridSelection.getSelectionModel().getSelection();
     var AnyChangeInFolderFilter = false;
     var groupIdList = [];
     Ext.each(selectedGroups, function(item) {
     groupIdList.push(item.data.Id);
     });
     var shouldReload1 = false;
     //if (groupIdList.length > 0) {
     shouldReload1 = myLocalStorage.setOrUnsetSelectedGroupsIdsObject(getQueryVariable("token"), groupIdList, "GroupId");

     //}
     var resourceGridSelection = Ext.ComponentQuery.query("#resourceGridPanel")[0];
     var selectedResourceGroups = resourceGridSelection.getSelectionModel().getSelection();
     var resourceIdList = [];
     Ext.each(selectedResourceGroups, function(item) {
     resourceIdList.push(item.data.Id);
     });
     var shouldReload2 = false;
     //if (resourceIdList.length > 0) {
     shouldReload2 = myLocalStorage.setOrUnsetSelectedGroupsIdsObject(getQueryVariable("token"), resourceIdList, "ResourcesIds");
     //}
     //Check general filter text
     var shouldReload3 = false;
     Ext.each(GFW.generalFilterAllComponents, function(item) {
     var fieldValue = null;
     if (item.elementId == "compareBtn" || item.elementId == "displayFieldType") {
     fieldValue = Ext.ComponentQuery.query('[name=' + item.field + ']')[0].getGroupValue();
     } else if (item.elementId == "ShowAllSubTask1") {
     fieldValue = Ext.getCmp('ShowAllSubTask1').checked;
     //item.elementId = item.field;
     } else {
     console.log(item.elementId);
     if (item.elementId == "Criterion1FromDate" ||
     item.elementId == "Criterion1ToDate" ||
     item.elementId == "Criterion2FromDate" ||
     item.elementId == "Criterion2ToDate"
     ) {
     if (Ext.getCmp(item.elementId).getValue()) {
     fieldValue =  Ext.util.Format.date(Ext.getCmp(item.elementId).getValue(), 'Y-m-d');
     }
     }
     else {
     if(item.elementId == "PrioTo" || item.elementId == "PrioFrom") {
     if(Ext.getCmp(item.elementId).getValue()) {
     fieldValue = (Ext.getCmp(item.elementId).getValue()).toString().trim();
     } else {
     fieldValue = "";
     }
     } else {
     if(Ext.getCmp(item.elementId).value) {
     fieldValue = (Ext.getCmp(item.elementId).value).toString().trim();
     } else {
     fieldValue = "";
     }
     }
     }
     }
     var operation = myLocalStorage.setUnsetGeneralFilterTabFields(getQueryVariable("token"), item.elementId, fieldValue);
     if (operation) {
     shouldReload3 = true;
     }
     GFW.checkIfGeneralFilterButtonShouldEnablebutton();
     if (shouldReload1 || shouldReload2 || shouldReload3) {
     SetFiltersDataFromApp(null, myLocalStorage.getRibbonFilters(getQueryVariable("token")));
     }
     });
     },*/

    checkIfGeneralFilterButtonShouldEnablebutton: function() {
        var savedGeneralGeneral = (GLOBAL_GENERAL_FILTERS && GLOBAL_GENERAL_FILTERS.hasOwnProperty("GroupId")) ? GLOBAL_GENERAL_FILTERS.GroupId : null;
        if(savedGeneralGeneral && savedGeneralGeneral.length > 1) {
            Ext.getCmp('folderFilterOnOff').enable();
        } else {
            Ext.getCmp('folderFilterOnOff').disable();
        }
        var savedFilterGeneral = GLOBAL_GENERAL_FILTERS;
        if(
            (savedFilterGeneral && savedFilterGeneral.hasOwnProperty("AnyText") && savedFilterGeneral["AnyText"]) ||
            (savedFilterGeneral && savedFilterGeneral.hasOwnProperty("PrioFrom") && savedFilterGeneral["PrioFrom"]) ||
            (savedFilterGeneral && savedFilterGeneral.hasOwnProperty("PrioTo") && savedFilterGeneral["PrioTo"]) ||
            (savedFilterGeneral && savedFilterGeneral.hasOwnProperty("Level") && savedFilterGeneral["Level"]) ||
            (savedFilterGeneral && savedFilterGeneral.hasOwnProperty("IndentNo") && savedFilterGeneral["IndentNo"]) ||
            (savedFilterGeneral && savedFilterGeneral.hasOwnProperty("TaskName") && savedFilterGeneral["TaskName"]) ||
            (savedFilterGeneral && savedFilterGeneral.hasOwnProperty("ClientId") && savedFilterGeneral["ClientId"]) ||
            (savedFilterGeneral && savedFilterGeneral.hasOwnProperty("ResponsiblePersonId") && savedFilterGeneral["ResponsiblePersonId"]) ||
            (savedFilterGeneral && savedFilterGeneral.hasOwnProperty("ResourceGroups") && savedFilterGeneral["ResourceGroups"]) ||
            (savedFilterGeneral && savedFilterGeneral.hasOwnProperty("ResourceId") && savedFilterGeneral["ResourceId"]) ||
            (savedFilterGeneral && savedFilterGeneral.hasOwnProperty("ResourceId") && savedFilterGeneral["ResourceId"])
        ) {
            Ext.getCmp('GeneralFilterOnOff').enable();
        } else {
            Ext.getCmp('GeneralFilterOnOff').disable();
        }

    },
    setSelectedGroups: function() {
        if (GLOBAL_GENERAL_FILTERS) {
            if (GLOBAL_GENERAL_FILTERS.hasOwnProperty("GroupId")) {
                if (GLOBAL_GENERAL_FILTERS.GroupId && GLOBAL_GENERAL_FILTERS.GroupId.length > 1) {
                    var grpArr = GLOBAL_GENERAL_FILTERS.GroupId.split(",");
                    if (grpArr.length > 1) {
                        var gridPanel = Ext.ComponentQuery.query("#taskGridPanel")[0];
                        Ext.each(grpArr, function(grpId) {
                            if (grpId) {
                                console.log(grpId);
                                var index = gridPanel.store.find('Id', grpId);
                                gridPanel.getSelectionModel().select(index, true);
                            }
                        });
                    }
                }
            }
        }
    },
    setSelectedResourceGroups: function() {
        if (GLOBAL_GENERAL_FILTERS) {
            if (GLOBAL_GENERAL_FILTERS.hasOwnProperty("ResourcesIds")) {
                if (GLOBAL_GENERAL_FILTERS.ResourcesIds && GLOBAL_GENERAL_FILTERS.ResourcesIds.length > 1) {
                    var grpArr = GLOBAL_GENERAL_FILTERS.ResourcesIds.split(",");
                    if (grpArr.length > 1) {
                        var gridPanel = Ext.ComponentQuery.query("#resourceGridPanel")[0];
                        Ext.each(grpArr, function(grpId) {
                            if (grpId) {
                                var index = gridPanel.store.find('Id', grpId);
                                gridPanel.getSelectionModel().select(index, true);
                            }
                        });
                    }
                }
            }
        }
    },
    initialisePreviousValues: function(generalFiltersToSet) {
        GFW.checkIfGeneralFilterButtonShouldEnablebutton();
        if (generalFiltersToSet) {
            if(generalFiltersToSet.hasOwnProperty("displayFieldType")) {
                if(generalFiltersToSet["displayFieldType"]) {
                    var hideString = (generalFiltersToSet["displayFieldType"] == "1") ? "Number" : "Text";
                    Ext.getCmp('displayFieldType').setValue({displayFieldType: generalFiltersToSet["displayFieldType"]});
                    GFW.hideTextOrNumber(hideString);
                }
            } else {
                GFW.hideTextOrNumber("Number");
            }
            var allSavedKeys = Object.keys(generalFiltersToSet);
            if (allSavedKeys && allSavedKeys.length) {
                Ext.each(allSavedKeys, function(item) {
                    if(generalFiltersToSet.hasOwnProperty(item) && generalFiltersToSet[item]) {
                        switch (item) {
                            case "AnyText":
                                var value = generalFiltersToSet["AnyText"].trim();
                                console.log(value);
                                if (value.length > 0) {
                                    Ext.getCmp("AnyText").setValue(generalFiltersToSet["AnyText"]);
                                }
                                break;

                            case "PrioFrom":
                                var value = generalFiltersToSet["PrioFrom"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("PrioFrom").setValue(generalFiltersToSet["PrioFrom"]);
                                }
                                break;

                            case "PrioTo":
                                var value = generalFiltersToSet["PrioTo"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("PrioTo").setValue(generalFiltersToSet["PrioTo"]);
                                }
                                break;

                            case "Level":
                                var value = generalFiltersToSet["Level"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Level").setValue(generalFiltersToSet["Level"]);
                                }
                                break;

                            case "IndentNo":
                                var value = generalFiltersToSet["IndentNo"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("IndentNo").setValue(generalFiltersToSet["IndentNo"]);
                                }
                                break;

                            case "TaskName":
                                var value = generalFiltersToSet["TaskName"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("TaskName").setValue(generalFiltersToSet["TaskName"]);
                                }
                                break;

                            case "ClientId":
                                var value = generalFiltersToSet["ClientId"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("ClientId").setValue(generalFiltersToSet["ClientId"]);
                                }
                                break;

                            case "ResponsiblePersonId":
                                var value = generalFiltersToSet["ResponsiblePersonId"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("ResponsiblePersonId").setValue(generalFiltersToSet["ResponsiblePersonId"]);
                                }
                                break;

                            case "ResourceGroups":
                                var value = generalFiltersToSet["ResourceGroups"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("ResourceGroups").setValue(generalFiltersToSet["ResourceGroups"]);
                                }
                                break;

                            case "ResourceId":
                                var value = generalFiltersToSet["ResourceId"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("ResourceId").setValue(generalFiltersToSet["ResourceId"]);
                                }
                                break;

                            case "ChangeBy":
                                var value = generalFiltersToSet["ChangeBy"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("ChangeBy").setValue(generalFiltersToSet["ChangeBy"]);
                                }
                                break;

                            case "ShowAllSubTask":
                                var value = generalFiltersToSet["ShowAllSubTask"].trim();
                                if(value) {
                                    Ext.getCmp("ShowAllSubTask1").setValue(true);
                                    Ext.getCmp("ShowAllSubTask2").setValue(true);
                                    Ext.getCmp("ShowAllSubTask3").setValue(true);
                                } else {
                                    Ext.getCmp("ShowAllSubTask1").setValue(false);
                                    Ext.getCmp("ShowAllSubTask2").setValue(false);
                                    Ext.getCmp("ShowAllSubTask3").setValue(false);
                                }
                                break;

                            case "displayFieldType":
                                var value = generalFiltersToSet["displayFieldType"].trim();
                                Ext.getCmp("displayFieldType").setValue(value);
                                break;

                            case "Text1":
                                var value = generalFiltersToSet["Text1"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Text1").setValue(generalFiltersToSet["Text1"]);
                                }
                                break;

                            case "Text2":
                                var value = generalFiltersToSet["Text2"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Text2").setValue(generalFiltersToSet["Text2"]);
                                }
                                break;

                            case "Text3":
                                var value = generalFiltersToSet["Text3"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Text3").setValue(generalFiltersToSet["Text3"]);
                                }
                                break;

                            case "Text4":
                                var value = generalFiltersToSet["Text4"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Text4").setValue(generalFiltersToSet["Text4"]);
                                }
                                break;

                            case "Text5":
                                var value = generalFiltersToSet["Text5"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Text5").setValue(generalFiltersToSet["Text5"]);
                                }
                                break;

                            case "Text6":
                                var value = generalFiltersToSet["Text6"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Text6").setValue(generalFiltersToSet["Text6"]);
                                }
                                break;

                            case "Text7":
                                var value = generalFiltersToSet["Text7"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Text7").setValue(generalFiltersToSet["Text7"]);
                                }
                                break;
                            case "Text8":
                                var value = generalFiltersToSet["Text8"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Text8").setValue(generalFiltersToSet["Text8"]);
                                }
                                break;

                            case "Text9":
                                var value = generalFiltersToSet["Text9"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Text9").setValue(generalFiltersToSet["Text9"]);
                                }
                                break;

                            case "Text10":
                                var value = generalFiltersToSet["Text10"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Text10").setValue(generalFiltersToSet["Text10"]);
                                }
                                break;

                            case "Number1":
                                var value = generalFiltersToSet["Number1"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Number1").setValue(generalFiltersToSet["Number1"]);
                                }
                                break;

                            case "Number2":
                                var value = generalFiltersToSet["Number1"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Number1").setValue(generalFiltersToSet["Number2"]);
                                }
                                break;

                            case "Number3":
                                var value = generalFiltersToSet["Number3"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Number3").setValue(generalFiltersToSet["Number3"]);
                                }
                                break;

                            case "Number4":
                                var value = generalFiltersToSet["Number4"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Number4").setValue(generalFiltersToSet["Number4"]);
                                }
                                break;

                            case "Number5":
                                var value = generalFiltersToSet["Number5"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Number5").setValue(generalFiltersToSet["Number5"]);
                                }
                                break;

                            case "Number6":
                                var value = generalFiltersToSet["Number6"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Number6").setValue(generalFiltersToSet["Number6"]);
                                }
                                break;

                            case "Number7":
                                var value = generalFiltersToSet["Number7"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Number7").setValue(generalFiltersToSet["Number7"]);
                                }
                                break;

                            case "Number8":
                                var value = generalFiltersToSet["Number8"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Number8").setValue(generalFiltersToSet["Number8"]);
                                }
                                break;

                            case "Number9":
                                var value = generalFiltersToSet["Number9"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Number9").setValue(generalFiltersToSet["Number9"]);
                                }
                                break;

                            case "Number10":
                                var value = generalFiltersToSet["Number10"].trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Number10").setValue(generalFiltersToSet["Number10"]);
                                }
                                break;

                            case "Criterion1":
                                var value = (generalFiltersToSet["Criterion1"]).toString().trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Criterion1").setValue(generalFiltersToSet["Criterion1"]);
                                }
                                break;

                            case "Criterion1Type":
                                var value = (generalFiltersToSet["Criterion1Type"]).toString().trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Criterion1Type").setValue(generalFiltersToSet["Criterion1Type"]);
                                }
                                break;

                            case "Criterion2Type":
                                var value = (generalFiltersToSet["Criterion2Type"]).toString().trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Criterion2Type").setValue(generalFiltersToSet["Criterion2Type"]);
                                }
                                break;

                            case "Criterion2":
                                var value = (generalFiltersToSet["Criterion2"]).toString().trim();
                                if (value.length > 0) {
                                    Ext.getCmp("Criterion2").setValue(generalFiltersToSet["Criterion2"]);
                                }
                                break;

                            case "Criterion1FromDate":
                                var value = (generalFiltersToSet["Criterion1FromDate"]).toString().trim();
                                if (value.length > 0) {
                                    var dates = Ext.util.Format.date(generalFiltersToSet["Criterion1FromDate"], Config.Data.DE_DATE_FORMAT_WINDOW);
                                    Ext.getCmp("Criterion1FromDate").setValue(dates);
                                }
                                break;

                            case "Criterion1ToDate":
                                var value = (generalFiltersToSet["Criterion1ToDate"]).toString().trim();
                                if (value.length > 0) {
                                    var dates = Ext.util.Format.date(generalFiltersToSet["Criterion1ToDate"], Config.Data.DE_DATE_FORMAT_WINDOW);
                                    Ext.getCmp("Criterion1ToDate").setValue(dates);
                                }
                                break;

                            case "Criterion2FromDate":
                                var value = (generalFiltersToSet["Criterion2FromDate"]).toString().trim();
                                if (value.length > 0) {
                                    var dates = Ext.util.Format.date(generalFiltersToSet["Criterion2FromDate"], Config.Data.DE_DATE_FORMAT_WINDOW);
                                    Ext.getCmp("Criterion2FromDate").setValue(dates);
                                }
                                break;

                            case "Criterion2ToDate":
                                var value = (generalFiltersToSet["Criterion2ToDate"]).toString().trim();
                                if (value.length > 0) {
                                    var dates = Ext.util.Format.date(generalFiltersToSet["Criterion2ToDate"], Config.Data.DE_DATE_FORMAT_WINDOW);
                                    Ext.getCmp("Criterion2ToDate").setValue(dates);
                                }
                                break;
                        }
                    }
                });
            }
        }
        //General filters date
        for (var i = 1; i <= 2; i++) {
            if (generalFiltersToSet && generalFiltersToSet.hasOwnProperty("Criterion" + i) && generalFiltersToSet["Criterion" + i] != "(empty)") {
                var Criterion1 = generalFiltersToSet["Criterion" + i];
                Ext.getCmp("Criterion" + i + "Type").show();
                var showComponent = new Array();
                if (generalFiltersToSet.hasOwnProperty("Criterion" + i + "Type") && generalFiltersToSet["Criterion" + i + "Type"]) {
                    var CriterionType = generalFiltersToSet["Criterion" + i + "Type"];
                    switch (CriterionType) {
                        case "Before":
                            showComponent["Criterion" + i + "FromDate"] = true;
                            GFW["hideAndShowCrite" + i](showComponent);
                            break;
                        case "After":
                            showComponent["Criterion" + i + "FromDate"] = true;
                            GFW["hideAndShowCrite" + i](showComponent);
                            break;
                        case "On":
                            showComponent["Criterion" + i + "FromDate"] = true;
                            GFW["hideAndShowCrite" + i](showComponent);
                            break;
                        case "Between":
                            showComponent["Criterion" + i + "FromDate"] = true;
                            showComponent["Criterion" + i + "ToDate"] = true;
                            GFW["hideAndShowCrite" + i](showComponent);
                            break;
                        case "Time Range":
                            showComponent["Criterion" + i + "TimeFrom"] = true;
                            showComponent["Criterion" + i + "TimeTo"] = true;
                            GFW["hideAndShowCrite" + i](showComponent);
                            break;
                    }
                }
            } else {
                GFW["hideAndShowCrite" + i](showComponent, true);
            }
        }
    }
};
GFW["hideAndShowCrite1"] = function(data, includeType) {
    if (data === undefined) {
        data = new Array();
    }
    if (includeType) {
        console.log(includeType);
        if (data.hasOwnProperty("Criterion1Type")) {
            Ext.getCmp("Criterion1Type").show();
        } else {
            Ext.getCmp("Criterion1Type").hide();
        }
    }
    if (data.hasOwnProperty("Criterion1FromDate")) {
        Ext.getCmp("Criterion1FromDate").show();
    } else {
        Ext.getCmp("Criterion1FromDate").hide();
    }

    if (data.hasOwnProperty("Criterion1ToDate")) {
        Ext.getCmp("Criterion1ToDate").show();
    } else {
        Ext.getCmp("Criterion1ToDate").hide();
    }

    if (data.hasOwnProperty("Criterion1TimeFrom")) {
        Ext.getCmp("Criterion1TimeFrom").show();
    } else {
        Ext.getCmp("Criterion1TimeFrom").hide();
    }

    if (data.hasOwnProperty("Criterion1TimeTo")) {
        Ext.getCmp("Criterion1TimeTo").show();
    } else {
        Ext.getCmp("Criterion1TimeTo").hide();
    }

    if (data.hasOwnProperty("Criterion1TimeUnit")) {
        Ext.getCmp("Criterion1TimeUnit").show();
    } else {
        Ext.getCmp("Criterion1TimeUnit").hide();
    }
};

GFW["hideAndShowCrite2"] = function(data, includeType) {
    if (data === undefined) {
        data = new Array();
    }
    if (includeType) {
        if (data.hasOwnProperty("Criterion2Type")) {
            Ext.getCmp("Criterion2Type").show();
        } else {
            Ext.getCmp("Criterion2Type").hide();
        }
    }
    if (data.hasOwnProperty("Criterion2FromDate")) {
        Ext.getCmp("Criterion2FromDate").show();
    } else {
        Ext.getCmp("Criterion2FromDate").hide();
    }

    if (data.hasOwnProperty("Criterion2ToDate")) {
        Ext.getCmp("Criterion2ToDate").show();
    } else {
        Ext.getCmp("Criterion2ToDate").hide();
    }

    if (data.hasOwnProperty("Criterion2TimeFrom")) {
        Ext.getCmp("Criterion2TimeFrom").show();
    } else {
        Ext.getCmp("Criterion2TimeFrom").hide();
    }

    if (data.hasOwnProperty("Criterion2TimeTo")) {
        Ext.getCmp("Criterion2TimeTo").show();
    } else {
        Ext.getCmp("Criterion2TimeTo").hide();
    }

    if (data.hasOwnProperty("Criterion2TimeUnit")) {
        Ext.getCmp("Criterion2TimeUnit").show();
    } else {
        Ext.getCmp("Criterion2TimeUnit").hide();
    }
};