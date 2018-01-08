Ext.define('APlan.crud.CrudManager', {
    extend          : 'Gnt.data.CrudManager',
    alias           : 'crudmanager.advanced-crudmanager',
    autoLoad        : true,
    transport       : {
        load : {
            method      : 'GET',
            paramName   : 'q',
            url         : (Config.Data.ENV == "local")? 'php/read.php' :  'Get/All?filter=null&isCombine=false&Outline_Level=1&from=Ribbon&_dc=1468849437305&node='
        },
        sync : {
            method      : 'POST',
            url         : 'php/save.php'
        }
    }
});
