Ext.define("APlan.model.SchedulerResource", {
    extend: "Sch.model.Resource",
    clsField: "ResourceType",
    fields: [
        { name: "Id", type: "string", useNull: true },
        { name: "Name", type: "string", useNull: true },
        { name: "Folder", type: "string", useNull: true },
        { name: "Project", type: "string", useNull: true },
        { name: "Outline_Level", type: "int", useNull: true },
        { name: "Index", type: "int", useNull: true },
        { name: "Priority", type: "int", useNull: true },
        { name: "Code", type: "string", useNull: true },
    ]
});