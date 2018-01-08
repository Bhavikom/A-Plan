package de.smacsoftwares.aplanapp.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SSoft-13 on 17-06-2016.
 */
// this is for handle task listview in dashboard fragment
public class TaskListModelClass implements Serializable
{
    TaskListModelClass childDataEntity;

    public TaskListModelClass getChildDataEntity()
    {
        return childDataEntity;
    }

    public void setChildDataEntity(TaskListModelClass childDataEntity)
    {
        this.childDataEntity = childDataEntity;
    }

    String groupObject = "";

    public String getGroupObject()
    {
        return groupObject;
    }

    public void setGroupObject(String groupObject)
    {
        this.groupObject = groupObject;
    }

    public String getProjectObject()
    {
        return projectObject;
    }

    public void setProjectObject(String projectObject)
    {
        this.projectObject = projectObject;
    }

    public String getTaskObject()
    {
        return taskObject;
    }

    public void setTaskObject(String taskObject)
    {
        this.taskObject = taskObject;
    }

    String projectObject = "";
    String taskObject = "";
    String projectName = "";
    String strGroupId="";

    public ArrayList<String> getStatusArray()
    {
        return statusArray;
    }

    public void setStatusArray(ArrayList<String> statusArray)
    {
        this.statusArray = statusArray;
    }

    ArrayList<String> statusArray = new ArrayList<>();

    public String getStrGroupId()
    {
        return strGroupId;
    }

    public void setStrGroupId(String strGroupId)
    {
        this.strGroupId = strGroupId;
    }

    String strProjectId="";
    String strTaskid="";

    public String getStrProjectId()
    {
        return strProjectId;
    }

    public void setStrProjectId(String strProjectId)
    {
        this.strProjectId = strProjectId;
    }

    public String getStrTaskid()
    {
        return strTaskid;
    }

    public void setStrTaskid(String strTaskid)
    {
        this.strTaskid = strTaskid;
    }

    public String getStrparentId()
    {
        return strparentId;
    }

    public void setStrparentId(String strparentId)
    {
        this.strparentId = strparentId;
    }

    String strparentId="";

    public String getProjectName()
    {
        return projectName;
    }

    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

    public String getProjectId()
    {
        return projectId;
    }

    public void setProjectId(String projectId)
    {
        this.projectId = projectId;
    }

    String trafficLight = "";
    String statusImage = "";

    public String getStatusImage()
    {
        return statusImage;
    }

    public void setStatusImage(String statusImage)
    {
        this.statusImage = statusImage;
    }

    public String getDateImage()
    {
        return dateImage;
    }

    public void setDateImage(String dateImage)
    {
        this.dateImage = dateImage;
    }

    String dateImage = "";

    public String getTrafficLight()
    {
        return trafficLight;
    }

    public void setTrafficLight(String trafficLight)
    {
        this.trafficLight = trafficLight;
    }

    String projectId = "";
    String Id = "";
    String GroupId = "";
    String Name = "";
    String Notes = "";
    String Priority = "";
    String Stage = "";
    String Status = "";
    String StatusObj = "";
    String CreatorId = "";
    String TrafficLightObj = "";
    String Object = "";
    String ParentId = "";
    String StartDate = "";
    String EndDate = "";
    String LateFinish = "";
    String LateStart = "";
    String EarlyStart = "";
    String ActualStart = "";
    String Metas = "";
    String ActualFinish = "";
    String Rem_time = "";
    String Reshow_date = "";
    String Completed = "";
    String LatestEndDate = "";
    String IsCriticalTask = "";
    String isClarificationRequired = "";
    String IsNotice = "";
    String IsReshow = "";
    String isCompleted = "";
    String isPassive = "";
    String isCancelled = "";

    public ArrayList<TaskListModelClass> children = new ArrayList<>();


    public ArrayList<TaskListModelClass> getChildren()
    {
        return children;
    }

    public void setChildren(ArrayList<TaskListModelClass> children)
    {
        this.children = children;
    }

    public String getId()
    {
        return Id;
    }

    public void setId(String id)
    {
        Id = id;
    }

    public String getGroupId()
    {
        return GroupId;
    }

    public void setGroupId(String groupId)
    {
        GroupId = groupId;
    }

    public String getName()
    {
        return Name;
    }

    public void setName(String name)
    {
        Name = name;
    }

    public String getNotes()
    {
        return Notes;
    }

    public void setNotes(String notes)
    {
        Notes = notes;
    }

    public String getPriority()
    {
        return Priority;
    }

    public void setPriority(String priority)
    {
        Priority = priority;
    }

    public String getStage()
    {
        return Stage;
    }

    public void setStage(String stage)
    {
        Stage = stage;
    }

    public String getStatus()
    {
        return Status;
    }

    public void setStatus(String status)
    {
        Status = status;
    }

    public String getStatusObj()
    {
        return StatusObj;
    }

    public void setStatusObj(String statusObj)
    {
        StatusObj = statusObj;
    }

    public String getCreatorId()
    {
        return CreatorId;
    }

    public void setCreatorId(String creatorId)
    {
        CreatorId = creatorId;
    }

    public String getTrafficLightObj()
    {
        return TrafficLightObj;
    }

    public void setTrafficLightObj(String trafficLightObj)
    {
        TrafficLightObj = trafficLightObj;
    }

    public String getObject()
    {
        return Object;
    }

    public void setObject(String object)
    {
        Object = object;
    }

    public String getParentId()
    {
        return ParentId;
    }

    public void setParentId(String parentId)
    {
        ParentId = parentId;
    }

    public String getStartDate()
    {
        return StartDate;
    }

    public void setStartDate(String startDate)
    {
        StartDate = startDate;
    }

    public String getEndDate()
    {
        return EndDate;
    }

    public void setEndDate(String endDate)
    {
        EndDate = endDate;
    }

    public String getLateFinish()
    {
        return LateFinish;
    }

    public void setLateFinish(String lateFinish)
    {
        LateFinish = lateFinish;
    }

    public String getLateStart()
    {
        return LateStart;
    }

    public void setLateStart(String lateStart)
    {
        LateStart = lateStart;
    }

    public String getEarlyStart()
    {
        return EarlyStart;
    }

    public void setEarlyStart(String earlyStart)
    {
        EarlyStart = earlyStart;
    }

    public String getActualStart()
    {
        return ActualStart;
    }

    public void setActualStart(String actualStart)
    {
        ActualStart = actualStart;
    }

    public String getMetas()
    {
        return Metas;
    }

    public void setMetas(String metas)
    {
        Metas = metas;
    }

    public String getActualFinish()
    {
        return ActualFinish;
    }

    public void setActualFinish(String actualFinish)
    {
        ActualFinish = actualFinish;
    }

    public String getRem_time()
    {
        return Rem_time;
    }

    public void setRem_time(String rem_time)
    {
        Rem_time = rem_time;
    }

    public String getReshow_date()
    {
        return Reshow_date;
    }

    public void setReshow_date(String reshow_date)
    {
        Reshow_date = reshow_date;
    }

    public String getCompleted()
    {
        return Completed;
    }

    public void setCompleted(String completed)
    {
        Completed = completed;
    }

    public String getLatestEndDate()
    {
        return LatestEndDate;
    }

    public void setLatestEndDate(String latestEndDate)
    {
        LatestEndDate = latestEndDate;
    }

    public String getIsCriticalTask()
    {
        return IsCriticalTask;
    }

    public void setIsCriticalTask(String isCriticalTask)
    {
        IsCriticalTask = isCriticalTask;
    }

    public String getIsClarificationRequired()
    {
        return isClarificationRequired;
    }

    public void setIsClarificationRequired(String isClarificationRequired)
    {
        this.isClarificationRequired = isClarificationRequired;
    }

    public String getIsNotice()
    {
        return IsNotice;
    }

    public void setIsNotice(String isNotice)
    {
        IsNotice = isNotice;
    }

    public String getIsReshow()
    {
        return IsReshow;
    }

    public void setIsReshow(String isReshow)
    {
        IsReshow = isReshow;
    }

    public String getIsCompleted()
    {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted)
    {
        this.isCompleted = isCompleted;
    }

    public String getIsPassive()
    {
        return isPassive;
    }

    public void setIsPassive(String isPassive)
    {
        this.isPassive = isPassive;
    }

    public String getIsCancelled()
    {
        return isCancelled;
    }

    public void setIsCancelled(String isCancelled)
    {
        this.isCancelled = isCancelled;
    }

    public String getUnitforWork()
    {
        return UnitforWork;
    }

    public void setUnitforWork(String unitforWork)
    {
        UnitforWork = unitforWork;
    }

    public String getDurationUnit()
    {
        return DurationUnit;
    }

    public void setDurationUnit(String durationUnit)
    {
        DurationUnit = durationUnit;
    }

    public String getDuration_Act__div_Pld()
    {
        return Duration_Act__div_Pld;
    }

    public void setDuration_Act__div_Pld(String duration_Act__div_Pld)
    {
        Duration_Act__div_Pld = duration_Act__div_Pld;
    }

    public String getActualDuration()
    {
        return ActualDuration;
    }

    public void setActualDuration(String actualDuration)
    {
        ActualDuration = actualDuration;
    }

    public String getForcastedDuration()
    {
        return ForcastedDuration;
    }

    public void setForcastedDuration(String forcastedDuration)
    {
        ForcastedDuration = forcastedDuration;
    }

    public String getDurationUnitDefault()
    {
        return DurationUnitDefault;
    }

    public void setDurationUnitDefault(String durationUnitDefault)
    {
        DurationUnitDefault = durationUnitDefault;
    }

    public String getReq_Ress()
    {
        return Req_Ress;
    }

    public void setReq_Ress(String req_Ress)
    {
        Req_Ress = req_Ress;
    }

    public String getReq_Effort()
    {
        return Req_Effort;
    }

    public void setReq_Effort(String req_Effort)
    {
        Req_Effort = req_Effort;
    }

    public String getEffort()
    {
        return Effort;
    }

    public void setEffort(String effort)
    {
        Effort = effort;
    }

    public String getOvt_Effort()
    {
        return Ovt_Effort;
    }

    public void setOvt_Effort(String ovt_Effort)
    {
        Ovt_Effort = ovt_Effort;
    }

    public String getActual_Effort()
    {
        return Actual_Effort;
    }

    public void setActual_Effort(String actual_Effort)
    {
        Actual_Effort = actual_Effort;
    }

    public String getWork_progn()
    {
        return Work_progn;
    }

    public void setWork_progn(String work_progn)
    {
        Work_progn = work_progn;
    }

    public String getActual_ovt_Effort()
    {
        return Actual_ovt_Effort;
    }

    public void setActual_ovt_Effort(String actual_ovt_Effort)
    {
        Actual_ovt_Effort = actual_ovt_Effort;
    }

    public String getPercentDone()
    {
        return PercentDone;
    }

    public void setPercentDone(String percentDone)
    {
        PercentDone = percentDone;
    }

    public String getTime_Unit_of_Cost()
    {
        return Time_Unit_of_Cost;
    }

    public void setTime_Unit_of_Cost(String time_Unit_of_Cost)
    {
        Time_Unit_of_Cost = time_Unit_of_Cost;
    }

    public String getCost_Calc_Meth()
    {
        return Cost_Calc_Meth;
    }

    public void setCost_Calc_Meth(String cost_Calc_Meth)
    {
        Cost_Calc_Meth = cost_Calc_Meth;
    }

    public String getOvt_Cost()
    {
        return Ovt_Cost;
    }

    public void setOvt_Cost(String ovt_Cost)
    {
        Ovt_Cost = ovt_Cost;
    }

    public String getAct_Ovt_Cost()
    {
        return Act_Ovt_Cost;
    }

    public void setAct_Ovt_Cost(String act_Ovt_Cost)
    {
        Act_Ovt_Cost = act_Ovt_Cost;
    }

    public String getStandard_Rate()
    {
        return Standard_Rate;
    }

    public void setStandard_Rate(String standard_Rate)
    {
        Standard_Rate = standard_Rate;
    }

    public String getAct_Standard_Rate()
    {
        return Act_Standard_Rate;
    }

    public void setAct_Standard_Rate(String act_Standard_Rate)
    {
        Act_Standard_Rate = act_Standard_Rate;
    }

    public String getFixed_Cost()
    {
        return Fixed_Cost;
    }

    public void setFixed_Cost(String fixed_Cost)
    {
        Fixed_Cost = fixed_Cost;
    }

    public String getAct_Fixed_Cost()
    {
        return Act_Fixed_Cost;
    }

    public void setAct_Fixed_Cost(String act_Fixed_Cost)
    {
        Act_Fixed_Cost = act_Fixed_Cost;
    }

    public String getTotal_Cost()
    {
        return Total_Cost;
    }

    public void setTotal_Cost(String total_Cost)
    {
        Total_Cost = total_Cost;
    }

    public String getAct_Total_Cost()
    {
        return Act_Total_Cost;
    }

    public void setAct_Total_Cost(String act_Total_Cost)
    {
        Act_Total_Cost = act_Total_Cost;
    }

    public String getTotal_Cost_Progn()
    {
        return Total_Cost_Progn;
    }

    public void setTotal_Cost_Progn(String total_Cost_Progn)
    {
        Total_Cost_Progn = total_Cost_Progn;
    }

    public Boolean getLeaf()
    {
        return leaf;
    }

    public void setLeaf(Boolean leaf)
    {
        this.leaf = leaf;
    }

    String UnitforWork = "";
    String DurationUnit = "";
    String Duration_Act__div_Pld = "";
    String ActualDuration = "";
    String ForcastedDuration = "";
    String DurationUnitDefault = "";
    String Req_Ress = "";
    String Req_Effort = "";
    String Effort = "";
    String Ovt_Effort = "";
    String Actual_Effort = "";
    String Work_progn = "";
    String Actual_ovt_Effort = "";
    String PercentDone = "";
    String Time_Unit_of_Cost = "";
    String Cost_Calc_Meth = "";
    String Ovt_Cost = "";
    String Act_Ovt_Cost = "";
    String Standard_Rate = "";
    String Act_Standard_Rate = "";
    String Fixed_Cost = "";
    String Act_Fixed_Cost = "";
    String Total_Cost = "";
    String Act_Total_Cost = "";
    String Total_Cost_Progn = "";

    Boolean leaf;
    String strPorjectName;
    String strTaskName;
    int imgDate;
    int imgTraffic;

    public TaskListModelClass()
    {

    }

    public TaskListModelClass(String projectName, String taskname)
    {
        this.strPorjectName = projectName;
        this.strTaskName = taskname;
        // this.flag = flag;
    }


    public int getImgDate()
    {
        return imgDate;
    }

    public void setImgDate(int imgDate)
    {
        this.imgDate = imgDate;
    }

    public int getImgTraffic()
    {
        return imgTraffic;
    }

    public void setImgTraffic(int imgTraffic)
    {
        this.imgTraffic = imgTraffic;
    }

    public int getImgStatus()
    {
        return imgStatus;
    }

    public void setImgStatus(int imgStatus)
    {
        this.imgStatus = imgStatus;
    }

    public String getStrFinishedDate()
    {
        return strFinishedDate;
    }

    public void setStrFinishedDate(String strFinishedDate)
    {
        this.strFinishedDate = strFinishedDate;
    }

    int imgStatus;
    String strFinishedDate;
}
