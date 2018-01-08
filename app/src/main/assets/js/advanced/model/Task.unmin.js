Ext.define("APlan.model.Task", {
    extend: "Gnt.model.Task",
    clsField: 'TaskType',
    // Some custom field definitions
    fields: [
        { name: 'Id', type: 'string', useNull: true, filterable: true},
        { name: 'StartDate', type: 'date', dateFormat: 'MS', filterable: true, serialize: function (date) { return date; } },
        { name: 'EndDate', type: 'date', dateFormat: 'MS', serialize: function (date) { return date; } },

        /*Buggy code*/
        { name: 'index', type: 'int', persist: true },
        { name: 'PercentDone', type: 'int' },
        { name: 'Priority', defaultValue: 1 },
        { name: 'parentId', type: 'int', useNull: true, persist: true },
        { name: 'isCriticalTask', type: 'bool' },
        { name: 'isClarificationRequired', type: 'bool' },
        { name: 'LimitDate', type: 'date', dateFormat: 'MS' },
        { name: 'isNotice', type: 'bool' },
        { name: 'isReshow', type: 'bool' },
        { name: 'isCompleted', type: 'bool' },
        { name: 'isPassive', type: 'bool' },
        { name: 'isCancelled', type: 'bool' },
        { name: 'Act_Fixed_Cost', type: 'float' },
        { name: 'Contact', type: 'string' },
        { name: 'Bearb_ID', type: 'string' },
        { name: 'Auftr_ID', type: 'string' },
        { name: 'Resource_Names', type: 'string' },
        { name: 'Responsible', type: 'string' },
        { name: 'Notes', type: 'string' },
        { name: 'Created', type: 'date', dateFormat: 'MS' },
        { name: 'Reshow_Date', type: 'date', dateFormat: 'MS' },
        { name: 'Capacity_usage', type: 'string' },
        { name: 'Actual_Start', type: 'date', dateFormat: 'MS' },
        { name: 'Object', type: 'string' },
        { name: 'Actual_Finish', type: 'date', dateFormat: 'MS' },
        { name: 'Rem_Time', type: 'string' },
        { name: 'Completed', type: 'date', dateFormat: 'MS' },
        { name: 'Actual_Duration', type: 'float' },
        { name: 'Duration_Act_div_Pld', type: 'string' },
        { name: 'Duration_Act_Pld', type: 'string' },
        { name: 'Req_Effort', type: 'string' },
        { name: 'Req_Ress', type: 'float' },
        { name: 'Effort', type: 'float' },
        { name: 'Actual_Effort', type: 'float' },
        { name: 'Effort_Pld_Req', type: 'string' },
        { name: 'Effort_Pld_div_Req', type: 'string' },
        { name: 'Effort_Act_Pld1', type: 'string' },
        { name: 'Effort_Act_Pld', type: 'string' },
        { name: 'Ovt_Effort', type: 'float' },
        { name: 'Actual_Ovt_Effort', type: 'float' },
        { name: 'Fixed_Cost', type: 'float' },
        { name: 'Standard_Rate', type: 'float' },
        { name: 'Act_Standard_Rate', type: 'float' },
        { name: 'Labor_Cost', type: 'string' },
        { name: 'Act_Labor_Cost', type: 'string' },
        { name: 'Ovt_Cost', type: 'float' },
        { name: 'Act_Ovt_Cost', type: 'float' },
        { name: 'Total_Cost', type: 'float' },
        { name: 'Total_Cost_Progn', type: 'float' },
        { name: 'Act_Total_Cost', type: 'float' },
        { name: 'Total_Cost_Act_div_Pld', type: 'string' },
        { name: 'Total_Cost_Act_Pld', type: 'string' },
        { name: 'Cost_Calc_Meth', type: 'int' },
        { name: 'Time_Unit_of_Cost', type: 'int' },
        { name: 'Time_Unit_of_Effort', type: 'int' },
        { name: 'Time_Unit_of_Duration', type: 'int' },
        { name: 'Text1', type: 'string' },
        { name: 'Text2', type: 'string' },
        { name: 'Text3', type: 'string' },
        { name: 'Text4', type: 'string' },
        { name: 'Text5', type: 'string' },
        { name: 'Text6', type: 'string' },
        { name: 'Text7', type: 'string' },
        { name: 'Text8', type: 'string' },
        { name: 'Text9', type: 'string' },
        { name: 'Text10', type: 'string' },
        { name: 'Number1', type: 'float' },
        { name: 'Number2', type: 'float' },
        { name: 'Number3', type: 'float' },
        { name: 'Number4', type: 'float' },
        { name: 'Number5', type: 'float' },
        { name: 'Number6', type: 'float' },
        { name: 'Number7', type: 'float' },
        { name: 'Number8', type: 'float' },
        { name: 'Number9', type: 'float' },
        { name: 'Number10', type: 'float' },
        { name: 'Finished_pr', type: 'string' },
        { name: 'End_progn', type: 'date', dateFormat: 'MS' },
        { name: 'Dur_progn', type: 'float' },
        { name: 'Work_progn', type: 'float' },
        { name: 'EMail', type: 'string' },
        { name: 'Qty_Pld', type: 'float' },
        { name: 'Qty_durch_Time_Pld', type: 'float' },
        { name: 'Qty_Act', type: 'float' },
        { name: 'Qty_durch_Time_Act', type: 'float' },
        { name: 'Factor', type: 'float' },
        { name: 'Buffer', type: 'float' },
        { name: 'Buffer_cum', type: 'float' },
        { name: 'Buffer_Act', type: 'string' },
        { name: 'Buffer_Act_cum', type: 'string' },
        { name: 'Constraint_Type', type: 'string' },
        { name: 'Successors', type: 'string' },
        { name: 'Group_Name', type: 'string' },
        { name: 'Ord_ID', type: 'int' },
        { name: 'Pro_ID', type: 'int' },
        { name: 'Vor_ID', type: 'int' },
        { name: 'Ord_ID2', type: 'int' },
        { name: 'Pro_ID2', type: 'int' },
        { name: 'Vor_ID2', type: 'int' },
        { name: 'Rec_Options', type: 'string' },
        { name: 'Outline_Level', type: 'int' },
        { name: 'NoticeStatus', type: 'string' },
        { name: 'Auftr_ID', type: 'int' },
        { name: 'Bearb_ID', type: 'int' },
        { name: 'LastUserMod', type: 'date', dateFormat: 'MS' },
        { name: 'LastModification', type: 'date', dateFormat: 'MS' },
        { name: 'LastUser', type: 'string' },
        { name: 'Client', type: 'string' },
        { name: 'EarlyStart', type: 'date', dateFormat: 'MS' },
        { name: 'LateFinish', type: 'date', dateFormat: 'MS' },
        { name: 'LateStart', type: 'date', dateFormat: 'MS' },
        { name: 'ID_Nr', type: 'string' },
        { name: 'ClientName', type: 'string' },
        { name: 'ResponsiblePersonName', type: 'string' },
        { name: 'LatestEndDate', type: 'date', dateFormat: 'MS' }
    ]
});
