Ext.define("APlan.store.Assignment",{extend:"Gnt.data.AssignmentStore",autoLoad:!0,proxy:{method:"GET",type:"ajax",api:{read:"Assignments/Get",create:"Assignments/Create",destroy:"Assignments/Delete",update:"Assignments/Update"},reader:{type:"json",rootProperty:"assignmentdata"},writer:{rootProperty:"assignmentdata",type:"json",encode:!0,allowSingle:!1}},listeners:{load:function(){}}});