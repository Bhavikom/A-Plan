var GlbcurdTaskStore;
Ext.Loader.setConfig({ enabled: true, disableCaching: false });
Ext.Loader.setPath('APlan', 'js/advanced');
Ext.Loader.setPath('Ext.ux', 'js/ux');
Ext.Loader.setPath('Sch', 'js/advanced/Sch');
Ext.Loader.setPath('Gnt', 'js/advanced/Gnt');

//(getQueryVariable('HOST_URL') != "") ? Config.Data.HOST_URL = getQueryVariable('HOST_URL') : alert("Host Url Is Missing!");
if(getQueryVariable('HOST_URL') != "") {
    Ext.require([
        'APlan.model.Dependency',
        'APlan.model.Task',
        'APlan.model.Resource',
        'APlan.store.Task',
        'APlan.store.Resource',
        'APlan.store.Dependency',
        'APlan.store.Assignment',
        'Gnt.column.ResourceAssignment',
        'Gnt.model.Assignment',
        'Gnt.data.Calendar',
        'Gnt.data.calendar.BusinessTime',
        'Gnt.plugin.Printable',
        'Ext.tip.*',
//'Ext.grid.plugin.BufferedRenderer',
        'Ext.form.Panel',
        'Ext.ux.window.Notification',
        'Gnt.column.Scale',
        'Gnt.plugin.TaskEditor', //Uncomment line number 832 to 847 to enable task editing popup
        'Sch.plugin.TreeCellEditing',
        'Gnt.plugin.DependencyEditor',
//'Ext.grid.plugin.BufferedRenderer',
        'Gnt.plugin.Printable'
    ]);
}
// get requested locale from URL hash
var localeClass, locale = getQueryVariable('Lang');
switch (locale) {
    case 'ru': localeClass = 'RuRU'; break;
    case 'DE': localeClass = 'De'; break;
    case 'pl': localeClass = 'Pl'; break;
    case 'sv_SE': localeClass = 'SvSE'; break;
    case 'it': localeClass = 'It'; break;
    case 'nl': localeClass = 'Nl'; break;
    default: localeClass = 'En'; locale = 'en'; break;
}

// now when we know the requested locale
// we will require Ext to load gantt localization class
Ext.require('Gnt.locale.' + localeClass);
// also we need to include corresponding ExtJS localization file
Ext.Loader.loadScript({
    url: 'js/Sch/locale/' + localeClass + '.js',
    onLoad: function() {
        // after ExtJs locale is applied we invoke rendering
        // (when Ext will load all the required classes)
        Ext.onReady(function() {
            if (getQueryVariable('isWebViewer') == "0") { //Only for mobile devices
                APlan.loadProfileColumns();
                Ext.MessageBox.hide();
            } else if (getQueryVariable('token') && localStorage.getItem("user") &&
                (localStorage.getItem("user").indexOf(getQueryVariable('token')) < 0)) {
                localStorage.removeItem("user");
                location.href = location.origin + location.pathname;
            } else if (getQueryVariable('token') != "" && localStorage.getItem("user") &&
                (localStorage.getItem("user").indexOf(getQueryVariable('token')) >= 0)) {
                Ext.MessageBox.show({
                    msg: __("WE_ARE_SETTING_UP_PROFILE", " ") + __("PLEASE_WAIT"),
                    progressText: __("LOADING"),
                    width: 300,
                    wait: true,
                    waitConfig: { interval: 100 }
                });
                APlan.loadProfileColumns();
                Ext.MessageBox.hide();
            } else {
                if (localStorage && localStorage.getItem("user") &&
                    (localStorage.getItem("user").indexOf(getQueryVariable('token')) < 0)) {
                    location.href = location.origin + location.pathname;
                } else {
                    if (getQueryVariable('token')) {
                        location.href = location.origin + location.pathname;
                    } else {
                        APlan.confirmLogin();
                    }
                }
            }
        });
    }
});
Ext.onReady(function() {
    Ext.QuickTips.init();
});

TaskPriority = { Low: 0, Normal: 1, High: 2, Extra: 3 };
var ReadOnlyColumnIndex = [];
APlan = {
    ribbonHeaderContainer: null,
    GlobalFilters: null,
    confirmLogin: function() {
        APlan.loginWindow = new Ext.Window({
            layout: 'anchor',
            closable: false,
            draggable: false,
            resizable: false,
            width: (Ext.getBody().getViewSize().width > 510) ? 500 : Ext.getBody().getViewSize().width,
            plain: true,
            border: false,
            bbar: [{
                xtype: 'tbtext',
                text: __("COPY_RIGHT"),
                style: 'cursor: pointer;',
                listeners: {
                    el: {
                        click: function() {
                            var win = window.open("http://www.braintool.com", '_blank');
                            win.focus();
                        }
                    }
                },
            }],
            items: [loginPanel]
        });
        APlan.loginWindow.show();
    },
    loadProfileColumns: function() {
        Ext.MessageBox.show({
            msg: __("LOADING_PROFILE", " ") + __("PLEASE_WAIT"), //'Loading Profile, please wait...', //Lang
            progressText: __("LOADING"),
            width: 300,
            wait: true,
            waitConfig: { interval: 100 }
        });
        APlan.getAllProfiles();
        //Ext.MessageBox.hide();
    },

    init: function() {
        Sch.preset.Manager.registerPreset("dayNightShift", {
            timeColumnWidth: 35,
            rowHeight: 32,
            displayDateFormat: 'G:i',
            shiftIncrement: 1,
            shiftUnit: "DAY",
            timeResolution: {
                unit: "MINUTE",
                increment: 15
            },
            defaultSpan: 24,
            headerConfig: {
                bottom: {
                    unit: "HOUR",
                    increment: 1,
                    dateFormat: 'G'
                },
                middle: {
                    unit: "HOUR",
                    increment: 12,
                    renderer: function(startDate, endDate, headerConfig) {
                        // Setting align on the header config object
                        headerConfig.align = 'center';
                        if (startDate.getHours() === 0) {
                            // Setting a custom CSS on the header cell element
                            headerConfig.headerCls = 'nightShift';
                            return Ext.Date.format(startDate, 'M d') + ' Night Shift';
                        } else {
                            // Setting a custom CSS on the header cell element
                            headerConfig.headerCls = 'dayShift';
                            return Ext.Date.format(startDate, 'M d') + ' Day Shift';
                        }
                    }
                },
                top: {
                    unit: "DAY",
                    increment: 1,
                    dateFormat: 'd M Y'
                }
            }
        });
        Sch.preset.Manager.registerPreset("gujTimeAxis", {
            displayDateFormat: 'd.m.Y',

            shiftIncrement: 1,
            shiftUnit: "MONTH",

            columnLinesFor: 'bottom',
            timeResolution: {
                unit: "DAY",
                increment: 1
            },

            headerConfig: {
                top: {
                    unit: "MONTH",
                    dateFormat: "F Y"
                },
                bottom: {
                    unit: "DAY",
                    dateFormat: "d",
                    align: "center"
                },
                middle: {
                    unit: "WEEK",
                    dateFormat: "K\\W W"
                }
            }
        });
        var dependencyStore = Ext.create("APlan.store.Dependency");
        if (getQueryVariable("Control") == "1" || getQueryVariable("Control") == "3") {
            var resourceStore = Ext.create("Gnt.data.ResourceStore", {
                autoLoad: true,
                extend: 'Gnt.data.ResourceStore',
                model: 'APlan.model.Resource',
                groupField: 'Folder',
                rootProperty: {
                    loaded: true,
                    expanded: false
                },
                proxy: {
                    method: 'GET',
                    type: 'ajax',
                    api: {
                        read: 'Resource/GetResourceFolder'
                    },
                    reader: {
                        type: 'json',
                        rootProperty: 'resources'
                    }
                }
            });
            var assignmentStore = Ext.create("Gnt.data.AssignmentStore", {
                // Must pass a reference to resource store
                autoSync: true,
                autoLoad: true,
                proxy: {
                    method: 'GET',
                    type: 'ajax',
                    api: {
                        read: 'Resource/Assignments'
                    },
                    reader: {
                        type: 'json'
                    }
                },
                resourceStore: resourceStore
            });
            var taskStore = "";
            if ((getQueryVariable("from") != "dashboard")) { /*Load Data from server*/
                taskStore = Ext.create("Gnt.data.TaskStore", {
                    id: 'taskStore',
                    resourceStore: resourceStore,
                    assignmentStore: assignmentStore,
                    dependencyStore: dependencyStore,
                    extend: 'Gnt.data.TaskStore',
                    model: 'APlan.model.Task',
                    autoSync: false,
                    pageSize: 100,
                    /*trailingBufferZone: 50,
                     leadingBufferZone: 50,*/
                    numFromEdge: 5,
                    /*purgePageCount: 0,*/
                    /*scrollToLoadBuffer: 100,*/
                    rootVisible: false,
                    sortOnLoad: false,
                    forceFit: true,
                    autoLoad: (getQueryVariable("from") == "dashboard") ? false : true,
                    expanded: false,
                    proxy: {
                        type: 'ajax',
                        api: {
                            read: 'Tasks/Get?filter=&isCombine=' + false + '&Outline_Level=1&from=Ribbon',
                            create: 'Tasks/Create',
                            destroy: 'Tasks/Delete',
                            update: 'Tasks/Update'
                        },
                        writer: {
                            type: 'json',
                            rootProperty: 'taskdata',
                            encode: true,
                            allowSingle: false
                        },
                        reader: {
                            type: 'json'
                        }
                    },
                    listeners: {
                        beforeload: function() {
                            console.log("Start");
                            Ext.MessageBox.show({
                                msg: __("LOADING_TASKS_DATA", " ") + __("PLEASE_WAIT"), //Lang
                                progressText: __("LOADING"),
                                width: 300,
                                wait: true,
                                waitConfig: { interval: 100 }
                            });
                        }
                    }
                });
            } else {
                taskStore = Ext.create("Gnt.data.TaskStore", {
                    id: 'taskStore',
                    resourceStore: resourceStore,
                    assignmentStore: assignmentStore,
                    dependencyStore: dependencyStore,
                    extend: 'Gnt.data.TaskStore',
                    model: 'APlan.model.Task',
                    autoSync: false,
                    pageSize: 100,
                    trailingBufferZone: 50,
                    leadingBufferZone: 50,
                    numFromEdge: 5,
                    purgePageCount: 0,
                    scrollToLoadBuffer: 100,
                    rootVisible: false,
                    sortOnLoad: false,
                    forceFit: true,
                    autoLoad: true,
                    expanded: true,
                    proxy: {
                        type: 'memory',
                        reader: {
                            type: 'json'
                        },
                        data: DashBoardTaskData
                    },
                    listeners: {
                        beforeload: function() {
                            Ext.MessageBox.show({
                                msg: __("LOADING_DASHBOARD_DATA", " ") + __("PLEASE_WAIT"),
                                progressText: __("LOADING"),
                                width: 300,
                                wait: true,
                                waitConfig: { interval: 100 }
                            });
                        },
                        load: function() {
                            Ext.MessageBox.hide();
                        }
                    }
                });
            }
            var today = new Date();
            Ext.Date.clearTime(today);
            var start = Sch.util.Date.add(today, Sch.util.Date.MONTH, -5);
            var end = Sch.util.Date.add(today, Sch.util.Date.MONTH, 5);
            var grid = Ext.ComponentQuery.query("#ganttpanel-1013-body")[0];
            taskStore.on('load', function() {
                Ext.MessageBox.hide();
                var grid1 = Ext.ComponentQuery.query("#gv")[0];
            });
            taskStore.on('nodeexpand', function(clickedNode, childrens, callback, scope) {
                console.log(clickedNode.id);
                if (!Config.Data.ExpandedIds) {
                    Config.Data.ExpandedIds = "";
                }
                if (clickedNode.id != "root") {
                    Config.Data.ExpandedIds += clickedNode.id + ",";
                    var colsDetails = APlan.getColsDetail();
                    var form = new FormData();
                    form.append("Id", getQueryVariable('profile'));
                    form.append("ExpandedIds", Config.Data.ExpandedIds);
                    Config.Data.RequestheaderSettings.data = form;
                    Config.Data.RequestheaderSettings.url = "Update/Expanded";
                    $.ajax(Config.Data.RequestheaderSettings).done(function(response) {
                        var ResponseData = JSON.parse(response);
                    });
                }
            });
            taskStore.on('nodecollapse', function(clickedNode, childrens, callback, scope) {
                console.log(clickedNode.id);
                if (Config.Data.ExpandedIds && Config.Data.ExpandedIds.search(clickedNode.id + ",") >= 0 && clickedNode.id != "root") {
                    //Remove collapsed node id from expanded list
                    Config.Data.ExpandedIds = Config.Data.ExpandedIds.split(clickedNode.id + ",").join("");
                    var form = new FormData();
                    form.append("Id", getQueryVariable('profile'));
                    form.append("UserId", getQueryVariable("token"));
                    form.append("ExpandedIds", Config.Data.ExpandedIds);
                    Config.Data.RequestheaderSettings.data = form;
                    Config.Data.RequestheaderSettings.url = "Update/Expanded";
                    $.ajax(Config.Data.RequestheaderSettings).done(function(response) {
                        var ResponseData = JSON.parse(response);
                    });
                }
            });

            var g = this; var slider;
            var ganttPanel = Ext.create('Gnt.panel.Gantt', {
                id: "gv",
                height: Ext.getBody().getViewSize().height,
                width: Ext.getBody().getViewSize().width,
                headerHeight: 50,
                layout: 'fit',
                mask: true,
                viewPreset: myLocalStorage.getResolutionForGant(getQueryVariable("token")),
                startDate: new Date(2016, 07, 06),
                endDate: new Date(2016, 07, 25),
                infiniteScroll: true,
                animate: false,
                shiftIncrement: 1,
                multiSelect: true,
                highlightWeekends: true,
                showTodayLine: true,
                loadMask: true,
                enableDependencyDragDrop: true,
                snapToIncrement: true,
                showExactResizePosition: true,
                //title: 'Task',
                preventHeader: false,
                stateful: true,
                stateId: 'gantt',
                reserveScrollbar: true,
                viewConfig: {
                    stripeRows: true,
                    style: { overflow: 'auto', overflowX: 'hidden' },
                    emptyText: 'No data'
                },
                region: 'center',
                selModel: new Ext.selection.TreeModel({
                    ignoreRightMouseSelection: false,
                    mode: 'MULTI'
                }),
                eventRenderer: function(task) {
                    if (assignmentStore.findExact('TaskId', task.data.Id) >= 0) {
                        return { ctcls: 'resources-assigned' };
                    }
                },
                leftLabelField: {
                    dataIndex: 'Name',
                    editor: { xtype: 'textfield' }
                },
                rightLabelField: {
                    dataIndex: 'Id',
                    editor: { xtype: 'textfield' },
                    renderer: function(value, record) {
                        if (assignmentStore.findExact('TaskId', record.data.Id) >= 0) {
                            //return '<img src="Ribbon/images/Users.png" alt="resource" />';
                            return;
                        }
                    }
                },
                plugins: [
                    /*Ext.create("Gnt.plugin.TaskContextMenu"),*/ //Left side context menu
                    /*Ext.create("Gnt.plugin.DependencyEditor", {
                        hideOnBlur: true
                    }),*/
                    //gnt plugin for task editor in grid
                    /*Ext.create('Gnt.plugin.TaskEditor'),
                     {
                         ptype: 'scheduler_lines',
                         innerTpl: '<span class="line-text">{Text}</span>',
                         showHeaderElements: true,
                         store: new Ext.data.JsonStore({
                             fields: ['Date', 'Text', 'Cls'],
                             data: [
                                 {
                                     Date: new Date(2014, 3, 30),
                                     Text: 'Project kickoff',
                                     Cls: 'kickoff' // A CSS class
                                 }
                             ]
                        })
                     },*/

                    /*new Gnt.plugin.Printable({
                     printRenderer: function (task, tplData) {
                     if (task.isMilestone()) {
                     return;
                     } else if (task.isLeaf()) {
                     var availableWidth = tplData.width - 4,
                     progressWidth = Math.floor(availableWidth * task.get('PercentDone') / 100);

                     return {
                     // Style borders to act as background/progressbar
                     progressBarStyle: Ext.String.format('width:{2}px;border-left:{0}px solid #7971E2;border-right:{1}px solid #E5ECF5;', progressWidth, availableWidth - progressWidth, availableWidth)
                     };
                     } else {
                     var availableWidth = tplData.width - 2,
                     progressWidth = Math.floor(availableWidth * task.get('PercentDone') / 100);

                     return {
                     // Style borders to act as background/progressbar
                     progressBarStyle: Ext.String.format('width:{2}px;border-left:{0}px solid #FFF3A5;border-right:{1}px solid #FFBC00;', progressWidth, availableWidth - progressWidth, availableWidth)
                     };
                     }
                     },

                     beforePrint: function (sched) {
                     var v = sched.getSchedulingView();
                     this.oldRenderer = v.eventRenderer;
                     v.eventRenderer = this.printRenderer;
                     },

                     afterPrint: function (sched) {
                     var v = sched.getSchedulingView();
                     v.eventRenderer = this.oldRenderer;
                     }
                     }),
                     */
                    /*Ext.create('Sch.plugin.TreeCellEditing', {
                        clicksToEdit: 2,
                        listeners: {
                            edit: function() {
                                //g.assignmentStore.sync();
                            },
                            beforeedit: function(col, row) {
                                var rights = row.record.get('Permission');
                                var returnValue = false;
                                switch (rights) {
                                    case "ALL_RIGHTS":
                                        returnValue = true;
                                        break;

                                    case "EDIT":
                                        returnValue = true;
                                        break;

                                    case "READ":
                                        returnValue = false;
                                        break;

                                    case "DEFAULT":
                                        returnValue = false;
                                        break;

                                    case "READ_ONLY_FOR_PLANNED_DATA":
                                        returnValue = false;
                                        break;

                                    case "READ_ONLY_FOR_COSTS":
                                        returnValue = false;
                                        break;

                                    case "HIDE_COSTS":
                                        returnValue = false;
                                        break;

                                    case "READ_ONLY_FOR_LIMITS":
                                        returnValue = false;
                                        break;
                                }
                                return returnValue;
                            }
                        }
                    }),*/
                    /*this.depEditor = new Gnt.plugin.DependencyEditor({
                        showLag: true,
                        constrain: true,
                        buttons: [
                            {
                                text: 'Ok',
                                scope: this,
                                handler: function() {
                                    var formPanel = g.depEditor;
                                    formPanel.getForm().updateRecord(formPanel.dependencyRecord);
                                    g.depEditor.collapse();
                                }
                            },
                            {
                                text: 'Cancel',
                                scope: this,
                                handler: function() {
                                    g.depEditor.collapse();
                                }
                            },
                            {
                                text: 'Delete',
                                scope: this,
                                handler: function() {
                                    var formPanel = g.depEditor,
                                        record = g.depEditor.dependencyRecord;
                                    g.dependencyStore.remove(record);
                                    formPanel.collapse();
                                }
                            }
                        ]
                    })*/
                ],
                listeners: {
                    afterrender: function(cmp, eOpts) {
                        ganttPanel.setHeight(
                            (Ext.getBody().getViewSize().width <= 1115)
                                ? Ext.getBody().getViewSize().height-55
                                : Ext.getBody().getViewSize().height-147
                        );
                        ganttPanel.setWidth(Ext.getBody().getViewSize().width);
                        //Ext.get("TasksId").addCls("x-column-header-sort-ASC"); //Default is ascending so set ascending image
                        if (SelectedNode != "0") {
                            AddSelectionToNode(SelectedNode);
                        }
                    }
                },
                columns: Staticcolumns,
                isCellEditable: function(col, row) {
                    console.log(row);
                    /*var record = ds.getAt(row);
                     if (record.get('shownonDrawing') == 'Same As New Part') {
                     return false;
                     }
                     return Ext.grid.ColumnModel.prototype.isCellEditable.call(this, row, col);*/
                },
                taskStore: taskStore,
                layout: 'border',
                //Left side grid area
                lockedGridConfig: {
                    width: APlan.GetTreeWidthFromlocalDatabase(),
                    collapsible: true,
                    header: false,
                    split: true,
                    region: 'west',
                    scroll: true,
                    listeners: {
                        itemexpand: function() {

                        },
                        itemcollapse: function() {

                        }
                    }
                },
                //Right side grid area
                schedulerConfig: {
                    region: 'center'
                },
                lockedViewConfig: {
                    plugins: {
                        ptype: 'treeviewdragdrop'
                    }
                },
                // Tooltip template for show tooltip on task with startdate, enddate, percentage Done
                tooltipTpl: new Ext.XTemplate(
                    '<strong class="tipHeader">{Name}</strong>' +
                    '<table class="taskTip">' +
                    '<tr><td>' + __("START", ":") + '</td> <td align="right">{[values._record.getDisplayStartDate(Config.Data.DE_DATE_FORMAT)]}</td></tr>' + //Lang
                    '<tr><td>' + __("END", ":") + '</td> <td align="right">{[values._record.getDisplayEndDate(Config.Data.DE_DATE_FORMAT)]}</td></tr>' + //Lang
                    '<tr><td>' + __("PROGRESS", ":") + '</td><td align="right">{[ Math.round(values.PercentDone)]}%</td></tr>' + //Lang
                    '</table>'
                )
            });
            ganttPanel.setRowHeight(50);
        }
        if (getQueryVariable("isWebViewer") == "1") { //if webviewer then show ribbon else not
            APlan.ribbonHeaderContainer = Ext.create('Ext.tab.Panel', {
                height: 140,
                activeTab: 0,
                //renderTo: document.body,
                cls: "customHeaderBack",
                items: ribbonHeader,
                id: "ribbonContainer",
                listeners: {
                    afterrender: function() {
                        setActiveRibbonFilters();
                        Ext.getStore('allProfilesForCombo').loadData(APlan.allProfiles);
                        Ext.getCmp("profileSelection").setValue(getQueryVariable("profile"));
                        var savedGeneralFilter = GLOBAL_GENERAL_FILTERS;
                        if (savedGeneralFilter && savedGeneralFilter.length > 1) {
                            Ext.getCmp('folderFilterOnOff').enable();
                        } else {
                            Ext.getCmp('folderFilterOnOff').disable();
                        }
                        var savedFilterGeneral = GLOBAL_GENERAL_FILTERS;
                        if (
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
                        if (getQueryVariable("isWebViewer") == "1" && getQueryVariable("Control") == "3") {
                            Ext.getCmp("showresource").toggle(true);
                        }

                        var SamplePanel = Ext.extend(Ext.Panel, {
                            style: 'position:absolute; right:22px; top:5px;',
                            bodyStyle: 'padding:10px',
                            renderTo: Ext.getBody(),
                            scrollable: true,
                            width: 160,
                            height: 40
                        });

                        new SamplePanel({
                            tbar: [{
                                xtype: 'splitbutton',
                                text: __('MY_ACCOUNT'),
                                iconCls: 'add16',
                                menu: [
                                    {
                                        text: __('MY_PROFILE'),
                                        handler: function() {
                                            MyProfile.open();
                                        }
                                    },
                                    {
                                        text: __('LOGOUT'),
                                        handler: function() {
                                            APlan.callLogout();
                                        }
                                    }
                                ]
                            }]
                        });
                    }
                }
            });
            var container1 = Ext.create('Ext.container.Container', {
                width: '100%',
                items: APlan.ribbonHeaderContainer,
                renderTo: document.body,
                cls: "tab-container-custom",
                height: (Ext.getBody().getViewSize().width <= 1115) ? 50 : 142,
                html: "<button class='menu-open-close' onclick='APlan.openCloseMenu()'>" +
                "<div></div>"+
                "<div></div>"+
                "<div></div>"+
                "</button>"
            });
            if((Ext.getBody().getViewSize().width <= 1115)) {
                APlan.ribbonHeaderContainer.hide();
            } else {
                APlan.ribbonHeaderContainer.show();
            }
        }

        if (getQueryVariable("Control") == "1" || getQueryVariable("Control") == "3") {
            ganttPanel.render(Ext.getBody());
            ganttPanel.lockedGrid.view.on('itemclick', function(view, task) {
                ganttPanel.getSchedulingView().scrollEventIntoView(task, true, true);
            });
            ganttPanel.on('columnmove', function(ct, column, fromIdx, toIdx, eOpts) {
                if (column.xtype == "timeaxiscolumn")
                    return false;
                var colsDetails = APlan.getColsDetail();
                var form = new FormData();
                form.append("Id", getQueryVariable('profile'));
                form.append("Columns", JSON.stringify(colsDetails));
                APlan.updateProfile(form, Config.Data.RequestheaderSettings);
            });
            ganttPanel.on('columnresize', function(ct, column, fromIdx, toIdx, eOpts) {
                APlan.SetTreeWidthInlocalDatabase();
                if (column.xtype == "timeaxiscolumn")
                    return false;
                var colsDetails = APlan.getColsDetail();
                var form = new FormData();
                form.append("Id", getQueryVariable('profile'));
                form.append("Columns", JSON.stringify(colsDetails));
                APlan.updateProfile(form, Config.Data.RequestheaderSettings);
            });
            ganttPanel.setReadOnly(true);
        }
        if (getQueryVariable("Control") == "2" || getQueryVariable("Control") == "3" || getQueryVariable("isWebViewer") == "1") {
            APlan.initResourceControl(dependencyStore);
        }
    },
    openCloseMenu: function() {
        if(APlan.ribbonHeaderContainer.isVisible() == true)
            APlan.ribbonHeaderContainer.hide();
        else
            APlan.ribbonHeaderContainer.show();
    },
    callLogout: function() {
        Ext.MessageBox.show({
            msg: __("PLEASE_WAIT"),
            progressText: __("LOADING"),
            width: 300,
            wait: true,
            waitConfig: { interval: 1000}
        });
        var serviceSettings = Config.Data.RequestheaderSettings;
        serviceSettings.url = "Logout";
        serviceSettings.headers.Authorization = getQueryVariable("token");
        var ajaxCall = $.ajax(serviceSettings).done(function(response) {
            Ext.MessageBox.hide();
            var ResponseData = JSON.parse(response);
            if (ResponseData.hasOwnProperty("Payload") && ResponseData.Payload) {
                if(ResponseData.Payload.Id > 0) {
                    if (localStorage && localStorage.getItem("user")) {
                        localStorage.removeItem("user");
                        location.href = location.origin + location.pathname;
                    }
                    return 1;
                }
            }
        }).fail(function(response) {
            Ext.MessageBox.hide();
            var Statusheaders = ajaxCall.getResponseHeader("x-status");
            if (Statusheaders > 0) {
                var msg = GET_ERROR_DETAIL(Statusheaders);
                Ext.Msg.alert('Error', msg + ", " + __("LOGOUT"));
            }
            var SingleError = { "Id": GUID(), "ErrorCode": Statusheaders, "Message": response.statusText };
            InsertLog(SingleError);
            return 0;
        });
        return 0;
    },
    updateProfile: function(formData, serviceSettings) {
        serviceSettings.data = formData;
        serviceSettings.url = "Update/Profile";
        $.ajax(serviceSettings).done(function(response) {
            var ResponseData = JSON.parse(response);
            if (ResponseData.hasOwnProperty("Payload") && ResponseData.Payload) {
                Ext.create('widget.uxNotification', {
                    title: 'Profile', width: 300, position: 'tr', manager: 'instructions',
                    cls: 'ux-notification-light', iconCls: 'ux-notification-icon-information', html: __('PROFILE_SAVED_SUCCESSFULLY'),
                    autoClose: true, autoCloseDelay: 5000, slideBackDuration: 500, slideInAnimation: 'bounceOut',
                    slideBackAnimation: 'easeIn'
                }).show();
            }
        });
        return null;
    },
    initResourceControl: function(dependencyStore) {
        var dependencyStoreResource = dependencyStore;
        var resourceStoreResource = Ext.create("Gnt.data.ResourceStore", {
            autoLoad: true,
            extend: 'Gnt.data.ResourceStore',
            model: 'APlan.model.Resource',
            groupField: 'Folder',
            rootProperty: {
                loaded: true,
                expanded: false
            },
            proxy: {
                method: 'GET',
                type: 'ajax',
                api: {
                    read: 'Resource/GetResourceFolder'
                },
                reader: {
                    type: 'json',
                    rootProperty: 'resources'
                }
            }
        });
        var assignmentStoreResource = Ext.create("Gnt.data.AssignmentStore", {
            // Must pass a reference to resource store
            autoSync: true,
            autoLoad: true,
            proxy: {
                method: 'GET',
                type: 'ajax',
                api: {
                    read: 'Resource/Assignments'
                },
                reader: {
                    type: 'json'
                }
            },
            resourceStore: resourceStoreResource
        });
        var taskStoreResource = "";
        taskStoreResource = Ext.create("Gnt.data.TaskStore", {
            id: 'resourceStore',
            resourceStore: resourceStoreResource,
            assignmentStore: assignmentStoreResource,
            //dependencyStore: dependencyStoreResource,
            extend: 'Gnt.data.TaskStore',
            model: 'APlan.model.Task',
            autoSync: false,
            pageSize: 100,
            /*trailingBufferZone: 50,
             leadingBufferZone: 50,*/
            numFromEdge: 5,
            /*purgePageCount: 0,*/
            /*scrollToLoadBuffer: 100,*/
            rootVisible: false,
            sortOnLoad: false,
            forceFit: true,
            autoLoad: (getQueryVariable("from") == "dashboard") ? false : true,
            expanded: false,
            proxy: {
                type: 'ajax',
                api: {
                    read: 'Resource/Get?filter=&isCombine=' + false + '&Outline_Level=1&from=Ribbon',
                    create: 'Tasks/Create',
                    destroy: 'Tasks/Delete',
                    update: 'Tasks/Update'
                },
                writer: {
                    type: 'json',
                    rootProperty: 'taskdata',
                    encode: true,
                    allowSingle: false
                },
                reader: {
                    type: 'json'
                }
            },
            listeners: {
                beforeload: function() {
                    console.log("Start");
                    Ext.MessageBox.show({
                        msg: __("LOADING_TASKS_DATA", " ") + __("PLEASE_WAIT"),
                        progressText: __("LOADING"),
                        width: 300,
                        wait: true,
                        waitConfig: { interval: 100 }
                    });
                }
            }
        });

        var today = new Date();
        Ext.Date.clearTime(today);
        var start = Sch.util.Date.add(today, Sch.util.Date.MONTH, -5);
        var end = Sch.util.Date.add(today, Sch.util.Date.MONTH, 5);
        var grid = Ext.ComponentQuery.query("#ganttpanel-1013-body")[0];
        taskStoreResource.on('load', function() {
            Ext.MessageBox.hide();
            var grid1 = Ext.ComponentQuery.query("#resourceGV")[0];
        });
        taskStoreResource.on('nodeexpand', function(clickedNode, childrens, callback, scope) {

        });
        taskStoreResource.on('nodecollapse', function(clickedNode, childrens, callback, scope) {

        });
        var ResourceganttPanel = Ext.create('Gnt.panel.Gantt', {
            id: "resourceGV",
            height: Ext.getBody().getViewSize().height,
            width: Ext.getBody().getViewSize().width,
            headerHeight: 50,
            mask: true,
            viewPreset: myLocalStorage.getResolutionForGant(getQueryVariable("token")),
            startDate: new Date(2016, 07, 06),
            endDate: new Date(2016, 07, 25),
            infiniteScroll: true,
            animate: false,
            shiftIncrement: 1,
            multiSelect: true,
            highlightWeekends: true,
            showTodayLine: true,
            loadMask: true,
            enableDependencyDragDrop: true,
            snapToIncrement: true,
            showExactResizePosition: true,
            title: (getQueryVariable("isWebViewer") == '1') ? __("RESOURCES") : "",
            //preventHeader: true,
            stateful: true,
            stateId: 'gantt',
            reserveScrollbar: true,
            viewConfig: {
                stripeRows: true,
                style: { overflow: 'auto', overflowX: 'hidden' },
                emptyText: 'No data'
            },
            region: 'center',
            selModel: new Ext.selection.TreeModel({
                ignoreRightMouseSelection: false,
                mode: 'MULTI'
            }),
            eventRenderer: function(task) {
                if (assignmentStoreResource.findExact('TaskId', task.data.Id) >= 0) {
                    return { ctcls: 'resources-assigned' };
                }
            },
            leftLabelField: {
                dataIndex: 'Name',
                editor: { xtype: 'textfield' }
            },
            rightLabelField: {
                dataIndex: 'Id',
                editor: { xtype: 'textfield' },
                renderer: function(value, record) {
                    if (assignmentStoreResource.findExact('TaskId', record.data.Id) >= 0) {
                        //return '<img src="Ribbon/images/Users.png" alt="resource" />';
                        return;
                    }
                }
            },
            plugins: [
                /*Ext.create("Gnt.plugin.TaskContextMenu"),*/ //Left side context menu
                /*Ext.create("Gnt.plugin.DependencyEditor", {
                    hideOnBlur: true
                }),*/
                //gnt plugin for task editor in grid
                /*Ext.create('Gnt.plugin.TaskEditorTaskEditor'), {
                    ptype: 'scheduler_lines',
                    innerTpl: '<span class="line-text">{Text}</span>',
                    showHeaderElements: true,
                    store: new Ext.data.JsonStore({
                        fields: ['Date', 'Text', 'Cls'],
                        data: [{
                            Date: new Date(2014, 3, 30),
                            Text: 'Project kickoff',
                            Cls: 'kickoff' // A CSS class
                        }]
                    })
                },*/

                /*new Gnt.plugin.Printable({
                 printRenderer: function (task, tplData) {
                 if (task.isMilestone()) {
                 return;
                 } else if (task.isLeaf()) {
                 var availableWidth = tplData.width - 4,
                 progressWidth = Math.floor(availableWidth * task.get('PercentDone') / 100);

                 return {
                 // Style borders to act as background/progressbar
                 progressBarStyle: Ext.String.format('width:{2}px;border-left:{0}px solid #7971E2;border-right:{1}px solid #E5ECF5;', progressWidth, availableWidth - progressWidth, availableWidth)
                 };
                 } else {
                 var availableWidth = tplData.width - 2,
                 progressWidth = Math.floor(availableWidth * task.get('PercentDone') / 100);

                 return {
                 // Style borders to act as background/progressbar
                 progressBarStyle: Ext.String.format('width:{2}px;border-left:{0}px solid #FFF3A5;border-right:{1}px solid #FFBC00;', progressWidth, availableWidth - progressWidth, availableWidth)
                 };
                 }
                 },

                 beforePrint: function (sched) {
                 var v = sched.getSchedulingView();
                 this.oldRenderer = v.eventRenderer;
                 v.eventRenderer = this.printRenderer;
                 },

                 afterPrint: function (sched) {
                 var v = sched.getSchedulingView();
                 v.eventRenderer = this.oldRenderer;
                 }
                 }),
                 */
                Ext.create('Sch.plugin.TreeCellEditing', {
                    clicksToEdit: 2,
                    listeners: {
                        edit: function() {
                            //g.assignmentStore.sync();
                        },
                        beforeedit: function(col, row) {
                            console.log(row);
                        }
                    }
                }),
                /*this.depEditor = new Gnt.plugin.DependencyEditor({
                    showLag: true,
                    constrain: true,
                    buttons: [
                        {
                            text: 'Ok',
                            scope: this,
                            handler: function() {
                                var formPanel = g.depEditor;
                                formPanel.getForm().updateRecord(formPanel.dependencyRecord);
                                g.depEditor.collapse();
                            }
                        },
                        {
                            text: 'Cancel',
                            scope: this,
                            handler: function() {
                                g.depEditor.collapse();
                            }
                        },
                        {
                            text: 'Delete',
                            scope: this,
                            handler: function() {
                                var formPanel = g.depEditor,
                                    record = g.depEditor.dependencyRecord;
                                g.dependencyStore.remove(record);
                                formPanel.collapse();
                            }
                        }
                    ]
                })*/
            ],
            listeners: {
                afterrender: function(cmp, eOpts) {
                    ResourceganttPanel.setHeight(
                        (Ext.getBody().getViewSize().width <= 1115)
                            ? Ext.getBody().getViewSize().height-55
                            : Ext.getBody().getViewSize().height-147
                    );
                    ResourceganttPanel.setWidth(Ext.getBody().getViewSize().width);
                    //Ext.get("TasksId").addCls("x-column-header-sort-ASC"); //Default is ascending so set ascending image
                    //if (SelectedNode != "0") {
                    //AddSelectionToNode(SelectedNode);
                    //}
                }
            },
            columns: StaticcolumnsResource,
            taskStore: taskStoreResource,
            layout: 'border',

            //Left side grid area
            lockedGridConfig: {
                width: APlan.GetTreeWidthFromlocalDatabase(),
                collapsible: true,
                header: false,
                split: true,
                region: 'west',
                scroll: true,
                listeners: {
                    itemexpand: function() {

                    },
                    itemcollapse: function() {

                    }
                }
            },
            //Right side grid area
            schedulerConfig: {
                region: 'center'
            },

            lockedViewConfig: {
                plugins: {
                    ptype: 'treeviewdragdrop'
                }
            },
            // Tooltip template for show tooltip on task with startdate, enddate, percentage Done
            tooltipTpl: new Ext.XTemplate(
                '<strong class="tipHeader">{Name}</strong>' +
                '<table class="taskTip">' +
                '<tr><td>' + __("START", ":") + '</td> <td align="right">{[values._record.getDisplayStartDate(Config.Data.DE_DATE_FORMAT)]}</td></tr>' +
                '<tr><td>' + __("END", ":") + '</td> <td align="right">{[values._record.getDisplayEndDate(Config.Data.DE_DATE_FORMAT)]}</td></tr>' +
                '<tr><td>' + __("PROGRESS", ":") + '</td><td align="right">{[ Math.round(values.PercentDone)]}%</td></tr>' +
                '</table>'
            )
        });
        ResourceganttPanel.lockedGrid.view.on('itemclick', function(view, task) {
            ResourceganttPanel.getSchedulingView().scrollEventIntoView(task, false, true);
        });
        ResourceganttPanel.setRowHeight(50);
        ResourceganttPanel.setReadOnly(true);
        ResourceganttPanel.render(Ext.getBody());
        if (getQueryVariable("isWebViewer") == "1" && getQueryVariable("Control") == "1") {
            ResourceganttPanel.hide();
        }
    },
    getColsDetail: function() {
        var grid = Ext.ComponentQuery.query("#gv")[0];
        var colsDetail = grid.getColumns();
        var colsDetails = [];
        colsDetail.forEach(function(single) {
            if (single.xtype != "timeaxiscolumn") {
                colsDetails.push({ "Id": single.id, "Name": single.text, "Show": (single.hidden == true) ? 0 : 1, "Width": single.width });
            }
        });
        return colsDetails;
    },
    SetTreeWidthInlocalDatabase: function() {
        var grid = Ext.ComponentQuery.query("#gv")[0];
        console.log("Set tree width to : " + grid);
        localStorage.setItem('LeftTreeWidth', grid.lockedGrid.getWidth());
    },
    GetTreeWidthFromlocalDatabase: function() {
        var width = $(window).width() / 3;
        var StoredWidth = 0;
        if (localStorage.getItem("LeftTreeWidth") !== null) {
            StoredWidth = parseFloat(localStorage.getItem('LeftTreeWidth'));
            if (StoredWidth > 0) {
                console.log("Set tree width to : " + StoredWidth);
                return StoredWidth;
            }
        }
        console.log("Set tree width to : " + width);
        return width;
    },
    allProfiles: null,
    shouldShowButton: 0,
    getAllProfiles: function() {
        var settings = Config.Data.RequestheaderSettings;
        settings.url = "Get/Profiles";
        var form = new FormData();
        form.append("Lang", getQueryVariable("Lang"));
        //form.append("OrgId", getQueryVariable("orgId"));
        settings.data = form;
        var ajaxCall = $.ajax(settings).done(function(response) {
            var Statusheaders = ajaxCall.getResponseHeader("x-status");
            APlan.shouldShowButton = ajaxCall.getResponseHeader("x-message");
            if (Statusheaders && Statusheaders > 0) {
                var msg = GET_ERROR_DETAIL(Statusheaders);
                Ext.MessageBox.hide();
                Ext.Msg.alert('Error', msg + ", " + __("WHILE_FETCHING_PROFILE"));
            } else {
                var ResponseData = JSON.parse(response);
                if (ResponseData) {
                    APlan.allProfiles = ResponseData.Payload;
                    APlan.setColumnsDetail(APlan.allProfiles);
                    Ext.MessageBox.hide();
                    APlan.init();
                } else {
                    Ext.MessageBox.hide();
                }
            }
        }).fail(function(response) {
            Ext.MessageBox.hide();
            var Statusheaders = ajaxCall.getResponseHeader("x-status");
            if (Statusheaders > 0) {
                var msg = GET_ERROR_DETAIL(Statusheaders);
                Ext.Msg.alert('Error', msg + ", " + __("WHILE_FETCHING_PROFILE"));
            }
            var SingleError = { "Id": GUID(), "ErrorCode": Statusheaders, "Message": response.statusText };
            InsertLog(SingleError);
        });
    },
    setColumnsDetail: function(profilesDetail) {
        var Cols = [], Cols2 = [];
        if (profilesDetail && profilesDetail.length > 0) {
            for (var index = 0; index < profilesDetail.length; index++) {
                if (profilesDetail[index].Id == getQueryVariable('profile')) {
                    //Set Expanded Ids
                    var Attributes = profilesDetail[index].Attributes;
                    if (Attributes.length > 0) {
                        for (var index2 = 0; index2 < Attributes.length; index2++) {
                            if (Attributes[index2].AttributeName == "ExpandedIds") {
                                Config.Data.ExpandedIds = Attributes[index2].AttributeValue;
                            }
                            if (Attributes[index2].AttributeName == "RibbonFilter") {
                                GLOBAL_RIBBON_FILTER = Attributes[index2].AttributeValue;
                            }
                            if (Attributes[index2].AttributeName == "GeneralFilter") {
                                if (Attributes[index2].AttributeValue.length > 0) {
                                    GLOBAL_GENERAL_FILTERS = $.parseJSON(Attributes[index2].AttributeValue);
                                }
                            }
                            if (Attributes[index2].AttributeName == "Columns") {
                                var ColumnsValues = Attributes[index2].AttributeValue;
                                ColumnsValues = ColumnsValues.split("'").join('"');
                                var ColsDetail = $.parseJSON(ColumnsValues);
                                for (var index3 = 0; index3 < ColsDetail.length; index3++) {

                                    for (var index4 = 0; index4 < Staticcolumns.length; index4++) {
                                        ReadOnlyColumnIndex[ColsDetail[index3].Id] = index4;
                                        if (Staticcolumns[index4].id == ColsDetail[index3].Id) {
                                            Staticcolumns[index4].width = parseInt(ColsDetail[index3].Width);
                                            Staticcolumns[index4].hidden = (ColsDetail[index3].Show == 1) ? false : true;
                                            Staticcolumns[index4].header = ColsDetail[index3].Name;
                                            Cols.push(Staticcolumns[index4]);
                                        }
                                    }

                                    for (var index4 = 0; index4 < StaticcolumnsResource.length; index4++) {
                                        ReadOnlyColumnIndex[ColsDetail[index3].Id] = index4;
                                        if (StaticcolumnsResource[index4].id == ColsDetail[index3].Id) {
                                            StaticcolumnsResource[index4].width = parseInt(ColsDetail[index3].Width);
                                            StaticcolumnsResource[index4].hidden = (ColsDetail[index3].Show == 1) ? false : true;
                                            StaticcolumnsResource[index4].header = ColsDetail[index3].Name;
                                            if (ColsDetail[index3].Id == "TasksId") {
                                                StaticcolumnsResource[index4].header = "Res./Task";
                                            }
                                            StaticcolumnsResource[index4].id += StaticcolumnsResource[index4].id + "R";
                                            Cols2.push(StaticcolumnsResource[index4]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Cols.push(Staticcolumns[Staticcolumns.length - 1]);
            Staticcolumns = Cols;
            Cols2.push(StaticcolumnsResource[StaticcolumnsResource.length - 1]);
            StaticcolumnsResource = Cols2;
        }
    }
};
