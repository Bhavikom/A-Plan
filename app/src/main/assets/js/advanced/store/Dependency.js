Ext.define("APlan.store.Dependency", {
        extend: "Gnt.data.DependencyStore",
        autoLoad: true,
        autoSync: true,
        model: "APlan.model.Dependency",
        proxy: {
            type: "ajax",
            url: "dependencies.js",
            method: "GET",
            reader: {
                rootProperty: "dependencydata", type: "json"
            },
            writer: {
                rootProperty: "dependencydata", type: "json",
                encode: true, allowSingle: false
            },
            api: {
                read: "Dependencies/Get",
                create: "Dependencies/Create",
                destroy: "Dependencies/Delete",
                update: "Dependencies/Update"
            }
        }
    }
);