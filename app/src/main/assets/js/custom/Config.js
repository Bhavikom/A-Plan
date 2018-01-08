var UsersProfiles = null;
var Config = {}, URL = window.location.href;
Config.Data = {
    HOST_URL: getQueryVariable("HOST_URL"),
        //(window.location.origin + window.location.pathname).replace("Control", "management"),
    DEFAULT_URL: "",
    ENV: window.location.href.indexOf("localhost") >= 0 ? "local" : "live",
    RequestheaderSettings: {
        async: !0,
        crossDomain: !0,
        url: "Get/Profile",
        method: "POST",
        headers: { "cache-control": "no-cache", "postman-token": "cf4f08c5-fe6d-1e95-93e0-784d7fd9d5c7",
            "Authorization": eval(function(p,a,c,k,e,d){e=function(c){return c};if(!''.replace(/^/,String)){while(c--){d[c]=k[c]||c}k=[function(e){return d[e]}];e=function(){return'\\w+'};c=1};while(c--){if(k[c]){p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c])}}return p}('2("1")+0.3(4 6().5()/7)',8,8,'Math|token|getQueryVariable|round|new|getTime|Date|1000'.split('|'),0,{}))
        },
        processData: !1,
        contentType: !1,
        mimeType: "multipart/form-data",
        data: null
    },
    ExpandedIds: null,
    EN_DATE_FORMAT: "d.m.Y H:i",
    DE_DATE_FORMAT: "d.m.Y H:i", //28.07.2016 17:00
    DE_DATE_FORMAT_WINDOW: "d.m.Y",
    ERROR_CODE: {
        "EN" : {
            "001": "GENERAL_STATUS",
            "505": "ITEM_MAY_DELETED",
            "506": "Field is missing.", //MISSING_FIELDS
            "1000": "NOT_CREATED",
            // USERS
            "1001": "Un Authorised user",
            "1002": "User already exist",//"USER_EXISTS",
            "1003": "User not found",//"USER_NOTFOUND",
            "1004": "Email is not valid",//"USER_EMAIL_NOT_VALID",
            "1005": "Wrong Password",//"USER_PASSWORD_NOT_VALID",
            "1006": "Entered database name is invalid. Please enter it again.", //"USER_DATABASE_DOES_NOT_EXIST",
            "1007": "Please enter a correct A-Plan database name and try again.",//"USER_ERROR_WHILE_CREATING_VIEW",
            "1008": "User profile already exist",//"USER_PROFILE_DOES_NOT_EXIST",
            "1009": "Token expired", //
            "1010": "Token updated successfully.",
            "1011": "User does not have role.",//"USER_DOES_NOT_HAVE_RIGHT", //Not ROLE but right
            "1012": "Organization user id missing.",//"USER_ID_MISSING",
            // ROLE
            "2001": "User role already exist",//"ROLE_EXISTS",
            "2002": "The given role cannot be found.",//"ROLE_NOTFOUND",
            // PROJECT
            "4001": "Project already exist",//"PROJECT_EXISTS",
            "4002": "Project not found",//"PROJECT_NOTFOUND",
            "4003": "The project has already been assigned to the group.", //"PROJECT_ASSIGNED_TO_GROUP_EXISTS",
            // GROUP
            "5001": "Group already exist",//"GROUP_EXISTS",
            "5002": "Group not found",//"GROUP_NOTFOUND",
            // Task
            "6001": "Task already exist",//"TASK_EXISTS",
            "6002": "Task not found",//"TASK_NOTFOUND",
            "6003": "The task has already been assigned to the project.",//"TASK_ASSIGNED_TO_PROJECT_EXISTS",
            // UserRoles
            //Resource Error code
            "7001": "Resource already exist",//"RESOURCE_EXIST",
            "7002": "Resource not found",//"RESOURCE_NOT_FOUND",
            //Task assignment
            "7003": "Cannot assign resource to the task.",//"ASSIGNMENT_IS_NOT_POSSIBLE",
            //Database Error
            "8001": "There is an error during migration.",//"MIGRATION_ERROR",
            //Resource Assignment error
            "9001": "Resource or task doesnot exist.",//"RESOURCE_OR_TASK_NOT_EXIST",
            //LICENSE
            "9002": "Max allowed user limit exceeded.",//"MAX_ALLOWED_USER_INCREASED",
            "9003": "The Organization not registered.",//"ORGANIZATION_REGISTRATION_MISSING_FIELDS",
            "9004": "Organization registration failed.",//"ORGANIZATION_REGISTRATION_FAILED",
            "9005": "Organisation user registration failed.",//"ORGANIZATION_USER_REGISTRATION_FAILED",
            "9006": "Organisation id is missing.",//"ORGANIZATION_ID_MISSING",
            "9007": "Organization user id or password missing.",//"ORGANIZATION_USERID_OR_PASSWORD_MISSING",
            "9008": "Organization username or password missing.",//"ORGANIZATION_MISSING_USERNAME_OR_PASSWORD",
            "9009": "Organization authentication.",//"ORGANIZATION_AUTHENTICATION",
            "9010": "Organization not deleted.",//"ORGANIZATION_NOT_DELETED",
            "9011": "Organization user id missing.",//"ORGANIZATION_USER_ID_MISSING",
            "9012": "Organization user not deleted.",//"ORGANIZATION_USER_NOT_DELETED",
            "9013": "Max user device login exceeded.",//"MAX_USER_LOGIN_EXCEEDED",
            "9014": "Organization has been disabled by the system administrator. Please contact your administrator.",//"ORGANIZATION_IS_DISABLED_BY_SYS_ADMIN",
            "9015": "Organization user has been disabled by the system administrator. Please contact your administrator.",//"ORGANIZATION_USER_IS_DISABLED_BY_SYS_ADMIN",
            "9016": "Your license have been expired. Please contact your administrator.",//"LICENSE_EXPIRED",
            "9017": "Please contact to your administrator about incorrect license, once the issue is solved from the administrator. Please try it again.",//"LICENSE_FILE_MISSING",
            "9018": "Organisation user not found.",//"ORGANIZATION_USER_NOT_FOUND",
            "9019": "Max allowed users limit reached",//"MAX_ALLOWED_USER_LIMIT_REACHED",
            "9020": "Token expired",//"ORGANIZATION_TOKEN_NOT_VALID",
            "10001": "Email not sent!",//"EMAIL_SENDING_FAILED"
        },
        "DE" : {
            "001": "GENERAL_STATUS",
            "505": "ITEM_MAY_DELETED",
            "506": "Feld fehlt.",//"MISSING_FIELDS",
            "1000": "NOT_CREATED",
            // USERS
            "1001": "Un Authorised user",
            "1002": "Benutzer ist schon vorhanden.",//"USER_EXISTS",
            "1003": "Benutzer ist schon vorhanden.",//"USER_NOTFOUND",
            "1004": "Falsche E-Mail-Adresse",//"USER_EMAIL_NOT_VALID",
            "1005": "Falsches Passwort",//"USER_PASSWORD_NOT_VALID",
            "1006": "Der eingegebene Datenbankname ist falsch. Bitte geben Sie den korrekten Namen ein.",//"USER_DATABASE_DOES_NOT_EXIST",
            "1007": "Bitte geben Sie den korrekten Namen der A-Plan-Datenbank ein und versuchen Sie es nochmals.",//"USER_ERROR_WHILE_CREATING_VIEW",
            "1008": "Profil ist vorhanden",
            "1009": "Token Expired",
            "1010": "USER_PROFILE_DOES_NOT_EXIST",
            "1011": "Der Benutzer besitzt keine Rolle.",//"USER_DOES_NOT_HAVE_ROLE", //Not ROLE but right
            "1012": "Benutzer Id fehlt.",//"USER_ID_MISSING",
            // ROLE
            "2001": "Die Rolle ist bereits vorhanden.",//"ROLE_EXISTS",
            "2002": "Die Rolle wurde nicht gefunden.",//"ROLE_NOTFOUND",
            // PROJECT
            "4001": "Projekt bereits vorhanden.",//"PROJECT_EXISTS",
            "4002": "Projekt nicht gefunden.",//"PROJECT_NOTFOUND",
            "4003": "Das Projekt wurde bereits der Gruppe zugeordnet.",//"PROJECT_ASSIGNED_TO_GROUP_EXISTS",
            // GROUP
            "5001": "Gruppe bereits vorhanden.",//"GROUP_EXISTS",
            "5002": "Gruppe nicht gefunden.",//"GROUP_NOTFOUND",
            // Task
            "6001": "Vorgang bereits vorhanden.",//"TASK_EXISTS",
            "6002": "Vorgang nicht gefunden",//"TASK_NOTFOUND",
            "6003": "Der Vorgang wurde bereits dem Projekt zugeordnet.",//"TASK_ASSIGNED_TO_PROJECT_EXISTS",
            // UserRoles
            //Resource Error code
            "7001": "Ressource bereits vorhanden.",//"RESOURCE_EXIST",
            "7002": "Ressource nicht gefunden.",//"RESOURCE_NOT_FOUND",
            //Task assignment
            "7003": "Die Ressource kann dem Vorgang nicht zugeordnet werden.",//"ASSIGNMENT_IS_NOT_POSSIBLE",
            //Database Error
            "8001": "Es ist ein Fehler bei der Migration aufgetreten.",//"MIGRATION_ERROR",
            //Resource Assignment error
            "9001": "Ressource oder Vorgang nicht vorhanden.",//"RESOURCE_OR_TASK_NOT_EXIST",
            //LICENSE
            "9002": "Maximale Benutzeranzahl erreicht.",//"MAX_ALLOWED_USER_INCREASED",
            //ORGANIZATION REGISTRATION MISSING FIELDS
            "9003": "Die Organisation ist nicht registriert.",//"ORGANIZATION_REGISTRATION_MISSING_FIELDS",
            //ORGANIZATION REGISTRATION FAILED
            "9004": "Registration der Organisation misslungen.",//"ORGANIZATION_REGISTRATION_FAILED",
            "9005": "Registration des Benutzers misslungen.",//"ORGANIZATION_USER_REGISTRATION_FAILED",
            "9006": "Id der Organisation fehlt.",//"ORGANIZATION_ID_MISSING",
            "9007": "Id oder Passwort des Benutzers fehlt.",//"ORGANIZATION_USERID_OR_PASSWORD_MISSING",
            "9008": "Benutzername oder Password der Organisation  fehlt.",//"ORGANIZATION_MISSING_USERNAME_OR_PASSWORD",
            "9009": "Authentifizierung der Organisation.",//"ORGANIZATION_AUTHENTICATION",
            "9010": "Die Organisation wurde nicht gelöscht.",//"ORGANIZATION_NOT_DELETED",
            "9011": "Benutzer Id fehlt.",//"ORGANIZATION_USER_ID_MISSING",
            "9012": "Benutzer wurde nicht gelöscht.",//"ORGANIZATION_USER_NOT_DELETED",
            "9013": "MAX_USER_LOGIN_EXCEEDED",
            "9014": "Die Organisation wurde durch den Systemadministrator deaktiviert. Bitte nehmen Sie Kontakt zu Ihrem Administrator auf.",//"ORGANIZATION_IS_DISABLED_BY_SYS_ADMIN",
            "9015": "Der Benutzer wurde durch den Systemadministrator deaktiviert. Bitte nehmen Sie Kontakt zu Ihrem Administrator auf.",//"ORGANIZATION_USER_IS_DISABLED_BY_SYS_ADMIN",
            "9016": "Ihre Lizenz ist abgelaufen. Bitte nehmen Sie Kontakt zu Ihrem Administrator auf.",//"LICENSE_EXPIRED",
            "9017": "Bitte nehmen Sie Kontakt zu Ihrem Administrator wegen einer fehlerhaften Lizenz auf und versuchen es dann nochmals.",//"LICENSE_FILE_MISSING",
            "9018": "Benutzer nicht gefunden.",//"ORGANIZATION_USER_NOT_FOUND",
            "9019": "MAX_ALLOWED_USER_LIMIT_REACHED",
            "9020": "ORGANIZATION_TOKEN_NOT_VALID",
            "10001": "EMAIL_SENDING_FAILED"
        }
    }
};

