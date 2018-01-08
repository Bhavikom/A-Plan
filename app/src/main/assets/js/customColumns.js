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
        locked: true,
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
            var AllStatusSymbol = [];
            AllStatusSymbol = r.data.StatusObj.split(',');
            var a = '';
            if(AllStatusSymbol.length <= 1) {
                return a;
            }
            for (var i = 0; i < AllStatusSymbol.length; i++) {
                switch(AllStatusSymbol[i]) {
                    case "ShowOverdueFinished": //Status Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/04_task_overdue_16.png" >';
                        break;
                    case "ShowPassiv": //Status Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/11_task_passive_16.png">';
                        break;
                    case "ShowCanceled": //Status Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/12_task_canceled_16.png">';
                        break;
                    case "ShowArrow": //Status Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/16_arrow_16.png">';
                        break;
                    case "ShowCritical": //Status Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/17_flash_16.png">';
                        break;
                    case "ShowQuestion": //Status Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/18_question_16.png">';
                        break;
                    case "ShowReminder": //Status Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/19_warning_16.png">';
                        break;
                    case "ShowOverdueProgn": //Date Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/33_time_overrun_16.png">';
                        break;
                    case "ShowOverdueLimit": //Date Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/32_Limit_16.png" title="Date of completion overrun" style="vertical-align: middle">';
                        break;
                    case "ShowCapacityTooLow": //Date Columns red icon
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/CapacityTooLow.png" alt="Overdue Cost">';
                        break;
                    case "ShowCapacityTooHigh": //Date Columns Green icon
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/CapacityTooHigh.png" alt="Overdue Cost">';
                        break;
                    case "ShowFinished": //Status Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/10_check_16.png" style="vertical-align: middle">';
                        break;
                    case "ShowNormalLock": //Status Columns
                        a = a + '&nbsp;<img style="margin-top: -11px;" src="Ribbon/images/Icons/normal_lock.png" width="12"  style="vertical-align: middle">';
                        break;
                    case "ShowMasterLock": //Status Columns
                        a = a + '&nbsp;<img style="margin-top: -11px;" src="Ribbon/images/Icons/master_lock.png" width="17" height="19" style="vertical-align: middle">';
                        break;
                }
            }
            return a;
        },
        editor: new Ext.form.DateField({
            allowBlank: false,
            format: 'm/d/y'
        }),
        width: 50
    },
    {
        id:  "DateDiffAndIcons",
        header: "Rem Time",
        sortable: true,
        width: 70,
        menuDisabled: true,
        renderer: function (v, m, r) {
            var AllStatusSymbol = [];
            AllStatusSymbol = r.data.StatusObj.split(',');
            var a = '';
            if(AllStatusSymbol.length <= 1) {
                return a;
            }
            var DurationUnit = r.data.Rem_Time + GetDurationUnit(r.data.DurationUnitNew);
            for (var i = 0; i < AllStatusSymbol.length; i++) {
                switch(AllStatusSymbol[i]) {
                    case "ShowOverdueFinished": //Status Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/04_task_overdue_16.png" >';
                        return a + "&nbsp;" + DurationUnit;
                        break;

                    case "ShowStarted": //Date Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/07_task_running_16.png" title="Started! Task has started but not completed yet." style="vertical-align: middle">';
                        return a + "&nbsp;" + DurationUnit;
                        break;
                    case "ShowFinishToday": //Date Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/05_task_today_16.png"  title="Complete Today! Show Task which must be completed today." style="vertical-align: middle">';
                        return a + "&nbsp;" + DurationUnit;
                        break;
                    case "ShowFinishWeek": //Date Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/06_task_thisweek_16.png" title="To be completed in 1 week" style="vertical-align: middle">';
                        return a + "&nbsp;" + DurationUnit;
                        break;
                    case "ShowNoTime": //Date Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/09_task_missing_16.png" title="Tasks wihtout date" style="vertical-align: middle">';
                        return DurationUnit+ ' ' + a;
                        break;

                    case "ShowOverdueCost": //Date Columns
                        a = "";
                        //a = a + '&nbsp;<img src="Ribbon/images/Icons/33_time_overrun_16333.png" alt="Overdue Cost" style="vertical-align: middle">';
                        return DurationUnit + ' ' + a;
                        break;
                    case "ShowPending": //Date Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/08_task_waiting_16.png" title="Pending projects/tasks" alt="Overdue Cost" style="vertical-align: middle">';
                        return DurationUnit + "&nbsp;"+ a ;
                        break;
                    case "ShowFinished": //Date Columns
                        return a = a + '&nbsp;<img src="Ribbon/images/Icons/10_check_16.png" style="vertical-align: middle">';
                        break;
                }
            }
            //return r.data.Rem_Time + a;
        }
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
        width: 80
    },
    {
        header: 'Note',
        id:  "NoteId",
        dataIndex: 'Notes',
        width: 150,
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'resourceassignmentcolumn',
        header: 'Resources',
        id:  "AssignedResourcesId",
        width: 150,
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'startdatecolumn',
        header: 'Begin Pl\'d\'',
        id:  "StartId",
        dataIndex: 'StartDate',
        width: 80,
        sortable: false,
        menuDisabled: true,
        format : Config.Data.DE_DATE_FORMAT
    },
    {
        xtype: 'enddatecolumn',
        header: 'End Pl\'d\'',
        id:  "EndId",
        dataIndex: 'EndDate',
        width: 80,
        sortable: false,
        menuDisabled: true,
        format : Config.Data.DE_DATE_FORMAT
    },
    /*Duration cols Start*/
    {
        id:  "durationId",
        sortable: false,
        menuDisabled: true,
        renderer: function (e, t, n) {
            var DurationUnit = GetDurationUnit(n.data.DurationUnitNew);
            return (n.data.Duration > 0) ? n.data.Duration + DurationUnit: "";
        }
    },
    /*,
     {
     id:  "DurationActualColumnId",
     header: "Duration",
     sortable: true,
     width: 70,
     menuDisabled: true,
     dataIndex: "Actual_Duration"
     },*/
    /*{
     id:  "DurationPldColumnId",
     header: "Duration Pl'd",
     sortable: true,
     width: 70,
     menuDisabled: true,
     dataIndex: "Duration_Act_div_Pld"
     },*/
    /*{
     id:  "DurationActPldColumnId",
     header: "Duration Actual Pl'd",
     sortable: true,
     width: 70,
     menuDisabled: true,
     dataIndex: "Duration_Act_Pld"
     },*/
    /*{
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
     },*/
    /*Duration cols end*/
    {
        //xtype: 'effortcolumn',
        header: "Work Pl'd",
        id:"effortId",
        sortable: false,
        width: 70,
        menuDisabled: true,
        dataIndex : "Effort",
        renderer: function(e, t, n) {
            var Unit = GetWorkUnit(n.data.UnitForWorkNew);
            if(n.data.Effort == 0) return "";
            var ToStringData = n.data.Effort.toString();
            return (n.data.Effort > 0 && ToStringData.indexOf(".") == -1) ? n.data.Effort + ".0"+Unit: n.data.Effort +Unit;
        }
    },
    {
        //xtype: 'effortcolumn',
        header: "Work Pl'd/req",
        id:"WorkPldreqId",
        sortable: false,
        width: 70,
        menuDisabled: true,
        dataIndex : "DummyData",
        renderer: function(e, t, n) {
            var Data1 = n.data.Effort;
            var Data2 = n.data.Req_Effort;
            /*if(Data2 != 0)
             return (Math.round((((Data1 / Data2) * 100 ) - 100) * 100) / 100).toString() + " %";*/
            return "";
        }
    },
    {
        header: "Object",
        id:"ObjectId",
        sortable: false,
        width: 70,
        menuDisabled: true,
        dataIndex : "FileName"
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
        menuDisabled: true,
        renderer: function (v, m, r) {
            return FormatedNumber(r.data.Number1);
        }
    },
    {
        xtype: 'Number2column',
        header: 'Number2',
        id:  "Number2Id",
        dataIndex: "Number2",
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            return FormatedNumber(r.data.Number2);
        }
    },
    {
        xtype: 'Number3column',
        header: 'Number3',
        id:  "Number3Id",
        dataIndex: "Number3",
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            return FormatedNumber(r.data.Number3);
        }
    },
    {
        xtype: 'Number4column',
        header: 'Number4',
        id:  "Number4Id",
        dataIndex: "Number4",
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            return FormatedNumber(r.data.Number4);
        }
    },
    {
        xtype: 'Number5column',
        header: 'Number5',
        id:  "Number5Id",
        dataIndex: "Number5",
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            return FormatedNumber(r.data.Number5);
        }
    },
    {
        xtype: 'Number6column',
        header: 'Number6',
        id:  "Number6Id",
        dataIndex: "Number6",
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            return FormatedNumber(r.data.Number6);
        }
    },
    {
        xtype: 'Number7column',
        header: 'Number7',
        id:  "Number7Id",
        dataIndex: "Number7",
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            return FormatedNumber(r.data.Number7);
        }
    },
    {
        xtype: 'Number8column',
        header: 'Number8',
        id:  "Number8Id",
        dataIndex: "Number8",
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            return FormatedNumber(r.data.Number8);
        }
    },
    {
        xtype: 'Number9column',
        header: 'Number9',
        id:  "Number9Id",
        dataIndex: "Number9",
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            return FormatedNumber(r.data.Number9);
        }
    },
    {
        xtype: 'Number10column',
        header: 'Number10',
        id:  "Number10Id",
        dataIndex: "Number10",
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            return FormatedNumber(r.data.Number10);
        }
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
        format : Config.Data.DE_DATE_FORMAT
    },
    {
        xtype: 'lateenddatecolumn',
        header: 'End late',
        id:  "lateenddatecolumnId",
        dataIndex: "LateFinish",
        sortable: false,
        menuDisabled: true,
        format : Config.Data.DE_DATE_FORMAT
    },
    {
        header: 'End late curr',
        id:  "EndlatecurrId",
        dataIndex: "LateFinish",
        sortable: false,
        menuDisabled: true,
        format : Config.Data.DE_DATE_FORMAT
    },
    {
        //xtype: 'PriorityColumn',
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
        header: 'Finished %',
        id:  "FinishedId",
        dataIndex: 'Finished_pr',
        width: 80,
        sortable: true,
        menuDisabled: true,
        renderer: function(e, t, n) {
            return (n.data.Finished_pr > 0) ? n.data.Finished_pr + " %": "0.0 %";
        }
    },
    {
        header: 'Work Pld-req',
        id:  "WorkPld-ReqId",
        dataIndex: 'DummyData',
        width: 80,
        sortable: true,
        menuDisabled: true,
        renderer: function(e, t, n) {
            return (n.data.DummyData > 0) ? n.data.DummyData + " %": "0.0 %";
        }
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
        header: 'Begin Late Curr',
        id:  "BeginLateCurrId",
        dataIndex: "BeginLateCurr",
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
        header: 'Begin early curr',
        id:  "BeginEarlyCurrId",
        dataIndex: "BeginEarlyCurr",
        sortable: true,
        menuDisabled: true,
        format : Config.Data.DE_DATE_FORMAT
    },
    /*{
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
     },*/
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
        menuDisabled: true,
        renderer: function(e, t, n) {
            return (n.data.Buffer_cum > 0) ? n.data.Buffer_cum : "";
        }
    },
    {
        header: 'OvtWork Pld',
        id:  "OvtWorkPldId",
        dataIndex: "DummyData",
        sortable: true,
        menuDisabled: true
    },
    {
        header: 'Index',
        id:  "IndexId",
        dataIndex: "DummyData",
        sortable: true,
        menuDisabled: true
    },
    {
        header: 'TimeUnit Dur.',
        id:  "TimeUnitDurId",
        dataIndex: "DurationUnitNew",
        sortable: false,
        menuDisabled: true,
        renderer: function(e, t, n) {
            return GetDurationUnitColumn(n.data.DurationUnitNew);
        }
    },
    {
        header: 'TimeUnit Work',
        id:  "TimeUnitWorkId",
        dataIndex: "UnitForWorkNew",
        sortable: false,
        menuDisabled: true,
        renderer: function(e, t, n) {
            return GetDurationUnitColumn(n.data.UnitForWorkNew);
        }
    },
    /*{
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
     },*/
    {
        header: 'Link',
        id:  "LinkId",
        menuDisabled: true,
        width: 30,
        dataIndex: "Link"
    },
    {
        header: 'CapUsg Pld',
        id:  "CapacityUsageId",
        menuDisabled: true,
        width: 30,
        dataIndex: "Capacity_usage"
    },
    {
        header: 'Ident-No.',
        id:  "IdentNoId",
        menuDisabled: true,
        width: 30,
        dataIndex: "ID_Nr"
    },
    {
        header: 'Res. req.',
        id:  "ResourceRequiredId",
        menuDisabled: true,
        width: 30,
        dataIndex: "Req_Ress",
        renderer: function(e, t, n) {
            if(n.data.Req_Ress == 0) return "";
            var ToStringData = n.data.Req_Ress.toString();
            return (n.data.Req_Ress > 0 && ToStringData.indexOf(".") == -1) ? n.data.Req_Ress + ".0 Ak" : n.data.Req_Ress + " Ak";
        }
    },
    {
        header: 'Work Req',
        id:  "WorkReqId",
        menuDisabled: true,
        width: 30,
        dataIndex: "Req_Effort",
        renderer: function(e, t, n) {
            if(n.data.Req_Effort == 0)
                return "";
            var WorkUnit =  GetWorkUnit(n.data.UnitForWorkNew);
            return n.data.Req_Effort.toString() + WorkUnit;
        }
    },
    {
        header: '',
        id:  "BlankId",
        menuDisabled: true,
        width: 30
    }
];

var StaticcolumnsResource = [
    {
        header: 'No',
        name: 'SequenceNumber',
        type: 'string',
        id: "NoId",
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
        id: "TasksId",
        tdCls: 'namecell',
        sortable: true,
        locked: true,
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
        id: "StatusId",
        editable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            var AllStatusSymbol = [];
            if (r.data.StatusObj) {
                AllStatusSymbol = r.data.StatusObj.split(',');
            }
            var a = '';
            if (AllStatusSymbol.length <= 1) {
                return a;
            }
            for (var i = 0; i < AllStatusSymbol.length; i++) {
                switch (AllStatusSymbol[i]) {
                    case "ShowOverdueFinished": //Status Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/04_task_overdue_16.png" >';
                        break;
                    case "ShowPassiv": //Status Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/11_task_passive_16.png">';
                        break;
                    case "ShowCanceled": //Status Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/12_task_canceled_16.png">';
                        break;
                    case "ShowArrow": //Status Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/16_arrow_16.png">';
                        break;
                    case "ShowCritical": //Status Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/17_flash_16.png">';
                        break;
                    case "ShowQuestion": //Status Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/18_question_16.png">';
                        break;
                    case "ShowReminder": //Status Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/19_warning_16.png">';
                        break;
                    case "ShowOverdueProgn": //Date Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/33_time_overrun_16.png">';
                        break;
                    case "ShowOverdueLimit": //Date Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/32_Limit_16.png" title="Date of completion overrun" style="vertical-align: middle">';
                        break;
                    case "ShowCapacityTooLow": //Date Columns red icon
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/CapacityTooLow.png" alt="Overdue Cost">';
                        break;
                    case "ShowCapacityTooHigh": //Date Columns Green icon
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/CapacityTooHigh.png" alt="Overdue Cost">';
                        break;
                    case "ShowFinished": //Status Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/10_check_16.png" style="vertical-align: middle">';
                        break;
                    case "ShowNormalLock": //Status Columns
                        a = a + '&nbsp;<img style="margin-top: -11px;" src="Ribbon/images/Icons/normal_lock.png" width="12"  style="vertical-align: middle">';
                        break;
                    case "ShowMasterLock": //Status Columns
                        a = a + '&nbsp;<img style="margin-top: -11px;" src="Ribbon/images/Icons/master_lock.png" width="17" height="19" style="vertical-align: middle">';
                        break;
                }
            }
            return a;
        }
    },
    {
        id: "DateDiffAndIcons",
        header: "Rem Time",
        sortable: true,
        width: 70,
        menuDisabled: true,
        renderer: function (v, m, r) {
            var AllStatusSymbol = [];
            if (r.data.StatusObj) {
                AllStatusSymbol = r.data.StatusObj.split(',');
            }
            var a = '';
            if (AllStatusSymbol.length <= 1) {
                return a;
            }
            var DurationUnit = r.data.Rem_Time + GetDurationUnit(r.data.DurationUnitNew);
            for (var i = 0; i < AllStatusSymbol.length; i++) {
                switch (AllStatusSymbol[i]) {
                    case "ShowOverdueFinished": //Status Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/04_task_overdue_16.png" >';
                        return a + "&nbsp;" + DurationUnit;
                        break;

                    case "ShowStarted": //Date Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/07_task_running_16.png" title="Started! Task has started but not completed yet." style="vertical-align: middle">';
                        return a + "&nbsp;" + DurationUnit;
                        break;
                    case "ShowFinishToday": //Date Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/05_task_today_16.png"  title="Complete Today! Show Task which must be completed today." style="vertical-align: middle">';
                        return a + "&nbsp;" + DurationUnit;
                        break;
                    case "ShowFinishWeek": //Date Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/06_task_thisweek_16.png" title="To be completed in 1 week" style="vertical-align: middle">';
                        return a + "&nbsp;" + DurationUnit;
                        break;
                    case "ShowNoTime": //Date Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/09_task_missing_16.png" title="Tasks wihtout date" style="vertical-align: middle">';
                        return DurationUnit + ' ' + a;
                        break;

                    case "ShowOverdueCost": //Date Columns
                        a = "";
                        //a = a + '&nbsp;<img src="Ribbon/images/Icons/33_time_overrun_16333.png" alt="Overdue Cost" style="vertical-align: middle">';
                        return DurationUnit + ' ' + a;
                        break;
                    case "ShowPending": //Date Columns
                        a = a + '&nbsp;<img src="Ribbon/images/Icons/08_task_waiting_16.png" title="Pending projects/tasks" alt="Overdue Cost" style="vertical-align: middle">';
                        return DurationUnit + "&nbsp;" + a;
                        break;
                    case "ShowFinished": //Date Columns
                        return a = a + '&nbsp;<img src="Ribbon/images/Icons/10_check_16.png" style="vertical-align: middle">';
                        break;
                }
            }
            //return r.data.Rem_Time + a;
        }
    },
    {
        header: 'Traffic Light',
        id: "TrafficLightId",
        editable: false,
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            var returnData = [];
            switch (r.data.TrafficLightObj) {
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
        }
    },
    {
        header: 'Note',
        id: "NoteId",
        dataIndex: 'Notes',
        width: 150,
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'resourceassignmentcolumn',
        header: 'Resources',
        id: "AssignedResourcesId",
        width: 150,
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'startdatecolumn',
        header: 'Begin Pl\'d\'',
        id: "StartId",
        dataIndex: 'StartDate',
        width: 80,
        sortable: false,
        menuDisabled: true,
        format: Config.Data.DE_DATE_FORMAT
    },
    {
        xtype: 'enddatecolumn',
        header: 'End Pl\'d\'',
        id: "EndId",
        dataIndex: 'EndDate',
        width: 80,
        sortable: false,
        menuDisabled: true,
        format: Config.Data.DE_DATE_FORMAT
    },
    /*Duration cols Start*/
    {
        id: "durationId",
        sortable: false,
        menuDisabled: true,
        renderer: function (e, t, n) {
            var DurationUnit = GetDurationUnit(n.data.DurationUnitNew);
            return (n.data.Duration > 0) ? n.data.Duration + DurationUnit : "";
        }
    },

    /*Duration cols end*/
    {
        //xtype: 'effortcolumn',
        header: "Work Pl'd",
        id: "effortId",
        sortable: false,
        width: 70,
        menuDisabled: true,
        dataIndex: "Effort",
        renderer: function (e, t, n) {
            var Unit = GetWorkUnit(n.data.UnitForWorkNew);
            if (!n.data.Effort || n.data.Effort == 0) return "";
            var ToStringData = n.data.Effort.toString();
            return (n.data.Effort > 0 && ToStringData.indexOf(".") == -1) ? n.data.Effort + ".0" + Unit : n.data.Effort + Unit;
        }
    },
    {
        //xtype: 'effortcolumn',
        header: "Work Pl'd/req",
        id: "WorkPldreqId",
        sortable: false,
        width: 70,
        menuDisabled: true,
        dataIndex: "DummyData",
        renderer: function (e, t, n) {
            //var Data1 = n.data.Effort;
            //var Data2 = n.data.Req_Effort;
            /*if(Data2 != 0)
             return (Math.round((((Data1 / Data2) * 100 ) - 100) * 100) / 100).toString() + " %";*/
            return "";
        }
    },
    {
        header: "Object",
        id: "ObjectId",
        sortable: false,
        width: 70,
        menuDisabled: true,
        dataIndex: "FileName"
    },
    {
        xtype: 'Text1column',
        header: 'Text1',
        id: "Text1Id",
        dataIndex: "Text1",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Text2column',
        header: 'Text2',
        id: "Text2Id",
        dataIndex: "Text2",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Text3column',
        header: 'Text3',
        id: "Text3Id",
        dataIndex: "Text3",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Text4column',
        header: 'Text4',
        id: "Text4Id",
        dataIndex: "Text4",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Text5column',
        header: 'Text5',
        id: "Text5Id",
        dataIndex: "Text5",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Text6column',
        header: 'Text6',
        id: "Text6Id",
        dataIndex: "Text6",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Text7column',
        header: 'Text7',
        id: "Text7Id",
        dataIndex: "Text7",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Text8column',
        header: 'Text8',
        id: "Text8Id",
        dataIndex: "Text8",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Text9column',
        header: 'Text9',
        id: "Text9Id",
        dataIndex: "Text9",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Text10column',
        header: 'Text10',
        id: "Text10Id",
        dataIndex: "Text10",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'Number1column',
        header: 'Number1',
        id: "Number1Id",
        dataIndex: "Number1",
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            return FormatedNumber(r.data.Number1);
        }
    },
    {
        xtype: 'Number2column',
        header: 'Number2',
        id: "Number2Id",
        dataIndex: "Number2",
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            return FormatedNumber(r.data.Number2);
        }
    },
    {
        xtype: 'Number3column',
        header: 'Number3',
        id: "Number3Id",
        dataIndex: "Number3",
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            return FormatedNumber(r.data.Number3);
        }
    },
    {
        xtype: 'Number4column',
        header: 'Number4',
        id: "Number4Id",
        dataIndex: "Number4",
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            return FormatedNumber(r.data.Number4);
        }
    },
    {
        xtype: 'Number5column',
        header: 'Number5',
        id: "Number5Id",
        dataIndex: "Number5",
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            return FormatedNumber(r.data.Number5);
        }
    },
    {
        xtype: 'Number6column',
        header: 'Number6',
        id: "Number6Id",
        dataIndex: "Number6",
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            return FormatedNumber(r.data.Number6);
        }
    },
    {
        xtype: 'Number7column',
        header: 'Number7',
        id: "Number7Id",
        dataIndex: "Number7",
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            return FormatedNumber(r.data.Number7);
        }
    },
    {
        xtype: 'Number8column',
        header: 'Number8',
        id: "Number8Id",
        dataIndex: "Number8",
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            return FormatedNumber(r.data.Number8);
        }
    },
    {
        xtype: 'Number9column',
        header: 'Number9',
        id: "Number9Id",
        dataIndex: "Number9",
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            return FormatedNumber(r.data.Number9);
        }
    },
    {
        xtype: 'Number10column',
        header: 'Number10',
        id: "Number10Id",
        dataIndex: "Number10",
        sortable: false,
        menuDisabled: true,
        renderer: function (v, m, r) {
            return FormatedNumber(r.data.Number10);
        }
    },
    {
        xtype: 'ClientColumn',
        header: 'Client',
        id: "ClientNameId",
        dataIndex: "ClientName",
        sortable: false,
        menuDisabled: true
    },
    {
        xtype: 'ResponsibleColumn',
        header: 'Responsible',
        id: "ResponsiblePersonNameId",
        dataIndex: "ResponsiblePersonName",
        sortable: false,
        menuDisabled: true
    },
    {
        // xtype: 'earlystartdatecolumn',
        header: 'Begin early',
        id: "earlystartdatecolumnId",
        dataIndex: "EarlyStart",
        sortable: false,
        menuDisabled: true,
        format: Config.Data.DE_DATE_FORMAT
    },
    {
        // xtype: 'lateenddatecolumn',
        header: 'End late',
        id: "lateenddatecolumnId",
        dataIndex: "LateFinish",
        sortable: false,
        menuDisabled: true,
        format: Config.Data.DE_DATE_FORMAT
    },
    {
        header: 'End late curr',
        id: "EndlatecurrId",
        dataIndex: "LateFinish",
        sortable: false,
        menuDisabled: true,
        format: Config.Data.DE_DATE_FORMAT
    },
    {
        //xtype: 'PriorityColumn',
        header: 'Priority',
        id: "PriorityColumnId",
        dataIndex: "Priority",
        sortable: true,
        menuDisabled: true
    },
    {
        xtype: 'completedcolumn',
        header: 'Completed',
        id: "CompletedId",
        dataIndex: 'Completed',
        width: 80,
        sortable: false,
        menuDisabled: true,
        format: Config.Data.DE_DATE_FORMAT
    },
    {
        header: 'Finished %',
        id: "FinishedId",
        dataIndex: 'Finished_pr',
        width: 80,
        sortable: true,
        menuDisabled: true,
        renderer: function (e, t, n) {
            return (n.data.Finished_pr && n.data.Finished_pr > 0) ? n.data.Finished_pr + " %" : "0.0 %";
        }
    },
    {
        header: 'Work Pld-req',
        id: "WorkPld-ReqId",
        dataIndex: 'DummyData',
        width: 80,
        sortable: true,
        menuDisabled: true,
        renderer: function (e, t, n) {
            return (n.data.DummyData && n.data.DummyData > 0) ? n.data.DummyData + " %" : "0.0 %";
        }
    },
    {
        xtype: 'createdcolumn',
        header: 'Entry Date',
        id: "CreatedId",
        dataIndex: 'Created',
        width: 80,
        sortable: false,
        menuDisabled: true,
        format: Config.Data.DE_DATE_FORMAT
    },
    {
        xtype: 'ReshowDateColumn',
        header: 'Reshow Date',
        id: "ReshowDateColumnId",
        dataIndex: "Reshow_Date",
        sortable: true,
        menuDisabled: true,
        format: Config.Data.DE_DATE_FORMAT
    },
    {
        xtype: 'LatefinishColumn',
        header: 'End Late',
        id: "LatefinishColumnId",
        dataIndex: "LateFinish",
        sortable: true,
        menuDisabled: true,
        format: Config.Data.DE_DATE_FORMAT
    },
    {
        xtype: 'LateStartColumn',
        header: 'Begin Late',
        id: "LateStartColumnId",
        dataIndex: "LateStart",
        sortable: true,
        menuDisabled: true,
        format: Config.Data.DE_DATE_FORMAT
    },
    {
        header: 'Begin Late Curr',
        id: "BeginLateCurrId",
        dataIndex: "BeginLateCurr",
        sortable: true,
        menuDisabled: true,
        format: Config.Data.DE_DATE_FORMAT
    },
    {
        xtype: 'EarlyStartColumn',
        header: 'Begin Early',
        id: "EarlyStartColumnId",
        dataIndex: "EarlyStart",
        sortable: true,
        menuDisabled: true,
        format: Config.Data.DE_DATE_FORMAT
    },
    {
        header: 'Begin early curr',
        id: "BeginEarlyCurrId",
        dataIndex: "BeginEarlyCurr",
        sortable: true,
        menuDisabled: true,
        format: Config.Data.DE_DATE_FORMAT
    },
    {
        header: 'Last Change',
        id: "LastChangeColumnId",
        dataIndex: "LastUserDateAndName",
        sortable: true,
        menuDisabled: true
    },
    {
        header: 'Level',
        id: "TaskLevelColumnId",
        dataIndex: "TaskLevel",
        sortable: true,
        menuDisabled: true
    },
    {
        header: 'Factor',
        id: "FactorColumnId",
        dataIndex: "Factor",
        sortable: true,
        menuDisabled: true,
        renderer: function (e, t, n) {
            return (n.data.Factor) ? n.data.Factor : "";
        }
    },
    {
        header: 'Email To',
        id: "EmailToColumnId",
        dataIndex: "EMail",
        sortable: true,
        menuDisabled: true
    },
    {
        header: 'Buffer',
        id: "BufferColumnId",
        dataIndex: "Buffer",
        sortable: true,
        menuDisabled: true,
        renderer: function (e, t, n) {
            return (n.data.Buffer) ? n.data.Buffer + " h" : "";
        }
    },
    {
        header: 'Buffer Cum',
        id: "BufferCumColumnId",
        dataIndex: "Buffer_cum",
        sortable: true,
        menuDisabled: true,
        renderer: function (e, t, n) {
            return (n.data.Buffer_cum && n.data.Buffer_cum > 0) ? n.data.Buffer_cum : "";
        }
    },
    {
        header: 'OvtWork Pld',
        id: "OvtWorkPldId",
        dataIndex: "DummyData",
        sortable: true,
        menuDisabled: true
    },
    {
        header: 'Index',
        id: "IndexId",
        dataIndex: "DummyData",
        sortable: true,
        menuDisabled: true
    },
    {
        header: 'TimeUnit Dur.',
        id: "TimeUnitDurId",
        dataIndex: "DurationUnitNew",
        sortable: false,
        menuDisabled: true,
        renderer: function (e, t, n) {
            return GetDurationUnitColumn(n.data.DurationUnitNew);
        }
    },
    {
        header: 'TimeUnit Work',
        id: "TimeUnitWorkId",
        dataIndex: "UnitForWorkNew",
        sortable: false,
        menuDisabled: true,
        renderer: function (e, t, n) {
            return GetDurationUnitColumn(n.data.UnitForWorkNew);
        }
    },
    {
        header: 'Link',
        id: "LinkId",
        menuDisabled: true,
        width: 30,
        dataIndex: "Link"
    },
    {
        header: 'CapUsg Pld',
        id: "CapacityUsageId",
        menuDisabled: true,
        width: 30,
        dataIndex: "Capacity_usage"
    },
    {
        header: 'Ident-No.',
        id: "IdentNoId",
        menuDisabled: true,
        width: 30,
        dataIndex: "ID_Nr"
    },
    {
        header: 'Res. req.',
        id: "ResourceRequiredId",
        menuDisabled: true,
        width: 30,
        dataIndex: "Req_Ress",
        renderer: function (e, t, n) {
            if (!n.data.Req_Ress || n.data.Req_Ress == 0) return "";
            var ToStringData = n.data.Req_Ress.toString();
            return (n.data.Req_Ress > 0 && ToStringData.indexOf(".") == -1) ? n.data.Req_Ress + ".0 Ak" : n.data.Req_Ress + " Ak";
        }
    },
    {
        header: 'Work Req',
        id: "WorkReqId",
        menuDisabled: true,
        width: 30,
        dataIndex: "Req_Effort",
        renderer: function (e, t, n) {
            if (!n.data.Req_Effort || n.data.Req_Effort == 0)
                return "";
            var WorkUnit = GetWorkUnit(n.data.UnitForWorkNew);
            return n.data.Req_Effort.toString() + WorkUnit;
        }
    },
    {
        header: '',
        id: "BlankId",
        menuDisabled: true,
        width: 30
    }
];