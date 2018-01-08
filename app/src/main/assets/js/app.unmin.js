var GlbcurdTaskStore;
Ext.Loader.setConfig({ enabled: true, disableCaching: false });
Ext.Loader.setPath('APlan', 'js/advanced');
Ext.Loader.setPath('Ext.ux', 'ux');
Ext.Loader.setPath('Sch', 'js/advanced/Sch');
Ext.Loader.setPath('Gnt', 'js/advanced/Gnt');
(getQueryVariable('HOST_URL') != "") ? Config.Data.HOST_URL = getQueryVariable('HOST_URL'): alert("Host Url Is Missing!");
var Staticcolumns = [
    {
        header: 'No',
        name: 'SequenceNumber',
        type: 'string',
        id:  "NoId",
        order: 1,
        tdCls: 'nocell',
        sortable: true,
        width: 30,
        locked: true,
        menuDisabled: true,
        renderer: function (e, t, n) {
            return n.getSequenceNumber();
        }
    },
    {
        xtype: 'namecolumn',
        header: 'Tasks',
        dataIndex: 'Name',
        width: 150,
        id:  "TasksId",
        tdCls: 'namecell',
        sortable: true,
        autoScroll: true,
        menuDisabled: true,
        cls: "x-column-header-sort-ASC",
        listeners: {
            mouseover: function () {

            }
        },
        renderer: function (v, m, r) {
            if (r.get('IsLeaf')) {
                m.css = 'task';
            } else {
                m.css = 'parent';
            }
            if (r.data.Object == "0") {
                return r.data.Name;
            }
            return r.data.Name + ' (' + r.data.Object + ')';
        }
    },
    {
        header: 'Status',
        sortable: false,
        id:  "StatusId",
        editable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            var returnData = [];
            returnData = r.data.StatusObj.split(',');
            var a = '';
            if(returnData.length <= 1) {
                return a;
            }
            for (var i = 0; i < returnData.length; i++) {
                //console.log(returnData[i]);
                switch(returnData[i]) {
                    case "ShowStarted":
                        a = a + '<img src="Ribbon/images/Icons/07_task_running_16.png">';
                        break;
                    case "ShowOverdueFinished":
                        a = a + '<img src="Ribbon/images/Icons/04_task_overdue_16.png">';
                        break;
                    case "ShowFinishToday":
                        a = a + '<img src="Ribbon/images/Icons/05_task_today_16.png">';
                        break;
                    case "ShowFinishWeek":
                        a = a + '<img src="Ribbon/images/Icons/06_task_thisweek_16.png">';
                        break;
                    case "ShowRunning":
                        //a = a + '&nbsp;<img src="Ribbon/images/Icons/06_task_running_16-.png">';
                        break;
                    case "ShowNoTime":
                        a = a + '<img src="Ribbon/images/Icons/09_task_missing_16.png">';
                        break;
                    case "ShowFinished":
                        a = a + '<img src="Ribbon/images/Icons/10_check_16.png">';
                        break;
                    case "ShowPassiv":
                        a = a + '<img src="Ribbon/images/Icons/11_task_passive_16.png">';
                        break;
                    case "ShowCanceled":
                        a = a + '<img src="Ribbon/images/Icons/12_task_canceled_16.png">';
                        break;
                    case "ShowArrow":
                        a = a + '<img src="Ribbon/images/Icons/16_arrow_16.png">';
                        break;
                    case "ShowCritical":
                        a = a + '<img src="Ribbon/images/Icons/17_flash_16.png">';
                        break;
                    case "ShowQuestion":
                        a = a + '<img src="Ribbon/images/Icons/18_question_16.png">';
                        break;
                    case "ShowReminder":
                        a = a + '<img src="Ribbon/images/Icons/19_warning_16.png">';
                        break;
                    case "ShowOverdueLimit":
                        a = a + '<img src="Ribbon/images/Icons/32_Limit_16.png">';
                        break;
                    case "ShowOverdueProgn":
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/33_time_overrun_16.png">';
                        break;
                    case "ShowOverdueCost":
                        a = a + '<img src="Ribbon/images/Icons/33_time_overrun_16333.png" alt="Overdue Cost">';
                        break;
                    case "ShowPending":
                        //a = a + '<img src="Ribbon/images/Icons/08_task_waiting_16.png" alt="Overdue Cost">';
                        break;
                    case "ShowCapacityTooLow":
                        //a = a + '<img src="Ribbon/images/Icons/CapacityTooLow.png" alt="Overdue Cost">';
                        break;
                    case "ShowCapacityTooHigh":
                        //a = a + '<img src="Ribbon/images/Icons/CapacityTooHigh.png" alt="Overdue Cost">';
                        break;

                }
            }
            return a;
        },
        editor: new Ext.form.DateField({
            allowBlank: false,
            format: 'm/d/y'
        }),
        width: 50,
    },
    {
        header: 'Traffic Light',
        id:  "TrafficLightId",
        editable: false,
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            var returnData = [];
            switch (r.data.TrafficLightObj){
                case "TrafficLightYellow":
                    return '<img  src="Ribbon/images/Icons/trafficlight_yellow.png">';
                    break;
                case "TrafficLightRed":
                    return '<img src="Ribbon/images/Icons/trafficlight_red.png">';
                    break;
                case "TrafficLightGreen":
                    return '<img src="Ribbon/images/Icons/trafficlight_green.png">';
                    break;
            }
            return "";
        },
        editor: new Ext.form.DateField({
            allowBlank: false,
            format: 'm/d/y'
        }),
        width: 80,
    },
    {
        header: 'Resources',
        id:  "AssignedResourcesId",
        width: 150,
        sortable: false,
        xtype: 'resourceassignmentcolumn',
        menuDisabled: true,
    },
    {
        xtype: 'startdatecolumn',
        header: 'Begin Pl\'d\'',
        id:  "StartId",
        dataIndex: 'StartDate',
        width: 80,
        sortable: false,
        menuDisabled: true,
        format : Config.Data.DE_DATE_FORMAT,
    },
    {
        xtype: 'enddatecolumn',
        header: 'End Pl\'d\'',
        id:  "EndId",
        dataIndex: 'EndDate',
        width: 80,
        sortable: false,
        menuDisabled: true,
        format : Config.Data.DE_DATE_FORMAT,
    },
    /*Duration cols Start*/
    {
        xtype: 'durationcolumn',
        id:  "durationId",
        sortable: false,
        menuDisabled: true,
        renderer: function (e, t, n) {
            if (!Ext.isNumber(e)) return "";
            if (!n.isEditable(this.dataIndex)) {
                t.tdCls = (t.tdCls || "") + " sch-column-readonly";
            }
            var r = n.getDurationUnit();
            var diffDays = parseFloat((n.data.EndDate - n.data.StartDate) / (1000 * 60 * 60 * 24)).toFixed(1);
            if (diffDays <= 1) {
                return diffDays + ' day ';
            }
            return diffDays + ' days ';
        },
        editor: Ext.create('Gnt.field.Duration', {
            decimalPrecision: 2,
            instantUpdate: true,
            listeners: {
                change: function (field, newValue, oldValue, eOpts) {
                    var task = field.task;
                    task.setStartDate(new Date());
                },
                focus: function (field) { }
            }
        })
    },
    {
        id:  "DurationActualColumnId",
        header: "Duration",
        sortable: true,
        width: 70,
        menuDisabled: true,
        dataIndex: "Actual_Duration"
    },
    {
        id:  "DurationPldColumnId",
        header: "Duration Pl'd",
        sortable: true,
        width: 70,
        menuDisabled: true,
        dataIndex: "Duration_Act_div_Pld"
    },
    {
        id:  "DurationActPldColumnId",
        header: "Duration Actual Pl'd",
        sortable: true,
        width: 70,
        menuDisabled: true,
        dataIndex: "Duration_Act_Pld"
    },
    {
        id:  "DurationActORPldColumnId",
        header: "Duration Act/Pl'd",
        sortable: true,
        width: 70,
        menuDisabled: true,
        dataIndex: "DurationActORPld"
    },
    {
        id:  "DurationProgColumnId",
        header: "Duration Prog",
        sortable: true,
        width: 70,
        menuDisabled: true,
        dataIndex: "DurationProg"
    },
    {
        id:  "DurationProgPldColumnId",
        header: "Duration Prog-Pl'd",
        sortable: true,
        width: 70,
        menuDisabled: true,
        dataIndex: "DurationProgPld"
    },
    {
        id:  "DurationProgOrPldColumnId",
        header: "Duration Prog/Pl'd",
        sortable: true,
        width: 70,
        menuDisabled: true,
        dataIndex: "DurationProgOrPld"
    },
    /*Duration cols end*/
    {
        xtype: 'effortcolumn',
        id:  "effortId",
        sortable: false,
        width: 70,
        menuDisabled: true
    },
    {
        xtype: 'Text1column',
        header: 'Text1',
        id:  "Text1Id",
        dataIndex: "Text1",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Text2column',
        header: 'Text2',
        id:  "Text2Id",
        dataIndex: "Text2",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Text3column',
        header: 'Text3',
        id:  "Text3Id",
        dataIndex: "Text3",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Text4column',
        header: 'Text4',
        id:  "Text4Id",
        dataIndex: "Text4",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Text5column',
        header: 'Text5',
        id:  "Text5Id",
        dataIndex: "Text5",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Text6column',
        header: 'Text6',
        id:  "Text6Id",
        dataIndex: "Text6",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Text7column',
        header: 'Text7',
        id:  "Text7Id",
        dataIndex: "Text7",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Text8column',
        header: 'Text8',
        id:  "Text8Id",
        dataIndex: "Text8",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Text9column',
        header: 'Text9',
        id:  "Text9Id",
        dataIndex: "Text9",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Text10column',
        header: 'Text10',
        id:  "Text10Id",
        dataIndex: "Text10",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Number1column',
        header: 'Number1',
        id:  "Number1Id",
        dataIndex: "Number1",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Number2column',
        header: 'Number2',
        id:  "Number2Id",
        dataIndex: "Number2",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Number3column',
        header: 'Number3',
        id:  "Number3Id",
        dataIndex: "Number3",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Number4column',
        header: 'Number4',
        id:  "Number4Id",
        dataIndex: "Number4",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Number5column',
        header: 'Number5',
        id:  "Number5Id",
        dataIndex: "Number5",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Number6column',
        header: 'Number6',
        id:  "Number6Id",
        dataIndex: "Number6",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Number7column',
        header: 'Number7',
        id:  "Number7Id",
        dataIndex: "Number7",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Number8column',
        header: 'Number8',
        id:  "Number8Id",
        dataIndex: "Number8",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Number9column',
        header: 'Number9',
        id:  "Number9Id",
        dataIndex: "Number9",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Number10column',
        header: 'Number10',
        id:  "Number10Id",
        dataIndex: "Number10",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'ClientColumn',
        header: 'Client',
        id:  "ClientNameId",
        dataIndex: "ClientName",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'ResponsibleColumn',
        header: 'Responsible',
        id:  "ResponsiblePersonNameId",
        dataIndex: "ResponsiblePersonName",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'earlystartdatecolumn',
        header: 'Begin early',
        id:  "earlystartdatecolumnId",
        dataIndex: "EarlyStart",
        sortable: false,
        menuDisabled: true,
        format : Config.Data.DE_DATE_FORMAT,
    },
    {
        xtype: 'lateenddatecolumn',
        header: 'End late',
        id:  "lateenddatecolumnId",
        dataIndex: "LateFinish",
        sortable: false,
        menuDisabled: true,
        format : Config.Data.DE_DATE_FORMAT,
    },
    {
        xtype: 'PriorityColumn',
        header: 'Priority',
        id:  "PriorityColumnId",
        dataIndex: "Priority",
        sortable: true,
        menuDisabled: true
    },
    {
        xtype: 'completedcolumn',
        header: 'Completed',
        id:  "CompletedId",
        dataIndex: 'Completed',
        width: 80,
        sortable: false,
        menuDisabled: true,
        format : Config.Data.DE_DATE_FORMAT
    },
    {
        xtype: 'createdcolumn',
        header: 'Entry Date',
        id:  "CreatedId",
        dataIndex: 'Created',
        width: 80,
        sortable: false,
        menuDisabled: true,
        format : Config.Data.DE_DATE_FORMAT
    },
    {
        xtype: 'ReshowDateColumn',
        header: 'Reshow Date',
        id:  "ReshowDateColumnId",
        dataIndex: "Reshow_Date",
        sortable: true,
        menuDisabled: true,
        format : Config.Data.DE_DATE_FORMAT
    },
    {
        xtype: 'LatefinishColumn',
        header: 'End Late',
        id:  "LatefinishColumnId",
        dataIndex: "LateFinish",
        sortable: true,
        menuDisabled: true,
        format : Config.Data.DE_DATE_FORMAT
    },
    {
        xtype: 'LateStartColumn',
        header: 'Begin Late',
        id:  "LateStartColumnId",
        dataIndex: "LateStart",
        sortable: true,
        menuDisabled: true,
        format : Config.Data.DE_DATE_FORMAT
    },
    {
        xtype: 'EarlyStartColumn',
        header: 'Begin Early',
        id:  "EarlyStartColumnId",
        dataIndex: "EarlyStart",
        sortable: true,
        menuDisabled: true,
        format : Config.Data.DE_DATE_FORMAT
    },
    {
        xtype: 'LatestEndDateColumn',
        header: 'End Prog',
        id:  "LatestEndDateColumnId",
        dataIndex: "LatestEndDate",
        sortable: true,
        menuDisabled: true,
        format : Config.Data.DE_DATE_FORMAT
    },
    {
        xtype: 'ActualStartColumn',
        header: 'Begin Actual',
        id:  "ActualStartColumnId",
        dataIndex: "Actual_Start",
        sortable: true,
        menuDisabled: true,
        format : Config.Data.DE_DATE_FORMAT
    },
    {
        xtype: 'ActualEndColumn',
        header: 'End Actual',
        id:  "ActualEndColumnId",
        dataIndex: "Actual_End",
        sortable: true,
        menuDisabled: true,
        format : Config.Data.DE_DATE_FORMAT
    },
    {
        header: 'Last Change',
        id:  "LastChangeColumnId",
        dataIndex: "LastUserDateAndName",
        sortable: true,
        menuDisabled: true
    },
    {
        header: 'Level',
        id:  "TaskLevelColumnId",
        dataIndex: "TaskLevel",
        sortable: true,
        menuDisabled: true
    },
    {
        header: 'Factor',
        id:  "FactorColumnId",
        dataIndex: "Factor",
        sortable: true,
        menuDisabled: true,
        renderer: function(e, t, n) {
            return (n.data.Factor) ? n.data.Factor: "";
        }
    },
    {
        header: 'Email To',
        id:  "EmailToColumnId",
        dataIndex: "EMail",
        sortable: true,
        menuDisabled: true
    },
    {
        header: 'Buffer',
        id:  "BufferColumnId",
        dataIndex: "Buffer",
        sortable: true,
        menuDisabled: true,
        renderer: function(e, t, n) {
            return (n.data.Buffer) ? n.data.Buffer + " h" : "";
        }
    },
    {
        header: 'Buffer Cum',
        id:  "BufferCumColumnId",
        dataIndex: "Buffer_cum",
        sortable: true,
        menuDisabled: true
    },
    {
        header: 'Buffer Actual',
        id:  "BufferActColumnId",
        dataIndex: "Buffer_Act",
        sortable: true,
        menuDisabled: true
    },
    {
        header: 'Buffer Actual Cum',
        id:  "BufferCumActColumnId",
        dataIndex: "Buffer_Act_cum",
        sortable: true,
        menuDisabled: true
    }
];
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
    'Gnt.column.Scale',
    'Gnt.plugin.TaskEditor', //Uncomment line number 832 to 847 to enable task editing popup
    'Sch.plugin.TreeCellEditing',
    'Gnt.plugin.DependencyEditor',
    //'Ext.grid.plugin.BufferedRenderer',
    'Gnt.plugin.Printable'
]);

// get requested locale from URL hash
var localeClass, locale = window.location.hash.substr(1);

switch (locale) {
    case 'ru': localeClass = 'RuRU'; break;
    case 'de': localeClass = 'De'; break;
    case 'pl': localeClass = 'Pl'; break;
    case 'sv_SE': localeClass = 'SvSE'; break;
    case 'it': localeClass = 'It'; break;
    case 'nl': localeClass = 'Nl'; break;
}

// by default we will use English locale
if (!localeClass) {
    localeClass = 'En';
    locale = 'en';
}

// now when we know the requested locale
// we will require Ext to load gantt localization class
Ext.require('Gnt.locale.' + localeClass);

// also we need to include corresponding ExtJS localization file
Ext.Loader.loadScript({
    url: 'js/Sch/locale/' + localeClass + '.js',
    onLoad: function () {
        // after ExtJs locale is applied we invoke rendering
        // (when Ext will load all the required classes)
        Ext.onReady(function() {
            APlan.loadProfileColumns();
        });
    }
});

Ext.onReady(function () {
    Ext.QuickTips.init();
});

TaskPriority = {
    Low: 0,
    Normal: 1,
    High: 2,
    Extra: 3
};
var ReadOnlyColumnIndex = [];
APlan = {
    loadProfileColumns: function() {
        var form = new FormData();
        form.append("Id", getQueryVariable('profile'));
        form.append("UserId", getQueryVariable("token"));
        Config.Data.RequestheaderSettings.data = form;
        Ext.MessageBox.show({
            msg: 'Loading Profile, please wait..',
            progressText: 'Loading..',
            width: 300,
            wait: true,
            waitConfig: { interval: 100 }
        });
        $.ajax(Config.Data.RequestheaderSettings).done(function (response) {
            var ResponseData = JSON.parse(response);
            Config.Data.ExpandedIds = ResponseData.Payload.ExpandedIds;
            ResponseData.Payload.ColsDetail = ResponseData.Payload.ColsDetail.split("'").join('"');
            var ColsDetail = $.parseJSON(ResponseData.Payload.ColsDetail);
            var Cols = [];
            for(var index=0; index < ColsDetail.length; index++) {
                for(var index2=0; index2 < Staticcolumns.length; index2++) {
                    ReadOnlyColumnIndex[ColsDetail[index].Id] = index2;
                    if(Staticcolumns[index2].id == ColsDetail[index].Id) {
                        Staticcolumns[index2].width = parseInt(ColsDetail[index].Width);
                        Staticcolumns[index2].hidden = (ColsDetail[index].Show == 1) ? false : true;
                        Staticcolumns[index2].header = ColsDetail[index].Name;
                        Cols.push(Staticcolumns[index2]);
                    }
                }
            }
            //Redo this line once the Client name and responsible person name is shown
            Staticcolumns = Cols;
            Ext.MessageBox.hide();
            APlan.init();
        });
    },
    init: function () {

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
                    renderer: function (startDate, endDate, headerConfig) {
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
        var resourceStore = Ext.create("Gnt.data.ResourceStore", {
            autoLoad: (getQueryVariable("from") == "dashboard") ? false: true,
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
            autoLoad: (getQueryVariable("from") == "dashboard") ? false: true,
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
        if((getQueryVariable("from") != "dashboard")) { /*Load Data from server*/
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
                autoLoad: (getQueryVariable("from") == "dashboard") ? false: true,
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
                    beforeload: function () {
                        console.log("Start");
                        Ext.MessageBox.show({
                            msg: 'Loading Tasks data, please wait..',
                            progressText: 'Loading..',
                            width: 300,
                            wait: true,
                            waitConfig: { interval: 100 }
                        });
                    }
                }
            });
        }

        var today = new Date();
        Ext.Date.clearTime(today);
        var start = Sch.util.Date.add(today, Sch.util.Date.MONTH, -5);
        var end = Sch.util.Date.add(today, Sch.util.Date.MONTH, 5);
        var grid = Ext.ComponentQuery.query("#ganttpanel-1013-body")[0];
        taskStore.on('load', function () {
            Ext.MessageBox.hide();
            var grid1 = Ext.ComponentQuery.query("#gv")[0];

        });
        taskStore.on('nodebeforeexpand', function(ExpandedObj) {
            if(Config.Data.ExpandedIds.search(ExpandedObj.data.Id + ",") < 0) {
                Config.Data.ExpandedIds += ExpandedObj.data.Id + ",";
                var colsDetails = APlan.getColsDetail();
                var form = new FormData();
                form.append("Id", getQueryVariable('profile'));
                form.append("UserId", getQueryVariable("token"));
                form.append("ExpandedIds", Config.Data.ExpandedIds);
                Config.Data.RequestheaderSettings.data = form;
                Config.Data.RequestheaderSettings.url = "Update/Expanded";
                $.ajax(Config.Data.RequestheaderSettings).done(function (response) {
                    var ResponseData = JSON.parse(response);
                });
            }
        }, console);
        taskStore.on('nodebeforecollapse', function(ExpandedObj) {
            //console.log(ExpandedObj);
            if(Config.Data.ExpandedIds.search(ExpandedObj.data.Id + ",") >= 0) {
                //Remove collapsed node id from expanded list
                Config.Data.ExpandedIds = Config.Data.ExpandedIds.replace(ExpandedObj.data.Id +",", "");
                var form = new FormData();
                form.append("Id", getQueryVariable('profile'));
                form.append("UserId", getQueryVariable("token"));
                form.append("ExpandedIds", Config.Data.ExpandedIds);
                Config.Data.RequestheaderSettings.data = form;
                Config.Data.RequestheaderSettings.url = "Update/Expanded";
                $.ajax(Config.Data.RequestheaderSettings).done(function (response) {
                    var ResponseData = JSON.parse(response);
                });
            }
        }, console);
        var g = this; var slider;
        var ganttPanel = Ext.create('Gnt.panel.Gantt', {
            id: "gv",
            height     : Ext.getBody().getViewSize().height,
            width      : Ext.getBody().getViewSize().width,
            headerHeight: 50,
            layout: 'fit',
            mask: true,
            viewPreset : 'weekAndDayLetter',
            startDate  : new Date(2016, 07, 06),
            endDate    : new Date(2016, 07, 25),
            infiniteScroll: true,
            readOnly: true,
            animate: false,
            shiftIncrement: 1,
            multiSelect: true,
            highlightWeekends: true,
            showTodayLine: true,
            loadMask: true,
            enableDependencyDragDrop: true,
            snapToIncrement: true,
            stripeRows: true,
            showExactResizePosition: true,
            title: 'Task',
            preventHeader: true,
            stateful: true,
            stateId: 'gantt',
            reserveScrollbar: true,
            viewConfig: {
                style: { overflow: 'auto', overflowX: 'hidden' },
                emptyText: 'No data'
            },
            region: 'center',
            selModel: new Ext.selection.TreeModel({
                ignoreRightMouseSelection: false,
                mode: 'MULTI'
            }),
            eventRenderer: function (task) {
                if (assignmentStore.findExact('TaskId', task.data.Id) >= 0) {
                    return {
                        ctcls: 'resources-assigned'
                    };
                }
            },
            leftLabelField: {
                dataIndex: 'Name',
                editor: { xtype: 'textfield' }
            },
            rightLabelField: {
                dataIndex: 'Id',
                editor: { xtype: 'textfield' },
                renderer: function (value, record) {
                    if (assignmentStore.findExact('TaskId', record.data.Id) >= 0) {
                        //return '<img src="Ribbon/images/Users.png" alt="resource" />';
                        return;
                    }
                }
            },
            plugins: [
                Ext.create("Gnt.plugin.TaskContextMenu"),
                Ext.create("Gnt.plugin.DependencyEditor", {
                    hideOnBlur: true
                }),
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

                new Gnt.plugin.Printable({
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

                Ext.create('Sch.plugin.TreeCellEditing', {
                    clicksToEdit: 2,
                    listeners: {
                        edit: function () {
                            //g.assignmentStore.sync();
                        }
                    }
                }),
                this.depEditor = new Gnt.plugin.DependencyEditor({
                    showLag: true,
                    constrain: true,
                    buttons: [
                        {
                            text: 'Ok',
                            scope: this,
                            handler: function () {
                                var formPanel = g.depEditor;
                                formPanel.getForm().updateRecord(formPanel.dependencyRecord);
                                g.depEditor.collapse();
                            }
                        },
                        {
                            text: 'Cancel',
                            scope: this,
                            handler: function () {
                                g.depEditor.collapse();
                            }
                        },
                        {
                            text: 'Delete',
                            scope: this,
                            handler: function () {
                                var formPanel = g.depEditor,
                                    record = g.depEditor.dependencyRecord;
                                g.dependencyStore.remove(record);
                                formPanel.collapse();
                            }
                        }
                    ]
                })

            ],
            listeners: {
                afterrender: function (cmp, eOpts) {
                    ganttPanel.setHeight(Ext.getBody().getViewSize().height);
                    ganttPanel.setWidth(Ext.getBody().getViewSize().width);
                    Ext.get("TasksId").addCls("x-column-header-sort-ASC"); //Default is ascending so set ascending image
                }
            },
            columns : Staticcolumns,
            taskStore       : taskStore,
            layout: 'border',

            //Left side grid area
            lockedGridConfig: {
                width: $(window).width() / 3,
                split: true,
                region: 'west',
                scroll: true,
                listeners: {
                    itemexpand: function () {

                    },
                    itemcollapse: function () {

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
                '<tr><td>Start:</td> <td align="right">{[values._record.getDisplayStartDate(Config.Data.DE_DATE_FORMAT)]}</td></tr>' +
                '<tr><td>End:</td> <td align="right">{[values._record.getDisplayEndDate(Config.Data.DE_DATE_FORMAT)]}</td></tr>' +
                '<tr><td>Progress:</td><td align="right">{[ Math.round(values.PercentDone)]}%</td></tr>' +
                '</table>'
            )
        });
        ganttPanel.setRowHeight(50);
        ganttPanel.render(Ext.getBody());
        ganttPanel.lockedGrid.view.on('itemclick', function(view, task) {
            ganttPanel.getSchedulingView().scrollEventIntoView(task, false, true);
        });
        ganttPanel.on('columnmove', function(ct, column, fromIdx, toIdx, eOpts){
            var colsDetails = APlan.getColsDetail();
            var form = new FormData();
            form.append("Id", getQueryVariable('profile'));
            form.append("UserId", getQueryVariable("token"));
            form.append("Name", "");
            form.append("ColsDetail", JSON.stringify(colsDetails));
            form.append("ExpandedIds", "");
            Config.Data.RequestheaderSettings.data = form;
            Config.Data.RequestheaderSettings.url = "Update/Profile";
            $.ajax(Config.Data.RequestheaderSettings).done(function (response) {
                var ResponseData = JSON.parse(response);
            });
        });
        ganttPanel.on('columnresize', function(ct, column, fromIdx, toIdx, eOpts){
            var colsDetails = APlan.getColsDetail();
            var form = new FormData();
            form.append("Id", getQueryVariable('profile'));
            form.append("UserId", getQueryVariable("token"));
            form.append("Name", "");
            form.append("ColsDetail", JSON.stringify(colsDetails));
            form.append("ExpandedIds", "");
            Config.Data.RequestheaderSettings.data = form;
            Config.Data.RequestheaderSettings.url = "Update/Profile";
            $.ajax(Config.Data.RequestheaderSettings).done(function (response) {
                var ResponseData = JSON.parse(response);
            });
        });
        ganttPanel.setReadOnly(true);
    },
    getColsDetail: function () {
        var grid = Ext.ComponentQuery.query("#gv")[0];
        var colsDetail = grid.getColumns();
        var colsDetails = [];
        colsDetail.forEach(function(single){
            if(single.xtype != "timeaxiscolumn") {
                colsDetails.push({"Id": single.id,"Name": single.text, "Show": (single.hidden == true) ? 0 : 1, "Width": single.width});
            }
        });
        return colsDetails;
    }
};
//Customize Task Context Menu
Ext.define("Gnt.plugin.TaskContextMenu", {
    extend: "Ext.menu.Menu",

    requires: [
        'Gnt.model.Dependency'
    ],
    plain: true,
    triggerEvent: 'taskcontextmenu',

    texts: {
        cut: 'Cut',
        copy: 'Copy',
        paste: 'Paste',
        critical: 'Critical',
        reshow: 'Reshow',
        completed: 'Completed',
        notclear: 'Not Clear',
        noticearrow: 'Notice arrow',
        passive: 'Passive',
        cancelled: 'Cancelled',
        donotfilter: 'Do not filter',
        lock: 'Lock',
        masterlock: 'Master lock',
        pagebreak: 'Page break',
        newTaskText: 'Add Component',
        newMilestoneText: 'New milestone',
        deleteTask: 'Delete Component(s)',
        editLeftLabel: 'Edit left label',
        editRightLabel: 'Edit right label',
        convert: 'Convert',
        converToMilestone: 'Convert to Milestone',
        converToRegularTask: 'Convert to Regular Task',
        add: 'Add',
        deleteDependency: 'Delete dependency',
        //addTaskAbove: 'Task above',
        addTaskBelow: 'Same Level',
        addSubtask: 'on next lower level',
        OnLevel1: 'OnLevel1(Folder)',
        OnLevel2: 'OnLevel2(Project)',
        OnLevel3: 'OnLevel3(Task)',
        addMilestone: 'Milestone',
        addSuccessor: 'Successor',
        addPredecessor: 'Predecessor'

    },

    Id: 'TaskContextMenu',
    grid: null,

    rec: null,

    lastHighlightedItem: null,

    createMenuItems: function () {
        var texts = this.texts;

        return [
            {
                itemId: 'contextmenu-Cut',
                handler: this.cut,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Cut.png',
                text: texts.cut
            },
            {
                itemId: 'contextmenu-Copy',
                handler: this.copy,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Copy.png',
                text: texts.copy
            },
            {
                itemId: 'contextmenu-Paste',
                handler: this.paste,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/icon_paste.png',
                text: texts.paste
            },
            {
                itemId: 'contextmenu-Critical',
                handler: this.critical,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/17_flash_16.png',
                text: texts.critical
            },
            {
                itemId: 'contextmenu-Reshow',
                handler: this.reshow,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/19_warning_16.png',
                text: texts.reshow
            },
            {
                itemId: 'contextmenu-Completed',
                handler: this.completed,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/10_check_16.png',
                text: texts.completed
            },
            {
                itemId: 'contextmenu-NotClear',
                handler: this.notclear,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/18_question_16.png',
                text: texts.notclear
            },
            {
                itemId: 'contextmenu-NoticeArrow',
                handler: this.noticearrow,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/16_arrow_16.png',
                text: texts.noticearrow
            },
            {
                itemId: 'contextmenu-Passive',
                handler: this.passive,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/11_task_passive_16.png',
                text: texts.passive
            },
            {
                itemId: 'contextmenu-Cancelled',
                handler: this.cancelled,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/12_task_canceled_16.png',
                text: texts.cancelled
            },
            {
                itemId: 'contextmenu-DoNotFilter',
                handler: this.donotfilter,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/DonotFilter.png',
                text: texts.donotfilter
            },
            {
                itemId: 'contextmenu-Lock',
                handler: this.lock,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Lock.png',
                text: texts.lock
            },
            {
                itemId: 'contextmenu-MasterLock',
                handler: this.masterlock,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/MasterLock.png',
                text: texts.masterlock
            },
            {
                itemId: 'contextmenu-PageBreak',
                handler: this.pagebreak,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/PageBreak.png',
                text: texts.pagebreak
            },
            {
                itemId: 'contextmenu-DeleteTask',
                handler: this.deleteTask,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/12_task_canceled_16.png',
                text: texts.deleteTask
            },
            {
                itemId: 'contextmenu-EditLeftLable',
                handler: this.editLeftLabel,
                requiresTask: true,
                scope: this,
                text: texts.editLeftLabel
            },
            {
                itemId: 'contextmenu-EditRightLable',
                handler: this.editRightLabel,
                requiresTask: true,
                scope: this,
                text: texts.editRightLabel
            },
            {
                text: texts.convert,
                itemId: 'contextmenu-Convert',
                menu: {
                    plain: true,
                    items: [
                        {
                            itemId: 'contextmenu-ConvertToMilestone',
                            handler: this.converToMilestone,
                            requiresTask: true,
                            scope: this,
                            text: texts.converToMilestone
                        },
                        {
                            itemId: 'contextmenu-ConvertToRegularTask',
                            handler: this.converToRegularTask,
                            requiresTask: true,
                            scope: this,
                            text: texts.converToRegularTask
                        }
                    ]
                }
            },
            {
                text: texts.add,
                itemId: 'contextmenu-Add',
                menu: {
                    plain: true,
                    items: [
                        {
                            itemId: 'contextmenu-OnLevel1',
                            handler: this.OnLevel1Action,
                            scope: this,
                            text: texts.OnLevel1
                        },
                        {
                            itemId: 'contextmenu-OnLevel2',
                            handler: this.OnLevel2Action,
                            requiresTask: true,
                            scope: this,
                            text: texts.OnLevel2
                        },
                        {
                            itemId: 'contextmenu-OnLevel3',
                            handler: this.OnLevel3Action,
                            requiresTask: true,
                            scope: this,
                            text: texts.OnLevel3
                        },
                        //{
                        //    handler: this.addTaskAboveAction,
                        //    requiresTask: true,
                        //    scope: this,
                        //    text: texts.addTaskAbove
                        //},
                        {
                            itemId: 'contextmenu-AddTaskBelow',
                            handler: this.addTaskBelowAction,
                            requiresTask: true,
                            scope: this,
                            text: texts.addTaskBelow
                        },
                        {
                            itemId: 'contextmenu-AddSubTask',
                            handler: this.addSubtask,
                            requiresTask: true,
                            scope: this,
                            text: texts.addSubtask
                        },
                        {
                            itemId: 'contextmenu-AddMilsestone',
                            handler: this.addMilestone,
                            requiresTask: true,
                            scope: this,
                            text: texts.addMilestone
                        },
                        {
                            itemId: 'contextmenu-AddSuccessor',
                            handler: this.addSuccessor,
                            requiresTask: true,
                            scope: this,
                            text: texts.addSuccessor
                        },
                        {
                            itemId: 'contextmenu-AddPredecessor',
                            handler: this.addPredecessor,
                            requiresTask: true,
                            scope: this,
                            text: texts.addPredecessor
                        }
                    ]
                }
            },
            {
                itemId: 'contextmenu-deleteDependency',
                text: texts.deleteDependency,
                requiresTask: true,
                menu: {
                    plain: true,

                    listeners: {
                        beforeshow: this.populateDependencyMenu,

                        // highlight dependencies on mouseover of the menu item
                        mouseover: this.onDependencyMouseOver,

                        // unhighlight dependencies on mouseout of the menu item
                        mouseleave: this.onDependencyMouseOut,

                        scope: this
                    }
                }
            }
        ];
    },
    listeners: {
        'beforeshow': function () {
            var me = this;
            var tasks = this.grid.getSelectionModel().selected;
            var a = me.getComponent("contextmenu-Add");
            if (tasks.length > 0) {
                var rawId = tasks.items[0].data.Id;
                var level = tasks.items[0].data.Outline_Level;
                if (tasks.items[0].data.Outline_Level == 1) {
                    me.getComponent("contextmenu-Cut").show();
                    me.getComponent("contextmenu-Copy").show();
                    me.getComponent("contextmenu-Paste").show();
                    me.getComponent("contextmenu-Critical").hide();
                    me.getComponent("contextmenu-Reshow").hide();
                    me.getComponent("contextmenu-Completed").hide();

                    me.getComponent("contextmenu-NotClear").hide();
                    me.getComponent("contextmenu-NoticeArrow").hide();
                    me.getComponent("contextmenu-Passive").hide();

                    me.getComponent("contextmenu-Cancelled").show();
                    me.getComponent("contextmenu-Lock").show();
                    me.getComponent("contextmenu-MasterLock").hide();
                    me.getComponent("contextmenu-DoNotFilter").hide();

                    me.getComponent("contextmenu-DeleteTask").show();

                    me.getComponent("contextmenu-EditLeftLable").hide();
                    me.getComponent("contextmenu-EditRightLable").hide();
                    me.getComponent("contextmenu-Convert").hide();
                    if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                        a.menu.items.items[0].show();
                    }
                    if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                        a.menu.items.items[1].show();
                    }
                    if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                        a.menu.items.items[2].hide();
                    }
                    if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                        a.menu.items.items[3].show();
                    }
                    if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                        a.menu.items.items[4].show();
                    }
                    if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                        a.menu.items.items[5].hide();
                    }
                    if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                        a.menu.items.items[6].hide();
                    }
                    if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                        a.menu.items.items[7].hide();
                    }
                    me.getComponent("contextmenu-deleteDependency").hide();

                    if (tasks.items[0].data.Lock == 1) {
                        me.getComponent("contextmenu-Cut").hide();
                        me.getComponent("contextmenu-Copy").show();
                        me.getComponent("contextmenu-Paste").show();
                        me.getComponent("contextmenu-Critical").hide();
                        me.getComponent("contextmenu-Reshow").hide();
                        me.getComponent("contextmenu-Completed").hide();

                        me.getComponent("contextmenu-NotClear").hide();
                        me.getComponent("contextmenu-NoticeArrow").hide();
                        me.getComponent("contextmenu-Passive").hide();

                        me.getComponent("contextmenu-Cancelled").show();
                        me.getComponent("contextmenu-Lock").show();
                        me.getComponent("contextmenu-MasterLock").hide();
                        me.getComponent("contextmenu-DoNotFilter").hide();

                        me.getComponent("contextmenu-DeleteTask").hide();

                        me.getComponent("contextmenu-EditLeftLable").hide();
                        me.getComponent("contextmenu-EditRightLable").hide();
                        me.getComponent("contextmenu-Convert").hide();
                        if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                            a.menu.items.items[0].show();
                        }
                        if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                            a.menu.items.items[1].hide();
                        }
                        if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                            a.menu.items.items[2].hide();
                        }
                        if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                            a.menu.items.items[3].show();
                        }
                        if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                            a.menu.items.items[4].hide();
                        }
                        if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                            a.menu.items.items[5].hide();
                        }
                        if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                            a.menu.items.items[6].hide();
                        }
                        if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                            a.menu.items.items[7].hide();
                        }
                        me.getComponent("contextmenu-deleteDependency").hide();
                    }
                    $.ajax({
                        type: 'GET',
                        url: "Tasks/CheckPermission?rawId=" + rawId + '&level=' + level,
                        autoSync: true,
                        autoLoad: true,
                        success: function (response) {

                            if (response == "Read" || response == "Change") {
                                me.getComponent("contextmenu-Cut").hide();
                                var a = me.getComponent("contextmenu-Add");
                                if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                    a.menu.items.items[1].hide();
                                }
                                if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                    a.menu.items.items[2].hide();
                                }
                                if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                    a.menu.items.items[3].show();
                                }
                                if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                    a.menu.items.items[4].hide();
                                }
                                me.getComponent("contextmenu-DeleteTask").hide();
                            }
                        }
                    });
                }
                else if (tasks.items[0].data.Outline_Level == 2) {
                    me.getComponent("contextmenu-Cut").show();
                    me.getComponent("contextmenu-Copy").show();
                    me.getComponent("contextmenu-Paste").show();
                    me.getComponent("contextmenu-Critical").show();
                    me.getComponent("contextmenu-Reshow").show();
                    me.getComponent("contextmenu-Completed").show();

                    me.getComponent("contextmenu-NotClear").show();
                    me.getComponent("contextmenu-NoticeArrow").show();
                    me.getComponent("contextmenu-Passive").show();

                    me.getComponent("contextmenu-Cancelled").show();
                    me.getComponent("contextmenu-DoNotFilter").show();

                    me.getComponent("contextmenu-DeleteTask").show();

                    me.getComponent("contextmenu-EditLeftLable").show();
                    me.getComponent("contextmenu-EditRightLable").show();
                    me.getComponent("contextmenu-Convert").show();

                    if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                        a.menu.items.items[0].show();
                    }
                    if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                        a.menu.items.items[1].show();
                    }
                    if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                        a.menu.items.items[2].show();
                    }
                    if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                        a.menu.items.items[3].show();
                    }
                    if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                        a.menu.items.items[4].show();
                    }
                    if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                        a.menu.items.items[5].show();
                    }
                    if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                        a.menu.items.items[6].show();
                    }
                    if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                        a.menu.items.items[7].show();
                    }
                    me.getComponent("contextmenu-deleteDependency").show();

                    if (tasks.items[0].data.Lock == 1) {
                        me.getComponent("contextmenu-Cut").hide();
                        me.getComponent("contextmenu-Copy").show();
                        me.getComponent("contextmenu-Paste").show();
                        me.getComponent("contextmenu-Critical").hide();
                        me.getComponent("contextmenu-Reshow").hide();
                        me.getComponent("contextmenu-Completed").hide();

                        me.getComponent("contextmenu-NotClear").hide();
                        me.getComponent("contextmenu-NoticeArrow").hide();
                        me.getComponent("contextmenu-Passive").hide();

                        me.getComponent("contextmenu-Cancelled").show();
                        me.getComponent("contextmenu-Lock").show();
                        me.getComponent("contextmenu-MasterLock").hide();
                        me.getComponent("contextmenu-DoNotFilter").hide();

                        me.getComponent("contextmenu-DeleteTask").hide();

                        me.getComponent("contextmenu-EditLeftLable").hide();
                        me.getComponent("contextmenu-EditRightLable").hide();
                        me.getComponent("contextmenu-Convert").hide();
                        if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                            a.menu.items.items[0].show();
                        }
                        if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                            a.menu.items.items[1].hide();
                        }
                        if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                            a.menu.items.items[2].hide();
                        }
                        if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                            a.menu.items.items[3].show();
                        }
                        if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                            a.menu.items.items[4].hide();
                        }
                        if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                            a.menu.items.items[5].hide();
                        }
                        if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                            a.menu.items.items[6].hide();
                        }
                        if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                            a.menu.items.items[7].hide();
                        }
                        me.getComponent("contextmenu-deleteDependency").hide();
                    }
                    $.ajax({
                        type: 'GET',
                        url: "Tasks/CheckPermission?rawId=" + rawId + '&level=' + level,
                        autoSync: true,
                        autoLoad: true,
                        success: function (response) {
                            var a = me.getComponent("contextmenu-Add");
                            if (response == "Change") {
                                me.getComponent("contextmenu-Cut").hide();
                                if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                    a.menu.items.items[1].hide();
                                }
                                if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                    a.menu.items.items[2].hide();
                                }
                                if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                    a.menu.items.items[3].show();
                                }
                                if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                    a.menu.items.items[4].hide();
                                }
                                if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                    a.menu.items.items[5].hide();
                                }
                                if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                    a.menu.items.items[6].hide();
                                }
                                if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                    a.menu.items.items[7].hide();
                                }
                                me.getComponent("contextmenu-DeleteTask").hide();
                            }
                            else if (response == "Read") {
                                me.getComponent("contextmenu-Cut").hide();
                                me.getComponent("contextmenu-Copy").show();
                                me.getComponent("contextmenu-Paste").show();
                                me.getComponent("contextmenu-Critical").hide();
                                me.getComponent("contextmenu-Reshow").hide();
                                me.getComponent("contextmenu-Completed").hide();

                                me.getComponent("contextmenu-NotClear").hide();
                                me.getComponent("contextmenu-NoticeArrow").hide();
                                me.getComponent("contextmenu-Passive").hide();

                                me.getComponent("contextmenu-Cancelled").hide();
                                me.getComponent("contextmenu-Lock").show();
                                me.getComponent("contextmenu-MasterLock").show();
                                me.getComponent("contextmenu-DoNotFilter").hide();

                                me.getComponent("contextmenu-DeleteTask").hide();

                                me.getComponent("contextmenu-EditLeftLable").hide();
                                me.getComponent("contextmenu-EditRightLable").hide();
                                me.getComponent("contextmenu-Convert").hide();
                                if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                                    a.menu.items.items[0].show();
                                }
                                if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                    a.menu.items.items[1].hide();
                                }
                                if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                    a.menu.items.items[2].hide();
                                }
                                if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                    a.menu.items.items[3].show();
                                }
                                if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                    a.menu.items.items[4].hide();
                                }
                                if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                    a.menu.items.items[5].hide();
                                }
                                if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                    a.menu.items.items[6].hide();
                                }
                                if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                    a.menu.items.items[7].hide();
                                }
                                me.getComponent("contextmenu-deleteDependency").hide();
                            }
                        }
                    });
                } else if (tasks.items[0].data.Outline_Level == 3) {
                    if (tasks.items[0].data.Lock == 1) {
                        me.getComponent("contextmenu-Cut").hide();
                        me.getComponent("contextmenu-Copy").show();
                        me.getComponent("contextmenu-Paste").show();
                        me.getComponent("contextmenu-Critical").hide();
                        me.getComponent("contextmenu-Reshow").hide();
                        me.getComponent("contextmenu-Completed").hide();

                        me.getComponent("contextmenu-NotClear").hide();
                        me.getComponent("contextmenu-NoticeArrow").hide();
                        me.getComponent("contextmenu-Passive").hide();

                        me.getComponent("contextmenu-Cancelled").show();
                        me.getComponent("contextmenu-Lock").show();
                        me.getComponent("contextmenu-MasterLock").hide();
                        me.getComponent("contextmenu-DoNotFilter").hide();

                        me.getComponent("contextmenu-DeleteTask").hide();

                        me.getComponent("contextmenu-EditLeftLable").hide();
                        me.getComponent("contextmenu-EditRightLable").hide();
                        me.getComponent("contextmenu-Convert").hide();
                        if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                            a.menu.items.items[0].show();
                        }
                        if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                            a.menu.items.items[1].hide();
                        }
                        if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                            a.menu.items.items[2].hide();
                        }
                        if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                            a.menu.items.items[3].show();
                        }
                        if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                            a.menu.items.items[4].hide();
                        }
                        if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                            a.menu.items.items[5].hide();
                        }
                        if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                            a.menu.items.items[6].hide();
                        }
                        if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                            a.menu.items.items[7].hide();
                        }
                        me.getComponent("contextmenu-deleteDependency").hide();
                    } else {
                        $.ajax({
                            type: 'GET',
                            url: "Tasks/CheckPermission?rawId=" + tasks.items[0].data.parentId + '&level=' + level,
                            autoSync: true,
                            autoLoad: true,
                            success: function (response) {
                                var a = me.getComponent("contextmenu-Add");
                                if (response == "Change") {
                                    me.getComponent("contextmenu-Cut").hide();
                                    me.getComponent("contextmenu-Copy").show();
                                    me.getComponent("contextmenu-Paste").show();

                                    me.getComponent("contextmenu-Critical").show();
                                    me.getComponent("contextmenu-Reshow").show();
                                    me.getComponent("contextmenu-Completed").show();

                                    me.getComponent("contextmenu-NotClear").show();
                                    me.getComponent("contextmenu-NoticeArrow").show();
                                    me.getComponent("contextmenu-Passive").show();

                                    me.getComponent("contextmenu-Cancelled").show();
                                    me.getComponent("contextmenu-DoNotFilter").show();
                                    me.getComponent("contextmenu-Lock").show();
                                    me.getComponent("contextmenu-MasterLock").show();

                                    me.getComponent("contextmenu-EditLeftLable").show();
                                    me.getComponent("contextmenu-EditRightLable").show();
                                    me.getComponent("contextmenu-Convert").show();

                                    if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                                        a.menu.items.items[0].show();
                                    }
                                    if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                        a.menu.items.items[1].hide();
                                    }
                                    if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                        a.menu.items.items[2].hide();
                                    }
                                    if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                        a.menu.items.items[3].show();
                                    }
                                    if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                        a.menu.items.items[4].hide();
                                    }
                                    if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                        a.menu.items.items[5].hide();
                                    }
                                    if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                        a.menu.items.items[6].hide();
                                    }
                                    if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                        a.menu.items.items[7].hide();
                                    }
                                    me.getComponent("contextmenu-deleteDependency").hide();
                                    me.getComponent("contextmenu-DeleteTask").hide();
                                }
                                if (response == "Read") {
                                    me.getComponent("contextmenu-Cut").hide();
                                    me.getComponent("contextmenu-Copy").show();
                                    me.getComponent("contextmenu-Paste").show();
                                    me.getComponent("contextmenu-Critical").hide();
                                    me.getComponent("contextmenu-Reshow").hide();
                                    me.getComponent("contextmenu-Completed").hide();

                                    me.getComponent("contextmenu-NotClear").hide();
                                    me.getComponent("contextmenu-NoticeArrow").hide();
                                    me.getComponent("contextmenu-Passive").hide();

                                    me.getComponent("contextmenu-Cancelled").hide();
                                    me.getComponent("contextmenu-DoNotFilter").hide();
                                    me.getComponent("contextmenu-Lock").show();
                                    me.getComponent("contextmenu-MasterLock").show();

                                    me.getComponent("contextmenu-DeleteTask").hide();

                                    me.getComponent("contextmenu-EditLeftLable").hide();
                                    me.getComponent("contextmenu-EditRightLable").hide();
                                    me.getComponent("contextmenu-Convert").hide();
                                    if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                                        a.menu.items.items[0].show();
                                    }
                                    if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                        a.menu.items.items[1].hide();
                                    }
                                    if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                        a.menu.items.items[2].hide();
                                    }
                                    if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                        a.menu.items.items[3].show();
                                    }
                                    if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                        a.menu.items.items[4].hide();
                                    }
                                    if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                        a.menu.items.items[5].hide();
                                    }
                                    if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                        a.menu.items.items[6].hide();
                                    }
                                    if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                        a.menu.items.items[7].hide();
                                    }
                                    me.getComponent("contextmenu-deleteDependency").hide();
                                }
                                if (response == "Create" || response == "Standard") {
                                    me.getComponent("contextmenu-Cut").show();
                                    me.getComponent("contextmenu-Copy").show();
                                    me.getComponent("contextmenu-Paste").show();
                                    me.getComponent("contextmenu-Critical").show();
                                    me.getComponent("contextmenu-Reshow").show();
                                    me.getComponent("contextmenu-Completed").show();

                                    me.getComponent("contextmenu-NotClear").show();
                                    me.getComponent("contextmenu-NoticeArrow").show();
                                    me.getComponent("contextmenu-Passive").show();

                                    me.getComponent("contextmenu-Cancelled").show();
                                    me.getComponent("contextmenu-DoNotFilter").show();

                                    me.getComponent("contextmenu-DeleteTask").show();

                                    me.getComponent("contextmenu-EditLeftLable").show();
                                    me.getComponent("contextmenu-EditRightLable").show();
                                    me.getComponent("contextmenu-Convert").show();

                                    if (a.menu.items.items[0].itemId == "contextmenu-OnLevel") {
                                        a.menu.items.items[0].show();
                                    }
                                    if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                        a.menu.items.items[1].show();
                                    }
                                    if (a.menu.items.items[2].itemId == "contextmenu-AddTaskBelow") {
                                        a.menu.items.items[2].show();
                                    }
                                    if (a.menu.items.items[3].itemId == "contextmenu-AddSubTask") {
                                        a.menu.items.items[3].show();
                                    }
                                    if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                        a.menu.items.items[4].show();
                                    }
                                    if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                        a.menu.items.items[5].show();
                                    }
                                    if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                        a.menu.items.items[6].show();
                                    }
                                    if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                        a.menu.items.items[7].show();
                                    }
                                    me.getComponent("contextmenu-deleteDependency").show();
                                }
                            },
                            error: function () {
                                alert("Request Failed");
                            },
                            processData: true,
                            async: true
                        });
                    }
                }
            }
        }
    },

    // backward compat
    buildMenuItems: function () {
        this.items = this.createMenuItems();
    },

    initComponent: function () {
        this.buildMenuItems();
        this.callParent(arguments);
    },

    init: function (grid) {
        grid.on('destroy', this.cleanUp, this);
        var scheduleView = grid.getSchedulingView(),
            lockedView = grid.lockedGrid.getView();

        //TODO::This Comment is only for readonly mode

        if (this.triggerEvent === 'itemcontextmenu') {
            lockedView.on('itemcontextmenu', this.onItemContextMenu, this);
            scheduleView.on('itemcontextmenu', this.onItemContextMenu, this);
        } else {
            //get task context menu on both left and right grid click event
            scheduleView.on('taskcontextmenu', this.onTaskContextMenu, this);
            lockedView.on('itemcontextmenu', this.onItemContextMenu, this);
        }

        // Handle case of empty schedule too
        scheduleView.on('containercontextmenu', this.onContainerContextMenu, this);
        lockedView.on('containercontextmenu', this.onContainerContextMenu, this);
        this.grid = grid;
    },

    populateDependencyMenu: function (menu) {
        var grid = this.grid,
            taskStore = grid.getTaskStore(),
            dependencies = this.rec.getAllDependencies(),
            depStore = grid.dependencyStore;

        menu.removeAll();

        if (dependencies.length === 0) {
            return false;
        }

        var taskId = this.rec.getId() || this.rec.internalId;

        Ext.each(dependencies, function (dependency) {
            var fromId = dependency.get('From'),
                task = taskStore.getById(fromId == taskId ? dependency.get('To') : fromId);

            if (task) {
                menu.add({
                    depId: dependency.internalId,
                    text: Ext.util.Format.ellipsis(task.get('Name'), 30),

                    scope: this,
                    handler: function (menuItem) {
                        // in 4.0.2 `indexOfId` returns the record by the `internalId`
                        // in 4.0.7 `indexOfId` returns the record by its "real" id
                        // so need to manually scan the store to find the record

                        var record;

                        depStore.each(function (dependency) {
                            if (dependency.internalId == menuItem.depId) { record = dependency; return false; }
                        });

                        depStore.remove(record);
                    }
                });
            }
        }, this);
    },

    onDependencyMouseOver: function (menu, item, e) {
        if (item) {
            var schedulingView = this.grid.getSchedulingView();

            if (this.lastHighlightedItem) {
                schedulingView.unhighlightDependency(this.lastHighlightedItem.depId);
            }

            this.lastHighlightedItem = item;
            schedulingView.highlightDependency(item.depId);
        }
    },

    onDependencyMouseOut: function (menu, e) {
        if (this.lastHighlightedItem) {
            this.grid.getSchedulingView().unhighlightDependency(this.lastHighlightedItem.depId);
        }
    },

    cleanUp: function () {
        if (this.menu) {
            this.menu.destroy();
        }
    },

    onTaskContextMenu: function (g, record, e) {
        this.activateMenu(record, e);
    },

    onItemContextMenu: function (view, record, item, index, e) {
        this.activateMenu(record, e);
    },

    onContainerContextMenu: function (g, e) {
        this.activateMenu(null, e);
    },

    activateMenu: function (rec, e) {
        e.stopEvent();

        this.rec = rec;
        var reqTasks = this.query('[requiresTask]');
        Ext.each(reqTasks, function (item) {
            item.setDisabled(!rec);
        });
        this.showAt(e.getXY());
    },

    copyTask: function (originalRecord) {
        if (originalRecord != null) {
            var taskStore = this.grid.getTaskStore(),

                newTask = new taskStore.model({
                    PercentDone: 0,
                    Name: this.texts.newTaskText,
                    StartDate: originalRecord.get('StartDate'),
                    EndDate: originalRecord.get('EndDate'),
                    parentId: originalRecord.get('parentId') + 1,
                    leaf: true,
                    Outline_Level: originalRecord.get('Outline_Level')
                });
            return newTask;
        } else {
            var taskStore = this.grid.getTaskStore(),

                newTask = new taskStore.model({
                    leaf: true,
                    Id: null,
                    StartDate: null,
                    EndDate: null,
                    Priority: 1,
                    parentId: null,
                    isCriticalTask: false,
                    isClarificationRequired: false,
                    LimitDate: null,
                    isNotice: false,
                    isReshow: false,
                    isCompleted: false,
                    isPassive: false,
                    isCancelled: false,
                    index: 32,
                    Cls: "",
                    Name: "New task",
                    Duration: null,
                    Effort: null,
                    EffortUnit: "h",
                    CalendarId: "",
                    Note: "",
                    PercentDone: 0,
                    ManuallyScheduled: false,
                    SchedulingMode: "Normal",
                    BaselineStartDate: null,
                    BaselineEndDate: null,
                    BaselinePercentDone: 0,
                    PhantomId: "ext-record-363",
                    PhantomParentId: "",
                    DurationUnit: "d"
                });
            return newTask;
        }
    },

    addTaskAbove: function (newTask) {
        var task = this.rec;
        newTask.isCriticalTask = false;
        newTask.isClarificationRequired = false;
        newTask.isPassive = false;
        newTask.isCancelled = false;
        newTask.isNotice = false;
        newTask.isCompleted = false;
        newTask.isReshow = false;
        newTask.NoticeStatus = 3;
        newTask.LastModification = new Date();
        newTask.LastUserMod = new Date();
        newTask.Group_Name = task.parentNode.data.Name;
        newTask.Created = new Date();
        if (task.data.Outline_Level == 1) {
            newTask.Name = '(Project desig.)';
        }
        else if (task.data.Outline_Level >= 2) {
            newTask.Name = '(Task desig.)';
        }
        task.parentNode.set('Object', parseInt(task.parentNode.data.Object) + 1);
        if (task) {
            task.parentNode.insertBefore(newTask, task);
        } else {
            this.grid.taskStore.getRootNode().appendChild(newTask);
        }
    },

    addTaskBelow: function (newTask) {
        var task = this.rec;
        console.log(task.parentNode);
        if (task.data.Outline_Level == 1) {
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.data.Name;
            newTask.Created = new Date();
            newTask.set('Outline_Level', 1);
            newTask.set('leaf', true);
            newTask.set('Name', '(Folder desig.)');
            newTask.set('parentId', 0);
            task.parentNode.set('Object', parseInt(task.parentNode.data.Object) + 1);
            if (task) {
                task.parentNode.insertBefore(newTask, task.nextSibling);
            } else {
                this.grid.taskStore.getRootNode().appendChild(newTask);
            }
        }
        else if (task.data.Outline_Level == 2) {
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.data.Name;
            newTask.Created = new Date();
            newTask.set('Outline_Level', 2);
            newTask.set('Name', '(Project desig.)');
            newTask.set('leaf', true);
            task.parentNode.set('Object', parseInt(task.parentNode.data.Object) + 1);
            if (task) {
                task.parentNode.insertBefore(newTask, task.nextSibling);
            } else {
                this.grid.taskStore.getRootNode().appendChild(newTask);
            }
        } else {
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.data.Name;
            newTask.Created = new Date();
            newTask.set('Outline_Level', task.data.Outline_Level);
            newTask.set('Name', '(Task desig.)');
            newTask.set('leaf', task.data.leaf);
            task.parentNode.set('Object', parseInt(task.parentNode.data.Object) + 1);
            if (task) {
                task.parentNode.insertBefore(newTask, task.nextSibling);
            } else {
                this.grid.taskStore.getRootNode().appendChild(newTask);
            }
        }
    },

    critical: function () {
        var task = this.rec;
         if (task.data.isCriticalTask == true) {
            task.set('isCriticalTask', false);
        } else {
            task.set('isCriticalTask', true);
        }
    },

    reshow: function () {
        var task = this.rec;
       if (task.data.isReshow == true) {
            task.data.Reshow_Date = null;
            task.set('isReshow', false);
        } else {
            reshowDateWindow.show();
        }
    },

    completed: function () {
        var task = this.rec;
         Ext.defer(function () {
            /*statusBar.setStatus("Ready");*/
            if (task.data.isCompleted != true) {
                makeCompletedTasks(task);
                task.set('PercentDone', 100);
                task.set('isCompleted', true);
                task.set('Completed', new Date());
            } else {
                makeDefaultTasks(task);
                task.set('PercentDone', 0);
                task.set('isCompleted', false);
                task.set('Completed', null);
            }
            function makeCompletedTasks(node) {
                for (var i = 0; i < node.childNodes.length; i++) {
                    node.expand();
                    var child = node.childNodes[i];
                    makeCompletedTasks(child);
                    completedTasks(child);
                }
            }
            function makeDefaultTasks(node) {
                for (var i = 0; i < node.childNodes.length; i++) {
                    node.expand();
                    var child = node.childNodes[i];
                    makeDefaultTasks(child);
                    defaultTasks(child);
                }
            }

            function completedTasks(child) {
                child.set('PercentDone', 100);
                child.set('isCompleted', true);
                child.set('Completed', new Date());
            }
            function defaultTasks(child) {
                child.set('PercentDone', 0);
                child.set('isCompleted', false);
                child.set('Completed', null);
            }
        }, 1000);
    },

    notclear: function () {
        var task = this.rec;
        if (task.data.isClarificationRequired == true) {
            task.set('isClarificationRequired', false);
        } else {
            task.set('isClarificationRequired', true);
        }
    },

    noticearrow: function () {
        var task = this.rec;
        if (task.data.isNotice == true) {
            task.set('isNotice', false);
        } else {
            task.set('isNotice', true);
        }
    },

    passive: function () {
        var task = this.rec;
        task.expand();
        Ext.defer(function () {
            //statusBar.setStatus("Ready");
            if (task.data.isPassive != true) {
                makeisPassiveTasks(task);
                task.set('isPassive', true);
                task.set('isCancelled', false);
            } else {
                makeDefaultTasks(task);
                task.set('isPassive', false);
            }
            function makeisPassiveTasks(node) {
                for (var i = 0; i < node.childNodes.length; i++) {
                    node.expand();
                    var child = node.childNodes[i];
                    makeisPassiveTasks(child);
                    passiveTasks(child);
                }
            }
            function makeDefaultTasks(node) {
                for (var i = 0; i < node.childNodes.length; i++) {
                    node.expand();
                    var child = node.childNodes[i];
                    makeDefaultTasks(child);
                    defaultTasks(child);
                }
            }
            function passiveTasks(child) {
                child.set('isPassive', true);
                child.set('isCancelled', false);
            }
            function defaultTasks(child) {
                child.set('isPassive', false);
            }
        }, 1000);
    },

    cancelled: function () {
        var task = this.rec;
        Ext.defer(function () {
            //statusBar.setStatus("Ready");
            if (task.data.isCancelled != true) {
                makeCancelledTasks(task);
                task.set('isCancelled', true);
                task.set('isPassive', false);
            } else {
                makeDefaultTasks(task);
                task.set('isCancelled', false);
            }
            function makeCancelledTasks(node) {
                for (var i = 0; i < node.childNodes.length; i++) {
                    node.expand();
                    var child = node.childNodes[i];
                    makeCancelledTasks(child);
                    cancelledTasks(child);
                }
            }
            function makeDefaultTasks(node) {
                for (var i = 0; i < node.childNodes.length; i++) {
                    node.expand();
                    var child = node.childNodes[i];
                    makeDefaultTasks(child);
                    defaultTasks(child);
                }
            }
            function cancelledTasks(child) {
                child.set('isCancelled', true);
                child.set('isPassive', false);
            }
            function defaultTasks(child) {
                child.set('isCancelled', false);
            }
        }, 1000);
    },

    paste: function () {
        var task = this.rec;
        console.log(this.copiedTasks);

        if (task) {
            this.addTaskBelow(this.copiedTasks);
        }
        else {
            this.grid.taskStore.getRootNode().appendChild(this.copiedTasks);
        }
        this.copiedTasks = this.copyTaskWithChildren(this.copiedTasks);
    },

    copy: function () {
        var me = this;
        var tasks = this.grid.getSelectionModel().selected;
        tasks.each(function (t) {
            if (!tasks.contains(t.parentNode)) {
                me.copiedTasks = me.copyTaskWithChildren(t);
            }
        });
    },

    cut: function () {
        var me = this;
        var tasks = this.grid.getSelectionModel().selected;
        tasks.each(function (t) {
            if (!tasks.contains(t.parentNode)) {
                me.copiedTasks = me.copyTaskWithChildren(t);
                me.grid.taskStore.remove(tasks.getRange());
            }
        });
    },

    lock: function () {
        var task = this.rec;
        if (task.data.Lock == 0) {
            task.set('Lock', 1);
        } else {
            task.set('Lock', 0);
        }
    },

    copyTaskWithChildren: function (original) {
        var me = this;
        var model = this.grid.getTaskStore().model;

        var newTask = new model({
        });

        newTask.set('leaf', (original && original.get('leaf')) || null);
        newTask.set('expanded', (original && original.get('expanded')) || null);
        newTask.setName((original && original.getName()) || null);
        newTask.set(newTask.startDateField, (original && original.getStartDate()) || null);
        newTask.set(newTask.endDateField, (original && original.getEndDate()) || null);
        newTask.set(newTask.percentDoneField, (original && original.getPercentDone()) || null);
        newTask.set(newTask.durationField, (original && original.getDuration()) || null);
        newTask.set(newTask.durationUnitField, (original && original.getDurationUnit()) || 'd');
        newTask.set(newTask.effortField, (original && original.getEffort()) || null);
        newTask.set(newTask.effortUnitField, (original && original.getEffortUnit()) || 'h');
        newTask.set(newTask.schedulingModeField, (original && original.getSchedulingMode()) || 'Normal');
        newTask.set(newTask.clsField, (original && original.getCls()) || null);
        //newTask.set('parentId', (original && original.get('parentId')) || null);
        //newTask.set('Priority', (original && original.get('Priority')) || null);
        //newTask.set('index', (original && original.get('index')) || null);
        newTask.set('isCritical', (original && original.get('isCritical')) || null);
        newTask.set('isClarificationRequired', (original && original.get('isClarificationRequired')) || null);
        newTask.set('LimitDate', (original && original.get('LimitDate')) || null);
        newTask.set('isNotice', (original && original.get('isNotice')) || null);
        newTask.set('isReshow', (original && original.get('isReshow')) || null);
        newTask.set('isCompleted', (original && original.get('isCompleted')) || null);
        newTask.set('isPassive', (original && original.get('isPassive')) || null);
        newTask.set('isCancelled', (original && original.get('isCancelled')) || null);
        newTask.set('Act_Fixed_Cost', (original && original.get('Act_Fixed_Cost')) || null);
        newTask.set('Contact', (original && original.get('Contact')) || null);
        newTask.set('Responsible', (original && original.get('Responsible')) || null);
        newTask.set('Resource_Names', (original && original.get('Resource_Names')) || null);
        newTask.set('Created', (original && original.get('Created')) || null);
        newTask.set('Reshow_Date', (original && original.get('Reshow_Date')) || null);
        newTask.set('Capacity_usage', (original && original.get('Capacity_usage')) || null);
        newTask.set('Actual_Start', (original && original.get('Actual_Start')) || null);
        newTask.set('Object', (original && original.get('Object')) || null);
        newTask.set('Actual_Finish', (original && original.get('Actual_Finish')) || null);
        newTask.set('Rem_Time', (original && original.get('Rem_Time')) || null);
        newTask.set('Completed', (original && original.get('Completed')) || null);
        newTask.set('Actual_Duration', (original && original.get('Actual_Duration')) || null);
        newTask.set('Duration_Act_div_Pld', (original && original.get('Duration_Act_div_Pld')) || null);
        newTask.set('Duration_Act_Pld', (original && original.get('Duration_Act_Pld')) || null);
        newTask.set('Req_Effort', (original && original.get('Req_Effort')) || null);
        newTask.set('Req_Ress', (original && original.get('Req_Ress')) || null);
        newTask.set('Effort', (original && original.get('Effort')) || null);
        newTask.set('Actual_Effort', (original && original.get('Actual_Effort')) || null);
        newTask.set('Effort_Pld_Req', (original && original.get('Effort_Pld_Req')) || null);
        newTask.set('Effort_Pld_div_Req', (original && original.get('Effort_Pld_div_Req')) || null);
        newTask.set('Effort_Act_Pld1', (original && original.get('Effort_Act_Pld1')) || null);
        newTask.set('Effort_Act_Pld', (original && original.get('Effort_Act_Pld')) || null);
        newTask.set('Ovt_Effort', (original && original.get('Ovt_Effort')) || null);
        newTask.set('Actual_Ovt_Effort', (original && original.get('Actual_Ovt_Effort')) || null);
        newTask.set('Fixed_Cost', (original && original.get('Fixed_Cost')) || null);
        newTask.set('Standard_Rate', (original && original.get('Standard_Rate')) || null);
        newTask.set('Act_Standard_Rate', (original && original.get('Act_Standard_Rate')) || null);
        newTask.set('Labor_Cost', (original && original.get('Labor_Cost')) || null);
        newTask.set('Act_Labor_Cost', (original && original.get('Act_Labor_Cost')) || null);
        newTask.set('Ovt_Cost', (original && original.get('Ovt_Cost')) || null);
        newTask.set('Act_Ovt_Cost', (original && original.get('Act_Ovt_Cost')) || null);
        newTask.set('Total_Cost', (original && original.get('Total_Cost')) || null);
        newTask.set('Act_Total_Cost', (original && original.get('Act_Total_Cost')) || null);
        newTask.set('Total_Cost_Act_div_Pld', (original && original.get('Total_Cost_Act_div_Pld')) || null);
        newTask.set('Total_Cost_Act_Pld', (original && original.get('Total_Cost_Act_Pld')) || null);
        newTask.set('Cost_Calc_Meth', (original && original.get('Cost_Calc_Meth')) || null);
        newTask.set('Time_Unit_of_Cost', (original && original.get('Time_Unit_of_Cost')) || null);
        newTask.set('Time_Unit_of_Effort', (original && original.get('Time_Unit_of_Effort')) || null);
        newTask.set('Time_Unit_of_Duration', (original && original.get('Time_Unit_of_Duration')) || null);
        newTask.set('Text1', (original && original.get('Text1')) || null);
        newTask.set('Text2', (original && original.get('Text2')) || null);
        newTask.set('Text3', (original && original.get('Text3')) || null);
        newTask.set('Text4', (original && original.get('Text4')) || null);
        newTask.set('Text5', (original && original.get('Text5')) || null);
        newTask.set('Text6', (original && original.get('Text6')) || null);
        newTask.set('Text7', (original && original.get('Text7')) || null);
        newTask.set('Text8', (original && original.get('Text8')) || null);
        newTask.set('Text9', (original && original.get('Text9')) || null);
        newTask.set('Text10', (original && original.get('Text10')) || null);
        newTask.set('Number1', (original && original.get('Number1')) || null);
        newTask.set('Number2', (original && original.get('Number2')) || null);
        newTask.set('Number3', (original && original.get('Number3')) || null);
        newTask.set('Number4', (original && original.get('Number4')) || null);
        newTask.set('Number5', (original && original.get('Number5')) || null);
        newTask.set('Number6', (original && original.get('Number6')) || null);
        newTask.set('Number7', (original && original.get('Number7')) || null);
        newTask.set('Number8', (original && original.get('Number8')) || null);
        newTask.set('Number9', (original && original.get('Number9')) || null);
        newTask.set('Number10', (original && original.get('Number10')) || null);
        newTask.set('Finished_pr', (original && original.get('Finished_pr')) || null);
        newTask.set('End_progn', (original && original.get('End_progn')) || null);
        newTask.set('Dur_progn', (original && original.get('Dur_progn')) || null);
        newTask.set('Work_progn', (original && original.get('Work_progn')) || null);
        newTask.set('EMail', (original && original.get('EMail')) || null);
        newTask.set('Qty_Pld', (original && original.get('Qty_Pld')) || null);
        newTask.set('Qty_durch_Time_Pld', (original && original.get('Qty_durch_Time_Pld')) || null);
        newTask.set('Qty_Act', (original && original.get('Qty_Act')) || null);
        newTask.set('Qty_durch_Time_Act', (original && original.get('Qty_durch_Time_Act')) || null);
        newTask.set('Factor', (original && original.get('Factor')) || null);
        newTask.set('Buffer', (original && original.get('Buffer')) || null);
        newTask.set('Buffer_cum', (original && original.get('Buffer_cum')) || null);
        newTask.set('Buffer_Act', (original && original.get('Buffer_Act')) || null);
        newTask.set('Buffer_Act_cum', (original && original.get('Buffer_Act_cum')) || null);
        newTask.set('Constraint_Type', (original && original.get('Constraint_Type')) || null);
        newTask.set('Successors', (original && original.get('Successors')) || null);
        newTask.set('Group_Name', (original && original.get('Group_Name')) || null);
        //newTask.set('Ord_ID', (original && original.get('Ord_ID')) || null);
        //newTask.set('Pro_ID', (original && original.get('Pro_ID')) || null);
        //newTask.set('Vor_ID', (original && original.get('Vor_ID')) || null);
        //newTask.set('Ord_ID2', (original && original.get('Ord_ID2')) || null);
        //newTask.set('Pro_ID2', (original && original.get('Pro_ID2')) || null);
        //newTask.set('Vor_ID2', (original && original.get('Vor_ID2')) || null);
        newTask.set('Rec_Options', (original && original.get('Rec_Options')) || null);
        //newTask.set('Outline_Level', (original && original.get('Outline_Level')) || null);
        newTask.set('NoticeStatus', (original && original.get('NoticeStatus')) || null);
        newTask.set('Auftr_ID', (original && original.get('Auftr_ID')) || null);
        newTask.set('Bearb_ID', (original && original.get('Bearb_ID')) || null);
        newTask.set('LastUserMod', (original && original.get('LastUserMod')) || null);
        newTask.set('LastModification', (original && original.get('LastModification')) || null);
        newTask.set('LastUser', (original && original.get('LastUser')) || null);

        original.eachChild(function (t) {
            newTask.addSubtask(me.copyTaskWithChildren(t));
        });

        return newTask;
    },

    // actions follow below
    deleteTask: function () {
        var e = this.grid.getSelectionModel().selected;
        this.grid.taskStore.remove(e.getRange());
    },

    editLeftLabel: function () {
        this.grid.getSchedulingView().editLeftLabel(this.rec);
    },

    editRightLabel: function () {
        this.grid.getSchedulingView().editRightLabel(this.rec);
    },

    converToMilestone: function () {
        var task = this.rec;
        task.setStartDate(task.getEndDate(), false);
    },

    converToRegularTask: function () {
        var task = this.rec;
        task.set({
            StartDate: task.calculateStartDate(task.getStartDate(), 1, Sch.util.Date.DAY),
            EndDate: task.getStartDate(),
            Duration: 1,
            DurationUnit: Sch.util.Date.DAY
        });
    },

    /**
     * Handler for the "add task above" menu item
     */
    addTaskAboveAction: function () {
        this.addTaskAbove(this.copyTask(this.rec));
    },

    /**
     * Handler for the "add task below" menu item
     */
    addTaskBelowAction: function () {
        this.addTaskBelow(this.copyTask(this.rec));
    },

    OnLevel1Action: function (newTask) {
        var tasks = this.grid.getSelectionModel().selected;
        newTask.Outline_Level = 1;
        newTask.leaf = true;
        newTask.Name = '(Folder desig.)';
        newTask.isCriticalTask = false;
        newTask.isClarificationRequired = false;
        newTask.isPassive = false;
        newTask.isCancelled = false;
        newTask.isNotice = false;
        newTask.isCompleted = false;
        newTask.isReshow = false;
        newTask.NoticeStatus = 3;
        newTask.LastModification = new Date();
        newTask.LastUserMod = new Date();
        newTask.Group_Name = newTask.Name;
        newTask.Created = new Date();
        if (tasks.items.length > 0) {
            newTask.parentId = tasks.items[0].data.Id;
        } else {
            newTask.parentId = 0;
        }
        this.grid.taskStore.getRootNode().appendChild(newTask);
    },

    OnLevel2Action: function (newTask) {
        var task = this.rec;

        if (task.data.Outline_Level == 1) {
            newTask.Outline_Level = 2;
            newTask.leaf = true;
            newTask.Name = '(Project desig.)';
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.Name;
            newTask.Created = new Date();
            task.set('leaf', false);
            task.set('Object', parseInt(task.data.Object) + 1);
            task.appendChild(newTask);
            task.expand();
        }
        else if (task.data.Outline_Level == 2) {
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.data.Name;
            newTask.Created = new Date();
            newTask.Outline_Level = 2;
            newTask.Name = '(Project desig.)';
            newTask.leaf = true;
            task.parentNode.set('Object', parseInt(task.parentNode.data.Object) + 1);
            if (task) {
                task.parentNode.insertBefore(newTask, task.nextSibling);
            } else {
                this.grid.taskStore.getRootNode().appendChild(newTask);
            }
        } else if (task.data.Outline_Level >= 3) {
            var parentTask;
            findRootParentNode(task);
            function findRootParentNode(t) {
                if (t.parentNode.data.Id > 0) {
                    findRootParentNode(t.parentNode);
                } else {
                    parentTask = t;
                }
            }
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.data.Name;
            newTask.Created = new Date();
            newTask.Outline_Level = 2;
            newTask.Name = '(Project desig.)';
            newTask.leaf = true;
            parentTask.set('Object', parseInt(parentTask.data.Object) + 1);
            parentTask.appendChild(newTask);
        }
    },

    OnLevel3Action: function (newTask) {
        var task = this.rec;
        if (task.data.Outline_Level == 2) {
            newTask.Outline_Level = 3;
            newTask.leaf = true;
            newTask.Name = '(Task desig.)';
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.data.Name;
            newTask.Created = new Date();
            task.set('leaf', false);
            task.set('Object', parseInt(task.data.Object) + 1);
            task.appendChild(newTask);
            task.expand();
        }
        else if (task.data.Outline_Level >= 3) {
            var parentTask;
            findRootParentNode(task);
            function findRootParentNode(t) {
                if (t.parentNode.data.Outline_Level >= 2) {
                    findRootParentNode(t.parentNode);
                } else {
                    parentTask = t;
                }
            }
            newTask.Outline_Level = 3;
            newTask.leaf = true;
            newTask.Name = '(Task desig.)';
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.data.Name;
            newTask.Created = new Date();
            parentTask.set('leaf', false);
            parentTask.set('Object', parseInt(parentTask.data.Object) + 1);
            parentTask.appendChild(newTask);
            parentTask.expand();
        }

    },

    /**
     * Handler for the "add subtask" menu item
     */
    addSubtask: function (newTask) {
        var task = this.rec;
        newTask.leaf = true;
        newTask.parentId = task.data.Id;
        newTask.Outline_Level = task.data.Outline_Level + 1;
        if (task.data.Outline_Level == 1) {
            newTask.Name = '(Project desig.)';
        }
        else if (task.data.Outline_Level >= 2) {
            newTask.Name = '(Task desig.)';
        }
        newTask.isCriticalTask = false;
        newTask.isClarificationRequired = false;
        newTask.isPassive = false;
        newTask.isCancelled = false;
        newTask.isNotice = false;
        newTask.isCompleted = false;
        newTask.isReshow = false;
        newTask.NoticeStatus = 3;
        newTask.LastModification = new Date();
        newTask.LastUserMod = new Date();
        newTask.Group_Name = task.parentNode.data.Name;
        newTask.Created = new Date();
        task.set('leaf', false);
        task.set('Object', parseInt(task.data.Object) + 1);
        task.appendChild(newTask);
        task.expand();
    },

    /**
     * Handler for the "add successor" menu item
     */
    addSuccessor: function () {
        var task = this.rec,
            depStore = this.grid.dependencyStore,
            newTask = this.copyTask(task);

        this.addTaskBelow(newTask);
        newTask.set('StartDate', task.getEndDate());
        newTask.set('NoticeStatus', '3');
        if (task.data.Outline_Level == 2) {
            newTask.Name = '(Project desig.)';
        }
        else if (task.data.Outline_Level > 2) {
            newTask.Name = '(Task desig.)';
        }
        newTask.setDuration(1, Sch.util.Date.DAY);
        //task.parentNode.set('Object', parseInt(task.parentNode.data.Object) + 1);
        var dependencyClass = depStore.model;

        depStore.add(
            new dependencyClass({
                From: task.getId() || task.internalId,
                To: newTask.getId() || newTask.internalId,
                Type: dependencyClass.EndToStart
            })
        );
    },

    /**
     * Handler for the "add predecessor" menu item
     */
    addPredecessor: function () {
        var task = this.rec;
        var depStore = this.grid.dependencyStore;
        var newTask = this.copyTask(task);
        this.addTaskAbove(newTask);
        newTask.set('NoticeStatus', '3');
        if (task.data.Outline_Level == 1) {
            newTask.set('Name', '(Folder desig.)');
        }
        else if (task.data.Outline_Level == 2) {
            newTask.set('Name', '(Project desig.)');
        }
        else if (task.data.Outline_Level > 2) {
            newTask.set('Name', '(Task desig.)');
        }
        newTask.set({
            StartDate: newTask.calculateStartDate(task.getStartDate(), 1, Sch.util.Date.DAY),
            EndDate: task.getStartDate(),
            Duration: 1,
            DurationUnit: Sch.util.Date.DAY
        });

        var dependencyClass = depStore.model;

        depStore.add(
            new dependencyClass({
                From: newTask.getId() || newTask.internalId,
                To: task.getId() || task.internalId,
                Type: dependencyClass.EndToStart
            })
        );
    },

    /**
     * Handler for the "add milestone" menu item
     */
    addMilestone: function () {
        var task = this.rec,
            newTask = this.copyTask(task);

        this.addTaskBelow(newTask);
        if (task.data.Outline_Level == 1) {
            newTask.Name = '(Project desig.)';
        }
        else if (task.data.Outline_Level >= 2) {
            newTask.Name = '(Task desig.)';
        }
        newTask.setStartDate(newTask.getEndDate(), false);
        newTask.set('NoticeStatus', '1');
    }
});
function loadAllData(Tasks, Resources, ResourceAssignments, SelectedId) {
    var grid = Ext.ComponentQuery.query("#gv")[0];
    if (typeof(Tasks) !== 'undefined') {
        grid.taskStore.getRootNode().removeAll();
        grid.taskStore.getRootNode().appendChild(Tasks);
    }
    if (typeof(Resources) !== 'undefined') {
        grid.resourceStore.loadData(Resources);
    }
    if (typeof(ResourceAssignments) !== 'undefined') {
        grid.assignmentStore.loadData(ResourceAssignments);
    }
    grid.view.refresh();
}
